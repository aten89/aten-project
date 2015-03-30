var mainFrame = $.getMainFrame();
var holidayApply = null;

$(initVacationApprovalAdd);
function initVacationApprovalAdd(){
	
	//打开用户帐号
	$("#openAppointToSelect").click(
	   	function(e){
			var dialog = new UserDialog(ERMP_PATH, BASE_PATH);
			dialog.setCallbackFun(function(user){
				if (user != null) {
					$("#appointTo").val(user.id);
					$("#appointToName").val(user.name);
					$("#appointToName").focus();
				}
			});
			dialog.openDialog("single");
		}
	);
	//新增明细
    $("#holiAdd").click(function(){
    	addHoliday();
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

function displayReason(){
	if (document.getElementById("isSpecial").checked){
		$("#specialReasonPanel").show();
	}else{
		$("#specialReasonPanel").hide();
	}
};

//提交或保存草稿
function saveDraft(flag){
	
	var isSpecial = document.getElementById("isSpecial").checked;
	
	if (isSpecial && $.trim($("#specialReason").val())==""){
		$.alert("您申请了特批，请填写特批理由！");
		$("#specialReason").focus();
		return;
	}
	if (holiInfos.length==0){
		$.alert("您尚未填写请假信息，请详细填写！");
		return;
	}
	var holi = {};
	var holiid = $("#holiid").val();
   	if (holiid) {
   		holi.id = holiid;
   	}
	//将表单信息填写到请假单对象属性
	holi.regional = $("#regional").val();
	holi.applyDept = $("#groupname").val();
	holi.appointTo = $("#appointTo").val();
	holi.isSpecial = isSpecial;
	holi.specialReason = isSpecial?$.trim($("#specialReason").val()):"";
	holi.holidayDetail = holiInfos;
	var jsonStr = getJsonStr(holi);


	/*
	将生成的请假单对象转换成XML，并提交给后台进行保存
	*/
	$("#startFlow,#saveAsDraft").attr("disabled","true");
	
	$.ajax({
       type : "POST",
       cache: false,
       url  : "m/holi_start?act=add" + (flag ? "&flag=Y" : ""),
       data : {json:jsonStr},
       success : function(xml){
           var messageCode = $("message",xml).attr("code");
           if(messageCode == "1"){
           		//添加知会人
           		saveFlowNotify($("message",xml).text());
           		
               $.alert("保存成功", function(){
	                //刷新父列表
	               mainFrame.getCurrentTab().doCallback();
	               //关闭
	               mainFrame.getCurrentTab().close();
               });
              
           } else {
               $.alert($("message",xml).text());
               //设置按钮不可用
           }
           $("#startFlow,#saveAsDraft").removeAttr("disabled");
       },
       error : $.ermpAjaxError
   	});
};
