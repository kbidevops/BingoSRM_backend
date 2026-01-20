<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <c:set var="registerFlag" value="${empty repFormVO.repDetailVO.userId ? 'create' : 'modify'}"/>
    <title><c:out value="${LEFT_MENU_PROGRMACCESAUTHORVO.progrmNm}"/> <c:out value="${registerFlag == 'create' ? '등록' : '수정'}"/></title>
    <!--For Commons Validator Client Side-->
    <script type="text/javascript" src="/ckeditor/ckeditor.js"></script>
    <script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
    <validator:javascript formName="repFormVO" staticJavascript="false" xhtml="true" cdata="false"/>

<script type="text/javascript" language="javascript" defer="defer">
function fncRetrieveList(){
	document.listForm.elements['searchDetailVO.repTyCode'].value = "B002";
	document.listForm.action = "<c:url value='/itsm/rep/detail/mngr/retrievePagingList.do'/>";
   	document.listForm.submit();
}
</script>
</head>

<body>
		<main id="content">
		<form:form commandName="repFormVO" id="listForm" name="listForm" method="post">
		<form:hidden path="searchDetailVO.repTyCode"/>
		</form:form>
				<div class="page-header">
	            	<div class="row">
	            		<div class="col-6">
	            			<h3 class="page-title">
	            			<c:if test="${registerFlag != 'create'}">
	            			<strong><c:out value="${repFormVO.repDetailVOList.get(0).createDtDisplay }"/></strong>	
	            			</c:if>
	            			<c:out value="${LEFT_MENU_PROGRMACCESAUTHORVO.progrmNm}"/></h3>
	            		</div>
	            		<div class="col-6 text-right">
		                    <button type="button" onclick="fncRetrieveList()" class="btn btn-secondary"><i class="ico-list"></i>목록</button>
	            		</div>
	            	</div>
	            	<div class="row">
	            		<p class="col-8 text-left">확정또는 확정 요청중인 보고서입니다. 수정을 원하시면 관리자에게 문의해주세요.</p>
	            	</div>
	            </div>
	               
                <table class="reporttbl">
	               <caption>업무보고서 작성</caption>
	               <colgroup>
	                   <col style="width:10%">
	                   <col style="width:45%">
	                   <col style="width:45%">
	               </colgroup>
	               <thead>
	                   <tr>
	                       <th scope="col">구분</th>
	                       <th scope="col">실적</th>
	                       <th scope="col">계획</th>
	                   </tr>
	               </thead>
	               <tbody>
	                   <c:forEach items="${repDetailVOList }" var="repDetailVO">
						<tr style="height:50px;">
							<th id="${repDetailVO.sysCode }" scope="row" class="num"><b>${repDetailVO.sysCodeNm }</b></th>
							<c:if test="${repDetailVO.sysCode == 'B129'}">
								<td class="num" colspan="2" id="execDesc">
									<c:if test="${registerFlag != 'create' }">${repDetailVO.execDesc}</c:if> 
								</td>
							</c:if>
							<c:if test="${repDetailVO.sysCode != 'B129'}">
								<td class="num" id="execDesc">
									<c:if test="${registerFlag != 'create' }">${repDetailVO.execDesc}</c:if> 
								</td>
								<td class="num" id="planDesc">
									<c:if test="${registerFlag != 'create' }">${repDetailVO.planDesc}</c:if> 
								</td>
							</c:if>
						</tr>
					</c:forEach>
	               </tbody>
                </table>
	             <div class="btnbox right mb30">
	                 <button type="button" onclick="fncRetrieveList()" class="btn btn-secondary"><i class="ico-list"></i>목록</button>
	             </div>
         </main>
</body>
</html>
