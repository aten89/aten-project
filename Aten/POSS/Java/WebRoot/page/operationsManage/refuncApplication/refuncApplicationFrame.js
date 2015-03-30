/**********************
 客户回访管理
 *********************/
$(initRefuncApplication);
function initRefuncApplication() {
		//添加权限约束
	$.handleRights({
    	"refuncNotice" : "REFUND_NOTICE",
        "refuncApply1" : "REFUND_TODO",
//        "refuncApply2" : "REFUND_TODO",
        "refuncApply3" : "REFUND_TODO",
        "refuncApply4" : "REFUND_TRACK",
        "refuncApply5" : "REFUND_ARCH"
    },"FrameHidModuleRights");
    
	/*左边导航*/
	$("#crmSubNav li:first").addClass("current");
	$("#crmSubNav li").hover(function(){
	   		$(this).addClass("over");
		},function(){
	   		$(this).removeClass("over");
	}).click(function(){
	    $(this).addClass("current").siblings().removeClass("current");
		$("#crmFrameHead").text($(this).text());
	});
	$("#menuList").find("li").each(function(i){								
		$(this).click(function(){
			if($(this).attr("class")=="current over"){
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
	
	$("#menuList").find("li").eq(0).click();
};

