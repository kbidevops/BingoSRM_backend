package kr.go.smplatform.itsm.wdtb.vo;

import java.util.ArrayList;
import java.util.List;

import kr.go.smplatform.itsm.base.vo.BaseVO;
import kr.go.smplatform.itsm.funcimprvm.vo.FuncImprvmVO;
import kr.go.smplatform.itsm.srvcrspons.vo.SrvcRsponsVO;

public class WdtbFormVO extends BaseVO {
	
	private String processMt;
	
	private WdtbVO searchWdtbVO;
	private WdtbVO wdtbVO;
	private SrvcRsponsVO srvcRsponsVO;
	private FuncImprvmVO funcImprvmVO;
	private List<String> srvcRsponsNos;
	
	public WdtbFormVO(){
		searchWdtbVO = new WdtbVO();
		srvcRsponsVO = new SrvcRsponsVO();
		funcImprvmVO = new FuncImprvmVO();
		wdtbVO = new WdtbVO();
		srvcRsponsNos = new ArrayList<String>();
	}
	
	
	
	public SrvcRsponsVO getSrvcRsponsVO() {
		return srvcRsponsVO;
	}

	public void setSrvcRsponsVO(SrvcRsponsVO srvcRsponsVO) {
		this.srvcRsponsVO = srvcRsponsVO;
	}

	public FuncImprvmVO getFuncImprvmVO() {
		return funcImprvmVO;
	}

	public void setFuncImprvmVO(FuncImprvmVO funcImprvmVO) {
		this.funcImprvmVO = funcImprvmVO;
	}

	public List<String> getSrvcRsponsNos() {
		return srvcRsponsNos;
	}

	public void setSrvcRsponsNos(List<String> srvcRsponsNos) {
		this.srvcRsponsNos = srvcRsponsNos;
	}

	public String getProcessMt() {
		return processMt;
	}

	public void setProcessMt(String processMt) {
		this.processMt = processMt;
	}

	public WdtbVO getSearchWdtbVO() {
		return searchWdtbVO;
	}

	public void setSearchWdtbVO(WdtbVO searchWdtbVO) {
		this.searchWdtbVO = searchWdtbVO;
	}

	public WdtbVO getWdtbVO() {
		return wdtbVO;
	}

	public void setWdtbVO(WdtbVO wdtbVO) {
		this.wdtbVO = wdtbVO;
	}
	
	

}
