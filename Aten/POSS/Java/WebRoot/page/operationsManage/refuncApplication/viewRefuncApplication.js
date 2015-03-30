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

$(initPage);
function initPage(){
	//当前用户ID
	var userAccountID = $("#saleManId").val(); 
	
	//设置默认流程标题
//	setDefaultFlowTitle();
	
	//初始化各个下拉框
	initCustomerInfoSel();
	initProInfoSel();
	initCustPaymentSel();
	initFullRefundFlagSel();
	
	//初始化文件上传控件
	initControl();
	
	
	$("#commitBut").find(".allBtn").each(function(){
  		$(this).click(function(){
  			// 刷新父列表
			mainPanel.getCurrentTab().doCallback();
			// 关闭
			mainPanel.getCurrentTab().close();
  		});
  	});
}

/**----------------------------------------通用--------------------------------------**/
/**
 * 初始化客户下拉选择框
 */
function initCustomerInfoSel(){
	var html=" <div>**请选择...</div> ";
	var url = CRM_PATH + "m/customer/initCustomerInfoSel?pageSize=500&multipleCustomerStatus=5&format=json&jsoncallback=?";
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
			}
		});
		var custId = $("#custIdHid").val();
   		if (customerInfoSel != undefined && custId != null && custId != "") {
   			customerInfoSel.select(custId);
   		}
   		
   		customerInfoSel.disable(true);
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
	var html="<div>**请选择...</div>";
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
	   			width:400,
				popwidth:420,
				afterLoad : function() {

				},
				onChange : function(value, name) {
					//同步剩余额度，已划款总额度
					synchronousProdInfo(value);
				}
			});
	   		var prodInfoId = $("#prodInfoIdHid").val();
	   		if (prodInfoSel != undefined && prodInfoId != null && prodInfoId != "") {
	   			prodInfoSel.select(prodInfoId);
	   		}
	   		prodInfoSel.disable(true);
  	}});
}

/**
 * 同步项目所属供应商
 * @param value
 */
function synchronousProdInfo(value) {
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
	var html="<div>**请选择...</div>";
	var url = BASE_PATH + "m/operationsManage/initCustPaymentSel";
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
//					var paymentId = $("#paymentIdHid").val();
//					if (custpaymentSel != undefined && paymentId != null && paymentId != "") {
//						custpaymentSel.select(paymentId);
//					}
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
	   		custpaymentSel.disable(true);
  	}});
}

/**
 * 同步划款信息
 * @param value
 */
function synchronousPayment(value) {
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
			
		}
	});
	
	var fullRefundFlag = $("#fullRefundFlagHid").val();
	if (fullRefundFlagSel != null && fullRefundFlag != null && fullRefundFlag != "") {
		fullRefundFlagSel.select(fullRefundFlag);
	}
	
	fullRefundFlagSel.disable(true);
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
		add : false,
		update : false,
		del : false,
		saveas : false
	});

	ocx.setBasePath(BASE_PATH);
	// 定义保存成功或失败后执行的回调函数，执行时将自动带上code和message
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