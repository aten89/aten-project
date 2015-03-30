var mainFrame = $.getMainFrame();
var trunPageObj;//翻页组件对像
$(initPage);

function initPage(){
	//刷新
  	$("#refresh").click(function(){
  		queryList();
  	});
     //初始化翻页组件
    trunPageObj =  $(".pageNext").turnPage();
	trunPageObj.gotoPage(1);//搜索第一页
}

function queryList() {

	$("#taskList").empty();
 	//分页
	var pageNo=trunPageObj.getCurrentPageNo();//当前第几页
	var pageCount=trunPageObj.getPageCount();//一页多少条
	$.ajax({
	   	type : "POST",
	   	cache: false,
	   	async : true,
		url  : "l/task/dealingtasks",
	   	data : "pageNo="+pageNo+"&pageSize="+pageCount,
	   	dataType : "json",
		success : function(data){
			if ($.checkErrorMsg(data) ) {
	   			var listDate = "";
				if(data.taskPage && data.taskPage.dataList){
	   				var dataList = data.taskPage.dataList;
	   				$(dataList).each(function(i) {
	   					var viewFlag = dataList[i].viewFlag;
	                    listDate += " <tr onmouseout=\"$(this).removeClass('over');\" onmouseover=\"$(this).addClass('over');\" "
	                   			+ (viewFlag == false ? "style=\"font-weight:bold\">" : ">")
	                   			+ "<td>" + (i + 1) + "</td>"
								+ "<td>" + dataList[i].description + "</td>"
								+ "<td>" + dataList[i].flowName + "</td>"
								+ "<td>" + dataList[i].nodeName + "</td>"
								+ "<td>" + dataList[i].createTime + "</td>"
								+ "<td><a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"javascript:openTask('" + dataList[i].id +  "','" + dataList[i].taskInstanceId + "')\">处理</a> "
								+ "</td></tr>";

	   				});
				}
				$("#taskList").html(listDate);
				trunPageObj.setPageData(data.actInfoPage);
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
