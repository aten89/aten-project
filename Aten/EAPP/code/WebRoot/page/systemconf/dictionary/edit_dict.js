$(initDataDictionaryManage);

function initDataDictionaryManage(){
    
    //取得操作类型
    var action = $("#opType").val();
    if($.trim(action) == ""){
        return;
    }
    
    switch(action){
        case "add" :             
            $("#addDataDict,#addDataType,#modifyDataDict,#deleteDataDict,#sortDataDict").attr("disabled","true").addClass("icoNone");
            
            $("#saveTypeDict,#resetTypeDict,#saveAddTypeDict").removeAttr("disabled").removeClass("icoNone");
            
            $("#dataDictKey,#dataDictValue,#dataDictMaxValue,#dataDictMinValue,#description").removeAttr("readonly");
           	$("#showParentDataDict").attr("disabled","true").addClass("icoNone");

	    	//从外层框架取默认值
	    	$("#subSystemName").text($("#curSubSystemName").val());
	    	$("#hidSubSystemId").val($("#curSubSystemId").val());
	    	$("#hidParentDataDictId").val($("#curDataDictId").val());
	    	$("#parentDataDict").val($("#curDataDictName").val());
    		$("#dataDictType").val($("#curDataDictType").val());
    		$("#dataDictType").parent().append($("#curDataDictType").val());
    		$("#parentDataDict").parent().text($("#parentDataDict").val());
 //   		$("#dataDictType").hide().parent().append($("#dataDictType").val());
            //重写重置按扭
    		$("#resetTypeDict").click(function(){
    			document.mainForm.reset();
    			$("#subSystemName").text($("#curSubSystemName").val());
		    	$("#hidSubSystemId").val($("#curSubSystemId").val());
		    	$("#hidParentDataDictId").val($("#curDataDictId").val());
		    	$("#parentDataDict").val($("#curDataDictName").val());
	    		$("#dataDictType").val($("#curDataDictType").val());
    			return false;
    		});
            bindEvent();
            break;
        case "modify" :
            $("#addDataDict,#addDataType,#deleteDataDict,#sortDataDict").removeAttr("disabled").removeClass("icoNone");
            $("#modifyDataDict").attr("disabled","true").addClass("icoNone");
            
            $("#saveTypeDict,#resetTypeDict").removeAttr("disabled").removeClass("icoNone");
            $("#saveAddTypeDict").attr("disabled","true").addClass("icoNone");
            
            $("#dataDictKey,#dataDictValue,#dataDictMaxValue,#dataDictMinValue,#description").removeAttr("readonly");
           	$("#showParentDataDict").removeAttr("disabled").removeClass("icoNone");
            
 //          	$("#dataDictType").hide().parent().append($("#dataDictType").val());
           	
            bindEvent();     
            break;
        case "view" :
            $("#addDataDict,#addDataType,#modifyDataDict,#deleteDataDict,#sortDataDict").removeAttr("disabled").removeClass("icoNone");
            
            $("#saveTypeDict,#resetTypeDict,#saveAddTypeDict").remove();
            
            $("#showParentDataDict").attr("disabled","true").addClass("icoNone");
            
            //点击都是时入查看页，查看状态时把相关字段保存到外导框架中
            $('#titlName').html("— " + $("#dataDictKey").val());
		    $('#curDataDictId').val($('#hidDataDictId').val());
		    $('#curDataDictName').val($('#dataDictKey').val());
		    $('#curDataDictType').val($('#dataDictType').val());
		    
		    $("#parentDataDict").parent().text($("#parentDataDict").val());
 			$("#dataDictKey").parent().text($("#dataDictKey").val());
 			$("#dataDictValue").parent().text($("#dataDictValue").val());
 			$("#dataDictMaxValue").parent().text($("#dataDictMaxValue").val());
 			$("#dataDictMinValue").parent().text($("#dataDictMinValue").val());
 //			$("#dataDictType").parent().text($("#dataDictType").val());
 			$("#description").parent().text($("#description").val());
		    
            break;
    }
    
    //显示工具栏
    $('#manageToolBar').show();
    
}

function bindEvent() {
	$("#saveTypeDict").click(function(){
        	saveTypeDict(false);
        }
    );
    $("#saveAddTypeDict").click(function(){
        	saveTypeDict(true);
        }
    );

    //上级部门绑定
    $('#showParentDataDict').click(function(){
//    	var selector = new DictDialog(BASE_PATH);
//    	selector.setSubSystemId($("#curSubSystemId").val());
//		var dict = selector.openDialog("single");
//		if (dict != null){
//			$("#hidParentDataDictId").val(dict.id);
//			$("#parentDataDict").val(dict.name);
//		}
    	var selector = new DictDialog(BASE_PATH,BASE_PATH);
    	selector.setSubSystemId($("#curSubSystemId").val());
    	selector.setCallbackFun(function(dict){
			if (dict) {
				$("#hidParentDataDictId").val(dict.id);
				$("#parentDataDict").val(dict.name);
			}
		});
    	selector.openDialog("single");
    });
}

function saveTypeDict(saveAndAdd){
	if($.validInput("hidParentDataDictId", "父条目", true)){
		return false;
	}
	if($.validInput("dataDictKey", "名称", true)){
		return false;
	}
	if($.validInput("dataDictValue", "代码", true)){
		return false;
	}
	if($.validInput("description", "备注", false, null, 500)){
		return false;
	}
	
	var subSystemId = $.trim($("#curSubSystemId").val());
	var dataDictId = $.trim($("#hidDataDictId").val());
	var parentDataDictid = $.trim($("#hidParentDataDictId").val());
	var dictKey = $.trim($("#dataDictKey").val());
	var dictValue = $.trim($("#dataDictValue").val());
	var ceilValue = $.trim($("#dataDictMaxValue").val());
	var floorValue = $.trim($("#dataDictMinValue").val());
    var description = $.trim($("#description").val());
	var action = $("#opType").val();

	$.ajax({
		type : "POST",
		cache: false,
		url  : "m/datadict/" + action,
		dataType : "json",
		data : {
			subSystemID : subSystemId,
			dataDictID : dataDictId,
			parentDataDictID : parentDataDictid,
			dictName : dictKey,
			dictCode : dictValue,
			ceilValue : ceilValue,
			floorValue : floorValue,
            description : description
		},
		success : function(data){
			if ($.checkErrorMsg(data) ) {
				$.alert("保存子模块成功！");
				var newID= data.msg.text;
                if (action == "add") {
//                    var parentNode = $("#" + parentDataDictid, "#systemDicTree");
//                    childDataDictsTree.addNodes(parentNode, "<li id=\"" + data.msg.text + "\" dataDictId=\"" + data.msg.text + "\">" 
//                    		+ "<span dataDictId=" + data.msg.text + " class='text'>" + dictKey + "</span></li>");
                    childDataDictsTree.addNode(parentDataDictid, newID, dictKey, {dataDictId :  newID});
                	//判断是否进行下一步的新增操作
	                if(saveAndAdd){
	                    $("#addDataDict").removeAttr("disabled").click();
	                }else {
	                    //模拟树节点的点击事件
	                    childDataDictsTree.clickNode(newID);
	                }
                } else {
                	if ($("#hidParentDataDictIdOld").val() != parentDataDictid){
                		//刷新整个树
                		$("#subSystemList div > input[value='" + $("#curSubSystemId").val() + "']").parent().click();
                	} else {
                		//修改当前结点属性与名称，并点击
//                		$("#" + dataDictId).attr({dataDictId : dataDictId})
//                    		.find(">span").attr({dataDictId : dataDictId}).text(dictKey).click();
                    	childDataDictsTree.modifyNode(dataDictId, dictKey,{dataDictId : dataDictId});
                    	childDataDictsTree.clickNode(dataDictId);
                	}
                }
			}
		}
	});
}