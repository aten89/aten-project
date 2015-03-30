$(initSkinCustom);

function initSkinCustom(){
	var currentStyleThemes = $("#currentStyleThemes").val();
	$("#" + currentStyleThemes).attr("checked","true");
  
  	$("#thremList").find("li").click(function(){
  		$(this).find("input").attr("checked",true);
  		
  		var threm = $(this).find("input").val();
		$.ajax({
	   		type : "POST",
	   		cache: false,
	    	async : true,
	   		url  : "l/frame/setskin",
	   		dataType : "json",
	   		data : {
	   			styleThemes : threm
	   		},
	   		success : function(data){
	   			if ($.checkErrorMsg(data) ) {
        			$.confirm("设置成功，需要重新登录才能申效，是否马上登出？", function(r){
						if (r) {
							parent.logoutSystem();//主页框架的登出所有系统
//							window.top.location = BASE_PATH + "p/comm/logout";
						}
					});
        		}
	  		}
		});
  	});
  	
  	
};