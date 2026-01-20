<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><c:out value="${LEFT_MENU_PROGRMACCESAUTHORVO.progrmNm}"/></title>
<script type="text/javascript" language="javascript" defer="defer">
$(function() {
	$("#repTyCode").on("change", function(){
		$("#reportDt").val(null);
		document.listForm.elements['searchMasterVO.repTyCode'].value = this.value;
		document.listForm.elements['repMasterVO.repTyCode'].value = this.value;
		document.listForm.action = "<c:url value='/itsm/rep/master/mngr/retrievePagingList.do'/>";
	   	document.listForm.submit();
	});
});

/* 글 등록 화면 function */
function fncCreateView() {
	var repTyCode = document.listForm.elements['searchMasterVO.repTyCode'].value;
	document.listForm.elements['repMasterVO.repTyCode'].value = repTyCode;
   	document.listForm.action = "<c:url value='/itsm/rep/master/mngr/createView.do'/>";
   	document.listForm.submit();
}

function fncUpdateView(repSn){
	var repTyCode = document.listForm.elements['searchMasterVO.repTyCode'].value;
	document.listForm.elements['repMasterVO.repTyCode'].value = repTyCode;
	document.listForm.elements['repMasterVO.repSn'].value = repSn;
	document.listForm.action = "<c:url value='/itsm/rep/master/mngr/updateView.do' />";
	document.listForm.submit();
} 

function fncReset(){
	$("#reportDt").val(null);
	document.listForm.elements['repMasterVO.reportDtDisplay'].value = '';
}

function fncRetrieveList(){
	document.listForm.action = "<c:url value='/itsm/rep/master/mngr/retrievePagingList.do'/>";
   	document.listForm.submit();
}
/* pagination 페이지 링크 function */
function fncRetrievePagingList(pageNo){
	console.log(pageNo)
	$("#repSn").attr("disabled", "disabled");
	$("#reportDt").val(null);
	
	document.listForm.elements['searchMasterVO.pageIndex'].value = pageNo;
	document.listForm.action = "<c:url value='/itsm/rep/master/mngr/retrievePagingList.do'/>";
   	document.listForm.submit();
}
</script>
</head>
<body>
	<main id="content">
                <div class="page-header">
                    <h3 class="page-title"><c:out value="${LEFT_MENU_PROGRMACCESAUTHORVO.progrmNm}"/></h3>
                </div>

                <div class="row">
                    <div class="col-fix">
                        <div class="card">
                            <div class="card-header">
                                <h4 class="card-title">검색조건</h4>
                            </div>
                            <div class="card-body">
                            <form:form commandName="repFormVO" id="listForm" name="listForm" method="post">
	                            <form:hidden path="searchMasterVO.pageIndex"/>
								<form:hidden path="repMasterVO.saveToken"/>
								<form:hidden path="repMasterVO.repSn"/>
								<form:hidden path="repMasterVO.repTyCode"/>
								<form:hidden path="searchMasterVO.repTyCode"/>
								<form:hidden path="repMasterVO.sttusCode"/>
								<form:hidden path="repMasterVO.reportDtDisplay"/>
                                <div class="detailbox type1">
                                    <dl>
                                        <dt>보고일</dt>
                                        <dd><form:input id="reportDt" path="searchMasterVO.reportDtDisplay" class="control singleDate" title="보고일 선택기"/></dd>
                                    </dl>
                                    <dl>
                                        <dt>상태</dt>
                                        <dd> 
                                        	<form:select path="searchMasterVO.sttusCode" cssClass="control" title="상태코드">
											  	<form:option value="">전체</form:option>
											  	<form:options items="${sttusCodeList }" itemLabel="cmmnCodeNm" itemValue="cmmnCode"/>
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
                                <div class="btnbox fr col-2">
                                	<select name="searchMasterVO.repTyCode" id="repTyCode" class="control">
                               	 	<c:forEach items="${repTyCodes}" var="repTyCode">
                               	 		<c:if test="${repFormVO.searchMasterVO.repTyCode == repTyCode.cmmnCode }">
	                                	<option value="${repTyCode.cmmnCode }" selected="selected">${repTyCode.cmmnCodeNm }</option>
                               	 		</c:if>
                               	 		<c:if test="${repFormVO.searchMasterVO.repTyCode != repTyCode.cmmnCode }">
	                                	<option value="${repTyCode.cmmnCode }">${repTyCode.cmmnCodeNm }</option>
                               	 		</c:if>
                                	</c:forEach>
                                	</select>
                                </div>
                            </div>

                            <!-- 검색결과 0건일 경우
                            <div class="nolist">
                               총 <strong>0</strong>건이 조회되었습니다.
                            </div>
                            -->
                            <table class="gridtbl">
                                <caption>보고서 현황 목록</caption>
                                <colgroup>
                                    <col style="width:60px">
                                    <col style="width:auto">
                                    <col style="width:140px">
                                    <col style="width:140px">
                                    <col style="width:140px">
                                    <col style="width:120px">
                                </colgroup>
                                <thead>
                                    <tr>
                                        <th scope="col">No</th>
                                        <th scope="col">제목</th>
                                        <th scope="col">담당자</th>
                                        <th scope="col">보고일</th>
                                        <th scope="col">수정일</th>
                                        <th scope="col">상태</th>
                                    </tr>
                                </thead>
                                <tbody>
                                	<c:forEach var="result" items="${resultList}" varStatus="status">
                                    <tr>
                                        <td class="num"><c:out value="${paginationInfo.totalRecordCount+1 - ((repFormVO.searchMasterVO.pageIndex-1) * repFormVO.searchMasterVO.pageSize + status.count)}"/></td>
                                        <td><a href="javascript:fncUpdateView('<c:out value="${result.repSn }"/>')" class="ellipsis"><c:out value="${result.repNm}"/></a></td>
                                        <td><c:out value="${result.creatUserNm}"/></td>
                                        <td class="num"><c:out value="${result.reportDtDisplay}"/></td>
                                        <td class="num"><c:out value="${result.updtDtDisplay}"/></td>
                                        <td><c:out value="${result.sttusNm}"/></td>
                                    </tr>
                                    </c:forEach>
                                    <c:if test="${empty resultList}">
							  		<tr>
							  			<td class="text-center" colspan="6">검색된 데이터가 없습니다.</td>
							  		</tr>
							  		</c:if>
                                </tbody>
                            </table>
                        </div>
                        <div class="pagingbox">
                            <div class="paging">
                            <ui:pagination paginationInfo = "${paginationInfo}" type="image" jsFunction="fncRetrievePagingList" />
                            </div>
                        </div>
                    </div>
                </div>
            </main>
</body>
</html>