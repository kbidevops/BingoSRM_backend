<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%
  /**
  * @Class Name : /srvcrspons/site/edit.jsp
  * @Description : SR 관리 등록/수정 화면
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
  <c:set var="registerFlag" value="${empty srvcRsponsFormVO.srvcRsponsVO.srvcRsponsNo ? 'create' : 'modify'}"/>
  <title><c:out value="${LEFT_MENU_PROGRMACCESAUTHORVO.progrmNm}"/> <c:out value="${registerFlag == 'create' ? '등록' : '수정'}"/></title>
  <!--For Commons Validator Client Side-->
  <script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script>
  <validator:javascript formName="srvcRsponsFormVO" staticJavascript="false" xhtml="true" cdata="false"/>
  <script type="text/javaScript" language="javascript" defer="defer">
    var requstAtchmnflFileCnt = 0;
    
    $(document).ready(function() {
      $("#subject").on("input", function() {
        var value = $(this).val();

        if (value.length > 40) {
          $(this).val(value.substring(0, 40));
        }
      });

      $("#subject").on("change", function() {
        var value = $(this).val();

        if (value.length > 40) {
          $(this).val(value.substring(0, 40));
        }
      });
      // dotdot()
      
      $("#requstDtDateDisplay").mask("9999-99-99");
      $("#requstDtTimeDisplay").mask("99:99");
      $('.dropdown-toggle').dropdown();
      $('#requstAtchmnflSendBtn').hide();
      
      var url = '<c:url value='/atchmnfl/site/create.do'/>';
      
      $('#requstAtchmnfl').fileupload({
        url: url,
        dataType: "json",
        formData: {
          atchmnflId: $("#requstAtchmnflId").val(),
        },
        sequentialUploads: true,
        add: function (e, data) {
          var valid = true;
          var re = /^.+\.((doc)|(txt)|(xls)|(xlsx)|(docx)|(hwpx)|(hwp)|(pdf)|(pts)|(zip)|(jpg)|(gif)|(png)|(bmp))$/i;
          
          $.each(data.files, function (index, file) {
            console.log('Added file: ' + file.name);
            console.log('Added file: ' + file.type);
            console.log('Added file: ' + file.size);
            console.log('Added file: ' + filesize(file.size));
            // console.log('file size: ' + data.getNumberOfFiles());
            
            requstAtchmnflFileCnt++;
            
            if (!re.test(file.name)) {
              valid = false;
              alert(file.name + ' <br/>허용되지 않은 첨부파일은 제외됩니다.');
              requstAtchmnflFileCnt--;
            }
            // 5M
            else if (file.size > 5000000) {
              console.log('5M이하의 파일만 허용 합니다.');
              alert(file.name + ' <br/>5M이상의 첨부파일은 제외됩니다.');
              requstAtchmnflFileCnt--;
              valid = false;
            }
            // 동시 업로드 5개 제한
            else if (requstAtchmnflFileCnt > 5) {
              console.log('5개의 파일만 허용 합니다.' + requstAtchmnflFileCnt);
              alert(file.name+' <br/>동시에 5개의 파일만 첨부 허용 합니다.');
              requstAtchmnflFileCnt--;
              valid = false;
            }
            else {
              console.log('정상등록 ' + requstAtchmnflFileCnt);
              
              var abortBtn = $('<a/>')
                .attr('href', 'javascript:void(0)')
                .attr('class', 'close')
                .attr('aria-label', 'Close')
                .append('<span aria-hidden="true">&times;</span>')
                .click(function() {
                  data.abort();
                  data.context.remove();
                  data = null;
                  if (requstAtchmnflFileCnt > 0) {
                    requstAtchmnflFileCnt--;
                    console.log('requstAtchmnflFileCnt: ' + requstAtchmnflFileCnt);
                  }
                });

              data.context = $('<div/>').appendTo($('#requstAtchmnflFileListDiv'));
              data.context.attr('class', 'form-row');
              data.context.append(abortBtn).append(file.name + '(' + filesize(file.size) + ')');
            }
          });

          $("#requstAtchmnflSendBtn").click(function () {
            // validate file...
            if (valid && data !== null) {
              data.submit();
              // 해당 폼 전송 추가 하면 저장 버튼 완료
            }
          });
        },
        done: function (e, data) {
          console.log('done: ' + data.errorThrown);
          console.log('done: ' + data.textStatus);
          console.log('done: ' + data.jqXHR);
        },
        progressall: function (e, data) {
          // var progress = parseInt(data.percent * 100, 10);
        	var progress = parseInt(data.loaded / data.total * 100, 10);

          console.log('data.bitrate:' + data.bitrate);
          console.log('data.bitrate:' + progress);
          $('#requstAtchmnfl-progress .progress-bar').css('width', progress + '%');
          $('#requstAtchmnfl-progress .progress-bar').html(progress + '%');

          if (progress === 100) {
            frm.action = "<c:url value="${registerFlag == 'create' ? '/srvcrspons/site/create.do' : '/srvcrspons/site/update.do'}"/>";
            frm.submit();
          }
        },
        progressServerRate: 0.3,
      });

      // $("#rqesterCttpc").mask("999-9999-9999"); //전화, 휴대폰, xxxx-xxxx등과 같은 전화번호가 다양해 적용 불가
      $('#srvcRsponsNo').tooltip('show');
    });

    function fncSave() {
      frm = document.listForm;

      if (document.listForm.elements['srvcRsponsVO.trgetSrvcCode'].value === "") {
        alert('대상서비스를 입력하세요.');
        return;
      }

      if (!validateSrvcRsponsFormVO(frm)) {
        return;
      }

      // 첨부파일 존재 유무 확인
      if (requstAtchmnflFileCnt > 0) {
        $("#requstAtchmnflSendBtn").trigger("click");
      }
      else {
        frm.action = "<c:url value="${registerFlag == 'create' ? '/srvcrspons/site/create.do' : '/srvcrspons/site/update.do'}"/>";
        frm.submit();
      }
    }

    function fncDelete() {
      confirm('삭제하시겠습니까?', 'SR요청 삭제', null, function(request) {
        console.log('request: ' + request);
        if (request) {
          document.listForm.action = "<c:url value='/srvcrspons/site/delete.do'/>";
          document.listForm.submit();
        }
      });
    }

    <%-- 요청자동일 복사 --%>
    function fncChkCopyRqester(obj) {
      if (obj.checked) {
        document.listForm.elements['srvcRsponsVO.rqester1stNm'].value = document.listForm.elements['srvcRsponsVO.rqesterNm'].value;
        document.listForm.elements['srvcRsponsVO.rqester1stPsitn'].value = document.listForm.elements['srvcRsponsVO.rqesterPsitn'].value;
        document.listForm.elements['srvcRsponsVO.rqester1stCttpc'].value = document.listForm.elements['srvcRsponsVO.rqesterCttpc'].value;
        document.listForm.elements['srvcRsponsVO.rqester1stEmail'].value = document.listForm.elements['srvcRsponsVO.rqesterEmail'].value;
      }
      else {
        document.listForm.elements['srvcRsponsVO.rqester1stNm'].value = "";
        document.listForm.elements['srvcRsponsVO.rqester1stPsitn'].value = "";
        document.listForm.elements['srvcRsponsVO.rqester1stCttpc'].value = "";
        document.listForm.elements['srvcRsponsVO.rqester1stCttpc'].value = "";
      }
    }

    <%-- 요청자 변경시 처리 --%>
    function fncChangeRqesterNm(value) {
      console.log('요청자 value: ' + $.trim(value));
      $('#rqesterNmDropdown').dropdown('update');

      if ($.trim(value) !== "") {
        console.log('빈값일때');

        $.ajax({
          type: "POST",
          url: "<c:url value='/srvcrspons/site/retrieveRqesterNmListAjax.do'/>",
          dataType: "json",
          data: {
            rqesterNm: value,
          },
          async: false,
          success: function(request) {
            fncInitRqesterNmDropdown(request);
          },
          error: function(r) {
            alert("정보전송에 실패하였습니다.");
          }
        });
      }
    }

    function fncInitRqesterNmDropdown(request) {
      var rqesterNmDropdownHTML = "";
      console.log('request.returnMessage: '+request.returnMessage);

      if (request.returnMessage === "success") {
        resultList = request.resultList;
        for (var i = 0; i < resultList.length; i++) {
          rqesterNmDropdownHTML += "<a class=\"dropdown-item\" href=\"#\" id=\"rqesterNm" + (i + 1);
          rqesterNmDropdownHTML += "\" onclick=\"fncSelectRqesterNm('rqesterNm" + (i + 1) + "')\" data-name=\"";
          rqesterNmDropdownHTML += resultList[i].rqesterNm + "\" data-psitn=\"" + resultList[i].rqesterPsitn;
          rqesterNmDropdownHTML += "\" data-cttpc=\"" + resultList[i].rqesterCttpc + "\" data-email=\"" + resultList[i].rqesterEmail + "\">";
          rqesterNmDropdownHTML += resultList[i].rqesterNm + "("+resultList[i].rqesterPsitn+")</a>";
        }
      }
      else {
				rqesterNmDropdownHTML = '<a class="dropdown-item" href="#">일치하는데이터없음</a>';
			}

      console.log('rqesterNmDropdownHTML: ' + rqesterNmDropdownHTML);
      $('#rqesterNmDropdown').html(rqesterNmDropdownHTML);
    }

    function fncSelectRqesterNm(objId) {
      document.listForm.elements['srvcRsponsVO.rqesterNm'].value = $("#" + objId).data("name");
      document.listForm.elements['srvcRsponsVO.rqesterPsitn'].value = $("#" + objId).data("psitn");
      document.listForm.elements['srvcRsponsVO.rqesterCttpc'].value = $("#" + objId).data("cttpc");
      document.listForm.elements['srvcRsponsVO.rqesterEmail'].value = $("#" + objId).data("email");
    }

    <%-- 1차요청자 변경시 처리 --%>
    function fncChangeRqester1stNm(value) {
      console.log('1차 요청자 value: ' + $.trim(value));
      $('#rqester1stNmDropdown').dropdown('update');

      if ($.trim(value) !== "") {
        console.log('빈값일때');

        $.ajax({
          type: "POST",
          url: "<c:url value='/srvcrspons/site/retrieveRqester1stNmListAjax.do'/>",
          dataType: "json",
          data: {
            rqester1stNm: value,
          },
          async: false,
          success: function(request) {
            fncInitRqester1stNmDropdown(request);
          },
          error: function(r) {
            alert("정보전송에 실패하였습니다.");
          }
        });
      }
    }

    function fncInitRqester1stNmDropdown(request) {
      var rqester1stNmDropdownHTML = "";
      console.log('request.returnMessage: ' + request.returnMessage);

      if (request.returnMessage === "success") {
        resultList = request.resultList;
        for (var i = 0; i < resultList.length; i++) {
          rqester1stNmDropdownHTML += "<a class=\"dropdown-item\" href=\"#\" id=\"rqesterNm" + (i + 1);
          rqester1stNmDropdownHTML += "\" onclick=\"fncSelectRqester1stNm('rqesterNm" + (i + 1) + "')\" data-name=\"";
          rqester1stNmDropdownHTML += resultList[i].rqester1stNm + "\" data-psitn=\"" + resultList[i].rqester1stPsitn;
          rqester1stNmDropdownHTML += "\" data-cttpc=\"" + resultList[i].rqester1stCttpc + "\" data-email=\"" + resultList[i].rqester1stEmail + "\">";
          rqester1stNmDropdownHTML += resultList[i].rqester1stNm + "(" + resultList[i].rqester1stPsitn + ")</a>";
        }
      }
      else {
				rqester1stNmDropdownHTML = '<a class="dropdown-item" href="#">일치하는데이터없음</a>';
			}

      console.log('rqester1stNmDropdownHTML: ' + rqester1stNmDropdownHTML);
      $('#rqester1stNmDropdown').html(rqester1stNmDropdownHTML);
    }

    function fncSelectRqester1stNm(objId) {
      document.listForm.elements['srvcRsponsVO.rqester1stNm'].value = $("#" + objId).data("name");
      document.listForm.elements['srvcRsponsVO.rqester1stPsitn'].value = $("#" + objId).data("psitn");
      document.listForm.elements['srvcRsponsVO.rqester1stCttpc'].value = $("#" + objId).data("cttpc");
      document.listForm.elements['srvcRsponsVO.rqester1stEmail'].value = $("#" + objId).data("email");
    }

    /* 글 목록 화면 function */
    function fncRetrieveList() {
      <c:if test="${empty srvcRsponsFormVO.returnListMode}">
      document.listForm.action = "<c:url value='/srvcrspons/site/retrievePagingList.do'/>";
      </c:if>
      <c:if test="${!empty srvcRsponsFormVO.returnListMode}">
      document.listForm.action = "<c:url value='/srvcrspons/site/retrieveList.do'/>";
      </c:if>
      document.listForm.submit();
    }

    function fncDownloadFile(atchmnflId, atchmnflSn) {
      document.downloadForm.action = "<c:url value='/atchmnfl/site/retrieve.do'/>";
      document.downloadForm.atchmnflId.value = atchmnflId;
      document.downloadForm.atchmnflSn.value = atchmnflSn;
      document.downloadForm.submit();
    }

    function fncDeleteFile(atchmnflId, atchmnflSn) {
      console.log('atchmnflId: ' + atchmnflId);
      console.log('atchmnflSn: ' + atchmnflSn);
      $('#atchmnflId').val(atchmnflId);
      $('#atchmnflSn').val(atchmnflSn);
      confirm('해당 파일이 즉시 삭제됩니다. 계속 진행하시겠습니까?','파일 삭제', null, function(request) {
        console.log('request: ' + request);

        if (request) {
          $.ajax({
            type: "POST",
            url: "<c:url value='/atchmnfl/site/delete.do'/>",
            dataType: "json",
            data: {
              atchmnflId: $('#atchmnflId').val(),
              atchmnflSn: $('#atchmnflSn').val(),
            },
            async: false,
            success: function(request) {
              console.log('request.returnMessage: ' + request.returnMessage);

              if (request.returnMessage === "success") {
                console.log('delete: ' + $('#fileId_' + $('#atchmnflId').val() + '_' + $('#atchmnflSn').val()));
                console.log('delete: ' + '#fileId_' + $('#atchmnflId').val() + '_' + $('#atchmnflSn').val());
                $('#fileId_' + $('#atchmnflId').val() + '_' + $('#atchmnflSn').val()).remove();
              }
            },
            error: function(r) {
              alert("정보전송에 실패하였습니다.");
            }
          });
        }
      });
    }

    function fncChangeTrgetSrvcCode(trgetSrvcCode) {

    }
  </script>
</head>

<body>

<main id="content">
  <form name="downloadForm" action="<c:url value='/atchmnfl/site/retrieve.do'/>" target="">
    <input type="hidden" name="atchmnflId" id="atchmnflId"/>
		<input type="hidden" name="atchmnflSn" id="atchmnflSn"/>
  </form>

  <div class="page-header">
    <h3 class="page-title"><c:out value="${LEFT_MENU_PROGRMACCESAUTHORVO.progrmNm}"/> <c:out value="${registerFlag == 'create' ? '등록' : '수정'}"/></h3>
    <div class="btnbox fr">
      <button type="button" onclick="fncRetrieveList()" class="btn btn-secondary">
        <i class="ico-list"></i>목록
      </button>
      <button type="button" onclick="fncDelete()" class="btn btn-dark">
        <i class="ico-delete"></i>삭제
      </button>
      <button type="button" onclick="fncSave()" class="btn btn-primary">
        <i class="ico-save"></i>저장
      </button>
    </div>
  </div>

  <form:form commandName="srvcRsponsFormVO" id="listForm" name="listForm" method="post" enctype="multipart/form-data">
    <form:hidden path="searchSrvcRsponsVO.pageIndex" />
    <form:hidden path="searchSrvcRsponsVO.srvcRsponsNo" />
    <form:hidden path="searchSrvcRsponsVO.srvcRsponsSj" />
    <form:hidden path="searchSrvcRsponsVO.trgetSrvcCode" />
    <form:hidden path="searchSrvcRsponsVO.processMt" />
    <form:hidden path="searchSrvcRsponsVO.srvcRsponsCn" />
    <form:hidden path="searchSrvcRsponsVO.srvcRsponsClCode" />
    <form:hidden path="searchSrvcRsponsVO.excludeprocessYn" />
    <form:hidden path="srvcRsponsVO.requstAtchmnflId" id="requstAtchmnflId"/>
    <form:hidden path="srvcRsponsVO.rsponsAtchmnflId" id="rsponsAtchmnflId"/>
    <form:hidden path="srvcRsponsVO.rspons1stDtDateDisplay" />
    <form:hidden path="srvcRsponsVO.rspons1stDtTimeDisplay" />
    <form:hidden path="srvcRsponsVO.processDtDateDisplay" />
    <form:hidden path="srvcRsponsVO.processDtTimeDisplay" />
    <form:hidden path="srvcRsponsVO.srvcProcessDtls" />
    <form:hidden path="srvcRsponsVO.srvcRsponsBasisCode" />
    <form:hidden path="srvcRsponsVO.rqesterId" />
    <form:hidden path="srvcRsponsVO.srvcRsponsNo" />
    <form:hidden path="srvcRsponsVO.saveToken" />
    <form:hidden path="returnListMode" />

    <div class="row srarea equalheight">
      <div class="col-left">
        <div class="card">
          <div class="card-header">
            <h4 class="card-title">SR 정보</h4>
            <dl class="sr-info">
              <dt>SR번호</dt>
              <dd>
                <c:out value="${srvcRsponsFormVO.srvcRsponsVO.srvcRsponsNo }"/>
                <c:if test="${registerFlag == 'create' }">
                  자동생성
                </c:if>
              </dd>
              <dt>요청일시</dt>
              <dd>
                <div class="input-group datetime">
                  <form:input path="srvcRsponsVO.requstDtDateDisplay" id="requstDtDateDisplay" placeholder="yyyy-MM-dd" class="control inpdate" cssStyle="ime-mode:disabled;" readonly="true"/>
                  <form:input path="srvcRsponsVO.requstDtTimeDisplay" id="requstDtTimeDisplay" placeholder="HH:mm" class="control inptime" cssStyle="ime-mode:disabled;" readonly="true"/>
                </div>
              </dd>
            </dl>
            <dl class="sr-info">
              <dt>대상서비스</dt>
              <dd>
                <form:select path="srvcRsponsVO.trgetSrvcCode" class="control w-auto" onclick="fncChangeTrgetSrvcCode(this.value)">
                  <form:option value="">선택하세요</form:option>
                  <form:options items="${trgetSrvcCodeVOList}" itemLabel="cmmnCodeNm" itemValue="cmmnCode"/>
                </form:select>
              </dd>
              <form:errors path="srvcRsponsVO.trgetSrvcCode" />
              <c:if test="${!empty srvcRsponsFormVO.srvcRsponsVO.srvcRsponsBasisCodeNm}">
                <dt>SR근거</dt>
                <dd>
                  <c:out value="${srvcRsponsFormVO.srvcRsponsVO.srvcRsponsBasisCodeNm}"/>
                </dd>
              </c:if>
            </dl>
          </div>
        </div>

        <div class="row">
          <div class="col">
            <!-- 요청자정보 -->
            <div class="card">
              <div class="card-header">
                <h5 class="card-title">요청자 정보</h5>
              </div>

              <div class="card-body">
                <div class="detailbox type1">
                  <dl>
                    <dt>성명</dt>
                    <dd>
                      <form:input path="srvcRsponsVO.rqesterNm" id="rqesterNm" data-toggle="dropdown" onkeyup="fncChangeRqesterNm(this.value)" cssClass="control" tabindex="1" cssStyle="ime-mode:active;"/>
                      <div class="dropdown-menu" id="rqesterNmDropdown">
                        <a class="dropdown-item" href="#">일치하는데이터없음</a>
                      </div>
                    </dd>
                  </dl>
                  <dl>
                    <dt>소속</dt>
                    <dd><form:input path="srvcRsponsVO.rqesterPsitn" cssClass="control" tabindex="2" cssStyle="ime-mode:active;" title="소속"/></dd>
                  </dl>
                  <dl>
                    <dt>연락처</dt>
                    <dd><form:input path="srvcRsponsVO.rqesterCttpc" id="rqesterCttpc" cssClass="control" tabindex="3" cssStyle="ime-mode:inactive;" title="연락처"/></dd>
                  </dl>
                  <dl>
                    <dt>이메일</dt>
                    <dd><form:input path="srvcRsponsVO.rqesterEmail" cssClass="control" tabindex="4" cssStyle="ime-mode:inactive;" title="이메일"/></dd>
                  </dl>
                </div>
              </div>
            </div>
            <!-- //요청자정보 -->
          </div>

          <!-- 1차 요청자 -->
          <!-- 
          <div class="col">
            <div class="card">
              <div class="card-header">
                <h5 class="card-title">1차 요청자(외부고객)</h5>
                <div class="control-group mt5">
                  <input type="checkbox" name="chkCopyRqester" onclick="fncChkCopyRqester(this)" id="chk1" class="checkbox"/>
                  <label for="chk1">요청자와 동일</label>
                </div>
              </div>

              <div class="card-body">
                <div class="detailbox type1">
                  <dl>
                    <dt>성명</dt>
                    <dd><form:input path="srvcRsponsVO.rqester1stNm" id="rqester1stNm" data-toggle="dropdown" onkeyup="fncChangeRqester1stNm(this.value)" cssClass="control" tabindex="11" cssStyle="ime-mode:active;" title="성명"/>
                      <div class="dropdown-menu" id="rqester1stNmDropdown">
                        <a class="dropdown-item" href="#">일치하는데이터없음</a>
                      </div>
                    </dd>
                  </dl>
                  <dl>
                    <dt>소속</dt>
                    <dd><form:input path="srvcRsponsVO.rqester1stPsitn" cssClass="control" tabindex="12" cssStyle="ime-mode:active;" title="소속"/></dd>
                  </dl>
                  <dl>
                    <dt>연락처</dt>
                    <dd>
                      <form:input path="srvcRsponsVO.rqester1stCttpc" cssClass="control" tabindex="13" cssStyle="ime-mode:inactive;" title="휴대폰 번호를 입력하시면 처리가 완료된 후에 문자로 안내 받으실 수 있습니다."/>
                    </dd>
                  </dl>
                  <dl>
                    <dt>이메일</dt>
                    <dd><form:input path="srvcRsponsVO.rqester1stEmail" cssClass="control" tabindex="14" cssStyle="ime-mode:inactive;" title="이메일"/></dd>
                  </dl>
                </div>
              </div>
            </div>
          </div>
          -->
          <!-- //1차 요청자 -->

          <div class="col-12">
            <!-- 요청사항 -->
            <div class="card">
              <div class="card-header">
                <h5 class="card-title">요청사항(상세기술)</h5>
              </div>

              <div class="card-body">
                <div class="detailbox type1">
                  <dl>
                    <dt>요청제목</dt>
                    <dd><form:input id="subject" path="srvcRsponsVO.srvcRsponsSj" tabindex="21" cssStyle="ime-mode:active;" class="control" title="요청제목"/></dd>
                  </dl>
                  <dl>
                    <dt>요청상세내용</dt>
                    <dd>
                      <form:textarea path="srvcRsponsVO.srvcRsponsCn" tabindex="22" cssStyle="ime-mode:active;" class="control" title="요청상세내용" rows="15"/>
                    </dd>
                  </dl>
                  <dl>
                    <dt>요청첨부파일</dt>
                    <dd class="d-block">
                      <div class="file-box">
                        <!-- The fileinput-button span is used to style the file input field as button -->
                        <span class="btn btn-secondary fileinput-button">
                          <i class="ico-search"></i>파일찾기
                          <!-- The file input field used as target for the file upload widget -->
                          <input id="requstAtchmnfl" type="file" name="multipartFile" multiple>
                        </span>
                      </div>
                      <button type="button" class="btn btn-sm btn-primary" id="requstAtchmnflSendBtn">파일전송</button>
                      <ul id="requstAtchmnflFileListDiv" class="file-list-group">
                        <c:forEach var="atchmnflVO" items="${requstAtchmnflList}">
                          <li id="fileId_<c:out value="${atchmnflVO.atchmnflId}"/>_<c:out value="${atchmnflVO.atchmnflSn}"/>">
                            <a href="#" class="file-delete" aria-label="삭제" onclick="fncDeleteFile('<c:out value="${atchmnflVO.atchmnflId}"/>', '<c:out value="${atchmnflVO.atchmnflSn}"/>')">
                              <span aria-hidden="true">&times;</span>
                            </a>
                            <a href="#" onclick="fncDownloadFile('<c:out value="${atchmnflVO.atchmnflId}"/>', '<c:out value="${atchmnflVO.atchmnflSn}"/>')">
                              <i class="fa fa-download" aria-hidden="true"></i>  <c:out value="${atchmnflVO.orginlFileNm}"/>(<c:out value="${atchmnflVO.fileSizeDisplay}"/>)
                            </a>
                          </li>
                        </c:forEach>
                      </ul>
                    </dd>
                  </dl>
                </div>
              </div>
            </div>
            <!-- //요청사항 -->
          </div>
        </div>
      </div>
    </div>
  </form:form>

  <div class="btnbox right mb30">
    <button type="button" onclick="fncRetrieveList()" class="btn btn-secondary">
      <i class="ico-list"></i>목록
    </button>
    <button type="button" onclick="fncDelete()" class="btn btn-dark">
      <i class="ico-delete"></i>삭제
    </button>
    <button type="button" onclick="fncSave()" class="btn btn-primary">
      <i class="ico-save"></i>저장
    </button>
  </div>
</main>
<!-- //본문영역 -->

<script>
  $(".inpdate").mask("9999-99-99");
  $(".inptime").mask("99:99")
</script>

</body>
</html>