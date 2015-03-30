<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="page/address/alladdr/edit_addr.js"></script>

</head>
<body class="bdNone">
<form id="addresslistForm" name="addresslistForm">
<input id="hidModuleRights" type="hidden" value="<oa:right key='all_addr'/>"/>
<input name="id" id="id" type="hidden" value="${addrList.id }" />
<input id="userSex" name="userSex" type="hidden" value="${addrList.userSex }"/>
<input type="hidden" id="saveImgPath" name="saveImgPath" value="${saveImgPath }" />
<input name="userPhoto" id="userPhoto" type="hidden" value="${addrList.userPhoto }" />
<div class="tabMid">
<div id="divMyData" class="addCon">
	
	<table width="720" border="0" align="center" cellpadding="0"  cellspacing="1">
		<tr>
			<th colspan="4">基本信息</th>
		</tr>	
		<tr>
			<td rowspan="3" class="tit">头像
			</td>
			<td rowspan="3">
				<div style="width:227px">
					<iframe id="iframeHD" style="float:left;margin:0 0 0 3px" width="170" height="110" frameborder="0" scrolling="no" src="page/address/alladdr/upload_hd.jsp" allowTransparency="true"></iframe>
				</div>
	        </td>
			<td class="tit">姓名 </td>
		    <td width="200">${addrList.userName } </td>
		</tr>
		<tr>
		  <td class="tit">帐户名 </td>
		  <td><span id="userAccountId">${addrList.userAccountId }</span> </td>
		</tr>
		<tr>
		  <td class="tit">所在的部门 </td>
		  <td><span id="userDept">${addrList.userDeptName }</span> </td>
	  	</tr>
		<tr>
		  
		  <td class="tit">职位 </td>
			<td><span id="userPost">${addrList.userPostName }</span> </td>
			<td class="tit">工号 </td>
		  <td><input id="employeeNumber" name="employeeNumber" maxlength="30" type="text" class="ipt01" value="${addrList.employeeNumber }"/>
          </td>
		</tr>	
		<tr>
		  <td class="tit">移动电话 </td>
		  <td><input id="userMobile" name="userMobile" maxlength="15" type="text" class="ipt01" value="${addrList.userMobile }"/>
          </td>
			<td class="tit">办公电话 </td>
			<td><input id="userOfficeTel" name="userOfficeTel" type="text" maxlength="14" class="ipt01" value="${addrList.userOfficeTel }" />
            </td>
		</tr>	  
		<tr>
		  <td class="tit">座位号 </td>
		  <td><input id="seatNumber" name="seatNumber" maxlength="10" type="text" class="ipt01" value="${addrList.seatNumber }" />
          </td>
          <td class="tit">E-mail 
		  </td>
		  <td><input maxlength="50" name="userEmail" id="userEmail" type="text" class="ipt01" value="${addrList.userEmail }"/>
          </td>
			
		</tr>
		<tr>
		  	<td class="tit">昵称
		  	</td>
			<td><input maxlength="30" name="userNickName" id="userNickName" type="text" class="ipt01" value="${addrList.userNickName }" />
			</td>
		  	<td class="tit">入司时间 
			</td>
			<td><input style="width:115px" readonly"readonly" name="userEnterCorpDate" id="userEnterCorpDate" type="text" class="invokeBoth" value="<fmt:formatDate value="${addrList.userEnterCorpDate }"/>"/> 
			</td>
		</tr>		
		
		<tr>
		  <td class="tit">性别 </td>
		  <td><div id="userSexSel">
		  		<div>**请选择...</div>
		  		<div>M**男</div>
		  		<div>F**女</div>
		  </div></td>	
			<td class="tit">籍贯 </td>
			<td><input maxlength="30" name="userNativePlace" id="userNativePlace" type="text" class="ipt01" value="${addrList.userNativePlace }"/>
            </td>
		</tr>
		<tr>
			<td class="tit">QQ号
		  	</td>
			<td><input maxlength="20" name="userQQ" id="userQQ" type="text" class="ipt01" value="${addrList.userQQ==0?"":addrList.userQQ }"/>
			</td>
		  	<td class="tit">微信号
		  	</td>
			<td><input maxlength="50" name="userMSN" id="userMSN" type="text" class="ipt01" value="${addrList.userMSN }" />
			</td>
		</tr>		
		
		<tr>
		  	<td class="tit">出生日期
		  	</td>
			<td><input style="width:115px" readonly name="userBirthDate" id="userBirthDate" type="text" class="invokeBoth" value="<fmt:formatDate value="${addrList.userBirthDate }"/>"/>
			</td>
		  	<td class="tit">民族 </td>
		  	<td><input maxlength="30" name="userNation" id="userNation" type="text" class="ipt01" value="${addrList.userNation }"/>
            </td>
		</tr>		
		<tr>
		  <td class="tit">家庭电话 </td>
		  <td><input maxlength="14" name="userHomeTel" id="userHomeTel" type="text" class="ipt01" value="${addrList.userHomeTel }" />
          </td>
		  	<td class="tit">通讯地址邮编 </td>
			<td><input maxlength="6" name="zipCode" id="zipCode" type="text" class="ipt01" value="${addrList.zipCode }"/>
            </td>
		</tr>
		<tr>
		  	<td class="tit">通讯地址
		  	</td>
			<td colspan="3"><input style="width:504px" maxlength="100" name="userCommAddr" id="userCommAddr" type="text" class="ipt01" value="${addrList.userCommAddr }"/>
			</td>
	  	</tr>
		<tr>
		  	<td class="tit">家庭住址
		  	</td>
			<td colspan="3"><input style="width:504px" maxlength="100" name="userHomeAddr" id="userHomeAddr" type="text" class="ipt01" value="${addrList.userHomeAddr }"/>
			</td>
	  	</tr>		
		<tr>
			<td class="tit">自我描述
			</td>
			<td colspan="3"><textarea style="width:504px" id="remark" class="area01">${addrList.remark }</textarea>
	        </td>
		</tr>
	</table>
</div>
</div>
<!-- 按钮区域 -->
<div class="addTool2">	
    <input id="btnSave" type="button" value="保存" class="allBtn" />
	<input id="btnReset" type="button" value="重置" class="allBtn" />
</div>
</form>
</body>
</html>
