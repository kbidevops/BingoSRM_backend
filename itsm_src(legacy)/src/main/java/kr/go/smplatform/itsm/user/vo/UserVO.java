package kr.go.smplatform.itsm.user.vo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.Sha2Crypt;
import org.apache.commons.validator.GenericValidator;

import kr.go.smplatform.itsm.base.vo.BaseVO;
import kr.go.smplatform.itsm.config.ITSMDefine;
import kr.go.smplatform.itsm.repcharger.vo.RepChargerVO;
import kr.go.smplatform.itsm.syscharger.vo.SysChargerVO;

public class UserVO extends BaseVO{
	public static final String LOGIN_USER_VO = "LOGIN_USER_VO";
	public static final String USER_TY_CODE_TEMP = "R000"; //임시사용자
	public static final String USER_TY_CODE_MNGR = "R001"; //시스템관리자
	public static final String USER_TY_CODE_CSTMR = "R002"; //고객사용자
	public static final String USER_TY_CODE_CHARGER = "R003"; //시스템담당자
	public static final String USER_TY_CODE_CNSLT = "R004"; //상담센터
	public static final String USER_TY_CODE_R005 = "R005"; //협단체
	
	public static final String USER_STTUS_CODE_WAIT = "U001"; //승인요청/승인대기
	public static final String USER_STTUS_CODE_ALLOW = "U002"; //승인
	public static final String USER_STTUS_CODE_STOP = "U003"; //사용중지
	
	public static final String TEMP_USER = "tempUser";
	public static final String DEFAULT_USER_PASSWORD = "DefauPSSword7979!!";
	
	private String userId;
    private String userPassword;
    private String userNm;
    private String userTyCode2; 
    private String userTyCode; 
    public String getUserTyCode2() {
		return userTyCode2;
	}


	public void setUserTyCode2(String userTyCode2) {
		this.userTyCode2 = userTyCode2;
	}

	private String userTyCodeNm;
    private String userSttusCode;
    private String userSttusCodeNm;
    private String psitn;
    private String clsf;
    private String email;
    private String moblphon;
    private String acntReqstResn;
    private String changePasswordYN="N";
    private String conectIp;
    
    private String comboName; //쿼리에서 소속과 이름 연결해서 불시보안점검에서 사용
    
    private List<SysChargerVO> sysChargerVOList = new ArrayList<SysChargerVO>();
    
    
    public String getUSER_TY_CODE_TEMP() {
		return USER_TY_CODE_TEMP;
	}


	public String getUSER_TY_CODE_MNGR() {
		return USER_TY_CODE_MNGR;
	}


	public String getUSER_TY_CODE_CSTMR() {
		return USER_TY_CODE_CSTMR;
	}


	public String getUSER_TY_CODE_CHARGER() {
		return USER_TY_CODE_CHARGER;
	}


	public String getUSER_TY_CODE_CNSLT() {
		return USER_TY_CODE_CNSLT;
	}


	public String getUSER_STTUS_CODE_WAIT() {
		return USER_STTUS_CODE_WAIT;
	}


	public String getUSER_STTUS_CODE_ALLOW() {
		return USER_STTUS_CODE_ALLOW;
	}


	public String getUSER_STTUS_CODE_STOP() {
		return USER_STTUS_CODE_STOP;
	}


	public void encodeUserPassword() {
		if(!GenericValidator.isBlankOrNull(this.getUserPassword())
				&& !GenericValidator.isBlankOrNull(this.getUserId())){
			//비밀번호 암호화
			String salt = ITSMDefine.PREFIX_SALT + this.getUserId();
			String encodeSHA256Password = Sha2Crypt.sha256Crypt(this.getUserPassword().getBytes(), salt);
			this.setUserPassword(encodeSHA256Password);
		}
	}
    
    
	public List<SysChargerVO> getSysChargerVOList() {
		return sysChargerVOList;
	}


	public void setSysChargerVOList(List<SysChargerVO> sysChargerVOList) {
		this.sysChargerVOList = sysChargerVOList;
	}

	public String getComboName() {
		return comboName;
	}
	public void setComboName(String comboName) {
		this.comboName = comboName;
	}
	public String getConectIp() {
		return conectIp;
	}
	public void setConectIp(String conectIp) {
		this.conectIp = conectIp;
	}	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public String getUserNm() {
		return userNm;
	}
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}
	public String getPsitn() {
		return psitn;
	}
	public void setPsitn(String psitn) {
		this.psitn = psitn;
	}
	public String getClsf() {
		return clsf;
	}
	public void setClsf(String clsf) {
		this.clsf = clsf;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMoblphon() {
		return moblphon;
	}
	public void setMoblphon(String moblphon) {
		this.moblphon = moblphon;
	}
	public String getAcntReqstResn() {
		return acntReqstResn;
	}
	public void setAcntReqstResn(String acntReqstResn) {
		this.acntReqstResn = acntReqstResn;
	}
	public String getChangePasswordYN() {
		return changePasswordYN;
	}
	public void setChangePasswordYN(String changePasswordYN) {
		this.changePasswordYN = changePasswordYN;
	}
	public String getUserTyCode() {
		return userTyCode;
	}
	public void setUserTyCode(String userTyCode) {
		this.userTyCode = userTyCode;
	}
	public String getUserTyCodeNm() {
		return userTyCodeNm;
	}
	public void setUserTyCodeNm(String userTyCodeNm) {
		this.userTyCodeNm = userTyCodeNm;
	}
	public String getUserSttusCode() {
		return userSttusCode;
	}
	public void setUserSttusCode(String userSttusCode) {
		this.userSttusCode = userSttusCode;
	}
	public String getUserSttusCodeNm() {
		return userSttusCodeNm;
	}
	public void setUserSttusCodeNm(String userSttusCodeNm) {
		this.userSttusCodeNm = userSttusCodeNm;
	}

	// ----------------------
	private String userLocat;
	private String userSignature;
	private String userSignatureFileName;
	private String userSignatureFileSize;
	private String deleteSignature;

	public String getUserLocat() {
		return userLocat;
	}

	public void setUserLocat(String userLocat) {
		this.userLocat = userLocat;
	}

	public String getUserSignature() {
		return userSignature;
	}

	public void setUserSignature(String userSignature) {
		this.userSignature = userSignature;
	}

	public String getUserSignatureFileName() {
		return userSignatureFileName;
	}

	public void setUserSignatureFileName(String userSignatureFileName) {
		this.userSignatureFileName = userSignatureFileName;
	}

	public String getUserSignatureFileSize() {
		return userSignatureFileSize;
	}

	public void setUserSignatureFileSize(String userSignatureFileSize) {
		this.userSignatureFileSize = userSignatureFileSize;
	}

	public String getDeleteSignature() {
		return deleteSignature;
	}

	public void setDeleteSignature(String deleteSignature) {
		this.deleteSignature = deleteSignature;
	}
// ------- utils ------------------------

	public String getUserSignatureFileSizeCalculated() {
		try {
			double size = Double.parseDouble(userSignatureFileSize);
			final String[] units = { "Byte", "KiB", "MiB", "GiB", "TiB" };
			int index = 0;

			while (size >= 1024 && index < units.length) {
				size /= 1024;
				index++;
			}

			return String.format("%.2f %s", size, units[Math.min(index, units.length - 1)]);
		} catch (Exception e) {
			return "";
		}
	}
}
