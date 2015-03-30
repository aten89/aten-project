var mainFrame = $.getMainFrame();

$(initInfoTODO);
function initInfoTODO(){

	//添加权限约束
    $.handleRights({
        "searchInfo" : $.SysConstants.QUERY
    });

	//刷新
  	$("#refresh").click(function(){
  		queryPage();
  	});
  	
  	//查询
  	$("#searchInfo").click(function(){
  		gotoPage(1);
  	});
  	gotoPage(1);
  	//回车搜索
	$.EnterKeyPress($("#subject"),$("#searchInfo"));
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
}

function queryPage() {
	if ( $.compareDate($("#beginDraftDate").val(), $("#endDraftDate").val())) {
		$.alert("开始时间不能大于结束时间！");
		return false;
	}
	//设置查询按钮
	$("#searchInfo").attr("disabled","true");
	$("#inforTrackList").empty();
 	
	var pageno=$.trim($("#hidNumPage").val());
	var pagecount=$.trim($("#hidPageCount").val());
	var beginDraftDate=$.trim($("#beginDraftDate").val());
	var endDraftDate=$.trim($("#endDraftDate").val());
	var subject = $.trim($("#subject").val());

	$.ajax({
	   type : "POST",
	   cache: false,
	   async : true,
	   url  : "m/info_track",
	   data : {
	   		act:"query",
	   		pageno:pageno,
	   		pagecount:pagecount,
	   		subject:subject,
	   		beginDraftDate:beginDraftDate,
	   		endDraftDate:endDraftDate	
	   },             
	   success : function (xml){
	   		var message = $("message",xml);
			var content = $("content",xml);
			var listDate = "";
			//是否有数据返回
		    if (message.attr("code") == "1") {	                              	    	  
		    	$(xml).find('info-form').each(function(index){
		    		var curELe = $(this);
                    var id = curELe.attr("id");
                    listDate += " <tr onmouseout=\"$(this).removeClass('over');\" onmouseover=\"$(this).addClass('over');\">"
							+ "<td>" + $("subject",curELe).text() + "</td>"
							+ "<td>" + $("draft-date",curELe).text() + "</td>"
							+ "<td>" + $("drafts-man",curELe).text() + "</td>"
							+ "<td>" + $("node-name",curELe).text() + "</td>"
							+ "<td>" + $("transactor",curELe).text() + "</td>"
							+ "<td>" + $("create-time",curELe).text() + "</td>"
							+ "<td>" + ( $.hasRight($.SysConstants.VIEW)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"initView('" + id + "')\">详情</a> " : "")
							+ "</td></tr>";

				});
				$("#inforTrackList").html(listDate);
				enWrap();
				//----------------------------------------------------
		        //------------------翻页数据----------------------------
		        $(".pageNext").html($.createTurnPage(xml));
		        //----------------------------------------------------		
		        $.EnterKeyPress($("#numPage"),$("#numPage").next());
		    } else {
		        $.alert("没有查询结果");
		    };
	   		$("#searchInfo").removeAttr("disabled");
	   },
	   error : function(){
	        $("#searchInfo").removeAttr("disabled");
	        $.ermpAjaxError();
	    }
	});
}

function initView(id) {
	mainFrame.addTab({
		id:"oa_info_track_viewd_"+id,
		title:"跟踪审批单",
		url:BASE_PATH + "/m/info_track?act=view&id="+id
	});
}

function enWrap(){
	var D=document; F(D.body); function F(n){var u,r,c,x; if(n.nodeType==3){ u=n.data.search(/\S{10}/); if(u>=0) { r=n.splitText(u+10); n.parentNode.insertBefore(D.createElement("WBR"),r); } }else if(n.tagName!="STYLE" && n.tagName!="SCRIPT"){for (c=0;x=n.childNodes[c];++c){F(x);}} } 
}