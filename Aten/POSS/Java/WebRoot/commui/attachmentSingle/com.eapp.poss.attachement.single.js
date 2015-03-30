/**
 * 单个附件上传
 *
 * 时间			作者		修改原因
 * 2012-10-25 	卢凯宁 	修改成JQuery 扩展 
 *
 * 单个附件上传,基于ajaxfileupload 上传控件，使用引入
	<script type="text/javascript" src="webui/attachmentSingle/ajaxfileupload.js"></script>
	<link rel="STYLESHEET" type="text/css" href="webui/attachmentSingle/ajaxfileupload.css"/>
	<script type="text/javascript" src="webui/attachmentSingle/com.eapp.poss.attachement.single.js"></script>
 * 
 * 
 * 
 * 参数说明：
 * 		divId:目标div的id,必填
 * 		fileType：限定上传的文件类型，可以为 "" 可上传文件类型,选填 单个形式 ".doc",多个"doc,docx"
 * 		type：附件类型
 * 		url :附件上传url,必填
 * 		oldShowId:旧的显示区域id,必填
 * 		uploadImage : 上传按钮图片
 * 		callback：自定义上传完成后回调函数，
 * 		showNameFlag : 是否显示文件名 true,false
 * 		beforeDelete : 自定义删除前函数,选填 ,返回结果 true or false
 * 		deleteCallback：自定义点击删除按钮后回调函数，
 * 
 * 使用方法：
 * 	var upgradeScheme;
	upgradeScheme = $("#upgradeSchemeShow").singleAttaUpLoad({
		divId : "upgradeSchemeShow",
		oldShowId : "upgradeScheme",
		fileType : "doc,docx",
		url : "m/release_Prod_Version_Form/save_attachment",
		uploadCallback : function () {
			alert("添加完后回调函数");
		},
		deleteCallback : function () {
			alert("点击删除后回调函数");
		}
	});
 * 	var path = functionObject.getTempDir() //取得文件上传后在服务器上的临时路径
 * 
 * 如果 var funFlag = functionObject.getHasValueFlag(); 
 * 
 * 后台处理：funFlag 为 true，则标识该附件有值，判断 path 是否为空，
 * 如果path 为 “”，则标识附件没有新上传，使用原来的附件；如果path不为空，则使用 path 属性copy 附件 关联新的附件到 发布单下，并要删除原来 发布单下的附件。
 * 
 * functionObject.getId() //返回显示区域id
 * functionObject.getTempDir() //返回上传后临时目录路径
 * functionObject.getFileName() //取得文件名
 * functionObject.getFileType() //取得文件类型
 * functionObject.remove(true) //被动调用删除方法 true,调用删除回调方法 false,不调用回调方法
 * functionObject.getAttrachment() //返回组装好的附件对象
 * 
 * 页面：
 * 	<td width="400" colspan="5">
	      	<div id="upgradeScheme" style="display:none;">
				<c:if test="${!empty releaseProdVersionForm.functionAttach}">
	           			<li><img src="themes/comm/images/fjicon.gif"/><a class="opLink c06c" href="upload/${releaseProdVersionForm.functionAttach.filePath }" target="_blank">${releaseProdVersionForm.functionAttach.displayName }${releaseProdVersionForm.functionAttach.fileExt }</a></li>
		         </c:if>
	        </div>
       		<div id="upgradeSchemeShow"></div>
       	</td>
	* 如上所示，upgradeScheme 就是 "oldShowId" ,upgradeSchemeShow 就是 divId
 */

(function($) {
	
	$.fn.singleAttaUpLoad = function(options) {
		
		//默认参数
		var defaults = {
			divId : null, //目标div的id,必填
			oldShowId : null, //旧的显示区域id,必填
			url : null , //附件上传url,必填
			type : null, //附件类型
			fileType : null, //可上传文件类型,选填 单个形式 ".doc",多个"doc,docx"
			uploadImage : "themes/comm/images/upload.gif", //上传按钮图片
			showNameFlag : true, // 是否显示文件名
			showDeleteButton : true, // 上传完成后,是否显示删除按钮
			showFileButtion : true, // 上传完成后,是否显示文件图标
			uploadCallback : null, //自定义上传完成后回调函数,选填
			beforeDelete : null, //自定义删除前函数,选填 ,返回结果 true or false
			deleteCallback : null //自定义点击删除按钮后回调函数,选填
		};
		
		//当前引用的DIV的jquery对像
		var p_this = $(this);
		
		//=======设置用户自定义参数====================
		//1.合并参数：用传入的参数覆盖默认参数
		$.extend(defaults, options);
		
		// 加载上传控件
		initAttachement(p_this,defaults);
		// 给上传,删除按钮绑定方法
		bindMethods(defaults);
		
		//=========对外调用方法 start ==========//
		
		// 1.判断附件是否有值
		this.getHasValueFlag = function () {
			var hasValueFlag = $("#value"+defaults.divId).val();
			var oldHasFlag = checkOldHasFlag(defaults);
			if (hasValueFlag == "true") {
				if (oldHasFlag) {
					$("#value"+defaults.divId).val("true");
				} else if (!oldHasFlag && (this.getTempDir() == null || this.getTempDir() == "")) {
					$("#value"+defaults.divId).val("false");
				} else if (!oldHasFlag && (this.getTempDir() != null || this.getTempDir() != "")) {
					$("#value"+defaults.divId).val("true");
				}
			}
			if ($("#value"+defaults.divId).val() == "true") {
				return true;
			} else {
				return false;
			}
		};
		
		// 2.返回上传附件的临时目录路径
		this.getTempDir=function () {
			return $.trim($("#path"+defaults.divId).val());
		};
		
		// 3.取文件名
		this.getFileName = function () {
			return $("#displayName"+defaults.divId).val();
		};
		
		// 4.取文件类型
		this.getFileType = function () {
			return $("#extName"+defaults.divId).val();
		};
		
		// 5.返回改上传控件id
		this.getId = function () {
			return defaults.divId;
		};
		
		// 6.删除方法
		this.remove = function (bool) {
			if (bool) {
				del('del'+defaults.divId,'show'+defaults.divId,defaults.oldShowId,'value'+defaults.divId,defaults.beforeDelete,defaults.deleteCallback);
			} else {
				del('del'+defaults.divId,'show'+defaults.divId,defaults.oldShowId,'value'+defaults.divId,null,null);
			}
		};
		
		// 7.取得附件类型
		this.getType = function () {
			return defaults.type;
		};
		
		// 8.取得附件对象
		this.getAttrachment = function() {
			// 组装附件
			var attrachment = {};
			if (this.getHasValueFlag()) {
				attrachment.path = this.getTempDir();
				attrachment.fileName = this.getFileName();
				attrachment.fileType = this.getFileType();
				if (this.getType() != null) {
					attrachment.type = this.getType();
				} else {
					attrachment.type = "";
				}
				attrachment.valueFlag = "true";
			} else {
				attrachment.valueFlag = "false";
				attrachment.type = this.getType();
			}
			return attrachment;
		};
		
		//=========对外调用方法 end ==========//
		return this;
	};
	
	/**
	 * 加载上传控件
	 */
	function initAttachement(p_this,defaults) {
		// 先设置为空
		p_this.html("");
		// 待填充的html
		var html = "";
		if (defaults.divId == null || defaults.divId == "" || defaults.oldShowId == null || 
				defaults.oldShowId == "" || defaults.url == null || defaults.url == "") {
			html += "<span style='color:red;'>控件加载失败!</span>";
			// 填充控件
			p_this.html(html);
		} else {
			html +="<table border='0' cellpadding='0'  cellspacing='0'>";
	        html += "<tr><td id='show"+defaults.divId+"'></td>";
			html += "<td><div class='fileinputs' style='width:auto;width:50px'>";
			html += "<input type='hidden' id='extName"+defaults.divId+"' name='extName"+defaults.divId+"' />";
			html += "<input type='hidden' id='displayName"+defaults.divId+"' name='displayName"+defaults.divId+"'/>";
			//如果是火狐浏览器
			if ($.browser.mozilla) {	
				html += "<input width='0px' type='file' style='display: none' class='file hidden' style='height:22px;' id='attachment"+defaults.divId+"' name='attachment'" +
					"/>";
				html += "<label for='attachment"+defaults.divId+"'><div class='fakefile' >";
				html += "<img src='themes/comm/images/upload.gif' id='openUpload"+defaults.divId+"' style='cursor:pointer;'/>";
				html += "</div></label>";
			} else {
				html += "<input width='0px' type='file' class='file hidden' style='height:22px;' id='attachment"+defaults.divId+"' name='attachment'" +
					"/>";
				html += "<label for='attachment"+defaults.divId+"'><div class='fakefile'>";
				html += "<img src='"+defaults.uploadImage+"'/>";
				html += "</div></label>";
			}
			html += "&nbsp;&nbsp;</div>";
			html += "<td><input type='image'  src='themes/comm/images/crmDel_ico.gif' title='删除' id='del"+defaults.divId+"'/></td>";
			html += "<input type='hidden' id='path"+defaults.divId+"'/>" +
					"<input type='hidden' id='value"+defaults.divId+"' value='true'/></td>";
			html += "</tr></table>";
			// 填充控件
			p_this.html(html);
			// 初始化是否显示已有附件,删除按钮
			var oldHtml = $.trim($("#"+defaults.oldShowId).html());
			if (oldHtml != null && oldHtml != "") {
				if (!defaults.showNameFlag) {
					oldHtml = "<ul><li><a target='_blank' id='uploadFileHrefId' href='"+$("#"+defaults.oldShowId).find('a').attr("href");
					oldHtml += "'><img src='themes/comm/images/fjicon.gif' ";
					oldHtml += " title='"+$("#"+defaults.oldShowId).find('a').text()+"'";	
					oldHtml += "/></a></ul>";
				}
				$("#show"+defaults.divId).html(oldHtml);
				$("#show"+defaults.divId).show();
				$("#del"+defaults.divId).show();
			} else {
				$("#show"+defaults.divId).hide();
				$("#del"+defaults.divId).hide();
			}
		}
	}
	
	/**
	 * 给上传按钮绑定上传方法，分不同浏览器
	 */
	function bindMethods(defaults) {
		// jquery 1.2.6.min 不支持live方法
		$("#attachment"+defaults.divId).bind('change', function() {
		  	ajaxFileUpload(defaults.url,'attachment'+defaults.divId,defaults.fileType,'extName'+defaults.divId,'displayName'+defaults.divId,'show'+defaults.divId,'path'+defaults.divId,defaults.oldShowId,'del'+defaults.divId,'value'+defaults.divId,defaults.uploadCallback,defaults.beforeDelete,defaults.deleteCallback,defaults.showNameFlag,defaults.showDeleteButton,defaults.showFileButtion);
		});
		$("#del"+defaults.divId).bind('click',function() {
			del("del"+defaults.divId,"show"+defaults.divId,defaults.oldShowId,"value"+defaults.divId,defaults.beforeDelete,defaults.deleteCallback);
		});
		/*if (isIE()) {
			//IE 浏览器
			$("#attachment"+defaults.divId).bind('change', function() {
			  	ajaxFileUpload(defaults.url,'attachment'+defaults.divId,defaults.fileType,'extName'+defaults.divId,'displayName'+defaults.divId,'show'+defaults.divId,'path'+defaults.divId,defaults.oldShowId,'del'+defaults.divId,'value'+defaults.divId,defaults.uploadCallback,defaults.beforeDelete,defaults.deleteCallback,defaults.showNameFlag,defaults.showDeleteButton,defaults.showFileButtion);
			});
			$("#del"+defaults.divId).bind('click',function() {
				del("del"+defaults.divId,"show"+defaults.divId,defaults.oldShowId,"value"+defaults.divId,defaults.beforeDelete,defaults.deleteCallback);
			});
		} else if ($.browser.mozilla) {
			//火狐浏览器
			$("#attachment"+defaults.divId).live('change', function() {
			  ajaxFileUpload(defaults.url,'attachment'+defaults.divId,defaults.fileType,'extName'+defaults.divId,'displayName'+defaults.divId,'show'+defaults.divId,'path'+defaults.divId,defaults.oldShowId,'del'+defaults.divId,'value'+defaults.divId,defaults.uploadCallback,defaults.beforeDelete,defaults.deleteCallback,defaults.showNameFlag,defaults.showDeleteButton,defaults.showFileButtion);
			});
			$("#openUpload"+defaults.divId).live('click', function() {
			  $("#attachment"+defaults.divId).click();	
			});
			$("#del"+defaults.divId).live('click',function() {
				del("del"+defaults.divId,"show"+defaults.divId,defaults.oldShowId,"value"+defaults.divId,defaults.beforeDelete,defaults.deleteCallback);
			});
		} else {
			$("#attachment"+defaults.divId).live('change', function() {
			  ajaxFileUpload(defaults.url,'attachment'+defaults.divId,defaults.fileType,'extName'+defaults.divId,'displayName'+defaults.divId,'show'+defaults.divId,'path'+defaults.divId,defaults.oldShowId,'del'+defaults.divId,'value'+defaults.divId,defaults.uploadCallback,defaults.beforeDelete,defaults.deleteCallback,defaults.showNameFlag,defaults.showDeleteButton,defaults.showFileButtion);
			});
			$("#del"+defaults.divId).live('click',function() {
				del("del"+defaults.divId,"show"+defaults.divId,defaults.oldShowId,"value"+defaults.divId,defaults.beforeDelete,defaults.deleteCallback);
			});
		} */
	}
	
	/**
	 * 判断控件是否有链接地址,用来判断附件是否存在
	 * 后台处理：funFlag 为 true，则标识该附件有值，判断 path 是否为空，
 	 * 1.如果path 为 “”，则标识附件没有新上传，使用原来的附件；
 	 * 2.如果path不为空，则使用 path 属性copy 附件 关联新的附件到 发布单下，并要删除原来 发布单下的附件。
	 */
	function checkOldHasFlag(defaults) {
		var oldHasFlag = false;
		var oldHtml = $.trim($("#"+defaults.oldShowId).html());
		if (oldHtml != null && oldHtml != "") {
			$("#show"+defaults.oldShowId).html(oldHtml);
			var array = $("#"+defaults.oldShowId+"> a");
			if (array != null && array.length > 0) {
				oldHasFlag = true;
			} 
		} 
		return oldHasFlag;
	}
	
	/**
	 * 文件上传
	 * @param {String} url 附件保存url
	 * @param {String} attachmentId 上传input的id
	 * @param {String} type 限制上传的文件类型
	 * @param {String} extNameId 文件后缀名id
	 * @param {String} displayNameId 文件原名
	 * @param {String} showId 显示区域id
	 * @param {String} pathId 存放上传后的服务器路径id
	 * @param {String} oldShowId 隐藏的显示区域id
	 * @param {String} delId 移除按钮id
	 * @param {String} valueId 移除按钮id
	 * @param {function} callback 回调函数
	 * @param {function} beforeDelete 删除前
	 * @param {function} deleteCallBack 删除回调函数
	 * @param {function} showNameFlag 是否显示文件名
	 * @param {function} showDeleteButton 上传完成后是否显示删除按钮
	 * @param {function} showFileButtion 上传完成后,是否显示文件图标
	 */
	function ajaxFileUpload(url,attachmentId,type,extNameId,displayNameId,showId,pathId,oldShowId,delId,valueId,callback,beforeDelete,deleteCallBack,showNameFlag,showDeleteButton,showFileButtion) {
		
		// 判断是否上传
		var guideFile=$("#"+attachmentId).val();
		if (guideFile == "") {
			alert("请选择导入的文件");
			return ;
		}
		// 判断上传文件是否妇科自定义
		var fileext = guideFile.substring(guideFile.lastIndexOf('.') ,guideFile.length);
		var fileExt = guideFile.substring(guideFile.lastIndexOf('.') + 1 ,guideFile.length);
		if (type != null && type != "" && type.indexOf(fileExt) < 0 && type.toUpperCase().indexOf(fileExt) < 0) {
			alert("导入的附件必须是"+type+"类型的文件!");
			return;
		}
		// 文件名
		var displayName = guideFile.substring(guideFile.lastIndexOf('\\'),guideFile.lastIndexOf('.'));
		$("#"+extNameId).val(fileext);
		$("#"+displayNameId).val(displayName);
		displayName = displayName.replace("\\","");
		// 调用封装的上传控件
		var realUrl = url;
		if(url.indexOf("?") == -1) {
			realUrl = url + '?displayName='+displayName+"&extName="+fileext;
		} else {
			realUrl = url + '&displayName='+displayName+"&extName="+fileext;
		}
		$.ajaxFileUpload({	
			url: realUrl, 
			secureuri:false,
			fileElementId:attachmentId,
			dataType: 'xml',
			success: function (data, status) {	
				var message =$("message",data);
				var messageCode = message.attr("code");
				if(messageCode == "0"){
					alert(message.text());
				} else if(messageCode == "1"){
					$("#"+valueId).val("true");
					var html = "";
					if (showNameFlag) {
						html = "<ul><li><img src='themes/comm/images/fjicon.gif'/>";
						html += "<a id='uploadFileHrefId' class='opLink c06c' href='javaScript:void(0);'>"+displayName+fileext+"</a></li>";
						html += "</ul>";
					} else {
						if (showFileButtion) {
							html = "<ul><li><a id='uploadFileHrefId' class='opLink c06c' href='javaScript:void(0);'><img src='themes/comm/images/fjicon.gif' ";
							html += " title='"+displayName+fileext+"'";	
							html += "/></a></ul>";
						} else {
							html = "";
						}
					}
					// 页面显示	
					$("#"+showId).html(html);
					$("#"+showId).show();
					// 是否显示删除按钮
					if (showDeleteButton) {
						$("#"+delId).show();
					} else {
						$("#"+delId).hide();
					}
					$("#"+pathId).val(message.text());
					//响应回调方法
					if($.isFunction(callback)){
			           callback();
		       		 };
				}
				//IE下浏览器再次 绑定方法
				if (isIE()) {
					$("#"+attachmentId).bind('change', function() {
					  ajaxFileUpload(url,attachmentId,type,extNameId,displayNameId,showId,pathId,oldShowId,delId,valueId,callback,beforeDelete,deleteCallBack,showNameFlag,showDeleteButton,showFileButtion);
					});
					$("#"+delId).bind('click',function() {
						del(delId,showId,oldShowId,valueId,beforeDelete,deleteCallBack);
					});
				} 
			},
			error: function (data, status, e) {
				alert(data);
			}
		});
		return false;
	}  
	
	/**
	 * 点击删除按钮移除操作
	 * delClick
	 * @param {String} id 移除按钮id
	 * @param {String} showId 显示区域id
	 * @param {String} oldShowId 隐藏的显示区域id
	 */
	 function del(id,showId,oldShowId,valueId,beforeDelete,deleteCallback) {
	 	
	 	// 响应删除前方法
	 	var bool = true;
	 	// 回调函数不为空,并且 beforeDelete 是一个方法
		if($.isFunction(beforeDelete)){
	       bool = beforeDelete();
		};
	 	
		if (!bool) {
			return;
		}
	 	
	 	$("#"+valueId).val("false");
	 	$("#"+id).hide();
	 	var html = $("#"+showId).html();
	 	var oldhtml = $("#"+oldShowId).html();
	 	if (oldhtml != null || oldhtml != ""){
	 		 $("#"+oldShowId).html("");
	 		 $("#"+showId).hide();
	 	}
	 	
	 	//响应回调方法
		if($.isFunction(deleteCallback)){
	       deleteCallback();
		};
	 }
	 
	 function isIE() {
		 var flag = $.browser.msie;
		 if(!flag) {
			 var userAgentTmp = navigator.userAgent.toLowerCase();
			 flag = /Trident\/7/i.test(userAgentTmp);
		 }
		 return flag;
	 }

})(jQuery);