var mainFrame = $.getMainFrame();

$(initPage);
function initPage(){
	//添加权限约束
    $.handleRights({
   		"addInfo" : $.SysConstants.ADD
	});
    
    //刷新
  	$("#refresh").click(function(){
  		$("#infoDraftList").empty();
  		queryList();
  	});
  	
	var addImg = $("#addInfo").offset();
	if (addImg) {
		$("#newType").css({"top":(addImg.top - 4 ) + "px"});
		$("#newType").css({"left":(addImg.left - 5) + "px"});
		
	    $("#addInfo,#addInfo1").click(function(){
	    	if ($('#newType').is(':visible')) {
	    		$('#newType').slideUp();
	    	} else {
	    		$('#newType').slideDown();
	    	}
	    });
	}
    //初始化信息版块列表
	initInfoLayouts();
	queryList();
	
}

function initInfoLayouts() {
	$.ajax({
	    type : "POST",
	    cache: false,
	    async : true,
	    url  : "m/info_param?act=getassignlayout",
	    success : function(xml){
	        var message = $("message",xml);
	        if(message.attr("code") == "1"){
	            var listDate = "";
	            $("info-layout",xml).each(
	                function(index){
	                    listDate += "<li id='" + $(this).attr("id") + "'>" + $("name",$(this)).text() + "</li>";
	                }
	            );
	            $("#infoLayouts").html(listDate);
	            if($("#infoLayouts li").size() == 0) {
	            	$("#addInfo").remove();
	            } else {
				    $("#infoLayouts li").hover(function(){
						$(this).addClass("current");
					},function(){
						$(this).removeClass("current");
					}).click(function(){
						$("#newType").hide();
						mainFrame.addTab({
							id:"oa_info_man_add"+Math.floor(Math.random() * 1000000),
							title:"新增信息",
							url:BASE_PATH + "/m/info_draft?act=initadd&layout=" + encodeURI($(this).text()),
							callback:queryList
						});
					});
				}
	        }
	    },
	    error : $.ermpAjaxError
	});
}

function queryList() {
	$.ajax({
	    type : "POST",
	    cache: false,
	    async : true,
	    url  : "m/info_draft?act=query",
	    success : function(xml){
	        var listDate = "";
	        var message = $("message",xml);
	        if(message.attr("code") == "1"){
	          $(xml).find('info-form').each(function(index){
		    		var curELe = $(this);
                    var id = curELe.attr("id");
                    var op = ( $.hasRight($.SysConstants.MODIFY)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"modifyDraft('" + id + "')\">修改</a>&nbsp;|&nbsp;" : "")
                    	   + ( $.hasRight($.SysConstants.DELETE)? "<a class=\"opLink\" href=\"javascript:void(0);\" onclick=\"delDraft('" + id + "')\">删除</a>&nbsp;|&nbsp;" : "")
                   	listDate += " <tr onmouseout=\"$(this).removeClass('over');\" onmouseover=\"$(this).addClass('over');\">"
							+ "<td>" + $("subject",curELe).text() + "</td>"
							+ "<td>" + $("draft-date",curELe).text() + "</td>"
							+ "<td>" + $("info-layout",curELe).text() + "</td>"
					//		+ "<td>" + $("info-class",curELe).text() + "</td>"
							+ "<td>" + op.substring(0,op.lastIndexOf("&nbsp;|&nbsp;"))
							+ "</td></tr>";
				});
				
				$("#infoDraftList").html(listDate);
				enWrap();
	        }
	    },
	    error : $.ermpAjaxError
	});
}
function modifyDraft(id){
	mainFrame.addTab({
		id:"oa_info_man_modify"+id,
		title:"修改信息",
		url:BASE_PATH + "/m/info_draft?act=initmodify&id=" + id,
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
			    url  : "m/info_draft",
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

function enWrap(){
	var D=document; F(D.body); function F(n){var u,r,c,x; if(n.nodeType==3){ u=n.data.search(/\S{10}/); if(u>=0) { r=n.splitText(u+10); n.parentNode.insertBefore(D.createElement("WBR"),r); } }else if(n.tagName!="STYLE" && n.tagName!="SCRIPT"){for (c=0;x=n.childNodes[c];++c){F(x);}} } 
}