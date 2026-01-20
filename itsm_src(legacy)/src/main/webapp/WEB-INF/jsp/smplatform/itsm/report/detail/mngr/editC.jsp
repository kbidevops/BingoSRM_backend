<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <title>${programName}</title>
  <script>
    var params = (function() {
      var queryList = location.search.replace(/^\?/, "").split("&");
      var params = {};

      for (var i = 0; i < queryList.length; i++) {
        var queries = queryList[i].split("=");

        params[queries[0]] = queries[1];
      }

      return params;
    })();

    function manageAttachmentIsVisible(list) {
      if (!list) {
        return true;
      }

      for (var i = 0; i < list.length; i++) {
        var item = list[i];

        if (/^B1.0$/.test(item.assignCode)) {
          return true;
        }
      }

      $("#attachments").remove();
      return false;
    }

    var repTyCode = params.repTyCode;

    function fncRetrieveList() {
      location.href = "${pageContext.request.contextPath}/itsm/rep/detail/mngr/retrievePagingList.do?repTyCode=" + (params.repTyCode || repTyCode);
    }

    var headFuncs = {
      B001: function(value) {
        var now = value ? new Date(value) : new Date();

        return now.getFullYear() + "-" + ("0" + (now.getMonth() + 1)).slice(-2) + "-" + ("0" + now.getDate()).slice(-2);
      },
      B002: function(value) {
        var now = value ? new Date(value) : new Date();
        var init = new Date(now.getFullYear(), 0, 1);

        return Math.ceil((now - init + init.getDay()) / 1000 / 60 / 60 / 24 / 7) + "주차";
      },
      B003: function(value) {
        return (value ? new Date(value) : new Date()).getMonth() + 1 + "월";
      }
    };

    function setTitle(repTyCode, head) {
      if (head) {
        $("#programNameHead").text(head);
      }
      else {
        var today = headFuncs[repTyCode]();

        $("#programNameHead").text(today);
      }

      var name = ({
        B001: "일일업무보고작성",
        B002: "주간업무보고작성",
        B003: "월간업무보고작성",
      })[repTyCode];

      $("#programNameBody").text(name);
    }

    function isNewer() {
      return !params.repSn;
    }

    function fetchAssignList(repTyCode) {
      return $.ajax({
        url: "${pageContext.request.contextPath}/itsm/rep/detail2/mngr/getAssignListAjax.do",
        method: "GET",
        data: {
          repTyCode: repTyCode,
        },
      });
    }

    function fetchDetail(repSn, userId) {
      return $.ajax({
        url: "${pageContext.request.contextPath}/itsm/rep/detail2/mngr/getDetailsAjax.do",
        method: "GET",
        data: {
          repSn: repSn,
          userId: userId,
        },
      });
    }

    function fncRetrieveRep() {
      var reportDt = $("#reportDt").val();
      if (!reportDt) {
        var now = new Date();
        reportDt = now.getFullYear() + "-" + ("0" + (now.getMonth() + 1)).slice(-2) + "-" + ("0" + now.getDate()).slice(-2);
      }

      $.ajax({
        url: "${pageContext.request.contextPath}/itsm/rep/detail2/mngr/getLastReportInfoAjax.do",
        method: "GET",
        data: {
          reportDt: reportDt,
          repTyCode: repTyCode,
        },
      }).done(function(response) {
        setDetail(response, true);
      });
    }

    function fncSave() {
      var reportDt = $("#reportDt").val();
      if (!reportDt) {
        var now = new Date();
        reportDt = now.getFullYear() + "-" + ("0" + (now.getMonth() + 1)).slice(-2) + "-" + ("0" + now.getDate()).slice(-2);
      }

      $.ajax({
        url: "${pageContext.request.contextPath}/itsm/rep/detail2/mngr/getStatusAjax.do",
        data: {
          reportDt: reportDt,
          repTyCode: repTyCode,
          repSn: params.repSn,
        },
        method: "GET",
      }).done(function(response) {
        if (response.confirmed) {
          return alert("같은 날짜에 확정된 보고서가 있습니다. \n관리자에게 문의해주세요.");
        }

        if (response.duplicated) {
          return confirm("중복된 보고서가 있습니다. 덮어 씌우시겠습니까?", "덮어 씌우시겠습니까?", null, function(request) {
            if (request) {
              save();
            }
            else {
              return false;
            }
          });
        }

        confirm("작성하시겠습니까?", "", null, function(request) {
          if (request) {
            save();
          }
          else {
            return false;
          }
        });
      })
    }

    function fncDeleteFile() {
      confirm("파일은 즉시 삭제됩니다. 삭제 하시겠습니까?", "", null, function(request) {
        if (request) {
          $.ajax({
            url: "${pageContext.request.contextPath}/itsm/rep/detail2/mngr/deleteFileAjax.do",
            method: "POST",
            data: {
              repSn: params.repSn,
              userId: params.userId,
            },
          }).done(function(response) {
            if (response.message) {
              alert(response.message);
            }
            else if (response.result) {
              alert("삭제했습니다", undefined, function() {
                location.reload();
              });
            }
            else {
              alert("삭제에 실패했습니다", undefined, function() {
                location.reload();
              });
            }
          });
        }
        else {
          return false;
        }
      });
    }

    function save() {
      var formData = new FormData(document.forms.data);

      if (params.repSn) {
        formData.append("repSn", params.repSn);
      }

      if (params.repTyCode) {
        formData.append("type", params.repTyCode);

        var reportDt = $("#reportDt").val();

        if (reportDt) {
          formData.append("date", reportDt);
        }
        else {
          var now = new Date();
          formData.append("date", now.getFullYear() + "-" + ("0" + (now.getMonth() + 1)).slice(-2) + "-" + ("0" + now.getDate()).slice(-2));
        }
      }

      $.ajax({
        //url: "${paegContext.request.contextPath}/itsm/rep/detail2/mngr/createAjax.do",
        url: "/itsm/itsm/rep/detail2/mngr/createAjax.do",
        method: "POST",
        enctype: "multipart/form-data",
        data: formData,
        processData: false,
        contentType: false,
        cache: false,
      }).done(function(response) {
        if (response.message) {
          alert(response.message);
        }
        else if (response.result) {
          alert("저장했습니다", undefined, function() {
            fncRetrieveList();
          });
        }
        else {
          alert("저장에 실패했습니다", undefined, function() {
            location.reload();
          });
        }
      });
    }

    function fncDelete() {
      $.ajax({
        url: "${pageContext.request.contextPath}/itsm/rep/detail2/mngr/deleteAjax.do",
        method: "POST",
        data: {
          repSn: params.repSn,
        },
      }).done(function(response) {
        if (response.message) {
          alert(response.message);
        }
        else if (response.result) {
          alert("삭제했습니다", undefined, function() {
            fncRetrieveList();
          });
        }
        else {
          alert("삭제에 실패했습니다", undefined, function() {
            location.reload();
          });
        }
      });
    }

    // --------------------------

    // 상세 내용 세팅
    function setDetails(assignList, details) {
      $("#details tr[id!=attitudes]").remove();

      if (!details) {
        details = {};
      }

      $(assignList).each(function(i, e) {
        var $el = $("\
                <tr>\
                  <th scope=\"row\">" + e.assignName + "</th>\
                  <input type=\"hidden\" name=\"descriptions[" + i + "].code\" value=\"" + e.assignCode + "\">\
                  <td>\
                    <div class=\"preview\">\
                      <textarea rows=\"15\" cols=\"50\" name=\"descriptions[" + i + "].currentDescription\"></textarea>\
                    </div>\
                  </td>\
                  <td>\
                    <div class=\"preview\">\
                      <textarea rows=\"15\" cols=\"50\" name=\"descriptions[" + i + "].nextDescription\"></textarea>\
                    </div>\
                  </td>\
                </tr>");

        var detail = details[e.assignCode] || {};

        $el.find("[name='descriptions[" + i + "].currentDescription']").val(detail.execDesc);
        $el.find("[name='descriptions[" + i + "].nextDescription']").val(detail.planDesc);
        $el.appendTo("#details");
      });
    }

    // 근태 세팅
    function setAttitudes(currentList, nextList) {
      var $current = $("<td>");

      $(currentList).each(function(i, e) {
        var checked = e.attitudePick ? " checked" : "";

        $current
          .append($("<input id=\"currentAttitude" + i + "\" name=\"currentAttitude\" type=\"radio\" class=\"radio\" value=\"" + e.attitudeCode + "\"" + checked + ">"))
          .append($("<label for=\"currentAttitude" + i + "\">" + e.attitudeName + "</label>"));
      });

      var $next = $("<td>");

      $(nextList).each(function(i, e) {
        var checked = e.attitudePick ? " checked" : "";

        $next
          .append($("<input id=\"planAttitude" + i + "\" name=\"planAttitude\" type=\"radio\" class=\"radio\" value=\"" + e.attitudeCode + "\"" + checked + ">"))
          .append($("<label for=\"planAttitude" + i + "\">" + e.attitudeName + "</label>"));
      });

      $("#attitudes td").remove();

      $("#attitudes")
        .append($current)
        .append($next);

      if ($("input[name=currentAttitude]:checked").length === 0) {
        $("input[name=currentAttitude]:first-child")[0].checked = true;
      }

      if ($("input[name=planAttitude]:checked").length === 0) {
        $("input[name=planAttitude]:first-child")[0].checked = true;
      }
    }

    function setAttachments(attachments) {
      function href(fileId) {
        return "${pageContext.request.contextPath}/itsm/atchmnfl/site/retrieve.do?atchmnflSn=1&atchmnflId=" + fileId;
      }

      if (attachments.requiredFileId) {
        $("#requiredFileName").html("<a href=\"" + href(attachments.requiredFileId) + "\">" + attachments.requiredFileName + "</a>");
      }
      if (attachments.additionalFileId) {
        $("#additionalFileName").html("<a href=\"" + href(attachments.additionalFileId) + "\">" + attachments.additionalFileName + "</a>");
      }
    }

    /**
     * 상세 정보 받아온 뒤 화면에 설정
     * @param response
     */
    function setDetail(response, next) {
      var head = headFuncs[response.master.repTyCode](response.prevDate);

      setTitle(response.master.repTyCode, head);

      var details = {};

      $(response.detailList).each(function(i, e) {
        details[e.assignCode] = e;
      });

      var visible = manageAttachmentIsVisible(response.detailList);

      if (visible && response.attachments) {
        setAttachments(response.attachments);
      }

      fetchAssignList(response.master.repTyCode).done(function(response) {
        setDetails(response.list, details);
      });

      repTyCode = response.master.repTyCode;

      if (response.master.repTyCode === "B001") {
        $("#attachments").remove();

        $.ajax({
          url: "${pageContext.request.contextPath}/itsm/rep/detail2/mngr/getAttitudesAjax.do",
          method: "GET",
          data: {
            reportDt: response.master.reportDt,
            userId: params.userId,
            next: next,
          },
          success: function(response) {
            setAttitudes(response.currentList, response.nextList);
          }
        });
      }
      else {
        $("#attitudes").remove();
      }
    }

    $(function() {
      if (params.repSn) {
        $("#isCreated").remove();
        $("#buttons").removeClass("col-4").addClass("col-8");
      }

      //
      if (isNewer()) {
        setTitle(params.repTyCode);

        fetchAssignList(params.repTyCode).done(function(response) {
          setDetails(response.list);
          manageAttachmentIsVisible(response.list);
        });

        if (params.repTyCode === "B001") {
          $("#attachments").remove();

          $.ajax({
            url: "${pageContext.request.contextPath}/itsm/rep/detail2/mngr/getAttitudesAjax.do",
            method: "GET",
            data: {
              reportDt: headFuncs.B001(),
            },
            success: function(response) {
              setAttitudes(response.currentList, response.nextList);
            },
          })
        }
        else {
          $("#attitudes").remove();
        }
      }
      else {
        fetchDetail(params.repSn, params.userId).done(function(response) {
          setDetail(response);
        });
      }
    });
  </script>
</head>

<body>

<main id="content">
  <form name="data">
    <div class="page-header">
      <div class="row">
        <div class="col-4">
          <h3 class="page-title">
            <strong id="programNameHead"></strong> <span id="programNameBody"></span>
          </h3>
        </div>

  <%--      TODO 불러오기: 생성일때만 불러오게, 월간: 날짜, 주간 및 월간: 숫자?--%>
        <div class="col-4" id="isCreated">
          <div class="input-group">
            <input type="text" class="control singleDate" name="date" id="reportDt">
            <button type="button" onclick="fncRetrieveRep()" class="btn btn-primary">이전 보고서 불러오기</button>
          </div>
        </div>

        <div id="buttons" class="col-4 text-right">
          <button type="button" onclick="fncRetrieveList()" class="btn btn-secondary"><i class="ico-list"></i>목록</button>
  <%--        TODO 삭제: 생성중이 아니고 본인일때만 --%>
          <button type="button" id="deleteBtn" onclick="fncDelete()" class="btn btn-dark"><i class="ico-delete"></i>삭제</button>
  <%--        TODO 저장: 생성이거나 본인일때만--%>
          <button type="button" id="saveBtn" onclick="fncSave()" class="btn btn-primary"><i class="ico-save"></i>저장</button>
        </div>
      </div>

  <%--    TODO 확정된 날짜의 보고서일 경우 수정삭제불가--%>
      <div id="confirm_note" style="display:none;" class="row">
        <p class="col-8 text-left">확정 또는 확정 요청중인 보고서입니다. 수정을 원하시면 관리자에게 문의해주세요.</p>
      </div>
    </div>

    <table class="reporttbl">
      <caption>업무보고서 작성</caption>
      <colgroup>
        <col style="width:10%">
        <col style="width:45%">
        <col style="width:45%">
      </colgroup>
      <thead>
      <tr>
        <th scope="col">구분</th>
        <th scope="col">실적</th>
        <th scope="col">계획</th>
      </tr>
      </thead>
      <tbody id="details">
        <tr id="attitudes">
          <th scope="row">근태</th>
        </tr>
      </tbody>
    </table>
    <div id="attachments" style="display: none">
      <div class="input-group">
        <p class="control">필수 첨부파일: <span id="requiredFileName"></span></p>
        <input type="file" class="control" aria-label="Upload" name="requiredFile">
      </div>
      <div class="input-group">
        <p class="control">추가 첨부파일: <span id="additionalFileName"></span></p>
        <input type="file" class="control" aria-label="Upload" name="additionalFile">
        <button type="button" class="btn btn-danger" onclick="fncDeleteFile()">삭제</button>
      </div>
    </div>
  </form>
</main>

</body>
</html>