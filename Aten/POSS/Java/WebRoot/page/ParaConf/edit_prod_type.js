$(initProdTypeManage);

function initProdTypeManage(){ 
    
    //取得操作类型
    var action = $("#opType").val();
    if($.trim(action) == ""){
        return;
    }
    
	switch(action){
        case "add" :
            $("#addSubProdType,#editProdType,#delProdType,#sortSub").attr("disabled","true").addClass("icoNone");
            
            $("#saveProdType,#resetProdType,#saveAddProdType").removeAttr("disabled").removeClass("icoNone");
            
            $("#prodType").removeAttr("readonly");
            $("#remark").removeAttr("readonly");
            //默认值
            $("#hidParentID").val($("#curProdTypeID").val());
            
            //重写重置按扭
    		$("#resetProdType").click(function(){
    			document.groupFrm.reset();
            	$("#hidParentID").val($("#curProdTypeID").val());
    			return false;
    		});
            bindEvent();
            break;
        case "modify" :
            $("#addSubProdType,#delProdType,#sortSub").removeAttr("disabled").removeClass("icoNone");
            $("#editProdType").attr("disabled","true").addClass("icoNone");
            
            $("#saveProdType,#resetProdType").removeAttr("disabled").removeClass("icoNone");
            $("#saveAddProdType").attr("disabled","true").addClass("icoNone");
            
            $("#prodType").removeAttr("readonly");
            $("#remark").removeAttr("readonly");
            
            //重写重置按扭
    		$("#resetProdType").click(function(){
    			document.groupFrm.reset();
    			return false;
    		});
            bindEvent();
            break;
        case "view" :
            $("#addSubProdType,#editProdType,#delProdType,#sortSub").removeAttr("disabled").removeClass("icoNone");
            
            $("#saveProdType,#resetProdType,#saveAddProdType").remove();
            
            //标题
            $('#titlName').html("—" + $("#prodType").val());
            //保存页面值
            $("#curProdTypeID").val($("#hidProdTypeID").val());
            $("#curProdType").val($("#hidProdType").val());
            break;
    }

    //显示工具栏
    $('#allTool').show();
}

//绑定事件
function bindEvent(){
    $("#saveProdType").click(function(){
        	saveProdType(false);
        }
    );
    $("#saveAddProdType").click(function(){
        	saveProdType(true);
        }
    );
}

//保存
function saveProdType(saveAndAdd){
	if($.validInput("prodType", "名称", true)){
		return false;
	}
	if($.validInput("remark", "备注", false, null, 500)){
		return false;
	}
    
    var parentID = $.trim($('#hidParentID').val());
    var prodTypeID = $('#hidProdTypeID').val();
    var prodType = $.trim($('#prodType').val());
    var remark = $.trim($('#remark').val());
	
	var servletAction = $("#opType").val();
    $.ajax({
        type : "POST",
        cache: false,
        url  : "m/prod_type/"+servletAction,
        dataType : "json",
        data : {
    			parentID : parentID,
    			prodTypeID : prodTypeID,
    			prodType : prodType,
    			remark : remark},
        success : function(data){
        	if ($.checkErrorMsg(data) ) {
        		if(!saveAndAdd) {
        			$.alert("产品分类保存成功!");
        		}
            	
               //增加树节点
                if (servletAction == "add"){
                	var parentNode;
                    if (parentID != ""){
                    	parentNode = $("li[prodTypeID=\""+parentID+"\"]","#tree");
                    }else{
                    	parentNode = $(".root","#tree");
                    }
//                    listTree.addNodes(parentNode, "<li id=\"" + data.msg.text + "\" prodTypeID=\"" + data.msg.text + "\">" + "<span prodTypeID=\"" + data.msg.text + "\">" + prodType + "</span></li>");
                    listTree.addNode(parentNode, data.msg.text, prodType, {prodTypeID :  data.msg.text});
                    
                    //模拟树节点的点击事件,增加时用返回的编号
                    if(saveAndAdd){
                        $("#addSubProdType").removeAttr("disabled").click();
                    }else{
                        $("li[prodTypeID=\""+data.msg.text+"\"] > span","#tree").click();
                    }
                }else{
                    if ($("#hidOldParentID").val() != parentID){
	                    listGroupTree.removeNode($("li[prodTypeID="+prodTypeID+"]"));
	                    var node = $("li[prodTypeID=\"" + parentID + "\"]");              
	                    listGroupTree.reloadNodes(node, "m/prod_type/subProdTypes?prodTypeID=" + parentID);

	                    $('#curProdTypeID').val("");
	                    $('#curProdType').val("");
	                    $('#allTool').hide();
	                    $("#moduleMain").html("<div class=\"czbks\">&nbsp;</div>");

                    }else{
                        $("li[prodTypeID=\""+prodTypeID+"\"] > span","#tree").text(prodType);                 
                        $("li[prodTypeID=\""+prodTypeID+"\"] > span","#tree").click();
                    }                              
                }                       
                       		
        	}
        }
    }); 
}