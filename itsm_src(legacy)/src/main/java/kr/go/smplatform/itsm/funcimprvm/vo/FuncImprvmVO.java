package kr.go.smplatform.itsm.funcimprvm.vo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kr.go.smplatform.itsm.base.vo.BaseVO;
import kr.go.smplatform.itsm.config.ITSMDefine;
import kr.go.smplatform.itsm.srvcrspons.vo.SrvcRsponsVO;

public class FuncImprvmVO extends SrvcRsponsVO {
//	private String fnctImprvmNo;
//	private String srvcRsponsNo;
	
	/** 타시스템 연계 유무*/
	private String conectSysYn;
	/** 연계 시스템*/
	private String conectSys;
	private String conectSysNm;
	/** 기능 개선 분류*/
	private String fiCl;
	private String fiClNm;
	/** 적용예정일*/
	private Date applyPlanDt;
	private String applyPlanDtDisplay;
	/** 배포예정일*/
	private Date applyRDt;
	private String applyRDtDisplay;
	/** 수용 여부*/
	private String cnfrmYn;
	/** 미수용 사유*/
	private String noCnfrmResn;
	
	/** 개선 관련시스템*/
	private String fiRunSvrYn = "N"; //운영서버
	private String fiDevSvrYn = "N"; //개발서버
	private String fiDbYn = "N"; //DB
	private String fiVmYn = "N"; //VM
	private String fiEtcYn = "N"; //기타
	
	/** 개선계획*/
	private String fiPlan;
	
	/** 필요 산출물 */
	private String rquireDfnYn = "N"; //요구사항정의서
	private String rquireSpcYn = "N"; //요구사항명세서
	private String rquireTrcYn = "N"; //요구사항추적표
	private String pckgProgrmListYn = "N"; //패키지 프로그램목록
	private String uiDsignYn = "N"; //UI설계서
	private String progrmDsignYn = "N"; //프로그램설계서
	private String tableDsignYn = "N"; //테이블설계서
	private String progrmListYn = "N"; //프로그램목록
	private String userMnualYn = "N"; //사용자메뉴얼
	private String admnMnualYn = "N"; //관리자메뉴얼
	private String unitTestYn = "N"; //단위테스트
	private String unionTestYn = "N"; //통합테스트
	
	/** 변경방안*/
	private String chngePlan;
	/** 백업방안*/
	private String backupPlan;
	/** 복원방안*/
	private String rstorePlan;
	/** 제약조건*/
	private String constrnt;
	/** 고려사항*/
	private String consder;
	/** 네비게이션*/
	private String navigation;
	/** 개선요구사항*/
	private String fiRquire;
	/** 개선내용(결과)*/
	private String fiCn;
	
	/** 첨부자료 ID*/
	private String fiAtchmnflId;
	private String asisAtchmnflId;
	private String tobeAtchmnflId;
	
	
	public String getFiClNm() {
		return fiClNm;
	}
	public void setFiClNm(String fiClNm) {
		this.fiClNm = fiClNm;
	}
	public String getConectSysNm() {
		return conectSysNm;
	}
	public void setConectSysNm(String conectSysNm) {
		this.conectSysNm = conectSysNm;
	}
	public String getConectSysYn() {
		return conectSysYn;
	}
	public void setConectSysYn(String conectSysYn) {
		this.conectSysYn = conectSysYn;
	}
	public String getConectSys() {
		return conectSys;
	}
	public void setConectSys(String conectSys) {
		this.conectSys = conectSys;
	}
	public String getFiCl() {
		return fiCl;
	}
	public void setFiCl(String fiCl) {
		this.fiCl = fiCl;
	}
	
	public void makeApplyPlanDt(){
		String dateString = this.getApplyPlanDtDisplay();
		
		try {
			this.applyPlanDt = new SimpleDateFormat(ITSMDefine.DATE_FORMAT).parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void makeApplyRDt(){
		String dateString = this.getApplyRDtDisplay();
		
		try {
			this.applyRDt = new SimpleDateFormat(ITSMDefine.DATE_FORMAT).parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Date getApplyPlanDt() {
		return applyPlanDt;
	}
	public void setApplyPlanDt(Date applyPlanDt) {
		this.applyPlanDt = applyPlanDt;
	}
	public Date getApplyRDt() {
		return applyRDt;
	}
	public void setApplyRDt(Date applyRDt) {
		this.applyRDt = applyRDt;
	}
	public String getApplyPlanDtDisplay() {
		if(applyPlanDt != null){
			applyPlanDtDisplay = new SimpleDateFormat(ITSMDefine.DATE_FORMAT).format(applyPlanDt);
		}
		return applyPlanDtDisplay;
	}
	public void setApplyPlanDtDisplay(String applyPlanDtDisplay) {
		
		this.applyPlanDtDisplay = applyPlanDtDisplay;
	}
	public String getApplyRDtDisplay() {
		if(applyRDt != null){
			applyRDtDisplay = new SimpleDateFormat(ITSMDefine.DATE_FORMAT).format(applyRDt);
		}
		return applyRDtDisplay;
	}
	public void setApplyRDtDisplay(String applyRDtDisplay) {
		this.applyRDtDisplay = applyRDtDisplay;
	}
	public String getCnfrmYn() {
		return cnfrmYn;
	}
	public void setCnfrmYn(String cnfrmYn) {
		this.cnfrmYn = cnfrmYn;
	}
	public String getNoCnfrmResn() {
		return noCnfrmResn;
	}
	public void setNoCnfrmResn(String noCnfrmResn) {
		this.noCnfrmResn = noCnfrmResn;
	}
	public String getFiRunSvrYn() {
		return fiRunSvrYn;
	}
	public void setFiRunSvrYn(String fiRunSvrYn) {
		this.fiRunSvrYn = fiRunSvrYn;
	}
	public String getFiDevSvrYn() {
		return fiDevSvrYn;
	}
	public void setFiDevSvrYn(String fiDevSvrYn) {
		this.fiDevSvrYn = fiDevSvrYn;
	}
	public String getFiDbYn() {
		return fiDbYn;
	}
	public void setFiDbYn(String fiDbYn) {
		this.fiDbYn = fiDbYn;
	}
	public String getFiVmYn() {
		return fiVmYn;
	}
	public void setFiVmYn(String fiVmYn) {
		this.fiVmYn = fiVmYn;
	}
	public String getFiEtcYn() {
		return fiEtcYn;
	}
	public void setFiEtcYn(String fiEtcYn) {
		this.fiEtcYn = fiEtcYn;
	}
	public String getFiPlan() {
		return fiPlan;
	}
	public void setFiPlan(String fiPlan) {
		this.fiPlan = fiPlan;
	}
	public String getRquireDfnYn() {
		return rquireDfnYn;
	}
	public void setRquireDfnYn(String rquireDfnYn) {
		this.rquireDfnYn = rquireDfnYn;
	}
	public String getRquireSpcYn() {
		return rquireSpcYn;
	}
	public void setRquireSpcYn(String rquireSpcYn) {
		this.rquireSpcYn = rquireSpcYn;
	}
	public String getRquireTrcYn() {
		return rquireTrcYn;
	}
	public void setRquireTrcYn(String rquireTrcYn) {
		this.rquireTrcYn = rquireTrcYn;
	}
	public String getPckgProgrmListYn() {
		return pckgProgrmListYn;
	}
	public void setPckgProgrmListYn(String pckgProgrmListYn) {
		this.pckgProgrmListYn = pckgProgrmListYn;
	}
	public String getUiDsignYn() {
		return uiDsignYn;
	}
	public void setUiDsignYn(String uiDsignYn) {
		this.uiDsignYn = uiDsignYn;
	}
	public String getProgrmDsignYn() {
		return progrmDsignYn;
	}
	public void setProgrmDsignYn(String progrmDsignYn) {
		this.progrmDsignYn = progrmDsignYn;
	}
	public String getTableDsignYn() {
		return tableDsignYn;
	}
	public void setTableDsignYn(String tableDsignYn) {
		this.tableDsignYn = tableDsignYn;
	}
	public String getProgrmListYn() {
		return progrmListYn;
	}
	public void setProgrmListYn(String progrmListYn) {
		this.progrmListYn = progrmListYn;
	}
	public String getUserMnualYn() {
		return userMnualYn;
	}
	public void setUserMnualYn(String userMnualYn) {
		this.userMnualYn = userMnualYn;
	}
	public String getAdmnMnualYn() {
		return admnMnualYn;
	}
	public void setAdmnMnualYn(String admnMnualYn) {
		this.admnMnualYn = admnMnualYn;
	}
	public String getUnitTestYn() {
		return unitTestYn;
	}
	public void setUnitTestYn(String unitTestYn) {
		this.unitTestYn = unitTestYn;
	}
	public String getUnionTestYn() {
		return unionTestYn;
	}
	public void setUnionTestYn(String unionTestYn) {
		this.unionTestYn = unionTestYn;
	}
	public String getChngePlan() {
		return chngePlan;
	}
	public void setChngePlan(String chngePlan) {
		this.chngePlan = chngePlan;
	}
	public String getBackupPlan() {
		return backupPlan;
	}
	public void setBackupPlan(String backupPlan) {
		this.backupPlan = backupPlan;
	}
	public String getRstorePlan() {
		return rstorePlan;
	}
	public void setRstorePlan(String rstorePlan) {
		this.rstorePlan = rstorePlan;
	}
	public String getConstrnt() {
		return constrnt;
	}
	public void setConstrnt(String constrnt) {
		this.constrnt = constrnt;
	}
	public String getConsder() {
		return consder;
	}
	public void setConsder(String consder) {
		this.consder = consder;
	}
	public String getFiAtchmnflId() {
		return fiAtchmnflId;
	}
	public void setFiAtchmnflId(String fiAtchmnflId) {
		this.fiAtchmnflId = fiAtchmnflId;
	}
	public String getNavigation() {
		return navigation;
	}
	public void setNavigation(String navigation) {
		this.navigation = navigation;
	}
	public String getFiRquire() {
		return fiRquire;
	}
	public void setFiRquire(String fiRquire) {
		this.fiRquire = fiRquire;
	}
	public String getFiCn() {
		return fiCn;
	}
	public void setFiCn(String fiCn) {
		this.fiCn = fiCn;
	}
	public String getAsisAtchmnflId() {
		return asisAtchmnflId;
	}
	public void setAsisAtchmnflId(String asisAtchmnflId) {
		this.asisAtchmnflId = asisAtchmnflId;
	}
	public String getTobeAtchmnflId() {
		return tobeAtchmnflId;
	}
	public void setTobeAtchmnflId(String tobeAtchmnflId) {
		this.tobeAtchmnflId = tobeAtchmnflId;
	}

	// ------------
	private String confirmUsr;

	public String getConfirmUsr() {
		return confirmUsr;
	}

	public void setConfirmUsr(String confirmUsr) {
		this.confirmUsr = confirmUsr;
	}
}
