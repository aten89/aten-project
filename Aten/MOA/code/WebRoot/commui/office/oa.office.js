/**
* 类型：公用组件
* 名称：OFFICE文档管理（使用NTKO OFFICE文档控件）
* 用途：用于各应用模块集成OFFICE在线编辑，实现WORD、EXCEL等文件的在线编辑
*	   实现三种权限模式：readonly,draft,approve
*	   1）readonly:只读，没有菜单，不能编辑；
*	   2）draft:起草，有菜单，可以编辑，但没有痕迹；
*	   3）approve:审批，有菜单，可以编辑，有痕迹；
*
* 用法：
*	1、集成：
*		1）在需要集成附件控件的页面中，引用.ermp.office.js（如下）：
*			<script language="javascript" src=".ermp.office.js" />
*		2）在页面加载完成之后（如onload事件中），对控件进行初始化：
*			//设置保存到服务器的URL，用于控件向服务器提交文件
*			$.NTKOOfficeControl.setSaveURL("/m_office?act=save");
*			$.NTKOOfficeControl.setSaveFinalURL("/m_office?act=savefinal");
*			$.NTKOOfficeControl.setPublishURL("/m_office?act=save");
*			//初始化控件，并指定显示的容器和ID
*			$.NTKOOfficeControl.init("DIV_OFFICE" [,ID]);
*			//加载文档
*			$.NTKOOfficeControl.openOfficeFile(url [,status (default readonly)]);
*				//status表示打开的状态，用于控制操作权限，包括：readonly|只读(不能编辑)，draft｜起草（没有痕迹），approve|审批（有痕迹）
*	2、保存：
*		//在进行保存之时，调用控件的提交方法，并传入最终的关联ID如下：
*		var obj = $.NTKOOfficeControl.saveOffice(refId);
*			obj.code == 1 || 0	操作结果：1｜成功，0｜失败
*			obj.message	= string	提示信息
*	3、发布为HTML：
*		//公告等需要发布为HTML的，调用控件的以下方法：
*		var obj = $.NTKOOfficeControl.saveAsHTML(refId [,htmlFileName]);	//htmlFileName 指定HTML文件名，须后缀名
*			obj.code == 1 || 0	操作结果：1｜成功，0｜失败
*			obj.message	= string	提示信息
*
*	4、套红头：
*		//公文需要套红头时，调用控件的以下方法，传入红头模版的URL：
*		var obj = $.NTKOOfficeControl.setRedHeader(url);
*
*	5、保存正式文件（接受所有修订后）：
*		//公文除了需要保存含修订痕迹的草稿外，还需要生成一份接受所有修订后的“正式”文档，用于发布给相关部门
*		//调用控件的以下方法：
*		var obj = $.NTKOOfficeControl.saveFinalOffice(refId);
*			obj.code == 1 || 0	操作结果：1｜成功，0｜失败
*			obj.message	= string	提示信息
*
*	6、全屏编辑：
*		//由于目前将OFFICE的编辑窗口嵌套在网页中，所点幅面太小，不便编辑。所以增加了此功能
*		//调用控件的以下方法：
*		$.NTKOOfficeControl.showFullWindow();
*
* 作者：richfans  2009-05-13
*/

var NTKO_OFFICE_OCX_STATUS = "readonly";
var NTKO_OFFICE_OCX_CONTROL = null;
var NTKO_OFFICE_OCX_OPERATOR = null;
var NTKO_OFFICE_OCX_AFTEROPENFROMURLEVENT = null;

function NTKOOfficeControl(){
    this.refId = null;		//关联主体的ID，用于管理附件的关联性
    this.basePath = "";		//访问附件文件的根目录
    this.saveURL = null;	//保存OFFICE（草稿）的URL
	this.saveFinalURL = null;	//保存OFFICE（最终稿）的URL
    this.publishURL = null;	//发布HTML的URL
    this.element = null;	//附件控件实体
    this.isTrackRevisions = true;
	this.HTMLFileName = "htmlfile.html";
	
	/**
	*  设置控件的保存连接
	*  @param URL
	*/
	this.setSaveURL = function(url){
		this.saveURL = url;
	};

	/**
	*  设置控件的保存连接
	*  @param URL
	*/
	this.setSaveFinalURL = function(url){
		this.saveFinalURL = url;
	};
	
	/**
	*  设置控件的发布为HTML的连接
	*  @param URL
	*/
	this.setPublishURL = function(url){
		this.publishURL = url;
	};
	
	/**
	*  设置附件的链接根目录
	*  @param URL
	*/
	this.setBasePath = function(url){
		this.basePath = url;
	};
	
	/**
	*  设置当前操作者的姓名
	*  @param username
	*/
	this.setOperator = function(username){
		NTKO_OFFICE_OCX_OPERATOR = username;
	};
	
	/**
	*  设置加载文档后执行的事件
	*  @param 方法名
	*/
	this.setAfterOpenFromURLEvent = function(functionName){
		NTKO_OFFICE_OCX_AFTEROPENFROMURLEVENT = functionName;
	};
	
	/**
	*  获取控件实体
	*/
	this.getElement = function(){
	    return this.element;
	};

	/**
	*  初始化控件， 由外部集成方调用
	*  @param 附件控件显示的容器ID
	*  @param 关联ID（可选）
	*/
	this.init = function(containerId,refId){
		if (containerId == null){
			alert("未设置OFFICE控件的容器，请联系管理员。");
			return false;
		}
		this.refId = refId;
		//获取容器
		var container = document.getElementById(containerId);
		
		//生成附件控件的代码
		var controlHTML = "<object id=\"NTKO_OFFICE_OCX\" classid=\"clsid:01DFB4B4-0E07-4e3f-8B7A-98FD6BFF153F\" codebase=\"/oa/ocx/officecontrol.cab#version=5,0,2,7\" width=100% height=100%>"
				+ "<param name=\"Titlebar\" value=\"false\">"
	 			+ "<param name=\"Menubar\" value=\"false\">"
	 			+ "<param name=\"Toolbar\" value=\"false\">"
	 			+ "<param name=\"IsShowToolMenu\" value=\"false\">"
        		+ "<param name=\"IsUseUTF8URL\" value=\"true\">"
        		+ "<param name=\"IsUseUTF8Data\" value=\"true\">"
				+ "<param name=\"ProductCaption\" value=\"云和思创信息科技(北京)有限公司\">"
				+ "<param name=\"ProductKey\" value=\"82E51BE101B46380CB69481C294F675B59EF27A7\">"
				+ "<SPAN STYLE=\"color:red\">不能装载NTKO OFFICE控件。请使用IE浏览器，并检查浏览器的安全设置。</SPAN>"
				+ "</object>";
		//将附件对象装入容器中
		container.innerHTML = controlHTML;
		
		this.element = document.getElementById("NTKO_OFFICE_OCX");
		try{
			this.element.setReadonly(true);
			//绑定打开后事件，用于处理后续权限问题
			this.element.attachEvent("AfterOpenFromURL",this.afterOpenFromURL);
		}catch(e){
			this.element = null;
		}
		NTKO_OFFICE_OCX_CONTROL = this.element;
	};
	
	//打开文档之后触发的事件
	this.afterOpenFromURL = function(doc){
		if (NTKO_OFFICE_OCX_CONTROL == null) return false;

		//触发外部定制事件
		if (NTKO_OFFICE_OCX_AFTEROPENFROMURLEVENT){
			eval(NTKO_OFFICE_OCX_AFTEROPENFROMURLEVENT);
		}
		//如果目标文档为空，则默认为当前活动文档
		if (doc == null){
			doc = NTKO_OFFICE_OCX_CONTROL.ActiveDocument;
		}
		//最终还是打不开活动文档，说明控件加载出错了
		if (doc == null){
			alert("加载文档时出错，可能是目标文件不存在。");
			return false;
		}
		//设置当前操作用户，用于保护修订痕迹
		doc.Application.UserName = NTKO_OFFICE_OCX_OPERATOR;
		
		//主要用于控制文档的操作权限
		switch(NTKO_OFFICE_OCX_STATUS){
			case "draft":
				NTKO_OFFICE_OCX_CONTROL.setReadonly(false);
				NTKO_OFFICE_OCX_CONTROL.Toolbars = true;
				doc.CommandBars("Reviewing").Enabled = false;
				doc.CommandBars("Track Changes").Enabled = false;
				doc.TrackRevisions = false;
				break;
			case "approve":
				NTKO_OFFICE_OCX_CONTROL.setReadonly(false);
				NTKO_OFFICE_OCX_CONTROL.Toolbars = true;
				doc.TrackRevisions = true;
				doc.CommandBars("Reviewing").Enabled = false;
				doc.CommandBars("Track Changes").Enabled = false;
				doc.ShowRevisions = false;
				break;
			default:	//readonly
				doc.CommandBars("Reviewing").Enabled = false;
				doc.CommandBars("Track Changes").Enabled = false;
				doc.ShowRevisions = false;
				NTKO_OFFICE_OCX_CONTROL.setReadonly(true);
				NTKO_OFFICE_OCX_CONTROL.Toolbars = false;
				break;
		}
		
		
	};
	
	/**
	* 开关修改痕迹
	* @param flag 开关值｜boolean
	*/
	this.showRevisions = function(flag){
		if (flag == null) return false;
		if (flag != true) flag = false;
		NTKO_OFFICE_OCX_CONTROL.setReadonly(false);
		NTKO_OFFICE_OCX_CONTROL.ActiveDocument.ShowRevisions = flag;
	}

	/**
	* 打开Office文档
	* @param url 打开文档的链接
	* @param status 打开文档的状态
	*	readonly	|	只读（不能进行任何操作）
	*	draft		|	起草（没有修订痕迹）
	*	approve		|	审批（强制修订痕迹）
	*/
	this.openOfficeFile = function(url, status){
		if (status == null || status == "") status = "readonly";
		//如果没有加载控件，则放弃此操作
		if (this.element != null){
			NTKO_OFFICE_OCX_STATUS = status;
			var isReadonly = false;
			if (status == "readonly") isReadonly = true;
			this.element.BeginOpenFromURL(url , false, isReadonly);
		}
	};
	
	/**
	* 插入WORD文档的书签
	*/
	this.insertBookmark = function(sText,sName){
		//如果没有加载控件，则放弃此操作
		if (this.element != null){
			var doc = this.element.ActiveDocument;
			var rangeObj = doc.Application.Selection.Range;
			doc.Bookmarks.Add(sName,rangeObj); 
			this.element.SetBookmarkValue(sName,sText);
		}
	};
	
	/**
	* 设置WORD文档的书签值
	*/
	this.setBookmarkValue = function(bmName,bmValue){
		//如果没有加载控件，则放弃此操作
		if (this.element != null){
			this.element.SetBookmarkValue(bmName,bmValue);
		}
	};
	
	
	/**
	* 读取WORD文档的书签值
	*/
	this.getBookmarkValue = function(bmName){
		//如果没有加载控件，则放弃此操作
		if (this.element != null){
			return this.element.GetBookmarkValue(bmName);
		}else{
			return "";
		}
	};
	
	//更新所有的意见书签，书签名为：[COMMENT:步骤名称]
	//外部传入回调方法，该回调方法负责读取意见内容，并更新书签
	this.setAllCommentBookmark = function(callback){
		var bookmarks = this.element.ActiveDocument.Bookmarks;
		for(var i=1; i<=bookmarks.Count;i++){
			var bookmark = bookmarks(i).name;
			if (bookmark.indexOf("COMMENT：") == 0){
				//以“COMMENT:”开头的，表示意见
				eval(callback+"('" + bookmark + "')");
			}
		}
	}
	
	/**
	*  在文档中插入指定的服务器Word片段(此方法也可用于加载模版和套红头)
	*/
	this.insertWordDocumentFromURL = function(url){
		//如果没有加载控件，则放弃此操作
		if (this.element != null){
			this.element.AddTemplateFromURL(url);
		}
	};
	
	/**
	*  在文档中插入指定的本地Word片段(此方法也可用于加载模版和套红头)
	*/
	this.insertWordDocumentFromLocal = function(filepath){
		//如果没有加载控件，则放弃此操作
		if (this.element != null){
			this.element.AddTemplateFromLocal(filepath);
		}
	};
	
	/**
	*  读取WORD文档的文本内容
	*  @return 纯文本
	*/
	this.getWordText = function(){
		//如果没有加载控件，则放弃此操作
		if (this.element == null){
			return false;
		}
		var doc = this.element.ActiveDocument;
		if (doc == null){
			return "";
		}else{
			return doc.Content.Text;
		}
	};
	
	/**
	*  向服务器保存当前编辑的文档（草稿）
	*  @return 保存成功与否：true/false
	*/
	this.saveOffice = function(refId){
		//如果没有加载控件，则放弃此操作
		if (this.element == null){
			return null;
		}
		if (refId != null){
			this.refId = refId;
		}
		if (NTKO_OFFICE_OCX_STATUS=="draft" || NTKO_OFFICE_OCX_STATUS=="approve"){
			//调用控件的保存方法
			var xml = this.element.SaveAsOtherFormatToURL(5,this.saveURL,"NTKO_UPLOADFILES","referid=" + this.refId);
			return parseXML(xml);
		}else{
			var ret = new Object();
			ret.code = 1;
			ret.message = "只读方式，不能保存";
			return ret;
		}
	};
	
	/**
	*  向服务器保存去除修订痕迹的文档（最终稿）
	*  @return 保存成功与否：true/false
	*/
	this.saveFinalOffice = function(refId,params){
		//如果没有加载控件，则放弃此操作
		if (this.element == null){
			return null;
		}
		if (refId != null){
			this.refId = refId;
		}
		//擦除所有的修订痕迹
		this.element.ActiveDocument.AcceptAllRevisions();
		
		var queryString = "referid=" + this.refId;
		if(params){
			queryString += params;
		}
		//调用控件的保存方法
		var xml = this.element.SaveAsOtherFormatToURL(5,this.saveFinalURL,"NTKO_UPLOADFILES",queryString);
		return parseXML(xml);
	};

	/**
	*  套红头：默认向文档顶端插入指定的文档片断
	*/
	this.setRedHeader = function(url){
		//如果没有加载控件，则放弃此操作
		if (this.element != null && url != ""){
			try{
				this.element.ActiveDocument.Application.Selection.HomeKey(6);	//光标移到页首
				this.element.AddTemplateFromURL(url);
			}catch(err){};
		}
	};

	/**
	*  保存为HTML文件
	*  @return 保存成功与否：true/false
	*/
	this.saveAsHTML = function(refId, htmlFileName,params){
		//如果没有加载控件，则放弃此操作
		if (this.element == null){
			return null;
		}
		if (refId != null){
			this.refId = refId;
		}
		//调用控件的保存方法
		//为HTML文件重命名
		if (htmlFileName != null && htmlFileName != ""){
			this.HTMLFileName = htmlFileName;
		}
		//先接受所有修订
		try{
			this.element.SetReadOnly(false);
		}catch(E){}finally{
			this.element.ActiveDocument.AcceptAllRevisions();
		};
		
		//再发布为HTML
		var queryString = "referid=" + this.refId + "&filename=" + this.HTMLFileName;
		if (params){
			queryString += params;
		}
		var xml = this.element.PublishAsHTMLToURL(this.publishURL,"NTKO_UPLOADFILES",queryString, this.HTMLFileName);
		return parseXML(xml);
	};
	
	this.showFullWindow = function(){
		//如果没有加载控件，则放弃此操作
		if (this.element == null){
			return false;
		}
		//使用网页对话框打开新窗口
		var args = new Object();
		args.userID = NTKO_OFFICE_OCX_OPERATOR;
		args.data = this.element.Data;
		args.status = NTKO_OFFICE_OCX_STATUS;
		
		var width = screen.width;
		var height = screen.height;
		var fullWindow = showModalDialog(this.basePath + "/commui/office/full_edit.html",args,"dialogTop:0;dialogLeft:0;dialogWidth:" + width + ";dialogHeight:" + height + ";status:no;help:no;resizable:yes;");
		if (fullWindow != null){
			//正常返回
			this.element.Data = args.data;
		}
	};
	
	function parseXML(xml){
		var ret = new Object();
		try {
			var xmldom = new ActiveXObject("Microsoft.XMLDOM");
			xmldom.loadXML(xml);
			var root = xmldom.selectSingleNode("root");
			var message = root.selectSingleNode("message");
			ret.code = parseInt(message.getAttribute("code"));
			ret.message = message.text;
		}catch(e){
			ret.code = 0;
			ret.message = "读取返回信息时出错";
		}
		return ret;
	};
}