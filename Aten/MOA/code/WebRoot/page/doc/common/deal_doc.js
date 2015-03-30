var mainFrame = $.getMainFrame();

$(init);
function init(){
	//刷新
  	$("#refresh").click(function(){
  		queryList();
  	});
  	//查询
  	queryList();
};

function queryList(){

	$("#docList").empty();
	$.ajax({
	    type : "POST",
	    cache: false,
	    async : true,
	    url  : "m/nor_deal?act=query",
	    success : function(xml){
	        var message = $("message",xml);
	        if(message.attr("code") == "1"){
	            //------------------列表数据----------------------------
	            var listDate = "";
	            $("document",xml).each(
	                function(index){
	                    var curELe = $(this);
	                    var viewFlag = $("view-flag",curELe).text();
	                    listDate += " <tr onmouseout=\"$(this).removeClass('over');\" onmouseover=\"$(this).addClass('over');\" "
	                    		+ (viewFlag == "false" ? "style=\"font-weight:bold\">" : ">")
								+ "<td>" + $("subject",curELe).text() + "</td>"
								+ "<td>" + $("draft-date",curELe).text() + "</td>"
								+ "<td>" + $("drafts-man",curELe).text() + "</td>"
								+ "<td>" + $("doc-class",curELe).text() + "</td>"
								//+ "<td>" + $("task-name",curELe).text() + "</td>"
								+ "<td>" + ( $.hasRight($.OaConstants.DISPOSE) ? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"initDispose('" + $("task-id",curELe).text() + "','" + $("taskinstance-id",curELe).text() + "')\">处理</a> " : "")
								+ "</td></tr>";
	                }
	            );
	            $("#docList").append(listDate);
				enWrap();
	            $("#totalCount").html($("content",xml).attr("total-count"));
	        }
	    },
	    error : $.ermpAjaxError
	});
}
function initDispose(taskid, tiid) {
	mainFrame.addTab({
		id:"oa_document_deal_dispose_" + taskid,
		title:"处理公文",
		url:BASE_PATH + "/m/nor_deal?act=dispose&taskid="+taskid+"&tiid=" + tiid,
		callback:queryList
	});
}

function enWrap(){
	var D=document; F(D.body); function F(n){var u,r,c,x; if(n.nodeType==3){ u=n.data.search(/\S{10}/); if(u>=0) { r=n.splitText(u+10); n.parentNode.insertBefore(D.createElement("WBR"),r); } }else if(n.tagName!="STYLE" && n.tagName!="SCRIPT"){for (c=0;x=n.childNodes[c];++c){F(x);}} } 
}