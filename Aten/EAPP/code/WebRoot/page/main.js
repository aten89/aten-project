/*
//执行初始化
$(loadPortal);
		
//解释用户门户XML信息，添加到相应的列中
//add by zsy
function loadPortal() {
	//点击刷新页面
	rootPanel.getMainPanel().getTabPanel().getSelectedItem().fnAfterClick = "refreshHomepage";
	
	var containers = [];
	//取得页面存放门户的容器
	$(".divSpaceZone").each(function(index) {
		containers[$(this).attr("zoneId")] = $(this);
		$(this).html("");
	});
	
	$.ajax({
		type : "POST",
      	url : "l/frame/xmlportals",
      	dataType : "xml",
        success : function(xml){	
        	var message = $("message",xml);
        	if(message.attr("code") == "1") {
            	$("content > portal",xml).each(
            		
                	function(){
                		var pid = $(this).attr("id");
                		var url = $(this).children("portal-url").text();
                		var moreUrl = $(this).children("portal-more-url").text();
                		var name = $(this).children("portal-name").text();
                		var containerID = $(this).children("page-container-id").text();
               // 		var positionIncex = $(this).children("position-index").text();
                		var style = $(this).children("style").text();
                		if (!url || !containerID) {
                			return;
                		}
                		if (!style || style == "") {
                			style = "divTitle01";
                		} 
                		var portalDiv = "<div class='divPanel'><h1 class='" + style + "'><div><span>" + name + "</span>";
                		if (moreUrl != "") {
                			portalDiv += "<button class='more' onclick=\"openMorePortlet('"+ pid + "','" + moreUrl + "','"+ name + "')\"></button>"; 
                		}
                		portalDiv += "</div></h1><div class='mb' id='" + pid + "'><iframe src='" + url + "' marginheight='0' marginwidth='0'  frameborder='0' width='100%' height='100%' scrolling='no'></iframe></div></div>";
						
						if (containers[containerID]) {
							containers[containerID].html(containers[containerID].html() + portalDiv);
						}
                	}
            	);
        	} else {
                $.alert(message.text());
        	}
        }
    });
}


function refreshHomepage(){
	var iframes = document.getElementsByTagName("iframe");
	for (var i=0; i<iframes.length ; i++){
		iframes[i].contentWindow.location.reload();
	}
}

//打开更多列表
function openMorePortlet(id, url, name) {
	if (!url || url == "") {
		return;
	}
	var tabMain = rootPanel.getMainPanel();
	tabMain.addItem("ermp_morePortlet_"+id, name, url);
}*/
//本页面没用到，但如EAPP与子系统都是DIALOG模式时，初始后门户块里的打开窗口就会使用parent的（即本对象），这样才能全屏打开
//但是，如果门户块与EAPP是不同域名那就无效，只能在门户块里弹出小窗口
var mainFrame = $.getMainFrame();
//执行初始化
$(loadPortal);
		
//解释用户门户XML信息，添加到相应的列中
//add by zsy
function loadPortal() {
	/*
	if($("#_contentIFrame", parent.document).attr("src").indexOf("index?act=initindex") < 0) {
		//src值不等于首页时，收藏是通过src属性读取地址的
		parent.openModuleUrl("xmlportals/initindex", "首页", "门户首页");
	}
	*/
	var url = "l/frame/portals";
	if ($("#showFlag").val() == "1") {
		url += "?systemID=" +parent.currentSystemId;
	}
	$.ajax({
		type : "POST",
      	url : url,
      	dataType : "json",
        success : function(data)  {
        	if ($.checkErrorMsg(data) ) {
        		if(data.portlets){
					var dataList = data.portlets;
					$(dataList).each(function(i) {
						var pid =  dataList[i].portletID;
	            		var name = dataList[i].portletName;
	            		var url = dataList[i].urlPath;
	            		var moreUrl = dataList[i].moreUrlPath;
	            		var style = dataList[i].style;
	            		if (!url) {
	            			return;
	            		}
	            		if (!style) {
	            			style = "width:33.3%;height:220px";
	            		} 
	            		//heigth要减去40（标题栏与top padding）
	            		style = style.replace(/(width:[0-9.\%px]+;height:)([0-9.]+)([\%px]+)/g,function(a,b,c,d){
							return b+ (c - 40) +d;
						}); 
						var portalDiv = "<div class='divLayout' style='" + style +"'><div class='divPanel'><h1 class='divTitle01 flatTit0" + (i%6) + "'><div><span>" + name + "</span>";
	            		if (moreUrl) {
	            			portalDiv += "<button class='more' onclick=\"openMorePortlet('"+ pid + "','" + moreUrl + "','"+ name + "')\"></button>"; 
	            		}
	            		portalDiv += "</div></h1><div class='mb' id='" + pid + "'><iframe src='" + url + "' marginheight='0' marginwidth='0'  frameborder='0' width='100%' height='100%' scrolling='no' allowTransparency='true'></iframe></div></div></div>";
						
	            		$("#portletPanelDiv").append(portalDiv);
						
					});
        		}
        	}
        }
    });
}

//在父框架里打开更多列表
function openMorePortlet(id, url, name) {
	parent.openModuleUrl(url, name);
}