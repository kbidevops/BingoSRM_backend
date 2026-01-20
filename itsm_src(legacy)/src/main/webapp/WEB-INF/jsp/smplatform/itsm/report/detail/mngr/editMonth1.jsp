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
    <script type="text/javascript" src="/smarteditor/js/service/HuskyEZCreator.js" charset="utf-8"></script>
    <script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
    <validator:javascript formName="repFormVO" staticJavascript="false" xhtml="true" cdata="false"/>
</head>
<body>
	<main id="content">
	   <form:form commandName="repFormVO" id="listForm" name="listForm" method="post" enctype="multipart/form-data">
          <form:hidden path="repDetailVO.saveToken" />
          <form:hidden path="repDetailVO.repTyCode" id="repTyCode" value="B003"/>
          <form:hidden path="searchDetailVO.repTyCode" value="B003"/>
          <form:hidden path="repMasterVO.repSn" id="repSn"/>
          <input type="hidden" name="registerFlag" value="${registerFlag }">
		  <input type="hidden" name="repDetailVO.checkDuo" value="false"/>
			<div class="page-header">
	            <div class="row">
		            <div class="col-4">
		            	<h3 class="page-title">
		            	<strong> <c:out value="${month}"/>월 </strong>
		            	월간<c:out value="${LEFT_MENU_PROGRMACCESAUTHORVO.progrmNm}"/>
		            	</h3>
		            </div>
		            
		            <div class="col-4">
		            <c:if test="${registerFlag == 'create'}">
	                <div class="input-group">
		            	 <form:input id="reportDt" style="display:none;" path="repDetailVO.reportDtDisplay" class="control singleDate" title="작성일 선택기"/>
		            </div>
		            </c:if>
		            </div>
		            
		            <div class="col-4 text-right">
		            	<c:if test="${registerFlag == 'create'}">
		            	<button type="button" onclick="fncRetrieveRep()" class="btn btn-primary">이전 보고서 불러오기</button>
	                	</c:if>
	                    <button type="button" onclick="fncRetrieveList()" class="btn btn-secondary"><i class="ico-list"></i>목록</button>
		            	<c:if test="${registerFlag != 'create'}">
	                	<c:if test="${chargerId == LOGIN_USER_VO.userId }">
		                    	<button type="button" onclick="fncDelete()" class="btn btn-dark"><i class="ico-delete"></i>삭제</button>
	                	</c:if>
	                    </c:if>
	                    <c:if test="${registerFlag == 'create' || chargerId == LOGIN_USER_VO.userId}">
	                    	<button type="button" onclick="fncSave()" class="btn btn-primary"><i class="ico-save"></i>저장</button>
	                    </c:if>
		            </div>
	            </div>
	        </div>
            <textarea style="width:1500px;height:500px;" id="ir1" name="ir1"></textarea><br>
           
	        <div class="btnbox right mb30">
	            <button type="button" onclick="fncRetrieveList()" class="btn btn-secondary"><i class="ico-list"></i>목록</button>
	            <c:if test="${registerFlag != 'create'}">
	            <button type="button" onclick="fncDelete()" class="btn btn-dark"><i class="ico-delete"></i>삭제</button>
	            </c:if>
	            <button type="button" onclick="fncSave()" class="btn btn-primary"><i class="ico-save"></i>저장</button>
	        </div>
	        
	        <div id="hideDiv" style="display:none">
	        </div>
	            
       </form:form>
    </main>

<script type="text/x-tamplate" id="report-template">
<div align="left" style="text-align: left;">
	<b style="font-size: 14.6667px;">[시스템명]</b><br>
	<span style="font-size: 14.6667px;">&nbsp;- 샘플입니다. 이렇게 작성해주세요.<br></span>
	<span style="font-size: 14.6667px;">&nbsp;- 문단 시작번호, 1수준 마이너스 기호 입니다.<br></span>
	<span style="font-size: 14.6667px;">&nbsp; &nbsp;· 2수준 점 입니다.<br></span>
	<span style="font-size: 14.6667px;">&nbsp; &nbsp; &nbsp;→ 3수준 화살표 입니다.<br></span>
<div/><br>

<div align="center">
	<table style="width:100%;margin-bottom:30px; border-collapse:separate;empty-cells:show;border-radius:5px;overflow:hidden;border:1px solid #b5c8e0">
		<colgroup>
		    <col style="width:10%">
		    <col style="width:45%">
		    <col style="width:45%">
		</colgroup>
		<thead>
		   <tr>
		        <th style="background:#d7e2ef;font-weight:700;border-bottom:1px solid #b5c8e0;border-right:1px solid #b5c8e0" scope="col">구분</th>
		        <th style="background:#d7e2ef;font-weight:700;border-bottom:1px solid #b5c8e0;border-right:1px solid #b5c8e0" scope="col">실적</th>
		        <th style="background:#d7e2ef;font-weight:700;border-bottom:1px solid #b5c8e0;border-right:1px solid #b5c8e0" scope="col">계획</th>
		   </tr>
		</thead>
		<tbody>
		<c:forEach items="${repDetailVOList }" var="repDetailVO">
			<input type="hidden" class="sysCodes" name="sysCodes" value="${repDetailVO.sysCode }">
			<input id="${repDetailVO.sysCode }execDesc" type="hidden" name="execDescs">
	   		<input id="${repDetailVO.sysCode }planDesc" type="hidden" name="planDescs">
			<c:if test="${registerFlag != 'create' }"><input type="hidden" name="repSns" value="${repDetailVO.repSn }"></c:if>
			<c:if test="${repDetailVO.sysCode != 'B129'}">
				<tr style="height:50px;">
				<th id="${repDetailVO.sysCode }" scope="row" style="border-right:1px solid #b5c8e0;border-top: 1px solid #eaf0f7;background:#f9fbfd;font-weight:500">
					<b><c:out value="${repDetailVO.sysCodeNm }" escapeXml="false"/></b>
					<c:if test="${LOGIN_USER_VO.userTyCode == 'R003' }">
						<span id="sysCodeNm" class="label type2"><c:out value="${repDetailVO.sysCodeNm }" escapeXml="false"/></span>
					</c:if>
				</th>
				<td style="border-top: 1px solid #eaf0f7;border-right:1px solid #d7e2ef;background:#fff;vertical-align:top" id="execDesc">
					<c:if test="${registerFlag != 'create' }">${repDetailVO.execDesc}</c:if>
					<c:if test="${registerFlag == 'create' && repDetailVO.sysCode == 'B100' }">今月  추진실적 (${dayRange.get(0)} ~ ${dayRange.get(1)})</c:if>
				</td>
				<td style="border-top: 1px solid #eaf0f7;border-right:1px solid #d7e2ef;background:#fff;vertical-align:top" id="planDesc">
					<c:if test="${registerFlag != 'create' }">${repDetailVO.planDesc}</c:if>
					<c:if test="${registerFlag == 'create' && repDetailVO.sysCode == 'B100' }">次月  추진계획 (${dayRange.get(2)} ~ ${dayRange.get(3)})</c:if>
					</td>
				</tr>
			</c:if>
			<c:if test="${repDetailVO.sysCode == 'B129'}">
			<tr style="height:50px;">
				<th id="${repDetailVO.sysCode }" scope="row" style="border-right:1px solid #b5c8e0;border-top: 1px solid #eaf0f7;background:#f9fbfd;font-weight:500">
					<b>${repDetailVO.sysCodeNm }</b>
					<c:if test="${LOGIN_USER_VO.userTyCode == 'R003' }">
						<span id="sysCodeNm" class="label type2"> <c:out value="${repDetailVO.sysCodeNm }" escapeXml="false"/></span>
					</c:if>
				</th>
				<td colspan="2" id="execDesc" style="border-top: 1px solid #eaf0f7;border-right:1px solid #d7e2ef;background:#fff;vertical-align:top" id="execDesc">
					<c:if test="${registerFlag != 'create' }"> <c:out value="${repDetailVO.execDesc }" escapeXml="false"/></c:if>
				</td>
			</tr>
			</c:if>
		</c:forEach>
		</tbody>
	</table>
</div>
</script>          
<script type="text/javascript" language="javascript" defer="defer">
	var oEditors = [];
	$(function(){
		nhn.husky.EZCreator.createInIFrame({
		 oAppRef: oEditors,
		 elPlaceHolder: "ir1",
		 sSkinURI: "/smarteditor/SmartEditor2Skin_ko_KR.html",
		 htParams:{
			 bUseModeChanger:true,
		 },
		 fOnAppLoad:function(){
				//LOAD_CONTENTS_FIELD - 내용 전체 교체
				//PASTE_HTML - 내용붙여넣기
				//UPDATE_CONTENTS_FIELD - HTML 받기
				oEditors.getById["ir1"].exec("PASTE_HTML",[document.querySelector('#report-template').textContent]);
		 },
		 
		 fCreator: "createSEditor2"
		});
	});
	
	function fncSave(){
		oEditors.getById["ir1"].exec("UPDATE_CONTENTS_FIELD",[]);
		$("#hideDiv").html($("#ir1").val());
		
		$("#repTyCode").val("B003");
		
		var sysCodes = $("#hideDiv input[name='sysCodes']");
		
		
		for(var i=0; i<sysCodes.length; i++){
			var sysCode = sysCodes[i].value;
			
			$("#"+sysCode+"execDesc").val($("#"+sysCode).siblings("#execDesc").html())
			$("#"+sysCode+"planDesc").val($("#"+sysCode).siblings("#planDesc").html())
		}
		
		<c:if test="${registerFlag == 'create'}">
		document.listForm.action = "<c:url value='/itsm/rep/detail/mngr/create.do'/>";
		document.listForm.submit();                                   
		</c:if>
		
		<c:if test="${registerFlag != 'create'}">
		document.listForm.action = "<c:url value='/itsm/rep/detail/mngr/update.do'/>";
		document.listForm.submit();
		</c:if>
		
	}
	function fncDelete(){
		confirm('삭제하시겠습니까?','보고서 삭제', null, function(request){
			if(request){
				oEditors.getById["ir1"].exec("UPDATE_CONTENTS_FIELD",[])
				$("#hideDiv").html($("#ir1").val())
				
				document.listForm.action = "<c:url value='/itsm/rep/detail/mngr/delete.do'/>";
				document.listForm.submit();
			}
		})
		
	}
	function fncRetrieveRep(){
			
			if($("#creatDt").val() == ''){
				alert("날짜를 선택해주세요");
				return;
			}
			
			$.ajax({
		  		type : "post",
		  		url : "<c:url value='/itsm/rep/detail/mngr/retreiveRepDetailAjax.do'/>",
		  		data : {
		  			"reportDt" : document.listForm.elements.reportDt.value,
		  			"repTyCode" : "B003"
		  		},
		  		dataType : "json",
		  		success : function(request){
		  			
		  			console.log(request);
		  			oEditors.getById["ir1"].exec("UPDATE_CONTENTS_FIELD",[])
		  			$("#hideDiv").html($("#ir1").val())
		  			
		  			$.each(request.detailVOList, function(idx, detailVO){
		  				var exec = document.getElementById(detailVO.sysCode).nextElementSibling;
		  				exec.innerHTML = detailVO.execDesc;
		  				if(detailVO.sysCode != "B129"){
			  				exec.nextElementSibling.innerHTML = detailVO.planDesc;
		  				}
		  			});
		  			
		  			oEditors.getById["ir1"].exec("SET_IR",[""]);
		  			oEditors.getById["ir1"].exec("PASTE_HTML",[document.getElementById("hideDiv").innerHTML]);
		  			
		  		},
		  		error : function(xhr){
		  			alert("불러올 보고서가 없습니다.");
		  			console.log(xhr.status);
		  		}
		  	})
	}

	function fncRetrieveList(){
		$("#creatDt").val("")
		document.listForm.action = "<c:url value='/itsm/rep/detail/mngr/retrievePagingList.do'/>";
	   	document.listForm.submit();
	}
	
	/*  
		보고서 중복검증하는 함수
	*/
	function overlapValidation(){
	    //sysCode 배열로 만들기
	    var sysValue = $("input[name='sysCodes']").length;
	    var sysCodes = new Array(sysValue);
	    for(var i=0; i<sysValue; i++){                          
	    	sysCodes[i] = $("input[name='sysCodes']")[i].value;
	    }
	
	 	$.ajax({
	 		type : "post",
	 		url : "<c:url value='/itsm/rep/detail/mngr/retrieveAjax.do'/>",
	 		data : {
	 			"reportDt" : frm.elements.reportDt.value,
	 			"sysCodes" : sysCodes,
	 			"repTyCode" : "B002"
	 		},
	 		dataType : "json",
	 		traditional: true,
	 		success : function(request){
	 			console.log(request);
	 			//생성 가능
	 			if(request.returnMessage == "create"){
	 				document.listForm.action = "<c:url value='/itsm/rep/detail/mngr/create.do'/>";
	 				document.listForm.submit();
	 			}
	 			else if(request.returnMessage == "confirm"){//마스터 보고서가 확정됨
	 				alert("같은 날짜에 확정된 보고서가 있습니다. \n관리자에게 문의해주세요.")
	 			}else{//중복된 보고서 있음
	 				$("#repSn").val(request.returnMessage);
					document.listForm.action = "<c:url value='/itsm/rep/detail/mngr/update.do'/>";
					document.listForm.submit();
	 			}
	 		},
	 		error : function(xhr){
	 			alert("보고서를 등록하는데 실패하였습니다. 관리자에게 문의해주세요.");
	 		}
	 		
	 	})
	}
</script>
</body>
</html>
