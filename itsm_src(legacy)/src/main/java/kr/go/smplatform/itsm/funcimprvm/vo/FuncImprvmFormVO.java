package kr.go.smplatform.itsm.funcimprvm.vo;

import kr.go.smplatform.itsm.base.vo.BaseVO;
import kr.go.smplatform.itsm.srvcrspons.vo.SrvcRsponsVO;

public class FuncImprvmFormVO extends BaseVO {
	
	private FuncImprvmVO searchFuncImprvmVO;
	private FuncImprvmVO funcImprvmVO;
	
	private String processMt;
	
	public FuncImprvmFormVO() {
		searchFuncImprvmVO = new FuncImprvmVO();
		funcImprvmVO = new FuncImprvmVO();
	}
	
	public String getProcessMt() {
		return processMt;
	}


	public void setProcessMt(String processMt) {
		this.processMt = processMt;
	}

	
	public FuncImprvmVO getSearchFuncImprvmVO() {
		return searchFuncImprvmVO;
	}


	public void setSearchFuncImprvmVO(FuncImprvmVO searchFuncImprvmVO) {
		this.searchFuncImprvmVO = searchFuncImprvmVO;
	}


	public FuncImprvmVO getFuncImprvmVO() {
		return funcImprvmVO;
	}


	public void setFuncImprvmVO(FuncImprvmVO funcImprvmVO) {
		this.funcImprvmVO = funcImprvmVO;
	}

	
}
