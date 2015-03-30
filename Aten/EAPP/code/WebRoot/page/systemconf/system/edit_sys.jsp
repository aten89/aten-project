<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="tabMid">
<div class="addCon">
	<input type="hidden" name="subSystemId" value="${subSystem.subSystemID }" />
	<table width="100%" border="0" align="center" cellpadding="0" cellspacing="1">
		<tr>
			<td class="tit">
				系统编号
			</td>
			<td>
				${subSystem.subSystemID }
			</td>
			<td class="tit">
				图标&nbsp;URL
			</td>
			<td colspan="3">
				<input readonly name="logourl" id="logourl" type="text" value="${subSystem.logoURL }" class="ipt01" style="width:70%" maxlength="500"/><font style="font-size:11px"> [大小：62×62px]</font>
			</td>
		</tr>
		<tr>
			<td class="tit">
				系统名称
			</td>
			<td>
				<input readonly name="name" id="subSysName" type="text" value="${subSystem.name }" class="ipt01"  maxlength="40"/>
			</td>
			<td class="tit">
				IP地址
			</td>
			<td>
				<input readonly name="ipaddress" id="ipaddress" type="text" value="${subSystem.ipAddress }" class="ipt01"  maxlength="20"/>
			</td>
			<td class="tit">
				端&nbsp;&nbsp;口
			</td>
			<td>
				<input readonly name="port" id="subSysPort" type="text" value="${subSystem.port }" class="ipt01" maxlength="10"/>
			</td>
		</tr>
		<tr>
			<td class="tit">
				域&nbsp;&nbsp;&nbsp;&nbsp;名
			</td>
			<td>
				<input readonly name="domainname" id="domainname" type="text" value="${subSystem.domainName }" class="ipt01"  maxlength="50"/>
			</td>
			<td class="tit">
				服 务 名
			</td>
			<td>
				<input readonly name="servername" id="servername" type="text" value="${subSystem.serverName }" class="ipt01" maxlength="50"/>
			</td>
			<td class="tit">
				是否开放
			</td>
			<td id="isValidTD">
				<input name="isValid" type="radio" value="true" ${empty subSystem || subSystem.isValid ? "checked" : ""}/>是&nbsp;&nbsp;&nbsp;<input name="isValid" type="radio" value="false" ${empty subSystem || subSystem.isValid ? "" : "checked"}/>否
			</td>
		</tr>
		<tr>
			<td class="tit">
				备&nbsp;&nbsp;&nbsp;&nbsp;注
			</td>
			<td colspan="5">
				<textarea readonly name="description" id="description" class="area01">${subSystem.description }</textarea>
			</td>
		</tr>
	</table>
</div>
<div class="addTool2">
	<input type="button" value="保存" id="saveSubSystem" class="allBtn" />
	<input type="button" value="保存并新增" id="saveAddSubSystem" class="allBtn" />
	<input type="reset" value="重置" id="resetSubSystem" class="allBtn" />
</div>
</div>
<script type="text/javascript"	src="page/systemconf/system/edit_sys.js"></script>