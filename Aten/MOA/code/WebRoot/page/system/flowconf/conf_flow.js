var mainFrame = $.getMainFrame();

$(initFlowCollocate);

function initFlowCollocate(){
    //左边导航选项，滑过的动作
    $("#costsNav li:first").addClass("current");
		$("#costsNav li").hover(function(){
		   $(this).addClass("over");
		},function(){
		   $(this).removeClass("over");
	}).click(function(){
	    $(this).addClass("current").siblings().removeClass("current"); 
	});
	//添加权限约束
    $.handleRights({
        "flowDraft" : "flow_draft",
        "IssueFlow" : "flow_man"
    },"hidModuleMenus");
	
	$("#menuList").find("li").each(function(){
		$(this).click(function(){
	       if($(this).attr("class")=="current over"){
	       		//已选中，刷新页面
	       		$("#refresh").click();
	         	return;
	       }else{
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
