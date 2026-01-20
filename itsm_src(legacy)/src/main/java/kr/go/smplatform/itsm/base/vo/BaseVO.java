package kr.go.smplatform.itsm.base.vo;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.validator.GenericValidator;

import kr.go.smplatform.itsm.config.ITSMDefine;


/**
 * @Class Name : SampleDefaultVO.java
 * @Description : SampleDefaultVO Class
 * @Modification Information
 * @
 * @  수정일      수정자              수정내용
 * @ ---------   ---------   -------------------------------
 * @ 2009.03.16           최초생성
 *
 * @author 개발프레임웍크 실행환경 개발팀
 * @since 2009. 03.16
 * @version 1.0
 * @see
 *
 *  Copyright (C) by MOPAS All right reserved.
 */
public class BaseVO implements Serializable {
	private String rowNum;
	/** 검색조건 */
	private String searchCondition = "";

	/** 검색Keyword */
	private String searchKeyword = "";

	/** 검색사용여부 */
	private String searchUseYn = "";

	/** 현재페이지 */
	private int pageIndex = 1;

	/** 페이지갯수 */
	private int pageUnit = 10;

	/** 페이지사이즈 */
	private int pageSize = 10;

	/** firstIndex */
	private int firstIndex = 1;

	/** lastIndex */
	private int lastIndex = 1;

	/** recordCountPerPage */
	private int recordCountPerPage = 15;
	
	//중복등록 방지를 위해 추가
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
    
    public static final String DECORATOR_MAIN="main";
	public static final String DECORATOR_POPUP="popup";
	
	protected String decorator = DECORATOR_MAIN;
	
	//목록 복귀시 활용
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
		if(creatDt != null){
			createDtDisplay = new SimpleDateFormat(ITSMDefine.DATE_FORMAT).format(creatDt);
		}
		return createDtDisplay;
	}

	public void setCreateDtDisplay(String createDtDisplay) {
		this.createDtDisplay = createDtDisplay;
		if(!GenericValidator.isBlankOrNull(this.createDtDisplay)){
			try {
				creatDt = new SimpleDateFormat(ITSMDefine.DATE_FORMAT).parse(createDtDisplay);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public String getUpdtDtDisplay() {
		if(updtDt != null){
			updtDtDisplay = new SimpleDateFormat(ITSMDefine.DATE_FORMAT).format(updtDt);
		}
		return updtDtDisplay;
	}

	public void setUpdtDtDisplay(String updtDtDisplay) {
		this.updtDtDisplay = updtDtDisplay;
		if(!GenericValidator.isBlankOrNull(this.updtDtDisplay)){
			try {
				creatDt = new SimpleDateFormat(ITSMDefine.DATE_FORMAT).parse(updtDtDisplay);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
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
