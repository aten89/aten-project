var mainFrame = $.getMainFrame();
$(initPage);

function initPage() {
	$("#rptFrame").height($(window).height()-70);
	
	$.handleRights({
        "query" : $.SysConstants.QUERY,
        "exportExcel" : $.SysConstants.EXPORT,
        "saveNorm" : "confnorm"
    });
    
    var d=new Date(); 
	var year = d.getFullYear();
	var month = d.getMonth() + 1;
	month =( month < 10 ? "0" : "") + month;
	$("#monthSel").val(month);
	var yearHTML = "";
    for (var i = 0; i <10; i++){
    	yearHTML += "<option value='" + (year - i) + "'>" + (year - i) + "年</option>";
    }
    $("#yearSel").html(yearHTML);

  	//导出excel
	$("#exportExcel").click(function(){
		exportRpt("XLS");
  	});
  	//指标配置
	$("#saveNorm").click(function(){
		mainFrame.addTab({
			id:"oa_report_norm_tdyxrl",
			title:"报表指标配置",
			url:BASE_PATH + "m/report/initnorm?rptID=tdyxrl"
		});
  	});
  	
  	//查询
  	$("#query").click(function(){
  		$("#rptFrame").hide();
  		exportRpt("HTML");
  	});
  	//全屏查看
  	$("#fullView").click(function(){
  		var url = BASE_PATH + "page/report/hr/view_tdyxrl.jsp?url=" + $("#rptFrame").attr("src");
  		if (url) {
	  		var width = screen.width;
			var height = screen.height;
			showModalDialog(url,"","dialogTop:0;dialogLeft:0;dialogWidth:" + width + ";dialogHeight:" + height + ";status:no;help:no;resizable:yes;");
  		}
  	});
 	
  	//默认当月
//  	$("#queryMonth").val(new Date().format("yyyy-MM-dd"));
  	//默认查询
//  	exportRpt("HTML");
}

//生成报表
function exportRpt(exportType) {
//	var queryMonth = $("#queryMonth").val();
//	if (!queryMonth) {
//		$.alert("请选择查询月份");
//		return;
//	}
//	queryMonth = queryMonth.substr(0, 7);
	queryMonth = $("#yearSel").val() + "-" + $("#monthSel").val();
	var params = {};
	params.queryMonth = queryMonth;
	
	$.ajax({
		type : "POST",
	    dataType: "json",
	   	url  : "m/report/export",
	   	data : {
	   		exportType : exportType,
	   		tempName : "hr_tdyxrl.jrxml",
	   		EXE_SQL : getExeSql(params),
	   		QUERY_MONTH : queryMonth
	   	},             
		success : function(data) {
			if ($.checkErrorMsg(data)) {
				var url = BASE_PATH + data.reportPath;
				if (exportType == "HTML") {
					$("#rptFrame").show().attr("src", url);
					$("#fullViewDiv").show();
				} else {
					$.openDownloadDialog(url + "?filename=" + getDownloadFilename("团队有效人力统计.xls"), "报表已生成");
				}
			}
		}
	});
}

//生成报表模板的SQL语句
function getExeSql(params) {
	var exeSql = "SELECT GE.GROUP_ID_, GE.GROUP_NAME_, "
			+ "SUBSTR((SELECT SYS_CONNECT_BY_PATH(G.GROUP_NAME_,'/') FROM EAPP.EAPP_GROUP G WHERE G.GROUP_ID = GR.GROUP_ID START WITH G.PARENT_GROUP_ID_ IS NULL CONNECT BY PRIOR G.GROUP_ID = G.PARENT_GROUP_ID_),2) AS GROUP_NAME_PATH, "
			+ "(SELECT COUNT(1) FROM EAPP.EAPP_GROUP_USER GRU WHERE GRU.GROUP_ID_=GR.GROUP_ID) AS ALL_USER_COUNT, "
			+ "(SELECT COUNT(1) FROM (SELECT PM.SALE_MAN_ID_  FROM POSS.POSS_CUST_PAYMENT PM WHERE PM.FORM_STATUS_=2 AND (TO_CHAR(PM.TRANSFER_DATE_,'yyyy-mm') ='" + params.queryMonth + "' OR TO_CHAR(ADD_MONTHS(PM.TRANSFER_DATE_,1),'yyyy-mm') ='" + params.queryMonth + "' OR TO_CHAR(ADD_MONTHS(PM.TRANSFER_DATE_,2),'yyyy-mm') ='" + params.queryMonth + "') GROUP BY PM.SALE_MAN_ID_ HAVING SUM(PM.TRANSFER_AMOUNT_) / 3 >= (SELECT TO_NUMBER(RNC.NORM_VALUE_) FROM CRM.CRM_RPT_NORM_CONF RNC WHERE RNC.RPT_ID_='tdyxrl' AND RNC.NORM_CODE_='AVG_AMOUNT')) R WHERE R.SALE_MAN_ID_ IN (SELECT UA1.ACCOUNT_ID_ FROM EAPP.EAPP_USER_ACCOUNT UA1, EAPP.EAPP_GROUP_USER GRU1 WHERE GRU1.USER_ID_=UA1.USER_ID_ AND GRU1.GROUP_ID_=GR.GROUP_ID)) AS EFFE_USER_COUNT " 
			+ "FROM CRM.CRM_GROUP_EXT GE, EAPP.EAPP_GROUP GR WHERE GE.GROUP_NAME_=GR.GROUP_NAME_ AND GE.BUSINESS_TYPE_ = 'S' ORDER BY GE.GROUP_NAME_";

	return exeSql;
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
