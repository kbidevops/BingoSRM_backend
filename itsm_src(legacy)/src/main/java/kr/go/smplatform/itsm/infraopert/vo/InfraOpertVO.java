package kr.go.smplatform.itsm.infraopert.vo;

import kr.go.smplatform.itsm.srvcrspons.vo.SrvcRsponsVO;

public class InfraOpertVO extends SrvcRsponsVO {
	
	/** 인프라 작업 번호*/
//	private String infraOpertNo;
	private String infraOpertNo_sub;
	/** 인프라 작업계획서 파일 ID*/
	private String infraPlanAtchmnflId;
	/** 인프라 작업결과서 파일 ID*/
	private String infraResultAtchmnflId;
	/** 인프라 기타 첨부 파일 ID*/
	private String infraPlanEtcAtchmnflId;
	private String infraResultEtcAtchmnflId;
	
	public String getInfraOpertNo_sub() {
		return infraOpertNo_sub;
	}
	public void setInfraOpertNo_sub(String infraOpertNo_sub) {
		this.infraOpertNo_sub = infraOpertNo_sub;
	}
	public String getInfraPlanAtchmnflId() {
		return infraPlanAtchmnflId;
	}
	public void setInfraPlanAtchmnflId(String infraPlanAtchmnflId) {
		this.infraPlanAtchmnflId = infraPlanAtchmnflId;
	}
	public String getInfraResultAtchmnflId() {
		return infraResultAtchmnflId;
	}
	public void setInfraResultAtchmnflId(String infraResultAtchmnflId) {
		this.infraResultAtchmnflId = infraResultAtchmnflId;
	}
	public String getInfraPlanEtcAtchmnflId() {
		return infraPlanEtcAtchmnflId;
	}
	public void setInfraPlanEtcAtchmnflId(String infraPlanEtcAtchmnflId) {
		this.infraPlanEtcAtchmnflId = infraPlanEtcAtchmnflId;
	}
	public String getInfraResultEtcAtchmnflId() {
		return infraResultEtcAtchmnflId;
	}
	public void setInfraResultEtcAtchmnflId(String infraResultEtcAtchmnflId) {
		this.infraResultEtcAtchmnflId = infraResultEtcAtchmnflId;
	}
	
}
