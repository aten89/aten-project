var mainFrame = $.getMainFrame();

//父页面传过来的参数信息
var params = parent.window.dialogParam;

$(initPage);
function initPage(){
	
	$("#saveBtn").click(function(){
		addConsultRecord();
	});
	$("#doclose").click(function(){
		params.returnValue = false;
		params.callback();
	});
	
	if(params.content){
		$("#consultContent").val(decodeURI(params.content));
	}
}


function addConsultRecord(){
	var consultContent = $("#consultContent").val();
	if($.validInput("consultContent", "咨询内容", false, "\<\>\'\"", 512)){
		return false;
	}
	var customerId = params.customerId;
	var id = "";
	if(params.id){
		id = params.id;
	}
	if(customerId==null || customerId==""){
		alert("请选择客户");
		return;
	}
	
	$.ajax({
        type : "POST",
		cache: false,
		async: true,
		url : BASE_PATH + "/m/customer/addConsultRecord",
		dataType : "json",
		data:{
			customerId : customerId,
			consultContent : consultContent,
			customerConsultId : id
		},
        success : function(data){
        	if ($.checkErrorMsg(data)) {
        		params.returnValue = true;
        		params.callback();
	    	} 
        },
        error : $.ermpAjaxError
    });
	
}
