var mainPanel = $.getMainFrame();
var selectScore; //知识评分
/**
 * 知识点默认评分
 */
var DRFAULT_SCORE = 80;
$(initArchivesManage);

var totalCount;

function initArchivesManage(){
	$("#addReply").click(function(){
		$("#addReply").attr("disabled",true);
		saveReply();
	});
	$("#comment li").click(function(){
		$(this).addClass("current").siblings().removeClass("current");
		if($(this).attr("value")=="0"){
			$("#addReplyDiv").show();
			gotoPage(1);
		}else{
			$("#addReplyDiv").hide();
			gotoPage(1);
		}
	});
	
	gotoPage(1);
	initControl();
	sizeImg();
	enWrap();
	
}

function initControl(){
	//附件控件
	var id = $("#id").val();
  	NTKO_ATTACH_OCX = $.getNewAttachmentControl("NTKO_AttachmentCtrl");
	NTKO_ATTACH_OCX.setPermission({view:true,add:false,update:false,del:false,saveas:true});
	NTKO_ATTACH_OCX.setSaveURL(BASE_PATH + "m/knowledge?act=uploadattachments");
	NTKO_ATTACH_OCX.setBasePath(BASE_PATH);
	NTKO_ATTACH_OCX.init();
	if(id !=null && id.length>0){
		$.ajax({
	       type : "POST",
	       cache: false,
	       async: false,
	       url  : "m/knowledge",
	       data : "act=getattachments&referid=" + id,
	       success : function(xml){
	           var messageCode = $("message",xml).attr("code");
	           var msg = $("message",xml).text();
	           if(messageCode == "1"){
	               NTKO_ATTACH_OCX.setFileList($("content",xml)[0]);
	           } else {
	               $.alert(msg);
	           }
	       }
	   	});
	}
}

function saveReply(){
	var replyContent = $("#reply").val();
	var id = $("#id").val();
	var pageno=$.trim($("#hidNumPage").val());
	var pagecount=$.trim($("#hidPageCount").val());
	if(!vaildateParam()){
		$("#addReply").attr("disabled",false);
		return;
	}
	$.ajax({
		type : "POST",
		cache: false,
   		async : false,
      	url : "m/knowledge",
      	dataType : "xml",
      	data : {
      		act : "saveReply",
			id : id,
			replyContent : replyContent,
			pageno:pageno,
			pagecount: pagecount
      	},
        success : function(xml){
        	var message = $("message",xml);
			var replyHtm ="";
        	if(message.attr("code") == "1") {
				$.alert("保存成功");
				$("#reply").val("");
				//转到最后一页
				var pagecount=$.trim($("#hidPageCount").val());
				gotoPage(0);
        	}else{
        		$.alert("保存失败，原因："+message.text());
        	}
        	$("#addReply").attr("disabled",false);
        },
       	error : $.ermpAjaxError
	});

}
function gotoPage(pageNo, totalPage){
    if(isNaN(pageNo)){
        $.alert("页索引只能为数字！");
        return;
    } else if(pageNo < 1){
        pageNo = 1;
    }
    if (totalPage && !isNaN(totalPage) && pageNo > totalPage) {
    	pageNo = totalPage;
    }
    $("#hidNumPage").val(parseInt(pageNo));
    replyList();
}

function replyList(){
	var id = $("#id").val();
	var pageno=$.trim($("#hidNumPage").val());
	var pagecount=$.trim($("#hidPageCount").val());
	$.ajax({
		type : "POST",
		cache: false,
   		async : true,
      	url : "m/knowledge",
      	dataType : "xml",
      	data : {
      		act : "loadreplylist",
			id : id,
			pageno:pageno,
			pagecount: pagecount
      	},
        success : function(xml){
        	var message = $("message",xml);
			var replyHtm ="";
        	if(message.attr("code") == "1") {
        		var	pageCount =	$("#hidPageCount").val();
        		var content = $("content", xml);
        		var total_num = content.attr("total-count");
        		$("#klNum").html(total_num);
        		totalCount = content.attr("total-count");
        		var startNum =0;
        		startNum = (content.attr("current-page")-1)*pageCount +1;
				$("knowledge_reply" ,xml).each(function (index){		
					var curEle = $(this);
					var isOwner = false;
					if ($("accountID",curEle).text() == $("#userAccount").val()) {
						isOwner = true;
					}
					replyHtm +="<div class=\"klResponses\">"
		       					+"<div class=\"klName\"><span style=\"float:right\">"+ (($.hasRight("1")|| isOwner) ? "<A class=\"cb01\" style=\"text-decoration:none\" href=\"javascript:void(0)\" onclick=\"deleteKnowledegeReply('"+curEle.attr("id")+"')\">删除</A>" : "") +"</span>"+"# "+$("reply_man",curEle).text() + "["+$("group",curEle).text()+ "]" +" <span class=\"klTime\">["+$("reply_date",curEle).text()+"]</span></div>"
		            			+"<p>"+$("reply_content",curEle).text()+"</p>"
       							+"</div>";
					
				});
				$("#replyLst").html(replyHtm);
				//------------------翻页数据----------------------------
		        $(".pageNext").html($.createTurnPage(xml));
		        //----------------------------------------------------		
		        $.EnterKeyPress($("#numPage"),$("#numPage").next());
				enWrap();
        	}else{
        		$.alert("保存失败，原因："+message.text());
        	}
        },
       	error : $.ermpAjaxError
	});
}

function vaildateParam() {
	var reply = $.trim($("#reply").val());
	var result = $.validChar(reply);
	if (result) {
		$.alert("回复内容不能包含非法字符：" + result);
		$("#reply").focus();
		return false;
	}
	if (reply == "") {
		$.alert("请输入回复内容");
		$("#reply").focus();
		return false;
	}
	return true;
}

//折行
function enWrap(){
	var D=document; F(D.body); function F(n){var u,r,c,x; if(n.nodeType==3){ u=n.data.search(/\S{10}/); if(u>=0) { r=n.splitText(u+10); n.parentNode.insertBefore(D.createElement("WBR"),r); } }else if(n.tagName!="STYLE" && n.tagName!="SCRIPT"){for (c=0;x=n.childNodes[c];++c){F(x);}} } 
}

function sizeImg(){
	var ws=730;
	var hs=500;
	var rate;
	$("#knowledgeCon img").each(function(){
		rate=(ws/$(this).width()<hs/$(this).height()?ws/$(this).width():hs/$(this).height());
		if(rate<=1){
			var newW=$(this).width()*rate;
			var newH=$(this).height()*rate;		
			$(this).width(newW);
			$(this).height(newH);
			
		}else{				
			return;
		}
	});
}
function deleteKnowledegeReply (knowledgeReplyId){
	$.confirm("确定要删除评论？",function(r){
		if (r) {
			$.ajax({
				type : "POST",
				cache: false,
		   		async : false,
		      	url : "m/knowledge",
		      	dataType : "xml",
		      	data : {
		      		act : "deleteReply",
					id : knowledgeReplyId
		      	},
		        success : function(xml){
		        	var message = $("message",xml);
		        	if(message.attr("code") == "1") {
		//				$.alert("删除成功");
						gotoPage(1);
		        	}else{
		        		$.alert("保存失败，原因："+message.text());
		        	}
		        },
		       	error : $.ermpAjaxError
			});
		}
	});
}