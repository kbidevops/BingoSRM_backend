<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>ITSM-<sitemesh:write property='title'/></title>
    <link rel="shortcut icon" href="<c:url value='/images/favicon.ico'/>" type="image/x-icon">
	<link rel="icon" href="<c:url value='/images/favicon.ico'/>" type="image/x-icon">
	<%@include file="/WEB-INF/decorator/cssJs.jsp"%>
    <sitemesh:write property='head'/>
</head>

<body>
	<div class="container">
    	<sitemesh:write property='body'/>
    </div>
    <%@include file="/WEB-INF/decorator/footer.jsp"%>
</body>
</html>
