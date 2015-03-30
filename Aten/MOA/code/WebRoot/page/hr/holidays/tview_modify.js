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

function displayReason(){
	if (document.getElementById("isSpecial").checked){
		$("#specialReasonPanel").show();
	}else{
		$("#specialReasonPanel").hide();
	}
};

function commitDispose(){
	
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
	holi.id = $("#holiid").text();;
	//将表单信息填写到请假单对象属性
	holi.regional = $("#regional").val();
	holi.applyDept = $("#applyDept").text();
	holi.appointTo = $("#appointTo").val();
	holi.isSpecial = isSpecial;
	holi.specialReason = isSpecial?$.trim($("#specialReason").val()):"";
	holi.holidayDetail = holiInfos;
	var jsonStr = getJsonStr(holi);
	
	var taskid = $("#taskInstanceID").val();
	var transitionName = $("#transitionName").val();
	if (transitionName == "") {
		$.alert("请选择转向");
		return;
	}
	//设置按钮不可用
	$("#commitBut").find(".allBtn").attr("disabled","true");
	
	//添加知会人
    saveFlowNotify($("#holiid").text());
           		
	$.ajax({
       type : "POST",
       cache: false,
       url  : "m/holi_deal",
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