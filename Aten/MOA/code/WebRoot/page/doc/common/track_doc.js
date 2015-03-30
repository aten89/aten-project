var mainFrame = $.getMainFrame();

$(init);
function init(){   
  //跟踪文件  	
	 $.handleRights({
        "searchDoc" : $.SysConstants.QUERY
    });

	//刷新
  	$("#refresh").click(function(){
  		queryList();
  	});
  	
  	//查询
  	$("#searchDoc").click(function(){
  		gotoPage(1);
  	});
  	gotoPage(1);
  	$('.invokeBoth').attachDatepicker({showOn: 'both', buttonImage: 'themes/comm/spacer.gif', buttonImageOnly: true});
  	//回车搜索
	$.EnterKeyPress($("#subject"),$("#searchDoc"));
	
	
};

function queryList(){

	if ( $.compareDate($("#beginDraftDate").val(), $("#endDraftDate").val())) {
		$.alert("开始时间不能大于结束时间！");
		return false;
	}
	//设置查询按钮
	$("#searchDoc").attr("disabled","true");

	$("#docTrackList").empty();
 	
	var pageno=$.trim($("#hidNumPage").val());
	var pagecount=$.trim($("#hidPageCount").val());
	var beginDraftDate=$.trim($("#beginDraftDate").val());
	var endDraftDate=$.trim($("#endDraftDate").val());
	var subject = $.trim($("#subject").val());
	
	$.ajax({
	   type : "POST",
	   cache: false,
	   async : true,
	   url  : "m/nor_track",
	   data : {
	   		act:"query",
	   		pageno:pageno,
	   		pagecount:pagecount,
	   		beginDraftDate:beginDraftDate,
	   		endDraftDate:endDraftDate,
	   		subject:subject
	   },             
	   success : function (xml){
	   		var message = $("message",xml);
			var content = $("content",xml);
			var listDate = "";
			//是否有数据返回
		    if (message.attr("code") == "1") {	                              	    	  
		    	$(xml).find("doc-form").each(function(index){
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
				$("#docTrackList").html(listDate);
		        //------------------翻页数据----------------------------
		        $(".pageNext").html($.createTurnPage(xml));
		        //----------------------------------------------------		
		        $.EnterKeyPress($("#numPage"),$("#numPage").next());
		    } else {
		        $.alert("没有查询结果");
		    };
	      	$("#searchDoc").removeAttr("disabled");
	   },
	   error : function(){
	   	  	$("#searchDoc").removeAttr("disabled");
	        $.ermpAjaxError();
	    }
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
    queryList();
}

function initView(id) {
	mainFrame.addTab({
		id:"oa_doc_track_viewd_"+id,
		title:"跟踪审批单",
		url:BASE_PATH + "/m/nor_track?act=view&id="+id,
		callback:queryList
	});
}

