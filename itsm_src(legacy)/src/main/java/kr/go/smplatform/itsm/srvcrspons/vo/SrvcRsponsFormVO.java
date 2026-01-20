package kr.go.smplatform.itsm.srvcrspons.vo;

import java.util.List;

import kr.go.smplatform.itsm.base.vo.BaseVO;

public class SrvcRsponsFormVO extends BaseVO{
	private SrvcRsponsVO searchSrvcRsponsVO;
	private SrvcRsponsVO srvcRsponsVO;
	
	private String processMt;
	
	public SrvcRsponsFormVO(){
		searchSrvcRsponsVO = new SrvcRsponsVO();
		srvcRsponsVO = new SrvcRsponsVO();
	}
	
	public String getProcessMt() {
		return processMt;
	}


	public void setProcessMt(String processMt) {
		this.processMt = processMt;
	}


	public SrvcRsponsVO getSearchSrvcRsponsVO() {
		return searchSrvcRsponsVO;
	}

	public void setSearchSrvcRsponsVO(SrvcRsponsVO searchSrvcRsponsVO) {
		this.searchSrvcRsponsVO = searchSrvcRsponsVO;
	}

	public SrvcRsponsVO getSrvcRsponsVO() {
		return srvcRsponsVO;
	}

	public void setSrvcRsponsVO(SrvcRsponsVO srvcRsponsVO) {
		this.srvcRsponsVO = srvcRsponsVO;
	}
	
	
}
