$(initSortGroup);

function initSortGroup(){ 
    if ($("#curGroupId").val() != "") {  
    	$("#sortSub").attr("disabled","true").addClass("icoNone");
    	$("#addSubGroup,#editGroup,#delGroup,#bindRole,#bindPerson,#bindPost").removeAttr("disabled","true").removeClass("icoNone");    
    } else {
    	$("#sortSub,#editGroup,#delGroup,#bindRole,#bindPerson,#bindPost").attr("disabled","true").addClass("icoNone");
    	$("#addSubGroup").removeAttr("disabled","true").removeClass("icoNone");
    }  
    initSubGroupList();
    
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
function initSubGroupList(){
    $('#subGroup').empty();
    $.ajax({
        type : "POST",
        cache: false,
        url  : "m/rbac_group/xmlsubgroups",
        dataType : "json",
        data : "groupID=" + $('#curGroupId').val(),
        success : initSubSuccess
    }); 
}

function initSubSuccess(data){
	if ($.checkErrorMsg(data) ) {
		var dataList = data.groups;
		if (dataList) {
			var StrHtml = "";
			$(dataList).each(function(i) {
				StrHtml+="<option value=\"" + dataList[i].groupID + "\">" + dataList[i].groupName + "</option>";
			});
			$("#subGroup").html(StrHtml);
		    //建立副本,以备重置使用
		    $("#subGroup").clone().attr("id","subGroupBak").insertAfter("#subGroup").hide();        
	        //列表发生改变事件，则对应的按钮发生相应的可用状态变化
	        $("#subGroup").change(
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
	                $("#subGroup").html($("#subGroupBak").html());
	                $("#setTop,#setUp,#setDown,#setBottom,#saveOrder,#reset").attr("disabled", "true").addClass("icoNone");
	            }
	        );
		}
	}
}

//设置到顶部
function setTop(){
    var topVal=$('option','#subGroup').eq(0).val();       
    $('option:selected','#subGroup').each(function(){
        $(this).insertBefore($('option[value=\''+topVal+'\']','#subGroup'));
    });
    //设置按钮权限
    $("#setDown,#setBottom,#saveOrder,#reset").removeAttr("disabled").removeClass("icoNone");           
    $("#setTop,#setUp").attr("disabled","true").addClass("icoNone");
}

//设置到底部
function setBottom(){
    $('option:selected','#subGroup').each(function(){
        $(this).appendTo('#subGroup');
    });
    //设置按钮权限    
    $("#setTop,#setUp,#saveOrder,#reset").removeAttr("disabled").removeClass("icoNone");     
    $("#setDown,#setBottom").attr("disabled","true").addClass("icoNone");   
}

//设置上移
function setUp(){
    //取得选中的顶
    var selectTopVal=$('option:selected','#subGroup').eq(0).val();
    var preIndex=0;
    //搜索要插入的位置
    $('option','#subGroup').each(function(index){
        if ($(this).val() == selectTopVal){
            preIndex=index-1;
            return false;
        }
    });
    var preVal=$('option','#subGroup').eq(preIndex).val();
    //逐个插入
    $('option:selected','#subGroup').each(function(){
        $(this).insertBefore($('option[value=\''+preVal+'\']','#subGroup'));
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
    var selectDownVal=$('option:selected','#subGroup').eq($('option:selected','#subGroup').length-1).val();
    var nextIndex=0;
    //搜索插入位置
    $('option','#subGroup').each(function(index){
        if ($(this).val() == selectDownVal){
            nextIndex=index+1;
            return false;
        }
    });
    var nextVal=$('option','#subGroup').eq(nextIndex).val();
    //逐个插入
    $('option:selected','#subGroup').each(function(){
        $(this).insertAfter($('option[value=\''+nextVal+'\']','#subGroup'));
        nextVal=$(this).val();
    });
    //判断是否到底,控制按钮
    if (nextIndex == ($('option','#subGroup').length-1)){
        $("#setDown,#setBottom").attr("disabled","true").addClass("icoNone");
    }else{
        $("#setDown,#setBottom").removeAttr("disabled").removeClass("icoNone");
    }   
    $("#setTop,#setUp,#saveOrder,#reset").removeAttr("disabled").removeClass("icoNone");          
}

//保存排序
function saveOrder(){
    var ordIdList="";
    $('option','#subGroup').each(function(){
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
        url  : "m/rbac_group/order",
        dataType : "json",
        data : {orderIDs : ordIdList,
        	groupID : $('#curGroupId').val()
        },
        success : function(data){
        	if ($.checkErrorMsg(data) ) {
        		$.alert('保存排序成功!');
			    listGroupTree.reloadNodes($('#curGroupId').val() , "m/rbac_group/subgroups?groupID=" + $('#curGroupId').val());  
        	}
        }
    });     
}