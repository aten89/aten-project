var mainFrame = $.getMainFrame();

$(initPage);

function initPage(){
	//刷新
  	$("#refresh").click(function(){
  		queryPage();
  	});
    gotoPage(1);
}

function gotoPage(pageNo, totalPage){
    if(isNaN(pageNo)){
        $.alert("页索引只能为数字！");
        return;
    } else if(pageNo < 1){
        pageNo = 1;
    }
    if (totalPage && !isNaN(totalPage) && pageNo > totalPage) {
    	pageNo = totalPage;
    }
    $("#hidNumPage").val(parseInt(pageNo));
    queryPage();
};

function queryPage() {

	$("#taskList").empty();
 	
	var pageno=$.trim($("#hidNumPage").val());
	var pagecount=$.trim($("#hidPageCount").val());
	$.ajax({
	   type : "POST",
	   cache: false,
	   async : true,
	   url  : "page/portlet",
	   data : "act=dealingtasks&pageno="+pageno+"&pagecount="+pagecount,             
	   success : function (xml){
	   		var message = $("message",xml);
			var content = $("content",xml);
			var listDate = "";
			//是否有数据返回
		    if (message.attr("code") == "1") {                      	    	  
		    	$(xml).find('task').each(function(index){
		    		var curELe = $(this);
		    		var viewFlag = $("view-flag",curELe).text();
                    var id = curELe.attr("id");
                    listDate += " <tr onmouseout=\"$(this).removeClass('over');\" onmouseover=\"$(this).addClass('over');\" "
                   			+ (viewFlag == "false" ? "style=\"font-weight:bold\">" : ">")
                   			+ "<td>" + (index + 1) + "</td>"
							+ "<td>" + $("description",curELe).text() + "</td>"
							+ "<td>" + $("flow-class-name",curELe).text() + "</td>"
							+ "<td>" + $("node-name",curELe).text() + "</td>"
							+ "<td>" + $("create-time",curELe).text() + "</td>"
							+ "<td><a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"javascript:openTask('" 
                    		+ $("task-id",curELe).text() + "','" + $("flow-class-name",curELe).text() + "','" + $("form-id",curELe).text() + "','" + $("taskinstance-id",curELe).text() + "','" + $("description",curELe).text() + "')\">处理</a> "
							+ "</td></tr>";

				});
				$("#taskList").html(listDate);
				//----------------------------------------------------
		        //------------------翻页数据----------------------------
		        $(".pageNext").html($.createTurnPage(xml));
		        //----------------------------------------------------		
		        $.EnterKeyPress($("#numPage"),$("#numPage").next());
		    } else {
		        $.alert("没有查询结果");
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
		id:"oa_portlet_deal_dispose_" + taskId,
		title:"处理任务",
		url:url,
		callback:queryPage
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