var mainFrame = $.getMainFrame();

$(BudgetItemManage);
var passedSelect;
//报销单查询
function BudgetItemManage(){
	//添加权限约束
	$.handleRights({
        "searchLog" : $.SysConstants.QUERY
	});
	 
	//打开用户帐号
    $('#openUserSelect').click(
	   	function(e){
	   		var dialog = new UserDialog(ERMP_PATH, BASE_PATH);
	   		dialog.setCallbackFun(function(user){
				if (user != null) {
					$("#applicant").val(user.id);
	   				$("#applicantName").val(user.name);
				}
			});
	   		dialog.openDialog();
		}
	);
	
	//打开部门
    $('#openDeptSelect').click(
	   	function(e){
	   		var dialog = new DeptDialog(ERMP_PATH, BASE_PATH);
	   		dialog.setCallbackFun(function(user){
				if (user != null) {
	   				$("#deptName").val(user.name);
				}
			});
	   		dialog.openDialog();
		}
	);
	 
	 //报销单查询 
	$("#searchLog").click(function(){
  		gotoPage(1);
  	});//搜索事件
  	$.EnterKeyPress($("#reimId"),$("#searchLog"));
	$.EnterKeyPress($("#applicant"),$("#searchLog"));
	 //刷新
  	$("#refresh").click(function(){
  		loadList();
	});
  	
	passedSelect = $("#passedDiv").ySelect({width:40});
	 
	$('.invokeBoth').attachDatepicker({showOn: 'both', buttonImage: 'themes/comm/spacer.gif', buttonImageOnly: true});
}

//-------------------加载查询列表-------------------
function loadList(){
	if ( $.compareDate($("#beginArchiveDate").val(), $("#endArchiveDate").val())) {
		$.alert("开始时间不能大于结束时间！");
		return false;
	}
	$("#expenseAccountTab > tbody").empty();
	//分页
  	var pageno=$.trim($("#hidNumPage").val());
  	var pagecount=$.trim($("#hidPageCount").val());
  	
  	//查询字符串
  	var queryString = "&pageNo=" + pageno + "&pageSize=" + pagecount;
  	
	//获得参数
	var budgetItemName=$.trim($("#deptName").val());
	var beginArchiveDate=$.trim($("#beginArchiveDate").val());
	var endArchiveDate=$.trim($("#endArchiveDate").val());
	var applicant=$.trim($("#applicant").val());
	var reimId=$.trim($("#reimId").val());
	var passed = passedSelect.getValue();
	//提交
	$.ajax({
        type : "POST",
		cache: false,
		url  : "m/bud_bxdcxcw",
		data : "act=query"
				+ "&budgetItemName=" + budgetItemName
	 			+ "&beginArchiveDate=" + beginArchiveDate
	 			+ "&endArchiveDate=" + endArchiveDate
	 			+ "&applicant=" + applicant
	 			+ "&id=" + reimId
	 			+ "&passed="+passed
	 			+ queryString,
        
        success : function(xml){
           
            //解析XML中的返回代码
            var message = $("message",xml);
            if(message.attr("code") == "1"){
                var tBodyHTML = "";
                var pageNextHTML="";
                $("reimbursement",xml).each(
                    function(index){
                        var reimbursementConfig = $(this);
                        tBodyHTML += createTR(reimbursementConfig.attr("id"), 
                        $("apply-date",reimbursementConfig).text(), 
                        $("applicant",reimbursementConfig).text(),
                        $("budget-item",reimbursementConfig).text(),
                        $("archive-date",reimbursementConfig).text(),
                        $("passed",reimbursementConfig).text());
                    }
                );
                $("#expenseAccountTab > tbody").html(tBodyHTML).find("td:empty").html("&nbsp;");
                //------------------翻页数据--------------------------
	            $(".pageNext").html($.createTurnPage(xml));
	            $.EnterKeyPress($("#numPage"),$("#numPage").next());
            }
            else{
               $.alert($("message",xml).text());
            };
        },
        error : $.ermpAjaxError
    });	
}
//创建行
function createTR(id,applyDate,applicant,budgetItem,archiveDate,passed){
	var html = "";
	html += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" 
         + "<td>" + id + "</td>" //报销单号
         + "<td>" + applyDate + "</td>" 
         + "<td>" + applicant + "</td>" 
         + "<td>" + budgetItem + "</td>" 
         + "<td>" + archiveDate + "</td>"
         + "<td>" + passed + "</td>"; 
         //操作
	var op = ($.hasRight($.SysConstants.VIEW)?("<a href=\"javascript:void(0);\" onclick=\"viewReimbursement('" + id + "');\"  class=\"opLink\">查看</a> ") : "") 
    html += "<td>" + op;                   	           
    html += "</td></tr>";
    return html;
}

//查看调用的方法
function viewReimbursement(id){
	mainFrame.addTab({
		id:"oa_rei_track_view_" + id,
		title:"查看报销单",
		url:BASE_PATH + "m/bud_bxdcxcw?act=view&id="+id
	});	
	
}

//转向第几页
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
    loadList();
};

