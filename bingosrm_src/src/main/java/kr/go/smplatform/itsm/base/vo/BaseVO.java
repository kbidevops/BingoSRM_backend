package kr.go.smplatform.itsm.base.vo;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.validator.GenericValidator;

import kr.go.smplatform.itsm.config.ITSMDefine;

public class BaseVO implements Serializable {
    private String rowNum;
    private String searchCondition = "";
    private String searchKeyword = "";
    private String searchUseYn = "";
    private int pageIndex = 1;
    private int pageUnit = 10;
    private int pageSize = 10;
    private int firstIndex = 1;
    private int lastIndex = 1;
    private int recordCountPerPage = 15;
    private String saveToken;
    private Date creatDt;
    private String creatId;
    private String creatUserNm;
    private Date updtDt;
    private String updtId;
    private String updtUserNm;
    private String deleteYn = "N";
    private String createDtDisplay;
    private String updtDtDisplay;

    public static final String DECORATOR_MAIN = "main";
    public static final String DECORATOR_POPUP = "popup";

    protected String decorator = DECORATOR_MAIN;
    protected String returnListMode;

    public String getRowNum() {
        return rowNum;
    }

    public void setRowNum(String rowNum) {
        this.rowNum = rowNum;
    }

    public String getReturnListMode() {
        return returnListMode;
    }

    public void setReturnListMode(String returnListMode) {
        this.returnListMode = returnListMode;
    }

    public String getDECORATOR_MAIN() {
        return DECORATOR_MAIN;
    }

    public String getDECORATOR_POPUP() {
        return DECORATOR_POPUP;
    }

    public String getCreateDtDisplay() {
        if (creatDt != null) {
            createDtDisplay = new SimpleDateFormat(ITSMDefine.DATE_FORMAT).format(creatDt);
        }
        return createDtDisplay;
    }

    public void setCreateDtDisplay(String createDtDisplay) {
        this.createDtDisplay = createDtDisplay;
        if (!GenericValidator.isBlankOrNull(this.createDtDisplay)) {
            try {
                creatDt = new SimpleDateFormat(ITSMDefine.DATE_FORMAT).parse(createDtDisplay);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public String getUpdtDtDisplay() {
        if (updtDt != null) {
            updtDtDisplay = new SimpleDateFormat(ITSMDefine.DATE_FORMAT).format(updtDt);
        }
        return updtDtDisplay;
    }

    public void setUpdtDtDisplay(String updtDtDisplay) {
        this.updtDtDisplay = updtDtDisplay;
        if (!GenericValidator.isBlankOrNull(this.updtDtDisplay)) {
            try {
                creatDt = new SimpleDateFormat(ITSMDefine.DATE_FORMAT).parse(updtDtDisplay);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public Date getCreatDt() {
        return creatDt;
    }

    public void setCreatDt(Date creatDt) {
        this.creatDt = creatDt;
    }

    public String getCreatId() {
        return creatId;
    }

    public void setCreatId(String creatId) {
        this.creatId = creatId;
    }

    public Date getUpdtDt() {
        return updtDt;
    }

    public void setUpdtDt(Date updtDt) {
        this.updtDt = updtDt;
    }

    public String getUpdtId() {
        return updtId;
    }

    public void setUpdtId(String updtId) {
        this.updtId = updtId;
    }

    public String getDeleteYn() {
        return deleteYn;
    }

    public void setDeleteYn(String deleteYn) {
        this.deleteYn = deleteYn;
    }

    public String getSaveToken() {
        return saveToken;
    }

    public void setSaveToken(String saveToken) {
        this.saveToken = saveToken;
    }

    public int getFirstIndex() {
        return firstIndex;
    }

    public void setFirstIndex(int firstIndex) {
        this.firstIndex = firstIndex;
    }

    public int getLastIndex() {
        return lastIndex;
    }

    public void setLastIndex(int lastIndex) {
        this.lastIndex = lastIndex;
    }

    public int getRecordCountPerPage() {
        return recordCountPerPage;
    }

    public void setRecordCountPerPage(int recordCountPerPage) {
        this.recordCountPerPage = recordCountPerPage;
    }

    public String getSearchCondition() {
        return searchCondition;
    }

    public void setSearchCondition(String searchCondition) {
        this.searchCondition = searchCondition;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public String getSearchUseYn() {
        return searchUseYn;
    }

    public void setSearchUseYn(String searchUseYn) {
        this.searchUseYn = searchUseYn;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageUnit() {
        return pageUnit;
    }

    public void setPageUnit(int pageUnit) {
        this.pageUnit = pageUnit;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getCreatUserNm() {
        return creatUserNm;
    }

    public void setCreatUserNm(String creatUserNm) {
        this.creatUserNm = creatUserNm;
    }

    public String getUpdtUserNm() {
        return updtUserNm;
    }

    public void setUpdtUserNm(String updtUserNm) {
        this.updtUserNm = updtUserNm;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
