package kr.go.smplatform.itsm.syscharger.vo;

import kr.go.smplatform.itsm.user.vo.UserVO;

/**
 * @author CEPARK
 *
 */
public class SysChargerVO extends UserVO{
	/** 시스템_코드 */
	private String sysCode;
	
	/** 시스템_코드명1 */
	private String sysCodeNm;
	/** 시스템_코드명2*/
	private String sysCodeSubNm1;
	
	private String[] sysCodes;

	public String getSysCode() {
		return sysCode;
	}

	public String getSysCodeSubNm1() {
		return sysCodeSubNm1;
	}

	public void setSysCodeSubNm1(String sysCodeSubNm1) {
		this.sysCodeSubNm1 = sysCodeSubNm1;
	}

	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}

	public String getSysCodeNm() {
		return sysCodeNm;
	}

	public void setSysCodeNm(String sysCodeNm) {
		this.sysCodeNm = sysCodeNm;
	}

	public String[] getSysCodes() {
		return sysCodes;
	}

	public void setSysCodes(String[] sysCodes) {
		this.sysCodes = sysCodes;
	}
	
	
	
}
