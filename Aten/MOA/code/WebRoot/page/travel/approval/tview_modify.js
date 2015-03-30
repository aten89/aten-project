/**********************
出差管理--出差审批单--驳回修改
*********************/ 
var mainFrame = $.getMainFrame();
$(initTripApproval);
function initTripApproval(){
	//日志展开
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
				}
			});
			dialog.openDialog("single");
		}
	);
	//新增明细
    $("#tripAdd").click(function(){
    	addTravel();
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
	var borrowSum = $("#borrowSum").val();;
	if (isNaN(borrowSum)) {
		$.alert("差旅借款只能为数字！");
		$("#borrowSum").focus();
		return;
	}
	//提交之前，判断有效性
	if (tripInfos.length==0){
		$.alert("您尚未填写出差信息，请详细填写！");
		return;
	}
	
	var trip = {};
	var tripid = $("#tripid").val();
   	if (tripid) {
   		trip.id = tripid;
   	}
	//将表单信息填写到请假单对象属性
	trip.regional = $("#regional").val();
	trip.applyDept = $("#applyDept").text();
	trip.appointTo = $("#appointTo").val();
	trip.borrowSum = borrowSum;
	trip.termType = $("input[name='termType'][@checked]").val();
	
	trip.busTripApplyDetail = tripInfos;
	var jsonStr = getJsonStr(trip);
	
	var comment = $.trim($("#comment").val());
	var transitionName = $("#transitionName").val();
	var taskid = $("#taskInstanceID").val();
	
	if(comment==""){
		$.alert("请填写审批意见");
		$("#comment").focus();
		return;
	}
	if (transitionName == "") {
		$.alert("请选择转向");
		return;
	}
	
	//设置按钮不可用
	$("#commitBut").find(".allBtn").attr("disabled","true");
	
	$.ajax({
       type : "POST",
       cache: false,
       url  : "m/trip_deal",
       data : {act:"deal_modify",
       			tiid:taskid,
       			transition:transitionName,
       			comment:comment,
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