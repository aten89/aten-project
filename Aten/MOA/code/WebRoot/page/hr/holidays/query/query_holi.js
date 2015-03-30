var mainFrame = $.getMainFrame();

$(initVacationApprovalArch);

function initVacationApprovalArch() {
	$.handleRights({
        "query" : $.SysConstants.QUERY,
        "export" : $.SysConstants.EXPORT
    });
	//刷新
  	$("#refresh").click(function(){
  		queryPage();
  	});
  	
  	//查询
  	$("#query").click(function(){
  		gotoPage(1);
  	});
  	
  	//导出
  	$("#export").click(function(){
  		exportExcel();
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
	var bgnQueryDate = $.trim($("#bgnQueryDate").val());
	var endQueryDate = $.trim($("#endQueryDate").val());
	
	$.ajax({
	   type : "POST",
	   cache: false,
	   async : true,
	   url  : "m/holi_query",
	   data : "act=query&pageno="+pageno+"&pagecount="+pagecount+"&applicant="+applicant+"&bgnquerydate="+bgnQueryDate+"&endquerydate="+endQueryDate,             
	   success : function (xml){
	   		var message = $("message",xml);
			var content = $("content",xml);
			var listDate = "";
			//是否有数据返回
		    if (message.attr("code") == "1") {  	    	  
		    	$(xml).find('holiday-detail').each(function(index){
		    		var curELe = $(this);
                    var hpplyId = curELe.attr("hpply-id");
                    listDate += " <tr onmouseout=\"$(this).removeClass('over');\" onmouseover=\"$(this).addClass('over');\">"
							+ "<td>" + hpplyId + "</td>"
							+ "<td>" + $("apply-account",curELe).text() + "</td>"
							+ "<td>" + $("dept",curELe).text() + "</td>"
							+ "<td>" + $("regional",curELe).text() + "</td>"
							+ "<td>" + $("holiday-name",curELe).text() + "</td>"
							+ "<td>" + $("holiday-date",curELe).text() + "</td>"
							+ "<td>" + $("days",curELe).text() + "</td>"
							+ "<td>" + $("cancel-days",curELe).text() + "</td>"
							+ "<td>" + $("remark",curELe).text() + "</td>"
							+ "<td>" + ( $.hasRight($.SysConstants.VIEW)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"initView('" + hpplyId + "')\">详情</a>" : "");	
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
		url:BASE_PATH + "/m/holi_query?act=view&id="+id
	});
}

function exportExcel() {
	var applicant = $.trim($("#applicant").val());
	var bgnQueryDate = $.trim($("#bgnQueryDate").val());
	var endQueryDate = $.trim($("#endQueryDate").val());
	$.ajax({type:"POST",
		cache:false, 
		url:"m/holi_query", 
		data : "act=export&applicant="+applicant+"&bgnquerydate="+bgnQueryDate+"&endquerydate="+endQueryDate,   
        success : function(xml){
            //解析XML中的返回代码
            var message = $("message",xml);
            if(message.attr("code") == "1"){
            	$.openDownloadDialog(BASE_PATH + $("message",xml).text());
            }
            else{
               $.alert($("message",xml).text());
            };
        }
    });
}

