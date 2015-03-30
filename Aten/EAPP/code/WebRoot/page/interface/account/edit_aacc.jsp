<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<input type="hidden" id="hidislock" value="${actorAccount.isLock ? 'Y' : 'N'}">
<div class="addCon tabMid"> 
  <table width="100%"  border="0" align="center" cellpadding="0"  cellspacing="1">
  <tr>
	<td class="tit">帐&nbsp;&nbsp;&nbsp;&nbsp;号</td>
	<td><input id="accountid" name="accountid" maxlength="40" type="text" readonly class="ipt01" value="${actorAccount.accountID}"/></td>
	<td  class="tit">显&nbsp;示&nbsp;名</td>
	<td><input id="displayname" name="displayname" maxlength="40" readonly type="text" class="ipt01" value="${actorAccount.displayName}"/></td>
	<td class="tit">锁定状态</td>
	<td><!--下拉框-->
	  <div id="islock" style="display:none">
	  	<div>N**未锁定</div>
		<div>Y**锁定</div>
	  </div>
	  <!--下拉框 end-->
	  </td>
  </tr>
  <tr>
	<td class="tit">创建时间</td>
	<td><fmt:formatDate value="${actorAccount.createDate }" pattern="yyyy-MM-dd HH:mm"/></td>
	<td class="tit">失效时间</td>
	<td>
	<input id="invaliddate" name="invaliddate" readonly type="text" style="width:115px" class="invokeBoth" value="<fmt:formatDate value="${actorAccount.invalidDate }" pattern="yyyy-MM-dd"/>"/>
	</td>
	<td class="tit">密码凭证</td>
	<td><input id="credence" name="credence" maxlength="500" type="password" readonly class="ipt01" value="${actorAccount.credence}"/></td>
  </tr>
  <tr>
	<td class="tit">备&nbsp;&nbsp;&nbsp;&nbsp;注</td>
	<td colspan="5"><textarea id="desc" name="textarea" readonly class="area01">${actorAccount.description }</textarea></td>
  </tr>
</table>
<div class="addTool2">
  <input id="saveActorAcc" name="saveActorAcc" type="button" value="保存" class="allBtn"/>
  <input id="saveAddActorAcc" name="saveAddActorAcc" type="button" value="保存并新增" class="allBtn"/>
  <input id="resetActorAcc" name="resetActorAcc" type="reset" value="重置" class="allBtn"/>
</div>
</div>
<!--接口帐号 新增 end-->
<script type="text/javascript" src="page/interface/account/edit_aacc.js"></script>