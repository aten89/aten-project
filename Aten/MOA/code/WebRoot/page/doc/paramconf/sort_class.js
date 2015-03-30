var mainFrame = $.getMainFrame();

$(initForm);
function initForm(){

    /*刷新*/
	$("#opRefresh").click(
        function(){
            window.location.reload(true);
        }
    );
    
    initDocClassesList();
    
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

//初始化下级列表
function initDocClassesList(){
    $('#docClasses').empty();
    $.ajax({
        type : "POST",
        cache: false,
        url  : "m/doc_class",
        data : "act=findall",
        success : initSubSuccess,
        error : $.ermpAjaxError
    }); 
}

function initSubSuccess(xml){
    //解析XML中的返回代码
    var messageCode = $("message",xml).attr("code");
    var StrHtml = "";
    if(messageCode == "1"){
        $(xml).find("doc-class").each(function(index){
            StrHtml+="<option value=\"" + $(this).attr("id") + "\">" + $(this).find("name").text() + "</option>";
        });
        $("#docClasses").html(StrHtml);
	    //建立副本,以备重置使用
	    $("#docClasses").clone().attr("id","docClassesBak").insertAfter("#docClasses").hide();        
        //列表发生改变事件，则对应的按钮发生相应的可用状态变化
        $("#docClasses").change(
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
                $("#docClasses").html($("#docClassesBak").html());
                $("#setTop,#setUp,#setDown,#setBottom,#saveOrder,#reset").removeAttr("disabled").removeClass("icoNone");
            }
        );
    }else {
        $.alert($("message",xml).text());     
    } 
}


//设置到顶部
function setTop(){
    var topVal=$('option','#docClasses').eq(0).val();       
    $('option:selected','#docClasses').each(function(){
        $(this).insertBefore($('option[value=\''+topVal+'\']','#docClasses'));
    });
    //设置按钮权限
    $("#setDown,#setBottom,#saveOrder,#reset").removeAttr("disabled").removeClass("icoNone");           
    $("#setTop,#setUp").attr("disabled","true").addClass("icoNone");
}

//设置到底部
function setBottom(){
    $('option:selected','#docClasses').each(function(){
        $(this).appendTo('#docClasses');
    });
    //设置按钮权限    
    $("#setTop,#setUp,#saveOrder,#reset").removeAttr("disabled").removeClass("icoNone");     
    $("#setDown,#setBottom").attr("disabled","true").addClass("icoNone");   
}

//设置上移
function setUp(){
    //取得选中的顶
    var selectTopVal=$('option:selected','#docClasses').eq(0).val();
    var preIndex=0;
    //搜索要插入的位置
    $('option','#docClasses').each(function(index){
        if ($(this).val() == selectTopVal){
            preIndex=index-1;
            return false;
        }
    });
    var preVal=$('option','#docClasses').eq(preIndex).val();
    //逐个插入
    $('option:selected','#docClasses').each(function(){
        $(this).insertBefore($('option[value=\''+preVal+'\']','#docClasses'));
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
    var selectDownVal=$('option:selected','#docClasses').eq($('option:selected','#docClasses').length-1).val();
    var nextIndex=0;
    //搜索插入位置
    $('option','#docClasses').each(function(index){
        if ($(this).val() == selectDownVal){
            nextIndex=index+1;
            return false;
        }
    });
    var nextVal=$('option','#docClasses').eq(nextIndex).val();
    //逐个插入
    $('option:selected','#docClasses').each(function(){
        $(this).insertAfter($('option[value=\''+nextVal+'\']','#docClasses'));
        nextVal=$(this).val();
    });
    //判断是否到底,控制按钮
    if (nextIndex == ($('option','#docClasses').length-1)){
        $("#setDown,#setBottom").attr("disabled","true").addClass("icoNone");
    }else{
        $("#setDown,#setBottom").removeAttr("disabled").removeClass("icoNone");
    }   
    $("#setTop,#setUp,#saveOrder,#reset").removeAttr("disabled").removeClass("icoNone");          
}

function saveOrder(){
	var ordIdList="";
    $('option','#docClasses').each(function(){
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
		url  : "m/doc_class",
		data : "act=order&orderids=" + ordIdList,
   		success : saveSuccess,
		error : $.ermpAjaxError
	});
};

//保存成功回调函数
function saveSuccess(xml){
    var messageCode = $("message",xml).attr("code");
    var messageInfo = $("message",xml).text();
	
    if(messageCode == "1"){
    	$.alert("保存成功");
    	//刷新父列表
        mainFrame.getCurrentTab().doCallback();
    }else{
    	$.alert(messageInfo);
    };
};
