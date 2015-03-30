/**********************
合同评审--框架页面
*********************/ 

$(initContractReview);
function initContractReview(){
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
	$("#menuList").find("li").each(function(){
		$(this).click(function(){
			if($(this).attr("class")=="current over"){
	       		$("#refresh").click();
	         	return;
	       	}else{
	       		var url = $(this).attr("url");
	       		if (!url) {
	       			$("#costsCon").empty();
	       			return;
	       		}
	         	$.ajax({
			        type : "POST",
					cache: false,
					url  : url,
			        success : function(txt){
			        	$("#costsCon").html(txt);
			     	}
        		});
	       	}
	    });
	});
	$("#menuList").find("li").eq(0).click();
	
};
