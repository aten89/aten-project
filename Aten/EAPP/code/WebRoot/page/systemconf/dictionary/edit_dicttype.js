$(initTypeDictionaryView);

function initTypeDictionaryView(){
    
    //取得操作类型
    var action = $("#opType").val();
    if($.trim(action) == ""){
        return;
    }
    
    switch(action){
        case "add" :             
            $("#addDataDict,#addDataType,#modifyDataDict,#deleteDataDict,#sortDataDict").attr("disabled","true").addClass("icoNone");
            
            $("#saveTypeDict,#resetTypeDict,#saveAddTypeDict").removeAttr("disabled").removeClass("icoNone");
            
            $("#dictType,#dictKey,#description").removeAttr("readonly");

	    	//从外层框架取默认值
	    	$("#subSystemName").text($("#curSubSystemName").val());
	    	$("#hidSubSystemId").val($("#curSubSystemId").val());
	    	
            bindEvent();
            break;
        case "modify" :
            $("#addDataDict,#addDataType,#deleteDataDict,#sortDataDict").removeAttr("disabled").removeClass("icoNone");
            $("#modifyDataDict").attr("disabled","true").addClass("icoNone");
            
            $("#saveTypeDict,#resetTypeDict").removeAttr("disabled").removeClass("icoNone");
            $("#saveAddTypeDict").attr("disabled","true").addClass("icoNone");
            
            $("#dictType,#dictKey,#description").removeAttr("readonly");
            
            bindEvent();     
            break;
        case "view" :
            $("#addDataDict,#addDataType,#modifyDataDict,#deleteDataDict,#sortDataDict").removeAttr("disabled").removeClass("icoNone");
            
            $("#saveTypeDict,#resetTypeDict,#saveAddTypeDict").remove();
            
            //点击都是时入查看页，查看状态时把相关字段保存到外导框架中
		    $('#titlName').html("— " + $("#dictKey").val());
		    $('#curDataDictId').val($('#hidDataDictId').val());
		    $('#curDataDictName').val($('#dictKey').val());
		    $('#curDataDictType').val($('#dictType').val());
		    
		    $("#dictType").parent().text($("#dictType").val());
 			$("#dictKey").parent().text($("#dictKey").val());
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
}    
    
function saveTypeDict(saveAndAdd){
	if($.validInput("dictType", "类型", true)){
		return false;
	}
	if($.validInput("dictKey", "名称", true)){
		return false;
	}
	if($.validInput("description", "备注", false, null, 500)){
		return false;
	}

	var dictType = $.trim($("#dictType").val());
    var dictKey = $.trim($("#dictKey").val());
    var subSystemId = $.trim($("#curSubSystemId").val());
    var description = $.trim($("#description").val());
    var dataDictId = $.trim($("#hidDataDictId").val());
	var action = $("#opType").val();
	$.ajax({
		type : "POST",
		cache: false,
		url  : "m/datadict/" + action,
		dataType : "json",
		data : {
            dictType : dictType,
            subSystemID : subSystemId,
            dataDictID : dataDictId,
            dictName : dictKey,
            description : description
		},
		success : function(data){
			if ($.checkErrorMsg(data) ) {
				$.alert("保存子模块成功！");
				var newID= data.msg.text;
                if (action == "add") {
//                    var parentNode = $("#" + subSystemId, "#systemDicTree");
//                    childDataDictsTree.addNodes(parentNode, "<li id=\"" + data.msg.text + "\" type=\"" + dictType + "\" dataDictId=\"" + data.msg.text + "\">" 
//                    		+ "<span dataDictId=" + data.msg.text + " type=\"" + dictType + "\" class='text'>" + dictKey + "</span></li>");
						childDataDictsTree.addNode("", newID, dictKey, {dataDictId :  newID, type:dictType});
                	//判断是否进行下一步的新增操作
	                if(saveAndAdd){
	                    $("#addDataType").removeAttr("disabled").click();
	                }else {
	                    //模拟树节点的点击事件
	                     childDataDictsTree.clickNode(newID);
	                }
                } else {
//                	$("#" + dataDictId).attr({dataDictId : dataDictId})
//                    		.find(">span").attr({dataDictId : dataDictId}).text(dictKey).click();
//                  	$("#" + dataDictId)[0].setAttribute("type",dictType);
//                   	$("#" + dataDictId).find(">span")[0].setAttribute("type",dictType);
                   	childDataDictsTree.modifyNode(dataDictId, dictKey,{dataDictId : dataDictId, type:dictType});
                   	childDataDictsTree.clickNode(dataDictId);
                }
			}
		}
	});
}