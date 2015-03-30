var mainFrame = $.getMainFrame();
var queryMonth;
$(initPage);

function initPage() {
	$("#viewRptFrame").height($(window).height()-15);
}

//打开报表里的详情报表链接
function openLink(val, queryMonth) {
	$.ajax({
		type : "POST",
	    dataType: "json",
	   	url  : "m/report/export",
	   	data : {
	   		exportType : "HTML",
	   		tempName : "hr_tdyxrl_detail.jrxml",
	   		DEPT_ID : val,
	   		QUERY_MONTH : queryMonth
	   	},             
		success : function(data) {
			if ($.checkErrorMsg(data)) {
				var url = BASE_PATH + data.reportPath;
//				window.open(url);
				$.showModalDialog("有效人力明细", url, 460, 233);
			}
		}
	});
}
