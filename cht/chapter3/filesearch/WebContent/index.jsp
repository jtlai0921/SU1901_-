<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.util.Calendar"%>
<%
	Calendar cal = Calendar.getInstance();
	int year = cal.get(Calendar.YEAR); //取得年份
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>lucene檔案檢索</title>
<link type="text/css" rel="stylesheet" href="css/index.css">
</head>
<body>
	<div class="indexbox">
		<div class="logo">
			<a href="index.jsp">
				<img alt="檔案檢索" src="images/logo.png">
			</a>
		</div>
		<div class="searchform">
			<form action="SearchFile" method="get" >
				<input type="text" name="query"> <input type="submit"
					value="搜尋">
			</form>
		</div>
		<div class="info">
			<p>基於Lucene的檔案檢索系統</p>
			<br />
			<p>&copy;<%=year > 2016 ? (2016 + "-" + year) : year%> 清華大學出版社 All rights Reserved</p>
		</div>
	</div>
</body>
</html>
