var tripInfo;
$(initTripAdd);
function initTripAdd(){
	tripInfo = parent.tripInfo;
	$("#saveBtn").click(function(){
		saveTripInfo();
	});
	$("#closeBtn").click(function(){
		parent.closeDialog();
	});	

	//如果不是新增，则填充数据
	if (tripInfo != null){
		$("#fromPlace").val(tripInfo.fromPlace);
		$("#toPlace").val(tripInfo.toPlace);
		$("#startDate").val(tripInfo.startDate);
		$("#endDate").val(tripInfo.endDate);
		$("#causa").val(tripInfo.causa);
		$("#tripDays").val(tripInfo.days);
		if (tripInfo.days) {
			$("#totalDays").html("（共 " + tripInfo.days + " 天）");
		}
	}
}

/*相差日期 */
function getDays(){
	var startDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	if(startDate!="" && endDate!=""){
		if (startDate > endDate ){
			$.alert("出差的时间段是一个无效区间，请重新填写出差时间！");
			return;
		}	
	 	var s1 = startDate.replace(/-/g, "/");
		var s2 = endDate.replace(/-/g, "/");
		var d1 = new Date(Date.parse(s1));
		var d2 = new Date(Date.parse(s2));	
		var time= d2.getTime() - d1.getTime();
		var days = parseInt(time / (1000 * 60 * 60 * 24)) + 1;	
		$("#tripDays").val(days);
		$("#totalDays").html("（共 " + days + " 天）");	
	}
}    

function saveTripInfo(){
	var fromPlace = $.trim($("#fromPlace").val());
	var toPlace = $.trim($("#toPlace").val());
	var startDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	var days = parseFloat($("#tripDays").val());
	var causa = $.trim($("#causa").val());
	if (fromPlace==""){
		$.alert("请填写出发地点！");
		return;
	}
	if (toPlace==""){
		$.alert("请填写到达地点！");
		return;
	}	
	if (fromPlace==toPlace){
		$.alert("请正确填写‘出发地点’与‘到达地点’");
		return;
	}
	if (startDate==""){
		$.alert("请填写出差开始日期！");
		return;
	}	
	if (endDate==""){
		$.alert("请填写出差结束日期！");
		return;
	}
	if ( $.compareDate(startDate, endDate)) {
		$.alert("出差的时间段是一个无效区间，请重新填写出差时间！");
		return;
	}
	if(causa.length > 256) {
	    $.alert("出差事由不能超过256个字符");
	    $("#causa").focus();
	    return false;
    }
	

	tripInfo.fromPlace = fromPlace;
	tripInfo.toPlace = toPlace;
	tripInfo.startDate = startDate;
	tripInfo.endDate = endDate;
	tripInfo.days = days;
	tripInfo.causa = causa;	
	parent.window.returnValue = true;
	parent.closeDialog();
}
