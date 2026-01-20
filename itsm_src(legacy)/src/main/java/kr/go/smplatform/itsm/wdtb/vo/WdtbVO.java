package kr.go.smplatform.itsm.wdtb.vo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kr.go.smplatform.itsm.config.ITSMDefine;
import kr.go.smplatform.itsm.funcimprvm.vo.FuncImprvmVO;

public class WdtbVO extends FuncImprvmVO {
	/** 배포확인서 번호*/
	private String wdtbCnfirmNo;
	/** 최종배포일*/
	private Date wdtbDt;
	private String wdtbDtDateDisplay;
//	private String wdtbDtTimeDisplay;
	/** 작업사유*/
	private String opertReason;
	/** 개선 사항*/
	private String imprvmMatter;
	/** 배포구분*/
	private String wdtbSe;
	/** 배포IP*/
	private String wdtbIp;
	/** 배포번호(운영1,2)*/
	private String wdtbNoOne;
	private String wdtbNoTwo;
	/** 배포일(운영1,2)*/
	private Date wdtbDtOne;
	private String wdtbDtOneDateDisplay;
	private String wdtbDtOneTimeDisplay;
	private Date wdtbDtTwo;
	private String wdtbDtTwoDateDisplay;
	private String wdtbDtTwoTimeDisplay;
	/** 배포횟수(운영1,2)*/
	private String wdtbCoOne;
	private String wdtbCoTwo;
	/** 오류사유(운영1,2)*/
	private String errorReasonOne;
	private String errorReasonTwo;
	/**기타사항*/
	private String wdtbEtc;
	/**특이사항*/
	private String partclrMatter;
	/**솔루션접속확인파일ID*/
	private String solutConectflId;
	/**작업결과파일ID*/
	private String opertResultflId;
	/**로그인결과파일ID*/
	private String loginResultflId;
	/**서버1로그파일ID*/
	private String serverOneLogflId;
	/**서버2로그파일ID*/
	private String serverTwoLogflId;
	/**네비게이션*/
	private String wdtbNavigation;
	
	
	public String getWdtbNavigation() {
		return wdtbNavigation;
	}
	public void setWdtbNavigation(String wdtbNavigation) {
		this.wdtbNavigation = wdtbNavigation;
	}
	public String getWdtbCnfirmNo() {
		return wdtbCnfirmNo;
	}
	public void setWdtbCnfirmNo(String wdtbCnfirmNo) {
		this.wdtbCnfirmNo = wdtbCnfirmNo;
	}
	public Date getWdtbDt() {
		return wdtbDt;
	}
	public void setWdtbDt(Date wdtbDt) {
		this.wdtbDt = wdtbDt;
	}
	public String getWdtbDtDateDisplay() {
		if(wdtbDt != null){
			wdtbDtDateDisplay = new SimpleDateFormat(ITSMDefine.DATE_FORMAT).format(wdtbDt);
		}
		return wdtbDtDateDisplay;
	}
	public void setWdtbDtDateDisplay(String wdtbDtDateDisplay) {
		this.wdtbDtDateDisplay = wdtbDtDateDisplay;
	}
	public void makeWdtbDt(){
		String dateString = this.getWdtbDtDateDisplay();
		
		try {
			this.wdtbDt = new SimpleDateFormat(ITSMDefine.DATE_FORMAT).parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getOpertReason() {
		return opertReason;
	}
	public void setOpertReason(String opertReason) {
		this.opertReason = opertReason;
	}
	public String getImprvmMatter() {
		return imprvmMatter;
	}
	public void setImprvmMatter(String imprvmMatter) {
		this.imprvmMatter = imprvmMatter;
	}
	public String getWdtbSe() {
		return wdtbSe;
	}
	public void setWdtbSe(String wdtbSe) {
		this.wdtbSe = wdtbSe;
	}
	public String getWdtbIp() {
		return wdtbIp;
	}
	public void setWdtbIp(String wdtbIp) {
		this.wdtbIp = wdtbIp;
	}
	public String getWdtbNoOne() {
		return wdtbNoOne;
	}
	public void setWdtbNoOne(String wdtbNoOne) {
		this.wdtbNoOne = wdtbNoOne;
	}
	public String getWdtbNoTwo() {
		return wdtbNoTwo;
	}
	public void setWdtbNoTwo(String wdtbNoTwo) {
		this.wdtbNoTwo = wdtbNoTwo;
	}
	public Date getWdtbDtOne() {
		return wdtbDtOne;
	}
	public void setWdtbDtOne(Date wdtbDtOne) {
		this.wdtbDtOne = wdtbDtOne;
	}
	public String getWdtbDtOneDateDisplay() {
		if(wdtbDtOne != null){
			wdtbDtOneDateDisplay = new SimpleDateFormat(ITSMDefine.DATE_FORMAT).format(wdtbDtOne);
		}
		return wdtbDtOneDateDisplay;
	}
	public void setWdtbDtOneDateDisplay(String wdtbDtOneDateDisplay) {
		this.wdtbDtOneDateDisplay = wdtbDtOneDateDisplay;
	}
	public String getWdtbDtOneTimeDisplay() {
		if(wdtbDtOne != null){
			wdtbDtOneTimeDisplay = new SimpleDateFormat(ITSMDefine.DATE_HH_MM_FORMAT).format(wdtbDtOne);
		}
		return wdtbDtOneTimeDisplay;
	}
	public void setWdtbDtOneTimeDisplay(String wdtbDtOneTimeDisplay) {
		this.wdtbDtOneTimeDisplay = wdtbDtOneTimeDisplay;
	}
	public void makeWdtbDtOne(){
		String dateString = this.getWdtbDtOneDateDisplay();
		dateString += " ";
		dateString += this.getWdtbDtOneTimeDisplay();
		
		try {
			this.wdtbDtOne = new SimpleDateFormat(ITSMDefine.DATE_TIME_FORMAT).parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Date getWdtbDtTwo() {
		return wdtbDtTwo;
	}
	public void setWdtbDtTwo(Date wdtbDtTwo) {
		this.wdtbDtTwo = wdtbDtTwo;
	}
	public String getWdtbDtTwoDateDisplay() {
		if(wdtbDtTwo != null){
			wdtbDtTwoDateDisplay = new SimpleDateFormat(ITSMDefine.DATE_FORMAT).format(wdtbDtTwo);
		}
		return wdtbDtTwoDateDisplay;
	}
	public void setWdtbDtTwoDateDisplay(String wdtbDtTwoDateDisplay) {
		this.wdtbDtTwoDateDisplay = wdtbDtTwoDateDisplay;
	}
	public String getWdtbDtTwoTimeDisplay() {
		if(wdtbDtTwo != null){
			wdtbDtTwoTimeDisplay = new SimpleDateFormat(ITSMDefine.DATE_HH_MM_FORMAT).format(wdtbDtTwo);
		}
		return wdtbDtTwoTimeDisplay;
	}
	public void setWdtbDtTwoTimeDisplay(String wdtbDtTwoTimeDisplay) {
		this.wdtbDtTwoTimeDisplay = wdtbDtTwoTimeDisplay;
	}
	public void makeWdtbDtTwo(){
		String dateString = this.getWdtbDtTwoDateDisplay();
		dateString += " ";
		dateString += this.getWdtbDtTwoTimeDisplay();
		
		try {
			this.wdtbDtTwo = new SimpleDateFormat(ITSMDefine.DATE_TIME_FORMAT).parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getWdtbCoOne() {
		return wdtbCoOne;
	}
	public void setWdtbCoOne(String wdtbCoOne) {
		this.wdtbCoOne = wdtbCoOne;
	}
	public String getWdtbCoTwo() {
		return wdtbCoTwo;
	}
	public void setWdtbCoTwo(String wdtbCoTwo) {
		this.wdtbCoTwo = wdtbCoTwo;
	}
	public String getErrorReasonOne() {
		return errorReasonOne;
	}
	public void setErrorReasonOne(String errorReasonOne) {
		this.errorReasonOne = errorReasonOne;
	}
	public String getErrorReasonTwo() {
		return errorReasonTwo;
	}
	public void setErrorReasonTwo(String errorReasonTwo) {
		this.errorReasonTwo = errorReasonTwo;
	}
	public String getWdtbEtc() {
		return wdtbEtc;
	}
	public void setWdtbEtc(String wdtbEtc) {
		this.wdtbEtc = wdtbEtc;
	}
	public String getPartclrMatter() {
		return partclrMatter;
	}
	public void setPartclrMatter(String partclrMatter) {
		this.partclrMatter = partclrMatter;
	}
	public String getSolutConectflId() {
		return solutConectflId;
	}
	public void setSolutConectflId(String solutConectflId) {
		this.solutConectflId = solutConectflId;
	}
	public String getOpertResultflId() {
		return opertResultflId;
	}
	public void setOpertResultflId(String opertResultflId) {
		this.opertResultflId = opertResultflId;
	}
	public String getLoginResultflId() {
		return loginResultflId;
	}
	public void setLoginResultflId(String loginResultflId) {
		this.loginResultflId = loginResultflId;
	}
	public String getServerOneLogflId() {
		return serverOneLogflId;
	}
	public void setServerOneLogflId(String serverOneLogflId) {
		this.serverOneLogflId = serverOneLogflId;
	}
	public String getServerTwoLogflId() {
		return serverTwoLogflId;
	}
	public void setServerTwoLogflId(String serverTwoLogflId) {
		this.serverTwoLogflId = serverTwoLogflId;
	}
	
	// ----------------

	private String confirmUsr;

	@Override
	public String getConfirmUsr() {
		return confirmUsr;
	}

	@Override
	public void setConfirmUsr(String confirmUsr) {
		this.confirmUsr = confirmUsr;
	}
}
