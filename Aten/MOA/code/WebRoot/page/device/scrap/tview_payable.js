var mainFrame = $.getMainFrame();
$(initFinanceHandle);

function initFinanceHandle() {
	//添加权限约束
//    $.handleRights({
//        "commitBut" : $.OaConstants.DISPOSE
//    });
    $("div.feesSp").each(function(i){
		$("span[name='orderNum']", this).text(i+1);
	});
//	$("#costsLog .mb").hide();
	//日志展开
	$("#costsLog a").toggle(function(){
		$("#costsLog").find(".mb").hide();
		$("#costsLog h1").removeClass("logArrow");
	},function(){
		$("#costsLog").find(".mb").show();
		$("#costsLog h1").addClass("logArrow");
		
	});
	
	$(".addTool2 .allBtn").each(function(){
		$(this).click(function(){
			$("#transitionName").val($(this).val());
			$.confirm("确认是否" + $(this).val() + "？", function(r){
				if (r) {
					commitDispose();
				} else {
					return;
				}
			});
		});
	});			
}
function commitDispose(){
	var aDevList = [];
	var flag=false;
	$("div.feesSp").each(function(){
		var devDetail = new Object();
		var listID= $.trim($("span[name='listID']", this).text());
		var InDate =$.trim($("input[name='InDate']", this).val());
		var deviceName =$.trim($("span[name='deviceName']", this).text());
		var spanInDate =$.trim($("span[name='spanInDate']", this).html());
		if(typeof(spanInDate)!="undefined" && spanInDate!=""){
			if(InDate==""){
				$.alert("设备名称为【"+deviceName+"】的”款项到账日期”不能为空！")
				flag=true;
				return false;
			}
			devDetail.inDate = InDate;
			devDetail.devDetailId=listID;
			aDevList.push(devDetail);
		}
	});
	if(flag){
		return false;
	}
	var transitionName = $("#transitionName").val();
	if (transitionName == "") {
		$.alert("请选择转向");
		return;
	}
	var taskid = $("#taskInstanceID").val();
	var comment =  $.trim($("#comment").val());
	//驳回修改 一定要写意见
	if (comment=="") {
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
	var id = $("#id").val();
	if (id == "") {
		return;
	} 
    $.ajax({
        type:"POST",
		cache:false,
		url:"m/dev_deal?act=deal_payable",//
		data:{
			id : id,
			aDevList : $.toJSON(aDevList),
			tiid:taskid,
   			transition:transitionName,
   			comment:comment
		},
		success:function saveSuccess(xml){
		   var messageCode = $("message",xml).attr("code");
           var msg = $("message",xml).text();
           if(messageCode == "1"){
               $.alert("操作成功", function(){
	            	//刷新父列表
					mainFrame.getCurrentTab().doCallback();
					//关闭
					mainFrame.getCurrentTab().close();
            	});
           } else {
               $.alert($("message",xml).text());
           }
		},
        error:$.ermpAjaxError
    });  
}
