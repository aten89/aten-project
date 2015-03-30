var args = parent.window.dialogArguments1;
$(initPage);
function initPage(){
	initDeptList();
}

//初始化组织机构树
function initDeptList(){
	var url = ERMP_PATH + "m/rbac_group/subgroups?type=&jsoncallback=?";
	
	$.getJSON(url,function(data){
		$("#groupsList > li > ul").html(data.htmlValue);
		$("#groupsList").simpleTree({
            animate: true,
            basePath : ERMP_PATH,
            json:true,
            createCheckBox : true,
            afterAjax : function(node){
            	//由于树是分步加载的
            	//默认选中外部传入的部门
				initSelected($(node));
            	//禁用不可选的值
        		initEnableVals($(node));
            }
        });
        //默认选中外部传入的部门
		initSelected($("#groupsList"));
        //禁用不可选的值
        initEnableVals($("#groupsList"));
	});
}

//禁用不可选的值
function initEnableVals(obj) {
	var enableVals = args.enableVals;
    if (enableVals) {
    	obj.find("input[type='checkbox']").each(
			function(i){
				var array = this.value.split(":");
				var finded = false;
				for (var i=0 ; i<enableVals.length ; i++){
    				if (enableVals[i] == array[1]) {
    					finded = true;
    					break;
    				}
    			}
    			if (!finded) {
    				$(this).attr("checked", false).attr("disabled",true);
    			} else {
    				//可用的复选框增加双击事件，双击选中或取消选中所有下级节点
    				$(this).dblclick(function(){
    					//当前节点选中状态
    					var thisCheckflag = $(this).attr("checked");
    					//找到下级所有复选框
    					$(this).parent().find("ul").find("input[type='checkbox']").each(function(){
    						if ($(this).attr("disabled") == false) {
    							//状态为可用的修改选中状态
    							$(this).attr("checked", thisCheckflag);
    						}
    					});
    				});
    			}
			}
		);
    }
}

//默认选中外部传入的部门
function initSelected(obj) {
	var selected = args.selected;
	for (var i=0 ; i<selected.length ; i++){
		var name = selected[i];
		obj.find("input[name='chk_" + name + "']").attr("checked",true);
	}
}

function selectDept(){
	var selected = args.selected;
	for (var i=selected.length-1 ; i>=0 ; i--){
		var checkbox = $("#groupsList").find("input[name='chk_" + selected[i] + "']")[0];
		
		if (checkbox != null){
			if (checkbox.checked==false) selected.splice(i,1);
			checkbox.checked = false;
		}
	}
	$("#groupsList").find("input[type='checkbox']").each(
		function(i){
			if (this.checked && !this.disabled){
				var array = this.value.split(":");
				selected.push(array[1]);
			}
		}
	);
	parent.window.returnValue1 = true;
	args.closeDialog();
}

function cancel(){
	parent.window.returnValue1 = false;
	args.closeDialog();
}

function clearDept(){
//	args.selected = [];
	args.selected.splice(0,args.selected.length);
	$("#groupsList").find("input[type='checkbox']").each(
		function(i){
			this.checked = false;
		}
	);
}