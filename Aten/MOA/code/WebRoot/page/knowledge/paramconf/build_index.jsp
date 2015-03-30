<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>

<script type="text/javascript" src="page/knowledge/paramconf/build_index.js"></script>

<input id="hidModuleRights" type="hidden" value="<oa:right key='kb_index'/>"/>
<div class="blank" style="height: 60px"></div>
<div class="cxbsBg">
	知识库的索引是在队列中定时更新的。<Br />
	(1)知识库改动时，想立即生效请点击“更新索引”<Br />
	(2)需要重建知识库的索引请点击“重建索引”<Br />
	(3)重建索引可能要花很长时间<Br />
</div>
<div class="cxbsBk">
	<input id="updateIndex" type="button" value="更新索引" class="allBtn" />&nbsp;
	<input id="rebuildIndex" type="button" value="重建索引" class="allBtn" />
</div>

