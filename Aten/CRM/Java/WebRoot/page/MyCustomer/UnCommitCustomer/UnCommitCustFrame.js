/**********************
未提交客户--框架页面
 *********************/
$(initUnCommitCustFrame);
function initUnCommitCustFrame(){
	//权限控制
	$.handleRights({
    	"company_allot" : "COMPANY_ALLOT",
        "my_draft" : "MY_DRAFT_CUST",
        "unpass_cust" : "UNPASS_CUST"
    },"FrameHidModuleRights");
	
	$("#costsNav li:first").addClass("current");
	$("#costsNav li").hover(function(){
		   $(this).addClass("over");
		},function(){
		   $(this).removeClass("over");
	}).click(function(){
	    $(this).addClass("current").siblings().removeClass("current"); 
	});

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
