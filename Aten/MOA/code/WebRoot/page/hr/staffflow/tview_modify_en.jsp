<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<title></title>
<script type="text/javascript" src="${ermpPath}/jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="${ermpPath}/page/dialog/dialog.dept.js"></script>
<script type="text/javascript" src="page/hr/staffflow/tview_modify_en.js"></script>
<script type="text/javascript" src="page/system/flownotify/comm_notify.js"></script>
</head>
<body class="tabBd" scroll="no">
<div class="tabScroll" style="bottom:0px;_bottom:0px;">
  <!--请假单-->
  <div class="addCon" style="padding:8px">
    <table width="100%" border="0" align="center" cellpadding="0"  cellspacing="1">
      <tr>
        <th colspan="6" onclick="hideTbody('showFlag1','staffInfo1');"><b id="showFlag1">-</b> 基本信息</th>
      </tr>
      <tbody id="staffInfo1">
	      <tr>
	      	<td class="tit">申请单号</td>
	        <td><input type="hidden" name="id" id="staffid" value="${staffFlowApply.id}"/>${staffFlowApply.id}</td>
	        <td class="tit">申 请 人</td>
	        <td id="applicantName">${staffFlowApply.applicantName}</td>
	        <td class="tit"><span class="cRed" valid="userName">*</span>入职者姓名</td>
	        <td>
	          <input id="userName" maxlength="40" type="text" class="ipt01" value="${staffFlowApply.userName}"/>
	        </td>
	      </tr>
	      <tr>
	      	<td class="tit">人员隶属</td>
	        <td>
	          <select id="companyArea" name="regional" disabled class="sel02" style="width:110px">
				<c:forEach var="d" items="${areas}" >
			        <option value="${d.dictCode }" ${d.dictCode eq staffFlowApply.companyArea ? 'selected':'' }>${d.dictName }</option>
				</c:forEach>
	   		  </select>
	        </td>
	        <td class="tit"><span class="cRed" valid="groupName">*</span>入职部门</td>
	        <td colspan="3">
	          <div style="float:left">
				<input id="groupName" disabled maxlength="40" type="text" style="width:70%" class="ipt01" value="${staffFlowApply.groupName}"/>
			<!-- 	<input type="button" id="openDeptSelect" class="selBtn"/> -->
			  </div>
			  <div id="groupFullName" style="color:#9E9E9E">${staffFlowApply.groupFullName}</div>
	    	</td>
	      </tr>
	      <tr>
	    	<td class="tit"><span class="cRed" valid="post">*</span>岗 位</td>
	        <td>
	          <input id="post" maxlength="40" type="text" class="ipt01" value="${staffFlowApply.post}"/>
	    	</td>
	    	<td class="tit"><span class="cRed" valid="level">*</span>级 别</td>
	        <td>
	           <input id="hidLevel" type="hidden" value="${staffFlowApply.level}"/>
	           <select id="level" class="sel02">
	             <option value="">请选择...</option>
	             <option value="总经理">总经理</option>
	             <option value="总监">总监</option>
	             <option value="部门经理">部门经理</option>
	             <option value="部门副经理">部门副经理</option>
	             <option value="经理">经理</option>
	             <option value="主管">主管</option>
	             <option value="专员">专员</option>
	             <option value="助理">助理</option>
	             <option value="销售">销售</option>
	             <option value="销售管理">销售管理</option>
	           </select>
	    	</td>
	    	<td class="tit"><span class="cRed" valid="staffStatus">*</span>员工状态</td>
	        <td>
	          <input id="hidStaffStatus" type="hidden" value="${staffFlowApply.staffStatus}"/>
	          <select id="staffStatus" class="sel02">
	             <option value="试用">试用</option>
	             <option value="正式">正式</option>
	           </select>
	    	</td>
	      </tr>
	      <tr>
	        <td class="tit"><span class="cRed" valid="entryDate">*</span>入职时间</td>
	        <td>
	          <input id="entryDate" readonly type="text" class="invokeBoth" style="width:75%" value="<fmt:formatDate value="${staffFlowApply.entryDate}" pattern="yyyy-MM-dd"/>"/>
	    	</td>
	    	<td class="tit"><span class="cRed" valid="formalDate">*</span>转正时间</td>
	        <td>
	          <input id="formalDate" readonly type="text" class="invokeBoth" style="width:75%" value="<fmt:formatDate value="${staffFlowApply.formalDate}" pattern="yyyy-MM-dd"/>"/>
	    	</td>
	    	<td class="tit"><span class="cRed" valid="employeeNumber">*</span>分配工号</td>
	        <td>
	          <input id="employeeNumber" maxlength="40" type="text" class="ipt01" value="${staffFlowApply.employeeNumber}"/>
	        </td>
	      </tr>
	      <tr>
	        <td class="tit"><span class="cRed" valid="idCard">*</span>身份证号</td>
	        <td>
	          <input id="idCard" maxlength="40" type="text" class="ipt01" onchange="onChangeIdCard(this.value)" value="${staffFlowApply.idCard}"/>
	    	</td>
	    	<td class="tit"><span class="cRed" valid="birthdate">*</span>出生日期</td>
	        <td>
	          <input id="birthdate" readonly type="text" class="invokeBoth" onchange="onChangeBirthdate(this.value)" style="width:75%" value="<fmt:formatDate value="${staffFlowApply.birthdate}" pattern="yyyy-MM-dd"/>"/>
	    	</td>
	    	<td class="tit"><span class="cRed">*</span>年 龄</td>
	        <td>
	          <input id="age" maxlength="3" type="text" class="ipt01" value="${staffFlowApply.age}"/>
	    	</td>
	      </tr>
	      <tr>
	        <td class="tit"><span class="cRed">*</span>性 别</td>
	        <td>
	          <input class="cBox" name="sexOption" type="radio" value="0" ${staffFlowApply.sex == '1' ? '':'checked'}/>&nbsp;男&nbsp;&nbsp;
	          <input class="cBox" name="sexOption" type="radio" value="1" ${staffFlowApply.sex == '1' ? 'checked':''}/>&nbsp;女
	    	</td>
	    	<td class="tit"><span class="cRed" valid="mobile">*</span>手机号码</td>
	        <td>
	          <input id="mobile" maxlength="40" type="text" class="ipt01" value="${staffFlowApply.mobile}"/>
	    	</td>
	        <td class="tit">办公电话</td>
	        <td>
	          <input id="officeTel" maxlength="40" type="text" class="ipt01" value="${staffFlowApply.officeTel}"/>
	    	</td>
	      </tr>
	      <tr>
	    	<td class="tit"><span class="cRed" valid="email">*</span>电子邮箱</td>
	        <td>
	          <input id="email" maxlength="40" type="text" class="ipt01" value="${staffFlowApply.email}"/>
	    	</td>
	    	<td class="tit"><span class="cRed" valid="politicalStatus">*</span>政治面貌</td>
	        <td>
	          <input id="hidPoliticalStatus" type="hidden" value="${staffFlowApply.politicalStatus}"/>
	          <select id="politicalStatus" class="sel02">
	             <option value="党员">党员</option>
	             <option value="群众">群众</option>
	             <option value="团员">团员</option>
	             <option value="预备党员">预备党员</option>
	             <option value="无党派人士">无党派人士</option>
	             <option value="其他">其他</option>
	           </select>
	    	</td>
	        <td class="tit"><span class="cRed" valid="nation">*</span>民 族</td>
	        <td>
	          <input id="nation" maxlength="40" type="text" class="ipt01" value="${staffFlowApply.nation}"/>
	    	</td>
	      </tr>
	      <tr>
	    	<td class="tit"><span class="cRed">*</span>是否有销售经验</td>
	        <td>
	          <input class="cBox" name="salesExperienceOption" type="radio" value="1" ${staffFlowApply.salesExperience == '1' ? 'checked':''}/>&nbsp;是&nbsp;&nbsp;
	          <input class="cBox" name="salesExperienceOption" type="radio" value="0" ${staffFlowApply.salesExperience == '1' ? '':'checked'}/>&nbsp;否
	    	</td>
	        <td class="tit"><span class="cRed">*</span>是否有金融从业经验</td>
	        <td>
	          <input class="cBox" name="financialExperienceOption" type="radio" value="1" ${staffFlowApply.financialExperience == '1' ? 'checked':''}/>&nbsp;是&nbsp;&nbsp;
	          <input class="cBox" name="financialExperienceOption" type="radio" value="0" ${staffFlowApply.financialExperience == '1' ? '':'checked'}/>&nbsp;否
	    	</td>
	    	<td class="tit"><span class="cRed">*</span>是否有金融从业资格</td>
	        <td>
	          <input class="cBox" name="financialQualificationOption" type="radio" value="1" ${staffFlowApply.financialQualification == '1' ? 'checked':''}/>&nbsp;是&nbsp;&nbsp;
	          <input class="cBox" name="financialQualificationOption" type="radio" value="0" ${staffFlowApply.financialQualification == '1' ? '':'checked'}/>&nbsp;否
	    	</td>
	      </tr>
	      <tr>
	    	<td class="tit"><span class="cRed" valid="workStartDate">*</span>开始工作日期</td>
	        <td>
	          <input id="workStartDate" readonly type="text" class="invokeBoth" style="width:75%" value="<fmt:formatDate value="${staffFlowApply.workStartDate}" pattern="yyyy-MM-dd"/>"/>
	    	</td>
	    	<td class="tit"><span class="cRed" valid="recruitmentType">*</span>招聘方式</td>
	        <td>
	          <input id="hidRecruitmentType" type="hidden" value="${staffFlowApply.recruitmentType}"/>
	          <select id="recruitmentType" class="sel02" onchange="onChangerecruitmentType(this.value);">
	             <option value="">请选择...</option>
	             <option value="网络招聘">网络招聘</option>
	             <option value="内部推荐">内部推荐</option>
	             <option value="招聘会">招聘会</option>
	             <option value="其他">其他</option>
	           </select>
	    	</td>
	    	<td class="tit" id="recommendedTD"></td>
	        <td>
	          <input id="recommended" style="display:none" maxlength="40" type="text" class="ipt01" value="${staffFlowApply.recommended}"/>
	    	</td>
	      </tr>
	      <tr>
	        <td class="tit"><span class="cRed" valid="supervisor">*</span>上级主管</td>
	        <td>
	          <input id="supervisor" maxlength="40" type="text" class="ipt01" value="${staffFlowApply.supervisor}"/>
	    	</td>
	        <td class="tit">司 龄</td>
	        <td>
	          <input id="seniority" disabled maxlength="3" type="text" class="ipt01" value="${staffFlowApply.seniority}"/>
	    	</td>
	    	<td class="tit"><span class="cRed" valid="emergencyContact">*</span>紧急联系人</td>
	        <td>
	          <input id="emergencyContact" maxlength="40" type="text" class="ipt01" value="${staffFlowApply.emergencyContact}"/>
	    	</td>
	      </tr>
	      <tr>
	    	<td class="tit"><span class="cRed" valid="emergencyContactTel">*</span>紧急联系人电话</td>
	        <td>
	          <input id="emergencyContactTel" maxlength="40" type="text" class="ipt01" value="${staffFlowApply.emergencyContactTel}"/>
	    	</td>
	    	<td class="tit"><span class="cRed" valid="bankCardNO">*</span>开户行</td>
	        <td>
	          <input id="bankCardNO" maxlength="40" type="text" class="ipt01" value="${staffFlowApply.bankCardNO}"/>
	    	</td>
	    	<td class="tit"><span class="cRed" valid="bankType">*</span>银行账号</td>
	        <td>
	          <input id="bankType" maxlength="40" type="text" class="ipt01" value="${staffFlowApply.bankType}"/>
	    	</td>
	      </tr>
	  </tbody>
      
      <tr>
        <th colspan="6" onclick="hideTbody('showFlag2','staffInfo2');"><b id="showFlag2">-</b> 家庭信息</th>
      </tr>
      <tbody id="staffInfo2">
        <tr>
	    	<td class="tit"><span class="cRed" valid="nativePlace">*</span>籍 贯</td>
	        <td>
	          <input id="nativePlace" maxlength="40" type="text" class="ipt01" value="${staffFlowApply.nativePlace}"/>
	    	</td>
	    	<td class="tit"><span class="cRed" valid="homeAddr">*</span>住 址</td>
	        <td>
	          <input id="homeAddr" maxlength="40" type="text" class="ipt01" value="${staffFlowApply.homeAddr}"/>
	    	</td>
	        <td class="tit">邮 编</td>
	        <td>
	          <input id="zipCode" maxlength="40" type="text" class="ipt01" value="${staffFlowApply.zipCode}"/>
	    	</td>
	      </tr>
	      <tr>
	    	<td class="tit"><span class="cRed" valid="domicilePlace">*</span>户口所在地</td>
	        <td>
	          <input id="domicilePlace" maxlength="40" type="text" class="ipt01" value="${staffFlowApply.domicilePlace}"/>
	    	</td>
	    	<td class="tit"><span class="cRed" valid="domicileType">*</span>户口性质</td>
	        <td>
	          <input id="hidDomicileType" type="hidden" value="${staffFlowApply.domicileType}"/>
	          <select id="domicileType" class="sel02">
	             <option value="本市城镇">本市城镇</option>
	             <option value="本市农业">本市农业</option>
	             <option value="外埠城镇">外埠城镇</option>
	             <option value="外埠农业">外埠农业</option>
	           </select>
	    	</td>
	        <td class="tit"><span class="cRed" valid="maritalStatus">*</span>婚姻状况</td>
	        <td>
	          <input id="hidMaritalStatus" type="hidden" value="${staffFlowApply.maritalStatus}"/>
	          <select id="maritalStatus" class="sel02">
	             <option value="未婚">未婚</option>
	             <option value="已婚">已婚</option>
	             <option value="离异">离异</option>
	             <option value="丧偶">丧偶</option>
	           </select>
	    	</td>
	      </tr>
	      <tr>
	    	<td class="tit"><span class="cRed">*</span>是否生育</td>
	        <td>
	          <input class="cBox" name="hadKidsOption" type="radio" value="1" ${staffFlowApply.hadKids == '1' ? 'checked':''}/>&nbsp;是&nbsp;&nbsp;
	          <input class="cBox" name="hadKidsOption" type="radio" value="0" ${staffFlowApply.hadKids == '1' ? '':'checked'}/>&nbsp;否
	    	</td>
	    	<td colspan="4">
	    	</td>
	      </tr>
      </tbody>
      
      <tr>
        <th colspan="6" onclick="hideTbody('showFlag3','staffInfo3');"><b id="showFlag3">-</b> 教育信息</th>
      </tr>
      <tbody id="staffInfo3">
        <tr>
	    	<td class="tit"><span class="cRed" valid="education">*</span>学 历</td>
	        <td>
	           <input id="education" maxlength="40" type="text" class="ipt01" value="${staffFlowApply.education}"/>
	    	</td>
	    	<td class="tit"><span class="cRed" valid="finishSchool">*</span>毕业院校</td>
	        <td>
	           <input id="finishSchool" maxlength="40" type="text" class="ipt01" value="${staffFlowApply.finishSchool}"/>
	    	</td>
	        <td class="tit"><span class="cRed" valid="professional">*</span>专 业</td>
	        <td>
	          <input id="professional" maxlength="40" type="text" class="ipt01" value="${staffFlowApply.professional}"/>
	    	</td>
	      </tr>
	      <tr>
	    	<td class="tit"><span class="cRed" valid="degree">*</span>学 位</td>
	        <td>
	          <input id="degree" maxlength="40" type="text" class="ipt01" value="${staffFlowApply.degree}"/>
	    	</td>
	    	<td colspan="4">
	    	</td>
	      </tr>
      </tbody>
      
      <tr>
        <th colspan="6" onclick="hideTbody('showFlag4','staffInfo4');"><b id="showFlag4">-</b> 培训情况</th>
      </tr>
      <tbody id="staffInfo4">
		<tr>
	        <td colspan="6"><textarea id="trainInfo" class="area01">${staffFlowApply.trainInfo}</textarea></td>
		</tr>
      </tbody>
      
      <tr>
        <th colspan="6" onclick="hideTbody('showFlag5','staffInfo5');"><b id="showFlag5">-</b> 专业技能信息</th>
      </tr>
      <tbody id="staffInfo5">
		<tr>
	        <td colspan="6"><textarea id="skillsInfo" class="area01">${staffFlowApply.skillsInfo}</textarea></td>
		</tr>
      </tbody>
      
      <tr>
        <th colspan="6" onclick="hideTbody('showFlag6','staffInfo6');"><b id="showFlag6">-</b> 合同信息</th>
      </tr>
      <tbody id="staffInfo6">
       	<tr>
	        <td class="tit"><span class="cRed" valid="contractType">*</span>合同类型</td>
	        <td>
	           <select id="contractTypeSel" class="sel02" onchange="onChangecontractType(this.value);">
	             <option value="">请选择...</option>
	             <option value="固定劳动合同">固定劳动合同</option>
	             <option value="劳务协议">劳务协议</option>
	             <option value="实习协议">实习协议</option>
	             <option value="ELSE">其他</option>
	           </select>
	           <br/>
	           <span id="contractTypeSpan" style="display:none"><input id="contractType" maxlength="40" type="text" class="ipt01" value="${staffFlowApply.contractType}"/></span>
	    	</td>
	        <td class="tit"><span class="cRed" valid="contractStartDate">*</span>合同开始时间</td>
	        <td>
	          <input id="contractStartDate" readonly type="text" class="invokeBoth" style="width:75%" value="<fmt:formatDate value="${staffFlowApply.contractStartDate}" pattern="yyyy-MM-dd"/>"/>
	    	</td>
	    	<td class="tit"><span class="cRed" valid="contractEndDate">*</span>合同终止日</td>
	        <td>
	          <input id="contractEndDate" readonly type="text" class="invokeBoth" style="width:75%" value="<fmt:formatDate value="${staffFlowApply.contractEndDate}" pattern="yyyy-MM-dd"/>"/>
	    	</td>
		</tr>
      </tbody>
      
      <tr>
        <th colspan="6" onclick="hideTbody('showFlag7','staffInfo7');"><b id="showFlag7">-</b> 工作经历</th>
      </tr>
      <tbody id="staffInfo7">
	      <tr>
	        <td colspan="6" class="tipShow" style="padding:0 6px 20px 6px">
	          <div class="costsListHead">
			  	<div style="width:200px;padding:0 0px 20px 0px">明细信息</div>
			  	<span><input id="workAdd" class="allBtn" type="button" value="新增"/></span>
			  </div>
	      
	          <div class="boxShadow" style="left:5px">
	            <div class="shadow01">
	              <div class="shadow02" >
	                <div class="shadow03" >
	                  <div class="shadow04" >
	                    <div class="shadowCon">
	                      <!--预算信息-->
	                      <div class="ywjgList">
	                        <table width="100%" border="0" cellspacing="1" cellpadding="0">
	                          <tr>
	                            <th width="14%">起始日期</th>
	                            <th width="14%">结束日期</th>
	                            <th width="30%">公司名称</th>
	                            <th width="15%">岗位名称</th>
	                            <th width="15%">岗位职责</th>
	                            <th width="12%">操作</th>
	                          </tr>
	                          <tbody id="workList">
					<c:forEach var="work" items="${staffFlowApply.workExperiences}">
				        		<tr onmouseover="$(this).addClass('over');" onmouseout="$(this).removeClass('over');">
								  <td><fmt:formatDate value="${work.startDate}" pattern="yyyy-MM-dd"/></td>
								  <td><fmt:formatDate value="${work.endDate}" pattern="yyyy-MM-dd"/></td>
								  <td>${work.companyName}</td>
								  <td>${work.postName}</td>
								  <td>${work.postDuty}</td>
								  <td><a href="javascript:void(0);" onclick="cancelInsert(this);" class="opLink">删除</a></td>
								</tr>
					</c:forEach>                          
	                          </tbody>
	                        </table>
	                      </div>
	                      <!--预算信息 end-->
	                    </div>
	                  </div>
	                </div>
	              </div>
	            </div>
	          </div>
	         </td>
	      </tr>
		</tbody>
    </table>
  </div>
  
  <div class="blank"></div>
  <div class="costsLog" id="costsLog">
	<h1 class="logArrow"><a  href="javascript:void(0)">各级审批意见显示</a></h1>
	<div class="mb">
<c:forEach var="task" items="${tasks}" >
	  <div class="processBk">
		<div class="clgcTit">
		  <ul class="clearfix">
			<li><b>处理人：</b>${task.transactorDisplayName }</li>
			<li><b>收到时间：</b><fmt:formatDate value="${task.createTime}" pattern="yyyy-MM-dd HH:mm"/></li>
			<li><b>完成时间：</b><fmt:formatDate value="${task.endTime}" pattern="yyyy-MM-dd HH:mm"/></li>
			<li><b>处理状态：</b>
		<c:if test="${task.taskState=='ps_create'}">
			  <img src="themes/comm/images/stateIcoNone.gif"/>[未查看]
		</c:if>
		<c:if test="${task.taskState=='ps_start'}">
			  <img src="themes/comm/images/stateIco02.gif"/>[已查看,未处理]
		</c:if>
		<c:if test="${task.taskState=='ps_end'}">
		 	  <img src="themes/comm/images/stateIco01.gif"/>[已处理]
		</c:if>
			</li>
		  </ul>
		</div>
		<div class="taskCon nonebb">
		  <div class="handleCon"> ${task.comment } </div>
		</div>
	  </div>
</c:forEach>
	</div>
  </div>
  <div class="blank"></div>
  <div class="addTool2">
  	<table width="100%"  border="0" align="center" cellpadding="0"  cellspacing="1">
	  <tr>
		<td>
		  <input type="hidden" id="taskInstanceID" value="${param.tiid }"/>
		  <input type="hidden" id="transitionName"/>
	<c:if test="${not empty param.notify}"> 
		<span id="notifyUserNames" style="display:none;color:blue;"></span>
		<div class="blank" style="height:5px;"></div>
		<input type="button" value="添加知会人" class="allBtn" onclick="addFlowNotify($('#applicantName').text(),'修改的入职申请', 'RZLC', '/m/staff_arch?act=toview&id=');" style="margin-left:50px"/>&nbsp;&nbsp;&nbsp;&nbsp;
    </c:if>
		  
		  <span id="commitBut">
	<c:forEach var="transition" items="${transitions}" >
			<input class="allBtn" type="button" value="${transition}"/>&nbsp;&nbsp;
	</c:forEach>
		  </span>
		</td>
	  </tr>
  	</table>
  </div>
</div>
</body>
