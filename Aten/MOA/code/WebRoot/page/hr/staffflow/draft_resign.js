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
					$("#userAccountID").val(user.id);
					$("#userName").val(user.name);
//					$("#groupName").val(user.deptName);
					//自动加载入职申请单中的信息
					loadEntryInfo(user.id);
				}
			});
			dialog.openDialog("single");
		}
	);
	
	//打开用户帐号
	$("#openDeptSelect").click(
	   	function(e){
			var dialog = new DeptDialog(ERMP_PATH, BASE_PATH);
			dialog.setCallbackFun(function(retVal){
				if (retVal != null) {
//					$("#deptids").val(retVal.id);
					$("#groupName").val(retVal.name);
					$("#groupFullName").text(retVal.path);
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
    
    var selVal = $("#hidStaffClass").val();
    if (selVal) {
    	$("#staffClass").val(selVal);
    }
    selVal = $("#hidResignType").val();
    if (selVal) {
    	$("#resignType").val(selVal);
    }
    selVal = $("#hidResignReason").val();
    if (selVal) {
    	$("#resignReason").val(selVal);
    }
}

//自动加载入职申请单中的信息
function loadEntryInfo(userAccountId) {
	$.ajax({
	   type : "POST",
	   cache: false,
	   async : true,
	   url  : "m/staff_start",
	   data : "act=entryinfo&userAccountID="+userAccountId,             
	   success : function (xml){
			var curELe = $("staff-flow",xml).eq(0);
//			if (contents.size() > 0) {
//				var curELe = contents.eq(0);
				var area = $("company-area",curELe).text();
				if (area) {
					$("#companyArea").val(area);
				}
				$("#post").val($("post",curELe).text());
				$("#entryDate").val($("entry-date",curELe).text());
//				$("#birthdate").val($("birthdate",curELe).text());
				$("#employeeNumber").val($("employee-number",curELe).text());
//				$("input[name='sexOption'][value='" + $("sex",curELe).text() + "']").attr("checked","checked");
				$("#groupName").val($("dept",curELe).text());
				$("#groupFullName").text($("dept-full-name",curELe).text());
//			}
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
	
	var staff = {};
	var staffid = $("#staffid").val();
   	if (staffid) {
   		staff.id = staffid;
   	}
	//将表单信息填写到请假单对象属性
   	staff.applyType = $("#applyType").val();
   	staff.userAccountID = $("#userAccountID").val();
	staff.employeeNumber = $("#employeeNumber").val();
	staff.userName = $("#userName").val();
	staff.companyArea = $("#companyArea").val();
	staff.groupName = $("#groupName").val();
	staff.groupFullName = $("#groupFullName").text();
	staff.post = $("#post").val();
	staff.entryDate = $("#entryDate").val();
	staff.resignDate = $("#resignDate").val();
	
	staff.project = $("#project").val();
	staff.tranStartDate = $("#tranStartDate").val();
	staff.tranEndDate = $("#tranEndDate").val();
	staff.tranCost = $("#tranCost").val();
	staff.penalty = $("#penalty").val();
	staff.staffClass = $("#staffClass").val();
	staff.achievement = $("#achievement").val();
	staff.resignType = $("#resignType").val();
	staff.resignReason = $("#resignReason").val();
	staff.resignDesc = $("#resignDesc").val();
	staff.workExperiences = [];
	
	var jsonStr = $.toJSON(staff);
	/*
	将生成的请假单对象转换成XML，并提交给后台进行保存
	*/
	$("#startFlow,#saveAsDraft").attr("disabled","true");
	
	$.ajax({
       type : "POST",
       cache: false,
       url  : "m/staff_start?act=add" + (flag ? "&flag=Y" : ""),
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
