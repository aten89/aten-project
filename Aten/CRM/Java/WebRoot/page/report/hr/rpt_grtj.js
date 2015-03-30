var mainFrame = $.getMainFrame();
//var groupSelect;
var userSelect;
$(initPage);

function initPage() {
	$("#rptFrame").height($(window).height()-93);
	
	$.handleRights({
        "query" : $.SysConstants.QUERY,
        "exportExcel" : $.SysConstants.EXPORT,
        "queryAssign" : $.OaConstants.ASSIGN
    });

  	//导出excel
	$("#exportExcel").click(function(){
		exportRpt("XLS");
  	});
  	//授权
	$("#queryAssign").click(function(){
		mainFrame.addTab({
			id:"oa_report_assign_grtj",
			title:"报表查询授权",
			url:BASE_PATH + "page/report/query_assign.jsp?rptId=grtj"
		});
  	});
  	
  	//查询
  	$("#query").click(function(){
  		$("#rptFrame").hide();
  		exportRpt("HTML");
  	});
  	//全屏查看
  	$("#fullView").click(function(){
  		var url = $("#rptFrame").attr("src");
  		if (url) {
	  		var width = screen.width;
			var height = screen.height;
			showModalDialog(url,"","dialogTop:0;dialogLeft:0;dialogWidth:" + width + ";dialogHeight:" + height + ";status:no;help:no;resizable:yes;");
  		}
  	});
 	//初始化机构列表
//	groupSelect = $("#groupIdDiv").ySelect({width: 100, json:true,url : "m/report/assignsel?rptID=grtj",onChange : groupOnchange,afterLoad:addGroupOption});
//   	function addGroupOption() {
//   		groupSelect.addOption("", "所有...", 0);
//	}
  	$('#showGroup').click(function(){
    	var selector = new DeptDialog();
		var selectedNames = $("#deptName").val().split(",");
		for (var i=0 ; i<selectedNames.length ; i++){
			if ($.trim(selectedNames[i]) != ""){
				selector.appendSelected(selectedNames[i]);
			}
		}
		selector.setEnableVals(getAssignDepts());
		selector.setCallbackFun(function(retVal){
			if (retVal != null) {
				var names = "";
				for (var i = 0 ; i < retVal.length; i++){
					names += retVal[i];
					if (i + 1 < retVal.length) {
						names += ",";
					}
				}
				$("#deptName").val(names);
				groupOnchange(names);
			}
		});
		selector.openDialog();
		
    });
    
	userSelect =	$("#userIdDiv").ySelect({width: 70, isdisabled:true});
	
	$.EnterKeyPress($("#bgnTransferAmount"),$("#query"));
	$.EnterKeyPress($("#endTransferAmount"),$("#query"));
	//默认查询
//  	exportRpt("HTML");
}

function groupOnchange(value){
	if (!value) {
		userSelect.select(0);
		userSelect.disable(true);
		return;
	}
	userSelect = $("#userIdDiv").ySelect({width: 70,json:true,url : "m/report/depusersel?userDeptName=" + encodeURI(value),afterLoad:addUserOption});
   	function addUserOption() {
		userSelect.addOption("", "所有...", 0);
	}
}

//获取有权限的部门
function getAssignDepts() {
	var depts = [];
	$.ajax({
		type : "POST",
	    dataType: "json",
	   	url  : "m/report/assigndept?rptID=grtj",
	   	async : false,          
		success : function(data) {
			if ($.checkErrorMsg(data)) {
				var dataList = data.queryAssigns;
				$(dataList).each(function(i) {
					depts.push(dataList[i].groupName);
				});
			}
		}
	});
	return depts;
}

//生成报表
function exportRpt(exportType) {
	if ($.validNumber("bgnTransferAmount", "开始到账金额")) {
		return;
	}
	if ($.validNumber("endTransferAmount", "结束到账金额")) {
		return;
	}
	
	var deptName = $("#deptName").val();
//	if (!deptName) {
//		$.alert("请选择所属团队");
//		return;
//	}
	
	var params = {};
	params.bgnTransferDate = $("#bgnTransferDate").val();
	params.endTransferDate = $("#endTransferDate").val();
	params.deptName = deptName;
	params.saleMan = userSelect ? userSelect.getValue() : "";
	params.bgnTransferAmount = $("#bgnTransferAmount").val();
	params.endTransferAmount = $("#endTransferAmount").val();
	params.isResign = $("input[name='resignOpt'][@checked]").val();
	params.currentUser = $("#currentUser").val();

	$.ajax({
		type : "POST",
	    dataType: "json",
	   	url  : "m/report/export",
	   	data : {
	   		exportType : exportType,
	   		tempName : "hr_grtj.jrxml",
	   		SUBREPORT_DIR : "$SUB_TEMP_NAME:hr_grtj_sub.jrxml",
	   		BGN_TRANSFER_DATE : params.bgnTransferDate,
	   		END_TRANSFER_DATE : params.endTransferDate,
	   		EXE_SQL : getExeSql(params),
	   		EXE_SQL_SUB : getExeSqlSub(params)
	   	},             
		success : function(data) {
			if ($.checkErrorMsg(data)) {
				var url = BASE_PATH + data.reportPath;
				if (exportType == "HTML") {
					$("#rptFrame").show().attr("src", url);
					$("#fullViewDiv").show();
				} else {
					$.openDownloadDialog(url + "?filename=" + getDownloadFilename("个人统计.xls"), "报表已生成");
				}
			}
		}
	});
}

//生成报表模板的SQL语句
function getExeSql(params) {
	var exeSql = "SELECT G.GROUP_NAME_ AS DEPT_NAME_ FROM EAPP.EAPP_GROUP G WHERE G.GROUP_NAME_ IN";
	if (params.deptName) {
		var deptNameInVal = params.deptName.replace(/,/g, "','");
		exeSql += " ('" + deptNameInVal + "')";
	} else {
		exeSql += " (SELECT RQA.GROUPNAME_ FROM CRM.CRM_RPT_QUERY_ASSIGN RQA WHERE RQA.RPT_ID_='grtj' AND RQA.USERACCOUNTID_='" + params.currentUser + "')";
	}
	exeSql += " ORDER BY G.GROUP_NAME_"
	return exeSql;
}

//生成报表子模板的SQL语句
function getExeSqlSub(params) {
	var exeSql = "SELECT R.SALE_MAN_ID_, R.SALE_MAN_NAME_, R.ACCOUNTING_COEFFICIENT_,R.TRANSFER_AMOUNT_, R.TRANSFER_DATE_, R.RESIGN_COUNT FROM " + 
			"	(SELECT UA.ACCOUNT_ID_ AS SALE_MAN_ID_, UA.DISPLAY_NAME_ AS SALE_MAN_NAME_,PIM.ACCOUNTING_COEFFICIENT_, PIM.TRANSFER_AMOUNT_, PIM.TRANSFER_DATE_, " + 
  			"        (SELECT COUNT(1) AS IS_RESIGN FROM OA.OA_STAFFFLOWAPPLY SA WHERE SA.PASSED_=1 AND SA.APPLYTYPE_=2 AND SA.USERACCOUNTID_=UA.ACCOUNT_ID_) AS RESIGN_COUNT " + 
  			" 	FROM EAPP.EAPP_USER_ACCOUNT UA LEFT JOIN " +  
 			"       	(SELECT PM.SALE_MAN_ID_, PI.ACCOUNTING_COEFFICIENT_, PM.TRANSFER_AMOUNT_, PM.TRANSFER_DATE_ FROM POSS.POSS_CUST_PAYMENT PM, POSS.POSS_PROD_INFO PI WHERE PM.PROD_ID_=PI.ID_ AND PM.FORM_STATUS_=2) PIM ON PIM.SALE_MAN_ID_=UA.ACCOUNT_ID_ ORDER BY UA.ACCOUNT_ID_ " +  
 			") R WHERE 1=1";
	if (params.saleMan) {//投资顾问
		exeSql += " AND R.SALE_MAN_ID_='" + params.saleMan + "'";
	}
	if (params.isResign == '0') {//是否离职
		exeSql += " AND R.RESIGN_COUNT > 0";
	} else {
		exeSql += " AND R.RESIGN_COUNT = 0";
	}

	if (params.bgnTransferDate) {//开始 到账日期
		exeSql += " AND R.TRANSFER_DATE_>=TO_DATE('" + params.bgnTransferDate + "','yyyy-MM-dd')";
	}
	if (params.endTransferDate) {//结束 到账日期
		exeSql += " AND R.TRANSFER_DATE_<=TO_DATE('" + params.endTransferDate + " 23:59:59','yyyy-MM-dd HH24:mi:ss')";
	}
	if (params.bgnTransferAmount) {//开始 到账金额
		exeSql += " AND R.TRANSFER_AMOUNT_>=" + params.bgnTransferAmount;
	}
	if (params.endTransferAmount) {//结束 到账金额
		exeSql += " AND R.TRANSFER_AMOUNT_<=" + params.endTransferAmount;
	}
	exeSql += " AND R.SALE_MAN_ID_ IN ";
	
	return exeSql;
}