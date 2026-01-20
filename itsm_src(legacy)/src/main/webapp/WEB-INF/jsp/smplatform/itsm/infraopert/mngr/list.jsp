<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <title><c:out value="${LEFT_MENU_PROGRMACCESAUTHORVO.progrmNm}"/></title>
	<script type="text/javaScript" language="javascript" defer="defer">
	function fncRetrievePagingList(pageNo){
    	document.listForm.elements['searchInfraOpertVO.pageIndex'].value = pageNo;
    	document.listForm.action = "<c:url value='/itsm/infraOpert/mngr/retrievePagingList.do'/>";
       	document.listForm.submit();
    }
    function fncRetrieveList() {
    	document.listForm.action = "<c:url value='/itsm/infraOpert/mngr/retrievePagingList.do'/>";
    	document.listForm.elements['searchInfraOpertVO.pageIndex'].value = 1;
       	document.listForm.submit();
    }
    function fncReset(){
    	document.listForm.elements['searchInfraOpertVO.infraOpertNo'].value = '';
    	document.listForm.elements['searchInfraOpertVO.chargerId'].value = '';
    	document.listForm.elements['searchInfraOpertVO.trgetSrvcCode'].value = '';
    }
    function fncCreateView() {
		var srvcNos = $("#listForm input[type='checkbox']:checked");
		
		if(srvcNos.length > 0){
			$.each(srvcNos, function(i,srvcNo){
				$("#"+srvcNo.id).val(srvcNo.id);
			})
			
		  	document.listForm.action = "<c:url value='/itsm/infraOpert/mngr/createView.do'/>";
	       	document.listForm.submit();
		}else{
			alert("SR을 하나 이상 선택해주세요.")
		}
    }
    function fncUpdateView(srvcRsponsNo,infraOpertNo) {
    	console.log(srvcRsponsNo);
    	console.log(infraOpertNo);
    	
    	document.listForm.elements['srvcRsponsVO.srvcRsponsNo'].value = srvcRsponsNo;
		document.listForm.elements['infraOpertVO.srvcRsponsNo'].value = srvcRsponsNo;
		
		if(infraOpertNo == '') {
	    	$("#"+srvcRsponsNo).prop("checked", true);
	    	fncCreateView()
    	}else{
		  	document.listForm.action = "<c:url value='/itsm/infraOpert/mngr/updateView.do'/>";
    		document.listForm.submit();
    	}
    }
    </script>
    <style type="text/css">
    </style>
</head>
	

<body>
		<main id="content">
            <div class="page-header">
                <h3 class="page-title"><c:out value="${LEFT_MENU_PROGRMACCESAUTHORVO.progrmNm}"/></h3>
            </div>
				
		    <form:form commandName="infraOpertFormVO" id="listForm" name="listForm" method="post">
				<form:hidden path="infraOpertVO.srvcRsponsNo" />
				<form:hidden path="srvcRsponsVO.srvcRsponsNo" />
				<form:hidden path="searchInfraOpertVO.pageIndex" />
<%-- 			<form:hidden path="srvcRsponsVO.processMt" /> --%>
				
                <div class="row">
                    <div class="col-fix">
                        <div class="card">
                            <div class="card-header">
                                <h4 class="card-title">검색조건</h4>
                            </div>
                            <div class="card-body">
                                <div class="detailbox type1">
                                    <dl>
                                        <dt>인프라번호</dt>
                                        <dd><form:input path="searchInfraOpertVO.infraOpertNo" class="control" title="인프라번호"/></dd>
                                    </dl>
                                    <dl>
                                        <dt>담당자</dt>
                                        <dd>
                                        	<form:select path="searchInfraOpertVO.chargerId" cssClass="control" title="대상서비스">
										  		<form:option value="">전체</form:option>
										  		<form:options items="${sysChargerList}" itemLabel="userNm" itemValue="userId"/>
										  	</form:select>
                                        </dd>
                                    </dl>
                                </div>
                                <hr>
                                <div class="detailbox type1">
                                    <dl>
                                        <dt>대상서비스</dt>
                                        <dd>
                                          <form:select path="searchInfraOpertVO.trgetSrvcCode" cssClass="control" title="대상서비스">
										  	<form:option value="">전체</form:option>
										  	<form:options items="${trgetSrvcCodeVOList}" itemLabel="cmmnCodeNm" itemValue="cmmnCode"/>
										  </form:select>
                                        </dd>
                                    </dl>
                                    <dl>
                                        <%-- <dt>작성일</dt>
                                        <dd>
                                        	<form:input id="wdtbDt" path="searchWdtbVO.wdtbDtDateDisplay" class="control singleDate" title="작성일 선택기"/>
                                        </dd> --%>
                                    </dl>
                                </div> 
                                <hr>
                                <div class="detailbox type1">
                                    <dl>
                                        <dt>제목</dt>
                                        <dd><form:input path="searchInfraOpertVO.srvcRsponsSj" class="control" title="제목"/></dd>
                                    </dl>
                                    <dl>
                                        <dt>내용</dt>
                                        <dd><form:input path="searchInfraOpertVO.srvcRsponsCn" class="control" title="내용"/></dd>
                                    </dl>
                                </div>
                                <hr>
                                <div class="btnbox center">
                                    <button type="button" onclick="fncReset()" class="btn btn-secondary"><i class="ico-reset"></i>초기화</button>
                                    <button type="button" onclick="fncRetrieveList()" class="btn btn-primary"><i class="ico-search"></i>검색</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col">
                        <div class="card">
                            <div class="card-header">
                                <span class="numbadge">${paginationInfo.totalRecordCount}</span>
                                <div class="btnbox fr">
                                    <button type="button" onclick="fncCreateView()" class="btn btn-primary"><i class="ico-write"></i>등록</button>
                                </div>
                            </div>
                            
                            <table class="gridtbl">
                                <caption>인프라작업 현황 목록</caption>
                                <colgroup>
                                    <col style="width:60px">
                                    <col style="width:100px">
                                    <col style="width:auto">
                                    <col style="width:100px">
                                    <col style="width:90px">
                                    <col style="width:140px">
                                    <col style="width:140px">
                                    <col style="width:140px">
                                </colgroup>
                                <thead>
                                    <tr>
                                        <th scope="col">선택</th>
                                        <th scope="col">작업번호</th>
                                        <th scope="col">SR제목</th>
                                        <th scope="col">SR번호</th>
                                        <th scope="col">대상서비스</th>
                                        <th scope="col">SR요청일</th>
                                        <th scope="col">등록일</th>
                                        <th scope="col">수정일</th>
                                    </tr>
                                </thead>
                                <tbody>
                                  <c:forEach var="result" items="${resultList}" varStatus="status">
                                    <tr>
                                    	<c:choose>
	                                    	<c:when test="${empty result.infraOpertNo}">
	                                        	<td class="num"><input type="checkbox" id="${result.srvcRsponsNo}" name="srvcRsponsNos" class="checkbox"><label for="${result.srvcRsponsNo}"></label></td>
	                                    	</c:when>
	                                    	<c:otherwise>
	                                        	<td class="num"><input type="checkbox" id="${result.srvcRsponsNo}" name="srvcRsponsNos" disabled="disabled" class="checkbox"><label for="${result.srvcRsponsNo}"></label></td>
	                                    	</c:otherwise>
                                    	</c:choose>
                                        <td class="num"><c:out value="${result.infraOpertNo}"/></td>
                                        <td><a href="javascript:fncUpdateView('<c:out value="${result.srvcRsponsNo}"/>','<c:out value="${result.infraOpertNo}"/>')" class="ellipsis"><c:out value="${result.srvcRsponsSj}"/></a></td>
                                        <td class="num"><c:out value="${result.srvcRsponsNo}"/></td>
                                        <td class="trgetSrvcCodeNm"><c:out value="${result.trgetSrvcCodeNm}"/></td>
                                        <td class="num"><c:out value="${result.requstDtDateDisplay}"/> <c:out value="${result.requstDtTimeDisplay}"/></td>
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
            </form:form>
        </main>
</body>
</html>
