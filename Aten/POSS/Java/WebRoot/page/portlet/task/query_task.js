var mainFrame = $.getMainFrame();

$(initPage);
function initPage(){
	$.ajax({
	   	type : "POST",
	   	cache: false,
	   	async : true,
	   	url  : "l/task/dealingtasks",
		data : "pageNo=1&pageSize=5",    
		dataType : "json",
		success : function(data){
			if ($.checkErrorMsg(data) ) {
	   			if(data.taskPage && data.taskPage.dataList){
	   				var dataList = data.taskPage.dataList;
	   				var listDate = "<b>您当前总共有&nbsp;<font color=red>" +data.taskPage.totalCount + "</font>&nbsp;个待办任务。</b>";
	   				$(dataList).each(function(i) {
			    		var viewFlag = dataList[i].viewFlag;
			    		var description = dataList[i].description;
	                    listDate += "<li "
	                    		+ (viewFlag == false ? "style=\"font-weight:bold\">" : ">")
	                    		+ "<a href=\"javascript:openTask('" + dataList[i].id +  "','" + dataList[i].taskInstanceId + "')\"  title=" + description + ">（" 
	                    		+ dataList[i].createTime + "）" 
	                    		+ description + "</a></li>";
		    		});
		    		$("#taskList").html(listDate);
	   			}
			}
		}
	});
}

function openTask(taskId,tiid) {
	var url=BASE_PATH + "l/task/dispose?taskid=" + taskId + "&tiid=" + tiid;//+"&formType="+formType;
	mainFrame.addTab({
		id:"task_" + taskId,
		title:"处理任务",
		url:url,
		callback:initPage
	});
}

