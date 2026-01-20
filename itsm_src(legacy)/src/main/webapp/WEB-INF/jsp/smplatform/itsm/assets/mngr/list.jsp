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
	$(function(){
		var assetsSe1 = "${assetsFormVO.searchAssetsVO.assetsSe1}";
		if(assetsSe1 == null || assetsSe1 == ""){
			$("#T001").prop("checked",true);
		}else{
			$("#"+assetsSe1).prop("checked", true);
		}
		
		var infra = $("input[name='searchAssetsVO.assetsSe1']:checked").val();
		if(infra == 'T001'){
			document.listForm.sw.style.display = "none"
		}else if(infra == 'T002'){
			document.listForm.hw.style.display = "none"
		}
		
		$("input[name='searchAssetsVO.assetsSe1']").on("change", function(){
			if($(this).val() == 'T001'){
				document.listForm.sw.style.display = "none";
				document.listForm.hw.style.display = "";
			}else if($(this).val() == 'T002'){
				document.listForm.sw.style.display = "";
				document.listForm.hw.style.display = "none";
			}
		})
	})
	
	/* 리스트 불러오기 function*/
	function fncRetrieveList(){
	   	if($("input[name='searchAssetsVO.assetsSe1']:checked").val() == 'T001'){
			document.listForm.sw.disabled = "true";
		}else{
			document.listForm.hw.disabled = "true";
		}
	   	
		document.listForm.action = "<c:url value='/itsm/assets/mngr/retrievePagingList.do'/>";
		document.listForm.elements['searchAssetsVO.pageIndex'].value = 1;
	   	document.listForm.submit();
	   	
	}
	/* pagination 페이지 링크 function */
    function fncRetrievePagingList(pageNo){
    	if($("input[name='searchAssetsVO.assetsSe1']:checked").val() == 'T001'){
			document.listForm.sw.disabled = "true";
		}else{
			document.listForm.hw.disabled = "true";
		}
    	
    	document.listForm.elements['searchAssetsVO.pageIndex'].value = pageNo;
    	document.listForm.action = "<c:url value='/itsm/assets/mngr/retrievePagingList.do'/>";
       	document.listForm.submit();
       	
    }
	function fncReset(){
		var inputs = $(".search input");
		
		$.each(inputs, function(i,input){
			input.value = "";
		})
	}
	/* 글 등록 화면 function */
	function fncCreateView() {
		var assetsSe1 = document.listForm.elements['searchAssetsVO.assetsSe1'].value;
		document.listForm.elements['assetsVO.assetsSe1'].value = assetsSe1;
	   	document.listForm.action = "<c:url value='/itsm/assets/mngr/createView.do'/>";
	   	document.listForm.submit();
	}

	function fncUpdateView(assetsSn){
		document.listForm.elements['assetsVO.assetsSn'].value = assetsSn;
		document.listForm.action = "<c:url value='/itsm/assets/mngr/updateView.do'/>";
		document.listForm.submit();
	}
	function fncRetrieveExcelList() {
    	document.listForm.action = "<c:url value='/itsm/assets/mngr/retrieveExcelList.do'/>";
       	document.listForm.submit();
    }
    </script>
</head>
	

<body>
		<main id="content">
            <div class="page-header">
                <h3 class="page-title"><c:out value="${LEFT_MENU_PROGRMACCESAUTHORVO.progrmNm}"/></h3>
            </div>
				
		    <form:form commandName="assetsFormVO" id="listForm" name="listForm" method="post">
				<form:hidden path="assetsVO.assetsSn" />
				<form:hidden path="assetsVO.assetsSe1" />
				<form:hidden path="searchAssetsVO.pageIndex" />
                <div class="row">
                    <div class="col-fix">
                        <div class="card">
                            <div class="card-header">
                                <h4 class="card-title">검색조건</h4>
                            </div>
                            <div class="card-body">
                                <div class="detailbox type1">
                                    <dl>
                                        <dt>인프라</dt>
                                        <dd>
 										  	<c:forEach items="${assetsSe1List}" var="assetsSe1">
 										  		<form:radiobutton path="searchAssetsVO.assetsSe1" class="radio" id="${assetsSe1.cmmnCode }" 
 										  			label="${assetsSe1.cmmnCodeNm }"  value="${assetsSe1.cmmnCode }"/>
 										  	</c:forEach> 
                                        </dd>
                                    </dl>
                                    <dl>
                                        <dt>구분</dt>
	                                    <dd>
	                                    	<form:select path="searchAssetsVO.assetsSe2" class="control" title="구분" id="hw">
												<form:option value="">전체</form:option>
			                                    <c:forEach items="${hwList}" var="hw">
													<form:option value="${hw.cmmnCode }">${hw.cmmnCodeNm }</form:option>
		 										</c:forEach> 
											</form:select>
	 										<form:select path="searchAssetsVO.assetsSe2" class="control" title="구분" id="sw">
												<form:option value="">전체</form:option>
			                                    <c:forEach items="${swList}" var="sw">
													<form:option value="${sw.cmmnCode }">${sw.cmmnCodeNm }</form:option>
		 										</c:forEach> 
											</form:select>
	                                    </dd>
                                    </dl>
                                </div>
                                <hr>
                                <div class="detailbox type1 search">
                                    <dl>
                                        <dt>제조사</dt>
                                        <dd><form:input path="searchAssetsVO.maker" class="control" title="제조사"/></dd>
                                    </dl>
                                    <dl>
                                        <dt>제품명</dt>
                                        <dd><form:input path="searchAssetsVO.productNm" class="control" title="제품명"/></dd>
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
                                    <button type="button" onclick="fncRetrieveExcelList()" class="btn btn-dark"><i class="ico-download"></i>다운로드</button>
                                    <button type="button" onclick="fncCreateView()" class="btn btn-primary"><i class="ico-write"></i>등록</button>
                                </div>
                            </div>
                            
                            <table class="gridtbl">
                                <caption>자산관리 현황 목록</caption>
                                <colgroup>
                                    <col style="width:140px">
                                    <col style="width:140px">
                                    <col style="width:120px">
                                    <col style="width:auto">
                                    <col style="width:60px">
                                    <col style="width:250px">
                                    <col style="width:100px">
                                </colgroup>
                                <thead>
                                    <tr>
                                        <th scope="col">자산관리번호</th>
                                        <th scope="col">구분</th>
                                        <th scope="col">제조사</th>
                                        <th scope="col">제품명</th>
                                        <th scope="col">수량</th>
                                        <th scope="col">용도</th>
                                        <th scope="col">도입일자</th>
                                    </tr>
                                </thead>
                                <tbody>
                                  <c:forEach var="result" items="${resultList}" varStatus="status">
                                    <tr>
<%--                                        <td class="num"><c:out value="${paginationInfo.totalRecordCount+1 - ((assetsFormVO.searchAssetsVO.pageIndex-1) * assetsFormVO.searchAssetsVO.pageSize + status.count)}"/></td>--%>
                                        <td class="num"><c:out value="${result.assetsNo}"/></td>
                                        <td class="num"><c:out value="${result.assetsSe2Nm}"/></td>
                                        <td class="num"><c:out value="${result.maker}"/></td>
                                        <td><a href="javascript:fncUpdateView('<c:out value="${result.assetsSn}"/>')" class="ellipsis"><c:out value="${result.productNm}"/></a></td>
                                        <td class="num"><c:out value="${result.assetQy}"/></td>
                                        <td><c:out value="${result.assetPrpos}"/></td>
                                        <td class="num"><c:out value="${result.indcDtDisplay}"/></td>
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
