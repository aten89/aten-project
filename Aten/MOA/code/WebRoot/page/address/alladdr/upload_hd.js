$(initUpload);

function initUpload(){
    var code = $("#code").val();
    var msg = $("#msg").val();
    var saveImgPath = parent.$("#saveImgPath").val();
    var tempImgPath = $("#tempImgPath").val();//预览图片
    var fileName = "";
    if(tempImgPath != ""){    	
    	fileName = tempImgPath;
    } else if(saveImgPath == ""){
    	//不存在即将上传的头像,同时数据库中也没有头像时,显示默认头像
    	fileName = BASE_PATH + "themes/comm/images/hd_default.jpg";//默认头像
    } else { 
    	//不存在即将上传的头像,数据库中有头像时显示数据库中的头像
    	fileName = BASE_PATH + saveImgPath;
    }    
    
    $("#imgDiv").html("<img src='"+fileName +"' width=102px height=103px>");
    
    if (msg !== ""){
		$.alert(msg);
	}
}


function postFile(){
	if (document.forms["formHD"].fileHD.value != ""){
		document.forms["formHD"].submit();
		return true;
	} else {
		return false;
	}
}

function fclick(obj){
   with(obj){
     style.posTop=event.srcElement.offsetTop;
     var x=event.x-offsetWidth/2;
     if(x<event.srcElement.offsetLeft)x=event.srcElement.offsetLeft;
     if(x>event.srcElement.offsetLeft+event.srcElement.offsetWidth-offsetWidth)x=event.srcElement.offsetLeft+event.srcElement.offsetWidth-offsetWidth;
     style.posLeft=x;
   }
}