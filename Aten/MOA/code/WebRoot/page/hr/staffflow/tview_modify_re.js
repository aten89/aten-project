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
				$("#groupName").val($("dept",curELe).text());
				$("#groupFullName").text($("dept-full-name",curELe).text());
//			}
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
	
	var taskid = $("#taskInstanceID").val();
	var transitionName = $("#transitionName").val();
	if (transitionName == "") {
		$.alert("请选择转向");
		return;
	}
	//设置按钮不可用
	$("#commitBut").find(".allBtn").attr("disabled","true");
	//添加知会人
    saveFlowNotify($("#staffid").val());
    
	$.ajax({
       type : "POST",
       cache: false,
       url  : "m/staff_deal",
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