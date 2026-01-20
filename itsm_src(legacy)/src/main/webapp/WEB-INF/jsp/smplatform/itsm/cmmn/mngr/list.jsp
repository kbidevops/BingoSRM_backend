<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <title><c:out value="${LEFT_MENU_PROGRMACCESAUTHORVO.progrmNm}"/></title>
  <style type="text/css">
    .tbDiv { height: 450px; overflow: auto; }
    .tbTy { height: 250px; overflow: auto; width: 800px; }
  </style>
  <script type="text/javaScript" language="javascript" defer="defer">
    $.fn.setCursorPosition = function(pos) {
      this.each(function(index, elem) {
        if (elem.setSelectionRange) {
          elem.setSelectionRange(pos, pos);
        }
        else if (elem.createTextRange) {
          var range = elem.createTextRange();
          range.collapse(true);
          range.moveEnd('character', pos);
          range.moveStart('character', pos);
          range.select();
        }
      });

      return this;
    };

    /* 하위코드 가져오는 함수 */
    function retrieveList(td) {
      var cmmnCodeTy = $(td).siblings("td").eq(1).children("input[name='cmmnCodeTys']").val();

      $("input[name='cmmnCodeTy']").val(cmmnCodeTy);

      $.ajax({
        type: "post",
        url: "<c:url value='/itsm/cmmncode/mngr/retrieveCmmnCodeAjax.do'/>",
        data: {
          cmmnCodeTy: cmmnCodeTy,
        },
	  		dataType: "json",
	  		traditional: true,
	  		success: function(request) {
          $("#codeTb tbody").remove();
          $("#codeTb").append("<tbody></tbody>");
          $.each(request.resultList, function(i, result) {
            var tr = $("<tr></tr>");
		  			var td = $("<td></td>");

            td.append($("<input/>", { type: "checkbox", name: "deleteYns", value: result.cmmnCode }));

            if (result.deleteYn === 'Y') {
	  					td.children("input[name='deleteYns']").attr("checked","checked");
	  				}

	  				tr.append(td);
	  				tr.append($("<td>" + result.cmmnCodeTy + "</td>"));
	  				td = $("<td>" + result.cmmnCode + "</td>");
	  				td.append($("<input/>", { type: "hidden", name: "cmmnCodes", value: result.cmmnCode }));
	  				td.children("input[name='cmmnCodes']").addClass("cmmnCodes");
	  				td.children("input[name='cmmnCodes']").addClass("control");
	  				tr.append(td);
	  				td = $("<td id='cmmnCodeNm' onclick='updateView(this)'>" + result.cmmnCodeNm + "</td>");
	  				td.append($("<input/>", { type: "hidden", name: "cmmnCodeNms", value: result.cmmnCodeNm }));
	  				td.children("input[name='cmmnCodeNms']").addClass("cmmnCodeNms");
	  				td.children("input[name='cmmnCodeNms']").addClass("control");
	  				tr.append(td);
            td = $("<td id='cmmnCodeSubNm1' onclick='updateView(this)'>" + (result.cmmnCodeSubNm1 || "") + "</td>");
	  				td.append($("<input/>", { type: "hidden", name: "cmmnCodeSubNm1s", value: result.cmmnCodeSubNm1 }));
	  				td.children("input[name='cmmnCodeSubNm1s']").addClass("cmmnCodeSubNm1s");
	  				td.children("input[name='cmmnCodeSubNm1s']").addClass("control");
	  				tr.append(td);
	  				td = $("<td id='sortNo' onclick='updateView(this)'>" + result.sortNo + "</td>");
	  				td.append($("<input/>", { type: "hidden", name: "sortNos", value: result.sortNo }));
	  				td.children("input[name='sortNos']").addClass("sortNos");
	  				td.children("input[name='sortNos']").addClass("control");
	  				tr.append(td);
	  				td = $("<td id='cmmnCodeDc' onclick='updateView(this)'>" + result.cmmnCodeDc + "</td>");
	  				td.append($("<input/>", { type: "hidden", name: "cmmnCodeDcs", value: result.cmmnCodeDc }));
	  				td.children("input[name='cmmnCodeDcs']").addClass("cmmnCodeDcs");
	  				td.children("input[name='cmmnCodeDcs']").addClass("control");
	  				tr.append(td);
            $("#codeTb tbody").append($(tr));
          });

          if (request.resultList.length > 0) {
            $(".nolist").remove();
	  			}
          else {
		  			$("#tbDiv").append('<div class="nolist"> 조회된 데이터가 없습니다.</div>');
	  			}
        },
	  		error: function(xhr) {
	  			alert("불러오기 실패");
	  		},
      });
    }

    /* 코드 수정 입력창 생성 함수 */
    var hidden;
    function updateView(td) {
      if ($(td).children("input[type='hidden']").length === 0) {
        return;
      }

      var preTd = $(hidden).parent("td");
      $(hidden).attr("type", "hidden");
      $(preTd).text($(hidden).val());
      $(preTd).prepend($(hidden));
      hidden = $(td).children("input[type='hidden']");
      $(td).text("");
      $(td).prepend($(hidden).attr("type", "text"));
      var length = $(td).children("input[type='text']").val().length;
      $(td).children("input[type='text']").focus();
      $(td).children("input[type='text']").setCursorPosition(length);
    }

    /* 신규 코드유형 입력창 생성 함수 */
    function fncTyAdd() {
      var newTd = "<tr>" +
        "<td>" + "</td>" +
        "<td id='cmmnCodeTy' onclick='updateView(this)'>" +
        "<input type='hidden' class='cmmnCodes control' name='cmmnCodeTys' value='${result.cmmnCodeTy }'>" +
        "</td>" +
        "<td id='cmmnCodeNm' onclick='updateView(this)'>" +
        "<input type='hidden' class='cmmnCodeTyNms control' name='cmmnCodeTyNms' value='${result.cmmnCodeTyNm }'>" +
        "</td>" +
        "<td id='cmmnCodeTyDc' onclick='updateView(this)'>" +
        "<input type='hidden' class='cmmnCodeTyDcs control' name='cmmnCodeTyDcs' value='${result.cmmnCodeTyDc }'>" +
        "</td>" +
        "</tr>";
      $("#codeTyTb tbody").prepend(newTd);
    }

    /* 신규 코드 입력창 생성 함수 */
    function fncAdd() {
      var cmmnCodeTy = $("input[name='cmmnCodeTy']").val();

      if (cmmnCodeTy === null || cmmnCodeTy === "") {
        alert("코드유형을 먼저 선택해주세요.");
    		return;
    	}

      var newTd = "<tr>" +
        "<td>" + "</td>" +
        "<td>" + cmmnCodeTy + "</td>" +
        "<td id='cmmnCode' onclick='updateView(this)'>" +
        "<input type='hidden' class='cmmnCodes control' name='cmmnCodes' value='${result.cmmnCode }'>" +
        "</td>" +
        "<td id='cmmnCodeNm' onclick='updateView(this)'>" +
        "<input type='hidden' class='cmmnCodeNms control' name='cmmnCodeNms' value='${result.cmmnCodeNm }'>" +
        "</td>" +
        "<td id='cmmnCodeSubNm1s' onclick='updateView(this)'>" +
        "<input type='hidden' class='cmmnCodeSubNm1s control' name='cmmnCodeSubNm1s' value='${result.cmmnCodeSubNm1s }'>" +
        "</td>" +
        "<td id='sortNo' onclick='updateView(this)'>" +
        "<input type='hidden' class='sortNos control' name='sortNos' value='${result.sortNo }'>" +
        "</td>" +
        "<td id='cmmnCodeDc' onclick='updateView(this)'>" +
        "<input type='hidden' class='cmmnCodeDcs control' name='cmmnCodeDcs' value='${result.cmmnCodeDc }'>" +
        "</td>" +
        "</tr>";
      $("#codeTb tbody").prepend(newTd);
    }

    /* 신규 코드와 수정사항 저장 함수 */
    function fncSave() {
      confirm("저장하시겠습니까?", "코드 변경 사항 저장", null, function(request) {
			  if (request) {
          codeexp = /^[A-Z]{1}[0-9]+$/; //code 정규식

          // cmmnCode 배열로 만들기
          var codeLength = $("input[name='cmmnCodes']").length;
			    var codes = new Array(codeLength);

          for (var i = 0; i < codeLength; i++) {
            var code = $("input[name='cmmnCodes']")[i].value;

            // 정규식 검증
            if (codeexp.test(code)) {
				    	codes[i] = code;
            }
            else {
              alert("'" + code + "'는 형식에 맞지 않습니다.");
              return;
            }
			    }

          // 정렬번호 입력 확인
          var sortNoList = $("input[name='sortNos']");

          for (var i = 0; i < sortNoList.length; i++) {
            var sortNo = sortNoList[i].value;

            if (!sortNo) {
              alert("정렬번호를 모두 입력해주시기 바랍니다.");
              return;
            }

            if (!/^\d+$/.test(sortNo)) {
              alert("정렬번호는 숫자로만 입력해주시기 바랍니다.");
              return;
            }
          }

          // ?
          var codeTyLength = $("input[name='cmmnCodeTys']").length;
			    var codeTys = new Array(codeTyLength);

          for (var i = 0; i < codeTyLength; i++) {
            var code = $("input[name='cmmnCodeTys']")[i].value;

            // 정규식 검증
            if (codeexp.test(code)) {
              codeTys[i] = code;
            }
            else {
              alert("'" + code + "'는 형식에 맞지 않습니다.");
              return;
            }
          }

          // 중복검증
          $.ajax({
            type: "post",
            url: "<c:url value='/itsm/cmmncode/mngr/cmmnCodeValidationAjax.do'/>",
            data: {
              cmmnCodes: codes,
              cmmnCodeTys: codeTys,
            },
			  		dataType: "json",
			  		traditional: true,
			  		success: function(request) {
              if (request.returnMessage === "") {
                $("#tableForm").attr("action", "/itsm/cmmncode/mngr/update.do");
					    	$("#tableForm").submit();
			  			}
			  			else {
                alert("[" + request.returnMessage + "]는 중복될 수 없습니다.");
			  			}
			  		},
			  		error: function(xhr) {
              alert("저장 실패");
			  		},
          });
        }
      });
    }
  </script>
</head>

<body>

<main id="content">
  <div class="page-header">
    <h3 class="page-title"><c:out value="${LEFT_MENU_PROGRMACCESAUTHORVO.progrmNm}"/></h3>
  </div>

  <form id="tableForm" method="post">
    <div class="row">
      <div class="card">
        <div class="card-header">
          <h4 class="card-title">코드유형목록</h4>
          <div class="btnbox fr">
            <button type="button" class="btn btn-primary" onclick="fncSave()"> 저 장</button>
            <button type="button" class="btn btn-secondary" onclick="fncTyAdd()">신 규</button>
          </div>
        </div>

        <div class="tbTy">
          <table id="codeTyTb" class="gridtbl">
            <caption>코드 목록</caption>
            <colgroup>
              <col style="width:100px">
              <col style="width:100px">
              <col>
              <col style="width:400px">
            </colgroup>
            <thead>
            <tr>
              <th scope="col">삭제여부</th>
              <th scope="col">코드</th>
              <th scope="col">코드명</th>
              <th scope="col">코드설명</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="result" items="${tyList}" varStatus="status">
              <tr>
                <c:if test="${ result.deleteYn == 'Y' }">
                  <td><input checked type="checkbox" name="deleteYnTys" value="${result.cmmnCodeTy}"/></td>
                </c:if>
                <c:if test="${ result.deleteYn == 'N' }">
                  <td><input type="checkbox" name="deleteYnTys" value="${result.cmmnCodeTy}"/></td>
                </c:if>
                <td onclick="retrieveList(this)">
                  <c:out value="${result.cmmnCodeTy}"/>
                  <input type="hidden" class="cmmnCodeTys control" name="cmmnCodeTys" value="${result.cmmnCodeTy }">
                </td>
                <td onclick="retrieveList(this)">
                  <c:out value="${result.cmmnCodeTyNm}"/>
                  <input type="hidden" class="cmmnCodeTyNms control" name="cmmnCodeTyNms" value="${result.cmmnCodeTyNm }">
                </td>
                <td onclick="retrieveList(this)">
                  <c:out value="${result.cmmnCodeTyDc}"/>
                  <input type="hidden" class="cmmnCodeTyDcs control" name="cmmnCodeTyDcs" value="${result.cmmnCodeTyDc }">
                </td>
              </tr>
            </c:forEach>
            </tbody>
          </table>
          <c:if test="${empty tyList}">
            <div class="nolist"> 조회된 데이터가 없습니다.</div>
          </c:if>
        </div>
      </div>
    </div>

    <div class="row">
      <input type="hidden" name="cmmnCodeTy">
      <div class="card">
        <div class="card-header">
          <h4 class="card-title">코드목록</h4>
          <div class="btnbox fr">
            <button type="button" class="btn btn-secondary" onclick="fncAdd()">신 규</button>
          </div>
        </div>

        <div class="tbDiv">
          <table id="codeTb" class="gridtbl">
            <caption>코드 목록</caption>
            <colgroup>
              <col style="width:100px">
              <col style="width:100px">
              <col style="width:150px">
              <col style="width:250px">
              <col style="width:250px">
              <col style="width:100px">
              <col>
            </colgroup>
            <thead>
            <tr>
              <th scope="col">삭제 여부</th>
              <th scope="col">상위코드</th>
              <th scope="col">코드</th>
              <th scope="col">코드명</th>
              <th scope="col">서브코드명</th>
              <th scope="col">정렬번호</th>
              <th scope="col">코드설명</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="result" items="${resultList}" varStatus="status">
              <tr>
                <c:if test="${ result.deleteYn == 'Y' }">
                  <td><input checked type="checkbox" name="deleteYns" value="${result.cmmnCode}"/></td>
                </c:if>
                <c:if test="${ result.deleteYn == 'N' }">
                  <td><input type="checkbox" name="deleteYns" value="${result.cmmnCode}"/></td>
                </c:if>
                <td>
                  <c:out value="${result.cmmnCodeTy}"/>
                </td>
                <td>
                  <c:out value="${result.cmmnCode}"/>
                  <input type="hidden" class="cmmnCodes control" name="cmmnCodes" value="${result.cmmnCode }">
                </td>
                <td id="cmmnCodeNm" onclick="updateView(this)">
                  <c:out value="${result.cmmnCodeNm}"/>
                  <input type="hidden" class="cmmnCodeNms control" name="cmmnCodeNms" value="${result.cmmnCodeNm }">
                </td>
                <td id="cmmnCodeSubNm1" onclick="updateView(this)">
                  <c:out value="${result.cmmnCodeSubNm1}"/>
                  <input type="hidden" class="cmmnCodeSubNm1s control" name="cmmnCodeSubNm1s" value="${result.cmmnCodeSubNm1 }">
                </td>
                <td id="sortNo" onclick="updateView(this)">
                  <c:out value="${result.sortNo}"/>
                  <input type="hidden" class="sortNos control" name="sortNos" value="${result.sortNo }">
                </td>
                <td id="cmmnCodeDc" onclick="updateView(this)">
                  <c:out value="${result.cmmnCodeDc}"/>
                  <input type="hidden" class="cmmnCodeDcs control" name="cmmnCodeDcs" value="${result.cmmnCodeDc }">
                </td>
              </tr>
            </c:forEach>
            </tbody>
          </table>
          <c:if test="${empty resultList}">
            <div class="nolist"> 조회된 데이터가 없습니다.</div>
          </c:if>
        </div>
      </div>
    </div>
  </form>
</main>

</body>
</html>
