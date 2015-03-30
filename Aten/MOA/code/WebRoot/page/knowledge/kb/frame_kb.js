var mainFrame = $.getMainFrame();
$(initPage);
//初始化页面
function initPage(){
	//拉伸页面
	$("#leftTree").css("height",document.documentElement.clientHeight - 39);
	$("#tdScaleMove").css("height",document.documentElement.clientHeight - 33);
	//初始化表头列排序
	columnSortObj = $("#tableTH").columnSort(knowledgeList);
	initTree("final_knowledge_id");
	$("#reFlash").click(function(){
		$("#knowledgeList").empty();
		knowledgeList();
	});
	$("#addKnowledge").click(function(){
		mainFrame.addTab({
			id:"oa_knowledge_add_"+openNodeId,
			title:"新增知识信息",
			url:BASE_PATH + "/m/knowledge?act=initaddknowledge&id=" + openNodeId+"&isFinal=true",
			callback:knowledgeList
		});
	});
	
	//查询
	$("#searchProblem").click(function(){	    
		gotoPage(1);
	});
	
	//全选
	$("#checkAll").click(function (){
		var isCheck = $(this).attr("checked");
		$("#knowledgeList tr input[name='know']").each(function(index){
			$(this).attr("checked",isCheck);
		});
	});
	
	//移动
	$("#changeType").click(function(){
		var ids = "";
		$("input[name='know']").each(function(){
			if($(this).attr("checked")){
				ids+=","+$(this).val();
			}
		});
		if(ids == ""){
			$.alert("请选择知识点");
			return;
		}
	
		var selector = new FinalKnowledgeDialog(BASE_PATH,BASE_PATH);
		selector.setCallbackFun(function(val){
			knowledgeList();
		});
		selector.openDialog(ids,"changetype");
	});
	
	//知识点拷贝
	$("#copyKnowledge").click(function(){
		var ids = "";
		$("input[name='know']").each(function(){
			if($(this).attr("checked")){
				ids+=","+$(this).val();
			}
		});
		if(ids == ""){
			$.alert("请选择知识点");
			return;
		}
	
		var selector = new FinalKnowledgeDialog(BASE_PATH,BASE_PATH);
		selector.setCallbackFun(function(val){
			knowledgeList();
		});
		selector.openDialog(ids,"copyKnowledge");
	});
	
	//回车搜索
	$.EnterKeyPress($("#subject"),$("#searchProblem"));
	$.EnterKeyPress($("#beginPublishDate"),$("#searchProblem"));
	$.EnterKeyPress($("#endPublishDate"),$("#searchProblem"));
	
   //打开用户帐号
   $("#openUserSelect").click(
	   	function(e){
			var dialog = new UserDialog(ERMP_PATH, BASE_PATH);
			dialog.setCallbackFun(function(user){
				if (user != null) {
					$("#publisher").val(user.id);
					$("#publisherName").val(user.name);
				}
			});
			dialog.openDialog("single");
		}
	);
}

//左右模块拖拉,如果鼠标移动太快的话，最小宽度会有偏差；
function MouseDownToResize(e,obj){ 
	e = e || event;
	obj.mouseDownX=e.clientX; 	
	obj.pareneTdW=obj.parentElement.offsetWidth; 
	obj.pareneTableW=theObjTable.offsetWidth; 
	obj.setCapture(); 
} 
function MouseMoveToResize(e,obj){ 
	e = e || event;
	if(!obj.mouseDownX) return false; 
		var newWidth=obj.pareneTdW + e.clientX-obj.mouseDownX; 
	if(newWidth>=185) 
	{ 
		obj.parentElement.style.width = newWidth; 
		theObjTable.style.width="100%"; 
	} else{return;}
} 
function MouseUpToResize(e,obj){ 
	obj.releaseCapture(); 
	obj.mouseDownX=0; 
} 
