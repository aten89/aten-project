var mainFrame = $.getMainFrame();

var submitFlag = false;//会议室预订是否冲突
var beginTimeSel;
var endTimeSel;
var areaSel;
var meetingRoomSelect;
var NTKO_ATTACH_OCX;

var isReNofity;//是否为重发通知
var isModify;//是否更新通知
$(initParameterSet);
function initParameterSet(){
	isReNofity = $("#isResend").val() == "true";
	isModify = $("#id").val() != "" && !isReNofity;
	if (isReNofity) {//重发通知邮件
		$("#tab01").show();	
		$("#but01").show();	
		loadMeetingInfo();
	} else {//修改或新增
		//隐藏会议内容页面
		$("#tab00").show();	
		$("#but00").show();	
		
		//显示手动添加
		$("#addMeeting").click(function(){
			$("#addMan").toggle();
		});
		
		//关闭手动添加
		$("#tipClose").click(function(){
			if ( $("#addMan").is(':visible')) {
		    	$("#addMan").hide();
			}						 
		});
		//初始化会议室选择列表
		areaSel = $("#areaCodeDiv").ySelect({width:70});
		initArea();
		meetingRoomSelect = $("#meetingRoomDiv").ySelect({width:100,isdisabled: true});
		//初始化会议时间
		initTimeBox();
		
	
		//手动添加
		$("#addUserBtn").click(function(){
			addManCust();	
		});
		
		//初始化参与人选择中的机构列表
	  	initDeptList();
	  	//搜索用户帐号
	   	$("#searchGroupUsers").click(function(e){
			searchUser();
	   	});
	   	if (isModify) {//修改操作
	   		//重加参会人员
	   		loadParticipants();
	   	}
		
	   	//提交会议预订
		$("#saveOrderBtn").click(function(){
	   	 	saveMeetingInfo();
	   	});
   	}
   	
	//初始化附件控件
 	initControl();
   
   	//保存并发送
   	$("#sendEmailBtn").click(function(){
		submitEmail(true);
   	});
   	
   	//保存不发送
	$("#saveEmailBtn").click(function(){
		submitEmail(false);
	});
   
   	//关闭
   	$("#closeBtn").click(function(){
		mainFrame.getCurrentTab().doCallback();
   		mainFrame.getCurrentTab().close();
   	});
   	
 	//回车搜索
	$.EnterKeyPress($("#userKeyword"),$("#searchGroupUsers"));
	$.EnterKeyPress($("#name"),$("#addUserBtn"));
	$.EnterKeyPress($("#email"),$("#addUserBtn"));
}

//初始化会议室选择列表
function initMeetingRoomDiv() {
	
	var areaCode = "";
	if(typeof(areaSel)!="undefined"){
		areaCode=areaSel.getValue();
	}
	if(areaCode==null || areaCode==""){
		meetingRoomSelect = $("#meetingRoomDiv").ySelect({width:70,isdisabled: true});
		return;
	}
	meetingRoomSelect = $("#meetingRoomDiv").ySelect({width:100, url:"m/meet_param?act=meetingroomselect&isOrder=Y&areaCode="+areaCode,
		afterLoad:function(){
			meetingRoomSelect.addOption("", "请选择...", 0);
			var roomId =$.trim( $("#roomId").val());
			meetingRoomSelect.select(roomId);
			 $("#roomId").val("");
		//	onChangeMeetionRoomSelect(roomId);
		},
		onChange : onChangeMeetionRoomSelect
	});	
}

function initArea(){
	$.ajax({
	        type : "POST",
			cache: false,
			url  : "m/data_dict?act=ereasel",
			dataType : "html",
	        success : function(data){
	        	var html=" <div isselected='true'>**请选择...</div>";
	            html+=data;
	            $("#areaCodeDiv").html(html);
	            areaSel=$("#areaCodeDiv").ySelect({width :70,name:"areaCodeDiv",
	            afterLoad:function(){
					var areaCode = $("#areaCode").val();
					areaSel.select(areaCode);
				},
	            onChange : initMeetingRoomDiv});
	        },
	        error : $.ermpAjaxError
	    });
}

function onChangeMeetionRoomSelect() {
	$.ajax({
		type : "POST",
		cache: false,
   		async : false,
      	url : "m/meet_param",
      	dataType : "xml",
      	data : {
      		act : "meetingroomenv",
      		id : meetingRoomSelect.getValue()
      	},
        success : function(xml){
        	var message = $("message",xml);
        	if(message.attr("code") == "1") {
        		var msgInfo = message.text();
        		if (msgInfo != "") {
        			$("#meetRoomEnv").html(meetingRoomSelect.getDisplayValue() + "环境：" + msgInfo);
        		} else {
        			$("#meetRoomEnv").empty();
        		}
        	}
        },
       	error : $.ermpAjaxError
	});
	checkMeetingRoom();
}

//初始化会议时间
function initTimeBox(){
	var times = new Array(
				//		'00:00','00:15','00:30','00:45',
				//		'01:00','01:15','01:30','01:45',
				//		'02:00','02:15','02:30','02:45',
				//		'03:00','03:15','03:30','03:45',
				//		'04:00','04:15','04:30','04:45',
				//		'05:00','05:15','05:30','05:45',
				//		'06:00','06:15','06:30','06:45',
				//		'07:00','07:15','07:30','07:45',
						'08:00','08:15','08:30','08:45',
						'09:00','09:15','09:30','09:45',
						'10:00','10:15','10:30','10:45',
						'11:00','11:15','11:30','11:45',
						'12:00','12:15','12:30','12:45',
						'13:00','13:15','13:30','13:45',
						'14:00','14:15','14:30','14:45',
						'15:00','15:15','15:30','15:45',
						'16:00','16:15','16:30','16:45',
						'17:00','17:15','17:30','17:45',
						'18:00','18:15','18:30','18:45',
						'19:00','19:15','19:30','19:45',
						'20:00'
					//	,'20:15','20:30','20:45',
					//	'21:00','21:15','21:30','21:45',
					//	'22:00','22:15','22:30','22:45',
					//	'23:00','23:15','23:30','23:45'
						 );
	var optionHtm ="";
	for(var i=0; i< times.length ; i++){
		optionHtm +="<div>"+times[i]+"**"+times[i]+"</div>"
	}		
	$("#beginTimeSel").html(optionHtm);
	beginTimeSel =$("#beginTimeSel").ySelect({width:35,onChange:initDeviceAndCheckRoom});
	
	$("#endTimeSel").html(optionHtm);
	endTimeSel =$("#endTimeSel").ySelect({width:35,onChange:initDeviceAndCheckRoom});
	
	$("#beginDate").change(function(){
 		if($.compareDate($.trim($("#beginDate").val()),$.trim($("#endDate").val()))){
 			//开始日期大于结束日期，结束日期设为开始日期一样
   			$("#endDate").val($.trim($("#beginDate").val()));
   		}
   		initDeviceAndCheckRoom();
   	});

 	$("#endDate").change(function(){
		initDeviceAndCheckRoom();
   	});
   	
   	var _bDate;
   	var _bTime;
   	var _eDate;
   	var _eTime;
   	
   	var bTime = $("#bTime").val();
   	var eTime = $("#eTime").val();
   	if (bTime != "" && eTime != "") {//修改时，开始时间与结束时间有值
   		var temArr = bTime.split(" ");
   		_bDate = temArr[0];
   		_bTime = temArr[1];
   		temArr = eTime.split(" ");
   		_eDate = temArr[0];
   		_eTime = temArr[1];
   	} else {//新增时定义默认值
		_bDate = _eDate = $("#orderDate").val();//点击新增传进来的日期
   		
		_bTime = getfloorTime(15, 0);
		_eTime = getfloorTime(15, 30);
   	}
   	$("#beginDate").val(_bDate);
	$("#endDate").val(_eDate);
   	beginTimeSel.select(_bTime);
	endTimeSel.select(_eTime);
}

//取得整分钟的时间
function getfloorTime(n, min) {
	var d = new Date();	
	var m = d.getMinutes();
	var m = parseInt((m + min + n - 1)/n) * n;
	d.setMinutes(m);
	
	var h = d.getHours();
	m = d.getMinutes();

	if (h < 10) {
		h = "0" + h;
	}
	if (m < 10) {
		m = "0" + m;
	}
	return h + ":" + m;
}

//检查会议室预订冲突与刷新会议设备
function initDeviceAndCheckRoom(){
	checkMeetingRoom();//
//	initDevices();//初始化会议设备
}

//初始化会议设备
//function initDevices(){
//	var _bDate  = $.trim($("#beginDate").val());
//   	var _bTime = beginTimeSel.getValue();
//   	var _eDate = $.trim($("#endDate").val());
//   	var _eTime = endTimeSel.getValue();
//   	
//	if(_bDate == "" || _bTime == "" || _eDate == "" || _eTime == ""){
//		return;
//	}
//	var beginTime = _bDate + " " + _bTime;
//	var endTime = _eDate + " " + _eTime;
//	if(	$.compareDate(beginTime,endTime) || beginTime == endTime){
//		$.alert("开始时间必须小于结束时间！");
//		return;
//	};
//	$.ajax({
//		type : "POST",
//		cache: false,
//   		async : false,
//      	url : "m/meet_info",
//      	dataType : "xml",
//      	data : {
//      		act : "meetingroomdevice",
//      		id : $("#id").val(),
//      		beginTime :beginTime,
//      		endTime	:endTime
//      	},
//        success : function(xml){
//        	var message = $("message",xml);
//        	if(message.attr("code") == "1") {
//        		var devHtm="";
//        		$("device",xml).each(function(){
//        			var device = $(this);
//        			var id = device.attr("id");
//        			var name = $("name",device).text();
//        			var number = $("number",device).text();
//        			var orderNum = $("order-num",device).text();
//        			var theOrderNum = $("the-order-num",device).text();
//        			var _order = (Number(number)-Number(orderNum));
//        			if (_order < 0) {
//        				_order = 0;
//        			}
//         			devHtm +="<table border=\"0\" cellspacing=\"1\" cellpadding=\"0\">" +
//        					"<tr><th>"+name+"</th></tr>" +
//        					"<tr><td>共有"+number+"个</td></tr>" +
//        					"<tr><td>可用<font>"+_order+"</font>个</td></tr>" +
//        					"<tr><td>预定<input name=\"remainNum\" type=\"hidden\" value=\""+_order+"\"/>" +
//        					"<input name=\"devId\" type=\"hidden\" value=\""+id+"\"/>" +
//        					"<input name=\"orderNum\" type=\"text\" class=\"ipt01\" style=\"width:15px; height:12px; vertical-align:middle\" value=\"" + theOrderNum + "\"  onblur=\"checkOrderNum($(this))\" />个</td> </tr>" +
//        					"</table>";
//        		});
//        		$("#deviceList").html(devHtm);
//        	}
//        },
//       	error : $.ermpAjaxError
//	});
//}

function checkOrderNum(obj){
	var num = obj.val();
	if(num == ""){
		return true;
	}
	var reg = /^[0-9]\d*$/;
	var isNumber = reg.test(num);
	if(!isNumber){
		$.alert("请输入正整数");
		obj.val("");
		obj.focus();
		return false;
	}
	var remainNum = Number(obj.parent().find("input[name='remainNum']").val());
	 if(Number(num) > remainNum){
	 	remainNum = remainNum < 0 ? 0 : remainNum;
		$.alert("该设备可预订数量为" + remainNum + "个");
		obj.val(remainNum);
		
		obj.select();
		return false;
	}
	return true;	
}

//检查会议室预订冲突
function checkMeetingRoom() {
	var roomId = meetingRoomSelect.getValue();
	if (roomId == "") {
		return;
	}
	var _bDate  = $.trim($("#beginDate").val());
   	var _bTime = beginTimeSel.getValue();
   	var _eDate = $.trim($("#endDate").val());
   	var _eTime = endTimeSel.getValue();
   	
	if(_bDate == "" || _bTime == "" || _eDate == "" || _eTime == ""){
		return;
	}
	var beginTime = _bDate + " " + _bTime;
	var endTime = _eDate + " " + _eTime;
	if(	$.compareDate(beginTime,endTime) || beginTime == endTime ){
		return;
	};
	
	$.ajax({
       type : "POST",
       cache: false,
       async: true,
       url  : "m/meet_info",
       data : {
       		act:"checkmeetingroom",
       		id : $("#id").val(),
       		beginTime: beginTime,
       		endTime : endTime,
       		roomId : roomId
       },
       success : function(xml){
           var messageCode = $("message",xml).attr("code");
           var msg = $("message",xml).text();
           if(messageCode == "1"){
             	submitFlag = true;
             	$("#roomFlag").html("");
           } else {
              	submitFlag = false;
             	$("#roomFlag").html("&nbsp;"+msg);
           }
       }
   	});
}

//手动添加参会人员
function addManCust(){
	var name = $.trim($("#name").val());
	var email = $.trim($("#email").val());
	var result = $.validChar(name);
	if (result) {
		$.alert("姓名不能包含非法字符：" + result);
		$("#name").focus();
		return;
	}
	if (name == "") {
		$.alert("姓名不能为空");
		$("#name").focus();
		return;
	}
	if(name.length > 36){
		$.alert("姓名长度不能超过36");
		return ;
	}
	if (email == "") {
		$.alert("邮箱不能为空");
		$("#email").focus();
		return;
	}
	if(email.length > 64){
		$.alert("邮箱长度不能超过64");
		return ;
	}
	if(!isEmail(email)){
		$.alert("邮箱格式不正确");
		$("#email").focus();
		return;
	}

	$("#bindUserList > tbody").append("<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">"
            						  +"<td><a class=\"opLink\" onclick=\"delUsers(this);\">删除</a><input name=\"name\" type=\"hidden\" value=\"" + name + "\"/></td>"
            						  +"<td style='white-space:normal'><div class='klWrap'>"+name +"</div></td>"
            						  +"<td class=\"klBox\">"+email+"<input name=\"email\" type=\"hidden\" value=\"" + email + "\"/></td>"
									  +"</tr>");
  	$("#name").val("");
  	$("#email").val("");
	$("#addMan").hide();
}
function isEmail(str){
	var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
	return reg.test(str);
}

function delUsers(a){
	$(a).parent().parent().remove();
	$("#saveUserRoles").removeAttr("disabled").removeClass("icoNone");
}

//初始化参与人选择中的机构列表
function initDeptList(){
	var url = "m/rbac_group?act=groupselect";
	$.getJSON(ERMP_PATH + "m/rbac_group/groupselect?jsoncallback=?",function(data){
		$("#groupIdDiv").html(data.htmlValue);
		var groupSelect = $("#groupIdDiv").ySelect({
			width:55,
			onChange: function(value){
				if (value != "") {
					$("#searchGroupUsers").click();
				} else {
					$("#actorUserList > tbody").empty();
				}
			}
		});
		groupSelect.addOption("", "所有...", 0);
		groupSelect.select(0);
	});
			
}

function searchUser(){
	var groupId = $.trim($("#hidGroupId").val());
	var keyword = $.trim($("#userKeyword").val());
	if (groupId == "" && keyword == "") {
		$.alert("请输入查询条件！");
		$("#userKeyword").focus();
		return;
	}
	var url = ERMP_PATH + "m/user_account/xmlusers?groupID="+groupId+"&keyword="+encodeURI(keyword) + "&jsoncallback=?";
	
	$.getJSON(url,function(data){
				if ($.checkErrorMsg(data) ) {
					var dataList = data.userAccounts;
        			if (dataList) {
		                var moduleActionHTML = "";
		                $(dataList).each(function(i) {
		                    	var groupName = dataList[i].groupNames;
		                        moduleActionHTML += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">" 
									+ "<td><a class=opLink>选择</a><input type=\"hidden\" value=\"" + dataList[i].accountID + "\"></td>"
									+ "<td>" + dataList[i].accountID + "</td>"
									+ "<td>" + dataList[i].displayName + "</td>" 
									+ "<td>" + groupName + "</td>"
									+ "</tr>";
		                });
		                $("#actorUserList > tbody").html(moduleActionHTML).find("td:empty").html("&nbsp;");
		               //点击绑定事件
			            $("#actorUserList a").click(function(e){
			                    //判断是否已被添加
			                    if($("#bindUserList input[value=" + $("+input",this).val() + "]").length != 0){
			                    	$.alert("不能重复选择");
			                        return;
			                    };
			                    var ojb = $(this).parent().next();
			                    var accountId = ojb.text();
			                    var displayName = ojb.next().text();
			                    
			                    $("#bindUserList > tbody").append("<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">"
			        						  +"<td><a class=\"opLink\" onclick=\"delUsers(this);\">删除</a><input name=\"name\" type=\"hidden\" value=\"" + accountId + "\"/></td>"
			        						  +"<td style='white-space:normal'><div class='klWrap'>"+displayName +"</div></td>"
			        						  +"<td class=\"klBox\">"+accountId+"<input name=\"email\" type=\"hidden\"/></td>"
											  +"</tr>");
			                    $("#saveUserRoles").removeAttr("disabled").removeClass("icoNone");
			              });
		        	}
	            } else {
	                $("#actorUserList > tbody").empty();
	            }
			});	
			
	
}

//加载参会人员
function loadParticipants() {
	$.ajax({
        type : "POST",
        cache: false,
        async : true,
		url  : "m/meet_info",
        data : {act : "getParticipants",
        		id : $("#id").val()},
        success : function(xml){
            var message = $("message",xml);
            if (message.attr("code") == "1") {
                var meetpHtml = "";
                $("meeting-participant",xml).each(function(index) {
                	var personName = $("person-name",this).text();
                	var accountOrEmail = $("account-or-email",this).text();
                	var type = $("type",this).text();
                	var name = type == "1" ? personName : accountOrEmail;
                	var email = type == "1" ? accountOrEmail : "";
               		meetpHtml += "<tr onmouseover=\"$(this).addClass('over');\" onmouseout=\"$(this).removeClass('over');\">"
          						+"<td><a class=\"opLink\" onclick=\"delUsers(this);\">删除</a><input name=\"name\" type=\"hidden\" value=\"" + name + "\"/></td>"
          						+"<td style='white-space:normal'><div class='klWrap'>"+personName +"</div></td>"
          						+"<td class=\"klBox\">"+accountOrEmail+"<input name=\"email\" type=\"hidden\" value=\"" + email + "\"/></td>"
							  	+"</tr>";
                    }
                );
                
                $("#bindUserList > tbody").html(meetpHtml);
            } else {
                $.alert(message.text());
            }
        },
        error : $.ermpAjaxError
    });
}

//初始化附件控件
function initControl(){
	//附件控件
	var id = $("#id").val();
  	NTKO_ATTACH_OCX = $.getNewAttachmentControl("NTKO_AttachmentCtrl");
	NTKO_ATTACH_OCX.setPermission({view:true,add:true,update:false,del:true,saveas:false});
	NTKO_ATTACH_OCX.setSaveURL(BASE_PATH + "m/meet_info?act=uploadAttachments&attachType=0");
	NTKO_ATTACH_OCX.setBasePath(BASE_PATH);
	NTKO_ATTACH_OCX.setAfterSaveEvent("saveAttachmentEvent");
	NTKO_ATTACH_OCX.init();
	if(id !=null && id.length>0){
		$.ajax({
	       type : "POST",
	       cache: false,
	       async: false,
	       url  : "m/meet_info",
	       data : "act=getattachments&attachType=0&referid=" + id,
	       success : function(xml){
	           var messageCode = $("message",xml).attr("code");
	           var msg = $("message",xml).text();
	           if(messageCode == "1"){
	               NTKO_ATTACH_OCX.setFileList($("content",xml)[0]);
	           } else {
	               $.alert($("message",xml).text());
	           }
	       }
	   	});
	}
}

//保存预订信息
function saveMeetingInfo(){	
	var roomId = meetingRoomSelect.getValue();
	var areaCode = areaSel.getValue();
	if(areaCode==""){
		$.alert("请选择所属地区");
		return;
	}
	if(roomId==""){
		$.alert("请选择会议室");
		return;
	}
	if(!submitFlag){
		$.alert("该会议室在该时间段不可以预订");
		return;
	}
	var _bDate  = $.trim($("#beginDate").val());
   	var _bTime = beginTimeSel.getValue();
   	var _eDate = $.trim($("#endDate").val());
   	var _eTime = endTimeSel.getValue();
   	
	if(_bDate == "" || _bTime == "" || _eDate == "" || _eTime == ""){
		return;
	}
	var beginTime = _bDate + " " + _bTime;
	var endTime = _eDate + " " + _eTime;
	if(	$.compareDate(beginTime,endTime) || beginTime == endTime){
		$.alert("开始时间必须小于结束时间！");
		return;
	}
	var theme = $.trim($("#theme").val());
	if (theme == "") {
		$.alert("会议主题不能为空");
		$("#theme").focus();
		return;
	}
	var result = $.validChar(theme);
	if (result) {
		$.alert("会议主题不能包含非法字符：" + result);
		$("#theme").focus();
		return;
	}
	var remark = $("#remark").val();
	result = $.validChar(remark);
	if (result) {
		$.alert("备注不能包含非法字符：" + result);
		$("#remark").focus();
		return;
	}
	if (remark.length >300) {
		$.alert("备注长度不能大于300");
		$("#remark").focus();
		return;
	}
	var persons="";
	$("#bindUserList > tbody tr").each(function(i){
		var users = $(this).find("input[type='hidden'][name='name']").val();
		var email = $(this).find("input[type='hidden'][name='email']").val();
		persons += "&person=" +encodeURI(users) +"**"+email;
	});
	
	if(persons==""){
		$.alert("请选择参会人员");
		return;
	}
	
	$.ajax({
		type : "POST",
		cache: false,
   		async : true,
      	url : "m/meet_info",
      	dataType : "xml",
      	data : "act=add&id=" + $("#id").val() + "&roomId=" + roomId + "&beginTime=" + beginTime + "&endTime=" + endTime + "&theme=" + theme + "&remark=" + remark + persons ,
        success : saveMeetingInfoSuccess,
       	error : $.ermpAjaxError
   	});
}

function saveMeetingInfoSuccess(xml){
	var message = $("message",xml);
	if(message.attr("code") == "1") {
		$("#tab00").hide();
		$("#but00").hide();
		$("#tab01").show();
		$("#but01").show();
		//刷新父列表
		mainFrame.getCurrentTab().doCallback();
		
   		var meeting = $("meet-info",xml);
   		
   		$("#id").val(meeting.attr("id"));
   		var begin_time = $("begin-time",meeting).text();
   		var end_time = $("end-time",meeting).text();		
   		var room_name =$("room-name",meeting).text();
   		var area_name =$("area-name",meeting).text();
   		var theme =$("theme",meeting).text();		
   		var group_name =$("group-name",meeting).text();
   		var appMan =$("apply-man-name",meeting).text();
   		var appTime =$("reserve-time",meeting).text();
   		var begin_times = begin_time.split(" ");
   		var end_times = end_time.split(" ");
   		var oldContent = $("content",meeting).text();
   		var content;
   		if (isReNofity//重发通知邮件
   				&& oldContent != "") {
   			$("#title").val("《"+ $("theme",meeting).text()+"》的会议通知");
   			content = oldContent;
   		} else if (isModify) {//会议变更通知
   			$("#title").val("《"+ $("theme",meeting).text()+"》的会议变更通知");
   			content = "　　<font style='font-style:italic;'>【此邮件系统自动发送，请勿回复！】</font><br/><br/>　　由于&nbsp;<U><B>________</U></B>&nbsp;原因，会议变更为：于<U>&nbsp;<B>"+formatTime(begin_times[0],begin_times[1],end_times[0],end_times[1])+"</B></U> 在  <U><B>"+area_name+room_name+"</B></U> 召开 <U><B>"+theme+"</B> </U>会议。 请相互转告，谢谢！ " +
        			"<BR><BR>" +
        			"<P style=\"PADDING-RIGHT: 1px\" align=right>"+group_name+" -&gt; "+appMan+"<BR>"+appTime+"</P><BR><BR><HR>";
        	content += $("content",meeting).text();
   		} else {//
   			$("#title").val("《"+ $("theme",meeting).text()+"》的会议通知");
   			//"<H3 align=center>会议通知</H3>"
   			content = "　　<font style='font-style:italic;'>【此邮件系统自动发送，请勿回复！】</font><br/><br/>　　兹定于<U>&nbsp;<B>"+formatTime(begin_times[0],begin_times[1],end_times[0],end_times[1])+"</B></U> 在  <U><B>"+area_name+room_name+"</B></U> 召开 <U><B>"+theme+"</B> </U>会议。 请准时参加。 " +
        			" <BR>会议内容： <BR><BR>1、［这里输入会议内容］ <BR><BR>" +
        		//	"<DIV id=divFiles>&nbsp;</DIV><BR>" +
        			"<P style=\"PADDING-RIGHT: 1px\" align=right>"+group_name+" -&gt; "+appMan+"<BR>"+appTime+"</P>";
   		}
   		
		$("#wysiwyg").val(content);	
		$('#wysiwyg').wysiwyg({
			getImg:getContentImg,
			html:'<html><head><base href="'+ BASE_PATH +'" /><meta http-equiv="Content-Type" content="text/html; charset=utf-8" />STYLE_SHEET</head><body>INITIAL_CONTENT</body></html>',
			controls : {
				separator06 : { visible : false }
			}
		});
	}else{
		$.alert(message.text());
	}
}

function loadMeetingInfo() {
	$.ajax({
		type : "POST",
		cache: false,
   		async : true,
      	url : "m/meet_info",
      	dataType : "xml",
      	data : {
      		act : "getMeetingInfo",
      		id : $("#id").val()
      	},
        success : saveMeetingInfoSuccess,
       	error : $.ermpAjaxError
   	});
}

//富文本控件绑定方法（上传图片）
function getContentImg() {
//	var params = new Object();
//	params.szURL ="";
//	params.id = $("#id").val();
//	params.attType="img";
//	var returnStatus = window.showModalDialog(BASE_PATH + "page/meeting/order/dialog_addimg.jsp",params,"dialogHeight:400px;dialogWidth:590px;status:no;scroll:auto;help:no");
// 	if(returnStatus =="ok"){
// 		 if ( params.szURL && params.szURL.length > 0 ){
//           return params;
// 		 }
// 	}	
//	return null;
}

var isSendEmail = false;
//提交会议内容
function submitEmail(flag){
	isSendEmail = flag;
	NTKO_ATTACH_OCX.saveAttachments($("#id").val());
}

function saveAttachmentEvent(code,message){
	if(code == 0){
		$.alert(message);
		return;
	}
	var id = $("#id").val();
	var title = $("#title").val();
	var content = $("#wysiwyg").val();
	$.ajax({
       	type : "POST",
       	cache: false,
       	async: false,
       	url  : "m/meet_info",
      	data : {
       		act : "savemeetingcontent",
       		id: id,
       		title : title,
       		content : content,
       		flag :isSendEmail
       	},
       	success : function(xml){
           	var messageCode = $("message",xml).attr("code");
           	var msg = $("message",xml).text();
           	if(messageCode == "1"){
               $.alert($("message",xml).text(), function(){
	               //关闭
					mainFrame.getCurrentTab().close();
               });
				
           	} else {
               	$.alert($("message",xml).text());
           	}
       	}
	});
}

function formatTime(bDate,bTime,eDate,eTime) {
	if (bDate == eDate) {
		return bDate + " " + bTime + "至" + eTime;
	} else {
		return bDate + " " + bTime + "至" + eDate + " " + eTime;
	}
}