var holiInfo;
var beginTimeObj = null;
var endTimeObj = null;
$(init);

//初始化页面
function init(){
	holiInfo = parent.holiInfo;
	$("#btnCancel").click(function(){
		parent.closeDialog();
	});
	
	$("#btnSave").click(function(){
		saveHoliInfo();
	});

	beginTimeObj = $("#beginTime").ySelect({width:40, onChange:calcDays});
	endTimeObj = $("#endTime").ySelect({width:40, onChange:calcDays});
	
	//如果不是新增，则填充数据
	if (holiInfo){
		//选中
		$("input[name='holidayOption'][value='" + holiInfo.holidayName + "']").attr("checked","checked");
		$("#beginDate").val(holiInfo.startDate);
		$("#endDate").val(holiInfo.endDate);
		$("#remark").val(holiInfo.remark);
		$("#holidayDays").val(holiInfo.days);
		beginTimeObj.select(holiInfo.startTime);
		endTimeObj.select(holiInfo.endTime);
		if (holiInfo.days) {
			$("#totalDays").html("（共 " + holiInfo.days + " 天）");
		}
	}
	
	$("#beginDate,#endDate").change(function(){
		calcDays();
	});
	$("input", "#holidayOptions").click(function(){
		calcDays();
	});
}

function calcDays(){
	var holidayName = $("input[name='holidayOption'][@checked]").val();
	var beginDate = $("#beginDate").val();
	var beginTime = beginTimeObj.getValue();
	var endDate = $("#endDate").val();
	var endTime = endTimeObj.getValue();

	if (holidayName == "" || beginDate == "" || beginTime=="" || endDate=="" || endTime=="" || $.compareDate(beginDate, endDate)){
		$("#holidayDays").val(0);
		$("#totalDays").html("");
		return false;
	}
	
	$.ajax({
		   type : "POST",
		   cache: false,
		   async : true,
		   url  : "m/holi_start",
		   data : "act=calcDays&holidayName="+holidayName+"&startDate="+beginDate +"&startTime="+beginTime+"&endDate="+endDate +"&endTime="+endTime,             
		   success : function (xml){
		   		var message = $("message",xml);
		   		var days = message.text();
				$("#holidayDays").val(days);
				$("#totalDays").html("（共 " + days + " 天）");
		   }
	});
}

function saveHoliInfo(){
	var holidayName = $("input[name='holidayOption'][@checked]").val();
	if (holidayName == null){
		$.alert("请选择所请的假期类型！");
		return;
	}
	var beginDate = $("#beginDate").val();
	if (beginDate==""){
		$.alert("请填写假期的开始日期！");
		return;
	}
	var beginTime = beginTimeObj.getValue();
	if (beginTime==""){
		$.alert("请填写假期的开始时间！");
		return;
	}
	var endDate = $("#endDate").val();
	if (endDate==""){
		$.alert("请填写假期的结束日期！");
		return;
	}
	var endTime = endTimeObj.getValue();
	if (endTime==""){
		$.alert("请填写假期的结束时间！");
		return;
	}
	
	if ( $.compareDate(beginDate, endDate)) {
		$.alert("假期开始时间不能大于结束时间！");
		return false;
	}
	
	var days = parseFloat($("#holidayDays").val());
	
	holiInfo.holidayName = holidayName;
	holiInfo.startDate = $("#beginDate").val();
	holiInfo.startTime = beginTimeObj.getValue();
	holiInfo.endDate = $("#endDate").val();
	holiInfo.endTime = endTimeObj.getValue();
	holiInfo.days = parseFloat($("#holidayDays").val());
	holiInfo.remark = $.trim($("#remark").val());
	
	parent.window.returnValue = true;
	parent.closeDialog();
}
    