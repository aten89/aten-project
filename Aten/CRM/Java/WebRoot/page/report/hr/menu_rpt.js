$(initVacation);

//初始化页面--参数设置
function initVacation(){
	//权限控制
    $.handleRights({
        "GRTJ" : "GRTJ",
        "YXRL" : "YXRL",
        "TDYXRL" : "TDYXRL"
    },"hidModuleMenus");
	//添加权限约束
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

//文件名编码
function getDownloadFilename(filename){
	if (navigator.userAgent.indexOf("MSIE") > 0 
			// IE11:Mozilla/5.0 (Windows NT 6.1; Trident/7.0; rv:11.0) like Gecko
			|| navigator.userAgent.indexOf("Trident") > 0) {
		return encodeURI(encodeURI(filename));
	} 
	return encodeURIComponent(escapeToMime(filename, "base64", "UTF-8"));
}
    