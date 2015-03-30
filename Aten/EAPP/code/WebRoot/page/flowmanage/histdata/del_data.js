var mainFrame = $.getMainFrame();
$(initPage);

function initPage(){
	var d=new Date(); 
    var year = d.getFullYear();
    var month = d.getMonth() + 1;
    var date = d.getDate();
	var curDate=(year - 1)+ (month < 10 ? "-0" : "-") + month + (date < 10 ? "-0" : "-") + date;
	$("#startDate").val(curDate);
 	// 删除操作
    $("#delData").click(function(){
    	// 按钮不可用
    	delFlowData();
    });
    
}

/**
 * 删除操作
 */
function delFlowData() {
	// 获取时间
	var time = $("#startDate").val();
	if (time == "") {
		$.alert("请选择时间.");
		// 按钮可用
		return false;
	}
	$.confirm("确认删除 "+time+" 之前的流程数据?", function(r){
		if (r) {
			$("#delData").attr("disabled","true").addClass("icoNone");
			$.ajax({
				type : "POST",
				cache: false, 
				url  : "m/flow_data/delete",
				dataType : "json",
				data : {clearDate : time},
				success : function(data){
//					if ($.checkErrorMsg(data) ) {
//						 $.alert("动作信息删除成功！");
//					}
					$("#delData").removeAttr("disabled").removeClass("icoNone");
		        }
			});
		}
	});
}