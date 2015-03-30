<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../../../base_path.jsp"></jsp:include>
<script type="text/javascript" src="page/device/paramconf/check/view_item.js"></script>
<title>设备检查配置</title>
</head>
<body class="bdNone">
<div class="tabMid">
  <div class="addCon">
    <table border="0" cellpadding="0"  cellspacing="1" width="100%">
      <tr>
        <th class="tipBg" colspan="2"><img src="themes/comm/images/frameNav2.gif"/>设备检查配置</th>
      </tr>
      <tr>
        <td class="tit">设备类别</td>
        <td width="770">${deviceClass.name }</td>
      </tr>
      <tr>
        <td class="tit">备注</td>
        <td>${deviceClass.deviceCheckItemRemark }</td>
      </tr>
      <tr>
        <td colspan="2" class="tipList">&nbsp;</td>
      </tr>
      <tr>
        <td colspan="2" class="tipShow"><span class="crmSubTabsBk">
          <ul class="crmSubTabs">
            <li class="current default" >
              <div class="lastNone">设备检查项</div>
            </li>
          </ul>
          </span>
          <div class="boxShadow" >
            <div class="shadow01">
              <div class="shadow02" >
                <div class="shadow03" >
                  <div class="shadow04" >
                    <div class="shadowCon">
                      <div id="tab00">
                        	${deviceClass.deviceCheckItemStr }
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div></td>
      </tr>
    </table>
  </div>
  <!--故障编辑 end--> 
</div>
</body>
