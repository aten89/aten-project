var mainFrame = $.getMainFrame();

var NTKO_ATTACH_OCX_MINUTES;
var sendMinutesFlag = false;
$(initPage);

function initPage(){
	var beginTime = $("#beginTime").val();
	var endTime = $("#endTime").val();
	var bTem = beginTime.split(" ");
	var eTem = endTime.split(" ");
	$("#termTime").html(formatTime(bTem[0], bTem[1], eTem[0], eTem[1]));
	sizeImg();
	//会议变更
	$("#meetingModify").click(function(){
  		meetingModify();
	});
	
	//会议取消
	$("#meetingCancel").click(function(){
  		meetingCancel();
	});
	
	//保存会议纪要
	$("#submitMinutes").click(function(){
  		submitMinutes(false);
	});
	
	//保存并发送会议纪要
	$("#submitAndSend").click(function(){
  		submitMinutes(true);
	});
	
	//重发会议通知
	$("#meetingReInform").click(function(){
  		meetingReNotify();
	});
	
	if($("#NTKO_AttachmentCtrl1").size() == 1) {
		initAttasControl();
	}
	if($("#NTKO_AttachmentCtrl2").size() == 1) {
		initMinutesControl();
	}
	
	enWrap();
}

function sizeImg(){
	var ws=550;
	var hs=300;
	var rate;
	$("#mCon01 img").each(function(){
		rate=(ws/$(this).width()<hs/$(this).height()?ws/$(this).width():hs/$(this).height());
		//$.alert(rate);
		if(rate<=1){
			var newW=$(this).width()*rate;
			var newH=$(this).height()*rate;		
			$(this).width(newW);
			$(this).height(newH);
			
		}else{				
			return;
		}
	})	
}
function formatTime(bDate,bTime,eDate,eTime) {
	if (bDate == eDate) {
		return bDate + " " + bTime + "至" + eTime;
	} else {
		return bDate + " " + bTime + "至" + eDate + " " + eTime;
	}
}

function initAttasControl(){
	//附件控件
	var id = $("#id").val();
	var NTKO_ATTACH_OCX = $.getNewAttachmentControl("NTKO_AttachmentCtrl1");
	NTKO_ATTACH_OCX.setPermission({view:true,add:false,update:false,del:false,saveas:true});
	NTKO_ATTACH_OCX.setBasePath(BASE_PATH);
	NTKO_ATTACH_OCX.init();
	if(id !=null && id.length>0){
		$.ajax({
	       type : "POST",
	       cache: false,
	       async: false,
	       url  : "m/meet_info",
	       data : "act=getattachments&referid=" + id + "&attachType=0", //attachType=0为会议资料
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

function initMinutesControl(){
	//附件控件
	var id = $("#id").val();
  	NTKO_ATTACH_OCX_MINUTES = $.getNewAttachmentControl("NTKO_AttachmentCtrl2");
	NTKO_ATTACH_OCX_MINUTES.setPermission({view:true,add:true,update:true,del:true,saveas:true});
	NTKO_ATTACH_OCX_MINUTES.setSaveURL(BASE_PATH + "m/meet_info?act=uploadAttachments&attachType=1");
	NTKO_ATTACH_OCX_MINUTES.setBasePath(BASE_PATH);
	NTKO_ATTACH_OCX_MINUTES.setAfterSaveEvent("saveAttachmentEvent");
	NTKO_ATTACH_OCX_MINUTES.init(id);
	
	if(id !=null && id.length>0){
		$.ajax({
	       type : "POST",
	       cache: false,
	       async: false,
	       url  : "m/meet_info",
	       data : "act=getattachments&referid=" + id + "&attachType=1", //attachType=1为会议纪要
	       success : function(xml){
	           var messageCode = $("message",xml).attr("code");
	           var msg = $("message",xml).text();
	           if(messageCode == "1"){
	               NTKO_ATTACH_OCX_MINUTES.setFileList($("content",xml)[0]);
	           } else {
	               $.alert($("message",xml).text());
	           }
	       }
	   	});
	}
}


function meetingModify() {
	var id = $("#id").val();
	mainFrame.getCurrentTab().setTitle("会议变更", "会议变更");
	self.location = BASE_PATH + "/m/meet_info?act=toModifyPage&id="+id;
}

function meetingCancel() {
	var id = $("#id").val();
	var returnValue = window.showModalDialog(BASE_PATH + "/m/meet_info?act=toCancelPage&id=" + id,"","dialogHeight:340px;dialogWidth:630px;status:no;scroll:auto;help:no");
	if("cancel"==returnValue || "cancelAndSend"==returnValue){
		//执行回调函数，刷新父列表
		mainFrame.getCurrentTab().doCallback();
		//关闭自己
		mainFrame.getCurrentTab().close();
	}
	else if("close"==returnValue){
		//不做响应
	}
}

function meetingReNotify(){
	var id = $("#id").val();
	mainFrame.getCurrentTab().setTitle("重发会议通知", "重发会议通知");
	self.location = BASE_PATH + "/m/meet_info?act=toModifyPage&resend=true&id="+id;
}

function submitMinutes(flag){
	sendMinutesFlag = flag;
	//保存附件
	NTKO_ATTACH_OCX_MINUTES.saveAttachments();
}

function saveAttachmentEvent(code,message){
	if(code == 0){
		$.alert(message);
		return;
	}
	if (sendMinutesFlag) {
		var id = $("#id").val();
		window.showModalDialog(BASE_PATH + "/m/meet_info?act=toMinutePage&id=" + id,"","dialogHeight:300px;dialogWidth:630px;status:no;scroll:auto;help:no");
	} else {
		$.alert("保存成功");
	}
	//关闭
	mainFrame.getCurrentTab().close();
}
function enWrap(){
	var D=document; F(D.body); function F(n){var u,r,c,x; if(n.nodeType==3){ u=n.data.search(/\S{10}/); if(u>=0) { r=n.splitText(u+10); n.parentNode.insertBefore(D.createElement("WBR"),r); } }else if(n.tagName!="STYLE" && n.tagName!="SCRIPT"){for (c=0;x=n.childNodes[c];++c){F(x);}} } 
}