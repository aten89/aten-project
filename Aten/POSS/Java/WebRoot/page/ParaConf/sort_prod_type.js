$(initSortProdType);

function initSortProdType(){ 
    if ($("#curProdTypeID").val() == "") {
    	$("#sortSub,#editProdType,#delProdType").attr("disabled","true").addClass("icoNone");
    	$("#addSubProdType").removeAttr("disabled","true").removeClass("icoNone");
    } else {
    	$("#sortSub").attr("disabled","true").addClass("icoNone");
    	$("#addSubProdType,#editProdType,#delProdType").removeAttr("disabled","true").removeClass("icoNone");
    }
    initSubProdTypeList();
    
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
function initSubProdTypeList(){
    $('#subProdType').empty();
    $.ajax({
        type : "POST",
        cache: false,
        url  : "m/prod_type/xmlsubProdTypes",
        dataType : "json",
        data : "prodTypeID=" + $('#curProdTypeID').val(),
        success : initSubSuccess
    }); 
}

function initSubSuccess(data){
	if ($.checkErrorMsg(data) ) {
		var dataList = data.prodTypeEntitys;
		if (dataList) {
			var StrHtml = "";
			$(dataList).each(function(i) {
				StrHtml+="<option value=\"" + dataList[i].id + "\">" + dataList[i].prodType + "</option>";
			});
			$("#subProdType").html(StrHtml);
		    //建立副本,以备重置使用
		    $("#subProdType").clone().attr("id","subProdTypeBak").insertAfter("#subProdType").hide();   
	        //列表发生改变事件，则对应的按钮发生相应的可用状态变化
	        $("#subProdType").change(
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
	                $("#subProdType").html($("#subProdTypeBak").html());
	                $("#setTop,#setUp,#setDown,#setBottom,#saveOrder,#reset").attr("disabled", "true").addClass("icoNone");
	            }
	        );
		}
	}
}

//设置到顶部
function setTop(){
    var topVal=$('option','#subProdType').eq(0).val();       
    $('option:selected','#subProdType').each(function(){
        $(this).insertBefore($('option[value=\''+topVal+'\']','#subProdType'));
    });
    //设置按钮权限
    $("#setDown,#setBottom,#saveOrder,#reset").removeAttr("disabled").removeClass("icoNone");           
    $("#setTop,#setUp").attr("disabled","true").addClass("icoNone");
}

//设置到底部
function setBottom(){
    $('option:selected','#subProdType').each(function(){
        $(this).appendTo('#subProdType');
    });
    //设置按钮权限    
    $("#setTop,#setUp,#saveOrder,#reset").removeAttr("disabled").removeClass("icoNone");     
    $("#setDown,#setBottom").attr("disabled","true").addClass("icoNone");   
}

//设置上移
function setUp(){
    //取得选中的顶
    var selectTopVal=$('option:selected','#subProdType').eq(0).val();
    var preIndex=0;
    //搜索要插入的位置
    $('option','#subProdType').each(function(index){
        if ($(this).val() == selectTopVal){
            preIndex=index-1;
            return false;
        }
    });
    var preVal=$('option','#subProdType').eq(preIndex).val();
    //逐个插入
    $('option:selected','#subProdType').each(function(){
        $(this).insertBefore($('option[value=\''+preVal+'\']','#subProdType'));
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
    var selectDownVal=$('option:selected','#subProdType').eq($('option:selected','#subProdType').length-1).val();
    var nextIndex=0;
    //搜索插入位置
    $('option','#subProdType').each(function(index){
        if ($(this).val() == selectDownVal){
            nextIndex=index+1;
            return false;
        }
    });
    var nextVal=$('option','#subProdType').eq(nextIndex).val();
    //逐个插入
    $('option:selected','#subProdType').each(function(){
        $(this).insertAfter($('option[value=\''+nextVal+'\']','#subProdType'));
        nextVal=$(this).val();
    });
    //判断是否到底,控制按钮
    if (nextIndex == ($('option','#subProdType').length-1)){
        $("#setDown,#setBottom").attr("disabled","true").addClass("icoNone");
    }else{
        $("#setDown,#setBottom").removeAttr("disabled").removeClass("icoNone");
    }   
    $("#setTop,#setUp,#saveOrder,#reset").removeAttr("disabled").removeClass("icoNone");          
}

//保存排序
function saveOrder(){
    var ordIdList="";
    $('option','#subProdType').each(function(){
        if ($.trim(ordIdList) == ""){
            ordIdList=$(this).val();
        }
        else{
            ordIdList+=","+$(this).val();
        }        
    });
    
    var curProdTypeID = $('#curProdTypeID').val();
    
    $.ajax({
        type : "POST",
        cache: false,
        url  : "m/prod_type/order",
        dataType : "json",
        data : {orderIDs : ordIdList,
    		parentID : curProdTypeID
        },
        success : function(data){
        	if ($.checkErrorMsg(data) ) {
        		$.alert('保存排序成功!');
        		initTree("");
//			    var node = $("li[prodTypeID=\"" + curProdTypeID + "\"]");
//			    listGroupTree.reloadNodes(node, "m/prod_type/subProdTypes?prodTypeID=" + curProdTypeID);  
        	}
        }
    });
}