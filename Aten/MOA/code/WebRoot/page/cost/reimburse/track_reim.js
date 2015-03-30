var budgetItemSel;//费用归属项目下拉列表
$(initPage);
function initPage(){
	 	//添加权限约束
    $.handleRights({
        "searchReim" : $.SysConstants.QUERY
    });
	$('.invokeBoth').attachDatepicker({showOn: 'both', buttonImage: 'themes/comm/spacer.gif', buttonImageOnly: true});
	//添加权限约束
    $.handleRights({
        "searchReim" : $.SysConstants.QUERY
    });
  	
  	//初始化表头列排序
    columnSortObj = $("#tableTH").columnSort(queryPage);
    
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

	$('.invokeBoth').attachDatepicker({showOn: 'both', buttonImage: 'themes/comm/spacer.gif', buttonImageOnly: true});
  	
  	
	//刷新
  	$("#refresh").click(function(){
  		queryPage();
  	});
  	//查询
  	$("#searchReim").click(function(){
  		gotoPage(1);
  	});
  	$.EnterKeyPress($("#applicant"),$("#searchReim"));
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
	if ( $.compareDate($("#beginApplyDate").val(), $("#endApplyDate").val())) {
		$.alert("开始时间不能大于结束时间！");
		return false;
	}
	//设置查询按钮
	$("#searchReim").attr("disabled","true");
	
	$("#reimTrackList").empty();
 	
	//对排序参数的处理
	var sortCol = columnSortObj.getSortColumn();
	var ascend = columnSortObj.getAscend();
	
	var pageno=$.trim($("#hidNumPage").val());
	var pagecount=$.trim($("#hidPageCount").val());
	var id=$.trim($("#reiId").val());
	var beginApplyDate=$.trim($("#beginApplyDate").val());
	var endApplyDate=$.trim($("#endApplyDate").val());
	var applicant = $.trim($("#applicant").val());
	
	$.ajax({
	   type : "POST",
	   cache: false,
	   async : true,
	   url  : "m/rei_track",
	   data : "act=query&pageno="+pageno+"&pagecount="+pagecount+"&sortCol=" + sortCol + "&ascend=" + ascend 
	    		+ "&id=" + id + "&beginApplyDate="+beginApplyDate +"&endApplyDate="+endApplyDate+"&applicant="+applicant,
	   success : function (xml){
	   		var message = $("message",xml);
			var content = $("content",xml);
			var listDate = "";
			//是否有数据返回
		    if (message.attr("code") == "1") {	                              	    	  
		    	$(xml).find('reimbursement').each(function(index){
		    		var curELe = $(this);
                    var id = curELe.attr("id");
                    listDate += " <tr onmouseout=\"$(this).removeClass('over');\" onmouseover=\"$(this).addClass('over');\">"
							+ "<td>" + id + "</td>"
							+ "<td>" + $("apply-date",curELe).text() + "</td>"
							+ "<td>" + $("applicant",curELe).text() + "</td>"
							+ "<td>" + $("reimbursement-sum",curELe).text() + "</td>"
							+ "<td>" + $("budget-item",curELe).text() + "</td>"
							+ "<td>" + $("node-name",curELe).text() + "</td>"
							+ "<td>" + $("transactor",curELe).text() + "</td>"
							+ "<td>" + $("create-time",curELe).text() + "</td>"
							+ "<td>" + ( $.hasRight($.SysConstants.VIEW)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"initView('" + id + "')\">详情</a> " : "")
							+ "</td></tr>";

				});
				$("#reimTrackList").html(listDate);
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
		id:"oa_rei_track_viewdraft_"+id,
		title:"查看报销单", 
		url:BASE_PATH + "/m/rei_track?act=view&id="+id
	});
}