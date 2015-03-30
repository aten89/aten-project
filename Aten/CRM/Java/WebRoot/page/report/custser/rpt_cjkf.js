var mainFrame = $.getMainFrame();
//var groupSelect;
var userSelect;
var prodInfoSel;
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
			id:"oa_report_assign_cjkf",
			title:"报表查询授权",
			url:BASE_PATH + "page/report/query_assign.jsp?rptId=cjkf"
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
//	groupSelect = $("#groupIdDiv").ySelect({width: 100, json:true,url : "m/report/assignsel?rptID=cjkf",onChange : groupOnchange,afterLoad:addGroupOption});
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
	
	initProdInfoSel();
	
	$.EnterKeyPress($("#custName"),$("#query"));
	
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

function initProdInfoSel() {
	prodInfoSel = $("#prodInfoDiv").ySelect({
		width : 160,
		url : POSS_PATH + "/m/prod_info/initProdInfoSel",
		afterLoad : function() {
			prodInfoSel.addOption("", "所有...", 0);
		}
	});
}

//获取有权限的部门
function getAssignDepts() {
	var depts = [];
	$.ajax({
		type : "POST",
	    dataType: "json",
	   	url  : "m/report/assigndept?rptID=cjkf",
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
	var params = {};
	params.bgnTransferDate = $("#bgnTransferDate").val();
	params.endTransferDate = $("#endTransferDate").val();
	params.deptName = $("#deptName").val();
	params.saleMan = userSelect ? userSelect.getValue() : "";
	params.custName = $("#custName").val();
	params.productId = prodInfoSel ? prodInfoSel.getValue() : "";
	params.currentUser = $("#currentUser").val();

	$.ajax({
		type : "POST",
	    dataType: "json",
	   	url  : "m/report/export",
	   	data : {
	   		exportType : exportType,
	   		tempName : "custser_cjkf.jrxml",
	   		SUBREPORT_DIR : "$SUB_TEMP_NAME:custser_cjkf_sub.jrxml",
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
					$.openDownloadDialog(url + "?filename=" + getDownloadFilename("成交客户.xls"), "报表已生成");
				}
			}
		}
	});
}

//生成报表模板的SQL语句
function getExeSql(params) {
	var custNameSql = "";
	if (params.custName) {//客户名称
		custNameSql = " AND CI.CUST_NAME_ LIKE '%" + params.custName + "%' ";
	}
	var productIdSql = "";
	if (params.productId) {//成交产品
		productIdSql = " AND CP.PROD_ID_='" + params.productId + "' ";
	}
	
	var deptNameSql = "";
	if (params.deptName) {//所属团队
		var deptNameInVal = params.deptName.replace(/,/g, "','");
		deptNameSql = " AND GR.GROUP_NAME_ IN ('" + deptNameInVal + "') ";
	} else {//查所有有权限的
		deptNameSql = " AND GR.GROUP_NAME_  IN (SELECT RQA.GROUPNAME_ FROM CRM.CRM_RPT_QUERY_ASSIGN RQA WHERE RQA.RPT_ID_='cjkf' AND RQA.USERACCOUNTID_='" + params.currentUser + "') ";
	}
	var saleManSql = "";
	if (params.saleMan) {//资顾问
		saleManSql = " AND CI.SALE_MAN_='" + params.saleMan + "' ";
	}
	var bgnTransferDateSql = "";
	if (params.bgnTransferDate) {//开始 成交时间
		bgnTransferDateSql = " AND CP.TRANSFER_DATE_>=TO_DATE('" + params.bgnTransferDate + "','yyyy-MM-dd') ";
	}
	var endTransferDateSql = "";
	if (params.endTransferDate) {//结束 成交时间
		endTransferDateSql = " AND CP.TRANSFER_DATE_<=TO_DATE('" + params.endTransferDate + " 23:59:59','yyyy-MM-dd HH24:mi:ss') ";
	}
	
	var exeSql = "SELECT R.CUST_ID_,R.CUST_NAME_,R.IDENTITY_NUM_,R.BANKBRANCH_,R.BANKACCOUNT_,R.SALE_MAN_, R.SALE_MAN_NAME_, R.GROUP_NAME_,R.TEL_, "
//				+ "SUBSTR(R.TEL_, 1, 3) || '****' || SUBSTR(R.TEL_, 8) AS TEL_, "
				+ "SUBSTR((SELECT SYS_CONNECT_BY_PATH(G.GROUP_NAME_,'/') FROM EAPP.EAPP_GROUP G WHERE G.GROUP_NAME_ = R.GROUP_NAME_ START WITH G.PARENT_GROUP_ID_ IS NULL CONNECT BY PRIOR G.GROUP_ID = G.PARENT_GROUP_ID_),2) AS GROUP_NAME_PATH "
				+ "FROM (   "
				+ "  SELECT CI.ID_ AS CUST_ID_,CI.CUST_NAME_,CI.TEL_,CI.IDENTITY_NUM_,CI.BANKBRANCH_,CI.BANKACCOUNT_,CI.SALE_MAN_, MIN(UA.DISPLAY_NAME_) AS SALE_MAN_NAME_, MIN(GR.GROUP_NAME_) AS GROUP_NAME_ "
				+ "  FROM POSS.POSS_CUST_PAYMENT CP JOIN CRM_CUSTOMER_INFO CI ON CP.CUST_ID_=CI.ID_ LEFT JOIN EAPP.EAPP_USER_ACCOUNT UA ON UA.ACCOUNT_ID_=CI.SALE_MAN_ LEFT JOIN EAPP.EAPP_GROUP_USER GU ON GU.USER_ID_=UA.USER_ID_ LEFT JOIN EAPP.EAPP_GROUP GR ON GR.GROUP_ID=GU.GROUP_ID_ "
				+ "  WHERE 1=1" + custNameSql + productIdSql + deptNameSql + saleManSql + bgnTransferDateSql + endTransferDateSql
				+ "  GROUP BY CI.ID_,CI.CUST_NAME_,CI.TEL_,CI.IDENTITY_NUM_,CI.BANKBRANCH_,CI.BANKACCOUNT_,CI.SALE_MAN_ ORDER BY CI.CUST_NAME_ "
				+ ") R";
	return exeSql;
}

//生成报表子模板的SQL语句
function getExeSqlSub(params) {
	var exeSql = "SELECT CP.TRANSFER_AMOUNT_,CP.TRANSFER_DATE_,PI.PROD_NAME_,PI.REDEEM_FLAG_,PI.PROD_SET_UP_DATE_,PI.PAY_INTEREST_METHOD_,PI.PROD_CASH_DATE_,PI.SELL_TIME_LIMIT_, "
			+ "(SELECT SUP.SUPPLIER_ FROM POSS.POSS_SUPPLIER SUP WHERE SUP.ID_=PI.SUPPLIER_) AS SUPPLIER_NAME "
			+ "FROM POSS.POSS_CUST_PAYMENT CP,POSS.POSS_PROD_INFO PI WHERE CP.PROD_ID_=PI.ID_";

	if (params.bgnTransferDate) {//开始 成交时间
		exeSql += " AND CP.TRANSFER_DATE_>=TO_DATE('" + params.bgnTransferDate + "','yyyy-MM-dd')";
	}
	if (params.endTransferDate) {//结束 成交时间
		exeSql += " AND CP.TRANSFER_DATE_<=TO_DATE('" + params.endTransferDate + " 23:59:59','yyyy-MM-dd HH24:mi:ss')";
	}

	exeSql += " AND CP.CUST_ID_=";
	return exeSql;
}

