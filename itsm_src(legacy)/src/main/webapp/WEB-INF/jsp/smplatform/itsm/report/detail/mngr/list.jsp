<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><c:out value="${LEFT_MENU_PROGRMACCESAUTHORVO.progrmNm}"/></title>
<script type="text/javascript" language="javascript" defer="defer">
$(function() {
	$("#repTyCode").on("change", function(){
		$("#creatDt").val(null);
		document.listForm.elements['searchDetailVO.repTyCode'].value = this.value;
		document.listForm.elements['repDetailVO.repTyCode'].value = this.value;
		document.listForm.action = "<c:url value='/itsm/rep/detail/mngr/retrievePagingList.do'/>";
	   	document.listForm.submit()
	});
	
});

/* 글 등록 화면 function */
function fncCreateView() {
	var repTyCode = document.listForm.elements['searchDetailVO.repTyCode'].value;
  location.href = "${pageContext.request.contextPath}/itsm/rep/detail/mngr/createView.do?repTyCode=" + repTyCode;
}

function fncUpdateView(repSn, userId){
  location.href = "${pageContext.request.contextPath}/itsm/rep/detail/mngr/createView.do?repSn=" + repSn + "&userId=" + userId;
}

/* 리스트 불러오기 function*/
function fncRetrieveList(){
	document.listForm.action = "<c:url value='/itsm/rep/detail/mngr/retrievePagingList.do'/>";
	document.listForm.elements['searchDetailVO.pageIndex'].value = 1;
   	document.listForm.submit();
}

/* pagination 페이지 링크 function */
function fncRetrievePagingList(pageNo){
	$("#repSn").attr("disabled", "disabled");
	$("#creatDt").val(null);
	
	document.listForm.elements['searchDetailVO.pageIndex'].value = pageNo;
	document.listForm.action = "<c:url value='/itsm/rep/detail/mngr/retrievePagingList.do'/>";
   	document.listForm.submit();
}

function fncReset(){
	document.listForm.elements['searchDetailVO.createDtDisplay'].value = '';
	document.listForm.elements['searchDetailVO.userId'].value = '';
}
</script>
</head>
<body>
	<main id="content">
                <div class="page-header">
                    <h3 class="page-title">업무보고작성</h3>
                </div>

                <div class="row">
                    <div class="col-fix">
                        <div class="card">
                            <div class="card-header">
                                <h4 class="card-title">검색조건</h4>
                            </div>
                            <div class="card-body">
                            <form:form commandName="repFormVO" id="listForm" name="listForm" method="post">
								<form:hidden path="searchDetailVO.pageIndex"/>
								<form:hidden path="repDetailVO.repSn" id="repSn" />
								<form:hidden path="repDetailVO.deleteYn"/>
								<form:hidden path="repDetailVO.userId"/>
								<form:hidden path="repDetailVO.repTyCode"/>
								<form:hidden path="searchDetailVO.repTyCode"/>
                                <div class="detailbox type1">
                                    <dl>
                                        <dt>작성일</dt>
                                        <dd><form:input id="creatDt" path="searchDetailVO.createDtDisplay" class="control singleDate" title="작성일 선택기"/></dd>
                                    </dl>
                                    <dl>
                                        <dt>담당자</dt>
                                        <dd>
                                        <form:select path="searchDetailVO.userId" cssClass="control" title="담당자">
											<form:option value="">전체</form:option>
											<form:options items="${chargerList}" itemLabel="userNm" itemValue="userId"/>
										</form:select>
                                        </dd>
                                    </dl>
                                </div>
                                <hr>
                                <div class="btnbox center">
                                    <button type="button" onclick="fncReset()" class="btn btn-secondary"><i class="ico-reset"></i>초기화</button>
                                    <button type="button" onclick="fncRetrieveList()" class="btn btn-primary"><i class="ico-search"></i>검색</button>
                                </div> 
                            </form:form>                        
                            </div>
                        </div>
                    </div>
                    <div class="col">
                        <div class="card">
                            <div class="card-header">
                                <span class="numbadge"><c:out value="${paginationInfo.totalRecordCount}"/></span>
                                <div class="btnbox fr col-3 input-group">
	                              	<select name="repFormVO.searchDetailVO.repTyCode" id="repTyCode" class="control">
	                               	 	<c:forEach items="${repTyCodes}" var="repTyCode">
	                               	 		<c:if test="${repFormVO.searchDetailVO.repTyCode == repTyCode.cmmnCode }">
		                                		<option value="${repTyCode.cmmnCode }" selected="selected">${repTyCode.cmmnCodeNm }</option>
	                               	 		</c:if>
	                               	 		<c:if test="${repFormVO.searchDetailVO.repTyCode != repTyCode.cmmnCode }">
		                                		<option value="${repTyCode.cmmnCode }">${repTyCode.cmmnCodeNm }</option>
	                               	 		</c:if>
	                                	</c:forEach>
	                                </select>
	                                <button type="button" onclick="fncCreateView()" class="btn btn-primary"><i class="ico-write"></i>등록</button>
                                </div>
                            </div>
                            <table class="gridtbl">
                                <caption>일일보고 현황 목록</caption>
                                <colgroup>
                                    <col style="width:60px">
                                    <col style="width:auto">
                                    <col style="width:140px">
                                    <col style="width:140px">
                                    <col style="width:140px">
                                </colgroup>
                                <thead>
                                    <tr>
                                        <th scope="col">No</th>
                                        <th scope="col">제목</th>
                                        <th scope="col">담당자</th>
                                        <th scope="col">작성일</th>
                                        <th scope="col">수정일</th>
                                    </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="result" items="${resultList}" varStatus="status">
                                    <tr>
                                        <td class="num"><c:out value="${paginationInfo.totalRecordCount+1 - ((repFormVO.searchDetailVO.pageIndex-1) * repFormVO.searchDetailVO.pageSize + status.count)}"/></td>
                                        <td><a href="javascript:fncUpdateView('<c:out value="${result.repSn }"/>','<c:out value="${result.userId }"/>')" class="ellipsis">
                                        	<c:if test="${repFormVO.searchDetailVO.repTyCode == 'B003' }"><c:out value="${result.repNm }"/></c:if>
                                        	<c:if test="${repFormVO.searchDetailVO.repTyCode == 'B002' }"><c:out value="${result.repNm }"/></c:if>
                                        	<c:if test="${repFormVO.searchDetailVO.repTyCode == 'B001' }">
                                        		<c:out value="${result.dailyReportName}"/>
                                        	</c:if>
                                        </a></td>
                                        <td><c:out value="${result.userNm}"/></td>
                                        <td class="num"><c:out value="${result.createDtDisplay}"/></td>
                                        <td class="num"><c:out value="${result.updtDtDisplay}"/></td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                            <c:if test="${empty resultList}">
	                            <div class="nolist"> 총 <strong>0</strong>건이 조회되었습니다.</div>
	                        </c:if>
                        </div>
                        <div class="pagingbox">
                            <div class="paging"><ui:pagination paginationInfo = "${paginationInfo}" type="image" jsFunction="fncRetrievePagingList" /></div>
                        </div>
                    </div>
                </div>
            </main>
</body>
</html>