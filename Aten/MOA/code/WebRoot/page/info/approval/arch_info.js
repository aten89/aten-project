var mainFrame = $.getMainFrame();

$(initInfoTODO);
var passedSelect;
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
  	
	passedSelect = $("#passedDiv").ySelect({width:40});
	
	gotoPage(1);
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
};

function queryPage() {
	
	if ( $.compareDate($("#beginArchiveDate").val(), $("#endArchiveDate").val())) {
		$.alert("开始时间不能大于结束时间！");
		return false;
	}
	
	//设置查询按钮
	$("#searchInfo").attr("disabled","true");
	
	$("#infoArchList").empty();
 	
	var pageno=$.trim($("#hidNumPage").val());
	var pagecount=$.trim($("#hidPageCount").val());
	var beginArchiveDate=$.trim($("#beginArchiveDate").val());
	var endArchiveDate=$.trim($("#endArchiveDate").val());
	var subject = $.trim($("#subject").val());
	var passed = passedSelect.getValue();
	
	$.ajax({
	   type : "POST",
	   cache: false,
	   async : true,
	   url  : "m/info_arch",
	   data : {
	   		act:"query",
	   		pageno:pageno,
	   		pagecount:pagecount,
	   		beginArchiveDate:beginArchiveDate,
	   		endArchiveDate:endArchiveDate,
	   		subject:subject,
	   		passed:passed
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
                    var passed = $("passed",curELe).text();
                    var op = ( $.hasRight($.SysConstants.VIEW)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"initView('" + id + "')\">详情</a>&nbsp;|&nbsp;" : "")
                    	   + ( $.hasRight($.SysConstants.MODIFY) && passed =="通过"? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"initModify('" + id + "')\">修改</a>&nbsp;|&nbsp; " : "")
                    listDate += " <tr onmouseout=\"$(this).removeClass('over');\" onmouseover=\"$(this).addClass('over');\">"
							+ "<td>" + $("subject",curELe).text() + "</td>"
							+ "<td>" + $("info-layout",curELe).text() + "</td>"
//							+ "<td>" + $("info-class",curELe).text() + "</td>"
							+ "<td>" + $("drafts-man",curELe).text() + "</td>"
							+ "<td>" + $("arch-date",curELe).text() + "</td>"
							+ "<td>" + passed + "</td>"
							+ "<td>" + op.substring(0,op.lastIndexOf("&nbsp;|&nbsp;"))
							+ "</td></tr>";

				});
				$("#infoArchList").html(listDate);
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
		id:"oa_info_arch_view_"+id,
		title:"查看归档信息",
		url:BASE_PATH + "/m/info_arch?act=view&id="+id
	});
}

function initModify(id) {
	
	//先判断是否已经进行过修改，如果进行过修改则给出提示。
	$.ajax({
	   type : "POST",
	   cache: false,
	   async : true,
	   url  : "m/info_arch",
	   data : {
	   		act:"validate",
	 		id:id
	   },             
	   success : function (xml){
	   		var message = $("message",xml);
		    if (message.attr("code") == "0") {
		    	//已经进行过修改，提示	                              	    	  
		    	$.alert(message.text());
		    } else {
		    	$.confirm("点击修改后将生成一份信息副本,如果您未对此副本进行提交操作那么在您的起草中可以找到此副本,并可以对其进行再次修改和删除等操作!确定继续?", function(r){
					if (r) {
						//提交拷贝
					   $.ajax({
						   type : "POST",
						   cache: false,
						   async : true,
						   url  : "m/info_arch",
						   data : {
						   		act:"initmodify",
						 		id:id
						   },             
						   success : function (xml){
						   		var message = $("message",xml);
							    var msgText = message.text();
							    if (message.attr("code") == "1") {
							    	//进行修改。
							    	mainFrame.addTab({
										id:"oa_info_man_modify"+msgText,
										title:"修改信息",
										url:BASE_PATH + "/m/info_draft?act=initmodify&id=" + msgText
									});
							    } else {
							    	$.alert(msgText);
							    }
						   }
					   });
					}
				});
		    }
	   },
	   error : $.ermpAjaxError
	});	
}

function enWrap(){
	var D=document; F(D.body); function F(n){var u,r,c,x; if(n.nodeType==3){ u=n.data.search(/\S{10}/); if(u>=0) { r=n.splitText(u+10); n.parentNode.insertBefore(D.createElement("WBR"),r); } }else if(n.tagName!="STYLE" && n.tagName!="SCRIPT"){for (c=0;x=n.childNodes[c];++c){F(x);}} } 
}