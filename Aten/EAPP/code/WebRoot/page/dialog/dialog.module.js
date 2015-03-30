/**
* 类型：公用组件
* 名称：模块选择框
* 用途：用于各应用模块中实现模块的选择
*	   实现两种模式：single(单选),multi(多选)
*
* 用法：
*	1、集成：
*		1）在需要集成的页面中，引用dialog.module.js（如下）：
*			<script language="javascript" src="dialog.module.js" />
*		2）在页面加载完成之后（如onload事件中），对控件进行初始化：
*			//注意：要传入ERMP的路径
*			var selector = new ModuleDialog(ERMP_PATH);
*	2、选择模块：
*		//在需要调用模块选择框的地方，调用以下方法：
*			selector.setSelectType("single");	//或者multi
*			selector.appendSelectedModule(moduleId);
*			var result = selector.openDialog();		//返回的结果是一个数组
*			
* 作者：richfans  2010-01-27
*/

function ModuleDialog(ermp_path,base_path){
	this.ermpPath = (ermp_path==null?"/":ermp_path);
	this.basePath = (base_path==null?this.ermpPath:base_path);
	this.callbackFun = null;
	this.subSystemId = "";
    this.selected = [];		//选择结果
    
    this.dialogWin=null;
    
    this.setErmpPath = function(path){
    	this.ermpPath = path;
    };
    
    this.setBasePath = function(path){
    	this.basePath = path;
    };
    
    this.setSubSystemId = function(id){
    	this.subSystemId = id;
    };
    
    this.setCallbackFun = function(fun) {
    	this.callbackFun = fun;
    }
    
	/**
	*  添加已选择模块（单个）
	*  @param module
	*/
	this.appendSelected = function(module){
		this.selected.push(module);
	};

	/**
	*  添加已选择模块（数组）
	*  @param modules
	*/
	this.appendSelectedList = function(modules){
		for (var i=0 ; i<modules.length ; i++){
			this.selected.push(modules[i]);
		}
	};
	
	/**
	*  设置选择方式，默认为：单人
	*  @param type
	*/
	this.setSelectType = function(type){
		if (type=="multi"){
			this.selectType = "multi";
		}else{
			this.selectType = "single";
		}
	};
	
	/**
	*  打开选择框
	*/
	this.openDialog = function(selectType){
		
		//判断是单选还是多选，将打开不同的网页
		
		//暂时仅支持单选模式
		//if (selectType == "multi"){
		//	var ok = window.showModalDialog(this.basePath + "webui/dialog/module_multi.jsp",arg,"dialogWidth:370px;dialogHeight:420px;help:no;center:yes;status:no");
		//	if (ok){
		//		return arg.selected;
		//	}else{
		//		return null;
		//	}
		//}else{
		var height = 345;
		if (this.subSystemId != null && this.subSystemId != "")  {
			height -= 26;
		}
		//参数绑定到window，弹出IFRAME直接访问
		window.dialogArguments1=this;
		var callback_fun = this.callbackFun;
		var selected = this.selected;
		
		this.dialogWin = $.showModalDialog("模块选择框（单选）", this.basePath + "page/dialog/module_single.jsp", 342, height, function(){
			if (typeof callback_fun === 'function') {
				if (window.returnValue1) {
					callback_fun(selected[0]);
				} else {
					callback_fun(null);
				}
			}
		});
//		
//		var ok = window.showModalDialog(this.basePath + "page/dialog/module_single.jsp",this,"dialogWidth:342px;dialogHeight:" + height + "px;help:no;center:yes;status:no");
//		if (ok){
//			return this.selected[0];
//		}else{
//			return null;
//		}
	};
	
	this.closeDialog = function() {
		if (this.dialogWin) {
			this.dialogWin.close();
		}
	};
}