/**
* 类型：公用组件
* 名称：附件管理（支持WEB与控件两种方式）
* 用途：用于各应用模块管理附件所用，实现附件的上传、查看、删除功能
*	   实现四种权限模式：view,add,delete（控件多了update与saveas两种权限）
*	   1）view:允许以只读方式打开附件文件；
*	   2）add:允许上传新附件；
*	   3）delete:允许删除附件；
* 用法：
*	1）在需要集成附件控件的页面中，引用oa.attachment.js（如下）：
*		<script language="javascript" src="oa.attachment.js" />
*	2）在页面加载完成之后（如onload事件中），对控件进行初始化：
*		//获取控件实例,需要提供容器的ID
*		var ocx = $.getNewAttachmentControl(containerId);
*		//设置控件的使用权限(要求在第一时间设置权限)
*		ocx.setPermission({view:false,add:false,del:false});
*		//设置保存附件的URL，用于控件向服务器提交附件
*		ocx.setSaveURL("/m_attachment?act=save");
*		//定义保存成功或失败后执行的回调函数，执行时将自动带上code和message
*		ocx.setAfterSaveEvent("回调函数名");
*		//初始化控件，并指定关联的数据ID
*		ocx.init(ID);
*		//设置附件文件列表（XMLDOM，格式见方法体的说明）
*		ocx.setFileList(xmldom);
*	3）在进行保存之时，调用控件的提交方法，并传入最终的关联ID如下：
*		ocx.saveAttachments(refId);
*
* 作者：richfans 2009-02-02
*/

jQuery(function(){
	//页面加载时运行，强制下载安装控件
	var noUsingDiv = $("<div />").css({position:'absolute',top:'-1000px',left:'-1000px'}).appendTo('body');
	noUsingDiv.append("<object id=\"NO_USING_NTKO_ATTACHMENT_OCX" + "\" classid=\"clsid:E8FD8E76-203A-48ed-9C39-481479080C34\" codebase=\"/oa/ocx/ntkofman.cab#version=5,0,0,4\" width=1 height=1>"
					+ "<param name=\"ProductCaption\" value=\"\">"
					+ "<param name=\"ProductKey\" value=\"\">"
					+ "</object>");
});

jQuery.extend({

	getNewAttachmentControl:function(containerId, ctrlType){
		if (containerId == null){
			alert("未设置控件的容器，无法创建控件实例。");
			return false;
		}
		
		var isNTKO = true;
		var obj = null;
		if (ctrlType!="web"){
			obj = new NTKOAttachmentControl();
			
			//获取容器
			var container = document.getElementById(containerId);
			
			//生成附件控件的代码
			var controlHTML = "<object id=\"NTKO_ATTACHMENT_OCX_" + obj.id + "\" classid=\"clsid:E8FD8E76-203A-48ed-9C39-481479080C34\" codebase=\"/oa/ocx/ntkofman.cab#version=5,0,0,4\" width=100% height=100%>"
					+ "<param name=\"TitlebarTextColor\" value=\"0\">"
					+ "<param name=\"Toolbar\" value=\"-1\">"
					+ "<param name=\"BackColor\" value=\"FFFFFF\">"
					+ "<param name=\"ViewType\" value=\"2\">"
					+ "<param name=\"DelFileField\" value=\"delFilenames\">"
					+ "<param name=\"DefaultAddFileTypes\" value=\"*.*\">"
					+ "<param name=\"IsConfirmSaveModified\" value=\"0\">"
					+ "<param name=\"IsShowContextMenu\" value=\"0\">"
					+ "<param name=\"IsUseUTF8URL\" value=\"-1\">"
					+ "<param name=\"IsUseUTF8Data\" value=\"-1\">"
					+ "<param name=\"ProductCaption\" value=\"\">"
					+ "<param name=\"ProductKey\" value=\"\">"
					+ "<SPAN STYLE=\"color:red\">不能装载NTKO 附件管理控件。请使用IE浏览器，并检查浏览器的安全设置。</SPAN>"
					+ "</object>";
			//将附件对象装入容器中
			container.innerHTML = controlHTML;
			obj.element = document.getElementById("NTKO_ATTACHMENT_OCX_" + obj.id);
			try{
				//试着调用一下控件的方法，看是否已正常安装
				obj.element.ResetViewSort();
			}catch(e){
				isNTKO = false;
			}
		}else{
			isNTKO = false;
		}
		
		if (!isNTKO){
			obj = null;
			obj = new WebAttachmentControl();
			
			//获取容器
			obj.container = $("#"+containerId);

		}
		//返回对象实例
		return obj;
	},
	
    ajaxAttachmentUpload: function(s) {
        // introduce global settings, allowing the client to modify them for all requests, not only timeout		
        s = jQuery.extend({}, jQuery.ajaxSettings, s);

        var id = new Date().getTime()        
		var form = jQuery.creatAttachmenteUploadForm(id);
		var io = jQuery.createAttachmentUploadIframe(id, s.secureuri);
		var frameId = 'jUploadFrame' + id;
		var formId = 'jUploadForm' + id;
		//clone the file element to form
		jQuery.cloneElementToForm(form, s);

        // Watch for a new set of requests
        if ( s.global && ! jQuery.active++ )
		{
			jQuery.event.trigger( "ajaxStart" );
		}
        var requestDone = false;
        // Create the request object
        var xml = {}   
        if ( s.global )
            jQuery.event.trigger("ajaxSend", [xml, s]);
        // Wait for a response to come back
        var uploadCallback = function(isTimeout)
		{
			if(xml == null){
				return;
			}
			var io = document.getElementById(frameId);
            try{
				if(io.contentWindow){
					xml.responseText = io.contentWindow.document.body?io.contentWindow.document.body.innerHTML:null;
                	xml.responseXML = io.contentWindow.document.XMLDocument?io.contentWindow.document.XMLDocument:io.contentWindow.document;
				}else if(io.contentDocument){
					xml.responseText = io.contentDocument.document.body?io.contentDocument.document.body.innerHTML:null;
					xml.responseXML = io.contentDocument.document.XMLDocument?io.contentDocument.document.XMLDocument:io.contentDocument.document;
				}						
            }catch(e){
				jQuery.handleError(s, xml, null, e);
			}
            if ( xml || isTimeout == "timeout"){				
                requestDone = true;
                var status;
                try {
                    status = isTimeout != "timeout" ? "success" : "error";
                    // Make sure that the request was successful or notmodified
                    if ( status != "error" ){
                        // process the data (runs the xml through httpData regardless of callback)
                        var data = jQuery.convertHttpData( xml, s.dataType );    
                        // If a local callback was specified, fire it and pass it the data
                        if ( s.success )
                            s.success( data, status );
    
                        // Fire the global callback
                        if( s.global )
                            jQuery.event.trigger( "ajaxSuccess", [xml, s] );
                    } else{
                        jQuery.handleError(s, xml, status);
                    }
                } catch(e){
                    status = "error";
                    jQuery.handleError(s, xml, status, e);
                }

                // The request was completed
                if( s.global )
                    jQuery.event.trigger( "ajaxComplete", [xml, s] );

                // Handle the global AJAX counter
                if ( s.global && ! --jQuery.active )
                    jQuery.event.trigger( "ajaxStop" );

                // Process result
                if ( s.complete )
                    s.complete(xml, status);

                jQuery(io).unbind();

                setTimeout(function(){
                	try {
						$(io).remove();
						$(form).remove();	
						
					} catch(e){
						jQuery.handleError(s, xml, null, e);
					}									
				}, 100);

                xml = null;
            }
        }
        
        // Timeout checker
        if ( s.timeout > 0 ){
            setTimeout(function(){
                // Check to see if the request is still happening
                if( !requestDone ) uploadCallback( "timeout" );
            }, s.timeout);
        }
        
        try{
			var form = $('#' + formId);
			var iframe = document.getElementById(frameId);
			$(form).attr('action', s.url);
			$(form).attr('method', 'POST');
			$(form).attr('target', frameId);
            if(form.encoding){
                form.encoding = 'multipart/form-data';				
            }else{
                form.enctype = 'multipart/form-data';
            }
            $(form).submit();
            
			if(iframe.attachEvent){
	            iframe.attachEvent('onload', uploadCallback);
	        }else{
	            iframe.onload = uploadCallback;
	        }
        } catch(e){			
            jQuery.handleError(s, xml, null, e);
        }
        
        return {abort: function () {}};	

    },

	createAttachmentUploadIframe: function(id, uri)
	{
			//create frame
            var frameId = 'jUploadFrame' + id;
            
            if(window.ActiveXObject) {
            	try {
	                var io = document.createElement('<iframe id="' + frameId + '" name="' + frameId + '" />');
	                if(typeof uri== 'boolean'){
	                    io.src = 'javascript:false';
	                }
	                else if(typeof uri== 'string'){
	                    io.src = uri;
	                }
            	}catch(e){
            		var io = document.createElement('iframe');
	                io.id = frameId;
	                io.name = frameId;
            	}
            }else {
                var io = document.createElement('iframe');
                io.id = frameId;
                io.name = frameId;
            }
            io.style.position = 'absolute';
            io.style.top = '-1000px';
            io.style.left = '-1000px';

            document.body.appendChild(io);

            return io;
    },

    creatAttachmenteUploadForm: function(id)
	{
		//create form	
		var formId = 'jUploadForm' + id;
		var form = $('<form  action="" method="POST" name="' + formId + '" id="' + formId + '" enctype="multipart/form-data"></form>');	
		
		//set attributes
		$(form).css('position', 'absolute');
		$(form).css('top', '-1200px');
		$(form).css('left', '-1200px');
		$(form).appendTo('body');

		return form;
    },

	cloneElementToForm: function(form, s)
	{
		//file elements
		for (var i=0; i<s.fileElements.length; i++)
		{
			var oldElement = s.fileElements[i];
			//var newElement = $(oldElement).clone();
			//$(oldElement).attr('id', "toUploadFile" + i);
			//$(oldElement).before(newElement);
			$(oldElement).appendTo(form);
		}
		//referid
		$("<input type='hidden' name='referid' value='" + s.referId + "' />").appendTo(form);
		//delfilenames
		for (var i=0; i<s.deletedFileNames.length ; i++){
			$("<input type=\"hidden\" name=\"delFilenames\" value=\"" + s.deletedFileNames[i] + "\" />").appendTo(form);
		}
	},

    convertHttpData: function( r, type ) {
        var data = !type;
        data = type == "xml" || data ? r.responseXML : r.responseText;
        // If the type is "script", eval it in global context
        if ( type == "script" )
            jQuery.globalEval( data );
        // Get the JavaScript object, if JSON is used.
        if ( type == "json" )
            eval( "data = " + data );
        // evaluate scripts within html
        if ( type == "html" )
            jQuery("<div>").html(data).evalScripts();
			//alert($('param', data).each(function(){alert($(this).attr('value'));}));
        return data;
    }
});

var ALL_ATTACHMENT_CONTROLS = [];
var NTKO_ATTACHMENT_OCX_EVENT_AFTERSAVE = "";

function NTKOAttachmentControl(){
	this.id = "" + ALL_ATTACHMENT_CONTROLS.length + "";	//用于识别当前页面中多个附件控件，自己是哪一个
	ALL_ATTACHMENT_CONTROLS[this.id] = this;

    this.refId = null;		//关联主体的ID，用于管理附件的关联性
    this.basePath = "";		//访问附件文件的根目录
    this.saveURL = null;	//保存附件的URL
    this.element = null;	//附件控件实体
    this.permission = {view:false,add:false,update:false,del:false,saveas:false};	//权限
    this.isNTKOControl = true;
    this.initFileCount = 0;
    this.afterSaveEvent = null;
	
	/**
	*  设置控件的操作权限
	*  此处只控制整个控件的操作权限，具体的文件还可以控制更细粒度的权限，在加载文件列表时进行控制
	*  @param 数组{view:true,add:false,update:false,del:false}
	*/
	this.setPermission = function(options){
		this.permission.view = options.view!=null?options.view:this.permission.view;
		this.permission.add = options.add!=null?options.add:this.permission.add;
		this.permission.update = options.update!=null?options.update:this.permission.update;
		this.permission.del = options.del!=null?options.del:this.permission.del;
		this.permission.saveas = options.saveas!=null?options.saveas:this.permission.saveas;
	};
	
	/**
	*  设置控件的保存连接
	*  @param URL
	*/
	this.setSaveURL = function(url){
		this.saveURL = url;
	};
	
	/**
	*  设置附件的链接根目录
	*  @param URL
	*/
	this.setBasePath = function(url){
		this.basePath = url;
	};
	
	/**
	*  限制控件可添加的文件类型，默认为全部
	*  @param 文件类型列表
	*/
	this.setDefaultFileTypes = function(fileTypes){
		this.element.DefaultAddFileTypes = fileTypes;
	};
	
	/**
	*  保存之后触发的事件
	*  @param 回调函数名称
	*/
	this.setAfterSaveEvent = function(func){
		this.afterSaveEvent = func;
	};
	
	/**
	*  初始化附件控件， 由外部集成方调用
	*  @param 附件控件显示的容器ID
	*  @param 关联ID（可选）
	*  @param 权限(可选)
	*/
	this.init = function(refId){
		this.refId = refId;
		var obj = this;
		try{
			this.element.attachEvent("AfterSaveToURL", 
				function(message,code){
					obj.afterSaveAttachment(message,code,obj.id);
				});	//保存之后触发事件
			
			this.element.SetColumnVisible(5,false);
			//初始化权限
			//先将所有权限关闭
			this.element.IsPermitAddDelFiles = false;
			this.element.IsReadOnlyMode = true;
			//add
			if (this.permission.add == true){
				this.element.IsPermitAddDelFiles = true;
				this.element.IsEnableAdd = true;
				this.element.IsReadOnlyMode = false;
			}
			//update
			if (this.permission.update == true){
				this.element.IsReadOnlyMode = false;
			}
			//delete
			if (this.permission.del == true){
				this.element.IsPermitAddDelFiles = true;
				this.element.IsReadOnlyMode = false;
			}
		}catch(e){
			//未正常安装NTKO控件
		}
	};
	
	/**
	*  设置控件的文件列表
	*  @param XMLDOM
	*    XML格式如下：
	*		<root>
	*		<message code="1"/>
	*		<content>
	*			<attachment id="8a2881da1f3a2dce011f3a36ad200007">
	*				<file-path><![CDATA[budget/200902/89570bd1-78be-47d0-ac30-33bf585687f4.jsp]]></file-path>
	*				<display-name><![CDATA[uploadfiles]]></display-name>
	*				<file-ext><![CDATA[.jsp]]></file-ext>
	*				<size><![CDATA[2427]]></size>
	*				<upload-date><![CDATA[2009-02-03 11:37]]></upload-date>
	*			</attachment>
	*			……
	*		</content>
	*	</root>
	*/
	this.setFileList = function(xmldom){
		if (xmldom == null) return false;
		
		//进入JQUERY的EACH后，this就不能用了，所以这里要先把用到的变量记下来
		var isNTKOControl = this.isNTKOControl;
		var permissions = this.permission;
		var NTKOCTRL = this.element;
		var basePath = this.basePath;
		var fileCount = 0;
		
		$("attachment",xmldom).each(
			function(index){
				var xmlNode = $(this);
				var fileName = $("display-name",xmlNode).text() + $("file-ext",xmlNode).text();
				var fileSize = parseFloat($("size",xmlNode).text());
				var fileURL = basePath + $("file-path",xmlNode).text();
				var modTime = $("upload-date",xmlNode).text();
				fileCount += 1;
				
				if (isNTKOControl){
					//NTKO控件
					var attachFile = NTKOCTRL.AddServerFile(fileURL, fileName, fileSize, modTime);
					if (attachFile != null){
						//针对每个文件设置操作权限
						attachFile.IsAllowEdit = false;
						attachFile.IsAllowOpen = false;
						attachFile.IsAllowDelete = false;
						attachFile.IsAllowSaveAs = false;
						attachFile.IsAllowPrint = false;
						
						//view
						if (permissions.view == true){
							attachFile.IsAllowOpen = true;
							attachFile.isAllowPrint = true;
						}
						//update
						if (permissions.update == true){
							attachFile.IsAllowEdit = true;
						}
						//del
						if (permissions.del == true){
							attachFile.IsAllowDelete = true;
						}
						//saveas
						if (permissions.saveas == true){
							attachFile.IsAllowSaveAs = true;
						}
					}
				}
			}
		);
		//附件个数
		this.initFileCount = fileCount;
	};
	
	/**
	*  最终向服务器提交所有文件变动
	*  @return 保存成功与否：true/false
	*/
	this.saveAttachments = function(refId){
		//如果没有加载控件，则放弃此操作
		if (this.element == null){
			if (this.afterSaveEvent!="" && this.afterSaveEvent!=null){
				eval(this.afterSaveEvent + "(1,'保存附件成功。')");
			}
			return false;
		}
		if (refId != null){
			this.refId = refId;
		}
		if (this.element.FilesCount == 0 && this.initFileCount == 0){
			//没有附件时，不用保存，也不会触发保存后事件
			//所以这里要帮调用者执行回调函数
			if (this.afterSaveEvent!="" && this.afterSaveEvent!=null){
				eval(this.afterSaveEvent + "(1,'保存附件成功。')");
			}
		}else{
			//调用控件的保存方法，这个方法会触发保存后事件
			this.element.BeginSaveToURL(this.saveURL,"uploadFiles","referid=" + this.refId);
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
	
	this.afterSaveAttachment = function(message, code, ctrlId){
		var obj = ALL_ATTACHMENT_CONTROLS[ctrlId];
		
		if (obj.afterSaveEvent == null || obj.afterSaveEvent == ""){
			return false;
		}
		if (code == 0){
			//控件的请求已成功
			//但是有可能后台返回的却是错误信息，所以需要解析返回的XML
			var xml = parseXML(message);
			if (xml.code == 1){
				//成功，触发成功事件
				eval(obj.afterSaveEvent + "(1,'" + xml.message + "')");
			}else{
				//失败，触发失败事件
				eval(obj.afterSaveEvent + "(0,'" + xml.message + "')");
			}
		}else{
			//失败，触发失败事件
			var error = "附件保存失败。";
			if (code ==1) error = "附件保存失败，原因：文件存取错误";
			if (code ==2) error = "附件保存失败，原因：网络传送错误";
			eval(obj.afterSaveEvent + "(0,'" + error + "')");
		}
	};
}

/**
*	WEB版附件管理组件
*/
function WebAttachmentControl(){
	this.id = "" + ALL_ATTACHMENT_CONTROLS.length + "";	//用于识别当前页面中多个附件控件，自己是哪一个
	ALL_ATTACHMENT_CONTROLS[this.id] = this;
	
	this.container = null;
    this.refId = null;		//关联主体的ID，用于管理附件的关联性
    this.basePath = "";		//访问附件文件的根目录
    this.saveURL = null;	//保存附件的URL
	this.limitedFileCount = 0;		//限制上传的文件数量，0为不限制
    this.permission = {view:false,add:false,del:false};	//权限
    this.afterSaveEvent = null;		
	this.afterAddFileEvent = null;	//添加单个文件后触发的事件，可能的用途：选择文件后，通过这个事件实现立即上传
	this.limitedFileExts = null;	//限制上传的文件扩展名(数组，允许多个)

    //以下用于控制当前的附件，包括：所有附件、新增附件、被删除附件
    this.attachments = [];
    this.addedAttachments = [];
    this.deletedAttachments = [];
	
	/**
	*  设置控件的操作权限
	*  此处只控制整个控件的操作权限，具体的文件还可以控制更细粒度的权限，在加载文件列表时进行控制
	*  @param 数组{view:true,add:false,del:false}
	*/
	this.setPermission = function(options){
		this.permission.view = options.view!=null?options.view:this.permission.view;
		this.permission.add = options.add!=null?options.add:this.permission.add;
		this.permission.del = options.del!=null?options.del:this.permission.del;
	};
	
	/**
	*  设置控件的保存连接
	*  @param URL
	*/
	this.setSaveURL = function(url){
		this.saveURL = url;
	};
	
	/**
	*  设置附件的链接根目录
	*  @param URL
	*/
	this.setBasePath = function(url){
		this.basePath = url;
	};

	/**
	*  设置限制上传的文件数量
	*/
	this.setLimitedFileCount = function(count){
		if (isNaN(count)) count = 0;
		
		this.limitedFileCount = count;
	}

	/**
	*  设置限制上传的文件扩展名
	*  @param exts 数组，允许的扩展名
	*/
	this.setLimitedFileExts = function(exts){
		this.limitedFileExts = exts;
	}
	
	/**
	*  保存之后触发的事件
	*  @param 回调函数名称
	*/
	this.setAfterSaveEvent = function(func){
		this.afterSaveEvent = func;
	};

	/**
	*  添加单个文件后触发的事件
	*  @param 回调函数名称
	*/
	this.setAfterAddFileEvent = function(func){
		this.afterAddFileEvent = func;
	};
	
	/**
	*  设置控件的文件列表
	*  @param XMLDOM
	*    XML格式如下：
	*		<root>
	*		<message code="1"/>
	*		<content>
	*			<attachment id="8a2881da1f3a2dce011f3a36ad200007">
	*				<file-path><![CDATA[budget/200902/89570bd1-78be-47d0-ac30-33bf585687f4.jsp]]></file-path>
	*				<display-name><![CDATA[uploadfiles]]></display-name>
	*				<file-ext><![CDATA[.jsp]]></file-ext>
	*				<size><![CDATA[2427]]></size>
	*				<upload-date><![CDATA[2009-02-03 11:37]]></upload-date>
	*			</attachment>
	*			……
	*		</content>
	*	</root>
	*/
	this.setFileList = function(xmldom){
		if (xmldom == null) return false;
		
		var basePath = this.basePath;
		var attachments = this.attachments;
		
		$("attachment",xmldom).each(
			function(index){
				var xmlNode = $(this);
				var attach = new WebAttachment();
				attach.id = xmlNode.attr("id");
				attach.fileName = $("display-name",xmlNode).text() + $("file-ext",xmlNode).text();
				attach.fileSize = parseFloat($("size",xmlNode).text());
				attach.fileURL = basePath + $("file-path",xmlNode).text();
				attach.modTime = $("upload-date",xmlNode).text();
				//将附件添加到数组中，以文件名为关键字
				attachments.push(attach);
				
			}
		);
		
		//全部添加后，一次性绘制
		this.drawFileList();
	};
	
	/**
	*	根据attachments生成附件列表
	*/
	this.drawFileList = function(){
		//生成附件列表
		var htmlCode = "";
		for (var i=0 ; i<this.attachments.length ; i++){
			var attachment = this.attachments[i];
			htmlCode += "<li>" + (i+1) + ".&nbsp;" ;
			if (this.permission.view == true){
				htmlCode += "<a href=\"" + attachment.fileURL + "\" target=\"_blank\">" + attachment.fileName + "</a>";
			}else{
				htmlCode += attachment.fileName;
			}
			if (this.permission.del == true){
				htmlCode += "&nbsp;&nbsp;&nbsp;<a style=\"cursor:hand;\" onclick=\"WebAttachmentDeleteFile('" + this.id + "','" + attachment.id + "')\"><input type=\"button\" class=\"fjDel\"/>删除</a>";
			}
			htmlCode += "</li>";
		}
		this.container.find("[name='ATTACHMENT_LIST']").html(htmlCode);
	}
	
	/**
	*  初始化附件控件， 由外部集成方调用
	*  @param 关联ID（可选）
	*  @param 权限(可选)
	*/
	this.init = function(refId){
		this.refId = refId;
		//生成附件控件的代码
		var htmlCode = "<div class='fjObj'>"
			+ "<div class='fjTools'><span></span>"
			+ "<ul><li>";
		if (this.permission.add == true){
			htmlCode += "<div class='fileinputs'>"
	           		+ "<div class='fileTextList'><input type='file'  class='file hidden' noscript='true' size='10' name='uploadFiles' onchange=\"WebAttachmentAddFile(this,'" + this.id + "');\"/></div>"
	           		+ "<div class='fakefile' style='left:1px'><input type=\"button\" class=\"fjAdd\"/></div></div>";
		}else{
			htmlCode += "附件";
		}
	    htmlCode += "</li></ul>"
	       + "</div>"
	       + "<div class='fjMb'><ul name='ATTACHMENT_LIST'></ul></div>"
	       + "</div>";
		this.container.html(htmlCode);
	};
	
	/**
	*  最终向服务器提交所有文件变动
	*  @return 保存成功与否：true/false
	*/
	this.saveAttachments = function(refId){
		if (refId != null){
			this.refId = refId;
		}
		if (this.addedAttachments.length==0 && this.deletedAttachments.length == 0){
			//附件没有变动，不用保存
			//直接触发保存后事件
			if (this.afterSaveEvent != null && this.afterSaveEvent != ""){
				eval(this.afterSaveEvent + "(1,'保存附件成功。')");
			}
		}else{
			//提取新增的附件
			var fileElements = [];
			for (var i=0; i<this.addedAttachments.length; i++){
				fileElements.push(getAttachObjectByFileName(this.attachments,this.addedAttachments[i]).element);
			}
			var deletedFileNames = [];
			for (var i=0; i<this.deletedAttachments.length; i++){
				deletedFileNames.push(this.deletedAttachments[i]);
			}
			var afterSaveEventFunc = this.afterSaveEvent;
			//调用附件保存方法，这个方法会触发保存后事件
			$.ajaxAttachmentUpload({
				url:this.saveURL,
				secureuri:false,
				dataType: 'xml',
				referId:this.refId,
				fileElements:fileElements,
				deletedFileNames:deletedFileNames,
				success: function(data, status){
					//触发保存后事件
					if (afterSaveEventFunc != null && afterSaveEventFunc != ""){
						var xml = parseXML(data);
						//需要解析返回的XML
						if (xml.code == 1){
							//成功，触发成功事件
							eval(afterSaveEventFunc + "(1,'" + xml.message + "')");
						}else{
							//失败，触发失败事件
							eval(afterSaveEventFunc + "(0,'" + xml.message + "')");
						}
					}
				},
				error: function (data, status, e){
					alert(e);
				}
			});
		}
	};
	
	function parseXML(xml){
		var ret = new Object();
		try {
			var xmldom = null;
			if (typeof xml == "string"){
				xmldom = new ActiveXObject("Microsoft.XMLDOM");
				xmldom.loadXML(xml);
			}else{
				xmldom = xml;
			}
			//var root = xmldom.selectSingleNode("root");
			//var message = root.selectSingleNode("message");
			
			//Firefox不支持selectSingleNode方法，所以重写了同时支持IE和FF的SelectSingleNode方法。
			var root = SelectSingleNode(xmldom, "root");
			var message = SelectSingleNode(xmldom, "root/message");
			ret.code = parseInt(message.getAttribute("code"));
			ret.message = message.text;
		}catch(e){
			ret.code = 0;
			ret.message = "读取返回信息时出错";
		}
		return ret;
	};
}

function SelectSingleNode(xmlDoc, elementPath) 
{ 
    if(window.ActiveXObject) 
     { 
        return xmlDoc.selectSingleNode(elementPath); 
     } 
    else 
     { 
        var xpe = new XPathEvaluator(); 
        var nsResolver = xpe.createNSResolver( xmlDoc.ownerDocument == null ? xmlDoc.documentElement : xmlDoc.ownerDocument.documentElement); 
        var results = xpe.evaluate(elementPath,xmlDoc,nsResolver,XPathResult.FIRST_ORDERED_NODE_TYPE, null); 
        return results.singleNodeValue; 
     } 
} 


/**
*	文件对象
*/
function WebAttachment(){
	this.id = "" + Math.floor(Math.random() * 1000000) + "";
	this.fileName = "";
	this.fileSize = 0;
	this.fileURL = "";
	this.modTime = "";
	this.element = null;	//记录文件上传组件FILE INPUT
	//移除FILE INPUT
	this.remove = function(){
		if (this.element != null){
			$(this.element).remove();
		}
	}
}

/**
*	根据关键字（文件名）从数组中读取文件对象
*/
function getAttachObjectByFileName(attachments, fileName){
	var attachment = null;
	for (var i=0 ; i<attachments.length ; i++){
		if (attachments[i].fileName == fileName){
			attachment = attachments[i];
			break;
		}
	}
	return attachment;
}

/**
*	添加本地附件到列表中（仅生成FILE INPUT，未上传）
*/
function WebAttachmentAddFile(fileObj, ctrlId){
	var ctrl = ALL_ATTACHMENT_CONTROLS[ctrlId];
	//先读取要上传的文件名，看看是否重名（由于以文件名为关键字，所以不允许重名）
	var localFilePath = fileObj.value;
	var fileName = localFilePath.substring(localFilePath.lastIndexOf("\\")+1,localFilePath.length);

	//如果没前文件数已超过限制的个数，则不允许添加新文件
	if (ctrl.limitedFileCount != 0 && ctrl.limitedFileCount <= ctrl.attachments.length){
		alert("附件数量已超过限制数，不允许再添加新的文件。");
		return false;
	}

	//判断是否是允许范围内的扩展名
	if (ctrl.limitedFileExts != null){
		var fileExt = "";
		var extIndex = fileName.lastIndexOf(".");
		if (extIndex != -1){
			fileExt = fileName.substring(extIndex+1,fileName.length);
		}
		var inArray = false;
		for (var i=0 ; i<ctrl.limitedFileExts.length ; i++){
			if (fileExt.toLowerCase() == ctrl.limitedFileExts[i].toLowerCase()){
				inArray = true;
				break;
			}
		}
		if (!inArray){
			alert("对不起，此处仅允许上传扩展名为：" + ctrl.limitedFileExts.toString() + " 的文件。");
			return false;
		}
	}

	//判断是否有文件名重复
	if (getAttachObjectByFileName(ctrl.attachments, fileName) != null){
		alert("列表中已存在相同名称的附件，请重新命名！");
		return false;
	}
	//处理页面控件，把原来的隐藏掉，再增加一个新的FILE INPUT
	$(fileObj).hide();
	ctrl.container.find(".fileTextList").append("<input type=\"file\" class=\"file hidden\" noscript=\"true\" size=\"10\" name=\"uploadFiles\" onchange=\"WebAttachmentAddFile(this,'" + ctrlId + "');\"/>");
	
	//记录新增附件信息
	var attach = new WebAttachment();
	attach.fileName = fileName;
	attach.fileURL = localFilePath;
	attach.element = fileObj;
	//在数组中增加一个文件
	ctrl.addedAttachments.push(fileName);
	ctrl.attachments.push(attach);
	//要重新绘制附件列表
	ctrl.drawFileList();

	//触发AfterAddFileEvent
	if (ctrl.afterAddFileEvent != null && ctrl.afterAddFileEvent != ""){
		eval(ctrl.afterAddFileEvent + "()");
	}
}

/**
*	从列表中删除附件
*/
function WebAttachmentDeleteFile(ctrlId,attachId){
	var ctrl = ALL_ATTACHMENT_CONTROLS[ctrlId];
	var fileName = "";
	//先移除attachments中的对象
	for (var i=0 ; i<ctrl.attachments.length ; i++){
		if (ctrl.attachments[i].id == attachId){
			fileName = ctrl.attachments[i].fileName;
			ctrl.attachments[i].remove();
			ctrl.attachments.splice(i,1);
			break;
		}
	}
	//再判断是否新增的文件
	var isAddedFile = false;
	for (var i=0 ; i<ctrl.addedAttachments.length ; i++){
		if (ctrl.addedAttachments[i] == fileName){
			ctrl.addedAttachments.splice(i,1);
			isAddedFile = true;
			break;
		}
	}
	if (isAddedFile==false){
		//如果不是新增的文件被删除，则表示从服务器上删除，需要记录
		ctrl.deletedAttachments.push(fileName);
	}
	//要重新绘制附件列表
	ctrl.drawFileList();
}