<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body onload="initCallCenter();">

<OBJECT
	id="oOCX"
	classid="clsid:69FA966F-0F98-4CB5-B73A-78D21F7CFD67"	  
	codebase=""
	width=0
	height=0
	align=center
	hspace=0
	vspace=0>
</OBJECT>

<SCRIPT>
	var ccc_localTelAreaCode = '10';//本地区号
	
	var ccc_ipAddr = '192.168.10.8';//IP地址：内网192.168.10.8 外网119.57.189.34
	var ccc_endPoint = '9004288';//软电话终端:内网9004288 外网9004289
	var ccc_port = '4288';//端口号：内网4288 外网4289
	var ccc_CTIPort = 10002;//CTI端口：内网10002 外网9002
	var ccc_defaultCaller = '53505254';//默认主叫号码
	
//	var ccc_userNO = '${sessionUser.accountID}';//系统用户账号
//	ccc_userNO = ccc_userNO.substr(2);//如QY0001，截去前两位
<%
	
	String staffInfo = org.eapp.crm.util.CallCenterUtils.getStaffInfo(session);
	out.write("var ccc_userNO = '" + staffInfo + "';");
%>
	var ccc_username = '${sessionUser.displayName}';//系统用户姓名
	var ccc_userDept = '${sessionUser.deptNames}';//系统用户部门
	var ccc_userRole = '${sessionUser.deptNames}';//系统用户角色
	
	//挂到top对象
	top.CRM_CALLCENTER_OBJ = new CallcenterObj();
	
	function CallcenterObj() {
		//呼出; callee-被叫号码,calleeAreaCode-被叫区号,caller-主叫号码，
		this.dialout = function(callee, calleeAreaCode, caller){
			if (!caller) {
				caller = ccc_defaultCaller;
			}
			if (calleeAreaCode != ccc_localTelAreaCode && calleeAreaCode != -1) {
				//外地号码加拨0（-1为未知区域，默认本地）
				callee = '0' + callee;
			}
			var dialoutReturn;
			try {
				//设置主叫，呼出时必须设置对应的主叫号码，否则呼出会使用我们平台默认主叫
				//可在初始化的时候设置一次就可以，除非中途需要更改主叫
				oOCX.IMRISetValue(1, "CALLOUT_NBR",caller);
				//呼出动作
		 	//	dialoutReturn=oOCX.IMRIDialout(1,-1,callee,"");
		 		dialoutReturn=oOCX.IMRIDialout(1, -1, callee, 'abcdefghijk1234567890abcdefghijk1234567890')
			} catch (ex) {
				return ex;
			}	
			if(dialoutReturn != 0) {
				var errorInfo;
				switch(dialoutReturn) {
					case 1:
					errorInfo = "对方超时未摘机";
					break;
					case 2:
					errorInfo = "对方忙";
					break;
					case 3:
					errorInfo = "无呼出资源";
					break;
					case 4:
					errorInfo = "系统错误";
					break;
					case 10:
					errorInfo = "当前状态不能拨电话";
					break;
					default:
						errorInfo = "未知错误";
				}
				alert(errorInfo + "("+ dialoutReturn + ")，被叫电话" + callee);
			}
		};
	
		//示闲
		this.showIdle = function(){
			oOCX.IMRISetLocalState(1, 1);
		}
		//示忙
		this.showBusy = function(){
			oOCX.IMRISetLocalState(1, 2);
		};
		
		//挂机
		this.handUp = function(){
			oOCX.IMRIClearCall(1, 0);
		};
		//静音
		this.showMute = function(){
			oOCX.IMRIMuteOnLink(1, 1);
		};
		//取消静音
		this.cancelMute = function(){
			oOCX.IMRIMuteOnLink(1, 0);
		};
		
		this.exitCallCenter = function() {
			exitCallCenter();
		};
	}
	
	//初始化控件，页面加载后调用
	function initCallCenter() {
		try {
			var retCode = oOCX.IMRIOpenImr();
			if (retCode != 0) {
				alert("统一版本打开IMR，出错代码:" + retCode);
				return;
			}
			retCode = setAllValue();
			if (retCode != 0) {
				alert("统一版本设置变量，出错代码:" + retCode);
				return;
			}
			retCode = oOCX.IMRISetTerminalBaseEx(1, ccc_ipAddr, ccc_ipAddr, ccc_port, 1,2,1, ccc_CTIPort);
			if (retCode != 0) {
				alert("统一终端初始参数，出错代码:" + retCode);
				return;
			}
			retCode = oOCX.IMRISetValue(1,'PHONE_TYPE', '6');
			if (retCode != 0) {
				alert("设置IPPhoneTYPE，出错代码:" + retCode);
				return;
			}
			retCode = oOCX.IMRISetTerminalIpAgent(1,ccc_ipAddr + ':5061',1, ccc_endPoint + '/' + ccc_port);
			if (retCode != 0) {
				alert("设置IPPhone参数，出错代码:" + retCode);
				return;
			}
			retCode = oOCX.IMRIInitIpAgent(1);
			if (retCode != 0) {
				alert("设置初始化IPPhone，出错代码:" + retCode);
				return;
			}
			retCode = oOCX.IMRIConnectServer(1);
			if (retCode != 0) {
				alert("统一版本连接，出错代码:" + retCode);
				return;
			}
			retCode = oOCX.IMRITermRegister(1,'', '', 2,'' ,'','');
			if (retCode != 0) {
				alert("统一版本终端注册，出错代码:" + retCode);
				return;
			}
			retCode = oOCX.IMRISetSkill(1,ccc_userNO,ccc_username,1,getSkillString(),ccc_userDept,ccc_userRole);
			if (retCode != 0) {
				alert("统一版本注册技能，出错代码:" + retCode);
				return;
			}
		} catch(e){
//			alert("初始化CALLCENTER的OCX出错");
		}
	}
	
	//退出CALLCENTER控件，页面关闭时调用
	function exitCallCenter() {
		try {
			var retCode = oOCX.IMRITermUnregister(1);
			if (retCode != 0) {
				alert("注销终端，出错代码:" + retCode);
				return;
			}
			retCode = oOCX.IMRITellIMRExit();
			if (retCode != 0) {
				alert("退出IMR，出错代码:" + retCode);
				return;
			}
		} catch(e){
//			alert("退出CALLCENTER的OCX出错");
		}
	}
	
	function getSkillString() {
		var sSkillString="";
		sSkillString=sSkillString+String.fromCharCode(255);		//0技能组技能级别0
		sSkillString=sSkillString+String.fromCharCode(10);	//1技能组技能级别10
		sSkillString=sSkillString+String.fromCharCode(255);		//2技能组技能级别0，大于等于32即为0
		sSkillString=sSkillString+String.fromCharCode(255);		//3技能组技能级别0
		sSkillString=sSkillString+String.fromCharCode(21);	//4技能组技能级别21
		return sSkillString;
	}
	
	function setAllValue() {
		oOCX.IMRISetValue(1,'DEPARTMENT_ID','2070');
		oOCX.IMRISetValue(1,'AREA_ID','010');
		oOCX.IMRISetValue(1,'TIME_OUT_TIMES','3');
		oOCX.IMRISetValue(1,'CALLOUT_NBR','53505254');	//主叫号码配置
		oOCX.IMRISetValue(1,'AUTO_ACCEPT_CALL','1');
		oOCX.IMRISetValue(1,'ANSWER_MODE','1');
		oOCX.IMRISetValue(1,'AUTO_CLEAR_CALL','1');	
		oOCX.IMRISetValue(1,'SERVICE_CODE','00');
		oOCX.IMRISetValue(1,'sAni','10000');
		oOCX.IMRISetValue(1,'sDni','9004288');					//接听号码配置，内网9004288 外网9004289
		oOCX.IMRISetValue(1,'nTermIDoutbound','4256');
		oOCX.IMRISetValue(1,'nTermIDRec','4160');
		oOCX.IMRISetValue(1,'nTermIDDtmf','4224');
		oOCX.IMRISetValue(1,'nTermIDVox','4224');
		oOCX.IMRISetValue(1,'nTermIDFax','4224');
		oOCX.IMRISetValue(1,'nTermIDConf','2047');
		oOCX.IMRISetValue(1,'nAnswerInterval','10');
		oOCX.IMRISetValue(1,'lTimeOutTimes','3');
		oOCX.IMRISetValue(1,'nDnisLimited','2');
		oOCX.IMRISetValue(1,'bLogPTI2','1');
		oOCX.IMRISetValue(1,'bLogAPP','1');
		oOCX.IMRISetValue(1,'LISTEN_TYPE','2');
		return 0;
	}
</SCRIPT>

<SCRIPT for="oOCX" event="OnIMRIStateChanged(nObjectId,nOldState, nNewState)">
	if (nNewState == 0x15) {
		oOCX.IMRIClearCall(1, 0);
//		alert("进入HANUP状态，自动挂机!");
		return;
	} 
/*	var msg = "<br>OnIMRIStateChanged   nObj=" + nObjectId
     +"   nOldSt=" + nOldState
     +"   nNewSt=" + nNewState
     +"   Sess=" + oOCX.IMRIGetValue(0,'SESSION_NO')
     +"   SubSess=" + oOCX.IMRIGetValue(0,'SEQUENCE')
     +"   CallType=" + oOCX.IMRIGetValue(0,'CALL_TYPE')
     +"   CallerTerm=" + oOCX.IMRIGetValue(0,'SRC_TERMINAL_ID')
     +"   ANI=" + oOCX.IMRIGetValue(0,'CALLING_NBR')
     +"   DNIS=" + oOCX.IMRIGetValue(0,'CALLED_NBR')
     +"   ORG_DNIS=" + oOCX.IMRIGetValue(0,'ORG_CALLED_NBR')
     +"   InComing=" + oOCX.IMRIGetValue(0,'INCOMING_TIME')
     +"   Calling=" + oOCX.IMRIGetValue(0,'CALLING_TIME')
     +"   Answer=" + oOCX.IMRIGetValue(0,'ANSWER_TIME')
     +"   OnHook=" + oOCX.IMRIGetValue(0,'ONHOOK_TIME')
     +"   Free=" + oOCX.IMRIGetValue(0,'FREE_TIME')
     +"   OnhookType=" + oOCX.IMRIGetValue(0,'ONHOOK_TYPE');*/
</SCRIPT>

<SCRIPT for="oOCX" event="OnIMRICallBreak(nObjectId,psSessionNo)">
/*    myarea3.innerHTML=myarea3.innerHTML
     +"<br>OnIMRICallBreak   nObjectId=" + nObjectId + ",   psSessionNo=" + psSessionNo;*/
</SCRIPT>

<SCRIPT for="oOCX" event="OnIMRICallIn(nObjectId, psSessionNo, nSubSessionNo, nCallerTerminalId, nCalleeTerminalId, psANIS, psDNIS1, psDNIS2, nCallerUserType, nCallType, nAnswerMode, psShortData);">
/*    myarea2.innerHTML = myarea2.innerHTML
     +"<br>OnIMRICallIn   nObjectId=" + nObjectId + ",   psSessionNo=" + psSessionNo
     +"   nSubSessionNo=" + nSubSessionNo
     +"   nCallerTerminalId=" + nCallerTerminalId
     +"   nCalleeTerminalId=" + nCalleeTerminalId
     +"   psANIS=" + psANIS
     +"   psDNIS1=" + psDNIS1
     +"   psDNIS2=" + psDNIS2
     +"   nCallerUserType=" + nCallerUserType
     +"   nCallType=" + nCallType
     +"   nAnswerMode=" + nAnswerMode
     +"   psShortData=" + psShortData;*/
</SCRIPT>

<SCRIPT for="oOCX" event="OnIMRIDialOutResp(nObjectId, psSessionNo, nSubSessionNo, nCallerTerminalId, nCalleeTerminalId, psANIS, psDNIS1, psDNIS2, nCallerUserType, nCallType, nAnswerMode, psDialTime, psAnswerTime, psShortData);">
/*    myarea4.innerHTML = myarea4.innerHTML
     +"<br>OnIMRIDialOutResp   nObjectId=" + nObjectId + ",   psSessionNo=" + psSessionNo
     +"   nSubSessionNo=" + nSubSessionNo
     +"   nCallerTerminalId=" + nCallerTerminalId
     +"   nCalleeTerminalId=" + nCalleeTerminalId
     +"   psDNIS1=" + psDNIS1
     +"   psDNIS2=" + psDNIS2
     +"   nCallerUserType=" + nCallerUserType
     +"   nCallType=" + nCallType
     +"   nAnswerMode=" + nAnswerMode
     +"   psDialTime=" + psDialTime
     +"   psAnswerTime=" + psAnswerTime
     +"   psShortData=" + psShortData;*/
</SCRIPT>

<SCRIPT for="oOCX" event="OnIMRICallEnd(nObjectId, psSessionNo, nSubSessionNo, nCallerTerminalId, nCalleeTerminalId, psANI, psDNIS1, psDNIS2, nCallType, nAnswerMode, psTime, nOnHookType, nMuteTime);">
/*    myarea50.innerHTML = myarea50.innerHTML
     +"<br>OnIMRICallEnd   nObjectId=" + nObjectId
     +"   psSessionNo=" + psSessionNo
     +"   nSubSessionNo=" + nSubSessionNo
     +"   nCallerTerminalId=" + nCallerTerminalId
     +"   nCalleeTerminalId=" + nCalleeTerminalId
     +"   psANI=" + psANI
     +"   psDNIS1=" + psDNIS1
     +"   psDNIS2=" + psDNIS2
     +"   nCallType=" + nCallType
     +"   nAnswerMode=" + nAnswerMode
     +"   psTime=" + psTime
     +"   nOnHookType=" + nOnHookType
     +"   nMuteTime=" + nMuteTime; */
</SCRIPT>


<input type="button" value="呼叫外线电话" onclick="top.CRM_CALLCENTER_OBJ.dialout(document.getElementById('callee').value),'10',document.getElementById('caller').value;">&nbsp;&nbsp;
<input type="button" value="示闲" onclick="top.CRM_CALLCENTER_OBJ.showIdle();">&nbsp;&nbsp;
<input type="button" value="示忙" onclick="top.CRM_CALLCENTER_OBJ.showBusy();">&nbsp;&nbsp;
<input type="button" value="挂机" onclick="top.CRM_CALLCENTER_OBJ.handUp();">&nbsp;&nbsp;
<input type="button" value="静音" onclick="top.CRM_CALLCENTER_OBJ.showMute();">&nbsp;&nbsp;
<input type="button" value="取消静音" onclick="top.CRM_CALLCENTER_OBJ.cancelMute();">&nbsp;&nbsp;
<input type="button" value="统一版本查询终端" onclick="alert(oOCX.IMRISetQueryTerminalState(1,0,3,-1));">
<input type="button" value="统一版本取消查询终端" onclick="alert(oOCX.IMRISetQueryTerminalState(1,1,3,-1));">

<br/></br>
<input type="button" value="注销" onclick="exitCallCenter();">


主叫号码：<input type="text" value="53505254" id="caller"/>&nbsp;&nbsp;
被叫号码：<input type="text" value="13859953200" id="callee"/>&nbsp;&nbsp;

</body>
</html>

