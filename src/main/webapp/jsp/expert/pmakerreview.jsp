<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>创客项目作品评审</title>

<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/css/table.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/js/jquery-easyui-1.4.4/themes/icon.css" />
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/js/jquery-easyui-1.4.4/themes/gray/easyui.css" />
<script
	src="<%=request.getContextPath()%>/js/jquery-easyui-1.4.4/jquery.min.js"
	type="text/javascript"></script>
<script
	src="<%=request.getContextPath()%>/js/jquery-easyui-1.4.4/jquery.easyui.min.js"
	type="text/javascript"></script>
<script
	src="<%=request.getContextPath()%>/js/jquery-easyui-1.4.4/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	
	
	var ctime="2100-1-1";
	
	$("#ctime").datebox("setValue", ctime); 
});
function save(){
	if(confirm("确认保存检查意见？")){
		document.getElementById("sort").value="save";
		return true;
	}
	else{
		return false;
	}
}
function sub(){
	var ctime=$("#ctime").datebox("getValue");
	var presention=document.getElementById("presention").value;
	var advice=document.getElementById("advice").value;
	if(ctime==""){
		alert("提示：\n\n请填写评审日期！");
		return false;
	}
	if(presention==""){
		alert("提示：\n\n请填写专家意见与评述！");
		return false;
	}
	if(advice==""){
		alert("提示：\n\n请填写主要问题及建议！");
		return false;
	}
	if(confirm("确认提交回复？")){
		document.getElementById("sort").value="submit";
		return true;
	}
	else{
		return false;
	}
}
</script>

</head>

<body>
	<div class="commonPage">

		<div class="topnav">
			<div class="path">
				<span><font>当前位置:</font></span> <span><font>项目评审&nbsp;&nbsp;
						&gt;&nbsp;&nbsp; 创客项目作品评审</font></span>
			</div>
		</div>
		<% String urlString=request.getContextPath()+"/maker/evaluate?id="+request.getParameter("id"); %>
		<div class="context">
			<div class="titlebox">
				<span class="title">创客项目作品评审</span>
			</div>
			<div>
				<form method="post" action=<%=urlString%> name="Form1" id="Form1">
					<input type="hidden" name="sort" id="sort">
					<table class="persional" align="center" border="1" cellpadding="0"
						cellspacing="0" bordercolor="#DEE5EA" >
						<tr>
							<td class="tdname">创新成果名称：</td>
							<td class="tdcontent1">${detail.title}</td>
							<td class="tdname">团队名称：</td>
							<td class="tdcontent2">${detail.id}</td>
						</tr>
						<tr>
							<td class="tdname">所属创新项目：</td>
							<td class="tdcontent1">${detail.makerProjectID}</td>
							<td class="tdname">技术领域：</td>
							<td class="tdcontent2">${detail.field}</td>
						</tr>

						<tr>
							<td class="tdname">提交日期：</td>
							<td class="tdcontent1">${detail.submitTime}</td>
							<td style="background-color: #F2F2F2; width: 15%" colspan="2">
								<div align="center">
									<a href="#" class="easyui-linkbutton button"
										style="width: 120px; height: 25px">相关附件下载</a>
								</div>
							</td>
						</tr>
						<tr>
							<td class="tdname" align="center">创新成果简述</td>
							<td colspan="3">
								<div class="tdcontent3">
									<div
										style="word-wrap: break-word; word-break: break-all; margin: 0 auto; width: 95%;">
										<pre
											style="width: 100%; white-space: pre-wrap; font-size: 16px"> ${detail.description}</pre>
									</div>
								</div>
						</tr>
						<tr>
							<td class="tdname" align="center" height="100px">专家意见与评述</td>
							<td align="center" height="100px" colspan="3">
								<div class="tdcontent3">
									<textarea id="presention" name="presention" cols="40" rows="12"
										style="width: 90%; height: 100%;"> ${detail.evaluation}</textarea>
								</div>
						</tr>

						<tr>
							<td class="tdname" align="center">主要问题及建议</td>
							<td align="center" height="400px" colspan="3">
								<div class="tdcontent3">
									<textarea id="advice" name="advice" cols="40" rows="12"
										style="width: 90%; height: 100%;">${detail.problems}</textarea>
								</div>
							</td>
						</tr>

						<tr>
							<td align="center" colspan="4">
								<div
									style="margin-top: 20px; margin-left: auto; margin-right: auto; margin-bottom: 20px">
									<a href="javascript:void(document.Form1.submit())"
										style="height: 35px; width: 70px; font-size: 16px;"
										class="easyui-linkbutton button" onclick="return sub();">提&nbsp;&nbsp;交</a>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<a href="javascript:void(document.Form1.submit())"
										style="height: 35px; width: 70px; font-size: 16px"
										class="easyui-linkbutton button" onclick="return save();">保&nbsp;&nbsp;存</a>
								</div>
							</td>
						</tr>
					</table>
				</form>
			</div>

		</div>

	</div>
</body>
</html>