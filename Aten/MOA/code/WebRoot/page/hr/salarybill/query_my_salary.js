var mainFrame = $.getMainFrame();
var yearSel;
var monthSel;
$(initPage);
//var typeSel;
function initPage(){
	$.handleRights({
        "querySalary" : $.SysConstants.QUERY,
        "impSalary" : $.OaConstants.IMPORT
    });
    
    var d=new Date(); 
    var year = d.getFullYear();
    var yearHTML = "";
    for (var i = 0; i <10; i++){
    	yearHTML += "<div>" + (year - i) + "**" + (year - i) + "年</div>";
    }
    
    yearSel = $("#yearDIV").html(yearHTML).ySelect({width:40});
    yearSel.select(0);
    monthSel = $("#montyDIV").ySelect({width:30});
    monthSel.select(d.getMonth());
    
  	$("#refresh").click(function(){
  		$("#dataList").empty();
  		loadList();
  	});
	
	//搜索提交
	$("#querySalary").click(function(){
  		gotoPage(1);
  	});
  	
  	//导入
	$("#impSalary").click(function(){
  		alert();
  	});
  	
  	//回车搜索
	$.EnterKeyPress($("#month"),$("#querySalary"));
	$.EnterKeyPress($("#userkeyword"),$("#querySalary"));
	$.EnterKeyPress($("#employeeno"),$("#querySalary"));
  	
  	gotoPage(1);
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

//-------------------加载查询列表-------------------
function loadList(){
	
	 //分页
  	var pageno=$.trim($("#hidNumPage").val());
  	var pagecount=$.trim($("#hidPageCount").val());
  	var month = yearSel.getValue() + monthSel.getValue();
	
	$.ajax({
        type : "POST",
		cache: false,
		url  : "m/my_salary",
		data : {
			act:"query",
			pageno:pageno,
			pagecount:pagecount,
			month:month
		},
        success : function(xml){
            //解析XML中的返回代码
            var message = $("message",xml);
            if(message.attr("code") == "1"){
            	var tBodyHTML = "";
	            $("salary-bill",xml).each(
	                    function(index){
	                        var salaryBill = $(this);
				    			tBodyHTML += createTR(
				    					(index + 1),
				    					$(salaryBill).attr("id"),
				    					$("month",salaryBill).text(),
				    					$("user-account",salaryBill).text(),
				    					$("user-name",salaryBill).text(),
				    					$("employee-number",salaryBill).text(),
				    					$("post",salaryBill).text(),
				    					$("wage-total",salaryBill).text(),
				    					$("wage-basic",salaryBill).text(),
				    					$("wage-post",salaryBill).text(),
				    					$("wage-attendance",salaryBill).text(),
				    					$("wage-real",salaryBill).text()
				    			);
//				    		}
	                    }
	             );
	            $("#dataList").html(tBodyHTML);
                //------------------翻页数据--------------------------
	            $(".pageNext").html($.createTurnPage(xml));
	            $.EnterKeyPress($("#numPage"),$("#numPage").next());
            };
        },
        error : $.ermpAjaxError
    });
}

//创建TR
function createTR(index,id,month,userAccount,userName,employeeNumber,post,wageTotal,wageBasic,wagePost,wageAttendance, wageReal){
	var html = "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" 
		 + "<td>" + index + "</td>"
		 + "<td>" + month + "</td>"
		 + "<td>" + userAccount + "</td>"
		 + "<td>" + userName + "</td>"
		 + "<td>" + employeeNumber + "</td>"
		 + "<td>" + post + "</td>"
		 + "<td>" + wageTotal + "</td>"
		 + "<td>" + wageBasic + "</td>"
		 + "<td>" + wagePost + "</td>"
		 + "<td>" + wageAttendance + "</td>"
		 + "<td>" + wageReal + "</td>"
		 + "<td>" + ( $.hasRight($.SysConstants.VIEW)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"view('" + id + "')\">查看</a> " : "")
	     + "</td></tr>";
    return html;
}

//查看详情
function view(id){
	mainFrame.addTab({
		id:"oa_salary_view_"+id,
		title:"查看信息",
		url:BASE_PATH + "m/salary_bill?act=view&id=" + id
	});
}
