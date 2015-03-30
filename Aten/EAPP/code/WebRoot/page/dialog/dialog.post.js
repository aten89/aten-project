/**
* 类型：公用组件
* 名称：职位选择框
* 用途：用于各应用模块中实现职位的选择
*	   实现两种模式：single(单选),multi(多选)
*
* 用法：
*	1、集成：
*		1）在需要集成的页面中，引用dialog.post.js（如下）：
*			<script language="javascript" src="dialog.post.js" />
*		2）在页面加载完成之后（如onload事件中），对控件进行初始化：
*			//注意：要传入ERMP的路径和本系统的根路径
*			var selector = new PostDialog(ERMP_PATH,BASE_PATH);
*	2、选择职位：
*		//在需要调用职位选择框的地方，调用以下方法：
*			selector.setSelectType("single");	//或者multi
*			selector.appendSelectedPost(postId);
*			var result = selector.openDialog();		//返回的结果是一个数组
*			
* 作者：richfans  2010-01-27
*/

function PostDialog(ermp_path,base_path){
	this.ermpPath = (ermp_path==null?"/":ermp_path);
	this.basePath = (base_path==null?this.ermpPath:base_path);
	this.callbackFun = null;
    this.selected = [];		//选择结果
   
    this.dialogWin=null;
    
    this.setErmpPath = function(path){
    	this.ermpPath = path;
    };
    
    this.setBasePath = function(path){
    	this.basePath = path;
    };
    
    this.setCallbackFun = function(fun) {
    	this.callbackFun = fun;
    }
    
	/**
	*  添加已选择值
	*  @param valArr
	*/
	this.appendSelected = function(valArr){
		this.selected.push(valArr);
	};
	
	/**
	*  添加已选择值（数组）
	*  @param valArrs
	*/
	this.appendSelectedList = function(valArrs){
		for (var i=0 ; i<valArrs.length ; i++){
			this.selected.push(valArrs[i]);
		}
	};
	
	
	/**
	*  打开选择框
	*/
	this.openDialog = function(selectType){
		//参数绑定到window，弹出IFRAME直接访问
		window.dialogArguments1=this;
		var callback_fun = this.callbackFun;
		var selected = this.selected;
		//判断是单选还是多选，将打开不同的网页
		if (selectType == "multi"){
			this.dialogWin = $.showModalDialog("职位选择框（多选）", this.basePath + "page/dialog/post_multi.jsp", 342, 347, function(){
				if (typeof callback_fun === 'function') {
					if (window.returnValue1) {
						callback_fun(selected);
					} else {
						callback_fun(null);
					}
				}
			});
		} else {
			this.dialogWin = $.showModalDialog("职位选择框（单选）", this.basePath + "page/dialog/post_single.jsp", 342, 347, function(){
				if (typeof callback_fun === 'function') {
					if (window.returnValue1) {
						callback_fun(selected[0]);
					} else {
						callback_fun(null);
					}
				}
			});
		}
	};
	
	this.closeDialog = function() {
		if (this.dialogWin) {
			this.dialogWin.close();
		}
	};
}