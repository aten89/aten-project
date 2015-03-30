/**********************
出差审批--待办列表
*********************/ 
var mainFrame = $.getMainFrame();
$(initApprovalDeal);
function initApprovalDeal(){
	//查询
	queryPage();
};

function queryPage() {
	$("#tripDealList").empty();
	$.ajax({
	    type : "POST",
	    cache: false,
	    async : true,
	    url  : "m/trip_deal",
	    data : "act=query",    
	    success : function(xml){
	        var message = $("message",xml);
	        if(message.attr("code") == "1"){
	            //------------------列表数据----------------------------
	            var listDate = "";
	            $("busTripApply",xml).each(
	                function(index){
	                    var curELe = $(this);
	                    var viewFlag = $("flag",curELe).text();
	                    listDate += " <tr onmouseout=\"$(this).removeClass('over');\" onmouseover=\"$(this).addClass('over');\" "
	                    		+ (viewFlag == "false" ? "style=\"font-weight:bold\">" : ">")
	                    		+ "<td>" + curELe.attr("id") + "</td>"
								+ "<td>" + $("apply-account",curELe).text() + "</td>"
								+ "<td>" + $("dept",curELe).text() + "</td>"
								+ "<td>" + $("trip-sche",curELe).text() + "</td>"
								+ "<td>" + $("days",curELe).text() + "</td>"
								+ "<td>" + $("task-start",curELe).text() + "</td>"
								+ "<td>" + ( $.hasRight($.OaConstants.DISPOSE)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"initDispose('" + $("task-id",curELe).text() + "','" + $("taskinstance-id",curELe).text() + "')\">处理</a> " : "")
								+ "</td></tr>";
	                }
	            );
	            $("#tripDealList").append(listDate);
		        //----------------------------------------------------		
		        $.EnterKeyPress($("#numPage"),$("#numPage").next());
	        }
	    },
	    error : $.ermpAjaxError
	});
}


function initDispose(taskid, tiid) {
	mainFrame.addTab({
		id:"oa_bustrip_deal" + taskid,
		title:"处理出差单",
		url:BASE_PATH + "m/trip_deal?act=dispose&taskid="+taskid+"&tiid=" + tiid,
		callback:queryPage
	});
}
