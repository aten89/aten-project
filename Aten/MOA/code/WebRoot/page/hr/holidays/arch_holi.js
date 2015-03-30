var mainFrame = $.getMainFrame();

$(initVacationApprovalArch);

function initVacationApprovalArch() {
	$.handleRights({
        "query" : $.SysConstants.QUERY
    });
	//刷新
  	$("#refresh").click(function(){
  		queryPage();
  	});
  	
  	//查询
  	$("#query").click(function(){
  		gotoPage(1);
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
  	$.EnterKeyPress($("#applicantName"),$("#query"));
  	
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

	$("#list").empty();
 	
	var pageno=$.trim($("#hidNumPage").val());
	var pagecount=$.trim($("#hidPageCount").val());
	var applicant = $.trim($("#applicant").val());
	
	$.ajax({
	   type : "POST",
	   cache: false,
	   async : true,
	   url  : "m/holi_arch",
	   data : "act=query&pageno="+pageno+"&pagecount="+pagecount+"&applicant="+applicant,             
	   success : function (xml){
	   		var message = $("message",xml);
			var content = $("content",xml);
			var listDate = "";
			//是否有数据返回
		    if (message.attr("code") == "1") {	                              	    	  
		    	$(xml).find('holidayApply').each(function(index){
		    		var curELe = $(this);
                    var id = curELe.attr("id");
                    var cancelHoli = $("cancel-opt",curELe).text() =="1";
                    listDate += " <tr onmouseout=\"$(this).removeClass('over');\" onmouseover=\"$(this).addClass('over');\">"
							+ "<td>" + id + "</td>"
							+ "<td>" + $("apply-account",curELe).text() + "</td>"
							+ "<td>" + $("dept",curELe).text() + "</td>"
							+ "<td>" + $("cancel-flag",curELe).text() + "</td>"
							+ "<td>" + $("days",curELe).text() + "</td>"
							+ "<td>" + $("apply-date",curELe).text() + "</td>"
							+ "<td>" + $("status",curELe).text() + "</td>"
							+ "<td>" + ( $.hasRight($.SysConstants.VIEW)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"initView('" + id + "')\">详情</a>" : "");
					if(cancelHoli){
						listDate+=($.hasRight($.SysConstants.VIEW)? "&nbsp;|&nbsp;<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"initCancel('" + id + "')\">销假</a>" : "");
					}		
							
					listDate+= "</td></tr>";

				});
				$("#list").html(listDate);
				//----------------------------------------------------
		        //------------------翻页数据----------------------------
		        $(".pageNext").html($.createTurnPage(xml));
		        //----------------------------------------------------		
		        $.EnterKeyPress($("#numPage"),$("#numPage").next());
		    } else {
		        $.alert("没有查询结果");
		    };
	   		$("#searchReim").removeAttr("disabled");
	   },
	   error : function(){
	        $("#searchReim").removeAttr("disabled");
	        $.ermpAjaxError();
	    }
	});
}

function initView(id) {
	mainFrame.addTab({
		id:"oa_holiday_arch_viewdraft_"+id,
		title:"查看请假单",
		url:BASE_PATH + "/m/holi_arch?act=view&id="+id
	});
}

function initCancel(id) {
	mainFrame.addTab({
		id:"oa_holiday_arch_canceldraft_"+id,
		title:"请假单销假",
		url:BASE_PATH + "/m/holi_arch?act=initcancel&id="+id,
		callback:queryPage
	});
}

