<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>BingoSRM-로그인</title>
    
    <link rel="shortcut icon" href="<c:url value='/images/favicon.ico'/>" type="image/x-icon">
	<link rel="icon" href="<c:url value='/images/favicon.ico'/>" type="image/x-icon">
    <!-- Bootstrap CSS -->    
<%--     <link type="text/css" rel="stylesheet" href="<c:url value='/css/login.css'/>"/> --%>
    <!--For Commons Validator Client Side-->
    <script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
    <validator:javascript formName="userVO" staticJavascript="false" xhtml="true" cdata="false"/>
    <script type="text/javaScript" language="javascript" defer="defer">
    <!--
    $(function() {
		$("#userPassword").keydown(function(event) {
			if ( event.which == 13 ) {
				// 로그인 정보 유효성 검사
				fncLogin();
			}
		});
		
	});
    
    function fncLogin() {	
    	frm = document.loginForm;
    	if(!validateUserVO(frm)){
            return;
        }else{
        	document.loginForm.action = "<c:url value='/login/site/login.do'/>";
        	document.loginForm.submit();
        }
    }
    
    function fncEdit(){
    	alert('회원가입 페이지 이동');
    }
    
    function fncEdit() {	
    	frm = document.loginForm;
    	document.loginForm.action = "<c:url value='/user/site/createView.do'/>";
    	document.loginForm.submit();
    }
    
    
  	//-->
    </script>
</head>
	

<body>
	
		<%-- <div class="jumbotron">
          <h1 class="display-3"><i class="fa fa-thumbs-o-up" aria-hidden="true"></i> ITSM</h1>
          <p class="lead">경영혁신플랫폼 사업간 발생된 SR업무 처리 시간을 단축 시켜드립니다.</p>
          
				<form:form commandName="userVO" id="loginForm" name="loginForm" cssClass="form-signin" method="post">
				
 			        <h3 class="form-signin-heading">사용자LOGIN <span class="badge badge-success" onclick="fncEdit()"><i class="fa fa-user" aria-hidden="true"></i> 회원가입</span></h3> 
 			        <label for="userId" class="sr-only">사용자ID</label>
 			        <form:input path="userId" cssClass="form-control" placeholder="사용자ID"/>
 			        <label for="userPassword" class="sr-only">비밀번호</label> 
 			        <form:password path="userPassword" cssClass="form-control" placeholder="비밀번호"/> 
 			        <button class="btn btn-lg btn-primary btn-block" type="button" onclick="fncLogin()"><i class="fa fa-sign-in" aria-hidden="true"></i>
 			        로그인</button> 
			        
			    </form:form>
	    </div> --%>
	    
	<div class="loginwrap">
        <div class="logininner">
            <h1>ITSM</h1>
            <p class="log-text">Korail Tech 업무중 발생된 <br>SR요청 사항을 처리해 드립니다.</p>
            
            <div class="loginbox">
               <form:form commandName="userVO" id="loginForm" name="loginForm" method="post">
	                <div class="logtit">
	                    <h3>Korail Tech 로그인</h3>
	                    <!-- 
	                    <a href="javascript:fncEdit()" class="btn-join">Korail Tech 회원가입</a>
	                    -->
	                </div>
	                <div class="formbox">
	                    <p>
	                        <label for="userId">아이디</label>
	                        <form:input path="userId" cssClass="inp" id="userId"/>
	                    </p>
	                    <p>
	                        <label for="userPassword">비밀번호</label>
	                        <form:password path="userPassword" id="userPassword" class="inp"/> 
	                    </p>
	                    <button type="button" onclick="fncLogin()" class="btn-primary btn">로그인</button>
	                </div>
                </form:form>
            </div>
        </div>
    </div>

 	
<script type="text/javaScript" language="javascript" defer="defer">

	<c:if test="${!empty sessionScope.ERROR_MESSAGE}">
		alert('${sessionScope.ERROR_MESSAGE}');
		<%session.removeAttribute("ERROR_MESSAGE");%>
	</c:if>

</script>

</body>
</html>
