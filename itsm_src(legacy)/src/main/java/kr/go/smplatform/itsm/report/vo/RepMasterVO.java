package kr.go.smplatform.itsm.report.vo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.validator.GenericValidator;

import kr.go.smplatform.itsm.config.ITSMDefine;
import kr.go.smplatform.itsm.repAttd.vo.RepAttdVO;

public class RepMasterVO extends RepAttdVO {
	
	public static final String REP_TY_CODE_DAILY = "B001"; //일일보고서
	public static final String REP_TY_CODE_WEEKLY = "B002"; //주간보고서
	public static final String REP_TY_CODE_MONTHLY = "B003"; //월간보고서
	
	public static final String STTUS_CODE_WRITE = "B301"; //작성중
	public static final String STTUS_CODE_CONFIRMING = "B302"; //확정중
	public static final String STTUS_CODE_CONFIRM = "B303"; //확정
	public static final String STTUS_CODE_RETURN = "B304"; //반려
	public static final String DATE_FORMAT_YYMMDD = "yyMMdd";

	/** 보고서 순번 */
	private int repSn;
	/** 보고서 이름*/
	private String repNm;
	/** 보고서 구분*/
	private String repTyCode = REP_TY_CODE_DAILY;
	
	private String execAttdDj;
	private String execNmDj;
	private String planAttdDj;
	private String planNmDj;
	
	/** 확정 상태 코드*/
	private String sttusCode;
	/** 확정 상태명*/
	private String sttusNm;
	/** 확정 담당자 아이디*/
	private String confirmUsr;
	/** 반려 사유*/
	private String returnResn;
	private Date reportDt;
	private String reportDtDisplay;
	
	public String getReturnResn() {
		return returnResn;
	}
	public void setReturnResn(String returnResn) {
		this.returnResn = returnResn;
	}
	public String getExecNmDj() {
		return execNmDj;
	}
	public void setExecNmDj(String execNmDj) {
		this.execNmDj = execNmDj;
	}
	public String getSttusNm() {
		return sttusNm;
	}
	public void setSttusNm(String sttusNm) {
		this.sttusNm = sttusNm;
	}
	public String getExecAttdDj() {
		return execAttdDj;
	}
	public void setExecAttdDj(String execAttdDj) {
		this.execAttdDj = execAttdDj;
	}
	public String getPlanAttdDj() {
		return planAttdDj;
	}
	public void setPlanAttdDj(String planAttdDj) {
		this.planAttdDj = planAttdDj;
	}
	public String getPlanNmDj() {
		return planNmDj;
	}
	public void setPlanNmDj(String planNmDj) {
		this.planNmDj = planNmDj;
	}
	public String getConfirmUsr() {
		return confirmUsr;
	}
	public void setConfirmUsr(String confirmUsr) {
		this.confirmUsr = confirmUsr;
	}
	public String getSttusCode() {
		return sttusCode;
	}
	public void setSttusCode(String sttusCode) {
		this.sttusCode = sttusCode;
	}
	public int getRepSn() {
		return repSn;
	}
	public void setRepSn(int repSn) {
		this.repSn = repSn;
	}
	public String getRepNm() {
		return repNm;
	}
	public void setRepNm(String repNm) {
		this.repNm = repNm;
	}
	public String getRepTyCode() {
		return repTyCode;
	}
	public void setRepTyCode(String repTyCode) {
		this.repTyCode = repTyCode;
	}
	public Date getReportDt() {
		return reportDt;
	}
	public void setReportDt(Date reportDt) {
		this.reportDt = reportDt;
	}
	public String getReportDtDisplay() {
		if(reportDt != null){
			reportDtDisplay = new SimpleDateFormat(ITSMDefine.DATE_FORMAT).format(reportDt);
		}
		return reportDtDisplay;
	}
	public void setReportDtDisplay(String reportDtDisplay) {
		this.reportDtDisplay = reportDtDisplay;
		if(!GenericValidator.isBlankOrNull(this.reportDtDisplay)){
			try {
				reportDt = new SimpleDateFormat(ITSMDefine.DATE_FORMAT).parse(reportDtDisplay);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}