var mainFrame = $.getMainFrame();
var typeSel;
var groupSelect;
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
  	
  	typeSel = $("#typeDiv").ySelect({width:60});
  	typeSel.select(0);
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
	var userDeptName = groupSelect.getValue() ? groupSelect.getDisplayValue() : "";
	var userAccountId = $.trim($("#userAccountId").val());
	var type = typeSel.getValue();
	
	$.ajax({
	   type : "POST",
	   cache: false,
	   async : true,
	   url  : "m/staff_query",
	   data : "act=query&pageno="+pageno+"&pagecount="+pagecount+"&userDeptName="+userDeptName+"&userAccountId="+userAccountId+"&type="+type,             
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
							+ "<td>" + $("user-account",curELe).text() + "</td>"
							+ "<td>" + $("user-name",curELe).text() + "</td>"
							+ "<td>" + $("employee-number",curELe).text() + "</td>"
							+ "<td>" + $("company-area-name",curELe).text() + "</td>"
							+ "<td>" + $("dept",curELe).text() + "</td>"
							+ "<td>" +  $("post",curELe).text() + "</td>"
							+ "<td>" + $("entry-date",curELe).text() + "</td>"
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
		url:BASE_PATH + "/m/staff_query?act=view&id="+id
	});
}

