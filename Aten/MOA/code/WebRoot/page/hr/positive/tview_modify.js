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
	var posiid = $("#posiid").text();
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
;
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
    saveFlowNotify(posiid);
           		
	$.ajax({
       type : "POST",
       cache: false,
       url  : "m/posi_deal",
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