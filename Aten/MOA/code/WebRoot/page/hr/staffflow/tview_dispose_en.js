var mainFrame = $.getMainFrame();

$(initVacationApprovalHandle);

function initVacationApprovalHandle() {
	
//	$("#costsLog .mb").hide();
	//日志展开
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
  	
  	var val = $("#hidRecruitmentType").val();
  	if (val == "网络招聘") {
		$("#recommended").show();
		$("#recommendedTD").text("招聘人");
	} else if (val == "内部推荐") {
		$("#recommended").show();
		$("#recommendedTD").text("推荐人");
	} 
}

function hideTbody(flagId, dataId) {
	var flag = $("#" + flagId).text();
	if (flag == "-") {
		$("#" + flagId).text("+");
		$("#" + dataId).hide();
	} else {
		$("#" + flagId).text("-");
		$("#" + dataId).show();
	}
}

function commitDispose(){
	var transitionName = $("#transitionName").val();
	if (transitionName == "") {
		$.alert("请选择转向");
		return;
	}
	var taskid = $("#taskInstanceID").val();
	var comment =  $.trim($("#comment").val());
	//驳回修改 一定要写意见
	if (transitionName == "驳回修改" && comment=="") {
		$.alert("请填写驳回意见");
		$("#comment").focus();
		return;
	}
	if(comment.length > 500){
		$.alert("填写意见请不要超过500个字符");
		return;
	}
	var result = $.validChar(comment,"<>");
	if (result) {
		$.alert("审批意见不能包含非法字符：" + result);
		$("#comment").focus();
		return;
	}
	//设置按钮不可用
	$("#commitBut").find(".allBtn").attr("disabled","true");
	//添加知会人
    saveFlowNotify($("#staffid").text());
    
	$.ajax({
       type : "POST",
       cache: false,
       url  : "m/staff_deal",
       data : {act:"deal_approve",
       			tiid:taskid,
       			transition:transitionName,
       			comment:comment},
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
           }
       },
       error : $.ermpAjaxError
   	});
}
