$(BudgetItemManage);

var outlayCategorySelect;
var outlayNameSelect;

function BudgetItemManage(){
	//添加权限约束
    $.handleRights({
        "searchLog": $.SysConstants.QUERY
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
	 
	 //费用明细查询 
	$("#searchLog").click(function(){
  		gotoPage(1);
  	});//搜索事件
	 
	 //刷新
	$("#refresh").click(function(){
  		loadList();
	});
	$('.invokeBoth').attachDatepicker({showOn: 'both', buttonImage: 'themes/comm/spacer.gif', buttonImageOnly: true});
	
	outlayCategorySelect = $("#outlayCategoryDiv").ySelect({width:80, url:"m/rei_start?act=costclass",
		onChange : onSelectoutlayCategory,
		afterLoad:function(){
			outlayCategorySelect.addOption("", "请选择...", 0);
		}
	});
	outlayNameSelect = $("#outlayNameDiv").ySelect({width:80,isdisabled:true});
}

//选费用类别时
function onSelectoutlayCategory() {
	var outlayCategory = outlayCategorySelect.getDisplayValue();
	outlayNameSelect = $("#outlayNameDiv").ySelect({
		width:65, 
		url:"m/rei_start?act=costitem",
		data:"costclass=" + outlayCategory,
		afterLoad:function(){
			outlayNameSelect.addOption("", "请选择...", 0);
		}
	});
}
//-------------------加载查询列表-------------------
function loadList(){
	if ( $.compareDate($("#beginArchiveDate").val(), $("#endArchiveDate").val())) {
		$.alert("开始时间不能大于结束时间！");
		return false;
	}
	$("#outlayListTab > tbody").empty();
	 //分页
  	var pageno=$.trim($("#hidNumPage").val());
  	var pagecount=$.trim($("#hidPageCount").val());
	var applicant=$.trim($("#applicant").val());
	
	//获得参数
	var budgetItemName=$.trim($("#deptName").val());
	var outlayName=outlayNameSelect.getValue()==""?"":outlayNameSelect.getDisplayValue();
	var outlayCategory=outlayCategorySelect.getValue()==""?"":outlayCategorySelect.getDisplayValue();
	var beginArchiveDate=$.trim($("#beginArchiveDate").val());
	var endArchiveDate=$.trim($("#endArchiveDate").val());
	//提交
	$.ajax({
        type : "POST",
		cache: false,
		url  : "m/bud_fymxcw",
		data : {
				act:"QUERY",
				budgetItemName:budgetItemName,
				beginArchiveDate:beginArchiveDate,
				endArchiveDate:endArchiveDate,
				outlayName:outlayName,
				applicant:applicant,
				outlayCategory:outlayCategory,
				pageNo:pageno,
				pageSize:pagecount
			},
        success : function(xml){
            //解析XML中的返回代码
            var message = $("message",xml);
            if(message.attr("code") == "1"){
                var tBodyHTML = "";
                var pageNextHTML="";
                $("outlayList",xml).each(
                    function(index){
                        var outlayListConfig = $(this);
                        tBodyHTML += createTR(outlayListConfig.attr("id"),
                        $("reimbursement-id",outlayListConfig).text(), 
                        $("applicant",outlayListConfig).text(),
                        $("budget-item",outlayListConfig).text(),
                        $("outlay-category",outlayListConfig).text(),
                        $("outlay-name",outlayListConfig).text(),
                        $("outlay-sum",outlayListConfig).text(),
                        $("description",outlayListConfig).text(),
                        $("archive-date",outlayListConfig).text());
                    }
                );
                $("#outlayListTab > tbody").html(tBodyHTML).find("td:empty").html("&nbsp;");
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
function createTR(id,reimbursementid,applicant,budgetitem,outlaycategory,outlayname,outlaySum,description,archivedate){
	var html = "";
	html += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" 
         + "<td>" + reimbursementid + "</td>" 
         + "<td>" + applicant + "</td>" 
         + "<td>" + archivedate + "</td>"
         + "<td>" + budgetitem + "</td>" 
         + "<td>" + outlaycategory + "</td>" 
         + "<td>" + outlayname + "</td>" 
         + "<td>" + outlaySum + "</td>" 
         + "<td>" + description + "</td>"
         ; 
         //操作
    html += "</tr>";
    return html;
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
