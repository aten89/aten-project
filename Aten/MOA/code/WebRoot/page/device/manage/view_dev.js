var mainFrame = $.getMainFrame();
var loadedUseList = false;
var loadedAllotList = false;
var loadedRepairList = false;
var loadedScrapList = false;
var loadedUptLogList = false;
var loadedDevFlowLogList = false;

$(initEquipmentInfoView);
/**
 * 设备详情
 */
function initEquipmentInfoView() {
	var deviceID = $("#deviceID").val();
	//tab标签
    $("#crmTab li").each(function(i){
		$(this).click(function(){
		  	$(this).addClass("current").siblings().removeClass("current");
		  	$("#tab0" + i).show().siblings().hide();
		  	if ($(this).attr("id") == "liUseList") {
		  		if (loadedUseList == false) {
		  			//加载使用记录。未加载过才要加载
		  			getUseList(deviceID);
		  			loadedUseList = true;
		  		}
		  	} else if ($(this).attr("id") == "liAllotList") {
		  		if (loadedAllotList == false) {
		  			//加载调拨记录。未加载过才要加载
		  			getAllotList(deviceID);
		  			loadedAllotList = true;
		  		}
		  	} else if ($(this).attr("id") == "liRepairList") {
		  		if (loadedRepairList == false) {
		  			//加载维修记录。未加载过才要加载
		  			getRepairList(deviceID);
		  			loadedRepairList = true;
		  		}
		  	} else if ($(this).attr("id") == "liUptLogList") {
		  		if (loadedUptLogList == false) {
		  			//加载更新记录。未加载过才要加载
		  			getUptLogList(deviceID);
		  			loadedUptLogList = true;
		  		}
		  	} else if ($(this).attr("id") == "liDevFlowLogList") {
		  		if (loadedDevFlowLogList == false) {
		  			//加载设备流程记录。未加载过才要加载
		  			getDevFlowLogList(deviceID);
		  			loadedDevFlowLogList = true;
		  		}
		  	} 
		});
	});
}

/**
 * 获取领用记录列表
 */
function getUseList(deviceID) {
	$.ajax({
		type : "post",
		cache : false,
		url : BASE_PATH + "m/it_dev_man?act=loaduselist",
		data : {
			deviceID : deviceID
		},
		success : function(xml){
			var message = $("message",xml);
			var content = $("content",xml);
			var listData = "";
			//是否有数据返回
		    if (message.attr("code") == "1") {
		    	$(xml).find('document').each(function(index){
		    		var curELe = $(this);
                    var id = $.trim(curELe.attr("id"));
                    var refFormID = $.trim(curELe.attr("ref-form-id"));
                    listData += " <tr onmouseout=\"$(this).removeClass('over');\" onmouseover=\"$(this).addClass('over');\" id=\"" + id + "\">"
							+ "<td>" + $("applicant-display-name",curELe).text() + "</td>"
			  				+ "<td>" + $("apply-group-name",curELe).text() + "</td>"
							+ "<td>" + $("use-date",curELe).text() + "</td>"
			  				+ "<td>" + $("back-date",curELe).text() + "</td>"
							+ "<td>" + $("purpose",curELe).text() + "</td>"
							+ "<td><a class=\"linkOver\" onclick=\"viewDeviceUse('" + refFormID + "')\">查看设备领用单</a></td>"
							+ "</tr>";

				});
				$("#tbodyUse").html(listData);
		    }
		},
		error : $.ermpAjaxError
	});
}

/**
 * 查看设备领用单
 * @param {} id
 */
function viewDeviceUse(id){
	mainFrame.addTab({
		id:"oa_equipmentInfoUseView" + id,
		title:"设备领用单",
		url:BASE_PATH +"m/it_dev_man?act=viewuse&id=" + id
	});
}

/**
 * 查看设备申购单
 * @param {} id
 */
function viewDevicePurchase(id){
	mainFrame.addTab({
		id:"oa_equipmentInfoPurchaseView" + id,
		title:"设备领用单",
		url:BASE_PATH +"m/it_dev_man?act=viewuse&id=" + id
	});
}

/**
 * 获取调拨记录列表
 */
function getAllotList(deviceID) {
	$.ajax({
		type : "post",
		cache : false,
		url : BASE_PATH + "m/it_dev_man?act=loadallotlist",
		data : {
			deviceID : deviceID
		},
		success : function(xml){
			var message = $("message",xml);
			var content = $("content",xml);
			var listData = "";
			//是否有数据返回
		    if (message.attr("code") == "1") {
		    	$(xml).find('document').each(function(index){
		    		var curELe = $(this);
                    var id = $.trim(curELe.attr("id"));
                    var refFormID = $.trim(curELe.attr("ref-form-id"));
                    var outMsg = $("in-user-name",curELe).text() + "(" + $("in-group",curELe).text() + ")" + "/" + $("area-name",curELe).text() 
                    if($("move-type",curELe).text()=="IN_STORAGE" ){
                    	outMsg="";
                    }
                    listData += " <tr onmouseout=\"$(this).removeClass('over');\" onmouseover=\"$(this).addClass('over');\" id=\"" + id + "\">"
							+ "<td>" + $("move-type-display-name",curELe).text() + "</td>"
			  				+ "<td>" +outMsg+ "</td>"
							+ "<td>" + $("out-user-name",curELe).text() + "(" + $("out-group",curELe).text() + ")" + "/" + $("area-name-bef",curELe).text() + "</td>"
							+ "<td>" + $("move-date",curELe).text() + "</td>"
							+ "<td><a class=\"linkOver\" onclick=\"viewDeviceAllot('" + refFormID + "')\">查看设备调拨单</a></td>"
							+ "</tr>";
				});
				$("#tbodyAllot").html(listData);
		    }
		},
		error : $.ermpAjaxError
	});
}

/**
 * 查看设备调拨单
 * @param {} id
 * @return {Boolean}
 */

function viewDeviceAllot(id){
	mainFrame.addTab({
		id:"oa_equipmentAllotView" + id,
		title:"设备调拨单",
		url:BASE_PATH +"m/it_dev_man?act=viewallot&id=" + id
	});
}

/**
 * 加载维修单
 */
function getRepairList(deviceID) {
	$.ajax({
		type : "post",
		cache : false,
		url : BASE_PATH + "m/it_dev_man?act=loadrepairlist",
		data : {
			deviceID : deviceID
		},
		success : function(xml){
			var message = $("message",xml);
			var content = $("content",xml);
			var listData = "";
			//是否有数据返回
		    if (message.attr("code") == "1") {
		    	$(xml).find('document').each(function(index){
		    		var curELe = $(this);
                    var id = $.trim(curELe.attr("id"));
                    listData += " <tr onmouseout=\"$(this).removeClass('over');\" onmouseover=\"$(this).addClass('over');\" id=\"" + id + "\">"
							+ "<td>" + $("applicant-display-name",curELe).text() + "</td>"
			  				+ "<td>" + $("apply-group-name",curELe).text() + "</td>"
							+ "<td>" + $("apply-date",curELe).text() + "</td>"
			  				+ "<td>" + $("budget-money",curELe).text() + "</td>"
							+ "<td><a class=\"linkOver\" onclick=\"viewDeviceRepair('" + id + "')\">查看设备维修单</a></td>"
							+ "</tr>";

				});
				$("#tbodyRepair").html(listData);
		    }
		},
		error : $.ermpAjaxError
	});
}

/**
 * 查看设备维修单
 * @param {} id
 */
function viewDeviceRepair(id){
	mainFrame.addTab({
		id:"oa_equipmentRepairView" + id,
		title:"设备维修单",
		url:BASE_PATH +"m/it_dev_man?act=viewrepair&id=" + id
	});
}

///**
// * 获取设备报废列表
// */
//function getScrapList(deviceID) {
//	$.ajax({
//		type : "post",
//		cache : false,
//		url : BASE_PATH + "m/it_dev_man?act=loadscraplist",
//		data : {
//			deviceID : deviceID
//		},
//		success : function(xml){
//			var message = $("message",xml);
//			var content = $("content",xml);
//			var listData = "";
//			//是否有数据返回
//		    if (message.attr("code") == "1") {
//		    	$(xml).find('document').each(function(index){
//		    		var curELe = $(this);
//                    var id = $.trim(curELe.attr("id"));
//                    var refFormID = $.trim(curELe.attr("ref-form-id"));
//                    listData += " <tr onmouseout=\"$(this).removeClass('over');\" onmouseover=\"$(this).addClass('over');\" id=\"" + id + "\">"
//							+ "<td>" + $("applicant-display-name",curELe).text() + "</td>"
//			  				+ "<td>" + $("apply-group-name",curELe).text() + "</td>"
//							+ "<td>" + $("apply-date",curELe).text() + "</td>"
//			  				+ "<td>" + $("discard-type-display-name",curELe).text() + "</td>"
//			  				+ "<td>" + $("deal-type-display-name",curELe).text() + "</td>"
//							+ "<td>" + $("deal-date",curELe).text() + "</td>"
//							+ "<td><a class=\"linkOver\" onclick=\"viewDeviceScrap('" + refFormID + "')\">查看设备报废单</a></td>"
//							+ "</tr>";
//				});
//				$("#tbodyScrap").html(listData);
//		    }
//		},
//		error : $.ermpAjaxError
//	});
//}

/**
 * 查看设备报废单
 * @param {} id
 */
function viewDeviceScrap(id) {
	mainFrame.addTab({
		id:"oa_equipmentScrapView" + id,
		title:"设备报废单",
		url:BASE_PATH +"m/it_dev_man?act=viewscrap&id=" + id
	});
}

/**
 * 查看设备报废处理单
 * @param {} id
 */
function viewDeviceScrapDispose(id) {
	mainFrame.addTab({
		id:"oa_equipmentScrapDisposeView" + id,
		title:"设备报废处理单",
		url:BASE_PATH +"m/it_dev_man?act=viewscrapdispose&id=" + id
	});
}

/**
 * 获取设备更新列表
 */
function getUptLogList(deviceID) {
	$.ajax({
		type : "post",
		cache : false,
		url : BASE_PATH + "m/it_dev_man?act=loaduptloglist",
		data : {
			deviceID : deviceID
		},
		success : function(xml){
			var message = $("message",xml);
			var content = $("content",xml);
			var listData = "";
			//是否有数据返回
		    if (message.attr("code") == "1") {
		    	$(xml).find('document').each(function(index){
		    		var curELe = $(this);
                    var id = $.trim(curELe.attr("id"));
                    listData += " <tr onmouseout=\"$(this).removeClass('over');\" onmouseover=\"$(this).addClass('over');\" id=\"" + id + "\">"
							+ "<td>" + $("opterator-display-name",curELe).text() + "</td>"
							+ "<td>" + $("opt-date",curELe).text() + "</td>"
			  				+ "<td>" + $("opt-type-display-name",curELe).text() + "</td>"
			  				+ "<td>" + $("opt-update-uontent",curELe).text() + "</td>"
							+ "</tr>";
				});
				$("#tbodyUptLog").html(listData);
		    }
		},
		error : $.ermpAjaxError
	});
}

/**
 * 获取设备更新列表
 */
function getDevFlowLogList(deviceID) {
	$.ajax({
		type : "post",
		cache : false,
		url : BASE_PATH + "m/it_dev_man?act=loaddevflowloglist",
		data : {
			deviceID : deviceID
		},
		success : function(xml){
			//$.alert($(xml).get(0).xml)
			var message = $("message",xml);
			var content = $("content",xml);
			var listData = "";
			//是否有数据返回
		    if (message.attr("code") == "1") {
		    	$(xml).find('document').each(function(index){
		    		var strViewTitle = "";
		    		var curELe = $(this);
                    var id = $.trim(curELe.attr("id"));
                    var refFormID = $("ref-form-id",curELe).text();
                    var optType = $("opt-type",curELe).text();
                    var formNO = $("form-no",curELe).text();
                    switch (optType) {
                    	case "0" : 
                    	case "1" :
                    	case "6" :
                    		strViewTitle = "设备领用单";
                    		if (refFormID != "") {
                    			strViewTitle = "设备调拨单";
                    			id = refFormID;
                    		}
                    		break;
                    	case "2" :
                    		strViewTitle = "设备维修单";
                    		break;
                    	case "3" :
                    		strViewTitle = "设备报废单";
	                    	break;
	                    case "5" :
	                    	strViewTitle = "设备调拨单";
	                    	break;
	                    case "8" :
	                    	strViewTitle = "设备申购单";
	                    	break;
	                    case "9" :
	                    	strViewTitle = "离职处理单";
	                    	break;
                    }
                    listData += " <tr onmouseout=\"$(this).removeClass('over');\" onmouseover=\"$(this).addClass('over');\" id=\"" + id + "\">"
							+ "<td>" + $("operator-display-name",curELe).text() + "</td>"
							+ "<td>" + $("operator-group-name",curELe).text() + "</td>"
							+ "<td>" + $("opt-date",curELe).text() + "</td>"
			  				+ "<td>" + (optType == "8" ? "领用" : $("opt-type-display-name",curELe).text()) + "</td>"
			  				+ "<td>" + $("remark",curELe).text() + "</td>"
			  				+ "<td><a class=\"linkOver\" onclick=\"viewForm('" + optType + "','" + id + "','" + refFormID + "','" + strViewTitle + "','" + formNO + "')\">" + "查看" + strViewTitle + "</a>";
			  		if (optType == "3" && refFormID != "") {
			  			listData += "<br/><a class=\"linkOver\" onclick=\"viewForm('4','" + id + "','" + refFormID + "', '报废处理单', '" + formNO + "')\">查看报废处理单</a>";
			  		}
			  		listData += "</td></tr>";
				});
				$("#tbodyDevFlowLog").html(listData);
		    }
		},
		error : $.ermpAjaxError
	});
}

//查看
function openDetail(formType, formID, title,formNO){
	mainFrame.addTab({
		id:"oa_equipmentArchHandle" + formID,
		title:title + formNO,
		url:BASE_PATH +"m/dev_arch?act=view&formId=" + formID+"&formType="+formType
	});
}

function viewForm(optType, id, refFormID, title, formNO) {
	switch (optType) {
    	case "0" :
    		if (refFormID != "") {
    			openDetail(2, refFormID, title, formNO);
    		} else {
    			openDetail(0, id, title, formNO);
    		}
    		break;
    	case "1" :
    		openDetail(2, refFormID, title, formNO);
    		break;
    	case "2" :
    		viewDeviceRepair(id, title, formNO);
    		break;
    	case "3" :
        	openDetail(3, id, title, formNO);
        	break;
        case "4" :
        	viewDeviceScrapDispose(refFormID, title, formNO);
        	break;
        case "5" :
        	openDetail(2, id, title, formNO);
        	break;
        case "6" :
        	openDetail(2, id, title, formNO);
        	break;
        case "8" :
        	//申购
        	openDetail(1, id, title, formNO);
        	break;
        case "9" :
        	//离职处理
        	openDetail(4, id, title, formNO);
        	break;
    }
}