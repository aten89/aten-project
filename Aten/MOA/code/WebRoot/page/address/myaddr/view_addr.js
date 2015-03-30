$(init);//程序入口

function init(){
	$.handleRights({
		"btnModify" : $.SysConstants.MODIFY
	});
	
	//修改
    $("#btnModify").click(function(){
	    	window.location.href = BASE_PATH + "/m/myaddr?act=initmodify";    	
    		
        }
    );  
    
    var saveImgPath = $("#saveImgPath").val();   
    var fileName = "";
    if(saveImgPath == ""){
    	//数据库中没有头像时,显示默认头像
    	fileName = BASE_PATH + "themes/comm/images/hd_default.jpg";//默认头像
    } else {
    	//数据库中有头像时显示数据库中的头像
    	fileName = saveImgPath;
    }    
    $("#imgDiv").html("<img src=\""+fileName +"\" width=\"102px\" height=\"102px\">");
    enWrap();	
}

//折行
function enWrap(){
	var D=document; F(D.body); function F(n){var u,r,c,x; if(n.nodeType==3){ u=n.data.search(/\S{10}/); if(u>=0) { r=n.splitText(u+10); n.parentNode.insertBefore(D.createElement("WBR"),r); } }else if(n.tagName!="STYLE" && n.tagName!="SCRIPT"){for (c=0;x=n.childNodes[c];++c){F(x);}} } 
}
