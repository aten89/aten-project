//离职设备处理--我名下的设备列表
var mainFrame = $.getMainFrame();
$(initLeaveDealMyEquipment);

function initLeaveDealMyEquipment() {
	   //添加权限约束
	$.handleRights({
		"btnDeviceUse" : $.OaConstants.RECIPIENTS,//领用
		"equipAllot" : $.OaConstants.ALLOCATION,//调拨
		"equipScrap" : $.OaConstants.SCRAP,//报废
		"equipmentLeaveDealAdd" : $.OaConstants.SCRAP,//离职处理
		"queryDevice" : $.SysConstants.QUERY//查询
	});
	
	$("#btnDeviceUse").click(function(){
		deviceUse();
	});
	
	$("#equipAllot").click(function(){
		equipmentAllotAdd();
	});
	
	$("#equipScrap").click(function(){
		equipmentScrapAdd();
	});
	
	$("#equipmentLeaveDealAdd").click(function(){
		equipmentLeaveDealAdd();
	});
	
	$("#refreshBtn").click(function(){
		queryPage();
	});
	
	//全选
	$("#selectAll").click(function(){
		$("input[name='_doc_chk']", "#deviceList").attr("checked", this.checked);
	});
     
	queryPage();
}

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
    $("#hidNumPage").val(parseInt(pageNo));
    queryPage();
}

function queryPage() {	
	//设置查询按钮
 	$("#deviceList").empty();
	var pageno=$.trim($("#hidNumPage").val());
	var pagecount=$.trim($("#hidPageCount").val());
	$.ajax({
	   type : "POST",
	   cache: false,
	   async : true,
	   url  : BASE_PATH + "m/device_myuse",
	   data : "act=query&pageno="+pageno+"&pagecount="+pagecount,             
	   success : function (xml){
	   		//$.alert($(xml).get(0).xml);		
	   		var message = $("message",xml);
			var content = $("content",xml);
			var listData = "";
			//是否有数据返回
		    if (message.attr("code") == "1") {	
		    	$(xml).find('device').each(function(index){
		    		var curELe = $(this);
                    var id = $.trim(curELe.attr("id"));
                    var buyType = $("buy-type",curELe).text();
                    listData += " <tr onmouseout=\"$(this).removeClass('over');\" onmouseover=\"$(this).addClass('over');\">"
                    		+ "<td><input name=\"_doc_chk\" type=\"checkbox\" value=\"" + id + "\" class=\"cBox\"/></td>"
			  				+ "<td>" + $("device-no",curELe).text() + "</td>"
							+ "<td>" + $("device-name",curELe).text() + "</td>"
							+ "<td>" + $("device-class-str",curELe).text() + "</td>"
							+ "<td>" + $("device-type-str",curELe).text() + "<span name=\"deviceTypeCode\" style=\"display:none;\">" + $("device-type",curELe).text() + "</span></td>"
							+ "<td>" + $("area-name",curELe).text() + "</td>"
							+ "<td>" + $("buy-type-str",curELe).text() + "</td>"
							+ "<td>" + $("apply-date",curELe).text() + "</td>";
							
							var op =  "";
							op += ( $.hasRight($.SysConstants.VIEW)?"<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"initView('" + id + "')\">查看</a> | ":"");
							if (buyType != "BUY-TYPE-SELF") {
								op += ( $.hasRight($.OaConstants.ALLOCATION)?"<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"equipmentAllotAdd('" + id + "')\">调拨</a> | ":"");
							}
							op += ( $.hasRight($.OaConstants.SCRAP)?"<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"equipmentLeaveDealAdd('" + id + "')\">离职处理</a> | ":"");
							op += ( $.hasRight($.OaConstants.SCRAP)?"<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"equipmentScrapAdd('" + id + "')\">报废</a> | ":"");
							listData += "<td>" + op.substring(0, op.lastIndexOf(" | ")) + " </td> </tr>";

				});
				$("#deviceList").html(listData);
				//----------------------------------------------------
		        //------------------翻页数据----------------------------
		        $(".pageNext").html($.createTurnPage(xml));
		        //----------------------------------------------------		
		        $.EnterKeyPress($("#numPage"),$("#numPage").next());
		    } else {
		        $(".pageNext").html("");
		    };
	   		$("#refreshBtn").removeAttr("disabled");
	   },
	   error : function(){
	        $("#refreshBtn").removeAttr("disabled");
	        $.ermpAjaxError();
	    }
	});
}

/**
 * 设备领用
 */
function deviceUse(deviceTypeCode, areaCode, deviceID) {
	var sURL = BASE_PATH + "m/dev_apply_start?act=initadd";
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
		id:"oa_device_apply_reci"+Math.floor(Math.random() * 1000000),
		title:"领用申购申请", 
		url:sURL,
		callback:queryPage
	});
}

//报废新增/修改
function equipmentScrapAdd(id){
	if (!validDeviceSameType()) {
		var strOptType = "报废";
		$.alert("每一次所" + strOptType + "的设备其资产类别必须相同，如果要" + strOptType + "的设备的资产类别不一致，请分别" + strOptType + "！");
		return;
	}
	var	deviceIDs = "";
	if (id != null) {
		deviceIDs = id;
	} else {
		deviceIDs = getSelectedIDs();
	}
	var sURL = BASE_PATH + "m/dev_discard_start?act=initadd&formType=0&deviceIDs="+ deviceIDs;
	mainFrame.addTab({
		id:"oa_device_apply_disc"+Math.floor(Math.random() * 1000000),
		title:"报废申请", 
		url:sURL,
		callback:queryPage
	});
}

//调拨新增/修改
function equipmentAllotAdd(id){
	if (!validDeviceSameType()) {
		var strOptType = "调拨";
		$.alert("每一次所" + strOptType + "的设备其资产类别必须相同，如果要" + strOptType + "的设备的资产类别不一致，请分别" + strOptType + "！");
		return;
	}
	var	deviceIDs = "";
	if (id != null) {
		deviceIDs = id;
	} else {
		deviceIDs = getSelectedIDs();
	}
	var sURL = BASE_PATH + "m/dev_alc_start?act=initadd&deviceIDs="+ deviceIDs;
	mainFrame.addTab({
		id:"oa_device_apply_allo"+Math.floor(Math.random() * 1000000),
		title:"调拨申请", 
		url:sURL,
		callback:queryPage
	});
}

//离职处理
function equipmentLeaveDealAdd(id){
	if (!validDeviceSameType()) {
		$.alert("每一次所处理的设备其资产类别必须相同，如果要要处理的设备，其资产类别不一致，请分成多个单进行处理！");
		return;
	}
	var	deviceIDs = "";
	if (id != null) {
		deviceIDs = id;
	} else {
		deviceIDs = getSelectedIDs();
	}
	
	var sURL = BASE_PATH + "m/dev_discard_start?act=initadd&formType=1&deviceIDs="+ deviceIDs;
	mainFrame.addTab({
		id:"oa_device_apply_leave"+Math.floor(Math.random() * 1000000),
		title:"离职处理申请", 
		url:sURL,
		callback:queryPage
	});
}

/**
 * 获取选择的设备ID
 * @return {}
 */
function getSelectedIDs() {
	var deviceIDs = "";
	$(":checkbox[name='_doc_chk']:checked").each(function(){
		if (deviceIDs != "") {
			deviceIDs += ",";
		}
		deviceIDs += $(this).val();
	});
	return deviceIDs;
}

/**
 * 获取选择的设备的资产类别
 * @return {}
 */
function validDeviceSameType() {
	var bSameType = true;
	var deviceTypeCode = "";
	$(":checkbox[name='_doc_chk']:checked").each(function(){
		if (deviceTypeCode == "") {
			deviceTypeCode = $("span[name='deviceTypeCode']", $(this).parent().parent().get(0)).text();
		} else {
			if (deviceTypeCode != $("span[name='deviceTypeCode']", $(this).parent().parent().get(0)).text()) {
				bSameType = false;
				return false;
			}
		}
	});
	return bSameType;
}

/**
 * 查看设备详情
 */
function initView(id) {
	//设备报废审批
	mainFrame.addTab({
		id:"oa_equipmentInfo"+id,
		title:"查看设备信息",
		url:BASE_PATH +"m/device_myuse?act=view&id="+id,
		callback:queryPage
	});
}