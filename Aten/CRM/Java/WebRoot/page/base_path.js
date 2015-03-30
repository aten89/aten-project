
(function($){
	//翻页页码组件
	//pageCount：一页显示数，默认10
	//callBack：查询的回调方法
	$.fn.turnPage = function(pageCount, callQuery) {
		try{pageCount = $.cookie("PAGE_COUNT");}catch(e){};//从COOCIE读
		pageCount = (pageCount != null) ? pageCount : 15;
		var obj = this;
		//翻页内容
		html = "<a href='javascript:void(0);' id='_toFirstPage'>首页</a>&nbsp;"
				+ "<a href='javascript:void(0);' id='_toPreviousPage'>上页</a>&nbsp;"
				+ "<a href='javascript:void(0);' id='_toNextPage'>下页</a>&nbsp;"
				+ "<a href='javascript:void(0);' id='_toLastPage'>末页</a>&nbsp;&nbsp;"
				+ "第<input id='currentPageNo' type='text' class='pageIpt01'/>页&nbsp;共<span id='_totalPageCount'>0</span>页&nbsp;"
				+ "每页<input type='text' id='pageCount' value='" + pageCount +"' class='pageIpt01'/>条&nbsp;共<span id='_totalCount'>0</span>条"
		       	+ "<br/>";
		obj.html(html);
		
		var totalPage;//总页数
	    //跳转到第几页
		this.gotoPage = function(pageNo){
		    if(isNaN(pageNo)){
		        $.alert("页索引只能为数字！");
		        return;
		    } else if(pageNo < 1){
		        pageNo = 1;
		    }
		    if (totalPage && pageNo > totalPage) {
		    	pageNo = totalPage;
		    }
		    $("#currentPageNo",obj).val(pageNo);
		    if (callQuery) {
				callQuery();
			} else {
				queryList();//页面翻页查询的函数名统一为queryList();
			}
		};
		//设置翻页数据
	    this.setPageData = function(data) {
	    	if(!data){
	    		totalPage = null;
				return ;
			}
			var currentPageNo = data.currentPageNo;
			var currentPageSize = data.currentPageSize;
			var totalCount = data.totalCount;
			var totalPageCount = data.totalPageCount;
			$("#pageCount", obj).unbind().change(function() {//一页显示数改变事件
			 	var _pcount = parseInt($(this).val());
			 	if(_pcount < 1 || isNaN(_pcount)) {
			 		_pcount = 15;
			 	}
			 	$(this).val(_pcount);
				obj.gotoPage(1);
				try{$.cookie("PAGE_COUNT",_pcount,{expires:120});}catch(e){};//写入COOCIE
				return false;
			}).keypress(function(e){
		   		if(e.keyCode == 13){
		  			obj.gotoPage(1);
		       		return false;
		   		}
			});
			
			$("#currentPageNo", obj).unbind().change(function() {//页码改变事件
			 	obj.gotoPage($(this).val());
				return false;
			}).keypress(function(e){
		   		if(e.keyCode == 13){
		  			obj.gotoPage($(this).val());
		       		return false;
		   		}
			});
			
			var _ele = $("#_toFirstPage", obj).unbind();
			if(currentPageNo > 1) {//首页事件
				_ele.click(function() {
					obj.gotoPage(1);
				});
			}
			_ele = $("#_toPreviousPage", obj).unbind();
			if(currentPageNo > 1){//上页事件
				_ele.click(function() {
					obj.gotoPage(currentPageNo - 1);
				});
			}
			_ele = $("#_toNextPage", obj).unbind();
			if(currentPageNo < totalPageCount){//下页事件
				_ele.click(function() {
					obj.gotoPage(currentPageNo + 1);
				});
			}
			_ele = $("#_toLastPage", obj).unbind();
			if(currentPageNo < totalPageCount) {//未页事件
				_ele.click(function() {
					obj.gotoPage(totalPageCount);
				});
			}
			
			$("#_totalCount", obj).text(totalCount);
			$("#_totalPageCount", obj).text(totalPageCount);
			$("#currentPageNo",obj).val(currentPageNo);
			totalPage = totalPageCount;
	    };
		//取得当前页码
	    this.getCurrentPageNo = function() {
	    	return $("#currentPageNo",obj).val();
	    };
	    //取得总页数
	    this.getPageCount = function() {
	    	return $("#pageCount",obj).val();
	    };
		return this;
	};
	
	
	//检查AJAX返回的错误信息，
	//参数json.msg是后台返固定的格式;
	//参数msg不传时取后台的错误信息;
	//参数cmd超时时回调方法或执行的命令
	$.checkErrorMsg = function(json, msg, cmd) {
		if (json && json.msg) {
			var code = json.msg.code;
			if (code == "1") {
				return true;
			} else if (code == "-1") {
	        	$.alert("会话已超时,请重新登入");
	        	if (typeof cmd == "function") {
	        		cmd();
	        	} else if (cmd == "close") {
	        		window.close();
	        	} else {
	        		top.location.reload();
	        	}
	        	return false;
        	} else {
                $.alert((msg) ? msg : json.msg.text);
                return false;
        	}
		} else {
			$.alert("返回的JSON格式不正确");
			return false;
		}
	};
	
	 //日期比较
    $.compareDate = function(beginDate, endDate) {
    	if (!beginDate || !endDate) {
    		return 0;
    	}
    	return Number(beginDate.replace(/(-|:| )/igm, "")) > Number(endDate.replace(/(-|:| )/igm, ""));
    };
    
    //input验证，参数input：input的Id或JQuery对像；label：input的标签名；required：是否是必需的（不能为空）；illegalChar：不能包含的字符；maxLength：长度限制（0为不限制）
    $.validInput = function(input, label, required, illegalChar, maxLength){
    	if(!input){
    		$.alert("验证对象为空");
			return true;
		}
		var inputObj = (typeof(input) == "string") ? $("#" + input) : input;
		var validValue = $.trim(inputObj.val());
		validValue = validValue.replace(/[　]/g, "");//将全角空格替换成空串
		if(!validValue){
			//非空验证
			if (required) {
				$.alert(label + "不能为空");
				inputObj.focus();
				return true;
			} else {
				return false;
			}
		}
		if (illegalChar) {
			//非法字符验证
			var re = new RegExp(".*?([" + illegalChar + "]?).*?","igm");
			var matches = validValue.match(re);
			var matchString = "";
			for(var i=0; i < matches.length; i++){
				var item = matches[i];
				matchString += item;
			}
			if(matchString){
				$.alert(label + "不能包含字符：" + matchString);
				inputObj.focus();
				return true;
			}
		}
		if (maxLength) {
			//长度限制
			var re = /[^\x00-\xff]/g;
			var tem = validValue.replace(re, "**");//将中文汉字替换成两个字符
			re = /[·]/g;
			tem = tem.replace(re, "**");//特殊字符替换成两个字符
			if (tem.length > maxLength) {
				$.alert(label + "长度不能超过" + maxLength + "（一个汉字长度为2）");
				inputObj.focus();
				return true;
			}
		}
		return false;
    };
    
    $.validNumber = function(input, label, required, maxValue, comma){
    	if(!input){
    		$.alert("验证对象为空");
			return true;
		}
		var inputObj = (typeof(input) == "string") ? $("#" + input) : input;
		var validValue = $.trim(inputObj.val());
		validValue = validValue.replace(/[　]/g, "");//将全角空格替换成空串
		if(!validValue){
			//非空验证
			if (required) {
				$.alert(label + "不能为空");
				inputObj.focus();
				return true;
			} else {
				return false;
			}
		}
		if (comma) {
			validValue = validValue.replace(/,/g, '');//将逗号替换成空串	
		}
//		var re = new RegExp("^([-]){0,1}([0-9]){1,}([.]){0,1}([0-9]){0,}$","igm");
//		if(!re.test(validValue)){
		if (isNaN(validValue)) {
       		$.alert(label+"不是有效的数字类型");
       		inputObj.focus();
       		return true;
       	}
       	if (maxValue) {
			//最大值
			
			if (Number(validValue) > maxValue) {
				$.alert(label + "不能大于" + maxValue);
				inputObj.focus();
				return true;
			}
		}
       	return false;
    }
    
    $.validEmail=function (input, label, required){
    	if(!input){
    		$.alert("验证对象为空");
			return true;
		}
		var inputObj = (typeof(input) == "string") ? $("#" + input) : input;
		var validValue = $.trim(inputObj.val());
		validValue = validValue.replace(/[　]/g, "");//将全角空格替换成空串
		if(!validValue){
			//非空验证
			if (required) {
				$.alert(label + "不能为空");
				inputObj.focus();
				return true;
			} else {
				return false;
			}
		}
        var reg = new RegExp("^[0-9a-zA-Z]+@[0-9a-zA-Z]+[\.]{1}[0-9a-zA-Z]+[\.]?[0-9a-zA-Z]+$");
        if( !reg.test(validValue)){
        	$.alert(label+"不是有效的Email格式");
        	inputObj.focus();
        	return true;
		}
      	return false;
    }
    
    $.validTel=function (input, label, required){
    	if(!input){
    		$.alert("验证对象为空");
			return true;
		}
		var inputObj = (typeof(input) == "string") ? $("#" + input) : input;
		var validValue = $.trim(inputObj.val());
		validValue = validValue.replace(/[　]/g, "");//将全角空格替换成空串
		if(!validValue){
			//非空验证
			if (required) {
				$.alert(label + "不能为空");
				inputObj.focus();
				return true;
			} else {
				return false;
			}
		}
       	var reg = /^\d{3,4}-\d{7,9}(-\d{1,4})?$/;
       	if(!reg.test(validValue)){
       		$.alert("您输入的" + label + "格式有误，请按照\"xxx-xxxxxxxx-xxxx\"的格式重新输入");
       		inputObj.focus();
       		return true;
       	}
       	return false;
    }
    
    //验证汉字
    $.validChinese=function (input, label, required){
    	if(!input){
    		$.alert("验证对象为空");
			return true;
		}
		var inputObj = (typeof(input) == "string") ? $("#" + input) : input;
		var validValue = $.trim(inputObj.val());
		validValue = validValue.replace(/[　]/g, "");//将全角空格替换成空串
		if(!validValue){
			//非空验证
			if (required) {
				$.alert(label + "不能为空");
				inputObj.focus();
				return true;
			} else {
				return false;
			}
		}
       	var reg = /([\u4E00-\u9FA5]|[\uFE30-\uFFA0])/g;//汉字
       	//var reg = /[^\x00-\xff]/g;//汉字
       	if(reg.test(validValue)){
       		$.alert(label+"不能为汉字");
       		inputObj.focus();
       		return true;
       	}
       	return false;
    }
    
    //验证邮政编码：按我国邮政编码的编码规则：采用六位阿拉伯数字组成
    $.validZipCode=function (input, label, required){
    	if(!input){
    		$.alert("验证对象为空");
			return true;
		}
		var inputObj = (typeof(input) == "string") ? $("#" + input) : input;
		var validValue = $.trim(inputObj.val());
		validValue = validValue.replace(/[　]/g, "");//将全角空格替换成空串
		if(!validValue){
			//非空验证
			if (required) {
				$.alert(label + "不能为空");
				inputObj.focus();
				return true;
			} else {
				return false;
			}
		}
       	var reg = /^[0-9]{6}$/;
       	if(!reg.test(validValue)){
       		$.alert(label+"不是有效的格式");
       		inputObj.focus();
       		return true;
       	}
       	return false;
    }
	
     //Javascript对象转JSON
	$.toJSON = function (o){
		if(typeof o=="undefined"){
			return null;
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
	
	//生成表头点击排序
	//callBack：查询的回调方法
	$.fn.columnSort = function(callBack){
		if ($("#sortCol").length > 0){
			$.alert("页面ID“sortCol”重复");
			return; 
		} 
		var obj = $(this);
	    obj.append("<input type=\"hidden\" id=\"sortCol\" name=\"sortCol\" /><input type=\"hidden\" id=\"ascend\" name=\"ascend\" />");
	    $("th", obj).each(function() {//查找所有TH
	    	var sortCol = $(this).attr("sort");
	    	if (sortCol) {//找到有sort属性的进行处理
	    		$(this).html(("<div class=\"sortDot\">" + $(this).html() + "</div>")).attr("title","点击排序");//添加可排序的样式
	    		$(this).click(function(){//绑定排序事件
	    			var o = $(this).find(">div");
	    			var ascend;
	    			var sortType;
	    		 	if(o.attr("class") == "sortUp") {
		    			sortType = "sortDown";
						ascend = false;
					} else {
						sortType = "sortUp";
						ascend = true;
					}
					//将其他排序列的class设为sortDot
					$(".sortDown,.sortUp", obj).removeClass().addClass("sortDot");
					//设置当前点击列的class
	    			o.attr("class",sortType);
					$("#sortCol").val(sortCol);
					$("#ascend").val(ascend);
					
					if (callBack) {
						callBack();
					} else {
						queryList();
					}
	    		});
	    	}
	    });
	    //取得排序字段
	    this.getSortColumn = function() {
	    	var tem = $("#sortCol",obj).val();
	    	return tem ? tem : "";
	    };
	    //取得是否升序
	    this.getAscend = function() {
	    	var tem = $("#ascend",obj).val();
	    	return tem ? tem : "";
	    };
	    return this;
	};

    //权限点枚举
	$.OaConstants = {
        ENABLE : "enable",//启用
        COPY : "copy",//复制到
        DISABLE : "disable",//禁用
        STARTFLOW : "startflow",//启动流程
//        QUERY : "query",//查询
//        ADD : "add",//新增
        DISPOSE : "dispose",//处理
        CREATE_BUDGET : "createbudget",//生成预算
        UPDATE_BUDGET : "updatebudget",//更新预算
        TURN_BUDGET : "turnbudget",//结转预算
        COMPARE : "compare",//对比
        REPORT_FORMS : "reportforms",//报表
        SHIELD:"shield",//屏蔽
        TO_TOP:"totop",//置顶
        DEPT_REPORTFORMS:"deptforms",//生成部门报表
		CHARGE_REPORTFORMS:"chargeforms",//生成部门费用报表
        BUDGET_REPORTFORMS:"budgetforms",//生成预算报表
        ASSIGN:"assign",//授权
        REBUILD:"rebuild",//复建
        UPDATE:"update",//更新
        CONFIG:"config",
        CHECK:"check",//验证
        TOCLEAR:"toclear",//清零
        CANCEL:"cancel",//撤销
        ONE_LEVEL_SPLIT_STRING : "~!@", //一级分隔符
        TWO_LEVEL_SPLIT_STRING : "!@#", //二级分隔符
        THREE_LEVEL_SPLIT_STRING : "@#;",//三级分隔符
        MANAGERASSIGN:"managerassign",//管理授权
        SELECTASSIGN:"selectassign",//管理授权
        REGISTER:"register",//登记
        RECIPIENTS:"recipients",//领用
        ALLOCATION:"allocation",//调拨
        MAINTAIN:"maintain",//维修
        SCRAP:"scrap",//报废
        SCRAPDEAL:"scrapdeal",//报废处理
        LEAVELDEAL:"leavedeal",//离职处理
        LEAVEDEALRECORD:"leavedealrecord",//离职处理登记
        TRIPCHANGE:"tripchange",//出差变更
        KB_LOG_STATISTICS:"kb_logStatistics",//知识点日志统计
        KB_SCORE_STATISTICS:"kb_scoreStatistics",//知识点有效性评分
        KB_QUOTE_TIME:"kb_quoteTime"//知识点被工单引用情况
    };
	
	$.CrmConstants = {
		RETURNVISIT : "returnvisit",//回访    
		APPOINT : "appoint",//预约    
		REQUEST : "request",//咨询 
		MESSAGE : "message",//发送短信
		ALLOT : "allot", //分配
		IMPORT : "import" // 导入
	};
	
	$.openDownloadDialog = function(url,text){
		if (url == null || url=="") return;
		if (text==null || text=="") text = "已生成文件，请点击下载！";
		
		var left = parseInt((document.documentElement.clientWidth - 300) / 2);
		var top = parseInt((document.documentElement.clientHeight - 150) / 2);
		$("<div style='z-index:999;background:#ddd;height:160px;width:300px;position:absolute;left:" + left + "px;top:" + top + "px;'/>").append("<div class='downBg' style='height:158px;'><div id='downloadDiv'><div class='dialogTit'><a herf='#' onclick='$(this).parent().parent().parent().parent().remove();'>[关闭]</a></div><div class='downloadDiv'><div style='white-space:nowrap'>"
			+ text + "</div><div class='txtDown'>"
			+ "<img src='themes/comm/images/downloadn01.gif' style='cursor:hand' onclick='window.open(\"" + url + "\");$(this).parent().parent().parent().parent().parent().remove();'/></div></div></div></div> ").appendTo(document.body);
		
	};
	
})(jQuery);

/**
 * JS 替换字符串
 * s1: 要替换的字符串
 * s2: 要 替换 成什么样
 */
String.prototype.replaceAll = function(s1,s2) { 
    return this.replace(new RegExp(s1,"gm"),s2); 
};

/**
 * 时间格式化
 */
Date.prototype.format = function(format){
	var o = { 
		"M+" : this.getMonth()+1, //month 
		"d+" : this.getDate(), //day 
		"h+" : this.getHours(), //hour 
		"m+" : this.getMinutes(), //minute 
		"s+" : this.getSeconds(), //second 
		"q+" : Math.floor((this.getMonth()+3)/3), //quarter 
		"S" : this.getMilliseconds() //millisecond 
	} 

	if(/(y+)/.test(format)) { 
		format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
	} 

	for(var k in o) { 
		if(new RegExp("("+ k +")").test(format)) { 
			format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length)); 
		} 
	} 
	return format; 
};