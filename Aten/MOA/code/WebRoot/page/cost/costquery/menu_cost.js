var mainFrame = $.getMainFrame();
$(initParameterManage);

//初始化页面--参数设置
function initParameterManage(){
    $("#costsNav li:first").addClass("current");
		$("#costsNav li").hover(function(){
		   $(this).addClass("over");
		},function(){
		   $(this).removeClass("over");
	}).click(function(){
		if ($(this).attr("target") != "_new") {
	       $(this).addClass("current").siblings().removeClass("current"); 
	    }
	    
	});
	//添加权限约束
    $.handleRights({
        "CostListSearchCw" : "bud_fymxcw",
        "ExpenseAccountSearchCw" : "bud_bxdcxcw"
    },"hidModuleMenus");
	
	
	$("#menuList").find("li").each(function(){
		$(this).click(function(){
	       if($(this).attr("class")=="current over"){
	         return;
	       }else{
	       		if ($(this).attr("target") == "_new") {
	       			//打开报表页
	       			openReport($(this).attr("url"), $(this).text());
	       			return;
	       		}
	         	$.ajax({
			        type : "POST",
					cache: false,
					url  : $(this).attr("url"),
			        success : function(txt){
			        	$("#costsCon").html(txt);
			     	}
        		});
	       }
	    });
	});
	//打开第一个
	$("#menuList").find("li").eq(0).click();

}

function openReport(url,txtName) {
	mainFrame.addTab({
		id:"oa_bud_lsnd_budget_reportforms"+Math.floor(Math.random() * 1000000),
		title:txtName,
		url:BASE_PATH + url
	});
}