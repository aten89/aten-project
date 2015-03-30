//====================================================================
//功能描述：JS公用函数,只能使用在jQuery的环境下
//====================================================================

(function($){

	/*==========屏蔽按键=============*/
	//为所有的页面加上以下屏蔽功能：
	//	1.F5,CTRL+R 刷新网页
	//	2.BackSpace键使IE后退，但保留所有文本框的编辑功能（不然也会被屏蔽，不能删除文字）
	$(document).bind("keydown",function(e){
		//重写刷新快捷键F5,CTRL+R
		if (event.keyCode == 116 || event.ctrlKey && event.keyCode == 82){
			event.keyCode = 0;
			event.returnValue = false;
			$.getEAPPMainFrame().currentTab.reload();
		}

		//BackSpace,but not text,textarea,password
//		if (event.keyCode == 8 && 
//				((event.srcElement.type != "text" &&    
//				event.srcElement.type != "textarea" && 
//				event.srcElement.type != "password") ||    
//				event.srcElement.readOnly == true)) {
//			event.keyCode = 0;    
//			event.returnValue = false;    
//		}

		//BackSpace键使IE后退
		if (event.keyCode == 8) {
			var ev = e || window.event;//获取event对象     
		    var obj = ev.target || ev.srcElement;//获取事件源       
		    var t = obj.type || obj.getAttribute('type');//获取事件源类型       
		    var vReadOnly = (obj.readOnly == undefined) ? false : obj.readOnly;  
		    var vDisabled = (obj.disabled == undefined) ? true : obj.disabled;  
		    if (t != "password" && t != "text" && t != "textarea") {//不可输入元素
		    	event.keyCode = 0;
		        event.returnValue = false;
		    } else if (vReadOnly == true || vDisabled == true) {//可输入元素，但是只读或禁用
		    	event.keyCode = 0;
		    	event.returnValue = false;
		    }
		}
	});
	
	/*==========AJAX事件=============*/
	
	//修改描述：由于在网络状况不佳时，AJAX请求的时间较长。
	//		为了避免异步数据返回的延时造成用户的误操作，为每个AJAX请求加载START和END两个事件
	//		在请AJAX请求过程中为页面添加一个覆盖层，不允许任何操作
	//		为了改善用户的体验，为事件的触发增加一秒的延时，如果AJAX请求在一秒之内完成，则不添加覆盖层
	$(document).bind("ajaxSend", ajaxSendEvent);
	$(document).bind("ajaxComplete", ajaxCompleteEvent);
	$(document).bind("ajaxError", ajaxErrorEvent);
	
	var ajaxLoadingShowDiv = null;//存储加载进度条的DIV对象
	var isShowLoading = false;//进度条是否已显示
	var ajaxSendingCount = 0;//请求中的Ajax数量
	
	//ajaxSend事件
	function ajaxSendEvent(){
		ajaxSendingCount++;//请求数量加1
		
		if (!isShowLoading) {
			//如果一段时间后Ajax请求还未结束，且当前进度条不是显示中，则显示
			showLoadingDiv();
		}
		//为了避免此方法与完成事件同时触发，交叉执行而造成覆盖层无法消除，在生成之后立即进行一次判断
		if (ajaxSendingCount == 0  && isShowLoading) {
			//如果Ajax请求刚好已结束，且当前进度条是显示中，则隐藏
			hideLoadingDiv();
		}
			
		//延时执行，如果AJAX在短时间之内完成，则不显示进度条
//		setTimeout(function() {
//			if (ajaxSendingCount > 0 && !isShowLoading) {
//				//如果一段时间后Ajax请求还未结束，且当前进度条不是显示中，则显示
//				showLoadingDiv();
//			}
//			//为了避免此方法与完成事件同时触发，交叉执行而造成覆盖层无法消除，在生成之后立即进行一次判断
//			if (ajaxSendingCount == 0  && isShowLoading) {
//				//如果Ajax请求刚好已结束，且当前进度条是显示中，则隐藏
//				hideLoadingDiv();
//			}
//		}, 300);		
	}
	
	//ajaxComplete事件
	function ajaxCompleteEvent(){
		if (ajaxSendingCount > 0) {
			ajaxSendingCount--;//请求数量减1
		}
		if (ajaxSendingCount == 0 && isShowLoading){
			hideLoadingDiv();
		}
	}
	
	//ajaxError事件
	function ajaxErrorEvent() {
		$.alert("与服务器通讯过程发生未知错误！");
	}
   
	//显示加载进度条
	function showLoadingDiv() {
		isShowLoading = true;//标记为显示中
		ajaxLoadingShowDiv = $("<div style='position:absolute; z-index:999999;filter:alpha(opacity=95);opacity:0.9;top:0;left:0;width:" + document.body.clientWidth + "px;height:" + document.body.clientHeight + 
				"px;'><div style='display:none;position:absolute; z-index:999999;background:#fff;border:1px solid #eee;padding:20px 0 0 20px;width:390px;height:48px;left:" + (document.body.clientWidth/2-200) + 
				"px;top:120px;'><img src='" + BASE_PATH + "themes/comm/images/loading.gif' align='absmiddle'/>&nbsp;<font style='font-size:20px;color:#f30'>正在加载数据……</font></div></div>").appendTo($(document.body));
		//延时显示提示信息
		setTimeout(function() {
			if (isShowLoading) {
				ajaxLoadingShowDiv.find("div").show();
			}
		}, 300);
	}
	
	//隐藏加载进度条
	function hideLoadingDiv() {
		isShowLoading = false;//标记为未显示
		ajaxLoadingShowDiv.remove();
		ajaxLoadingShowDiv = null;
	}
	

	/*==========客户端权限验证=============*/
    
	//判断是否有权限
    $.testRight = function(rightKey, elementId) {
    	var rightKeys = $("#" + elementId).val();
        if(!rightKeys){
            return false;
        }
        return (rightKeys.indexOf("," + rightKey + ",") != -1);
    };
    
	//查找页面上所有有定义特定属性的元素，如果些属性值无权限则自动移除
	$.removeNoRightEles = function() {
		$("[" + ATTR_NAME_ACTION_KEY + "]").each(function(){
			var actionKey = $(this).attr(ATTR_NAME_ACTION_KEY);
			if(!$.testRight(actionKey, INPUT_ID_USER_ACTION_KEYS)){
	            $(this).remove();
	        };
		});
		
		$("[" + ATTR_NAME_MODULE_KEY + "]").each(function(){
			var moduleKey = $(this).attr(ATTR_NAME_MODULE_KEY);
			if(!$.testRight(moduleKey, INPUT_ID_USER_MODULE_KEYS)){
	            $(this).remove();
	        };
		});
	};

	//如无动作权限返回空字串
    $.wrapActionRight = function(actionKey, content) {
    	if($.testRight(actionKey, INPUT_ID_USER_ACTION_KEYS)) {
    		return content;
    	} else {
    		return "";
    	}
    };
    
    //如无模块权限返回空字串
    $.wrapModuleRight = function(actionKey, content) {
    	if($.testRight(actionKey, INPUT_ID_USER_MODULE_KEYS)) {
    		return content;
    	} else {
    		return "";
    	}
    };
    
    
    //判断是否有权限（保留旧处理机制）
    $.hasRight = function(rightKey, elementId) {
    	return $.testRight(rightKey, elementId ? elementId :"hidModuleRights");
    };
    //没权限时移除页面对象（保留旧处理机制）
    $.handleRights = function(rights, elementId){
        $.each(rights, function(key, value) {
            if(!$.hasRight(value, elementId)){
                $("#" + key).remove();
            };
		});
    };
    
    
    //权限点枚举
    $.SysConstants = {
        ADD : "add",//新增
	    VIEW : "view",//查看
	    MODIFY : "modify",//修改
	    QUERY : "query",//查询
	    DELETE : "delete",//删除
	    ORDER : "order",//排序
	    BIND_ACTION : "bindaction",//绑定角色
	    BIND_ROLE : "bindrole",//绑定角色
	    BIND_USER : "binduser",//绑定用户
	    BIND_SERVICE : "bindservice",//绑定接口服务
	    REDEPLOY : "redeploy",//重部署
	    ENABLE : "enable",//发布
	    DISABLE : "disable",//撤销
	    EXPORT : "export",//导出
	    BIND_GROUP : "bindgroup",//绑定群组
	    BIND_RIGHT : "bindright",//绑定权限
	    BIND_ACTOR : "bindactor",//绑定接口帐号
	    SET_PASSWORD : "setpassword",//设置密码
	    BIND_POST : "bindpost",//绑定职位
	    SET_DEFAULT : "setdefault",//设置默认参数
	    SEND : "send"//发送
    };
   
    
	/*==========公用方法=============*/
		
	//响应回车
	//src对象上回车时，响应tar对象点击
	$.EnterKeyPress = function(src, tar){
		src.keypress(function(e){
	   		if(e.keyCode == 13){
	  			tar.click();
	       		return false;
	   		}
		});
	};
	
	//提示对话框
	$.alert = function(msg, fun) {
		$.alerts.okButton = "确定";
		jAlert(msg, '提示对话框', fun);
	};
	
	//确认对话框
	$.confirm = function(msg, fun) {
		$.alerts.okButton = "确定";
		$.alerts.cancelButton = "取消";
		jConfirm(msg, '确认对话框', fun);
	};
	
	//输入对话框
	$.prompt = function(msg, defaultText, fun) {
		$.alerts.okButton = "确定";
		$.alerts.cancelButton = "取消";
		jPrompt(msg, defaultText, '输入对话框', fun);
	};
	
	//弹出模式对话框
	$.showModalDialog = function(title, url, width, height, onClose, position) {
		var dialog = $("#_time20140404234850").wBox({//随便引用个不存在的对象
			title : title,
			closeCallBack : function(){
				if (typeof onClose === 'function') {
					onClose();
				}
				//IE下有时间关闭窗口后INPUT不能输入
				//解决：焦点定位到第一个INPUT
				$("input[type='text']:first").focus();
			},
		   	requestType : "iframe",
		   	iframeWH : {width : width, height : height},
		   	position : position, 
		   	target : url
		});
		dialog.showBox();
		return dialog
	};
	
	//Javascript对象转JSON
	$.toJSON = function (o){
		if(typeof o == "undefined"){
			return null;
		}
		if(typeof JSON != "undefined"){
			//使用浏览器自带的对象
			return JSON.stringify(o);
		}
		var r = [];
	    if(typeof o =="string") return "\""+o.replace(/([\'\"\\])/g,"\\$1").replace(/(\n)/g,"\\n").replace(/(\r)/g,"\\r").replace(/(\t)/g,"\\t")+"\"";
	    if(typeof o == "object"){
	        if(!o.sort){
	            for(var i in o)
	                r.push("\"" + i + "\":"+$.toJSON(o[i]));
	            if(!!document.all && !/^\n?function\s*toString\(\)\s*\{\n?\s*\[native code\]\n?\s*\}\n?\s*$/.test(o.toString)){
	                r.push("toString:"+o.toString.toString());
	            }
	            r="{"+r.join()+"}"
	        }else{
	            for(var i =0;i<o.length;i++)
	                r.push($.toJSON(o[i]))
	            r="["+r.join()+"]"
	        }
	        return r;
	    }
	    
	    return o.toString();
	};
})(jQuery);

//页面存储权限的INPUT ID
var INPUT_ID_USER_ACTION_KEYS = "r_userActionKeys";	//登录用户在当前模块下有权限的动作列表
var INPUT_ID_USER_MODULE_KEYS = "r_userModuleKeys";//登录用户在当前模块下有权限的子菜单列表

//页面元素要权限判断的属性名称
var ATTR_NAME_ACTION_KEY = "r-action";
var ATTR_NAME_MODULE_KEY = "r-module";

//自动移除无权限的元素
$($.removeNoRightEles);
