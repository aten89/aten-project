<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title></title>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="page/address/alladdr/view_addr.js"></script>
</head>
<body class="bdNone">
<input id="hidModuleRights" type="hidden" value="<oa:right key='all_addr'/>"/>
<input name="id" id="id" type="hidden" value="${addrList.id }" />
<input type="hidden" id="saveImgPath" name="saveImgPath" value="${saveImgPath }" />
<div class="tabMid">
<div class="addCon">
	<table width="720" border="0" align="center" cellpadding="0"  cellspacing="1">
		<tr>
			<th colspan="4">基本信息</th>
		</tr>
		<tr>
			<td rowspan="3" class="tit">头像
			</td>
			<td rowspan="3">
				<div style="width:227px">
					<div id="imgDiv"></div>		
				</div>			
	        </td>
			<td class="tit">姓名 </td>
		    <td><span id="userName">${addrList.userName }</span> </td>
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
		  <td class="tit"><div style="width:80px">职位</div></td>
			<td><span id="userPost">${addrList.userPostName }</span> </td>
		  	<td class="tit"><div style="width:80px">工号</div> </td>
		  	<td width="235"><span id="employeeNumber">${addrList.employeeNumber }</span> </td>
	  	</tr>	 
		<tr>
		  <td class="tit">移动电话 </td>
		  <td><span id="userMobile">${addrList.userMobile }</span>
          </td>
			<td class="tit">办公电话 </td>
			<td><span id="userOfficeTel">${addrList.userOfficeTel }</span>
            </td>
		</tr>  
		<tr>
		  <td class="tit">座位号 
		  </td>
		  <td><span id="seatNumber">${addrList.seatNumber }</span>
          </td>
		  <td class="tit">E-mail 
          </td>
		  <td><span id="userEmail">${addrList.userEmail }</span>
		  </td>
		</tr>
		<tr>
		  	<td class="tit">昵称
		  	</td>
			<td><span id="userNickName">${addrList.userNickName }</span>
			</td>
			<td class="tit">入司时间 </td>
			<td><span id="userEnterCorpDate"><fmt:formatDate value="${addrList.userEnterCorpDate }" pattern="yyyy-MM-dd"/></span> </td>
		  	
		</tr>		
		
		<tr>
		  <td class="tit">性别 </td>
		  <td><span id="userSex">${addrList.userSex == "F" ? "女" : (addrList.userSex == "M" ? "男" : "")}</span></td>	
			<td class="tit">籍贯 </td>
			<td><span id="userNativePlace">${addrList.userNativePlace }</span></td>
		</tr>
		<tr>
			<td class="tit">QQ号
		  	</td>
			<td><span id="userQQ">${addrList.userQQ==0?"":addrList.userQQ }</span>
			</td>
		  	<td class="tit">微信号
		  	</td>
			<td><span id="userMSN">${addrList.userMSN }</span></td>
		</tr>		
		<tr>
		  	<td class="tit">出生日期
		  	</td>
			<td><span id="userBirthDate"><fmt:formatDate value="${addrList.userBirthDate }" pattern="yyyy-MM-dd"/></span>
			</td>
		  	<td class="tit">民族 </td>
		  	<td><span id="userNation">${addrList.userNation }</span>
            </td>
		</tr>		
		<tr>
		  <td class="tit">家庭电话 </td>
		  <td><span id="userHomeTel">${addrList.userHomeTel }</span>
          </td>
		  	<td class="tit">通讯地址邮编 </td>
			<td><span id="zipCode">${addrList.zipCode }</span>
            </td>
		</tr>
		<tr>
		  	<td class="tit">通讯地址
		  	</td>
			<td colspan="3"><span id="userCommAddr">${addrList.userCommAddr }</span>
			</td>
	  	</tr>
		<tr>
		  	<td class="tit">家庭住址
		  	</td>
			<td colspan="3"><span id="userHomeAddr">${addrList.userHomeAddr }</span>
			</td>
	  	</tr>		
		<tr>
			<td class="tit">自我描述
			</td>
			<td colspan="3" width="600"><span id="remark">${addrList.remark }</span>
	        </td>
		</tr>
	</table>
</div>
</div>
<!-- 按钮区域 -->
<div class="addTool2">	
    <input id="btnModify" type="button" value="修改" class="allBtn" />
</div>

</body>
</html>
