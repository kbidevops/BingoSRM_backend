
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
	<%@include file="/WEB-INF/decorator/head.jsp"%>
    <sitemesh:write property='head'/>
</head>
<script type="text/javascript">
$(function(){
	<c:if test="${isSwitchOn == false }">
	$("#switch").trigger("click");
	</c:if>
	
	$("#switch").on("change", function(){
		var isSwitchOn = $(this).is(":checked");
		$.ajax({
	  		type : "post",
	  		url : "<c:url value='/progrm/mngr/menuSwitchOnAjax.do'/>",
	  		data : {
	  			"isSwitchOn" : isSwitchOn,
	  		},
	  		dataType : "json",
	  		success : function(request){
	  			console.log(request);
	  		},
	  		error : function(xhr){
	  			
	  		}
		})
	})
})
</script>
<body>
	  <!-- 본문바로가기 -->
    <div class="skipnav"><a href="#content">본문바로가기</a></div>
    <div class="wrap">
        <header id="header">
            <h1><a href="<c:url value="/main/index.do"/>">ITSM</a></h1>
            <div class="user-box">
              <c:if test="${sessionScope.LOGIN_USER_VO.userNm != '가입전사용자'}">
                <a href="<c:url value="/user/site/updateView.do"/>" class="usersetting"  title="회원정보수정">
                    <strong><c:out value="${sessionScope.LOGIN_USER_VO.userNm}"/></strong>
                    <span>회원정보수정</span>
                </a>
              </c:if> 
                <button onclick="location.href='<c:url value="/login/site/loginOut.do"/>'" type="button" class="btn btn-logout">로그아웃</button>
            </div>
        </header>
        <div class="contwrap">
            <aside id="sidebar">
               <div class="nav-control">
                    <label class="switch-wrap">
                        <span for="switch" class="switch-label">전체메뉴 펼치기</span>
                        <input type="checkbox" class="switch" id="switch" checked="checked" >
                        <span class="slider"></span>
                    </label>
                </div>
                <nav class="lnb">
                    <ul class="lnb-menu">
                    <c:forEach items="${TOP_MENU_LIST}" var="topMenu">
			          	<c:if test="${!empty topMenu.progrmAccesAuthorCode}">
				          	<li class="current has-sub">
					           <a href="#"><c:out value="${topMenu.progrmNm}"/>${topMenu.progrmSn eq TOP_MENU_PROGRMACCESAUTHORVO.progrmSn ? '<span class="sr-only">(current)</span>' : ''}</a>
					           <ul>
					           		<c:forEach items="${topMenu.progrmList}" var="leftMenu">
						          		<c:if test="${!empty leftMenu.progrmAccesAuthorCode}">
							          		<li class="${leftMenu.progrmSn == LEFT_MENU_PROGRMACCESAUTHORVO.progrmSn ? ' current' : ''}">
								              <a class="" href="<c:url value="${leftMenu.progrmUri}"/>"><c:out value="${leftMenu.progrmNm}"/>${leftMenu.progrmSn == LEFT_MENU_PROGRMACCESAUTHORVO.progrmSn ? '<span class="sr-only">(current)</span>' : ''}</a>
								            </li>
							            </c:if>
						          	</c:forEach>
					           </ul>
					        </li>	
				        </c:if>
			        </c:forEach>
                    </ul>
                </nav>
            </aside>
            
            <!-- 본문영역 -->
            <sitemesh:write property='body'/>
            <%@include file="/WEB-INF/decorator/footer2.jsp"%>
        </div>
    </div>
</body>
</html>
