var mainFrame = $.getMainFrame();
var holidayApply = null;

$(initVacationApprovalAdd);
function initVacationApprovalAdd(){
	//打开用户帐号
   $("#openUserSelect").click(
	   	function(e){
			var dialog = new UserDialog(ERMP_PATH, BASE_PATH);
			dialog.setCallbackFun(function(user){
				if (user != null) {
					$("#transferUser").text(user.id);
					$("#transferUserName").val(user.name);
					//自动加载入职申请单中的信息
					loadEntryInfo(user.id);
				}
			});
			dialog.openDialog("single");
		}
	);
	//打开用户帐号
   $("#openFIUserSelect").click(
	   	function(e){
			var dialog = new UserDialog(ERMP_PATH, BASE_PATH);
			dialog.setCallbackFun(function(user){
				if (user != null) {
					$("#finaceSupportUser").val(user.id);
					$("#finaceSupportUserName").val(user.name);
					//自动加载入职申请单中的信息
					loadEntryInfo(user.id, 1);
				}
			});
			dialog.openDialog("single");
		}
	);
	//打开用户帐号
   $("#openITUserSelect").click(
	   	function(e){
			var dialog = new UserDialog(ERMP_PATH, BASE_PATH);
			dialog.setCallbackFun(function(user){
				if (user != null) {
					$("#itSupportUser").val(user.id);
					$("#itSupportUserName").val(user.name);
					//自动加载入职申请单中的信息
					loadEntryInfo(user.id, 2);
				}
			});
			dialog.openDialog("single");
		}
	);
	//打开用户帐号
   $("#openHRUserSelect").click(
	   	function(e){
			var dialog = new UserDialog(ERMP_PATH, BASE_PATH);
			dialog.setCallbackFun(function(user){
				if (user != null) {
					$("#hrSupportUser").val(user.id);
					$("#hrSupportUserName").val(user.name);
					//自动加载入职申请单中的信息
					loadEntryInfo(user.id, 3);
				}
			});
			dialog.openDialog("single");
		}
	);
	
	//打开部门
	$("#openOutDeptSelect").click(
	   	function(e){
			var dialog = new DeptDialog(ERMP_PATH, BASE_PATH);
			dialog.setCallbackFun(function(retVal){
				if (retVal != null) {
					$("#transferOutDept").val(retVal.name);
				}
			});
			dialog.openDialog("single");
		}
	);
	//打开部门
	$("#openInDeptSelect").click(
	   	function(e){
			var dialog = new DeptDialog(ERMP_PATH, BASE_PATH);
			dialog.setCallbackFun(function(retVal){
				if (retVal != null) {
					$("#transferInDept").val(retVal.name);
				}
			});
			dialog.openDialog("single");
		}
	);
	
	//提交开始流程
    $("#startFlow").click(function(){
    	saveDraft(true);
    });
    //保存为草稿
    $("#saveAsDraft").click(function(){
    	saveDraft(false);
    });
}

//自动加载入职申请单中的信息
function loadEntryInfo(userAccountId, type) {
	$.ajax({
	   type : "POST",
	   cache: false,
	   async : true,
	   url  : "m/staff_start",
	   data : "act=entryinfo&userAccountID="+userAccountId,             
	   success : function (xml){
			var curELe = $("staff-flow",xml).eq(0);
			if (type==1) {
				$("#finaceSupportEmail").val($("email",curELe).text());
				$("#finaceSupportTel").val($("mobile",curELe).text());
			} else if (type==2) {
				$("#itSupportEmail").val($("email",curELe).text());
				$("#itSupportTel").val($("mobile",curELe).text());
			} else if (type==3) {
				$("#hrSupportEmail").val($("email",curELe).text());
				$("#hrSupportTel").val($("mobile",curELe).text());
			} else {
				$("#transferOutDept").val($("dept",curELe).text());
				$("#transferOutPost").val($("post",curELe).text());
				$("#entryDate").val($("entry-date",curELe).text());
				$("#contractStartDate").val($("contract-start-date",curELe).text());
				$("#contractEndDate").val($("contract-end-date",curELe).text());
			}
	   }
	});
}

//提交或保存草稿
function saveDraft(flag){
	var result = true;
	$(".cRed").each(function(){
		var obj = $(this);
		var valid = obj.attr("valid");
		if (valid) {
			if($.validInput(valid, obj.parent().text(), true)){
				result = false;
				return false;
			}
		}
	});
	if (!result) {
		return;
	}
	
	var tran = {};
	var tranid = $("#tranid").val();
   	if (tranid) {
   		tran.id = tranid;
   	}
	//将表单信息填写到请假单对象属性
	tran.transferUser = $("#transferUser").text();
	tran.contractBody = $("#contractBody").val();
	tran.contractStartDate = $("#contractStartDate").val();
	tran.contractEndDate = $("#contractEndDate").val();
	tran.transferDate = $("#transferDate").val();
	tran.entryDate = $("#entryDate").val();
	tran.transferOutDept = $("#transferOutDept").val();
	tran.transferOutPost = $("#transferOutPost").val();
	tran.transferInDept = $("#transferInDept").val();
	tran.transferInPost = $("#transferInPost").val();
	tran.reportDate = $("#reportDate").val();
	tran.transferReason = $("#transferReason").val();
	tran.transitionContent = $("#transitionContent").val();
	tran.finaceSupportUser = $("#finaceSupportUser").val();
	tran.finaceSupportTel = $("#finaceSupportTel").val();
	tran.finaceSupportEmail = $("#finaceSupportEmail").val();
	tran.itSupportUser = $("#itSupportUser").val();
	tran.itSupportTel = $("#itSupportTel").val();
	tran.itSupportEmail = $("#itSupportEmail").val();
	tran.hrSupportUser = $("#hrSupportUser").val();
	tran.hrSupportTel = $("#hrSupportTel").val();
	tran.hrSupportEmail = $("#hrSupportEmail").val();
	tran.changeOffice = $("#changeOffice").val();
	tran.weeklyReportTo = $("#weeklyReportTo").val();
	tran.monthlyReportTo = $("#monthlyReportTo").val();
	var jsonStr = $.toJSON(tran);


	/*
	将生成的请假单对象转换成XML，并提交给后台进行保存
	*/
	$("#startFlow,#saveAsDraft").attr("disabled","true");
	
	$.ajax({
       type : "POST",
       cache: false,
       url  : "m/tran_start?act=add" + (flag ? "&flag=Y" : ""),
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
