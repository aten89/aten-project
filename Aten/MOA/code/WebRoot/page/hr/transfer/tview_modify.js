var mainFrame = $.getMainFrame();

var holidayApply = null;

$(initVacationApprovalAdd);
function initVacationApprovalAdd(){
	
 	$("#costsLog a").toggle(function(){
		$("#costsLog").find(".mb").hide();
		$("#costsLog h1").removeClass("logArrow");
	},function(){
		$("#costsLog").find(".mb").show();
		$("#costsLog h1").addClass("logArrow");
		
	});
	
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
	
	//提交
  	$("#commitBut").find(".allBtn").each(function(){
  		$(this).click(function(){
  			$("#transitionName").val($(this).val());
  			$.confirm("确认是否" + $(this).val() + "？", function(r){
				if (r) {
  					commitDispose();
				}
			});
  		});
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
			} 
	   }
	});
}


function commitDispose(){
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
	var tranid = $("#tranid").text();
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
	
	var taskid = $("#taskInstanceID").val();
	var transitionName = $("#transitionName").val();
	if (transitionName == "") {
		$.alert("请选择转向");
		return;
	}
	//设置按钮不可用
	$("#commitBut").find(".allBtn").attr("disabled","true");
	
	//添加知会人
    saveFlowNotify(tranid);
           		
	$.ajax({
       type : "POST",
       cache: false,
       url  : "m/tran_deal",
       data : {act:"deal_modify",
       			tiid:taskid,
       			transition:transitionName,
       			comment:"",
       			json:jsonStr},
       success : function(xml){
           var messageCode = $("message",xml).attr("code");
           var msg = $("message",xml).text();
           if(messageCode == "1"){
               $.alert($("message",xml).text(), function(){
	                //刷新父列表
	               mainFrame.getCurrentTab().doCallback();
	               //关闭
	               mainFrame.getCurrentTab().close();
               });
           } else {
               $.alert($("message",xml).text());
               //设置按钮可用
				$("#commitBut").find(".allBtn").removeAttr("disabled");
           }
       },
       error : $.ermpAjaxError
   	});
}