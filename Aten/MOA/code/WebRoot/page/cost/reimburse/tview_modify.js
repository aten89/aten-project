var mainFrame = $.getMainFrame();
$(init);
function init(){
   	//添加费用明细
    $("#costsAdd").click(function(){
    	addGroup();
    });
    
    //打开用户帐号
   $("#openUserSelect").click(
	   	function(e){
			var dialog = new UserDialog(ERMP_PATH, BASE_PATH);
			dialog.setCallbackFun(function(user){
				if (user != null) {
					$("#payee").val(user.id);
				$("#payeeName").val(user.name);
				}
			});
			dialog.openDialog("single");
		}
	);
	
	    //打开用户帐号
   $("#openAppointToSelect").click(
	   	function(e){
			var dialog = new UserDialog(ERMP_PATH, BASE_PATH);
			dialog.setCallbackFun(function(user){
				if (user != null) {
					$("#appointTo").val(user.id);
				$("#appointToName").val(user.name);
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
};


function commitDispose(){
	
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
    
	var transitionName = $("#transitionName").val();
	if (transitionName == "") {
		$.alert("请选择转向");
		return;
	}
	var comment = $("#comment").val();
	var result = $.validChar(comment,"<>");
	if (result) {
		$.alert("审批意见不能包含非法字符：" + result);
		$("#comment").focus();
		return;
	}
	
	var reim = {};
   	var reimid = $("#reimid").text();
   	if (reimid) {
   		reim.id = reimid;
   	}
   	reim.finance = $("#finance").val();
   	reim.payee = $("#payee").val(); 
	reim.causa = $("#causa").val(); 
	reim.loanSum = $("#loansum").val() ; 
	reim.reimbursementSum =  $("#outlaySumShow").text() ; 
	reim.appointTo = $("#appointTo").val();
	reim.applyDept =   $("#applyDept").text() ; 
	var jsonStr = getJsonStr(reim);
	
	var tid =  $("#taskInstanceID").val(); 
	
	//设置按钮不可用
	$("#commitBut").find(".allBtn").attr("disabled","true");
	
	$.ajax({
		type : "POST",
		cache: false,
		url  : encodeURI("m/rei_deal?act=deal_modify&transition="+transitionName+"&comment="+comment+"&tiid="+tid),
		data : {json:jsonStr},
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
		    //设置按钮不可用
			$("#commitBut").find(".allBtn").removeAttr("disabled");
       	}
     	},
       error : $.ermpAjaxError
   	});
}