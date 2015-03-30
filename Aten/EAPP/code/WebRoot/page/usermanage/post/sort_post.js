$(initSortPost);

function initSortPost(){ 
    if ($("#curPostId").val() != "") {  
    	$("#sortSub").attr("disabled","true").addClass("icoNone");
    	$("#addSubPost,#editPost,#delPost,#bindRole,#bindPerson").removeAttr("disabled","true").removeClass("icoNone");    
    } else {
    	$("#sortSub,#editPost,#delPost,#bindRole,#bindPerson").attr("disabled","true").addClass("icoNone");
    	$("#addSubPost").removeAttr("disabled","true").removeClass("icoNone");
    }  
    initSubPostList();
    
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
function initSubPostList(){
    $('#subPost').empty();
    $.ajax({
        type : "POST",
        cache: false,
        url  : "m/post/xmlsubposts",
        dataType : "json",
        data : "postID=" + $('#curPostId').val(),
        success : initSubSuccess
    }); 
}

function initSubSuccess(data){
	if ($.checkErrorMsg(data) ) {
		var dataList = data.posts;
		if (dataList) {
			var StrHtml = "";
			$(dataList).each(function(i) {
				StrHtml+="<option value=\"" + dataList[i].postID + "\">" + dataList[i].postName + "</option>";
			});
			$("#subPost").html(StrHtml);
		    //建立副本,以备重置使用
		    $("#subPost").clone().attr("id","subPostBak").insertAfter("#subPost").hide();        
	        //列表发生改变事件，则对应的按钮发生相应的可用状态变化
	        $("#subPost").change(
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
	                $("#subPost").html($("#subPostBak").html());
	                $("#setTop,#setUp,#setDown,#setBottom,#saveOrder,#reset").attr("disabled", "true").addClass("icoNone");
	            }
	        );
		}
	}
}

//设置到顶部
function setTop(){
    var topVal=$('option','#subPost').eq(0).val();       
    $('option:selected','#subPost').each(function(){
        $(this).insertBefore($('option[value=\''+topVal+'\']','#subPost'));
    });
    //设置按钮权限
    $("#setDown,#setBottom,#saveOrder,#reset").removeAttr("disabled").removeClass("icoNone");           
    $("#setTop,#setUp").attr("disabled","true").addClass("icoNone");
}

//设置到底部
function setBottom(){
    $('option:selected','#subPost').each(function(){
        $(this).appendTo('#subPost');
    });
    //设置按钮权限    
    $("#setTop,#setUp,#saveOrder,#reset").removeAttr("disabled").removeClass("icoNone");     
    $("#setDown,#setBottom").attr("disabled","true").addClass("icoNone");   
}

//设置上移
function setUp(){
    //取得选中的顶
    var selectTopVal=$('option:selected','#subPost').eq(0).val();
    var preIndex=0;
    //搜索要插入的位置
    $('option','#subPost').each(function(index){
        if ($(this).val() == selectTopVal){
            preIndex=index-1;
            return false;
        }
    });
    var preVal=$('option','#subPost').eq(preIndex).val();
    //逐个插入
    $('option:selected','#subPost').each(function(){
        $(this).insertBefore($('option[value=\''+preVal+'\']','#subPost'));
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
    var selectDownVal=$('option:selected','#subPost').eq($('option:selected','#subPost').length-1).val();
    var nextIndex=0;
    //搜索插入位置
    $('option','#subPost').each(function(index){
        if ($(this).val() == selectDownVal){
            nextIndex=index+1;
            return false;
        }
    });
    var nextVal=$('option','#subPost').eq(nextIndex).val();
    //逐个插入
    $('option:selected','#subPost').each(function(){
        $(this).insertAfter($('option[value=\''+nextVal+'\']','#subPost'));
        nextVal=$(this).val();
    });
    //判断是否到底,控制按钮
    if (nextIndex == ($('option','#subPost').length-1)){
        $("#setDown,#setBottom").attr("disabled","true").addClass("icoNone");
    }else{
        $("#setDown,#setBottom").removeAttr("disabled").removeClass("icoNone");
    }   
    $("#setTop,#setUp,#saveOrder,#reset").removeAttr("disabled").removeClass("icoNone");          
}

//保存排序
function saveOrder(){
    var ordIdList="";
    $('option','#subPost').each(function(){
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
        url  : "m/post/order",
        dataType : "json",
        data : {orderIDs : ordIdList,
        	postID : $('#curPostId').val()
        },
        success : function(data){
        	if ($.checkErrorMsg(data) ) {
        		$.alert('保存排序成功!');
			    listPostTree.reloadNodes($('#curPostId').val(), "m/post/subposts?postID=" + $('#curPostId').val());  
        	}
        }
    });     
}
