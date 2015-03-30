<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<jsp:include page="../../base_path.jsp"></jsp:include>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<title>添加图片</title>
	<script type="text/javascript" src="commui/jwysiwyg/ajaxfileupload.js"></script>
	<script type="text/javascript" src="page/knowledge/kb/dialog_addimg.js"></script>

</head>
<body class="bdDia" style="overflow-y:auto">  
    <div>
      <div id="tab00">
        <div class="addCon">
           <table cellspacing="1" cellpadding="0" border="0" align="center" width="100%">
                  <tbody>
                  <tr>
                  <td style="height:auto">
                     图片选择
                    <!--图片选择-->
                    <div class="selPic" id="selPic">
                     
                    </div>
                    <!--图片选择 end-->
                  </td>
                  </tr>
             </tbody>
             </table>
        </div>
      </div>
    </div>
<!--选项卡 end-->  

<div class="upLoadPic">
<form enctype="multipart/form-data" action="m/knowledge" name="imgForm" id="imgForm" method="post" >
<div class="fileinputs" style="float:right;width:auto;width:70px">
    <input type="file"  class="file hidden" noscript="true" size="1" id="uploadImg" name="imgfile" onchange="ajaxFileUpload();"/>
    <div class="fakefile">
        <img src="themes/comm/images/upload.gif"/>
    </div>
</div>

	<input type="hidden" id="referid" name="referid"/>
	<input type="hidden" id="act" name ="act" value="uploadimage"/>
</form>
<div id="fileinfo"></div>
</div>
<div class="addTool2">
	<input name="" type="button" value="确定" class="allBtn" id="selImg"/>
	<input name="" type="button" value="取消" class="allBtn" onclick="window.close();"/>
</div>
</body>
</html>