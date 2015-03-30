var columnSortObj;//表头列排序对像

$(initPage);
function initPage() {
  	
  	$('.invokeBoth').attachDatepicker({showOn: 'both', buttonImage: 'themes/comm/spacer.gif', buttonImageOnly: true});
  	//添加权限约束
    $.handleRights({
        "searchReim" : $.SysConstants.QUERY
    });
	//打开用户帐号
	$("#openUserSelect").click(
	   	function(e){
			var dialog = new UserDialog(ERMP_PATH, BASE_PATH);
			dialog.setCallbackFun(function(user){
				if (user != null) {
					$("#applicant").val(user.id);
					$("#applicantName").val(user.name);
				}
			});
			dialog.openDialog("single");
		}
	);
	
	//查询
  	$("#searchReim").click(function(){
  		$("#hidNumPage").val(1);
  		queryList();
  	});
  	
  	//初始化表头列排序
    columnSortObj = $("#tableTH").columnSort();
	//查询
  	queryList();
}

function queryList() {
	//对排序参数的处理
	var sortCol = columnSortObj.getSortColumn();
	var ascend = columnSortObj.getAscend();
	
	var pageno=$.trim($("#hidNumPage").val());
	var pagecount=$.trim($("#hidPageCount").val());
	var id=$.trim($("#reiId").val());
	var beginApplyDate=$.trim($("#beginApplyDate").val());
	var endApplyDate=$.trim($("#endApplyDate").val());
	var applicant = $.trim($("#applicant").val());
	
	$("#reimList").empty();
	$.ajax({
	    type : "POST",
	    cache: false,
	    async : true,
	    url  : "m/rei_deal",
	    data : "act=query&pageno="+pageno+"&pagecount="+pagecount+"&sortCol=" + sortCol + "&ascend=" + ascend 
	    		+ "&id=" + id + "&beginApplyDate="+beginApplyDate +"&endApplyDate="+endApplyDate+"&applicant="+applicant,
	    success : function(xml){
	        var message = $("message",xml);
	        if(message.attr("code") == "1"){
	            //------------------列表数据----------------------------
	            var listDate = "";
	            $("reimbursement",xml).each(
	                function(index){
	                    var curELe = $(this);
	                    var viewFlag = $("view-flag",curELe).text();
	                    listDate += " <tr onmouseout=\"$(this).removeClass('over');\" onmouseover=\"$(this).addClass('over');\" "
	                    		+ (viewFlag == "false" ? "style=\"font-weight:bold\">" : ">")
								+ "<td>" + curELe.attr("id") + "</td>"
								+ "<td>" + $("apply-date",curELe).text() + "</td>"//报销时间
								+ "<td>" + $("applicant",curELe).text() + "</td>"
								+ "<td>" + $("budget-item",curELe).text() + "</td>"
								+ "<td>" + $("reimbursement-sum",curELe).text() + "</td>"
								+ "<td>" + $("node-name",curELe).text() + "</td>"
								+ "<td>" + $("create-time",curELe).text() + "</td>"
								+ "<td>" + ( $.hasRight($.OaConstants.DISPOSE)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"initDispose('" + $("task-id",curELe).text() + "','" + $("taskinstance-id",curELe).text() + "')\">处理</a> " : "")
								+ "</td></tr>";
	                }
	            );
	            $("#reimList").append(listDate);
	             var totalCount = $("content",xml).attr("total-count");
                $("#totalCount").html(totalCount ? totalCount : "0");
	            $(".pageNext").html($.createTurnPage(xml));
		        //----------------------------------------------------		
		        $.EnterKeyPress($("#numPage"),$("#numPage").next());
	        }
	    },
	    error : $.ermpAjaxError
	});
}

function initDispose(taskid, tiid) {
	mainFrame.addTab({
		id:"oa_rei_deal_dispose_" + taskid,
		title:"处理报销单",
		url:BASE_PATH + "/m/rei_deal?act=dispose&taskid="+taskid+"&tiid=" + tiid,
		callback:queryList
	});
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
    queryList();
};