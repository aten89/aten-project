var mainFrame = $.getMainFrame();
$(initPage);
function initPage(){
	//切换显示图片
	$('#flashbox').smallslider({
		showText: false,
		onImageStop: true,
		buttonOffsetX: 20,
		buttonOffsetY: 6
	});
	
	$.ajax({
	   type : "POST",
	   cache: false,
	   async : true,
	   url  : BASE_PATH + "page/portlet",
	   data : {act:"getinfos",
	   			pageno:"1",
	   			pagecount:"6",
	   			layoutname:$("#layoutName").val()},             
	   success : function (xml){
	   		var message = $("message",xml);
			var content = $("content",xml);
			var listDate = "";
			//是否有数据返回
		    if (message.attr("code") == "1") {	                              	    	  
		    	$(xml).find('information').each(function(index){
		    		var curELe = $(this);
		    		var subject = $("subject",curELe).text();
		    		var color = "";
		    		if ($("info-property",curELe).text() == "0") {
		    			color = "style='color:blue'";
		    		} else if ($("is-new",curELe).text() == "true") {
		    			color = "style='color:red'";
		    		}
		    		listDate += "<li> "
                		+ "<a href=\"javascript:openInfo('" 
                		+ curELe.attr("id") + "')\" title='" + subject + "' " + color + ">（" 
                		+ $("short-public-date",curELe).text() + "）>"
                		+ subject + "</a></li>";
				});
				$("#infokList").html(listDate);
		    } else {
		        $.alert(message.text());
		    };
	   },
	   error : function(){
	        $.ermpAjaxError();
	    }
	});
}

function openInfo(id){
	mainFrame.addTab({
		id:"portlet_view_" + id,
		title:"查看信息",
		url:BASE_PATH + "/page/portlet?act=viewinfo&id=" + id,
		callback:initPage
	});
}
