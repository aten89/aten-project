var mainFrame = $.getMainFrame();

$(initReimburseDrfat);

function initReimburseDrfat(){
   	//添加费用明细
    $("#costsAdd").click(function(){
    	addGroup();
    });
    
    //提交开始流程
    $("#startFlow").click(function(){
    	saveDraft(true);
    	
    });
    //保存为草稿
    $("#saveAsDraft").click(function(){
    	saveDraft(false);
    });
    
    //打开用户帐号
   $("#openUserSelect").click(function(e){
			var dialog = new UserDialog(ERMP_PATH, BASE_PATH);
			dialog.setCallbackFun(function(user){
				if (user != null) {
					$("#payee").val(user.id);
					$("#payeeName").val(user.name);
					$("#payeeName").focus();
				}
			});
			dialog.openDialog("single");
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
};

function saveDraft(startFlow) {
	if (isNaN($("#loansum").val())) {
		$.alert("预借款额只能为数字！");
		$("#loansum").focus();
		return;
	}
	if($.trim($("#payee").val()) == ""){
		$.alert("受款人不能为空！");
		$("#payee").focus();
		return false;
	} else if($.trim($("#causa").val()).length > 1000) {
    	$.alert("事由不能超过1000个字符");
    	$("#cause").focus();
    	return false;
    }
    if (reimItemDrafts.length ==0) {
    	$.alert("请添加费用明细！");
    	return false;
    }
   	var reim = {};
   	var reimid = $("#reimid").val();
   	if (reimid) {
   		reim.id = reimid;
   	}
   	reim.finance = $("#finance").val();
   	reim.payee = $("#payee").val(); 
	reim.causa = $("#causa").val(); 
	reim.loanSum = $("#loansum").val() ; 
	reim.reimbursementSum =  $("#outlaySumShow").text() ; 
	reim.appointTo = $("#appointTo").val();
	reim.applyDept = $("#groupname").val();
	var jsonStr = getJsonStr(reim);
//	$.alert(jsonStr);
	//设置按钮不可用
	$("#startFlow,#saveAsDraft").attr("disabled","true");
	$.ajax({
       type : "POST",
       cache: false,
       url  : "m/rei_start?act=add" + (startFlow ? "&flag=Y" : ""),
       data : {json : jsonStr},
       success : function(xml){
           var messageCode = $("message",xml).attr("code");
           if(messageCode == "1"){
               $.alert($("message",xml).text(),function(){
	                //刷新父列表
	               mainFrame.getCurrentTab().doCallback();
	               //关闭
	               mainFrame.getCurrentTab().close();
               });
           } else {
               $.alert($("message",xml).text());
               //设置按钮不可用
				$("#startFlow,#saveAsDraft").removeAttr("disabled");
           }
       },
       error : $.ermpAjaxError
   	});
}
