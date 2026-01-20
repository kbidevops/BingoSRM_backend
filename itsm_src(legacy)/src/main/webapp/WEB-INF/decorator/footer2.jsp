<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<footer id="footer">
  Copyright ⓒ 2025 KORAILTECH. All Rights Reserved.
</footer>
<script type="text/javaScript" language="javascript" defer="defer">
	<c:if test="${!empty sessionScope.ERROR_MESSAGE}">
		alert('${sessionScope.ERROR_MESSAGE}');
		<%session.removeAttribute("ERROR_MESSAGE");%>
	</c:if>
</script>