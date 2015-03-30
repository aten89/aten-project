var mainFrame = $.getMainFrame();

$(initVacationApproval);

function initVacationApproval(){
  	//查询
  	queryList();
	
}
function queryList() {
	$("#reimList").empty();
	$.ajax({
	    type : "POST",
	    cache: false,
	    async : true,
	    url  : "m/staff_deal?act=query",
	    success : function(xml){
	        var message = $("message",xml);
	        if(message.attr("code") == "1"){
	            //------------------列表数据----------------------------
	            var listDate = "";
	            $("staff-flow",xml).each(
	                function(index){
	                    var curELe = $(this);
	                    var viewFlag = $("flag",curELe).text();
	                    var applyType = $("apply-type",curELe).text();
	                    listDate += " <tr onmouseout=\"$(this).removeClass('over');\" onmouseover=\"$(this).addClass('over');\" "
	                    		+ (viewFlag == "false" ? "style=\"font-weight:bold\">" : ">")
								+ "<td>" + $("apply-account",curELe).text() + "</td>"
								+ "<td>" + (applyType == "1" ? "入职申请":"离职申请") + "</td>"
								+ "<td>" + (applyType == "2" ? $("resign-date",curELe).text() : $("entry-date",curELe).text()) + "</td>"
								+ "<td>" + $("company-area-name",curELe).text() + "</td>"
								+ "<td>" + $("dept",curELe).text() + "</td>"
								+ "<td>" + $("user-name",curELe).text() + "</td>"
								+ "<td>" + $("task-start",curELe).text() + "</td>"
								+ "<td>" + ( $.hasRight($.OaConstants.DISPOSE)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"initDispose('" + $("task-id",curELe).text() + "','" + $("taskinstance-id",curELe).text() + "','" + applyType + "')\">处理</a> " : "")
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


function initDispose(taskid, tiid, applyType) {
	mainFrame.addTab({
		id:"oa_holiday_deal" + taskid,
		title:"处理" + (applyType == "1" ? "入职审批" : "离职审批"),
		url:BASE_PATH + "/m/holi_deal?act=dispose&taskid="+taskid+"&tiid=" + tiid,
		callback:queryList
	});
}
