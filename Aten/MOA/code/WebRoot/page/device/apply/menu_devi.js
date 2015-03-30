//离职设备--框架
$(initEquipmentInfo);

//初始化页面--参数设置
function initEquipmentInfo(){
	$.handleRights({
        "myuse" : "device_myuse",
        "start" : "device_start",
        "todo" : "device_deal",
        "track" : "device_track",
        "arch" : "device_arch"
    },"hidModuleMenus");
    
	/*
     //添加权限约束
    $.handleRights({
    	"deal":"device_myuse",//我名下的设备
    	"start":"device_start",//起草
    	"track":"device_track",//跟踪
    	"arch":"device_arch",//归档
        "todo" : "device_deal"//待办
    },"hidModuleMenus");
    */
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

    