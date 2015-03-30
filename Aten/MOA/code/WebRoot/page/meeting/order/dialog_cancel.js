$(initPage);
var sendEmailFlag = false;
function initPage(){
	$("#editMeeting,#editMeetingTit").hover(function(){
		$(this).addClass("itfocus");
		$(this).attr("title","可编辑会议内容");
	},function(){
		$(this).removeClass("itfocus");
	});

	var beginTime = $("#beginTime").val();
	var endTime = $("#endTime").val();
	var bTem = beginTime.split(" ");
	var eTem = endTime.split(" ");
	$("#termTime").html(formatTime(bTem[0], bTem[1], eTem[0], eTem[1]));
	
	//关闭	
	$("#closeBtn").click(function(){
		window.returnValue = "close";
  		window.close(); //关闭当前窗口
  		
	});
	
	//取消并发送
	$("#cancelAndSendBtn").click(function(){
  		cancelMeeting(true);
	});
	
	//我要取消
	$("#cancelBtn").click(function(){
		cancelMeeting(false);
  		
	});
	
	//生成当前日期
	$("#currentDate").html((new Date()).format("20YY年MM月dd日"));
}

function formatTime(bDate,bTime,eDate,eTime) {
	if (bDate == eDate) {
		return bDate + " " + bTime + "至" + eTime;
	} else {
		return bDate + " " + bTime + "至" + eDate + " " + eTime;
	}
}

function cancelMeeting(sendEmailFlag){
	var id = $("#id").val();
	var subject = $("#editMeetingTit").html();
	var content = $("#editMeeting").html();
	
	if (subject == "") {
		$.alert("邮件标题不能为空");
		$("#editMeetingTit").focus();
		return false;
	}
	
	//提交到后台
	$.ajax({
	    type : "POST",
		cache: false,
		async : true,
		url  : "m/meet_info",
		data : {act:"cancelMeeting",
				id:id,
				subject:subject,
				sendEmailFlag:sendEmailFlag,
				content:content},
	       success : function(xml){
	     		//解析XML中的返回代码
				var messageCode = $("message",xml).attr("code");
				if(messageCode == "1"){
					if (sendEmailFlag) {
						window.returnValue = "cancelAndSend";
						$.alert("取消并发送邮件成功", function(){
							window.close(); //关闭当前窗口
						});
					} else {
						window.returnValue = "cancel";
						$.alert("取消成功", function(){
							window.close(); //关闭当前窗口
						});
					}
					
				}else {
					$.alert($("message",xml).text());
					//$.alert("发送失败");
				}
	       },
	       error : $.ermpAjaxError
	});	
}

//格式化日期
Date.prototype.format = function(format){
	var o = {
  		"YY+" :  this.getFullYear(),//year
  		"M+" :  this.getMonth()+1,  //month
  		"d+" :  this.getDate()     //day
   	}
   	if(/(y+)/.test(format)) {
    	format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
   	}
   	for(var k in o) {
    	if(new RegExp("("+ k +")").test(format)) {
      	format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length));
    	}
   	}
 	return format;
}

