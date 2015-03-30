var mainFrame = $.getMainFrame();

$(initPage);
var params = window.dialogArguments;

//提醒类型下拉框（多选）
var appointmentTypeSel;

function initPage(){
	
	$("#saveBtn").click(function(){
		addAppointmentRecord();
	});
	$("#doclose").click(function(){
		window.close();
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
	//预约时间
	var appointmentTime = $.trim($("#appointmentTime").val());
	//预约时机
	var warnOpportunity = $.trim($("#warnOpportunity").val());
	//提醒方式
	var appointmentType = appointmentTypeSel.getValue();
	//预约备注
	var remark = $.trim($("#remark").val());
	var customerId = params.customerId;
	var id = "";
	if(params.id){
		id = params.id;
	}
	if(customerId==null || customerId==""){
		alert("请选择客户");
		return;
	}
	
	$.ajax({
        type : "POST",
		cache: false,
		async: true,
		url : BASE_PATH + "/m/customer/addAppointmentRecord",
		dataType : "json",
		data:{
			customerId : customerId,
			appointmentTime : appointmentTime,
			warnOpportunity : warnOpportunity,
			appointmentType : appointmentType,
			remark : remark,
			customerConsultId : id
		},
        success : function(data){
        	alert(2);
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
		width : 80,
		height: 150,
		type : "multiple",
		url : BASE_PATH + "/l/dict/initAppointmentTypeSel"
	});
}