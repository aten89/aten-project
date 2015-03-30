var mainFrame = $.getMainFrame();
var columnSortObj;//表头列排序对像
var classSelect;
var areaCode="";//区域代码
var deviceTypeCode;//设备类型代码
var deviceStatusSel;
var deviceUseSel;  //是否领用
var groupSelect;//使用部门
$(initEquipmentInfoList);
/**
 * 初始化
 */
function initEquipmentInfoList() {
	//初始化表头列排序
    columnSortObj = $("#tableTH").columnSort();
	areaCode = $.trim($("#areaCode").val()); 
	deviceTypeCode = $.trim($("#deviceTypeCode").val());
	initDeviceStatusSel();
	initClassDiv();
	deviceUseSel=$("#deviceUseredSel").ySelect({width:80});
        //添加权限约束
	$.handleRights({
		"register" : $.OaConstants.REGISTER,//登记
		"btnDeviceUse" : $.OaConstants.RECIPIENTS,//领用
		"allocation" : $.OaConstants.ALLOCATION,//调拨
		"maintain" : $.OaConstants.MAINTAIN,//维修
		"scrap" : $.OaConstants.SCRAP,//报废
		"scrapdeal" : $.OaConstants.SCRAPDEAL,//报废处理
		"leaveDeal" : $.OaConstants.SCRAP,//离职处理
		"queryDevice" : $.SysConstants.QUERY//查询
	});
	
	//登记
	$("#register").click(function () {
		equipmentAdd();
	});
	//调拨
	$("#allocation").click(function () {
		equipmentAllot(deviceTypeCode, areaCode);
	});
	$("#scrap").click(function(){
		equipmentScrap(deviceTypeCode, areaCode);
	});
	initGroupSel();
	 //打开用户帐号
	$("#openUserSelect").click(
	   	function(e){
			var dialog = new UserDialog(ERMP_PATH, BASE_PATH);
			dialog.setCallbackFun(function(user){
				if (user != null) {
					$("#userId").val(user.id);
	   				$("#applicantName").val(user.name);
				}
			});
			dialog.openDialog("single");
		}
	);
	//绑定查询按钮
	$("#queryDevice").click(function () {
		gotoPage(1);
	});
	gotoPage(1);
	
	//维修
	$("#maintain").click(function(){
		equipmentRepair(deviceTypeCode, areaCode);
	});
	
	$("#btnDeviceUse").click(function(){
		deviceUse(deviceTypeCode, areaCode);
	});
	//刷新
	$("#refresh").click(function(){
		queryList();
	});
	$("#scrapdeal").click(function(){
		devDiscardDispose(deviceTypeCode, areaCode);
	});
	$("#leaveDeal").click(function(){
		leaveDeal(deviceTypeCode, areaCode);
	});
	$.EnterKeyPress($("#deviceNO,#deviceName,#deviceModel"),$("#queryDevice"));
}

/**
 * 设备领用
 */
function deviceUse(deviceTypeCode, areaCode, deviceID) {
	var showMsg="";
	if(deviceID!=null && deviceID!=""){
		var msg = msgSuccess(deviceID);
		if(msg!=""){
//			showMsg="该设备目前正在"+msg+"，如果进行“领用登记”操作系统将自动终止该流程，您仍要进行该操作吗？";
//			if(!confirm(showMsg)){
//				return ;
//			}
			showMsg="该设备目前正在"+msg+"，您当前不能进行“领用登记”操作。";
			$.alert(showMsg);
			return;
		}
	}
	
	var sURL = BASE_PATH + "m/it_dev_man?act=initpurchase";
	if (deviceTypeCode != null) {
		sURL += "&deviceTypeCode=" + deviceTypeCode;
	}
	if (areaCode != null) {
		sURL += "&areaCode=" + areaCode;
	}
	if (deviceID != null && deviceID != "") {
		sURL += "&deviceID="+deviceID;
	}
	
	mainFrame.addTab({
		id:"oa_device_man_reci"+Math.floor(Math.random() * 1000000),
		title:"领用申购登记", 
		url:sURL,
		callback:queryList
	});
}
/**
 * 设备状态
 */
function initDeviceStatusSel() {
	deviceStatusSel = $("#deviceStateSel").ySelect({width:80, url:"m/it_dev_man?act=getstatusselect",
		afterLoad:function(){
			deviceStatusSel.addOption("", "所有....", 0);
			deviceStatusSel.select(0);
		}
	});
}
//使用部门
function initGroupSel(){
	var html="<div>**所有...</div>";
	$.getJSON(ERMP_PATH + "m/rbac_group/groupselect?type=D&jsoncallback=?",function(data){
		html+= data.htmlValue;
		$("#groupIdDiv").html(html);
	    groupSelect = $("#groupIdDiv").ySelect({width:80});
	});
	return false;
}
/**
 * 设备类别
 */
function initClassDiv() {
	var deviceType = $("#deviceTypeCode").val();
	classSelect = $("#deviceClassDiv").ySelect({width:80, url:"m/device_class?act=classsselassign&deviceType="+deviceType+"&assignType=0&areaCode="+areaCode,
		afterLoad:function(){
			classSelect.addOption("", "所有....", 0);
			classSelect.select(0);
		}
	});
}
//详情
function openDetail(id){
	mainFrame.addTab({
		id:"oa_equipmentInfo"+id,
		title:"查看设备信息",
		url:BASE_PATH +"m/it_dev_man?act=view&id="+id
	});
}

//新增
function equipmentAdd(){
	mainFrame.addTab({
		id:"oa_device_man_add"+Math.floor(Math.random() * 1000000),
		title:"设备入库登记", 
		url:BASE_PATH + "m/it_dev_man?act=initadd&deviceTypeCode="+deviceTypeCode+"&areaCode="+areaCode,
		callback:queryList
	});
}

//设备报废
function equipmentScrap(deviceTypeCode, areaCode, deviceID){
	if(deviceID!=null && deviceID!=""){
		var msg = msgSuccess(deviceID);
		if(msg!=""){
//			showMsg="该设备目前正在"+msg+"，如果进行“报废登记”操作系统将自动终止该流程，您仍要进行该操作吗？";
//			if(!confirm(showMsg)){
//				return ;
//			}
			showMsg="该设备目前正在"+msg+"，您当前不能进行“报废登记”操作。";
			$.alert(showMsg);
			return;
		}
	}
	var sURL = BASE_PATH + "m/it_dev_man?act=initdiscard";
	if (deviceTypeCode != null) {
		sURL += "&deviceTypeCode=" + deviceTypeCode;
	}
	if (areaCode != null) {
		sURL += "&areaCode=" + areaCode;
	}
	if (deviceID != null) {
		sURL += "&deviceID=" + deviceID;
	}
	mainFrame.addTab({
		id:"oa_device_man_scra"+Math.floor(Math.random() * 1000000),
		title:"设备报废登记", 
		url:sURL,
		callback:queryList
	});
//	
//	var ok;
//	if($.browser.msie && $.browser.version==6.0){
//		 ok =$.showModalDialog(sURL, "", "dialogHeight:386px;dialogWidth:816px;status:no;scroll:auto;help:no");
//	}else{
//		 ok =$.showModalDialog(sURL, "", "dialogHeight:331px;dialogWidth:808px;status:no;scroll:auto;help:no");
//	}
//	if (ok) {
//		queryList();
//	}
}

/**
 * 设备维修 
 * @param {} deviceTypeCode 设备类型代码
 * @param {} areaCode 区域代码
 */
function equipmentRepair(deviceTypeCode, areaCode, deviceID){
	var sURL = BASE_PATH + "m/it_dev_man?act=initmaintain";
	if (deviceTypeCode != null) {
		sURL += "&deviceTypeCode=" + deviceTypeCode;
	}
	if (areaCode != null) {
		sURL += "&areaCode=" + areaCode;
	}
	if (deviceID != null) {
		sURL += "&deviceID=" + deviceID;
	}
//	var sFeature = "dialogHeight:331px;dialogWidth:808px;status:no;scroll:auto;help:no";
//	if($.browser.msie && $.browser.version==6.0){
//		 sFeature = "dialogHeight:386px;dialogWidth:816px;status:no;scroll:auto;help:no";
//	}
	mainFrame.addTab({
		id:"oa_device_man_main"+Math.floor(Math.random() * 1000000),
		title:"设备维修登记", 
		url:sURL,
		callback:queryList
	});
	
//	var result = $.showModalDialog(sURL, "", sFeature);
//	if (result) {
//		queryList();
//	}
}

//设备调拨
function equipmentAllot(deviceTypeCode, areaCode, deviceID){
	if(deviceID!=null && deviceID!=""){
		var msg = msgSuccess(deviceID);
		if(msg!=""){
//			showMsg="该设备目前正在"+msg+"，如果进行“调拨登记”操作系统将自动终止该流程，您仍要进行该操作吗？";
//			if(!confirm(showMsg)){
//				return ;
//			}
			showMsg="该设备目前正在"+msg+"，您当前不能进行“调拨登记”操作。";
			$.alert(showMsg);
			return;
		}
	}
	var sURL = BASE_PATH + "m/it_dev_man?act=initallot";
	if (deviceTypeCode != null) {
		sURL += "&deviceTypeCode=" + deviceTypeCode;
	}
	if (areaCode != null) {
		sURL += "&areaCode=" + areaCode;
	}
	if (deviceID != null) {
		sURL += "&deviceID=" + deviceID;
	}
	mainFrame.addTab({
		id:"oa_device_man_allo"+Math.floor(Math.random() * 1000000),
		title:"设备调拨登记", 
		url:sURL,
		callback:queryList
	});
}

/**
 * 分页
 **/
function gotoPage(pageNo, totalPage){
    if(isNaN(pageNo)){
        $.alert("页索引只能为数字！");
        return;
    } else if(pageNo < 1){
        pageNo = 1;
    }
    if (totalPage && !isNaN(totalPage) && pageNo > totalPage) {
    	pageNo = totalPage;
    }
    $("#hidNumPage").val(pageNo);
    queryList();
};
/**
 * 查询列表信息
 */
function queryList() {
	$("#DeviceList > tbody").empty();
	//分页
	var sortCol = columnSortObj.getSortColumn();
	var ascend = columnSortObj.getAscend();
  	var pageno = $.trim($("#hidNumPage").val());
  	var pagecount = $.trim($("#hidPageCount").val());
	
	var deviceName = $.trim($("#deviceName").val());
	var deviceNO = $.trim($("#deviceNO").val());
	var areaCode = $.trim($("#areaCode").val());
	var status = deviceStatusSel.getValue();
	var deviceClass = classSelect.getValue();
	var isUsered = deviceUseSel.getValue();
    var buyTimeStart = $.trim($("#buyTimeStart").val());
    var buyTimeEnd = $.trim($("#buyTimeEnd").val());
   	var groupName = "";
   	if(typeof(groupSelect)!="undefined"){
   		groupName = groupSelect.getDisplayValue();
   	}
  	if(groupName=="请选择..."||groupName==undefined||groupName=="所有...") groupName="";
   	var userId = $.trim($("#userId").val());
   	
    if ( $.compareDate(buyTimeStart, buyTimeEnd)) {
		$.alert("开始日期不能大于结束日期！");
		return false;
	}
	var deviceModel = $.trim($("#deviceModel").val());
	$.ajax({type:"POST",
		 cache:false, 
		 url:"m/it_dev_man", 
		 data:{
			 act:"query",
			 deviceType : deviceTypeCode,
			 areaCode:areaCode,
			 deviceClass:deviceClass,
			 deviceNO : deviceNO,
			 deviceName : deviceName,
			 status : status,
			 buyTimeStart : buyTimeStart,
			 buyTimeEnd : buyTimeEnd,
			 isUsered : isUsered,
			 userId : userId,
			 groupName:groupName,
			 deviceModel:deviceModel,
			 sortCol : sortCol,
			 ascend : ascend,
			 pageNo : pageno,
			 pageSize : pagecount
		 }, 
		 success:function (xml) {
            //解析XML中的返回代码
		var message = $("message", xml);
		if (message.attr("code") == "1") {
			var tBodyHTML = "";
			$("device", xml).each(function (index) {
				var deviceConfig = $(this);
				tBodyHTML += createTR($.trim($("device-type", deviceConfig).text()), 
					$.trim($("area-code", deviceConfig).text()),  
					$.trim(deviceConfig.attr("id")),
					$.trim($("device-name", deviceConfig).text()), 
					$.trim($("device-no", deviceConfig).text()),
					$.trim($("device-model", deviceConfig).text()), 
					$.trim($("device-class-str", deviceConfig).text()), 
					$.trim($("status", deviceConfig).text()), 
					$.trim($("status-str", deviceConfig).text()), 
					$.trim($("buy-type", deviceConfig).text()),
					$.trim($("buy-time", deviceConfig).text()),
					$.trim($("device-user", deviceConfig).text()),
					$.trim($("approve-type", deviceConfig).text()),
					$.trim($("approve-type-str", deviceConfig).text()));
			});
			
			$("#DeviceList > tbody").html(tBodyHTML).find("td:empty").html("&nbsp;");   
			
	        //------------------翻页数据--------------------------
            $(".pageNext").html($.createTurnPage(xml));
            $.EnterKeyPress($("#numPage"),$("#numPage").next());
            
		} else {
			$.alert($("message", xml).text());
		}
	}, error:$.ermpAjaxError});
}
/**
 * HTML 构造器
 */
function createTR(deviceTypeCode, areaCode, deviceId,no, name ,model, className,status, statusStr, 
		buyType, buyTime,users, approveType, approveTypeStr) {
	var html = "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">"
	 + "<td>" + name + "</td>" 
	 + "<td>" + no + "</td>" 
	 + "<td>" + model + "</td>" 
	 + "<td>" + className + "</td>"
	 + "<td>" + statusStr + "</td>"
	 + "<td>" + buyTime + "</td>" 
	 + "<td>" + ((users=="")?"<font color='red'>[闲置]</font>": users) + "</td>";
	 
	var op =  "";
		op += ( $.hasRight($.SysConstants.VIEW)?"<a href=\"javascript:void(0);\" onclick=\"openDetail('"+ deviceId +"')\"  class=\"opLink\">查看</a> | ":"");
		op += ( $.hasRight($.SysConstants.MODIFY)&& getRightFlag(7, status, users, buyType)?"<a href=\"javascript:void(0);\" onclick=\"modifyDevice('"+ deviceId +"')\"  class=\"opLink\">修改</a> | ":"");
		op += ( $.hasRight($.SysConstants.DELETE) ?"<a href=\"javascript:void(0);\" onclick=\"delDevice('"+ deviceId +"',"+status+",'"+users+"')\"  class=\"opLink\">删除</a> | ":"");
		op += ( $.hasRight($.OaConstants.RECIPIENTS) && getRightFlag(1, status, users, buyType) ?"<a href=\"javascript:void(0);\" onclick=\"deviceUse('" + deviceTypeCode + "','" + areaCode + "','"+ deviceId +"')\"  class=\"opLink\">领用登记</a> | ":"");
		op += ( $.hasRight($.OaConstants.ALLOCATION) && getRightFlag(2, status, users, buyType) ?"<a href=\"javascript:void(0);\" onclick=\"equipmentAllot('" + deviceTypeCode + "','" + areaCode + "','"+ deviceId +"')\"  class=\"opLink\">调拨登记</a> | ":"");
		op += ( $.hasRight($.OaConstants.SCRAP) && getRightFlag(3, status, users, buyType) ?"<a href=\"javascript:void(0);\" onclick=\"equipmentScrap('" + deviceTypeCode + "','" + areaCode + "','"+ deviceId +"','" + status + "','" + users + "','" + approveType + "','" + approveTypeStr + "')\"  class=\"opLink\">报废登记</a> | ":"");
		op += ( $.hasRight($.OaConstants.SCRAPDEAL) && getRightFlag(4, status, users, buyType) ?"<a href=\"javascript:void(0);\" onclick=\"devDiscardDispose('" + deviceTypeCode + "','" + areaCode + "','"+ deviceId +"')\"  class=\"opLink\">报废处理登记</a> | ":"");
		op += ( $.hasRight($.OaConstants.MAINTAIN) && getRightFlag(5, status, users, buyType) ? "<a href=\"javascript:void(0);\" onclick=\"equipmentRepair('" + deviceTypeCode + "','" + areaCode + "','"+ deviceId +"')\"  class=\"opLink\">维修登记</a> | ":"");
		//op += ( $.hasRight($.SysConstants.VIEW) && getRightFlag(6, status, users) ?"<a href=\"javascript:void(0);\" onclick=\"delDevice('"+ deviceId +"')\"  class=\"opLink\">离职处理登记</a> | ":"");
		
		//op += ( $.hasRight($.OaConstants.RECIPIENTS) ?"<a href=\"javascript:void(0);\" onclick=\"deviceUse('" + deviceTypeCode + "','" + areaCode + "','"+ deviceId +"','" + approveTypeStr + "')\"  class=\"opLink\">领用登记</a> | ":"");
		//op += ( $.hasRight($.OaConstants.ALLOCATION) ?"<a href=\"javascript:void(0);\" onclick=\"equipmentAllot('" + deviceTypeCode + "','" + areaCode + "','"+ deviceId + "','" + status + "','" + users + "','" + approveType + "','" + approveTypeStr + "')\"  class=\"opLink\">调拨登记</a> | ":"");
		//op += ( $.hasRight($.OaConstants.SCRAP) ?"<a href=\"javascript:void(0);\" onclick=\"equipmentScrap('" + deviceTypeCode + "','" + areaCode + "','"+ deviceId + "','" + status + "','" + users + "','" + approveType + "','" + approveTypeStr + "')\"  class=\"opLink\">报废登记</a> | ":"");
		//op += ( $.hasRight($.OaConstants.SCRAPDEAL) ?"<a href=\"javascript:void(0);\" onclick=\"devDiscardDispose('" + deviceTypeCode + "','" + areaCode + "','"+ deviceId + "','" + status + "','" + users + "','" + approveType + "','" + approveTypeStr + "')\"  class=\"opLink\">报废处理登记</a> | ":"");
		
	html += "<td>" + op.substring(0, op.lastIndexOf(" | ")) + " </td> </tr>";
	return html;
}

/**
 * 
 * @param {} assignType 授权类型
 * @param {} status 表单状态
 * @param {} users 使用人
 * @return {Boolean}
 */
function getRightFlag(assignType, status, users, buyType) {
	switch(assignType) {
		case 1: //领用
			if (status == "1" && users == "") {
				//闲置的未删除的才可以
				return true;
			}
			return false;
		case 2://调拨
			if (status == "1" && users != "") {
				//正常的才可以
				return true;
			}
			return false;
		case 3://报废
			if (status != "6" && status != "7" && status != "8" && 
					status != "4" && status != "5" && status != "14" && buyType != "BUY-TYPE-SELF") {
				//只要不是已回购、已报废、已丢失的，其余的情况下都可以
				return true;
			}
			return false;
		case 5://维修
			if (status == "1") {
				//正常，且不在审批当中才可以
				return true;
			}
			return false;
		case 6://离职处理
			if (status == "1" && users != "") {
				//正常，且不在审批当中才可以
				return true;
			}
			return false;
		case 4://报废处理
			if (status == "7" || status == "9") {
				//报废未处理的才可以
				return true;
			}
			return false;
		case 7://修改
			if (status == "1") {
				//正常，且不在审批当中
				return true;
			}
			return false;
	}
}

/**
 * 
 * @param {} assignType 授权类型
 * @param {} status 表单状态
 * @param {} users 使用人
 * @return {Boolean}
 */
/*
function getRightFlag(assignType, status, users, approveType) {
	switch(assignType) {
		case 1: //领用
			if (status == "1" && users == "" && approveType == "") {
				//闲置的未删除的才可以
				return true;
			}
			return false;
		case 2://调拨
			if (status == "1" && users != "" && approveType == "") {
				//正常的才可以
				return true;
			}
			return false;
		case 3://报废
			if (status == "1") {
				//正常的才可以
				return true;
			}
			return false;
		case 5://维修
			if (status == "1" && approveType == "") {
				//正常，且不在审批当中才可以
				return true;
			}
			return false;
		case 6://离职处理
			if (status == "1" && users != "" && approveType == "") {
				//正常，且不在审批当中才可以
				return true;
			}
			return false;
		case 4://报废处理
			if (status == "7" || status == "9") {
				//报废未处理的才可以
				return true;
			}
			return false;
		case 7://修改
			if (status == "1" && approveType == "") {
				//正常，且不在审批当中
				return true;
			}
			return false;
	}
}
*/
//新增
function modifyDevice(id){
	if(id!=null && id!=""){
		var msg = msgSuccess(id);
		if(msg!=""){
//			showMsg="该设备目前正在"+msg+"，如果进行“离职处理登记”操作系统将自动终止该流程，您仍要进行该操作吗？";
//			if(!confirm(showMsg)){
//				return ;
//			}
			showMsg="该设备目前正在"+msg+"，您当前不能进行“修改”操作。";
			$.alert(showMsg);
			return;
		}
	}
	mainFrame.addTab({
		id:"oa_device_man_add"+Math.floor(Math.random() * 1000000),
		title:"设备入库修改", 
		url:BASE_PATH + "m/it_dev_man?act=initmodify&id=" + id,
		callback:queryList
	});
	
//	var ok;
//	if($.browser.msie && $.browser.version==6.0){
//		ok = $.showModalDialog(BASE_PATH + "/m/it_dev_man?act=initmodify&id=" + id,"","dialogHeight:663px;dialogWidth:816px;status:no;scroll:auto;help:no");
//	}else{
//		ok = $.showModalDialog(BASE_PATH + "/m/it_dev_man?act=initmodify&id=" + id,"","dialogHeight:608px;dialogWidth:808px;status:no;scroll:auto;help:no");
//	}
//	if(ok){
//		queryList();
//	}
}
function getStatusFlag(status,users) {
	switch(status) {
		case 1: //领用
			if ( users != "") {
				return "该设备已被"+users+"领用";
			}else{
				return "";
			}
		case 2://维修
				return "该设备已维修";
		case 3://入库未验收
			return "该设备已入库未验收";
		case 4://离职回购
			return "该设备已离职回购";
		case 5://离职赠送
			return "该设备已离职赠送";
		case 6://拿走
			return "该设备已回购";
		case 7://离职处理
			return "该设备已报废未处理";
		case 8://报废已处理
			return "该设备已报废已处理";
		case 9://外借
			return "该设备已外借";
		case 10://领用审批中
			return "该设备在领用审批中";
		case 11://调拨审批中
			return "该设备在调拨审批中";
		case 12://报废审批中
			return "该设备在报废审批中";
		case 13://离职审批中
			return "该设备在离职审批中";
		case 14://丢失
			return "该设备已丢失";
	}
}
/**
 * 删除信息
 */
function delDevice(id,status,users) {
//	var msg=getStatusFlag(status,users);
	var showMsg="";
	var msg = msgSuccess(id);
	if(msg!=""){
//		showMsg="该设备目前正在"+msg+"，如果删除该设备系统将自动终止该流程，您仍要进行该操作吗？";
		showMsg="该设备目前正在"+msg+"，您当前不能删除该设备。";
		$.alert(showMsg);
		return;
	}else{
		showMsg="确认要删除该设备吗？";
	}
	$.confirm(showMsg, function(r){
		if (r) {
			$.ajax({type:"POST",
				 cache:false, 
				 url:"m/it_dev_man", 
				 data:{
					 act:"delete",
					 id:id
				 }, 
				 success:function (xml) {
		            //解析XML中的返回代码
					var message = $("message", xml);
					$.alert($("message", xml).text());
					queryList();
				}, 
				error:$.ermpAjaxError
			});
		}
	});
}
function msgSuccess(id){
	var msgSuccess="";
    $.ajax({
        type:"POST",
		cache:false,
		async : false,
		url:"m/it_dev_man?act=getstatus",//
		data:{
			id : id
		},
		success:function saveSuccess(xml){
			//$.alert($(xml).get(0).xml)
		    var message = $("message", xml);
			var messageCode = message.attr("code");
			if(messageCode == "1"){
				msgSuccess = message.text();
			}
			else{
				$.alert(message.text());
				return;
			}
		},
        error:$.ermpAjaxError
    }); 
    return msgSuccess;
}
function devDiscardDispose(deviceTypeCode, areaCode, deviceID) {
	var sURL = BASE_PATH + "m/it_dev_man?act=initscrapdeal";
	if (deviceTypeCode != null) {
		sURL += "&deviceTypeCode=" + deviceTypeCode;
	}
	if (areaCode != null) {
		sURL += "&areaCode=" + areaCode;
	}
	if (deviceID != null) {
		sURL += "&deviceID=" + deviceID;
	}
	
	mainFrame.addTab({
		id:"oa_device_man_scrap_desp"+Math.floor(Math.random() * 1000000),
		title:"报废处理登记", 
		url:sURL,
		callback:queryList
	});
}

//设备报废
function leaveDeal(deviceTypeCode, areaCode, deviceID){
	if(deviceID!=null && deviceID!=""){
		var msg = msgSuccess(deviceID);
		if(msg!=""){
//			showMsg="该设备目前正在"+msg+"，如果进行“离职处理登记”操作系统将自动终止该流程，您仍要进行该操作吗？";
//			if(!confirm(showMsg)){
//				return ;
//			}
			showMsg="该设备目前正在"+msg+"，您当前不能进行“离职处理登记”操作。";
			$.alert(showMsg);
			return;
		}
	}
	var sURL = BASE_PATH + "m/it_dev_man?act=initdiscard&formType=1";
	if (deviceTypeCode != null) {
		sURL += "&deviceTypeCode=" + deviceTypeCode;
	}
	if (areaCode != null) {
		sURL += "&areaCode=" + areaCode;
	}
	if (deviceID != null) {
		sURL += "&deviceID=" + deviceID;
	}
	mainFrame.addTab({
		id:"oa_device_man_leave"+Math.floor(Math.random() * 1000000),
		title:"离职处理登记", 
		url:sURL,
		callback:queryList
	});
	
//	var ok;
//	if($.browser.msie && $.browser.version==6.0){
//		 ok =$.showModalDialog(sURL, "", "dialogHeight:386px;dialogWidth:816px;status:no;scroll:auto;help:no");
//	}else{
//		 ok =$.showModalDialog(sURL, "", "dialogHeight:331px;dialogWidth:808px;status:no;scroll:auto;help:no");
//	}
//	if (ok) {
//		queryList();
//	}
}
