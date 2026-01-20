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
    <title>ISRM-로그인</title>
    
    <link rel="shortcut icon" href="<c:url value='/images/favicon.ico'/>" type="image/x-icon">
	<link rel="icon" href="<c:url value='/images/favicon.ico'/>" type="image/x-icon">

    <!-- Korail CSS -->    
    <link rel="stylesheet" type="text/css" href="<c:url value='/korail/css/default.css'/>">

    <!-- Bootstrap CSS -->    
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
	

    <div class="container">
        <div class="login-wrapper">
            <div class="login-logo"><img src="/korail/images/login_logo.png" alt="Korailtech ITSM"></div>
            <div class="login-box">
                <h2 class="login-title">로그인</h2>
                <form:form class="login-form" commandName="userVO" id="loginForm" name="loginForm" method="post" autocomplete="off">
                    <div class="form-group">
                        <label for="user_id">ID</label>
                        <input type="text" path="userId" id="userId" name="userId" placeholder="아이디를 입력하세요" required>
                    </div>
                    <div class="form-group">
                        <label for="user_pw">Password</label>
                        <input type="password" path="userPassword" id="userPassword" name="userPassword" placeholder="비밀번호를 입력하세요" required>
                    </div>
                    <button type="button" onclick="fncLogin()" class="login-btn btn">로그인</button>
                </form:form>
                <p class="copyright">©2025. KORAILTECH. ALL RIGHTS RESERVED.</p>
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
