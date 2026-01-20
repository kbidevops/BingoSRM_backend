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
	<nav class="navbar navbar-expand-md navbar-dark fixed-top bg-dark">
      <a class="navbar-brand col-md-1" href="<c:url value="/main/index.do"/>"><i class="fa fa-thumbs-o-up" aria-hidden="true"></i> ITSM</a>
<!--       <button class="navbar-toggler d-lg-none" type="button" data-toggle="collapse" data-target="#navbarsExampleDefault" aria-controls="navbarsExampleDefault" aria-expanded="false" aria-label="Toggle navigation"> -->
<!--         <span class="navbar-toggler-icon"></span> -->
<!--       </button> -->

      <div class="collapse navbar-collapse col-md-7" id="navbarsExampleDefault">
        <ul class="navbar-nav mr-auto">
          <c:forEach items="${TOP_MENU_LIST}" var="topMenu">
          	<c:if test="${!empty topMenu.progrmAccesAuthorCode}">
	          	<li class="nav-item${topMenu.progrmSn eq TOP_MENU_PROGRMACCESAUTHORVO.progrmSn ? ' active' : ''}">
		           <a class="nav-link" href="<c:url value='${topMenu.progrmUri}'/>"><c:out value="${topMenu.progrmNm}"/>${topMenu.progrmSn eq TOP_MENU_PROGRMACCESAUTHORVO.progrmSn ? '<span class="sr-only">(current)</span>' : ''}</a>
		        </li>	
	        </c:if>
          </c:forEach>
        </ul>
      </div>
      
<!--       <div class="nav-item"> -->
<!--         	<a class="nav-item" href="#">회원정보수정</a> -->
<!--         	<a class="nav-item" href="#">로그아웃</a> -->
<!--         </div> -->
		
		<c:if test="${!empty sessionScope.LOGIN_USER_VO and sessionScope.LOGIN_USER_VO.userTyCode ne sessionScope.LOGIN_USER_VO.USER_TY_CODE_TEMP}">
	        <ul class="navbar-nav mr-auto text-right">
			  <li class="nav-item">
			    <span class="nav-link text-warning"><c:out value="${sessionScope.LOGIN_USER_VO.userNm}"/></span>
			  </li>
			  <li class="nav-item">
			    <a class="nav-link" href="<c:url value="/user/site/updateView.do"/>"><i class="fa fa-user" aria-hidden="true"></i>회원정보</a>
			  </li>
			  <li class="nav-item">
			    <a class="nav-link" href="<c:url value="/login/site/loginOut.do"/>"><i class="fa fa-sign-out" aria-hidden="true"></i>로그아웃</a>
			  </li>
			</ul>
		</c:if>
		
    </nav>
    <div class="container-fluid">
      <div class="row">
        <nav class="col-sm-3 col-md-2 d-none d-sm-block bg-light sidebar">
          <ul class="nav nav-pills flex-column">
          	<c:forEach items="${LEFT_MENU_LIST}" var="leftMenu">
          		<c:if test="${!empty leftMenu.progrmAccesAuthorCode}">
	          		<li class="nav-item${leftMenu.progrmSn == LEFT_MENU_PROGRMACCESAUTHORVO.progrmSn ? ' current active' : ''}">
		              <a class="nav-link" href="<c:url value="${leftMenu.progrmUri}"/>"><c:out value="${leftMenu.progrmNm}"/>${leftMenu.progrmSn == LEFT_MENU_PROGRMACCESAUTHORVO.progrmSn ? '<span class="sr-only">(current)</span>' : ''}</a>
		            </li>
	            </c:if>
          	</c:forEach>
          </ul>          
        </nav>
	
		
		<main class="col-sm-9 ml-sm-auto col-md-10 pt-3" role="main" id="mainDiv">
          <sitemesh:write property='body'/>
        </main>
        
        
      </div>
      
    </div>
    <%@include file="/WEB-INF/decorator/footer.jsp"%>
</body>
</html>
