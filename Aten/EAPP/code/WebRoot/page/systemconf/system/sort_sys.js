$(initForm);
function initForm(){

    /*刷新*/
	$("#opRefresh").click(
        function(){
            window.location.reload(true);
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
	//备份，重置用
	$("#sortedShortCut").clone().attr("id","sortedShortCutBak").insertAfter("#sortedShortCut").hide();
	$("#sortedShortCut").change(
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
     //重置按钮事件
     $("#reset").click(
         function(e){
             //注：FF在进行副本的拷贝时，也会拷贝注释，这将会发生错误
             $("#sortedShortCut").html($("#sortedShortCutBak").html());
             $("#setTop,#setUp,#setDown,#setBottom,#saveOrder,#reset").attr("disabled", "true").addClass("icoNone");
         }
     );
}


//设置到顶部
function setTop(){
    var topVal=$('option','#sortedShortCut').eq(0).val();       
    $('option:selected','#sortedShortCut').each(function(){
        $(this).insertBefore($('option[value=\''+topVal+'\']','#sortedShortCut'));
    });
    //设置按钮权限
    $("#setDown,#setBottom,#saveOrder,#reset").removeAttr("disabled").removeClass("icoNone");           
    $("#setTop,#setUp").attr("disabled","true").addClass("icoNone");
}

//设置到底部
function setBottom(){
    $('option:selected','#sortedShortCut').each(function(){
        $(this).appendTo('#sortedShortCut');
    });
    //设置按钮权限    
    $("#setTop,#setUp,#saveOrder,#reset").removeAttr("disabled").removeClass("icoNone");     
    $("#setDown,#setBottom").attr("disabled","true").addClass("icoNone");   
}

//设置上移
function setUp(){
    //取得选中的顶
    var selectTopVal=$('option:selected','#sortedShortCut').eq(0).val();
    var preIndex=0;
    //搜索要插入的位置
    $('option','#sortedShortCut').each(function(index){
        if ($(this).val() == selectTopVal){
            preIndex=index-1;
            return false;
        }
    });
    var preVal=$('option','#sortedShortCut').eq(preIndex).val();
    //逐个插入
    $('option:selected','#sortedShortCut').each(function(){
        $(this).insertBefore($('option[value=\''+preVal+'\']','#sortedShortCut'));
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
    var selectDownVal=$('option:selected','#sortedShortCut').eq($('option:selected','#sortedShortCut').length-1).val();
    var nextIndex=0;
    //搜索插入位置
    $('option','#sortedShortCut').each(function(index){
        if ($(this).val() == selectDownVal){
            nextIndex=index+1;
            return false;
        }
    });
    var nextVal=$('option','#sortedShortCut').eq(nextIndex).val();
    //逐个插入
    $('option:selected','#sortedShortCut').each(function(){
        $(this).insertAfter($('option[value=\''+nextVal+'\']','#sortedShortCut'));
        nextVal=$(this).val();
    });
    //判断是否到底,控制按钮
    if (nextIndex == ($('option','#sortedShortCut').length-1)){
        $("#setDown,#setBottom").attr("disabled","true").addClass("icoNone");
    }else{
        $("#setDown,#setBottom").removeAttr("disabled").removeClass("icoNone");
    }   
    $("#setTop,#setUp,#saveOrder,#reset").removeAttr("disabled").removeClass("icoNone");          
}

function saveOrder(){
	var ordIdList="";
    $('option','#sortedShortCut').each(function(){
        if ($.trim(ordIdList) == ""){
            ordIdList=$(this).val();
        }
        else{
            ordIdList+=","+$(this).val();
        }        
    });
	$.ajax({
		type : "POST",
		cache: false,
		url  : "m/subsystem/order",
		dataType : "json",
		data : {orderIDs : ordIdList},
   		success : function(data){
			if ($.checkErrorMsg(data) ) {
				$.alert("保存成功");
		    	//刷新父列表
		        $.getMainFrame().getCurrentTab().doCallback();
			}
   		}
	});
}