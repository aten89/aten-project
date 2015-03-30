var mainFrame = $.getMainFrame();

$(initPage);
function initPage(){
	$.ajax({
	   type : "POST",
	   cache: false,
	   async : true,
	   url  : BASE_PATH + "page/portlet",
	   data : "act=dealingtasks&pageno=1&pagecount=5",             
	   success : function (xml){
	   		var message = $("message",xml);
			var content = $("content",xml);
			var listDate = "<b>您当前总共有&nbsp;<font color=red>" + content.attr("total-count") + "</font>&nbsp;个待办任务。</b>";
			//是否有数据返回
		    if (message.attr("code") == "1") {	                              	    	  
		    	$(xml).find('task').each(function(index){
		    		var curELe = $(this);
		    		var viewFlag = $("view-flag",curELe).text();
		    		var description = $("description",curELe).text();
                    listDate += "<li "
                    		+ (viewFlag == "false" ? "style=\"font-weight:bold\">" : ">")
                    		+ "<a href=\"javascript:openTask('"
                    		+ $("task-id",curELe).text() + "','" + $("flow-class-name",curELe).text() + "','" + $("form-id",curELe).text() + "','" 
                    		+ $("taskinstance-id",curELe).text() + "','" + $("description",curELe).text() + "')\"  title=" + description + ">（" 
                    		+ $("create-time",curELe).text() + "）" 
                  //  		+ $("flow-class-name",curELe).text() + "--" 
                    		+ description + "</a></li>";
				});
				$("#taskList").html(listDate);
		    } else {
		        $.alert(message.text());
		    };
	   },
	   error : function(){
	        $.ermpAjaxError();
	    }
	});
}

function openTask(taskId,flowName,formId,tiid,description) {
	
//	var formType="";
//	if (flowName.indexOf("领用") !=-1 || flowName.indexOf("申购") !=-1) {
//		//领用申购
//		initDisposePurchase(formId, taskId,tiid);
//		return;
//	} else if(flowName.indexOf("调拨") !=-1) {
//		//调拨
//		formType = 2;
//	} else if(flowName.indexOf("报废") !=-1) {
//		//报废
//		formType = 3;
//	} else if(flowName.indexOf("离职") !=-1) {
//		//离职处理
//		formType = 4;
//	}
	
	var url=BASE_PATH + "page/portlet?act=dispose&taskid=" + taskId + "&tiid=" + tiid;//+"&formType="+formType;
//	if(flowName.indexOf("销售模块映射业务模块")!=-1){//CRM 维护页面
//		url = CRM_PATH + "/m/latestsellmod/initSellModMapping?taskId="+taskId+"&productId=" + formId +"&text=" + encodeURI(description.substring(0,description.length-15));
//	}
	mainFrame.addTab({
		id:"task_" + taskId,
		title:"处理任务",
		url:url,
		callback:initPage
	});
}

//function initDisposePurchase(formId, taskId,tiid){
//	mainFrame.addTab({
//		id:"task_" + taskId,
//		title:"待办任务",
//		url:BASE_PATH + "m/dev_deal?act=dispose_purchase&taskid=" + taskId + "&tiid=" + tiid+"&formId="+formId,
//		callback:initPage
//	});
//}