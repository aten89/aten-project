var mainFrame = $.getMainFrame();

var supplierSel;

var prodTypeSel;

var secondClassifySel;

var prodStatusSel;

var sellRankSel;

/**
 * 简版附件
 */
var janeEditionObj;
/**
 * 非简版附件
 */
var otherEditionObj;

/**
 * 编辑前的HTML
 */
var expectYearYieldHTML = "";

var prodPayDateHTML = "";

var editFlag = false;

$(initEditProdInfoPage);
function initEditProdInfoPage(){
	
	var needGradeFlag = $("#needGradeFlag").val();
	$("input[name='needGradeFlag'][value='"+needGradeFlag+"']").attr("checked",true);
	var redeemFlag = $("#redeemFlag").val();
	$("input[name='redeemFlag'][value='"+redeemFlag+"']").attr("checked",true);
	
	// 初始化下拉框
	initSupplierSel();
	initProdTypeSel();
	initSecondClassifySel("");
	initProdStatusSel();
	initSellRankSel();
	
	janeEditionObj = $("#janeEditionAttchDIV").singleAttaUpLoad({
		divId : "janeEditionAttchDIV",
		oldShowId : "janeEditionAttchOldShow",
//		fileType : "xls",
		url : "m/prod_info/save_attachment"
	});
	
	otherEditionObj = $("#otherEditionAttchDIV").singleAttaUpLoad({
		divId : "otherEditionAttchDIV",
		oldShowId : "otherEditionAttchOldShow",
//		fileType : "xls",
		url : "m/prod_info/save_attachment"
	});
	
	// 预期年化收益率
	$("#addExpectYearYield").click(function(e) {
		addExpectYearYield();
	});
	//产品付息日期
	$("#addProdPayDates").click(function(){
		addProdPayDate();
	});
	
	// add prodInfo
	$("#save").click(function(){
		saveProdInfo();
	});
	
	//
	$("#cancel").click(function(){
		if ($.getMainFrame() != null && $.getMainFrame().getCurrentTab()!= null){
   			//刷新父列表
           	$.getMainFrame().getCurrentTab().doCallback();
   			$.getMainFrame().getCurrentTab().close();
   		}
	});
	
}

/************************预期年化收益率 start********************************/
function addExpectYearYield() {
	if(editFlag){
		$.alert("请先保存");
		return;
	}
	var html = createEditExpectYearYieldHTML(null);
	$("#expectYearYield").append(html);
	editFlag = true;
	//按钮控制
	setCommitButtonDisabled(true);
}

function editExpectYearYield(obj) {
	if(editFlag){
		$.alert("请先保存");
		return;
	}
	var TR = $(obj).parent().parent();
	var id = TR.attr("id");
	// 编辑前html
	expectYearYieldHTML = "<tr id='"+id+"'>" + TR.html() +"</tr>";
	var lowerLimit = TR.find("td").eq(0).text();
	var upperLimit = TR.find("td").eq(1).text();
	var yearYield = TR.find("td").eq(2).text();
	var benefitType = TR.find("td").eq(3).text();
	var expectYearYield = new Object();
	expectYearYield.id = id;
	expectYearYield.lowerLimit = lowerLimit;
	expectYearYield.upperLimit = upperLimit;
	expectYearYield.yearYield = yearYield;
	expectYearYield.benefitType = benefitType;
	
	var html = createEditExpectYearYieldHTML(expectYearYield);
	TR.replaceWith(html);
	editFlag = true;
	//按钮控制
	setCommitButtonDisabled(true);
}

function saveExpectYearYield(obj) {
	var TR = $(obj).parent().parent();
	var id = TR.attr("id");
	
	if ($.validNumber("lowerLimit", "下限", true, null, "\<\>\'\"")) {
		return;
	}
//	if ($.validNumber("upperLimit", "上限", true, null, "\<\>\'\"")) {
//		return;
//	}
	if ($.validNumber("yearYield", "年化收益率", true, null, "\<\>\'\"")) {
		return;
	}
	
	var lowerLimit = TR.find("td").eq(0).find("input").eq(0).val();
	var upperLimit = TR.find("td").eq(1).find("input").eq(0).val();
	var yearYield = TR.find("td").eq(2).find("input").eq(0).val();
	var benefitType = TR.find("td").eq(3).find("input").eq(0).val();
	var expectYearYield = new Object();
	expectYearYield.id = id;
	expectYearYield.lowerLimit = lowerLimit;
	expectYearYield.upperLimit = upperLimit;
	expectYearYield.yearYield = yearYield;
	expectYearYield.benefitType = benefitType;
	
	var html = createExpectYearYieldHTML(expectYearYield);
	TR.replaceWith(html);
	
	expectYearYieldHTML = "";
	editFlag = false;
	//按钮控制
	setCommitButtonDisabled(false);
}

function cancleEditExpectYearYield(obj) {
	var TR = $(obj).parent().parent();
	//原行显示
	TR.after(expectYearYieldHTML);
	// 移除编辑行
	TR.remove();
	// 启用按钮
	expectYearYieldHTML = "";
	editFlag = false;
	//按钮控制
	setCommitButtonDisabled(false);
}

function delExpectYearYield(obj) {
	$.confirm("确定删除该预期年化收益率", function(result) {
		if (result) {
			var TR = $(obj).parent().parent();
			TR.remove();
		}
	});
}

function createEditExpectYearYieldHTML(expectYearYield) {
	var html = "";
	if (expectYearYield != null && expectYearYield != "") {
		html += "<tr id='"+expectYearYield.id+"'>";
	} else {
		html += "<tr>";
	}
	if (expectYearYield != null && expectYearYield != "") {
		html += "<td><input id='lowerLimit' class='ipt05' type='text' value='"+expectYearYield.lowerLimit+"'/></td>";
	} else {
		html += "<td><input id='lowerLimit' class='ipt05' type='text' value=''/></td>";
	}
	if (expectYearYield != null && expectYearYield != "") {
		html += "<td><input id='upperLimit' class='ipt05' type='text' value='"+expectYearYield.upperLimit+"'/></td>";
	} else {
		html += "<td><input id='upperLimit' class='ipt05' type='text' value=''/></td>";
	}
	if (expectYearYield != null && expectYearYield != "") {
		html += "<td><input id='yearYield' class='ipt05' type='text' value='"+expectYearYield.yearYield+"'/></td>";
	} else {
		html += "<td><input id='yearYield' class='ipt05' type='text' value=''/></td>";
	}
	if (expectYearYield != null && expectYearYield != "") {
		html += "<td><input id='benefitType' class='ipt05' type='text' value='"+expectYearYield.benefitType+"'/></td>";
	} else {
		html += "<td><input id='benefitType' class='ipt05' type='text' value=''/></td>";
	}
	html += "<td><a class='opLink' onclick='saveExpectYearYield(this)'>确定</a>&nbsp;";
	html += "<a class='opLink' onclick='cancleEditExpectYearYield(this)'>取消</a></td>";
	html += "</tr>";
	return html;
}

function createExpectYearYieldHTML(expectYearYield) {
	var html = "";
	html += "<tr id='" + expectYearYield.id + "'>";
	html += "<td>" + expectYearYield.lowerLimit + "</td>";
	html += "<td>" + expectYearYield.upperLimit+ "</td>";
	html += "<td>" + expectYearYield.yearYield + "</td>";
	html += "<td>" + expectYearYield.benefitType + "</td>";
	html += "<td><input type='image'  src='themes/comm/images/crmEdit_ico.gif' title='修改' onclick='editExpectYearYield(this);'/>&nbsp;";
	html += "<input type='image'  src='themes/comm/images/crmDel_ico.gif' title='删除' onclick='delExpectYearYield(this);'/></td>";
	html += "</tr>";
	return html;
}

/************************预期年化收益率 end********************************/

/************************产品付息日期 start********************************/
function addProdPayDate() {
	if(editFlag){
		$.alert("请先保存");
		return;
	}
	var html = createEditProdPayDateHTML(null);
	$("#prodPayDate").append(html);
	$('.invokeBoth').attachDatepicker({showOn: 'both', buttonImage: 'themes/comm/spacer.gif', buttonImageOnly: true});

	editFlag = true;
	//按钮控制
	setCommitButtonDisabled(true);
}

function editProdPayDate(obj) {
	if(editFlag){
		$.alert("请先保存");
		return;
	}
	var TR = $(obj).parent().parent();
	var id = TR.attr("id");
	// 编辑前html
	prodPayDateHTML = "<tr id='"+id+"'>" + TR.html() +"</tr>";
	var expectPayDate = TR.find("td").eq(0).text();
	var actualPayDate = TR.find("td").eq(1).text();
	var prodPayDate = new Object();
	prodPayDate.id = id;
	prodPayDate.expectPayDate = expectPayDate;
	prodPayDate.actualPayDate = actualPayDate;
	
	var html = createEditProdPayDateHTML(prodPayDate);
	TR.replaceWith(html);
	$('.invokeBoth').attachDatepicker({showOn: 'both', buttonImage: 'themes/comm/spacer.gif', buttonImageOnly: true});

	editFlag = true;
	//按钮控制
	setCommitButtonDisabled(true);
}

function saveProdPayDate(obj) {
	var TR = $(obj).parent().parent();
	var id = TR.attr("id");
	var expectPayDate = TR.find("td").eq(0).find("input").eq(0).val();
	var actualPayDate = TR.find("td").eq(1).find("input").eq(0).val();
	var prodPayDate = new Object();
	prodPayDate.id = id;
	prodPayDate.expectPayDate = expectPayDate;
	prodPayDate.actualPayDate = actualPayDate;
	
	var html = createProdPayDateHTML(prodPayDate);
	TR.replaceWith(html);
	
	prodPayDateHTML = "";
	editFlag = false;
	//按钮控制
	setCommitButtonDisabled(false);
}

function cancleEditProdPayDate(obj) {
	var TR = $(obj).parent().parent();
	//原行显示
	TR.after(prodPayDateHTML);
	// 移除编辑行
	TR.remove();
	// 启用按钮
	prodPayDateHTML = "";
	editFlag = false;
	//按钮控制
	setCommitButtonDisabled(false);
}

function delProdPayDate(obj) {
	$.confirm("确定删除该产品付息日", function(result) {
		if (result) {
			var TR = $(obj).parent().parent();
			TR.remove();
		}
	});
}

function createEditProdPayDateHTML(prodPayDate) {
	var html = "";
	if (prodPayDate != null && prodPayDate != "") {
		html += "<tr id='"+prodPayDate.id+"'>";
	} else {
		html += "<tr>";
	}
	if (prodPayDate != null && prodPayDate != "") {
		html += "<td><input id='expectPayDate' readonly class='invokeBoth soTimeW' type='text' value='"+prodPayDate.expectPayDate+"'/></td>";
	} else {
		html += "<td><input id='expectPayDate' readonly class='invokeBoth soTimeW' type='text' value=''/></td>";
	}
	if (prodPayDate != null && prodPayDate != "") {
		html += "<td><input id='actualPayDate' readonly class='invokeBoth soTimeW' type='text' value='"+prodPayDate.actualPayDate+"'/></td>";
	} else {
		html += "<td><input id='actualPayDate' readonly class='invokeBoth soTimeW' type='text' value=''/></td>";
	}
	html += "<td><a class='opLink' onclick='saveProdPayDate(this)'>确定</a>&nbsp;";
	html += "<a class='opLink' onclick='cancleEditProdPayDate(this)'>取消</a></td>";
	html += "</tr>";
	return html;
}

function createProdPayDateHTML(prodPayDate) {
	var html = "";
	html += "<tr id='" + prodPayDate.id + "'>";
	html += "<td>" + prodPayDate.expectPayDate + "</td>";
	html += "<td>" + prodPayDate.actualPayDate+ "</td>";
	html += "<td><input type='image'  src='themes/comm/images/crmEdit_ico.gif' title='修改' onclick='editProdPayDate(this);'/>&nbsp;";
	html += "<input type='image'  src='themes/comm/images/crmDel_ico.gif' title='删除' onclick='delProdPayDate(this);'/></td>";
	html += "</tr>";
	return html;
}

/************************产品付息日期 end********************************/

function saveProdInfo() {
	// 
	setCommitButtonDisabled(true);
	if (!valiDate()) {
		setCommitButtonDisabled(false);
		return;
	}
	var prodInfoObj = getProdInfo();
	var url = BASE_PATH + "m/prod_info/add_prodInfo";
	if (prodInfoObj != null && prodInfoObj.id != "") {
		url = BASE_PATH + "m/prod_info/modify_prodInfo";
	}
	$.ajax({
        type : "POST",
		cache: false,
		url  : url,
		dataType : "json",
		data : {
			prodInfoJSON : $.toJSON(prodInfoObj)
		},
	    success : function(data,i){
        	if ($.checkErrorMsg(data)) {
        		$.alert("操作成功!", function() {
        			if ($.getMainFrame() != null && $.getMainFrame().getCurrentTab()!= null){
               			//刷新父列表
    		           	$.getMainFrame().getCurrentTab().doCallback();
               			$.getMainFrame().getCurrentTab().close();
               		} else {
               			setCommitButtonDisabled(false);
               		}
        		});
        	} else {
        		setCommitButtonDisabled(false);
        	}
        },
        error : $.ermpAjaxError
    });
}

function valiDate() {
	var prodCode = $.trim($("#prodCode").val());
	if (prodCode == "") {
		$.alert("产品代码不能为空");
		return false;
	}
	var prodName = $.trim($("#prodName").val());
	if (prodName == "") {
		$.alert("产品名称不能为空");
		return false;
	}
	
	var prodStatus = "";
	if (prodStatusSel != null) {
		prodStatus = prodStatusSel.getValue();
	}
	if ("STATUS_FOUND" != prodStatus) {
		//已成立产品，可为空
		var prodType = "";
		if (prodTypeSel != null) {
			prodType = prodTypeSel.getValue();
		}
		if (prodType == "") {
			$.alert("产品类型不能为空");
			return false;
		}
		var secondClassify = "";
		if (secondClassifySel != null) {
			secondClassify = secondClassifySel.getValue();
		}
		if (secondClassify == "") {
			$.alert("产品二级分类不能为空");
			return false;
		}
	}
	
//	var pjtTotalAmount = $.trim($("#pjtTotalAmount").val());
	if ($.validNumber("pjtTotalAmount", "项目总额度", true)) {
//		$.alert("项目总额度不能为空");
		return false;
	}
	
	if ($.validNumber("sellAmount", "发行额度", true)) {
		return false;
	}
	
	if ($.validNumber("totalAppointmentAmount", "预约划款合计", false)) {
		return false;
	}
	
	if ($.validNumber("transferAmount", "划款金额", false)) {
		return false;
	}
	
	if ($.validNumber("toAccountAmount", "到账金额", false)) {
		return false;
	}
	if ($.validNumber("toAccountSmallAmount", "到账小额数", false)) {
		return false;
	}
	if ($.validNumber("remainAmount", "项目剩余额度", false)) {
		return false;
	}
	
	if ($.validNumber("sellTimeLimit", "期限(月)", false)) {
		return false;
	}
	if ($.validNumber("accountCoefficient", "产品核算系数", false)) {
		return false;
	}
	
	return true;
}

function getProdInfo() {
	var prodInfoId = $.trim($("#prodInfoId").val());
	var prodCode = $.trim($("#prodCode").val());
	var prodName = $.trim($("#prodName").val());
	var prodType = "";
	if (prodTypeSel != null) {
		prodType = prodTypeSel.getValue();
	}
	var secondClassify = "";
	if (secondClassifySel != null) {
		secondClassify = secondClassifySel.getValue();
	}
	var financeCompanyName = $.trim($("#financeCompanyName").val());
	var prodStatus = "";
	if (prodStatusSel != null) {
		prodStatus = prodStatusSel.getValue();
	}
	var supplierId = "";
	if (supplierSel != null) {
		supplierId = supplierSel.getValue();
	}
	var pjtTotalAmount = $.trim($("#pjtTotalAmount").val());
	var pjtAmountRemark = $.trim($("#pjtAmountRemark").val());
	var sellAmount = $.trim($("#sellAmount").val());
	var sellTimeLimit = $.trim($("#sellTimeLimit").val());
	var timeLimitRemark = $.trim($("#timeLimitRemark").val());
	var sellRank = "";
	if (sellRankSel != null) {
		sellRank = sellRankSel.getValue();
	}
	var operationWarning = $.trim($("#operationWarning").val());
	var accountCoefficient = $.trim($("#accountCoefficient").val());
	var sellDate = $.trim($("#sellDate").val());
	var transferDeadline = $.trim($("#transferDeadline").val());
	var raiseFundsEndTime = $.trim($("#raiseFundsEndTime").val());
	var prodSetUpDate = $.trim($("#prodSetUpDate").val());
	var prodCashDate = $.trim($("#prodCashDate").val());
	var actualCashDate = $.trim($("#actualCashDate").val());
	var payInterestMethod = $.trim($("#payInterestMethod").val());
	var trustBank = $.trim($("#trustBank").val());
	var raiseBank = $.trim($("#raiseBank").val());
	var raiseAccount = $.trim($("#raiseAccount").val());
	var prodManager = $.trim($("#prodManager").val());
	var prodManagerTel = $.trim($("#prodManagerTel").val());
	var prodManagerEmail = $.trim($("#prodManagerEmail").val());
	var prodSupervisor = $.trim($("#prodSupervisor").val());
	var supervisorContactWay = $.trim($("#supervisorContactWay").val());
	var supervisorRemark = $.trim($("#supervisorRemark").val());
	var expectSellDate = $.trim($("#expectSellDate").val());
	var operationPeriod = $.trim($("#operationPeriod").val());
	var totalAppointmentAmount = $.trim($("#totalAppointmentAmount").val());
	var transferAmount = $.trim($("#transferAmount").val());
	var toAccountAmount = $.trim($("#toAccountAmount").val());
	var toAccountSmallAmount = $.trim($("#toAccountSmallAmount").val());
	var remainAmount = $.trim($("#remainAmount").val());
	var needGradeFlag = $("input[name=needGradeFlag][checked]").val();
	var redeemFlag = $("input[name=redeemFlag][checked]").val();
	var videoLectures = $.trim($("#videoLectures").val());
	var janeEditionAttch = null;
	var otherEditionAttch = null;
	if (janeEditionObj && janeEditionObj.getHasValueFlag()) {
		if (janeEditionObj.getTempDir() != "") {
			janeEditionAttch = new Object();
			janeEditionAttch.filePath = janeEditionObj.getTempDir();
			janeEditionAttch.displayName = janeEditionObj.getFileName();
		} else {
			janeEditionAttch = new Object();
		}
	}
	
	if (otherEditionObj && otherEditionObj.getHasValueFlag()) {
		if (otherEditionObj.getTempDir() != "") {
			otherEditionAttch = new Object();
			otherEditionAttch.filePath = otherEditionObj.getTempDir();
			otherEditionAttch.displayName = otherEditionObj.getFileName();
		} else {
			otherEditionAttch = new Object();
		}
	}
	
	var prodInfoObj = new Object();
	prodInfoObj.id = prodInfoId;
	prodInfoObj.prodCode = prodCode;
	prodInfoObj.prodName = prodName;
	prodInfoObj.prodType = prodType;
	prodInfoObj.prodSecondaryClassify = secondClassify;
	prodInfoObj.financeCompanyName = financeCompanyName;
	prodInfoObj.prodStatus = prodStatus;
	prodInfoObj.pjtTotalAmount = pjtTotalAmount;
	prodInfoObj.pjtAmountRemark = pjtAmountRemark;
	prodInfoObj.sellAmount = sellAmount;
	prodInfoObj.sellTimeLimit = sellTimeLimit;
	prodInfoObj.timeLimitRemark = timeLimitRemark;
	prodInfoObj.sellRank = sellRank;
	prodInfoObj.operationWarning = operationWarning;
	prodInfoObj.accountCoefficient = accountCoefficient;
	prodInfoObj.sellDate = sellDate;
	prodInfoObj.transferDeadline = transferDeadline;
	prodInfoObj.raiseFundsEndTime = raiseFundsEndTime;
	prodInfoObj.prodSetUpDate = prodSetUpDate;
	prodInfoObj.prodCashDate = prodCashDate;
	prodInfoObj.actualCashDate = actualCashDate;
	prodInfoObj.payInterestMethod = payInterestMethod;
	prodInfoObj.trustBank = trustBank;
	prodInfoObj.raiseBank = raiseBank;
	prodInfoObj.raiseAccount = raiseAccount;
	prodInfoObj.prodManager = prodManager;
	prodInfoObj.prodManagerTel = prodManagerTel;
	prodInfoObj.prodManagerEmail = prodManagerEmail;
	prodInfoObj.prodSupervisor = prodSupervisor;
	prodInfoObj.supervisorContactWay = supervisorContactWay;
	prodInfoObj.supervisorRemark = supervisorRemark;
	prodInfoObj.expectSellDate = expectSellDate;
	prodInfoObj.operationPeriod = operationPeriod;
	prodInfoObj.totalAppointmentAmount = totalAppointmentAmount;
	prodInfoObj.transferAmount = transferAmount;
	prodInfoObj.toAccountAmount = toAccountAmount;
	prodInfoObj.toAccountSmallAmount = toAccountSmallAmount;
	prodInfoObj.remainAmount = remainAmount;
	prodInfoObj.needGradeFlag = needGradeFlag;
	prodInfoObj.redeemFlag = redeemFlag;
	prodInfoObj.videoLectures = videoLectures;
	if (supplierId != "") {
		var supplier = new Object();
		supplier.id = supplierId;
		prodInfoObj.supplier = supplier;
	}
	if (janeEditionAttch != null) {
		prodInfoObj.janeEditionAttch = janeEditionAttch;
	}
	if (otherEditionAttch != null) {
		prodInfoObj.otherEditionAttch = otherEditionAttch;
	}
	
	var expectYearYieldArray = getExpectYearYieldInfo();
	var prodPayDateArray = getProdPayDateInfo();
	prodInfoObj.expectYearYields = expectYearYieldArray;
	prodInfoObj.prodPayDates = prodPayDateArray;
	
	return prodInfoObj;
	
}

function getExpectYearYieldInfo() {
	var expectYearYieldArray = new Array();
	$("#expectYearYield tr").each(function(i,value) {
		var id = $.trim($(value).attr("id"));
		var lowerLimit = $.trim($(value).find("td").eq(0).text());
		var upperLimit = $.trim($(value).find("td").eq(1).text());
		var yearYield = $.trim($(value).find("td").eq(2).text());
		var benefitType = $.trim($(value).find("td").eq(3).text());
		var expectYearYield = new Object();
		expectYearYield.id = id;
		expectYearYield.lowerLimit = lowerLimit;
		expectYearYield.upperLimit = upperLimit;
		expectYearYield.yearYield = yearYield;
		expectYearYield.benefitType = benefitType;
		expectYearYieldArray.push(expectYearYield);
	});
	return expectYearYieldArray;
}

function getProdPayDateInfo() {
	var prodPayDateArray = new Array();
	$("#prodPayDate tr").each(function(i,value) {
		var id = $.trim($(value).attr("id"));
		var expectPayDate = $.trim($(value).find("td").eq(0).text());
		var actualPayDate = $.trim($(value).find("td").eq(1).text());
		var prodPayDate = new Object();
		prodPayDate.id = id;
		prodPayDate.expectPayDate = expectPayDate;
		prodPayDate.actualPayDate = actualPayDate;
		prodPayDateArray.push(prodPayDate);
	});
	return prodPayDateArray;
}


function initSupplierSel() {
	if ($("#supplierSel").size() > 0) {
		supplierSel = $("#supplierSel").ySelect({
			width : 180,
			height: 150,
			url : BASE_PATH + "/m/supplier/query_supplierSel",
			afterLoad : function() {
				supplierSel.addOption("", "请选择...", 0);
				// 设置默认值
				var supplierIdValue = $("#supplierId").val();
				if (supplierIdValue != "") {
					supplierSel.select(supplierIdValue);
				} else {
					supplierSel.select(0);
				}
			},
			onChange : function(value, name) {
				// null
			}
		});
	}
}

function initProdTypeSel() {
	if ($("#prodTypeSel").size() > 0) {
		prodTypeSel = $("#prodTypeSel").ySelect({
			width : 180,
			height: 150,
			url : BASE_PATH + "/m/prod_type/query_prodTypeSel",
			data : {
				prodTypeID : ""
			},
			afterLoad : function() {
				prodTypeSel.addOption("", "请选择...", 0);
				// 设置默认值
				var prodTypeValue = $("#prodType").val();
				if (prodTypeValue != "") {
					prodTypeSel.select(prodTypeValue);
				} else {
					prodTypeSel.select(0);
				}
			},
			onChange : function(value, name) {
				var prodTypeId = value;
				// 初始化变更操作
				initSecondClassifySel(prodTypeId);
			}
		});
	}
}

function initSecondClassifySel(prodTypeId) {
	if (prodTypeId == "undefined" || prodTypeId == "") {
		prodTypeId = "";
	}
	if ($("#secondClassifySel").size() > 0) {
		if (prodTypeId == "undefined" || prodTypeId == "") {
			prodTypeId = "";
			secondClassifySel = $("#secondClassifySel").ySelect({
				width : 180,
				height: 150,
				afterLoad : function() {
					if (secondClassifySel != null) {
						secondClassifySel.addOption("", "请选择...", 0);
						// 设置默认值
						secondClassifySel.select(0);
					}
				},
				onChange : function(value, name) {
					// null
				}
			});
		} else {
			secondClassifySel = $("#secondClassifySel").ySelect({
				width : 180,
				height: 150,
				url : BASE_PATH + "/m/prod_type/query_prodTypeSel?prodTypeID=" + prodTypeId,
				afterLoad : function() {
					secondClassifySel.addOption("", "请选择...", 0);
					// 设置默认值
					var secondaryClassify = $("#secondaryClassify").val();
					if (secondaryClassify != "" && secondaryClassify != "undefined") {
						secondClassifySel.select(secondaryClassify);
					} else {
						secondClassifySel.select(0);
					}
				},
				onChange : function(value, name) {
					// null
				}
			});
		}
		
	}
}

function initProdStatusSel() {
	if ($("#prodStatusSel").size() > 0) {
		prodStatusSel = $("#prodStatusSel").ySelect({
			width : 180,
			height: 150,
			url : BASE_PATH + "/l/dict/initProdStatusSel",
			afterLoad : function() {
				prodStatusSel.addOption("", "请选择...", 0);
				// 设置默认值
				var prodStatusValue = $("#prodStatus").val();
				if (prodStatusValue != "") {
					prodStatusSel.select(prodStatusValue);
				} else {
					prodStatusSel.select(0);
				}
			},
			onChange : function(value, name) {
				if ("STATUS_FOUND" == value) {
					//选中已成立时，产品类型，产品二级分类置空
					prodTypeSel.select(0);
					prodTypeSel.disable(true);
					secondClassifySel.select(0);
					secondClassifySel.disable(true);
				} else {
					prodTypeSel.disable(false);
					secondClassifySel.disable(false);
				}
			}
		});
	}
}

function initSellRankSel() {
	if ($("#sellRankSel").size() > 0) {
		sellRankSel = $("#sellRankSel").ySelect({
			width : 180,
			height: 150,
			url : BASE_PATH + "/l/dict/initSellRankSel",
			afterLoad : function() {
				sellRankSel.addOption("", "请选择...", 0);
				// 设置默认值
				var sellRankValue = $("#sellRank").val();
				if (sellRankValue != "") {
					sellRankSel.select(sellRankValue);
				} else {
					sellRankSel.select(0);
				}
			},
			onChange : function(value, name) {
				// null
			}
		});
	}
}

function setCommitButtonDisabled(disabled) {
	if (disabled) {
		$(".allBtn").attr("disabled","true");
	} else {
		$(".allBtn").removeAttr("disabled");
	}
}
