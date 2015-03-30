var mainFrame = $.getMainFrame();

var openNodeId = "";//当前选中的节点ID
var editingId = "";//当前编辑ID
var editingText = "";//当前编辑名称
var rootID = "";//根节点ID
var knowledgeClassTree;
var firstTypeSel; // 一级分类下拉框
var secondTypeSel; // 二级分类下拉框
var columnSortObj; //表头列排序对像
function initTree(pid){
	
	rootID = pid;
    //载入群组树
 	knowledgeClassTree = $("#leftTree").simpleTree({
         animate : true,
         autoclose :false,
         drag : false,
         selectRoot : true,
         docToFolderConvert : true,
         afterAjax : afterLoadNode,
         afterClick : onClickNode,
         afterContextMenu : onContextMenu
     })[0];
     knowledgeClassTree.openNode(pid);
     knowledgeClassTree.selectNode(pid);
     
     addContextMenu($("span:first","#leftTree"));
     
}

//异步加载下级时生成左键菜单
function afterLoadNode(spanObj) {
	$("span",spanObj).each(function(){
		addContextMenu($(this));
	});
}

//添加左键菜单
function addContextMenu(obj) {
	obj.contextMenu("shortKnowledgeMenuAll", {
        bindings: {
		  	'liAdd': initAddKnowledgeClass,
			'liDel': delKnowledgeClass,
			'liMod': initModKnowledgeClass,
			'liPre': moveUp,
			'liFor': moveDown,
			'liFst': moveToFirst,
			'liLst': moveToLast	,
			'liAss': initAssign,
			'liSynAss': synAssign
	    },
         //菜单弹出事件
        menuStyleClass: "rightKey",
        itemStyleClass: "",
        itemHoverStyleClass: "over"
	}); 
}

//点击事件
function onClickNode(node) {
	var selId = node.attr("id");
	if (selId != editingId) {//点击非编辑节点时
		var addOrMidNodeInput = $('#tmpNodeName');
		if (addOrMidNodeInput.size() > 0) {//当前有节点在编辑
			knowledgeClassTree.selectNode(editingId);//选中编辑中的节点
	//		$('#tmpNodeName').blur();//firefox下点击其它节点不参使新增节点的INPUT触发失焦事件
			return;
		}
	}
	
	if(openNodeId == selId && $("#hidModuleRights").val() !=""){//点击的是当前结点，不操作
		return;
	}

	openNodeId = selId;//设置当前选中的节点ID
	//加载权限
	$.ajax({
		type : "POST",
		cache: false,
   		async : false,
      	url : "m/knowledge",
      	dataType : "xml",
      	data : {
      		act : "classright",
			classid : selId
      	},
        success : function(xml){
        	var message = $("message",xml);
        	$("#hidModuleRights").val(message.text());
        },
       	error : $.ermpAjaxError
	});
	if($.hasRight("1")){
		$("#addKnowledge").show();
		$("#changeType").show();
		$("#changeFinal").show();
		$("#copyKnowledge").show();
	}else{
		$("#addKnowledge").hide();
		$("#changeType").hide();
		$("#changeFinal").hide();
		$("#copyKnowledge").hide();
	}
	//清除搜索条件
	$("#subject").val("");
	$("#beginSubmitDate").val("");
	$("#endSubmitDate").val("");
	$("#beginPublishDate").val("");
	$("#endPublishDate").val("");
	$("#prodVer").val("");
	$("#publisher").val("");
	$("#publisherName").val("");
	if (firstTypeSel != undefined && secondTypeSel != undefined) {
		firstTypeSel.select(0);
		secondTypeSel.select(0);
	}
	gotoPage(1);//搜索
}

//右击事件
function onContextMenu(node){
	onClickNode(node);//响应点击
	
	// 授权管理权限
	var assignRight = $.hasRight("2");
	// 分类管理权限
	var mgtRight = $.hasRight("3");
	// 知识类别id
	var classId = node.attr("id");
	// 右键弹出菜单 html
	var menuDiv = "";
	if (!assignRight && mgtRight) {
		// 不具有授权管理权限,具有分类管理权限
		menuDiv += "<ul><li id='liAdd' style='display:none'><button class='treeAddSub'></button>增加下级</li>";
		menuDiv += "<li id='liDel' style='display:none'><button class='treeDel'></button>删除</li>";
		menuDiv += "<li id='liMod' style='display:none'><button class='treeEdit'></button>修改</li>";
		menuDiv += "<li class='treeBlank'></li>";
		menuDiv += "<li id='liPre' style='display:none'><button class='treeFront'></button>向前移</li>";
		menuDiv += "<li id='liFor' style='display:none'><button class='treeBack'></button>向后移</li>";
		menuDiv += "<li id='liFst' style='display:none'><button class='treeTop'></button>移至最前</li>";
		menuDiv += "<li id='liLst' style='display:none'><button class='treeBottom'></button>移至最后</li>";
		menuDiv += "</ul>";	
		
	} else if(!mgtRight && assignRight){
			menuDiv += "<ul><li id='liAss' style='display:none'><button class='treeRight'></button>授权</li>";
			menuDiv += "<li id='liSynAss' style='display:none'><button class='treeUpdate'></button>同步权限</li>";
			menuDiv += "</ul>";	
	} else if(mgtRight && assignRight){
		// 授权管理权限和分类管理权限 都有权限
		menuDiv += "<ul><li id='liAdd' style='display:none'><button class='treeAddSub'></button>增加下级</li>";
		menuDiv += "<li id='liDel' style='display:none'><button class='treeDel'></button>删除</li>";
		menuDiv += "<li id='liMod' style='display:none'><button class='treeEdit'></button>修改</li>";
		menuDiv += "<li class='treeBlank'></li>";
		menuDiv += "<li id='liPre' style='display:none'><button class='treeFront'></button>向前移</li>";
		menuDiv += "<li id='liFor' style='display:none'><button class='treeBack'></button>向后移</li>";
		menuDiv += "<li id='liFst' style='display:none'><button class='treeTop'></button>移至最前</li>";
		menuDiv += "<li id='liLst' style='display:none'><button class='treeBottom'></button>移至最后</li>";
		menuDiv += "<li class='treeBlank'></li>";
		menuDiv += "<li id='liAss' style='display:none'><button class='treeRight'></button>授权</li>";
		menuDiv += "<li id='liSynAss' style='display:none'><button class='treeUpdate'></button>同步权限</li>";
		menuDiv += "</ul>";	
		
	} else {
		menuDiv = "";
	}
	// 填充右键弹出菜单
	$("#shortKnowledgeMenuAll").html(menuDiv);
}



//初始化增加节点页面
function initAddKnowledgeClass(){

	//先展开交节点
	knowledgeClassTree.openNode(knowledgeClassTree.getSelected(),initAddNode);
}

function initAddNode(spanObj, isNode){
	var node = isNode ? spanObj : spanObj.parent();
	var parentId = node.attr("id");
	knowledgeClassTree.addNode("tmpNodeId","", function(desc, obj2){
		var inputObj = $("<input type='text' id='tmpNodeName' maxlength='20' value='新建类别' style='width:120px' />");
		obj2.find("span:first").hide().before(inputObj);//就隐藏SPAN并在前面添加input
		
		editingId = "tmpNodeId";
		openNodeId = "";//清空当前选中ID
		
		inputObj.blur(function(e){//失去焦点提交
			inserNode(inputObj, parentId);
		}).keypress(//回车提交
	       	function(e){
	           	if(e.keyCode == 13){
	               	inserNode(inputObj, parentId);
	               	return false;
	           	}
	  		}
	   	);
	   	
	   	//延迟才可以选中，见鬼
		setTimeout(function(){
			inputObj.select().focus();
		},10);
//新增权限与上级一样	   
//	   $("#hidModuleRights").val("");//清除权限
	   
	   $("#knowledgeList").html("");//清空知识列表
	});
}

var commiting = false;//防止重复提交
//保存新节点
function inserNode(obj, pid){
	if (commiting) {
		return;
	}
	commiting = true;
//	obj = $(obj);
	if(!vaildateNodeParam(obj)){
		commiting = false;
		return;
	}
	
	var nodeText = $.trim(obj.val());
	$.ajax({
		type : "POST",
		cache: false,
   		async : false,
      	url : "m/knowledge",
      	dataType : "xml",
      	data : {
      		act : "addnode",
			parentId : pid,
			nodeText : nodeText
      	},
        success : function(xml){
        	var message = $("message",xml);
        	if(message.attr("code") == "1") {
				openNodeId = message.text();//设置当前节点ID
				knowledgeClassTree.modifyNode(openNodeId, nodeText, function(node){
					$("input", node).remove();//移除input
					var spanOb = $("span:first", node);
					spanOb.show();//显示span
					addContextMenu(spanOb);//右键菜单
				});//设置新ID
				
        	}else{
        		$.alert(message.text());
        		obj.select().focus();
        	}
        	commiting = false;
         },
       	error : function() {
       		$.ermpAjaxError;
       		obj.select().focus();
       		commiting = false;
       	}
	});
}

function vaildateNodeParam(obj) {
	var nodeText = $.trim(obj.val());
	var result = $.validChar(nodeText,"&<>\"'#");
	if (result) {
		$.alert("类别名称不能包含非法字符：" + result);
		obj.select().focus();
		return false;
	}
	if (nodeText == "") {
		$.alert("类别名称不能为空");
		obj.select().focus();
		return false;
	}
	return true;
}
//删除节点
function delKnowledgeClass(){
	if (openNodeId==rootID) {
		$.alert("不能删除根类别");
		return;
	}
	$.confirm("确定删除该类别？",function(r){
		if (r) {
			$.ajax({
				type : "POST",
				cache: false,
		   		async : true,
		      	url : "m/knowledge",
		      	dataType : "xml",
		      	data : {
		      		act : "delNode",
					id : openNodeId
		      	},
		        success : function(xml){
		        	var message = $("message",xml);
		        	if(message.attr("code") == "1") {
					//	$.alert(message.text());
						knowledgeClassTree.delNode();
		        	}else{
		        		$.alert(message.text());
		        	}
		        },
		       	error : $.ermpAjaxError
			});
		}
	});

}

//初始化修改节点页面
function initModKnowledgeClass(){
	var node = knowledgeClassTree.getSelected();
	
	editingText = $('span:first',node).text();
	editingId = openNodeId;
	if (editingId==rootID) {
		$.alert("不能修改根类别");
		return;
	}
	
	var inputObj = $("<input type='text' id='tmpNodeName' maxlength='20' value='" + editingText + "' style='width:120px' />");
	node.find("span:first").hide().before(inputObj);//就隐藏SPAN并在前面添加input
	
	
	inputObj.blur(function(){//失去焦点提交
		modifyNode(this, openNodeId);
	}).keypress(//回车提交
       	function(e){
           	if(e.keyCode == 13){
               modifyNode(this, openNodeId);
               	return false;
           	}
  		}
   	);
	   	
   	//延迟才可以选中，见鬼
	setTimeout(function(){
		inputObj.select().focus();
	},10); 
}

function modifyNode(obj, id){
	if (commiting) {
		return;
	}
	commiting = true;
	
	obj =  $(obj);
	if(!vaildateNodeParam(obj)){
		commiting = false;
		return;
	}
	
	var nodeText = $.trim(obj.val());
	if (editingText == nodeText) {//文本内容无改变不提交
		knowledgeClassTree.modifyNode(null, nodeText, function(node){
			$("input", node).remove();//移除input
			$("span:first", node).show();//显示span
		});//设置新名称
		commiting = false;
		return;
	}
	
	$.ajax({
		type : "POST",
		cache: false,
   		async : true,
      	url : "m/knowledge",
      	dataType : "xml",
      	data : {
      		act : "modifyNode",
			id : id,
			nodeText : nodeText
      	},
        success : function(xml){
        	var message = $("message",xml);
        	if(message.attr("code") == "1") {
				knowledgeClassTree.modifyNode(null, nodeText, function(node){
					$("input", node).remove();//移除input
					$("span:first", node).show();//显示span
				});//设置新名称
        	} else {
        		$.alert(message.text());
        		obj.select().focus();
        	}
        	commiting = false;
         },
       	error : function() {
       		$.ermpAjaxError;
       		obj.select();
       		commiting = false;
       	}
	});
}


//初始化授权页面
function initAssign(){
	mainFrame.addTab({
		id:"oa_impower_knowledgeClass" + openNodeId,
		title:"知识授权",
		url:BASE_PATH+"m/knowledge?act=init_assign&id=" + openNodeId +"&flag=0",
		callback:refreshNode
	});
}

//
function refreshNode(){
	$("#hidModuleRights").val("");//清除权限
	var parentNode = knowledgeClassTree.getParentNode();

	if (parentNode.size() > 0) {
		knowledgeClassTree.refreshNode(parentNode, "m/knowledge?act=classtree&pid=" + parentNode.attr("id"),function(){
			if ($('>ul:has(span)',parentNode).size() == 0) {//没有子节点，转为文件
				knowledgeClassTree.convertToDoc(parentNode);
			}
			knowledgeClassTree.selectNode(openNodeId);//选中
		});
		
	}
	
}

/**
 * 同步权限,以及 是否强制评分 配置.
 */
function synAssign(){
	$.confirm("是否同步[当前权限]到所有下级类别？",function(r){
		if (r) {
			$.ajax({
				type : "POST",
				cache: false,
		   		async : true,
		      	url : "m/knowledge",
		      	dataType : "xml",
		      	data : {
		      		act : "synassign",
					id : openNodeId
		      	},
		        success : function(xml){
		        	var message = $("message",xml);
		        	if (message.attr("code") == "1") {
		            //	$.alert("权限同步成功！");
		              	//默认刷新当前节点
						knowledgeClassTree.refreshNode(null, "m/knowledge?act=classtree&pid=" + openNodeId, function(obj, isNode){
							var cNode = isNode ? obj : obj.parent();
							if ($('>ul:has(span)',cNode).size() > 0) {//有子节点，转为文件夹
								knowledgeClassTree.convertToFolder(cNode);
							}
						
						});	
		          	} else {
		           		$.alert(message.text());
		         	}
		        },
		       	error : $.ermpAjaxError
			});
		}
	});
}

//上移
function moveUp(){
	knowledgeClassTree.moveUp(function(node, target){
		moveNode(0, node, target);
	});
}

//下移
function moveDown(){
	knowledgeClassTree.moveDown(function(node, target){
		moveNode(1, node, target);
	});
}
//最前
function moveToFirst(){
	knowledgeClassTree.moveTop(function(node, target){
		moveNode(2, node, target);
	});
}
//最后
function moveToLast(){
	knowledgeClassTree.moveBottom(function(node, target){
		moveNode(3, node, target);
	});
}

//移动节点
function moveNode(status, node, target){//status: up = 0, down=1, fist= 2, last=3
	var id = node.attr("id");
	if(id == ""){
		return;
	}		
//	var parentNode = node.parent().parent();
//	var parentId = parentNode.attr("id");
	var targetId = target.attr("id");
	if(targetId == ""){
		return;
	}

	$.ajax({
		type : "POST",
		cache: false,
   		async : true,
      	url : "m/knowledge",
      	dataType : "xml",
      	data : {
      		act : "movenode",
      		id	: id,
			targetId : targetId,
			status	: status
      	},
        success : function(xml){
        	var message = $("message",xml);
        	if(message.attr("code") != "1") {
        		$.alert(message.text());
        	}
        },
       	error : $.ermpAjaxError
	});
}








function gotoPage(pageNo, totalPage){
    if(isNaN(pageNo)){
        $.alert("页索引只能为数字！");
        return;
    } else if(pageNo < 1){
        pageNo = 1;
    }
    if (totalPage && !isNaN(totalPage) && pageNo > totalPage) {
    	pageNo = totalPage;
    }
    $("#hidNumPage").val(parseInt(pageNo));
    knowledgeList();
}

//知识点列表
function knowledgeList(){
	if (!$.hasRight("0")) {//没有查询权限
		$("#knowledgeList").html("");
		return;
	}
	
	var subject = $.trim($("#subject").val());
	var result = $.validChar(subject);
	if (result) {
		$.alert("标题不能包含非法字符：" + result);
		$("#subject").focus();
		return ;
	}	
	var beginPublishDate = $.trim($("#beginPublishDate").val());
	var endPublishDate = $.trim($("#endPublishDate").val());
	if(	$.compareDate(beginPublishDate,endPublishDate)){
		$.alert("发布开始时间不能大于结束时间！");
		return;
	};		
	var pageno=$.trim($("#hidNumPage").val());
	var pagecount=$.trim($("#hidPageCount").val());
	
	var sortCol = columnSortObj.getSortColumn();
	var ascend =  columnSortObj.getAscend();
	
	//发布者
	var publisher = $("#publisher").val(); 
	
	$.ajax({
		type : "POST",
		cache: false,
   		async : true,
      	url : "m/knowledge",
      	dataType : "xml",
      	data : {
      		act : "knowledgelist",
      		pageno : pageno,
      		pagecount : pagecount,
      		knowClassId : openNodeId,
      		subject : subject,
      		beginPublishDate : beginPublishDate,
      		endPublishDate : endPublishDate,
      		publisher : publisher,
      		sortCol : sortCol,
      		ascend : ascend
      	},
        success : function(xml){
        	var message = $("message",xml);
        	if(message.attr("code") == "1") {
        		var hasMamRight = $.hasRight("1");//是否有管理权限
        		if (hasMamRight) {
        			$("#checkAll").show().attr("checked",false);
        		} else {
        			$("#checkAll").hide();
        		}
        		var knowledgeLstHtml=""; 
        		$("knowledge",xml).each(function(index){
        			var knowledge = $(this);
        			knowledgeLstHtml+="<tr onmouseout=\"$(this).removeClass('over');\" onmouseover=\"$(this).addClass('over');\"><td>";
        			if (hasMamRight) {
        				knowledgeLstHtml+="<input onclick='event.cancelBubble=true;' type=\"checkbox\" name=\"know\" value=\""+knowledge.attr("id")+"\"/>";
        			}
        			knowledgeLstHtml+="</td><td>"+$("subject",knowledge).text()+"</td>"
        							+"<td>"+$("publisher",knowledge).text()+"</td>"
        							+"<td>"+$("apply-date",knowledge).text()+"</td>"
        							+"<td  class='oprateImg' onclick='event.cancelBubble=true;'><input type='image'  src='themes/comm/images/crmView_ico.gif' title='详情' onclick=\"knowledgeInfo('"+knowledge.attr("id")+"')\"/>";
        			if (hasMamRight) {
        				knowledgeLstHtml+= "<input type='image'  src='themes/comm/images/crmEdit_ico.gif' title='修改' onclick=\"initModify('"+knowledge.attr("id")+"')\"><input type='image'  src='themes/comm/images/crmDel_ico.gif' title='删除' onclick=\"delKnowledge('"+knowledge.attr("id")+"')\">";
        			}
        			knowledgeLstHtml+="</td></tr>";
        		});
        		$("#knowledgeList").html(knowledgeLstHtml);
        		enWrap();
				//----------------------------------------------------
		        //------------------翻页数据----------------------------
		        $(".pageNext").html($.createTurnPage(xml));
		        //----------------------------------------------------		
		        $.EnterKeyPress($("#numPage"),$("#numPage").next());
        	}
        },
       	error : $.ermpAjaxError
	});
		
}

//查看详情页面
function knowledgeInfo(id){//id是类型的id
	mainFrame.addTab({
		id:"oa_knowledge_view_"+id,
		title:"查看知识信息",
		url:BASE_PATH + "/m/knowledge?act=knowledgeinfo&id="+id+"&openNodeId="+openNodeId,
		callback:knowledgeList
	});
}
//修改知识点页面
function initModify(id){
	mainFrame.addTab({
		id:"oa_knowledge_modify_"+id,
		title:"修改知识信息",
		url:BASE_PATH + "/m/knowledge?act=initmodifyknowledge&id=" + id,
		callback:knowledgeList
	});
}
//删除知识点
function delKnowledge(id){
	$.confirm("是否删除?", function(r){
		if (r) {
			$.ajax({
				type : "POST",
				cache: false,
		   		async : false,
		      	url : "m/knowledge",
		      	dataType : "xml",
		      	data : {
		      		act : "delknowledge",
		      		id	: id,
		      		classid : openNodeId
		      	},
		        success : function(xml){
		        	var message = $("message",xml);
		        	if(message.attr("code") == "1") {
		        		//刷新父列表
						knowledgeList();
		        	}else{
		        		$.alert(message.text());
		        	}
		        },
		        error : $.ermpAjaxError
		    });
		}
	});
}

function enWrap(){
	var D=document; F(D.getElementById("knowledgeList")); function F(n){var u,r,c,x; if(n.nodeType==3){ u=n.data.search(/\S{10}/); if(u>=0) { r=n.splitText(u+10); n.parentNode.insertBefore(D.createElement("WBR"),r); } }else if(n.tagName!="STYLE" && n.tagName!="SCRIPT"){for (c=0;x=n.childNodes[c];++c){F(x);}} } 
}
