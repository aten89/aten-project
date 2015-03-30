/**
 * 父页面传过来的参数
 */
var args = parent.window.dialogParam;
var mainPanel = $.getMainFrame();

//是否全额退款下拉框
var fullRefundFlagSel;
//客户下拉框
var customerInfoSel;
//划款登记下拉框
var custPaymentSel;
//产品下拉框
var prodInfoSel;
/**
 * 性别下拉选择框
 */
var sexSel;
/**
 * 客户性质下拉选择框
 */
var customerNatureSel;
/**
 * 沟通类型下拉选择框
 */
var commTypeSel;

var changeCount = 0;
var custpaymentSelChangeCount = 0;

$(initPage);
function initPage(){
	//当前用户ID
	var userAccountID = $("#saleManId").val(); 
	//设置默认流程标题
	if("" == $("#flowTitle").val()) {
		setDefaultFlowTitle();
	}
	
	//初始化各个下拉框
	initCustomerInfoSel();
	initProInfoSel();
//	initCustPaymentSel();
	initFullRefundFlagSel();
	
	//用户选择弹出框
    $('#openDispacher').click(
	   	function(e){
	   		dialog = new UserDialog(ERMP_PATH, BASE_PATH);
	   		dialog.setCallbackFun(callbackFun);
	   		var user = dialog.openDialog();
		}
	);
	
	//初始化文件上传控件
	initControl();
	
	
	$("#commitBut").find(".allBtn").each(function(){
  		$(this).click(function(){
  			$("#transitionName").val($(this).val());
  			var commId = "";//处理意见id
		  	if($("#commId").size() != 0){
				commId = $("#commId").val();
		  	}
	  		var tiid = $("#taskInstanceID").val();//任务实例ID
			var comment = $.trim($("#comment").val());//填写的意见
			
			if ($("#transitionName").val() == "修改提交"){
				//重新提交
				if(commitDispose(tiid, commId, comment,$("#transitionName").val())){
					$.alert("操作成功", function() {
						// 刷新父列表
						mainPanel.getCurrentTab().doCallback();
						// 关闭
						mainPanel.getCurrentTab().close();
					});
				}
			} else if($("#transitionName").val() == "作废") {
				//作废
				if(endTransition(tiid, commId, comment,$("#transitionName").val())){
					$.alert("操作成功", function() {
						// 刷新父列表
						mainPanel.getCurrentTab().doCallback();
						// 关闭
						mainPanel.getCurrentTab().close();
					});
				}
			}
  		});
  	});
}

/**
 * 设置默认流程标题
 */
function setDefaultFlowTitle() {
	$("#flowTitle").val($("#saleManName").val() + "于" + $("#applyTime").val() + "提交的客户服务部退款申请");
}

function callbackFun() {
	var selected = dialog.selected;
		if (selected!=null){
			$("#approver").val(selected[0].id);
			$("#approverDis").val(selected[0].name);
		}
}

/**
 * 提交流程动作
 * @param {} commId　划款处理意见id
 * @param {} tiid 
 * @param {} comment
 * @param {} transitionName 转向的名称
 * @return {Boolean}
 */
function commitDispose(tiid, commId, comment, transitionName){
	if($.validInput("comment", "处理意见", true, null, 1000)){
		return false;
	}
	
	var successStatus = false;

	var id = $("#idHid").val();
	var custRefundJson = getCustRefundJson();	
	
	
	
	if (transitionName == "") {
		alert("请选择转向");
		return false;
	}	
	
	$.ajax({
	       type : "POST",
	       cache: false,
	       async: false,
	       dataType : "json",
	       url  : "m/operationsManage/againCommitCustRefund",
	       data : {	
		    	   	id : id,
		   			tiid : tiid,
		   			custRefundJson : custRefundJson,
	       			transition : transitionName,
	       			taskCommentId : commId,
	       			comment:comment
	       		},
	       success : function(data){
		       	if ($.checkErrorMsg(data)) {
					successStatus = true;
					// 保存主体成功，准备上传附件
					ocx.refId = data.custRefund.id;
					saveFileList();
		       	}
	       },
	       error : $.ermpAjaxError
	   	});
	return successStatus;
}

/**
 * 作废
 * @param tiid
 * @param commId
 * @param comment
 * @param transitionName
 * @returns {Boolean}
 */
function endTransition(tiid, commId,comment, transitionName) {
	if($.validInput("comment", "处理意见", true, null, 1000)){
	  			return false;
	  }
	var successStatus = false;
	
	var id = $("#idHid").val();
	var custRefund = new Object();
	custRefund.id = id;
	if (transitionName == "") {
		alert("请选择转向");
		return false;
	}	
	
	$.ajax({
       type : "POST",
       cache: false,
       async: false,
       dataType : "json",
       url  : "m/operationsManage/dealCustRefund",
       data : {
	   		id : id,
   			tiid : tiid,
   			transition : transitionName,
   			comment:comment,
   			taskCommentId : commId
   		},
       success : function(data){
	       	if ($.checkErrorMsg(data)) {
	       		successStatus = true;
	       	}
       },
       error : $.ermpAjaxError
   	});
	return successStatus;
}

/**----------------------------------------通用--------------------------------------**/
/**
 * 初始化客户下拉选择框
 */
function initCustomerInfoSel(){
	var html=" <div>**请选择...</div> ";
	var url = CRM_PATH + "m/customer/initCustomerInfoSel?pageSize=500&multipleCustomerStatus=4,5&format=json&jsoncallback=?";
	$.getJSON(url,function(data){
		$("#customerInfoSel").html(data.htmlValue);
		
		customerInfoSel = $("#customerInfoSel").ySelect({
			width:400,
			popwidth:420,
			afterLoad : function() {

			},
			onChange : function(value, name) {
				//同步客户性质 
				synchronousCustInfo(value);
				initProInfoSel();
			}
		});
		var custId = $("#custIdHid").val();
   		if (customerInfoSel != undefined && custId != null && custId != "") {
   			customerInfoSel.select(custId);
   		}
	});
}

/**
 * 同步客户性质
 */
function synchronousCustInfo(value) {
	var custProperty = null;
	$.ajax({
        type : "POST",
		cache: false,
		url  : CRM_PATH +"m/customer/getCustomerInfo",
		dataType : "json",
		data : {
			customerId : value
		},
        success : function(data,i){
        	if ($.checkErrorMsg(data)) {
        		var customer = data.customer;
        		$("#customerProperty").html(customer.custPropertyName);
        	}
        },
        error : $.ermpAjaxError
    });
	
}

/**
 * 初始化产品下拉选择框
 */
function initProInfoSel(){
	var custId = null;
	//获取客户ID
	if(customerInfoSel != undefined) {
		custId = customerInfoSel.getValue();
	} else if($("#custIdHid").val()) {
		custId = $("#custIdHid").val();
	}
	var html="<div>**请选择...</div>";
	if(null != custId && "" != custId) {
		var url = BASE_PATH + "m/operationsManage/initCustProdSel?custId=" + custId;
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
		   			width:400,
					popwidth:420,
					afterLoad : function() {

					},
					onChange : function(value, name) {
						//同步剩余额度，已划款总额度
						synchronousProdInfo(value);
						initCustPaymentSel();
					}
				});
		   		var prodInfoId = $("#prodInfoIdHid").val();
		   		if (prodInfoSel != undefined && prodInfoId != null && prodInfoId != "") {
		   			prodInfoSel.select(prodInfoId);
		   		}
	  	}});
	} else {
		$("#prodInfoSel").html(html);
   		prodInfoSel = $("#prodInfoSel").ySelect({
   			width:400,
			popwidth:420,
			onChange : function(value, name) {
				//同步剩余额度，已划款总额度
				synchronousProdInfo(value);
				initCustPaymentSel();
			}
		});
	}
	
	//初始化划款记录
	initCustPaymentSel();
}

/**
 * 同步项目所属供应商
 * @param value
 */
function synchronousProdInfo(value) {
	if(null == value || "" == value)
		return;
	$.ajax({
        type : "POST",
		cache: false,
		url  : BASE_PATH +"m/prod_info/queryProdInfoById",
		dataType : "json",
		data : {
			id : value //产品ID
		},
        success : function(data,i){
        	if ($.checkErrorMsg(data)) {
        		var prodInfo = data.prodInfo;
        		$("#supplier").html(prodInfo.supplier.supplier);
        	}
        },
        error : $.ermpAjaxError
    });
}

/**
 * 初始化我的划款登记下拉选择框
 */
function initCustPaymentSel(){
	var custId = null;
	var prodId = null;
	//获取客户ID
	if(customerInfoSel != undefined) {
		custId = customerInfoSel.getValue();
	} else if($("#custIdHid").val()) {
		custId = $("#custIdHid").val();
	}
	
	//获取产品ID
	if(prodInfoSel != undefined) {
		prodId = prodInfoSel.getValue();
	} else if($("#prodInfoIdHid").val()) {
		prodId = $("#prodInfoIdHid").val();
	}
	
	//只有两个都有值，才去后台取对应的划款记录
	var html="<div>**请选择...</div>";
	if(null != custId && "" != custId && null != prodId && "" != prodId) {
		var url = BASE_PATH + "m/operationsManage/initCustPaymentSel?custId=" + custId + "&prodId=" + prodId;
		$.ajax({
	        type : "POST",
			cache: false,
			async: false,
			url  : url,
			dataType : "html",
		    success : function(data){
		   		html+=data;
		   		$("#custPaymentSel").html(html);
		   		custpaymentSel = $("#custPaymentSel").ySelect({
		   			width:400,
					popwidth:420,
					afterLoad : function() {
						
					},
					onChange : function(value, name) {
						//获取划款信息
						synchronousPayment(value);
					}
				});
		   		
		   		var paymentId = $("#paymentIdHid").val();
		   		if (custpaymentSel != undefined && paymentId != null && paymentId != "") {
		   			custpaymentSel.select(paymentId);
		   		}
	  	}});
	} else {
		$("#custPaymentSel").html(html);
   		custpaymentSel = $("#custPaymentSel").ySelect({
   			width:400,
			popwidth:420,
			onChange : function(value, name) {
				//获取划款信息
				synchronousPayment(value);
			}
		});
	}
	
}

/**
 * 同步划款信息
 * @param value
 */
function synchronousPayment(value) {
	if("" == value) {
		$("#transferAmount").html("");
		$("#transferDate").html("");
		return;
	}
	$.ajax({
        type : "POST",
		cache: false,
		url  : BASE_PATH +"m/operationsManage/queryCustPaymentById",
		dataType : "json",
		data : {
			id : value //产品ID
		},
        success : function(data,i){
        	if ($.checkErrorMsg(data)) {
        		var custPayment = data.custPayment;
        		$("#transferAmount").html(custPayment.transferAmount);
        		$("#transferDate").html(custPayment.transferDate);
        		
        		if("" == $("#idHid").val() || custpaymentSelChangeCount >= 4) {
					$("#refundAmount").val($("#transferAmount").html());
				}
				
				custpaymentSelChangeCount = 1;
        	}
        },
        error : $.ermpAjaxError
    });
}

/**
 * 初始化是否有全额退款
 */
function initFullRefundFlagSel() {
	fullRefundFlagSel = $("#fullRefundFlagSel").ySelect({
		width : 80,
		name : "fullRefundFlagSel",
		afterLoad : function() {
			
		},
		onChange : function(value, name) {
			if("true" == value) {
				if(changeCount != 0) {
					$("#refundAmount").val($("#transferAmount").html());
				}
				$("#refundAmount").attr("disabled","disabled");
			} else {
				$("#refundAmount").removeAttr("disabled");
			}
			changeCount = 1;
		}
	});
	
	var fullRefundFlag = $("#fullRefundFlagHid").val();
	if (fullRefundFlagSel != null && fullRefundFlag != null && fullRefundFlag != "") {
		fullRefundFlagSel.select(fullRefundFlag);
	}
}

/**
 * 封装数据
 * @return {Boolean}
 */
function getCustRefundJson(){
	var custRefund = {};
	var custRefundId = $("#idHid").val();
   	if (custRefundId) {
   		custRefund.id = custRefundId;
   	}
   	//认购产品
   	var prodInfo = new Object();
   	prodInfo.id = prodInfoSel.getValue();
	custRefund.prodInfo = prodInfo;
	//申请时间
	custRefund.applyTime = $.trim($("#applyTime").val());
	debugger;
	//供应商
	custRefund.supplier = $("#supplier").html();
	//客户ID
	custRefund.custId = customerInfoSel.getValue();
	//流程标题
	custRefund.flowTitle = $.trim($("#flowTitle").val());
	//划款登记ID
	custRefund.paymentId = custpaymentSel.getValue();
	//是否全额退款
	custRefund.fullRefundFlag = fullRefundFlagSel.getValue();
	//退款金额
	custRefund.refundAmount = $.trim($("#refundAmount").val());
	//退款原因
	custRefund.refundReason = $.trim($("#refundReason").val());
	//审批人
	custRefund.approver = $.trim($("#approver").val());
	
	var custRefundJson = $.toJSON(custRefund);
	return custRefundJson;
}

/**
 * 表单验证
 * @return {Boolean}
 */
function validateForm(){
	if(true)
		return true;
	if($.validInput("custName", "公司名称", true, "\<\>\'\"", 128)){
		return false;
	}
	if($.validInput("tel", "电话", true, "", 128)){
		return false;
	}
  	if(sexSel.getValue() == ""){
  		$.alert("性别不能为空");
  		return false;
  	}
  	if(customerNatureSel.getValue() == ""){
  		$.alert("客户性质不能为空");
  		return false;
  	}
	return true;
}

/**---------------------------附件上传相关----------------------------------*/
/**
 * 初始化附件上传控件
 */
function initControl() {
	// 获取控件实例,需要提供容器的ID
	ocx = $.getNewAttachmentControl("NTKO_AttachmentCtrl");
	ocx.setPermission({
		view : true,
		add : true,
		update : true,
		del : true,
		saveas : true
	});

	ocx.setBasePath(BASE_PATH);
	// 定义保存成功或失败后执行的回调函数，执行时将自动带上code和message
	ocx.setAfterSaveEvent("saveAttachmentEvent");
	ocx.setSaveURL(BASE_PATH + "m/operationsManage/uploadCustRefundFile");
	ocx.init("NTKO_AttachmentCtrl");
	
	// 取得退款申请的ID
	var custRefundId = $("#idHid").val();
	if (custRefundId) {
		// 加载文件列表
		var sUrl = BASE_PATH + "m/operationsManage/loadCustRefundAttachments";
		$.post(sUrl, {"referid" : custRefundId}, callbackfun, "json");
	}
}

/**
 * 回调函数：把加载回来的数据展示在附件上传控件上
 * @param data
 */
function callbackfun(data) {
	if ($.checkErrorMsg(data)) {
		ocx.setFileList(data.custRefundAttachments);
	}
}

/**
 * 回调函数：上传完附件之后的操作
 */
function saveAttachmentEvent(code, message) {
	if (code == "1") {
		$.alert("操作成功", function() {
			// 刷新父列表
			mainPanel.getCurrentTab().doCallback();
			// 关闭
			mainPanel.getCurrentTab().close();
		});
	} else {
		$.alert("操作失败", function() {
			// 刷新父列表
			mainPanel.getCurrentTab().doCallback();
			// 关闭
			mainPanel.getCurrentTab().close();
		});
	}
}

/**
 * 上传附件
 */
function saveFileList() {
	ocx.saveAttachments();
}

/**
 * 查看产品详情
 * @param proId
 */
function viewProductInfo() {
	var proId = prodInfoSel.getValue();
	if(null != proId && "" != proId) {
		mainPanel.addTab({
			id:"view_prodInfo_"+proId,
			title:"查看产品信息",
			url:BASE_PATH + "/m/prod_info/init_prodInfoFrame?prodInfoId=" + proId
		});
	} else {
		$.alert("请选择一项产品");
	}
}

/**
 * 查看用户详情
 * @param custId
 */
function viewCustomerInfo() {
	var custId = customerInfoSel.getValue();
	if(null != custId && "" != custId) {
		mainPanel.addTab({
			id:"viewCustomerInfo" + custId,
			title:"客户资料",
			url:CRM_PATH + "page/MyCustomer/CustomerInfo/customerInfoFrame.jsp?customerId=" + custId
		});
	} else {
		$.alert("请选择一位客户");
	}
}