var mainFrame = $.getMainFrame();
$(initPage);
function initPage(){
	
	//信息内容块切换	
	$("#crmTab li").each(function(i){
		$(this).click(function(){
			$(this).addClass("current").siblings().removeClass("current");
			$("#tab0" + i).show().siblings().hide();
			loadStaff(i);
		});	
	}) ;
	//选中第一个
	$("#crmTab li:first-child").click();
	
	//设置固定宽度
	$(".gnmkBk,#crmTab").width($(".boxShadow").width());
	$(".gnmkBk").height($(window).height()-40);
}

function loadStaff(i){
	var act = "query_birth";
	if (i == 0) {
		act = "query_contr";
	} else if (i == 1) {
		act = "query_formal";
	}
	
	$.ajax({
        type : "POST",
		cache: false,
		url  : "m/staff_prompt",
		data : "act=" + act + "&pageno=1&pagecount=5",
        success : function (xml){
	   		var message = $("message",xml);
			var content = $("content",xml);
			//是否有数据返回
		    if (message.attr("code") == "1") {
		    	var listDate = "";
		    	$(xml).find('staff-flow').each(function(index){
		    		var curELe = $(this);
                    var id = curELe.attr("id");
                    if (i == 0) {
                    	listDate += "<li> <a href=\"javascript:initView('" + id + "');\">距离<b>" + $("userName",curELe).text() + "</b>合同到期还有<b>" + $("statDays",curELe).text() + "</b>天</a></li>"
                   	} else if (i == 1) {
                   		listDate += "<li> <a href=\"javascript:initView('" + id + "');\">距离<b>" + $("userName",curELe).text() + "</b>转正还有<b>" + $("statDays",curELe).text() + "</b>天</a></li>"
                   	} else {
                   		listDate += "<li> <a href=\"javascript:initView('" + id + "');\">距离<b>" + $("userName",curELe).text() + "</b>生日（" + $("birthdate",curELe).text() + "）还有<b>" + $("statDays",curELe).text() + "</b>天</a></li>"
                   	}
				});
				$("#dataList" + i).html(listDate)
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
        error : $.ermpAjaxError
    });
}

/**
 * 查看
 * @param id
 */
function initView(id) {
	mainFrame.addTab({
		id:"oa_staff_arch_viewarch_"+id,
		title:"查看员工信息",
		url:BASE_PATH + "/m/staff_query?act=view&id="+id
	});
}