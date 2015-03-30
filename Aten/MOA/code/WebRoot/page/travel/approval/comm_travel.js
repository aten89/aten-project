var dialogWin;
var tripInfos = [];
var tripInfo;
var editTripInfo = true;

function getJsonStr(trip) {
	trip.busTripApplyDetail = [];
	for (var i=0; i < tripInfos.length; i++) {
		var detail = {};
		if (tripInfos[i].id.substr(0, 5) !="NEWID") {
			detail.id=tripInfos[i].id;
		} 
		detail.fromPlace = tripInfos[i].fromPlace;
		detail.toPlace = tripInfos[i].toPlace;
		detail.startDate = tripInfos[i].startDate;
		detail.endDate = tripInfos[i].endDate;
		detail.days = tripInfos[i].days;
		detail.causa = tripInfos[i].causa;
	
		trip.busTripApplyDetail.push(detail);
	}
	return $.toJSON(trip);
}

function closeDialog() {
	if (dialogWin) {
		dialogWin.close();
	}
}

//新增出差明细
function addTravel(){
	tripInfo = {};
	tripInfo.id = "NEWID_"+Math.floor(Math.random() * 1000000);
	var url = BASE_PATH + "page/travel/approval/edit_travel.jsp";
	dialogWin = $.showModalDialog("出差日程明细", url, 470, 265, function(){
		if (window.returnValue) {
			tripInfos.push(tripInfo);
			draw();
		}
	});
};

//编辑出差明细
function modifyTravel(busTripId){
	tripInfo = null;
	//查找对象
	for (var i=0; i<tripInfos.length; i++){
		if (tripInfos[i].id == busTripId){
			tripInfo = tripInfos[i];
		}
	}
	if (tripInfo != null){
		dialogWin = $.showModalDialog("出差日程明细", BASE_PATH + "page/travel/approval/edit_travel.jsp", 470, 265, function(){
			if (window.returnValue) {
				draw();
			}
		});
	} else{
		$.alert("未找到要修改的组。");
	}
}


//删除出差明细
function deleteTravel(busTripId){
	$.confirm("您确定要删除所选中的出差信息吗?", function(r){
		if (r) {
			for (var i=0; i<this.tripInfos.length; i++){
				if (tripInfos[i].id == busTripId){
					tripInfos.splice(i,1);
					break;
				}
			}
			draw();
		}
	});
}

//取得所有出差的总天数
function getTotalDays(){
	var count = 0;
	for (var i=0 ; i<tripInfos.length; i++){
		var busTrip = tripInfos[i];
		count += parseFloat(busTrip.days);
	}
	return count;
}
	
function draw (){
	var html = "";
	var totalInfo = "";
	if (tripInfos.length > 0){
		totalInfo = "合计：共申请 " + this.getTotalDays() + " 天";
		for (var i=0 ; i<tripInfos.length; i++){
			var busTrip = tripInfos[i];				
			html += "<tr>"
				+ "<td>" + busTrip.fromPlace + " --> " + busTrip.toPlace + "</td>"
				+ "<td>从" + busTrip.startDate +  " 到 " + busTrip.endDate + "</td>"
                + "<td>" + busTrip.days + "</td>"
                + "<td>" + busTrip.causa + "</td>"
                + "<td>";
            if (editTripInfo) {
            	html += "<a href=\"javascript:modifyTravel('" + busTrip.id + "');\" class=\"opLink\">修改</a> | ";
            	html += "<a href=\"javascript:deleteTravel('" + busTrip.id + "');\" class=\"opLink\">删除</a>";
            }
            html += "</td></tr>";
		}
	}
	$("#tripList").html(html);
	$("#totalInfo").html(totalInfo);
}

