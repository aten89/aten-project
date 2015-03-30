var mainFrame = $.getMainFrame();
//var groupSelect;
var userSelect;
var prodInfoSel;
$(initPage);

function initPage() {
	$("#rptFrame").height($(window).height()-70);
	
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
			id:"oa_report_assign_yjcx",
			title:"报表查询授权",
			url:BASE_PATH + "page/report/query_assign.jsp?rptId=yjcx"
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
//	groupSelect = $("#groupIdDiv").ySelect({width: 100, isdisabled:true, json:true,url : "m/report/assignsel?rptID=yjcx",onChange : groupOnchange,afterLoad:addGroupOption});
//   	function addGroupOption() {
//		groupSelect.addOption("", "所有...", 0);
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
	
	initProdInfoSel();
	
  	$.EnterKeyPress($("#custName"),$("#query"));
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

function clickPersonal(flag) {
//	groupSelect.select(0);
//	groupSelect.disable(flag);
	$("#deptName").val("");
	$("#showGroup,#deptName").attr("disabled", flag);
	userSelect.select(0);
	userSelect.disable(flag);
}

function initProdInfoSel() {
	prodInfoSel = $("#prodInfoDiv").ySelect({
		width : 160,
		url : POSS_PATH + "/m/prod_info/initProdInfoSel",
		afterLoad : function() {
			prodInfoSel.addOption("", "所有...", 0);
		}
	});
}

//显示更多查询字段
function showMoreQueryField(obj) {
	if ($(".moreSoso").is(':visible')) {
		$(".moreSoso").hide();
		$(obj).text("更多条件↓");
		$("#rptFrame").height($("#rptFrame").height()+54);
		//清空隐藏的条件内容
		prodInfoSel.select(0);
		$("#bgnTransferDate").val("");
		$("#endTransferDate").val("");
		$("#bgnTransferAmount").val("");
		$("#endTransferAmount").val("");
		$("#bgnProdCashDate").val("");
		$("#endProdCashDate").val("");
		$("#bgnActualCashDate").val("");
		$("#endActualCashDate").val("");
	} else {
		$(".moreSoso").show();
		$(obj).text("精简条件↑");
		$("#rptFrame").height($("#rptFrame").height()-54);
	}
}

//获取有权限的部门
function getAssignDepts() {
	var depts = [];
	$.ajax({
		type : "POST",
	    dataType: "json",
	   	url  : "m/report/assigndept?rptID=yjcx",
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
	
	var personal = $("input[name='personalOpt'][@checked]").val();
	var saleMan = "";
	var deptName = "";
	if (personal == "1") {//查本人
		saleMan = $("#currentUser").val();
	} else {//查团队
//		deptName = groupSelect ? groupSelect.getValue() : "";
		deptName = $("#deptName").val();
//		if (!deptName) {
//			$.alert("请选择所属团队");
//			return;
//		}
		saleMan = userSelect ? userSelect.getValue() : "";
	}
	var params = {};
	params.deptName = deptName;
	params.saleMan = saleMan;
	params.custName = $("#custName").val();
	params.productId = prodInfoSel ? prodInfoSel.getValue() : "";
	params.bgnTransferDate = $("#bgnTransferDate").val();
	params.endTransferDate = $("#endTransferDate").val();
	params.bgnTransferAmount = $("#bgnTransferAmount").val();
	params.endTransferAmount = $("#endTransferAmount").val();
	params.bgnProdCashDate = $("#bgnProdCashDate").val();
	params.endProdCashDate = $("#endProdCashDate").val();
	params.bgnActualCashDate = $("#bgnActualCashDate").val();
	params.endActualCashDate = $("#endActualCashDate").val();
	params.currentUser = $("#currentUser").val();
	
	$.ajax({
		type : "POST",
	    dataType: "json",
	   	url  : "m/report/export",
	   	data : {
	   		exportType : exportType,
	   		tempName : "marke_yjcx.jrxml",
	   		SUBREPORT_DIR : "$SUB_TEMP_NAME:marke_yjcx_sub.jrxml",
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
					$.openDownloadDialog(url + "?filename=" + getDownloadFilename("业绩查询统计.xls") , "报表已生成");
				}
			}
		}
	});
}

//生成报表模板的SQL语句
function getExeSql(params) {
	var saleManSql = "";
	var deptNameSql = "";
	if (params.saleMan) {//查个人或选团队与团队下的人
		saleManSql = " AND UA.ACCOUNT_ID_='" + params.saleMan + "' ";
	} else {//
		if (params.deptName){//只选了团队未选人
			var deptNameInVal = params.deptName.replace(/,/g, "','");
			deptNameSql = " AND GR.GROUP_NAME_ IN ('" + deptNameInVal + "') ";
		} else {//所有团队
			deptNameSql = " AND GR.GROUP_NAME_ IN (SELECT RQA.GROUPNAME_ FROM CRM.CRM_RPT_QUERY_ASSIGN RQA WHERE RQA.RPT_ID_='yjcx' AND RQA.USERACCOUNTID_='" + params.currentUser + "') ";
		}
	}
	
	var exeSql = "SELECT UA.ACCOUNT_ID_ AS SALE_MAN_ID_, UA.DISPLAY_NAME_ AS SALE_MAN_NAME_, MIN(GR.GROUP_NAME_) AS DEPT_NAME_ " +
			"FROM EAPP.EAPP_USER_ACCOUNT UA LEFT JOIN EAPP.EAPP_GROUP_USER GU ON UA.USER_ID_=GU.USER_ID_ LEFT JOIN EAPP.EAPP_GROUP GR ON GR.GROUP_ID=GU.GROUP_ID_ AND GR.TYPE_='D' " +
			"WHERE UA.IS_LOGIC_DELETE_=0 " + saleManSql + deptNameSql +
			"GROUP BY UA.ACCOUNT_ID_,UA.DISPLAY_NAME_ ORDER BY MIN(GR.GROUP_NAME_) ,UA.ACCOUNT_ID_";
	return exeSql;
}

//生成报表子模板的SQL语句
function getExeSqlSub(params) {
	var exeSql = "SELECT PM.CUST_NAME_, PM.TRANSFER_DATE_, PI.PROD_CODE_, PI.PROD_NAME_, PI.PROD_CASH_DATE_, PI.ACTUAL_CASH_DATE_,PI.ACCOUNTING_COEFFICIENT_, PM.TRANSFER_AMOUNT_ " +
			"FROM POSS.POSS_CUST_PAYMENT PM, POSS.POSS_PROD_INFO PI WHERE PM.PROD_ID_=PI.ID_ AND PM.FORM_STATUS_=2";
	if (params.custName) {//客户姓名
		exeSql += " AND PM.CUST_NAME_ like '%" + params.custName + "%'";
	}
	if (params.productId) {//项目名称
		exeSql += " AND PM.PROD_ID_='" + params.productId + "'";
	}
	if (params.bgnTransferDate) {//开始 到账日期
		exeSql += " AND PM.TRANSFER_DATE_>=TO_DATE('" + params.bgnTransferDate + "','yyyy-MM-dd')";
	}
	if (params.endTransferDate) {//结束 到账日期
		exeSql += " AND PM.TRANSFER_DATE_<=TO_DATE('" + params.endTransferDate + " 23:59:59','yyyy-MM-dd HH24:mi:ss')";
	}
	if (params.bgnTransferAmount) {//开始 到账金额
		exeSql += " AND PM.TRANSFER_AMOUNT_>=" + params.bgnTransferAmount;
	}
	if (params.endTransferAmount) {//结束 到账金额
		exeSql += " AND PM.TRANSFER_AMOUNT_<=" + params.endTransferAmount;
	}
	if (params.bgnProdCashDate) {//开始 产品兑付日期
		exeSql += " AND PI.PROD_CASH_DATE_>=TO_DATE('" + params.bgnProdCashDate + "','yyyy-MM-dd')";
	}
	if (params.endProdCashDate) {//结束 产品兑付日期
		exeSql += " AND PI.PROD_CASH_DATE_<=TO_DATE('" + params.endProdCashDate + " 23:59:59','yyyy-MM-dd HH24:mi:ss')";
	}
	if (params.bgnActualCashDate) {//开始 实际兑付日期
		exeSql += " AND PI.ACTUAL_CASH_DATE_>=TO_DATE('" + params.bgnActualCashDate + "','yyyy-MM-dd')";
	}
	if (params.endActualCashDate) {//结束 实际兑付日期
		exeSql += " AND PI.ACTUAL_CASH_DATE_<=TO_DATE('" + params.endActualCashDate + " 23:59:59','yyyy-MM-dd HH24:mi:ss')";
	}
	exeSql += " AND PM.SALE_MAN_ID_=";
	return exeSql;
}