$(initDataDictionarySort);

function initDataDictionarySort(){
    //设置工具栏的状态为禁用状态
    if ($("#curDataDictId").val() != "") {
    	$("#sortDataDict").attr("disabled","true").addClass("icoNone");
    	$("#addDataDict,#addDataType,#modifyDataDict,#deleteDataDict").removeAttr("disabled","true").removeClass("icoNone");
    }else {
    	$("#addDataDict,#sortDataDict,#modifyDataDict,#deleteDataDict").attr("disabled","true").addClass("icoNone");
    	$("#addDataType").removeAttr("disabled","true").removeClass("icoNone");
    }

    //建立副本,以备重置使用
    $("#childDataDicts").clone().attr("id","childDataDictsBak").insertAfter("#childDataDicts").hide();
    
    //重置按钮事件
    $("#reset").click(function(e){
             //注：FF在进行副本的拷贝时，也会拷贝注释，这将会发生错误
             $("#childDataDicts").html($("#childDataDictsBak").html());
             $("#setTop,#setUp,#setDown,#setBottom,#saveOrder,#reset").attr("disabled", "true").addClass("icoNone");
         }
     );
     
	$("#childDataDicts").change(
        function(e){
            $("#setTop,#setUp,#setDown,#setBottom").removeAttr("disabled").removeClass("icoNone");
            
            if(this.selectedIndex == 0){
                $("#setTop,#setUp").attr("disabled","true").addClass("icoNone");
            }
            if(this.selectedIndex == this.length - 1){
                $("#setDown,#setBottom").attr("disabled","true").addClass("icoNone");
            } 
        }
    );
        
	$('#setTop').click(function(){
        setTop();
    });
    $('#setUp').click(function(){
        setUp();
    });
    $('#setDown').click(function(){
        setDown();
    });
    $('#setBottom').click(function(){
        setBottom();
    });     
    $('#saveOrder').click(function(){
        saveOrder();
    });       
    $("#setTop,#setUp,#setDown,#setBottom,#saveOrder,#reset").attr("disabled","true").addClass("icoNone");
}    

//设置到顶部
function setTop(){
    var topVal=$('option','#childDataDicts').eq(0).val();       
    $('option:selected','#childDataDicts').each(function(){
        $(this).insertBefore($('option[value=\''+topVal+'\']','#childDataDicts'));
    });
    //设置按钮权限
    $("#setDown,#setBottom,#saveOrder,#reset").removeAttr("disabled").removeClass("icoNone");           
    $("#setTop,#setUp").attr("disabled","true").addClass("icoNone");
}

//设置到底部
function setBottom(){
    $('option:selected','#childDataDicts').each(function(){
        $(this).appendTo('#childDataDicts');
    });
    //设置按钮权限    
    $("#setTop,#setUp,#saveOrder,#reset").removeAttr("disabled").removeClass("icoNone");     
    $("#setDown,#setBottom").attr("disabled","true").addClass("icoNone");   
}

//设置上移
function setUp(){
    //取得选中的顶
    var selectTopVal=$('option:selected','#childDataDicts').eq(0).val();
    var preIndex=0;
    //搜索要插入的位置
    $('option','#childDataDicts').each(function(index){
        if ($(this).val() == selectTopVal){
            preIndex=index-1;
            return false;
        }
    });
    var preVal=$('option','#childDataDicts').eq(preIndex).val();
    //逐个插入
    $('option:selected','#childDataDicts').each(function(){
        $(this).insertBefore($('option[value=\''+preVal+'\']','#childDataDicts'));
    }); 
    //判断是否到顶,控制按钮
    if (preIndex == 0){
        $("#setTop,#setUp").attr("disabled","true").addClass("icoNone");
    }else{
        $("#setTop,#setUp").removeAttr("disabled").removeClass("icoNone");    
    }  
    $("#setDown,#setBottom,#saveOrder,#reset").removeAttr("disabled").removeClass("icoNone");        
}

//设置下移
function setDown(){
    //取得选中底部
    var selectDownVal=$('option:selected','#childDataDicts').eq($('option:selected','#childDataDicts').length-1).val();
    var nextIndex=0;
    //搜索插入位置
    $('option','#childDataDicts').each(function(index){
        if ($(this).val() == selectDownVal){
            nextIndex=index+1;
            return false;
        }
    });
    var nextVal=$('option','#childDataDicts').eq(nextIndex).val();
    //逐个插入
    $('option:selected','#childDataDicts').each(function(){
        $(this).insertAfter($('option[value=\''+nextVal+'\']','#childDataDicts'));
        nextVal=$(this).val();
    });
    //判断是否到底,控制按钮
    if (nextIndex == ($('option','#childDataDicts').length-1)){
        $("#setDown,#setBottom").attr("disabled","true").addClass("icoNone");
    }else{
        $("#setDown,#setBottom").removeAttr("disabled").removeClass("icoNone");
    }   
    $("#setTop,#setUp,#saveOrder,#reset").removeAttr("disabled").removeClass("icoNone");          
}

//保存排序
function saveOrder(){
	var opDataDicts = "";
	$("#childDataDicts > option").each(
		function(){
       		opDataDicts += this.value + ",";
		}
	);
	//去掉最后的,号
	opDataDicts = opDataDicts.substr(0,opDataDicts.length - 1);
    
    $.ajax({
        type : "POST",
        cache: false,
        url  : "m/datadict/order",
        dataType : "json",
        data : {orderIDs : opDataDicts},
        success : function(data){
        	if ($.checkErrorMsg(data) ) {
        		$.alert('保存排序成功!');
        		var parentDataDictId = $("#curDataDictId").val();
		    	if (parentDataDictId != ""){
			     	childDataDictsTree.reloadNodes(parentDataDictId, "m/datadict/expanddatadict?dataDictID=" + parentDataDictId);
		        } else {
		        //	$("#subSystemList div > input[value='" + $("#curSubSystemId").val() + "']").parent().click();
		        	loadTree($("#curSubSystemId").val());
		        }
        	}
        }
    });     
}