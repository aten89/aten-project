/**
*	扩展jQuery的方法，用于所有子页面获取“主框架”
*		从本窗口起，直到top窗口，看能不能取到mainFrame对象
*/
var USE_DIALOG_FRAME = false;//是否强制使用弹出框不用TAB
$.getMainFrame = function(){
	if (USE_DIALOG_FRAME) {
		return $.getEAPPDialogFrame();
	}
	try {
		/**
		 * 与EAPP系统是同一域时，使用默认的多TAB方案
		 * 把EAPPMainFrame对象绑定取top实现全局共享
		 */
		return $.getEAPPMainFrame();
	} catch(e) {
		/**
		 * 与EAPP系统不是同一域时，访问top时会抛无限异常，这时使用弹出框方案
		 * 这时只能把EAPPDialogFrame对象绑定到自己的Window，并传递到子Window（弹出框使用jquery.wbox组件，也是使用iframe来实现）
		 */
		return $.getEAPPDialogFrame();
	}
};

$.getEAPPMainFrame = function() {
	//从top对象中取框架对象
	var frame = top.MAIN_FRAME_20140504301941;
	if (frame==null){
		//如果为空创建
		frame = new EAPPMainFrame();
		//把自己挂在window对象上，以便所有子页面都能引用到
		top.MAIN_FRAME_20140504301941 = frame;
	}
	return frame;
}

$.getEAPPDialogFrame = function() {
	var frame = null;
	try {
		frame = parent.MAIN_FRAME_20140413185023;
	} catch(e) {
		//没权限访问parent，取自己的
		frame = window.MAIN_FRAME_20140413185023;
	}
	if (frame==null){
		//如果为空创建
		frame = new EAPPDialogFrame();
	}
	//不管是从parent取到的还是创建的都要挂到自己的window上，以便传递给子页面
	window.MAIN_FRAME_20140413185023 = frame;
	return frame;
}


/**
*	定义主框架对象
*		由框架页进行实例化，其它子页面进行引用与操作
*		子页面的引用方法：$.getMainFrame()  （跨域时返回EAPPIFrame，否则返回EAPPMainFrame）
*/
function EAPPMainFrame(){
	//同时，记住主框架所在的窗口对象window，以便当所有TAB都被关闭后，能控制主框架打开空白网页
	this.topWindow = window;
	
    this.tabs = [];
    this.currentTab = null;

	/**
	*=====================================================================
	*	1、以下是：：翻页相关的属性与方法
	*=====================================================================
	*/
	/**
	*	1.1.定义翻页属性：当前页与每页条目数
	*		默认为第一页，每页10条。如果要修改条目数，修改这个属性即可
	*		方法：	$.getMainFrame().page.size = 10;
	*/
    
    
    var size = 15;
    try {
    	//TAB DIV的高度 减去上下空隙40 除以一个TBA高变31
	    size = ($("#contentPanel").height() - 40) / 31;
	    size = parseInt(size, 10);
    }catch(e){}
    
	this.page = {no:1,size:size};
	
	this.setPageSize = function(size){
		if (isNaN(size) || size<1){
			return;
		}
		this.page.size = parseInt(size,10);
	}
	/**
	*	1.2.翻到上一页：当前为第一页时，不予理会
	*/
	this.turnPrevPage = function(){
		var pageNo = this.page.no;
		if (pageNo == 1) return false;
	
		pageNo = pageNo - 1;

		this.turnPage(pageNo);
	};

	/**
	*	1.3.翻到下一页：当前为最后一页时，不予理会
	*/
	this.turnNextPage = function(){
		var pageNo = this.page.no;
		var pageCount = parseInt((this.tabs.length-1) / this.page.size , 10) + 1;

		if (pageNo == pageCount) return false;
	
		pageNo = pageNo + 1;

		this.turnPage(pageNo);
	};

	/**
	*	1.4.翻到指定页
	*		@param pageNo 页码
	*			为空时：认为是当前页
	*			小于1时：认为是第一页
	*			大于最大范围时：认为是最后一页
	*		@param	selectedTab	指定翻页后要选中的TAB（用于打开新TAB后，刚好要翻到下一页时）
	*/
	this.turnPage = function(pageNo, selectedTab){
		var pageCount = parseInt((this.tabs.length-1) / this.page.size , 10) + 1;
		//未指定页码时，默认为当前页
		if (pageNo == null) {
			pageNo = this.page.no;
		}else{
			//页码无效时，以就近原则修正为正确的页面
			//	非数字 或 < 1 时: 第一页
			//	> 总页数 时： 最后一页
			if (isNaN(pageNo) || pageNo <=0){
				pageNo = 1;
			}else if (pageNo > pageCount){
				pageNo = pageCount;
			}
			this.page.no = pageNo;
		}
		//控制翻页按钮是否显示，原则：
		//	只有一页时，上下按钮都不可见
		//	第一页时，向上按钮不可见
		//	最后一页时，向下按钮不可见
		if (pageCount == 1){
			$("#btnTurnPrev").hide();
			$("#btnTurnNext").hide();
		}else{
			$("#btnTurnPrev").show();
			$("#btnTurnNext").show();
			if (pageNo==1){
				$("#btnTurnPrev").hide();
			}
			if (pageNo==pageCount){
				$("#btnTurnNext").hide();
			}
		}
		//计算当前页中TAB的起始位置与结束位置（从0开始计数）
		var beginIndex = (this.page.no-1) * this.page.size;
		var endIndex = this.page.no * this.page.size;
		if (endIndex > this.tabs.length) endIndex = this.tabs.length;
		//开始绘制当前页中的所有TAB
		//其原理是：显示在本页范围内的TAB，隐藏其它所有的TAB页
		for (var i=0; i<this.tabs.length ; i++){
			if (i>=beginIndex && i<endIndex){
				this.tabs[i].tabItem.show();
			}else{
				this.tabs[i].tabItem.hide();
			}
		}
		//翻页后，要选中一个TAB为当前页
		//	如果指定了要选中的TAB，则显示该TAB
		if (selectedTab != null){
			selectedTab.select();
		}
	};

    /**
	*=====================================================================
	*	2、以下是：：TAB页的管理方法
	*=====================================================================
	*/

	this.getCurrentTab = function(){
		return this.currentTab;
	}
	
	this.getOpener = function(){
		if (this.currentTab==null) return null;
		
		return  this.getTab(this.currentTab.openerId);
	}
	
	/**
	*	2.1.根据ID，获取TAB对象
	*		如果不存在，则返回null
	*/
	this.getTab = function(id){
		var tab = null;
		for (var i=0 ; i < this.tabs.length ; i++){
			if (this.tabs[i].id == id){
				tab = this.tabs[i];
				break;
			}
		}
		return tab;
	};
	
	/**
	*	2.2.新建TAB页
	*		可指定以下参数：
	*			id:string	窗口ID，当重复时打开已有窗口
	*			title:string	窗口标题
	*			alt:string	TAB页签的鼠标提示文字，为空时默认＝title
	*			url:string	窗口要打开的网页地址
	*			canClosed:boolean	指定本窗口是否可以关闭（如果设定为不可关闭，则不会产生[X]按钮，也无法通过右键菜单关闭掉），默认为true
	*			callback:function	挂接外部回调函数，可通过tab.doCallback()触发
	*			onFocus:function	当窗口被选中时触发onFocus事件，调用此函数
	*			onClose:function	当窗口被关闭时触发onClose事件，调用此函数
	*			onReload:functon	当窗口被刷新时触发onReload事件，调用此函数
	*		返回：TAB对象
	*/
	this.addTab = function(args){
		var options = {
			id:null,
			icon:null,
			title:null,
			alt:null,
			url:null,
			canClosed:true,
			callback:null,
			onFocus:null,
			onClose:null,
			onReload:null
		};
		$.extend(options,args);

		//先判断是否已存在相同ID的TAB，如果存在，则直接打开
		var tab = this.getTab(options.id);
		if (tab != null){
			tab.select();
		}else{
			//不存在，则新建一个TAB对象
			tab = new FrameTab(options.id,this);
			tab.openerId = this.currentTab==null?"":this.currentTab.id;
			tab.icon = options.icon;
			tab.title=options.title;
			tab.alt=options.alt;
			tab.url=options.url;
			tab.canClosed=options.canClosed;
			tab.callback=options.callback;
			tab.onFocus=options.onFocus;
			tab.onClose=options.onClose;
			tab.onReload=options.onReload;
			
			//将TAB对象添加到主框架中，并初始化其内容
			this.tabs.push(tab);
			tab.init();
		}
		
		//此方法将返回TAB对象
		return tab;
	};
	
	/**
	*	2.3.关闭指定ID的TAB窗口
	*		当ID为空时，默认为当前TAB窗口
	*		当该窗口不存在，或者被设置为“不可关闭（canClosed=false）”时，不予理会
	*/
	this.closeTab = function(id){
		//＝＝＝有效性判断＝＝＝＝＝＝＝＝＝＝＝
		if (this.tabs.length == 0){
			return false;
		}
		//判断TAB是否存在
		var tab = null;
		if (id==null || id==""){
			tab = this.currentTab;
		}else{
			tab = this.getTab(id);
		}
		if (tab == null){
			return false;
		}
		//==============================
		//调用TAB的close方法
		tab.close();
	};

    /**
	*=====================================================================
	*	3、以下是：：主框架的各类子对象：
	*		TAB窗口对象
	*		TAB窗口的页签（TAB ITEM）对象
	*		TAB窗口的内容（IFRAME）对象
	*=====================================================================
	*/
	
	/**
	*	3.1.定义TAB对象
	*/
	function FrameTab(id,parent){
		this.id = id;
		this.parent = parent;	//main frame
		this.openerId = null;
		this.tabItem = null;
		this.tabContent = null;
		this.selected = false;
		
		this.icon = null;
		this.title=null;
		this.alt=null;
		this.url=null;
		this.canClosed=true;
		this.callback=null;
		this.onFocus=null;
		this.onClose=null;
		this.onReload=null;
		
		/**
		*	获取自身(TAB)在框架数组中的页码
		*	从1开数计数，如果不存在，则返回-1
		*/
		this.getPageIndex = function(){
			var tabIndex = this.getTabIndex();
			if (tabIndex == -1){
				return -1;
			}else{
				return parseInt(tabIndex / this.parent.page.size,10) + 1;
			}
		};
		
		/**
		*	获取自身(TAB)在框架数组中的序号
		*	从0开数计数，如果不存在，则返回-1
		*/
		this.getTabIndex = function(){
			var idx = -1;
			for (var i=0 ; i < this.parent.tabs.length ; i++){
				if (this.parent.tabs[i].id == id){
					idx = i;
					break;
				}
			}
			return idx;
		};
		
		//更新标题
		this.setTitle = function(title,alt){
			if (title==null) return;
			
			this.title = title;
			
			if (alt != null){
				this.alt = alt;
			}else{
				this.alt = title;
			}
			
			if (this.tabItem != null){
				this.tabItem.setTitle(this.title, this.alt);
			}
		};
		
		//更新URL
		this.loadURL = function(url){
			this.url = url;
			
			if (this.tabContent != null){
				this.tabContent.loadURL(this.url);
			}
		};
		
		//初始化TAB页的内容
		this.init = function(){
			//生成页签
			this.tabItem = new FrameTabItem(this);
			this.tabItem.setIcon(this.icon);
			this.tabItem.setTitle(this.title, this.alt);
			//生成报表容器
			this.tabContent = new FrameTabContent(this);
			this.tabContent.loadURL(this.url);
			//显示自己
			this.select();
		};
		
		//将当前TAB添加到收藏夹
		this.addToFav = function(){
			var url = this.url;
			var menutitle = this.title;
			var moduletitle = this.alt != null ? this.alt : this.id;
			addFavShortCutMenu(url, menutitle, moduletitle);
		};
		
		//刷新当前TAB页的内容
		this.reload = function(){
			if (this.onReload != null){
				//触发onReload事件
				if(!this.onReload(this)) {
					return;
				}
			}
			this.tabContent.loadURL(this.url);
		};
		
		//选中当前TAB
		this.select = function(){
			//触发onFocus事件
			if (this.onFocus != null){
				//try{
					this.onFocus(this);
				//}catch(e){
					//不处理
				//}
			}
			//如果不在当前页，则先翻页到所在页
			var pageIndex = this.getPageIndex();
			if (pageIndex != this.parent.page.no){
				this.parent.turnPage(pageIndex, this);
			}else{
				for (var i=0 ; i<this.parent.tabs.length ; i++){
					if (this.parent.tabs[i].selected){
						this.parent.tabs[i].unselect();
					}
				}
				this.tabContent.show();
				$(this.tabItem.element).addClass("current");
				if (this.canClosed){
					$(this.tabItem.element).find("button").show();
				}
				this.selected = true;
				this.parent.currentTab = this;
			}
		};
		
		//执行回调函数
		this.doCallback = function(args){
			if (this.callback != null){
				try{
					if (typeof this.callback == "function"){
						this.callback(args);
					}else{
						if (this.openerId != null){
							var opener =this.parent.getTab(this.openerId);
							if (opener != null){
								var referWindow = opener.tabContent.element[0].contentWindow;
								eval("referWindow." + this.callback + "(args)");
							}
						}
					}
				}catch(e){}
			}
			
		};
		
		//取消当前TAB的选中状态
		this.unselect = function(){
			this.tabContent.hide();
			$(this.tabItem.element).removeClass("current");
			$(this.tabItem.element).find("button").hide();
			this.selected = false;
		};
	
		//关闭当前TAB
		this.close = function(openTab){
			var obj = this;
			//由于执行回调和关闭方法之间必须有执行间隔否则会出现输入框不能输入的问题
			if (!obj.canClosed) return;
			
			var nextTab = null;
			var ret = true;
			if (obj.onClose != null){
				//触发onClose事件
				ret = obj.onClose(obj);
				if (ret==null) ret = true;
			}
			if (ret){
				//关闭之后，要打开其它的TAB窗口
				//	顺序如下：opener——>prev——>next——>about:blank
				//	所以，要先找到下一个定位的TAB窗口
				var tabIndex = obj.getTabIndex();
				if (openTab != null){
					nextTab = openTab;
				}else{
					nextTab = obj.parent.getTab(obj.openerId);
					if (nextTab == null){
						//如果前一个不存在，则打开后一个
						nextTab = obj.parent.tabs[tabIndex+1];
					}
					if (nextTab == null){
						//如果没有opener，则打开前一个。
						nextTab = tabIndex>0?obj.parent.tabs[tabIndex-1]:null;
					}
				}
				//下面要开始删除自己了
				//	移除子元素（TabItem,TabContent）
				
				$(obj.tabContent.element).removeAttr("src").remove();
				$(obj.tabItem.element).remove();
				//	帮主框架整合数组
				obj.parent.tabs.splice(tabIndex,1);
				
				//定位新的窗口
				if (nextTab != null){
					//显示TAB窗口的同时，还要重新翻页
					obj.parent.turnPage(nextTab.getPageIndex(),nextTab);
				}else{
					//以上都不存在，则说明已经没有任何TAB页了
					//如果指定了ERMP路径，则转到ERMP的首页，否则把整个主框架切换到空白网页
					obj.parent.topWindow.location = "about:blank";
				}
			}
		};
	}
	
	/**
	*	3.2.定义TAB窗口的页签（ITEM）对象
	*/
	function FrameTabItem(parent){
		this.id = null;
		this.parent = parent;
		
		this.element = $("<li/>").append("<span></span><button style=\"display:none\" class=\"tabClose\" onclick=\"$.getEAPPMainFrame().closeTab('" + parent.id + "');\" title=\"关闭本窗口\"></button>").appendTo($(".sideBar").find("ul")); 
		
		$(this.element).click(function(){
			parent.select();
		});
		
		$(this.element).dblclick(function(){
			parent.parent.closeTab(parent.id);
		});
	
		//====about context menu==============
		var menuOptions;
	    if (this.parent.canClosed){
	    	menuOptions = {
		        bindings: {
		        	//添加到收藏夹
		        	'liFav':tabFav,
		            //刷新
		            'liRefresh': tabRefresh,
		            //关闭
		            'liClose': tabClose,
		            //关闭其它
		            'liCloseOther': tabCloseOther,
		            //关闭所有
		            'liCloseAll': tabCloseAll
		        },
		        //菜单弹出事件
		        onContextMenu: contextMenuShow,
		        menuStyleClass: "rightKey",
		        itemStyleClass: "",
		        itemHoverStyleClass: "over"
		    };
		}else{
			menuOptions = {
		        bindings: {
		            //刷新
		            'liRefresh': tabRefresh,
		            //关闭其它
		            'liCloseOther': tabCloseOther
		        },
		        //菜单弹出事件
		        onContextMenu: contextMenuShow,
		        menuStyleClass: "rightKey",
		        itemStyleClass: "",
		        itemHoverStyleClass: "over"
		    };
		}
	    $(this.element).contextMenu("tabContextMenu", menuOptions);
	
		//右键菜单：添加到收藏夹
		function tabFav(){
			var mainFrame = $.getEAPPMainFrame();
			mainFrame.currentTab.addToFav();
		}
		
	    //右键菜单刷新事件
	    function tabRefresh(o){
			var mainFrame = $.getEAPPMainFrame();
			mainFrame.currentTab.reload();
	    };
	    
	    //右键菜单关闭事件
	    function tabClose(o){
			var mainFrame = $.getEAPPMainFrame();
	        mainFrame.closeTab(mainFrame.currentTab.id);
	    };
	    
	    //右键菜单关闭其它事件 
	    function tabCloseOther(o){
	        var mainFrame = $.getEAPPMainFrame();
			var currentTabId = mainFrame.currentTab.id;
	
	        for (var i = mainFrame.tabs.length-1; i >=0 ; i--) {
				if (mainFrame.tabs[i].canClosed && mainFrame.tabs[i].id != currentTabId){
					mainFrame.closeTab(mainFrame.tabs[i].id);
				}
	        };
	    };
	    
	    //右键菜单关闭所有事件
	    function tabCloseAll(o){
	        var mainFrame = $.getEAPPMainFrame();
	
	        for (var i = mainFrame.tabs.length-1; i >=0 ; i--) {
				if (mainFrame.tabs[i].canClosed){
					mainFrame.closeTab(mainFrame.tabs[i].id);
				}
	        };
	    };
	    
	    //右键菜单显示回调函数
	    function contextMenuShow(e){
	    	//由于标签处加了一层<SPAN>，点击的区域不一定是<SPAN>还是本来的<LI>
	    	//为了使二者都有效，只能两个点击事件都触发了
	    	$(e.srcElement || e.target).click();
	    	$(e.srcElement || e.target).parent().click();
	        return true;
	    };
	
		
		this.setTitle = function(title , alt){
			if (alt == null) alt = title;
			$(this.element).find("span").attr({title:alt}).html(title);
		};
		
		this.setIcon = function(icon){
			if (icon != null){
				$(this.element).find("span").before("<label class='" + icon + "'></label>");
			}
		};
		
		this.show = function(){
			this.element.show();
		};
		
		this.hide = function(){
			this.element.hide();
		};
	}
	
	/**
	*	3.3.定义TAB窗口的内容（IFRAME）对象
	*/
	function FrameTabContent(parent){
		this.id = null;
		this.parent = parent;
		this.element = $("<iframe frameborder='0'  scrolling='auto' allowTransparency='true' />").attr({width:'100%',height:'100%'}).appendTo($("#contentPanel"));
		this.loadURL = function(url){
			this.element.attr({src:url});
		};
		
		this.hide = function(){
			$(this.element).hide();
		};
		
		this.show = function(){
			$(this.element).show();
		};
	}
}







/**
*	定义主框架对象
*	跨域情况下取不到EAPPMainFrame时使用此对象的实现
*	子页面的引用方法：$.getMainFrame() （跨域时返回EAPPIFrame，否则返回EAPPMainFrame）
*/
function EAPPDialogFrame() {
	this.wBoxDialog = null;
	this.currentTab = null;
	
	this.setPageSize = function(size){
		
	}
	
	this.turnPrevPage = function(){
		
	}
	
	this.turnNextPage = function(){
		
	}
	
	this.turnPage = function(pageNo, selectedTab){
		
	}
	
	this.getOpener = function(){
		return null;
	}
	
	this.getTab = function(id){
		return this.currentTab;
	}
	
	this.addTab = function(args){
		var tab = new FrameTab(this);
		tab.icon = args.icon;
		tab.title=args.title;
		tab.alt=args.alt;
		tab.url=args.url;
		tab.callback=args.callback;
		tab.onClose=args.onClose;
		tab.onReload=args.onReload;
		this.currentTab = tab;
		
		var win = $(window);
		this.wBoxDialog= $("#_time20140413201522").wBox({//随便引用个不存在的对象
			title : args.title,
		   	requestType : "iframe",
		   	drag : false,
		   	iframeWH : {width : win.width() - 30, height : win.height() - 58},
		   	position : {left: 0, top: 0},
		   	target : args.url,
		   	closeCallBack : args.onClose
		});
		this.wBoxDialog.showBox();
	};
	
	this.closeTab = function(id){
		this.wBoxDialog.close();
	};
	
	this.getCurrentTab = function(){
		return this.currentTab;
	};
	
	/**
	*	定义弹出窗口对象
	*/
	function FrameTab(parent) {
		this.icon = null;
		this.title=null;
		this.alt=null;
		this.url=null;
		this.callback=null;
		this.onClose=null;
		this.onReload=null;
		
		this.getPageIndex = function(){
			return -1;
		}
		
		this.getTabIndex = function(){
			return -1;
		}
		
		this.init = function(){
			
		}
		
		this.addToFav = function(){
			
		}
		
		this.select = function(){
			
		}
		
		this.unselect = function(){
			
		}
		
		//更新标题
		this.setTitle = function(title, alt){
			if (title) {
				$(".wBox_itemTitle", "#wBox").html(title);
				this.title = title;
			}
		}
		
		//更新URL
		this.loadURL = function(url){
			$("iframe", "#wBox").attr("src", url);
		}
		
		//刷新当前TAB页的内容
		this.reload = function(){
			if (this.onReload != null){
				//触发onReload事件
				if(!this.onReload(this)) {
					return;
				}
			}
			var ifr = $("iframe", "#wBox");
			ifr.attr("src", ifr.attr("src"));
		}
		
		//关闭
		this.close = function(){
			parent.closeTab();
		};
		
		//执行回调函数
		this.doCallback = function(args){
			if (this.callback != null){
				try{
					if (typeof this.callback == "function"){
						this.callback(args);
					}else{
						eval(this.callback + "(args)");
					}
				}catch(e){}
			}
		};
	}
}
