	/**
	 * 命名空间
	 */
	var EAPP;
	if(!EAPP){
		EAPP={};
	}
	
	/**
	 *工作流命名空间
	 */
	if(!EAPP.Workflow){
		EAPP.Workflow = {};
	}
	
	
	/**
	 * 工作流图形化命名空间
	 */
	EAPP.Workflow.Graphic = {};
	
	
	/**
	 * 扩展string 的trim 函数
	 */
	String.prototype.trim = function(){   
		//用正则表达式将前后空格   
		//用空字符串替代。   
		return   this.replace(/(^\s*)|(\s*$)/g,"");   
	};

	/**
 * EAPP Workflow,需要jQuery框架支持
 * version  1.0.1.0 
 * date 2008年9月23日
 * 本文档生成采用http://jsdoc.sourceforge.net/的生成工具
 */



	

	
/**
 * 构造一个工作流对象
 * @class 工作流对象
 * @type EAPPWorkflow
 * @return 返回工作流对象的一个新实例
 * @constructor 
 */
EAPP.Workflow.EAPPWorkflow = function(){

	/**
	 * @property version 当前版本号
	 */
	var version = "1.0.3.0";
	
	/**
	 * 获取当前版本号
	 * @type String 
	 * @return 返回当前版本号
	 */
	this.getVersion = function(){
		return version;
	};
	
	/**
	 * 当前的类型
	 * @type String
	 */
	this.typeName = "EAPP.Workflow.EAPPWorkflow";
	this.nodyType = "workflow";
	
	this.id = 0;
	
	// 全局ID从0开始编号
	var idIndex = 0;
	
	/**
	 * 设置全局Id索引
	 * @param {Number} index Id索引
	 */
	function setIdIndex(index){
		idIndex = index;
	};
	
	/**	
	 *  创建元素ID
	 * @member EAPPWorkflow
	 * @type Number
	 * @return 返回生成的全局元素ID
	 */
	this.createGUID = function (){
		idIndex++;
		return idIndex;
	};
	
	
	/**
	 * // 此处可以改为get方式访问
	 * @description 图元内部样式
	 * @property entityStyle
	 * @member EAPPWorkflow
	 */
	this.entityStyle = "<v:shadow on=\"t\" color=\"#364670\" opacity=\"52428f\" offset=\"1pt,1pt\"/>";
	this.entityStyle += "<v:fill type=\"gradient\" angle=\"45\"color=\"#ebedf4\" color2=\"#b4bdd6\" focus=\"100%\" />";
	
	/**
	 * // 此处可以改为get方式访问
	 * @description 小标的鼠标样式数组
	 * @property resizeFlagCursorStyles
	 * @memberOf EAPPWorkflow
	 */
	this.resizeFlagCursorStyles = new Array("se-resize", "n-resize", "ne-resize", "w-resize", "w-resize", "sw-resize", "s-resize", "se-resize", "crosshair", "crosshair");
	
	//存放图元类型的数组
	this.shapeTypeArray = new Array();
	
	this.shapePropertyPanels = new Array();
	
	//图元与xml元素节点映射
	this.xmlNodeMap = {
		"EAPP.Workflow.StrokeLine":"transition",
		"EAPP.Workflow.StartState":"start-node",
		"EAPP.Workflow.EndState":"end-node",
		"EAPP.Workflow.TaskNode":"task-node",
		"EAPP.Workflow.Node":"node",
		"EAPP.Workflow.Decision":"decision",
		"EAPP.Workflow.Fork":"fork",
		"EAPP.Workflow.Join":"join",
		"EAPP.Workflow.SubflowNode":"subflow-node",
		"EAPP.Workflow.EAPPWorkflow":"flow-definition"
	};
	
	/** 
	 * 注册图元类型
	 * @param {String} 要注册的图元类名()
	 */
	this.registerShapeType = function(shapeType){
		this.shapeTypeArray.push(shapeType);
		
		//如果当前结点对应的xml映射不存在
		if(!eval("this.xmlNodeMap['" + shapeType + "']")){
			for(var nodeTypeName in this.xmlNodeMap){
				//查找父类
				if(eval('new ' + shapeType + '() instanceof '+ nodeTypeName)){
					//添加到当前map对象
					var mapValue = eval("this.xmlNodeMap['"+ nodeTypeName +"']");
					eval("this.xmlNodeMap['" + shapeType + "']='" + mapValue +"';");
					break;
				}
			}
		}
	};
	
	


	
	//系统预注册图元类型
	this.registerShapeType("EAPP.Workflow.StrokeLine");
	//this.registerShapeType("EAPP.Workflow.StartState");
	//this.registerShapeType("EAPP.Workflow.EndState");
	this.registerShapeType("EAPP.Workflow.TaskNode");
	this.registerShapeType("EAPP.Workflow.Node");
	this.registerShapeType("EAPP.Workflow.Decision");
	this.registerShapeType("EAPP.Workflow.Fork");
	this.registerShapeType("EAPP.Workflow.Join");
	this.registerShapeType("EAPP.Workflow.SubflowNode");
	
	//父面板对象
	var parentPanel = null;
	//主容器面板
	var container = null;
	//顶部面板
	var headPanel = null;
	//主面板
	var mainPanel = null;
	
	//工具栏面板
	var toolsPanel = null;
	//图元选择面板
	var graphPanel = null;
	//工作区面板
	var workPanel = null; //工作区
	//属性面板
	var propertyPanel = null;
	//属性内容面板
	var propertyContentPanel = null;
	
	//当前工作流属性面板
	var workfowPropertyPanel = null;
	
	//工作区顶部面板
	var workHeadPanel = null;
	//工作区标题栏
	var workTitlePanel = null;
	//工作区内容面板
	var workContentPanel = null;
	
	//保存子结点的数组（包括图元、线、小标）
	var childNodes = null;
	//专门保存小标的数组
	var resizeFlagPoints = null;
	
	/**
	 * 通过Id获取子结点对象
	 * @param {String} nodeId 子结点对象的Id
	 * @type Object
	 * @result 返回获取的图元对象，取不到返回null
	 */
	this.getNodeById = function(nodeId){
		var result = null;
		//传入参数不正确或数组为空
		if (!nodeId || !childNodes || !childNodes.length) {return null;}
		//遍历查找
		for (var i=0; i<childNodes.length; i++) {
			var node = childNodes[i];
			if(node && nodeId == node.id){
				result = node;
				break;
			}
		}
		return result;//查找不到返回null
	};
	
	/**
	 * 添加子结点对象
	 * @param {Object} node 子结点对象
	 * @type Boolean
	 * @result 返回添加子结点对象是否成功
	 */
	this.addChildNode = function(node){
		if (!node) {return false;}
		try {
			node.render();//进行绘制
			if (!node.element) {return false;}//绘制失败
			childNodes.push(node);
			workContentPanel.append(node.element);//添加到工作区
			return true;
		}catch (e) {
			return false;
		}
	};
	
	
	//通过Id删除对象
	//@param id 对角的Id
	//@return true/false 删除是否成功
	this.deleteNodeById =function(id){ 
		if(!id){return false;}
	    for(var i=0;i<childNodes.length;i++){
	        if(childNodes[i].id == id){
	            childNodes.splice(i,1);//从工作区删除当前对象
	            return true;
	        }
	    }
	    return false;
	};
	
	
	function validateWorkflowConnect(){
		var result = true;
		//遍历查找
		for (var i=0; i<childNodes.length; i++) {
			var node = childNodes[i];
			if(node.nodeType == "shape" || node.nodeType == "line"){
				result = node.validConnect() && result ; 
			}
		}
		return result;
	};
	
	//开放外部访问
	this.validateWorkflowConnect = validateWorkflowConnect;
	
	//当前选择的图元类型
	var selectedShapeType = "";
	
		
	/**	
	 * 选择图元类型
	 * @param {String} selectTypeName 所选择的图元类型名称
	 */
	function selectShape(selectTypeName){
		// 选中对象，开始绘制
		selectedShapeType = selectTypeName ? selectTypeName : "";
		try {
			//设置工作区鼠标样式
			if (selectTypeName) {
				//document.getElementById("contentsPanel").style.cursor = "crosshair";
				workContentPanel.css("cursor","crosshair");
			}else{
				workContentPanel.css("cursor","default");
				// 图元选择为默认常规
				setToolItemOutChecked(document.getElementById("defaultCursor"));
			}
		}catch (e){
			//trace("选择图元类型出错！selectTypeName:" + selectTypeName + "  Error:" + e);
		}
	};
	
	
	//判断浏览器的类型
	var ie = document.all;
	var ns6 = document.getElementById && !document.all;
	
	//箭头拖过的图元
	var mouseOverShape = null;
	// 箭头拖过的小标(改变图元大小的小标)
	var mouseOverResizeFlag = null;
	
	//鼠标移出小标的填充颜色
	var mouseOutResizeFlagFillCor = "white";
	//鼠标移入小标的填充颜色
	var mouseOverResizeFlagFillCor = "yellow";
	
	// 最后被选中的标记ID（注：是10个标记中的一个），“线”根据此ID可以判断线的前与后端来做连接
	var lastFlagID = null;
	//当前是否可以拖动
	var isDragapproved = false;
		
	//上次鼠标坐标
	var lastMouseX = 0;
	var lastMouseY = 0;
	
	//上次相对工作区坐标
	var lastRelactiveX = 0;
	var lastRelactiveY = 0;
	
	//正在绘制的对象元素
	var drawingShapeElement = null;
	//选中的图形元素
	var checkedShapeElement = null;
	//选中的DOM元素（激发事件的元素）
	var checkedObjectElemnt = null;
	
	//this.basePath = "";
	
	/** 
	 * 工作流对象的初始化方法
	 */
	this.init = function(parentContainer){
		//父容器面板
		parentPanel = $(parentContainer);

		//初始化工作流属性面板
		initWorkflowProperty();

		//初始化工作流界面面板
		initPanel();
		
		//初始化界面样式
		initStyle();
		
		//初始化工作流数据
		initData();
	};
	
	/** 
	 * 初始化工作流界面面板
	 */
	function initPanel(){
		//清空父面板
		parentPanel.html("");
		
		var parentTable = $("<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" ><tr><td></td></tr></table>").appendTo(parentPanel)
		
		//添加容器面板(附加一个div到父面板中)
		container = $("<div/>").attr({
			width: "100%",
			height: "100%"
		}).appendTo($("td",parentTable)); //容器
		
		//添加tooltip面板
		$("<div/>").addClass("workflowToolTip").appendTo(container);
		
		
		//添加工作流头部面板
		headPanel = $("<div/>").addClass("workflowHead").appendTo(container);
		
		var workflowMinWidthPanel = $("<div/>").addClass("workflowMinWidth").appendTo(headPanel);	
			
		//工具栏面板
		toolsPanel = $("<div/>").addClass("workflowTools").appendTo(workflowMinWidthPanel);
		var logoHTML = "<div class=\"workflowLogo\"><img src=\"" + gEAPPWorkflow.basePath + "images/logo.png\" /></div>";//Logo
		var toolbarHTML = "<div class=\"workflowMenu\">"//菜单div
						+ "<div class=\"workflowNew\">"
					 // + "工作流引擎" + gEAPPWorkflow.getVersion()
						+ "</div>" //end workflowNew
						
						+ "<div class=\"workflowTy\">" //工具栏按钮
						+ "<ul>"
						//+ "<li cmdName=\"cutentity\"><img src=\"" + gEAPPWorkflow.basePath + "images/mCut.gif\" /></li>"
						+ "<li cmdName=\"copyentity\" onclick=\"gEAPPWorkflow.setEntityCommands('copyentity');\"><img src=\"" + gEAPPWorkflow.basePath + "images/mCopy.gif\" /></li>"
						+ "<li cmdName=\"parseentity\"onclick=\"gEAPPWorkflow.setEntityCommands('parseentity');\"><img src=\"" + gEAPPWorkflow.basePath + "images/mPaste.gif\" /></li>"
						+ "<li cmdName=\"delentity\"  onclick=\"gEAPPWorkflow.setEntityCommands('delentity');\" style=\"margin:0\"><img src=\"" + gEAPPWorkflow.basePath + "images/mDelty.gif\" /></li>"
						+ "</ul>" 
						+ "</div>"//end 工具栏按钮
						+ "</div>";//end workflowMenu
		
		var toolPanelHTML = logoHTML + toolbarHTML;
		toolsPanel.html(toolPanelHTML);
		
		//图元选择面板
		graphPanel = $("<div/>").addClass("workflowOperate").appendTo(workflowMinWidthPanel);
		var shapeMenuItemList = $("<ul/>").appendTo(graphPanel);
		//添加默认选择菜单项
		shapeMenuItemList.append("<li id=\"defaultCursor\" type=\"\"><img src=\"" + gEAPPWorkflow.basePath + "images/mSel01.gif\" /></li>");
		//动态添加图元
		for (var i=0; i<gEAPPWorkflow.shapeTypeArray.length; i++) {
			var shape = eval('new ' + gEAPPWorkflow.shapeTypeArray[i] + '()');
			var shapeHTML = "<li typeName=\"" + shape.typeName + "\"><img src=\"" + shape.imgSrc + "\" /></li>";
			shapeMenuItemList.append(shapeHTML);			
		}
		
		//补充注册开始与结束图元(除了显示在工具栏外)
		gEAPPWorkflow.registerShapeType("EAPP.Workflow.StartState");
		gEAPPWorkflow.registerShapeType("EAPP.Workflow.EndState");
		//注册工作流对象本身
		gEAPPWorkflow.registerShapeType("EAPP.Workflow.EAPPWorkflow");
		
		//工作区主面板
		mainPanel = $("<div/>").addClass("workflowMain").appendTo(container);
		
		//属性面板
		propertyPanel = $("<div/>").addClass("workflowProperty").appendTo(mainPanel);
		propertyPanel.html("<div class='wftit'>属性</div>");
		//属性内容面板
		//propertyContentPanel = $("<div/>").addClass("sxmb").text("属性内容").appendTo(propertyPanel);
		propertyContentPanel = $("<div/>").addClass("sxmb").appendTo(propertyPanel);
		
		//alert("gEAPPWorkflow.attributeArray.length " + gEAPPWorkflow.attributeArray.length  );
		
		//动态生成属性面板
		for (var typeIndex=0; typeIndex<gEAPPWorkflow.shapeTypeArray.length; typeIndex++) {
			var shape = null;
			if(gEAPPWorkflow.shapeTypeArray[typeIndex] == 'EAPP.Workflow.EAPPWorkflow'){
				shape = gEAPPWorkflow;//工作流对象只能采用单例
			}else{
				shape = eval('new ' + gEAPPWorkflow.shapeTypeArray[typeIndex] + '()');
			}
			
			//生成属性面板，并保存映射关系
			var htmlString = generatePropertyPanel(shape.typeName,shape,'');
		 	//添加到属性面板区
		 	var panel = $("<div/>").css("display","none").appendTo(propertyContentPanel);
			panel.html(htmlString);
			
			var shapePanelMap = new Object();
			shapePanelMap.typeName = shape.typeName;
			shapePanelMap.panel = panel;
			
	
			gEAPPWorkflow.shapePropertyPanels.push(shapePanelMap);			
		}					
				
		
		
		//工作区面板
		workPanel = $("<div/>").addClass("workflowArea").appendTo(mainPanel);
		//工作区头部面板
		workHeadPanel = $("<div/>").addClass("workflowAreaHead").appendTo(workPanel);
		//标题栏
		workTitlePanel = $("<div/>").addClass("workflowArea").appendTo(workHeadPanel);
		workTitlePanel.css({
			"text-overflow":"ellipsis",
			"white-space":"nowrap",
			"overflow":"hidden",
			"width":"500px"
		});
		
		//工作区内容面板
		workContentPanel = $("<div/>").addClass("workflowCon").appendTo(workPanel);
		workContentPanel.attr("id", "contentsPanel");
		
		//
		var workflowShadowPanel = $("<div/>").addClass("workflowShadow").appendTo(workContentPanel);
		
		//显示主面板
		container.show();
		
		//获取工作流的当前属性
		var nameValue = getAttributeValue(gEAPPWorkflow,"name");
		var stateValue = getAttributeValue(gEAPPWorkflow,"state");			
		//更新工作区的标题栏
		workTitlePanel.html("状态：" + stateValue + " 流程名称：" + nameValue).click(function(){
			//单击标题栏显示工作流属性
			gEAPPWorkflow.showPropertyPanel(gEAPPWorkflow);
			setPointResizerVisible(false);//隐藏选中图元的小标
		});	
		
		//显示工作流对象的属性面板
		gEAPPWorkflow.showPropertyPanel(gEAPPWorkflow);
		//附加小标到工作区面板
		//workContentPanel.html(optionflags);
	};
	
	
	//当前选中的图元菜单项
	var checkedShapeMenuItem = null;
	
	//工作区面板的DOM元素
	var contentPanelElement = null;
	//工作区面板的绝对位置
	var contentPanelAbsolutePositon = null;
	

	
	/**
	 * 当前正在编辑属性的Id
	 */
	var curPropertyEditShapeId = -1;
	
	var lastPropertyEditShapeId = -1;
	
	
	//校验为空的原则
	//1.如果值为空，恢复到默认值
	//2.如果值为空，alert提示,并返回输入框 focus
	
	this.updateAttributeValue = function(objInput){
		//var tagName = objInput.tagName;
		//trace("richfans:1");
		//获取更新的属性名称
		var inputId = objInput.id;
		if(arguments.length == 2){
			//如果当前并非处于编辑状态 // 修改shapes那边onblur事件的调用顺序
			//if(!objInput.contentEditable){return ;}
			inputId = arguments[1];
			
			var inputNameObj = document.getElementById(inputId);
			if(inputNameObj) inputNameObj.value = $(objInput).text(); 
			
		}
		
		var index = inputId.indexOf('#');
		var attributeName = inputId.slice(index+1);
		var shapeId = inputId.slice(0,index);//取得shapeId
		
		index = attributeName.indexOf('#');
		var typeName = attributeName.slice(0,index);//取得类型名称
		attributeName = attributeName.slice(index+1);
		

		//根据输入内容的不同处理属性值
		var attributeValue = null;
		if(objInput.tagName == "input" && objInput.type =="checkbox"){
			if(objInput.checked){
				attributeValue = true;
			}else{
				attributeValue = false;
			}; 
		}else if(objInput.tagName == "span" || objInput.tagName == "SPAN"){
			attributeValue = objInput.innerText;
		}else{
			attributeValue =objInput.value;
		}
		
		//如果值是字符串型，去除空格
		if(attributeValue && typeof(attributeValue) == "string"){
			attributeValue = attributeValue?attributeValue.trim():"";
		}
		
		//获取要更新的元素对象
		var shape = gEAPPWorkflow.getNodeById(shapeId);
		if(curPropertyEditShapeId == 0 || typeName == "EAPPWorkflow"){
			//如果是工作流对象，直接赋值
			shape = gEAPPWorkflow;
		}
				
		if(!shape){
			alert("shape can't be null updateAttributeValue 出错了!");
			////trace("curPropertyEditShapeId " + curPropertyEditShapeId);
			//throw "shape can't be null!";
			return false;
		}
		

		
		var attr = getAttribute(shape,attributeName);
		if(!attr){
			alert("查找图元" + shape +  "  " + shapeId  + "的属性" + attributeName + "出错！\n请联系技术支持人员！");
			return;
		}
				
		//验证非空属性
		if(attr && (!attr.isNull) && (!attributeValue)){
				alert(attr.getDisplayName() + "不能为空！");
				//trace(attr.getDisplayName() + "不能为空！");
			
			///**
			if ((attributeName == "name") && (typeName != "EAPP.Workflow.EAPPWorkflow")) {
				//恢复为原来的图元名称
				var oldShapeName = getAttributeValue(shape,"name");
				$('#td_editbox',shape.element).text(oldShapeName);
			}
			//**/
			showLastPropertyPanel(objInput);
			return false;
		}
		
		//如果当前更新名称 
		if((attributeName == "name") && (typeName != "EAPP.Workflow.EAPPWorkflow")){
			//验证名称的唯一性
			if(!checkShapeUniqueName(shape,attributeValue)){
				
				//恢复为原来的图元名称
				var oldShapeName = getAttributeValue(shape,"name");
				$('#td_editbox',shape.element).text(oldShapeName);
				
				showLastPropertyPanel(objInput);
				return false;
			}
			
			shape.text = attributeValue;//顺便更新一下text
			$('#td_editbox',shape.element).text(attributeValue);
		} 
		
		if(!attr || (typeof(attr.validType) == "undefine") || !attr.validType){
			alert("查找图元" + shape + "属性" + attributeName + "的validType出错！\n请联系技术支持人员！");
			return;
		}
		
		//调用相应函数，进行验证
		if(attr.validType == "method" && attr.validMethod){
			//调用验证方法
			var validResult = eval(attr.validMethod + "(objInput)");
			if(!validResult){//验证失败返回
				showLastPropertyPanel(objInput);
				return false;
			}
		}
		
		//如果属性值与原来的属性值相等
		if(attributeValue == attr.getValue(true)){return false;}
		
		////trace("this.updateAttributeValue curPropertyEditShapeId:"+ curPropertyEditShapeId +" shape:" + shape + " attributeName:" + attributeName + "\t inputId: " + inputId + " attributeValue:" + attributeValue);
		//更改属性
		alterAttribute(shape,attributeName,attributeValue);
		if((typeName == "EAPP.Workflow.EAPPWorkflow") && (attributeName == "name" )){
			//更新工具栏
			
			//获取工作流的当前属性
			var nameValue = getAttributeValue(shape,"name");
			var stateValue = getAttributeValue(shape,"state");			
			//更新工作区的标题栏
			workTitlePanel.html("状态：" + stateValue + " 流程名称：" + nameValue);			
		}
		
		//已经修改
		gEAPPWorkflow.hasChanged = true;
		
		//如果当前属性有onchange方法，执行相应的方法
		if(attr.valueChanagedMethod && attr){
			//alert("attr.valueChanagedMethod");
			eval(attr.valueChanagedMethod+ "(objInput)" );
		}
		//trace("alterAttribute(shape,attributeName,attributeValue);" + shape.id + "  "+ attributeName + "  "+ attributeValue);
	};
	
	/**
	 * 验证图元名称的唯一性
	 * @param {String} name
	 * @param {String} id
	 * @type {Boolean}
	 * @return 唯一返回true,否则返回false
	 */
	function checkShapeUniqueName(shape,attributeValue){
		
		//验证图元名称的唯一性
		if(shape.nodeType =="shape"){
			var shapeId = shape.id;
			
		    for(var i=0,len =childNodes.length ;i<len;i++){
				if(childNodes[i].nodeType !="shape"){continue;}
				 
		        if((childNodes[i].id != shapeId) && (getAttributeValue(childNodes[i],"name") == attributeValue)){
			   		alert("节点名称不能重复！名称\"" + attributeValue + "\"已存在！");
					return false;
		        }
		    }
		}else if(shape.nodeType == "line"){
			//验证线名称的唯一性
			//规则:验证线在双向图元中名称的唯一性
			
			var line = shape;
			var flag = false;
			
			if(line.fromShape){
				flag = line.fromShape.innerLine.validUniqueLineName("out",line.id,attributeValue);
				if(!flag){
					alert("图元: " + getAttributeValue(line.fromShape,"name") + "出线名称不能重复！\n名称:\"" + attributeValue + "\"的出线已存在！");
					return false;
				}
			}
			/**
			if(line.toShape){
				flag = line.toShape.innerLine.validUniqueLineName("in",line.id,attributeValue);
				if(!flag){
					alert( "线名称不能重复！名称\"" + attributeValue + "\"已存在！");
					return false;
				}
			}
			**/
		}
	 
		

		return true;
	};
	
	function showLastPropertyPanel(objInput){
		if(lastPropertyEditShapeId == -1){ return false;}
		var lastShape = gEAPPWorkflow.getNodeById(curPropertyEditShapeId);
		if(!lastShape){return false;}
		
		gEAPPWorkflow.showPropertyPanel(lastShape);
		
		checkedObjectElemnt = lastShape.element[0];
		checkedShapeElement = lastShape.element[0];
		setPointResizerVisible(false);
	
		//当前为当前图元	
		if(lastShape.typeName != "EAPP.Workflow.EAPPWorkflow"){
			setPointResizerVisible(true);
			resizePointForShape(lastShape.domElement);
		}
		
		//如果在图元上输入名称,则把图元文本区设置为可编辑状态
		if((objInput.tagName.toLowerCase() == "span") && (typeof(objInput.contentEditable)!= "undefined")){
			objInput.contentEditable = true;
		}
		
		objInput.focus();
		return false;
	};

	
	/**
	 * 显示相应图形的属性面板
	 */
	this.showPropertyPanel = function(shape){
				
		//遍历属性面板数组
		for (var i = 0; i < gEAPPWorkflow.shapePropertyPanels.length; i++) {
			var curPanel = gEAPPWorkflow.shapePropertyPanels[i];
			
			
			//如果是当前图形，则显示相应的属性面板
			if(curPanel.typeName == shape.typeName){
				
				//任何图元都重新绘制
				/**
				if((curPropertyEditShapeId == shape.id) && (!(shape instanceof EAPP.Workflow.Decision)) && (!(shape instanceof EAPP.Workflow.Fork))){
					return;//如果当前正处于编辑状态返回
				}
				*/
				//trace("before change:lastID=" + lastPropertyEditShapeId + ",curID=" + curPropertyEditShapeId);
				lastPropertyEditShapeId = curPropertyEditShapeId;								
				//保存正在编辑的Id
				curPropertyEditShapeId = shape.id;
				//trace("after change:lastID=" + lastPropertyEditShapeId + ",curID=" + curPropertyEditShapeId);
				//if (lastPropertyEditShapeId == curPropertyEditShapeId) {
				//	trace("///---------------------------------------");
				//}
				
				
				//如果是条件图元，则每次的属性面板都是重新生成
				//因为不同条件图元，属性也可能不同
				//if(shape instanceof EAPP.Workflow.Decision){
				var htmlString = generatePropertyPanel(shape.typeName,shape,'');
				curPanel.panel.html(htmlString);
				//}
				
				curPanel.panel.show();
				setAttributePanelValue(shape.typeName,shape,'');
				
/*				
				var lastShape = gEAPPWorkflow.getNodeById(lastPropertyEditShapeId);
				if(lastShape && lastPropertyEditShapeId != shape.id){
					try{
						innerTextBox = $('#td_editbox',lastShape.element);
						innerTextBox.blur();
					}catch(e){
					}
				}
*/
			}else{//否则隐藏
				curPanel.panel.hide();
			}
		}
	};
	
	/**
	 * 设置属性值
	 * 把图元的属性赋给属性面板
	 * @param {String} typeName 
	 * @param {Object} object
	 * @param {String} preAttributeName
	 */
	function setAttributePanelValue(typeName,object,preAttributeName){
		
		var attributeArray = object.attributeArray;
		if(null == attributeArray || attributeArray.length == 0){ return;}	
		
		for(var i=0; i<attributeArray.length; i++){
			var attr = attributeArray[i];			
						
			//如果当前属性无效或不可见，则跳过
			if(!attr.isEnable //当前属性无效，直接跳过
				//当前属性不可见，且子属性为空
				|| ((attr.attributeArray == null || attr.attributeArray.length ==0) && !attr.isVisible)){
					continue;
			}
			
			
			//获取当前属性的访问名称
			var depthName = preAttributeName + '#' + attr.attributeName ;
			if(preAttributeName == ''){
				depthName = object.id + "#" + typeName + '#' + attr.attributeName;//如果是第一级，前面加上类型名称 以保持唯一性
			}
			
			
			//如果当前属性可见,则设置其属性值
			if(attr && attr.isVisible){
						
				//获取属性值
				var attr_value = attr.getValue();
				//获取输入对象
				var inputObject = document.getElementById(depthName);
				//if(!inputObject){alert("设置属性出错" +  depthName + "的输入项不存在！");continue;}
				//如果输入项存在
				if (inputObject) {
					//根据输入的类型生成不同的td
					switch (attr.inputType) {
						case 'input_text': //输入框
						case 'input_textarea': //文本区
						case 'html'://HTML 自定义方式
							$(inputObject).val(attr_value);
							break;
							
						case 'input_select'://下拉框
							//如果没有值，则默认为第一项
							if (!attr_value && typeof(inputObject.options) != "undefined" && inputObject.options) {
								inputObject.selectedIndex = 0;
								attr.value = inputObject.options[0].value;
								break;
							}
							$(inputObject).val(attr_value);
							break;
							
						case 'input_checkbox'://单选框
							attr_value = attr_value ? true : false;
							$(inputObject).val(attr_value);
							break;
						case 'readonly'://只读
							$(inputObject).text(attr_value);
							break;
						case 'none':
							break;
					}
				}
				
				//如果当前属性有onchange方法，执行相应的方法
				if(attr.valueChanagedMethod && inputObject){
					eval(attr.valueChanagedMethod+ "(inputObject)" );
				}
			
			}//end 设置当前属性值
			
			//如果有子属性，那么生成子属性
			if(attr.attributeArray != null &&  attr.attributeArray.length>0){
				setAttributePanelValue(typeName,attr,depthName);
			}
		}
	};

	/**
	 * 操作者类型下拉框change响应
	 * @param {Object} objInput
	 */
	function bingdingTypeChanage(objInput){
		if(!objInput){return false;}
		
		var inputId = objInput.id;
		
		var index = inputId.indexOf('#');
		var attributeName = inputId.slice(index+1);
		var shapeId = inputId.slice(0,index);//取得shapeId
		
		index = attributeName.indexOf('#');
		var typeName = attributeName.slice(0,index);//取得类型名称
		attributeName = attributeName.slice(index+1);
		
		var view_binding_expression_id = shapeId + "#" + typeName+ "#task#view-binding#expression";
		var class_id = shapeId + "#" + typeName+ "#task#view-binding#class";
		
		var expressionObj = document.getElementById(view_binding_expression_id);
		var classObj = document.getElementById(class_id);
		
		if(objInput.value =="expression"){
			$(expressionObj).parent().parent().show();
			$(classObj).parent().parent().hide();
			$(classObj).val("");//2008年12月2日19时23分04秒 清空未选择的属性
			
		}else if(objInput.value == "program"){
			$(classObj).parent().parent().show();
			$(expressionObj).parent().parent().hide();
			$(expressionObj).val("");//2008年12月2日19时23分04秒 清空未选择的属性
		}
	};
	
	/**
	 * 操作者类型下拉框change响应
	 * @param {Object} objInput
	 */
	function operatorTypeChanage(objInput){
		if(!objInput){return false;}
		
		var inputId = objInput.id;
		var index = inputId.indexOf('#');
		var attributeName = inputId.slice(index+1);
		var shapeId = inputId.slice(0,index);//取得shapeId
		
		index = attributeName.indexOf('#');
		var typeName = attributeName.slice(0,index);//取得类型名称
		attributeName = attributeName.slice(index+1);
		
		var operator_user_id = shapeId + "#" + typeName+ "#task#assignment#operator-user";
		var operator_role_id = shapeId + "#" + typeName+ "#task#assignment#operator-role";
		var class_id = shapeId + "#" + typeName+ "#task#assignment#class";
		var operator_single_user_id = shapeId + "#" + typeName+ "#task#assignment#operator_single_user";
		var operator_expression_id = shapeId + "#" + typeName+ "#task#assignment#operator_expression";
		
		var sigleUserObj = document.getElementById(operator_single_user_id);
		var operatorUserObj = document.getElementById(operator_user_id);
		var operatorRoleObj = document.getElementById(operator_role_id);
		var classObj = document.getElementById(class_id);
		var expressionObj = document.getElementById(operator_expression_id);
		
		//选项与相应的输入框Map
		var inputMap = {
			"single-user":sigleUserObj,
			"user":operatorUserObj,
			"role":operatorRoleObj,
			"program":classObj,
			"expression":expressionObj
		};
		

		changeInputList(inputMap,objInput);
	};
	
	/**
	 * 响应Handler类型
 	 * @param {Object} objInput
	 */
	function handlerTypeChange(objInput){
		if(!objInput){return false;}
		
		var inputId = objInput.id;
		var index = inputId.indexOf('#');
		var shapeId = inputId.slice(0,index);//取得shapeId

		//取得类型名称		
		var attributeName = inputId.slice(index+1);
		index = attributeName.indexOf('#');
		var typeName = attributeName.slice(0,index);
		attributeName = attributeName.slice(index+1);
		
		var attr_class_id = shapeId + "#" + typeName + "#handler#class";
		var attr_expression_id = shapeId + "#" + typeName + "#handler#expression";
		
		var att_class_obj = document.getElementById(attr_class_id);
		var att_expression_obj = document.getElementById(attr_expression_id);
		
		//选项与相应的输入框Map
		var inputMap = {
			"program":att_class_obj,
			"expression":att_expression_obj
		};
		
		changeInputList(inputMap,objInput);
	}
	
	
	function changeInputList(inputMap,objInput){
		for(var item in inputMap){
			var inputTextObj = eval("inputMap[\"" + item + "\"]");
			var tr = $(inputTextObj).parent().parent().show()
			if(item.toString() == objInput.value.toLowerCase()){
				tr.show();
			}else{
				tr.hide();
				$(inputTextObj).val("");
			}
		}
	}
	
	/** 
	 * 初始化界面样式
	 */
	function initStyle(){
		
		//获取工作区元素的绝对位置
		contentPanelElement = document.getElementById('contentsPanel');
		contentPanelAbsolutePositon = getAbsoluteLocationEx(contentPanelElement);
		
		
		//工具栏菜单样式
		$("li", toolsPanel).each(function(i, item){
			$(item).mouseover(function(){
				$(item).addClass('over');
			}).mouseout(function(){
				$(item).removeClass('over');
			}).click(function(){
				//setEntityCommands($(item).attr('cmdName')); // 响应工具栏菜单事件
			});
		});
		
		//绑定图元菜单事件
		$("li", graphPanel).each(function(i, item){
			$(item).mouseover(function(){
				$(item).addClass('over');
			}).mouseout(function(){
				if(checkedShapeMenuItem != item){$(item).removeClass('over');}
			}).click(function(){
				setToolItemOutChecked(item);
				selectShape($(item).attr('typeName'));//选中当前图元类型
			});
		});
	};
	
	
	/** 
	 * 选中图元菜单项
	 * @param {Element} 当前选中的菜单项
	 */
	function setToolItemOutChecked(imgToolItem){
		if (checkedShapeMenuItem) {
			$(checkedShapeMenuItem).removeClass('over');
		}
		$(imgToolItem).addClass('over');
		checkedShapeMenuItem = imgToolItem;
	};
	
	/** 
	 * 获取鼠标相对工作区面板的左偏移
	 */
	function getRelativeLeft(mouseX){
		if(!contentPanelAbsolutePositon){return 0;}
		//获取工作区元素的绝对位置
		var offsetLeft = Number(contentPanelAbsolutePositon.absoluteLeft);
		//计算相对左边偏移
		var leftX = Number(mouseX) + Number(contentPanelElement.scrollLeft) + Number(document.body.scrollLeft) - Number(offsetLeft);
		return leftX;
	};
	
	/** 
	 * 获取鼠标相对工作区面板的上偏移
	 */
	function getRelativeTop(mouseY){
		if(!contentPanelAbsolutePositon){return 0;}
		//获取工作区元素的绝对位置
		var offsetTop = Number(contentPanelAbsolutePositon.absoluteTop);
		//计算相对顶部偏移
		var topY = Number(mouseY) + Number(contentPanelElement.scrollTop) + Number(document.body.scrollTop) - Number(offsetTop);
		return topY;
			 	
	};
	
	/** 
	 * 获取Html元素在页面中的绝对位置
	 * http://www.cnblogs.com/birdshome/archive/2005/01/04/86507.html
	 * @param {Element} 要获取绝对位置的DOM元素
	 */
	function getAbsoluteLocationEx(element){
		if (arguments.length != 1 || element == null) {
			return null;
		}
		var elmt = element;
		var offsetTop = elmt.offsetTop;
		var offsetLeft = elmt.offsetLeft;
		var offsetWidth = elmt.offsetWidth;
		var offsetHeight = elmt.offsetHeight;
		while ((elmt = elmt.offsetParent)) {
			// add this judge 
			if (elmt.style.position == 'absolute' || elmt.style.position == 'relative' ||
			(elmt.style.overflow != 'visible' && elmt.style.overflow != '')) {
				break;
			}
			offsetTop += elmt.offsetTop;
			offsetLeft += elmt.offsetLeft;
		}
		return {
			absoluteTop: offsetTop,
			absoluteLeft: offsetLeft,
			offsetWidth: offsetWidth,
			offsetHeight: offsetHeight
		};
	};
	
	/** 
	 *  工作区选取事件
	 */
	function getSelectStartEnabled(){
		var targetElement = event.srcElement ;
		if((targetElement.tagName.toLowerCase()== "input" && targetElement.type.toLowerCase() == "text" ) //输入框
			 || targetElement.tagName.toLowerCase()== "textarea"  //文本区
			 || (targetElement.tagName.toLowerCase()== "span"  && targetElement.contentEditable == "true")){ //图元内嵌编辑
			return true;
		}
		return false;
	};

	/** 
	 *  工作区上下文菜单事件
	 */
	function getContextMenuEnabled(){
		var targetElement = event.srcElement ;
		if((targetElement.tagName.toLowerCase()== "input" && targetElement.type.toLowerCase() == "text" ) //输入框
			 || targetElement.tagName.toLowerCase()== "textarea"  //文本区
			 || (targetElement.tagName.toLowerCase()== "span"  && targetElement.contentEditable == "true")){ //图元内嵌编辑
			return true;
		}
		return false;
	};
	
	/**
	 * 属性数组 
	 */
	this.attributeArray = new Array();
		
	/***
	 * 初始化工作流属性
	 */	
	function initWorkflowProperty(){
		
		var attr_workflow = new EAPP.Workflow.Graphic.Attribute.Workflow();
		
		//名称属性
		var attr_name = getAttribute(attr_workflow,"name");
		attr_name.isNull = false;//可为空
		attr_name.value = '新建流程';//默认值
		
		/**
		 * flow-key属性
		 * 流程Key（36位ID）（只读）
		 * 隐藏
		 * 由流程引擎生成
		 * 新建时默认为空，修改时从配置文件加载
		 */
		var attr_flow_key = getAttribute(attr_workflow,"flow-key");
		attr_flow_key.isEnable = true;
		attr_flow_key.isVisible = false;
		
		//分类属性
		//（目前不可用）
		var attr_category = getAttribute(attr_workflow,"category");
		attr_category.isEnable = false;
		
		//状态属性
		/**
		 * 状态属性
		 * 下拉框选择
		 * 取值：未发布 | 启用 | 禁用[NO_ENABLE | ENABLED | DISABLED]
		 */
		var attr_state = getAttribute(attr_workflow,"state");
		attr_state.displayValue = "未发布";
		attr_state.value = 'NO_ENABLE';//默认值
		attr_state.inputType = 'readonly';
		
		//initial属性
		//暂时不支持
		var attr_initial = getAttribute(attr_workflow,"initial");
		attr_initial.isEnable = false;
		
		//版本属性
		/**
		 * 版本属性
		 * long类型（只读）
		 * 隐藏
		 * 由流程引擎生成
		 * 新建时默认为空，修改时从配置文件加载
		 */
		var attr_version = getAttribute(attr_workflow,"version");
		attr_version.isVisible = false;
		
		//流程启动事件动作
		var flow_start_action = getAttribute(attr_workflow,'event_flow_start#action');
		flow_start_action.isHideHeader = true;
		//设置action的name属性
		var flow_start_action_name = getAttribute(flow_start_action,"name");
		flow_start_action_name.value = "流程启动动作";
		flow_start_action_name.isVisible = false; 
		
		//流程结束事件动作
		var flow_end_action = getAttribute(attr_workflow,'event_flow_end#action');
		flow_end_action.isHideHeader = true;
		//设置action的name属性
		var flow_end_action_name = getAttribute(flow_end_action,"name");
		flow_end_action_name.value = "流程结束动作";
		flow_end_action_name.isVisible = false; 
		
		//添加到工作流属性数组
		gEAPPWorkflow.attributeArray = attr_workflow.attributeArray;
	};
	
	
	this.initWorkflowProperty = initWorkflowProperty;
		
	
	/** 
	 *  初始化工作流数据
	 */
	function initData(){
		//初始化数组对象	
		childNodes = new Array();

 		//小标数组
		resizeFlagPoints = new Array();
		
		//添加 10个小标对象
		for (var i = 0; i < 10; i++) {
			var newFlag = new EAPP.Workflow.Flag(i);
			gEAPPWorkflow.addChildNode(newFlag);
			resizeFlagPoints.push(newFlag.domElement);
		};
		
		//重新获取一遍
		resizeFlagPoints = getResizePoints();
		
		//隐藏所有的小标
		setPointResizerVisible(false);
		setPointResizerForLineVisible(false);
		
		//全局ID索引重置为0
		setIdIndex(0);
		//当前选择的图元类型
		
		selectedShapeType = "";
		//设置图元类型为空
		selectShape(null);
		
		
		//鼠标经过的图元
		mouseOverShape = null;
		//鼠标经过的小标
		mouseOverResizeFlag = null;
		
		//设置 最后被选择的标记点
		lastFlagID = null;
		//当前是否可拖拽
		isDragapproved = false;
		
		//上次鼠标坐标
		lastMouseX = 0;
		lastMouseY = 0;
		
		//上次相对工作区坐标
		lastRelactiveX = 0;
		lastRelactiveY = 0;
		
		//正在绘制的对象元素
		drawingShapeElement = null;
		//选中的图形元素
		checkedShapeElement = null;
		//选中的DOM元素（激发事件的元素）
		checkedObjectElemnt = null;
		
		
		
		//添加到当前子对象数组(将工作流对象本身添加到子对象数组) 
		//childNodes.push(gEAPPWorkflow);
		
		
		//添加"开始"图元与"结束"图元
		var startNode = new EAPP.Workflow.StartState();
		var endNode = new EAPP.Workflow.EndState();
		gEAPPWorkflow.addChildNode(startNode);
		gEAPPWorkflow.addChildNode(endNode);
		
		//定位
		startNode.element.css({top:"50px",left:"300px"});
		endNode.element.css({top:"400px",left:"300px"});
		
		
		//初始化工作区事件
		initWorkSpaceEvent();

		//workContentPanel.click(onClick);
		
		//已经修改
		gEAPPWorkflow.hasChanged = false;
	};
	
	
	/**
	 * 初始化工作区事件
	 * 2008年11月29日14时00分06秒 添加
	 */
	function initWorkSpaceEvent(){
		
		//绑定工作区选取事件与上下文菜单  //屏蔽工作区选取行为与IE默认右键菜单
		workContentPanel.bind('selectstart',getSelectStartEnabled);
		workContentPanel.bind('contextmenu',getContextMenuEnabled);
		
		//绑定工作区事件
		workContentPanel.mousedown(onMousedown);
		workContentPanel.mousemove(onMousemove);
		workContentPanel.mouseup(onMouseup);
		workContentPanel.keydown(onKeydown);
		
		//窗口关闭或刷新时，响应的未保存提醒
		window.attachEvent("onbeforeunload",function(){
			gEAPPWorkflow.unloadWindow();
		});
	};

	
	/**	
	 * 响应工作区鼠标按下的事件
	 * @param {Event} 事件对象
	 */
	function onMousedown(e){
		
		//如果当前浏览器即不是IE或ns6则返回 
		if (!ie && !ns6) {return true;}
		
		//引发事件的对象
		var actionObject = ns6 ? e.target : event.srcElement;
		var topelement = ns6 ? "HTML" : "BODY";
		//当前鼠标坐标
		mouseX = ns6? e.clientX: event.clientX;
		mouseY = ns6? e.clientY: event.clientY;
		
		//不是页面主要元素，冒泡到上级元素响应
		while (actionObject.tagName != topelement && actionObject.className != "workflowCon"//页面或工作区顶级对象
 		&&	actionObject.className != "drag" &&	actionObject.className != "line" //图元或连接线对象
 		&&	actionObject.className != "resizePoint" ){//调整小标
			actionObject = ns6 ? actionObject.parentNode : actionObject.parentElement;
		}
		
		//获取选中元素的对象
		var actionNode = gEAPPWorkflow.getNodeById(actionObject.id);
		
		//按下鼠标左键
		if (event.button == 1) {
			//隐藏全部小标
			//2008年11月28日 TD343 修改为任何时候都不隐藏当前图元 
			//setPointResizerVisible(false); // 先初始化为不可见
			setPointResizerForLineVisible(false); // 初始化为不可见
			
			
			//选中某个图元类型
			if (selectedShapeType) {
				//判断引发对象是否处在工作区中
				if (actionObject.id != "contentsPanel" && actionObject.className != "resizePoint") {
					return true;
				}
				
				//可以拖拽
				isDragapproved = true;
				
				//创建新对象
				var newShape = eval('new ' +selectedShapeType + "()");
				//添加到当前工作区
				gEAPPWorkflow.addChildNode(newShape);
				//所选图元类型置空
				selectShape(null);
				
				// 设置正在绘制的对象
				drawingShapeElement = newShape.domElement;
				//drawingShapeElement = document.getElementById(newShape.id);
				
				// 绘制图元对象
				if (newShape.nodeType == "shape") {
					
					//根据当前鼠标坐标进行定位
					var shapeWidth = filterPX(drawingShapeElement.style.width);
					var shapeHeight = filterPX(drawingShapeElement.style.height);
					var leftX = getRelativeLeft(mouseX) - Number(shapeWidth)/2;
					var topY  = getRelativeTop(mouseY) - Number(shapeHeight)/2;
					//刷新位置
					//newShape.element.css({left:leftX,top:topY});
					drawingShapeElement.style.left = leftX;
					drawingShapeElement.style.top = topY;
					 
					
					lastFlagID = "p7"; // 设置 最后被选择的标记点
					
					//显示小标
					setPointResizerVisible(true);
					//调整图元对象小标
					resizePointForShape(drawingShapeElement);
					
				}else if (newShape.nodeType == "line") {
						// 绘制连接线
						setPointResizerForLineVisible(true);//显示线的小标
						drawingShapeElement.from = getRelativeLeft(mouseX) + "," + getRelativeTop(mouseY);
						drawingShapeElement.to = (parseInt(getRelativeLeft(mouseX)) + 30) + "," + (parseInt(getRelativeTop(mouseY))+ 30);
						
						
						lastFlagID = "p9";// 设置 最后被选择的标记点
						
						//当前鼠标在图元图象上，并且经过某一结点
						if (mouseOverShape != null && mouseOverResizeFlag != null) {
							////trace("onMousedown AddIntityRel");
							// 如果开始绘制线头时，线头的位置在某个图元的相应位置上时，则设置其关联
							AddIntityRel(mouseOverShape.id, mouseOverResizeFlag.id, drawingShapeElement.id, "p8");//添加出站线
							SetIntityRels(mouseOverShape);//修正当前图元的连接线
						}
				}//end drawing line
				
				checkedObjectElemnt = drawingShapeElement;//设置当前选中元素
				checkedShapeElement = drawingShapeElement;//设置当前选中图形
				
				//保存临时坐标
				lastRelactiveX = parseInt(checkedObjectElemnt.style.left + 0);
				lastRelactiveY = parseInt(checkedObjectElemnt.style.top + 0);
				lastMouseX = mouseX;
				lastMouseY = mouseY;
				
				//已经修改
				gEAPPWorkflow.hasChanged = true;
				
				//手工触发click事件以编辑属性
				newShape.element.click();
					
				// 释放当前选中的元素类型
				
			//end 结束绘制对象
			}else if (actionNode && actionNode.nodeType == "shape"){
				//修改/移动对象
				
				isDragapproved = true;
				checkedObjectElemnt = actionObject;//设置当前选中元素
				checkedShapeElement = actionObject;//设置当前选中图形
				
				//保存临时坐标
				lastRelactiveX = parseInt(checkedObjectElemnt.style.left + 0);
				lastRelactiveY = parseInt(checkedObjectElemnt.style.top + 0);
				lastMouseX = mouseX;
				lastMouseY = mouseY;
				//调整图元小标
				setPointResizerVisible(true);
				resizePointForShape(checkedShapeElement);
			}else if(actionNode && actionNode.nodeType == "line"){
				//修改/移动线
							
				isDragapproved = true;
				checkedObjectElemnt = actionObject;//设置当前选中元素
				checkedShapeElement = actionObject;//设置当前选中图形
				
				//显示并调整线的小标
				setPointResizerForLineVisible(true);				
				//resizePointForLine(actionObject);
				resizePointForLineSimple(actionObject);
				
				//保存临时坐标
				lastRelactiveX = parseInt(checkedObjectElemnt.style.left + 0);
				lastRelactiveY = parseInt(checkedObjectElemnt.style.top + 0);
				lastMouseX = mouseX;
				lastMouseY = mouseY;
				 
				setPointResizerVisible(false);//隐藏图元的小标
				
			}else if(actionNode && actionNode.nodeType == "flag"){
				//单击小标

				
				lastFlagID = actionObject.id;// 设置 最后被选择的标记点
				
				if (actionObject.id == "p8" || actionObject.id == "p9") {
					// 选择了连接线的小标
					setPointResizerForLineVisible(true);
				}else {
					// 选择了图元对象的小标
					setPointResizerVisible(true);
				}
				
				isDragapproved = true;
				checkedObjectElemnt = actionObject;//设置当前选中元素
				
				//保存临时坐标
				lastRelactiveX = parseInt(checkedObjectElemnt.style.left + 0);
				lastRelactiveY = parseInt(checkedObjectElemnt.style.top + 0);
				lastMouseX = mouseX;
				lastMouseY = mouseY;
				
			}else if(actionObject.className == "workflowCon" && !selectedShapeType){
				return true;
			}else{
				//空白，不处理
			};
	
		}else if(event.button == 2){
			//按下右键
			
			if(actionObject.className == "drag"){
				document.focus();
				checkedObjectElemnt = actionObject;//设置当前选中元素
			}else{
				
			}
			return;
	}
};
	 
	/** 
	 * 响应工作区鼠标移动事件	
	 * @param {Event} 事件对象
	 */
	function onMousemove(e){		
		
		//获取选中结点对象
		var checkedNode = null;
		if(checkedObjectElemnt){checkedNode = gEAPPWorkflow.getNodeById(checkedObjectElemnt.id);}
		
		//保存临时的x与y坐标
		var lcursorX;
		var lcursorY;
		
		//如果当中有选中可拖拽的对象
		if(isDragapproved){			
			
			//获取当前鼠标光标相对工作区坐标
			lcursorX = lastRelactiveX + (event.clientX - lastMouseX);
			lcursorY = lastRelactiveY + (event.clientY - lastMouseY);
			
			//当前正在绘制线
			if(drawingShapeElement && checkedNode && checkedNode.nodeType == "line"){
				
				//已经修改
				gEAPPWorkflow.hasChanged = true;
				
				//绘制对象时，移动的大小
				var changedWidth  =  lcursorX - checkedObjectElemnt.style.left.replace("px","");
				var changedHeight =  lcursorY - checkedObjectElemnt.style.left.replace("px","");
				
				//正绘制的线元素
				var drawingLineElement = checkedObjectElemnt;
				
				var toX = getRelativeLeft(event.clientX);
				var toY = getRelativeTop(event.clientY);
				drawingLineElement.to = toX + "," + toY;
				
				overResizeFlag = null;//鼠标经过的标志设为空
				resizePointForLine(drawingLineElement);//调整线的小标
			
			//end 绘制线	
			}else if (checkedNode && checkedNode.nodeType == "shape") {
				//移动图元对象   
				
				 var innerTextBox = $('#td_editbox',checkedObjectElemnt)[0];
				//编辑时不能移动
				if (innerTextBox && innerTextBox.contentEditable == "true") { return true;} 
				
				try {
				 	checkedObjectElemnt.style.left = lcursorX;
					checkedObjectElemnt.style.top = lcursorY;
					resizePointForShape(checkedObjectElemnt);// 移动对象时，同时移动设置大小标记
					SetIntityRels(checkedObjectElemnt); // 设置图元的关联
					
					//已经修改
					gEAPPWorkflow.hasChanged = true;
				} 
				catch (e) {
					alert("移动图元发生错误！" + e);					
 					return false;
				}
				 

				// 处于非编辑状态
			}else if (checkedNode && checkedNode.nodeType == "line") {
					//屏蔽直接拖拽线的功能
					
					/**
					//选中的连接线元素
					var checkedLineElement = checkedObjectElemnt;
					// 移动连接线
					checkedLineElement.style.left = lcursorX;
					checkedLineElement.style.top = lcursorY;
					//调整线的小标
					resizePointForLine(checkedLineElement);
					
					// 当移动连接线时，删除所有两边所关联的图元
					RemoveRelByLineFlag(checkedLineElement, "p8");
					RemoveRelByLineFlag(checkedLineElement, "p9");
					*/
			}else if (checkedNode && checkedNode.nodeType == "flag") {
					// 正在移动小标
					
					//设置当前选中图元的所有关联线
					SetIntityRels(checkedShapeElement); // 此处如果是连接线的话，有可能引发错误
					
					//调整选中结点对象的大小
					resizeNode(checkedShapeElement, checkedObjectElemnt, lcursorX, lcursorY);
					//已经修改
					gEAPPWorkflow.hasChanged = true;
					
					return true;
			}//end the condition
			return true;
			
		//end if(isDragapproved)	
		}else {

			//如果当前要绘制连接线
			if (selectedShapeType == "EAPP.Workflow.StrokeLine") {// 此处的连接线类型名称不应当写死
				
				//设置鼠标经过的图元小标样式
				setMouseOverIntityStyle(false, false);
			}
		}//end outer if 
		
	};//end onMousemove(e)
	 
	this.onMousemove = onMousemove;

	/**	
	 *  响应工作区鼠标松开事件
	 * @param {Event} 事件对象
	 */
	function onMouseup(e){
		
		/**
		if(!drawingShapeElement && ( ($('#td_editbox',checkedShapeElement)[0].contentEditable == "false"))){
			//gEAPPWorkflow.showPropertyPanel(gEAPPWorkflow);//显示工作流对象的属性
		}
		**/
		var actionObject = ns6 ? e.target : event.srcElement;
		//松开左键
		if (event.button == 1) {
			isDragapproved = false;
			drawingShapeElement = null;
			
			
			//设置图元的最小长宽
			var minWidth = 80;
			var minHeight = 40;
			var checkedNode = null;
			if(checkedShapeElement){checkedNode = gEAPPWorkflow.getNodeById(checkedShapeElement.id);}
			if(checkedNode && checkedNode.nodeType == "shape" && typeof(checkedShapeElement.style) != "undefined"){
					//如果小于最小值长宽，则自动调整大小
					if(parseInt(checkedShapeElement.style.width) < minWidth){checkedShapeElement.style.width = minWidth + 'px';}
					if(parseInt(checkedShapeElement.style.height) < minHeight){checkedShapeElement.style.height = minHeight + 'px';}
					resizePointForShape(checkedShapeElement);// 移动对象时，同时移动设置大小标记
					SetIntityRels(checkedShapeElement); // 设置图元的关联
					
					//已经修改
					gEAPPWorkflow.hasChanged = true;
			}
			
			
			//将当前设置为未选中对象
			selectShape(null);
			
			//mouseOverShape = getMouseOverIntity();
			//mouseOverResizeFlag = getMouseOverResizerFlag();
			
			//如果此时鼠标在图元对象的某一结点上，进行关联设置
			if (mouseOverShape != null && mouseOverResizeFlag != null) { // 判断状态设置关联
				var checkedLineElement = checkedShapeElement; // 刚好当前选中的是线
				var resizeLineFlagID = actionObject.id; // p8~p9
				if (actionObject.className != 'resizePoint' && actionObject.className == 'line' && (actionObject.id != "p8" || actionObject.id != "p9")) {
					//alert("请拖动连接线两头的圆圈小标才能建立关联！");
					setMouseOverIntityStyle(true);
					return false;
				}
				
				//
				setMouseOverIntityStyle(true); // 强制设置为未选择对象
				
				var checkedLine = null;
				if(checkedShapeElement) checkedLine = gEAPPWorkflow.getNodeById(checkedShapeElement.id);
				
				//如果，当前小标是线并且为线对象
				if (checkedLine && (resizeLineFlagID == "p8" || resizeLineFlagID == "p9") && checkedLine && checkedLine.nodeType == "line") {
					////trace("onmouseup AddIntityRel:  (mouseOverShape.id " + mouseOverShape.id + ", mouseOverResizeFlag.id " + mouseOverResizeFlag.id + " , checkedLineElement.id" + checkedLineElement.id + ", resizeLineFlagID " + resizeLineFlagID); // 添加关联)
					AddIntityRel(mouseOverShape.id, mouseOverResizeFlag.id, checkedLineElement.id, resizeLineFlagID); // 添加关联
					//var lineObj = document.getElementById(lineId);
					//resizePointForLine(checkedShapeElement);
					SetIntityRels(mouseOverShape);//修正当前图元的连接线
					try {
						resizePointForLine(checkedLine.element[0]);
					}catch(e){}
					
					//已经修改
					gEAPPWorkflow.hasChanged = true;
					
					setMouseOverIntityStyle(true); // 强制设置为未选择对象
					mouseOverShape = null;//鼠标经过图元置空
					mouseOverResizeFlag = null;
				}
				
			}
			
			if (mouseOverShape) {
				mouseOverShape = null;
				setMouseOverIntityStyle(true);
			}// 强制设置为未选择对象
			
			
		    //验证线是否连接正常
			if(checkedNode && checkedNode.nodeType == "line"){
				checkedNode.validConnect();//验证线的连接是否正确
			}
		}//end if 松开左键
		//松开右键
		else if (event.button == 2) {
				// 将当前设置为未选中对象
				selectShape(null);
		}
				
	
		return true;

		//checkedShapeElement = null;
	};//end document.onmouseup
 

	// JScript 文件


 function onKeydown(e) {
 	//
	//获取内部文本框
    var innerTextBox = $('#td_editbox',checkedShapeElement)[0];
    switch (event.keyCode) {
        case 13:
            // 回车键
            if (checkedShapeElement != null) {
                ContextMenuItemClick("edit"); //开始编辑
            }
            break;
        case 46:
            // 删除键
			setEntityCommands("delentity"); 
            break;
        case 37:
            // 左键
            if (innerTextBox && innerTextBox.contentEditable == "true"){ return true;}
			var checkedNode = null;
			if(checkedShapeElement){checkedNode = gEAPPWorkflow.getNodeById(checkedShapeElement.id);}
			if(checkedNode && checkedNode.nodeType =="line"){return;}//禁止通过键盘移动线
			
			if(parseInt(checkedShapeElement.style.left) == 0){
				//alert("当前图元已在左边界，不能移动超过左边界！");
				return;
			}
			
            checkedShapeElement.style.left = filterPX(checkedShapeElement.style.left) - 10;
			//不能超过左边界
			checkedShapeElement.style.left = (parseInt(checkedShapeElement.style.left) < 0)? 0: checkedShapeElement.style.left;
			
            SetIntityRels(checkedShapeElement);
            resizePointForShape(checkedShapeElement);
            break;
        case 38:
            // 上键
         	if (innerTextBox && innerTextBox.contentEditable == "true"){ return true;}
			var checkedNode = null;
			if(checkedShapeElement){checkedNode = gEAPPWorkflow.getNodeById(checkedShapeElement.id);}
			if(checkedNode && checkedNode.nodeType =="line"){return;}//禁止通过键盘移动线
			
			if(parseInt(checkedShapeElement.style.top) == 0){return;}
			
            checkedShapeElement.style.top = filterPX(checkedShapeElement.style.top) - 10;
			checkedShapeElement.style.top = (parseInt(checkedShapeElement.style.top) < 0)? 0: checkedShapeElement.style.top;
			
            SetIntityRels(checkedShapeElement);
            resizePointForShape(checkedShapeElement);
            break;
        case 39:
            // 右键
            if (innerTextBox && innerTextBox.contentEditable == "true"){ return true;}
			var checkedNode = null;
			if(checkedShapeElement){checkedNode = gEAPPWorkflow.getNodeById(checkedShapeElement.id);}
			if(checkedNode && checkedNode.nodeType =="line"){return;}
			
            checkedShapeElement.style.left = filterPX(checkedShapeElement.style.left) + 10;
            SetIntityRels(checkedShapeElement);
            resizePointForShape(checkedShapeElement);
            break;
        case 40:
            // 下键
            if (innerTextBox && innerTextBox.contentEditable == "true"){ return true;}
			var checkedNode = null;
			if(checkedShapeElement){checkedNode = gEAPPWorkflow.getNodeById(checkedShapeElement.id);}
			if(checkedNode && checkedNode.nodeType =="line"){return;}
			
            checkedShapeElement.style.top = filterPX(checkedShapeElement.style.top) + 10;
            SetIntityRels(checkedShapeElement);
            resizePointForShape(checkedShapeElement);
            break;
		//
		
        case 67:
            // 复制(Ctrl + C)
            if (window.event.ctrlKey) {
                setEntityCommands("copyentity");
            }
            break;
        case 86:
            // 粘帖(Ctrl + V)
            if (window.event.ctrlKey) {
                setEntityCommands("parseentity");
            }
            break;
		/**
        case 88:
            // 剪切(Ctrl + X)
            if (window.event.ctrlKey) {
                setEntityCommands("cutentity");
            }
            break;
        case 83:
            // 保存(Ctrl + S)
            if (window.event.ctrlKey) {
                //fileCommand("save");
            }
            break;
		 
        case 78:
            // 新建(Ctrl + N)
            if (window.event.ctrlKey) {
                //fileCommand('new');
            }
            break;
        
        **/
    }
};

this.setEntityCommands = setEntityCommands;

function setEntityCommands(cmdName)
{
    switch(cmdName){
        case "delentity": // 删除图元
        
            //图元编辑时，直接返回
        	 if(checkedShapeElement){
        	 	var innerTextBox = $('#td_editbox',checkedShapeElement)[0];
				if (innerTextBox && innerTextBox.contentEditable == "true"){
					return true;//在图元编辑时的删除
				}
        	 }
        	
        	if(!checkedShapeElement){
        		alert("当前没有选中图元，无法删除！");
        		return false;
        	}
        	        	
            if(checkedShapeElement){
				var innerTextBox = $('#td_editbox',checkedShapeElement)[0];
				if(!innerTextBox){
					alert("当前没有选中图元，无法删除！");
					return false;
				}
				    
				if (innerTextBox && innerTextBox.contentEditable == "true"){
					alert("图元处于编辑状态，无法删除！")
					return false;
				}
                  
                // 删除后，也应切换属性面板
                
                //删除图元
                if(checkedShapeElement.className == "drag"){
                
                    setPointResizerVisible(true);
					gEAPPWorkflow.deleteChild();                
                   	gEAPPWorkflow.hasChanged = true;
                   	workContentPanel.focus();
                   	return true;
                    
                }else if(checkedShapeElement.className == "line"){
                	//删除连接线
                    setPointResizerForLineVisible(true);	            
	                gEAPPWorkflow.deleteChild();
	                gEAPPWorkflow.hasChanged = true;
	                // 应该在删除方法内部，实现这个hasChanged的改变
                   	workContentPanel.focus();
                   	return true;
                }else{
					alert("当前没有选中图元，无法删除！");
					return false;
                }
            }
            break;
        case "edittext":
            if(checkedShape != null)
            {
                ContextMenuItemClick('edit');
                setPointResizerVisible(true);
            }
            break;
        case "cutentity"://剪切
            if(checkedShape != null)
            {
                setPointResizerVisible(true);
                window.clipboardData.setData('text',checkedShape.outerHTML);
				gEAPPWorkflow.deleteChild(true);
            }
			workContentPanel.focus();
            break;
        case "parseentity"://粘贴
            // 粘帖图元
			
            //图元编辑时，直接返回
        	 if(checkedShapeElement){
        	 	var innerTextBox = $('#td_editbox',checkedShapeElement)[0];
				if (innerTextBox && innerTextBox.contentEditable == "true"){
					return true;//在图元编辑时的删除
				}
        	 }
			
/*
			if(!window.clipboardData || !window.clipboardData.getData){
				var msg = "操作失败！原因：当前浏览器不允许脚本访问剪贴板!\n";
				msg += "解决方式如下（详情参见用户手册）:\n";
				msg += "\t请在当前浏览器的“Internet选项”中，";
				msg += "访问“安全”选项卡》点击“Internet”区域》单击“自定义级别（C）”》";
				msg += "在弹出的对话框中找到“允许对剪贴板进行编程访问”》选中“启用”单选框》";
				msg += "再单击“确定”按钮》最后“确定”以关闭“Internet选项”对话框。";
				//msg += "在保存流程后，重新刷新页面生效！";
				alert(msg);
			}
*/
			
			//获取剪贴板的内容
			var copyId = window.clipboardData.getData('text');
			var checkedNode = gEAPPWorkflow.getNodeById(copyId);	
					
			if(!copyId || !checkedNode){
				alert("剪贴板无内容，取消粘贴！");
				return false;
			}else if(checkedNode.nodeType != "line" && checkedNode.nodeType != "shape"){
				//只能拷贝图元或线			
				alert("剪贴板无内容，取消粘贴！");
				return false;
			}
			
			//置空剪贴板，以屏蔽多次复制
			//window.clipboardData.setData('text',"");
			
			//创建新图元
			var newShape = eval('new ' + checkedNode.typeName + '()');
			var outerhtmlString = checkedNode.domElement.outerHTML;
			
			//修改界面元素id
			var beginIndex = outerhtmlString.indexOf("id=");
			var endIndex = outerhtmlString.indexOf(" ",beginIndex);
			var htmlString = outerhtmlString.substring(0,beginIndex) + "id=" + newShape.id + " " + outerhtmlString.substring(endIndex);
			
		 
			//以下三行为了修正jQuery在复制时保存了jQueryId，进行删除
			beginIndex = htmlString.indexOf("jQuery");
			endIndex = htmlString.indexOf(" ",beginIndex);
			htmlString = htmlString.substring(0,beginIndex) + " "+ htmlString.substring(endIndex);
		 
			
			//修改名称 
			var newName = newShape.text + "(复制)" ;//+ newShape.generateId;
			var attr_name_id = newShape.id + "#" +  newShape.typeName + "#name";
			
			//更新图元内部的span名称的onblur响应事件
			var innerBoxHTML = "<span  id=\"td_editbox\" "
					+ " onblur=\"setEntityTextBoxStyle(this);gEAPPWorkflow.updateAttributeValue(this,'" + attr_name_id + "')\" >" 
					+ newName
					+ "</span>";
					
			// 
			beginIndex = htmlString.indexOf("SPAN");
			endIndex = htmlString.indexOf("SPAN",beginIndex+5);
			htmlString = htmlString.substring(0,beginIndex-1) + innerBoxHTML + htmlString.substring(endIndex+5);
			
			
			
			workContentPanel.append(htmlString);//添加到工作区
			
			//获取元素 
			newShape.domElement = document.getElementById(newShape.id);
			newShape.element = $('#'+ newShape.id ,workContentPanel);
			
			

			//新生成的图元在旧图元的右下方10px
			var newShapeLeft = parseInt(checkedNode.element.css("left")) + 10;
			var newShapeTop =  parseInt(checkedNode.element.css("top")) + 10;
			//调整位置	 
			newShape.element.css({
				left:newShapeLeft + 'px',
				top:newShapeTop + 'px'
			});
			
			
			//附加事件
			newShape.element.click(function(){
				//显示属性面板 
				gEAPPWorkflow.showPropertyPanel(newShape);
			}).dblclick(function(){
				//快速编辑 
				gEAPPWorkflow.ContextMenuItemClick("edit");
			});
			
			
			//复制属性数组
			copyAttributeArray(checkedNode,newShape);
			alterAttribute(newShape,'name',newName);//更新名称属性
			
			//添加到当前工作区
			childNodes.push(newShape);
			//newShape.element.show();
			
			//选中当前被复制对象
	    	checkedShapeElement = newShape.domElement;
		    setPointResizerVisible(true);
			
			if(checkedNode.nodeType == "line"){
			    resizePointForLine(checkedShapeElement);
			}else if(checkedNode.nodeType == "shape"){
			    resizePointForShape(checkedShapeElement);
			}
			
			//显示属性面板 
			gEAPPWorkflow.showPropertyPanel(newShape);
			
			//已经修改
			gEAPPWorkflow.hasChanged = true;
			workContentPanel.focus();
			
            break;
        case "copyentity":
        
            //图元编辑时，直接返回
        	 if(checkedShapeElement){
        	 	var innerTextBox = $('#td_editbox',checkedShapeElement)[0];
				if (innerTextBox && innerTextBox.contentEditable == "true"){
					return true;//在图元编辑时的删除
				}
        	 }
        
			if(!checkedShapeElement){
				alert("当前没有选中图元，无法复制！");
				return false;
			}
				
            if(checkedShapeElement != null)
            {
				var shapeId = checkedShapeElement.id;
				var checkedNode = gEAPPWorkflow.getNodeById(shapeId);
				
				if(!shapeId || !checkedNode){
					alert("当前没有选中图元，无法复制！");
					return false;
				}
				
							
				//开始与结束图元不能复制
				if(checkedNode instanceof EAPP.Workflow.StartState){
					alert("禁止复制“开始”图元！");
					return false;
				}else if(checkedNode instanceof EAPP.Workflow.EndState){
					alert("禁止复制“结束”图元！");
					return false;
				}
				
				//复制线时显示被复制图元的小标
				if(checkedNode.nodeType == "shape"){
	                setPointResizerVisible(true);
				}else if(checkedNode.nodeType == "line"){
					setPointResizerForLineVisible(true);
				}
				
/*
			if(!window.clipboardData || !window.clipboardData.setData){
				var msg = "操作失败！原因：当前浏览器不允许脚本访问剪贴板!\n";
				msg += "解决方式如下（详情参见用户手册）:\n";
				msg += "\t请在当前浏览器的“Internet选项”中，";
				msg += "访问“安全”选项卡》点击“Internet”区域》单击“自定义级别（C）”》";
				msg += "在弹出的对话框中找到“允许对剪贴板进行编程访问”》选中“启用”单选框》";
				msg += "再单击“确定”按钮》最后“确定”以关闭“Internet选项”对话框。";
				//msg += "在保存流程后，重新刷新页面生效！";
				alert(msg);
			}
*/
			
                window.clipboardData.setData('text',checkedShapeElement.id);
            }
			workContentPanel.focus();
            break;
    }
};



//上下文菜单
//@param EventArgs 操作类型
//@return void
function ContextMenuItemClick(EventArgs)
{
	if(EventArgs == "edit")
	{
	    if(checkedShapeElement == null) return;
	    var innerTextBox = $('#td_editbox',checkedShapeElement)[0];
	    innerTextBox.style.cursor = "text";
		innerTextBox.contentEditable = true;
		innerTextBox.focus();
	}
};

this.ContextMenuItemClick = ContextMenuItemClick;

	//------------------------------------------------------------图形显示隐藏控制----------------------------------------
	
	/**
	 * 调整选中元素大小
	 * @param {Element} panel 要调整大小的DOM元素对象
	 * @param lcursorX
	 * @param lcursorY
	 */
	function resizeNode(panel, flagElement, lcursorX, lcursorY){
	
		var panelLeft = filterPX(panel.style.left);
		var panelTop = filterPX(panel.style.top);
		var panelWidth = filterPX(panel.style.width);
		var panelHeight = filterPX(panel.style.height);
		
		//根据所单击的点计算图形长、宽
		switch (flagElement.id) {
			//图元小标的情况:p0~p7
			case "p0":{
				try {
					panel.style.width = panelWidth - (lcursorX - panelLeft);
					panel.style.height = panelHeight - (lcursorY - panelTop);
				} 
				catch (e) {
				}
				panel.style.left = lcursorX;
				panel.style.top = lcursorY;
				resizePointForShape(panel);
				break;
			}
			case "p1":{
				panel.style.height = panelHeight - (lcursorY - panelTop);
				panel.style.top = lcursorY;
				resizePointForShape(panel);
				break;
			}
			case "p2":{
				try {
					panel.style.width = lcursorX - panelLeft;
					panel.style.height = panelHeight - (lcursorY - panelTop);
				} 
				catch (e) {
				}
				panel.style.top = lcursorY;
				resizePointForShape(panel);
				break;
			}
			case "p3":{
				try {
					panel.style.width = panelWidth - (lcursorX - panelLeft);
				} 
				catch (e) {
				}
				panel.style.left = lcursorX;
				resizePointForShape(panel);
				break;
			}
			case "p4":{
				try {
					panel.style.width = lcursorX - panelLeft;
				} 
				catch (e) {
				}
				resizePointForShape(panel);
				break;
			}
			case "p5":{
				try {
					panel.style.width = panelWidth - (lcursorX - panelLeft);
				} 
				catch (e) {
				}
				panel.style.left = lcursorX;
				panel.style.height = lcursorY - panelTop;
				resizePointForShape(panel);
				break;
			}
			case "p6":{
				try {
					panel.style.height = lcursorY - panelTop;
				} 
				catch (e) {
				}
				resizePointForShape(panel);
				break;
			}
			case "p7":{
				try {
					panel.style.width = lcursorX - panelLeft;
					panel.style.height = lcursorY - panelTop;
				} 
				catch (e) {
				}
				resizePointForShape(panel);
				break;
			}
			
			//线小标的情况：p8~p9	
			case "p8":{
				// panel 属于当前被强制变化的线
				panel.from = (lcursorX - panelLeft + 2.5) + "," + (lcursorY - panelTop + 2.5);
				// +2.5将点向右移变居中。
				resizePointForLine(panel);//调整连接线的小标
				// 当移动箭头时，删除该箭头方向所关联的图元
				RemoveRelByLineFlag(panel, "p8"); 
				break;
			}
			case "p9":{
				// panel 属于当前被强制变化的线
				panel.to = (lcursorX - panelLeft + 2.5) + "," + (lcursorY - panelTop + 2.5);
				resizePointForLine(panel);//调整连接线的小标
				// 当移动箭头时，删除该箭头方向所关联的图元
				RemoveRelByLineFlag(panel, "p9");
				break;
			}
			default:{
				break;//
			}
		}//end switch
	};
	
	/**	
	 *  显示/隐藏图元的小标
	 * @return {Boolean} isVisble true显示，false隐藏
	 */
	function setPointResizerVisible(isVisble){
		
		var dispVal;
		if (isVisble){
			dispVal = "block";
		}else {
			dispVal = "none";
		}
		
		for (i = 0; i < 8; i++) {
			document.getElementById('p'+i).style.display = dispVal;
		}
	};
	
	function getResizePoints(){
		var points = new Array();
		for (i = 0; i < 10; i++) {
			var point = document.getElementById('p'+i);
			points.push(point);
		}
		return points;
	};
	
	/**
	 *  显示/隐藏连接线的小标
	 * @return {Boolean} IsVisible true显示，false隐藏
	 */
	function setPointResizerForLineVisible(IsVisible){
		var dispVal;
		if (IsVisible == true) {
			dispVal = "block";
		}else {
			dispVal = "none";
		}
		
		var resizeFlags = getResizePoints();
		for (i = 8; i < 10; i++) {
			resizeFlags[i].style.display = dispVal;
		}
	};
	
	
	/**
	 *  调整图元的小标
	 * @return {Element} panel 要调整小标的图元DOM元素对象
	 */
	function resizePointForShape(panel){
		if (panel == null){return;} 	
		
		var panelLeft = filterPX(panel.style.left);
		var panelTop = filterPX(panel.style.top);
		var panelWidth = filterPX(panel.style.width);
		var panelHeight = filterPX(panel.style.height);
		
		//var resizeFlags = workContentPanel.find("oval");
		var resizeFlags = getResizePoints();
		
		var flagWidth = filterPX(resizeFlags[0].style.width);
		//var flagheight = filterPX(resizeFlags[0].style.height);
		
		resizeFlags[0].style.left = panelLeft - flagWidth / 2;
		resizeFlags[0].style.top = panelTop - flagWidth / 2;
		
		resizeFlags[1].style.left = panelLeft + (panelWidth / 2) - flagWidth / 2;
		resizeFlags[1].style.top = panelTop - flagWidth / 2;
		
		resizeFlags[2].style.left = panelLeft + panelWidth - 2.5;
		resizeFlags[2].style.top = panelTop - flagWidth / 2;
		
		resizeFlags[3].style.left = panelLeft - flagWidth / 2;
		resizeFlags[3].style.top = panelTop + (panelHeight / 2) - flagWidth / 2;
		
		resizeFlags[4].style.left = panelLeft + panelWidth - 2.5;
		resizeFlags[4].style.top = panelTop + (panelHeight / 2) - flagWidth / 2;
		
		resizeFlags[5].style.left = panelLeft - flagWidth / 2;
		resizeFlags[5].style.top = panelTop + panelHeight - 2.5;
		
		resizeFlags[6].style.left = panelLeft + (panelWidth / 2) - flagWidth / 2;
		resizeFlags[6].style.top = panelTop + panelHeight - 2.5;
		
		resizeFlags[7].style.left = panelLeft + panelWidth - 2.5;
		resizeFlags[7].style.top = panelTop + panelHeight - 2.5;
	};
	
	
	/**
	 *  调整连接线的小标
	 * @return {Element} panel 要调整小标的连接线DOM元素对象
	 */
	function resizePointForLineSimple(line, IsMultiline){
		if (!line) {return;}
		
		var toValue;
		var fromValue;
		var fromX;
		var fromY;
		
		fromValue = (line.from + "").split(",");
		fromX = getPx(fromValue[0]);
		fromY = getPx(fromValue[1]);
		
		toValue = (line.to + "").split(",");
		var toX = getPx(toValue[0]);
		var toY = getPx(toValue[1]);
				
	
		//var resizeFlags = workContentPanel.find("oval");
		var resizeFlags = resizeFlagPoints;
		
		try {
			resizeFlags[8].style.left = fromX - 2.5;
			resizeFlags[8].style.top = fromY - 2.5;
			resizeFlags[9].style.left = toX - 2.5;
			resizeFlags[9].style.top = toY - 2.5;
		} 
		catch (e) {
		}
	};
	
	/**
	 *  调整连接线的小标
	 * @return {Element} panel 要调整小标的连接线DOM元素对象
	 */
	function resizePointForLine(line, IsMultiline){
		if (!line) {return;}
		
		var toValue;
		var fromValue;
		var fromX;
		var fromY;
		
		fromValue = (line.from + "").split(",");
		fromX = getPx(fromValue[0]);
		fromY = getPx(fromValue[1]);
		
		toValue = (line.to + "").split(",");
		var toX = getPx(toValue[0]);
		var toY = getPx(toValue[1]);
				
		//var resizeFlags = workContentPanel.find("oval");
		var resizeFlags = resizeFlagPoints;
		
		try {
			resizeFlags[8].style.left = fromX - 2.5;
			resizeFlags[8].style.top = fromY - 2.5;
			resizeFlags[9].style.left = toX - 2.5;
			resizeFlags[9].style.top = toY - 2.5;
		} 
		catch (e) {
		}
		
			
		//设置鼠标经过的图元样式 //
		setMouseOverIntityStyle(false);
	};
	
	//过滤px
	function filterPX(pxString){
		return pxString.replace("px", "") - 0;
	};
	
	function getPx(ptObj){
		ptString = ptObj.toString();
		if(ptString.indexOf('pt') != -1){
			return ptString.replace("pt", "") * 1 / 72 * 96;
		}else if(ptString.indexOf('in') != -1){
			return ptString.replace("in", "") * 96;
		}		
	};
	
	// 获得鼠标经过的图元 //fixed
	function getMouseOverIntity(){
		//获取工作区内的所有元素
		var elements = contentPanelElement.all;
		//获取工作区内的相对坐标
		var mouseX = getRelativeLeft(event.clientX);
		var mouseY = getRelativeTop(event.clientY);
		var offset = 17;
		
		
		//遍历工作区的所有图元
		for (i = 0; i < elements.length; i++) {
			if (elements[i].className == "drag") { //属于图元图标时
			
				if (mouseX + offset > filterPX(elements[i].style.left) && mouseX - offset < filterPX(elements[i].style.left) + filterPX(elements[i].style.width)) {
					if (mouseY + offset > filterPX(elements[i].style.top) && mouseY - offset < filterPX(elements[i].style.top) + filterPX(elements[i].style.height)) {
						return elements[i];
					}
				}
			}
		}//end for
		return null;
	};
	
	// 获得鼠标经过的重设大小标记 //fixed
	function getMouseOverResizerFlag(){
		var mouseX = getRelativeLeft(event.clientX);
		var mouseY = getRelativeTop(event.clientY);
		var result;
		//var elements = workContentPanel.find("oval");
		var elements = resizeFlagPoints;
		var offset = 10; // 扩展出20px
		for (i = 0; i < 8; i++) {
			if (mouseX + offset > filterPX(elements[i].style.left) && mouseX - offset < filterPX(elements[i].style.left) + filterPX(elements[i].style.width)) {
				if (mouseY + offset > filterPX(elements[i].style.top) && mouseY - offset < filterPX(elements[i].style.top) + filterPX(elements[i].style.height)) {
					result = elements[i];
				}
			}
			elements[i].fillcolor = mouseOutResizeFlagFillCor;
		}
		return result;
	};
	
	// setStandardStyle是否强制执行 else 下面的内容？ 
	// autoZero 是否自动对齐
	function setMouseOverIntityStyle(setStandardStyle, autoZero){
		//var resizeFlags = workContentPanel.find("oval");
		var resizeFlags = resizeFlagPoints;
		
		
		
		// 获取鼠标经过的图元
		var mouseOverObj = getMouseOverIntity(); 
		
		mouseOverShape = mouseOverObj;
		if (mouseOverObj != null && setStandardStyle == false ) {
			resizePointForShape(mouseOverObj); // 将小标移动到图元的周围
			setPointResizerVisible(true); // 显示小标
			for (i = 0; i < 8; i++) {
				resizeFlags[i].style.width = 10; // 将小标的高款设置为10px
				resizeFlags[i].style.height = 10;
				resizeFlags[i].style.cursor = "";
			}
			mouseOverShape = mouseOverObj;
			
			// 当鼠标经过设置标记节点时，将节点的颜色填充为mouseOverResizeFlagFillCor 色
			mouseOverResizeFlag = getMouseOverResizerFlag();
			if (mouseOverResizeFlag != null) {
				mouseOverResizeFlag.fillcolor = mouseOverResizeFlagFillCor;
				
				//
				 
				// 临时自动对准
				if (autoZero != false) {
					var relObj = new Object();
					relObj.IntityObjid = mouseOverObj.id; //图元Id
					relObj.IntityPointBearing = mouseOverResizeFlag.id;//结点Id
					relObj.LineObjid = checkedShapeElement.id; //线的Id
					relObj.lLineBearing = lastFlagID; //方向
					//SetIntityRel(relObj);
					SetIntityRel(relObj.IntityObjid, relObj.IntityPointBearing, relObj.LineObjid, relObj.lLineBearing);
				}
				 
			}
		}
		else if(setStandardStyle || !mouseOverObj){
			for (i = 0; i < 8; i++) {
				resizeFlags[i].style.width = 5;
				resizeFlags[i].style.height = 5;
				resizeFlags[i].fillcolor = mouseOutResizeFlagFillCor;
				resizeFlags[i].style.cursor = gEAPPWorkflow.resizeFlagCursorStyles[i];
			}
			setPointResizerVisible(false);
		}
	};
	
	
	
		
	//添加图元关联
	//@param IntityObjId 图元对象Id
	//@param IntityPointBearing p0~ p8 点 ID ，建立关联的结点
	//@param LineObjId 线对象Id
	//@param lLineBearing p8~p9 点 ID //LineBearing方向 p8 属于前端 p9属于后端(箭头端)
	//@return void
	function AddIntityRel(IntityObjId,IntityPointBearing,LineObjId,lLineBearing){
		//获取图元对象
	    var shape = gEAPPWorkflow.getNodeById(IntityObjId);
	    if(!shape){return ;}
 
		
		var lineDirection = (lLineBearing == "p8") ? "out":"in";
		if(lineDirection != "out" && lineDirection != "in"){return ;}
		
		//添加到图元的关联上
		var bSuccess = false;
		bSuccess = shape.innerLine.addLine(IntityPointBearing,LineObjId,lLineBearing);
		var lineObj = document.getElementById(LineObjId);
 
		//建立连接失败时，是否提示并删除当前操作的线的
		if(bSuccess == false){
			//如果连接失败，删除当前连线
			var lineObj = document.getElementById(LineObjId);
			var lineNode  = gEAPPWorkflow.getNodeById(LineObjId);
			if(lineObj && lineNode && lineNode.nodeType =="line"){
 				//静默删除
				gEAPPWorkflow.deleteChild(true,lineObj);
				
			}
			
			if(lineDirection == "out"){
				//alert("建立与图元连接");
			}else if(lineDirection == "in"){
				//alert("建立与图元连接");
			}
  		}
	};
	
	//删除该对象的所有关联
	//@param 图元dom对象
	//@return true/false 删除是否成功
	function RemoveRelByIntity(IntityObj){
		
		if(IntityObj){return false;}
		
		//获取图元对象
	    var shape = gEAPPWorkflow.getNodeById(IntityObj.id);
	    if(!shape){ return false;}
		
		//删除该图元的所有关联
		return shape.innerLine.removeAllLine();
	};

	//删除一条线的关联
	//@param lineObj 线对象
	//@param lineBearing 方向 p8~p9 点 ID
	//@return void
	function RemoveRelByLineFlag(LineObj,lineBearing){
	    if(!LineObj || !LineObj.id) return false;
	    var lineId = LineObj.id;
	    
	    //取得方向，out/in 是针对当前图元对象而言
	    var direction = (lineBearing == "p8") ? "out":"in";
	    
		//获取图元对象
	    var line = gEAPPWorkflow.getNodeById(lineId);
	    if(!line){ return false;}
		
		//删除线的该方向关联
		return line.removeRelByDirection(direction);
	};
	
	
	
/**
 * 设置单个图元关联
 * @param {String} IntityObjId 图元Id                               
 * @param {String} point       关联线的Id                            
 * @param {String} lineId      建立关联的结点                        
 * @param {String} direction   箭头的方向（in指向图元，out从图元指出） 
 */
function SetIntityRel(IntityObjId,point,lineId,direction)	 
{
	//获取图元的dom对象
	var intityobj = document.getElementById(IntityObjId);
	if(!intityobj){return false;}
	
	//如果当前传进来的参数不是out/in,进行转换
	if((direction=="p8") || (direction=="p9")){
		direction = (direction == "p8") ? "out":"in";
	}
 
	var intityLeft = parseFloat(intityobj.style.left.replace("px",""));
	var intityTop = parseFloat(intityobj.style.top.replace("px",""));
	var intityWidth = parseFloat(intityobj.style.width.replace("px",""));
	var intityHeight = parseFloat(intityobj.style.height.replace("px",""));
	var resultX = 0;
	var resultY = 0;
	//根据结点计算位置
	switch(point)
	{
		case "p0":
			resultX = intityLeft;
			resultY = intityTop;
			resultLineVal = intityLeft +  "," + intityTop;
			break;
		case "p1":
			resultX = intityLeft + intityWidth / 2;
			resultY = intityTop;
			resultLineVal = intityLeft + intityWidth / 2  + "," + intityTop;
			break;
		case "p2":
			resultX = intityLeft + intityWidth ;
			resultY = intityTop;
			resultLineVal = intityLeft + intityWidth + "," + intityTop;
			break;
		case "p3":
			resultX = intityLeft;
			resultY = intityTop + intityHeight /2;
			resultLineVal = intityLeft +  "," + (intityTop + intityHeight /2);
			break;
		case "p4":
			resultX = intityLeft + intityWidth;
			resultY = intityTop + intityHeight /2;
			resultLineVal = intityLeft + intityWidth + "," + (intityTop + intityHeight /2);
			break;
		case "p5":
			resultX = intityLeft;
			resultY = intityTop + intityHeight;
			resultLineVal = intityLeft +  "," + (intityTop + intityHeight);
			break;
		case "p6":
			resultX = intityLeft+ intityWidth / 2;
			resultY = intityTop + intityHeight;
			resultLineVal = intityLeft + intityWidth / 2  + "," + (intityTop + intityHeight);
			break;
		case "p7":
			resultX = intityLeft+ intityWidth;
			resultY = intityTop + intityHeight;
			resultLineVal = intityLeft + intityWidth + "," + (intityTop + intityHeight);
			break;
	}
    
    //线的dom对象
	var lineObj = document.getElementById(lineId);
	
	//var lineFlag = document.getElementById(relObj.lLineBearing);
	resultX = resultX - filterPX(lineObj.style.left);
	resultY = resultY - filterPX(lineObj.style.top);
	
	try{
		//如果是出线
		if(direction == "out"){
			if(lineObj.from){
				lineObj.from = resultX + "," + resultY;
			}
			else{
				//trace("连接线出错啦！" + lineObj.innerText);
			}
		}else if(direction == "in"){
			if(lineObj.to){lineObj.to = resultX + "," + resultY;}
			else{
				//trace("连接线出错啦！" +lineObj.innerText);
			}
 
		}
	}catch(e){
	}
};

//设置图元所有关联
//@param actionIntityObj 页面图元dom对象
//@return void
function SetIntityRels(actionIntityObj)
{
	//获取图元对象
    if(!actionIntityObj) return false;
    var shape = gEAPPWorkflow.getNodeById(actionIntityObj.id);
    if(!shape || shape.nodeType != "shape"){return false;}

	//遍历线的所有关联
	var relArray = shape.innerLine.relationArray;
	if(!relArray || !relArray.length){return false;}
	
	for (var i=0; i<relArray.length; i++) {
		var rel = relArray[i];
		SetIntityRel(shape.id,rel.pointIndex,rel.line.id,rel.lineDirection);	 
	}
};

	this.deleteChild = function(nUseConfirm,delObject){
		//默认删除当前选中对象
		var deleteElement = checkedShapeElement;
		//如果有传入删除对象，则删除传入的对象
		if(delObject){deleteElement = delObject;}
		
		if (deleteElement != null) {
		
			//获取要删除的元素对象
			var checkedNode = this.getNodeById(deleteElement.id);
			if(!checkedNode){
				//如果对象不存在，则直接删除页面上的元素
				alert("删除失败！");
				//trace("所删除的对象不在工作区的结点集中！");
			}
			
			//禁止删除开始与结束图元
			if(checkedNode instanceof EAPP.Workflow.StartState){
				alert("禁止删除“开始”图元！");
				return false;
			}else if(checkedNode instanceof EAPP.Workflow.EndState){
				alert("禁止删除“结束”图元！");
				return false;
			}
				
			if (nUseConfirm == true || confirm("确实要删除\"" + checkedNode.text + "\"吗？")) {
				////trace("删除了对象" + deleteElement.id);
				
				if (checkedNode.nodeType == "shape") {
					////trace("\n删除" + deleteElement.id + "对象所有关联");
					
					checkedNode.innerLine.removeAllLine();//删除所有关联
					RemoveRelByIntity(deleteElement);// 删除该对象的相关关联
				}
				else {
					////trace("\n删除" + deleteElement.id + "线所有关联");
					RemoveRelByLineFlag(deleteElement, "p8");
					RemoveRelByLineFlag(deleteElement, "p9");
				}
				////trace("剩余 " + RelsArray.length + " 个关联");
				deleteElement.parentNode.removeChild(deleteElement);
				//workContentPanel.remove("#" + deleteElement.id);
				//从工作区中删除当前图元对象
				this.deleteNodeById(checkedNode.id);

				//如果删除的是选中对象				
				if(!delObject){
					checkedShapeElement = null;
				}else{
					mouseOverShape = null;//鼠标经过图元置空
					mouseOverResizeFlag = null;
					
					//drawingShapeElement = null;
					isDragapproved = false;
				}
				setPointResizerVisible(false);
				setPointResizerForLineVisible(false);
				//删除后显示工作流对象的属性面板
				gEAPPWorkflow.showPropertyPanel(gEAPPWorkflow);
			}
		}
	};
	
	/**
	 * 保存为工作流引擎解析的xml文件
	 * @return {String} 返回生成的xml文件字符串
	 */
	this.saveAsXml = function(){
		
		if(!validateWorkflowConnect()){
			
			var forceSave = confirm("校验失败！当前工作流程图中存在没有正确连接的线或图元！\n按“确定”按钮,进行强制保存！\n按“取消”按钮,进行修正关联！");
			if(!forceSave){//按取消
				return false;
			}
		}
		
		var xmlDocHead = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		
		var xmlDocBody  = "";
		//添加工作流的属性内容
		//var xmlDocBody = "\n<flow-definition>";
		xmlDocBody += generateXML(gEAPPWorkflow,'',true);
		
		//保存meta数据
		xmlDocBody +=  "\n" + this.getMetadataXml() + "\n";
		
		//遍历结点生成xml
		for(var i=0; i<childNodes.length; i++){
			var node = childNodes[i];
			
			if(node.nodeType == "shape"){
				xmlDocBody +=generateXML(node,'',true) + "\n\n";
			}
		}
		
		var graphicXML = this.saveAsGraphicXml();
		xmlDocBody += graphicXML;
		
		//添加结束工作流结束
		xmlDocBody += "\n</flow-definition>";
		
		var xmlDoc = xmlDocHead + xmlDocBody;
		
		//保存变为未更改
		gEAPPWorkflow.hasChanged = false;
		
		return xmlDoc;
	};
	

	
	
	this.saveAsGraphicXml = function(){
		
		/***
		var xmlDocHead = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
		                 +"\n<graphic>\n"
		                 +"\n\t<id-index>" + idIndex + "</id-index>\n";
		*/
		var xmlDocHead = "\n<graphic>\n"
		                 +"\n\t<id-index>" + idIndex + "</id-index>\n";
		
		//保存meta数据
		xmlDocHead += "\n\t<metas>"
		xmlDocHead += this.getMetadataXml();
		xmlDocHead += "\n\t</metas>";
						 
		xmlDocHead += "\n\t<attributes>\n";
		xmlDocHead += generateGraphicXML(gEAPPWorkflow,'\t');
		xmlDocHead += "\t</attributes>\n";
						 
		var xmlDocEnd = "\n</graphic>";
		
		var xmlDocBody ="";
		for(var i=0; i<childNodes.length; i++){
			var node = childNodes[i];
			
			var nodeString = "";
			if(node.nodeType == "shape"){
				nodeString = "\n\t<shape id=\""+ node.id +"\"  text=\""+ node.text +"\"  typeName=\""  + node.typeName +"\"  description=\""+ node.description +"\" >";
				nodeString += "\n\t\t<attr name=\""+ node.element.attr('name') +"\" className=\"" + node.element.attr("className") +"\" />";
				nodeString += "\n\t\t<style position=\""+ node.element.css("position") +"\" left=\""+ node.element.css("left") +"\" top=\"" + node.element.css("top") +"\" width=\""+ node.element.css('width') +"\" height=\""  + node.element.css('height') +"\" zIndex=\""+ node.element.css("zIndex") +"\" />";
				nodeString += "\n\t\t<rels>";
				for(var j=0; j<node.innerLine.relationArray.length; j++){
					var rel = node.innerLine.relationArray[j];
					nodeString += "\n\t\t\t<rel shapeId=\""+ node.id +"\"  pointIndex=\""+ rel.pointIndex  +"\"  lineId=\""+ rel.line.id +"\"  direction=\""+ rel.lineDirection +"\" />";
				}
				nodeString += "\n\t\t</rels>";
				nodeString += "\n\t\t<attributes>\n";
				nodeString += generateGraphicXML(node,'\t\t');
				nodeString += "\t\t</attributes>";
				nodeString += "\n\t</shape>\n";
			}else if(node.nodeType == "line"){
				nodeString = "\n\t<line id=\""+ node.id +"\"  text=\""+ node.text +"\"  typeName=\"" + node.typeName +"\"  description=\""+ node.description +"\"  fromId=\"" + node.fromId +"\" toId=\""+ node.toId +"\">";
				nodeString += "\n\t\t<attr name=\"" + node.element.attr('name') +"\"  className=\"" + node.element.attr("className") +"\" from=\""+ node.element.attr("from")  +"\"   to=\""+ node.element.attr("to") +"\" stroked=\""+ node.element.attr("stroked")+"\"  strokecolor=\""+ node.element.attr("strokecolor") +"\"  strokeweight=\""+ node.element.attr("strokeweight") +"\" />";
				nodeString += "\n\t\t<style position=\""+ node.element.css("position") +"\" left=\""+ node.element.css("left") +"\" top=\""  + node.element.css("top")+"\" cursor=\""+ node.element.css("cursor") +"\" width=\""+ node.element.css('width') +"\" height=\""  + node.element.css('height') +"\" zIndex=\""+ node.element.css("zIndex") +"\"  />";
				nodeString += "\n\t\t<attributes>\n";
				nodeString += generateGraphicXML(node,'\t\t');
				nodeString += "\t\t</attributes>";
				nodeString += "\n\t</line>\n";
			}
			
			xmlDocBody += nodeString;
		}
		
        var htmlString = workContentPanel.html();
        htmlString = htmlString.replace(/\&/g, "&amp;");
        htmlString = htmlString.replace(/</g, "&lt;");
        htmlString = htmlString.replace(/>/g, "&gt;");
		
		var iconPath =  "src=\"" + gEAPPWorkflow.basePath + "icons/";
		htmlString = htmlString.replace(/src="http:\/\/.*?\/icons\/.*?/g,iconPath);
		
		var htmlContent = "\n\t<html-content>\n"+ htmlString+"\n\t</html-content>";
		
		var xmlDoc = xmlDocHead + xmlDocBody + htmlContent + xmlDocEnd;
 
		return xmlDoc;
		//$.post("http://localhost:8080/workflow/test?act=save&saveType=Graphic",{xml:xmlDoc});   
	};
	
	
	function generateGraphicXML(shape,parentTab){
		//获取当前对象的类型名，没有定义的为空
		//var typeName = "";
		//if(typeof(shape.typeName)) typeName = shape.typeName;
		
		
		var resultXML ="";
		var attributeArray = shape.attributeArray;
		if(null == attributeArray || attributeArray.length == 0){ return "";}
		
		for(var i=0; i<attributeArray.length; i++){
			var attr = attributeArray[i];
			
			//如果当前属性无效，则跳过
			if(!attr.isEnable ){continue;}
			
			resultXML += parentTab + "\t<attribute name=\"" + attr.attributeName  + "\"";
			
			/**
			if (attr.isDynamicCreate) {
				resultXML +=" isDynamicCreate=\"true\" typeName=\"" + attr.typeName + "\" >";

				for(var item in attr){
					if((typeof(item) == "string") && (item.constructor.toString()!= "funciton")&& (item.toString() != "value")){
						itemValue = eval('attr.' + item.toString() );
						if(itemValue){if(itemValue.toString().indexOf("function" != -1)){continue;}}//如果是函数则跳过
						resultXML +="\n"+ parentTab + "\t\t<item name=\"" +item.toString() + "\">" + eval('attr.' + item.toString() ) + "</item>";
					}
				}
			}else{
				resultXML +=" >";
			} 
			**/
			
			resultXML +=" >";
			resultXML += "\n"+ parentTab + "\t\t<value><![CDATA[" + attr.value + "]]></value>";

			//如果有子属性，那么生成子属性
			if(attr.attributeArray != null &&  attr.attributeArray.length>0){
				var childXML = generateGraphicXML(attr,parentTab + '\t');
				resultXML += "\n" +childXML;
				resultXML += "" + parentTab + "\t</attribute>\n";
			}else{
				resultXML += "\n" +parentTab + "\t</attribute>\n";
			}
			
			//resultXML += parentTab + "\t</attribute>\n";
		}
		return resultXML;
	};
	
	
	this.parseGraphicXml = function(xmlString){
        var xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
        xmlDoc.async = false;
		try{
        	xmlDoc.loadXML(xmlString);	//对文件和XML片断的加载
		}catch(e){
			xmlDoc.load(xmlString);		//对完整XML文档和URL的加载
		} 
        
		//initData();
		 //加载图形到工作区
		workContentPanel.html($("html-content",xmlDoc).text()); 
		
		//恢复id索引值 
		setIdIndex($('id-index',xmlDoc).text());
		
		//获取工作流对象的属性
		parseAttributeXML(gEAPPWorkflow,'graphic>attributes',xmlDoc);
		
		//获取工作流的当前属性
		var nameValue = getAttributeValue(gEAPPWorkflow,"name");
		var stateValue = getAttributeValue(gEAPPWorkflow,"state");			
		//更新工作区的标题栏
		workTitlePanel.html("状态：" + stateValue + " 流程名称：" + nameValue);

		curPropertyEditShapeId = -1;
		//显示工作流对象的属性面板
		gEAPPWorkflow.showPropertyPanel(gEAPPWorkflow);
		
		//获取meta数据
		gEAPPWorkflow.metadata.splice(0,gEAPPWorkflow.metadata.length);
		
		
		//alert(xmlDoc);
		this.setMetadata($('metas',xmlDoc)[0].xml);
		
		//初始化数组对象	
		childNodes = new Array();
		
		$('shape',xmlDoc).each(function(i,xmlNode){
			var node = $(xmlNode);
			var typeName = node.attr('typeName');
			
			var nodeId = node.attr('id');
 
			 
			var newString  =  'new ' + typeName + "('"  + nodeId + "')";
			var shape = eval(newString);
 
			 
			if(!shape) return false;
			 
			shape.id = node.attr('id');
			shape.text = node.attr('text');
			shape.typeName = node.attr('typeName');
			shape.description = node.attr('description');
			
			
			shape.domElement = document.getElementById(shape.id);
			shape.element = $(shape.domElement);
			
			//----
			var property = $('attr',node);
			var name = property.attr('name');
			var className = property.attr('name');
			//style 属性暂不解释
			
			childNodes.push(shape);
			//解释属性
			//parseAttributeXML(shape,'attributes',node);
			
			//添加时单击触发click事件
			shape.element.click(function(){
				//显示属性面板 
				gEAPPWorkflow.showPropertyPanel(shape);
			}).dblclick(function(){
				//快速编辑 
				if(shape.typeName == "EAPP.Workflow.StartState" || shape.typeName == "EAPP.Workflow.EndState"){return false;}
				gEAPPWorkflow.ContextMenuItemClick("edit");
			});
			
		});
		
		$('line',xmlDoc).each(function(i,xmlNode){
			var node = $(xmlNode);
			var typeName = node.attr('typeName');
			
			var nodeId = node.attr('id');
			var newString  =  'new ' + typeName + "('"  + nodeId + "')";
			 
			var line = eval(newString);
			if(!line) return false;
			line.id = node.attr('id');
			line.text = node.attr('text');
			line.typeName = node.attr('typeName');
			line.description = node.attr('description');
			
			line.domElement = document.getElementById(line.id);
			line.element = $(line.domElement);
			
			childNodes.push(line);
			//----
			//style 属性暂不解释
			
			//解释属性
			parseAttributeXML(line,'attributes',node);
			
			//添加时单击触发click事件
			line.element.click(function(){
				//显示属性面板 
				gEAPPWorkflow.showPropertyPanel(line);
			}).dblclick(function(){
					//快速编辑 
					gEAPPWorkflow.ContextMenuItemClick("edit");
			});
			
		});
		
		$('rel',xmlDoc).each(function(i,xmlNode){
			var node = $(xmlNode);
			var shapeId = node.attr('shapeId');
			var pointIndex = node.attr('pointIndex');
			var lineId = node.attr('lineId');
			var direction = node.attr('direction');
			
			var shape = gEAPPWorkflow.getNodeById(shapeId);
			if(!shape) {return false;}
			//添加关系到数组
			shape.innerLine.addLine(pointIndex,lineId,direction);
			
		});
		
		
				
		$('shape',xmlDoc).each(function(i,xmlNode){
			var node = $(xmlNode);
			shapeId = node.attr('id');
			var shape = gEAPPWorkflow.getNodeById(shapeId);
			if(!shape) {return false;}
			
			//解释属性
			parseAttributeXML(shape,'attributes',node);
			
		});

		//恢复id索引值 
		setIdIndex($('id-index',xmlDoc).text());
		 
 		//小标数组
		resizeFlagPoints = new Array();
		
		//添加 10个小标对象
		for (var i = 0; i < 10; i++) {
			var newFlag = new EAPP.Workflow.Flag(i);
			newFlag.domElement = document.getElementById('p'+i);
			newFlag.element = $(newFlag.domElement);
			childNodes.push(newFlag);
			resizeFlagPoints.push(newFlag.domElement);
			
		};
		
		
		resizeFlagPoints = getResizePoints();
		
		//隐藏所有的小标
		setPointResizerVisible(false);
		setPointResizerForLineVisible(false);
		
		//当前选择的图元类型
		selectedShapeType = "";
		//设置图元类型为空
		selectShape(null);
		
		//鼠标经过的图元
		mouseOverShape = null;
		//鼠标经过的小标
		mouseOverResizeFlag = null;
		
		//设置 最后被选择的标记点
		lastFlagID = null;
		//当前是否可拖拽
		isDragapproved = false;
		
		//上次鼠标坐标
		lastMouseX = 0;
		lastMouseY = 0;
		
		//上次相对工作区坐标
		lastRelactiveX = 0;
		lastRelactiveY = 0;
		
		//正在绘制的对象元素
		drawingShapeElement = null;
		//选中的图形元素
		checkedShapeElement = null;
		//选中的DOM元素（激发事件的元素）
		checkedObjectElemnt = null;
		
		 
		//已经修改
		this.hasChanged = false;
		//workTitlePanel.html("流程名称：新建流程，状态：未启用");
		 
	};
	
	function parseAttributeXML(parent,parentExpression,node){

		
		var queryString = parentExpression + ">attribute";
		var attributeArray = $(queryString,node);

		//如果没找到子属性，则返回
		if(!attributeArray || !attributeArray.length){return false;}
		
		for(var i=0; i<attributeArray.length; i++){
			//获取当前属性对象
			var attrNode = $(attributeArray[i]);

			
			
			var attrName = attrNode.attr('name');
			var attrValue = $(">value",attrNode).text();
			var attrType = attrNode.attr('typeName');
			var isDynamicCreate = attrNode.attr('isDynamicCreate');

			
			//获取当前属性
			var attr = getAttribute(parent,attrName);
			attr.value = attrValue;

			
			//递归
			parseAttributeXML(attr,"",attrNode);

		}
	}//end  parseAttributeXML
	
};

function setEntityTextBoxStyle(TextBox)
{
	if(!TextBox || typeof(TextBox.contentEditable) == "undefined"){return false;}
    TextBox.contentEditable=false;
    TextBox.style.cursor = "default";
};

/**
 * EAPP.Workflow 的属性对象
 * date 2008年9月23日
 * 本文档生成采用http://jsdoc.sourceforge.net/的生成工具
 */




/**
 * 工作流图形化属性命名空间
 */
EAPP.Workflow.Graphic.Attribute = {};


/** 
 * 创建一个属性基类实例
 * @class 工作流图元属性基类
 * @type EAPP.Workflow.Graphic.Attribute.BaseAttribute
 * @constructor
 */
EAPP.Workflow.Graphic.Attribute.BaseAttribute = function(){

	/**
	 * 当前对象类型名称
	 * @type String
	 */
	this.typeName = "EAPP.Workflow.Graphic.Attribute.BaseAttribute";
	
	/**
	 * 属性名称
	 * @type String
	 */
	this.attributeName = null;
	
	/**
	 * 相关联的图元
	 * @type Object
	 */
	this.shape = null;
	
	/**
	 * 相关联的图元Id
	 * @type String
	 */
	this.shapeId = null;
	
	/**
	 * 当前属性的父属性
	 */
	this.parent = null;
	
	/**
	 * 界面上的显示名称
	 * @type String
	 */
	this.displayName = null;
	
	/**
	 * 显示名称的类型
	 * 1.direct (默认)
	 * 2.child 包含子级显示名称
	 * 3.ref 引用其它对象，本身存储访问表达式
	 */
	this.displayNameType = "direct";
	
	/**
	 * 显示名称引用对象
	 * @type String
	 */
	this.displayNameRefObj = null;
	
	/**
	 * 在界面上的显示索引
	 * 先后的位置
	 */
	this.displayIndex = null;	
	
	/**
	 * 映射的工作流引擎xml对应的节点名称或属性名称
	 * @type String
	 */
	this.mapName = null;
	
	/**
	 * 映射的工作流引擎xml的类型
	 * 1.attribute 元素属性
	 * 2.child-node 映射为子结点
	 * 3.none 当前属性不生成xml
	 */
	this.mapType = "none";
	
	/**
	 * 能否为空
	 * (默认可为空)
	 * @type Boolean
	 */
	this.isNull = true;
	
	/**
	 * 是否在界面上可见
	 * (默认可见)
	 * @type Boolean
	 */
	this.isVisible = true;
	
	/**
	 * 是否启用
	 * (默认启用)
	 * @type Boolean
	 */
	this.isEnable = true;
	
	/**
	 * 验证的类型
	 * 1.regExp 正则表达式
	 * 2.method 方法验证
	 * 3.none 不进行验证(默认)
	 * @type String
	 */
	this.validType = "none";
	
	/**
	 * 设置验证的正则表达式
	 * @type String
	 */
	this.regExp = null;
	
	/**
	 * 验证方法名称
	 * @type String
	 */
	this.validMethod = null;
	
	/**
	 * 数据的输入方式
	 * 1.input 直接从页面上输入项获取
	 *   input_text 文本框
	 *   input_textarea 文本区
	 *   input_select 选择框
	 *   input_multi_select
	 *   input_checkbox 
	 *   input_multi_checkbox
	 *   input_date 输入日期
	 * 2.html 提供html字符串来生成输入元素
	 * 3.method 提供方法获取返回值
	 * 4.child 当前属性没有值，只包含子属性
	 * 5.none 无输入项，预设为默认值
	 * 6.readonly 只显示，界面上不能修改
	 * 7.diplaynone 完全不显示在界面上，如果有子属性只显示子属性，不显示当前项的标题
	 * @type String
	 */
	this.inputType = null;
	
	/**
	 * 输入的最大长度
	 */
	this.maxLength = null;
	
	/**
	 * 有子属性的情况下，是否隐藏标题行
	 * (默认标题行可见)
	 * @type Boolean
	 */
	this.isHideHeader = false;
	
	/**
	 * 标题行是否为一个引用值
	 * 1.displayName 的值(默认)
	 * 2.getDisplayName get出来的dipslayName
	 */
	this.headerType = "displayName";
	
	/**
	 * 获取输入项的字符串
	 * @type String
	 */
	this.inputString = null;
	
	/**
	 * 数据源
	 * 将根据输入的方式不同，解析数据源
	 */	
	this.dataSource = null;
	
	/**
	 * 数据源的类型
	 * 将根据输入的不同，进行不同的解析
	 * 1.direct 直接赋值(默认)
	 * 2.method 调用方法获取
	 * 3.url  	从url上获取数据源(暂时不支持)
	 * 4.none   不提供数据源
	 * @type String
	 */
	this.dataSourceType = "direct";
	
	/**
	 * 属性值的数据类型
	 * 1.integer 整型
	 * 2.float   浮点型
	 * 3.boolean 布尔型
	 * 4.string  字符串
	 * 5.cdata   CDATA类型数据
	 * 6.date    日期型 yy-MM-dd
	 * 7.time    时间   hh:mm:ss
	 * 8.full-time 完整的时间 yy-MM-dd hh:mm:ss
	 * 9.child   只是含有子对象，本身没有值
	 * 10.none    不包含任何属性值(使用不到)
	 * @type String
	 */
	this.dataType = null;
	
	/**
	 * 属性的取值方式
	 * 1.direct 直接赋值(默认)
	 * 2.ref 引用当前父属性的同级属性；value 保存所引用的属性名称
	 *  	 引用顶级图元对象的属性；value 保存所引用属性的相对图元的访问名称
	 * 		 引用其它图元或对象的属性，value 保存其它图元的id#引用属性相对路径
	 * 3.direct-ref 直接采用引用对象的值
	 * //4.ref-relative-attribute 引用当前属性的相对路径属性,value 保存相对路径
	 * 3.method 通过调用一个方法来获取属性值，value 保存要调用的方法名
	 * @type String
	 */
	this.getValueType = 'direct';
	
	/**
	 * 生成xml的方式
	 * 1.direct 
	 * 2.getXmlString（默认方式）
	 * 3.customXmlString(自定义方式生成)
	 * @type
	 */
	this.getXmlType = "getXmlString";
	
	/**
	 * 是否挂起，阻止子属性生成xml
	 */
	this.isSuspendChildXml = false;
	
	/**
	 * 即使值为空也生成xml
	 */
	this.isForceSaveXml = false;
	
	/**
	 * 是否在运行时动态创建的
	 * 如果是将在保存xml时保存除attributeArray外的所有属性
	 * 默认为false
	 * @type Boolean
	 */
	this.isDynamicCreate = false;
	
	/**
	 * 界面显示的值
	 */
	this.displayValue = null;
	
	/**
	 * 当前属性的值
	 * 实际类型将根据输入类型的不同决定 
	 */
	this.value = "";
	
	/**
	 * 属性值所引用的对象
	 */
	this.refObject = "";
	
	/**
	 *  当前属性值改变时，调用的方法名
	 *  @type String
	 */
	this.valueChanagedMethod = null;
	
	/**
	 * 子属性数组
	 * @type Array
	 */
	this.attributeArray = null;
	
	/**
	 * 获取显示名称
	 */
	this.getDisplayName = function(){
	/**
	 * 显示名称的类型
	 * 1.direct  
	 * 2.child 包含子级显示名称
	 * 3.ref 引用其它对象，本身存储访问表达式
	 */
		if(this.displayNameType == "direct"){
			return this.displayName;
			
		}else if(this.displayNameType == "child"){
			return this.displayNameRefObj;
			
		}else if(this.displayNameType == "ref"){
			
			//如果当前引用对象或属性值为空，则返回空字符串
			if(!this.displayNameRefObj || !this.displayName){return "";}
			
			//获取内部路径表达式
			var beginIndex = this.displayName.indexOf('%');
			var endIndex =  this.displayName.lastIndexOf('%');
			var preString = this.displayName.substring(0,beginIndex);
			var backString = this.displayName.substring(endIndex+1);
			
			var expression = this.displayName.substring(beginIndex+1,endIndex);
			var refAttributeValue = "";
			
			var refAttribute = getAttribute(this.displayNameRefObj,expression);
			if(refAttribute){refAttributeValue = refAttribute.getValue();}
			
			var realValue = preString + refAttributeValue + backString;
			return realValue;
			
		}else if(this.getValueType == "method"){
			return eval("method" + '(this)');//传递当前属性对象给函数
		}
	};
	
	/**
	 * 获取当前属性的值
	 */
	this.getValue = function(real){
		
		if(!real && this.displayValue){
			return this.displayValue?this.displayValue:"";
			
		}else if(this.getValueType == "direct"){
			return this.value?this.value:"";//如果值为空，返回空字符串
			
		}else if(this.getValueType == "direct-ref"){
			//如果当前引用对象或属性值为空，则返回空字符串
			if(!this.refObject || !this.refObject.getValue(true)){return "";}
			return this.refObject.getValue(true);
		}else if(this.getValueType == "ref"){
			
			//如果当前引用对象或属性值为空，则返回空字符串
			if(!this.refObject || !this.value){return "";}
			
			//获取内部路径表达式
			var beginIndex = this.value.indexOf('%');
			var endIndex =  this.value.lastIndexOf('%');
			var preString = this.value.substring(0,beginIndex);
			var backString = this.value.substring(endIndex+1);
			
			var expression = this.value.substring(beginIndex+1,endIndex);
			var refAttribute = getAttribute(this.refObject,expression);
			if(!refAttribute){return null;}
			
			var realValue = preString + refAttribute.getValue() + backString;
			return realValue;
			
		}else if(this.getValueType == "method"){
			return eval("method" + '(this)');//传递当前属性对象给函数
		}
	};
	
	/**
	 * 检查当前属性值是否为空
	 * 为空返回true,否则返回false
	 */
	this.checkNull = function(){
		return this.getValue(true)?false:true;
	};
	
	/**
	 * 生成xml字符串
	 * @type String
	 * 返回生成的xml字符串
	 */
	this.getXmlString = function(){
		//无效属性只接返回
		if(!this.isEnable){return "";}
		
		if(this.mapType == "none"){return "";}
		
		//如果采用自定义方法生成
		if(this.getXmlType == "customXmlString"){
			return this.getCustomeXmlString();
		}
		
		//获取当前的属性值
		var realValue = this.getValue(true);
		
		//如果当前属性值为空，则返回空字符串
		if(!realValue && !this.isForceSaveXml){return "";}
		
		switch(this.dataType){
			case "cdata":
				realValue = "<![CDATA[" + realValue  +  "]]>" ;
				break;
			case "string":
				break;
			default:
				break;
		}
		
		return realValue?realValue:"";
	};
	
	/**
	 * 用户定制生成xml的方法
	 */
	this.getCustomeXmlString = function(){
		return null;
		//return this.getXmlString();
	};
	
	
};


/** 
 * 创建一个常规属性实例
 * @class 工作流图元常规属性类
 * @type EAPP.Workflow.Graphic.Attribute.NormalAttribute
 * @constructor
 */
EAPP.Workflow.Graphic.Attribute.NormalAttribute = function() {

	//继承自基属性类
	EAPP.Workflow.Graphic.Attribute.BaseAttribute.call(this);
	
	/**
	 * 当前对象类型名称
	 * @type String
	 */
	this.typeName = "EAPP.Workflow.Graphic.Attribute.NormalAttribute";
	
	/**
	 * 子属性数组
	 * @type Array
	 */
	this.attributeArray = new Array();
	
	/**
	 * 获取显示名称
	 */
	this.getDisplayName = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getDisplayName.apply(this);
	};
	
	/**
	 * 获取当前属性的值
	 */
	this.getValue = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getValue.apply(this); 
	};
	
	/**
	 * 生成xml字符串
	 * @type String
	 * 返回生成的xml字符串
	 */
	this.getXmlString = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getXmlString.apply(this); 
	};	
	
	/**
	 * 用户定制生成xml的方法
	 */
	this.getCustomeXmlString = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getXmlString.apply(this); 
	};	
	
};

//继承原型链
EAPP.Workflow.Graphic.Attribute.NormalAttribute.prototype = new EAPP.Workflow.Graphic.Attribute.BaseAttribute();




/** 
 * 创建一个工作流属性类实例
 * @class 工作流属性类
 * @type EAPP.Workflow.Graphic.Attribute.FlowAttribute
 * @constructor
 */
EAPP.Workflow.Graphic.Attribute.Workflow = function() {

	//继承自基属性类
	EAPP.Workflow.Graphic.Attribute.BaseAttribute.call(this);
	
	
	/**
	 * 当前对象类型名称
	 * @type String
	 */
	this.typeName = "EAPP.Workflow.Graphic.Attribute.Workflow";
	
	/**
	 * 属性名称
	 * @type String
	 */
	this.attributeName = "workflow";
	
	/**
	 * 界面上的显示名称
	 * @type String
	 */
	this.displayName = "工作流";

	
	/**
	 * 映射的工作流引擎xml对应的节点名称或属性名称
	 * @type String
	 */
	this.mapName = "flow-define";
	
	/**
	 * 映射的工作流引擎xml的类型
	 * 1.child-node 映射为子结点
	 * 2.attribute 元素属性
	 */
	this.mapType = "child-node";
	
	
	/**
	 * 子属性数组
	 * @type Array
	 */
	this.attributeArray = new Array();
	
		//名称属性
		var attr_name = new EAPP.Workflow.Graphic.Attribute.Name();
		//attr_name.attributeName = "name";
		attr_name.displayName = '流程名称';
		attr_name.value = '新建流程';//默认值
		attr_name.maxLength = 50;
		
		//flow-key属性
		/**
		 * flow-key属性
		 * 流程Key（36位ID）（只读）
		 * 隐藏
		 * 由流程引擎生成
		 * 新建时默认为空，修改时从配置文件加载
		 */
		var attr_flow_key = new EAPP.Workflow.Graphic.Attribute.NormalAttribute();
		attr_flow_key.attributeName = 'flow-key';
		attr_flow_key.displayName = 'flow-key';
		attr_flow_key.mapName = 'flow-key';
		attr_flow_key.mapType = 'attribute';
		attr_flow_key.isVisible = false;
		attr_flow_key.inputType = 'none';
		attr_flow_key.dataType = 'string';
		attr_flow_key.isForceSaveXml = true;
		
		//分类属性
		//（目前不可用）
		var attr_category = new EAPP.Workflow.Graphic.Attribute.NormalAttribute();
		attr_category.attributeName = 'category';
		attr_category.displayName = '分类';
		attr_category.mapName = 'category';
		attr_category.mapType = 'attribute';
		attr_category.inputType = 'input_text';
		attr_category.dataType = 'string';
		
		//状态属性
		/**
		 * 状态属性
		 * 下拉框选择
		 * 取值：未发布 | 启用 | 禁用[NO_ENABLE | ENABLED | DISABLED]
		 */
		var attr_state = new EAPP.Workflow.Graphic.Attribute.NormalAttribute();
		attr_state.attributeName = 'state';
		attr_state.displayName = '流程状态';
		attr_state.mapName = 'state';
		attr_state.mapType = 'attribute';
		attr_state.dataType = 'string';
		//attr_state.dataSourceType = 'direct';
		//attr_state.dataSource = new Array('未启用','启用','禁用'); 
		
		//initial属性
		//暂时不支持
		var attr_initial = new EAPP.Workflow.Graphic.Attribute.NormalAttribute();
		attr_initial.attributeName = 'initial';
		attr_initial.displayName = 'initial';
		attr_initial.mapName = 'initial';
		attr_initial.mapType = 'attribute';
		attr_initial.inputType = 'input_text';
		attr_initial.dataType = 'string';
		
		//版本属性
		/**
		 * 版本属性
		 * long类型（只读）
		 * 隐藏
		 * 由流程引擎生成
		 * 新建时默认为空，修改时从配置文件加载
		 */
		var attr_version = new EAPP.Workflow.Graphic.Attribute.NormalAttribute();
		attr_version.attributeName = 'version';
		attr_version.displayName = '版本';
		attr_version.mapName = 'version';
		attr_version.mapType = 'attribute';
		attr_version.inputType = 'readonly';
		attr_version.dataType = 'string';
		attr_version.isForceSaveXml = true;
		
		//说明属性
		var attr_description = new EAPP.Workflow.Graphic.Attribute.Description();
		attr_description.displayName ="流程描述";
		
		//流程启动事件
		var attr_event_flow_start = new EAPP.Workflow.Graphic.Attribute.Event();
		attr_event_flow_start.attributeName ="event_flow_start";
		attr_event_flow_start.displayName = "流程开始事件";
		
		alterAttribute(attr_event_flow_start,'type','FLOW_START');
		hideAttribute(attr_event_flow_start,"type");
		alterAttribute(attr_event_flow_start,'action#name','流程启动事件动作');
		alterAttributeProperty(attr_event_flow_start,'action','displayName','流程启动事件动作');
		
		//流程结束事件
		var attr_event_flow_end = new EAPP.Workflow.Graphic.Attribute.Event();
		attr_event_flow_end.attributeName ="event_flow_end";
		attr_event_flow_end.displayName = "流程结束事件";
		
		alterAttribute(attr_event_flow_end,'type','FLOW_END');
		hideAttribute(attr_event_flow_end,"type");
		alterAttribute(attr_event_flow_end,'action#name','流程结束事件动作');
		alterAttributeProperty(attr_event_flow_end,'action','displayName','流程结束事件动作');
		
		//添加到工作流属性数组
		this.attributeArray.push(attr_name);
		this.attributeArray.push(attr_flow_key);
		this.attributeArray.push(attr_category);
		this.attributeArray.push(attr_state);
		this.attributeArray.push(attr_initial);
		this.attributeArray.push(attr_version);
		this.attributeArray.push(attr_description);
		this.attributeArray.push(attr_event_flow_start);
		this.attributeArray.push(attr_event_flow_end);
		
	/**
	 * 获取显示名称
	 */
	this.getDisplayName = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getDisplayName.apply(this);
	};
	
	/**
	 * 获取当前属性的值
	 */
	this.getValue = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getValue.apply(this); 
	};
	
	/**
	 * 生成xml字符串
	 * @type String
	 * 返回生成的xml字符串
	 */
	this.getXmlString = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getXmlString.apply(this); 
	};	
	
	/**
	 * 用户定制生成xml的方法
	 */
	this.getCustomeXmlString = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getXmlString.apply(this); 
	};	
	
};

//继承原型链
EAPP.Workflow.Graphic.Attribute.Workflow.prototype = new EAPP.Workflow.Graphic.Attribute.BaseAttribute();

/** 
 * 创建一个 Task 属性
 * @class Task 属性类
 * @type EAPP.Workflow.Graphic.Attribute.Task
 * @constructor
 */
EAPP.Workflow.Graphic.Attribute.Task = function(){
	

	//继承自基属性类
	EAPP.Workflow.Graphic.Attribute.BaseAttribute.call(this);
	
	
	/**
	 * 当前对象类型名称
	 * @type String
	 */
	this.typeName = "EAPP.Workflow.Graphic.Attribute.Task";
	
	/**
	 * 属性名称
	 * @type String
	 */
	this.attributeName = "task";
	
	/**
	 * 界面上的显示名称
	 * @type String
	 */
	this.displayName = "任务";

	
	/**
	 * 映射的工作流引擎xml对应的节点名称或属性名称
	 * @type String
	 */
	this.mapName = "task";
	
	/**
	 * 映射的工作流引擎xml的类型
	 * 1.child-node 映射为子结点
	 * 2.attribute 元素属性
	 */
	this.mapType = "child-node";
	
	
	/**
	 * 子属性数组
	 * @type Array
	 */
	this.attributeArray = new Array();
	
	/**
	 * 名称属性
	 */
	var attr_name = new EAPP.Workflow.Graphic.Attribute.NormalAttribute();
	attr_name.attributeName = 'name';
	attr_name.displayName = '任务名称';
	attr_name.mapName = 'name';
	attr_name.mapType = 'attribute';
	attr_name.inputType = 'input_text';
	attr_name.dataType = 'string';
	
	/**
	 * 任务执行顺序
	 */
    var	attr_excute_order = new EAPP.Workflow.Graphic.Attribute.NormalAttribute();
	attr_excute_order.attributeName = 'execute-order';
	attr_excute_order.displayName = '执行顺序';
	attr_excute_order.mapName = 'execute-order';
	attr_excute_order.mapType = 'attribute';
	attr_excute_order.inputType = 'input_text';
	attr_excute_order.dataType = 'number';
	
	/**
	 * 到期时间
	 * [yyyy-MM-dd | yyyy-MM-dd HH:mm | yyyy-MM-dd HH:mm:ss]
	 */
    var	attr_due_date = new EAPP.Workflow.Graphic.Attribute.NormalAttribute();
	attr_due_date.attributeName = 'due-date';
	attr_due_date.displayName = '到期时间';
	attr_due_date.mapName = 'due-date';
	attr_due_date.mapType = 'attribute';
	attr_due_date.inputType = 'input_date';//还得加上日期输入框
	attr_due_date.dataType = 'date';
	
	/**
	 * 任务优先级
	 * [100 | 200 | 300 | 400 | 500]
	 */
    var	attr_priority = new EAPP.Workflow.Graphic.Attribute.NormalAttribute();
	attr_priority.attributeName = 'priority';
	attr_priority.displayName = '优先级';
	attr_priority.mapName = 'priority';
	attr_priority.mapType = 'attribute';
	attr_priority.inputType = 'input_select';
	attr_priority.dataType = 'string';
	// 此处所提供的dataSource 有bug
	//attr_priority.dataSource = new Array('lowest','low','normal','high','highest');
	//attr_priority.dataSourceType = 'direct';
	
	/**
	 * 任务类型
	 * 单例｜多人并行｜多人串行
	 * [single | parallel | serial]
	 */
	var attr_multi_type =  new EAPP.Workflow.Graphic.Attribute.NormalAttribute();
	attr_multi_type.attributeName = "multi-type";
	attr_multi_type.displayName = "任务类型";
	attr_multi_type.mapName = "multi-type";
	attr_multi_type.mapType = 'attribute';
	attr_multi_type.inputType = 'input_select';
	attr_multi_type.dataType = 'string';
	attr_multi_type.dataSourceType = 'direct';
	attr_multi_type.dataSource = {"单人":"single","多人并行":"parallel","多人串行":"serial"};
	
	//说明属性
	var attr_description = new EAPP.Workflow.Graphic.Attribute.Description();
	
	//view-binding 属性
	var attr_view_binding = new EAPP.Workflow.Graphic.Attribute.ViewBinding();
	
	//assignment 属性
	var attr_assignment = new EAPP.Workflow.Graphic.Attribute.Assignment();
	
	
	//任务创建事件
	var attr_event_task_create = new EAPP.Workflow.Graphic.Attribute.Event();
	attr_event_task_create.attributeName = "event_task_create";
	attr_event_task_create.displayName = "任务创建事件";

	alterAttribute(attr_event_task_create,'type','TASK_CREATE');
	var attr_event_task_create_type = getAttribute(attr_event_task_create,"type");
	attr_event_task_create_type.isVisible = false;
	
	alterAttributeProperty(attr_event_task_create,'action','displayName','任务创建事件动作');
	
	
	//任务分派事件
	var attr_event_task_assign = new EAPP.Workflow.Graphic.Attribute.Event();
	attr_event_task_assign.attributeName = "event_task_assign";
	attr_event_task_assign.displayName = "任务分派事件";
	
	alterAttribute(attr_event_task_assign,'type','TASK_ASSIGN');
	var attr_event_task_assign_type = getAttribute(attr_event_task_assign,"type");
	attr_event_task_assign_type.isVisible = false;
	
	alterAttributeProperty(attr_event_task_assign,'action','displayName','任务分派事件动作');
	
	
	//任务开始（启用）事件
	var attr_event_task_start = new EAPP.Workflow.Graphic.Attribute.Event();
	attr_event_task_start.attributeName = "event_task_start";
	attr_event_task_start.displayName = "任务开始（启用）事件";
	
	alterAttribute(attr_event_task_start,'type','TASK_START');
	var attr_event_task_start_type = getAttribute(attr_event_task_start,"type");
	attr_event_task_start_type.isVisible = false;
	
	alterAttributeProperty(attr_event_task_start,'action','displayName','任务开始事件动作');
	
	//任务取消事件
	var attr_event_task_giveup = new EAPP.Workflow.Graphic.Attribute.Event();
	attr_event_task_giveup.attributeName = "event_task_giveup";
	attr_event_task_giveup.displayName = "任务取消事件";
	
	alterAttribute(attr_event_task_giveup,'type','TASK_GIVEUP');
	var attr_event_task_giveup_type = getAttribute(attr_event_task_giveup,"type");
	attr_event_task_giveup_type.isVisible = false;
	
	alterAttributeProperty(attr_event_task_giveup,'action','displayName','任务取消事件动作');
	
	//任务结束事件
	var attr_event_task_end = new EAPP.Workflow.Graphic.Attribute.Event();
	attr_event_task_end.attributeName = "event_task_end";
	attr_event_task_end.displayName = "任务结束事件";
	
	alterAttribute(attr_event_task_end,'type','TASK_END');
	var attr_event_task_end_type = getAttribute(attr_event_task_end,"type");
	attr_event_task_end_type.isVisible = false;
	
	alterAttributeProperty(attr_event_task_end,'action','displayName','任务结束事件动作');
	
	
	//任务通知事件
	var attr_event_task_notify = new EAPP.Workflow.Graphic.Attribute.Event();
	attr_event_task_notify.attributeName = "event_task_notify";
	attr_event_task_notify.displayName = "任务通知事件";
	
	alterAttribute(attr_event_task_notify,'type','TASK_NOTIFY');
	var attr_event_task_notify_type = getAttribute(attr_event_task_notify,"type");
	attr_event_task_notify_type.isVisible = false;	
	alterAttributeProperty(attr_event_task_notify,'action','displayName','任务通知事件动作');
	
	
	//添加到当前属性数组
	this.attributeArray.push(attr_name);
	this.attributeArray.push(attr_excute_order);
	this.attributeArray.push(attr_due_date);
	this.attributeArray.push(attr_priority);
	this.attributeArray.push(attr_multi_type);
	this.attributeArray.push(attr_description);
	this.attributeArray.push(attr_view_binding);
	this.attributeArray.push(attr_assignment);
	this.attributeArray.push(attr_event_task_create);
	this.attributeArray.push(attr_event_task_notify);
	this.attributeArray.push(attr_event_task_assign);
	this.attributeArray.push(attr_event_task_start);
	this.attributeArray.push(attr_event_task_giveup);
	this.attributeArray.push(attr_event_task_end);
	
	/**
	 * 获取显示名称
	 */
	this.getDisplayName = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getDisplayName.apply(this);
	};
	
	/**
	 * 获取当前属性的值
	 */
	this.getValue = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getValue.apply(this); 
	};
	
	/**
	 * 生成xml字符串
	 * @type String
	 * 返回生成的xml字符串
	 */
	this.getXmlString = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getXmlString.apply(this); 
	};	
	
	/**
	 * 用户定制生成xml的方法
	 */
	this.getCustomeXmlString = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getXmlString.apply(this); 
	};	
	
};


	
//继承原型链
EAPP.Workflow.Graphic.Attribute.Task.prototype = new EAPP.Workflow.Graphic.Attribute.BaseAttribute();




/** 
 * 创建一个 ViewBinding 属性
 * @class ViewBinding 属性类
 * @type EAPP.Workflow.Graphic.Attribute.ViewBinding
 * @constructor
 */
EAPP.Workflow.Graphic.Attribute.ViewBinding = function(){


	//继承自基属性类
	EAPP.Workflow.Graphic.Attribute.BaseAttribute.call(this);
	
		
	/**
	 * 当前对象类型名称
	 * @type String
	 */
	this.typeName = "EAPP.Workflow.Graphic.Attribute.ViewBinding";
	
	/**
	 * 属性名称
	 * @type String
	 */
	this.attributeName = "view-binding";
	
	/**
	 * 界面上的显示名称
	 * @type String
	 */
	this.displayName = "视图绑定";
	
	/**
	 * 显示名称的类型
	 * 1.direct (默认)
	 * 2.child 包含子级显示名称
	 * 3.ref 引用其它对象，本身存储访问表达式
	 */
	this.displayNameType = "child";
	
	/**
	 * 显示名称引用对象
	 * @type String
	 */
	this.displayNameRefObj = "表达式";
	
	
	/**
	 * 映射的工作流引擎xml对应的节点名称或属性名称
	 * @type String
	 */
	this.mapName = "view-binding";
	
	/**
	 * 映射的工作流引擎xml的类型
	 * 1.child-node 映射为子结点
	 * 2.attribute 元素属性
	 */
	this.mapType = "child-node";
	
	/**
	 * 数据的输入方式
	 * 1.input 直接从页面上输入项获取
	 *   input_text 文本框
	 *   input_select 选择框
	 *   input_multi_select
	 *   input_checkbox 
	 *   input_multi_checkbox
	 *   input_date 输入日期
	 * 2.method 提供方法获取返回值
	 * 3.child 当前属性没有值，只包含子属性
	 * 4.none 无输入项，预设为默认值
	 * @type String
	 */
	this.inputType = 'input_text';
	
	/**
	 * 获取输入值的方法名
	 * @type String
	 */
	this.inputString = "";//BUG 获取输入该项的方法没有实现
	
	
	/**
	 * 属性值的数据类型
	 * 1.string 字符串
	 * 2.cdata CDATA类型数据
	 * 3.date 日期型
	 * 4.number 数值型
	 * 5.boolean 布尔型
	 * 6.child 只是含有子对象，本身没有值
	 * @type String
	 */
	this.dataType = 'cdata';
	
	/**
	 * 生成xml的方式
	 * 1.direct 
	 * 2.getXmlString（默认方式）
	 * 3.customXmlString(自定义方式生成)
	 * @type
	 */
	this.getXmlType = "getXmlString";
	
	//管理本属性及子结点xml的生成
	this.isSuspendChildXml = true;
	
	/**
	 * 子属性数组
	 * @type Array
	 */
	this.attributeArray = new Array();
	
	//class属性
	var attr_class = new EAPP.Workflow.Graphic.Attribute.Class();
	this.attributeArray.push(attr_class);
	
	//绑定类型(虚拟操作属性)
	var attr_binding_type = new EAPP.Workflow.Graphic.Attribute.NormalAttribute();
	attr_binding_type.attributeName = "binding-type";
	attr_binding_type.displayName = "绑定类型";
	attr_binding_type.mapType = "none";
	attr_binding_type.inputType = "input_select";
	attr_binding_type.dataSource = {"表达式":"expression","程序":"program"};
	attr_binding_type.valueChanagedMethod = "bingdingTypeChanage";
	
	//添加到委派属性对象
	this.attributeArray.unshift(attr_binding_type);
	
		
	/**
	 * 获取显示名称
	 */
	this.getDisplayName = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getDisplayName.apply(this);
	};
	
	/**
	 * 获取当前属性的值
	 */
	this.getValue = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getValue.apply(this); 
	};
	
	/**
	 * 生成xml字符串
	 * @type String
	 * 返回生成的xml字符串
	 */
	this.getXmlString = function(){
		var classString =" class=\"" +  attr_class.getXmlString() + "\"";
		var innerValue = this.getValue(true);
		
		if((attr_binding_type.getValue(true) == "expression") && innerValue){
			//2008年11月29日13时49分12秒 修正表达式不加引号，应当在界面上手动输入 ""
			return "\n\t\t\t<" + this.mapName + "><![CDATA[" + innerValue + "]]></" + this.mapName +">";
		}else if((attr_binding_type.getValue(true) == "program") && classString){
			return "\n\t\t\t<" + this.mapName + classString + "></" + this.mapName +">";
		}else{
			return "";
		}
	};	
	
	
	/**
	 * 用户定制生成xml的方法
	 */
	this.getCustomeXmlString = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getXmlString.apply(this); 
	};	
	
};

//继承原型链
EAPP.Workflow.Graphic.Attribute.ViewBinding.prototype = new EAPP.Workflow.Graphic.Attribute.BaseAttribute();


/** 
 * 创建一个 Assignment 属性
 * @class Assignment 属性类
 * @type EAPP.Workflow.Graphic.Attribute.Assignment
 * @constructor
 */
EAPP.Workflow.Graphic.Attribute.Assignment = function(){
	

	//继承自基属性类
	EAPP.Workflow.Graphic.Attribute.BaseAttribute.call(this);
	
	
	/**
	 * 当前对象类型名称
	 * @type String
	 */
	this.typeName = "EAPP.Workflow.Graphic.Attribute.Assignment";
	
	/**
	 * 属性名称
	 * @type String
	 */
	this.attributeName = "assignment";
	
	/**
	 * 界面上的显示名称
	 * @type String
	 */
	this.displayName = "assignment";
	
	/**
	 * 显示名称的类型
	 * 1.direct (默认)
	 * 2.child 包含子级显示名称
	 * 3.ref 引用其它对象，本身存储访问表达式
	 */
	this.displayNameType = "child";
	
	/**
	 * 显示名称引用对象
	 * @type String
	 */
	this.displayNameRefObj = "表达式";
	
	/**
	 * 映射的工作流引擎xml对应的节点名称或属性名称
	 * @type String
	 */
	this.mapName = "assignment";
	
	/**
	 * 映射的工作流引擎xml的类型
	 * 1.child-node 映射为子结点
	 * 2.attribute 元素属性
	 */
	this.mapType = "child-node";
	
	//管理本属性及子结点xml的生成
	this.isSuspendChildXml = true;
	
	/**
	 * 数据的输入方式
	 * 1.input 直接从页面上输入项获取
	 *   input_text 文本框
	 *   input_select 选择框
	 *   input_multi_select
	 *   input_checkbox 
	 *   input_multi_checkbox
	 *   input_date 输入日期
	 * 2.method 提供方法获取返回值
	 * 3.child 当前属性没有值，只包含子属性
	 * 4.none 无输入项，预设为默认值
	 * @type String
	 */
	this.inputType = 'input_text';
	
	
	/**
	 * 获取输入值的方法名
	 * @type String
	 */
	this.inputString = null;//BUG
	
	
	
	
	/**
	 * 属性值的数据类型
	 * 1.string 字符串
	 * 2.cdata CDATA类型数据
	 * 3.date 日期型
	 * 4.number 数值型
	 * 5.boolean 布尔型
	 * 6.child 只是含有子对象，本身没有值
	 * @type String
	 */
	this.dataType = 'cdata';
	
	/**
	 * 子属性数组
	 * @type Array
	 */
	this.attributeArray = new Array();
	
	//class属性
	var attr_class = new EAPP.Workflow.Graphic.Attribute.Class();
	this.attributeArray.push(attr_class);
	
	//操作者类型(虚拟操作属性)
	var attr_operator_type = new EAPP.Workflow.Graphic.Attribute.NormalAttribute();
	attr_operator_type.attributeName = "operator-type";
	attr_operator_type.displayName = "操作者类型";
	attr_operator_type.mapType = "none";
	attr_operator_type.inputType = "input_select";
	attr_operator_type.dataSource ={"单用户":"single-user","多用户":"user","角色":"role","程序":"program","表达式":"expression"};
	attr_operator_type.valueChanagedMethod = "operatorTypeChanage";
	//添加到委派属性对象
	this.attributeArray.unshift(attr_operator_type);
	
	/**
	 * 获取显示名称
	 */
	this.getDisplayName = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getDisplayName.apply(this);
	};
	
	/**
	 * 获取当前属性的值
	 */
	this.getValue = function(flag){
		if((attr_operator_type.getValue(true) == "single-user")){
			return getAttributeValue(this,'operator_single_user',flag);
		}else if((attr_operator_type.getValue(true) == "user")){
			return getAttributeValue(this,'operator-user',flag);
		}else if((attr_operator_type.getValue(true) == "role")){
			return getAttributeValue(this,'operator-role',flag);
		}else if((attr_operator_type.getValue(true) == "expression")){
			return getAttributeValue(this,'operator_expression',flag);
		}else {
			return "";
		}
		//return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getValue.apply(this); 
	};
	
	/**
	 * 生成xml字符串
	 * @type String
	 * 返回生成的xml字符串
	 */
	this.getXmlString = function(){
		var classString =" class=\"" +  attr_class.getXmlString() + "\"";
		var innerValue = this.getValue(true);
		
		if((attr_operator_type.getValue(true) == "single-user") && innerValue){
			return "\n\t\t\t<" + this.mapName + "><![CDATA[\"ACTOR:" + innerValue + "\"]]></" + this.mapName +">";
		}else if((attr_operator_type.getValue(true) == "user") && innerValue){
			return "\n\t\t\t<" + this.mapName + "><![CDATA[\"POOLEDACTORS:" + innerValue + "\"]]></" + this.mapName +">";
		}else if((attr_operator_type.getValue(true) == "role") && classString){
			return "\n\t\t\t<" + this.mapName + "><![CDATA[\"POOLEDROLES:" + innerValue + "\"]]></" + this.mapName +">";
		}else if((attr_operator_type.getValue(true) == "expression") && classString){
			return "\n\t\t\t<" + this.mapName + "><![CDATA[" + innerValue + "]]></" + this.mapName +">";
		}else if ((attr_operator_type.getValue(true) == "program") && classString) {
			return "\n\t\t\t<" + this.mapName + classString + "></" + this.mapName + ">";
		}else {
			return "";
		}
	};	
	
	/**
	 * 用户定制生成xml的方法
	 */
	this.getCustomeXmlString = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getXmlString.apply(this); 
	};	
};

//继承原型链
EAPP.Workflow.Graphic.Attribute.Assignment.prototype = new EAPP.Workflow.Graphic.Attribute.BaseAttribute();




/** 
 * 创建一个 Event 属性
 * @class Event 属性类
 * @type EAPP.Workflow.Graphic.Attribute.Event
 * @constructor
 */
EAPP.Workflow.Graphic.Attribute.Event = function(){
	

	//继承自基属性类
	EAPP.Workflow.Graphic.Attribute.BaseAttribute.call(this);
	
	
	/**
	 * 当前对象类型名称
	 * @type String
	 */
	this.typeName = "EAPP.Workflow.Graphic.Attribute.Event";
	
	/**
	 * 属性名称
	 * @type String
	 */
	this.attributeName = "event";
	
	/**
	 * 界面上的显示名称
	 * @type String
	 */
	this.displayName = "事件";
	
	/**
	 * 映射的工作流引擎xml对应的节点名称或属性名称
	 * @type String
	 */
	this.mapName = "event";
	
	/**
	 * 映射的工作流引擎xml的类型
	 * 1.child-node 映射为子结点
	 * 2.attribute 元素属性
	 */
	this.mapType = "child-node";
	
	
	/**
	 * 数据的输入方式
	 * 1.input 直接从页面上输入项获取
	 *   input_text 文本框
	 *   input_select 选择框
	 *   input_multi_select
	 *   input_checkbox 
	 *   input_multi_checkbox
	 *   input_date 输入日期
	 * 2.method 提供方法获取返回值
	 * 3.child 当前属性没有值，只包含子属性
	 * 4.none 无输入项，预设为默认值
	 * @type String
	 */
	this.inputType = 'none';
	
	
	/**
	 * 属性值的数据类型
	 * 1.string 字符串
	 * 2.cdata CDATA类型数据
	 * 3.date 日期型
	 * 4.number 数值型
	 * 5.boolean 布尔型
	 * 6.child 只是含有子对象，本身没有值
	 * 7.none
	 * @type String
	 */
	this.dataType = 'none';
	
	
	/**
	 * 子属性数组
	 * @type Array
	 */
	this.attributeArray = new Array();
	
	/**
	 * 事件类型
	 * 是一个枚举类型的值，添加事件时，预先定义好类型
	 * 1.流程事件[FLOW_START | FLOW_END]
	 * 2.节点事件[NODE_ENTER | NODE_LEAVE]
	 * 3.任务事件[TASK_CREATE | TASK_ASSIGN | TASK_START | TASK_GIVEUP | TASK_END]
	 */
	//type属性
	var attr_type = new EAPP.Workflow.Graphic.Attribute.NormalAttribute();
	attr_type.attributeName = 'type';
	attr_type.displayName = '事件类型';
	attr_type.mapName = 'type';
	attr_type.mapType = 'attribute';
	attr_type.inputType = 'input_text';
	attr_type.dataType = 'string';
	
	//事件Action
	var attr_action = new EAPP.Workflow.Graphic.Attribute.Action(); 
	
	//添加到属性数组
	this.attributeArray.push(attr_type);
	this.attributeArray.push(attr_action);
	
	/**
	 * 获取显示名称
	 */
	this.getDisplayName = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getDisplayName.apply(this);
	};
	
	/**
	 * 获取当前属性的值
	 */
	this.getValue = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getValue.apply(this); 
	};
	
	/**
	 * 生成xml字符串
	 * @type String
	 * 返回生成的xml字符串
	 */
	this.getXmlString = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getXmlString.apply(this); 
	};	
	
	/**
	 * 用户定制生成xml的方法
	 */
	this.getCustomeXmlString = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getXmlString.apply(this); 
	};	
};

//继承原型链
EAPP.Workflow.Graphic.Attribute.Event.prototype = new EAPP.Workflow.Graphic.Attribute.BaseAttribute();



/** 
 * 创建一个 Action 属性
 * @class Action 属性类
 * @type EAPP.Workflow.Graphic.Attribute.Action
 * @constructor
 */
EAPP.Workflow.Graphic.Attribute.Action = function(){
	
	
	//继承自基属性类
	EAPP.Workflow.Graphic.Attribute.BaseAttribute.call(this);
	
	/**
	 * 当前对象类型名称
	 * @type String
	 */
	this.typeName = "EAPP.Workflow.Graphic.Attribute.Action";
	
	/**
	 * 属性名称
	 * @type String
	 */
	this.attributeName = "action";
	
	/**
	 * 界面上的显示名称
	 * @type String
	 */
	this.displayName = "Action";
	
	
	/**
	 * 映射的工作流引擎xml对应的节点名称或属性名称
	 * @type String
	 */
	this.mapName = "action";
	
	/**
	 * 映射的工作流引擎xml的类型
	 * 1.child-node 映射为子结点
	 * 2.attribute 元素属性
	 */
	this.mapType = "child-node";
	

	/**
	 * 数据的输入方式
	 * 1.input 直接从页面上输入项获取
	 *   input_text 文本框
	 *   input_select 选择框
	 *   input_multi_select
	 *   input_checkbox 
	 *   input_multi_checkbox
	 *   input_date 输入日期
	 * 2.method 提供方法获取返回值
	 * 3.child 当前属性没有值，只包含子属性
	 * 4.none 无输入项，预设为默认值
	 * @type String
	 */
	this.inputType = 'none';
	
	
	/**
	 * 子属性数组
	 * @type Array
	 */
	this.attributeArray = new Array();
	
	
	//名称属性
	var attr_name = new EAPP.Workflow.Graphic.Attribute.Name();
	
	//class属性
	var attr_class = new EAPP.Workflow.Graphic.Attribute.Class();
	
	//accept-propagated-events属性
	var attr_accept_propagated_events = new EAPP.Workflow.Graphic.Attribute.NormalAttribute();
	attr_accept_propagated_events.attributeName = 'accept-propagated-events';
	attr_accept_propagated_events.displayName = 'accept-propagated-events';
	attr_accept_propagated_events.mapName = 'accept-propagated-events';
	attr_accept_propagated_events.mapType = 'attribute';
	attr_accept_propagated_events.inputType = 'input_checkbox';
	attr_accept_propagated_events.dataType = 'string';
	
	//异步属性
	var attr_is_async = new EAPP.Workflow.Graphic.Attribute.NormalAttribute();
	attr_is_async.attributeName = 'is-async';
	attr_is_async.displayName = '是否异步';
	attr_is_async.mapName = 'is-async';
	attr_is_async.mapType = 'attribute';
	attr_is_async.inputType = 'input_checkbox';
	attr_is_async.dataType = 'boolean';
	
	//ref-name属性
	var attr_ref_name = new EAPP.Workflow.Graphic.Attribute.NormalAttribute();
	attr_ref_name.attributeName = 'ref-name';
	attr_ref_name.displayName = 'ref-name';
	attr_ref_name.mapName = 'ref-name';
	attr_ref_name.mapType = 'attribute';
	attr_ref_name.inputType = 'input_text';
	attr_ref_name.dataType = 'string';
	
 	//临时解决方案 //
	//attr_name.isVisible = false;
	attr_accept_propagated_events.isEnable = false;
	attr_is_async.isEnable = false;
	attr_ref_name.isEnable = false;
	
	
	//添加到当前属性数组
	this.attributeArray.push(attr_name);
	this.attributeArray.push(attr_class);
	this.attributeArray.push(attr_accept_propagated_events);
	this.attributeArray.push(attr_is_async);
	this.attributeArray.push(attr_ref_name);
	
	
	/**
	 * 获取显示名称
	 */
	this.getDisplayName = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getDisplayName.apply(this);
	};
	
	/**
	 * 获取当前属性的值
	 */
	this.getValue = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getValue.apply(this); 
	};
	
	/**
	 * 生成xml字符串
	 * @type String
	 * 返回生成的xml字符串
	 */
	this.getXmlString = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getXmlString.apply(this); 
	};	
	
	/**
	 * 用户定制生成xml的方法
	 */
	this.getCustomeXmlString = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getXmlString.apply(this); 
	};	
};

//继承原型链
EAPP.Workflow.Graphic.Attribute.Action.prototype = new EAPP.Workflow.Graphic.Attribute.BaseAttribute();





/** 
 * 创建一个 Handler 属性
 * @class  Handler 属性类
 * @type EAPP.Workflow.Graphic.Attribute.Handler
 * @constructor
 */
EAPP.Workflow.Graphic.Attribute.Handler = function(){
	
	/**
	 * 当前对象类型名称
	 * @type String
	 */
	this.typeName = "EAPP.Workflow.Graphic.Attribute.Handler";
	
	/**
	 * 属性名称
	 * @type String
	 */
	this.attributeName = "handler";
	
	/**
	 * 界面上的显示名称
	 * @type String
	 */
	this.displayName = "handler";
	
	
	/**
	 * 显示名称的类型
	 * 1.direct (默认)
	 * 2.child 包含子级显示名称
	 * 3.ref 引用其它对象，本身存储访问表达式
	 */
	this.displayNameType = "child";
	
	/**
	 * 显示名称引用对象
	 * @type String
	 */
	this.displayNameRefObj = "表达式";
	
	/**
	 * 映射的工作流引擎xml对应的节点名称或属性名称
	 * @type String
	 */
	this.mapName = "handler";
	
	/**
	 * 映射的工作流引擎xml的类型
	 * 1.child-node 映射为子结点
	 * 2.attribute 元素属性
	 */
	this.mapType = "child-node";

	
	/**
	 * 数据的输入方式
	 * 1.input 直接从页面上输入项获取
	 *   input_text 文本框
	 *   input_select 选择框
	 *   input_multi_select
	 *   input_checkbox 
	 *   input_multi_checkbox
	 *   input_date 输入日期
	 * 2.method 提供方法获取返回值
	 * 3.child 当前属性没有值，只包含子属性
	 * 4.none 无输入项，预设为默认值
	 * @type String
	 */
	this.inputType = 'input_text';
	
	/**
	 * 获取输入值的方法名
	 * @type String
	 */
	this.inputString = null;
	
	
	/**
	 * 属性值的数据类型
	 * 1.string 字符串
	 * 2.cdata CDATA类型数据
	 * 3.date 日期型
	 * 4.number 数值型
	 * 5.boolean 布尔型
	 * 6.child 只是含有子对象，本身没有值
	 * 7.none
	 * @type String
	 */
	this.dataType = 'child';
	
	/**
	 * 生成xml的方式
	 * 1.direct 
	 * 2.getXmlString（默认方式）
	 * 3.customXmlString(自定义方式生成)
	 * @type
	 */
	this.getXmlType = "getXmlString";
	
	//管理本属性及子结点xml的生成
	this.isSuspendChildXml = true;
	
	/**
	 * 子属性数组
	 * @type Array
	 */
	this.attributeArray = new Array();
	
	//class属性
	var attr_class = new EAPP.Workflow.Graphic.Attribute.Class();
	this.attributeArray.push(attr_class);
	
	//绑定类型(虚拟操作属性)
	var attr_handler_type = new EAPP.Workflow.Graphic.Attribute.NormalAttribute();
	attr_handler_type.attributeName = "handler-type";
	attr_handler_type.displayName = "执行程序类型";
	attr_handler_type.mapType = "none";
	attr_handler_type.inputType = "input_select";
	attr_handler_type.dataSource = {"表达式":"expression","程序":"program"};
	attr_handler_type.valueChanagedMethod = "handlerTypeChange";
	
	//添加到委派属性对象
	this.attributeArray.unshift(attr_handler_type);
	
		
	/**
	 * 获取显示名称
	 */
	this.getDisplayName = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getDisplayName.apply(this);
	};
	
	/**
	 * 获取当前属性的值
	 */
	this.getValue = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getValue.apply(this); 
	};
	
	/**
	 * 生成xml字符串
	 * @type String
	 * 返回生成的xml字符串
	 */
	this.getXmlString = function(){
		var classString =" class=\"" +  attr_class.getXmlString() + "\"";
		var innerValue = this.getValue(true);
		
		if((attr_handler_type.getValue(true) == "expression") && innerValue){
			return "\n\t\t\t<" + this.mapName + "><![CDATA[" + innerValue + "]]></" + this.mapName +">";
		}else if((attr_handler_type.getValue(true) == "program") && classString){
			return "\n\t\t\t<" + this.mapName + classString + "></" + this.mapName +">";
		}else{
			return "";
		}
	};	
	
	/**
	 * 用户定制生成xml的方法
	 */
	this.getCustomeXmlString = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getXmlString.apply(this); 
	};	
};


/** 
 * 创建一个 Condition 属性
 * @class Condition 属性类
 * @type EAPP.Workflow.Graphic.Attribute.Condition
 * @constructor
 */
EAPP.Workflow.Graphic.Attribute.Condition = function(){
	
	//继承自基属性类
	EAPP.Workflow.Graphic.Attribute.BaseAttribute.call(this);
	
	/**
	 * 当前对象类型名称
	 * @type String
	 */
	this.typeName = "EAPP.Workflow.Graphic.Attribute.Condition";
	
	/**
	 * 属性名称
	 * @type String
	 */
	this.attributeName = "condition";
	
	/**
	 * 界面上的显示名称
	 * @type String
	 */
	this.displayName = "条件";
	
	/**
	 * 二级显示名称
	 * @type String
	 */
	this.secondDisplayName = "";
	
	/**
	 * 映射的工作流引擎xml对应的节点名称或属性名称
	 * @type String
	 */
	this.mapName = "condition";
	
	/**
	 * 映射的工作流引擎xml的类型
	 * 1.child-node 映射为子结点
	 * 2.attribute 元素属性
	 */
	this.mapType = "child-node";
	
	
	/**
	 * 数据的输入方式
	 * 1.input 直接从页面上输入项获取
	 *   input_text 文本框
	 *   input_select 选择框
	 *   input_multi_select
	 *   input_checkbox 
	 *   input_multi_checkbox
	 *   input_date 输入日期
	 * 2.method 提供方法获取返回值
	 * 3.child 当前属性没有值，只包含子属性
	 * 4.none 无输入项，预设为默认值
	 * @type String
	 */
	this.inputType = 'input_text';
	
	
	/**
	 * 属性值的数据类型
	 * 1.integer 整型
	 * 2.float   浮点型
	 * 3.boolean 布尔型
	 * 4.string  字符串
	 * 5.cdata   CDATA类型数据
	 * 6.date    日期型 yy-MM-dd
	 * 7.time    时间   hh:mm:ss
	 * 8.full-time 完整的时间 yy-MM-dd hh:mm:ss
	 * 9.child   只是含有子对象，本身没有值
	 * 10.none    不包含任何属性值(使用不到)
	 * @type String
	 */
	this.dataType = "cdata";
	
	
	/**
	 * 子属性数组
	 * @type Array
	 */
	this.attributeArray = new Array();
	
	//ref-transition属性
	var attr_ref_transition = new EAPP.Workflow.Graphic.Attribute.NormalAttribute();
	attr_ref_transition.attributeName = 'ref-transition';
	attr_ref_transition.displayName = 'ref-transition';
	attr_ref_transition.mapName = 'ref-transition';
	attr_ref_transition.mapType = 'attribute';
	attr_ref_transition.isNull = true;//可为空
	attr_ref_transition.isEnable = true;
	attr_ref_transition.isVisible = false;
	attr_ref_transition.defaultValue = "";
	attr_ref_transition.validType = 'none';
	attr_ref_transition.inputType = 'none';
	attr_ref_transition.dataType = 'string';
	
	//添加到当前属性数组
	this.attributeArray.push(attr_ref_transition);
	
	/**
	 * 获取显示名称
	 */
	this.getDisplayName = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getDisplayName.apply(this);
	};
	
	/**
	 * 获取当前属性的值
	 */
	this.getValue = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getValue.apply(this); 
	};
	
	/**
	 * 生成xml字符串
	 * @type String
	 * 返回生成的xml字符串
	 */
	this.getXmlString = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getXmlString.apply(this); 
	};	
	
	/**
	 * 用户定制生成xml的方法
	 */
	this.getCustomeXmlString = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getXmlString.apply(this); 
	};	
};

//继承原型链
EAPP.Workflow.Graphic.Attribute.Handler.prototype = new EAPP.Workflow.Graphic.Attribute.BaseAttribute();

	

/** 
 * 创建一个属性基类实例
 * @class 工作流图元属性基类
 * @type EAPP.Workflow.Graphic.Attribute.BaseAttribute
 * @constructor
 */
EAPP.Workflow.Graphic.Attribute.Description = function(){


	//继承自基属性类
	EAPP.Workflow.Graphic.Attribute.BaseAttribute.call(this);
	
		
	/**
	 * 当前对象类型名称
	 * @type String
	 */
	this.typeName = "EAPP.Workflow.Graphic.Attribute.Description";
	
	/**
	 * 属性名称
	 * @type String
	 */
	this.attributeName = "description";
	
	/**
	 * 界面上的显示名称
	 * @type String
	 */
	this.displayName = "说明";
	
	/**
	 * 映射的工作流引擎xml对应的节点名称或属性名称
	 * @type String
	 */
	this.mapName = "description";
	
	/**
	 * 映射的工作流引擎xml的类型
	 * 1.attribute 元素属性
	 * 2.child-node 映射为子结点
	 * 3.none 当前属性不生成xml
	 */
	this.mapType = "child-node";
	
	
	/**
	 * 数据的输入方式
	 * 1.input 直接从页面上输入项获取
	 *   input_text 文本框
	 *   input_textarea 文本区
	 *   input_select 选择框
	 *   input_multi_select
	 *   input_checkbox 
	 *   input_multi_checkbox
	 *   input_date 输入日期
	 * 2.method 提供方法获取返回值
	 * 3.child 当前属性没有值，只包含子属性
	 * 4.none 无输入项，预设为默认值
	 * 5.readonly 只显示，界面上不能修改
	 * @type String
	 */
	this.inputType = 'input_textarea';
	
	/**
	 * 输入的最大长度
	 */
	this.maxLength = 500;
	
	/**
	 * 属性值的数据类型
	 * 1.integer 整型
	 * 2.float   浮点型
	 * 3.boolean 布尔型
	 * 4.string  字符串
	 * 5.cdata   CDATA类型数据
	 * 6.date    日期型 yy-MM-dd
	 * 7.time    时间   hh:mm:ss
	 * 8.full-time 完整的时间 yy-MM-dd hh:mm:ss
	 * 9.child   只是含有子对象，本身没有值
	 * 10.none    不包含任何属性值(使用不到)
	 * @type String
	 */
	this.dataType = "cdata";
	
	/**
	 * 获取显示名称
	 */
	this.getDisplayName = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getDisplayName.apply(this);
	};
	
	/**
	 * 获取当前属性的值
	 */
	this.getValue = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getValue.apply(this); 
	};
	
	/**
	 * 生成xml字符串
	 * @type String
	 * 返回生成的xml字符串
	 */
	this.getXmlString = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getXmlString.apply(this); 
	};	
	
	/**
	 * 用户定制生成xml的方法
	 */
	this.getCustomeXmlString = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getXmlString.apply(this); 
	};	

};

//继承原型链
EAPP.Workflow.Graphic.Attribute.Description.prototype = new EAPP.Workflow.Graphic.Attribute.BaseAttribute();

/** 
 * 创建一个属性基类实例
 * @class 工作流图元属性基类
 * @type EAPP.Workflow.Graphic.Attribute.BaseAttribute
 * @constructor
 */
EAPP.Workflow.Graphic.Attribute.Class = function(){


	//继承自基属性类
	EAPP.Workflow.Graphic.Attribute.BaseAttribute.call(this);
	
		
	/**
	 * 当前对象类型名称
	 * @type String
	 */
	this.typeName = "EAPP.Workflow.Graphic.Attribute.Class";
	
	/**
	 * 属性名称
	 * @type String
	 */
	this.attributeName = "class";
	
	/**
	 * 界面上的显示名称
	 * @type String
	 */
	this.displayName = "类";
	
	/**
	 * 映射的工作流引擎xml对应的节点名称或属性名称
	 * @type String
	 */
	this.mapName = "class";
	
	/**
	 * 映射的工作流引擎xml的类型
	 * 1.attribute 元素属性
	 * 2.child-node 映射为子结点
	 * 3.none 当前属性不生成xml
	 */
	this.mapType = "attribute";
	
	
	/**
	 * 数据的输入方式
	 * 1.input 直接从页面上输入项获取
	 *   input_text 文本框
	 *   input_textarea 文本区
	 *   input_select 选择框
	 *   input_multi_select
	 *   input_checkbox 
	 *   input_multi_checkbox
	 *   input_date 输入日期
	 * 2.method 提供方法获取返回值
	 * 3.child 当前属性没有值，只包含子属性
	 * 4.none 无输入项，预设为默认值
	 * 5.readonly 只显示，界面上不能修改
	 * @type String
	 */
	this.inputType = 'input_text';
	
	/**
	 * 输入的最大长度
	 */
	this.maxLength = 200;
	
	/**
	 * 属性值的数据类型
	 * 1.integer 整型
	 * 2.float   浮点型
	 * 3.boolean 布尔型
	 * 4.string  字符串
	 * 5.cdata   CDATA类型数据
	 * 6.date    日期型 yy-MM-dd
	 * 7.time    时间   hh:mm:ss
	 * 8.full-time 完整的时间 yy-MM-dd hh:mm:ss
	 * 9.child   只是含有子对象，本身没有值
	 * 10.none    不包含任何属性值(使用不到)
	 * @type String
	 */
	this.dataType = "string";
	
	/**
	 * 获取显示名称
	 */
	this.getDisplayName = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getDisplayName.apply(this);
	};
	
	/**
	 * 获取当前属性的值
	 */
	this.getValue = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getValue.apply(this); 
	};
	
	/**
	 * 生成xml字符串
	 * @type String
	 * 返回生成的xml字符串
	 */
	this.getXmlString = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getXmlString.apply(this); 
	};	
	
	/**
	 * 用户定制生成xml的方法
	 */
	this.getCustomeXmlString = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getXmlString.apply(this); 
	};	

};

//继承原型链
EAPP.Workflow.Graphic.Attribute.Class.prototype = new EAPP.Workflow.Graphic.Attribute.BaseAttribute();	



/** 
 * 创建一个名称属性实例
 * @class 工作流图元属性基类
 * @type EAPP.Workflow.Graphic.Attribute.BaseAttribute
 * @constructor
 */
EAPP.Workflow.Graphic.Attribute.Name = function(){


	//继承自基属性类
	EAPP.Workflow.Graphic.Attribute.BaseAttribute.call(this);
	
		
	/**
	 * 当前对象类型名称
	 * @type String
	 */
	this.typeName = "EAPP.Workflow.Graphic.Attribute.Name";
	
	/**
	 * 属性名称
	 * @type String
	 */
	this.attributeName = "name";
	
	/**
	 * 界面上的显示名称
	 * @type String
	 */
	this.displayName = "名称";
	
	/**
	 * 映射的工作流引擎xml对应的节点名称或属性名称
	 * @type String
	 */
	this.mapName = "name";
	
	/**
	 * 映射的工作流引擎xml的类型
	 * 1.attribute 元素属性
	 * 2.child-node 映射为子结点
	 * 3.none 当前属性不生成xml
	 */
	this.mapType = "attribute";
	
	/**
	 * 能否为空
	 * (默认可为空)
	 * @type Boolean
	 */
	this.isNull = false;
	
	/**
	 * 数据的输入方式
	 * 1.input 直接从页面上输入项获取
	 *   input_text 文本框
	 *   input_textarea 文本区
	 *   input_select 选择框
	 *   input_multi_select
	 *   input_checkbox 
	 *   input_multi_checkbox
	 *   input_date 输入日期
	 * 2.method 提供方法获取返回值
	 * 3.child 当前属性没有值，只包含子属性
	 * 4.none 无输入项，预设为默认值
	 * 5.readonly 只显示，界面上不能修改
	 * @type String
	 */
	this.inputType = 'input_text';
	
	/**
	 * 输入的最大长度
	 */
	this.maxLength = 200;
	
	/**
	 * 属性值的数据类型
	 * 1.integer 整型
	 * 2.float   浮点型
	 * 3.boolean 布尔型
	 * 4.string  字符串
	 * 5.cdata   CDATA类型数据
	 * 6.date    日期型 yy-MM-dd
	 * 7.time    时间   hh:mm:ss
	 * 8.full-time 完整的时间 yy-MM-dd hh:mm:ss
	 * 9.child   只是含有子对象，本身没有值
	 * 10.none    不包含任何属性值(使用不到)
	 * @type String
	 */
	this.dataType = "string";
	
	/**
	 * 获取显示名称
	 */
	this.getDisplayName = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getDisplayName.apply(this);
	};
	
	/**
	 * 获取当前属性的值
	 */
	this.getValue = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getValue.apply(this); 
	};
	
	/**
	 * 生成xml字符串
	 * @type String
	 * 返回生成的xml字符串
	 */
	this.getXmlString = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getXmlString.apply(this); 
	};	
	
	/**
	 * 用户定制生成xml的方法
	 */
	this.getCustomeXmlString = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getXmlString.apply(this); 
	};	

};

//继承原型链
EAPP.Workflow.Graphic.Attribute.Name.prototype = new EAPP.Workflow.Graphic.Attribute.BaseAttribute();	






/** 
 * 创建一个 Subflow 属性
 * @class Subflow 属性类
 * @type EAPP.Workflow.Graphic.Attribute.Subflow
 * @constructor
 */
EAPP.Workflow.Graphic.Attribute.Subflow = function(){
	

	//继承自基属性类
	EAPP.Workflow.Graphic.Attribute.BaseAttribute.call(this);
	
	
	/**
	 * 当前对象类型名称
	 * @type String
	 */
	this.typeName = "EAPP.Workflow.Graphic.Attribute.Subflow";
	
	/**
	 * 属性名称
	 * @type String
	 */
	this.attributeName = "sub-flow";
	
	/**
	 * 界面上的显示名称
	 * @type String
	 */
	this.displayName = "子流程";
	
	/**
	 * 映射的工作流引擎xml对应的节点名称或属性名称
	 * @type String
	 */
	this.mapName = "sub-flow";
	
	/**
	 * 映射的工作流引擎xml的类型
	 * 1.child-node 映射为子结点
	 * 2.attribute 元素属性
	 */
	this.mapType = "child-node";
	
	
	/**
	 * 数据的输入方式
	 * 1.input 直接从页面上输入项获取
	 *   input_text 文本框
	 *   input_select 选择框
	 *   input_multi_select
	 *   input_checkbox 
	 *   input_multi_checkbox
	 *   input_date 输入日期
	 * 2.method 提供方法获取返回值
	 * 3.child 当前属性没有值，只包含子属性
	 * 4.none 无输入项，预设为默认值
	 * @type String
	 */
	this.inputType = 'none';
	
	
	/**
	 * 属性值的数据类型
	 * 1.string 字符串
	 * 2.cdata CDATA类型数据
	 * 3.date 日期型
	 * 4.number 数值型
	 * 5.boolean 布尔型
	 * 6.child 只是含有子对象，本身没有值
	 * 7.none
	 * @type String
	 */
	this.dataType = 'none';
	
	
	/**
	 * 子属性数组
	 * @type Array
	 */
	this.attributeArray = new Array();
	
	
		//子流程名称
		var attr_subflow_name = new EAPP.Workflow.Graphic.Attribute.NormalAttribute();
		attr_subflow_name.attributeName = 'name';
		attr_subflow_name.mapName = "name";
		attr_subflow_name.mapType = "attribute";
		attr_subflow_name.displayName = '子流程名称';
		attr_subflow_name.inputType = "input_text";
		attr_subflow_name.dataType = 'string';
		attr_subflow_name.value = '子流程名称';
		attr_subflow_name.maxLength = 200;
		attr_subflow_name.isNull = false;
		
		//子流程版本
		var attr_subflow_version = new EAPP.Workflow.Graphic.Attribute.NormalAttribute();
		attr_subflow_version.attributeName = 'version';
		attr_subflow_version.mapName = "version";
		attr_subflow_version.mapType = "attribute";
		attr_subflow_version.displayName = '子流程版本';
		attr_subflow_version.inputType = "input_text";
		attr_subflow_version.dataType = 'string';
		attr_subflow_version.maxLength = 200;
		
		//子流程初始化
		var attr_subflow_initial = new EAPP.Workflow.Graphic.Attribute.NormalAttribute();
		attr_subflow_initial.attributeName = 'initial';
		attr_subflow_initial.mapName = "initial";
		attr_subflow_initial.mapType = "attribute";
		attr_subflow_initial.displayName = '子流程初始化';
		attr_subflow_initial.inputType = "input_select";
		attr_subflow_initial.dataType = 'string';
		attr_subflow_initial.dataSource = {"运行时":"runtime","立即":"immediate"};
			
	//添加到属性数组
	this.attributeArray.push(attr_subflow_name);
	this.attributeArray.push(attr_subflow_version);
	this.attributeArray.push(attr_subflow_initial);
	
	/**
	 * 获取显示名称
	 */
	this.getDisplayName = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getDisplayName.apply(this);
	};
	
	/**
	 * 获取当前属性的值
	 */
	this.getValue = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getValue.apply(this); 
	};
	
	/**
	 * 生成xml字符串
	 * @type String
	 * 返回生成的xml字符串
	 */
	this.getXmlString = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getXmlString.apply(this); 
	};	
	
	/**
	 * 用户定制生成xml的方法
	 */
	this.getCustomeXmlString = function(){
		return new EAPP.Workflow.Graphic.Attribute.BaseAttribute().getXmlString.apply(this); 
	};	
};

//继承原型链
EAPP.Workflow.Graphic.Attribute.Subflow.prototype = new EAPP.Workflow.Graphic.Attribute.BaseAttribute();
	
/**
 * 获取某一个对象的某一属性对象
 * @param {Object} object
 * @param {String} attributeName
 * @type Object
 * @return 返回属性对象
 */
function getAttribute(object,attributeName){
	var attributeArray = object.attributeArray;
	if(null == attributeArray || attributeArray.length == 0){ return;}
	
	var index = attributeName.indexOf('#');
	//当前处于最深层
	if(index == -1){
		for(var i=0; i<attributeArray.length; i++){
			var attr = attributeArray[i];
			//给相应的属性赋值
			if (attr.attributeName == attributeName){
				return attr;
			}
		}
	}else if(index >= 0){
		var currentName = attributeName.slice(0,index);
		for(var i=0; i<attributeArray.length; i++){
			var attr = attributeArray[i];
			if(attr.attributeName == currentName){
				
				//切去当前的名称
				var nextAttributeName = attributeName.slice(index+1);
				return getAttribute(attr,nextAttributeName);//进行递归
			}
		}
	}
};





/**
 * 通过属性名称删除从父对象中移除当前属性
 * @param {Object} object
 * @param {Object} attributeName
 * @param {Object} resultArray
 */
function deleteAttributeByName(object,attributeName){
	var attributeArray = object.attributeArray;
	if(null == attributeArray || attributeArray.length == 0){ return null;}
	
	var index = attributeName.indexOf('#');
	//当前处于最深层
	if(index == -1){
		for(var i=0; i<attributeArray.length; i++){
			var attr = attributeArray[i];
			//给相应的属性赋值
			if (attr.attributeName == attributeName){
				attributeArray.splice(i,1);
				return  attr;
			}
		}
	}else if(index >= 0){
		var currentName = attributeName.slice(0,index);
		for(var i=0; i<attributeArray.length; i++){
			var attr = attributeArray[i];
			if(attr.attributeName == currentName){
				
				//切去当前的名称
				var nextAttributeName = attributeName.slice(index+1);
				return deleteAttributeByName(attr,nextAttributeName);//进行递归
			}
		}
	}
	return null;
};


/**
 * 拷贝属性
 * bug:
 * 只有两个相同类型的对象才能复制
 * 动态引用的属性无法复制
 * 复制属性时，引用的属性不复制
 * 条件判断等动态添加的属性可能会有问题
 */
function copyAttributeArray(soureObj,destObj){
	//验证类型相同
	if(!soureObj || !destObj){return false;}
	if(soureObj.typeName != destObj.typeName){return false;}
	
	for(var i=0; i<soureObj.attributeArray.length; i++){
		var attr = soureObj.attributeArray[i];
		//只有直接赋值的属性，才进行拷贝
		if(attr.getValueType == "direct"){
			destObj.attributeArray[i].value = attr.value;
		}
		//复制子属性
		if(attr.attributeArray && attr.attributeArray.length){
			copyAttributeArray(attr,destObj.attributeArray[i]);
		}
	}
};

/**
 * 通过属性名称获取属性数组
 * @param {Object} object
 * @param {Object} attributeName
 * @param {Object} resultArray
 */
function getAttributeArrayByName(object,attributeName,resultArray){
	var attributeArray = object.attributeArray;
	if(null == attributeArray || attributeArray.length == 0){ return null;}
	
	if(!resultArray) resultArray = new Array();
	
	for(var i=0; i<attributeArray.length; i++){
		var attr = attributeArray[i];
		//给相应的属性赋值
		if (attr.attributeName == attributeName){
			resultArray.push(attr);
		}
		
		if(attr.attributeArray && attr.attributeArray.length){
			getAttributeArrayByName(attr,attributeName,resultArray);
		}
	}
	
	return attributeArray;
};


/**
 * 将某个对象的所有属性设置为动态生成
 * @param {Object} object
 * @param {String} attributeName
 * @type Object
 * @return 返回属性对象
 */
function setAttributeDynamicCreate(object){
	var attributeArray = object.attributeArray;
	if(null == attributeArray || attributeArray.length == 0){ return;}
	
	for(var i=0; i<attributeArray.length; i++){
		var attr = attributeArray[i];
		attr.isDynamicCreate = true;
		
		if(attr.attributeArray && attr.attributeArray.length){
			setAttributeDynamicCreate(attr);
		}
	}
};



/**
 * 获取某一个对象的某一属性值
 * @param {Object} object
 * @param {String} attributeName
 * @param {Boolean} flag 表示为true，表示获取真实值
 * @type Object
 * @return 返回属性对象
 */
function getAttributeValue(object,attributeName,flag){
	var attr = getAttribute(object,attributeName);
	if(attr){ return attr.getValue(flag);}
	return "";
};



/**
 * 修改属性值
 * @param {Object} object 要修改属性值的对象
 * @param {String} attributeName 要修改的属性名称，多个层次之间用#分隔
 * @param {String} value 属性值
 */
function alterAttribute(object,attributeName,value){
 	return setAttributeValue(object,attributeName,value);	
};


/**
 * 修改属性值
 * @param {Object} object 要修改属性值的对象
 * @param {String} attributeName 要修改的属性名称，多个层次之间用#分隔
 * @param {String} value 属性值
 */
function setAttributeValue(object,attributeName,value){
	var attr = getAttribute(object,attributeName);
	if(!attr){ return false;}
	attr.value = value;
	return true;	
};

/**
 * 修改属性对象的配置
 * @param {Object} object 要修改属性值的对象
 * @param {String} attributeName 要修改的属性名称，多个层次之间用#分隔
 * @param {String} value 属性值
 */
function alterAttributeProperty(object,attributeName,propertyName,value){
	var attr = getAttribute(object,attributeName);
	if(!attr){ return false;}
	//try{
		if(typeof(eval('attr.' + propertyName)) == "string"){
			eval('attr.' + propertyName + "=\"" + value + "\"");
		}else{
			eval('attr.' + propertyName + "=" + value);
		}
		//alert(typeof(eval('attr.' + propertyName)));
		
	//}catch(e){
		//return false;
	//}
	return true;	
};

/**
 * 禁用属性
 * 不支持运行时禁用属性
 * @type Boolean
 * @return 返回隐藏属性是否成功
 */
function disableAttribute(object,attributeName){
	var attr = getAttribute(object,attributeName);
	if(!attr){ return false;}
	attr.isEnable = false;
	return true;	
};

/**
 * 隐藏属性
 * 不支持运行时隐藏属性
 * @type Boolean
 * @return 返回隐藏属性是否成功
 */
function hideAttribute(object,attributeName){
	var attr = getAttribute(object,attributeName);
	if(!attr){ return false;}
	attr.isVisible = false;
	return true;		
};/*拖动*/
if  (document.getElementById){
(function(){
	var n = 500;
	var dragok = false;
	var y,x,d,dy,dx;
	
	function move(e){
		if (!e) e = window.event;
		if (dragok){
			d.style.left = dx + e.clientX - x + "px";
			d.style.top  = dy + e.clientY - y + "px";
		return false;
		}
	};

	function down(e){
		if (!e) e = window.event;
		var temp = (typeof e.target != "undefined")?e.target:e.srcElement;
		if(typeof temp.tagName == "undefined"){return false;}
		
		if (temp.tagName != "HTML"|"BODY" && temp.className != "wfDialog"){
		 temp = (typeof temp.parentNode != "undefined")?temp.parentNode:temp.parentElement;
		 }
		//如果事件是发生在 TR标签,则..
		if('TR'==temp.tagName){
		//指向 TBODY 标签
		temp = (typeof temp.parentNode != "undefined")?temp.parentNode:temp.parentElement;
		//指向 TABLE 标签
		temp = (typeof temp.parentNode != "undefined")?temp.parentNode:temp.parentElement;
		//指向 DIV 标签,这样就可以移动啦
		temp = (typeof temp.parentNode != "undefined")?temp.parentNode:temp.parentElement;
		}
		if (temp.className == "wfDialog"){
		if (window.opera){
		  document.getElementById("Q").focus();
		 }
		 dragok = true;
		 temp.style.zIndex = n++;
		 d = temp;
		 dx = parseInt(temp.style.left+0);
		 dy = parseInt(temp.style.top+0);
		 x = e.clientX;
		 y = e.clientY;
		 document.onmousemove = move;
		 return false;
		 }
	};
	
	function up(){
	dragok = false;
	document.onmousemove = null;
	};
	
	document.onmousedown = down;
	document.onmouseup = up;
  })();//end anonymous funciton
}//end if 
/*拖动 end*/
/*背景*/
var isIe=(document.all)?true:false;
//弹出方法
function showMessageBox(wTitle,content,wWidth,wHeight)
{
	//绑定屏蔽Tab功能
	document.attachEvent("onkeydown",disableTabKey);
	
	//closeWindow();
	var bWidth=parseInt(document.documentElement.scrollWidth);
	var bHeight=parseInt(document.documentElement.scrollHeight);
	
	var bodyWidth = parseInt(document.body.scrollWidth);
	var bodyHeight = parseInt(document.body.scrollHeight);
	
	var screenWidth = parseInt(screen.width);
	var screenHeight = parseInt(screen.height);
 
//	alert("screen.width " + screen.width  + " screen.height " + screen.height);
//	alert("screen.width " +screenWidth + " screen.height " +screenHeight);
//	alert("bodyWidth" +bodyWidth + " bodyHeight " + bodyHeight);
	
	if (bodyWidth < screenWidth) bodyWidth = screenWidth;
	if (bodyHeight < screenHeight) bodyHeight = screenHeight;
	
//	alert("bodyw=" + bodyWidth + ",bodyh=" + bodyHeight);

	var back=document.createElement("div");
	back.id="back";
	var styleStr="top:0px;left:0px;position:absolute;background:#666;width:"+bodyWidth+"px;height:"+bodyHeight+"px;";
	styleStr+=(isIe)?"filter:alpha(opacity=0);":"opacity:0;";
	back.style.cssText=styleStr;
	document.body.appendChild(back);
	showBackground(back,20);
	var mesW=document.createElement("div");
	mesW.id="wfDialog";
	mesW.className="wfDialog";
	mesW.innerHTML=" <table width='100%' border='0' cellspacing='0' cellpadding='0'>"
	              +" <tr><td class='wfDialog01'><div class='wfBlank7'></div></td><td class='wfDialog02'><span>"+wTitle+"</span><input type='button' class='wfDialogClose02' id='wfDialogClose' onclick='closeWindow()' onmouseover='overWindow()' onmouseout='outWindow()'/></td><td class='wfDialog03'><div class='wfBlank12'></div></td></tr>"
	              +" <tr><td class='wfDialog04'></td><td class='wfDialogCon'>"+content+"</td><td class='wfDialog05'></td></tr>"
				  +" <tr><td class='wfDialog06'></td><td class='wfDialog07'></td><td class='wfDialog08'></td></tr></table>";
	styleStr="left:"+(bWidth-wWidth)/2+"px;top:"+(bHeight-wHeight)/2+"px;position:absolute;width:"+wWidth+"px;";
	mesW.style.cssText=styleStr;
	document.body.appendChild(mesW);
};


//让背景渐渐变暗
function showBackground(obj,endInt){
	if(isIe){
		obj.filters.alpha.opacity+=12;
		if(obj.filters.alpha.opacity<endInt){
		 setTimeout(function(){showBackground(obj,endInt)},5);
		}
    }else{
		var al=parseFloat(obj.style.opacity);
		al+=0.09;
		obj.style.opacity=al;
		if(al<(endInt/100)){setTimeout(function(){showBackground(obj,endInt)},5);}
   }
};
//关闭窗口
function closeWindow(){

	if( document.getElementById('back')!=null){
		document.getElementById('back').parentNode.removeChild(document.getElementById('back'));
    }
    if(document.getElementById('wfDialog')!=null){
	    document.getElementById('wfDialog').parentNode.removeChild(document.getElementById('wfDialog'));
    }
	
	//绑定屏蔽Tab功能
	document.detachEvent("onkeydown",disableTabKey);
};


//关闭按钮样式
function overWindow(){
	if(document.getElementById('wfDialogClose')!=null){
		document.getElementById('wfDialogClose').className="wfDialogClose01";
	}
};

function outWindow(){
	if(document.getElementById('wfDialogClose')!=null){
		document.getElementById('wfDialogClose').className="wfDialogClose02";
	}
};
 





//测试弹出
function showExpressionDialog(inputId){
	
	messContent =  " <table id=\"tableExpression\" width='96%' border='0' align='center' cellpadding='0' cellspacing='0'";
	//保存inputId
    messContent +=  inputId?"inputId="+inputId:"inputId=\"\"";
    messContent += " >";//end table_begin
    
	messContent += " <tr>";
    messContent += " <td width='30%'>";
	messContent += "变量名称：<select id='selectVariantName' name='selectVariantName' onchange=\"onSelectVariantChange(this);\">";
	
	var optionsHtml = gEAPPWorkflow.getMetaHtml();
	messContent += optionsHtml?optionsHtml:"";
	messContent += " </select>";
	messContent += " </td>";
	
	messContent += " <td width='27%'>";
	messContent += " 判断符：<select id='selectOperator' name='selectOperator' style='width:52px' >";
	messContent += " <option value=\"==\">==</option>";
	messContent += " <option value=\"!=\">!=</option>";
	messContent += " <option value=\">\" >></option>";
	messContent += " <option value=\">=\">>=</option>";
	messContent += " <option value=\"<\" ><</option>";
	messContent += " <option value=\"<=\"><=</option>";
	messContent += " </select>";
	messContent += " </td>";
	
	messContent += " <td width='43%'>";
	messContent += " 判断值：<input type='text' id='inputtextJudgeValue' name='inputtextJudgeValue' class='wfIpt' maxlength='2000'>&nbsp;";
	messContent += " <input type='button' id='btnAddExpression' name='btnAddExpression' value='增加' class='wfBtn' onclick=\"addExpression();\">";
	messContent += " </td>";
	messContent += " </tr>";
	
	
	messContent += " <tr>";
	messContent += " <td colspan=\"3\">";
	
	//运算符
	messContent += " <div class=\"wfOperators\">";
	messContent += " <ul>";
	messContent += " <li class=\"overWf\" onclick=\"insertAtCaret('&&')\"  onmouseover=\"$(this).addClass('currentWf');\"  onmouseout=\"$(this).removeClass('currentWf');\"> && </li>";
	messContent += " <li class=\"overWf\" onclick=\"insertAtCaret('||');\" onmouseover=\"$(this).addClass('currentWf');\"  onmouseout=\"$(this).removeClass('currentWf');\"> ||</li>";
	messContent += " <li class=\"overWf\" onclick=\"insertAtCaret('!');\"  onmouseover=\"$(this).addClass('currentWf');\"  onmouseout=\"$(this).removeClass('currentWf');\"> ! </li>";
	messContent += " <li class=\"overWf\" onclick=\"insertAtCaret('+');\" onmouseover=\"$(this).addClass('currentWf');\"  onmouseout=\"$(this).removeClass('currentWf');\"> + </li>";
	messContent += " <li class=\"overWf\" onclick=\"insertAtCaret('-');\" onmouseover=\"$(this).addClass('currentWf');\"  onmouseout=\"$(this).removeClass('currentWf');\"> - </li>";
	messContent += " <li class=\"overWf\" onclick=\"insertAtCaret('*');\" onmouseover=\"$(this).addClass('currentWf');\"  onmouseout=\"$(this).removeClass('currentWf');\"> * </li>";
	messContent += " <li class=\"overWf\" onclick=\"insertAtCaret('/');\" onmouseover=\"$(this).addClass('currentWf');\"  onmouseout=\"$(this).removeClass('currentWf');\"> / </li>";
	messContent += " <li class=\"overWf\" onclick=\"insertAtCaret('(');\" onmouseover=\"$(this).addClass('currentWf');\"  onmouseout=\"$(this).removeClass('currentWf');\"> ( </li>";
	messContent += " <li class=\"overWf\" onclick=\"insertAtCaret(')');\" onmouseover=\"$(this).addClass('currentWf');\"  onmouseout=\"$(this).removeClass('currentWf');\"> ) </li>";
	messContent += " </ul>";
	messContent += " </div>";
	messContent += " </td>";
	messContent += " </tr>";
	
    messContent += " <tr>";
	messContent += " <td colspan='3'>";
	messContent += " <div class='wfTit'>表达式编辑结果：</div>";
	
	messContent += " <textarea id=\"textareaExpression\" name='textarea' class='wfArea'  style='width:450px;' ";
	
	//记住当前输入位置
	messContent += " onselect=\"storeCaret(this);\" onclick=\"storeCaret(this);\" onkeyup=\"storeCaret(this);\"";
	//限制文本区最大长度 expression：4000				
/*
	messContent += " onkeypress=\"new EAPP.Workflow.Graphic.Util.Textarea.MaxLength().onKeyPress(this,2000)\"";
    messContent += " onkeydown=\"new EAPP.Workflow.Graphic.Util.Textarea.MaxLength().onKeyDown(this,2000)\"";
	messContent += " onbeforepaste=\"new EAPP.Workflow.Graphic.Util.Textarea.MaxLength().onBeforePaste(this,2000)\"";
	messContent += " onpaste=\"new EAPP.Workflow.Graphic.Util.Textarea.MaxLength().onPaste(this,2000)\"";
*/
	
	messContent += " >";
	//如果当前有值则显示当前的值，否则显示为空
	var displayValue = getDisplayValueFromInputId(inputId);
	messContent += displayValue?displayValue:"";
	
	messContent += " </textarea>";
	messContent += " </td>";
	messContent += " </tr>";
	
    messContent += " <tr>";
	messContent += " <td height='40' colspan='3' align='right'>";
	//三个操作按钮,确定时进行自动校验
	messContent += " <input type='button' name='Submit2' value='确定' class='wfBtn' onclick=\"commitExpression();\">&nbsp;";
	messContent += " <input type='button' name='Submit2' value='清空' class='wfBtn' onclick=\"clearTextareaExpression();\">&nbsp;";
	messContent += " <input type='button' name='Submit2' value='取消' class='wfBtn' onclick=\"closeWindow();\">";
	messContent += " </td>";
	messContent += " </tr>";
	messContent += " </table>";
	showMessageBox('表达式编辑器',messContent,500,238);
	
	//过滤运算符
	onSelectVariantChange();
	

};

/**
 * 浮出弹出层时，屏蔽Tab功能键
 */
function disableTabKey(event){
	if(event.keyCode == 9){
		event.keyCode = 0;
		return false;
	}
}


function commitExpression(){
	//首先获取表达式
	var textAreaExpression = $("#tableExpression #textareaExpression")[0];
	if(!textAreaExpression){return false;}
	var expressionValue = textAreaExpression.value;
	expressionValue = expressionValue?expressionValue.trim():"";
	
 	/**
	if(!expressionValue){
		alert("表达式为空,不能保存！\n 如要取消保存，请直接点击取消按钮！");
		return false;
	}
	**/
	 
	
	//	
	/**
	var validMethod = gEAPPWorkflow.validExpression;
	if(!validMethod){
		var forceSave = confirm("表达式验证失败,缺少验证函数！\n\t按\"确定\"按钮，强制继续进行保存！\n\t按\"取消\"按钮，返回继续编辑！");
		if(!forceSave){return false;}//返回继续编辑
	}
	**/
	
	if (expressionValue) {
		
		if(expressionValue.length > 2000){
			alert("表达式现在的字符数为" + expressionValue.length + "，超过最大长度2000个字符，请修正后提交");
			return false;
		}
	}
	
	//表达式不为空时，才进行验证
	if (expressionValue) {
		var validResult = false;
		try {
			validResult = gEAPPWorkflow.validExpression(textAreaExpression);
		} 
		catch (e) {
			alert("验证出错！请联系技术支持人员！");
			return false;
		}
		
		
		if (!validResult) {
			var forceSave = confirm("表达式验证失败！\n\t按\"确定\"按钮，强制继续进行保存！\n\t按\"取消\"按钮，返回继续编辑！");
			if (!forceSave) {
				return false;
			}//返回继续编辑
		}
	}
	
	//进行保存操作
	
	//获取输入项的id
	var inputId = $("#tableExpression").attr("inputId");
	inputId = inputId?inputId.trim():"";//清除空格
	var inputObj = null;
	if (inputId) {
		inputObj = document.getElementById(inputId);
	}
	if(!inputId || !inputObj){
		alert("保存出错！请联系技术支持人员！");
		return false;
	}
	
	//把值赋给文本框
	inputObj.value = expressionValue;
	//更新值
	gEAPPWorkflow.updateAttributeValue(inputObj);
	
	closeWindow();	
};

function getDisplayValueFromInputId(inputId){
	//获取输入
	var inputObj = null;
	if (inputId) {
		inputObj = document.getElementById(inputId);
	}
	
	if(!inputId || !inputObj){
		alert("编辑出错！请联系技术支持人员！");
		return null;
	}
	return inputObj.value;
};

/**
 * 清空表达式文本区
 */
function clearTextareaExpression(){
	$("#tableExpression #textareaExpression").text("");
	
	//清空判断值
	$("#tableExpression #inputtextJudgeValue")[0].value = "";
};

function addExpression(){
	var variantDisplayName = $("#tableExpression #selectVariantName option:selected").text();
 
	var variantValue = $("#tableExpression #selectVariantName").val();
	var operator = $("#tableExpression #selectOperator").val();
	var judgeValue = $("#tableExpression #inputtextJudgeValue").val();
 
	//去除相应的空格
	variantDisplayName = variantDisplayName?variantDisplayName.trim():"";
	operator = operator?operator.trim():"";
	judgeValue = judgeValue?judgeValue.trim():"";
	
	if(!variantDisplayName){alert("变量名不能为空！");return false;};
	if(!operator){alert("运算算不能为空！");return false;};
	
	//验证变量类型与运算符的匹配
	var meta = gEAPPWorkflow.getMetaByName(variantValue);
	if(!meta){alert("所添加的变量\"" + variantDisplayName + "\"对应的元数据不存在！");return false;}
	if((meta.type == "DATATYPE_STRING") && (operator != "==" && operator != "!=" )){
		alert("所添加的变量\"" + variantValue + "\"元数据为字符串型，只能进行添加==（相等）或!=（不相等）的运算符进行比较！");
		return false;
	}else if((meta.type == "DATATYPE_BOOLEAN") && (operator != "==" && operator != "!=" )){
		alert("所添加的变量\"" + variantValue + "\"元数据为布尔型，只能进行添加==（相等）或!=（不相等）的运算符进行比较！");
		return false;
	}
	
	if((meta.type != "DATATYPE_STRING") && (!judgeValue)){alert("判断值不能为空！");return false;};//不是字符开串的值不能为空
	
	if(meta.type == "DATATYPE_STRING"){
		//字符串型加上双引号（""）
		judgeValue = judgeValue?"\""+judgeValue + "\"":"\"\"";
	}else if(meta.type == "DATATYPE_DATE"){
		//日期型数据加上中括号[]
		judgeValue = "["+judgeValue + "]";
	}
	
	//首先获取表达式
	var textAreaExpression = $("#tableExpression #textareaExpression")[0];
	if(!textAreaExpression){alert("添加失败！");return false;}
	var expressionValue = textAreaExpression.value;
	expressionValue = expressionValue?expressionValue.trim():"";
	
	var expressionString = " " +  variantDisplayName + " " + operator + " " + judgeValue;
	expressionValue +=  expressionString;
	
/*
	//验证添加表达式后的长度
	var iMaxExpressLength = 2000;
	if(expressionValue.length > iMaxExpressLength){
		alert("无法添加，表达式的必须少于2000个字符！");
		return false;
	}
*/
	
	$("#tableExpression #textareaExpression").val(expressionValue);
	
	//2008年11月29日 修订 文本区在添加新的判断表达式后，将插入点重新定位到文本框的结束位置
	var textRange = $("#tableExpression #textareaExpression")[0].createTextRange();
	var curPosRange = textRange.duplicate();
	curPosRange.collapse(false);
	$("#tableExpression #textareaExpression")[0].caretPos = curPosRange;
	
	//清空判断值
	$("#tableExpression #inputtextJudgeValue")[0].value = "";
};

function onSelectVariantChange(objSelect){
	if(!objSelect){
		objSelect = $("#tableExpression #selectVariantName")[0];
	}
	
	if(!objSelect){return false;}
	var variantValue = objSelect.value;
	if(!variantValue){return false;}
	
	var variantDisplayName = $("#tableExpression #selectVariantName option:selected").text();
	//去除相应的空格
	variantDisplayName = variantDisplayName?variantDisplayName.trim():"";
	if(!variantDisplayName){return false;}
	
	var opterateHtml = "";
	opterateHtml += " <option value=\"==\">==</option>";
	opterateHtml += " <option value=\"!=\">!=</option>";
	
	var meta = gEAPPWorkflow.getMetaByName(variantValue);
	if(!meta){alert("所添加的变量\"" + variantDisplayName + "\"对应的元数据不存在！");return false;}
	
	//只有在不是字符串或布尔型的时候，才包含有其它运算符
	if((meta.type != "DATATYPE_STRING") && (meta.type != "DATATYPE_BOOLEAN")){
		opterateHtml += " <option value=\">\" >></option>";
		opterateHtml += " <option value=\">=\">>=</option>";
		opterateHtml += " <option value=\"<\" ><</option>";
		opterateHtml += " <option value=\"<=\"><=</option>";
	}
	
	$("#tableExpression #selectOperator").html(opterateHtml);
};


//保存文本区的输入位置
function storeCaret(oTextarea){
    if (oTextarea.createTextRange) {
		//用一个属性记录当前位置
        oTextarea.caretPos = document.selection.createRange().duplicate();
    }
};

//将运算符插入文本区
function insertAtCaret(text){
	
	text = ' ' + text + ' ';
    var oTextarea = $("#tableExpression #textareaExpression")[0];
    if (oTextarea.createTextRange && oTextarea.caretPos) {
		var caretPos = oTextarea.caretPos;

		if (caretPos) {
			caretPos.text += text;
		}else{
			oTextarea.value += text;
		}
	}
	else {
		oTextarea.value  += text;
	}
    
};
/*背景 end*///--------------------------------------------工作流对象工厂类-----------------------------------------------------------------
/**
 * 创建工作流对象工厂实例
 * @class 工作流对象工厂类
 * @constructor
 */
EAPP.Workflow.Factory = function(){
	
	//类型名称
	this.typeName = "EAPP.Workflow.Factory";
		
	/**
	 * 获取工作流对象实例
	 * @type EAPPWorkflow
	 * @return 返回工作流实例
	 */
	this.getInstance = function(){
		if(gEAPPWorkflow) return gEAPPWorkflow;
		//不存在，创建一个实例
		gEAPPWorkflow = new EAPP.Workflow.EAPPWorkflow();
		return gEAPPWorkflow;
	};
};

/**
 * 全局工作流实例对象
 * @type EAPPWorkflow
 */
var  gEAPPWorkflow = null;
if(!gEAPPWorkflow){gEAPPWorkflow = new EAPP.Workflow.Factory().getInstance(); }/**
 * 解析工作流xml文件
 * @param {Object} xmlString
 */
EAPP.Workflow.EAPPWorkflow.prototype.parseXml = function(xmlString){
	
	    var xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
        xmlDoc.async = false;
		//2008年11月30日13时49分36秒 修正
		try{
        	xmlDoc.loadXML(xmlString);	//对文件和XML片断的加载
		}catch(e){
			xmlDoc.load(xmlString);		//对完整XML文档和URL的加载
		} 
		
		var flowNode = $('flow-definition',xmlDoc);
		if(!flowNode){return false;}
		
		var flow_key = $(flowNode).attr('flow-key');
		var state = $(flowNode).attr('state');
		var version = $(flowNode).attr('version');
		
		//初始化属性
		//this.initWorkflowProperty();
		var attr_workflow =this.attributeArray;
		if(!this.attributeArray){return false;}
		
		var attr_flow_key = getAttribute(this,"flow-key");
		var attr_state = getAttribute(this,"state");
		var attr_version = getAttribute(this,"version");
		
		//给属性赋值
		attr_flow_key.value = flow_key;
		attr_state.value = state;
		attr_version.value = version;
		
		this.parseGraphicXml(xmlString);
		
};/**
 * @author Caijianfeng
 */


	function generateXML(shape,parentTab,topLevel){
		
		var curTab = parentTab + '\t';
		if(shape.typeName == "EAPP.Workflow.EAPPWorkflow"){
			curTab = parentTab;
		}
		
		//自定义生成
		if(shape.isSuspendChildXml){
			return shape.getXmlString();
		}
		
		//自定义生成
		if(shape.getXmlType == "customXmlString"){
			return shape.getCustomeXmlString();
		}
		
		
		var attributeNodeString = "";
		var chldNodeString ="";
		
		var attributeArray = shape.attributeArray;
		if(null != attributeArray){
		
			//先遍历元素属性
			for(var i=0; i<attributeArray.length; i++){
				var attr = attributeArray[i];
				
				//如果当前属性无效，则跳过
				if(!attr.isEnable ){continue;}
				
				if(attr.mapType == "attribute"){
					//如果当前属性有值
					if(attr.getXmlString() || attr.isForceSaveXml){
						attributeNodeString += " " + attr.mapName + "=\"" + attr.getXmlString() + "\" ";
					}
				}
			}
	
			//遍历子结点
			for(var i=0; i<attributeArray.length; i++){
				var attr = attributeArray[i];
				
				//如果当前属性无效，则跳过
				if(!attr.isEnable ){continue;}
				
				if(attr.mapType == "child-node"){
					chldNodeString += generateXML(attr,curTab);
				}
	
			}
		}

		//连线接
		var lineNodeString ="";
		if(topLevel && shape.nodeType =="shape"){//图形
				for(var lineIndex=0; lineIndex<shape.innerLine.relationArray.length; lineIndex++){
					var lineRelation = shape.innerLine.relationArray[lineIndex];
					var curLine = lineRelation.line;
					if(!curLine || lineRelation.lineDirection == "in"){continue;}
					
					lineNodeString +="\n" + curTab  + " " + generateXML(curLine,curTab,true);
				}
		}
		
		var selfXmlString = "";
		if(!topLevel){
			selfXmlString = shape.getXmlString();
		}
		
		//如果子属性性与子结点全为空
		if(!attributeNodeString && !chldNodeString && !lineNodeString && !selfXmlString){
			return "";
		}
		
		//获取节点名称
		var nodeName = "";
		if(topLevel && shape.nodeType =="shape"){//图形
			nodeName = eval("gEAPPWorkflow.xmlNodeMap['" + shape.typeName + "']");
		}else if(topLevel && shape.nodeType =="line"){//连接线
			nodeName = "transition";
		}else if(shape.typeName == "EAPP.Workflow.EAPPWorkflow"){
			nodeName = "flow-definition"
		}else{
			nodeName = shape.mapName;
		}
		
		//组合xml
		var resultXML ="";
		resultXML += "\n" + curTab + "<"+nodeName + " " + attributeNodeString + ">";
		resultXML += "\n" + curTab + "\t" + selfXmlString;
		resultXML += chldNodeString;
		resultXML += lineNodeString;
		
		if(shape.typeName != "EAPP.Workflow.EAPPWorkflow"){
			resultXML += "\n" + curTab + "</"+nodeName + ">";
		}
		
		//遍历子结点
		return resultXML;
	};
		

/**
 * 是否已修改
 */
EAPP.Workflow.EAPPWorkflow.prototype.hasChanged = false;


/**
 * 表达式编辑器面板是否正在显示
 */
EAPP.Workflow.EAPPWorkflow.prototype.isShowExpressionPanel = false;

/**
 * 选择人员的函数
 * @objInput {Object} 页面上的输入框
 */
EAPP.Workflow.EAPPWorkflow.prototype.unloadWindow = function(){
	if(this.hasChanged){
		window.event.returnValue= "警告：当前流程已修改，但未进行保存，建议点击“取消”进行保存!"; 
	}
};/**
 * @author Caijianfeng
 */

/**
 * 选择人员的方法
 */
EAPP.Workflow.EAPPWorkflow.prototype.choosePersonMethod = showChooseUserDialog;

/**
 * 设置选择人员的方法
 * @param {String} methodName
 */
EAPP.Workflow.EAPPWorkflow.prototype.setChoosePersonMethod = function(methodName){
	this.choosePersonMethod = methodName;
};

/**
 * 选择人员的函数
 * @objInput {Object} 页面上的输入框
 */
EAPP.Workflow.EAPPWorkflow.prototype.choosePerson = function(inputId){
	if(!this.choosePersonMethod || !inputId){return false;}
	var objTextarea = document.getElementById(inputId);
	if(!objTextarea){return false;}
	try{
		this.choosePersonMethod(inputId);
	}catch(e){
		throw e;
		return;
	}
};

/**
 * 选择角色的方法
 */
EAPP.Workflow.EAPPWorkflow.prototype.chooseRoleMethod = showChooseRoleDialog;

/**
 * 设置选择角色的方法
 * @param {String} methodName
 */
EAPP.Workflow.EAPPWorkflow.prototype.setChooseRoleMethod = function(methodName){
	this.chooseRoleMethod = methodName;
};

  
/**
 * 选择角色的函数
 * @objInput {Object} 页面上的输入框
 */
EAPP.Workflow.EAPPWorkflow.prototype.chooseRole = function(inputId){
	if(!this.chooseRoleMethod || !inputId){return false;}
	var objTextarea = document.getElementById(inputId);
	if(!objTextarea){return false;}
	try{
		this.chooseRoleMethod(inputId);
	}catch(e){
		throw e;
		return;
	}
};


/**
 * 表达式的方法
 */
EAPP.Workflow.EAPPWorkflow.prototype.validExpressionMethod = null;

/**
 * 设置验证表达式的方法
 * @param {String} methodName
 */
EAPP.Workflow.EAPPWorkflow.prototype.setValidExpressionMethod = function(methodName){
	this.validExpressionMethod = methodName;
};

/**
 * 获取验证表达式的方法
 */
EAPP.Workflow.EAPPWorkflow.prototype.getValidExpressionMethod = function(){
	return this.validExpressionMethod;
};

/**
 * 
 * @param {Object} objInput
 */
EAPP.Workflow.EAPPWorkflow.prototype.validExpression = function(objInput){
	if(!objInput){return false;}
	var expressionValue = objInput.value;
	expressionValue = expressionValue?expressionValue.trim():"";
	
	if(!expressionValue){
		alert("表达式为空,不能保存！\n 如要取消保存，请直接点击取消按钮！");
		return false;
	}
	
	var validXml = "<expression-check>";
	
	//再获取meta  xml 
	var metaXml = this.getShortMetadataXml();
	if(!metaXml){alert("验证错误，元数据不存在！");return false;}
	
	validXml += metaXml;
	validXml += "\n\t<expression><![CDATA[" + expressionValue + "]]></expression>";
	validXml += "\n</expression-check>";
	
	var validResult = false;
	try{
		validResult = this.validExpressionMethod(validXml);
	}catch(e){
		alert("表达式验证失败,缺少验证函数！");
		return false;
		
	}
	
	//返回验证结果
	return validResult;
	
};



	function showChooseUserDialog(inputId){
		var inputValue = document.getElementById(inputId).value;
		
		var chooseUserDivHTML = "<div id=\"chooseUserDiv\">";
			chooseUserDivHTML +="<table  id=\"tableExpression2\" width='96%' border='0' align='center' cellpadding='0' cellspacing='0'>";
			chooseUserDivHTML +="<tr><td>";
			chooseUserDivHTML +="<textarea id=\"textareaExpression\" name='textarea' class='wfArea'  ";
			
			//限制长度
			chooseUserDivHTML += " onkeypress=\"new EAPP.Workflow.Graphic.Util.Textarea.MaxLength().onKeyPress(this,2000)\"";
		    chooseUserDivHTML += " onkeydown=\"new EAPP.Workflow.Graphic.Util.Textarea.MaxLength().onKeyDown(this,2000)\"";
			chooseUserDivHTML += " onbeforepaste=\"new EAPP.Workflow.Graphic.Util.Textarea.MaxLength().onBeforePaste(this,2000)\"";
			chooseUserDivHTML += " onpaste=\"new EAPP.Workflow.Graphic.Util.Textarea.MaxLength().onPaste(this,2000)\"";			
			chooseUserDivHTML += " >";
			
			chooseUserDivHTML += inputValue?inputValue:"";//添加当前的值
			
			chooseUserDivHTML +="</textarea>";
			chooseUserDivHTML +="</td></tr>";
			chooseUserDivHTML +="</table>";
	 		chooseUserDivHTML +="<input type='button' id='btnEnter' value='确定' class='wfBtn' />";
	 		chooseUserDivHTML +="<input type='button' id='btnCancel' value='取消' class='wfBtn' onclick=\"closeWindow();\" />";
			chooseUserDivHTML +="</div>";
			
		//alert(inputId + " value " + inputValue);
		showMessageBox('选择用户',chooseUserDivHTML,500,238);
		
	 
		//绑定确定提交事件
		$("#chooseUserDiv>#btnEnter").click(function(){
			var inputObj = document.getElementById(inputId);
			inputObj.value = $("#chooseUserDiv #textareaExpression").val();
			gEAPPWorkflow.updateAttributeValue(inputObj);
			closeWindow();
		});
	 
		
	}

	
	function showChooseRoleDialog(inputId){
		var inputValue = document.getElementById(inputId).value;
		
		var chooseUserDivHTML = "<div id=\"chooseRoleDiv\">";
			chooseUserDivHTML +="<table  id=\"tableExpression2\" width='96%' border='0' align='center' cellpadding='0' cellspacing='0'>";
			chooseUserDivHTML +="<tr><td>";
			chooseUserDivHTML +="<textarea id=\"textareaExpression\" name='textarea' class='wfArea' ";
			//限制长度
			chooseUserDivHTML += " onkeypress=\"new EAPP.Workflow.Graphic.Util.Textarea.MaxLength().onKeyPress(this,2000)\"";
		    chooseUserDivHTML += " onkeydown=\"new EAPP.Workflow.Graphic.Util.Textarea.MaxLength().onKeyDown(this,2000)\"";
			chooseUserDivHTML += " onbeforepaste=\"new EAPP.Workflow.Graphic.Util.Textarea.MaxLength().onBeforePaste(this,2000)\"";
			chooseUserDivHTML += " onpaste=\"new EAPP.Workflow.Graphic.Util.Textarea.MaxLength().onPaste(this,2000)\"";			
			chooseUserDivHTML += " >";
			
			chooseUserDivHTML += inputValue?inputValue:"";//添加当前的值
			
			chooseUserDivHTML +="</textarea>";
			chooseUserDivHTML +="</td></tr>";
			chooseUserDivHTML +="</table>";
	 		chooseUserDivHTML +="<input type='button' id='btnEnter' value='确定' class='wfBtn' />";
	 		chooseUserDivHTML +="<input type='button' id='btnCancel' value='取消' class='wfBtn' onclick=\"closeWindow();\" />";
			chooseUserDivHTML +="</div>";
			
		//alert(inputId + " value " + inputValue);
		showMessageBox('选择角色',chooseUserDivHTML,500,238);
		
		//绑定确定提交事件
		$("#chooseRoleDiv>#btnEnter").click(function(){
			var inputObj = document.getElementById(inputId);
			inputObj.value = $("#chooseRoleDiv #textareaExpression").val();
			gEAPPWorkflow.updateAttributeValue(inputObj);
			closeWindow();
		});
	}



/**
 * 获取工作流对象的名称
 */
EAPP.Workflow.EAPPWorkflow.prototype.getFlowName = function(){
	return getAttributeValue(this,'name',true);
};

/**
 * 获取工作流对象的flow-key
 */
EAPP.Workflow.EAPPWorkflow.prototype.getFlowkey = function(){
	return getAttributeValue(this,'flow-key',true);
};

/**
 * 获取工作流对象的名称
 */
EAPP.Workflow.EAPPWorkflow.prototype.getFlowVersion = function(){
	return getAttributeValue(this,'version',true);
};

/**
 * 所引用的工作流设计器组件根路径
 */
EAPP.Workflow.EAPPWorkflow.prototype.basePath = "";/**
 * @author Caijianfeng
 */


/**
 * 存放元数据的数组
 */
EAPP.Workflow.EAPPWorkflow.prototype.metadata = new Array();

EAPP.Workflow.EAPPWorkflow.prototype.resetMeta = function(){
	this.metadata = new Array();
};


/**
 * 设置元数据<br>
 * <pre>
 * 目前支持两种传入参数的方式
 * 1.xmlString 传入一个xml文件片断，格式如下
 * &lt;metas&gt;
 * 	&lt;meta name="admin"  display-name="管理员"  type="DATATYPE_STRING"  not-null="false" /&gt;
 * &lt;/metas&gt;
 * 
 * 2.传入4个参数(name,display-name,type,not-null)
 * 一次构造一个meta元素
 * name 		meta的名称
 * display-name	显示名称
 * type 		meta的数据类型
 * not-null 	meta能否为空
 * </pre>
 */
EAPP.Workflow.EAPPWorkflow.prototype.setMetadata = function(){
	
	//传入xml片断
	if(arguments.length == 1){
		//重置meta 
		this.resetMeta();

		var xmlString = arguments[0];
		
        var xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
        xmlDoc.async = false;
        var flag = false;
		
		try{
        	flag = xmlDoc.loadXML(xmlString);	//对文件和XML片断的加载
		}catch(e){
			flag = xmlDoc.load(xmlString);		//对完整XML文档和URL的加载
		} 
		
		if(!flag){return false;}
		
		var metadataNodes = $('meta',xmlDoc);
		for(var i=0; i<metadataNodes.length; i++){
			var node = metadataNodes[i];
			
			var name = $(node).attr('name');
			if(!this.checkUniqueMeta(name)){continue;}
			
			var display_name = $(node).attr('display-name');
			var type = $(node).attr('type');
			var not_null = $(node).attr('not-null');
			
			var meta = {name:name,display_name:display_name,type:type,not_null:not_null};
			this.metadata.push(meta);
		}
	}else if(arguments.length == 4){
			//参数形式分别传入
		
			var name = arguments[0];
			if(!this.checkUniqueMeta(name)){return false;}
			
			var display_name = arguments[1];
			var type = arguments[2];
			var not_null = arguments[3];
			
			var meta = {name:name,display_name:display_name,type:type,not_null:not_null};
			this.metadata.push(meta);
	}
};

/**
 * 验证当前元数据的唯一性
 * @param {Object} metaName
 * @type {Boolean}
 * @return 唯一返回true,不唯一返回false
 */
EAPP.Workflow.EAPPWorkflow.prototype.checkUniqueMeta = function(metaName){
	for(var i=0; i<this.metadata.length; i++){
		var item = this.metadata[i];
		if(item.name == metaName){
			return false;
		}
	}
	return true;
};

/**
 * 获取元数据
 */
EAPP.Workflow.EAPPWorkflow.prototype.getMetadataXml = function(){
	
	var xmlString = "";
	for(var i=0; i<this.metadata.length; i++){
		var item = this.metadata[i];
		xmlString += "\n\t\t<meta name=\"" + item.name + "\" display-name=\"" + item.display_name + "\" type=\"" + item.type + "\" not-null=\"" + item.not_null + "\" />";
	}
	return xmlString;
};

/**
 * 获取元数据
 * 简略版，只包括name和type
 */
EAPP.Workflow.EAPPWorkflow.prototype.getShortMetadataXml = function(){
	
	var xmlString = "\n<metas>";
	for(var i=0; i<this.metadata.length; i++){
		var item = this.metadata[i];
		xmlString += "\n\t<meta name=\"" + item.name + "\"  type=\"" + item.type + "\"/>";
	}
	xmlString += "\n</metas>";
	return xmlString;
};


/**
 * 获取元数据
 * 简略版，只包括name和type
 */
EAPP.Workflow.EAPPWorkflow.prototype.getMetaHtml = function(){
	
	var xmlString = "";
	for(var i=0; i<this.metadata.length; i++){
		var item = this.metadata[i];
		xmlString += "<option value=\"" + item.name + "\">" + item.name + "</option>";
	}
	return xmlString;
};


/**
 * 通过传入名称，获取元数据名称
 */
EAPP.Workflow.EAPPWorkflow.prototype.getMetaByName = function(name){
	
	if(!name){return null;}
	name = name.replace(/(^\s*)|(\s*$)/g, "");
	
	for(var i=0; i<this.metadata.length; i++){
		var item = this.metadata[i];
		if(item.name == name){
			return item;
		}
	}
	return null;;
};



/**
 * 传入显示名称，获取元数据名称
 */
EAPP.Workflow.EAPPWorkflow.prototype.getMetaName = function(displayName){
	
	if(!displayName){return null;}
	displayName = displayName.replace(/(^\s*)|(\s*$)/g, "");
	
	for(var i=0; i<this.metadata.length; i++){
		var item = this.metadata[i];
		if(item.display_name == displayName){
			return item.name;
		}
	}
	return null;;
};
/**
 * @author Caijianfeng
 */


	/**
	 * 生成对象的属性面板字符串
	 * @param {Object} object 
	 * @param {String} strPreAttribute 上级属性前缀
	 */		
	function generatePropertyPanel(typaName,object,strPreAttribute){
		//验证传入参数
		if(!object){return false;}
		
		var attributeArray = object.attributeArray;
		if(!attributeArray || !attributeArray.length ){ return "";}
	
		//表格头
		var panelHTML = "<table  border=\"0\" cellpadding=\"0\" cellspacing=\"0\"   style=\"width:100%; border-bottom:1px solid #AEC8EA\">";
		for(var i=0; i<attributeArray.length; i++){
			panelHTML += "<tr>";
			
			//当前属性对象
			var attr = attributeArray[i];
			
 			
			//如果当前属性无效或不可见，则跳过
			if(!attr.isEnable //当前属性无效，直接跳过
				//当前属性不可见，且子属性为空
				|| ((!attr.attributeArray  || !attr.attributeArray.length) && !attr.isVisible)){
					continue;
					//跳过无效 或 隐藏且子属性为空的 属性					
 			}
			
			//拼接属性访问名称
			var depthName = strPreAttribute + '#' + attr.attributeName ;
			if(!strPreAttribute){
				depthName = object.id + "#" + typaName + '#' + attr.attributeName;//如果是第一级，前面加上类型名称 以保持唯一性
			}
			
						
			//如果当前属性包含子属性，则有必要显示标题栏
			if(attr.attributeArray  &&  attr.attributeArray.length){
					panelHTML += "<td colspan=\"2\"><table  border=\"0\"  cellpadding=\"0\" cellspacing=\"0\"  style=\"width:100%\" >";
					panelHTML += "<tr>";
			 
			 		//显示属性标题栏
					if (!attr.isHideHeader) {
						//获取属性标题名称
						var headerDisplayName = attr.displayName;
						if(attr.headerType == "getDisplayName"){
							headerDisplayName = attr.getDisplayName();
						}
						
						//2008年12月1日14时12分48秒 如果太长，则进行截断
						if(headerDisplayName && headerDisplayName.length > 20 ){
							var headerLength = headerDisplayName.length;
							//截取前16个字符 与 最后5个字符
							headerDisplayName = headerDisplayName.substring(0,16) + "..." + headerDisplayName.substring(headerLength -5,headerLength);
						}
						// 生成属性标题栏的表格行
						panelHTML += "<td colspan=\"2\" class=\"sxSubHead\"  >" + headerDisplayName + "</td></tr><tr>";
					}
			}
			
			//显示当前对象的属性值
			if (attr.inputType && attr.inputType != 'none' ) {
				panelHTML += "<td class=\"sxLeft\"><div style=\"width:100px\">" + attr.getDisplayName() + "：</div></td>";
				panelHTML += "<td class=\"sxRight\">";
			}
			
		
			//获取输入项的数据源   	//以下几行，抽出成函数 
			var dataSource = null;
			if(attr.dataSourceType == "direct"){
				//直接取值
				dataSource = attr.dataSource;
			}else if(attr.dataSourceType == "method"){
				//执行方法获取数据
				dataSource = eval(attr.dataSource);
			}else if(attr.dataSourceType == "none"){
				dataSource = null;
			}
			
			var tdInnerHTML = "";
			//根据输入的类型生成不同的td
			switch(attr.inputType){
				case 'input_text'://输入框
					tdInnerHTML += "<input class=\"sxIpt\" id=\"" + depthName + "\" type=\"text\" ";					
					//过滤输入
					tdInnerHTML += " onkeydown=\"checkInput(this);\" ";					
					//增加最大长度限制
					if(attr.maxLength){tdInnerHTML += " maxlength=\"" + attr.maxLength + "\" ";} 
					tdInnerHTML +=  " onblur=\"replaceHTML(this);gEAPPWorkflow.updateAttributeValue(this);\" ";
					tdInnerHTML +=  " onpaste=\"gEAPPWorkflow.hasChanged = true;\" ";
					//tdInnerHTML += " value=\"" + attr.getValue() + "\" ";					
					tdInnerHTML += "/>";				
					
					//tdInnerHTML = "<input type=\"text\"  maxLength=\"20\" style=\"width:20px;\"/>";
					break;
				case 'input_textarea'://文本框
					tdInnerHTML += getTextareaTD(attr,depthName);
					break;
				
				case 'input_select'://下拉框
					tdInnerHTML += "<select class=\"sxIpt\" id=\"" + depthName + "\"  onchange=\"gEAPPWorkflow.updateAttributeValue(this);\" ";
					tdInnerHTML += " onkeydown=\"EnterOk(this);\" ";
					//tdInnerHTML += " value=\"" + attr.getValue() + "\" ";
					tdInnerHTML += " >";
					
					//生成下拉框选项
					if(dataSource != null){
						for(var item in dataSource){
							try{
							var itemValue = eval("dataSource['" +  item +"']");
							tdInnerHTML += "<option value=\"" + itemValue + "\">" + item + "</option>";
							}catch(e){}
						}
					}
					tdInnerHTML += "</select>";
					break;
				
				case 'input_checkbox':
					tdInnerHTML += "<input id=\"" + depthName + "\" type=\"checkbox\" onclick=\"gEAPPWorkflow.updateAttributeValue(this);\"  checked=\"" + attr.getValue()  + "\"/>是";
					break;
				case "html"://采用HTML自定义				
					tdInnerHTML += attr.inputString;
					break;
				case 'none'://不显示
					break;
				
				//只读
				case 'readonly'://采用div显示
					tdInnerHTML += "<div id=\"" + depthName + "\" >" ;
					//tdInnerHTML +=  attr.getValue() ;
					tdInnerHTML += "</div>";
					break;
			}
			
			if (attr.inputType != 'none') {
				panelHTML += tdInnerHTML;
				panelHTML += "</td>";
			}			
			panelHTML += "</tr>";
			
			
			//如果有子属性，那么生成子属性
			if(attr.attributeArray != null &&  attr.attributeArray.length>0){
				var result = generatePropertyPanel(typaName,attr,depthName);
				if(result){					
					panelHTML += "<tr><td colspan=\"2\">";
					panelHTML += result;
					panelHTML += "</td></tr></table></td></tr>";
				}
			}
			
		}
		panelHTML += "</table>";
		return panelHTML;
	};
	

/**
 * 替换输入的html中< >符号
 * @param {Object} objInput
 */	
function checkInput(EntityTextBox){
	if ((event.keyCode == 60 || event.keyCode == 62 || event.keyCode == 34) //< > " &
		|| (event.keyCode == 38 )){
		event.keyCode = 0;
	}
	
	//已经修改
	gEAPPWorkflow.hasChanged = true;
	
	//实现回车确定的功能
	if(event.keyCode == 13){
		
		//不是图元上的编辑框
		if(EntityTextBox.tagName.toLowerCase() != "span"){
			event.keyCode = 9;
			return true;
		}else{
			$(EntityTextBox).blur();
		}
	}
}


function EnterOk(EntityTextBox){
 
	//实现回车确定的功能
	if(event.keyCode == 13){
		event.keyCode = 9;	
		return true;
		//$(EntityTextBox).blur();
	}
}
 
function replaceHTML(objInput){
    objInput.value = objInput.value.replace(/</g, "");
    objInput.value = objInput.value.replace(/>/g, "");
    objInput.value = objInput.value.replace(/&/g, "");
    objInput.value = objInput.value.replace(/"/g, "");
	return true;
}


/**
 * 限制字符串最大长度
 * @param str 字符串
 * @param iMaxLength 最大长度
 */
function limitInputLength(oText,iMaxLength){
	var strText = $(oText).val();
	strText = strText?strText:"";
	
	if(Number(strText.length) > Number(iMaxLength)){
		strText = strText.substring(0,iMaxLength);
		alert("输入字符太长被截断！");
	}
	return $(oText).val(strText);
}


function getTextareaTD(attr,id){
	if(!attr || !id){return "";}
	
	var tdInnerHTML = "";
	tdInnerHTML += "<textarea  class=\"sxIpt\" id=\"" + id + "\"";
	
	tdInnerHTML += " onkeydown=\"EnterOk(this);\" ";//回车确定
	tdInnerHTML += " onkeyup=\"gEAPPWorkflow.hasChanged = true;\" ";
	tdInnerHTML += " onpaste=\"gEAPPWorkflow.hasChanged = true;\" ";//已修改
	
	//增加文本区的长度限制
	/**
	if(attr.maxLength){
		tdInnerHTML += " onkeypress=\"new EAPP.Workflow.Graphic.Util.Textarea.MaxLength().onKeyPress(this," + attr.maxLength + ")\"";
		tdInnerHTML += " onkeydown=\"new EAPP.Workflow.Graphic.Util.Textarea.MaxLength().onKeyDown(this," + attr.maxLength + ");EnterOk(this);\"";
		tdInnerHTML += " onbeforepaste=\"new EAPP.Workflow.Graphic.Util.Textarea.MaxLength().onBeforePaste(this," + attr.maxLength + ")\"";
		tdInnerHTML += " onpaste=\"gEAPPWorkflow.hasChanged = true;new EAPP.Workflow.Graphic.Util.Textarea.MaxLength().onPaste(this," + attr.maxLength + ")\"";
	} 
	**/	
	
	tdInnerHTML +="  onblur=\"";
	if (attr.maxLength) {//校验字符串太长
		tdInnerHTML += "limitInputLength(this," + attr.maxLength + ");";
	}					
	tdInnerHTML += "gEAPPWorkflow.updateAttributeValue(this);\" ";	
	//tdInnerHTML += " value=\"" + attr.getValue() + "\" ";
	tdInnerHTML += "/>";	
	
	return tdInnerHTML;
}


function getExpressionTD(attr,id){
	if(!attr || !id){return "";}
	var tdInnerHTML = getTextareaTD(attr,id);
	if(!tdInnerHTML){return "";}
	
	var btnEditHtml = "";
	btnEditHtml += "<button id=\"btn_"+ id + "\"";
	btnEditHtml += "onclick=\"showExpressionDialog('"+ id + "');\"";
	btnEditHtml += ">编辑</button>";
	
	tdInnerHTML += btnEditHtml;
	return tdInnerHTML;
}


function getShapeInnerHTML(shape){
	

	
	//内嵌图片
	var imgHTML = "<img src=\"" + shape.innerIconSrc + "\" style=\"width:16px;height:16px;\"/>";
	
	var attr_name_id = shape.id + "#" +  shape.typeName + "#name";
	
	var imgTdHTML = "<td style=\"width:16px;\">" + imgHTML + "</td>"; //图片td
	var spaceTdHTML = "<td style=\"width:5px;\"/>";//间隔
	var innerBox = "<td><span  id=\"td_editbox\" ";
	 innerBox += " onkeypress=\"checkInput(this);\" ";
	//离开时更新名称
	innerBox += " onblur=\"replaceSpanHTML(this);limitMaxLength(this,200);setEntityTextBoxStyle(this);gEAPPWorkflow.updateAttributeValue(this,'" + attr_name_id + "')\"  " ;
	
	innerBox += " onkeypress=\"checkEnter(this);\" " ;//2008年11月29日13时53分17秒 添加
	
	innerBox += " >";//end 
	innerBox +=  shape.text ;
	
	//验证最大长度
	//innerBox += "";
	
	innerBox +=  "</span></td>";
	
	//添加内嵌样式
	shape.element[0].innerHTML += gEAPPWorkflow.entityStyle;				
	//添加内部文本框
	shape.element[0].innerHTML += "<div><div><table><tbody><tr>" + imgTdHTML + spaceTdHTML + innerBox +  "</tr></tbody></table><div></div>"; 
	
};

//2008年12月3日10时23分34秒
function replaceSpanHTML(objInput){
	var strTemp = $(objInput).text();
	if(!strTemp){return true;}
    strTemp = strTemp.replace(/</g, "");
    strTemp = strTemp.replace(/>/g, "");
    strTemp = strTemp.replace(/&/g, "");
    strTemp = strTemp.replace(/"/g, "");
	$(objInput).text(strTemp);
}




/**
 * 限制字符串最大长度
 * @param str 字符串
 * @param iMaxLength 最大长度
 */
function limitMaxLength(oDiv,iMaxLength){
	var strText = $(oDiv).text();
	strText = strText?strText:"";
	
	if(Number(strText.length) > Number(iMaxLength)){
		strText = strText.substring(0,iMaxLength);
		alert("输入字符太长被截断！");
	}
	$(oDiv).text(strText);
}


/**
 * 附加shape的事件
 * @param {Object} shape
 */
function attatchShapeEvent(shape){
	
	shape.element.click(function(){
		//显示属性面板 
		gEAPPWorkflow.showPropertyPanel(shape);
	}).dblclick(function(){
		//快速编辑 
		gEAPPWorkflow.ContextMenuItemClick("edit");
	});
};
/**
 * EAPP.Workflow 的图元对象
 * date 2008年9月23日
 * 本文档生成采用http://jsdoc.sourceforge.net/的生成工具
 */
//-------------------------------------------------以下为图元对象化声明-----------------------------------------------------------	

	
	
	//--------------------------------------------小标对象----------------------------------------------------
	/**
	 * 构建一个小标对象
	 * @class 小标对象
	 * @constructor
	 */
	EAPP.Workflow.Flag = function(index){
		//类型名称
		this.typeName = "EAPP.Workflow.Flag";
		//结点类型
		this.nodeType = "flag";// nodeType可以采用枚举类型表示	
		
		//id
		this.id = 'p' + index; //由于小标的Id是固定的，所以采用外部传参的形式构建
		//所包含的元素(jQuery对象)
		this.element = null;
		//DOM元素
		this.domElement = null;
		
		/** 
		 *  绘制
		 * @type {Boolean}
		 * @return 返回绘制是否成功
		 */
		this.render = function(){
			try {
				//创建元素
				this.domElement = document.createElement("v:oval");
				this.element = $(this.domElement);
				
				this.element.attr({//属性
					id: this.id,
					className: "resizePoint"
				}).css({//样式				// 样式有可能在CSS利用class直接控制
					"zIndex":"9999",//控制显示的层次
					position: "absolute",
					width: "5px",
					height: "5px",
					display: "none",
					left: "-99px",
					top: "-99px",
					cursor: gEAPPWorkflow.resizeFlagCursorStyles[index]
				});
				return true;
			} 
			catch (e) {
				//trace(this.typeName + " render 绘制失败！" + e);
				return false;
			}
		};//end render
		
		/** 
		 *  获取图元的DOM元素对象
		 * @return {Element} 返回当前对象的DOM元素对象
		 */
		this.getDomElement = function(){
			return document.getElementById(this.id);
		};
		
		/** 
		 *  获取图元元素的jQuery对象
		 * @return {Object}(jQuery) 返回当前对象元素的jQuery对象
		 */
		this.getElement = function(){
			this.domElement = document.getElementById(this.id);
			return $(this.domElement);
		};
	};
	//--------------------------------------------------------------------------------------------------------
	
	
	//-------------------------------连接线(StrokeLine)----------------------------------------------------------------
	/**
	 * @class 连接线
	 * @constructor
	 */
	EAPP.Workflow.StrokeLine = function(saveId){
		//类型名称
		this.typeName = "EAPP.Workflow.StrokeLine";
		//结点类型
		this.nodeType = "line";
		//图片路径
		this.imgSrc = gEAPPWorkflow.basePath + "/images/mLine01.gif";
		
		//系统生成的id
		this.generateId = gEAPPWorkflow.createGUID();
		//对象的ID
		this.id = saveId?saveId:'line_' + this.generateId;
		//对象的元素
		this.element = null;
		//DOM元素
		this.domElement = null;
		
		//起始与终止
		this.from = null;
		this.to = null;
		
		//连接的两个元素及Id
		this.fromId = null;
		this.fromShape = null;
		this.toId = null;
		this.toShape = null;
		
		//文本
		this.text = "线" +  this.generateId;
		//描述
		this.description = "";
		//表达式/条件
		this.condition = null;
		//所关联的action
		this.action = {
			id: null,
			name: null,
			className: null
		};
		
		/**
		 * 属性数组
		 * @type Array
		 */	
		this.attributeArray = new Array();
		
		//预注册属性--------------------------------------------------
		
		//名称属性
		var attr_name = new EAPP.Workflow.Graphic.Attribute.Name();
		attr_name.displayName = "路径名称";
		attr_name.value = this.text;
		
		//to属性
		var attr_to = new EAPP.Workflow.Graphic.Attribute.NormalAttribute();
		attr_to.attributeName = 'to';
		attr_to.displayName = '指向';
		attr_to.mapName = 'to';
		attr_to.mapType = 'attribute';
		attr_to.isNull = false;//可为空
		attr_to.isEnable = true;
		attr_to.isVisible = false;
		attr_to.defaultValue = "";//默认值
		attr_to.validType = 'none';
		attr_to.inputType = 'readonly';
		attr_to.dataType = 'string';// 引用属性
		
		//条件属性
		var attr_condition = new EAPP.Workflow.Graphic.Attribute.NormalAttribute();
		attr_condition.attributeName = 'condition';
		attr_condition.displayName = '条件表达式';
		attr_condition.mapName = 'condition';
		attr_condition.mapType = 'child-node';
		attr_condition.inputType = 'html';
		attr_condition.dataType = 'cdata';
		attr_condition.maxLength = 2000;
		
		var attr_condition_id = this.id + "#" + this.typeName + "#condition";
		var attr_condition_html = getExpressionTD(attr_condition,attr_condition_id);		
		attr_condition.inputString = attr_condition_html;
		
		//说明属性
		var attr_description = new EAPP.Workflow.Graphic.Attribute.Description();
		attr_description.displayName = "路径描述";
		
		//action属性
		var attr_action = new EAPP.Workflow.Graphic.Attribute.Action();
		attr_action.displayName = "执行程序";
		
		//设置name属性
		var attr_action_name = getAttribute(attr_action,"name");
		attr_action_name.isVisible = false;  
		
		
		//添加到当前属性数组
		this.attributeArray.push(attr_name);
		this.attributeArray.push(attr_action);
		this.attributeArray.push(attr_to);
		this.attributeArray.push(attr_condition);
		this.attributeArray.push(attr_description);
		
		/** 
		 *  绘制
		 * @type Boolean
		 * @return 返回绘制是否成功
		 */
		this.render = function(){
			try {
				//创建元素
				this.domElement = document.createElement("v:line");
				this.element = $(this.domElement);
				
				this.element.attr({//属性
					id: this.id,
					name: 'stroke',//name属性暂时为图形的形状 //
					className: "line",
					//起止属性
					from: '0,0',
					to: '0,0',
					//显示属性
					stroked: 't',
					strokecolor: '#425779',
					strokeweight: '0.75pt'
				
				}).css({//样式				
					position: "absolute",//绝对定位
					"zIndex": "9996",
					cursor: "move",//鼠标光标为十字箭头光标
					left: "0px",//左偏移
					top: "0px" //上偏移
				});
				
				// this.element 没有附加事件响应
				//事件响应包括
				//1.单击出现属性
				//2.双击快速编辑名称
				//3.失去焦点时，内部文本框不可编辑 Onblur='this.contentEditable = false;'
				
				//内部箭头
				var stroke = "<v:stroke opacity=\"1\" startarrow=\"none\" endarrow=\"classic\"/>";
				
				//添加箭头
				this.element[0].innerHTML += stroke;
				
				var attr_name_id = this.id + "#" +  this.typeName + "#name";
				//添加内部文本框
				this.element[0].innerHTML += "<div><div><span id=\"td_editbox\" onkeypress=\"checkInput(this);\" "
							+" onblur=\"replaceSpanHTML(this);limitMaxLength(this,2000);setEntityTextBoxStyle(this);gEAPPWorkflow.updateAttributeValue(this,'" + attr_name_id + "')\" >" 
							+ this.text + "</span></div></div>";
				
				//添加单击时触发click事件
				var shape = this;
				this.element.click(function(){
					//显示属性面板 
					gEAPPWorkflow.showPropertyPanel(shape);
				}).dblclick(function(){
					//快速编辑 
					gEAPPWorkflow.ContextMenuItemClick("edit");
				});
				
				return true;
			} 
			catch (e) {
				//trace(this.typeName + " render 绘制失败！" + e);
				return false;
			}
		};
		
		/**
		 * 验证线是否连接正常
		 */
		this.validConnect = function(){
			//return;// 验证事件不正常
			//如果连接不正确
			if(!this.fromId || !this.fromShape || !this.toId || !this.toShape){
				this.element.attr({strokecolor:'#FF0000'});//线变红色
				return false;
			}else{
				this.element.attr({strokecolor:'#425779'});//恢复默认颜色
				return true;
			}
		};
		
		/** 
		 *  获取图元的DOM元素对象
		 * @return {Element} 返回当前对象的DOM元素对象
		 */
		this.getDomElement = function(){
			return document.getElementById(this.id);
		};
		
		/** 
		 *  获取图元元素的jQuery对象
		 * @return {Object}(jQuery) 返回当前对象元素的jQuery对象
		 */
		this.getElement = function(){
			this.domElement = document.getElementById(this.id);
			return $(this.domElement);
		};
		
		/** 
		 *  删除连接接某一方向上的关联
		 * @param {String} lineDirection 
		 * @return {Boolean} 删除关联是否成功!
		 */
		this.removeRelByDirection = function(lineDirection){
			//try{
				//传入参数无效
				if(!lineDirection){return false;}
				//修正箭头方向参数
			   	if((lineDirection=="p8") || (lineDirection=="p9")){
					lineDirection = (lineDirection == "p8") ? "out":"in";
				}
				//箭头方向参数出错
				if(lineDirection!= "out" && lineDirection != "in" ){return false;}
				
				if(((lineDirection == "out")&&(!this.fromShape || !this.fromId))
				  ||((lineDirection == "in")&&(!this.toShape || !this.toId))){
				  		////trace("所要删除关联的图元不存在！");
						return false;
				 }
				 
				 //删除图元上的关联
				 if(lineDirection == "out"){
				 	////trace("this.fromShape.innerLine.removeLineByLineId(this.out");
				 	this.fromShape.innerLine.removeLineByLineId(this.id,"out");
					this.fromShape = null;
					this.fromId = null;
				 }else if(lineDirection == "in"){
				 	////trace("this.fromShape.innerLine.removeLineByLineId(this.in");
				 	this.toShape.innerLine.removeLineByLineId(this.id,"in");
					this.toShape = null;
					this.toId = null;
				 }
				 
				 return true;
			
			/**	
			 * 
			}catch(e){
				//trace("删除线关联失败!"+ lineDirection +"线:" + this.text + " \nError:" + e);				
				return false;
			}
			*/
		};
		
		/** 
		 *  保存为xml数据
		 * @return {String} 返回当前对象xml序列化字符串
		 */
		this.saveAsXML = function(){
		
		};
		
		/** 
		 *  解析xml数据为对象
		 * @param {String} xmlData xml序列化字符串
		 * @return {Object} 返回当前对象实例本身
		 */
		this.parseXML = function(xmlData){
			//parseing...
			return this;
		};
	};
	//--------------------------------------------------------------------------------------------------------
	
	/**
	 * @class 图元的内部连线管理对象
	 * @constructor
	 */
	EAPP.Workflow.InnerLines = function(parent){
		//类型名称
		this.typeName = "EAPP.Workflow.InnerLines";
		
		//父图元对象
		this.parent = parent;
		
		//存放连线关系的数组
		this.relationArray = new Array();
		
		/** 
		 * 添加图元的关联
		 * @param {String} pointIndex 图形建立连接的小标
		 * @param {String} lineId 关联的连接线Id
		 * @param {String} lineDirection 连接线的方向 p8 属于前端 p9属于后端(箭头端)
		 * @type Boolean
		 * @return 关联是否成功建立
		 */
		this.addLine = function(pointIndex,lineId,lineDirection){
			//try{
				
				
				//检查传入的参数
				if(!pointIndex || !lineId || !lineDirection){
					alert("传入的参数出错！");
					//trace("pointIndex "+ pointIndex + " lineId:" + lineId + " lineDirection:" + lineDirection);
					return false;
				}
				//修正箭头方向参数
			   	if((lineDirection=="p8") || (lineDirection=="p9")){
					lineDirection = (lineDirection == "p8") ? "out":"in";
				}
				//箭头方向参数出错
				if(lineDirection!= "out" && lineDirection != "in" ){
					alert("箭头方向参数出错！");
					//trace("pointIndex "+ pointIndex + " lineId:" + lineId + " lineDirection:" + lineDirection);
					return false;
				}
				//获取当前要关联的线
				var line = gEAPPWorkflow.getNodeById(lineId);
				if(!line){
					alert("线对象不存在！");
					return false;
				}//不存在则返回false
				
				//父图元丢失或关联数组无效
				if(!this.parent || !this.relationArray){
					alert("父图元丢失或关联数组无效！");
					return false;
				}
				
				// 验证连接线连接失败时，是否直接删除连接线，或者做位置移动处理
				////trace("parent " + this.parent.typeName + "  instanceof EAPP.Workflow.StartState "  + (this.parent instanceof EAPP.Workflow.StartState));
				//不能建立指向开始图元的连接线
				if((this.parent instanceof EAPP.Workflow.StartState) && lineDirection == "in" ){
					alert("建立 \""+ line.text + "\" 指向图元 \"" + this.parent.text + "\" 的连接失败！\n不能建立指向开始图元的连接线！");
					////trace("建立 \""+ line.text + "\" 指向图元 \"" + this.parent.text + "\" 的连接失败！\n不能建立指向开始图元的连接线！");
					
					return false;
				}else if((this.parent instanceof EAPP.Workflow.EndState) && lineDirection == "out" ){
					alert("建立 \""+ line.text + "\" 从图元 \"" + this.parent.text + "\" 指出的连接失败！\n不能建立由结束图元指出的连接线！");
					////trace("建立 \""+ line.text + "\" 从图元 \"" + this.parent.text + "\" 指出的连接失败！\n不能建立由结束图元指出的连接线！");
					
					return false;
				}
				
				//验证是否自关联
				////trace("line.toShape: " + line.toShape+  " && line.toId: " + line.toId +"&& lineDirection: " + lineDirection +  "  this.parent.id:  "+ this.parent.id );
				////trace("line.fromShape: " + line.fromShape+  " && line.toId: " + line.fromId +"&& lineDirection: " + lineDirection +  "  this.parent.id:  "+ this.parent.id );
				if((line.toShape && line.toId && lineDirection == "out" && line.toId == this.parent.id)
				|| (line.fromShape && line.fromId && lineDirection == "in" && line.fromId == this.parent.id)){
					//alert("建立 \""+ line.text + "\" 与图元 \"" + this.parent.text + "\" 的连接失败！\n不能建立自关联的图元(出线与入线不能指向同一个图元)！");
					////trace("建立 \""+ line.text + "\" 与图元 \"" + this.parent.text + "\" 的连接失败！\n不能建立自关联的图元(出线与入线不能指向同一个图元)！");
					
					//
					//return false;
					return true;
				}
				
				//验证重复关联
				for(var i=0; i<this.relationArray.length; i++){
					var rel = this.relationArray[i];
					
					//同一条线的多次关联 
					if(lineId == rel.line.id ){
						//alert("建立 \""+ line.text + "\" 与图元 \"" + this.parent.text + "\" 的连接失败！\n不能重复添加同一条连接线关联！");
						////trace("建立 \""+ line.text + "\" 与图元 \"" + this.parent.text + "\" 的连接失败！\n不能重复添加同一条连接线关联！");
						
						//
						//return false;
						return true;
						break;
					}
					
					//两个图元的同向多次关联
					if(line.toShape && line.toId && lineDirection == "out" && line.toId == rel.line.toId ){
						alert("建立 \""+ line.text + "\" 与图元 \"" + this.parent.text + "\" 的连接失败！\n重复关联！已存在从图元 \"" + this.parent.text + "\" 指向图元 \"" + line.toShape.text + "\" 的关联！");
						////trace("建立 \""+ line.text + "\" 与图元 \"" + this.parent.text + "\" 的连接失败！\n重复关联！已存在从图元 \"" + this.parent.text + "\" 指向图元 \"" + line.toShape.text + "\" 的关联！");
						
						return false;
						break;
					}else if(line.fromShape && line.fromId && lineDirection == "in" && line.fromId == rel.line.fromId ){
						alert("建立 \""+ line.text + "\" 与图元 \"" + this.parent.text + "\" 的连接失败！\n重复关联！已存在从图元 \"" + line.fromShape.text + "\" 指向图元 \"" + this.parent.text + "\" 的关联！");
						////trace("建立 \""+ line.text + "\" 与图元 \"" + this.parent.text + "\" 的连接失败！\n重复关联！已存在从图元 \"" + line.fromShape.text + "\" 指向图元 \"" + this.parent.text + "\" 的关联！");
						
						return false;
						break;	
					}
					
					//排除双向关联
					if(line.toShape && line.toId  && (line.toId == rel.line.toId || line.toId == rel.line.fromId)){//线的目标图元存在的情况
						//alert("建立 \""+ line.text + "\" 从图元 \"" + this.parent.text + "\" 指出的连接失败！\n重复关联！已存在图元 \"" + this.parent.text + "\" 与图元 \"" + line.toShape.text + "\" 的关联！");
						////trace("建立 \""+ line.text + "\" 人图元 \"" + this.parent.text + "\" 指出的连接失败！\n重复关联！已存在图元 \"" + this.parent.text + "\" 与图元 \"" + line.toShape.text + "\" 的关联！");
						
						//
						//return false;
						break;
					}else if(line.fromShape && line.fromId  && (line.fromId == rel.line.fromId || line.fromId == rel.line.toId)){
						//alert("建立 \""+ line.text + "\" 指向图元 \"" + this.parent.text + "\" 的连接失败！\n重复关联！已存在图元 \"" + this.parent.text + "\" 与图元 \"" + line.fromShape.text + "\" 的关联！");
						////trace("建立 \""+ line.text + "\" 指向图元 \"" + this.parent.text + "\" 的连接失败！\n重复关联！已存在图元 \"" + this.parent.text + "\" 与图元 \"" + line.fromShape.text + "\" 的关联！");
						
						//
						//return false;
						break;
					}
				}
				
				//检查新添加线的名字是否重复
				if (lineDirection == "out"){
					var lineName = getAttributeValue(line,"name");
					var isRepeatName = this.validUniqueLineName("out", line.id, getAttributeValue(line,"name"));
					if(!isRepeatName){
						lineName += "_1";
						//alert("新添加的线名称重复!");
						setAttributeValue(line,"name",lineName);
						//更新div 上的显示
						$('#td_editbox',line.element).text(lineName);
					}
				}
				
				
				//建立关联
				var relation = new Object();
				relation.shape = this.parent;
				relation.pointIndex = pointIndex;
				relation.line = line;
				relation.lineDirection = lineDirection;
				//添加到关联数组
				this.relationArray.push(relation);
				
				
				//在线上记住关联
				if(lineDirection == "out"){
					line.fromShape = this.parent;
					line.fromId = this.parent.id;
				}else if(lineDirection == "in"){
					line.toShape = this.parent;
					line.toId = this.parent.id;
					
					
					//修改线的属性
					var attrLineTo = getAttribute(line,"to");
					attrLineTo.getValueType = "ref";
					attrLineTo.refObject = line.toShape;
					attrLineTo.value = "%name%";					
					//alterAttribute(line,"to",line.toShape.text);
					
					
					alterAttribute(line,"action#name","进入" + line.toShape.text + "转向" + gEAPPWorkflow.createGUID());
					
				}
				
				// 添加时有建立对象，删除连线时，也要删除对象
				//更好的做法，是将每个对象里面自己管理自己的规则
				//把公共的规则放在一起
				//如果当前添加条件对象的出线，则动态添加条件结点的属性,并且已连接到目标图元
				
				var decisionShape = null; 
				if (((this.parent instanceof EAPP.Workflow.Decision) || (this.parent instanceof EAPP.Workflow.Fork)) 
					&& (lineDirection == "out")
					//&& (line.toShape && line.toId)
					) {
					decisionShape = this.parent;
				}
				
				/** //验证在建立正确关联时，才添加“执行条件”
				 else if ( (lineDirection == "in") && (line.fromShape && line.fromId) 
					&& ((line.fromShape instanceof EAPP.Workflow.Decision) || (line.fromShape instanceof EAPP.Workflow.Fork)) ) {
					decisionShape = line.fromShape;

				}
				*/
				
				if(decisionShape){
		
					var attr_condition = new EAPP.Workflow.Graphic.Attribute.Condition();
					attr_condition.isDynamicCreate = true;//设置为动态生成
					setAttributeDynamicCreate(attr_condition);
					
					attr_condition.attributeName += "*" + line.id;//加上lineId以便删除时，进行辨认 以*分隔
					
					attr_condition.displayNameType = "ref";
					attr_condition.displayName = "\"%name%\"执行条件";
					attr_condition.displayNameRefObj = line;
					attr_condition.headerType ="getDisplayName";
					
					var attr_ref_transition =  getAttribute(attr_condition,"ref-transition");
					attr_ref_transition.getValueType = "ref";
					attr_ref_transition.value = "%name%";
					attr_ref_transition.refObject = line;
					
					decisionShape.attributeArray.push(attr_condition);//添加到属性数组
					
					//表达式(虚拟属性)
					var attr_condition_expression = new EAPP.Workflow.Graphic.Attribute.NormalAttribute();
					attr_condition_expression.isDynamicCreate = true;//设置为动态生成
					attr_condition_expression.attributeName = "expression";
					attr_condition_expression.displayName = "表达式";
					attr_condition_expression.mapType = "none";
					attr_condition_expression.inputType = "html";
					attr_condition_expression.dataType ="cdata";
					attr_condition_expression.maxLength = 2000;
		
					var attr_condition_expression_id = decisionShape.id + "#" +  decisionShape.typeName + "#" + attr_condition.attributeName + "#expression";
					var attr_condition_expression_html = getExpressionTD(attr_condition_expression,attr_condition_expression_id);
			
					attr_condition_expression.inputString = attr_condition_expression_html;
					
					attr_condition.attributeArray.push(attr_condition_expression);
					
					//引用到虚拟属性
					attr_condition.inputType = "none";
					attr_condition.getValueType = "direct-ref";
					attr_condition.refObject = attr_condition_expression;
					attr_condition.isVisible = false;	
				}
				
				
				////trace("excute finish ... line.fromShape " + line.fromShape + " line.fromId " + line.fromId + " line.toShape " + line.toShape + " line.toId "+ line.toId);
				////trace("添加连线" + lineDirection + " " + line.text);
				
				this.parent.validConnect();
				
				return true;
				
			/***	
			}catch(e){
				//trace("添加线关联失败!图形名称:" + this.parent.text + "  在结点" + pointIndex + " 所关联的"+ lineDirection +"线:" + line.text + " \nError:" + e);
				return false;
			}*/
			
		};//end addLine
		
		/**
		 * 验证线名称的唯一性
		 * @param {Object} direction
		 * @param {Object} lineId
		 * @param {Object} lineName
		 */
		this.validUniqueLineName =function(direction,lineId,lineName){
			for(var i=0,len =this.relationArray.length ; i<len; i++){
				var rel = this.relationArray[i];

				if(!rel.line){continue;}//如果当前线不存在跳过
				if(rel.lineDirection == direction){
					if((getAttributeValue(rel.line,"name") == lineName) &&(lineId != rel.line.id)){
						//alert(this.parent.text + " lineId:" + lineId  + " rel.line.id:"+rel.line.id + " " + lineName + " " + getAttributeValue(rel.line,"name"));
						return false;
					} 					
				}
			}
			
			return true;
		};
		
		
		/** 
		 *  删除图元的所有关联
		 * @return {Boolean} 删除所有关联是否成功!
		 */
		this.removeAllLine = function(){
			//try{
				//父图元丢失或关联数组无效
				if(!this.parent || !this.relationArray ){return false;}
				if(!this.relationArray.length){return true};
				
				//遍历删除所有关联
				for(var i=0; i<this.relationArray.length; i++){
					var rel = this.relationArray[i];
					//trace("deleteing ..........." + rel.line.text); 
					if(!rel.line){continue;}//如果当前线不存在跳过
					if(rel.lineDirection == "out"){
						rel.line.fromShape = null;
						rel.line.fromId = null;
						
					}else if(rel.lineDirection == "in"){
						rel.line.toShape = null;
						rel.line.toId = null;
					}
					//验证未连接的线
					rel.line.validConnect();
					
					/**
					//从工作区删除当前线
					var lineObj = rel.line.domElement;
					gEAPPWorkflow.deleteChild(true,lineObj);//采用静默删除的方式
					**/
					
					// 是否同时从界面上删除线
				}
				
				//移除所有元素
				this.relationArray.splice(0,this.relationArray.length);
				
				//如果当前的图元是“条件”
				if(this.parent instanceof EAPP.Workflow.Decision){
					//
				}
				
				//重置关联数组
				//this.relationArray = new Array();	
				
				this.parent.validConnect();
				
				return true;
			/***
			}catch(e){
				//trace("删除图元的所有关联失败!图形名称:" + this.parent.text  + " \nError:" + e);
				return false;
			}	
			*/	
		};
		
		/** 
		 *  删除指定Id的关联线
		 * @param {String} lineId 关联的连接线Id
		 * @param {String}(Optional) lineDirection 连接线的方向 p8 属于前端 p9属于后端(箭头端)
		 * @type Boolean
		 * @return 删除所有关联是否成功!
		 */
		this.removeLineByLineId = function(lineId,lineDirection){
			//try{
				//检查传入的参数
				if(!lineId ){return false;}
				//如果当前有传入方向
			   	if(lineDirection && ((lineDirection=="p8") || (lineDirection=="p9"))){
					lineDirection = (lineDirection == "p8") ? "out":"in";
				}
				//验证箭头方向参数
				if(lineDirection && lineDirection!= "out" && lineDirection != "in" ){return false;}
				
				//获取当前要删除关联的线
				var line = gEAPPWorkflow.getNodeById(lineId);
				if(!line){return false;}//不存在则返回false
				
				
				//父图元丢失或关联数组无效
				if(!this.parent || !this.relationArray || !this.relationArray.length){return false;}
				
				//遍历删除所有关联
				for(var i=0; i<this.relationArray.length; i++){
					var rel = this.relationArray[i];
					if(!rel.line){continue;}//如果当前线不存在跳过
					
					if(lineId == rel.line.id ){
						if(!lineDirection || (lineDirection && lineDirection == rel.lineDirection)){
								
							//删除线本身的存储属性
							if(rel.lineDirection == "out"){
								rel.line.fromShape = null;
								rel.line.fromId = null;
							}else if(rel.lineDirection == "in"){
								rel.line.toShape = null;
								rel.line.toId = null;
							}
							
							//删除当前元素
							this.relationArray.splice(i,1);
							
							//如果当前的图元是“条件”
							if(this.parent instanceof EAPP.Workflow.Decision || this.parent instanceof EAPP.Workflow.Fork){
								var conditionAttributeName = "condition*" + rel.line.id; 
								deleteAttributeByName(this.parent,conditionAttributeName);
								////trace(conditionAttributeName);
							}
							
							
							this.parent.validConnect();
							return true;
							break;
						}
					}//end equal id 

				}//end for
				
				return false;
			/***
			}catch(e){
				//trace("删除指定Id的关联线失败!图形名称:" + this.parent.text + "   所"+ lineDirection +"关联的线:" + line.text + " \nError:" + e);
				return false;
			}	
			**/	
		};
	};


	//-------------------------------开始状态(start-state)----------------------------------------------
	/**
	 * 创建一个开始状态结点
	 * @class 开始状态
	 * @type EAPP.Workflow.StartState
	 * @constructor
	 */
	EAPP.Workflow.StartState = function(saveId){
	
		//调用基类构造
		//EAPP.Workflow.BaseNode.call();
		
		/**
		 * 类型名称
		 * @type String
		 */
		this.typeName = "EAPP.Workflow.StartState";
		
		/**
		 * 结点类型
		 * 1.shape 图元类型
		 * 2.line 连接线
		 * 3.flag 小标
		 * @type String
		 */
		this.nodeType = "shape";
		
		/**
		 * 图片路径
		 * @type String
		 */
		this.imgSrc =  gEAPPWorkflow.basePath + "images/mStar01.gif";
		
		/**
		 * 内嵌图标路径
		 * @type String
		 */
		this.innerIconSrc =  gEAPPWorkflow.basePath + "icons/start_state_enabled.gif";

		//系统生成的id
		this.generateId = gEAPPWorkflow.createGUID();
		
		/**
		 * 对象的Id
		 * @type (jQuery)Element
		 */	
		this.id = saveId?saveId:'start-state_' + this.generateId;
		/**
		 * 对象的元素
		 * @type (jQuery)Element
		 */	
		this.element = null;
		/**
		 * DOM元素
		 * @type DOMElement
		 */	
		this.domElement = null;
		
		/**
		 * 文本
		 * @type String
		 */
		this.text = "开始";
		/**
		 * 描述
		 * @type String
		 */		
		this.description = "";
		
		//储存关联连接线的对象
		this.innerLine = new EAPP.Workflow.InnerLines(this);
		
		/**
		 * 属性数组
		 * @type Array
		 */	
		this.attributeArray = new Array();
		
		//预注册属性--------------------------------------------------
		
		//名称属性
		var attr_name = new EAPP.Workflow.Graphic.Attribute.Name();
		attr_name.displayName = '节点名称';
		attr_name.value = this.text;
		attr_name.inputType = "readonly";
		
		//说明属性
		var attr_description = new EAPP.Workflow.Graphic.Attribute.Description();
		attr_description.displayName ="节点描述";
		
		//添加到当前属性数组
		this.attributeArray.push(attr_name);
		this.attributeArray.push(attr_description);
		
		
		/** 
		 * 绘制
		 * @return Boolean 返回绘制是否成功
		 */
		this.render = function(){
			try {
				//创建元素
				this.domElement = document.createElement("v:oval");//椭圆形
				this.element = $(this.domElement);
				
				this.element.attr({//属性
					id: this.id,
					name: 'arc',//name属性暂时为图形的形状 
					className: "drag",
					//显示属性
					strokecolor: '#9dadc7',
					strokeweight: '1px'
					
				}).css({//样式		
					position: "absolute",//绝对定位
					"zIndex": "1",
					left: "0px",//左偏移
					top: "0px",//上偏移
					//默认大小
					width: "80px",
					height: "40px"
				});
				
				// this.element 没有附加事件响应
				//事件响应包括
				//1.单击出现属性
				//2.双击快速编辑名称
				//3.响应调整大小 onResize=\"resizePointForShape(this);
				//4.失去焦点时，内部文本框不可编辑 Onblur='this.contentEditable = false;'
				//事件如果无法采用jQuery方式附加，则采用原始DOM的方式
				
				getShapeInnerHTML(this);
				
				//为元素附加事件
				this.attachEvent();	
				
				return true;
			} 
			catch (e) {
				//trace(this.typeName + " render 绘制失败！" + e);
				return false;
			}
		};
		
		/**
		 * 为图元附加事件响应
		 * @type Boolean
		 * @return  附加事件是否成功
		 */
		this.attachEvent = function(){
			//获取当前元素jQuery对象
			//添加时单击触发click事件
			var shape = this;
			this.element.click(function(){
				//显示属性面板 
				gEAPPWorkflow.showPropertyPanel(shape);
				//shape.validConnect();
			}).dblclick(function(){
				if(attr_name.inputType == "readonly"){return;}
				//快速编辑 
				gEAPPWorkflow.ContextMenuItemClick("edit");
			});
			
			return true;
		};
		
		/**
		 * 验证线是否连接正常
		 */
		this.validConnect = function(){
			//return;// 验证事件不正常
			//如果连接不正确
			if(this.innerLine.relationArray.length <1){
				this.element.attr({strokecolor:'#FF0000'});//线变红色
				return false;
			}else{
				this.element.attr({strokecolor:'#9dadc7'});//恢复默认颜色
				return true;
			}
		};
		
		/** 
		 *  获取图元的DOM元素对象
		 * @return {Element} 返回当前对象的DOM元素对象
		 */
		this.getDomElement = function(){
			return document.getElementById(this.id);
		};
		
		/** 
		 *  获取图元元素的jQuery对象
		 * @return {Object}(jQuery) 返回当前对象元素的jQuery对象
		 */
		this.getElement = function(){
			this.domElement = document.getElementById(this.id);
			this.element = $(this.domElement);
			return this.element;
		};
		
		/** 
		 *  保存为xml数据
		 * @return {String} 返回当前对象xml序列化字符串
		 */
		this.saveAsXML = function(){
		
		};
		
		/** 
		 *  解析xml数据为对象
		 * @param {String} xmlData xml序列化字符串
		 * @return {Object} 返回当前对象实例本身
		 */
		this.parseXML = function(xmlData){
			//parseing...
			return this;
		};
		
		/***
		 //获取工作流XML
		 this.getNodeXMLData = function() {
		 this.element = _$(this.id);
		 this.text = $('div>div',this.element).text();
		 var node_begin = "\n\t<start-node id=\""+ this.id +"\" name=\""+ this.text +"\">\n";
		 var content = getPointsXMLData(this.points);
		 var node_end = "\t</start-node>\n";
		 var curXMl = node_begin + content + node_end;
		 return curXMl;
		 };
		 
		 this.getSaveData = function(){
		 this.element = _$(this.id);
		 this.name = $('div>div',this.element).text();
		 var node_begin = "\n\t<start-node id=\"\" name=\""+ this.name +"\">\n";
		 node_begin += "\t\t<description><![CDATA[" + this.description + "]]></description>" ;
		 var content = getPointsSaveData(this.points);
		 var node_end = "\t</start-node>\n";
		 var curXMl = node_begin + content + node_end;
		 return curXMl;
		 };
		 **/
	};
	//--------------------------------------------------------------------------------------------------------
	
	
	//-------------------------------结束状态(end-state)----------------------------------------------
	/**
	 * 结束状态
	 * @class
	 * @constructor
	 */
	EAPP.Workflow.EndState = function(saveId){
	
		//调用基类构造
		//EAPP.Workflow.BaseNode.call();
		
		//类型名称
		this.typeName = "EAPP.Workflow.EndState";
		//结点类型
		this.nodeType = "shape";
		//图片路径
		this.imgSrc =  gEAPPWorkflow.basePath + "images/mEnd01.gif";
		//内嵌图标路径
		this.innerIconSrc =  gEAPPWorkflow.basePath + "icons/end_state_enabled.gif";
		
		//系统生成的id
		this.generateId = gEAPPWorkflow.createGUID();
		//对象的ID
		this.id = saveId?saveId: 'end-state_' + this.generateId;
		//对象的元素
		this.element = null;
		//DOM元素
		this.domElement = null;
		
		//文本
		this.text = "结束";
		//描述
		this.description = "";
		
		//储存关联连接线的对象
		this.innerLine = new EAPP.Workflow.InnerLines(this);
		
		/**
		 * 属性数组
		 * @type Array
		 */	
		this.attributeArray = new Array();
		
		//预注册属性--------------------------------------------------
		
		//名称属性
		var attr_name = new EAPP.Workflow.Graphic.Attribute.Name();
		attr_name.displayName = '节点名称';
		attr_name.value = this.text;
		attr_name.inputType = "readonly";
		
		//说明属性
		var attr_description = new EAPP.Workflow.Graphic.Attribute.Description();
		attr_description.displayName ="节点描述";
		
		//添加到当前属性数组
		this.attributeArray.push(attr_name);
		this.attributeArray.push(attr_description);
		
		/** 
		 * 绘制
		 * @type Boolean
		 * @return 返回绘制是否成功
		 */
		this.render = function(){
			try {
				//创建元素
				this.domElement = document.createElement("v:oval");//椭圆形
				this.element = $(this.domElement);
				
				this.element.attr({//属性
					id: this.id,
					name: 'arc',//name属性暂时为图形的形状 //
					className: "drag",
					//显示属性
					strokecolor: '#9dadc7',
					strokeweight: '1px'
				
				}).css({//样式				
					position: "absolute",//绝对定位
					"zIndex": "1",
					left: "0px",//左偏移
					top: "0px",//上偏移
					//默认大小
					width: "80px",
					height: "40px"
				});
				
				// this.element 没有附加事件响应
				//事件响应包括
				//1.单击出现属性
				//2.双击快速编辑名称
				//3.响应调整大小 onResize=\"resizePointForShape(this);
				//4.失去焦点时，内部文本框不可编辑 Onblur='this.contentEditable = false;'
				//事件如果无法采用jQuery方式附加，则采用原始DOM的方式
				
				getShapeInnerHTML(this);
				
				//添加时单击触发click事件
				var shape = this;
				this.element.click(function(){
					//显示属性面板 
					gEAPPWorkflow.showPropertyPanel(shape);
					//shape.validConnect();
				}).dblclick(function(){
					//快速编辑 
					if(attr_name.inputType == "readonly"){return;}
					gEAPPWorkflow.ContextMenuItemClick("edit");
				});
				
				return true;
			} 
			catch (e) {
				//trace(this.typeName + " render 绘制失败！" + e);
				return false;
			}
		};
		
		
		/**
		 * 验证线是否连接正常
		 */
		this.validConnect = function(){
			//return;// 验证事件不正常
			//如果连接不正确
			if(this.innerLine.relationArray.length <1){
				this.element.attr({strokecolor:'#FF0000'});//线变红色
				return false;
			}else{
				this.element.attr({strokecolor:'#9dadc7'});//恢复默认颜色
				return true;
			}
		};
		
		/** 
		 *  获取图元的DOM元素对象
		 * @return {Element} 返回当前对象的DOM元素对象
		 */
		this.getDomElement = function(){
			return document.getElementById(this.id);
		};
		
		/** 
		 *  获取图元元素的jQuery对象
		 * @return {Object}(jQuery) 返回当前对象元素的jQuery对象
		 */
		this.getElement = function(){
			this.domElement = document.getElementById(this.id);
			return $(this.domElement);
		};
		
		/** 
		 * @method
		 *  保存为xml数据
		 * @id 	  saveAsXML
		 * @alias saveAsXML
		 * @return {String} 返回当前对象xml序列化字符串
		 */
		this.saveAsXML = function(){
		
		};
		
		/** 
		 *  解析xml数据为对象
		 * @param {String} xmlData xml序列化字符串
		 * @return {Object} 返回当前对象实例本身
		 */
		this.parseXML = function(xmlData){
			//parseing...
			return this;
		};
		
		/***
		 //获取工作流XML
		 this.getNodeXMLData = function() {
		 this.element = _$(this.id);
		 this.text = $('div>div',this.element).text();
		 var node_begin = "\n\t<end-node id=\""+ this.id +"\" name=\""+ this.text +"\">\n";
		 //var content = getPointsXMLData(points);
		 var node_end = "\t</end-node>\n";
		 var curXMl = node_begin + node_end;
		 return curXMl;
		 };
		 
		 this.getSaveData = function(){
		 this.element = _$(this.id);
		 this.name = $('div>div',this.element).text();
		 var node_begin = "\n\t<end-node id=\"\" name=\""+ this.name +"\">\n";
		 node_begin += "\t\t<description><![CDATA[" + this.description + "]]></description>" ;
		 //var content = getPointsSaveData(this.points);
		 var node_end = "\n\t</end-node>\n";
		 var curXMl = node_begin  + node_end;
		 return curXMl;
		 };
		 */
	};
	//--------------------------------------------------------------------------------------------------------
	
	
	//-------------------------------任务节点(task-node)----------------------------------------------
	/**
	 * 任务节点
	 * @class
	 * @constructor
	 */
	EAPP.Workflow.TaskNode = function(saveId){
	
		//调用基类构造
		//EAPP.Workflow.BaseNode.call();
		
		//类型名称
		this.typeName = "EAPP.Workflow.TaskNode";
		//结点类型
		this.nodeType = "shape";
		//图片路径
		this.imgSrc =  gEAPPWorkflow.basePath + "images/mTaskPoint01.gif";
		//内嵌图标路径
		this.innerIconSrc =  gEAPPWorkflow.basePath + "icons/rwjd.gif";
		
		//系统生成的id
		this.generateId = gEAPPWorkflow.createGUID();
		//对象的ID
		this.id = saveId?saveId: 'task-node_' + this.generateId;
		//对象的元素
		this.element = null;
		//DOM元素
		this.domElement = null;
		
		//文本
		this.text = "任务结点" + this.generateId;
		//描述
		this.description = "" ;
		
		//储存关联连接线的对象
		this.innerLine = new EAPP.Workflow.InnerLines(this);
		
		
		/**
		 * 属性数组
		 * @type Array
		 */	
		this.attributeArray = new Array();
		
		//预注册属性--------------------------------------------------
		
		//名称属性
		var attr_name = new EAPP.Workflow.Graphic.Attribute.Name();
		attr_name.displayName = '节点名称';
		attr_name.value = this.text;
		
		//说明属性
		var attr_description = new EAPP.Workflow.Graphic.Attribute.Description();
		attr_description.displayName ="节点描述";
		
		//异步属性
		//禁用
		var attr_is_async = new EAPP.Workflow.Graphic.Attribute.NormalAttribute();
		attr_is_async.attributeName = 'is-async';
		attr_is_async.displayName = '是否异步';
		attr_is_async.mapName = 'is-async';
		attr_is_async.mapType = 'attribute';
		attr_is_async.isNull = true;//可为空
		attr_is_async.isEnable = false;
		attr_is_async.isVisible = true;
		attr_is_async.defaultValue = "false";//默认值
		attr_is_async.validType = 'none';
		attr_is_async.inputType = 'input_checkbox';
		attr_is_async.dataType = 'boolean';
		
		//信号属性
		//隐藏默认为3
		var attr_signal = new EAPP.Workflow.Graphic.Attribute.NormalAttribute();
		attr_signal.attributeName = 'signal';
		attr_signal.displayName = '信号';
		attr_signal.mapName = 'signal';
		attr_signal.mapType = 'attribute';
		attr_signal.isVisible = false;
		attr_signal.value = "3";//默认值
		attr_signal.validType = 'none';
		attr_signal.inputType = 'input_text';
		attr_signal.dataType = 'integer';
		
		
		//task属性
		var attr_task = new EAPP.Workflow.Graphic.Attribute.Task();
		attr_task.isHideHeader = true;//隐藏标题行
		
		/**
		 * 名称属性
		 * 目前Task-Node只支持一个Task
		 * 命名规则：task-node名称 + 全局ID
		 * 隐藏 
		 */
		var attr_task_name = getAttribute(attr_task,"name");
		attr_task_name.isVisible = false;
		attr_task_name.getValueType = "ref";
		var newId = gEAPPWorkflow.createGUID();
		attr_task_name.refObject = this;
		attr_task_name.value = "%name%" + newId;
		
		/**
		 * 任务执行顺序
	 	 * 禁用
	 	 */
		var	attr_excute_order = getAttribute(attr_task,"execute-order");
		attr_excute_order.isEnable = false;
		
		/**
		 * 到期时间
		 * [yyyy-MM-dd | yyyy-MM-dd HH:mm | yyyy-MM-dd HH:mm:ss]
		 * 禁用
		 */
		var	attr_due_date = getAttribute(attr_task,"due-date");
		attr_due_date.isEnable = false;
		
		/**
		 * 任务优先级
		 * [100 | 200 | 300 | 400 | 500]
		 * 禁用
		 */
	    var	attr_priority = getAttribute(attr_task,"priority");
		attr_priority.isEnable = false;
		
		//任务说明属性禁用
		var attr_task_description = getAttribute(attr_task,"description");
		attr_task_description.isEnable = false;
		
		
		//-----------------------------------任务绑定表单-----------------------------------
		//任务绑定表单
		var attr_view_binding = getAttribute(attr_task,"view-binding");
		attr_view_binding.displayName = "任务绑定表单";
		
		//绑定表达式(虚拟属性)
		var attr_view_binding_expression = new EAPP.Workflow.Graphic.Attribute.NormalAttribute();
		attr_view_binding_expression.attributeName = "expression";
		attr_view_binding_expression.displayName = "表达式";
		attr_view_binding_expression.mapType = "none";
		attr_view_binding_expression.inputType = "html";
		attr_view_binding_expression.maxLength = 2000;
		
		var view_binding_expression_id = this.id + "#" + this.typeName + "#task#view-binding#expression";
		var view_binding_expression_html = getExpressionTD(attr_view_binding_expression,view_binding_expression_id);
		attr_view_binding_expression.inputString = view_binding_expression_html;
		
		attr_view_binding.attributeArray.push(attr_view_binding_expression);
		
		//引用到虚拟属性
		attr_view_binding.inputType = "none";
		attr_view_binding.getValueType = "direct-ref";
		attr_view_binding.refObject = attr_view_binding_expression;
		attr_view_binding.isVisible = false;
		
		
		
		//-----------------------------------操作者-----------------------------------
		
		var attr_assignment = getAttribute(attr_task,"assignment");
		alterAttributeProperty(attr_task,"assignment",'displayName',"操作者");
		alterAttributeProperty(attr_task,"assignment#class",'displayName',"类");
		
		 //--------------------------------单用户---------------------------------------
		 var attr_operator_single_user = new EAPP.Workflow.Graphic.Attribute.NormalAttribute();
		attr_operator_single_user.attributeName = "operator_single_user";
		attr_operator_single_user.displayName = "用户名";
		attr_operator_single_user.mapType = "none";
		attr_operator_single_user.inputType = 'input_text';
		attr_operator_single_user.maxLength = 200;
		
		attr_assignment.attributeArray.push(attr_operator_single_user);

	 
		//操作者集合属性(虚拟属性)
		//-----------------------------------用户
		var attr_operator_user = new EAPP.Workflow.Graphic.Attribute.NormalAttribute();
		attr_operator_user.attributeName = "operator-user";
		attr_operator_user.displayName = "多用户";
		attr_operator_user.mapType = "none";
		attr_operator_user.inputType = "html";
		attr_operator_user.maxLength = 2000;
		
		var operator_user_id = this.id + "#" + this.typeName + "#task#assignment#operator-user";
		var operator_user_html = getTextareaTD(attr_operator_user,operator_user_id);
		operator_user_html += "<button id=\"btn_operator_user\"";
		operator_user_html += "onclick=\"gEAPPWorkflow.choosePerson('"+ operator_user_id + "');\"";//弹出选择人员的对话框
		operator_user_html += ">选择</button>";
			
		attr_operator_user.inputString = operator_user_html;
		attr_assignment.attributeArray.push(attr_operator_user);
			
		//-----------------------------------角色	
		var attr_operator_role = new EAPP.Workflow.Graphic.Attribute.NormalAttribute();
		attr_operator_role.attributeName = "operator-role";
		attr_operator_role.displayName = "角色";
		attr_operator_role.mapType = "none";
		attr_operator_role.inputType = "html";
		attr_operator_role.maxLength = 2000;
		
		var operator_role_id = this.id + "#" + this.typeName + "#task#assignment#operator-role";
		var operator_role_html = getTextareaTD(attr_operator_role,operator_role_id);
		
		operator_role_html += "<button id=\"btn_operator_role\"";
		operator_role_html +=  "onclick=\"gEAPPWorkflow.chooseRole('"+ operator_role_id + "');\"";//弹出选择角色的对话框
		operator_role_html += ">选择</button>";
			
			
		attr_operator_role.inputString = operator_role_html;
		attr_assignment.attributeArray.push(attr_operator_role);
		
		 //--------------------------------表达式---------------------------------------
		 var attr_operator_expression = new EAPP.Workflow.Graphic.Attribute.NormalAttribute();
		attr_operator_expression.attributeName = "operator_expression";
		attr_operator_expression.displayName = "表达式";
		attr_operator_expression.mapType = "none";
		attr_operator_expression.inputType = 'html';
		attr_operator_expression.maxLength = 2000;
		
		var attr_operator_expression_id = this.id + "#" + this.typeName + "#task#assignment#operator_expression";
		var attr_operator_expression_html = getExpressionTD(attr_operator_expression,attr_operator_expression_id);
		attr_operator_expression.inputString = attr_operator_expression_html;
		
		attr_assignment.attributeArray.push(attr_operator_expression);
		
		
		//引用到虚拟属性
		attr_assignment.inputType = "none";
		attr_assignment.getValueType = "direct-ref";
		attr_assignment.refObject = attr_operator_role;//如何绑定bug
		attr_assignment.isVisible = false;
		 
		
		//-----------------------------------任务创建事件-----------------------------------
		var task_create_action = getAttribute(attr_task,"event_task_create#action");
		task_create_action.isHideHeader = true;//隐藏标题行
		//指定名称
		var task_create_action_name = getAttribute(attr_task,"event_task_create#action#name");
		newId = gEAPPWorkflow.createGUID();
		task_create_action_name.getValueType = "ref";
		task_create_action_name.refObject = this;
		task_create_action_name.value = "%name%任务创建动作" + newId;
		task_create_action_name.isVisible = false;
		
		
		//-----------------------------------任务分派事件-----------------------------------
		var task_assign_action = getAttribute(attr_task,"event_task_assign#action");
		task_assign_action.isHideHeader = true;//隐藏标题行
		//指定事件action名称
		var task_assign_action_name = getAttribute(attr_task,"event_task_assign#action#name");
		newId = gEAPPWorkflow.createGUID();
		task_assign_action_name.getValueType = "ref";
		task_assign_action_name.refObject = this;
		task_assign_action_name.value = "%name%任务分派动作" + newId;
		task_assign_action_name.isVisible = false;
		
		
		//-----------------------------------任务开始（启用）事件-----------------------------------
		var task_start_action = getAttribute(attr_task,"event_task_start#action");
		task_start_action.isHideHeader = true;//隐藏标题行
		//指定事件action名称
		var task_start_action_name = getAttribute(attr_task,"event_task_start#action#name");
		newId = gEAPPWorkflow.createGUID();
		task_start_action_name.getValueType = "ref";
		task_start_action_name.refObject = this;
		task_start_action_name.value = "%name%任务开始（启用）动作" + newId;
		task_start_action_name.isVisible = false;
		
		//-----------------------------------任务取消事件-----------------------------------
		var task_giveup_action = getAttribute(attr_task,"event_task_giveup#action");
		task_giveup_action.isHideHeader = true;//隐藏标题行
		//指定事件action名称
		var task_giveup_action_name = getAttribute(attr_task,"event_task_giveup#action#name");
		newId = gEAPPWorkflow.createGUID();
		task_giveup_action_name.getValueType = "ref";
		task_giveup_action_name.refObject = this;
		task_giveup_action_name.value = "%name%任务取消动作" + newId;
		task_giveup_action_name.isVisible = false;
		
		//-----------------------------------任务结束事件-----------------------------------
		var task_end_action = getAttribute(attr_task,"event_task_end#action");
		task_end_action.isHideHeader = true;//隐藏标题行
		//指定事件action名称
		var task_end_action_name = getAttribute(attr_task,"event_task_end#action#name");
		newId = gEAPPWorkflow.createGUID();
		task_end_action_name.getValueType = "ref";
		task_end_action_name.refObject = this;
		task_end_action_name.value = "%name%任务结束动作" + newId;
		task_end_action_name.isVisible = false;
		
		//-----------------------------------任务通知事件-----------------------------------
		var task_notify_action = getAttribute(attr_task,"event_task_notify#action");
		task_notify_action.isHideHeader = true;//隐藏标题行
		//指定事件action名称
		var task_notify_action_name = getAttribute(attr_task,"event_task_notify#action#name");
		newId = gEAPPWorkflow.createGUID();
		task_notify_action_name.getValueType = "ref";
		task_notify_action_name.refObject = this;
		task_notify_action_name.value = "%name%任务结束动作" + newId;
		task_notify_action_name.isVisible = false;
		
		
		//添加到当前属性数组
		this.attributeArray.push(attr_name);
		this.attributeArray.push(attr_is_async);
		this.attributeArray.push(attr_signal);
		this.attributeArray.push(attr_task);
		this.attributeArray.push(attr_description);
		
		/** 
		 * 绘制
		 * @return {Boolean} 返回绘制是否成功
		 */
		this.render = function(){
			try {
				//创建元素
				this.domElement = document.createElement("v:roundrect");//圆角矩形
				this.element = $(this.domElement);
				
				this.element.attr({//属性
					id: this.id,
					name: 'roundrect',//name属性暂时为图形的形状 //
					className: "drag",
					//显示属性
					strokecolor: '#9dadc7',
					strokeweight: '1px'
				
				}).css({//样式				
					position: "absolute",//绝对定位
					"zIndex": "1",
					left: "0px",//左偏移
					top: "0px",//上偏移
					//默认大小
					width: "140px",
					height: "40px"
				});
				
				getShapeInnerHTML(this);
				// this.element 没有附加事件响应
				//事件响应包括
				//1.单击出现属性
				//2.双击快速编辑名称
				//3.响应调整大小 onResize=\"resizePointForShape(this);
				//4.失去焦点时，内部文本框不可编辑 Onblur='this.contentEditable = false;'
				//事件如果无法采用jQuery方式附加，则采用原始DOM的方式
				
				 
				var shape = this;
				this.element.click(function(){
					//显示属性面板 
					gEAPPWorkflow.showPropertyPanel(shape);
					//shape.validConnect();
				}).dblclick(function(){
					//快速编辑 
					gEAPPWorkflow.ContextMenuItemClick("edit");
				});
			 
				
				
				return true;
			} 
			catch (e) {
				//trace(this.typeName + " render 绘制失败！" + e);
				return false;
			}
		};
		
		/**
		 * 验证线是否连接正常
		 */
		this.validConnect = function(){
			//return;// 验证事件不正常
			//如果连接不正确
			if(this.innerLine.relationArray.length <2){
				this.element.attr({strokecolor:'#FF0000'});//线变红色
				return false;
			}else{
				this.element.attr({strokecolor:'#9dadc7'});//恢复默认颜色
				return true;
			}
		};
		
		/** 
		 *  获取图元的DOM元素对象
		 * @return {Element} 返回当前对象的DOM元素对象
		 */
		this.getDomElement = function(){
			return document.getElementById(this.id);
		};
		
		/** 
		 *  获取图元元素的jQuery对象
		 * @return {Object}(jQuery) 返回当前对象元素的jQuery对象
		 */
		this.getElement = function(){
			this.domElement = document.getElementById(this.id);
			return $(this.domElement);
		};
		
		/** 
		 *  保存为xml数据
		 * @return {String} 返回当前对象xml序列化字符串
		 */
		this.saveAsXML = function(){
		
		};
		
		/** 
		 *  解析xml数据为对象
		 * @param {String} xmlData xml序列化字符串
		 * @return {Object} 返回当前对象实例本身
		 */
		this.parseXML = function(xmlData){
			//parseing...
			return this;
		};
		
		/***
		 //获取工作流XML
		 this.getNodeXMLData = function() {
		 this.element = _$(this.id);
		 this.text = $('div>div',this.element).text();
		 var node_begin = "\n\t<task-node id=\""+ this.id +"\" name=\""+ this.text +"\">\n";
		 var content = getPointsXMLData(this.points);
		 var node_end = "\t</task-node>\n";
		 var curXMl = node_begin + content + node_end;
		 return curXMl;
		 };
		 
		 this.getSaveData = function(){
		 this.element = _$(this.id);
		 this.name = $('div>div',this.element).text();
		 var node_begin = "\n\t<task-node id=\"\" name=\""+ this.name +"\">\n";
		 node_begin += "\t\t<description><![CDATA[" + this.description + "]]></description>" ;
		 var content = getPointsSaveData(this.points);
		 var node_end = "\t</task-node>\n";
		 var curXMl = node_begin + content + node_end;
		 return curXMl;
		 };
		 */
	};
	//--------------------------------------------------------------------------------------------------------
	
	
	//-------------------------------机器节点(node)----------------------------------------------
	/**
	 * 机器节点
	 * @class
	 * @constructor
	 */
	EAPP.Workflow.Node = function(saveId){
	
		//调用基类构造
		//EAPP.Workflow.BaseNode.call();
		
		//类型名称
		this.typeName = "EAPP.Workflow.Node";
		//结点类型
		this.nodeType = "shape";
		//图片路径
		this.imgSrc =  gEAPPWorkflow.basePath + "images/mComPoint01.gif";
		//内嵌图标路径
		this.innerIconSrc =  gEAPPWorkflow.basePath + "icons/jqjd.gif";
		
		//系统生成的id
		this.generateId = gEAPPWorkflow.createGUID();
		//对象的ID
		this.id = saveId?saveId: 'node_' + this.generateId;
		//对象的元素
		this.element = null;
		//DOM元素
		this.domElement = null;
		
		//文本
		this.text = "机器节点" + this.generateId;
		//描述
		this.description = "";
		
		//储存关联连接线的对象
		this.innerLine = new EAPP.Workflow.InnerLines(this);
		
		/**
		 * 子属性数组
		 * @type Array
		 */
		this.attributeArray = new Array();
		
		//名称属性
		var attr_name = new EAPP.Workflow.Graphic.Attribute.Name();
		attr_name.displayName = '节点名称';
		attr_name.value = this.text;
		
		//说明属性
		var attr_description = new EAPP.Workflow.Graphic.Attribute.Description();
		attr_description.displayName ="节点描述";
		
		
		/**
		 * 是否同步
		 * [true | false]
		 * （未实现）
		 * 禁用
		 */
		var attr_is_async = new EAPP.Workflow.Graphic.Attribute.NormalAttribute();
		attr_is_async.attributeName = 'is-async';
		attr_is_async.displayName = '是否异步';
		attr_is_async.mapName = 'is-async';
		attr_is_async.mapType = 'attribute';
		attr_is_async.isEnable = false;
		attr_is_async.inputType = 'input_checkbox';
		attr_is_async.dataType = 'boolean';
		
		
		
		//进入节点的事件
		var attr_event_node_enter = new EAPP.Workflow.Graphic.Attribute.Event();
		attr_event_node_enter.attributeName = "event_node_enter";
		attr_event_node_enter.displayName = "开始事件";
		alterAttribute(attr_event_node_enter,'type','NODE_ENTER');
		hideAttribute(attr_event_node_enter,'type');

		var attr_event_node_enter_action = getAttribute(attr_event_node_enter,"action");
		attr_event_node_enter_action.isHideHeader = true;//隐藏标题行
		//设置name属性
		var attr_event_node_enter_action_name = getAttribute(attr_event_node_enter_action,"name");
		var newId = gEAPPWorkflow.createGUID();
		attr_event_node_enter_action_name.getValueType = "ref";
		attr_event_node_enter_action_name.refObject = this;
		attr_event_node_enter_action_name.value = "%name%进入节点动作" + newId;//防止有多个action的情况发生名称冲突
		attr_event_node_enter_action_name.isVisible = false; // 不设置隐藏，自动隐藏的bug
		
		
		//离开节点事件
		var attr_event_node_leave = new EAPP.Workflow.Graphic.Attribute.Event();
		attr_event_node_leave.attributeName = "event_node_leave";
		attr_event_node_leave.displayName = "结束事件";
		alterAttribute(attr_event_node_leave,'type','NODE_LEAVE');
		hideAttribute(attr_event_node_leave,'type');
		
		var attr_event_node_leave_action = getAttribute(attr_event_node_leave,"action");
		attr_event_node_leave_action.isHideHeader = true;//隐藏标题行
		//设置name属性
		attr_event_node_leave_action_name = getAttribute(attr_event_node_leave_action,"name");
		var newId = gEAPPWorkflow.createGUID();
		attr_event_node_leave_action_name.getValueType = "ref";
		attr_event_node_leave_action_name.refObject = this;
		attr_event_node_leave_action_name.value = "%name%离开节点动作" + newId;//防止有多个action的情况发生名称冲突
		attr_event_node_leave_action_name.isVisible = false;// 不设置隐藏，自动隐藏的bug
		

		//数据提交动作
		var attr_action = new EAPP.Workflow.Graphic.Attribute.Action(); 
		attr_action.displayName = "执行程序";
		alterAttribute(attr_action,'name','数据提交');//
		
		//指定名称
		var attr_action_name = getAttribute(attr_action,"name");
		newId = gEAPPWorkflow.createGUID();
		attr_action_name.getValueType = "ref";
		attr_action_name.refObject = this;
		attr_action_name.value = "%name%数据提交" + newId;
		attr_action_name.isVisible = false;
		
		//将属性添加到当前数组
		this.attributeArray.push(attr_name);
		this.attributeArray.push(attr_is_async);
		this.attributeArray.push(attr_event_node_enter);
		this.attributeArray.push(attr_event_node_leave);
		this.attributeArray.push(attr_action);
		this.attributeArray.push(attr_description);
		
		
		/** 
		 *  绘制
		 * @return {Boolean} 返回绘制是否成功
		 */
		this.render = function(){
			try {
				//创建元素
				this.domElement = document.createElement("v:roundrect");//椭圆形
				this.element = $(this.domElement);
				
				this.element.attr({//属性
					id: this.id,
					name: 'roundrect',//name属性暂时为图形的形状 //
					className: "drag",
					//显示属性
					strokecolor: '#9dadc7',
					strokeweight: '1px'
				
				}).css({//样式				
					position: "absolute",//绝对定位
					"zIndex": "1",
					left: "0px",//左偏移
					top: "0px",//上偏移
					//默认大小
					width: "140px",
					height: "40px"
				});
				
				// this.element 没有附加事件响应
				//事件响应包括
				//1.单击出现属性
				//2.双击快速编辑名称
				//3.响应调整大小 onResize=\"resizePointForShape(this);
				//4.失去焦点时，内部文本框不可编辑 Onblur='this.contentEditable = false;'
				//事件如果无法采用jQuery方式附加，则采用原始DOM的方式
				
				getShapeInnerHTML(this);
				
				//添加时单击触发click事件
				var shape = this;
				this.element.click(function(){
					//显示属性面板 
					gEAPPWorkflow.showPropertyPanel(shape);
					//shape.validConnect();
				}).dblclick(function(){
					//快速编辑 
					gEAPPWorkflow.ContextMenuItemClick("edit");
				});
				
				return true;
			} 
			catch (e) {
				//trace(this.typeName + " render 绘制失败！" + e);
				return false;
			}
		};
		
		/**
		 * 验证线是否连接正常
		 */
		this.validConnect = function(){
			//return;// 验证事件不正常
			//如果连接不正确
			if(this.innerLine.relationArray.length <2){
				this.element.attr({strokecolor:'#FF0000'});//线变红色
				return false;
			}else{
				this.element.attr({strokecolor:'#9dadc7'});//恢复默认颜色
				return true;
			}
		};
		
		/** 
		 *  获取图元的DOM元素对象
		 * @return {Element} 返回当前对象的DOM元素对象
		 */
		this.getDomElement = function(){
			return document.getElementById(this.id);
		};
		
		/** 
		 *  获取图元元素的jQuery对象
		 * @return {Object}(jQuery) 返回当前对象元素的jQuery对象
		 */
		this.getElement = function(){
			this.domElement = document.getElementById(this.id);
			return $(this.domElement);
		};
		
		/** 
		 *  保存为xml数据
		 * @return {String} 返回当前对象xml序列化字符串
		 */
		this.saveAsXML = function(){
		
		};
		
		/** 
		 *  解析xml数据为对象
		 * @param {String} xmlData xml序列化字符串
		 * @return {Object} 返回当前对象实例本身
		 */
		this.parseXML = function(xmlData){
			//parseing...
			return this;
		};
		
		/***
		 //获取工作流XML
		 this.getNodeXMLData = function() {
		 this.element = _$(this.id);
		 this.text = $('div>div',this.element).text();
		 var node_begin = "\n\t<node id=\""+ this.id +"\" name=\""+ this.text +"\">\n";
		 var content = getPointsXMLData(this.points);
		 var node_end = "\t</node>\n";
		 var curXMl = node_begin + content + node_end;
		 return curXMl;
		 };
		 
		 this.getSaveData = function(){
		 this.element = _$(this.id);
		 this.name = $('div>div',this.element).text();
		 var node_begin = "\n\t<node id=\"\" name=\""+ this.name +"\">\n";
		 node_begin += "\t\t<description><![CDATA[" + this.description + "]]></description>" ;
		 var content = getPointsSaveData(this.points);
		 var node_end = "\t</node>\n";
		 var curXMl = node_begin + content + node_end;
		 return curXMl;
		 };
		 */
	};
	//--------------------------------------------------------------------------------------------------------
	
	
	//-------------------------------条件(decision)----------------------------------------------
	/**
	 * 条件
	 * @class
	 * @constructor
	 */
	EAPP.Workflow.Decision = function(saveId){
	
		//调用基类构造
		//EAPP.Workflow.BaseNode.call();
		
		//类型名称
		this.typeName = "EAPP.Workflow.Decision";
		//结点类型
		this.nodeType = "shape";
		//图片路径
		this.imgSrc =  gEAPPWorkflow.basePath + "images/mIf01.gif";
		//内嵌图标路径
		this.innerIconSrc =  gEAPPWorkflow.basePath + "icons/decision_enabled.gif";
		
		//系统生成的id
		this.generateId = gEAPPWorkflow.createGUID();
		//对象的ID
		this.id = saveId?saveId: 'decision_' + this.generateId;
		//对象的元素
		this.element = null;
		//DOM元素
		this.domElement = null;
		
		//文本
		this.text = "条件" + this.generateId;
		//描述
		this.description = "";
		
		//储存关联连接线的对象
		this.innerLine = new EAPP.Workflow.InnerLines(this);
		
		/**
		 * 子属性数组
		 * @type Array
		 */
		this.attributeArray = new Array();
		
		//名称属性
		var attr_name = new EAPP.Workflow.Graphic.Attribute.Name();
		attr_name.displayName = '节点名称';
		attr_name.value = this.text;

		/**
		 * 是否同步
		 * [true | false]
		 * （未实现）
		 * 禁用
		 */
		var attr_is_async = new EAPP.Workflow.Graphic.Attribute.NormalAttribute();
		attr_is_async.attributeName = 'is-async';
		attr_is_async.displayName = '是否异步';
		attr_is_async.mapName = 'is-async';
		attr_is_async.mapType = 'attribute';
		attr_is_async.isNull = true;//可为空
		attr_is_async.isEnable = false;
		attr_is_async.isVisible = true;
		attr_is_async.defaultValue = "false";//默认值
		attr_is_async.validType = 'none';
		attr_is_async.inputType = 'input_checkbox';
		attr_is_async.dataType = 'boolean';
		
		//进入节点的事件
		var attr_event_node_enter = new EAPP.Workflow.Graphic.Attribute.Event();
		attr_event_node_enter.attributeName = "event_node_enter";
		attr_event_node_enter.displayName = "开始事件";
		alterAttribute(attr_event_node_enter,'type','NODE_ENTER');
		hideAttribute(attr_event_node_enter,'type');

		var attr_event_node_enter_action = getAttribute(attr_event_node_enter,"action");
		attr_event_node_enter_action.isHideHeader = true;//隐藏标题行
		//设置name属性
		var attr_event_node_enter_action_name = getAttribute(attr_event_node_enter_action,"name");
		var newId = gEAPPWorkflow.createGUID();
		attr_event_node_enter_action_name.getValueType = "ref";
		attr_event_node_enter_action_name.refObject = this;
		attr_event_node_enter_action_name.value = "%name%进入节点动作" + newId;//防止有多个action的情况发生名称冲突
		attr_event_node_enter_action_name.isVisible = false; // 不设置隐藏，自动隐藏的bug
		
		
		//离开节点事件
		var attr_event_node_leave = new EAPP.Workflow.Graphic.Attribute.Event();
		attr_event_node_leave.attributeName = "event_node_leave";
		attr_event_node_leave.displayName = "结束事件";
		alterAttribute(attr_event_node_leave,'type','NODE_LEAVE');
		hideAttribute(attr_event_node_leave,'type');
		
		var attr_event_node_leave_action = getAttribute(attr_event_node_leave,"action");
		attr_event_node_leave_action.isHideHeader = true;//隐藏标题行
		//设置name属性
		attr_event_node_leave_action_name = getAttribute(attr_event_node_leave_action,"name");
		var newId = gEAPPWorkflow.createGUID();
		attr_event_node_leave_action_name.getValueType = "ref";
		attr_event_node_leave_action_name.refObject = this;
		attr_event_node_leave_action_name.value = "%name%离开节点动作" + newId;//防止有多个action的情况发生名称冲突
		attr_event_node_leave_action_name.isVisible = false;// 不设置隐藏，自动隐藏的bug
			
		//说明属性
		var attr_description = new EAPP.Workflow.Graphic.Attribute.Description();
		attr_description.displayName ="节点描述";

		//-----------------------------------Handler-----------------------------------
		//handler属性
		var attr_handler = new EAPP.Workflow.Graphic.Attribute.Handler();
		attr_handler.displayName = "执行程序";
		
		//绑定表达式(虚拟属性)
		var attr_handler_expression = new EAPP.Workflow.Graphic.Attribute.NormalAttribute();
		attr_handler_expression.attributeName = "expression";
		attr_handler_expression.displayName = "表达式";
		attr_handler_expression.mapType = "none";
		attr_handler_expression.inputType = "html";
		attr_handler_expression.maxLength = 2000;
		
		var attr_handler_expression_id = this.id + "#" + this.typeName + "#handler#expression";
		var attr_handler_expression_html = getExpressionTD(attr_handler_expression,attr_handler_expression_id);
		attr_handler_expression.inputString = attr_handler_expression_html;
		
		attr_handler.attributeArray.push(attr_handler_expression);
		
		//引用到虚拟属性
		attr_handler.inputType = "none";
		attr_handler.getValueType = "direct-ref";
		attr_handler.refObject = attr_handler_expression;
		attr_handler.isVisible = false;
		
		//将动态根据transition 添加condition属性
		
		//将属性添加到当前数组
		this.attributeArray.push(attr_name);
		this.attributeArray.push(attr_is_async);
		this.attributeArray.push(attr_event_node_enter);
		this.attributeArray.push(attr_event_node_leave);
		this.attributeArray.push(attr_handler);
		this.attributeArray.push(attr_description);
		
		/** 
		 * 绘制
		 * @return {Boolean} 返回绘制是否成功
		 */
		this.render = function(){
			try {
				//创建元素
				this.domElement = document.createElement("v:shape");
				this.element = $(this.domElement);
				
				this.element.attr({//属性
					id: this.id,
					name: 'diamond',//菱形 //name属性暂时为图形的形状 //
					className: "drag",
					//显示属性
					strokecolor: '#9dadc7',
					strokeweight: '1px',
					filled: "t",
					coordsize: "60,121",//坐标大小
					path: "m0,60 l30,0,60,60,30,120,0,60 e" //路径，控制形状
				}).css({//样式				
					position: "absolute",//绝对定位
					"zIndex": "1",
					left: "0px",//左偏移
					top: "0px",//上偏移
					//默认大小
					width: "140px",
					height: "40px"
				});
				
				// this.element 没有附加事件响应
				//事件响应包括
				//1.单击出现属性
				//2.双击快速编辑名称
				//3.响应调整大小 onResize=\"resizePointForShape(this);
				//4.失去焦点时，内部文本框不可编辑 Onblur='this.contentEditable = false;'
				//事件如果无法采用jQuery方式附加，则采用原始DOM的方式
				
				getShapeInnerHTML(this);
				
				//添加单击时触发click事件
				var shape = this;
				this.element.click(function(){
					//显示属性面板 
					gEAPPWorkflow.showPropertyPanel(shape);
					//shape.validConnect();
				}).dblclick(function(){
					//快速编辑 
					gEAPPWorkflow.ContextMenuItemClick("edit");
				});
				
				return true;
			} 
			catch (e) {
				//trace(this.typeName + " render 绘制失败！" + e);
				return false;
			}
		};
		
		/**
		 * 验证线是否连接正常
		 */
		this.validConnect = function(){
			//return;// 验证事件不正常
			//如果连接不正确
			if(this.innerLine.relationArray.length <2){
				this.element.attr({strokecolor:'#FF0000'});//线变红色
				return false;
			}else{
				this.element.attr({strokecolor:'#9dadc7'});//恢复默认颜色
				return true;
			}
		};
		
		
		
		/** 
		 *  获取图元的DOM元素对象
		 * @return {Element} 返回当前对象的DOM元素对象
		 */
		this.getDomElement = function(){
			return document.getElementById(this.id);
		};
		
		/** 
		 *  获取图元元素的jQuery对象
		 * @return {Object}(jQuery) 返回当前对象元素的jQuery对象
		 */
		this.getElement = function(){
			this.domElement = document.getElementById(this.id);
			return $(this.domElement);
		};
		
		/** 
		 *  保存为xml数据
		 * @return {String} 返回当前对象xml序列化字符串
		 */
		this.saveAsXML = function(){
		
		};
		
		/** 
		 *  解析xml数据为对象
		 * @param {String} xmlData xml序列化字符串
		 * @return {Object} 返回当前对象实例本身
		 */
		this.parseXML = function(xmlData){
			//parseing...
			return this;
		};
		
		/***
		 //获取工作流XML
		 this.getNodeXMLData = function() {
		 this.element = _$(this.id);
		 this.text = $('div>div',this.element).text();
		 var node_begin = "\n\t<decision id=\""+ this.id +"\" name=\""+ this.text +"\">\n";
		 var content = getPointsXMLData(this.points);
		 var node_end = "\t</decision>";
		 var curXMl = node_begin + content + node_end;
		 return curXMl;
		 };
		 
		 this.getSaveData = function(){
		 this.element = _$(this.id);
		 this.name = $('div>div',this.element).text();
		 var node_begin = "\n\t<decision id=\"\" name=\""+ this.name +"\">\n";
		 node_begin += "\t\t<description><![CDATA[" + this.description + "]]></description>" ;
		 var content = getPointsSaveData(this.points);
		 var node_end = "\t</decision>\n";
		 var curXMl = node_begin + content + node_end;
		 return curXMl;
		 };
		 */
	};
	//EAPP.Workflow.Decision.prototype  =  new EAPP.Workflow.BaseNode();
	//--------------------------------------------------------------------------------------------------------

/**	
var dd = {
	aaa: function()
	{
		
	},
	bbb: function(event)
	{
		event.
		
		return;
	}
}

setListern
**/

	
	//-------------------------------分支(fork)----------------------------------------------
	/**
	 * 分支
	 * @class
	 * @constructor
	 */
	EAPP.Workflow.Fork = function(saveId){
	
		//调用基类构造
		//EAPP.Workflow.BaseNode.call();
		
		//类型名称
		this.typeName = "EAPP.Workflow.Fork";
		//结点类型
		this.nodeType = "shape";
		//图片路径
		this.imgSrc =  gEAPPWorkflow.basePath + "images/mBranch01.gif";
		//内嵌图标路径
		this.innerIconSrc =  gEAPPWorkflow.basePath + "icons/fork_enabled.gif";
		
		//系统生成的id
		this.generateId = gEAPPWorkflow.createGUID();
		//对象的ID
		this.id = saveId?saveId: 'fork_' + this.generateId;
		//对象的元素
		this.element = null;
		//DOM元素
		this.domElement = null;
		
		//文本
		this.text = "分支" + this.generateId;
		//描述
		this.description = "";
		
		//储存关联连接线的对象
		this.innerLine = new EAPP.Workflow.InnerLines(this);
		
		/**
		 * 子属性数组
		 * @type Array
		 */
		this.attributeArray = new Array();
		
		
		//名称属性
		var attr_name = new EAPP.Workflow.Graphic.Attribute.Name();
		attr_name.displayName = '节点名称';
		attr_name.value = this.text;
		
		//说明属性
		var attr_description = new EAPP.Workflow.Graphic.Attribute.Description();
		attr_description.displayName ="节点描述";
		
		
		/**
		 * 是否同步
		 * [true | false]
		 * （未实现）
		 * 禁用
		 */
		var attr_is_async = new EAPP.Workflow.Graphic.Attribute.NormalAttribute();
		attr_is_async.attributeName = 'is-async';
		attr_is_async.displayName = '是否异步';
		attr_is_async.mapName = 'is-async';
		attr_is_async.mapType = 'attribute';
		attr_is_async.isEnable = false;
		attr_is_async.inputType = 'input_checkbox';
		attr_is_async.dataType = 'boolean';
		
		
		
		//进入节点的事件
		var attr_event_node_enter = new EAPP.Workflow.Graphic.Attribute.Event();
		attr_event_node_enter.attributeName = "event_node_enter";
		attr_event_node_enter.displayName = "开始事件";
		alterAttribute(attr_event_node_enter,'type','NODE_ENTER');
		hideAttribute(attr_event_node_enter,'type');

		var attr_event_node_enter_action = getAttribute(attr_event_node_enter,"action");
		attr_event_node_enter_action.isHideHeader = true;//隐藏标题行
		//设置name属性
		var attr_event_node_enter_action_name = getAttribute(attr_event_node_enter_action,"name");
		var newId = gEAPPWorkflow.createGUID();
		attr_event_node_enter_action_name.getValueType = "ref";
		attr_event_node_enter_action_name.refObject = this;
		attr_event_node_enter_action_name.value = "%name%进入节点动作" + newId;//防止有多个action的情况发生名称冲突
		attr_event_node_enter_action_name.isVisible = false; // 不设置隐藏，自动隐藏的bug
		
		
		//离开节点事件
		var attr_event_node_leave = new EAPP.Workflow.Graphic.Attribute.Event();
		attr_event_node_leave.attributeName = "event_node_leave";
		attr_event_node_leave.displayName = "结束事件";
		alterAttribute(attr_event_node_leave,'type','NODE_LEAVE');
		hideAttribute(attr_event_node_leave,'type');
		
		var attr_event_node_leave_action = getAttribute(attr_event_node_leave,"action");
		attr_event_node_leave_action.isHideHeader = true;//隐藏标题行
		//设置name属性
		var attr_event_node_leave_action_name = getAttribute(attr_event_node_leave_action,"name");
		var newId = gEAPPWorkflow.createGUID();
		attr_event_node_leave_action_name.getValueType = "ref";
		attr_event_node_leave_action_name.refObject = this;
		attr_event_node_leave_action_name.value = "%name%进入节点动作" + newId;//防止有多个action的情况发生名称冲突
		attr_event_node_leave_action_name.isVisible = false; // 不设置隐藏，自动隐藏的bug
		
		//将属性添加到当前数组
		this.attributeArray.push(attr_name);
		this.attributeArray.push(attr_description);
		this.attributeArray.push(attr_event_node_enter);
		this.attributeArray.push(attr_event_node_leave);
		
		/** 
		 * 绘制
		 * @return {Boolean} 返回绘制是否成功
		 */
		this.render = function(){
			try {
				//创建元素
				this.domElement = document.createElement("v:roundrect");
				this.element = $(this.domElement);
				
				this.element.attr({//属性
					id: this.id,
					name: 'roundrect',//name属性暂时为图形的形状 //
					className: "drag",
					//显示属性
					strokecolor: '#9dadc7',
					strokeweight: '1px'
				
				}).css({//样式				
					position: "absolute",//绝对定位
					"zIndex": "1",
					left: "0px",//左偏移
					top: "0px",//上偏移
					//默认大小
					width: "140px",
					height: "40px"
				});
				
				// this.element 没有附加事件响应
				//事件响应包括
				//1.单击出现属性
				//2.双击快速编辑名称
				//3.响应调整大小 onResize=\"resizePointForShape(this);
				//4.失去焦点时，内部文本框不可编辑 Onblur='this.contentEditable = false;'
				//事件如果无法采用jQuery方式附加，则采用原始DOM的方式
				
				getShapeInnerHTML(this);
				
				//添加单击时触发click事件
				var shape = this;
				this.element.click(function(){
					//显示属性面板 
					gEAPPWorkflow.showPropertyPanel(shape);
					//shape.validConnect();
				}).dblclick(function(){
					//快速编辑 
					gEAPPWorkflow.ContextMenuItemClick("edit");
				});
				
				return true;
			} 
			catch (e) {
				//trace(this.typeName + " render 绘制失败！" + e);
				return false;
			}
		};
		
		/**
		 * 验证线是否连接正常
		 */
		this.validConnect = function(){
			//return;// 验证事件不正常
			//如果连接不正确
			if(this.innerLine.relationArray.length <2){
				this.element.attr({strokecolor:'#FF0000'});//线变红色
				return false;
			}else{
				this.element.attr({strokecolor:'#9dadc7'});//恢复默认颜色
				return true;
			}
		};
		
		
		
		/** 
		 *  获取图元的DOM元素对象
		 * @return {Element} 返回当前对象的DOM元素对象
		 */
		this.getDomElement = function(){
			return document.getElementById(this.id);
		};
		
		/** 
		 *  获取图元元素的jQuery对象
		 * @return {Object}(jQuery) 返回当前对象元素的jQuery对象
		 */
		this.getElement = function(){
			this.domElement = document.getElementById(this.id);
			return $(this.domElement);
		};
		
		/** 
		 *  保存为xml数据
		 * @return {String} 返回当前对象xml序列化字符串
		 */
		this.saveAsXML = function(){
		
		};
		
		/** 
		 *  解析xml数据为对象
		 * @param {String} xmlData xml序列化字符串
		 * @return {Object} 返回当前对象实例本身
		 */
		this.parseXML = function(xmlData){
			//parseing...
			return this;
		};
		
	};
	//EAPP.Workflow.Decision.prototype  =  new EAPP.Workflow.BaseNode();
	//--------------------------------------------------------------------------------------------------------
	
	//-------------------------------汇聚 (join)----------------------------------------------
	/**
	 * 汇聚
	 * @class
	 * @constructor
	 */
	EAPP.Workflow.Join = function(saveId){
	
		//调用基类构造
		//EAPP.Workflow.BaseNode.call();
		
		//类型名称
		this.typeName = "EAPP.Workflow.Join";
		//结点类型
		this.nodeType = "shape";
		//图片路径
		this.imgSrc =  gEAPPWorkflow.basePath + "images/mJoin01.gif";
		//内嵌图标路径
		this.innerIconSrc =  gEAPPWorkflow.basePath + "icons/join_enabled.gif";
		
		//系统生成的id
		this.generateId = gEAPPWorkflow.createGUID();
		//对象的ID
		this.id = saveId?saveId: 'join_' + this.generateId;
		//对象的元素
		this.element = null;
		//DOM元素
		this.domElement = null;
		
		//文本
		this.text = "合并" + this.generateId;
		//描述
		this.description = "";
		
		//储存关联连接线的对象
		this.innerLine = new EAPP.Workflow.InnerLines(this);
		
		/**
		 * 子属性数组
		 * @type Array
		 */
		this.attributeArray = new Array();
		
		//名称属性
		var attr_name = new EAPP.Workflow.Graphic.Attribute.Name();
		attr_name.displayName = '节点名称';
		attr_name.value = this.text;
		
		//说明属性
		var attr_description = new EAPP.Workflow.Graphic.Attribute.Description();
		attr_description.displayName ="节点描述";
		
		
		/**
		 * 是否同步
		 * [true | false]
		 * （未实现）
		 * 禁用
		 */
		var attr_is_async = new EAPP.Workflow.Graphic.Attribute.NormalAttribute();
		attr_is_async.attributeName = 'is-async';
		attr_is_async.displayName = '是否异步';
		attr_is_async.mapName = 'is-async';
		attr_is_async.mapType = 'attribute';
		attr_is_async.isEnable = false;
		attr_is_async.inputType = 'input_checkbox';
		attr_is_async.dataType = 'boolean';
		
		
		
		//进入节点的事件
		var attr_event_node_enter = new EAPP.Workflow.Graphic.Attribute.Event();
		attr_event_node_enter.attributeName = "event_node_enter";
		attr_event_node_enter.displayName = "开始事件";
		alterAttribute(attr_event_node_enter,'type','NODE_ENTER');
		hideAttribute(attr_event_node_enter,'type');

		var attr_event_node_enter_action = getAttribute(attr_event_node_enter,"action");
		attr_event_node_enter_action.isHideHeader = true;//隐藏标题行
		//设置name属性
		var attr_event_node_enter_action_name = getAttribute(attr_event_node_enter_action,"name");
		var newId = gEAPPWorkflow.createGUID();
		attr_event_node_enter_action_name.getValueType = "ref";
		attr_event_node_enter_action_name.refObject = this;
		attr_event_node_enter_action_name.value = "%name%进入节点动作" + newId;//防止有多个action的情况发生名称冲突
		attr_event_node_enter_action_name.isVisible = false; // 不设置隐藏，自动隐藏的bug
		
		
		//离开节点事件
		var attr_event_node_leave = new EAPP.Workflow.Graphic.Attribute.Event();
		attr_event_node_leave.attributeName = "event_node_leave";
		attr_event_node_leave.displayName = "结束事件";
		alterAttribute(attr_event_node_leave,'type','NODE_LEAVE');
		hideAttribute(attr_event_node_leave,'type');
		
		var attr_event_node_leave_action = getAttribute(attr_event_node_leave,"action");
		attr_event_node_leave_action.isHideHeader = true;//隐藏标题行
		//设置name属性
		var attr_event_node_leave_action_name = getAttribute(attr_event_node_leave_action,"name");
		var newId = gEAPPWorkflow.createGUID();
		attr_event_node_leave_action_name.getValueType = "ref";
		attr_event_node_leave_action_name.refObject = this;
		attr_event_node_leave_action_name.value = "%name%进入节点动作" + newId;//防止有多个action的情况发生名称冲突
		attr_event_node_leave_action_name.isVisible = false; // 不设置隐藏，自动隐藏的bug
		
		//将属性添加到当前数组
		this.attributeArray.push(attr_name);
		this.attributeArray.push(attr_description);
		this.attributeArray.push(attr_event_node_enter);
		this.attributeArray.push(attr_event_node_leave);
		
		/** 
		 * 绘制
		 * @return {Boolean} 返回绘制是否成功
		 */
		this.render = function(){
			try {
				//创建元素
				this.domElement = document.createElement("v:roundrect");
				this.element = $(this.domElement);
				
				this.element.attr({//属性
					id: this.id,
					name: 'roundrect',//name属性暂时为图形的形状 //
					className: "drag",
					//显示属性
					strokecolor: '#9dadc7',
					strokeweight: '1px'
				
				}).css({//样式				
					position: "absolute",//绝对定位
					"zIndex": "1",
					left: "0px",//左偏移
					top: "0px",//上偏移
					//默认大小
					width: "140px",
					height: "40px"
				});
				
				// this.element 没有附加事件响应
				//事件响应包括
				//1.单击出现属性
				//2.双击快速编辑名称
				//3.响应调整大小 onResize=\"resizePointForShape(this);
				//4.失去焦点时，内部文本框不可编辑 Onblur='this.contentEditable = false;'
				//事件如果无法采用jQuery方式附加，则采用原始DOM的方式
				
				getShapeInnerHTML(this);
				
				//添加单击时触发click事件
				var shape = this;
				this.element.click(function(){
					//显示属性面板 
					gEAPPWorkflow.showPropertyPanel(shape);
					//shape.validConnect();
				}).dblclick(function(){
					//快速编辑 
					gEAPPWorkflow.ContextMenuItemClick("edit");
				});
				
				return true;
			} 
			catch (e) {
				//trace(this.typeName + " render 绘制失败！" + e);
				return false;
			}
		};
		
		/**
		 * 验证线是否连接正常
		 */
		this.validConnect = function(){
			//return;// 验证事件不正常
			//如果连接不正确
			if(this.innerLine.relationArray.length <2){
				this.element.attr({strokecolor:'#FF0000'});//线变红色
				return false;
			}else{
				this.element.attr({strokecolor:'#9dadc7'});//恢复默认颜色
				return true;
			}
		};
		
		
		
		/** 
		 *  获取图元的DOM元素对象
		 * @return {Element} 返回当前对象的DOM元素对象
		 */
		this.getDomElement = function(){
			return document.getElementById(this.id);
		};
		
		/** 
		 *  获取图元元素的jQuery对象
		 * @return {Object}(jQuery) 返回当前对象元素的jQuery对象
		 */
		this.getElement = function(){
			this.domElement = document.getElementById(this.id);
			return $(this.domElement);
		};
		
		/** 
		 *  保存为xml数据
		 * @return {String} 返回当前对象xml序列化字符串
		 */
		this.saveAsXML = function(){
		
		};
		
		/** 
		 *  解析xml数据为对象
		 * @param {String} xmlData xml序列化字符串
		 * @return {Object} 返回当前对象实例本身
		 */
		this.parseXML = function(xmlData){
			//parseing...
			return this;
		};
		
	};
	//EAPP.Workflow.Decision.prototype  =  new EAPP.Workflow.BaseNode();
	//--------------------------------------------------------------------------------------

	//-------------------------------子流程节点 (subflow-node)----------------------------------------------
	/**
	 * 子流程节点
	 * @class
	 * @constructor
	 */
	EAPP.Workflow.SubflowNode = function(saveId){
	
		//调用基类构造
		//EAPP.Workflow.BaseNode.call();
		
		//类型名称
		this.typeName = "EAPP.Workflow.SubflowNode";
		//结点类型
		this.nodeType = "shape";
		//图片路径
		this.imgSrc =  gEAPPWorkflow.basePath + "images/mSubflow01.gif";
		//内嵌图标路径
		this.innerIconSrc =  gEAPPWorkflow.basePath + "icons/graph.gif";
		
		//系统生成的id
		this.generateId = gEAPPWorkflow.createGUID();
		//对象的ID
		this.id = saveId?saveId: 'subflow-node_' + this.generateId;
		//对象的元素
		this.element = null;
		//DOM元素
		this.domElement = null;
		
		//文本
		this.text = "子流程节点" + this.generateId;
		//描述
		this.description = "";
		
		//储存关联连接线的对象
		this.innerLine = new EAPP.Workflow.InnerLines(this);
		
		/**
		 * 子属性数组
		 * @type Array
		 */
		this.attributeArray = new Array();
		
		//名称属性
		var attr_name = new EAPP.Workflow.Graphic.Attribute.Name();
		attr_name.displayName = '节点名称';
		attr_name.value = this.text;
		
		//说明属性
		var attr_description = new EAPP.Workflow.Graphic.Attribute.Description();
		attr_description.displayName ="节点描述";
		
		
		/**
		 * 是否同步
		 * [true | false]
		 * （未实现）
		 * 禁用
		 */
		var attr_is_async = new EAPP.Workflow.Graphic.Attribute.NormalAttribute();
		attr_is_async.attributeName = 'is-async';
		attr_is_async.displayName = '是否异步';
		attr_is_async.mapName = 'is-async';
		attr_is_async.mapType = 'attribute';
		attr_is_async.isEnable = false;
		attr_is_async.inputType = 'input_checkbox';
		attr_is_async.dataType = 'boolean';
		
		
		
		//进入节点的事件
		var attr_event_node_enter = new EAPP.Workflow.Graphic.Attribute.Event();
		attr_event_node_enter.attributeName = "event_node_enter";
		attr_event_node_enter.displayName = "开始事件";
		alterAttribute(attr_event_node_enter,'type','NODE_ENTER');
		hideAttribute(attr_event_node_enter,'type');

		var attr_event_node_enter_action = getAttribute(attr_event_node_enter,"action");
		attr_event_node_enter_action.isHideHeader = true;//隐藏标题行
		//设置name属性
		var attr_event_node_enter_action_name = getAttribute(attr_event_node_enter_action,"name");
		var newId = gEAPPWorkflow.createGUID();
		attr_event_node_enter_action_name.getValueType = "ref";
		attr_event_node_enter_action_name.refObject = this;
		attr_event_node_enter_action_name.value = "%name%进入节点动作" + newId;//防止有多个action的情况发生名称冲突
		attr_event_node_enter_action_name.isVisible = false; // 不设置隐藏，自动隐藏的bug
		
		
		//离开节点事件
		var attr_event_node_leave = new EAPP.Workflow.Graphic.Attribute.Event();
		attr_event_node_leave.attributeName = "event_node_leave";
		attr_event_node_leave.displayName = "结束事件";
		alterAttribute(attr_event_node_leave,'type','NODE_LEAVE');
		hideAttribute(attr_event_node_leave,'type');
		
		var attr_event_node_leave_action = getAttribute(attr_event_node_leave,"action");
		attr_event_node_leave_action.isHideHeader = true;//隐藏标题行
		//设置name属性
		var attr_event_node_leave_action_name = getAttribute(attr_event_node_leave_action,"name");
		var newId = gEAPPWorkflow.createGUID();
		attr_event_node_leave_action_name.getValueType = "ref";
		attr_event_node_leave_action_name.refObject = this;
		attr_event_node_leave_action_name.value = "%name%进入节点动作" + newId;//防止有多个action的情况发生名称冲突
		attr_event_node_leave_action_name.isVisible = false; // 不设置隐藏，自动隐藏的bug
		
		//子流程属性
		var attr_subflow =  new EAPP.Workflow.Graphic.Attribute.Subflow ();
		 
		

		
		//将属性添加到当前数组
		this.attributeArray.push(attr_name);
		this.attributeArray.push(attr_subflow);
		this.attributeArray.push(attr_description);
		this.attributeArray.push(attr_event_node_enter);
		this.attributeArray.push(attr_event_node_leave);
		
		/** 
		 * 绘制
		 * @return {Boolean} 返回绘制是否成功
		 */
		this.render = function(){
			try {
				//创建元素
				this.domElement = document.createElement("v:roundrect");
				this.element = $(this.domElement);
				
				this.element.attr({//属性
					id: this.id,
					name: 'roundrect',//name属性暂时为图形的形状 //
					className: "drag",
					//显示属性
					strokecolor: '#9dadc7',
					strokeweight: '1px'
				
				}).css({//样式				
					position: "absolute",//绝对定位
					"zIndex": "1",
					left: "0px",//左偏移
					top: "0px",//上偏移
					//默认大小
					width: "140px",
					height: "40px"
				});
				
				// this.element 没有附加事件响应
				//事件响应包括
				//1.单击出现属性
				//2.双击快速编辑名称
				//3.响应调整大小 onResize=\"resizePointForShape(this);
				//4.失去焦点时，内部文本框不可编辑 Onblur='this.contentEditable = false;'
				//事件如果无法采用jQuery方式附加，则采用原始DOM的方式
				
				getShapeInnerHTML(this);
				
				//添加单击时触发click事件
				var shape = this;
				this.element.click(function(){
					//显示属性面板 
					gEAPPWorkflow.showPropertyPanel(shape);
					//shape.validConnect();
				}).dblclick(function(){
					//快速编辑 
					gEAPPWorkflow.ContextMenuItemClick("edit");
				});
				
				return true;
			} 
			catch (e) {
				//trace(this.typeName + " render 绘制失败！" + e);
				return false;
			}
		};
		
		/**
		 * 验证线是否连接正常
		 */
		this.validConnect = function(){
			//return;// 验证事件不正常
			//如果连接不正确
			if(this.innerLine.relationArray.length <2){
				this.element.attr({strokecolor:'#FF0000'});//线变红色
				return false;
			}else{
				this.element.attr({strokecolor:'#9dadc7'});//恢复默认颜色
				return true;
			}
		};
		
		
		
		/** 
		 *  获取图元的DOM元素对象
		 * @return {Element} 返回当前对象的DOM元素对象
		 */
		this.getDomElement = function(){
			return document.getElementById(this.id);
		};
		
		/** 
		 *  获取图元元素的jQuery对象
		 * @return {Object}(jQuery) 返回当前对象元素的jQuery对象
		 */
		this.getElement = function(){
			this.domElement = document.getElementById(this.id);
			return $(this.domElement);
		};
		
		/** 
		 *  保存为xml数据
		 * @return {String} 返回当前对象xml序列化字符串
		 */
		this.saveAsXML = function(){
		
		};
		
		/** 
		 *  解析xml数据为对象
		 * @param {String} xmlData xml序列化字符串
		 * @return {Object} 返回当前对象实例本身
		 */
		this.parseXML = function(xmlData){
			//parseing...
			return this;
		};
		
	};
	//EAPP.Workflow.Decision.prototype  =  new EAPP.Workflow.BaseNode();
	//--------------------------------------------------------------------------------------


 
/**
 * @author Caijianfeng
 */

/**
 * 工作流图形化工具类命名空间
 */
EAPP.Workflow.Graphic.Util = {};

/**
 * 文本区的工具类
 */
EAPP.Workflow.Graphic.Util.Textarea = {};

/**
 * 验证文本区最大长度的工具类
 */
EAPP.Workflow.Graphic.Util.Textarea.MaxLength = function(){

	
	function onKeyPress(textareaElement,maxLength){
	
		if(!textareaElement || isNaN(maxLength)){return;}
		
		maxLength = parseInt(maxLength);
		
		var range = textareaElement.document.selection.createRange();
		if(range.text.length >=1){
			event.returnValue = true;
		}else if(textareaElement.value.length > maxLength-1){
			event.returnValue = false;
		}
	};
	
	
	function onKeyDown(textareaElement, maxLength){
		
		if(!textareaElement || isNaN(maxLength)){return;}
		
		if(textareaElement.value.length > maxLength){
			
			var range = textareaElement.document.selection.createRange();
			range.moveStart("character", -1*(textareaElement.value.length - maxLength));
			range.text = "";//把多余区域清空
		}
	};
	
	
	function onBeforePaste(textareaElement, maxLength){
		if(!textareaElement || isNaN(maxLength)){
			event.returnValue = false;
		}
	};
	
	
	function onPaste(textareaElement, maxLength){
		
		if (!textareaElement || isNaN(maxLength)){return false;}
		
		event.returnValue = false;
		maxLength = parseInt(maxLength);
		
		var range = textareaElement.document.selection.createRange();
		var insertLength = maxLength - textareaElement.value.length + range.text.length;
		
		if (!window.clipboardData || !window.clipboardData.getData || typeof(window.clipboardData.getData("Text")) == "undefined") {
			return false;
		}
		try {
			var pasteData = window.clipboardData.getData("Text").substr(0, insertLength);
			range.text = pasteData;
		}catch(e){}
	};
	
	this.onKeyPress = onKeyPress;
	this.onKeyDown = onKeyDown;
	this.onBeforePaste = onBeforePaste;
	this.onPaste = onPaste;
	
};





	
/**
 * 查看流程图
 */
EAPP.Workflow.Graphic.ViewFlow = function(xmlString,parentId){
	
	    var xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
	    xmlDoc.async = false;
		try{
	    	xmlDoc.loadXML(xmlString);	//对文件和XML片断的加载
		}catch(e){
			xmlDoc.load(xmlString);		//对完整XML文档和URL的加载
		} 
	
	
		var parentPanel = $("#" + parentId);
		
		parentPanel.html("");
			
		var parentTable = $("<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" ><tr><td></td></tr></table>").appendTo(parentPanel)
		
		//添加容器面板(附加一个div到父面板中)
		container = $("<div/>").attr({
			width: "100%",
			height: "100%"
		}).appendTo($("td",parentTable)); //容器
		
		//工作区主面板
		mainPanel = $("<div/>").addClass("workflowMain").appendTo(container);
		
		//工作区面板
		workPanel = $("<div/>").addClass("workflowArea").appendTo(mainPanel);
		//工作区头部面板
		workHeadPanel = $("<div/>").addClass("workflowAreaHead").appendTo(workPanel);
		//标题栏
		workTitlePanel = $("<div/>").addClass("workflowArea").appendTo(workHeadPanel);
		workTitlePanel.css({
			"text-overflow":"ellipsis",
			"white-space":"nowrap",
			"overflow":"hidden",
			"width":"500px"
		});
		
		//工作区内容面板
		workContentPanel = $("<div/>").addClass("workflowCon").appendTo(workPanel);
		workContentPanel.attr("id", "contentsPanel");
		
		//
		var workflowShadowPanel = $("<div/>").addClass("workflowShadow").appendTo(workContentPanel);
		
		var workflowName = $("flow-definition ",xmlDoc).attr("name");
		var workflowState = $("flow-definition ",xmlDoc).attr("state");
		
		//更新工作区的标题栏
		workTitlePanel.html("状态：" + workflowState + " 流程名称：" + workflowName);
	   	workContentPanel.html($("html-content",xmlDoc).text()); 

		//显示主面板
		container.show();	 
 
		$("span",workContentPanel).each(function(){
			var self = $(this);
			var shapeName = self.text();
			self.parent().html(shapeName);
		});
 
}