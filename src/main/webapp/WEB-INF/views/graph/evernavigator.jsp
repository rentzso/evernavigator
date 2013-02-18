<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
  <head>
  <title>EverNavigator</title>
  <!--script src='http://mbostock.github.com/d3/style.css?1.10.0' type='text/css'></script>
  <script src='http://mbostock.github.com/d3/syntax.css?1.6.0' type='text/css'></script-->
	<script src="<c:url value="/resources/lib/jquery.min.js" />"></script>
	<script src="<c:url value="/resources/lib/d3.v2.js" />"></script>
	<script src="<c:url value="/resources/web/fisheye.js" />"></script>
  </head>
<body>

<div class='graph' id='chart'> </div>


<!--div id='noteswindow'>
	<div id="title"></div>
	<ul id='noteslist'></ul>
</div-->

<script src="<c:url value="/resources/lib/json2.js" />"></script>
  <script src="<c:url value="/resources/lib/underscore-1.3.1.js" />"></script>
  <script src="<c:url value="/resources/lib/backbone.js" />"></script>
  <script src="<c:url value="/resources/lib/backbone-localstorage.js" />"></script>
  

<link href="<c:url value="/resources/web/force.css" />" rel='stylesheet' type='text/css' />
<!--script src='web/note_model.js' type='text/javascript'> </script-->


<script src="<c:url value="/resources/web/filters.js" />" type='text/javascript'></script>
<script type='text/javascript'>
	var userId = <%= request.getParameter("userId") %>;                   
</script>
<script src="<c:url value="/resources/web/master_complex.js" />" type='text/javascript'></script>

</body>
</html>