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
}

function commitDispose(){
	var staffid = $("#staffid").val();
	
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
    saveFlowNotify($("#staffid").val());
    
	$.ajax({
       type : "POST",
       cache: false,
       url  : "m/staff_deal",
       data : {act:"deal_addacc",
       			tiid:taskid,
       			transition:transitionName,
       			comment:comment,
       			id:staffid
       		},
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
