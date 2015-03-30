var classSelect;
var args = parent.window.dialogParams;
$(initDeviceStock);
/**
 * 库存设备
 */
function initDeviceStock() {
	$("#deviceClass").val(args.deviceClass);
	initDeviceClassSel(args.deviceType,args.deviceClass);
	
	$("#btnQueryDevices").click(function(){
  		gotoPage(1);
	});
	gotoPage(1)
	//手工输入进行搜索时,按回车直接搜索
  	$.EnterKeyPress($("#deviceName"), $("#btnQueryDevices"));
}


function saveInfo(){
	window.close();	
}

function doclose(){
	parent.closeDialog();	
}
/**
 * 初始化设备类别下拉列表
 * @param {} deviceClassCode 设备类别代码
 */
function initDeviceClassSel(deviceTypeCode, deviceClassCode) {
	var areaCode=args.areaCode == null ? "" : args.areaCode;
	if(deviceTypeCode==""){
		classSelect = $("#deviceClassSel").ySelect({
		width : 80
		});   
		classSelect.addOption("", "所有...", 0);
		return;
	}
	classSelect = $("#deviceClassSel").ySelect({
		width : 99, 
		url:BASE_PATH+"m/device_class?act=classselect&deviceType=" + deviceTypeCode+"&areaCode="+areaCode,
		afterLoad : function() {
			classSelect.addOption("", "所有...", 0);
			if (deviceClassCode != null && deviceClassCode != "") {
				classSelect.select(deviceClassCode);
			} else {
				classSelect.select(0);
			}
			
		}, 
		onChange : function(value, text) {
			$("#deviceClass").val(value);
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
	var deviceType=args.deviceType;
	var deviceClass=$("#deviceClass").val();;
	$.ajax({
		type : "post",
		cache : false,
		url : BASE_PATH + "m/it_dev_man?act=loadleaddevicepage",
		data : {
			pageNo : pageno,
			pageSize : pagecount,
			deviceTypeCode : args.deviceType == null ? "" : args.deviceType,
			deviceClassCode : $.trim(deviceClass),
			areaCode : args.areaCode == null ? "" : args.areaCode,
			deviceName : $.trim($("#deviceName").val())
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
			  				+ "<td>" +$("device-no",curELe).text() + "</td>"
			  				+ "<td>" + $("device-name",curELe).text() + "</td>"
			  				+ "<td>" + $("device-class-str",curELe).text() + "</td>"
							+ "<td>" + $("device-model",curELe).text() + "</td>"
							+ "<td>" + $("config-list",curELe).text() + "</td>"
							+ "<td>" + $("buy-time",curELe).text() + "</td>"
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


