var mainFrame = $.getMainFrame();

$(initVacationApproval);

function initVacationApproval(){
	//刷新
  	$("#refresh").click(function(){
  	    $("#infoDraftList").empty();
  		queryList();
  	});
  	//查询
  	queryList();
	
}
function queryList() {
	$("#reimList").empty();
	$.ajax({
	    type : "POST",
	    cache: false,
	    async : true,
	    url  : "m/tran_deal?act=query",
	    success : function(xml){
	        var message = $("message",xml);
	        if(message.attr("code") == "1"){
	            //------------------列表数据----------------------------
	            var listDate = "";
	            $("TransferApply",xml).each(
	                function(index){
	                    var curELe = $(this);
	                    var viewFlag = $("flag",curELe).text();
	                    listDate += " <tr onmouseout=\"$(this).removeClass('over');\" onmouseover=\"$(this).addClass('over');\" "
	                    		+ (viewFlag == "false" ? "style=\"font-weight:bold\">" : ">")
								+ "<td>" + curELe.attr("id") + "</td>"
								+ "<td>" + $("apply-account",curELe).text() + "</td>"
								+ "<td>" + $("transfer-user-name",curELe).text() + "</td>"
								+ "<td>" + $("transfer-out-dept",curELe).text() + "</td>"
								+ "<td>" + $("transfer-in-dept",curELe).text() + "</td>"
								+ "<td>" + $("transfer-date",curELe).text() + "</td>"
								+ "<td>" + $("task-start",curELe).text() + "</td>"
								+ "<td>" + ( $.hasRight($.OaConstants.DISPOSE)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"initDispose('" + $("task-id",curELe).text() + "','" + $("taskinstance-id",curELe).text() + "')\">处理</a> " : "")
								+ "</td></tr>";
	                }
	            );
	            $("#reimList").append(listDate);
	             var totalCount = $("content",xml).attr("total-count");
                $("#totalCount").html(totalCount ? totalCount : "0");
	        }
	    },
	    error : $.ermpAjaxError
	});
}


function initDispose(taskid, tiid) {
	mainFrame.addTab({
		id:"oa_holiday_deal" + taskid,
		title:"处理异动单",
		url:BASE_PATH + "/m/tran_deal?act=dispose&taskid="+taskid+"&tiid=" + tiid,
		callback:queryList
	});
}
