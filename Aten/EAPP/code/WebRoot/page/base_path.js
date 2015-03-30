
(function($){
	//翻页页码组件
	//pageCount：一页显示数，默认10
	//callBack：查询的回调方法
	$.fn.turnPage = function(pageCount, callQuery) {
		try{pageCount = $.cookie("PAGE_COUNT");}catch(e){};//从COOCIE读
		pageCount = (pageCount != null) ? pageCount : 12;
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
	        	$.alert("会话已超时,请重新登入", function(){
	        		if (typeof cmd == "function") {
		        		cmd();
		        	} else if (cmd == "close") {
		        		top.window.close();
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
	
	//AJAX同步加载页面或页面片段
	//参数target加载后页面显示的目标;
	//参数url页面的访问地址;
	//参数data请求参数;
	//参数cmd超时时回调方法或执行的命令
	$.ajaxLoadPage = function(target, url, data, cmd){
		if(!target){
    		$.alert("目标显示对象为空");
			return;
		}
		var targetObj = (typeof(target) == "string") ? $("#" + target) : target;
		
	    $.ajax({
	        type : "POST",
	        cache : false, 
	        async : false,
	        url : url,
	        data : data,
	        dataType : "html",
	        success : function(resText) {
	  			if (resText.charAt(0) =='{') {//返回JSON数据，说明已超时
			    	$.alert("会话已超时,请重新登入", function(){
		        		if (typeof cmd == "function") {
			        		cmd();
			        	} else if (cmd == "close") {
			        		top.window.close();
			        	} else {
			        		top.location.reload();
			        	}
		        	});
				} else {
					targetObj.html(resText);
				}
	        }
	    });
	}
	
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
    
    $.validNumber = function(input, label, required, maxValue, minValue){
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
		
		if (isNaN(validValue)) {
       		$.alert(label+"不是有效的数字类型");
       		inputObj.focus();
       		return true;
       	}
       	if("undefined" != typeof maxValue) {
			//最大值
			if (Number(validValue) > maxValue) {
				$.alert(label + "不能大于" + maxValue);
				inputObj.focus();
				return true;
			}
		}
		if("undefined" != typeof minValue) {
			//最小值
			if (Number(validValue) < minValue) {
				$.alert(label + "不能小于" + minValue);
				inputObj.focus();
				return true;
			}
		}
       	return false;
    };
})(jQuery);
