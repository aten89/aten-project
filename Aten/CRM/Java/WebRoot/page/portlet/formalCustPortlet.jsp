<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="eapp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<script type="text/javascript" src="page/portlet/formalCustPortlet.js"></script>
</head>
<body >
<table width="100%" border="0" align="center" cellpadding="0"  cellspacing="0">
	<tr>
     	<td colspan="4">&nbsp;</td>
   	</tr>
   	<tr>
     	<td colspan="4" class="tipList">&nbsp;</td>
   	</tr>
    <tr>
      <td colspan="4" class="tipShow">
      <span class="crmSubTabsBk">
        <ul class="crmSubTabs" id="crmTab">
          <li class="current"><div>潜在客户</div></li>
          <li><div class="lastNone">正式客户</div></li>
      </ul>
      </span>
      <div>
        <div class="boxShadow">
              <div class="shadow01">
                <div class="shadow02" >
                  <div class="shadow03" >
                    <div class="shadow04" >
                      <div class="shadow04" >
                        <div class="shadowCon" style="width:100%;" >
			                <div id="tab00">
			                	<!-- 潜在客户列表 -->	
			                	<div class="allList"  style="height:140px;overflow:auto">
									<table id="informationTab" width="100%" border="0" cellspacing="0" cellpadding="0">
										<thead>
											<tr>
												<th width="23%">客户名称</th>
												<th width="10%">性别</th>
												<th width="20%">电话</th>
												<th width="20%">邮箱</th>
												<th width="20%">推荐产品</th>
												<th width="7%">操作</th>
											</tr>
										</thead>
										 <tbody id="potentialList">
										 </tbody>
									</table>
								</div>	
			                </div> 
			                <div id="tab01" style=" display:none">
			                	<!-- 正式客户列表 -->
			                	<div class="allList" style="height:140px;overflow:auto">
									<table id="informationTab" width="100%" border="0" cellspacing="0" cellpadding="0">
									  <thead>
										<tr>
											<th width="23%">客户名称</th>
											<th width="10%">性别</th>
											<th width="20%">电话</th>
											<th width="20%">邮箱</th>
											<th width="20%">推荐产品</th>
											<th width="7%">操作</th>
										</tr>
									  </thead>
									 <tbody id="formalList">
									 </tbody>
									</table>
								</div>
              		  		 </div>
	        				</div>
				        </div>
				      </div>
				    </div>
				  </div>
				</div>
			</div>
        </td>
    </tr>
  </table>
  <!--内容信息块切换 end-->
</body>
</html>
<!--列表
<div class="allList">
<table id="informationTab" width="100%" border="0" cellspacing="0" cellpadding="0">
  <thead>
	<tr>
		<th width="20%">客户名称</th>
		<th width="10%">性别</th>
		<th width="20%">电话</th>
		<th width="20%">邮箱</th>
		<th width="20%">推荐产品</th>
		<th width="10%">操作</th>
	</tr>
  </thead>
 <tbody id="customerList">
 </tbody>
</table>
</div>-->