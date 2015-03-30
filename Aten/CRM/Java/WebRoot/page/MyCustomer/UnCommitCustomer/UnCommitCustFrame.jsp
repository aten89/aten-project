<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="page/MyCustomer/UnCommitCustomer/UnCommitCustFrame.js"></script>
<title></title>
</head>
<body class="oaBd">
<input id="FrameHidModuleRights" type="hidden" value="<eapp:menu key='UN_COMMIT_CUST'/>" />
<div class="costsBg" id="costsBg">
  <div class="costsBgTip">
    <table width="100%" cellspacing="0" cellpadding="0" border="0">
      <tbody>
        <tr>
          <td class="tdValign1"><div class="costsNav" id="costsNav" >
              <h1>我的客户</h1>
              <div class="mb">
                <ul id="menuList">
                  <li id="company_allot" url="page/MyCustomer/UnCommitCustomer/UnCommitCustList.jsp?dataSource=company_allot"><span class="costsDbsqd flatIcon_c1"></span>公司分配</li>
                  <li id="my_draft" url="page/MyCustomer/UnCommitCustomer/UnCommitCustList.jsp?dataSource=manual_entry"><span class="costsQdsqd flatIcon_c2"></span>未提交潜客</li>
                  <li id="unpass_cust" url="page/MyCustomer/UnCommitCustomer/UnPassCustList.jsp"><span class="costsQdsqd flatIcon_c4"></span>不通过客户</li>
                </ul>
              </div>
            </div></td>
          <td><div class="vBlank"></div></td>
          <td  class="tdValign2"><div class="costsCon" id="costsCon"> 加载页面中... </div></td>
        </tr>
      </tbody>
    </table>
  </div>
</div>
</body>
</html>