//it设备
$(initEquipmentInfo);

//初始化页面--参数设置
function initEquipmentInfo(){
	var deviceTypeCode=$("#deviceTypeCode").val();
    //添加权限约束
	var fuzhouKey="";
	var shhKey="";
	var xiamenKey="";
	if(deviceTypeCode=="DEVICE-IT"){
		fuzhouKey="device_it_fuzhou";
		xiamenKey="device_it_xiamen";
		shhKey="device_it_shh";
	} else if(deviceTypeCode=="DEVICE-OFFICE"){
		fuzhouKey="device_office_fuzh";
		xiamenKey="device_office_xm";
		shhKey="device_office_shh";
	} else if(deviceTypeCode=="FIXED-ASSETS"){
		fuzhouKey="device_assets_fuzh";
		xiamenKey="device_assets_xm";
		shhKey="device_assets_shh";
	} else if(deviceTypeCode=="DEVICE-MEET"){
		fuzhouKey="device_meet_fuzh";
		xiamenKey="device_meet_xm";
		shhKey="device_meet_shh";
	} else if(deviceTypeCode=="DEVICE-LOW"){
		fuzhouKey="device_low_fuzh";
		xiamenKey="device_low_xm";
		shhKey="device_low_shh";
	} else if(deviceTypeCode=="DEVICE-OTHERS"){
		fuzhouKey="device_others_fuzh";
		xiamenKey="device_others_xm";
		shhKey="device_others_shh";
	}
//    $.handleRights({
//        "fuzhou" : fuzhouKey,
//        "xiamen" : xiamenKey,
//        "shanghai" : shhKey
//    },"hidModuleMenus");
    
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

    