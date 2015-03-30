<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="../../base_path.jsp"></jsp:include>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<title></title>
		<script type="text/javascript" src="${ermpPath}page/dialog/dialog.user.js"></script>
		<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
		<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
		<script type="text/javascript" src="${ermpPath}jqueryui/contextmenu/jquery.contextmenu.r2.js"></script>
		<script type="text/javascript" src="commui/tree/jquery.simple.tree.js"></script>
		<script type="text/javascript" src="page/knowledge/kb/tree_comm.js"></script>	
		<script type="text/javascript" src="page/knowledge/kb/frame_kb.js"></script>
		<script type="text/javascript" src="page/knowledge/kb/kb_dialog.js"></script>
	</head>
	<body class="bdNone">
		<input id="hidModuleRights" type="hidden" value=""/>
		<!--正式知识库管理-->
		
		<div class="subBk">
		   <table width="100%"  border="0" cellspacing="0" cellpadding="0"  id="theObjTable">
			  <tr>
				<th colspan="2" class="sub02">正式知识库<b id="titlName"></b></th>
			  </tr>
			  <tr>
				<td  class="subLeft">
					<div id="tdScaleMove" class="tdScaleMove" onMouseDown="MouseDownToResize(event,this);" onMouseMove="MouseMoveToResize(event,this);" onMouseUp="MouseUpToResize(event,this);"></div>
					<div class="tdScaleLeft">
						<table width="100%" border="0" cellspacing="0" cellpadding="0" style="border:none; table-layout:fixed">
					      <tr>
					        <td style="padding-right:5px">
					            <ul class="simpleTree" id="leftTree" style="width:100%">
									<li class="root" id="final_knowledge_id"><span>知识库</span>
										<ul class="ajax">
											<li>{url:m/knowledge?act=classtree&pid=final_knowledge_id}</li>
										</ul>
									</li>
								</ul>
					        </td>
					      </tr>
					    </table>
					    <div style="height:1px; font-size:1px;width:180px"></div>
					</div>
				</td>
				<td class="subRight" id="subRight" style=" background:#fff">
				<!--工具栏-->
				<div class="addTool" id="allTool">
				  <div class="t01">
					  <input type="button" class="add_btn" id="addKnowledge"/>
					  <input type="button" class="inFile_btn" id="changeType"/>
					  <input type="button" class="fz_btn"  id="copyKnowledge"/>
					  <input type="button" class="flash_btn" id="reFlash"/>
				  </div>
				</div>
				<!--工具栏 end-->
				<div class="blank3"></div>
				<!--搜索-->
				<div class="soso">
				  <div class="t01">
				  
							标题：<input id="subject" type="text" class="ipt05"  style="width:100px" maxlength="50"/>
							发布时间：从<input id="beginPublishDate" readonly type="text" value="" class="invokeBoth" style="width:65px"/>
					       	到<input id="endPublishDate" readonly type="text" value="" class="invokeBoth" style="width:65px"/>
					    	发布人：<input id="publisher" name="qsdm" type="hidden" maxlength="40" class="ipt01" style="width:80px"/>
				     		 		<input id="publisherName" name="qsdm" type="text" maxlength="40" class="ipt01" style="width:70px"/><input type="button" id="openUserSelect" class="selBtn"/>
							<input id="searchProblem" name="" type="button" value="搜索" class="allBtn"/>
						
				  </div>
				</div>

				<!--搜索 end-->
				  <!--内容切换-->
				  <div class="allList">
				  		<table width="100%" cellspacing="0" cellpadding="0" border="0">
							 <thead>
								<tr id="tableTH">
									<th width="5%"><input type="checkbox" id="checkAll" /></th>
									<th width="50%">标题</th>
									<th width="15%">发布人</th>
									<th width="17%" sort="publishDate">发布时间</th>
									<th width="13%">操作</th>
								</tr>
							  </thead>
							  <tbody id="knowledgeList">
						 	  </tbody>
					 	 </table>
					 	 <div class="pageNext"></div>
						<input id="hidNumPage" type="hidden" value="1"/>
						<input id="hidPageCount" type="hidden" value="12"/>
				  </div>
				  <!--内容切换 end-->
		        </td>
			  </tr>
			</table>
		</div>
		<!-- 正式知识库管理 end-->
		
	<!--右键菜单-->
	  <div id="shortKnowledgeMenuAll" class="rightKey" style="position:absolute;top:80px;left:-12px; z-index:18;display:none;">
		<ul>
		<li id="liAdd" style="display:none"><button class="treeAddSub"></button>增加下级</li>
		<li id="liDel" style="display:none"><button class="treeDel"></button>删除</li>
		<li id="liMod" style="display:none"><button class="treeEdit"></button>修改</li>
		<li class="treeBlank"></li>
		<li id="liPre" style="display:none"><button class="treeFront"></button>向前移</li>
		<li id="liFor" style="display:none"><button class="treeBack"></button>向后移</li>
		<li id="liFst" style="display:none"><button class="treeTop"></button>移至最前</li>
		<li id="liLst" style="display:none"><button class="treeBottom"></button>移至最后</li>
		<li class="treeBlank"></li>
		<li id="liAss" style="display:none"><button class="treeRight"></button>授权</li>
		<li id="liSynAss" style="display:none"><button class="treeUpdate"></button>同步权限</li>
		</ul>
	  </div>
	  <!--右键菜单 end-->
	   
	</body>
</html>