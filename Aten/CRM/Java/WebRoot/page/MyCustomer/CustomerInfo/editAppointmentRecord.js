var mainFrame = $.getMainFrame();

$(initPage);
var params = window.dialogArguments;
var args = parent.window.dialogParam;

//提醒类型下拉框（多选）
var appointmentTypeSel;

function initPage(){
	
	$("#saveBtn").click(function(){
		addAppointmentRecord();
	});
	$("#doclose").click(function(){
		args.returnValue = false;
		args.callback();
	});
	
	//初始化下拉框
	initAppointmentTypeSel();
	$("#appointmentTime").calendar({
		selectDate : function (Input) {
			if (!Input) var Input = this.input;
			this.hideCalendar(this.speed);
			if (this.timeSeparators.length > 1) Input.val(this.formatDate(this.selectedDay, this.selectedMonth, this.selectedYear, this.selectedHour, this.selectedMinute));
			else Input.val(this.formatDate(this.selectedDay, this.selectedMonth, this.selectedYear));
		}
	});
	
	/**
	 * 初始化外部传进来的参数 TODO
	 */
	if(params.content){
		$("#consultContent").val(decodeURI(params.content));
	}
}

/**
 * 添加预约记录
 * @returns {Boolean}
 */
function addAppointmentRecord(){
	var appointmentRecord = {};
	//ID
	appointmentRecord.id = $.trim($("#idHid").val());
	//预约时间
	appointmentRecord.appointmentTime = $.trim($("#appointmentTime").val());
	//预约时机
	appointmentRecord.warnOpportunity = $.trim($("#warnOpportunity").val());
	//提醒方式
	appointmentRecord.appointmentType = appointmentTypeSel.getValue();
	//预约备注
	appointmentRecord.remark = $.trim($("#remark").val());
	//客户ID
	var customerId = $.trim($("#customerId").val());
	var url = BASE_PATH + "/m/customer/add_appointmentRecord";
	if(appointmentRecord.id != "") {
		url = BASE_PATH + "/m/customer/modify_appointmentRecord";
	}
	
	if(null == appointmentRecord.appointmentTime || "" == appointmentRecord.appointmentTime) {
		$.alert("请填写预约时间");
		return;
	}
	if(null == appointmentRecord.warnOpportunity || "" == appointmentRecord.warnOpportunity) {
		$.alert("请填写预约时机");
		return;
	}
	if(null == appointmentRecord.appointmentType || "" == appointmentRecord.appointmentType) {
		$.alert("请填写提醒方式");
		return;
	}
	if(null == appointmentRecord.remark || "" == appointmentRecord.remark) {
		$.alert("请填写预约备注");
		return;
	}
	
	$.ajax({
        type : "POST",
		cache: false,
		async: true,
		url : url,
		dataType : "json",
		data:{
			customerAppointmentJson : $.toJSON(appointmentRecord),
			customerId : customerId
		},
        success : function(data){
        	if ($.checkErrorMsg(data)) {
	    		args.returnValue = true;
	    		args.callback();
	    	} 
//        	window.returnValue=true;
//        	window.close();
        },
        error : $.ermpAjaxError
    });
}

/**
 * 初始化提醒类别下拉框
 */
function initAppointmentTypeSel() {
	appointmentTypeSel = $("#appointmentTypeSel").ySelect({
		width : 100,
		height: 100,
		type : "multiple",
		url : BASE_PATH + "/l/dict/initAppointmentTypeSel",
		afterLoad : function() {
			var appointmentTypeInit = $.trim($("#appointmentTypeHid").val());
			if(null != appointmentTypeInit && "" != appointmentTypeInit) {
				var keyArr = appointmentTypeInit.split(",");
				for(var i = 0; i < keyArr.length; i++) {
					for(var j = 0; j < 3; j++) {
						var checkBox = $("#appointmentTypeSel").children("div").eq(1).find("div").eq(0).find("div").eq(j).find("input").eq(0);
						if(keyArr[i] == checkBox.val()) {
							checkBox.attr("checked", true);
						}
					}
				}
				$(".ms_ok").click();
			}
		}
	});
}