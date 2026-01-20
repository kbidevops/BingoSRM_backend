<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="validator"
	uri="http://www.springmodules.org/tags/commons-validator"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<c:set var="registerFlag" value="${assetsFormVO.assetsVO.assetsSn == 0 ? 'create' : 'modify'}" />
<title><c:out value="${LEFT_MENU_PROGRMACCESAUTHORVO.progrmNm}" />
	<c:out value="${registerFlag == 'create' ? '등록' : '수정'}" /></title>
<!--For Commons Validator Client Side-->
<script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
<script type="text/javaScript" language="javascript" defer="defer">
var licenseAtchmnflFileCnt = 0;
var manualAtchmnflFileCnt = 0;

$(function(){
	$("#indcDtDisplay").mask("9999-99-99");
	$("#chargerCttpc").mask("999-9999-9999");
	
	$('#licenseAtchmnflSendBtn').hide();
	$('#manualAtchmnflSendBtn').hide();
	var atchmnflCnt = 0;
	var url = "<c:url value='/itsm/atchmnfl/site/create.do'/>";
	
	$('#licenseAtchmnfl').fileupload({
		 url: url,
		 dataType: 'json',
		 formData: {atchmnflId: $("#licenseAtchmnflId").val()},
		 sequentialUploads: true,
		 add: function (e, data) {
			 var valid = true;
			 var re = /^.+\.((doc)|(hwpx)|(hwp)|(pdf)|(pts)|(zip)|(jpg)|(png)|(jpeg))$/i;
			 
			 $.each(data.files, function (index, file) {
			        console.log('Added file: ' + file.name);
			        console.log('Added file: ' + file.type);
			        console.log('Added file: ' + file.size);
			        console.log('Added file: ' + filesize(file.size));
//			   		console.log('file size: ' + data.getNumberOfFiles());
			        
			        licenseAtchmnflFileCnt++;
			        
			     	if (!re.test(file.name)) {
			     		valid = false;
			     		alert(file.name+' <br/>허용되지 않은 첨부파일은 제외됩니다.');
			     		licenseAtchmnflFileCnt--;
			     	}else if (file.size > 5000000){ //5M
			     	    console.log('5M이하의 파일만 허용 합니다.');
			     		alert(file.name+' <br/>5M이상의 첨부파일은 제외됩니다.');
			     		licenseAtchmnflFileCnt--;
			     		valid = false;
			     	}else if($('#licenseAtchmnflListDiv li').length > atchmnflCnt){
			     		console.log('5개의 파일만 첨부 가능합니다.');
			     		alert(file.name+' <br/>5개의 파일만 첨부 가능합니다.');
			     	}else if (licenseAtchmnflFileCnt>5){ //동시 업로드 5개 제한
   			     		console.log('5개의 파일까지 허용 합니다.'+licenseAtchmnflFileCnt);
			     		alert(file.name+' <br/>동시에 5개의 파일만 첨부 허용 합니다.');
			     		licenseAtchmnflFileCnt--;
			     		valid = false;
			     	}else{
			     		console.log('정상등록 '+licenseAtchmnflFileCnt);
			     		var abortBtn = $( '<a/>' )
		                     .attr( 'href', 'javascript:void(0)' )
		                     .attr( 'class', 'close' )
		                     .attr( 'aria-label', 'Close' )
		                     .append( '<span aria-hidden="true">&times;</span>' )
		                     .click( function() {
			                    	data.abort();
			                        data.context.remove();
			                        data = null;
			                        if(licenseAtchmnflFileCnt>0){						                        	
			                        	licenseAtchmnflFileCnt--;
			                        	console.log('licenseAtchmnflFileCnt: '+licenseAtchmnflFileCnt);
			                        }
		                     });
			     		
 			        	data.context = $( '<div/>' ).appendTo($('#licenseAtchmnflListDiv'));
   			         	data.context.attr( 'class', 'form-row' );
   			         	data.context.append(abortBtn).append(file.name+'('+filesize(file.size)+')');
			     	}
			        
			     });
			 
			 $("#licenseAtchmnflSendBtn").click(function () {        				   
			        // validate file...        			        
			        if(valid && data != null){
			        	data.submit();
			        	//해당 폼 전송 추가 하면 저장 버튼 완료
			        }
			            
			  });
		 },
		 done: function (e, data) {
			 console.log('done: '+data.errorThrown);
			 console.log('done: '+data.textStatus);
			 console.log('done: '+data.jqXHR);
		 },
		 progressall: function (e, data) {
			if(manualAtchmnflFileCnt > 0){
		    	$("#manualAtchmnflSendBtn").trigger("click");
         	}else{
         		defaultValue()
				if($("input[name='assetsVO.assetsSe1']:checked").val() == 'T001'){
					document.listForm.sw.disabled = "true";
				}else{
					document.listForm.hw.disabled = "true";
				}
				
				if(document.listForm.etc.checked){
					var purchsMthds = $("input[name='assetsVO.purchsMthd']");
					$.each(purchsMthds, function(idx, purchsMthd){
						if(purchsMthd.id != "etcInput"){
							purchsMthd.disabled = "true";
						}
					})
				}
	   	        frm.action = "<c:url value="${registerFlag == 'create' ? '/itsm/assets/mngr/create.do' : '/itsm/assets/mngr/update.do'}"/>";
	   	        frm.submit();	
         	}
		 },
		    progressServerRate: 0.3
	});
	
	$('#manualAtchmnfl').fileupload({
		 url: url,
		 dataType: 'json',
		 formData: {atchmnflId: $("#manualAtchmnflId").val()},
		 sequentialUploads: true,
		 add: function (e, data) {
			 var valid = true;
			 var re = /^.+\.((doc)|(hwpx)|(hwp)|(pdf)|(pts)|(zip)|(jpg)|(png)|(jpeg))$/i;
			 
			 $.each(data.files, function (index, file) {
			        console.log('Added file: ' + file.name);
			        console.log('Added file: ' + file.type);
			        console.log('Added file: ' + file.size);
			        console.log('Added file: ' + filesize(file.size));
//			   		console.log('file size: ' + data.getNumberOfFiles());
			        
			        manualAtchmnflFileCnt++;
			        
			     	if (!re.test(file.name)) {
			     		valid = false;
			     		alert(file.name+' <br/>허용되지 않은 첨부파일은 제외됩니다.');
			     		manualAtchmnflFileCnt--;
			     	}else if (file.size > 5000000){ //5M
			     	    console.log('5M이하의 파일만 허용 합니다.');
			     		alert(file.name+' <br/>5M이상의 첨부파일은 제외됩니다.');
			     		manualAtchmnflFileCnt--;
			     		valid = false;
			     	}else if($('#manualAtchmnflListDiv li').length > atchmnflCnt){
			     		console.log('5개의 파일만 첨부 가능합니다.');
			     		alert(file.name+' <br/>5개의 파일만 첨부 가능합니다.');
			     	}else if (manualAtchmnflFileCnt>5){ //동시 업로드 1개 제한
  			     		console.log('5개의 파일만 허용 합니다.'+manualAtchmnflFileCnt);
			     		alert(file.name+' <br/>동시에 5개의 파일만 첨부 허용 합니다.');
			     		manualAtchmnflFileCnt--;
			     		valid = false;
			     	}else{
			     		console.log('정상등록 '+manualAtchmnflFileCnt);
			     		var abortBtn = $( '<a/>' )
		                     .attr( 'href', 'javascript:void(0)' )
		                     .attr( 'class', 'close' )
		                     .attr( 'aria-label', 'Close' )
		                     .append( '<span aria-hidden="true">&times;</span>' )
		                     .click( function() {
			                    	data.abort();
			                        data.context.remove();
			                        data = null;
			                        if(manualAtchmnflFileCnt>0){						                        	
			                        	manualAtchmnflFileCnt--;
			                        	console.log('manualAtchmnflFileCnt: '+manualAtchmnflFileCnt);
			                        }
		                     } );
			     		
			         data.context = $( '<div/>' ).appendTo($('#manualAtchmnflListDiv'));
  			         data.context.attr( 'class', 'form-row' );
  			         data.context.append(abortBtn).append(file.name+'('+filesize(file.size)+')');
			     	}
			        
			     });
			 
			 $("#manualAtchmnflSendBtn").click(function () {        				   
			        // validate file...        			        
			        if(valid && data != null){
			        	data.submit();
			        	//해당 폼 전송 추가 하면 저장 버튼 완료
			        }
			            
			  });
		},
		done: function (e, data) {
			 console.log('done: '+data.errorThrown);
			 console.log('done: '+data.textStatus);
			 console.log('done: '+data.jqXHR);
		 },
		progressall: function (e, data) {
			defaultValue()
			if($("input[name='assetsVO.assetsSe1']:checked").val() == 'T001'){
				document.listForm.sw.disabled = "true";
			}else{
				document.listForm.hw.disabled = "true";
			}
			
			if(document.listForm.etc.checked){
				var purchsMthds = $("input[name='assetsVO.purchsMthd']");
				$.each(purchsMthds, function(idx, purchsMthd){
					if(purchsMthd.id != "etcInput"){
						purchsMthd.disabled = "true";
					}
				})
			}
	   	    frm.action = "<c:url value="${registerFlag == 'create' ? '/itsm/assets/mngr/create.do' : '/itsm/assets/mngr/update.do'}"/>";
	   	    frm.submit();	
		 },
		    progressServerRate: 0.3
	});
	
	
	<c:if test="${registerFlag == 'create'}">
		$("#server").attr("checked", "checked");
		$("#indcUntpc").val("");
		$("#indcAmount").val("");
		$("#mntnceAmount").val("");
	</c:if>
	<c:if test="${registerFlag == 'modify'}">
		$("#indcUntpc").val(chkComma($("#indcUntpc")));
		$("#indcAmount").val(chkComma($("#indcAmount")));
		$("#mntnceAmount").val(chkComma($("#mntnceAmount")));
		
		console.log($("input[name='assetsVO.purchsMthd']:checked").length);
		if($("input[name='assetsVO.purchsMthd']:checked").length == 0){
			$("#etcInput").attr("disabled",false);
			$("#etc").prop("checked",true);
		}else{
			if($("input[name='assetsVO.purchsMthd']:checked").val() == ""){
				$("#etcInput").attr("disabled",false);
			}else{
				$("#etcInput").val("")
			}
		}
		
	</c:if>
	
	var infra = $("input[name='assetsVO.assetsSe1']:checked").val();
	if(infra == 'T001'){
		document.listForm.sw.style.display = "none"
	}else if(infra == 'T002'){
		document.listForm.hw.style.display = "none"
	}
	
	$("input[name='assetsVO.assetsSe1']").on("change", function(){
		if($(this).val() == 'T001'){
			document.listForm.sw.style.display = "none";
			document.listForm.hw.style.display = "";
		}else if($(this).val() == 'T002'){
			document.listForm.sw.style.display = "";
			document.listForm.hw.style.display = "none";
		}
	})
	
	
	/* 구매방식 직접입력 작성 */
	$("input[name='assetsVO.purchsMthd']").on("change", function(){
		var id = $(this).attr("id");
		if(id == "etc" || id == "etcInput"){
			$("#etcInput").prop("disabled",false);
		}else{
			$("#etcInput").prop("disabled",true);
		}
	});
})

function fncSave(){
	frm = document.listForm; 
	if(document.listForm.elements['assetsVO.instlLc'].value == ''){
		alert('설치위치를 선택하세요.');
		return;
	}
	
    confirm('저장하시겠습니까?','저장', null, function(request){
		console.log('request: '+request);
		if(request){
			
			
			//첨부파일 존재 유무 확인
		    if(licenseAtchmnflFileCnt>0 && manualAtchmnflFileCnt>0){
		    	$("#licenseAtchmnflSendBtn").trigger("click");	
		    }else if(manualAtchmnflFileCnt>0){
		    	$("#manualAtchmnflSendBtn").trigger("click");
		    }else{
		    	
		    	defaultValue()
				if($("input[name='assetsVO.assetsSe1']:checked").val() == 'T001'){
					document.listForm.sw.disabled = "true";
				}else{
					document.listForm.hw.disabled = "true";
				}
				
				if(document.listForm.etc.checked){
					var purchsMthds = $("input[name='assetsVO.purchsMthd']");
					$.each(purchsMthds, function(idx, purchsMthd){
						if(purchsMthd.id != "etcInput"){
							purchsMthd.disabled = "true";
						}
					})
				}
		    	
				document.listForm.action = "<c:url value="${registerFlag == 'create' ? '/itsm/assets/mngr/create.do' : '/itsm/assets/mngr/update.do'}"/>";
				document.listForm.submit();	
		    } 
		}
	});
}

function fncDelete(){
    confirm('삭제하시겠습니까?','자산관리 삭제', null, function(request){
		console.log('request: '+request);
		if(request){
			defaultValue()
			document.listForm.action = "<c:url value='/itsm/assets/mngr/delete.do'/>";
	        document.listForm.submit();
		}
	});
}
function fncRetrieveList(){
	defaultValue()
	document.listForm.action = "<c:url value='/itsm/assets/mngr/retrievePagingList.do'/>";
   	document.listForm.submit();
}

function fncDeleteFile(atchmnflId, atchmnflSn){
	console.log('atchmnflId: '+atchmnflId);
	console.log('atchmnflSn: '+atchmnflSn);
	$('#atchmnflId').val(atchmnflId);
	$('#atchmnflSn').val(atchmnflSn);
	confirm('해당 파일이 즉시 삭제됩니다. 계속 진행하시겠습니까?','파일 삭제', null, function(request){
		console.log('request: '+request);
		if(request){
			$.ajax({
    		    type:'POST',
    		    url:"<c:url value='/itsm/atchmnfl/site/delete.do'/>",
    		    dataType: "json",
    			data : { 
    				"atchmnflId":$('#atchmnflId').val(),
    				"atchmnflSn":$('#atchmnflSn').val()
    		   			},
    			async:false,
    			success : function(request){
    				console.log('request.returnMessage: '+request.returnMessage);
    				
    				if(request.returnMessage == 'success'){
    					console.log('delete: '+$('#fileId_'+$('#atchmnflId').val()+'_'+$('#atchmnflSn').val()));
    					console.log('delete: '+'#fileId_'+$('#atchmnflId').val()+'_'+$('#atchmnflSn').val());
    					$('#fileId_'+$('#atchmnflId').val()+'_'+$('#atchmnflSn').val()).remove(); 
    				}
    			},
    			error:function(r){
    				console.dir(r);
    				alert("정보전송에 실패하였습니다.");
    			}        			
    		});
		}
	});
}

function defaultValue(){
	if($("#indcUntpc").val() == ""){
		$("#indcUntpc").val("0")
	}else{
		$("#indcUntpc").val(unComma($("#indcUntpc").val()));
	}
	if($("#indcAmount").val() == ""){
		$("#indcAmount").val("0")
	}else{
		$("#indcAmount").val(unComma($("#indcAmount").val()));
	}
	if($("#mntnceAmount").val() == ""){
		$("#mntnceAmount").val("0")
	}else{
		$("#mntnceAmount").val(unComma($("#mntnceAmount").val()));
	}
}
function checkNumber(event, obj){
	if(event.keyCode < 48 || event.keyCode > 57){ //숫자 제외 입력불가
		return false;
	}
	/* if(obj.value.length == 0 && event.key == 0){ //첫자리 0 입력 불가
		return false;
	} */
}
/* 천단위 컴마 입력 */
function chkComma(obj){
	var str = $(obj).val();
	str = unComma(str);
	str = str.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	obj.value = str;
	return str;
}
/* 컴마 제거 */
function unComma(str){
	return str.replace(/[^\d]+/g, '');
}
/* 퍼센트 입력 */
function percentage(event, obj){
	checkNumber(event, obj)
	
	var per = obj.value.replace(/[^\d]/g,"");
	if(per < 0){
		obj.value = "0%";
		$(obj).setCursorPosition(1);
	}else if (per > 100){
		obj.value = "100%";
		$(obj).setCursorPosition(3);
	}else{
		obj.value = per + "%";
		$(obj).setCursorPosition(per.length);
	}
}

$.fn.setCursorPosition = function( pos )
{
  this.each( function( index, elem ) {
    if( elem.setSelectionRange ) {
      elem.setSelectionRange(pos, pos);
    } else if( elem.createTextRange ) {
      var range = elem.createTextRange();
      range.collapse(true);
      range.moveEnd('character', pos);
      range.moveStart('character', pos);
      range.select();
    }
  });
  
  return this;
};
</script>
</head>

<body>
	<main id="content">
	<div class="page-header">
		<h3 class="page-title">
			자산 관리(상세)
		</h3>
		<div class="btnbox fr">
			<button type="button" onclick="fncRetrieveList()" class="btn btn-secondary">
				<i class="ico-list"></i>목록
			</button>
			<c:if test="${registerFlag != 'create' && LOGIN_USER_VO.userTyCode == 'R001'}">
				<button type="button" onclick="fncDelete()" class="btn btn-dark">
					<i class="ico-delete"></i>삭제
				</button>
			</c:if>
			<button type="button" onclick="fncSave()" class="btn btn-primary">
				<i class="ico-save"></i>저장
			</button>
		</div>
	</div>
	<form name="downloadForm" action="<c:url value='/itsm/atchmnfl/site/retrieve.do'/>" target="">
		<input type="hidden" name="atchmnflId" id="atchmnflId" />
		<input type="hidden" name="atchmnflSn" id="atchmnflSn" />
	</form>
	<form:form commandName="assetsFormVO" id="listForm" name="listForm" method="post" enctype="multipart/form-data">
		<form:hidden path="assetsVO.saveToken"/>
		<form:hidden path="assetsVO.assetsSn"/>
		<form:hidden path="assetsVO.licenseAtchmnflId" id="licenseAtchmnflId" />
		<form:hidden path="assetsVO.manualAtchmnflId" id="manualAtchmnflId" />

		<div class="row equalheight">
			<div class="col-left">
				<div class="row">
					<div class="col">
						<!-- 요청자정보 -->
						<div class="card">
							<div class="card-header">
								<h5 class="card-title">기본정보</h5>
							</div>
							<div class="card-body">
								<div class="detailbox type1">
									<dl>
										<dt>인프라</dt>
                                        <dd>
 										  	<c:forEach items="${assetsSe1List}" var="assetsSe1">
 										  		<form:radiobutton path="assetsVO.assetsSe1" class="radio" id="${assetsSe1.cmmnCode }" 
 										  			label="${assetsSe1.cmmnCodeNm }"  value="${assetsSe1.cmmnCode }"/>
 										  	</c:forEach> 
                                        </dd>
									</dl>
									<dl>
		                                <dt>구분</dt>
		                                <dd>
	 										<form:select path="assetsVO.assetsSe2" class="control" title="구분" id="hw">
			                                    <c:forEach items="${hwList}" var="hw">
													<form:option value="${hw.cmmnCode }">${hw.cmmnCodeNm }</form:option>
		 										</c:forEach> 
											</form:select>
	 										<form:select path="assetsVO.assetsSe2" class="control" title="구분" id="sw">
			                                    <c:forEach items="${swList}" var="sw">
													<form:option value="${sw.cmmnCode }">${sw.cmmnCodeNm }</form:option>
		 										</c:forEach> 
											</form:select>
		                                </dd>
		                                <dd></dd>
		                                <dd></dd>
                               		</dl>
									<dl>
	                                    <dt>구매방식</dt>
	                                    <dd>
	                                    	<form:radiobutton path="assetsVO.purchsMthd" class="radio" id="indc"
	                                    	label="구축사업도입" value="구축사업도입"/>
	                                    	<form:radiobutton path="assetsVO.purchsMthd" class="radio" id="prcure"
	                                    	label="조달발주" value="조달발주"/>
		                                    <form:radiobutton path="assetsVO.purchsMthd" class="radio" id="etc"
		                                    label="직접입력" value=""/>
	                                    </dd>
                               		</dl>
									<dl>
										<dt>직접입력</dt>
										<dd>
											<form:input path="assetsVO.purchsMthd" class="control"
												title="직접입력" disabled="true" id="etcInput" />
										</dd>
									</dl>
									<hr>
									<dl>
										<dt>제조사</dt>
										<dd>
											<form:input path="assetsVO.maker" class="control"
												title="제조사" id="maker" />
										</dd>
									</dl>
									<dl>
										<dt>제품명</dt>
										<dd>
											<form:input path="assetsVO.productNm" class="control"
												title="제품명" id="productNm" />
										</dd>
									</dl>
									<dl>
										<dt>납품업체</dt>
										<dd>
											<form:input path="assetsVO.dlvgbiz" class="control"
												title="납품업체" id="dlvgbiz" />
										</dd>
									</dl>
									
									<dl>
										<dt>수량</dt>
										<div class="input-group datetime">
											<dd>
												<form:input path="assetsVO.assetQy" id="assetQy" onkeypress="return checkNumber(event);"
												class="control" title="수량" placeholder="99"/>
												<form:select path="assetsVO.qyUnit" class="control" title="수량단위">
													<form:option value="대">대</form:option>
													<form:option value="식">식</form:option>
												</form:select>
											</dd>
										</div>
									</dl>
									<dl>
										<dt>용도</dt>
										<dd>
											<form:input path="assetsVO.assetPrpos" class="control"
												title="용도" id="assetPrpos" />
										</dd>
									</dl>
									<dl>
										<dt>자산여부</dt>
										<dd>
											<form:checkbox path="assetsVO.assetYn" class="checkbox"
												label="" title="자산" id="assetYn" value="Y" />
										</dd>
										<dt>유지보수여부</dt>
										<dd>
											<form:checkbox path="assetsVO.mntnceYn" class="checkbox"
											label="" title="유지보수" id="mntnceYn" value="Y" />
										</dd>
										<dt>사용여부</dt>
										<dd>
											<form:checkbox path="assetsVO.useYn" class="checkbox"
											label="" title="사용여부" id="useYn" value="Y" />
										</dd>
									</dl>
									<dl>
										<dt>라이센스</dt>
										<dd class="d-block">
											<div class="file-box">

												<!-- The fileinput-button span is used to style the file input field as button -->
												<span class="btn btn-secondary fileinput-button"> <i class="ico-search"></i> 파일찾기 <!-- The file input field used as target for the file upload widget -->
													<input id="licenseAtchmnfl" type="file" name="multipartFile" multiple>
												</span>
											</div>
											<button type="button" class="btn btn-sm btn-primary" id="licenseAtchmnflSendBtn">파일전송</button>
											<ul id="licenseAtchmnflListDiv" class="file-list-group">
											<c:forEach var="atchmnflVO" items="${licenseAtchmnflList}">
												<li id="fileId_<c:out value="${atchmnflVO.atchmnflId}"/>_<c:out value="${atchmnflVO.atchmnflSn}"/>">
													<a href="#" class="file-delete" aria-label="삭제" onclick="fncDeleteFile('<c:out value="${atchmnflVO.atchmnflId}"/>', '<c:out value="${atchmnflVO.atchmnflSn}"/>')"><span aria-hidden="true">&times;</span></a> 
													<a href="#" onclick="fncDownloadFile('<c:out value="${atchmnflVO.atchmnflId}"/>', '<c:out value="${atchmnflVO.atchmnflSn}"/>')"><i class="fa fa-download" aria-hidden="true"></i>  <c:out value="${atchmnflVO.orginlFileNm}"/>(<c:out value="${atchmnflVO.fileSizeDisplay}"/>)</a>
												</li>
											</c:forEach>
											</ul>
										</dd>
									</dl>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>

			<div class="col-left">
				<div class="row">
					<div class="col">
						<div class="card">
							<div class="card-header">
								<h5 class="card-title">기본 정보</h5>
							</div>
							<div class="card-body">
								<div class="detailbox type1">
									<dl>
										<dt>자산관리번호</dt>
										<dd>
											<form:input path="assetsVO.assetsNo" class="control"
												title="기술지원업체" id="assetsNo" />
										</dd>
									</dl>
									<dl>
										<dt>기술지원업체</dt>
										<dd>
											<form:input path="assetsVO.tchnlgySprt" class="control"
												title="기술지원업체" id="tchnlgySprt" />
										</dd>
									</dl>
									<dl>
										<dt>담당자</dt>
										<dd>
											<form:input path="assetsVO.chargerNm" class="control"
												title="담당자" id="chargerNm" />
										</dd>
										<dt>직함</dt>
										<dd>
											<form:input path="assetsVO.chargerClsf" id="chargerClsf" class="control" title="직함"/>
										</dd>
									</dl>
									<dl>
										<dt>연락처</dt>
										<dd>
											<form:input path="assetsVO.chargerCttpc" id="chargerCttpc" class="control" title="연락처" />
										</dd>
										<dt>이메일</dt>
										<dd>
											<form:input path="assetsVO.chargerEmail" id="chargerEmail" class="control" title="이메일"/>
										</dd>
									</dl>
									<hr>
									<dl>
										<dt>설치위치</dt>
										<dd>
											<form:select id="instlLc" path="assetsVO.instlLc" class="control">
									          <form:options itemValue="cmmnCode" itemLabel="cmmnCodeNm" items="${locateList }"/>
								           </form:select>
										</dd>
										<dt></dt>
										<dd></dd>
									</dl>
									<dl>
										<dt>도입일자</dt>
										<dd>
											<form:input path="assetsVO.indcDtDisplay"
												id="indcDtDisplay" class="control" title="도입일자"
												placeholder="YYYY-MM-DD" />
										</dd>
										<dt>내용연수</dt>
										<dd>
											<form:input path="assetsVO.cnSdytrn" id="cnSdytrn" class="control" title="내용연수"/>
										</dd>
									</dl>
									<dl>
										<dt>도입단가</dt>
										<dd>
											<form:input path="assetsVO.indcUntpc" onkeypress="return checkNumber(event, this);"
												onkeyup="chkComma(this);" id="indcUntpc" class="control" title="도입단가"
												placeholder="999,999,999" />
										</dd>
										<dt>도입금액</dt>
										<dd>
											<form:input path="assetsVO.indcAmount" onkeypress="return checkNumber(event, this);"
												onkeyup="chkComma(this);" id="indcAmount" class="control" title="도입금액"
												placeholder="999,999,999" />
										</dd>
									</dl>
									<dl>
										<dt>유지보수요율</dt>
										<dd>
											<form:input path="assetsVO.mntnceTariff" onkeypress="return checkNumber(event, this);"
												id="mntnceTariff" class="control" title="유지보수요율"
												onkeyup="return percentage(event, this);" placeholder="99%" />
										</dd>
										<dt>유지보수금액</dt>
										<dd>
											<form:input path="assetsVO.mntnceAmount" onkeypress="return checkNumber(event, this);"
												onkeyup="chkComma(this);" id="mntnceAmount" class="control" title="유지보수금액"
												placeholder="999,999,999" />
										</dd>
									</dl>
									<dl>
										<dt>비고</dt>
										<dd>
											<form:textarea path="assetsVO.assetsRm" id="assetsRm" rows="5" class="control" title="비고"/>
										</dd>
									</dl>
									<dl>
										<dt>메뉴얼</dt>
										<dd class="d-block">
											<div class="file-box">
												<!-- The fileinput-button span is used to style the file input field as button -->
												<span class="btn btn-secondary fileinput-button"> <i class="ico-search"></i> 파일찾기 <!-- The file input field used as target for the file upload widget -->
													<input id="manualAtchmnfl" type="file" name="multipartFile" multiple>
												</span>
											</div>
											<button type="button" class="btn btn-sm btn-primary" id="manualAtchmnflSendBtn">파일전송</button>
											<ul id="manualAtchmnflListDiv" class="file-list-group">
											<c:forEach var="atchmnflVO" items="${manualAtchmnflList}">
												<li id="fileId_<c:out value="${atchmnflVO.atchmnflId}"/>_<c:out value="${atchmnflVO.atchmnflSn}"/>">
													<a href="#" class="file-delete" aria-label="삭제" onclick="fncDeleteFile('<c:out value="${atchmnflVO.atchmnflId}"/>', '<c:out value="${atchmnflVO.atchmnflSn}"/>')"><span aria-hidden="true">&times;</span></a> 
													<a href="#" onclick="fncDownloadFile('<c:out value="${atchmnflVO.atchmnflId}"/>', '<c:out value="${atchmnflVO.atchmnflSn}"/>')"><i class="fa fa-download" aria-hidden="true"></i>  <c:out value="${atchmnflVO.orginlFileNm}"/>(<c:out value="${atchmnflVO.fileSizeDisplay}"/>)</a>
												</li>
											</c:forEach>
											</ul>
										</dd>
									</dl>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</form:form>
	</main>
</body>
</html>
