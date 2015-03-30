var mainFrame = $.getMainFrame();
//var groupSelect;
var userSelect;
var statusSel;
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
			id:"oa_report_assign_qkxq",
			title:"报表查询授权",
			url:BASE_PATH + "page/report/query_assign.jsp?rptId=qkxq"
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
//	groupSelect = $("#groupIdDiv").ySelect({width: 100, json:true,url : "m/report/assignsel?rptID=qkxq",onChange : groupOnchange,afterLoad:addGroupOption});
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
	statusSel = $("#statusDiv").ySelect({width: 70});
	
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

//显示更多查询字段
function showMoreQueryField(obj) {
	if ($(".moreSoso").is(':visible')) {
		$(".moreSoso").hide();
		$(obj).text("更多条件↓");
		$("#rptFrame").height($("#rptFrame").height()+54);
		//清空隐藏的条件内容
		$("#bgnPassTime").val("");
		$("#endPassTime").val("");
		$("#bgnSubmitTime").val("");
		$("#endSubmitTime").val("");
		$("#bgnVisitTime").val("");
		$("#endVisitTime").val("");
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
	   	url  : "m/report/assigndept?rptID=qkxq",
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
	params.bgnPassTime = $("#bgnPassTime").val();
	params.endPassTime = $("#endPassTime").val();
	params.deptName = $("#deptName").val();
	params.saleMan = userSelect ? userSelect.getValue() : "";
	params.custName = $("#custName").val();
	params.status = statusSel ? statusSel.getValue() : "";
	params.currentUser = $("#currentUser").val();
	params.bgnSubmitTime = $("#bgnSubmitTime").val();
	params.endSubmitTime = $("#endSubmitTime").val();
	params.bgnVisitTime = $("#bgnVisitTime").val();
	params.endVisitTime = $("#endVisitTime").val();
	
	$.ajax({
		type : "POST",
	    dataType: "json",
	   	url  : "m/report/export",
	   	data : {
	   		exportType : exportType,
	   		tempName : "custser_qkxq.jrxml",
	   		SUBREPORT_DIR : "$SUB_TEMP_NAME:custser_qkxq_sub.jrxml",
	   		BGN_PASS_TIME : params.bgnPassTime,
	   		END_PASS_TIME : params.endPassTime,
	   		EXE_SQL : getExeSql(params)
	   	},             
		success : function(data) {
			if ($.checkErrorMsg(data)) {
				var url = BASE_PATH + data.reportPath;
				if (exportType == "HTML") {
					$("#rptFrame").show().attr("src", url);
					$("#fullViewDiv").show();
				} else {
					$.openDownloadDialog(url + "?filename=" + getDownloadFilename("潜客详情.xls"), "报表已生成");
				}
			}
		}
	});
}

//生成报表模板的SQL语句
function getExeSql(params) {

	var deptNameSql = "";
	if (params.deptName) {//所属团队
		var deptNameInVal = params.deptName.replace(/,/g, "','");
		deptNameSql = " AND GR.GROUP_NAME_ IN ('" + deptNameInVal + "') ";
	} else {//查所有有权限的
		deptNameSql = " AND GR.GROUP_NAME_  IN (SELECT RQA.GROUPNAME_ FROM CRM.CRM_RPT_QUERY_ASSIGN RQA WHERE RQA.RPT_ID_='qkxq' AND RQA.USERACCOUNTID_='" + params.currentUser + "') ";
	}
	var saleManSql = "";
	if (params.saleMan) {//资顾问
		saleManSql = " AND UA.ACCOUNT_ID_='" + params.saleMan + "' ";
	}
	
	var exeSql = "SELECT R.CUST_ID_, R.CUST_NAME_,R.SALE_MAN_,R.SUBMIT_TIME_,R.PASS_TIME_,R.STATUS_,R.SALE_MAN_NAME_, R.GROUP_NAME_, R.TEL_,"
//		+ "	SUBSTR(R.TEL_, 1, 3) || '****' || SUBSTR(R.TEL_, 8) AS TEL_, "
		+ "	SUBSTR((SELECT SYS_CONNECT_BY_PATH(G.GROUP_NAME_,'/') FROM EAPP.EAPP_GROUP G WHERE G.GROUP_NAME_ =R.GROUP_NAME_ START WITH G.PARENT_GROUP_ID_ IS NULL CONNECT BY PRIOR G.GROUP_ID = G.PARENT_GROUP_ID_),2) AS GROUP_NAME_PATH "
		+ "FROM ( "
		+ "     SELECT CUR.ID_ AS CUST_ID_,CUR.CUST_NAME_,CUR.SALE_MAN_,CUR.SUBMIT_TIME_,CUR.PASS_TIME_,CUR.STATUS_,CUR.TEL_,MIN(UA.DISPLAY_NAME_) AS SALE_MAN_NAME_, MIN(GR.GROUP_NAME_) AS GROUP_NAME_ "
		+ "     FROM CRM_CUSTOMER_INFO CUR LEFT JOIN EAPP.EAPP_USER_ACCOUNT UA ON UA.ACCOUNT_ID_=CUR.SALE_MAN_ LEFT JOIN EAPP.EAPP_GROUP_USER GU ON UA.USER_ID_ = GU.USER_ID_ LEFT JOIN EAPP.EAPP_GROUP GR ON GU.GROUP_ID_ = GR.GROUP_ID "
		+ "     WHERE 1=1 " + deptNameSql + saleManSql
		+ "     GROUP BY CUR.ID_,CUR.CUST_NAME_,CUR.SALE_MAN_,CUR.SUBMIT_TIME_,CUR.PASS_TIME_,CUR.STATUS_,CUR.TEL_ ORDER BY MIN(GR.GROUP_NAME_),CUR.SALE_MAN_ "
		+ ") R "
		+ "WHERE";
			
	if (params.status) {//回访状态
		exeSql += " R.STATUS_='" + params.status + "'";
	} else {
		exeSql += " R.STATUS_ IN ('-1','1','2','3')";
	}
	if (params.custName) {//客户名称
		exeSql += " AND R.CUST_NAME_ like '%" + params.custName + "%'";
	}
	if (params.bgnPassTime) {//开始 潜客通过时间
		exeSql += " AND R.PASS_TIME_>=TO_DATE('" + params.bgnPassTime + "','yyyy-MM-dd')";
	}
	if (params.endPassTime) {//结束 潜客通过时间
		exeSql += " AND R.PASS_TIME_<=TO_DATE('" + params.endPassTime + " 23:59:59','yyyy-MM-dd HH24:mi:ss')";
	}
	if (params.bgnSubmitTime) {//开始 潜客提交时间
		exeSql += " AND R.SUBMIT_TIME_>=TO_DATE('" + params.bgnSubmitTime + "','yyyy-MM-dd')";
	}
	if (params.endSubmitTime) {//结束 潜客提交时间
		exeSql += " AND R.SUBMIT_TIME_<=TO_DATE('" + params.endSubmitTime + " 23:59:59','yyyy-MM-dd HH24:mi:ss')";
	}
	if (params.bgnVisitTime || params.endVisitTime) {//最后回访时间
		exeSql += " AND R.CUST_ID_ IN (SELECT RVT.CUST_ID_ FROM CRM_RETURN_VIST RVT GROUP BY RVT.CUST_ID_ HAVING 1=1";
		if (params.bgnVisitTime) {//开始 最后回访时间
			exeSql += " AND MAX(RVT.RETURN_VIST_TIME_) >= TO_DATE('" + params.bgnVisitTime + "','yyyy-MM-dd')";
		}
		if (params.endVisitTime) {//结束 最后回访时间
			exeSql += " AND MAX(RVT.RETURN_VIST_TIME_) <= TO_DATE('" + params.endVisitTime + " 23:59:59','yyyy-MM-dd HH24:mi:ss')";
		}
		exeSql += ")";
	}
	
	return exeSql;
}