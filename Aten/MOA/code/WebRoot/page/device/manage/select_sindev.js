/**
 * args.deviceTypeCode 资产类别
 * args.deviceClassCode 设备类别
 * args.excludeDeviceIDs 要排除的设备ID
 * @type 
 */
var args = parent.window.dialogParams;
var deviceClassSel;//设备类别下拉列表
$(initEquipmentChoose);
/**
 * 设备选择
 */
function initEquipmentChoose() {
	//全选
	initDeviceClassSel(args.deviceTypeCode, args.deviceClassCode);
	$("#btnQueryDevices").click(function(){
  		gotoPage(1);
	});
	//手工输入进行搜索时,按回车直接搜索
  	$.EnterKeyPress($("#deviceNo,#deviceName"), $("#btnQueryDevices"));
}


/**
 * 初始化设备类别下拉列表
 * @param {} deviceClassCode 设备类别代码
 */
function initDeviceClassSel(deviceTypeCode, deviceClassCode) {
	deviceClassSel = $("#deviceClassSel").ySelect({
		width : 107, 
		url:BASE_PATH+"m/device_class?act=classsselassign&deviceType="+deviceTypeCode+"&assignType=0&areaCode="+args.areaCode,
		afterLoad : function() {
			deviceClassSel.addOption("", "---所有---", 0);
			if (deviceClassCode != null && deviceClassCode != "") {
				deviceClassSel.select(deviceClassCode);
			} else {
				deviceClassSel.select(0);
			}
		},
		onChange : function() {
			gotoPage(1);
		}
	});     
}

/**
 * 设备类别
 */
function initClassDiv() {
	
	classSelect = $("#deviceClassDiv").ySelect({width:80, url:"m/device_class?act=classsselassign&deviceType=DEVICE-IT&assignType=0&areaCode="+areaCode,
		afterLoad:function(){
			classSelect.addOption("", "所有....", 0);
			classSelect.select(0);
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
	$.ajax({
		type : "post",
		cache : false,
		url : BASE_PATH + "m/it_dev_man?act=loaddevicepage",
		data : {
			pageNo : pageno,
			pageSize : pagecount,
			deviceTypeCode : $.trim(args.deviceTypeCode),
			deviceClassCode : $.trim(deviceClassSel.getValue()),
			deviceName : $.trim($("#deviceName").val()),
			areaCode : args.areaCode == null ? "" : args.areaCode,
			isUsing : args.isUsing == null ? "" : args.isUsing,
			status : args.status == null ? "" : args.status,
			buyType: args.buyType == null ? "" : args.buyType,
			excludeSelfBuyFlag : args.excludeSelfBuyFlag == null ? "" : args.excludeSelfBuyFlag,
			excludeSubBuyFlag : args.excludeSubBuyFlag == null ? "" : args.excludeSubBuyFlag,
			assignDevClassFlag : true,
			approvingFlag : args.approvingFlag == null ? "" : args.approvingFlag,
			excludeDeviceIDs : $.trim(args.excludeDeviceIDs),//要排除的设备ID
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
			  				+ "<td><input type=\"radio\" name=\"_doc_chk\" value=\"" + id + "\"/></td>"
			  				+ "<td><input id=\"name_"+id+"\" type=\"hidden\"  value=\"" + $("device-name",curELe).text() + "\"/>" + $("device-class-str",curELe).text() + "</td>"
			  				+ "<td><input id=\"no_"+id+"\" type=\"hidden\" value=\"" + $("device-no",curELe).text() + "\"/>" + $("device-no",curELe).text() + "</td>"
			  				+ "<td>" + $("device-name",curELe).text() + "</td>"
							+ "<td><input id=\"model_"+id+"\" type=\"hidden\" value=\"" + $("device-model",curELe).text() + "\"/>" + $("device-model",curELe).text() + "</td>"
							+ "<td><input id=\"work_"+id+"\" type=\"hidden\" value=\"" + $("work-areaCode",curELe).text() + "\"/>" 
								+		"<input id=\"group_"+id+"\" type=\"hidden\" value=\"" + $("work-groupName",curELe).text() + "\"/>"
								+	"<input id=\"work_name"+id+"\" type=\"hidden\" value=\"" + $("work-areaName",curELe).text() + "\"/>"
								+	"<input id=\"userId_"+id+"\" type=\"hidden\" value=\"" + $("work-userId",curELe).text() + "\"/>"
								+	"<input id=\"userName_"+id+"\" type=\"hidden\" value=\"" + $("work-userName",curELe).text() + "\"/>"
								+	"<input id=\"deviceClassID_"+id+"\" type=\"hidden\" value=\"" + $("device-class",curELe).text() + "\"/>"
								+	"<input id=\"buyTime_"+id+"\" type=\"hidden\" value=\"" + $("buy-time",curELe).text() + "\"/>"
								+	"<input id=\"purposeBef_"+id+"\" type=\"hidden\" value=\"" + $("work-purpose",curELe).text() + "\"/>"
								+	"<input id=\"purposeNameBef_"+id+"\" type=\"hidden\" value=\"" + $("work-purposeName",curELe).text() + "\"/>"
								+	"<input id=\"status_"+id+"\" type=\"hidden\" value=\"" + $("status",curELe).text() + "\"/>"
								+	"<input id=\"buyType_"+id+"\" type=\"hidden\" value=\"" + $("buy-type",curELe).text() + "\"/>"
								+	"<input id=\"buyTypeStr_"+id+"\" type=\"hidden\" value=\"" + $("buy-type-str",curELe).text() + "\"/>"
								
								+	"<input id=\"deviceClassName_"+id+"\" type=\"hidden\" value=\"" + $("device-class-str",curELe).text() + "\"/>"
								+	"<input id=\"areaCode_"+id+"\" type=\"hidden\" value=\"" + $("area-code",curELe).text() + "\"/>"
								+	"<input id=\"areaName_"+id+"\" type=\"hidden\" value=\"" + $("area-name",curELe).text() + "\"/>"
								
								+ $("buy-time",curELe).text() + "</td>"
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
//	var deviceIDs = "";
//	$(":radio:checked").each(function(){
//		if (deviceIDs != "") {
//			deviceIDs += ",";
//		}
//		deviceIDs += $(this).val();
//	});
	var deviceID=$("input[name=_doc_chk][@type=radio][@checked]").val();
	args.selectDeviceID = deviceID;
	args.selectDeviceName = $("#name_"+deviceID).val();
	args.selectDeviceModel = $("#model_"+deviceID).val();
	args.selectDeviceNo = $("#no_"+deviceID).val();
	args.areaNameBef = $("#work_name"+deviceID).val();
	args.groupName = $("#group_"+deviceID).val();
	args.userId = $("#userId_"+deviceID).val();
	args.userName = $("#userName_"+deviceID).val();
	args.areaCodeBef = $("#work_"+deviceID).val();
	args.buyTime = $("#buyTime_"+deviceID).val();
	args.deviceClassID = $("#deviceClassID_"+deviceID).val();
	args.purposeBef = $("#purposeBef_"+deviceID).val();
	args.purposeNameBef = $("#purposeNameBef_"+deviceID).val();
	args.status = $("#status_"+deviceID).val();
	args.buyType = $("#buyType_"+deviceID).val();
	args.buyTypeStr = $("#buyTypeStr_"+deviceID).val();
	
	args.deviceClassName = $("#deviceClassName_"+deviceID).val();
	args.areaName = $("#areaName_"+deviceID).val();
	args.areaCode = $("#areaCode_"+deviceID).val();
	parent.window.returnValue1 = true;
	parent.closeDialog();	
}

function doclose(){
	parent.closeDialog();	
}




