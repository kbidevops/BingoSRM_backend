package kr.go.smplatform.itsm.infraopert.vo;

import java.util.ArrayList;
import java.util.List;

import kr.go.smplatform.itsm.srvcrspons.vo.SrvcRsponsVO;

public class InfraOpertFormVO {
	
	private InfraOpertVO infraOpertVO;
	private InfraOpertVO searchInfraOpertVO;
	private SrvcRsponsVO srvcRsponsVO;
	private List<String> srvcRsponsNos;
	
	private String processMt;
	
	public InfraOpertFormVO() {
		infraOpertVO = new InfraOpertVO();
		searchInfraOpertVO = new InfraOpertVO();
		srvcRsponsVO = new SrvcRsponsVO();
		srvcRsponsNos = new ArrayList<String>();
	}
	
	
	public List<String> getSrvcRsponsNos() {
		return srvcRsponsNos;
	}
	public void setSrvcRsponsNos(List<String> srvcRsponsNos) {
		this.srvcRsponsNos = srvcRsponsNos;
	}
	public SrvcRsponsVO getSrvcRsponsVO() {
		return srvcRsponsVO;
	}
	public void setSrvcRsponsVO(SrvcRsponsVO srvcRsponsVO) {
		this.srvcRsponsVO = srvcRsponsVO;
	}
	public InfraOpertVO getInfraOpertVO() {
		return infraOpertVO;
	}
	public void setInfraOpertVO(InfraOpertVO infraOpertVO) {
		this.infraOpertVO = infraOpertVO;
	}
	public InfraOpertVO getSearchInfraOpertVO() {
		return searchInfraOpertVO;
	}
	public void setSearchInfraOpertVO(InfraOpertVO searchInfraOpertVO) {
		this.searchInfraOpertVO = searchInfraOpertVO;
	}
	public String getProcessMt() {
		return processMt;
	}
	public void setProcessMt(String processMt) {
		this.processMt = processMt;
	}
	
	
	
}
