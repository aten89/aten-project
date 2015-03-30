/**
* 类型：公用组件
* 名称：设备选择框
* 用途：用于各应用模块中实现设备的选择
*
* 用法：
* 
* 1、增加html代码：
* <td>
* 	<input id="purchaseFlowKey" type="text" value="" >
* 	<input readonly id="purchaseFlowName" type="text" class="ipt01"  style="width:100px" value="">
* 	<input type="button" class="selBtn" onClick="openDeviceDialog({flowClass:'SBSGLC',flowKeyObjId:'purchaseFlowKey',flowNameObjId:'purchaseFlowName'})" /> 
* </td>
* 
* 2、引入JS：<script type="text/javascript" src="webui/dialog/org.eapp.oa.dialog.flow.js"></script>
*
*/

function KnowledgeDialog(oa_path, base_path){
	this.oaPath = oa_path;
	this.basePath = base_path;
    this.selected = [];		//选择结果
    this.knowledgeIds = null;
    this.act = null;
    
    this.callbackFun = null;
    this.dialogWin=null;
    
    this.setOaPath = function(path){
    	this.oaPath = path;
    };
    
    this.setBasePath = function(path){
    	this.basePath = path;
    };
    
    this.setKnowledgeId = function(knowledgeIds){
    	this.knowledgeIds = knowledgeIds;
    }
    
    this.setAct = function(act){
    	this.act = act;
    }
    
	/**
	*  添加已选择知识点（单个）
	*  @param flow
	*/
	this.appendSelectedKnowledge = function(knowledge){
		this.selected.push(knowledge);
	};

	/**
	*  添加已选择知识点（数组）
	*  @param knowledge
	*/
	this.appendSelectedList = function(knowledge){
		for (var i=0 ; i<knowledge.length ; i++){
			this.selected.push(knowledge[i]);
		}
	};
	
	this.setCallbackFun = function(fun) {
    	this.callbackFun = fun;
    };

	
	
	/**
	*  打开选择框
	*/
	this.openDialog = function(knowledgeIds,act){
		this.setKnowledgeId(knowledgeIds);
		this.setAct(act);
		
		//参数绑定到window，弹出IFRAME直接访问
		window.dialogArguments1=this;
		
		var callback_fun = this.callbackFun;
		var selected = this.selected;
		
		this.dialogWin = $.showModalDialog("知识点选择框", this.basePath + "page/knowledge/tempkb/kb_dialog.jsp", 342, 347, function(){
			if (typeof callback_fun === 'function') {
				if (window.returnValue1) {
					callback_fun(selected[0]);
				} else {
					callback_fun(null);
				}
			}
		});
		
//		var ok = window.showModalDialog(this.basePath + "page/Knowledge/Temp/knowledge_dialog.jsp",this,"dialogWidth:376px;dialogHeight:391px;help:no;center:yes;status:no");
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