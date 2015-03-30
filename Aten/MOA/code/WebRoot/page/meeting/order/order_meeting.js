var mainFrame = $.getMainFrame();
var areaSel;//
var __areaCode;
$(initMeetingReserve);
function initMeetingReserve(){
	initAreaSel();
	//initMeetingRoom();
}

function initAreaSel() {
	areaSel = $("#areaSel").ySelect({
		width : 45, 
		url:BASE_PATH+"m/data_dict?act=ereasel",
		afterLoad : function() {
			areaSel.select(0);
		},
		onChange : function(value,text) {
			//先清除掉原先的绘图
			$("#tabMain TR").each(function(i) {
				if (i > 1) {
					$(this).remove();
				}
			});
			
			$("#tabMain TR").each(function(i) {
				if (i > 1) {
					$(this).remove();
				}
			});
			initMeetingRoom();
		}
	}); 
}

function initMeetingRoom(){
	__areaCode = areaSel.getValue();
	//提交到后台
	$.ajax({
	    type : "POST",
		cache: false,
		async : false,
		url  : "m/meet_info",
		data : {act:"meetroomlist", areaCode:__areaCode},
	       success : function(xml){
	       		//alert(xml.xml)
	     		//解析XML中的返回代码
				var messageCode = $("message",xml).attr("code");
				if(messageCode == "1"){
					var bodyHTML = "";
 					$("meet-param",xml).each(function(index){
	                        var meetRoom = $(this);
	                        var roomDesc = "点击直接预订！\n"
	                        			+ "座位："+ $("seatNum",meetRoom).text()	+ "   电源："+ $("powerNum",meetRoom).text()
							 			+ "\n网线："+$("cableNum",meetRoom).text() + "   电话线："+$("lineNum",meetRoom).text();
	                        var envi = $("environment",meetRoom).text();
	                        if (envi) {
	                        	roomDesc += "\n环境：" + envi;
	                        }
	                        if (remark) {
	                        	roomDesc += "\n备注：" + remark;
	                        }
	                        
	                        var remark = $("remark",meetRoom).text();
	                        bodyHTML += createTR(meetRoom.attr("id"), $("name",meetRoom).text(), remark, $("status",meetRoom).text(), roomDesc);
	                       
	                });
	                if (bodyHTML != "") {
	                	$("#tabMain").append(bodyHTML);
	                }
	                showWeekTable(0);
				}else{
               		alert($("message",xml).text());
           		};
	       },
	       error : $.ermpAjaxError
	});	

}

//创建行
function createTR(id,name,remark, status, roomDesc){
	var html = "";
	html += "<tr id=\""+id+"\">" 
         + "<td height=\"75\" bgcolor=\"#E4ECF7\"><div style=\" width:81px;cursor:pointer\" onclick=\"meetRoomOrder('" + id + "','" + status + "','" + remark + "');\" title=\"" + roomDesc + "\">" + name + "</div><input type=\"hidden\" value=\""+remark+"\" id=\"remark_"+id+"\"/>"+"<input type=\"hidden\" value=\""+status+"\" id=\"status_"+id+"\"/>"+"</td>" 
         + "<td></td>"
		 + "<td></td>"
		 + "<td></td>"
		 + "<td></td>"
		 + "<td></td>"
		 + "<td></td>"
		 + "<td></td>"
		 +"</tr>";
    return html;
}

function meetRoomOrder(roomID, status, remark){

	if(status =="否")
	{
		var strRemark="";
		if(remark !="")
		{
		    strRemark=remark+"，";
		}
		alert(strRemark+'该会议室不能预订！');
		return false;
	}
	if(remark !="")
	{
	    alert(remark);
	}
	$.getMainFrame().addTab({
		id:"oa_meet_manage_"+Math.floor(Math.random() * 1000000),
		title:"预约会议室",
		url:BASE_PATH + "/m/meet_info?act=toReservePage&roomId="+roomID+"&areaCode="+__areaCode,
		callback:showWeekTable
	});
}


function focusMeetingRoom(name){
	$("#tabMain tr").removeClass("meetingSelBg");
	var focusTr = $("#tabMain td:contains(\""+name+"\")").parent();
	focusTr.addClass("meetingSelBg");
	setTimeout(function(){
			focusTr.ScrollTo("fast",null);
	},170);
}

function dbClickMeetingRoom(name){
	$("#tabMain tr").removeClass("meetingSelBg");
	var focusTr = $("#tabMain td:contains(\""+name+"\")").parent();
	var roomID =focusTr.attr("id");
	if (!roomID) {
		return;
	}
	
	var status = $("#status_"+roomID).val();
	var remark = $("#remark_"+roomID).val();
	
	if(status =="否")
	{
		var strRemark="";
		if(remark && remark !="")
		{
		    strRemark=remark+"，";
		}
		alert(strRemark+'该会议室不能预订！');
		return false;
	}
	
	if(remark && remark !="")
	{
	    alert(remark);
	}
	mainFrame.addTab({
		id:"oa_meet_manage_"+roomID,
		title:"预约会议室",
		url:BASE_PATH + "/m/meet_info?act=toReservePage&roomId="+roomID+"&areaCode="+__areaCode,
		callback:showWeekTable
	});
}
