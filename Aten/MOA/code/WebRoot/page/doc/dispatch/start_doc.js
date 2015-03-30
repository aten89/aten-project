var mainFrame = $.getMainFrame();

$(initPage);
function initPage(){
	//添加权限约束
    $.handleRights({
   		"addOfficeDoc" : $.SysConstants.ADD
	});
	
	//刷新
 	$("#refresh").click(function(){
  		$("#dispatchDraftList").empty();
  		queryList();
  	});
	
	var addImg = $("#addOfficeDoc").offset();
	if (addImg) {
		$("#newType").css({"top":(addImg.top - 4 ) + "px"});
		$("#newType").css({"left":(addImg.left - 5) + "px"});
		
		
	    $("#addOfficeDoc,#addOfficeDoc1").click(function(){
	    	if ($('#newType').is(':visible')) {
	    		$('#newType').slideUp();
	    	} else {
	    		$('#newType').slideDown();
	    	}
	    });
	};
	
	initDocLayouts();
	queryList();
}
function initDocLayouts(){
	
	$.ajax({
	    type : "POST",
	    cache: false,
	    async : true,
	    url  : "m/doc_class?act=getassigndoc",
	    success : function(xml){
	        var message = $("message",xml);
	        if(message.attr("code") == "1"){
	            var listDate = "";
	            $("doc-class",xml).each(
	                function(index){
	                    listDate += "<li id='" + $(this).attr("id") + "'>" + $("name",$(this)).text() + "</li>";
	                }
	            );
	            $("#officeDocLayouts").html(listDate);
	            if($("#officeDocLayouts li").size() == 0) {
	            	$("#addOfficeDoc").remove();
	            } else {
				    $("#officeDocLayouts li").hover(function(){
						$(this).addClass("current");
					},function(){
						$(this).removeClass("current");
					}).click(function(){
						$("#newType").hide();
						mainFrame.addTab({
							id:"oa_doc_man_add"+Math.floor(Math.random() * 1000000),
							title:"新增公文",
							url:BASE_PATH + "/m/dis_start?act=initadd&doc=" + encodeURI($(this).text()),
							callback:queryList
						});
					});					
				}
	        }
	    },
	    error : $.ermpAjaxError
	});

	
}

function queryList(){
	$.ajax({
	    type : "POST",
	    cache: false,
	    async : true,
	    url  : "m/dis_start?act=query",
	    success : function(xml){
	        var listDate = "";
	        var message = $("message",xml);
	        if(message.attr("code") == "1"){
	          $(xml).find('doc-form').each(function(index){
		    		var curELe = $(this);
                    var id = curELe.attr("id");
                    var op = ( $.hasRight($.SysConstants.MODIFY)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"modifyDraft('" + id + "')\">修改</a>&nbsp;|&nbsp;" : "")
                    	   + ( $.hasRight($.SysConstants.DELETE)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"delDraft('" + id + "')\">删除</a>&nbsp;|&nbsp;" : "")
                   	listDate += " <tr onmouseout=\"$(this).removeClass('over');\" onmouseover=\"$(this).addClass('over');\">"
							+ "<td>" + $("subject",curELe).text() + "</td>"
							+ "<td>" + $("draft-date",curELe).text() + "</td>"
							+ "<td>" + $("groupName",curELe).text() + "</td>"
							+ "<td>" + $("doc-class",curELe).text() + "</td>"
							+ "<td>" + op.substring(0,op.lastIndexOf("&nbsp;|&nbsp;"))
							+ "</td></tr>";
				});
				
				$("#officeDocDraftList").html(listDate);
				//enWrap();
	        }
	    },
	    error : $.ermpAjaxError
	});

}

function modifyDraft(id){
	mainFrame.addTab({
		id:"oa_doc_man_modify"+id,
		title:"修改公文",
		url:BASE_PATH + "/m/dis_start?act=initmodify&id=" + id,
		callback:queryList
	});
}

function delDraft(id){
	$.confirm("确认要删除吗?", function(r){
		if (r) {
			$.ajax({
			    type : "POST",
			    cache: false,
			    async : true,
			    url  : "m/dis_start",
			    data :{act:"delete",id:id},
			    success : function(xml){
			        var message = $("message",xml);
			        if(message.attr("code") == "1"){
			        	$.alert($("message",xml).text());
			        	queryList();
			        }else{
			        	$.alert($("message",xml).text());
			        }
			    },
			    error : $.ermpAjaxError
			});
		}
	});
}

