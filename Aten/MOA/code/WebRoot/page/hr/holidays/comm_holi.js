var dialogWin;
var holiInfos = [];
var holiInfo;
var editHoliInfo = true;

function getJsonStr(holi) {
	holi.holidayDetail = [];
	for (var i=0; i < holiInfos.length; i++) {
		var detail = {};
		if (holiInfos[i].id.substr(0, 5) !="NEWID") {
			detail.id=holiInfos[i].id;
		} 
		detail.holidayName = holiInfos[i].holidayName;
		detail.startDate = holiInfos[i].startDate;
		detail.startTime = holiInfos[i].startTime;
		detail.endDate = holiInfos[i].endDate;
		detail.endTime = holiInfos[i].endTime;
		detail.days = holiInfos[i].days;
		detail.remark = holiInfos[i].remark;
		
		holi.holidayDetail.push(detail);
	}
	return $.toJSON(holi);
}

function closeDialog() {
	if (dialogWin) {
		dialogWin.close();
	}
}
	
//新增明细
function addHoliday(){
	holiInfo= {};
	holiInfo.id="NEWID_"+Math.floor(Math.random() * 1000000);
	var url = BASE_PATH + "m/holi_start?act=initHolidayEdit&time="+(new Date()).getTime();
	dialogWin = $.showModalDialog("请假单明细", url, 610, 260, function(){
		//这个returnValue不是window.showModalDialog的returnValue，随便命都可以，
		//是在弹出窗口中设置parent.windwo.returnValue
		if (window.returnValue) {
			holiInfos.push(holiInfo);
			draw();
		}
	});
			
//	var wnd = showModalDialog(url,holiInfo,"dialogHeight:240px;dialogWidth:610px;status:no;scroll:auto;help:no");
//	if (wnd){
//		holiInfos.push(holiInfo);
//		draw();
//	}
};

//编辑明细
function modifyHoliday(holidayId){
	holiInfo = null;
	//查找对象
	for (var i=0; i<holiInfos.length; i++){
		if (holiInfos[i].id == holidayId){
			holiInfo = holiInfos[i];
		}
	}
	if (holiInfo != null){
		dialogWin = $.showModalDialog("请假单明细", BASE_PATH + "m/holi_start?act=initHolidayEdit&time="+(new Date()).getTime(), 610, 233, function(){
			if (window.returnValue) {
				draw();
			}
		});
//		var wnd = showModalDialog(BASE_PATH + "m/holi_start?act=initHolidayEdit&time="+(new Date()).getTime(),holiInfo,"dialogHeight:338px;dialogWidth:672px;status:no;scroll:auto;help:no");
//		if (wnd){
//			draw();
//		}
	} else{
		$.alert("未找到要修改的组。");
	}
}


//删除请假明细
function deleteHoliday(id){
	$.confirm("确定要删除所选中的请假信息吗?", function(r){
		if (r) {
			for (var i=0; i<this.holiInfos.length; i++){
				if (holiInfos[i].id == id){
					holiInfos.splice(i,1);
					break;
				}
			}
			draw();
		}
	});
}

//取得所有总天数
function getTotalDays(){
	var count = 0;
	for (var i=0; i < holiInfos.length; i++){
		var holiday = holiInfos[i];
		count += parseFloat(holiday.days);
	}
	return count;
}
	
function draw (){
	var html = "";
	var totalInfo = "";
	if (holiInfos.length > 0){
		totalInfo = "合计：共请假 " + getTotalDays() + " 天";
		for (var i=0 ; i<holiInfos.length; i++){
			var holiday = holiInfos[i];
			
			html += "<tr>"
				+ "<td>" + holiday.holidayName + "</td>"
				+ "<td>从 " + holiday.startDate + (holiday.startTime == "A" ? "上午":"下午") + " 到 " + holiday.endDate + (holiday.endTime == "A" ? "上午":"下午") + "</td>"
                + "<td>" + holiday.days + "</td>"
                + "<td>" + (holiday.remark ? holiday.remark : "") + "</td>"
                + "<td>";
            if (editHoliInfo){
            	html += "<a href=\"javascript:modifyHoliday('" + holiday.id + "');\" class=\"opLink\">修改</a> | ";
            	html += "<a href=\"javascript:deleteHoliday('" + holiday.id + "');\" class=\"opLink\">删除</a>";
            }
            html += "</td></tr>";
		}
	}
	$("#holidayList").html(html);
	$("#totalInfo").html(totalInfo);
}