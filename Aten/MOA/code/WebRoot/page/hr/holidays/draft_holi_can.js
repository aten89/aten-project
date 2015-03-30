var mainFrame = $.getMainFrame();
//var holidayApply = null;
$(initVacationApprovalView);

function initVacationApprovalView() {
//	holidayApply.draw();
	//提交开始流程
    $("#startFlow").click(function(){
    	startFlow();
    	
    });
    //取消
    $("#cancel").click(function(){
    	$.confirm("您确定要放弃正在填写的销假单吗?", function(r){
			if (r) {
				mainFrame.getCurrentTab().close();
			}
		});
    });
}

//提交或保存草稿
function startFlow(){
	var detaiInfos = [];
	$(".cancelInfo").each(function(){
		var detailId = $(this).attr("id");
		var cancelDays = $("input", this).val();
		if (cancelDays != "" && isNaN(cancelDays)) {
       		$.alert("不是有效的数字类型");
       		$("input", this).focus();
       		return true;
       	}
       	if (cancelDays != "" && Number(cancelDays) < 0) {
       		$.alert("销假天数必须大于0");
       		$("input", this).focus();
       		return true;
       	}
		var cancelRemark = $("input", $(this).next()).val();
		detaiInfos.push({id : detailId, cancelDays : cancelDays, cancelRemark : cancelRemark});
	});
//	alert($.toJSON(detaiInfos));
	
	
	$.ajax({
       type : "POST",
       cache: false,
       url  : "m/holi_start?act=add_can&id=" + $("#holiId").val(),
       data : {json:$.toJSON(detaiInfos)},
       success : function(xml){
           var messageCode = $("message",xml).attr("code");
           if(messageCode == "1"){
           		//添加知会人
           		saveFlowNotify($("#holiId").val());
				$.alert($("message",xml).text(), function(){
	                //刷新父列表
	               mainFrame.getCurrentTab().doCallback();
	               //关闭
	               mainFrame.getCurrentTab().close();
				});
           } else {
               $.alert($("message",xml).text());
               //设置按钮不可用
				$("#startFlow").removeAttr("disabled");
           }
       },
       error : $.ermpAjaxError
   	});
};