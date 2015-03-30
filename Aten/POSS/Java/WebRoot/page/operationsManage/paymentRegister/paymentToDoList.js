var mainFrame = $.getMainFrame();

/**
 * 翻页组件对像
 */
var trunPageObj;
/**
 * 表头列排序对像
 */
var columnSortObj;

var dialogWin = null;
var param = null;
var payTypeSel = null;
var prodInfoSel = null;
var customerInfoSel = null;

$(initPage);
function initPage(){
	
	//添加权限约束
	$.handleRights({
        "searchInfo" : $.SysConstants.QUERY, //查询
        "addOldCustomerPay" : $.SysConstants.ADD,
        "addNewCustomerPay" : $.SysConstants.ADD,
        "addFBYYCustomerPay" : $.SysConstants.ADD
    },"hidModuleRights");
	
	initPayTypeSel();
	initProInfoSel();
//	initCustomerInfoSel();
	
	//初始化表头列排序
	columnSortObj = $("#tableTH").columnSort();
	
	//放在这里不会报错
	trunPageObj =  $(".pageNext").turnPage(10);
	trunPageObj.gotoPage(1);//进入页面先进行一次搜索
	
	//支持回车
	$.EnterKeyPress($("#customerName"),$("#searchInfo"));
	$.EnterKeyPress($("#tel"),$("#searchInfo"));
	$.EnterKeyPress($("#email"),$("#searchInfo"));
	$.EnterKeyPress($("#recommendPro"),$("#searchInfo"));
	
	$("#searchInfo").click(function(){
		 trunPageObj.gotoPage(1);
	});
	
	//新建老客户划款
	$("#addOldCustomerPay").click(function(){
		addCustomerPay("addOldCustomerPay");
	});
	//新建新客户划款
	$("#addNewCustomerPay").click(function(){
		addCustomerPay("addNewCustomerPay");
	});
	
	
	//新建非标预约
	$("#addFBYYCustomerPay").click(function(){
		addFBYYCustomerPay();
	});
	

}

function queryList(){
	var standardFlag = $("#standardFlag").val(); 
	
	var bgnTransferDateTime =$.trim($("#bgnTransferDateTime").val());
	var endTransferDateTime = $.trim($("#endTransferDateTime").val());
//	var customerId = customerInfoSel.getValue();
	
	var customerId = "";
	var custName = $("#custName").val();
	var oriSelected = $("#oriSelected").val();
	if(custName == oriSelected){
		customerId = $("#custId").val();
	}
	
	var prodId = prodInfoSel.getValue();
	var payType = payTypeSel.getValue();
	
	var pageno = trunPageObj.getCurrentPageNo();//当前第几页
	var pagecount = trunPageObj.getPageCount();//一页多少条
	var sortCol = columnSortObj.getSortColumn();
	var ascend =  columnSortObj.getAscend();
	$.ajax({
	        type : "POST",
			cache: false,
			url  : "m/operationsManage/queryCustPaymentList",
			dataType : "json",
			data:{
				flag : standardFlag,
				bgnTransferDate : bgnTransferDateTime,
				endTransferDate : endTransferDateTime,
				custId : customerId,
				custName : custName,
				prodId : prodId,
				payType : payType,
				pageNo : pageno,
				pageSize : pagecount,
				sortCol : sortCol,
				ascend : ascend
			},
			success : function(data,i){
	           	var html="";
	        	if ($.checkErrorMsg(data)) {
	        		$("#userAccountID").val(data.userAccountID);
	        		$("#userDisplayName").val(data.userDisplayName);
	        		var page = data.listPage;
			        if(page != null && page.dataList!=null){
			           var list = page.dataList;
			           for(var i =0; i<list.length; i++){
			        	   var tiid = list[i].task.taskInstanceId;
			        	   var taskid = list[i].task.id;
			        		html += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" +
		           			"<td class=\"listData\">" + (list[i].cusManageName==null ? "" : list[i].cusManageName) + "</td>" +
		           			"<td class=\"listData\">" + (list[i].transferDate==null ? "" : list[i].transferDate) + "</td>" +
		           			"<td class=\"listData\">"+(list[i].custName==null ?"":list[i].custName)+"</td>" + 
		           			"<td class=\"listData\">"+(list[i].custProperty==null?"":(list[i].custProperty=="0" ? "个人":"机构"))+"</td>" +
		           			"<td class=\"listData\">"+(list[i].proName==null?"":list[i].proName)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].appointmentAmount==null ?"":list[i].appointmentAmount)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].payTypeName==null?"":list[i].payTypeName)+"</td>" +
		           			"<td class=\"listData\">"+(list[i].task.nodeName==null?"":list[i].task.nodeName)+"</td>" +
			        		"<td class=\"listData\">" +
//			        			"<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"initCustPayment('" + id + "', '"+tiid+"')\">办理</a> " + 
			        			($.hasRight($.OaConstants.DISPOSE)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"initCustPayment('" + taskid + "', '"+tiid+"')\">办理</a> " : "") + 
			        		"</td>";
			        		html += "</tr>";
			           }
			        }
		            $("#paymentToDoList").html(html);
		            trunPageObj.setPageData(page);
	        	}
	        },
	        error : $.ermpAjaxError
	    });
}

/**
 * 办理
 * @param id
 * @param tiid
 */
//function initCustPayment(id ,tiid){
//	param = new Object();
//	param.callback = closeWin;
//	window.dialogParam = param;
//	var title = "";
//	var url = BASE_PATH + "m/operationsManage/viewPayment?id=" + id +"&tiid=" + tiid;
//	var width = 800;
//	var height = 480;
//	var onClose = "";
//	var position = "";
//	dialogWin = $.showModalDialog(title, url, width, height, onClose, position);
//}

function initCustPayment(taskid ,tiid){
	mainFrame.addTab({
		id : "initCustPayment" + taskid,
		title : "处理客户划款",
		url : BASE_PATH + "m/operationsManage/viewPayment?taskid=" + taskid +"&tiid=" + tiid,
		callback : queryList
	});
}


//新建老客户划款
function addCustomerPay(bottonFlag){
	var url;
	
	if(bottonFlag == "addOldCustomerPay"){
		url = BASE_PATH + "m/operationsManage/initAddCustomerPay?flag=addOldCustomerPay";
	} else if(bottonFlag == "addNewCustomerPay"){
		url = BASE_PATH + "m/operationsManage/initAddCustomerPay?flag=addNewCustomerPay";
	}
	
	var userAccountID = $("#userAccountID").val();
	var userDisplayName = $("#userDisplayName").val();
	param = new Object();
	param.userAccountID = userAccountID;
	param.userDisplayName = userDisplayName;
	param.bottonFlag = bottonFlag;
	param.callback = closeWin;
	window.dialogParam = param;
	var title = "";
	var width = 775;
	var height = 360;
	var onClose = "";
	var position = "";
	dialogWin = $.showModalDialog(title, url, width, height, onClose, position);
}


//新建老客户划款
function addFBYYCustomerPay(){
	
	var url = BASE_PATH + "page/operationsManage/paymentRegister/addFBYYCustPayment.jsp";

	var userAccountID = $("#userAccountID").val();
	var userDisplayName = $("#userDisplayName").val();
	param = new Object();
	param.userAccountID = userAccountID;
	param.userDisplayName = userDisplayName;
	param.callback = closeWin;
	window.dialogParam = param;
	var title = "";
	var width = 775;
	var height = 280;
	var onClose = "";
	var position = "";
	dialogWin = $.showModalDialog(title, url, width, height, onClose, position);
}

//关闭窗口
function closeWin(){
	if (param.returnValue){
		$.alert("操作成功",function(){
			dialogWin.close();
			queryList();
		});
	}else{
		dialogWin.close();
	}
}

function initPayTypeSel(){
	payTypeSel = $("#payTypeSel").ySelect({
		name : "payTypeSel",
		width:107,
		popwidth:140
	});
}

/**
 * 初始化产品下拉选择框
 */
function initProInfoSel(){
	var html=" <div>**请选择...</div> ";
	var url = BASE_PATH + "m/prod_info/initProdInfoSel";
	$.ajax({
        type : "POST",
		cache: false,
		async: false,
		url  : url,
		dataType : "html",
	    success : function(data){
	   		html+=data;
	   		$("#prodInfoSel").html(html);
	   		prodInfoSel = $("#prodInfoSel").ySelect({
				width:255,
				popwidth:255,
				afterLoad : function() {

				},
				onChange : function(value, name) {
					
				}
			});
  	}});
}


/**
 * 初始化客户下拉选择框
 */
function initCustomerInfoSel(){

	var html=" <div>**请选择...</div> ";
	var url = CRM_PATH + "m/customer/initCustomerInfoSel?format=json&jsoncallback=?";
	$.ajax({
        type : "POST",
		cache: false,
		async: false,
		url  : url,
		dataType : "html",
	    success : function(data){
	   		html+=data;
	   		$("#customerInfoSel").html(html);
	   		customerInfoSel = $("#customerInfoSel").ySelect({
				width:107,
				popwidth:140,
				afterLoad : function() {

				},
				onChange : function(value, name) {
					
				}
			});
  	}});
}
