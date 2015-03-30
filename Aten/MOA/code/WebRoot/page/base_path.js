
(function($){
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
    //翻页组件
	$.createTurnPage = function(xml) {
		var pageNextHTML = "";
		var content = $("content",xml);
		if(content.attr("current-page") == "1"){
			pageNextHTML += "<a>首页</a>&nbsp;";
		} else {
//			pageNextHTML += "<a href=\"javascript:gotoPage(1);void(0);\">首页</a>&nbsp;";
			pageNextHTML += "<a href=\"#\" onclick=\"gotoPage(1);return false;\">首页</a>&nbsp;";
		}
		if(content.attr("previous-page") == "false"){
		 	pageNextHTML += "<a>上页</a>&nbsp;";
		} else {
//			pageNextHTML += "<a href=\"javascript:gotoPage(" + (parseInt(content.attr("current-page")) - 1) + ");void(0);\">上页</a>&nbsp;";
			pageNextHTML += "<a href=\"#\" onclick=\"gotoPage(" + (parseInt(content.attr("current-page")) - 1) + ");return false;\">上页</a>&nbsp;";
		}
		if(content.attr("next-page") == "false"){
		  	pageNextHTML += "<a>下页</a>&nbsp;";
		} else {
//		 	pageNextHTML += "<a href=\"javascript:gotoPage(" + (parseInt(content.attr("current-page")) + 1) + ");void(0);\">下页</a>&nbsp;";
		 	pageNextHTML += "<a href=\"#\" onclick=\"gotoPage(" + (parseInt(content.attr("current-page")) + 1) + ");return false;\">下页</a>&nbsp;";
		}
		if(content.attr("page-count") == content.attr("current-page")){
			pageNextHTML += "<a>末页</a>&nbsp;";
		} else {
//			pageNextHTML += "<a href=\"javascript:gotoPage(" + content.attr("page-count") + ");void(0);\">末页</a>&nbsp;";
			pageNextHTML += "<a href=\"#\" onclick=\"gotoPage(" + content.attr("page-count") + ");return false;\">末页</a>&nbsp;";
		}
		
		pageNextHTML += "&nbsp;共" + content.attr("total-count") + "条记录&nbsp;&nbsp;" + content.attr("current-page") + "/" + content.attr("page-count") + "&nbsp;页 ";
		pageNextHTML += "<input type=\"text\" name=\"num\" id=\"numPage\" class=\"pageIpt01\"/> "
		         		+ "<input type=\"button\" class=\"btn\" onclick=\"gotoPage($('#numPage').val()," + content.attr("page-count") + ");\"/>"
		       			+ "<br/>";
		       			
		//SET THE CURRENT PAGE NO
		try{
			$("#hidNumPage").val(content.attr("current-page"));
		}catch(e){}
		return pageNextHTML;
	};
	
	$.checkErrorMsg = function(json, msg, cmd) {
		if (json && json.msg) {
			var code = json.msg.code;
			if (code == "1") {
				return true;
			} else if (code == "-1") {
	        	$.alert("会话已超时,请重新登入", function(){
	        		if (typeof cmd == "function") {
		        		cmd();
		        	} else if (cmd == "close") {
		        		window.close();
		        	} else {
		        		top.location.reload();
		        	}
	        	});
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
    
    $.validNumber = function(input, label, required, maxValue){
    	if(!input){
    		$.alert("验证对象为空");
			return true;
		}
		var inputObj = (typeof(input) == "string") ? $("#" + input) : input;
		var validValue = $.trim(inputObj.val());
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
		var re = new RegExp("^([-]){0,1}([0-9]){1,}([.]){0,1}([0-9]){0,}$","igm");
		if(!re.test(validValue)){
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
        var reg = new RegExp("^[0-9a-zA-Z_&-.]+@[0-9a-zA-Z_&-.]+[\.]{1}[0-9a-zA-Z_-]+[\.]?[0-9a-zA-Z]+$");
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
    $.validChinese=function (input, label, required,illegalChar,maxLength){
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
				$.alert(label + "长度不能超过" + maxLength + "个字符（一个汉字长度为2个字符）");
				inputObj.focus();
				return true;
			}
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
    
    
    
    //旧版验证是否包函非法字符，可以使用validInput替代
    $.validChar = function(object, reChar){
    	if(!object){
			return false;
		}
		var validValue;
		if(typeof(object) == "string"){
			validValue = object;
		} else {
			validValue = object.val();
		}
		if(!validValue){
			return false;
		}
		if (!reChar) {
			reChar = "&<>\"'";
		}
		var re = new RegExp(".*?([" + reChar + "]?).*?","igm");
		var matches = validValue.match(re);
		if(!matches || !matches.length){
			return false;
		}
			
		var matchString = "";
		for(var i=0; i < matches.length; i++){
			var item = matches[i];
			matchString += item;
		}
		return matchString;
    };
    //权限点枚举
	$.OaConstants = {
        ENABLE : "enable",//启用
        COPY : "copy",//复制到
        DISABLE : "disable",//禁用
        STARTFLOW : "startflow",//启动流程
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
//        LEAVELDEAL:"leavedeal",//离职处理
//        LEAVEDEALRECORD:"leavedealrecord",//离职处理登记
        TRIPCHANGE:"tripchange",//出差变更
        IMPORT:"import"//
    };

    
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
	
//	$.alert = function(msg) {
//		alert(msg);
//	};
	
//	$.showModalDialog = function (url,params,features){
//    	var d = (Math.random())*10000000000;
//    	if(url.indexOf("?")>0){
//    		url+="&tmpRandom="+d;
//    	}else{
//    		url+="?tmpRandom="+d;
//    	}
//	    var status = window.showModalDialog(url,params,features);
//   		return status;
//	}

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

String.prototype.toUpperDate = function(){
	if (this == null) return "";
	//避免时间
	var date = this.split(" ");
	if (date.length ==0) return "";
	var d = date[0].split("-");
	//转换是为了去除前面的"0"
	var year = parseInt(d[0],10).toString();
	var month = parseInt(d[1],10).toString();
	var day = parseInt(d[2],10).toString();		
	try{
		//定义对应列表
		var numberMap = [];
		numberMap["0"] = "Ｏ";
		numberMap["1"] = "一";
		numberMap["2"] = "二";
		numberMap["3"] = "三";
		numberMap["4"] = "四";
		numberMap["5"] = "五";
		numberMap["6"] = "六";
		numberMap["7"] = "七";
		numberMap["8"] = "八";
		numberMap["9"] = "九";
		numberMap["10"] = "十";
		numberMap["11"] = "十一";
		numberMap["12"] = "十二";
		numberMap["20"] = "二十"
		numberMap["30"] = "三十";

		var upperDate = "";
		//年
		upperDate = numberMap[year.substring(0,1)] + numberMap[year.substring(1,2)] + numberMap[year.substring(2,3)] + numberMap[year.substring(3,4)] + "年";
		//月
		upperDate += numberMap[month] + "月";
		//日
		if (day.length > 1){
			upperDate += numberMap[day.substring(0,1) + "0"];
			if (day.substring(1,2)!="0"){
				upperDate += numberMap[day.substring(1,2)];
			}
		}else{
			upperDate += numberMap[day];
		}
		upperDate += "日";
		//return
		return upperDate;
	}catch(e){
		return "";
	}
};

/**  
 * Simple Map  
 *   
 *   
 * var m = new Map();  
 * m.put('key','value');  
 * ...  
 * var s = "";  
 * m.each(function(key,value,index){  
 *      s += index+":"+ key+"="+value+"\n";  
 * });  
 * alert(s);  
 *   
 * @author dewitt  
 * @date 2008-05-24  
 */  
function Map() {   
    /** 存放键的数组(遍历用到) */  
    this.keys = new Array();   
    /** 存放数据 */  
    this.data = new Object();   
       
    /**  
     * 放入一个键值对  
     * @param {String} key  
     * @param {Object} value  
     */  
    this.put = function(key, value) {   
        if(this.data[key] == null){   
            this.keys.push(key);   
        }   
        this.data[key] = value;   
    };   
       
    /**  
     * 获取某键对应的值  
     * @param {String} key  
     * @return {Object} value  
     */  
    this.get = function(key) {   
    	if(this.data[key] != null){  
        	return this.data[key];   
        }else{
        	return "";   
        }
    };   
       
    /**  
     * 删除一个键值对  
     * @param {String} key  
     */  
    this.remove = function(key) {   
    	 if(this.data[key] != null){
//        	this.keys.remove(key);   
    	 	 this.data[key] = null;   
        	for (var i = 0; i < this.keys.length; i++) {
			   v = this.keys.pop();
			   if ( v.key === key ) {
			   	 this.keys.unshift(v);
			   	 break;
			   }
			  
			  }
			 

        }
    };   
       
    /**  
     * 遍历Map,执行处理函数  
     *   
     * @param {Function} 回调函数 function(key,value,index){..}  
     */  
    this.each = function(fn){   
        if(typeof fn != 'function'){   
            return;   
        }   
        var len = this.keys.length;   
        for(var i=0;i<len;i++){   
            var k = this.keys[i];   
            fn(k,this.data[k],i);   
        }   
    };   
       
    /**  
     * 获取键值数组(类似Java的entrySet())  
     * @return 键值对象{key,value}的数组  
     */  
    this.entrys = function() {   
        var len = this.keys.length;   
        var entrys = new Array(len);   
        for (var i = 0; i < len; i++) {   
            entrys[i] = {   
                key : this.keys[i],   
                value : this.data[i]   
            };   
        }   
        return entrys;   
    };   
       
    /**  
     * 判断Map是否为空  
     */  
    this.isEmpty = function() {   
        return this.keys.length == 0;   
    };   
       
    /**  
     * 获取键值对数量  
     */  
    this.size = function(){   
        return this.keys.length;   
    };   
       
    /**  
     * 重写toString   
     */  
    this.toString = function(){   
        var s = "{";   
        for(var i=0;i<this.keys.length;i++,s+=','){   
            var k = this.keys[i];   
            s += k+"="+this.data[k];   
        }   
        s+="}";   
        return s;   
    };   
} 

/**
 * 审批意见切换
 * @param {} imgId
 * @param {} divId
 */
function apprCommSwitch(imgId,divId){
	var img = $("#" + imgId).attr("src");
	var imgPath ="themes/comm/images/";
	var fileName = img.substring(imgPath.lastIndexOf("/")+1,img.indexOf("."));
	if(fileName == 'htcpClose'){
		$("#" + imgId).attr("src",imgPath+"htcpOpen.gif");
		$("#" + divId).show();
	}else{
		$("#" + imgId).attr("src",imgPath+"htcpClose.gif");
		$("#" + divId).hide();
	}
}

/**
 * JS 替换字符串
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