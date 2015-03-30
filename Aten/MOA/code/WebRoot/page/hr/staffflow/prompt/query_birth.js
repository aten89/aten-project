var mainFrame = $.getMainFrame();
var groupSelect;
$(initVacationApprovalArch);

function initVacationApprovalArch() {
	
	$.handleRights({
        "query" : $.SysConstants.QUERY,
        "addEntry" : $.SysConstants.ADD,
        "exportExcel" : $.SysConstants.EXPORT//登记
    });
   
  	
  	
  	//查询
  	$("#query").click(function(){
  		gotoPage(1);
  	});
  //初始化机构列表
	var url =ERMP_PATH + "m/rbac_group/groupselect?jsoncallback=?";
	$.getJSON(url,function(data){
		$("#groupIdDiv").html(data.htmlValue);
		groupSelect = $("#groupIdDiv").ySelect({width:100});
		groupSelect.addOption("", "所有...", 0);
		groupSelect.select(0);
		
		//默认搜索
		queryPage();
	});
  	$.EnterKeyPress($("#userAccountId"),$("#query"));
  	
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

	$("#dataList > tbody").empty();
 	
	var pageno=$.trim($("#hidNumPage").val());
	var pagecount=$.trim($("#hidPageCount").val());
	var userDeptName = groupSelect.getValue() ? groupSelect.getDisplayValue() : "";
	var userAccountId = $.trim($("#userAccountId").val());
	var type = $("#type").val();
	
	$.ajax({
	   type : "POST",
	   cache: false,
	   async : true,
	   url  : "m/staff_prompt",
	   data : "act=query_birth&pageno="+pageno+"&pagecount="+pagecount+"&userDeptName="+userDeptName+"&userAccountId="+userAccountId,             
	   success : function (xml){
	   		var message = $("message",xml);
			var content = $("content",xml);
			//是否有数据返回
		    if (message.attr("code") == "1") {
		    	var listDate = "";
		    	$(xml).find('staff-flow').each(function(index){
		    		var curELe = $(this);
                    var id = curELe.attr("id");
                     var op = ( $.hasRight($.SysConstants.VIEW)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"initView('" + id + "')\">详情</a>" : "");
                    	   
                    listDate += " <tr onmouseout=\"$(this).removeClass('over');\" onmouseover=\"$(this).addClass('over');\">"
							+ "<td>" + $("userName",curELe).text() + "</td>"
							+ "<td>" + $("employeeNumber",curELe).text() + "</td>"
							+ "<td>" + $("statDays",curELe).text() + "</td>"
							+ "<td>" + $("birthdate",curELe).text() + "</td>"
							+ "<td>" + $("staffStatus",curELe).text() + "</td>"
							+ "<td>" + $("groupFullName",curELe).text() + "</td>"
							+ "<td>" + op + "</td></tr>";

				});
				$("#listData").html(listDate)
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
		id:"oa_staff_arch_viewarch_"+id,
		title:"查看员工信息",
		url:BASE_PATH + "/m/staff_query?act=view&id="+id
	});
}