$(initModuleSort);

function initModuleSort(){
    //设置工具栏的状态为禁用状态
    if ($("#curModuleId").val() != "") {
    	$("#sortSubModule").attr("disabled","true").addClass("icoNone");
    	$("#addSubModule,#modifyModule,#delModule,#bindModuleAction").removeAttr("disabled","true").removeClass("icoNone");     
    }else {
    	$("#sortSubModule,#modifyModule,#delModule,#bindModuleAction").attr("disabled","true").addClass("icoNone");
    	$("#addSubModule").removeAttr("disabled","true").removeClass("icoNone");
    }
    //建立副本,以备重置使用
    $("#childModules").clone().attr("id","childModulesBak").insertAfter("#childModules").hide();
	
	//重置按钮事件
    $("#reset").click(function(e){
             //注：FF在进行副本的拷贝时，也会拷贝注释，这将会发生错误
             $("#childModules").html($("#childModulesBak").html());
             $("#setTop,#setUp,#setDown,#setBottom,#saveOrder,#reset").attr("disabled", "true").addClass("icoNone");
         }
     );
     
	$("#childModules").change(
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
    var topVal=$('option','#childModules').eq(0).val();       
    $('option:selected','#childModules').each(function(){
        $(this).insertBefore($('option[value=\''+topVal+'\']','#childModules'));
    });
    //设置按钮权限
    $("#setDown,#setBottom,#saveOrder,#reset").removeAttr("disabled").removeClass("icoNone");           
    $("#setTop,#setUp").attr("disabled","true").addClass("icoNone");
}

//设置到底部
function setBottom(){
    $('option:selected','#childModules').each(function(){
        $(this).appendTo('#childModules');
    });
    //设置按钮权限    
    $("#setTop,#setUp,#saveOrder,#reset").removeAttr("disabled").removeClass("icoNone");     
    $("#setDown,#setBottom").attr("disabled","true").addClass("icoNone");   
}

//设置上移
function setUp(){
    //取得选中的顶
    var selectTopVal=$('option:selected','#childModules').eq(0).val();
    var preIndex=0;
    //搜索要插入的位置
    $('option','#childModules').each(function(index){
        if ($(this).val() == selectTopVal){
            preIndex=index-1;
            return false;
        }
    });
    var preVal=$('option','#childModules').eq(preIndex).val();
    //逐个插入
    $('option:selected','#childModules').each(function(){
        $(this).insertBefore($('option[value=\''+preVal+'\']','#childModules'));
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
    var selectDownVal=$('option:selected','#childModules').eq($('option:selected','#childModules').length-1).val();
    var nextIndex=0;
    //搜索插入位置
    $('option','#childModules').each(function(index){
        if ($(this).val() == selectDownVal){
            nextIndex=index+1;
            return false;
        }
    });
    var nextVal=$('option','#childModules').eq(nextIndex).val();
    //逐个插入
    $('option:selected','#childModules').each(function(){
        $(this).insertAfter($('option[value=\''+nextVal+'\']','#childModules'));
        nextVal=$(this).val();
    });
    //判断是否到底,控制按钮
    if (nextIndex == ($('option','#childModules').length-1)){
        $("#setDown,#setBottom").attr("disabled","true").addClass("icoNone");
    }else{
        $("#setDown,#setBottom").removeAttr("disabled").removeClass("icoNone");
    }   
    $("#setTop,#setUp,#saveOrder,#reset").removeAttr("disabled").removeClass("icoNone");          
}

//保存排序
function saveOrder(){
    var opModules = "";        
    $("#childModules > option").each(
        function(){
            opModules += this.value + ",";
        }
    );
    //去掉最后的,号
    opModules = opModules.substr(0,opModules.length - 1);
    
    $.ajax({
        type : "POST",
        cache: false,
        url  : "m/module/order",
        dataType : "json",
        data : {orderIDs : opModules},
        success : function(data){
        	if ($.checkErrorMsg(data) ) {
        		$.alert('保存排序成功!');
        		var parentModuleId = $("#curModuleId").val();
        		
        		if (parentModuleId != "") {
			     	childModulesTree.reloadNodes(parentModuleId, "m/module/moduletree?moduleID=" + parentModuleId);
		        } else {
		        //	$("#subSystemList div > input[value='" + $("#curSubSystemId").val() + "']").parent().click();
		        	loadTree($("#curSubSystemId").val());
		        }
        	}
        }
    });     
}