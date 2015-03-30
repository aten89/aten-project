
$(initProcess);
function initProcess(){
	//添加权限约束
	$.handleRights({
    	"paymentReg1" : "CUST_PAY_TODO",
    	"paymentReg2" : "CUST_PAY_TODO",
    	"paymentReg3" : "CUST_PAY_TRACK",
    	"paymentReg4" : "CUST_PAY_ARCH",
    	"paymentReg5" : "CUST_PAY_QUERY",
        "nsPaymentReg1" : "FBYY_TODO",
        "nsPaymentReg2" : "FBYY_TODO",
        "nsPaymentReg3" : "FBYY_TRACK",
        "nsPaymentReg4" : "FBYY_ARCH",
        "nsPaymentReg5" : "FBYY_QUERY"
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







