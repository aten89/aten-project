var mainFrame = $.getMainFrame();
var applyTypeSel;
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
  	
  	applyTypeSel = $("#applyTypeDiv").ySelect({width:60});
  	
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
	var applyType = applyTypeSel.getValue();
	
	$.ajax({
	   type : "POST",
	   cache: false,
	   async : true,
	   url  : "m/staff_arch",
	   data : "act=query&pageno="+pageno+"&pagecount="+pagecount+"&applicant="+applicant+"&applyType="+applyType,             
	   success : function (xml){
	   		var message = $("message",xml);
			var content = $("content",xml);
			var listDate = "";
			//是否有数据返回
		    if (message.attr("code") == "1") {	                              	    	  
		    	$(xml).find('staff-flow').each(function(index){
		    		var curELe = $(this);
                    var id = curELe.attr("id");
                     var applyType = $("apply-type",curELe).text();
                    listDate += " <tr onmouseout=\"$(this).removeClass('over');\" onmouseover=\"$(this).addClass('over');\">"
							+ "<td>" + $("apply-account",curELe).text() + "</td>"
							+ "<td>" + (applyType == "1" ? "入职申请":"离职申请") + "</td>"
							+ "<td>" + (applyType == "2" ? $("resign-date",curELe).text() : $("entry-date",curELe).text()) + "</td>"
							+ "<td>" + $("company-area-name",curELe).text() + "</td>"
							+ "<td>" + $("dept",curELe).text() + "</td>"
							+ "<td>" + $("user-name",curELe).text() + "</td>"
							+ "<td>" + $("apply-date",curELe).text() + "</td>"
							+ "<td>" + $("status",curELe).text() + "</td>"
							+ "<td>" + ( $.hasRight($.SysConstants.VIEW)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"initView('" + id + "','" + applyType + "')\">详情</a>" : "");	
							
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

function initView(id, applyType) {
	mainFrame.addTab({
		id:"oa_staff_arch_viewarch_"+id,
		title:"查看" + (applyType == "1" ? "入职审批" : "离职审批"),
		url:BASE_PATH + "/m/staff_arch?act=view&id="+id
	});
}

