<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<jsp:include page="../../base_path.jsp"></jsp:include>
	<title>知识点选择框</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <script type="text/javascript" src="commui/tree/jquery.simple.tree.js"></script>
    
	<script language="javascript">
		var args = parent.window.dialogArguments1;
		
		function init(){
			initKnowledgeList();
		}

		function selKnowledgeClass(node){
			$("#targetClassId").val(node.attr("id"));//保存选中的知识分类ID
		}
		
		//初始化组织机构树
		function initKnowledgeList(){
			var selectTree = $("#selTree").simpleTree({
				animate: true,
		  		autoclose:false,
				drag : false,
				selectRoot : true,
				afterClick: selKnowledgeClass
			})[0];
			selectTree.openNode("final_knowledge_id");
		}
		
		function selectKnowledge(){
			var parentId = $("#targetClassId").val();
			var ids = args.knowledgeIds;
			var act = args.act;
			if(ids==""){
				$.alert("未选择知识点");
				return;
			}
			if(parentId=="" ){
				$.alert("请选择知识类别");
				return;
			}
			$.ajax({
				type : "POST",
				cache: false,
		   		async : true,
		      	url : "m/knowledge",
		      	dataType : "xml",
		      	data : {
		      		act : act,
		      		ids	: ids,
		      		parentId : parentId
		      	},
		        success : function(xml){
		        	var message = $("message",xml);
		        	if(message.attr("code") == "1") {
		        		args.selected.push({targetClassId:parentId});//将选中的目标类id返回
		        		parent.window.returnValue1 = true;
		        		$.alert("操作成功", function(){
		        			args.closeDialog();
		        		});
		        	}else{
		        		$.alert(message.text());
		        	}
		        },
		        error : $.ermpAjaxError
		    });
		}
		
		function cancel(){
			parent.window.returnValue1 = false;
			args.closeDialog();
		}
		
	</script>
</head>
<body class="bdDia" onload="init();">
<input id="targetClassId" type="hidden"/>
<div class="dialogBk">
<div class="addCon">
	<table width="100%" border="0" cellspacing="1" cellpadding="0" >
		<tr>
			<td valign="top">
			  <!-- 对话框[正式库树] -->
					<div id="moduleDialog" style="padding:0 0 0 6px; margin:0">
						<div style="padding:0; margin:0">
							<ul id ="selTree" class="simpleTree" name="selTree" style="height:294px; width:248px">
								<li class="root" id="final_knowledge_id"><span>正式库</span>
									<ul class="ajax">
										<li>{url:m/knowledge?act=classtree&pid=final_knowledge_id&flag=1}</li>
									</ul>
								</li>
							</ul>
						</div>
					</div>
				<!-- 对话框[正式库树] end-->
			</td>
		</tr>
	</table>
    
</div>
</div>
<div class="addTool2">
        <input type="button" value="确 定" onclick="selectKnowledge();" class="allBtn">&nbsp;&nbsp;&nbsp;
        <input type="button" value="取 消" onclick="cancel();" class="allBtn">
</div>
</body>
</html>
