var mainFrame = $.getMainFrame();
//var groupSelect;
var userSelect;
$(initPage);

function initPage() {
	$("#rptFrame").height($(window).height()-70);
	
	$.handleRights({
        "query" : $.SysConstants.QUERY,
        "exportExcel" : $.SysConstants.EXPORT,
        "queryAssign" : $.OaConstants.ASSIGN,
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
  	//授权
	$("#queryAssign").click(function(){
		mainFrame.addTab({
			id:"oa_report_assign_yxrltj",
			title:"报表查询授权",
			url:BASE_PATH + "page/report/query_assign.jsp?rptId=yxrltj"
		});
  	});
  	//指标配置
	$("#saveNorm").click(function(){
		mainFrame.addTab({
			id:"oa_report_norm_yxrltj",
			title:"报表指标配置",
			url:BASE_PATH + "m/report/initnorm?rptID=yxrltj"
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
//	groupSelect = $("#groupIdDiv").ySelect({width: 100, isdisabled:true, json:true,url : "m/report/assignsel?rptID=yxrltj",onChange : groupOnchange,afterLoad:addGroupOption});
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
	
  	$.EnterKeyPress($("#custName"),$("#query"));
  	//默认当月
//  	$("#queryMonth").val(new Date().format("yyyy-MM-dd"));
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

//获取有权限的部门
function getAssignDepts() {
	var depts = [];
	$.ajax({
		type : "POST",
	    dataType: "json",
	   	url  : "m/report/assigndept?rptID=yxrltj",
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
	var personal = $("input[name='personalOpt'][@checked]").val();
	var saleMan = "";
	var deptName = "";
	if (personal == "1") {//查本人
		saleMan = $("#currentUser").val();
	} else {//查团队
		deptName = $("#deptName").val();
//		if (!deptName) {
//			$.alert("请选择所属团队");
//			return;
//		}
		saleMan = userSelect ? userSelect.getValue() : "";
	}
//	var queryMonth = $("#queryMonth").val();
//	if (!queryMonth) {
//		$.alert("请选择查询月份");
//		return;
//	}
//	queryMonth = queryMonth.substr(0, 7);
	queryMonth = $("#yearSel").val() + "-" + $("#monthSel").val();
	var params = {};
	params.deptName = deptName;
	params.saleMan = saleMan;
	params.queryMonth = queryMonth;
	params.currentUser = $("#currentUser").val();
	
	$.ajax({
		type : "POST",
	    dataType: "json",
	   	url  : "m/report/export",
	   	data : {
	   		exportType : exportType,
	   		tempName : "hr_yxrltj.jrxml",
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
					$.openDownloadDialog(url + "?filename=" + getDownloadFilename("有效人力统计.xls"), "报表已生成");
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
		saleManSql = " AND UA.ACCOUNT_ID_='" + params.saleMan + "'";
	} else {
		if (params.deptName) {//只选了团队未选人
			var deptNameInVal = params.deptName.replace(/,/g, "','");
			deptNameSql = " AND GR.GROUP_NAME_ IN ('" + deptNameInVal + "')";
		} else {
			deptNameSql = " AND GR.GROUP_NAME_ IN (SELECT RQA.GROUPNAME_ FROM CRM.CRM_RPT_QUERY_ASSIGN RQA WHERE RQA.RPT_ID_='yxrltj' AND RQA.USERACCOUNTID_='" + params.currentUser + "') ";
		}
	}
	
	var exeSql = "SELECT R.ACCOUNT_ID_, R.DISPLAY_NAME_, R.GROUP_NAME_, SIGN((R.CUR_MONTH+R.PRE_MONTH+R.PRE2_MONTH)/3 - (SELECT TO_NUMBER(RNC.NORM_VALUE_) FROM CRM.CRM_RPT_NORM_CONF RNC WHERE RNC.RPT_ID_='yxrltj' AND RNC.NORM_CODE_='AVG_AMOUNT')) AS EFFE_HR,R.PAYMET_COUNT, R.CUR_MONTH, R.PRE_MONTH, R.PRE2_MONTH FROM ( "
			  + "SELECT UA.ACCOUNT_ID_, UA.DISPLAY_NAME_, MIN(GR.GROUP_NAME_) AS GROUP_NAME_, "
			  + "(SELECT COUNT(PM.TRANSFER_AMOUNT_) FROM POSS.POSS_CUST_PAYMENT PM WHERE PM.FORM_STATUS_=2 AND PM.SALE_MAN_ID_= UA.ACCOUNT_ID_ AND (TO_CHAR(PM.TRANSFER_DATE_,'yyyy-mm') ='" + params.queryMonth + "' OR TO_CHAR(ADD_MONTHS(PM.TRANSFER_DATE_,1),'yyyy-mm') ='" + params.queryMonth + "' OR TO_CHAR(ADD_MONTHS(PM.TRANSFER_DATE_,2),'yyyy-mm') ='" + params.queryMonth + "')) AS PAYMET_COUNT,  "
			  + "(SELECT NVL(SUM(PM.TRANSFER_AMOUNT_),0) FROM POSS.POSS_CUST_PAYMENT PM WHERE PM.FORM_STATUS_=2 AND PM.SALE_MAN_ID_= UA.ACCOUNT_ID_ AND TO_CHAR(PM.TRANSFER_DATE_,'yyyy-mm') ='" + params.queryMonth + "') AS CUR_MONTH, "
			  + "(SELECT NVL(SUM(PM.TRANSFER_AMOUNT_),0) FROM POSS.POSS_CUST_PAYMENT PM WHERE PM.FORM_STATUS_=2 AND PM.SALE_MAN_ID_= UA.ACCOUNT_ID_ AND TO_CHAR(ADD_MONTHS(PM.TRANSFER_DATE_,1),'yyyy-mm') ='" + params.queryMonth + "') AS PRE_MONTH, "
			  + "(SELECT NVL(SUM(PM.TRANSFER_AMOUNT_),0) FROM POSS.POSS_CUST_PAYMENT PM WHERE PM.FORM_STATUS_=2 AND PM.SALE_MAN_ID_= UA.ACCOUNT_ID_ AND TO_CHAR(ADD_MONTHS(PM.TRANSFER_DATE_,2),'yyyy-mm') ='" + params.queryMonth + "') AS PRE2_MONTH "
			  + "FROM EAPP.EAPP_USER_ACCOUNT UA LEFT JOIN EAPP.EAPP_GROUP_USER GU ON GU.USER_ID_=UA.USER_ID_ LEFT JOIN EAPP.EAPP_GROUP GR ON GU.GROUP_ID_=GR.GROUP_ID WHERE UA.IS_LOGIC_DELETE_=0 " + saleManSql + deptNameSql
			  + "GROUP BY UA.ACCOUNT_ID_, UA.DISPLAY_NAME_ ORDER BY MIN(GR.GROUP_NAME_), UA.ACCOUNT_ID_ ";

	exeSql += ") R"; 

	return exeSql;
}
