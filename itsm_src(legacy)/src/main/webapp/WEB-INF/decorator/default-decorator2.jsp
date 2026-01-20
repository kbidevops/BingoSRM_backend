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
	<%@include file="/WEB-INF/decorator/cssJs.jsp"%>
    <sitemesh:write property='head'/>
</head>
	

<body>
	<nav class="navbar navbar-expand-md navbar-dark fixed-top bg-dark">
      <a class="navbar-brand" href="#">Dashboard</a>
      <button class="navbar-toggler d-lg-none" type="button" data-toggle="collapse" data-target="#navbarsExampleDefault" aria-controls="navbarsExampleDefault" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>

      <div class="collapse navbar-collapse" id="navbarsExampleDefault">
        <ul class="navbar-nav mr-auto">
          <li class="nav-item active">
            <a class="nav-link" href="#">Home <span class="sr-only">(current)</span></a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#">Settings</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#">Profile</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#">Help</a>
          </li>
        </ul>
<%--         <form class="form-inline mt-2 mt-md-0"> --%>
<!--           <input class="form-control mr-sm-2" type="text" placeholder="Search" aria-label="Search"> -->
<!--           <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button> -->
<%--         </form> --%>
      </div>
    </nav>
    
    <div class="container-fluid">
      <div class="row">
        <nav class="col-sm-3 col-md-2 d-none d-sm-block bg-light sidebar">
          <ul class="nav nav-pills flex-column">
            <li class="nav-item">
              <a class="nav-link active" href="<c:url value="/user/mngr/retrievePagingList.do"/>">사용자 관리 <span class="sr-only">(current)</span></a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="<c:url value="/progrm/mngr/retrieveTreeList.do"/>">menu</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="<c:url value="/progrmaccesauthor/mngr/retrieveTreeList.do"/>">progrmaccesauthor</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="<c:url value="/syscharger/mngr/retrieveList.do"/>">syscharger</a>
            </li>
          </ul>

          <ul class="nav nav-pills flex-column">
            <li class="nav-item">
              <a class="nav-link" href="<c:url value="/srvcrspons/mngr/retrievePagingList.do"/>">Nav item  SR MM</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="<c:url value="/login/site/loginView.do"/>">login again</a>
            </li>
            
            <li class="nav-item">
              <a class="nav-link" href="<c:url value="/srvcrspons/site/retrievePagingList.do"/>">sr list</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="<c:url value="/srvcrspons/site/retrieveList.do"/>">sr add</a>
            </li>
          </ul>

          <ul class="nav nav-pills flex-column">
            <li class="nav-item">
              <a class="nav-link" href="#">Nav item again</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#">One more nav</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#">Another nav item</a>
            </li>
          </ul>
        </nav>

        <main class="col-sm-9 ml-sm-auto col-md-10 pt-3" role="main">
          <sitemesh:write property='body'/>
        </main>
        
      </div>
      
    </div>
    <%@include file="/WEB-INF/decorator/footer.jsp"%>
</body>
</html>
