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
	var trgetSrvcCodeNm = "";
	$(document).ready(function() {
		<c:if test="${searchWdtbVO.wdtbDt == null }">
    	document.listForm.elements['searchWdtbVO.wdtbDtDateDisplay'].value = null;
    	</c:if>
    	
    	//SR선택시 같은 대상서비스만 선택할수있게 해주는 로직
    	$("input[name='srvcRsponsNos']").change(function(){
    		if($(this).is(":checked")){
    			var thisSrvcCodeNm = $(this).parent().siblings(".trgetSrvcCodeNm").text();
    			
    			if(trgetSrvcCodeNm == ""){
	    			trgetSrvcCodeNm = thisSrvcCodeNm;
    			}else if(trgetSrvcCodeNm != thisSrvcCodeNm){
    				alert("해당 SR은 선택하신 SR과 대상서비스가 다릅니다.")
    				$(this).prop("checked", false);
    			}
    		}else{
    			if($("input[name='srvcRsponsNos']:checked").length < 1){
    				trgetSrvcCodeNm = "";
    			} 
    		}
    	})
    	
	});
	
	/* 글 등록 화면 function */
    function fncCreateView() {
		var srvcNos = $("#listForm input[type='checkbox']:checked");
		
		if(srvcNos.length > 0){
			$.each(srvcNos, function(i,srvcNo){
				$("#"+srvcNo.id).val(srvcNo.id);
			})
			
	       	document.listForm.action = "<c:url value='/itsm/wdtb/mngr/createView.do'/>";
	       	document.listForm.submit();
		}else{
			alert("SR을 하나 이상 선택해주세요.")
		}
    }

    function fncUpdateView(srvcRsponsNo,wdtbCnfirmNo) {
    	console.log(srvcRsponsNo);
    	
    	if(wdtbCnfirmNo == '') {
    		if($("#"+srvcRsponsNo).is(":disabled")){
    			alert("기능개선을 먼저 작성해주세요.");
    		}else{
	    		$("#"+srvcRsponsNo).prop("checked", true);
	    		fncCreateView()
    		}
    	}else{
    		fncRetrieveWdtbAjax(srvcRsponsNo,wdtbCnfirmNo)
    		
    	}
    	
    }
    
    function fncRetrieveWdtbAjax(srvcRsponsNo,wdtbCnfirmNo){
    	$.ajax({
	  		type : "post",
	  		url : "<c:url value='/itsm/wdtb/mngr/retrieveWdtbAjax.do'/>",
	  		data : {
	  			"wdtbCnfirmNo" : wdtbCnfirmNo,
	  			"srvcRsponsNo" : srvcRsponsNo
	  		},
	  		dataType : "json",
	  		success : function(request){
	  			console.log(request);
	  			if(request.returnMessage == "empty"){
	  				alert("작성된 배포확인서가 없습니다.")
	  				return;
	  			}else{
	  				document.listForm.elements['srvcRsponsVO.srvcRsponsNo'].value = srvcRsponsNo;
	  				document.listForm.elements['wdtbVO.srvcRsponsNo'].value = srvcRsponsNo;
	  		  		document.listForm.action = "<c:url value='/itsm/wdtb/mngr/updateView.do'/>";
	  		  		document.listForm.submit()
	  			}
	  		},
	  		error : function(xhr){
	  			alert("보고서를 불러오는데 실패하였습니다.");
	  			console.log(xhr.status);
	  		}
	  	})
    }
    
    function fncDownloadModal(){
    	$("#downloadModal").modal("show");
    }
    
    function fncRetrieveExcelList() {
    	
		var processMt = $("#processMtInput").val();
    	if($.trim(processMt) != ''){
    		document.listForm.elements['srvcRsponsVO.processMt'].value = processMt;	
    	}
		
    	$("#downloadModal").modal("hide");
    	
    	document.listForm.action = "<c:url value='/itsm/wdtb/mngr/retrieveExcelList.do'/>";
       	document.listForm.submit();
    }
    function fncRetrievePagingList(pageNo){
    	document.listForm.elements['searchWdtbVO.pageIndex'].value = pageNo;
    	document.listForm.action = "<c:url value='/itsm/wdtb/mngr/retrievePagingList.do'/>";
       	document.listForm.submit();
    }
    function fncRetrieveList() {
    	document.listForm.action = "<c:url value='/itsm/wdtb/mngr/retrievePagingList.do'/>";
    	document.listForm.elements['searchWdtbVO.pageIndex'].value = 1;
       	document.listForm.submit();
    }
    function fncReset(){
    	document.listForm.elements['searchWdtbVO.wdtbCnfirmNo'].value = '';
    	document.listForm.elements['searchWdtbVO.chargerId'].value = '';
    	document.listForm.elements['searchWdtbVO.trgetSrvcCode'].value = '';
    	document.listForm.elements['searchWdtbVO.wdtbDtDateDisplay'].value = '';
    }
    
    //PDF 다운로드
    function fncPdfDownloadModal(){
    	$("#pdfDownloadModal").modal("show");
    }
    
    function fncRetrieveReport(){
    	var processMt = $("#processMtInputPdf").val();
    	$("#pdfDownloadModal").modal("hide");
    	
    	$.ajax({
	  		type : "post",
	  		url : "<c:url value='/itsm/wdtb/mngr/retrieveWdtbAjax.do'/>",
	  		data : {
	  			"processMt" : processMt,
	  		},
	  		dataType : "json",
	  		success : function(request){
	  			console.log(request);
	  			if(request.returnMessage == "empty"){
	  				alert("작성된 배포확인서가 없습니다.")
	  				return request.returnMessage;
	  			}else{
	  				var ozrFileNm = "/knfc/wdtbCnfirm.ozr";
	  				var odiFileNm = "/knfc/wdtbCnfirm.odi";
				    procPrintList(ozrFileNm, odiFileNm, request.wdtbList);
	  				
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
	
	var procPrintList = function(ozrFileNm, odiFileNm, wdtbList) {
		
		var keyNo = "";
		for(var i=0; i<wdtbList.length; i++){
			console.log(wdtbList[i].wdtbCnfirmNo)
			if(i==0){
				keyNo = keyNo + wdtbList[i].wdtbCnfirmNo;
			}else{
				keyNo = keyNo +","+ wdtbList[i].wdtbCnfirmNo;
			}
		}
		
		var filename = "_솔루션배포확인서";
		
	    var jsonData = "${jsonData }";
	    jsonData = jsonData.replaceAll("'", '"');
	    
	    var ozparams = {"ozr" : ozrFileNm
	                   , "odi" : odiFileNm
	                   , "filename" : filename
	                   , "keyNo" : keyNo
	                   , "length" : (wdtbList.length-1)
	                   , "pcount" : 1       // pcount는 jsonData 가 있으므로 밑의 params 갯수보다 1+해서 적어준다.
	                   , "params" : [ 
	                	  			]
	                   };
	    
	    var popUrl = "<c:url value='/itsm/rep/master/mngr/OzReportViewerMulti.do'/>";
		
	    var $formOzReport = $( "<form action='" + popUrl + "' method='post', target='_blank' id='formOzReport' name='formOzReport'/>" );
	    var html = $("<input type='hidden' name='ozparams' value='"+JSON.stringify(ozparams)+"'/>"
	            +"<input type='hidden' name='jsonData' value='"+jsonData+"'/>"
	            );
		
	    $formOzReport.append(html);
		
	    $('body').append( $formOzReport );
	    $formOzReport.submit();
	};
    </script>
    <style type="text/css">
    </style>
</head>
	

<body>
		<main id="content">
            <div class="page-header">
                <h3 class="page-title"><c:out value="${LEFT_MENU_PROGRMACCESAUTHORVO.progrmNm}"/></h3>
            </div>
				
		    <form:form commandName="wdtbFormVO" id="listForm" name="listForm" method="post">
				<form:hidden path="wdtbVO.srvcRsponsNo" />
				<form:hidden path="srvcRsponsVO.srvcRsponsNo" />
<%-- 				<form:hidden path="funcImprvmVO.fnctImprvmNo" /> --%>
				<form:hidden path="searchWdtbVO.pageIndex" />
				<form:hidden path="srvcRsponsVO.processMt" />
				
                <div class="row">
                    <div class="col-fix">
                        <div class="card">
                            <div class="card-header">
                                <h4 class="card-title">검색조건</h4>
                            </div>
                            <div class="card-body">
                                <div class="detailbox type1">
                                    <dl>
                                        <dt>배포번호</dt>
                                        <dd><form:input path="searchWdtbVO.wdtbCnfirmNo" class="control" title="배포번호"/></dd>
                                    </dl>
                                    <dl>
                                        <dt>담당자</dt>
                                        <dd>
                                        	<form:select path="searchWdtbVO.chargerId" cssClass="control" title="대상서비스">
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
                                          <form:select path="searchWdtbVO.trgetSrvcCode" cssClass="control" title="대상서비스">
										  	<form:option value="">전체</form:option>
										  	<form:options items="${trgetSrvcCodeVOList}" itemLabel="cmmnCodeNm" itemValue="cmmnCode"/>
										  </form:select>
                                        </dd>
                                    </dl>
                                    <dl>
                                        <dt>배포일</dt>
                                        <dd>
                                        	<form:input id="wdtbDt" path="searchWdtbVO.wdtbDtDateDisplay" class="control singleDate" title="작성일 선택기"/>
                                        </dd>
                                    </dl>
                                </div> 
                                <hr>
                                <div class="detailbox type1">
                                    <dl>
                                        <dt>제목</dt>
                                        <dd><form:input path="searchWdtbVO.srvcRsponsSj" class="control" title="제목"/></dd>
                                    </dl>
                                    <dl>
                                        <dt>내용</dt>
                                        <dd><form:input path="searchWdtbVO.srvcRsponsCn" class="control" title="내용"/></dd>
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
<%--                                	<button type="button" onclick="fncPdfDownloadModal()" class="btn btn-dark"><i class="ico-download"></i>pdf다운로드</button>--%>
                                    <button type="button" onclick="fncDownloadModal()" class="btn btn-dark"><i class="ico-download"></i>다운로드</button>
                                    <button type="button" onclick="fncCreateView()" class="btn btn-primary"><i class="ico-write"></i>등록</button>
                                </div>
                            </div>
                            
                            <table class="gridtbl">
                                <caption>배포 현황 목록</caption>
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
                                        <th scope="col">배포번호</th>
                                        <th scope="col">SR제목</th>
                                        <th scope="col">SR번호</th>
                                        <th scope="col">대상서비스</th>
                                        <th scope="col">요청일</th>
                                        <th scope="col">배포일자</th>
                                        <th scope="col">기능개선번호</th>
                                    </tr>
                                </thead>
                                <tbody>
                                  <c:forEach var="result" items="${resultList}" varStatus="status">
                                    <tr>
                                    	<c:choose>
	                                    	<c:when test="${empty result.wdtbCnfirmNo && result.progrmUpdtYn == 'N'}">
	                                        	<td class="num"><input type="checkbox" id="${result.srvcRsponsNo}" name="srvcRsponsNos" class="checkbox"><label for="${result.srvcRsponsNo}"></label></td>
	                                    	</c:when>
	                                    	<c:when test="${result.progrmUpdtYn == 'Y' && !empty result.fnctImprvmNo && empty result.wdtbCnfirmNo}">
	                                        	<td class="num"><input type="checkbox" id="${result.srvcRsponsNo}" name="srvcRsponsNos" class="checkbox"><label for="${result.srvcRsponsNo}"></label></td>
	                                    	</c:when>
	                                    	<c:otherwise>
	                                       		<td class="num"><input type="checkbox" disabled="disabled" id="${result.srvcRsponsNo}" name="srvcRsponsNos" class="checkbox"><label for="${result.srvcRsponsNo}"></label></td>
	                                    	</c:otherwise>
                                    	</c:choose>
                                        <td class="num"><c:out value="${result.wdtbCnfirmNo}"/></td>
                                        <td><a href="javascript:fncUpdateView('<c:out value="${result.srvcRsponsNo}"/>','<c:out value="${result.wdtbCnfirmNo}"/>')" class="ellipsis"><c:out value="${result.srvcRsponsSj}"/></a></td>
                                        <td class="num"><c:out value="${result.srvcRsponsNo}"/></td>
                                        <td class="trgetSrvcCodeNm"><c:out value="${result.trgetSrvcCodeNm}"/></td>
                                        <td class="num"><c:out value="${result.requstDtDateDisplay}"/> <c:out value="${result.requstDtTimeDisplay}"/></td>
                                        <td class="num"><c:out value="${result.srvcWdtbDtDateDisplay}"/> <c:out value="${result.srvcWdtbDtTimeDisplay}"/></td>
                                        <td class="num"><c:out value="${result.fnctImprvmNo}"/></td>
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
				           <h5 class="form-signin-heading">년월 입력 (미입력시 - ${wdtbFormVO.processMt })</h5>
				           <input id="processMtInput" class="control" name="processMtInput" value="${wdtbFormVO.processMt }">
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
				           <h5 class="form-signin-heading">년월 입력 (미입력시 - ${wdtbFormVO.processMt })</h5>
				           <input id="processMtInputPdf" class="control" name="processMtInputPdf" value="${wdtbFormVO.processMt }">
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
