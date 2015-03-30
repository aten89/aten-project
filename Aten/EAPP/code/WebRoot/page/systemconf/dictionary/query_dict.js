//左边的树对象
var childDataDictsTree = null;

var subsystemSelector = null;
$(initDataDictionaryList);

function initDataDictionaryList(){
    //$.getMainFrame().getCurrentTab().setTitle("管理-数据字典", "数据字典管理");
    //Role Check
//    $.handleRights(
//        {
//            "addDataDict" : $.SysConstants.ADD,
//            "addDataType" : $.SysConstants.ADD,
//            "modifyDataDict" : $.SysConstants.MODIFY,
//            "deleteDataDict" : $.SysConstants.DELETE,
//            "sortDataDict" : $.SysConstants.ORDER,
//            "search" : $.SysConstants.QUERY
//        }
//    );
    
    
    //高度设置
//	$("#contentMain").css("height",document.documentElement.clientHeight - 60);
//    $("#tdScaleMove").css("height",document.documentElement.clientHeight - 50);
//	$("#searchResults").css("height",document.documentElement.clientHeight - 337);
	
    //初始化子系统列表树
    subsystemSelector = $("#subSystemList").ySelect({width : 157, json:true ,url: "m/subsystem/allsystem",onChange : subSystemList_change});
    
    $("#search").click(function(){
        searchDataDict();
    });
    
    //回车搜索
    $.EnterKeyPress($("#searchDataDictName"),$("#search"));
    
    
    //新增子条目
    $("#addDataDict").click(function(e){
 		$('#opType').val('add');
 	//	var subSystemId = $("#curSubSystemId").val();
		var dataDictId = $("#curDataDictId").val();
		loadModule("initadd",dataDictId);
		 //展开左边的树
     	if (dataDictId && dataDictId != "" && $("#" + dataDictId + " > ul.ajax").length != 0) {
     		$("#" + dataDictId + " > img").click();
     	}
    });
    
    $("#addDataType").click(function(e){
    	$('#opType').val('add');
		loadModule("initadd");
	});
    
    //修改
    $("#modifyDataDict").click(function(e){
    	$('#opType').val('modify');
    	var dataDictId = $("#curDataDictId").val();
		loadModule("initmodify",dataDictId);
	});
    
    //删除
    $("#deleteDataDict").click(function(e){
    	$.confirm("是否删除本条目？", function(r){
			if (r) {
				var dataDictId = $("#curDataDictId").val();
				$.ajax({
					type : "POST",
					cache: false, 
			        async : true,
					url  : "m/datadict/delete",
					dataType : "json",
					data : {dataDictID : dataDictId},
					success : function(data){
		        		if ($.checkErrorMsg(data) ) {
							//	$.alert("删除成功！");
			                      
		             		$("#manageToolBar").hide();
		             		$("#dataDictMain").html("<div class=\"czbks\">&nbsp;</div>");
		                      
		               		//删除节点
		              		childDataDictsTree.removeNode(dataDictId);
		        		}
					}
				});
			}
		});
	});
    
    //排序
    $("#sortDataDict").click(function(e){
    	$('#opType').val('initorder');
        var subSystemId = $("#curSubSystemId").val();
        var dataDictId = $("#curDataDictId").val();
        loadModule("initorder",dataDictId,subSystemId);  
	});
    
};

//子系统列表发生改变的回调函数
function subSystemList_change(value, text){
    $("#searchResults").empty();
    $("#curSubSystemId").val(value);
    $("#curSubSystemName").val(text);
     //清空值
   	$('#titlName').html("");
  	$('#curDataDictId').val("");
 	$('#curDataDictName').val("");
 	$('#curDataDictType').val("");
 	
 	$("#manageToolBar").hide();        
	$("#dataDictMain").html("<div class=\"czbks\">&nbsp;</div>");
    //加载子系统的数据字典树列表
    loadTree(value);
}

function loadTree(systemID) {
	$.ajax({
		type : "POST",
		cache: false, 
        async : true,
		url  : "m/datadict/expandsubsystem",
		dataType : "json",
		data : {subSystemID : systemID},
		success : function(data){
	        $("#systemDicTree > ul").html(data.htmlValue);
	        childDataDictsTree = $("#systemDicTree > ul").simpleTree({
	        	animate: true,
	        	autoclose:false,
	        	selectRoot : true,
	        	json: true,
	        	afterClick : systemDicTree_click})[0];
		}
	});
}

function systemDicTree_click(o){
	childDataDictsTree.expandNode(o);//点击时展开下级
    //var dataDictId = $(">span",o).attr("dataDictId");
    var dataDictId = $(o).attr("dataDictId");
    
    //不存在时，即为树根
    if($.trim(dataDictId) != ""){
    	var p = $("#systemDicTree > ul")[0];
        if ($(o).offset().top < $(p).offset().top) {
            p.scrollTop = p.scrollTop - ($(p).offset().top - $(o).offset().top) - (p.clientHeight / 2);
        } else if ($(o).offset().top + $(o).height() > $(p).offset().top + $(p).height()) {
        	p.scrollTop = p.scrollHeight - ($(o).offset().top + p.scrollTop - $(p).offset().top - p.clientHeight) + (p.clientHeight / 2);
   		}
    	$('#opType').val('view');
		loadModule("view", dataDictId);
    } else {
    	//清空
        $('#titlName').html("");
        $('#curDataDictId').val("");
        $('#curDataDictName').val("");
        $('#curDataDictType').val("");
        //显示工具栏
    	$("#manageToolBar").show();
     	$("#addDataType,#sortDataDict").removeAttr("disabled").removeClass("icoNone");
    	$("#addDataDict,#modifyDataDict,#deleteDataDict").attr("disabled","true").addClass("icoNone");
    	$("#dataDictMain").html("<div class=\"czbks\">&nbsp;</div>");
    }
}

function searchDataDict(){
	if($.validInput("curSubSystemId", "子系统", true)){
		return false;
	}
	if($.validInput("searchDataDictName", "查询条件", true)){
		return false;
	}

    $.ajax({
		type : "POST",
		cache: false, 
        url  : "m/datadict/query",
        dataType : "json",
        data : {subSystemID : $("#curSubSystemId").val(),
                dictName : $("#searchDataDictName").val()},
        success : function(data){
        	if ($.checkErrorMsg(data) ) {
        		if(data.dataDictPage && data.dataDictPage.dataList){
	        		var strHtml = "<ul>";
	        		var dataList = data.dataDictPage.dataList;
	        		$(dataList).each(function(i) {
	                	strHtml += "<li><a class=\"opLink\" onclick=\"goToDataDict('" + dataList[i].dataDictID + "')\">" + dataList[i].dictName  + "</a></li>";
	                });
	                strHtml+="</ul>";
	                $("#searchResults").html(strHtml);
	        	}
        	}
        }
	});
};

function goToDataDict(dictId){
    $.ajax({
		type : "POST",
		cache: false, 
        url  : "m/datadict/parentlist",
        data : {dataDictID : dictId},
        dataType : "json",
        success : function(data){
        	if ($.checkErrorMsg(data) ) {
        		childDataDictsTree.expandNodePath(data.msg.text);
        	}
       	}
	});
}

//载入模块
function loadModule(servletAction,dataDictId,subsystemid){
	var url = "m/datadict/" + servletAction;
    var data = {dataDictID: dataDictId ? dataDictId : "",
       		subSystemID : subsystemid ? subsystemid : ""
   		};
	$.ajaxLoadPage("dataDictMain", url, data);
}

//左右模块拖拉,如果鼠标移动太快的话，最小宽度会有偏差；
//function MouseDownToResize(e,obj){ 
//	e = e || event;
//	obj.mouseDownX=e.clientX; 	
//	obj.pareneTdW=obj.parentElement.offsetWidth; 
//	obj.pareneTableW=theObjTable.offsetWidth; 
//	obj.setCapture(); 
//} 
//function MouseMoveToResize(e,obj){ 
//	e = e || event;
//	if(!obj.mouseDownX) return false; 
//		var newWidth=obj.pareneTdW + e.clientX-obj.mouseDownX; 
//	if(newWidth>=185) 
//	{ 
//		obj.parentElement.style.width = newWidth; 
//		theObjTable.style.width="100%"; 
//		subsystemSelector.resize(newWidth-31);
//	} else{return}
//} 
//function MouseUpToResize(e,obj){ 
//	obj.releaseCapture(); 
//	obj.mouseDownX=0; 
//} 