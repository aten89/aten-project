/**
 * args.deviceTypeCode 资产类别
 * args.deviceClassCode 设备类别
 * args.assignFlag 是否对设备类别有授权
 * args.assignType 授权类别。0：管理授权；1：查询授权
 * args.areaCode 设备所属的区域
 * args.excludeDeviceIDs 要排除的设备ID
 * args.isUsing 是否已领用
 * args.status 设备当前状态
 * args.approvingFlag 是否在审批中
 * args.deleteDeviceIDs 驳回修改删除的设备ID
 * args.excludeSelfBuyFlag 是否排除个人全额购买的设备
 * args.excludeScrapFlag 是否排除报废的设备
 * args.excludeSubBuyFlag 是否排除比例购买的设备
 * @type 
 */
var args = parent.window.dialogParams;
var deviceClassSel;//设备类别下拉列表
var assignFlag;
var mergeFlag = false;//是否追加结果集
$(initEquipmentChoose);
/**
 * 设备选择
 */
function initEquipmentChoose() {
	//全选
	$("#selectAll").click(function(){
		$("input[name='_doc_chk']", "#devicesBody").attr("checked", this.checked);
	});
	assignFlag = args.assignFlag;
	initDeviceClassSel(args.assignFlag, args.assignType, args.deviceTypeCode, args.areaCode, args.deviceClassCode);
	$("#btnQueryDevices").click(function(){
  		gotoPage(1);
	});
	//手工输入进行搜索时,按回车直接搜索
  	$.EnterKeyPress($("#deviceNo,#deviceName"), $("#btnQueryDevices"));
  	$(":checkbox[name='chkDevAttach']").click(function(){
  		var bUnUseFlagChecked = $("#unUseFlag:checked").size() > 0;
  		var bMyDeviceFlagChecked = $("#myDeviceFlag:checked").size() > 0;
  		if (bUnUseFlagChecked || !bUnUseFlagChecked && !bMyDeviceFlagChecked) {
  			assignFlag = true;
  		} else {
  			assignFlag = false;
  		}
  		if ($("#unUseFlag").size() == 0) {
  			assignFlag = false;
  		}
  		if (bMyDeviceFlagChecked && bUnUseFlagChecked) {
  			mergeFlag = true;//将我名下的设备和闲置设备的结果集累加
  		} else {
  			mergeFlag = false;
  		}
  		initDeviceClassSel(assignFlag, args.assignType, args.deviceTypeCode, args.areaCode, args.deviceClassCode);
  	});
  	if ($("#defaultMyDeviceFlag").val() == "true") {
  		$("#myDeviceFlag").click();
  	}
  	if ($("#disableMyDeviceFlag").val() == "true") {
		$("#myDeviceFlag").attr("disabled", true);
	}
}


/**
 * 初始化设备类别下拉列表
 * @param {} deviceClassCode 设备类别代码
 */
function initDeviceClassSel(assignFlag, assignType, deviceTypeCode, areaCode, deviceClassCode) {
	var act = "classselect";
	if (assignFlag) {
		act = "classsselassign";
	}
	var sURL = BASE_PATH + "m/device_class?act=" + act;
	if (deviceTypeCode != null && deviceTypeCode != "") {
		sURL += "&deviceType=" + deviceTypeCode;
	}
	if (assignType != null && assignType != "") {
		sURL += "&assignType=" + assignType;
	}
	if (areaCode != null && areaCode != "") {
		sURL += "&areaCode=" + areaCode;
	}
	deviceClassSel = $("#deviceClassSel").ySelect({
		width : 80, 
		url: sURL,
		afterLoad : function() {
			deviceClassSel.addOption("", "---所有---", 0);
			if (deviceClassCode != null && deviceClassCode != "") {
				deviceClassSel.select(deviceClassCode);
				deviceClassSel.disable(true);
			} else {
				deviceClassSel.select(0);
			}
			//loadDevices();
		},
		onChange : function() {
			gotoPage(1);
		}
	});     
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
    loadDevices();
}

function loadDevices() {
	$("#devicesBody").empty();
	var pageno=$.trim($("#hidNumPage").val());
	var pagecount=$.trim($("#hidPageCount").val());
	var deviceNo = $.trim($("#deviceNo").val());
	var myDeviceFlag = $("#myDeviceFlag:checked").size() > 0;//我名下的设备
	var unUseFlag = $("#unUseFlag:checked").size() > 0;//闲置设备
	if(args.isUsing=="1"){
		unUseFlag	= false;
	}
	$.ajax({
		type : "post",
		cache : false,
		url : BASE_PATH + "m/it_dev_man?act=loaddevicepage",
		data : {
			pageNo : pageno,
			pageSize : pagecount,
			deviceTypeCode : args.deviceTypeCode == null ? "" : args.deviceTypeCode,
			deviceClassCode : $.trim(deviceClassSel.getValue()),
			deviceName : $.trim($("#deviceName").val()),
			areaCode:args.areaCode == null ? "" : args.areaCode,
			isUsing : unUseFlag ? 0 : (args.isUsing == null ? "" : args.isUsing),//如果unUseFlag＝true，则传值领用标识0（未领用）；否则以前一页面传的isUsing为主。
			status : args.status == null ? "" : args.status,
			myDeviceFlag : myDeviceFlag,
			assignDevClassFlag : assignFlag == null ? "" : assignFlag,
			mergeFlag : mergeFlag,
			excludeDeviceIDs : args.excludeDeviceIDs == null ? "" : args.excludeDeviceIDs,//要排除的设备ID
			deleteDeviceIDs : args.deleteDeviceIDs == null ? "" : args.deleteDeviceIDs,
			approvingFlag : args.approvingFlag == null ? "" : args.approvingFlag,
			excludeSelfBuyFlag : args.excludeSelfBuyFlag == null ? "" : args.excludeSelfBuyFlag,//个人全额购买
			excludeScrapFlag : args.excludeScrapFlag == null ? "" : args.excludeScrapFlag,
			excludeSubBuyFlag : args.excludeSubBuyFlag == null ? "" : args.excludeSubBuyFlag,//比例购买
			isDealingFlag :args.isDealingFlag == null ? "" : args.isDealingFlag,//是否在审批流程中 
			deviceNo:deviceNo
		},
		success : function(xml){
			$("#devicesBody").empty();
			var message = $("message",xml);
			var content = $("content",xml);
			var listData = "";
			//是否有数据返回
		    if (message.attr("code") == "1") {	
		    	$(xml).find('device').each(function(index){
		    		var curELe = $(this);
                    var id = $.trim(curELe.attr("id"));
                    listData += " <tr onmouseout=\"$(this).removeClass('over');\" onmouseover=\"$(this).addClass('over');\">"
			  				+ "<td><input type=\"checkbox\" name=\"_doc_chk\" value=\"" + id + "\"/></td>"
			  				+ "<td>" + ($("work-userId",curELe).text() == "" ? "闲置设备" : "我名下的设备") + "</td>"
			  				+ "<td>" + $("device-class-str",curELe).text() + "</td>"
			  				+ "<td>" + $("device-no",curELe).text() + "</td>"
			  				+ "<td>" + $("device-name",curELe).text() + "</td>"
							+ "<td>" + $("device-model",curELe).text() + "</td>"
							+ "<td>" + $("buy-time",curELe).text() + "</td>"
							+ "<td>" + $("buy-type-str",curELe).text() + "</td>"
							+ "</tr>";

				});
				$("#devicesBody").html(listData);
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
		error : $.ermpAjaxError
	});
}
function saveInfo(){
	var deviceIDs = "";
	$(":checkbox[name='_doc_chk']:checked").each(function(){
		if (deviceIDs != "") {
			deviceIDs += ",";
		}
		deviceIDs += $(this).val();
	});
	if (deviceIDs == "") {
		$.alert("至少选择一个设备！");
		return;
	}
	args.selectDeviceIDs = deviceIDs;//返回已选择的设备ID，以逗号分隔
	parent.window.returnValue1 = true;
	parent.closeDialog();	
}

function doclose(){
	parent.closeDialog();	
}




