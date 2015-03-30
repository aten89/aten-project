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
			id:"oa_report_assign_kfqk",
			title:"报表查询授权",
			url:BASE_PATH + "page/report/query_assign.jsp?rptId=kfqk"
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
//	groupSelect = $("#groupIdDiv").ySelect({width: 100, json:true,url : "m/report/assignsel?rptID=kfqk",onChange : groupOnchange,afterLoad:addGroupOption});
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
    
	userSelect = $("#userIdDiv").ySelect({width: 70, isdisabled:true});
	
	$.EnterKeyPress($("#bgnPassCust"),$("#query"));
	$.EnterKeyPress($("#endPassCust"),$("#query"));
	
	//默认查询
//	exportRpt("HTML");
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
	   	url  : "m/report/assigndept?rptID=kfqk",
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
	if ($.validNumber("bgnPassCust", "开始合格潜客数")) {
		return;
	}
	if ($.validNumber("endPassCust", "结束合格潜客数")) {
		return;
	}
	
	var params = {};
	params.bgnSubmitTime = $("#bgnSubmitTime").val();
	params.endSubmitTime = $("#endSubmitTime").val();
	params.deptName = $("#deptName").val();
	params.saleMan = userSelect ? userSelect.getValue() : "";
	params.bgnPassCust = $("#bgnPassCust").val();
	params.endPassCust = $("#endPassCust").val();
	params.isResign = $("input[name='resignOpt'][@checked]").val();
	params.currentUser = $("#currentUser").val();

	$.ajax({
		type : "POST",
	    dataType: "json",
	   	url  : "m/report/export",
	   	data : {
	   		exportType : exportType,
	   		tempName : "custser_kfqk.jrxml",
	   		BGN_SUBMIT_TIME : params.bgnSubmitTime,
	   		END_SUBMIT_TIME : params.endSubmitTime,
	   		EXE_SQL : getExeSql(params)
	   	},             
		success : function(data) {
			if ($.checkErrorMsg(data)) {
				var url = BASE_PATH + data.reportPath;
				if (exportType == "HTML") {
					$("#rptFrame").show().attr("src", url);
					$("#fullViewDiv").show();
				} else {
					$.openDownloadDialog(url + "?filename=" + getDownloadFilename("客服潜客统计.xls"), "报表已生成");
				}
			}
		}
	});
}

//生成报表模板的SQL语句
function getExeSql(params) {
	var bgnSubmitTimeSql = "";
	if (params.bgnSubmitTime) {//开始 提交时间
		bgnSubmitTimeSql = " AND CI.SUBMIT_TIME_>=TO_DATE('" + params.bgnSubmitTime + "','yyyy-MM-dd') ";
	}
	var endSubmitTimeSql = "";
	if (params.endSubmitTime) {//结束 提交时间
		endSubmitTimeSql = " AND CI.SUBMIT_TIME_<=TO_DATE('" + params.endSubmitTime + " 23:59:59','yyyy-MM-dd HH24:mi:ss') ";
	}
	var deptNameSql = "";
	if (params.deptName) {//所属团队
		var deptNameInVal = params.deptName.replace(/,/g, "','");
		deptNameSql = " AND GR.GROUP_NAME_ IN ('" + deptNameInVal + "') ";
	} else {//查所有有权限的
		deptNameSql = " AND GR.GROUP_NAME_  IN (SELECT RQA.GROUPNAME_ FROM CRM.CRM_RPT_QUERY_ASSIGN RQA WHERE RQA.RPT_ID_='kfqk' AND RQA.USERACCOUNTID_='" + params.currentUser + "') ";
	}
	var saleManSql = "";
	if (params.saleMan) {//资顾问
		saleManSql = " AND UA.ACCOUNT_ID_='" + params.saleMan + "' ";
	}
	
	var exeSql = "SELECT R2.ACCOUNT_ID_, R2.DISPLAY_NAME_, R2.POST_NAME_, R2.GROUP_NAME_PATH, R2.RESIGN_COUNT, R2.ENTRY_DATE, R2.ALL_CUST, R2.COMM_CUST, R2.PASS_CUST "
			+ "FROM ( "
			+ "    SELECT R1.ACCOUNT_ID_, R1.DISPLAY_NAME_, R1.POST_NAME_, "
			+ "      SUBSTR((SELECT SYS_CONNECT_BY_PATH(G.GROUP_NAME_, '/') FROM EAPP.EAPP_GROUP G WHERE G.GROUP_ID = R1.GROUP_ID START WITH G.PARENT_GROUP_ID_ IS NULL CONNECT BY PRIOR G.GROUP_ID = G.PARENT_GROUP_ID_), 2) AS GROUP_NAME_PATH, "
			+ "      (SELECT COUNT(1) AS IS_RESIGN FROM OA.OA_STAFFFLOWAPPLY SA WHERE SA.PASSED_ = 1 AND SA.APPLYTYPE_ = 2 AND SA.USERACCOUNTID_ = R1.ACCOUNT_ID_) AS RESIGN_COUNT, "
			+ "      (SELECT SA.ENTRYDATE_ AS IS_RESIGN FROM OA.OA_STAFFFLOWAPPLY SA WHERE SA.PASSED_ = 1 AND SA.APPLYTYPE_ = 1 AND SA.USERACCOUNTID_ = R1.ACCOUNT_ID_ AND ROWNUM = 1) AS ENTRY_DATE, "
			+ "      (SELECT COUNT(1) FROM CRM_CUSTOMER_INFO CI WHERE CI.SALE_MAN_ = R1.ACCOUNT_ID_" + bgnSubmitTimeSql + endSubmitTimeSql + ") AS ALL_CUST, "
			+ "      (SELECT COUNT(1) FROM CRM_CUSTOMER_INFO CI WHERE CI.SALE_MAN_ = R1.ACCOUNT_ID_ AND CI.STATUS_ <> '0'" + bgnSubmitTimeSql + endSubmitTimeSql + ") AS COMM_CUST, "
			+ "      (SELECT COUNT(1) FROM CRM_CUSTOMER_INFO CI WHERE CI.SALE_MAN_ = R1.ACCOUNT_ID_ AND CI.STATUS_ IN ('3', '4', '5')" + bgnSubmitTimeSql + endSubmitTimeSql + ") AS PASS_CUST "
			+ "    FROM ( "
			+ "        SELECT UA.ACCOUNT_ID_, UA.DISPLAY_NAME_, MIN(GR.GROUP_ID) AS GROUP_ID, MIN(PO.POST_NAME_) AS POST_NAME_ "
			+ "        FROM EAPP.EAPP_USER_ACCOUNT UA LEFT JOIN EAPP.EAPP_GROUP_USER GU ON UA.USER_ID_ = GU.USER_ID_ LEFT JOIN EAPP.EAPP_GROUP GR ON GU.GROUP_ID_ = GR.GROUP_ID LEFT JOIN EAPP.EAPP_POST_USER PU ON UA.USER_ID_ = PU.USER_ID_ LEFT JOIN EAPP.EAPP_POST PO ON PU.POST_ID_ = PO.POST_ID_ "
			+ "        WHERE UA.ACCOUNT_ID_ IN (SELECT CI1.SALE_MAN_ FROM CRM_CUSTOMER_INFO CI1) " + deptNameSql + saleManSql
			+ "        GROUP BY UA.ACCOUNT_ID_, UA.DISPLAY_NAME_ ORDER BY MIN(GR.GROUP_ID),UA.ACCOUNT_ID_ "
			+ "    ) R1 "
			+ ") R2 "
			+ "WHERE R2.ALL_CUST > 0 ";
			
	if (params.isResign == '0') {//是否离职
		exeSql += " AND R2.RESIGN_COUNT > 0";
	} else {
		exeSql += " AND R2.RESIGN_COUNT = 0";
	}
	if (params.bgnPassCust) {//开始 合格潜客数
		exeSql += " AND R2.PASS_CUST>=" + params.bgnPassCust;
	}
	if (params.endPassCust) {//结束 合格潜客数
		exeSql += " AND R2.PASS_CUST<=" + params.endPassCust;
	}
	
	return exeSql;
}
