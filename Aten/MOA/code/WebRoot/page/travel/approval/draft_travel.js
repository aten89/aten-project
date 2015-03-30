/**********************
出差管理--出差审批单
*********************/ 
var mainFrame = $.getMainFrame();
$(initTripApproval);
function initTripApproval(){
	//打开用户帐号
	$("#openAppointToSelect").click(
	   	function(e){
			var dialog = new UserDialog(ERMP_PATH, BASE_PATH);
			dialog.setCallbackFun(function(user){
				if (user != null) {
					$("#appointTo").val(user.id);
					$("#appointToName").val(user.name);
				}
			});
			dialog.openDialog("single");
		}
	);
	
	//新增明细
    $("#tripAdd").click(function(){
    	addTravel();
    });
	
	//提交开始流程
    $("#startFlow").click(function(){
    	saveDraft(true);
    });
    //保存为草稿
    $("#saveAsDraft").click(function(){
    	saveDraft(false);
    });
}


//提交或保存草稿
function saveDraft(flag){	
	var borrowSum = $("#borrowSum").val();;
	if (isNaN(borrowSum)) {
		$.alert("差旅借款只能为数字！");
		$("#borrowSum").focus();
		return;
	}
	//提交之前，判断有效性
	if (tripInfos.length==0){
		$.alert("您尚未填写出差信息，请详细填写！");
		return;
	}
	
	var trip = {};
	var tripid = $("#tripid").val();
   	if (tripid) {
   		trip.id = tripid;
   	}
	//将表单信息填写到请假单对象属性
	trip.regional = $("#regional").val();
	trip.applyDept = $("#groupname").val();
	trip.appointTo = $("#appointTo").val();
	trip.borrowSum = borrowSum;
	trip.termType = $("input[name='termType'][@checked]").val();
	
	trip.busTripApplyDetail = tripInfos;
	var jsonStr = getJsonStr(trip);
	
	/*
	将生成的出差单对象转换成XML，并提交给后台进行保存
	*/
	$("#startFlow,#saveAsDraft").attr("disabled","true");
	
	$.ajax({
       type : "POST",
       cache: false,
       url  : "m/trip_start?act=add" + (flag ? "&flag=Y" : ""),
       data : {json:jsonStr},
       success : function(xml){
           var messageCode = $("message",xml).attr("code");
           if(messageCode == "1"){
              $.alert($("message",xml).text(), function(){
	                //刷新父列表
	               mainFrame.getCurrentTab().doCallback();
	               //关闭
	               mainFrame.getCurrentTab().close();
               });
           } else {
               $.alert($("message",xml).text());
               //设置按钮不可用
				$("#startFlow,#saveAsDraft").removeAttr("disabled");
           }
       },
       error : $.ermpAjaxError
   	});
};
