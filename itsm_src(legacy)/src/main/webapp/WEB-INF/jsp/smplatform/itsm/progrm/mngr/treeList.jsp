<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%
  /**
  * @Class Name : /itsm/user/mngr/list.jsp
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
    <link type="text/css" rel="stylesheet" href="<c:url value='/css/metroStyle/metroStyle.css'/>"/>    
    <script src="<c:url value='/js/jquery.ztree.all.min.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
    <validator:javascript formName="progrmVO" staticJavascript="false" xhtml="true" cdata="false"/>
	<script type="text/javaScript" language="javascript" defer="defer">
        <!--
        var setting = {
        		view: {
        			selectedMulti: false,
    				dblClickExpand: dblClickExpand,
    				showIcon : true,
    				showLine : true
    			},
    			data: {
    				key: {
                		name: "progrmNm"
                		,title: "progrmNm"
                	},
	    			simpleData: {
	                    enable: true
	    				,idKey: "progrmSn"
	    				,pIdKey: "upperProgrmSn"
	    				,rootPId: "0"
	                }
    			},
    			callback: {
    				beforeClick: beforeTreeClick,
    				onClick: onTreeClick
    			}
    		};
        
        function dblClickExpand(treeId, treeNode) {
			return treeNode.level > 0;
		}
        
        function fncResetForm(){
        	$('input[name=upperProgrmNm]').val('');
			$('input[name=progrmSn]').val('-1');
			$('input[name=upperProgrmSn]').val('');
			$('input[name=progrmNm]').val('');
			$('input[name=progrmUri]').val('');
			$('input[name=sortNo]').val('');
			document.editForm.menuIndictYn[0].checked = false;
			document.editForm.menuIndictYn[1].checked = false;
        }
        
        function beforeTreeClick(treeId, treeNode, clickFlag) {
			console.log("[ "+" beforeClick ]&nbsp;&nbsp;" + treeNode.progrmNm );
			fncResetForm();
			
			return (treeNode.click != false);
		}
		function onTreeClick(event, treeId, treeNode, clickFlag) {
			console.log("treeNode.progrmSn: "+treeNode.progrmSn);
			console.log("treeNode.progrmUri: "+treeNode.progrmUri);
			console.log("[ "+" onClick ]&nbsp;&nbsp;clickFlag = " + clickFlag + " (" + (clickFlag===1 ? "single selected": (clickFlag===0 ? "<b>cancel selected</b>" : "<b>multi selected</b>")) + ")");
			
			console.log("treeNode.getParentNode(): "+treeNode.getParentNode());
			
			if(treeNode.getParentNode() != null){
				console.log("treeNode.getParentNode().progrmNm: "+treeNode.getParentNode().progrmNm);
				$('input[name=upperProgrmNm]').val(treeNode.getParentNode().progrmNm);	
			}else{
				$('input[name=upperProgrmNm]').val(treeNode.progrmNm);
			}
			
			$('input[name=progrmSn]').val(treeNode.progrmSn);
			$('input[name=upperProgrmSn]').val(treeNode.upperProgrmSn);
			$('input[name=progrmNm]').val(treeNode.progrmNm);
			$('input[name=progrmUri]').val(treeNode.progrmUri);
			$('input[name=sortNo]').val(treeNode.sortNo);
			
			
			console.log("treeNode.menuIndictYn: "+treeNode.menuIndictYn);
			if(treeNode.menuIndictYn == 'Y'){
				document.editForm.menuIndictYn[0].checked = true;
				document.editForm.menuIndictYn[1].checked = false;
			}else{
				console.log("N selected");
				document.editForm.menuIndictYn[0].checked = false;
				document.editForm.menuIndictYn[1].checked = true;

			}
		}	
        
        $(document).ready(function() {
        	fncRetrieveTreeListAjax();
        });
        function fncInitTree(zNodes){
        	$.fn.zTree.init($("#menuTree"), setting, zNodes);
        	
        	var zTree = $.fn.zTree.getZTreeObj("menuTree");
        	zTree.expandAll(true);
        }
        
        
        /* 메뉴 다시 조회 */
    	function fncRetrieveTreeListAjax(){
    		$.ajax({
    		    type:'GET',
    		    url:"<c:url value='/progrm/mngr/retrieveTreeListAjax.do'/>",
    		    dataType: "json",
    			data : { 
//     				"userId":userId
    		   			},
    			async:false,
    			success : function(request){
    				if(request.returnMessage == 'success'){
    					fncInitTree(request.resultList);
    				}else{
    					alert("등록된 메뉴가 없습니다. 관리자에게 문의하세요.");	
    				}
    			},
    			
    			error:function(r){
    				alert("정보전송에 실패하였습니다.");
    			}
    			
    		});		
    	}
        
        function fncRetrieveList(){
        	confirm('저장하지 않은 정보는 잃어버릴 수 있습니다. 계속하시겠습니까?','메뉴 관리 재조회', null, function(request){
        			console.log('request: '+request);
        			if(request){
        				fncRetrieveTreeListAjax();
        				fncResetForm();
        			}
        	});
        }
        
        function fncAdd(){
        	$('input[name=upperProgrmNm]').val($('input[name=progrmNm]').val());
			$('input[name=upperProgrmSn]').val($('input[name=progrmSn]').val());
			$('input[name=progrmSn]').val('-1');
			$('input[name=progrmNm]').val('');
			$('input[name=progrmUri]').val('');
			$('input[name=sortNo]').val('');
			$('input[name="menuIndictYn"]:radio:input[value="Y"]').attr('checked', true);
			$('input[name="menuIndictYn"]:radio:input[value="N"]').attr('checked', false);
        }
        
        // 저장, 추가, 삭제는 연휴 이후에 개발
        // 수정화면은 둥둥 떠다니게 개발
        function fncSave(){
        	var frm = document.editForm;
        	
        	if(!validateProgrmVO(frm)){
                return;
            }else{
            	if($('input[name=progrmSn]').val() == '-1'){
            		fncCreateAjax();	
            	}else{
            		fncUpdateAjax();
            	}
            }
        }
       
        function fncCreateAjax(){
    		$.ajax({
    		    type:'POST',
    		    url:"<c:url value='/progrm/mngr/createAjax.do'/>",
    		    dataType: "json",
    			data : $('#editForm').serialize(),
    			async:false,
    			success : function(request){
    				if(request.returnMessage == 'success'){
    					fncRetrieveTreeListAjax();
    					alert("성공적으로 저장 되었습니다.");
    				}else{
    					alert("저장중 문제가 발생하였습니다. 지속될 경우 관리자에게 문의하세요.");	
    				}
    			},
    			
    			error:function(r){
    				alert("정보전송에 실패하였습니다.");
    			}
    			
    		});		
    	}
        
        function fncUpdateAjax(){
    		$.ajax({
    		    type:'POST',
    		    url:"<c:url value='/progrm/mngr/updateAjax.do'/>",
    		    dataType: "json",
    			data : $('#editForm').serialize(),
    			async:false,
    			success : function(request){
    				if(request.returnMessage == 'success'){
    					fncRetrieveTreeListAjax();
    					fncResetForm();
    					alert("성공적으로 저장 되었습니다.");
    				}else{
    					alert("저장중 문제가 발생하였습니다. 지속될 경우 관리자에게 문의하세요.");	
    				}
    			},
    			
    			error:function(r){
    				alert("정보전송에 실패하였습니다.");
    			}
    			
    		});		
    	}
        
        function fncDelete(){
        	if($('input[name=progrmSn]').val() == '-1'){
        		alert('삭제대상 프로그램이 없습니다.');	
        	}else{
        		confirm('삭제하시겠습니까?','메뉴 삭제', null, function(request){
        			console.log('request: '+request);
        			if(request){
        				fncDeleteAjax();
        			}
        		});	
        	}
        	
        }
        
        function fncDeleteAjax(){
    		$.ajax({
    		    type:'POST',
    		    url:"<c:url value='/progrm/mngr/deleteAjax.do'/>",
    		    dataType: "json",
    			data : $('#editForm').serialize(),
    			async:false,
    			success : function(request){
    				if(request.returnMessage == 'success'){
    					fncRetrieveTreeListAjax();
    					fncResetForm();
    					alert("성공적으로 삭제 되었습니다.");
    				}else{
    					alert("저장중 문제가 발생하였습니다. 지속될 경우 관리자에게 문의하세요.");	
    				}
    			},
    			
    			error:function(r){
    				alert("정보전송에 실패하였습니다.");
    			}
    			
    		});	
    	}
        //-->
    </script>
</head>
	

<body>
		<main id="content" class="mini">
                <div class="page-header">
                    <h3 class="page-title"><c:out value="${LEFT_MENU_PROGRMACCESAUTHORVO.progrmNm}"/></h3>
                </div>

                <div class="row">
                    <div class="col-4">
                        <div class="card">
                            <div class="card-header">
                                <h4 class="card-title">메뉴목록</h4>
                                <button type="button" class="btn btn-primary fr"><i class="ico-search"></i>조회</button>
                            </div>
                            <div class="card-body">
                                <div class="scrollbox" style="height:550px">
                                    <ul class="ztree" id="menuTree"></ul>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-8">
                        <form name="editForm" id="editForm">
                        <div class="card">
                            <div class="card-header">
                                <h4 class="card-title">메뉴정보</h4>                                
                            </div>
                            <div class="card-body">
                                <div class="detailbox type1">
                                    <dl>
                                        <dt>상위메뉴명</dt>
                                        <dd><input type="text" name="upperProgrmNm" class="control" title="상위메뉴명"/>
								  			<input type="hidden" name="progrmSn"/>
								  			<input type="hidden" name="upperProgrmSn"/></dd>
                                    </dl>
                                    <dl>
                                        <dt>메뉴명</dt>
                                        <dd><input type="text" name="progrmNm" class="control" title="메뉴명"/> </dd>
                                    </dl>
                                    <dl>
                                        <dt>메뉴URL</dt>
                                        <dd><input type="text" name="progrmUri"  class="control" title="메뉴URL"></dd>
                                    </dl>
                                    <dl>
                                        <dt>정렬순서</dt>
                                        <dd><input type="text" name="sortNo"  class="control w100" title="정렬순서"></dd>
                                    </dl>
                                    <dl>
                                        <dt>메뉴표시여부</dt>
                                        <dd>
                                            <!-- label for에 대응하는 id 입력-->
                                            <input type="radio" name="menuIndictYn" class="radio" value="Y" id="Y"/>
                                            <label for="Y">표시</label>
                                            <input type="radio" name="menuIndictYn" class="radio" value="N" id="N"/>
                                            <label for="N">미표시</label>
                                        </dd>
                                    </dl>
                                </div>
                            </div>
                        </div>
						</form>
						
                        <div class="btnbox right">
                            <button type="button" onclick="fncDelete()" class="btn btn-secondary"><i class="ico-delete"></i>삭제</button>
                            <button type="button" onclick="fncAdd()" class="btn btn-dark"><i class="ico-add"></i>추가</button>
                            <button type="button" onclick="fncSave()" class="btn btn-primary"><i class="ico-save"></i>저장</button>
                        </div>

                    </div>
                </div>                
            </main>
            <!-- //본문영역 -->

		    <!-- 개발시 삭제-->
		    <!-- <script>
		        $(function(){
		    
		            //개발시삭제
		            var zTreeObj;
		            // zTree configuration information, refer to API documentation (setting details)
		            var setting = {};
		            // zTree data attributes, refer to the API documentation (treeNode data details)
		            var zNodes = [
		            {name:"테스트1", open:true, children:[
		                {name:"테스트1-1"}, {name:"테스트1-2"}]},
		            {name:"테스트2", open:true, children:[
		                {name:"테스트2-1"}, {name:"테스트2-2"},{name:"테스트2-3"}, {name:"테스트2-4"}]},
		            {name:"테스트3", open:true, children:[
		                {name:"테스트3-1"}, {name:"테스트3-2"},{name:"테스트3-3"}, {name:"테스트3-4"},{name:"테스트3-5"}, {name:"테스트3-6"},{name:"테스트3-7"}, {name:"테스트3-8"}]},
		            {name:"테스트4", open:true, children:[
		                {name:"테스트4-1"}, {name:"테스트4-2"},{name:"테스트4-3"}, {name:"테스트4-4"},{name:"테스트4-5"}, {name:"테스트4-6"},{name:"테스트4-7"}, {name:"테스트4-8"},{name:"테스트4-9"},{name:"테스트4-10"}]}
		                  
		            ];
		    
		            zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
		        });
		    </script> -->
</body>
</html>
