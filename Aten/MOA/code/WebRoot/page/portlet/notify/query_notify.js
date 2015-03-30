var mainFrame = $.getMainFrame();

$(initPage);
function initPage(){
	$.ajax({
	   type : "POST",
	   cache: false,
	   async : true,
	   url  : BASE_PATH + "m/flow_notify",
	   data : "act=query&pageno=1&pagecount=5",             
	   success : function (xml){
	   		var message = $("message",xml);
			var content = $("content",xml);
			var listDate = "<b>您当前总共有&nbsp;<font color=red>" + content.attr("total-count") + "</font>&nbsp;个知会信息。</b>";
			//是否有数据返回
		    if (message.attr("code") == "1") {	                              	    	  
		    	$(xml).find('flow-notify').each(function(index){
		    		var curELe = $(this);
		    		var viewFlag = $("status",curELe).text() != "2";
		    		var subject = $("subject",curELe).text();
                    listDate += "<li "
                    		+ (viewFlag ? "style=\"font-weight:bold\">" : ">")
                    		+ "<a href=\"javascript:openNotify('"
                    		+ curELe.attr("id") + "')\"  title=" + subject + ">"
                    		+ subject + getFlowStatusName($("flow-status",curELe).text()) + "</a></li>";
				});
				$("#taskList").html(listDate);
		    } else {
		        $.alert(message.text());
		    };
	   },
	   error : function(){
	        $.ermpAjaxError();
	    }
	});
}

function getFlowStatusName(flowStatus) {
	if (flowStatus == "1") {
		return "【通过】"
	} else if (flowStatus == "0") {
		return "【作废】"
	} else if (flowStatus == "2") {
		return "【审批中】"
	} else {
		return "";
	}
}

function openNotify(id) {
	var url=BASE_PATH + "m/flow_notify?act=view&id=" + id;
	mainFrame.addTab({
		id:"notify+" + id,
		title:"知会信息",
		url:url
//		onClose:initPage
	});
	initPage();
}
