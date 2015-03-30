//参数设置 -- 框架
$(initEquipmentInfo);

//初始化页面--参数设置
function initEquipmentInfo(){
    //添加权限约束
    $.handleRights({
        "deviceProperty" : "dev_prop",
        "deviceClass" : "device_class",
        "areaCodeDev" : "device_areacfg",
        "checkOption" : "device_check_item",
        "assginMamager" : "device_assgin"
    },"hidModuleMenus");
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

    