/**
* 类型：公用组件
* 名称：部门选择框
* 用途：用于各应用模块中实现部门的选择
*	   实现两种模式：single(单选),multi(多选)
*
* 用法：
*	1、集成：
*		1）在需要集成的页面中，引用dialog.dept.js（如下）：
*			<script language="javascript" src="dialog.dept.js" />
*		2）在页面加载完成之后（如onload事件中），对控件进行初始化：
*			//注意：要传入ERMP的路径和本系统的根路径
*			var selector = new DeptDialog(ERMP_PATH, BASE_PATH);
*	2、选择部门：
*		//在需要调用部门选择框的地方，调用以下方法：
*			selector.setSelectType("single");	//或者multi
*			selector.appendSelectedDept(deptId);
*			var result = selector.openDialog();		//返回的结果是一个数组
*			
* 作者：richfans  2010-01-27
*/

function DeptDialog(){
	this.callbackFun = null;
	this.enableVals = null;	//可选择的值
    this.selected = [];		//选择结果
    
    this.dialogWin=null;
    
    this.setCallbackFun = function(fun) {
    	this.callbackFun = fun;
    }
    this.setEnableVals = function(vals) {
    	this.enableVals = vals;
    }
    
	/**
	*  添加已选择值
	*  @param valArr
	*/
	this.appendSelected = function(val){
		this.selected.push(val);
	};
	
	/**
	*  添加已选择值（数组）
	*  @param valArrs
	*/
	this.appendSelectedList = function(vals){
		for (var i=0 ; i<vals.length ; i++){
			this.selected.push(vals[i]);
		}
	};

	/**
	*  打开选择框
	*/
	this.openDialog = function(){
		
		//参数绑定到window，弹出IFRAME直接访问
		window.dialogArguments1=this;
		var callback_fun = this.callbackFun;
		var selected = this.selected;
		//判断是单选还是多选，将打开不同的网页
		this.dialogWin = $.showModalDialog("部门选择", BASE_PATH + "page/report/select_dept.jsp", 342, 347, function(){
			if (typeof callback_fun === 'function') {
				if (window.returnValue1) {
					callback_fun(selected);
				} else {
					callback_fun(null);
				}
			}
		});

	};
	
	this.closeDialog = function() {
		if (this.dialogWin) {
			this.dialogWin.close();
		}
	};
}