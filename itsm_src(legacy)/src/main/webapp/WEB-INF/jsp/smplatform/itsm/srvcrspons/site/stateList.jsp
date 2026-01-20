<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%
  /**
  * @Class Name : /user/mngr/list.jsp
  * @Description : 사용자 관리 목록 화면
  * @Modification Information
  *
  *   수정일         수정자                   수정내용
  *  -------    --------    ---------------------------
  *  2017.09.13 임영수               최초 생성
  *
  * author 지휘곰 개발팀
  * since 2017.09.13
  *
  * Copyright (C) 2017 by COMMANDER BEAR  All right reserved.
  */
%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <title><c:out value="${LEFT_MENU_PROGRMACCESAUTHORVO.progrmNm}"/></title>
	<script type="text/javaScript" language="javascript" defer="defer">
        <!--
        var processTimerId = 0;
        $.ajaxSetup({
        	cache: false
        });
        $(document).ready(function() {        	
        	fncRetrieveListAjax();
        });
        /* 글 수정 화면 function */
        function fncUpdateView(srvcRsponsNo) {
        	clearTimeout(processTimerId);
        	document.listForm.elements['srvcRsponsVO.srvcRsponsNo'].value = srvcRsponsNo;
           	document.listForm.action = "<c:url value='/srvcrspons/site/updateView.do'/>";
           	document.listForm.submit();
        }
        /* 글 등록 화면 function */
        function fncCreateView() {
        	clearTimeout(processTimerId);
           	document.listForm.action = "<c:url value='/srvcrspons/site/createView.do'/>";
           	document.listForm.submit();
        }
        /* 글 목록 화면 function */
        function fncRetrieveList() {
        	clearTimeout(processTimerId);
        	document.listForm.action = "<c:url value='/srvcrspons/site/retrieveList.do'/>";
           	document.listForm.submit();
        }
        
        function fncRetrieveListAjax(){
        	
    		if(document.listForm.elements['searchSrvcRsponsVO.excludeprocessYn'].checked){
    			$('#jobList').load('<c:url value='/srvcrspons/site/retrieveListDiv.do'/>?excludeprocessYn=Y',function(){
    				dotdot();
    			});	
    			
    		}else{
    			$('#jobList').load('<c:url value='/srvcrspons/site/retrieveListDiv.do'/>',function(){
    				dotdot();
    			});
    		}
    		clearTimeout(processTimerId);
    		processTimerId = setTimeout("fncRetrieveListAjax()",1*30*1000);
    		console.log('processTimerId: '+processTimerId);
    		
    		function dotdot(){
    	           $(".dotdotSj").each(function(){
    	                var length = 30;
    	                $(this).each(function(){
    		                if($(this).text().length >= length ){
    		                    $(this).text($(this).text().substr(0,length) + "...")
    		                }
    	           		})
    	      		})
    	      		
    	      		$(".dotdotCn").each(function(){
    	                 var length = 60;
    	                 $(this).each(function(){
    	 	                if($(this).text().length >= length ){
    	 	                    $(this).text($(this).text().substr(0,length) + "...")
    	 	                }
    	            		})
    	       		})
    	    }
    		
    	}
        //-->
    </script>
</head>
	

<body>
	<main id="content">
        <div class="page-header">
            <h3 class="page-title"><c:out value="${LEFT_MENU_PROGRMACCESAUTHORVO.progrmNm}"/></h3>
        </div>
               
		<form:form commandName="srvcRsponsFormVO" id="listForm" name="listForm" method="post">
			<input type="hidden" name="srvcRsponsVO.srvcRsponsNo" />
			<input type="hidden" name="returnListMode" value="stateList"/>
	        <div class="card">
	            <div class="card-header">
	                <div class="fl">
	                    <button type="button" onclick="fncCreateView()" class="btn btn-dark"><i class="ico-write"></i>등록</button>
	                </div>
	                <div class="fr"> 
	                    <input type="checkbox" class="checkbox" name="searchSrvcRsponsVO.excludeprocessYn" value="Y" id="chk1"/>
	                    <label for="chk1">완료 제외 </label>
	                    <button type="button" class="btn btn-primary" onclick="fncRetrieveList()"><i class="ico-search"></i>재검색</button>
	                </div>
	            </div>
	        </div>
	
	        <ul class="cardlist type1" id="jobList">
	        </ul> 
        </form:form>
	</main>
</body>
</html>
