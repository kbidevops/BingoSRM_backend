﻿﻿define([ "jquery", "jwork", "jwork.util.dateutils"], function($, J, D){

    var LOG_RESPONSE = location.hostname == "localhost" && true; //ajax response내용을 브라우저 콘솔에 출력 여부
    var INFO_RIGHT_CLICK = location.hostname == "localhost" && true; //오른쪽 클릭하면 jquery 정보 보여줌..

    var DELIMITER_DATE = "-";

    //공통 종료일자 값.(grid에는 millisecond값을 updateCellData : cmmGetDateMillis(CMM_CL_DATE))
    window.CMM_CL_DATE = "2099-12-31";
    window.CMM_COLOR_REQUIRE = "#FFF0B5"; //그리드 필수입력 색깔
    window.CMM_NOT_NULL_MARK = "<span class=star>*</span>"; //그리드 헤더의 필수입력 * 표시

    /**
    * 팝업창을 호출한다.
    * ex :
    *      var searchDealerSelPopup = function(dlrId, dlrNm, dlrTpCd, dlrStsCd, dlrAppStsCd) {
    *        cmmPopupOpen("<c:url value='/biz/prd/dealer/prdDealerSelPop.do' />",
    *                  "dealerSelPopup", //생략가능
    *                  { DLR_ID:dlrId,
    *                    DLR_NM:dlrNm,
    *                    DLR_TP_CD:dlrTpCd,
    *                    DLR_STS_CD:dlrStsCd,
    *                    DLR_APP_STS_CD:dlrAppStsCd,
    *                    POPUP_FLAG:"구분하고자하는명" <----- 같은 팝업을 여러항목에서 쓸때..(form, grid)
    *                  },                //생략가능
    *                  700,       //생략가능
    *                  500        //생략가능
    *                  );
    *        };
    *      받는 함수에서 POPUP_FLAG 사용
    *      window.dealerSelected = function(dealerInfo, popupFlag) {
    *         if ( popupFlag == "구분하고자하는명" ) {....
    */
    window.popupOpen = function(url, target, param, width, height ) {
        //alert('popupOpen()가 cmmPopupOpen()으로 변경 되었습니다.');
        return cmmPopupOpen(url, target, param, width, height);
    };

    window.cmmPopupOpen = function(url, target, data, width, height ) {
        var popupOptions = {
            height : nvl(height, 520)
            , width  : nvl(width, 700)
            , method : "post"
            , data   : data
        };
        var popup = J.open(url, nvl(target, 'POPUP_DEFAULT'), popupOptions );
        popup.focus();
        return popup;// {data:param, width:"700", height:"900", method:"post"});
    };

    /**
     * 우편번호 검색 팝업
     * var zipNo = $("#zipNo");
     * var bscAddr = $("#bscAddr");
     * var dtlAddr = $("#dtlAddr");
     * cmmPopupZip(zipNo, bscAddr, dtlAddr);
     * @param zipNo, bscAddr, dtlAddr 우편번호, 기본주소, 상세주소의 jquery 객체
     */
    window.cmmPopupZip = function(zipNo, bscAddr, dtlAddr) {
        window.cmmPopupOpen(J.getCtx() + "/biz/ord/zip/ordZipNoSelPop.do",
            "zipPopup",
            {zipCode: zipNo.val()}
            , 1000, 550);
        /**
         * 우편번호 값 입력
         */
        window.zipSelected= function(zipInfo) {
            zipNo.val(zipInfo.ZIP_NO);
            bscAddr.val(zipInfo.BSC_ADDR.trim()+" " + zipInfo.BSC_ADDR_DTL.trim());
            //dtlAddr.val(zipInfo.BSC_ADDR_DTL.trim()); //사용자입력
        };
    };

    /**
    * grid ajax 실행 : 조회결과를 grid에 출력.
    * ex)
    * cmmAjaxGrid({
    *      grid       : dealerGrid
    *     ,url        : "<c:url value='/biz/prd/dealer/dealerList.do' />"
    *     ,data       : {
    *           DLR_ID         : $("#dealerSelect_dlrId").val()
    *         , DLR_NM         : $("#dealerSelect_dlrNm").val()
    *         , DLR_APP_STS_CD : $("#dealerSelect_dlrAppStsCd").val()
    *         , RPS            : $("#dealerSelect_rps").val()
    *         , BIZ_NO         : $("#dealerSelect_bizNo").val()
    *      }
    *     ,path       : Mapping key // tree grid 시 controller 에서 매핑되는 key 값
    *     ,success    : function(response) {   //선택
    *          alert("success");
    *     }
    *     ,exception  : function(response) { //선택
    *          alert("exception");
    *     }
    *  });
    *
    */
    window.cmmAjaxGrid = function(param){
        param.grid.ajax({
            url       : param.url
            ,type      : nvl(param.type, "post")  //"post"
            ,data      : param.data
            ,path         : param.path
            ,async     : nvl(param.async, true )
            ,dataType  : nvl(param.dataType, "json")
            ,success   : function(response) {
                debugResponseInfo(param, response);
                if ( param.showSuccessMsg == true ) {
                    alert(/*response.code + " : " + */response.message);
                }
                if ( $.type(param.success) === 'function' ) {
                    param.success(response);
                }
            }
            ,exception : function(response) {
                alert(/*response.code + " : " + */response.message);
                if ( $.type(param.exception) === 'function' ) {
                    param.exception(response);
                }
            }
        });
    };

    /**
    * ajax 실행
    * ex)
    * cmmAjax({
    *     ,url        : "<c:url value='/biz/prd/dealer/dealerList.do' />"
    *     ,data       : {
    *           DLR_ID         : $("#dealerSelect_dlrId").val()
    *         , DLR_NM         : $("#dealerSelect_dlrNm").val()
    *         , DLR_APP_STS_CD : $("#dealerSelect_dlrAppStsCd").val()
    *         , RPS            : $("#dealerSelect_rps").val()
    *         , BIZ_NO         : $("#dealerSelect_bizNo").val()
    *      }
    *     ,success    : function(response) {   //선택
    *          alert("success");
    *     }
    *     ,exception  : function(response) { //선택
    *          alert("exception");
    *     }
    *  });
    */
    window.cmmAjax = function(param) {
        $.ajax({
            url       : param.url
           ,type      : nvl(param.type, "post")  //"post"
           ,data      : param.data
           ,async     : nvl(param.async, true )
           ,dataType  : nvl(param.dataType, "json")
           ,success   : function(response) {
                debugResponseInfo(param, response);
                if ( param.showSuccessMsg == true ) {
                    alert(/*response.code + " : " + */response.message);
                }
                if ( $.type(param.success) === 'function' ) {
                    param.success(response,param.flag);
                }
            }
            ,exception : function(response) {
                alert(/*response.code + " : " + */response.message);
                if ( $.type(param.exception) === 'function' ) {
                    param.exception(response);
                }
            }
        });
    };

    /**
    * form submit ajax 실행
    * cmmAjax({
    *      form       : formId
    *     ,url        : "<c:url value='/biz/prd/dealer/dealerList.do' />"
    *     ,data       : {
    *           DLR_ID         : $("#dealerSelect_dlrId").val()
    *         , DLR_NM         : $("#dealerSelect_dlrNm").val()
    *         , DLR_APP_STS_CD : $("#dealerSelect_dlrAppStsCd").val()
    *         , RPS            : $("#dealerSelect_rps").val()
    *         , BIZ_NO         : $("#dealerSelect_bizNo").val()
    *      }
    *     ,success    : function(response) {   //선택
    *          alert("success");
    *     }
    *     ,exception  : function(response) { //선택
    *          alert("exception");
    *     }
    *  });
    */
    window.cmmAjaxSubmit = function(param) {
        $("#"+param.form).ajaxSubmit({
             url       : param.url
            ,type      : nvl(param.type, "post")  //"post"
            ,data      : param.data
            ,async     : nvl(param.async, true )
            ,dataType  : nvl(param.dataType, "json")
            ,success   : function(response) {
                debugResponseInfo(param, response);
                if ( param.showSuccessMsg == true ) {
                    alert(/*response.code + " : " + */response.message);
                }
                if ( $.type(param.success) === 'function' ) {
                    param.success(response);
                }
            }
            ,exception : function(response) {
                alert(/*response.code + " : " + */response.message);
                if ( $.type(param.exception) === 'function' ) {
                    param.exception(response);
                }
            }

        });
    };

    window.formSerialize = function(formObj){
        var param = {};

        $(formObj.serializeArray()).each(function(i, v) {
            param[v.name] = v.value;
        });
        return param;
    };

    window.cmm_openPopupCode = function(param, cmm_form, cmm_rowId, isViewOnlyCodeAndCodeNm){
        param.cmm_form = cmm_form;
        param.cmm_rowId = cmm_rowId;
        param.cmm_onlyCode = isViewOnlyCodeAndCodeNm;
        
        var popupWidth = isViewOnlyCodeAndCodeNm ? 600 : 1000;
        
        J.open(
                J.getCtx() + '/jwork/code/codePopup.do'
                , 'AGCODEPOP'
                , {
                    method : "POST"
                    , width : popupWidth
                    , height : 650
                    , data : param
                }    
        );
    };
    ////////////////////////////////////////////////////////////////////
//개발중에는 마우스 오른쪽 누르면 element의 정보와 jquery를 보여줌.
    var showRightInfo = function(element) {
        var orgValue = element.data("orgValue");

        var title  = "▣ " + element.outerHTML().replace(/>\s+</g,"><").substring(0,1000) + "\n";
            title += "▣ orginal value =【" + orgValue + "】\n SHIFT, ALT, CTRL중 1개와 함께 클릭하면 이창이 뜹니다.";

        var jqry = "";

        if ( element.attr("id") && element.attr("id").length > 0 ) {
            jqry = '$("#' + element.attr("id") + '").val()';
        }

        if ( element.attr("name") && element.attr("name").length > 0 ) {
            jqry += '  $("#' + element.parents("form").attr("id") + ' ' + '[name=' + element.attr("name") + ']").val()';

        } else if ( element.attr("class") && element.attr("class").length > 0  ) {
            jqry += '  $(".' + element.attr("class") + '").val()';

    //        } else {
    //            jqry += '$("' + element.prop("tagName") + '")';
        }

        prompt( title, jqry.trim() );
    };

    $(function() {
        if ( !INFO_RIGHT_CLICK ) return true;
        //$("a, :input").bind("contextmenu", function(event) {
        $("body").bind("contextmenu", function(event) {
            if ( event.shiftKey || event.altKey || event.ctrlKey ) {
                event.preventDefault();
                event.stopPropagation();
                var target = $(event.target);
                if ( target.is("a") || target.is(":input") || target.is("img") ) {
                    showRightInfo( target );
                    return false;
                }
            }
            return true;
        });
    });

    //로컬용 로그 만들기
    var toStringBuilder = function(obj, isMulti) {
        var str = "",
            arr = [];
        for ( o in obj ) {
            if ( obj.hasOwnProperty(o) ){
            arr[arr.length] = o;
            }
        }
        arr.sort();
        for ( var i=0 ; i < arr.length ; i++ ) {
            if ( isMulti ) str+= "    ";
            str += arr[i] + " =【" + obj[arr[i]] + "】";
            if ( isMulti ) str+= "\n";
            else  str+= ", ";
        }
        return str;
    };

    //로컬용  로그
    var debugResponseInfo = function (param, response) {
        if (LOG_RESPONSE && window.console && window.console.log) {
            var logStr = "";
            logStr += "======== response info start ==========================================\n";
            if ( param.form ) {
                logStr +=  "== FORM : " + param.form + "\n";
            }
            if ( param.grid ) {
                logStr +=  "== GRID : " + param.grid.elementId + "\n";
            }
            logStr +=  "==  URL : " + param.url  + "\n";
            logStr +=  "== DATA : " + toStringBuilder(param.data)  + "\n";
            
            logStr +=  "--[ response ]---------------------------------------------------------";
            window.console.log( logStr );
            window.console.log( response );
            logStr = "";
            for ( resAttr in response ) {
                if ( response.hasOwnProperty(resAttr)) {
                    var data = response[resAttr];
                    if ( resAttr == "data" ) {
                        for ( dataAttr in data ) {
                            if ( "jgrid_totalCount,jgrid_details,jgrid_currentNumber".indexOf(dataAttr) > -1 )
                                continue;
                            if ( data.hasOwnProperty(dataAttr) ) {
                                var val = data[dataAttr];
                                if ( $.isArray(val) ) {
                                    logStr += "♠" + dataAttr + "♠ size=" + val.length + "\n" + toStringBuilder(val[0], true) +"\n";
                                } else {
                                    logStr += "◈" + dataAttr + "◈ = " +"\n";
                                    logStr += nvl(val, "value is null", val) +"\n";
                                }
                            }
                        }
                    } else { //mav.addObject("xxx")
                        if ( $.isArray(data) ) {
                            logStr += "♥" + resAttr + "♥ size=" + data.length + "\n" + toStringBuilder(data[0], true) +"\n";
                        } else if ( $.type(data) != "object" ) {
                            logStr += "[ " + resAttr + "] = " + data +"\n";
                        } else {
                            logStr += "♣" + resAttr + "♣\n" + toStringBuilder(data, true) +"\n";
                        }
                    }
                }
            }
            window.console.log( logStr + "======== response info end ==========================================" );
        }
    };
    ////////////////////////////////////////////////////////////////////

    /*
    * Full example here: http://jsfiddle.net/jboesch26/3SKsL/1/
    * jquery에 outerHTML 효과.
    */
    $.fn.outerHTML = function(){
        return $(this).clone().wrapAll("<div/>").parent().remove().html();
    };

    /**
    *  null, undefined를 ""으로 변환.
    *  val : 비교할값
    *  ifNullVal : val이 null일때 사용할 값.
    *  ifNotNullVal : val이 null이 아닐때 사용할 값.
    */
    window.nvl = function (val, ifNullVal, ifNotNullVal) {
        return val==null
            ?( ifNullVal==null?"":ifNullVal)
            :(ifNotNullVal==null?val:ifNotNullVal);
    };

    /**
    * 조회결과 response를 form에 채우는 populate후에
    * 탭화면에서 변경사항 검사하기위해 조회된 원래 값을 orgValue로 추가해줌.
    *
    * cmmGetFormState(formId)로 상태를 알아냄.
    *
    * checkbox, radio는 나중에
    */
    window.cmmMakeFormOrgValue = function(formId){

        var $elements = $("#"+formId + " :input");

        if ( $elements.length) {
            $elements.each(function(index) {
                var element = $(this),
                type = element.attr("type"),
                tagName = element.prop("tagName").toLowerCase();

                switch ( type || tagName ) {
                case "checkbox" :
                case "radio" :
                    element.data("orgValue", element.prop("checked") );
                    break;
                default :
                    element.data("orgValue", element.val() );
                }
            });
        }

    };

    /**
    * cmmMakeFormOrgValue()로 추가한 orgValue를 제거함.
    * resetForm() 후에 꼭 해줘야함..
    *
    *   $("#formDealerInsert").resetForm();
    *   cmmClearFormOrgValue("formDealerInsert");
    */
    window.cmmClearFormOrgValue = function(formId){
        var form = $("#"+formId+" :input");
        form.each(function(index) {
            $(this).removeData("orgValue");
        });
    };

    /**
    *  Form의 작업 상태를 반환한다.
    *  populate() 후에 조회결과에 없는 항목 값 설정후에 cmmMakeFormOrgValue(formId)를 실행해둬야함.
    *
    *  @return :
    //*     B : Form 조회전 초기화된 상태. orgValue가 존재하지 않음. cmmClearFormOrgValue()를 한 직후.
    *     I : 신규버튼에서 cmmMakeFormOrgValue()으로 "" 이나 selectedIndex=0 인 경우.
    *     N : 조회후에 Form의 데이터가 수정되지 않음.
    *     U : 조회후에 Form의 데이터가 수정됨.
    */
    window.cmmGetFormState = function(formId) {
        var $elements         = $("#"+formId + " :input"),
            returnFormState = "N",     //form's element have value. not changed
            orgValueCount   = 0;    //if orgValue not exists then returnFormState="I"

        if ( $elements.length) {
            $elements.each(function(index) {
                var $element = $(this);
                if ( $.type($element.data("orgValue")) !== "undefined" ){
                    orgValueCount++;

                    var type = $element.attr("type"),
                    tagName = $element.prop("tagName").toLowerCase();

                    switch ( type || tagName ) {
                    case "checkbox" :
                    case "radio" :
                        if ( $element.prop("checked") != $element.data("orgValue") ) {
                            returnFormState = "U";
                            return false;//break loop;
                        }
                        break;
                    default :
                        if ( $element.val() != $element.data("orgValue") ) {
                            returnFormState = "U";
                            return false;//break loop;
                        }
                    }
                }
            });
        }

        if ( returnFormState != "U" ) {
            if ( orgValueCount == 0 ) {
                //returnFormState = 'B';
            //} else {
                returnFormState = "I";
            }
        }
        return returnFormState ;
    };

    /**
    * 그리드에서 변경된 자료가 있는지 검사한다.
    * return : "N" : 변경된 자료없음.
    *          "U" : 변경된 자료있음.(신규 행추가도 U로본다.)
    */
    window.cmmGetGridState = function(grid) {
        alert("그리드가 변경되었는지 검사하는 함수 'isChanged()' 가 추가되었습니다. \n\n cmmGetGridState대신 'grid.isChanged()'를 애용해 주세요");
        throw new Error("그리드가 변경되었는지 검사하는 함수 'isChanged()' 가 추가되었습니다. \n\n cmmGetGridState대신 'grid.isChanged()'를 애용해 주세요");
        return false;
    };

    /**
     * 그리드에서 삭제되지 않은 행의 갯수를 센다.
     */
    window.cmmGetUndeletedRowCount = function(grid) {
        var cnt = 0;
        grid.iterate(function(rowId, data){
            if ( !grid.isDeletedRow(rowId) ) {
                cnt++;
            }
            return true;
        });
        return cnt;
    };

    /**
    * 그리드에서 삭제되지 않은 row중에 주어진 rowId가 몇번째 인지를 반환.
    * @param grid
    * @param rowId
    * @return rownum or null(not found or deleted)
    */
    window.cmmGetRowOrder = function(grid, rowId, includeDeleted) {
        var undeletedRowOrder = 0;
        grid.iterate(function(rowId2, data) {
            if ( includeDeleted || !grid.isDeletedRow(rowId2) ) {
                undeletedRowOrder++;
            }
            if ( rowId == rowId2 ) {
                return false;
            }
            return true;
        });

        return undeletedRowOrder==0 ? null : undeletedRowOrder;
    };

    /**
    * 그리드에서 컬럼에 중복된 값이 있는지 검사.
    * @return rowId : 중복값이 있으면 첫번째 중복 rowId를 반환
    *            -1 : 중복이 없으면 -1을 반환
    */
    window.cmmGetGridDuplicateRowId = function (grid, colName) {
        var gridDatas = grid.getDataJson(true).data;

        for ( var i = 0 ; i < gridDatas.length ; i++ ) {
            for ( var j = i+1 ; j < gridDatas.length ; j++ ) {
                var iRowId = gridDatas[i]._index_;
                var jRowId = gridDatas[j]._index_;

                if ( grid.isDeletedRow(iRowId) ) continue;
                if ( grid.isDeletedRow(jRowId) ) continue;

                if ( grid.getCellData(iRowId, colName) == grid.getCellData(jRowId, colName) ) {
                    return iRowId;
                }
            }
        }
        return -1;
    };

    /**
    * 그리드의 column의 값을 배열로 반환.
    */
    window.cmmGetColumnValueArray = function(grid, colName, isDeletedInclude) {
        var gridDatas = grid.getDataJson(true).data;
        var returnArray = [];

        for ( var i = 0 ; i < gridDatas.length ; i++ ) {
            var rowId = gridDatas[i]._index_;

            //삭제행 포함 안함 선택시 삭제된 행도 포함.
            if ( isDeletedInclude == false && grid.isDeletedRow(rowId) == true ) {
                continue;
            }
            returnArray[i] = grid.getCellData(rowId, colName);
        }
        return returnArray;
    };

    /**
     * 그리드의 column의 값을 중복되지 않은 배열로 반환.
     */
     window.cmmGetColumnValueDistinctArray = function(grid, colName, isDeletedInclude) {
         var gridDatas = grid.getDataJson(true).data;
         var returnArray = [];

         for ( var i = 0 ; i < gridDatas.length ; i++ ) {
             var rowId = gridDatas[i]._index_;
             var value = grid.getCellData(rowId, colName);
             //삭제행 포함 안함 선택시 삭제된 행도 포함.
             if ( isDeletedInclude == false && grid.isDeletedRow(rowId) == true ) {
                 continue;
             }
             if ( $.inArray(value, returnArray) == -1) {
                 returnArray[returnArray.length] = value;
             }
         }
         return returnArray;
     };

    /**
    * 배열에서 중복된 값이 있는지 여부
    * @return rowId : 중복값이 있으면 첫번째 중복된 index를 반환
    *            -1 : 중복이 없으면 -1을 반환
    */
    window.cmmGetDuplicateIndex = function(arr) {
        if ( $.isArray(arr) ) {
            for ( var i = 0 ; i < arr.length ; i++ ) {
                for ( var j = i+1 ; j < arr.length ; j++ ) {
                    if ( arr[i] == arr[j] ) return i;
                }
            }
        } else {
            throw new Error("param is not Array.");
        }
        return -1;
    };

    /**
    * 이메일 입력폼이 [ email앞부분 + @ + 이메일뒷부분 ] 형태인경우 검사
    * 컬럼명에 숫자 1, 2로 앞뒤를 구분했을때 사용.
    * @param formId
    * @param email검사할 name(컬럼명)
    */
    window.cmmValidateEmailTwoInput = function(formId, emailFormName) {
        var $email1 = $("#"+formId +" [name=" + emailFormName + "1]"),
            $email2 = $("#"+formId +" [name=" + emailFormName + "2]");
        if ( $email1.length == 0 || $email2.length == 0 ) {
            throw new Error("can not found element in " + formId + " : name=" + emailFormName + "1 and name=" + emailFormName + "2 ");
        }

        var emailVal1 = $email1.val().trim(),
            emailVal2 = $email2.val().trim();

        if ( ( emailVal1 == "" && emailVal2 == "" ) ) {
            return true;

        } else if ( ( emailVal1 != "" && emailVal2 == "" ) ||
                    ( emailVal1 == "" && emailVal2 != "" ) ) {
            alert( J.getMessage("cmm.warning.213"));//이메일 형식에 맞지 않습니다.
            $email1.focus();
            return false;

        } else {
            if ( ( emailVal1 + "@" + emailVal2 ).isEmail() == false) {
                alert( J.getMessage("cmm.warning.213"));//이메일 형식에 맞지 않습니다.
                $email1.focus();
                return false;
            }
        }
        return true;
    };

    /**
    * 이메일을 "@기준"으로 이메일컬럼1과 이메일컬럼2로 구분했을때.
    * 입력폼이 [ 이메일1 + @ + 이메일2 + email도메인콤보 ] 형태인경우
    * email도메인콤보값으로 이메일2에 채우기.
    */
    window.cmmSetEmail2WithDomainCd = function(emailDomainCdId, email2Id, isFocus) {
        var $emailDomainCd = $("#"+emailDomainCdId);
        var $email2 = $("#"+email2Id);

        if ($emailDomainCd.val() == "") {//직접입력
            $email2.prop("readonly", false).removeClass("off").val("");
            if ( isFocus ) $email2.focus();

        } else {
            $email2.prop("readonly", true).addClass("off").val($emailDomainCd.find("option:selected").text());
        }
    };

    /**
    * 이메일을 "@기준"으로 이메일컬럼1과 이메일컬럼2로 구분했을때.
    * 입력폼이 [ 이메일1 + @ + 이메일2 + email도메인콤보 ] 형태인경우
    * 폼을 조회하면 저장된 이메일2 값으로 email도메인콤보 선택하기.
    * 직접 입력 여부로 입력 비활성화도 한다.
    */
    window.cmmSetDomainCdWithEmail2 = function(email2Id, emailDomainCdId) {
        var $email2 = $("#"+email2Id);
        var $emailDomain = $("#" + emailDomainCdId);
        var $selectOptions = $emailDomain.find("option");
        $selectOptions.each(function(ii) {
            if ( $(this).text() == $email2.val() ) {
                $(this).prop("selected", true);
                return false;
            }
        });
        //email domain combo is selectable.
        if ( $emailDomain.prop("disabled") == false ) {
            var $emailDomainCd = $("#"+emailDomainCdId);
            if ($emailDomainCd.val() == "") {
                $email2.prop("readonly", false).removeClass("off");
            } else {
                $email2.prop("readonly", true).addClass("off");
            }
        }
    };

    /**
     * 그리드에 소수점이 포함된 숫자입력 유효성 및 길이제한
     *
     * @param precision 소수점 앞자리 길이
     * @param scale     소수점 뒤자리 길이
     * @example db column size : 7,2  -> ,resultMask:cmmGridMaskNum(5,2)  ,editMask:cmmGridMaskNum(5,2)
     *
     */
    window.cmmGridMaskNum = function(precision, scale) {
        return "(^\\d{0,"+precision+"}\\.{1}\\d{0,"+scale+"}?$|^\\d{0,"+precision+"}$)";
    };

    /**
     * 그리드에 이메일 유효성 검사 추가
     * @example ,resultMask:cmmGridMaskEmail()
     */
    window.cmmGridMaskEmail = function(precision, scale) {
        return "(^[\\w\\.\\+%-]+@[A-Za-z0-9\\.-]+\\.[A-Za-z]{2,6}$|^\\.{0}$)";
    };

    /**
    * Date 포멧 변환
    * yyyy|yy|MM|dd|e|E|hh|HH|mm|ss|a\/p
    */
    window.cmmGetDateFormat = function (dateObj, fmt) {
        if ( !dateObj ) return "";
        var ret = dateObj;
        switch($.type(dateObj)) {
            case "date" :  ret = D.format(dateObj, fmt); break;
            case "string" : ret = D.format(new Date(cmmGetDateMillis(dateObj)), fmt); break;
            case "number" : ret = D.format(new Date(dateObj), fmt); break;
        }
        return ret;
    };

    /**
    * millisecond 형식의 날짜를 더해서 지정한 포멧 또는 millisecond(grid용)로 반환.
    * @param millis
    */
    window.cmmAddDateMillis = function(millis, offsetDay, fmt) {
        var calcDate = new Date( millis + (offsetDay*1000*60*60*24)  );
        if ( fmt != null ) {
            return cmmGetDateFormat(calcDate, fmt);
        } else {
            return calcDate.getTime();
        }
    };

    window.getGmtLocalTime = function(tzOffset) { // 24시간제
          var now = new Date();
          var tz = now.getTime() + (now.getTimezoneOffset() * 60000) + (tzOffset * 3600000);
          now.setTime(tz);

          return D.format(now, "yyyy-MM-dd HH:mm:ss");
    };

    /**
     * 현재날짜시간 조회
     */
     window.getDateTime = function(){
         value = D.format(new Date(), "yyyy-MM-dd HH:mm:ss");
         return value;
    };

    /**
     * format : yyyy-MM-dd HH:mm:ss
     */
    window.getGmtDateTime = function(format){
        var date = null;
        if(format == null)    format = 'yyyy-MM-dd HH:mm:ss';
        $.ajax({
            url        : J.getCtx() + "/cmm/getGmtTime.do"
           ,type       : "post"
           ,async      : false
           ,dataType   : "json"
           ,data       : {format:format}
           ,exception  : function(response) {}
           ,success    :  function(response) {
               date =  response.TODAY;
           }
        });
        return date;        
    };
    
    /**
    * 현재날자 조회
    */
    window.getToday = function() {
        $.ajax({
            url        : "/cjlp/biz/cmm/time/baseTime.do"
           ,type       : "post"
           ,async      : false
           ,dataType   : "json"
           ,exception  : function(response) {}
           ,success    :  function(response) {
               date =  response.TODAY;
           }
        });
        value = D.format(new Date(date), "yyyy-MM-dd");

        return value;
    };

    /**
    * 현재날짜시간 조회
    */
    window.getToDateTime = function(){
        $.ajax({
            url        : "/biz/cmm/time/baseTime.do"
           ,type       : "post"
           ,async      : false
           ,dataType   : "json"
           ,exception  : function(response) {}
           ,success    :  function(response) {
               date =  response.TODAY;
           }
        });
        value = D.format(new Date(date), "yyyy-MM-dd HH:mm:ss");

        return value;
    };

    /**
    * 문자열 날짜객체 변환
    */
    window.getDateObject = function(dateStr) {
        var dateObj = new Date();

        dateObj.setFullYear( parseInt(dateStr.substr(0,4), 10),
                            parseInt(dateStr.substr(4,2), 10)-1,
                            parseInt(dateStr.substr(6,2), 10) );
        return dateObj;
    };

    /**
    * yyyy-MM-dd HH:mm:ss, yyyyMMddHHmmss 문자열 날짜객체를 millisecond (grid용) 형식으로 변환
    */
    window.cmmGetDateMillis = function(dateStrYmd){
        var dateStrNumber = dateStrYmd.replace(/\//g,"").replace(/-/g,"").replace(/\s/g,"").replace(/:/g,"");
        switch ( dateStrNumber.length ) {
        case 0 :
            return null;
        case 8 :
            return new Date( parseInt(dateStrNumber.substr(0,4), 10),
                                parseInt(dateStrNumber.substr(4,2), 10)-1,
                                parseInt(dateStrNumber.substr(6,2), 10),
                                0,0,0,0).getTime();
            break;
        case 14 :
            return new Date( parseInt(dateStrNumber.substr(0,4), 10),
                            parseInt(dateStrNumber.substr(4,2), 10)-1,
                            parseInt(dateStrNumber.substr(6,2), 10),

                            parseInt(dateStrNumber.substr(8,2), 10),
                            parseInt(dateStrNumber.substr(10,2), 10),
                            parseInt(dateStrNumber.substr(12,2), 10)   ).getTime();
            break;
        }
    };

    /**
    * 날짜객체 문자열 변환
    */
    window.getDateString = function(dateObj) {
        var year, mon, day;

        year = dateObj.getFullYear().toString();
        mon = (dateObj.getMonth() + 1).toString();
        day = dateObj.getDate().toString();

        if (mon.length == 1) mon = "0" + mon;
        if (day.length == 1) day = "0" + day;

        return year + DELIMITER_DATE + mon + DELIMITER_DATE + day;
    };

    /**
    * 입력일의 이전일 계산
    */
    window.getPrevDay = function(datestr, days) {
        if ( days == "0") return datestr;
        datestr = datestr.replace(/-/g, "");
        if (datestr == "") return "";
        var dateObj = window.getDateObject(datestr);

        dateObj.setDate(dateObj.getDate() - parseInt(days, 10));

        return window.getDateString(dateObj);
    };

    /**
    * 입력일의 다음일 계산
    */
    window.getNextDay = function(datestr, days) {
        if ( days == "0") return datestr;
        datestr = datestr.replace(/-/g, "");
        if (datestr == "") return "";
        var dateObj = window.getDateObject(datestr);

        dateObj.setDate(dateObj.getDate()+ parseInt(days, 10));

        return window.getDateString(dateObj);
    };

    /**
    * 해당월의 마지막날
    */
    window.getLastDay = function(datestr) {
        datestr = datestr.replace(/-/g, "");
        if (datestr == "") return "";
        var strNextMonth = window.addMonth(datestr, 1);
        strNextMonth = strNextMonth.replace(/-/g, "");
        datestr = strNextMonth.substring(0, 6) + '01';
        var strLastDay = window.addDate(datestr, -1);
        strLastDay = strLastDay.replace(/-/g, "");
        var dateObj = window.getDateObject(strLastDay);
        return dateObj.getDate();
    };

    /**
    * 년, 월, 일을 입력 받아 Date String으로 변환
    */
    window.getDatetime = function(nYear, nMonth, nDate)
    {
        var objDate = null;

        if (nYear.toString().trim().length >= 5) {
            var sDate    = new String(nYear);
            nYear    = sDate.substr(0,4);
            nMonth   = sDate.substr(4,2);
            nDate    = ((sDate.substr(6,2) == "") ? 1 : sDate.substr(6,2));
            var nHours   = ((sDate.substr(8,2) == "") ? 0 : sDate.substr(8,2));
            var nMinutes = ((sDate.substr(10,2) == "") ? 0 : sDate.substr(10,2));
            var nSeconds = ((sDate.substr(12,2) == "") ? 0 : sDate.substr(12,2));

            objDate = new Date(parseInt(nYear), parseInt(nMonth)-1, parseInt(nDate), parseInt(nHours), parseInt(nMinutes), parseInt(nSeconds));
        } else {
            objDate = new Date(parseInt(nYear), parseInt(nMonth)-1, parseInt(((nDate == null) ? 1 : nDate)));
        }

        var strYear  = objDate.getFullYear().toString();
        var strMonth = (objDate.getMonth() + 1).toString();
        var strDate  = objDate.getDate().toString();
        if (strMonth.length == 1)  strMonth  = "0" + strMonth;
        if (strDate.length == 1)   strDate   = "0" + strDate;
        return strYear + "-" + strMonth + "-" + strDate;
    };

    /**
    * 년 계산
    */
    window.addYear = function(date, nOffSet)
    {
        date = date.replace(/-/g, "");
        var nYear  = parseInt(date.substr(0, 4), 10) + nOffSet;
        var nMonth = parseInt(date.substr(4, 2), 10);
        var nDate  = parseInt(date.substr(6, 2), 10);
        return window.getDatetime(nYear, nMonth, nDate);
    };

    /**
    * 월 계산
    */
    window.addMonth = function(date, nOffSet)
    {
        date = date.replace(/-/g, "");
        var nYear  = parseInt(date.substr(0, 4), 10);
        var nMonth = parseInt(date.substr(4, 2), 10) + nOffSet;
        var nDate  = parseInt(date.substr(6, 2), 10);
        return window.getDatetime(nYear, nMonth, nDate);
    };

    /**
    * 일 계산
    */
    window.addDate = function(date, nOffSet)
    {
        date = date.replace(/-/g, "");
        var nYear  = parseInt(date.substr(0, 4), 10);
        var nMonth = parseInt(date.substr(4, 2), 10);
        var nDate  = parseInt(date.substr(6, 2), 10) + nOffSet;
        return window.getDatetime(nYear, nMonth, nDate);
    };

    /**
    * 이전해 계싼
    */
    window.getPrevYear = function(datestr, year) {
        if ( year == "0") return datestr;
        datestr = datestr.replace(/-/g, "");
        if (datestr == "") return "";
        var dateObj = window.getDateObject(datestr);

        dateObj.setYear(dateObj.getFullYear() - year);

        return window.getDateString(dateObj);
    };

    /**
    * 다음해 계산
    */
    window.getNextYear = function(datestr, year) {
        if ( year == "0") return datestr;
        datestr = datestr.replace(/-/g, "");
        if (datestr == "") return "";
        var dateObj = window.getDateObject(datestr);

        dateObj.setYear(dateObj.getFullYear() + year);

        return window.getDateString(dateObj);
    };

    /**
    * select 연도
    */
    window.setYearCombo = function(id,isAll,isSelect,range){
        var now = new Date();
        var dayYear = now.getFullYear();
        var str = "";
        if(range == null || range <= 0){
            range = 5;
        }
        if(isAll){
            str += "<option value=''>"+isAll+"</option>";
        }
        for(var year=dayYear-range; year<dayYear+range + 1 ; year++){
            str += "<option value='"+year+"'>"+year+"</option>";

        }

        $("#"+id).append(str);

        if(isSelect){
            $("#"+id).val(dayYear).attr("selected", "selected");
        }
    };


    /**
    * select 년월
    */
    window.setYearMonCombo = function(id,isAll,isSelect){
        var day = new Date();
        var dayYear = day.getFullYear().toString();
        var now = day.getMonth()+1;
        now = dayYear + (now < 10 ? "0" : "") + now;
        var month = "";
        var str = "";

        if(isAll){
            str += "<option value=''>"+isAll+"</option>";
        }
        for(var i = 1; i <13 ; i++){
            month = i < 10 ? "0" + i : "" + i;
            str +=  "<option value='"+dayYear +"" + month +"'>"+ dayYear + "/" + month + "</option>";
        }
        $("#"+id).append(str);
        if(isSelect){
            $("#"+id).val(now).attr("selected", "selected");
        }
    };

    /**
    * 문자열 날짜 숫자로 변경
    */
    window.stringDateToNum = function(paramStr){
        var str = paramStr.replace(/-/g, "");

        var dateNum = "";
        for (var i=0; i<str.length;i++ ) {
            if ((str.charAt(i) >= "0") && (str.charAt(i) <= "9"))
            dateNum = dateNum + str.charAt(i);
        }
        if (dateNum.length < 1) {
            alert(J.getMessage("cmm.warning.001"));
            return false;
        }
        //if ((dateNum.length != 6) && (dateNum.length != 8)) {
        if (dateNum.length != 8) {
            alert(J.getMessage("cmm.warning.001"));
            return false;
        }
        if (dateNum.length == 6) {
            if (parseInt(dateNum.substr(0, 2), 10) >= 50 )
                dateNum = "19" + dateNum;
            else
                dateNum = "20" + dateNum;
        }
        return dateNum;
    };

    /**
     * 두 일자간의 일수 계산
     * @param1 day1 : 일자 8자리 - 'YYYYMMDD', 'YYYY-MM-DD', 'YYYY/MM/DD', etc
     * @param2 day2 : 일자 8자리 - 'YYYYMMDD', 'YYYY-MM-DD', 'YYYY/MM/DD', etc
     * @return 두 일자간의 일수
     */
    window.calcDayCount = function(day1, day2) {
        day1 = day1.replace(/-/g, "");
        day2 = day2.replace(/-/g, "");
        if (day1 == "" || day2 == "") return "";
        var date1 = window.getDateObject(day1);
        var date2 = window.getDateObject(day2);

        if(date1 > date2) return ((date1.getTime() - date2.getTime()) / 1000 / 60 / 60 / 24) + 1;
        else              return ((date2.getTime() - date1.getTime()) / 1000 / 60 / 60 / 24) + 1;
    };

    /**
     * Get 요일
     */
    window.getWeek = function(datestr, isConvert){
        datestr = datestr.replace(/-/g, "");
        var dateObj = window.getDateObject(datestr);
        var week = dateObj.getDay();

        if(!isConvert)    return week;

        if(week == 0)           week = '일';
        else if(week == 1)     week = '월';
        else if(week == 2)     week = '화';
        else if(week == 3)     week = '수';
        else if(week == 4)     week = '목';
        else if(week == 5)     week = '금';
        else if(week == 6)     week = '토';
        return week;
    };

    /**
    * 코드/명 팝업 사용시 이벤트 Bind 처리
    * @param1 basicParam : 검색조건(SEARCH), 코드 Input(SOURCE), 명 Input id(TARGET), 검증용 Input id(VERIFY)의 JSON
    * @param2 addParam : 각 조건별 사용할 추가 조건의 JSON
    */
    window.cmmCodeInputEventBind = function(basicParam)
    {
        $('#' + basicParam.SOURCE).bind("keypress", function(event){
            window.cmmCodeKeyPress(event, basicParam);
        });
        /* 코드 삭제 또는 변경시 엔터 안쳐도 blur로 다시 조회 해야할지..
        $('#' + basicParam.SOURCE).bind("blur", function(event){
            if ($("#" + basicParam.SOURCE).val() == "") {
                if ( $("#" + basicParam.TARGET).hasClass("off") ) {
                    $("#" + basicParam.TARGET).val("");
                }
            } else
            if ($("#" + basicParam.SOURCE).val() != $("#" + basicParam.SOURCE).data("hidden") ) {
                window.cmmCodeKeyPress({keyCode:13}, basicParam);
            }
        });
        */
    };

  /**
   * 코드/명 팝업 사용시 코드 입력 후 엔터키 입력시 이름을 불러오도록함
   * @param event : event Event 항목으로 event라고 입력하면 됨
   * @param basicParam : 검색조건(SEARCH), 명 입력용 Input id(TARGET), 검증용 Input id(VERIFY)의 JSON
   * @param addParam : 각 조건별 사용할 추가 조건의 JSON
   *
   * 조회 성공시 처리해야할 로직이있다면 'window.afterLoadName' function 을 만들어 놓으면 호출이 됨
   * 필요 없을시 만들어 놓지 않으면됨
   */
    window.cmmCodeKeyPress = function(event, basicParam)
    {
        var code = {CODE_ID:$("#" + basicParam.SOURCE).val()};
        var argData = {};

        argData = $.extend({}, code, basicParam);

        if (event.keyCode == 13)
        {
            var targetUrl = "";
            switch(basicParam.SEARCH)
            {
                
                case ("USR") :
                    targetUrl = J.getCtx() + "/jwork/eam/user/findUserName.do";
                    break;
                case ("DEPT") :
                    targetUrl = J.getCtx() + "/biz/sys/code/sysGetDeptName.do";
                    break;
                
            }
            if (targetUrl != "")
            {
                $.ajax({
                     url        : targetUrl
                    ,type       : "post"
                    ,async      : false
                    ,data       : argData
                    ,dataType   : "json"
                    ,success : function(response) {
                        //$("#" + basicParam.SOURCE).data("hidden", code.CODE_ID); //onBlur
                        $("#" + basicParam.TARGET).val(response.CODE_NM);
                        if (basicParam.hasOwnProperty("VERIFY") && basicParam.VERIFY != "" && response.CODE_NM != null)
                            $("#" + basicParam.VERIFY).val($("#" + basicParam.SOURCE).val());
                        if (typeof window.afterLoadName == 'function')
                        {
                            window.afterLoadName(basicParam.SOURCE);
                        }
                    }
                    ,exception  : function(response) {
                        alert(/*response.code + " : " + */response.message);
                    }
                });
            }
        }
    };

    //-----------------------------------------------------------
    // 현재년월 구하기
    //-----------------------------------------------------------
    window.getYearMonth = function() {
        var today        = getToday();
        var yearMonth    = today.substr(0, 7);

        return yearMonth;
    };


    /**
    * Json Data 를 combo 로 변환
    * url : Action url, data : {param1,param2....}, id : combo id
    * <select id="id"></select>
    */
    window.dataCombo = function(url,data,id,selectValue){
        var comboList = null;
        $.ajax({
             url        : url
            ,type       : "post"
            ,async      : false
            ,data       : data
            ,dataType   : "json"
            ,exception  : function(response) {}
            ,success    : function(response) {
                comboList = response.data.jgrid_list;
            }
        });
        var str = "";
        $("#"+id).find("option").remove().end();
        for(var i = 0 ; i < comboList.length ; i++){
            str += "<option value='"+comboList[i].CODE + "'>"+ comboList[i].CODE_NM + "</option>";
        }
        $("#"+id).append(str);
        if(selectValue != null && selectValue != ""){
            $("#"+id).val(selectValue).attr("selected", "selected");
        }
    };

    /**
     * cmmClearForm(tagElementId)
     * tagElement의 하위 input tag 클리어 시 사용.
     * resetForm()은 input tag의 action 이전 값을 기억.
     * clearForm()은 input tag의 모든값을 nullString 로 세팅.
     * @param targetId : 초기화 하고자하는 상위 targetId
     */
    window.cmmClearForm = function (targetId) {
      var obj = $("#"+targetId);
        $(obj).find(':input').each(function() {
            switch(this.type) {
                case 'password':
                case 'select-multiple':
                case 'select-one':
                case 'text':
                case 'textarea':
                case 'hidden':
                    $(this).val('');
                    break;
                case 'checkbox':
                case 'radio':
                    this.checked = false;
            }
        });
      };

    /**
     * setTimeCombo(objectId, timeDiv)
     * objectI에 시간 또는 분 combobox 생성
     * timeDiv가 'H'이면 시간, 'M'이면 분
     * @param id : 콤보박스를 생성할 element ID
     * @param timeDiv : 'H'-시간, 'M'-분
     */
    window.setTimeCombo = function(id, timeDiv){
        var hh;

        if(timeDiv == 'H'){
            for (var i=0; i<24; i++){

                if(i < 10)    hh = '0'+i;
                else          hh = i+'';

                var option = document.createElement('option');
                var txt = document.createTextNode(hh);
                option.setAttribute("value", hh);
                option.appendChild(txt);
                 $("#"+id).append(option);
            }

        }else{
            for (var i=0; i<60; i++){

                if(i < 10)    hh = '0'+i;
                else          hh = i+'';

                var option = document.createElement('option');
                var txt = document.createTextNode(hh);
                option.setAttribute("value", hh);
                option.appendChild(txt);
                 $("#"+id).append(option);
            }
        }
    };
    
    /**
     * setWorkTimeCombo(objectId, timeDiv)
     * objectI에 시간 또는 분 combobox 생성
     * timeDiv가 'H'이면 시간, 'M'이면 분
     * @param id : 콤보박스를 생성할 element ID
     * @param timeDiv : 'H'-시간, 'M'-분
     */
    window.setWorkTimeCombo = function(id, timeDiv){
        var hh;

        if(timeDiv == 'H'){
            for (var i=9; i<19; i++){

                if(i < 10)    hh = '0'+i;
                else          hh = i+'';

                var option = document.createElement('option');
                var txt = document.createTextNode(hh);
                option.setAttribute("value", hh);
                option.appendChild(txt);
                 $("#"+id).append(option);
            }

        }else{
            for (var i=0; i<60; ){
                if(i<10)    hh = '0'+i;
                else         hh = i+'';

                var option = document.createElement('option');
                var txt = document.createTextNode(hh);
                option.setAttribute("value", hh);
                option.appendChild(txt);
                 $("#"+id).append(option);
                 i += 10;
            }
        }
    };
    
    window.s4 = function() {
        return Math.floor((1 + Math.random()) * 0x10000)
        .toString(16)
        .substring(1);
    };
    
    window.guid = function(sepStr) {
        if(sepStr == undefined){
            sepStr = '';
        }
        return s4() + s4() + sepStr + s4() + sepStr + s4() + sepStr +
        s4() + sepStr + s4() + s4() + s4();
    };

    var type01Obj = $('\
            <div class="name-sel" style="margin-right:3px">\
                <span name="cntnt"></span>\
                <input type="hidden" name="seq" value=""/>\
                <input type="hidden" name="cd"  value=""/>\
                <span class="close" title="닫기" style="width: 8px; height: 8px;"></span>\
            </div>\
        ');

    var type02Obj = $('\
            <div class="top-blank-3">\
                <input type="hidden" name="seq"   value=""/>\
                <input type="text"   name="cd"    value="" class="size-20p"/>\
                <input type="text"   name="cntnt" value="" class="size-70p"/>\
                <a href="#" name="searchPop"><img src="'+ctx+'/resources/theme/cjlp/img/contents/ico-sch.gif" class="img_ic" align="absmiddle" alt="검색제어" /></a>\
                <a href="#" name="btnAddRmv" class="padding-left-10 close"><img src="'+ctx+'/resources/theme/cjlp/img/contents/ico-minus.gif" align="absmiddle" alt="삭제" /></a>\
            </div>\
        ');

    /**
     * DivBox를 추가한다.
     *
     * @param   {String} DivBox Type.(01|02)
     * @param   {String|jQuery Object} DivBox가 생성될 위치의 DIV객체 또는 객체의ID.
     * @param   {String|jQuery Object} 삭제된 DivBox의 코드값을 저장할 Hidden객체 또는 객체의 ID.
     * @param   {Object} DivBox에 들어갈 Data
     * @param   {Object} DivBox에 들어갈 Event Function
     */
    window.addDivBox = function(type, divCmpnt, delCmpnt, data, functions) {
        var $divCmpnt = null;
        var $delCmpnt = null;
        
        switch( $.type(divCmpnt) ) {
            case 'string' : $divCmpnt = $("#" + divCmpnt); break;
            case 'object' : $divCmpnt = (divCmpnt instanceof $) ? divCmpnt : null;
        }
        
        switch( $.type(delCmpnt) ) {
            case 'string' : $delCmpnt = $("#" + delCmpnt); break;
            case 'object' : $delCmpnt = (delCmpnt instanceof $) ? delCmpnt : null;
        }

        // divCmpnt element가 없을 경우 에러 메시지
        if(!$.type($divCmpnt) || !$divCmpnt.length) {
            J.core.Error.throwError("[DivBox] 해당하는 HTML Div Element가 없습니다.");
            return;
        }

        // delCmpnt element가 없을 경우 에러 메시지
        if(!$.type($delCmpnt) || !$delCmpnt.length) {
            J.core.Error.throwError("[DivBox] 해당하는 HTML Delete Element가 없습니다.");
            return;
        }

        var divClone = null;
        
        if(type == "01") {
            var seq   = (data.seq   == undefined) ? "" : data.seq;
            var cd    = (data.cd    == undefined) ? "" : data.cd;
            var cntnt = (data.cntnt == undefined) ? "" : data.cntnt;
            
            if(cntnt == null || cntnt == "") return;
            
            divClone = $(type01Obj).clone();
            
            $(divClone).find("[name=seq]"  ).val (seq  );
            $(divClone).find("[name=cd]"   ).val (cd   );
            $(divClone).find("[name=cntnt]").html(cntnt);
        } else if(type == "02") {
            var seq   = (data.seq   == undefined) ? "" : data.seq;
            var cd    = (data.cd    == undefined) ? "" : data.cd;
            var cntnt = (data.cntnt == undefined) ? "" : data.cntnt;
            
            divClone = $(type02Obj).clone();
    
            $(divClone).find("[name=seq]"  ).val(seq  );
            $(divClone).find("[name=cd]"   ).val(cd   );
            $(divClone).find("[name=cntnt]").val(cntnt);

            if($divCmpnt.find("[name=btnAddRmv]").length == 0) {
                $(divClone).find("[name=btnAddRmv]").find("img").attr("src", ctx + "/resources/theme/cjlp/img/contents/ico-plus.gif");
                $(divClone).find("[name=btnAddRmv]").find("img").attr("alt", "추가");
                $(divClone).find("[name=btnAddRmv]").removeClass("close");
                
                $(divClone).find("[name=btnAddRmv]").click(function() {
                    addDivBox("02", $divCmpnt, $delCmpnt, {}, functions);
                });
            };

            $(divClone).find("[name=searchPop]"                 ).bind("click" , functions.pop);
            $(divClone).find("input[name=cd], input[name=cntnt]").bind("change", functions.chng);
        }
        
        // 삭제버튼 클릭
        $(divClone).find(".close").click(function() {
            var seq    = $(this).parent().find("[name=seq]").val();
            var delSeq = $delCmpnt.val();
            
            if(seq != "") {
                delSeq = (delSeq == "") ? seq : delSeq + "|" + seq; 
            }
            
            $delCmpnt.val(delSeq);
            $(divClone).remove();
        });
        
        $divCmpnt.append(divClone);
    };

    /**
     * DivBox를 추가한다.
     *
     * @param   {String} DivBox Type.(01|02)
     * @param   {String|jQuery Object} DivBox가 생성될 위치의 DIV객체 또는 객체의ID.
     * @param   {String|jQuery Object} 삭제된 DivBox의 코드값을 저장할 Hidden객체 또는 객체의 ID.
     * @param   {String} DivBox에 들어갈 전체 Data
     * @param   {Object} DivBox에 들어갈 Event Function
     */
    window.addAllDivBox = function(type, divCmpnt, delCmpnt, allData, functions) {
        var rowData = allData.split("|");
        
        for(var i=0 ; i<rowData.length ; i++) {
            var arrData = rowData[i].split("^");
            var data    = null;
            
            if(type == "01" || type == "02") {
                data = {
                        seq   : arrData[0]
                       ,cd    : arrData[1]
                       ,cntnt : arrData[2]
                };
            }
            
            addDivBox(type, divCmpnt, delCmpnt, data, functions);
        }
    };
    
    /**
     * DivBox의 내용을 구분자값 기준으로 String형태로 반환.
     *
     * @param   {String|jQuery Object} DivBox가 위치한 DIV객체 또는 객체의ID.
     * @param   {String} 삭제된 코드값의 구분자값(Default='|')
     * @returns {String} DivBox 내용.
     */
    window.getDivBoxToString = function(type, divCmpnt, rtnData) {
        var $divCmpnt = null;
        
        switch( $.type(divCmpnt) ) {
            case 'string' : $divCmpnt = $("#" + divCmpnt); break;
            case 'object' : $divCmpnt = (divCmpnt instanceof $) ? divCmpnt : null;
        }
        
        // divCmpnt element가 없을 경우 에러 메시지
        if(!$.type($divCmpnt) || !$divCmpnt.length) {
            J.core.Error.throwError("[DivBox] 해당하는 HTML Div Element가 없습니다.");
            return;
        }
        
        var rtnValue = "";
        var findTxt  = "";
        
        if(type == "01") {
            findTxt = ".name-sel";
        } else if(type == "02") {
            findTxt = ".top-blank-3";
        }

        $divCmpnt.find(findTxt).each(function(index, item) {
            var boxValue = "";
            
            if(index > 0) {
                boxValue += "|";
            }
            for(var i=0 ; i<rtnData.length ; i++) {
                var obj = $(this).find("[name="+rtnData[i]+"]");
                
                if(i > 0) {
                    boxValue += "^";
                }
                
                if(obj.prop("tagName") == "SPAN") {
                    boxValue += obj.text();
                } else {
                    boxValue += obj.val();
                }
            }
            
            if(boxValue.replace(/[|^]/g, "") != "") {
                rtnValue += boxValue;
            }
        });
        
        return rtnValue;
    };
    //separator
    window.getSplitData = function(allData, format, rtnData, separator) {
        if(allData == undefined || allData == null || allData == "") return "";
        
        var rtnValue = "";
        var rowData  = allData.split("|");

        for(var i=0 ; i<rowData.length ; i++) {
            var arrData = rowData[i].split("^");
            
            if(i > 0) {
                rtnValue += separator;
            }
            
            var rowFormat = format;
            
            if(rtnData) {
                for(var index in rtnData) {
                    rowFormat = rowFormat.replace("{"+index+"}", arrData[rtnData[index]]);
                }
                
                rtnValue += rowFormat;
            }
        }
        
        return rtnValue;
    };
    
    window.getSplitData_old = function(allData, rtnData, separator, brFlag) {
    	if(allData == undefined || allData == null || allData == "") return "";
    	
    	var rtnValue = "";
    	var rowData  = allData.split("|");
    	
    	for(var i=0 ; i<rowData.length ; i++) {
    		var arrData = rowData[i].split("^");
    		
    		if(brFlag && i > 0) {
    			rtnValue += "<br/>";
    		}
    		
    		for(var j=0 ; j<rtnData.length ; j++) {
    			if(rtnData.length == 1 && i > 0) {
    				rtnValue += separator + arrData[rtnData[j]];
    			} else if(rtnData.length > 1 && j > 0) {
    				rtnValue += separator + arrData[rtnData[j]];
    			} else {
    				rtnValue += arrData[rtnData[j]];
    			}
    		}
    	}
    	
    	return rtnValue;
    };
});


/**
 * 문자열이 숫자형인지의 여부를 반환한다.
 * @param exceptChar - 추가 허용할 문자
 * @return 숫자형여부
 */
String.prototype.isNum = function(exceptChar) {
    return (/^[0-9]+$/).test(this.remove(exceptChar)) ? true : false;
};

/**
 * 문자열을 숫자형으로 캐스팅한다.
 * @return 캐스팅된 숫자
 */
String.prototype.toNum = function() {

    if(this.isNum()) {
        return Number(this);
    } else {
        return -1;
    }
};

/**
 * 문자열을 3자리마다 콤마를 찍어서 반환한다.
 * @return 콤마가 첨가된 문자열
 */
String.prototype.money = function() {

    var num = this.replace(/,/g, "");

    while((/(-?[0-9]+)([0-9]{3})/).test(num)) {
        num = num.replace((/(-?[0-9]+)([0-9]{3})/), "$1,$2");
    }

    return num;
};

/**
 * 문자열의 UTF8 byte 길이를 반환한다.
 * @return 문자열의 UTF8 byte 길이
 */
window.cmmGetUTF8ByteSize = function(str) {
    if ( str == null || str.length == 0 ) {
        return 0;
    }
    var size = 0;
    for ( var i=0, len=str.length ; i < len ;i++ ) {
        var charCode = str.charCodeAt(i),
            charSize = 0;
        //http://ko.wikipedia.org/wiki/UTF-8
        if ( charCode <= 0x00007F ) { //127
            charSize = 1;
        } else if ( charCode <= 0x0007FF) { //2047
            charSize = 2;
        } else if ( charCode <= 0x00FFFF) { //65535
            charSize = 3;
        } else {
            charSize = 4;
        }
        size += charSize;
    }
    return size;

};

/**
 * 문자열의 byte 길이를 반환한다.
 * @return 문자열의 byte 길이
 */
String.prototype.getByte = function() {
    return cmmGetUTF8ByteSize(this);
//    var cnt = 0;
//
//    for (var i = 0; i < this.length; i++) {
//        if (this.charCodeAt(i) > 127) {
//            cnt += 2;
//        } else {
//            cnt++;
//        }
//    }
//
//    return cnt;
};

/**
 * 문자열이 지정한 최소길이 이상인지의 여부를 반환한다.
 * @param minLen - 최소길이
 * @return 최소길이 이상인지의 여부
 */
String.prototype.isMin = function(minLen) {

    return this.length >= minLen;
};

/**
 * 문자열이 지정한 최대길이 이하인지의 여부를 반환한다.
 * @param maxLen - 최대길이
 * @return 최대길이 이하인지의 여부
 */
String.prototype.isMax = function(maxLen) {

    return this.length <= maxLen;
};

/**
 * 문자열이 지정한 최소바이트수 이상인지의 여부를 반환한다.
 * @param minByte - 최소바이트수
 * @return 최소바이트수 이상인지의 여부
 */
String.prototype.isMinByte = function(minByte) {

    return this.getByte() >= minByte;
};

/**
 * 문자열이 지정한 최대바이트수 이하인지의 여부를 반환한다.
 * @param maxByte - 최대바이트수
 * @return 최대바이트수 이하인지의 여부
 */
String.prototype.isMaxByte = function(maxByte) {

    return this.getByte() <= maxByte;
};

/**
 * 문자열 좌우 공백을 제거한다.
 * @return 좌우 공백 제거된 문자열
 */
String.prototype.trim = function() {

    return this.replace(/^\s+/g, '').replace(/\s+$/g, '');
};

/**
 * 문자열 좌 공백을 제거한다.
 * @return 좌 공백 제거된 문자열
 */
String.prototype.ltrim = function() {
    return this.replace(/(^\s*)/, "");
};

/**
 * 문자열 우 공백을 제거한다.
 * @return 우 공백 제거된 문자열
 */
String.prototype.rtrim = function() {
    return this.replace(/(\s*$)/, "");
};

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

/**
 * 문자열을 거꾸로 치환한다.
 * @return 거꾸로 치환된 문자열
 */
String.prototype.reverse = function() {

    var result = '';

    for(var i=this.length-1; i>-1; i--) {
        result += this.substring(i, i+1);
    }

    return result;
};

/**
 * 지정한 길이만큼 원본 문자열 왼쪽에 패딩문자열을 채운다.
 * @param len - 채울 길이
 * @param padStr - 채울 문자열
 * @return 채워진 문자열
 */
String.prototype.lpad = function(len, padStr) {

    var result = '';
    var loop = Number(len) - this.length;

    for(var i=0; i<loop; i++) {
        result += padStr.toString();
    }

    return result + this;
};

/**
 * 지정한 길이만큼 원본 문자열 오른쪽에 패딩문자열을 채운다.
 * @param len - 채울 길이
 * @param padStr - 채울 문자열
 * @return 채워진 문자열
 */
String.prototype.rpad = function(len, padStr) {

    var result = '';
    var loop = Number(len) - this.length;

    for(var i=0; i<loop; i++) {
        result += padStr.toString();
    }

    return this + result;
};

/**
 * 문자열이 공백이나 널인지의 여부를 반환한다.
 * @return 공백이나 널인지의 여부
 */
String.prototype.isBlank = function() {

    var str = this.trim();

    for(var i = 0; i < str.length; i++) {
        if ((str.charAt(i) != "\t") && (str.charAt(i) != "\n") && (str.charAt(i)!="\r")) {
            return false;
        }
    }

    return true;
};

/**
 * 문자열이 영어만으로 구성되어 있는지의 여부를 반환한다.
 * @param exceptChar - 추가 허용할 문자
 * @return 영어만으로 구성되어 있는지의 여부
 */
String.prototype.isEng = function(exceptChar) {

    return (/^[a-zA-Z]+$/).test(this.remove(exceptChar)) ? true : false;
};

/**
 * 문자열이 숫자와 영어만으로 구성되어 있는지의 여부를 반환한다.
 * @param exceptChar - 추가 허용할 문자
 * @return 숫자와 영어만으로 구성되어 있는지의 여부
 */
String.prototype.isEngNum = function(exceptChar) {

    return (/^[0-9a-zA-Z]+$/).test(this.remove(exceptChar)) ? true : false;
};

/**
 * 이메일 주소의 유효성 여부를 반환한다.
 * @return 유효성 여부
 */
String.prototype.isEmail = function() {
    //return (/\w+([-+.]\w+)*@\w+([-.]\w+)*\.[a-zA-Z]{2,4}$/).test(this.trim());
    return /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))$/i.test(this);
};

////////////////////////////// add start Jerry Yang ///////////////////////////////////////
//----------------------------------- 필드(String) 내용 관련 함수  -----------------------------------

/******************************
*  기능 :  문자열 Valid 검사처리 *
*  수정일 : 2002-01-25              *
*  parameter : string, space  *
*******************************/

window.CheckValid = function (String, space)
{

    var retvalue = false;

    for (var i=0; i<String.length; i++)
    {        //String이 0("" 이나 null)이면 무조건 false
        if (space == true)
        {
            if (String.charAt(i) == ' ')
            {            //String이 0이 아닐때 space가 있어야만 true(valid)
                retvalue = true;
                break;
            }
        } else {
            if (String.charAt(i) != ' ')
            {            //string이 0이 아닐때 space가 아닌 글자가 있어야만 true(valid)
                retvalue = true;
                break;
            }
        }
    }

    return retvalue;
};

/******************************
*  기능 :  Empty 및 공백 처리    *
*  수정일 : 2002-01-25           *
*  parameter : field, error_msg  *
*******************************/

window.isEmpty = function (field, error_msg)
{
    // error_msg가 ""이면 alert와 focusing을 하지 않는다
    if(error_msg == "") {
        if(!CheckValid(field.value, false))     {
            return true;
        } else {
            return false;
        }
    } else {
        if(!CheckValid(field.value, false)) {
            alert(error_msg);
            field.focus() ;
            return true;
        } else {
            return false;
        }
    }
};

window.isNotSet = function (field, error_msg)
{
    //for hidden field....
    if(field.value == "")
    {
        alert(error_msg);
        return true;
    } else
    {
        return false;
    }
};

window.haveSpace = function (field, error_msg)
{
    if(CheckValid(field.value, true))
    {
        alert(error_msg);
        field.focus();
        field.select();
        return true;
    }
    return false;
};


/******************************
*  기능 :  NumberCheck           *
*  수정일 : 2002-03-29(denial)   *
*  parameter : field, error_msg  *
*******************************/
window.isNotNumber = function (field, error_msg)
{
    var val = field.value;

    if(isNaN(val) ) {
        if(error_msg.length > 0) {
            alert(error_msg);
            field.focus();
            field.select();
        }
        return true;
    } else {
        return false;
    }
};

/******************************
*  기능 :  NumberCheck And Empty Check
*  수정일 : 2002-04-02(denial)
*  parameter : field, error_msg
*******************************/
window.isNotNumberOrEmpty = function (field, error_msg)
{
    var val = field.value;

    if(val.length == 0 || isNaN(val) ) {
        if(error_msg.length > 0) {
            alert(error_msg);
            field.focus();
            field.select();
        }
        return true;
    } else {
        return false;
    }
};

window.alertAndFocus = function (field, error_msg)
{
    alert(error_msg);
    field.focus();
    field.select();
};

/***************************************
*  기능 : String 알파벳과 숫자만~ Check  *
*  수정일 : 2002-01-25                           *
*  parameter : Form                              *
****************************************/
window.isNotAlphaNumeric =  function (field,error_msg)
{

    for (var i=0; i < field.value.length; i++)
    {
        if ( ( (field.value.charAt(i) < "0") || (field.value.charAt(i) > "9") ) &&
            ( ( (field.value.charAt(i) < "A") || (field.value.charAt(i) > "Z") ) &&
                ( (field.value.charAt(i) < "a") || (field.value.charAt(i) > "z") ) ) )
        {
            alert(error_msg);
            field.focus();
            field.select();
            return true;
        }
    }

    return false;
};

// 필드(String) 길이 관련
window.strLength = function (field)
{

    var Length = 0;

    var Nav = navigator.appName;
    var Ver = navigator.appVersion;

    var IsExplorer = false;

    var ch;

    if ( (Nav == 'Microsoft Internet Explorer') && (Ver.charAt(0) >= 4) )
    {
        IsExplorer = true;
    }

    if(IsExplorer)
    {

        for(var i = 0 ; i < field.value.length; i++)
        {

            ch = field.value.charAt(i);

            if ((ch == "\n") || ((ch >= "ㅏ") && (ch <= "히")) ||
                ((ch >="ㄱ") && (ch <="ㅎ")))
            {
                Length += 2;
            } else
            {
                Length += 1;
            }

        }

    }else {
        Length = field.value.length ;
    }

    return Length;
};

/****************************************
*  기능 : 문자열 길이제한                          *
*  수정일 : 2002-01-25                              *
*  parameter : field, min, max, error_msg  *
*****************************************/
window.isOutOfRange = function (field, min, max, error_msg)
{
    if(strLength(field) < min || strLength(field) > max)
    {
        alert(error_msg);
        field.focus();
        field.select();
        return true;
    }
    return false;
};

window.isNotExactLength = function (field, len, error_msg) {
    if(strLength(field) != len) {
        alert(error_msg);
        field.focus();
        field.select();
        return true;
    }
    return false;
};

window.isOutOfNumericRange = function (field, min, max, error_msg) {
    if(field.value < min || field.value > max) {
        alert(error_msg);
        field.focus();
        field.select();
        return true;
    }
    return false;
};
//---------------//


//------------------------------------- SELECT, CHECK BOX 관련 함수 -------------------------------
/****************************************
*  기능 :  Select Box 선택여부 검사            *
*  수정일 : 2002-01-25                              *
*  parameter : field, error_msg                  *
*****************************************/

window.isNotSelected = function (field, error_msg) {
    if(field.selectedIndex == 0) {
        alert(error_msg);
        field.focus() ;
        return true;
    } else {
        return false;
    }
};

/******************************
*  기능 :  Radio Button Check    *
*  수정일 : 2002-01-25              *
*  parameter : field, error_msg  *
*******************************/
window.isNotCheckedRadio =  function (field, error_msg) {
    if ( field == null ) {
        alert(error_msg);
        return true;
    }

    if ( field.length == null ) {
        if ( field.checked == true ) {
            return false;
        } else {
            alert(error_msg);
            return true;
        }
    }

    for(var i = 0; i < field.length; i++) {
        if(field[i].checked == true) {
            return false;
        }
    }
    alert(error_msg);
    return true;
};
//---------------//

/**
 * Radio Button을 선택해제한다
 */
window.uncheckRadio = function (field) {
    for(var i = 0; i < field.length; i++) {
        field[i].checked = false;
    }
};

/**
 * Radio Button의 선택된 값을 가져온다
 */
window.getRadioVal = function (field) {
    for(var i = 0; i < field.length; i++) {
        if(field[i].checked == true)
            return field[i].value;
    }
    return "";
};


window.isNotValidChar = function (field,error_msg) {

    var Count;
    var PermitChar = "0123456789-.";  // 허용가능한 문자들을 모두 기록한다.

    for (var i = 0; i < field.value.length; i++) {
        Count = 0;
        for (var j = 0; j < PermitChar.length; j++) {
            if(field.value.charAt(i) == PermitChar.charAt(j)) {
                Count++;
                break;
            }
        }

        if (Count == 0) {
            alert(error_msg);
            field.focus();
            field.select();
            return true;
            break;
        }
    }
    return false;
};

window.isNotValidChar2 = function (field,error_msg) {

    var NotPermitChar = "\"";  //허용되어서는 안되는 문자들을 모두 기록한다.

    if(field.value == "") return false;
    for (var i = 0; i < field.value.length; i++) {
        for (var j = 0; j < NotPermitChar.length; j++) {
            if(field.value.charAt(i) == NotPermitChar.charAt(j)) {
                ans = confirm(error_msg);
                if(ans == true) {
                    return false;
                } else {
                    field.focus();
                    field.select();
                    return true;
                }
            }
        }
    }
    return false;
};

window.auto_fill_birth = function (pid1) {

    var year = pid1.value.substr(0,2);
    var month = pid1.value.substr(2,2);
    var date = pid1.value.substr(4,2);
    document.forms[0].year.value = year;
    document.forms[0].month.value = month;
    document.forms[0].date.value = date;

};

window.hide_in = function (field) {
    if(field.value == field.defaultValue) field.value = "";
};

window.show_out = function (field) {
    if(field.value == "") field.value = field.defaultValue;
};




/******************************
*  기능 :  Positive NumberCheck           *
*  수정일 : 2002-04-10(withsun)              *
*  parameter : field, error_msg  *
*******************************/
function isNotPositiveNumber(field, error_msg)
{
    for (var i=0; i < field.value.length; i++)
    {
        if ( field.value.charAt(i) < "0" || field.value.charAt(i) > "9" )
        {
            alert(error_msg);
            field.focus();
            field.select();
            return true;
        }
    }
}


/**
 * ENTER키 다운 되었을때 넘겨받은 Function실행
 *
 * @param    func    실행할 Function명
 */
function enterKeyDown(func)
{
    enter = event.keyCode;
    if(enter == 13)
    {
        eval(func);
    }

}

/**
 * 특정키 다운 되었을때 넘겨받은 Function실행
 *
 * @param    func    실행할 Function명
 */
function keyDown(func)
{
    enter = event.keyCode;
    if(enter == 13)
    {
        eval(func);
    }

}


/**
 * 주언진 8자리 문자열을 날짜포맷(YYYY-MM-DD or YYYY/MM/DD)로 바꾸어준다.
 *
 * @param    source        변환할 8자리 날짜문자열
 * @param    format        날짜형식
 * @return    ret            변환된 날짜 문자열
 **/
function dateFormat(source, format)
{
    ret = "";
    delimiter = "";

    if (format.indexOf("-") != -1)
        delimiter = "-";
    else if (format.indexOf("/") != -1)
        delimiter = "/";
    else
    {
        alert("입력된 날짜포맷이 잘못되었습니다.");
        return;
    }

    if (source.length == 8)
    {
        ret = source.substring(0, 4) + delimiter + source.substring(4, 6) + delimiter + source.substring(6, 8);
    } else if (source.length == 10)
    {
        ret = source.substring(0, 4) + delimiter + source.substring(5, 7) + delimiter + source.substring(8, 10);
    } else
    {
        alert("입력된 날짜형식이 잘못되었습니다.");
        return;
    }
    return ret;
}

/**
 * 날짜형식이 올바른지 검사
 *
 * @param    astrValue    날짜포맷(yyyymmdd, yyyy/mm/dd, yyyy-mm-dd)
 * @param    astrNotNull:    nn:not null, "": null 허용
 * @return    true/false
 **/
function blnOkDate(astrValue, astrNotNull)
{
    var arrDate;

    if (astrValue=='')
    {
        if (astrNotNull == "nn")
            return false;
        else
            return true;
    }else{
        if (astrValue.indexOf("-") != -1)
            arrDate = astrValue.split("-");
        else if (astrValue.indexOf("/") != -1)
            arrDate = astrValue.split("/");
        else
        {
            if (astrValue.length != 8) return false;
            astrValue = astrValue.substring(0,4)+"/"+astrValue.substring(4,6)+"/" +astrValue.substring(6,8);
            arrDate = astrValue.split("/");
        }

        if (arrDate.length != 3) return false;

        var chkDate = new Date(arrDate[0] + "/" + arrDate[1] + "/" + arrDate[2]);
        if (isNaN(chkDate) == true ||
            (arrDate[1] != chkDate.getMonth() + 1 || arrDate[2] != chkDate.getDate()))
        {
            return false;
        }
    }
    return true;
}



/**
 * INPUT field에서 날짜를 입력받고서 유효한지 체크(yyyymmdd or yyyy-mm-dd or yyyy/mm/dd)후 틀리면 Calendar Popup
 *
 * @param field INPUT 객체
 **/
function openCalendar(dateField)
{
    var obj = eval("document." + dateField);

    if (obj.value == "")
        return;
    if (!blnOkDate(obj.value, "nn"))
    {
        obj.value = "";
        showDateCalendar(dateField);
    }
    else
        obj.value = dateFormat(obj.value, "YYYY-MM-DD");

}

/**
 * 문자열내에 있는 ', "를 \', \" 로변환한다.
 *
 * @param    str    변환할 문자열
 **/
function toValidStr(str)
{
    re1 = /\'/gi;
    re2 = /\"/gi;
    str = str.replace(re1, "\\\'");
    str = str.replace(re2, "\\\"");
    return str;
}

function encChar(str)
{
    var temp1 = "@@@@@";
    re1 = /\'/g;
    re2 = /\"/g;
    str = str.replace(re1, temp1);
    return str;
}

function decChar(str)
{
    re3 = /@@@@@/g;
    str = str.replace(re3, "'");
    return str;
}


// system의 현재날짜를 return: yyyymmdd
function strGetToDay()
{
    var today=new Date();

    var strToDay = today.getYear();

    if (today.getMonth()+1 < 10)
        strToDay += "-0"+(today.getMonth()+1);
    else
        strToDay += "-" + today.getMonth()+1;

    if (today.getDate() < 10)
        strToDay += "-0"+today.getDate();
    else
        strToDay += "-" + today.getDate();

    return strToDay;
}

//주어진 값(val)을 소수점이하 num자리수에서 반올림한값을 리턴한다.
function round(val, num)
{
    val = val * Math.pow(10, num - 1);
    val = Math.round(val);
    val = val / Math.pow(10, num - 1);
    return val;
}

function isVaildMail(email)
{
    var result = false;

    if( email.indexOf("@") != -1 )
    {
        result = true;

        if( email.indexOf(".") != -1 )
        {
            result = true;
        }
        else
        {
            result = false;
        }
    }
    return result;
}

function isLoginname(obj) {
    len = obj.value.length;
    ret = true;

    if (len < 4)
        return false;
    if(!((obj.value.charAt(0) >= "a" && obj.value.charAt(0) <= "z") ||
            (obj.value.charAt(0) >= "A" && obj.value.charAt(0) <= "Z")))
        ret = false;
    for (var i = 1; i < len; i++) {
        if((obj.value.charAt(i) >= "0" && obj.value.charAt(i) <="9") ||
            (obj.value.charAt(i) >= "a" && obj.value.charAt(i) <= "z") ||
            (obj.value.charAt(i) >= "A" && obj.value.charAt(i) <= "Z"))
            ;
        else
            ret = false;
    }
    return ret;

}

//특정 필드값에 대해서 끝자리를 10단위로  전환
function roundValue(field) {

    field.value = Math.round(eval(field.value)/10) * 10;

}


/**
 * TAB키 다운 되었을때 넘겨받은 Function실행
 *
 * @param    func    실행할 Function명
 */
function tabKeyDown(func)
{
    enter = event.keyCode;
    if(enter == 09)
    {
        eval(func);
    }

}

///////////////////// 2002.07.03 추가 ///////////////////
function trim(str) {
    var count = str.length;
    var len = count;
    var st = 0;
    while ((st < len) && (str.charAt(st) <= ' ')) {
        st++;
    }
    while ((st < len) && (str.charAt(len - 1) <= ' ')) {
        len--;
    }
    return ((st > 0) || (len < count)) ? str.substring(st, len) : str ;
}

/////////////////////////MENU Botton 처리 ///////////////////////


///////////////////////////////////////////////////////////////////
// Function name    : connectMenuEvents
// Description        :
// Return type        : function
///////////////////////////////////////////////////////////////////
function connectMenuEvents()
{
    window.document.onmousedown = onMouseDown;
    window.document.onmousemove = onMouseMove;
    window.document.onclick = onClick;
    window.document.onunload = onCloseDocument;

    if (ie55)
    {
        oPopup = window.createPopup();
    }
}

///////////////////////////////////////////////////////////////////
// Function name    : setClassName
// Description        : sets the class name safely on both browsers
// Return type        : void
// Argument         : theEl
// Argument         : name
///////////////////////////////////////////////////////////////////
function setClassName(theEl, name)
{
    if (theEl)
    {
        theEl.setAttribute(document.all ? 'className' : 'class', name);
    }
}


///////////////////////////////////////////////////////////////////
// Function name    : getClassName
// Description        : returns the class name for an html element
// Return type        : string
// Argument         : theEl
///////////////////////////////////////////////////////////////////
function getClassName(theEl)
{
    if (theEl)
    {
        return theEl.getAttribute(document.all ? 'className' : 'class');
    }
    else
    {
        return ('class');
    }
}

//--- end cmd button handlers
///////////////////////////////////////////////////////////////////



/**
 * 문자열에서 특정문자 제외하여 문자열 조합
 */
function delChar(newValue, ch) {
    var len = newValue.length;
    var ret = "";

    for (var i=0; i<len; ++i) {
        if (newValue.substring(i,i+1) != ch) {
            ret = ret + newValue.substring(i,i+1);
        }
    }

    return ret;
}


/**
 * 날짜형식체크(방법)
 **/
function blnOkDate(astrValue, astrNotNull) {
    var arrDate;

    if (astrValue=='')
    {
        if (astrNotNull == "nn")
            return false;
        else
            return true;
    }else{
        if(astrValue.length == 10) {
            if (astrValue.indexOf("-") != -1){
                arrDate = astrValue.split("-");
            }else{
                return false;
            }

            if (arrDate.length != 3) return false;

            var chkDate = new Date(arrDate[0] + "/" + arrDate[1] + "/" + arrDate[2]);

            if (isNaN(chkDate) == true || (arrDate[1] != chkDate.getMonth() + 1 || arrDate[2] != chkDate.getDate())) {
                return false;
            } else {
                return return_date = astrValue;
            }
        } else if (astrValue.length == 8) {

            var chkDate = new Date(astrValue.substring(0,4) + "/" + astrValue.substring(4,6) + "/" + astrValue.substring(6));

            if (isNaN(chkDate) == true || (astrValue.substring(4,6) != chkDate.getMonth() + 1 || astrValue.substring(6) != chkDate.getDate())) {
                return false;
            } else {
                return return_date = astrValue.substring(0,4) + "-" + astrValue.substring(4,6) + "-" + astrValue.substring(6);
            }
        } else {
            return false;
        }
    }
}

/**
* 'YYYY-mm-dd' 문자타입을 Date타입으로 변환
* new Date(year,month,day) 로 date형식으로 변환        *
**/
function stringToDateType(dateValue) {
    if(dateValue.length != 10){
        return false;
    }
    var returnDate = new Date(dateValue.substr(0,4),dateValue.substr(5,2)-1, dateValue.substr(8,2));
    return returnDate;
}

/**
 * 권리사 조회를 위한 공통팝업 호출 Function
 * CallBack Function으로 cmpnySelected Funtion이 필요하다.
 */
var cmmRghtsCmpnyPopup = function(codeCd,codeNm,rowId,popupFlag){
	window.cmmPopupOpen("/cjlp/cjlp/po/pup/poPupRghts.do",
            "cmmRghtsCmpnyPopup",
            {
                codeNm 		: codeCd
               ,codeCd 		: codeNm
               ,rowId 		: nvl(rowId,"")
               ,popupFlag 	: nvl(popupFlag,"")
            }
    );
};

/**
 * 곡검색을 위한 공통팝업 호출 Funtion
 * CallBack Function으로 sngSelected Funtion이 필요하다.
 */
var cmmSngPopup = function(codeNm,codeCd,rowId,popupFlag){
	window.cmmPopupOpen("/cjlp/cjlp/po/pup/poPupSng.do",
            "cmmSngPopup",
            {
                codeNm 		: codeNm
               ,codeCd 		: codeCd
               ,rowId 		: nvl(rowId,"")
           	   ,popupFlag 	: nvl(popupFlag,"")
            }
    );
};

/**
 * 아티스트검색을 위한 공통팝업 호출 Funtion
 * CallBack Function으로 sngSelected Funtion이 필요하다.
 */
var cmmArtstPopup = function(codeNm,codeCd,rowId,popupFlag){
	window.cmmPopupOpen("/cjlp/cjlp/po/pup/poPupArtst.do",
            "cmmArtstPopup",
            {
                codeNm 		: codeNm
               ,codeCd 		: codeCd
               ,rowId 		: nvl(rowId,"")
           	   ,popupFlag 	: nvl(popupFlag,"")
            }
    );
};

/**
 * 앨범검색을 위한 공통팝업 호출 Funtion
 * CallBack Function으로 sngSelected Funtion이 필요하다.
 */
var cmmAbmPopup = function(codeNm,codeCd,rowId,popupFlag){
	window.cmmPopupOpen("/cjlp/cjlp/po/pup/poPupAbm.do",
            "cmmAbmtPopup",
            {
                codeNm 		: codeNm
               ,codeCd 		: codeCd
               ,rowId 		: nvl(rowId,"")
           	   ,popupFlag 	: nvl(popupFlag,"")
            }
    );
};

/**
 * 기획사검색을 위한 공통팝업 호출 Funtion
 * CallBack Function으로 sngSelected Funtion이 필요하다.
 */
var cmmTrdCmpnyPopup = function(codeNm,codeCd,rowId,popupFlag){
	window.cmmPopupOpen("/cjlp/cjlp/po/pup/poPupRghts.do",
            "cmmAbmtPopup",
            {
                codeNm 		: codeNm
               ,codeCd 		: codeCd
               ,rowId 		: nvl(rowId,"")
           	   ,popupFlag 	: nvl(popupFlag,"")
            }
    );
};

/**
 * UCI검색을 위한 공통팝업 호출 Funtion
 * CallBack Function으로 uciSelected Funtion이 필요하다.
 */
var cmmUciPopup = function(codeNm,codeCd,rowId,popupFlag){
	window.cmmPopupOpen("/cjlp/cjlp/po/pup/poPupUciAbmCdLkup.do",
            "cmmUciPopup",
            {
                codeNm 		: codeNm
               ,codeCd 		: codeCd
               ,rowId 		: nvl(rowId,"")
           	   ,popupFlag 	: nvl(popupFlag,"")
            }
    );
};


/**
 * 아티스트 고화질(또는 방송포토) 이미지 등록을 위한 공통팝업 호출 Funtion
 * CallBack Function으로 cmmArtstHdImgInsPopupCallback Funtion이 필요하다.
 * @param param 파라미터
 * @param imgTyp 이미지 타입, 고화질 이미지와 방송포토 이미지를 구분
 */
var cmmArtstHdImgInsPopup = function(param, imgTyp) {
	window.cmmPopupOpen("/cjlp/cjlp/po/pup/poPupArtstHdImgRgst.do",
			"cmmArtstHdImgInsPopup",
			param
	);
};

/**
 * 이미지를 팝업으로 연다.
 * @param url	이미지 URL
 */
function openImgPopup(url) {
	var img = new Image();
	img.src = url;
	
	// jfile의 미리보기 URL을 사용할 때, <img>객체의 width, height가 항상 0임
	// :대안을 찾고있습니다.
	//	if ((img.width != 0) && (img.height != 0)) {
		var pop = window.open("", "windowName", "toolbar=no, scrollbars=no, resizable=no, top=200, left=200 ,width="+ img.width +", height="+ img.height);
		
		if (pop != null) {
			var html = "html";
			var body = "body";
			var htmlStr = "<html><head><title>이미지</title></head>";
			htmlStr += "<body leftmargin='0' topmargin='0' marginwidth='0' marginheight='0'>";
			htmlStr += "<a href='javascript:window.close()'><img src='"+ url +"' border='0' alt='이미지클릭:화면닫기'></a>";
			htmlStr += "</body></html>";
			
			pop.document.open();
			pop.document.write(htmlStr);
			pop.document.close();
		}

		if (pop != null) pop.focus();
			
		return pop;
	//}
};

/**
 * 서비스상세유형단위로 요율/단가 일괄 입력을 위한 공통팝업 호출 Funtion
 * CallBack Function으로 cmmSrvcDtlTypPopupCallback Funtion이 필요하다.
 */
var cmmSrvcDtlTypPopup = function(param) {
	window.cmmPopupOpen("/cjlp/cjlp/po/pup/poPupSrvcDtlTyp.do",
			"cmmSrvcDtlTypPopup",
			param
	);
};

/**
 * 정산그룹을 관리하는 팝업 호출 Funtion
 */
var cmmCalGrpMngPopup = function() {
	window.cmmPopupOpen("/cjlp/cjlp/po/pup/poPupCalGrpMng.do",
			"cmmCalGrpMngPopup"
	);
};

/**
 * 사용자검색을 위한 공통팝업 호출 Funtion
 * CallBack Function으로 usrSelected Funtion이 필요하다.
 */
var cmmUsrPopup = function(codeNm,codeCd,rowId,popupFlag){
    window.cmmPopupOpen("/cjlp/cjlp/po/pup/poPupUsr.do",
            "cmmUsrPopup",
            {
                codeNm      : codeNm
               ,codeCd      : codeCd
               ,rowId       : nvl(rowId,"")
               ,popupFlag   : nvl(popupFlag,"")
            }
    );
};


//////////////////////////////////////////////////////////////////////////////////
//////////////////////////////add end  Jerry Yang ///////////////////////////////////////
