var listTree= null;

$(initListProdType);

//初始化页面
function initListProdType(){
	//添加权限约束
	$.handleRights({
		"addSubProdType" : $.SysConstants.ADD,
	    "editProdType" : $.SysConstants.MODIFY,
		"delProdType" : $.SysConstants.DELETE,
		"sortSub" : $.SysConstants.MODIFY
	});
	
    //初始化树
	initTree("");
    
    //绑定添加
    $('#addSubProdType').click(function(){
        $('#opType').val('add');
        var prodTypeID = $("#curProdTypeID").val();
        loadModule("initadd",prodTypeID);
         //如果下级是异步且未加载，而触发加载
        if (prodTypeID && prodTypeID != "" && $("li[prodTypeID=\"" + prodTypeID + "\"] > ul.ajax").length != 0) {
            $("li[prodTypeID=\"" + prodTypeID + "\"] > img").click();
        };
    });
    
    //绑定修改    
    $('#editProdType').click(function(){
        $('#opType').val('modify');
        var id = $("#curProdTypeID").val();
        loadModule("initmodify",id);
    });

    //绑定删除
    $('#delProdType').click(function(){
    	$.confirm("是否删除该产品分类?", function(r){
			if (r) {
				var prodTypeID = $("#curProdTypeID").val();
		        $.ajax({
		            type : "POST",
		            cache: false,
		            async : true,
		            url  : "m/prod_type/delete",
		            dataType : "json",
		            data : {prodTypeID : prodTypeID},
		            success : function(data){
		            	if ($.checkErrorMsg(data) ) {
//		            		$.alert("删除成功！");
		                    
		                    $("#allTool").hide();
		                    $("#moduleMain").html("<div class=\"czbks\">&nbsp;</div>");
		                    
		                     //删除节点
		                    listTree.removeNode($('li[prodTypeID='+prodTypeID+']'));
		            	}
		            }
		        });
			}
		});
    });
    
    //绑定排序
    $('#sortSub').click(function(){
    	var prodTypeID = $("#curProdTypeID").val();
    	
        $('#opType').val('initorder');
        loadModule("initorder",prodTypeID);
    });
}

//载入群组树
function initTree(value){
	$.ajax({
		type : "POST",
		cache: false, 
        async : true,
		url  : "m/prod_type/subProdTypes",
		dataType : "json",
		data : {type : value},
		success : function(data){
	        $("#tree > li > ul").html(data.htmlValue);
	        listTree = $("#tree").simpleTree({
	        	animate: true,
	        	autoclose:false,
	        	selectRoot : true,
	        	json: true,
	        	afterClick: clickTree
	        })[0];
		}
	});
}

//选中树节点
function clickTree(o){
    var prodTypeID = $(o).attr("prodTypeID");
    
    if ($.trim(prodTypeID) != ""){
        var p = $("#tree")[0];
        if($(o).offset().top < $(p).offset().top){
            p.scrollTop = p.scrollTop - ($(p).offset().top - $(o).offset().top) - (p.clientHeight / 2);
        }else if($(o).offset().top + $(o).height() > $(p).offset().top + $(p).height()){
            p.scrollTop = p.scrollHeight - ($(o).offset().top + p.scrollTop - $(p).offset().top - p.clientHeight) + (p.clientHeight / 2);
        }
        $('#opType').val('view');
        loadModule("view", prodTypeID);
    }else{
        //清空
        $('#titlName').html("");
        $('#curProdTypeID').val("");
        $('#curProdType').val("");
        //显示工具栏
        $('#allTool').show();
        $("#addSubProdType").removeAttr("disabled").removeClass("icoNone");
        $("#editProdType,#delProdType").attr("disabled","true").addClass("icoNone");      
        $("#moduleMain").html("<div class=\"czbks\">&nbsp;</div>");
    }
}

//载入模块
function loadModule(servletAction,prodTypeID){
    $.ajax({
        type : "POST",
        cache: false,
        url  : "m/prod_type/" + servletAction,
        data : {prodTypeID : prodTypeID?prodTypeID:""},
        success : function(html){
        	if (html.charAt(0) =='{') {//返回JSON数据，说明已超时
		        	top.location.reload();
			} 
            $("#moduleMain")[0].innerHTML = html;
            $("#moduleMain script").each(
                function(){
                    $(this).appendTo("#moduleMain");
                }
            );
        }
    });
}