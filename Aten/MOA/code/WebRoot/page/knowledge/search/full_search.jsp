<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/c.tld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<link rel="STYLESHEET" type="text/css" href="commui/autocomplete/jquery.autocomplete.css" />
<script type="text/javascript" src="${ermpPath}page/dialog/dialog.user.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="commui/autocomplete/jquery.bgiframe.min.js"></script>
<script type="text/javascript" src="commui/autocomplete/jquery.autocomplete.js"></script>

<script type="text/javascript" src="page/knowledge/search/full_search.js"></script>
<style>
.pageNext{ text-align:left}
.sosoheight{height: 30px;} 
</style>
</head>
<body class="bdNone">

<div class="klSoBk">
    <div class="klSo"> <input type="hidden" id="loadflag" value="0"/>
      <div id="searchKnowledge" style="padding-left:3px">
      	<table  border="0" cellspacing="0" cellpadding="0">
		  <tr>
		    <td>关键字：<input id="keyword" type="text" value="${keyword}" style="width:238px; height:16px" class="ipt01"/>&nbsp;</td>
        	<td>
	          <div id="statusDiv" name="status" style="display:none">
	        	<div>**所有...</div>
	        	<div>1**知识库</div>
	        	<div>0**学习资料</div>
	          </div>&nbsp;
	        </td>
        	<td><input id="searchKb" class="allBtn" type="button" value="搜索"/>&nbsp;<a href="javascript:void('0')" id="advancedSo">[精确搜索模式↓]</a></td>
          </tr>
        </table>
      </div>
        <div id="soCon" class="soCon" style="display:none">
        	<table  border="0" cellspacing="0" cellpadding="0">
			  <tr>
			    <td class="soPaddLeft">知识库：</td>
			    <td style="width:120px">
			       <div id="upstatusDiv" name="upstatus" style="display:none">
		        	<div>**所有...</div>
		        	<div>1**知识库</div>
		        	<div>0**学习资料</div>
		          </div>
		         </td>
		         <td class="soPaddLeft">一级分类：</td>
            	 <td style="width:120px">
		           	<div id="firstTypeSel" name="firstType"></div>				           	
                 </td>
			     <td class="soPaddLeft">二级分类：</td>	
	           	 <td style="width:250px">
	            	<div id="secondTypeSel"  name="secondType"></div>
	             </td>
	    	   </tr>
	    	   <tr>
			  	<td class="soPaddLeft">标&nbsp;&nbsp;题：</td>
			    <td colspan="5"><input id="title"  type="text" maxlength="40" style="width:473px" class="ipt01"/></td>
			   </tr>  	   
	    	   <tr> 
			  	<td class="soPaddLeft">发布人：</td>
			  	<td>
			  		<input id="publisher" name="qsdm" type="hidden" maxlength="40"/>
			      	<input id="publisherName" readonly="readonly" name="qsdm" type="text" maxlength="40" class="ipt01" style="width:80px"/><input type="button" id="openUserSelect" class="selBtn"/>
			  	</td>
			  	<td class="soPaddLeft">发布日期：</td>
			    <td colspan="3">
			    	<input id="beginPublishDate" readonly type="text" class="invokeBoth"  style="width:65px"/> 到 
	    	    	<input id="endPublishDate" readonly type="text" class="invokeBoth"  style="width:65px"/>
	    	    	&nbsp;&nbsp;&nbsp;&nbsp;<input name="upSearch" type="button" class="allBtn" id="upSearch" value="搜索"/>&nbsp;&nbsp;<a href="javascript:void('0')" id="advancedUpSo">[全文检索模式↑]</a>
	    	    </td>
			  </tr>	
			  <tr>
			  	<td></td>
			  	<td colspan="5">
			  		
			  	</td>
			  </tr>
			</table>
        </div>
        
    </div>
    <div id="searchList">
        
    </div>
    <div class="blank" style="height:16px"></div>
    <!--翻页-->
    <input type="hidden" id="hidNumPage" value="1"/>
	<input type="hidden" id="hidPageCount" value="10"/> 
    <div class="pageNext">
    </div>
    <!-- 翻页 end -->
</div>
</body>
</html>