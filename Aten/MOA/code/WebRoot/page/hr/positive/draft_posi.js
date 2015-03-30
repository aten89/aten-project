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
					$("#positiveUser").text(user.id);
					$("#positiveUserName").val(user.name);
					//自动加载入职申请单中的信息
					loadEntryInfo(user.id);
				}
			});
			dialog.openDialog("single");
		}
	);
	
	//打开部门
	$("#openDeptSelect").click(
	   	function(e){
			var dialog = new DeptDialog(ERMP_PATH, BASE_PATH);
			dialog.setCallbackFun(function(retVal){
				if (retVal != null) {
					$("#dept").val(retVal.name);
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
			$("#dept").val($("dept",curELe).text());
			$("#post").val($("post",curELe).text());
			$("#entryDate").val($("entry-date",curELe).text());
			$("#contractStartDate").val($("contract-start-date",curELe).text());
			$("#contractEndDate").val($("contract-end-date",curELe).text());
			$("input[name='sexOption'][value='" + $("sex",curELe).text() + "']").attr("checked",true);
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
	var posiid = $("#posiid").val();
   	if (posiid) {
   		tran.id = posiid;
   	}
	//将表单信息填写到请假单对象属性
	tran.positiveUser = $("#positiveUser").text();
	tran.dept = $("#dept").val();
	tran.entryDate = $("#entryDate").val();
	tran.post = $("#post").val();
	tran.sex = $("input[name='sexOption'][@checked]").val();
	tran.probation = $("#probation").val();
	tran.formalDate = $("#formalDate").val();
	tran.formalType = $("input[name='formalTypeOption'][@checked]").val();
	tran.workResults = $("#workResults").val();
	tran.cultureUnderstand = $("#cultureUnderstand").val();
	tran.rulesCompliance = $("#rulesCompliance").val();
	tran.workExperience = $("#workExperience").val();
	tran.workSummary = $("#workSummary").val();
	tran.workImprove = $("#workImprove").val();

	var jsonStr = $.toJSON(tran);


	/*
	将生成的请假单对象转换成XML，并提交给后台进行保存
	*/
	$("#startFlow,#saveAsDraft").attr("disabled","true");
	
	$.ajax({
       type : "POST",
       cache: false,
       url  : "m/posi_start?act=add" + (flag ? "&flag=Y" : ""),
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
