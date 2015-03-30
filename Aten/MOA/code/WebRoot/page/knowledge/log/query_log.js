$(initPage);

function initPage(){
    //搜索
	$("#lowSearch").click(function(){
	 	gotoPage(1);
	});
	$("#flashBtn").click(function(){
	 	gotoPage(1);
	});
	//打开用户帐号
    $('#openUserSelect').click(function(e){
		var dialog = new UserDialog(ERMP_PATH, BASE_PATH);
		dialog.setCallbackFun(function(user){
			if (user != null) {
				$("#draftsman").val(user.id);
			$("#draftsmanName").val(user.name);
			}
		});
		dialog.openDialog();
	});
	
	$.EnterKeyPress($("#knowledgetitle"),$("#lowSearch"));
	$.EnterKeyPress($("#draftsmanName"),$("#lowSearch"));
	$.EnterKeyPress($("#beginOperatetime"),$("#lowSearch"));
	$.EnterKeyPress($("#endOperatetime"),$("#lowSearch"));
	gotoPage(1);//进入页面先进行一次搜索
};
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
}
function queryList(){
	$("#logList").empty();
	var pageno=$.trim($("#hidNumPage").val());
	var pagecount=$.trim($("#hidPageCount").val());
	var userid = $.trim($("#draftsman").val());
	var knowledgetitle = $.trim($("#knowledgetitle").val());
	var beginOperatetime = $.trim($("#beginOperatetime").val());
	var endOperatetime = $.trim($("#endOperatetime").val());
	if($.validInput("draftsmanName", "姓名", false, "\<\>\'\"")){
  		return false;
  	}
	if($.validInput("knowledgetitle", "知识点标题", false, "\<\>\'\"")){
  		return false;
  	}
	if(	$.compareDate(beginOperatetime,endOperatetime)){
		$.alert("开始时间不能大于结束时间！");
		return false;
	};		
	$.ajax({
        type : "POST",
		cache: false,
		url  : "m/kb_log",
		dataType : "xml",
		data:{
			act:"query",
			pageNo:pageno,
			pageSize:pagecount,
			userid:userid,
			knowledgetitle:knowledgetitle,
			beginOperatetime:beginOperatetime,
			endOperatetime:endOperatetime
		},
        success : function(xml){
            //解析XML中的返回代码
            var message = $("message",xml);
            if(message.attr("code") == "1"){
            	var tBodyHTML="";
	            $("knowledgeLog",xml).each(function(index) {
	            	tBodyHTML += "<tr >" +
				           		 "<td class=\"listData\">"+$("userName",$(this)).text()+"</td>" +
				           		 "<td class=\"listData\">"+$("type",$(this)).text()+"</td>" +
				           		 "<td class=\"listData\">"+$("operatetime",$(this)).text()+"</td>" +
				           		 "<td class=\"listData\">"+$("knowledgetitle",$(this)).text()+"</td>" +
				           		 "</tr>";
        		});
				
	            $("#logList").html(tBodyHTML);
                //------------------翻页数据--------------------------
	            $(".pageNext").html($.createTurnPage(xml));
	            $.EnterKeyPress($("#numPage"),$("#numPage").next());
        	}
        },
        error : $.ermpAjaxError
    });
}
