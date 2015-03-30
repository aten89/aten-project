/**********************
潜在客户--框架页面
*********************/ 
$(initPendingMaintenance);
function initPendingMaintenance(){
	//添加权限约束
	$.handleRights({
    	"workorderLi" : "S_POTENTIAL_CUST",
        "workorderEvaluateLi" : "APPROVE_CUST"
    },"FrameHidModuleRights");
    
	/*左边导航*/
	$("#crmSubNav li:first").addClass("current");
	$("#crmSubNav li").hover(function(){
			$(this).addClass("over");
		},function(){
	   		$(this).removeClass("over");
	}).click(function(){
	    $(this).addClass("current").siblings().removeClass("current"); 
	    var liId = $(this).attr("id");
	    var title = $(this).text();
	    if($("#"+liId+"_desc").size()==1){
	    	title+=$("#"+liId+"_desc").html();
	    }
		$("#crmFrameHead").html(title);
	});
	$("#menuList").find("li").each(function(){
		$(this).click(function(){
			if($(this).attr("class")=="current over"){
	       		$("#refresh").click();
	         	return;
	       	}else{
	         	$.ajax({
			        type : "POST",
					cache: false,
					url  : $(this).attr("url"),
			        success : function(response){
			        	if(response != null && response.indexOf("{\"msg\":{\"code\":-1") > -1){
			        		var data;
			        		eval('data=' + response);
			        		alert(data.msg.text);
			        		return;
			        	}
			        	$("#costsCon").html(response);
			     	}
        		});
	       	}
	    });
	});
	$("#menuList").find("li").eq(0).click();
};



function openSubNav(obj,sub){
		$obj = $(obj);
		$("#menuList").find("[name=sub]").hide();
		for(var i=0;i<sub;i++){
				$obj.nextAll().eq(i).show();
		}
		$("#menuList").find("[name=first]").each(function(index){
				if($obj[0] == $("#menuList").find("[name=first]").eq(index)[0]){
						$("#menuList").find("[name=first]").eq(index).find("span").addClass("topLiOpen");
				}else{
						$("#menuList").find("[name=first]").eq(index).find("span").removeClass("topLiOpen");
					};
		});
}

