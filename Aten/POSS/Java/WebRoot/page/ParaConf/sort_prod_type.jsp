<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!--群组 排序-->
<div class="addCon">
<table width="100%" align="center" border="0" cellspacing="1" cellpadding="0" class="tabNone">

  <tr>
	<td>
	   <div class="dzbd">
	   <table width="300" align="center" border="0" cellspacing="0" cellpadding="0" class="tabNone">
		  <tr>
			<td width="230">
			<select id="subProdType" name="subProdType" size="1" multiple="multiple" class="dzbdSel01" style="height:295px">
			</select>
			</td>
			<td width="70">
			   <input id="setTop" name="setTop" type="button" value="移到顶部" class="allBtn pxBtn"/><br />
			   <input id="setUp" name="setUp" type="button" value="上移" class="allBtn pxBtn"  /><br />
			   <input id="setDown" name="setDown" type="button" value="下移" class="allBtn pxBtn" /><br />
			   <input id="setBottom" name="setBottom" type="button" value="移到底部" class="allBtn pxBtn"/>							</td>
		  </tr>
		</table>
	   </div>
		<div class="blank" style="height:10px"></div>
	</td>
  </tr>
</table>
		<div class="addTool2">
		<input id="saveOrder" name="saveOrder" type="button" value="保存" class="allBtn"/>
		<input id="reset" name="reset" type="reset" value="重置" class="allBtn"/>
		</div>
</div>
<!--群组  排序 end-->
<script type="text/javascript" src="page/ParaConf/sort_prod_type.js"></script>