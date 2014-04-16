<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Comparative study of Ranking algorithms</title>
</head>
<body>
	<h1 align="center">
		Comparative study of Ranking algorithms
	</h1>

	<form action="SearchServlet" method="get">
		<input type="text" name="searchbox" size="50" maxlength="100" style="margin-left:400px">
		<input type="submit" value="Search">	
		<br><br><br>
		<table style="border:2px solid black" align="center">
		<tr>
		<th>
		TFIDF Algorithm
		</th>
		<th>
		PageRank Algorithm
		</th>
		</tr>
		<%if(request.getAttribute("tfidfLink0")!=null)
		for(int i=0;i<10;i++){%>
		<tr>
		<td style="padding-left:30px">
		<a href="http://en.wikipedia.org/wiki/<%= request.getAttribute("tfidfLink"+i)%>" target="_blank">
		<%= request.getAttribute("tfidfLink"+i)%>
		</a>
		</td>
		<td style="padding-right:30px;padding-left:25px">
		<a href="http://en.wikipedia.org/wiki/<%= request.getAttribute("pagerankLink"+i)%>" target="_blank">
		<%= request.getAttribute("pagerankLink"+i)%>
		</a>
		</td>
		</tr>
		<%}%>
		<tr>
		<td>
		<br></td>
		</tr>
		</table>
	</form>
</body>
</html>