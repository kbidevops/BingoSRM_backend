package kr.go.smplatform.itsm.report.vo;

import java.util.ArrayList;
import java.util.List;

import kr.go.smplatform.itsm.base.vo.BaseVO;

public class RepFormVO extends BaseVO{
	private RepDetailVO repDetailVO;
	private RepMasterVO repMasterVO;
	private RepMasterVO searchMasterVO;
	private RepDetailVO searchDetailVO;
	private List<RepDetailVO> repDetailVOList;
	
	private String registerFlag;
	private List<Integer> repSns;
	private List<String> sysCodes;
	private List<String> planDescs;
	private List<String> execDescs;
	
	private String search;
	
	public RepFormVO() {
		repDetailVO = new RepDetailVO();
		repMasterVO = new RepMasterVO();
		searchMasterVO = new RepMasterVO();
		searchDetailVO = new RepDetailVO();
		
		repDetailVOList = new ArrayList<RepDetailVO>();
		
		repSns = new ArrayList<Integer>();
		sysCodes = new ArrayList<String>();
		planDescs = new ArrayList<String>();
		execDescs = new ArrayList<String>();
	}
	
	
	public String getSearch() {
		if(search == null) {
			search = "-";
		}
		return search;
	}
	
	
	public RepDetailVO getSearchDetailVO() {
		return searchDetailVO;
	}
	public void setSearchDetailVO(RepDetailVO searchDetailVO) {
		this.searchDetailVO = searchDetailVO;
	}
	public RepMasterVO getSearchMasterVO() {
		return searchMasterVO;
	}
	public void setSearchMasterVO(RepMasterVO searchMasterVO) {
		this.searchMasterVO = searchMasterVO;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	public List<Integer> getRepSns() {
		return repSns;
	}
	public void setRepSns(List<Integer> repSns) {
		this.repSns = repSns;
	}
	public String getRegisterFlag() {
		return registerFlag;
	}
	public void setRegisterFlag(String registerFlag) {
		this.registerFlag = registerFlag;
	}
	public List<String> getSysCodes() {
		return sysCodes;
	}
	public void setSysCodes(List<String> sysCodes) {
		this.sysCodes = sysCodes;
	}
	public List<String> getPlanDescs() {
		return planDescs;
	}
	public void setPlanDescs(List<String> planDescs) {
		this.planDescs = planDescs;
	}
	public List<String> getExecDescs() {
		return execDescs;
	}
	public void setExecDescs(List<String> execDescs) {
		this.execDescs = execDescs;
	}
	public List<RepDetailVO> getRepDetailVOList() {
		return repDetailVOList;
	}
	public void setRepDetailVOList(List<RepDetailVO> repDetailVOList) {
		this.repDetailVOList = repDetailVOList;
	}
	public RepDetailVO getRepDetailVO() {
		return repDetailVO;
	}
	public void setRepDetailVO(RepDetailVO repDetailVO) {
		this.repDetailVO = repDetailVO;
	}
	public RepMasterVO getRepMasterVO() {
		return repMasterVO;
	}
	public void setRepMasterVO(RepMasterVO repMasterVO) {
		this.repMasterVO = repMasterVO;
	}
	
	public List<RepDetailVO> getVariableVOList(){
		for(int i=0; i<sysCodes.size(); i++) {
			
			RepDetailVO repDetailVO = new RepDetailVO();
			
			repDetailVO.setRepTyCode(repMasterVO.getRepTyCode());
			repDetailVO.setExecDesc(execDescs.get(i));
			repDetailVO.setPlanDesc(planDescs.get(i));
			
			repDetailVO.setSysCode(sysCodes.get(i));
			repDetailVO.setDeleteYn("N");
			
			if(registerFlag.equals("modify")) {
				repDetailVO.setUpdtId(this.repDetailVO.getUpdtId());
				repDetailVO.setRepSn(repSns.get(0));
			}else if(registerFlag.equals("create")) {
				repDetailVO.setCreatId(this.repDetailVO.getCreatId());
				repDetailVO.setUserId(this.repDetailVO.getCreatId());
				repDetailVO.setCreatDt(this.repDetailVO.getCreatDt());
				repDetailVO.setRepSn(repMasterVO.getRepSn());
			}
			
			repDetailVOList.add(repDetailVO);
		}
		
		return repDetailVOList;
	}
	
}
