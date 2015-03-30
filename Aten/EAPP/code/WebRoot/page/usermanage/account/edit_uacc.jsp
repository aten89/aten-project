<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<input type="hidden" id="hidislock" value="${userAccount.isLock ? 'Y' : 'N'}">
<input type="hidden" id="hidcpflag" value="${userAccount.changePasswordFlag}">
<!--用户帐号 新增-->
<div class="addCon tabMid">
  <table width="100%"  border="0" align="center" cellpadding="0"  cellspacing="1">

  <tr>
    <td class="tit">帐&nbsp;&nbsp;&nbsp;&nbsp;号</td>
    <td><input id="accountid" name="accountid" maxlength="40" readonly type="text" class="ipt01" value="${userAccount.accountID}"/></td>
    <td class="tit">显示名称</td>
    <td><input id="displayname" name="displayname" maxlength="40" readonly type="text" class="ipt01" value="${userAccount.displayName}"/></td>
    <td class="tit">锁定状态</td>
    <td><!--下拉框-->
      <div id="islock" style="display:none">     
        <div>N**未锁定</div>
        <div>Y**锁定</div>         
      </div>
      <!--下拉框 end-->      </td>
  </tr>
  <tr>
    <td class="tit">登录IP限制</td>
    <td><input id="iplimit" name="iplimit" maxlength="1000" readonly type="text" class="ipt01" value="${userAccount.loginIpLimit}"/></td>
    <td class="tit">失效时间</td>
    <td>
    <input id="invaliddate" readonly type="text" style="width:115px" class="invokeBoth" value="<fmt:formatDate value="${userAccount.invalidDate }" pattern="yyyy-MM-dd"/>"/>  </td>
    <td class="tit">强制修改密码</td>
    <td><!--下拉框-->
      <div id="cpflag" style="display:none">
        <div>Y**登录时</div>
        <div>N**不约束</div>
      </div>
      <!--下拉框 end--></td>
  </tr>
  <tr>
  	<td class="tit">登录次数</td>
    <td>${userAccount.loginCount}</td>
    <td class="tit">最近登录</td>
    <td><fmt:formatDate value="${userAccount.lastLoginTime }" pattern="yyyy-MM-dd HH:mm"/></td>
    <td class="tit">创建时间</td>
    <td><fmt:formatDate value="${userAccount.createDate }" pattern="yyyy-MM-dd HH:mm"/></td>
  </tr>
  <tr>
    <td class="tit">备&nbsp;&nbsp;&nbsp;&nbsp;注</td>
    <td colspan="5"><textarea id="desc" name="textarea" readonly class="area01">${userAccount.description }</textarea></td>
  </tr>
</table>
<div class="addTool2">
  <input id="saveUserAcc" name="saveUserAcc" type="button" value="保存" class="allBtn"/>
  <input id="saveAddUserAcc" name="saveAddUserAcc" type="button" value="保存并新增" class="allBtn"/>
  <input id="resetUserAcc" name="resetUserAcc" type="reset" value="重置" class="allBtn"/>
</div>
</div>
<script type="text/javascript" src="page/usermanage/account/edit_uacc.js"></script>