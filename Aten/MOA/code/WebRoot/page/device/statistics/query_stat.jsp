<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/eapp.tld" prefix="oa" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<jsp:include page="../../base_path.jsp"></jsp:include>
<meta http-equiv="Content-Language" content="en" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="${ermpPath}jqueryui/select/jquery.select.js"></script>
<script type="text/javascript" src="${ermpPath}jqueryui/datepicker/ui.datepicker.js"></script>
<script type="text/javascript" src="page/device/statistics/field_info.js"></script>
<script type="text/javascript" src="page/device/statistics/query_stat.js"></script>
<title></title>
</head>
<body class="oaBd">
<input id="hidModuleRights" type="hidden" value="<oa:right key='inventory_equip'/>"/>
<input type="hidden" id="expNameAndValue" value=""/>
<table border="0" cellspacing="0" cellpadding="0" width="100%" >
  <tr>
    <td>
      <div>
          <div class="soso" style="height:auto">
            <div class="t01"  style="height:auto">
              <table border="0" cellpadding="0" cellspacing="0"> 
                <tr>
                  <td class="soPaddLeft">财务隶属：</td>
                  <td><input id="deviceClassDiv0_name" type="text" maxlength="50" value="所有..." readonly="readonly" class="ipt05" style="width:85px"/><input id="openUserSelect" class="selBtn" type="button" onclick="deviceClass('deviceClassDiv0_name','deviceClassDiv0')"/>
                  	<input id="deviceClassDiv0_ids" type="hidden"/>&nbsp;
                  </td>
                  <td class="soPaddLeft">资产类别：</td>
                  <td><input id="deviceClassDiv_name" type="text" maxlength="50" value="所有..." class="ipt05" readonly="readonly" style="width:118px"/><input id="openUserSelect" class="selBtn" type="button" onclick="deviceClass('deviceClassDiv_name','deviceClassDiv')"/>
                  	<input id="deviceClassDiv_ids" type="hidden"/>&nbsp;
                  </td>
                  <td class="soPaddLeft">设备类别：</td>
                  <td><input id="deviceClassDiv2_name" type="text" maxlength="50" value="所有..." class="ipt05" readonly="readonly" style="width:118px"/><input id="openUserSelect" class="selBtn" type="button" onclick="deviceClass('deviceClassDiv2_name','deviceClassDiv2')"/>
                  <input id="deviceClassDiv2_ids" type="hidden"/>&nbsp;
                  </td>
                  <td class="soPaddLeft">设备状态：</td>
                  <td><input id="deviceClassDiv3_name" type="text" maxlength="50" value="所有..." class="ipt05" readonly="readonly" style="width:118px"/><input id="openUserSelect" class="selBtn" type="button" onclick="deviceClass('deviceClassDiv3_name','deviceClassDiv3')"/>
                  	<input id="deviceClassDiv3_ids" type="hidden"/>
                  </td>
                </tr>
                <tr>
                  <td class="soPaddLeft">年度：</td>
                  <td><div id="yearSelectDiv" name="tjnd">
                    </div></td>
                  <td class="soPaddLeft">购买日期：</td>
                  <td colspan="5">从
                    <input type="text" id="stratBuyTime" maxlength="100" readonly="readonly" class="invokeBoth"  style="width:65px" />
                    到
                    <input maxlength="100" id="endBuyTime" readonly="readonly" type="text" class="invokeBoth"  style="width:65px" />
                  
                  <a href="javascript:void(0)" onclick="fieldDisplaySet()">【定制显示字段】</a> &nbsp;
                    <input id="queryEquipment" class="allBtn" type="button" value="搜索"/>
                    &nbsp;
                    <input id="exportExcel" class="allBtn" type="button" value="导出Excel" style="width:66px"/></td>
                </tr>
              </table>
            </div>
          </div>
      </div>
    </td>
  </tr>
  <tr>
    <td>
      <div id="divTable" class="allList" style="width:100%">
      </div>
     <!--翻页-->
<input type="hidden" id="hidNumPage" value="1"/>
<input type="hidden" id="hidPageCount" value="15"/> 
<div class="pageNext">
</div>
<!--翻页 end-->

    </td>
  </tr>
</table>
<br/><br/><br/><br/><br/>
<!--所属地区 弹出层 -->
<div class="boxShadow" id="deviceClassDiv0" style="display:none; width:120px;" >
  <div class="shadow01">
    <div class="shadow02" >
      <div class="shadow03" >
        <div class="shadow04" >
          <div class="shadowCon">
        	<span id="deviceClassDiv0_span">
          	  <input type="checkbox" id="darea01" class="cBox" name="exp1" onclick="checkAll('darea01','deviceClassDiv0_span')"/><label for="darea01"><span id="deviceClassDiv03">全部</span></label><br />
    <c:forEach items="${companyAreas }" var="area">
			  <input type="checkbox" id="dc0${area.dictCode}" class="cBox" name="exp" value="${area.dictCode}" onclick="deletecheckAll('dc0${area.dictCode}','deviceClassDiv0')"/><label for="dc0${area.dictCode}"><span id="deviceClassDiv0${area.dictCode}">${area.dictName }</span></label><br />
	</c:forEach>
       		</span>
          	<div class="addTool2" style="padding:18px 0 0">
           	  <input id="saveProblem" type="button" value="确定" class="allBtn" onclick="saveDevice('deviceClassDiv0')"/>
           	  <input id="selClose" type="button" value="关闭"    class="allBtn" onclick="closeDevice('deviceClassDiv0')"/>
          	</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<!--所属地区 弹出层 end-->

<!--资产类别 弹出层 -->
<div class="boxShadow" id="deviceClassDiv" style="display:none; width:158px;" >
  <div class="shadow01">
    <div class="shadow02" >
      <div class="shadow03" >
        <div class="shadow04" >
          <div class="shadowCon">
          		<span id="deviceClassDiv_span"></span>
          		<div class="addTool2" style="padding:18px 0 0">
                  <input id="saveProblem" type="button" value="确定" class="allBtn" onclick="saveDevice('deviceClassDiv')"/>
                  <input id="selClose" type="button" value="关闭"    class="allBtn" onclick="closeDevice('deviceClassDiv')"/>
                </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<!--设备类别 弹出层 end-->
<!--设备类型 弹出层 -->
<div class="boxShadow" id="deviceClassDiv2" style="display:none; width:158px;" >
  <div class="shadow01">
    <div class="shadow02" >
      <div class="shadow03" >
        <div class="shadow04" >
          <div class="shadowCon">
               <span id="deviceClassDiv2_span"></span>
          		<div class="addTool2" style="padding:18px 0 0">
                  <input id="saveProblem" type="button" value="确定" class="allBtn" onclick="saveDevice('deviceClassDiv2')"/>
                  <input id="selClose" type="button" value="关闭"    class="allBtn" onclick="closeDevice('deviceClassDiv2')"/>
                </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<!--设备类型 弹出层 end-->
<!--设备状态 弹出层 -->
<div class="boxShadow" id="deviceClassDiv3" style="display:none; width:158px;" >
  <div class="shadow01">
    <div class="shadow02" >
      <div class="shadow03" >
        <div class="shadow04" >
          <div class="shadowCon">
               <span id="deviceClassDiv3_span"></span>
          		<div class="addTool2" style="padding:18px 0 0">
                  <input id="saveProblem" type="button" value="确定" class="allBtn" onclick="saveDevice('deviceClassDiv3')"/>
                  <input id="selClose" type="button" value="关闭"    class="allBtn" onclick="closeDevice('deviceClassDiv3')"/>
                </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<!--设备状态 弹出层 end-->

</body>
</html>