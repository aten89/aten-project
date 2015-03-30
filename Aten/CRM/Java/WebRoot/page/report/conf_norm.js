$(initPage);

//初始化页面
function initPage(){
    //搜索
    $("#saveConf").click(function(){
        	saveNorms();
     });   
}


//保存授权信息
function saveNorms() {
	var normStr = "";
	$("input", ".addCon").each(function(){
		var obj = $(this);
		normStr += obj.attr("id") + "=" + obj.val() + ";";
	});
	
	$.ajax({
	    type : "POST",
	    cache: false,
	    async : true,
	    dataType : "json",
	    url  : "m/report/modify_norm",
	    data :{
	    	rptID : $("#rptId").val(),
	    	normStr:normStr
	    	},
	    success : function(data){
	    	if ($.checkErrorMsg(data) ) {
					$.alert("保存成功！");
			}
	    }
	});
}