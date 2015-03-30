var mainFrame;
var currentSystemId;//当前打开的子系统
var currentSystemName;//当前打开的子系统名称
var currentModuleName1th;//当前一级模块菜单名称
var currentModuleID1th;//当前一级模块ID
var moduleMenus2th = {};//当前子系统的二级菜单对像模型
var currentMenu1thListXML;

//框架在顶级窗口显示
if(top.location!=self.location){
	top.location = self.location;
}

//窗口大小改变时重载一级与二极菜单
window.onresize = arrangeMenu1th;

$(function(){
	//是否强制修改密码
	var forceChangePassword =$("#forceChangePassword").val();
	if (forceChangePassword == "true") {
		changePassword();
		return;
	}
	mainFrame =  $.getEAPPMainFrame();//强制使用EAPPMainFrame，框架必须用TAB形式打开模块
	//收藏夹
	initFav();
    //切换子系统菜单
	initSystemMenu();
    //加载固定系统的模块菜单
	loadFixedModuleMenu();
	//初始化页面框架
	loadPageFrame();
	//加载默认菜单
	loadDefaultModuleMenu();
	//加载快捷菜单
	loadShortCutMenu();
	//加载化收藏夹菜单
	loadFavShortCutMenu();
	//初始化弹出消息
	initSysMsgBox();
	
	//点击其它地方时隐藏所有弹出层
	$(document).bind(
   		"mousedown",
	   	hideAll
	);
	$(document).bind(
   		"mouseup",
	   	hideAll
	);
});

//初始化收藏夹
function initFav(){
	//收藏夹图标的单击事件
	$("#_favShortCutLink").bind(
  		"click",
  		showFavShortCutMenu
	);
}

//切换子系统菜单
function initSystemMenu(){
	var subSystemSize = $("li", "#_logoSysMenuUL").size();//子系统个数
	if (subSystemSize > 1) {//1个以上子系统时
		//显示提示信息
		$("#_logoImageDIV").attr("title", "点击切换系统菜单");
    	
		$("#_showSystemMenu").show();//显示右边的切换系统
		//切换系统的单击事件
		$("#_showSystemMenu").bind(
			"click",
			showToggleSysMenu
		);
		
    	//LOGO图标的单击事件
		$("#_logoImageDIV").bind(
	  		"click",
	  		showLogoSysMenu
		);
	}
}

//隐藏所有弹出层
function hideAll() {
	hideLogoSysMenu();
	hideToggleSysMenu();
	hideFavShortCutMenu();
	hideMoreMenu();
	hideMoreMenu2th();
}

//显示"切换系统"系统主菜单
function showToggleSysMenu(e) {
	var jqSubMenu = $("#_toggleSysMenuDIV");
	if(jqSubMenu.is(':visible')) {
		hideToggleSysMenu(e);
	} else {
		jqSubMenu.fadeIn(100);
	}
}
//隐藏"切换系统"系统主菜单
function hideToggleSysMenu(e){
    $("#_toggleSysMenuDIV").fadeOut(200);
}

//显示"LOGO"系统主菜单
function showLogoSysMenu(e) {
	var jqSubMenu = $("#_logoSysMenuDIV");
	if(jqSubMenu.is(':visible')) {
		hideLogoSysMenu(e);
	} else {
		jqSubMenu.fadeIn(100);
	}
}
//隐藏"LOGO"系统主菜单
function hideLogoSysMenu(e){
    $("#_logoSysMenuDIV").fadeOut(200);
}

//显示收藏夹菜单
function showFavShortCutMenu(e) {
	var jqSubMenu = $("#_favShortCutMenuDIV");
	if(jqSubMenu.is(':visible')) {
		hideFavShortCutMenu(e);
	} else {
		jqSubMenu.fadeIn(100);
	}
}
//隐藏收藏夹菜单
function hideFavShortCutMenu(e){
    $("#_favShortCutMenuDIV").fadeOut(200);
}

//隐藏"更多"菜单
function hideMoreMenu(e){
	if (document.getElementById("moreMenu")!=null){
    	$("#showMoreOption").hide();
		$("#original").show();
	}
}

//显示二级"更多"菜单
function showMoreMenu2th(e) {
	var jqSubMenu = $("#_toggleMoreMenu2thDIV");
	if(jqSubMenu.is(':visible')) {
		hideMoreMenu2th(e);
	} else {
		jqSubMenu.fadeIn(100);
	}
}
//隐藏二级"更多"菜单
function hideMoreMenu2th(e){
    $("#_toggleMoreMenu2thDIV").fadeOut(200);
}

//加载固定系统的模块菜单
function loadFixedModuleMenu() {
	$.ajax({
		type : "POST",
      	url : "l/frame/xmlfixedmodules",
      	dataType : "json",
        success : function(data){
        	if ($.checkErrorMsg(data) ) {
        		var xml = parseXMLDocument(data.xmlValue);
        		//生成固定系统菜单，并绑定事件
        		parseModuleMenuDom(xml, $("#_fixedModuleMenuUL"));
            	
            	//为了兼容IE6，记住宽度到时还原
            	$("li","#_fixedModuleMenuUL").each(function(){
            		$(this).attr("defaultwidth", $(this).width());
            	});
        	}
        }
    });
}

//加载默认系统的菜单
function loadDefaultModuleMenu() {
	var systemID = $.cookie("LAST_SUBSYSTEM_ID");
	var subSystemSize = $("li", "#_logoSysMenuUL").size();//子系统个数

	if (!systemID && subSystemSize > 1) {
		showLogoSysMenu();//没有cookie,即第一次访问系统时自动打开子系统切换菜单
	}
	if (!systemID || //cookie中记的子系统ID不存在
			$("#" + systemID, "#_logoSysMenuUL").size() == 0) {//或cookie中记的子系统ID不是有效的
		//默认第一个有权限的启用的系统ID
		systemID = $("li:first", "#_logoSysMenuUL").attr("id");
	}
	loadModuleMenu(systemID);
}

//加载选择系统的模块菜单
function loadModuleMenu(systemID) {
	if (currentSystemId == systemID) {
		return;
	}
	currentSystemId = systemID;//记住当前子系统ID
	currentSystemName = $.trim($("#" + currentSystemId, "#_logoSysMenuUL").text());//记住当前子系统名称
	//写入系统ID到Cookie
	$.cookie("LAST_SUBSYSTEM_ID",currentSystemId,{expires:30,path:"/"});
	
	//切换显示LOGO图标
	var subSystemLogo = $("#" + currentSystemId, "#_logoSysMenuUL").attr("logourl");
	$("img","#_logoImageDIV").attr("src", subSystemLogo);
	//切换显示系统名称
	$("#sub_system_name", "#_systemHeadInfoDIV").html(currentSystemName);
	$("#sub_system_name_shadow", "#_systemHeadInfoDIV").html(currentSystemName);
	
	$("li", "#_fixedModuleMenuUL").removeClass("current");//移除固定菜单选中样式
	//加载子系统模块菜单
	$.ajax({
		type : "POST",
      	url : "l/frame/xmlmodules?systemID=" + currentSystemId,
      	dataType : "json",
        success : function(data){
        	if ($.checkErrorMsg(data) ) {
        		var xml = parseXMLDocument(data.xmlValue);
        		            	//还原固定模块菜单的自适应调整
            	$("li", "#_fixedModuleMenuUL").css("width","auto");
            	//为了兼容IE6，还原之前记住的宽度
            	$("li","#_fixedModuleMenuUL").each(function(){
            		$(this).width($(this).attr("defaultwidth"));
            	});
            	
            	//生成子系统菜单，并绑定事件
            	currentMenu1thListXML = xml;
            	parseModuleMenuDom(currentMenu1thListXML, $("#_subSysModuleMenuUL"));

            	//固定菜单，靠右对齐时被挤没掉，要改变UL的width
            	//$("#_fixedModuleMenuUL").css({'float':'right'});	
            	var w = $("li","#_fixedModuleMenuUL").css("width");
            	$("#_fixedModuleMenuUL").width(w);
            	$("li","#_fixedModuleMenuUL").width("100%");
            	
            	//清空二级菜单，防止切换子系统时留下痕迹
            	$("#_moduleMenu02thUL").html("");
        	}
        }
    });
    
	//切换系统刷新首页
	mainFrame.getTab("HomePage").reload();
}

//添加二级菜单对像模型
function parseModuleMenuDom(xml, obj) {
	//计算出当前窗口，可以容纳多少个菜单项
	//超出的，放到更多菜单去
	var validWidth = document.body.clientWidth - 450;

	var moduleMenu01th = "";
	var lisWidth = 0;//累加LI宽度
	var firstMoreMenu = true;
	$("content > module",xml).each(function(i){
		//计数器，当模块数量超过时，生成更多菜单项
		//生成一级菜单	
		var moduleID = $(this).attr("id");
		var moduleName = $(this).children("module-name").text();
		lisWidth += moduleName.length * 13 + 21;//一个汉字约12，空隙约21
		if (lisWidth <= validWidth) {
			//正常菜单
       		moduleMenu01th += "<li id='" + moduleID + "' onclick=\"openModuleMenu('" + moduleID + "','" + moduleName + "', false);\"><span><label onmouseout='this.className=\"\"' onmouseover='this.className=\"over\"' title='" + moduleName + "'>" 
       			+ moduleName + "</label></span></li>";
       	}else{
       		//更多菜单
       		if (firstMoreMenu){
       			//第一个添加下拉头部
       			firstMoreMenu = false;
       			moduleMenu01th += "<li onmousedown='event.cancelBubble=true;' id='moreMenu' class='moreMenu'>"
       				+ "<div id='original'>"
                	+ "  <div class='dynamicOption' id='dynamicOptionName'>" 
                	+ "    <label onclick=\"openModuleMenu('" + moduleID + "','" + moduleName + "', true);\" onmouseout='this.className=\"\"' onmouseover='this.className=\"over\"' title='" + moduleName + "'>" + moduleName + "</label>"
                	+ "  </div>"
           			+ "  <div class='moreOptionBtn' id='moreOption'></div>"
            		+ "</div>"
            		+ "<div id='showMoreOption' class='moreOptionBg' style='display:none'>"
            		+ "  <div class='dynamicOption doShow' id='dynamicOptionShowName'>" + moduleName + "</div>"
       		    	+ "  <div class='moreOptionBtn mobShow' id='moreOptionBtnShow'></div>"
               		+ "  <div class='moreOptionBgHead'>"
                    + "    <div class='moreOptionMain' id='moreOptionMain'>"
            }
            moduleMenu01th += "<a href='javascript:void(0)' onclick='selectMoreMenu(\"" + moduleID + "\",\"" + moduleName +"\")'>" + moduleName + "</a>";
            
        }
       	//生成二级菜单对像模型
       	var moduleMenusArr = [];
       	$(this).children("module").each(function(){
       		var mObj = {};
       		mObj.id = $(this).attr("id");
       		mObj.url = $(this).children("module-url").text();
       		mObj.name = $(this).children("module-name").text();
       		moduleMenusArr[moduleMenusArr.length] = mObj;
		});
		moduleMenus2th[moduleID] = moduleMenusArr;
	});
	if (!firstMoreMenu){//为false时说明有更多菜单
		 moduleMenu01th += "</div></div></div></li>";
	}
	obj.html(moduleMenu01th);//填充一级菜单HTML
	//初始化更多菜单项
	if (!firstMoreMenu){//为false时说明有更多菜单
		initMoreMenu();
	}
}

//初始化更多菜单项
function initMoreMenu(){
 	$("#moreOption").click(function(){
		$("#showMoreOption").show();
		$("#original").hide();
	});
 	$("#moreOptionBtnShow").click(function(){
		$("#showMoreOption").hide();
		$("#original").show();
	});
}

//一级菜单 点击后，加载二级菜单
function openModuleMenu(moduleID, moduleName, isMoreMenu) {
	//是更多菜单打开的，ID为moreMenu的LI选中
	var parentLi = isMoreMenu ? $("#moreMenu") : $("#"+moduleID);
	$("li","#_moduleMenu01thDIV").removeClass("current");//移除所有选中样式
	parentLi.addClass("current");//设为当前选中样式
//	if (currentModuleID1th == moduleID) {//已经是打开状态
//		return;
//	}
	currentModuleName1th = moduleName;//记住当前一级模块菜单名称
	currentModuleID1th = moduleID;
	
	loadModuleMenu2th(moduleID, true);
}

//加载二级菜单内容
function loadModuleMenu2th(moduleID, openFirst) {
	//计算出当前窗口，可以容纳多少个菜单项
	//超出的，放到更多菜单去
	var validWidth = document.body.clientWidth - 280;
	
	//生成二级菜单
	var moduleMenusArr = moduleMenus2th[moduleID];//取得对应的数组对像
	
	var moduleMenu02th = "";
	var moreMenu02th = "";
	var lisWidth = 0;//累加LI宽度
	var firstMoreMenu = true;
	$(moduleMenusArr).each(function(i) {
		lisWidth += this.name.length * 12 + 21;//一个汉字约12，空隙约21
	//	alert(lisWidth + "=="+validWidth);
		if (lisWidth <= validWidth) {
			//正常菜单
			moduleMenu02th += "<li onmouseout='this.className=\"\"' onmouseover='this.className=\"over\"' onclick=\"openModuleUrl('" + this.url + "','" + this.name + "');\"><span><label onmouseout='this.className=\"\"' onmouseover='this.className=\"over\"' title='" + this.name + "'>" 
	       			+ this.name + "</label></span></li>";
		} else {
			//更多菜单
       		if (firstMoreMenu){
       			//第一个添加下拉头部
       			firstMoreMenu = false;
       			moduleMenu02th += "<li id='moreMenu2'><span class='moreOptionBtn2' id='moreOption2th'>更多菜单</span></li>";
       		}
       		moreMenu02th += "<li onmouseout='this.className=\"\"' onmouseover='this.className=\"over\"' onclick=\"openModuleUrl('" + this.url + "','" + this.name + "');\"><span><label>" 
	       			+ this.name + "</label></span></li>";
		}
	});
	
	var moduleMenu02thUL = $("#_moduleMenu02thUL");
	//如果是固定菜单，则二级菜单靠右对齐
	var moduleMenuULID = $("#" + moduleID).parent().attr("id");
	if (moduleMenuULID == "_fixedModuleMenuUL"){
		moduleMenu02thUL.css("float", "right");
	}else{
		moduleMenu02thUL.css("float", "left");
	}
	moduleMenu02thUL.html(moduleMenu02th);//填充二级菜单HTML
    
	if (!firstMoreMenu){//为false时说明有更多二级菜单
		
		$("ul", "#_toggleMoreMenu2thDIV").html(moreMenu02th);//填充二级菜单HTML
		
		//更多二级菜单的单击事件
		$("#moreOption2th").click(function(){
			//定位弹出位子
	  		$("#_toggleMoreMenu2thDIV").css("left", $(this).offset().left + 22);
	  		//显示弹出层
	  		showMoreMenu2th();
	  		
		});
	}

	if (openFirst) {
		$("li:first", moduleMenu02thUL).click();//默认打开第一个二级菜单
	}
}

//更多一级菜单点击事件
function selectMoreMenu(moduleID, moduleName){
	$("#dynamicOptionName").html("<label onclick=\"openModuleMenu('" + moduleID + "','" + moduleName + "', true);\" onmouseout='this.className=\"\"' onmouseover='this.className=\"over\"' title='" + moduleName + "'>" + moduleName + "</label>");
    $("#dynamicOptionShowName").text(moduleName);
	$("#showMoreOption").hide();
	$("#original").show();
	$("li","#_moduleMenu01thDIV").removeClass("current");//移除所有选中样式
	openModuleMenu(moduleID, moduleName, true);
}

//窗口RESIZE事件，用于适时排列菜单
function arrangeMenu1th(){
	//隐藏所有弹出层
	hideAll();
	
	//重新加载一级菜单
	parseModuleMenuDom(currentMenu1thListXML,$("#_subSysModuleMenuUL"));
	if (currentModuleID1th) {
		$("#"+currentModuleID1th).addClass("current");//设为当前选中样式
		//重新加载当前显示的二级菜单
		loadModuleMenu2th(currentModuleID1th, false);
	}
}

//打开模块地址
function openModuleUrl(url, title, alt) {
	if (url=='l/frame/initindex') {
		//如果打开的是菜单里的首页，（临时增加）
		loadPageFrame();
		return;
	}
	if (alt == null) {
		alt = currentSystemName + "->" + currentModuleName1th + "->" + title;
	}
	mainFrame.addTab({
		id:"menu_page_" + alt,
		title:title,
		url:url,
		alt:alt
	});
}

//登出所有系统
function logoutSystem() {
	try {
		//退出CALLCENTER控件
		top.CRM_CALLCENTER_OBJ.exitCallCenter();
	} catch(e) {
	}
	
	//通过各子系统配置CAS登出监听（查看web.xml里配置），只要调用CAS登出地址，CAS服务器就是自动触发各子系统登出
	window.location = BASE_PATH + "p/comm/logout";
}

//加载化快捷方式
function loadShortCutMenu() {
	$.ajax({
		type : "POST",
      	url : "l/frame/myshortcuts",
      	dataType : "json",
        success : function(data){
        	if ($.checkErrorMsg(data) ) {
        		var context = "";
				if(data.shortCutMenus){
					var dataList = data.shortCutMenus;
					$(dataList).each(function(i) {
	               		var menuTitle = dataList[i].menuTitle;
	               		context += "<img src='" + dataList[i].logoURL + "' title='" + menuTitle 
	               				+ "' onclick=\"openShortCutMenu('" + dataList[i].url + "','" 
	               				+ menuTitle + "','" + dataList[i].windowTarget + "','" + menuTitle + "')\"/>";
					});
				}
				$("#_shortCutMenuDIV").html(context);
        	}
        }
    });
}

//打开快捷方菜单
function openShortCutMenu(url, name, target, moduleTitle) {
	//target不等于_blank的话就在本页打开
	if (target && "_blank" == target.toLowerCase()) {
		window.open(url);
	} else {
		openModuleUrl(url, name, moduleTitle);
	}
}

//将当前页添加到收藏夹
function addToFavList(){
	mainFrame.currentTab.addToFav();
	hideFavShortCutMenu();
}

//加载收藏夹菜单
function loadFavShortCutMenu() {
	$.ajax({
		type : "POST",
      	url : "l/frame/favorites",
      	dataType : "json",
        success : function(data){
        	if ($.checkErrorMsg(data) ) {
        		var context = "";
				if(data.shortCutMenus){
					var dataList = data.shortCutMenus;
					$(dataList).each(function(i) {
						var id = dataList[i].shortCutMenuID;
	               		var menuTitle = dataList[i].menuTitle;
	               				
	               		context += "<li id='" + id + "' title='" + menuTitle + "' onclick=\"hideFavShortCutMenu();openShortCutMenu('" 
	               				+ dataList[i].url + "','" + menuTitle + "','" + dataList[i].windowTarget + "','" + dataList[i].moduleTitle 
	               				+ "');\"><button onclick=\"delFavShortCutMenu('" + id 
	               				+ "');\" class='delFavlink' onmouseover=\"this.className='delFavlinkOver'\" "
	               				+ "onmouseout=\"this.className='delFavlink'\" title='删除' style='display:none'></button><img src='" 
	               				+ dataList[i].logoURL + "' />" + menuTitle + "</li>";
					});
				}
				
				$("#_favShortCutMenuUL").html(context);
				//绑定收藏夹列表事件
        		$("li", "#_favShortCutMenuUL").each(function(){
        			$(this).mouseover(function(){
						$(this).addClass("over").find("button").show();
					}).mouseout(function(){
						$(this).removeClass("over").find("button").hide();
					});
        		});
        	}
        }
    });
}

//删除收藏夹条目
function delFavShortCutMenu(id) {
	$.ajax({
   		type : "POST",
   		cache: false,
    	async : true,
   		url  : "l/frame/delshortcut",
   		dataType : "json",
   		data : {
   			shortCutMenuID : id
   		},
   		success : function(data) {
   			if ($.checkErrorMsg(data) ) {
   				$("#" + id, "#_favShortCutMenuUL").remove();
   			}
  		}
	});
	//取消事件冒泡
	event.cancelBubble=true;
}

//增加收藏夹条目
function addFavShortCutMenu(url, menutitle, moduletitle) {
	$.ajax({
   		type : "POST",
   		cache: false,
    	async : true,
   		url  : "l/frame/addshortcut",
   		dataType : "json",
   		data : {
   			menuTitle : menutitle,
   			moduleTitle : moduletitle,
   			menuLink : url,
            logoURL : "themes/comm/images/favLink.png",
   			windowTarget : "_self",
   			type : "FAVORITE"
   		},
   		success : function(data){
   			if ($.checkErrorMsg(data) ) {
   				$.alert("收藏成功");
   				loadFavShortCutMenu();//刷新收藏夹菜单
   			}
  		}
	});
}

//打开首页
function loadPageFrame(){
	mainFrame.addTab({
		id:"HomePage",
		title:"首页",
		alt:"门户首页",
		url:"l/frame/initindex",
		canClosed:false,
		icon:"homeBg",
		onFocus:function(){mainFrame.getTab("HomePage").reload();}
	});
}

//将StringXML转为Document对象
function parseXMLDocument(string) {
	var browserName = navigator.appName;
	var doc;
	if (browserName == 'Microsoft Internet Explorer'){
		doc = new ActiveXObject('Microsoft.XMLDOM');
		doc.async = 'false'
		doc.loadXML(string);
	} else {
		doc = (new DOMParser()).parseFromString(string, 'text/xml');
	}
	return doc;
}

/*******弹出提示信息开始*********/
//初始化信息框
function initSysMsgBox() {
	setInterval(showSysMsg, 30000);
}

//弹出提示信息框
function showSysMsg() {
	$.ajax({
   		type : "POST",
   		cache: false,
    	async : true,
   		url  : "l/frame/newmsg",
   		dataType : "json",
   		success : function(data){
   			if ($.checkErrorMsg(data) ) {
   				if (data.sysMsg) {
   					/*
					 * 标题闪烁提示
					 */
					var timerArr = showSysMsgTitleTip();
					
   					/*
   					 * 显示弹出窗口
   					 */
   					var win = $(window);
					var dialogM = $("#_time20140415132613").wBox({
						title: "<span style='font-weight: normal'>来自" + data.sysMsg.msgSender + "的消息<span>",
						html: "<div style='width:160px;height:80px;overflow-y:auto;line-height:20px;padding:0 0 0 3px;'>" + data.sysMsg.msgContent + "</div>",
						overlayBG: false,
						drag: false,
						closeCallBack: function(){
							clearSysMsgTitleTip(timerArr);//去除闪烁提示
						},
						position: {
				        	left: win.width() - 185,
				           	top: win.height() - 130
				       	}
					});
					dialogM.showBox();
   				}
   			}
  		}
	});
}

//有新消息时在title处闪烁提示
function showSysMsgTitleTip() { 
	var step=0, _title = document.title;

	var timer = setInterval(function() {
		step++;
		if (step==3) {
			step=1
		}
		if (step==1) {
			document.title="【　　】"+_title
		}
      	if (step==2) {
      		document.title="【新消息】"+_title
      	}
	}, 1000);

	return [timer, _title];
}
    
    /**
     * 去除闪烁提示，恢复初始title文本
     * @param timerArr[0], timer标记
     * @param timerArr[1], 初始的title文本内容
     */
function clearSysMsgTitleTip(timerArr) { 
	if(timerArr) {
		clearInterval(timerArr[0]); 
		document.title = timerArr[1];
	}
}

/*******弹出提示信息结束*********/
    
/*******弹出强制修改密码开始*********/
//弹出修改密码
function changePassword() {
	//密码修改成功后回调此方法重新登录
	window.changePasswordDialogCloseFun = logoutSystem;
	//弹出窗口对象
	var changePasswordDialog;
	changePasswordDialog = $("#_time20141229155013").wBox({
		noTitle: true,
		drag: false,
		opacity: 1,//背景透明度
		requestType : "iframe",
		target : "l/frame/initpsw",
		iframeWH : {width : 480, height : 260}
	});
	changePasswordDialog.showBox();
}
/*******弹出强制修改密码结束*********/

//隐藏标题栏
function hideMenuBar() {
	$("#_topMenuBarDIV").hide();
	$("#_bottomConcentDIV").css("top", 0);
	
	$(".hideMenuBarBtn").hide();
	$(".showMenuBarBtn").show();
}
//显示标题栏
function showMenuBar() {
	$("#_topMenuBarDIV").show();
	$("#_bottomConcentDIV").css("top", 107);
	
	$(".hideMenuBarBtn").show();
	$(".showMenuBarBtn").hide();
}

//隐藏TAB栏
function hideTabBar() {
	$(".conRight").hide();
	$(".conLeft").width("100%");
	
	$(".hideTabBarBtn").hide();
	$(".showTabBarBtn").show();
}
//显示TAB栏
function showTabBar() {
	$(".conRight").show();
	$(".conLeft").width("auto");
	
	$(".hideTabBarBtn").show();
	$(".showTabBarBtn").hide();
}