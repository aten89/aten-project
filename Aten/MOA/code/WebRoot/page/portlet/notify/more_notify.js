var mainFrame = $.getMainFrame();
var flowClassSel;
var flowStatusSel;

var flowClassMeta = {
	QJLC : "请假流程",
	XJLC : "销假流程",
	RZLC : "入职流程",
	LZLC : "离职流程",
	YDLC : "异动流程",
	ZZLC : "转正流程"
};

$(initPage);

function initPage(){
	//刷新
  	$("#refresh").click(function(){
  		queryPage();
  	});
  	
  	//查询
  	$("#queryNotify").click(function(){
  		gotoPage(1);
  	});
  	
  	//打开用户帐号
   $("#openUserSelect").click(
	   	function(e){
			var dialog = new UserDialog(ERMP_PATH, BASE_PATH);
			dialog.setCallbackFun(function(user){
				if (user != null) {
					$("#creator").val(user.id);
					$("#creatorName").val(user.name);
				}
			});
			dialog.openDialog("single");
		}
	);
	
	//初始化下拦
	var selItemDIV = "<div>**所有...</div>";
	$.each(flowClassMeta, function(k, v) {
	  selItemDIV += "<div>" + k + "**" + v + "</div>"
	});
	flowClassSel = $("#flowClassDIV").html(selItemDIV).ySelect({width:100});
	
	flowStatusSel = $("#flowStatusDIV").ySelect({width:60});
	
	$.EnterKeyPress($("#subject"),$("#queryNotify"));
  	$.EnterKeyPress($("#creatorName"),$("#queryNotify"));
  	
    gotoPage(1);
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
    queryPage();
};

function queryPage() {

	$("#taskList").empty();
 	
	var pageno=$.trim($("#hidNumPage").val());
	var pagecount=$.trim($("#hidPageCount").val());
	var beginNotifyTimeStr = $("#begintime").val();
	var endNotifyTimeStr = $("#endtime").val();
	if ( $.compareDate(beginNotifyTimeStr, endNotifyTimeStr)) {
		$.alert("开始时间不能大于结束时间！");
		return false;
	}
	
	$.ajax({
	   type : "POST",
	   cache: false,
	   async : true,
	   url  : "m/flow_notify",
	   data : {act: "query",
	   		pageno : pageno,
	   		pagecount : pagecount,
	   		subject : $("#subject").val(),
	   		creator : $("#creator").val(),
	   		flowClass : flowClassSel.getValue(),
	   		flowStatus : flowStatusSel.getValue(),
	   		beginNotifyTimeStr : beginNotifyTimeStr,
	   		endNotifyTimeStr : endNotifyTimeStr
	   	},             
	   success : function (xml){
	   		var message = $("message",xml);
			var content = $("content",xml);
			var listDate = "";
			//是否有数据返回
		    if (message.attr("code") == "1") {                      	    	  
		    	$(xml).find('flow-notify').each(function(index){
		    		var curELe = $(this);
		    		var viewFlag = $("status",curELe).text() != "2";
                    var id = curELe.attr("id");
                    listDate += " <tr onmouseout=\"$(this).removeClass('over');\" onmouseover=\"$(this).addClass('over');\" "
                   			+ (viewFlag ? "style=\"font-weight:bold\">" : ">")
                   			+ "<td>" + (index + 1) + "</td>"
							+ "<td>" + $("subject",curELe).text() + "</td>"
							+ "<td>" + $("creator-name",curELe).text() + "</td>"
							+ "<td>" + $("group-full-name",curELe).text() + "</td>"
							+ "<td>" + getFlowClassName($("flow-class",curELe).text()) + "</td>"
							+ "<td>" + getFlowStatusName($("flow-status",curELe).text()) + "</td>"
							+ "<td>" + $("notify-time",curELe).text() + "</td>"
							+ "<td><a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"javascript:openNotify('" 
                    		+ curELe.attr("id")  + "')\">查看</a> "
							+ "</td></tr>";

				});
				$("#taskList").html(listDate);
				//----------------------------------------------------
		        //------------------翻页数据----------------------------
		        $(".pageNext").html($.createTurnPage(xml));
		        //----------------------------------------------------		
		        $.EnterKeyPress($("#numPage"),$("#numPage").next());
		    } else {
		        $.alert("没有查询结果");
		    };
	   },
	   error : function(){
	        $.ermpAjaxError();
	    }
	});
}

function getFlowClassName(flowClass) {
	var name = "";
	$.each(flowClassMeta, function(k, v) {
	  if (flowClass == k) {
	  	name = v;
	  	return false;
	  }
	});
	return name;
}

function getFlowStatusName(flowStatus) {
	if (flowStatus == "1") {
		return "通过"
	} else if (flowStatus == "0") {
		return "作废"
	} else if (flowStatus == "2") {
		return "审批中"
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
	queryPage();
}