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
	/* 글 등록 화면 function */
    function fncCreateView(srvcRsponsNo) {
    	document.listForm.elements['funcImprvmVO.srvcRsponsNo'].value = srvcRsponsNo;
       	document.listForm.action = "<c:url value='/itsm/funcimprvm/mngr/createView.do'/>";
       	document.listForm.submit();
    }
    function fncUpdateView(fnctImprvmNo) {
    	fncRetrievefnctImprvmAjax(fnctImprvmNo)
    }
    function fncDownloadModal(){
    	$("#downloadModal").modal("show");
    }
    function fncRetrieveExcelList() {
    	
		var processMt = $("#processMtInput").val();
    	if($.trim(processMt) != ''){
    		document.listForm.processMt.value = processMt;	
    	}
		
    	$("#downloadModal").modal("hide");
    	
    	document.listForm.action = "<c:url value='/itsm/funcimprvm/mngr/retrieveExcelList.do'/>";
       	document.listForm.submit();
    }
    /* pagination 페이지 링크 function */
    function fncRetrievePagingList(pageNo){
    	document.listForm.elements['searchFuncImprvmVO.pageIndex'].value = pageNo;
    	document.listForm.action = "<c:url value='/itsm/funcimprvm/mngr/retrievePagingList.do'/>";
       	document.listForm.submit();
    }
    /* 글 목록 화면 function */
    function fncRetrieveList() {
    	document.listForm.action = "<c:url value='/itsm/funcimprvm/mngr/retrievePagingList.do'/>";
    	document.listForm.elements['searchFuncImprvmVO.pageIndex'].value = 1;
       	document.listForm.submit();
    }
    function fncReset(){
    	document.listForm.elements['searchFuncImprvmVO.fnctImprvmNo'].value = '';
    	document.listForm.elements['searchFuncImprvmVO.chargerId'].value = '';
    	document.listForm.elements['searchFuncImprvmVO.trgetSrvcCode'].value = '';
    	document.listForm.elements['searchFuncImprvmVO.processStdrCode'].value = '';
    	document.listForm.elements['searchFuncImprvmVO.srvcRsponsSj'].value = '';
    	document.listForm.elements['searchFuncImprvmVO.srvcRsponsCn'].value = '';
    }
    function fncRetrievefnctImprvmAjax(fnctImprvmNo){
    	$.ajax({
	  		type : "post",
	  		url : "<c:url value='/itsm/funcimprvm/mngr/retrieveFnctImprvmAjax.do'/>",
	  		data : {
	  			"fnctImprvmNo" : fnctImprvmNo,
	  		},
	  		dataType : "json",
	  		success : function(request){
	  			console.log(request);
	  			if(request.returnMessage == "empty"){
	  				alert("작성된 기능개선이 없습니다.")
	  				return request.returnMessage;
	  			}else{
	  				document.listForm.elements['funcImprvmVO.fnctImprvmNo'].value = fnctImprvmNo;
	  		       	document.listForm.action = "<c:url value='/itsm/funcimprvm/mngr/updateView.do'/>";
	  		       	document.listForm.submit();
	  			}
	  		},
	  		error : function(xhr){
	  			alert("보고서를 불러오는데 실패하였습니다.");
	  			console.log(xhr.status);
	  		}
	  	})
    }
    
    function fncPdfDownloadModal(){
    	$("#pdfDownloadModal").modal("show");
    }
    
    function fncRetrieveReport(){
    	var processMt = $("#processMtInputPdf").val();
    	
    	$("#pdfDownloadModal").modal("hide");
    	
    	$.ajax({
	  		type : "post",
	  		url : "<c:url value='/itsm/funcimprvm/mngr/retrieveFnctImprvmAjax.do'/>",
	  		data : {
	  			"processMt" : processMt,
	  		},
	  		dataType : "json",
	  		success : function(request){
	  			console.log(request);
	  			if(request.returnMessage == "empty"){
	  				alert("작성된 기능개선이 없습니다.")
	  				return request.returnMessage;
	  			}else{
	  			    var ozrFileNm = "/knfc/funcImprvmPlan.ozr";
	  				var odiFileNm = "/knfc/funcImprvmPlan.odi";
				    procPrintList(ozrFileNm, odiFileNm, request.funcImprvmList);
	  				
	  			}
	  		},	
	  		error : function(xhr){
	  			alert("보고서를 불러오는데 실패하였습니다.");
	  			console.log(xhr.status);
	  		}
	  	})
    }
    
    /**
	 * 문자열에서 모든 교체할 문자열을 대체 문자열로 치환한다.
	 * @param pattnStr - 찾을 문자열
	 * @param chngStr - 대체 문자열
	 * @return 치환된 문자열
	 */
	String.prototype.replaceAll = function(pattnStr, chngStr) {
	
	    var retsult = "";
	    var trimStr = this;//.replace(/(^\s*)|(\s*$)/g, "");
	
	    if(trimStr && pattnStr != chngStr) {
	
	        retsult = trimStr;
	
	        while(retsult.indexOf(pattnStr) > -1) {
	            retsult = retsult.replace(pattnStr, chngStr);
	        }
	    }
	
	    return retsult;
	};
	
	var procPrintList = function(ozrFileNm, odiFileNm, funcImprvmList) {
		
		var fiNo = "";
		for(var i=0; i<funcImprvmList.length; i++){ 
			console.log(funcImprvmList[i].fnctImprvmNo)
			if(i==0){
				fiNo = fiNo + funcImprvmList[i].fnctImprvmNo;
			}else{
				fiNo = fiNo +","+ funcImprvmList[i].fnctImprvmNo;
			}
		}
		
	    var jsonData = "${jsonData }";
	    jsonData = jsonData.replaceAll("'", '"');
		
	    
	    console.log(funcImprvmList.length-1)
	    var ozparams = {"ozr" : ozrFileNm
	                   , "odi" : odiFileNm
	                   , "fiNo" : fiNo
	                   , "length" : (funcImprvmList.length*2)-1
	                   , "pcount" : 1       // pcount는 jsonData 가 있으므로 밑의 params 갯수보다 1+해서 적어준다.
	                   , "params" : [ 
	                	  			]
	                   };
	    
	    var popUrl = "<c:url value='/itsm/rep/master/mngr/OzReportViewerFi.do'/>";
		
	    var $formOzReport = $( "<form action='" + popUrl + "' method='post', target='_blank' id='formOzReport' name='formOzReport'/>" );
	    var html = $("<input type='hidden' name='ozparams' value='"+JSON.stringify(ozparams)+"'/>"
	            +"<input type='hidden' name='jsonData' value='"+jsonData+"'/>"
	            );
		
	    $formOzReport.append(html);
		
	    $('body').append( $formOzReport );
	    $formOzReport.submit();
	};
	
    </script>
</head>
	

<body>
		<main id="content">
            <div class="page-header">
                <h3 class="page-title"><c:out value="${LEFT_MENU_PROGRMACCESAUTHORVO.progrmNm}"/></h3>
            </div>
				
		    <form:form commandName="funcImprvmFormVO" id="listForm" name="listForm" method="post">
				<form:hidden path="funcImprvmVO.fnctImprvmNo" />
				<form:hidden path="funcImprvmVO.srvcRsponsNo" />
				<form:hidden path="searchFuncImprvmVO.pageIndex" />
				<form:hidden path="processMt" />
<!-- 				<input type="hidden" name="searchFuncImprvmVO.fnctImprvmNo" /> -->
                <div class="row">
                    <div class="col-fix">
                        <div class="card">
                            <div class="card-header">
                                <h4 class="card-title">검색조건</h4>
                            </div>
                            <div class="card-body">
                                <div class="detailbox type1">
                                    <dl>
                                        <dt>기능개선번호</dt>
                                        <dd><form:input path="searchFuncImprvmVO.fnctImprvmNo" class="control" title="기능개선번호"/></dd>
                                    </dl>
                                    <dl>
                                        <dt>담당자</dt>
                                        <dd>
                                        	<form:select path="searchFuncImprvmVO.chargerId" cssClass="control" title="대상서비스">
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
                                          <form:select path="searchFuncImprvmVO.trgetSrvcCode" cssClass="control" title="대상서비스">
										  	<form:option value="">전체</form:option>
										  	<form:options items="${trgetSrvcCodeVOList}" itemLabel="cmmnCodeNm" itemValue="cmmnCode"/>
										  </form:select>
                                        </dd>
                                    </dl>
                                    <dl>
                                        <dt>SR처리기준시간</dt>
                                        <dd>
                                            <form:select path="searchFuncImprvmVO.processStdrCode" cssClass="control" title="대상서비스">
										  		<form:option value="">전체</form:option>
										  		<form:options items="${processStdrCodeList}" itemLabel="cmmnCodeNm" itemValue="cmmnCode"/>
										  	</form:select>
                                        </dd>
                                    </dl>
                                </div> 
                                <hr>
                                <div class="detailbox type1">
                                    <dl>
                                        <dt>제목</dt>
                                        <dd><form:input path="searchFuncImprvmVO.srvcRsponsSj" class="control" title="제목"/></dd>
                                    </dl>
                                    <dl>
                                        <dt>내용</dt>
                                        <dd><form:input path="searchFuncImprvmVO.srvcRsponsCn" class="control" title="내용"/></dd>
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
<%--                                    <button type="button" onclick="fncPdfDownloadModal()" class="btn btn-dark"><i class="ico-download"></i>pdf다운로드</button>--%>
                                    <button type="button" onclick="fncDownloadModal()" class="btn btn-dark"><i class="ico-download"></i>다운로드</button>
<!--                                     <button type="button" onclick="fncCreateView()" class="btn btn-primary"><i class="ico-write"></i>등록</button> -->
                                </div>
                            </div>
                            
                            <table class="gridtbl">
                                <caption>기능개선 현황 목록</caption>
                                <colgroup>
                                    <col style="width:60px">
                                    <col style="width:100px">
                                    <col style="width:auto">
                                    <col style="width:100px">
                                    <col style="width:100px">
                                    <col style="width:140px">
                                    <col style="width:140px">
                                    <col style="width:140px">
                                </colgroup>
                                <thead>
                                    <tr>
                                        <th scope="col">No</th>
                                        <th scope="col">기능개선번호</th>
                                        <th scope="col">SR제목</th>
                                        <th scope="col">SR번호</th>
                                        <th scope="col">대상서비스</th>
                                        <th scope="col">요청일</th>
                                        <th scope="col">개선계획일</th>
                                        <th scope="col">개선적용일</th>
                                    </tr>
                                </thead>
                                <tbody>
                                  <c:forEach var="result" items="${resultList}" varStatus="status">
                                    <tr>
                                        <td class="num"><c:out value="${paginationInfo.totalRecordCount+1 - ((funcImprvmFormVO.searchFuncImprvmVO.pageIndex-1) * funcImprvmFormVO.searchFuncImprvmVO.pageSize + status.count)}"/></td>
                                        <td class="num"><c:out value="${result.fnctImprvmNo}"/></td>
                                        <c:if test="${empty result.fnctImprvmNo }">
                                        	<td><a href="javascript:fncCreateView('<c:out value="${result.srvcRsponsNo}"/>')" class="ellipsis"><c:out value="${result.srvcRsponsSj}"/></a></td>
                                        </c:if>
                                        <c:if test="${!empty result.fnctImprvmNo }">
                                        	<td><a href="javascript:fncUpdateView('<c:out value="${result.fnctImprvmNo}"/>')" class="ellipsis"><c:out value="${result.srvcRsponsSj}"/></a></td>
                                        </c:if>
                                        <td class="num"><c:out value="${result.srvcRsponsNo}"/></td>
                                        <td><c:out value="${result.trgetSrvcCodeNm}"/></td>
                                        <td class="num"><c:out value="${result.requstDtDateDisplay}"/> <c:out value="${result.requstDtTimeDisplay}"/></td>
                                        <td class="num"><c:out value="${resultMap.get(result.srvcRsponsNo).applyPlanDtDisplay}"/></td>
                                        <td class="num"><c:out value="${resultMap.get(result.srvcRsponsNo).applyRDtDisplay}"/></td>
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
                
                <!-- Modal -->
				<div class="modal fade" id="downloadModal" tabindex="-1" role="dialog" >
				  <div class="modal-dialog" role="document">
				    <div class="modal-content">
				      <div class="modal-header">
				        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				      </div>
				      <div class="modal-body">
				        <div class="container">
				           <h5 class="form-signin-heading">년월 입력 (미입력시 - ${funcImprvmFormVO.processMt })</h5>
				           <input id="processMtInput" class="control" name="processMtInput" value="${funcImprvmFormVO.processMt }">
				        </div>
				      </div>
				      <div class="modal-footer">
				        <a onclick="fncRetrieveExcelList()" class="btn btn-primary" style="color:white;">확인</a>
				      </div>
				    </div>
				  </div>
				</div> 
				
                <!-- Modal -->
				<div class="modal fade" id="pdfDownloadModal" tabindex="-1" role="dialog" >
				  <div class="modal-dialog" role="document">
				    <div class="modal-content">
				      <div class="modal-header">
				        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				      </div>
				      <div class="modal-body">
				        <div class="container">
				           <h5 class="form-signin-heading">년월 입력 (미입력시 - ${funcImprvmFormVO.processMt })</h5>
				           <input id="processMtInputPdf" class="control" name="processMtInputPdf" value="${funcImprvmFormVO.processMt }">
				        </div>
				      </div>
				      <div class="modal-footer">
				        <a onclick="fncRetrieveReport()" class="btn btn-primary" style="color:white;">확인</a>
				      </div>
				    </div>
				  </div>
				</div> 
               
            </form:form>
        </main>
</body>
</html>
