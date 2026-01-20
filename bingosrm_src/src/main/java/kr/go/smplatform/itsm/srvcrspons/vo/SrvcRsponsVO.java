package kr.go.smplatform.itsm.srvcrspons.vo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kr.go.smplatform.itsm.base.vo.BaseVO;
import kr.go.smplatform.itsm.config.ITSMDefine;

public class SrvcRsponsVO extends BaseVO {
	// -----------------
	private String userTyCode;
	private String userId;

	public String getUserTyCode() {
		return userTyCode;
	}

	public void setUserTyCode(String userTyCode) {
		this.userTyCode = userTyCode;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
// -----------------

	public static final String DEFAULT_PROCESS_STDR_CODE = "S201";
	public static final String DEFAULT_CHANGE_DFFLY_CODE = "S004";
	
	/** 전화*/
	public static final String DEFAULT_SRVC_RSPONS_BASIS_CODE = "S301";
	/** 시스템*/
	public static final String SRVC_RSPONS_BASIS_CODE_S306 = "S306";
	
	/** 데이터 변경*/
	public static final String SRVC_RSPONS_CL_CODE_S102 = "S102";
	
	private String srvcRsponsNo;
	/** 요청일시 */
	private Date requstDt;
	private String requstDtDateDisplay;
	private String requstDtTimeDisplay;
	
	private String rqester1stNm;
	private String rqester1stPsitn;
	private String rqester1stCttpc;
	
	private String rqester1stEmail;
	
	private String rqesterId;
	private String rqesterNm;
	private String rqesterPsitn;  //쿼리, validator 쪽 정리
	private String rqesterCttpc;
	private String rqesterEmail;
	
	private String trgetSrvcCode;
	private String trgetSrvcCodeNm;

	public String getTrgetSrvcDetailCode() {
		return trgetSrvcDetailCode;
	}

	public void setTrgetSrvcDetailCode(String trgetSrvcDetailCode) {
		this.trgetSrvcDetailCode = trgetSrvcDetailCode;
	}

	public String getTrgetSrvcDetailCodeNm() {
		return trgetSrvcDetailCodeNm;
	}

	public void setTrgetSrvcDetailCodeNm(String trgetSrvcDetailCodeNm) {
		this.trgetSrvcDetailCodeNm = trgetSrvcDetailCodeNm;
	}

	private String trgetSrvcDetailCode;
	private String trgetSrvcDetailCodeNm;

	/** 솔루션 구분 */
	private String trgetSrvcCodeSubNm1;
	/** SR 구분 */
	private String trgetSrvcCodeSubNm2;
	private String trgetSrvcCodeSubNm3;
	
	private String srvcRsponsSj; 
	
	private String srvcRsponsCn;
	private String requstAtchmnflId;
	
	/** 1차 응답일시*/
	private Date rspons1stDt;
	private String rspons1stDtDateDisplay;
	private String rspons1stDtTimeDisplay;
	
	private String processMt;
	/** 변경난이도*/
	private String changeDfflyCode;
	private String changeDfflyCodeNm;
	
	/** SR구분 */
	private String srvcRsponsClCode;
	private String srvcRsponsClCodeNm;
	/** SR처리기준시간 */
	private String processStdrCode;
	private String processStdrCodeNm;
	
	private String processTerm;
	
	
	public String getProcessTerm() {
		return processTerm;
	}

	public void setProcessTerm(String processTerm) {
		this.processTerm = processTerm;
	}

	private String srvcProcessDtls;
	private String etc;
	
	private String srvcRsponsBasisCode;
	private String srvcRsponsBasisCodeNm;
	
	private String rsponsAtchmnflId;
	
	/** 처리일시*/
	private Date processDt;
	private String processDtDateDisplay;
	private String processDtTimeDisplay;
	
	private String dataUpdtYn;
	private String progrmUpdtYn;
	private String stopInstlYn;
	private String noneStopInstlYn;
	private String instlYn; 
	private String infraOpertYn;
	
	private String chargerId;
	private String chargerUserNm;
	private String cnfrmrId;
	private String cnfrmrUserNm;// = "이경국";
	
	private String orderLevel;
	private String priorLevel;
	
	private String smsChk;
	private String excludeprocessYn;
	/** 첨부자료 유무 */
	private String requstAtchmnflAt;
	
	private String fnctImprvmNo;
	
	private String wdtbCnfirmNo;
	private Date srvcWdtbDt;
	private String srvcWdtbDtDateDisplay;
	private String srvcWdtbDtTimeDisplay;
	
	public String getPriorLevel() {
		return priorLevel;
	}

	public void setPriorLevel(String priorLevel) {
		this.priorLevel = priorLevel;
	}

	private String infraOpertNo;
	
	
	/** 재요청시 원 요청번호 */
	private String reSrvcRsponsNo;
	/** 재요청일시 */
	private Date reRequestDt;
	private String reRequestDtDateDisplay;
	private String reRequestDtTimeDisplay;

	/**  검증 완료 단계 추가   **/
	private String verifyYn;
	private Date verifyDt;
	private String verifyDtDateDisplay;
	private String verifyDtTimeDisplay;

	private Date finishDt;
	private String finishDtDateDisplay;
	private String finishDtTimeDisplay;
	
	private String verifyId;
	private String verifyUserNm;

	private String srvcVerifyDtls;
	private String srvcFinDtls;
	
	private String refIds;

	//검색조건-요청자
	private String srcRqesterId;
	private String srcRqesterNm;
	
	public String getSrcRqesterId() {
		return srcRqesterId;
	}

	public void setSrcRqesterId(String srcRqesterId) {
		this.srcRqesterId = srcRqesterId;
	}

	public String getSrcRqesterNm() {
		return srcRqesterNm;
	}

	public void setSrcRqesterNm(String srcRqesterNm) {
		this.srcRqesterNm = srcRqesterNm;
	}

	public String getRefIds() {
		return refIds;
	}

	public void setRefIds(String refIds) {
		this.refIds = refIds;
	}

	public String getSrvcVerifyDtls() {
		return srvcVerifyDtls;
	}

	public void setSrvcVerifyDtls(String srvcVerifyDtls) {
		this.srvcVerifyDtls = srvcVerifyDtls;
	}

	public String getSrvcFinDtls() {
		return srvcFinDtls;
	}

	public void setSrvcFinDtls(String srvcFinDtls) {
		this.srvcFinDtls = srvcFinDtls;
	}

	public String getVerifyId() {
		return verifyId;
	}

	public String getVerifyUserNm() {
		return verifyUserNm;
	}

	public void setVerifyUserNm(String verifyUserNm) {
		this.verifyUserNm = verifyUserNm;
	}

	public String getFinishUserNm() {
		return finishUserNm;
	}

	public void setFinishUserNm(String finishUserNm) {
		this.finishUserNm = finishUserNm;
	}

	public void setVerifyId(String verifyId) {
		this.verifyId = verifyId;
	}

	public String getFinishId() {
		return finishId;
	}

	public void setFinishId(String finishId) {
		this.finishId = finishId;
	}

	private String finishId;
	private String finishUserNm;
	
	public Date getVerifyDt() {
		return verifyDt;
	}

	public void setVerifyDt(Date verifyDt) {
		this.verifyDt = verifyDt;
	}

	public Date getFinishDt() {
		return finishDt;
	}

	public void setFinishDt(Date finishDt) {
		this.finishDt = finishDt;
	}

	/** 재용청시 이전 요청의 제목, 요청내용  **/
	private String reSrvcRsponsSj; 
	private String reSrvcRsponsCn;

	public String getVerifyDtDateDisplay() {
		if(verifyDt != null){
			verifyDtDateDisplay = new SimpleDateFormat(ITSMDefine.DATE_FORMAT).format(verifyDt);
		}
		return verifyDtDateDisplay;
	}

	public void setVerifyDtDateDisplay(String verifyDtDateDisplay) {
		this.verifyDtDateDisplay = verifyDtDateDisplay;
	}

	public String getVerifyDtTimeDisplay() {
		if(verifyDt != null){
			verifyDtTimeDisplay = new SimpleDateFormat(ITSMDefine.DATE_HH_MM_FORMAT).format(verifyDt);
		}
		return verifyDtTimeDisplay;
	}

	public void setVerifyDtTimeDisplay(String verifyDtTimeDisplay) {
		this.verifyDtTimeDisplay = verifyDtTimeDisplay;
	}
	public void makeVerifyDt(){
		String dateString = this.getVerifyDtDateDisplay();
		dateString += " ";
		dateString += this.getVerifyDtTimeDisplay();
		
		try {
			this.verifyDt = new SimpleDateFormat(ITSMDefine.DATE_TIME_FORMAT).parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getFinishDtDateDisplay() {
		if(finishDt != null){
			finishDtDateDisplay = new SimpleDateFormat(ITSMDefine.DATE_FORMAT).format(finishDt);
		}
		return finishDtDateDisplay;
	}

	public void setFinishDtDateDisplay(String finishDtDateDisplay) {
		this.finishDtDateDisplay = finishDtDateDisplay;
	}

	public String getFinishDtTimeDisplay() {
		if(finishDt != null){
			finishDtTimeDisplay = new SimpleDateFormat(ITSMDefine.DATE_HH_MM_FORMAT).format(finishDt);
		}
		return finishDtTimeDisplay;
	}
	public void makeFinishDt(){
		String dateString = this.getFinishDtDateDisplay();
		dateString += " ";
		dateString += this.getFinishDtTimeDisplay();
		
		try {
			this.finishDt = new SimpleDateFormat(ITSMDefine.DATE_TIME_FORMAT).parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setFinishDtTimeDisplay(String finishDtTimeDisplay) {
		this.finishDtTimeDisplay = finishDtTimeDisplay;
	}

	public void setReRequestDtDateDisplay(String reRequestDtDateDisplay) {
		this.reRequestDtDateDisplay = reRequestDtDateDisplay;
	}
	public String getReRequestDtDateDisplay() {
		if(reRequestDt != null){
			reRequestDtDateDisplay = new SimpleDateFormat(ITSMDefine.DATE_FORMAT).format(reRequestDt);
		}
		return reRequestDtDateDisplay;
	}
	public String getReRequestDtTimeDisplay() {
		if(reRequestDt != null){
			reRequestDtTimeDisplay = new SimpleDateFormat(ITSMDefine.DATE_HH_MM_FORMAT).format(reRequestDt);
		}
		return reRequestDtTimeDisplay;
	}
	public void setReRequestDtTimeDisplay(String reRequestDtTimeDisplay) {
		this.reRequestDtTimeDisplay = reRequestDtTimeDisplay;
	}
	
	public String getReSrvcRsponsNo() {
		return reSrvcRsponsNo;
	}

	public void setReSrvcRsponsNo(String reSrvcRsponsNo) {
		this.reSrvcRsponsNo = reSrvcRsponsNo;
	}

	public Date getReRequestDt() {
		return reRequestDt;
	}

	public void setReRequestDt(Date reRequestDt) {
		this.reRequestDt = reRequestDt;
	}

	public String getSmsChk() {
		return smsChk;
	}
	public void setSmsChk(String smsChk) {
		this.smsChk = smsChk;
	}
	public String getTrgetSrvcCodeSubNm3() {
		return trgetSrvcCodeSubNm3;
	}
	public void setTrgetSrvcCodeSubNm3(String trgetSrvcCodeSubNm3) {
		this.trgetSrvcCodeSubNm3 = trgetSrvcCodeSubNm3;
	}
	public Date getSrvcWdtbDt() {
		return srvcWdtbDt;
	}
	public void setSrvcWdtbDt(Date srvcWdtbDt) {
		this.srvcWdtbDt = srvcWdtbDt;
	}
	public String getSrvcWdtbDtDateDisplay() {
		if(srvcWdtbDt != null){
			srvcWdtbDtDateDisplay = new SimpleDateFormat(ITSMDefine.DATE_FORMAT).format(srvcWdtbDt);
		}
		return srvcWdtbDtDateDisplay;
	}
	public void setSrvcWdtbDtDateDisplay(String srvcWdtbDtDateDisplay) {
		this.srvcWdtbDtDateDisplay = srvcWdtbDtDateDisplay;
	}
	public String getSrvcWdtbDtTimeDisplay() {
		if(srvcWdtbDt != null){
			srvcWdtbDtTimeDisplay = new SimpleDateFormat(ITSMDefine.DATE_HH_MM_FORMAT).format(srvcWdtbDt);
		}
		return srvcWdtbDtTimeDisplay;
	}
	public void makesrvcWdtbDt(){
		String dateString = this.getSrvcWdtbDtDateDisplay();
		dateString += " ";
		dateString += this.getSrvcWdtbDtTimeDisplay();
		
		try {
			this.srvcWdtbDt = new SimpleDateFormat(ITSMDefine.DATE_TIME_FORMAT).parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setSrvcWdtbDtTimeDisplay(String srvcWdtbDtTimeDisplay) {
		this.srvcWdtbDtTimeDisplay = srvcWdtbDtTimeDisplay;
	}
	public String getFnctImprvmNo() {
		return fnctImprvmNo;
	}
	public void setFnctImprvmNo(String fnctImprvmNo) {
		this.fnctImprvmNo = fnctImprvmNo;
	}
	public String getWdtbCnfirmNo() {
		return wdtbCnfirmNo;
	}
	public void setWdtbCnfirmNo(String wdtbCnfirmNo) {
		this.wdtbCnfirmNo = wdtbCnfirmNo;
	}
	public String getRequstAtchmnflAt() {
		return requstAtchmnflAt;
	}
	public void setRequstAtchmnflAt(String requstAtchmnflAt) {
		this.requstAtchmnflAt = requstAtchmnflAt;
	}
	public String getTrgetSrvcCodeSubNm1() {
		return trgetSrvcCodeSubNm1;
	}
	public void setTrgetSrvcCodeSubNm1(String trgetSrvcCodeSubNm1) {
		this.trgetSrvcCodeSubNm1 = trgetSrvcCodeSubNm1;
	}
	public String getTrgetSrvcCodeSubNm2() {
		return trgetSrvcCodeSubNm2;
	}
	public void setTrgetSrvcCodeSubNm2(String trgetSrvcCodeSubNm2) {
		this.trgetSrvcCodeSubNm2 = trgetSrvcCodeSubNm2;
	}
	public String getExcludeprocessYn() {
		return excludeprocessYn;
	}
	public void setExcludeprocessYn(String excludeprocessYn) {
		this.excludeprocessYn = excludeprocessYn;
	}
	public String getOrderLevel() {
		return orderLevel;
	}
	public void setOrderLevel(String orderLevel) {
		this.orderLevel = orderLevel;
	}
	public String getSrvcRsponsNo() {
		return srvcRsponsNo;
	}
	public void setSrvcRsponsNo(String srvcRsponsNo) {
		this.srvcRsponsNo = srvcRsponsNo;
	}
	public Date getRequstDt() {
		return requstDt;
	}
	public void setRequstDt(Date requstDt) {
		this.requstDt = requstDt;
	}	
	public String getRequstDtDateDisplay() {
		if(requstDt != null){
			requstDtDateDisplay = new SimpleDateFormat(ITSMDefine.DATE_FORMAT).format(requstDt);
		}
		return requstDtDateDisplay;
	}
	public void setRequstDtDateDisplay(String requstDtDateDisplay) {
		this.requstDtDateDisplay = requstDtDateDisplay;
	}
	public String getRequstDtTimeDisplay() {
		if(requstDt != null){
			requstDtTimeDisplay = new SimpleDateFormat(ITSMDefine.DATE_HH_MM_FORMAT).format(requstDt);
		}
		return requstDtTimeDisplay;
	}
	public void setRequstDtTimeDisplay(String requstDtTimeDisplay) {
		this.requstDtTimeDisplay = requstDtTimeDisplay;
	}
	
	public void makeRequstDt(){
		String dateString = this.getRequstDtDateDisplay();
		dateString += " ";
		dateString += this.getRequstDtTimeDisplay();
		
		try {
			this.requstDt = new SimpleDateFormat(ITSMDefine.DATE_TIME_FORMAT).parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getRqester1stNm() {
		return rqester1stNm;
	}
	public void setRqester1stNm(String rqester1stNm) {
		this.rqester1stNm = rqester1stNm;
	}
	public String getRqester1stPsitn() {
		return rqester1stPsitn;
	}
	public void setRqester1stPsitn(String rqester1stPsitn) {
		this.rqester1stPsitn = rqester1stPsitn;
	}
	public String getRqester1stCttpc() {
		return rqester1stCttpc;
	}
	public void setRqester1stCttpc(String rqester1stCttpc) {
		this.rqester1stCttpc = rqester1stCttpc;
	}
	public String getRqesterId() {
		return rqesterId;
	}
	public void setRqesterId(String rqesterId) {
		this.rqesterId = rqesterId;
	}
	public String getRqesterNm() {
		return rqesterNm;
	}
	public void setRqesterNm(String rqesterNm) {
		this.rqesterNm = rqesterNm;
	}
	public String getRqesterCttpc() {
		return rqesterCttpc;
	}
	public void setRqesterCttpc(String rqesterCttpc) {
		this.rqesterCttpc = rqesterCttpc;
	}
	public String getRqesterEmail() {
		return rqesterEmail;
	}
	public void setRqesterEmail(String rqesterEmail) {
		this.rqesterEmail = rqesterEmail;
	}
	public String getTrgetSrvcCode() {
		return trgetSrvcCode;
	}
	public void setTrgetSrvcCode(String trgetSrvcCode) {
		this.trgetSrvcCode = trgetSrvcCode;
	}
	public String getTrgetSrvcCodeNm() {
		return trgetSrvcCodeNm;
	}
	public void setTrgetSrvcCodeNm(String trgetSrvcCodeNm) {
		this.trgetSrvcCodeNm = trgetSrvcCodeNm;
	}
	public String getSrvcRsponsCn() {
		return srvcRsponsCn;
	}
	public void setSrvcRsponsCn(String srvcRsponsCn) {
		this.srvcRsponsCn = srvcRsponsCn;
	}
	public String getRequstAtchmnflId() {
		return requstAtchmnflId;
	}
	public void setRequstAtchmnflId(String requstAtchmnflId) {
		this.requstAtchmnflId = requstAtchmnflId;
	}
	
	
	public Date getRspons1stDt() {
		return rspons1stDt;
	}
	public void setRspons1stDt(Date rspons1stDt) {
		this.rspons1stDt = rspons1stDt;
	}	
	
	public String getRspons1stDtDateDisplay() {
		if(rspons1stDt != null){
			rspons1stDtDateDisplay = new SimpleDateFormat(ITSMDefine.DATE_FORMAT).format(rspons1stDt);
		}
		return rspons1stDtDateDisplay;
	}
	public void setRspons1stDtDateDisplay(String rspons1stDtDateDisplay) {
		this.rspons1stDtDateDisplay = rspons1stDtDateDisplay;
	}
	public String getRspons1stDtTimeDisplay() {
		if(rspons1stDt != null){
			rspons1stDtTimeDisplay = new SimpleDateFormat(ITSMDefine.DATE_HH_MM_FORMAT).format(rspons1stDt);
		}
		return rspons1stDtTimeDisplay;
	}
	public void setRspons1stDtTimeDisplay(String rspons1stDtTimeDisplay) {
		this.rspons1stDtTimeDisplay = rspons1stDtTimeDisplay;
	}
	public void makeRspons1stDt(){
		String dateString = this.getRspons1stDtDateDisplay();
		dateString += " ";
		dateString += this.getRspons1stDtTimeDisplay();
		
		try {
			this.rspons1stDt = new SimpleDateFormat(ITSMDefine.DATE_TIME_FORMAT).parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getProcessMt() {
		return processMt;
	}
	public void setProcessMt(String processMt) {
		this.processMt = processMt;
	}
	public String getChangeDfflyCode() {
		return changeDfflyCode;
	}
	public void setChangeDfflyCode(String changeDfflyCode) {
		this.changeDfflyCode = changeDfflyCode;
	}
	public String getChangeDfflyCodeNm() {
		return changeDfflyCodeNm;
	}
	public void setChangeDfflyCodeNm(String changeDfflyCodeNm) {
		this.changeDfflyCodeNm = changeDfflyCodeNm;
	}
	public String getSrvcRsponsClCode() {
		return srvcRsponsClCode;
	}
	public void setSrvcRsponsClCode(String srvcRsponsClCode) {
		this.srvcRsponsClCode = srvcRsponsClCode;
	}
	public String getSrvcRsponsClCodeNm() {
		return srvcRsponsClCodeNm;
	}
	public void setSrvcRsponsClCodeNm(String srvcRsponsClCodeNm) {
		this.srvcRsponsClCodeNm = srvcRsponsClCodeNm;
	}
	public String getProcessStdrCode() {
		return processStdrCode;
	}
	public void setProcessStdrCode(String processStdrCode) {
		this.processStdrCode = processStdrCode;
	}
	public String getProcessStdrCodeNm() {
		return processStdrCodeNm;
	}
	public void setProcessStdrCodeNm(String processStdrCodeNm) {
		this.processStdrCodeNm = processStdrCodeNm;
	}
	public String getSrvcProcessDtls() {
		return srvcProcessDtls;
	}
	public void setSrvcProcessDtls(String srvcProcessDtls) {
		this.srvcProcessDtls = srvcProcessDtls;
	}
	public String getEtc() {
		return etc;
	}
	public void setEtc(String etc) {
		this.etc = etc;
	}
	public String getSrvcRsponsBasisCode() {
		return srvcRsponsBasisCode;
	}
	public void setSrvcRsponsBasisCode(String srvcRsponsBasisCode) {
		this.srvcRsponsBasisCode = srvcRsponsBasisCode;
	}
	public String getSrvcRsponsBasisCodeNm() {
		return srvcRsponsBasisCodeNm;
	}
	public void setSrvcRsponsBasisCodeNm(String srvcRsponsBasisCodeNm) {
		this.srvcRsponsBasisCodeNm = srvcRsponsBasisCodeNm;
	}
	public String getRsponsAtchmnflId() {
		return rsponsAtchmnflId;
	}
	public void setRsponsAtchmnflId(String rsponsAtchmnflId) {
		this.rsponsAtchmnflId = rsponsAtchmnflId;
	}
	public Date getProcessDt() {
		return processDt;
	}
	public void setProcessDt(Date processDt) {
		this.processDt = processDt;
	}	
	public String getProcessDtDateDisplay() {
		if(processDt != null){
			processDtDateDisplay = new SimpleDateFormat(ITSMDefine.DATE_FORMAT).format(processDt);
		}
		return processDtDateDisplay;
	}
	public void setProcessDtDateDisplay(String processDtDateDisplay) {
		this.processDtDateDisplay = processDtDateDisplay;
	}
	public String getProcessDtTimeDisplay() {
		if(processDt != null){
			processDtTimeDisplay = new SimpleDateFormat(ITSMDefine.DATE_HH_MM_FORMAT).format(processDt);
		}
		return processDtTimeDisplay;
	}
	public void setProcessDtTimeDisplay(String processDtTimeDisplay) {
		this.processDtTimeDisplay = processDtTimeDisplay;
	}
	public void makeProcessDt(){
		String dateString = this.getProcessDtDateDisplay();
		dateString += " ";
		dateString += this.getProcessDtTimeDisplay();
		
		try {
			this.processDt = new SimpleDateFormat(ITSMDefine.DATE_TIME_FORMAT).parse(dateString);
			this.processMt = new SimpleDateFormat(ITSMDefine.PROCESS_MT_FORMAT).format(this.processDt);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getChargerId() {
		return chargerId;
	}
	public void setChargerId(String chargerId) {
		this.chargerId = chargerId;
	}
	public String getChargerUserNm() {
		return chargerUserNm;
	}
	public void setChargerUserNm(String chargerUserNm) {
		this.chargerUserNm = chargerUserNm;
	}
	public String getCnfrmrId() {
		return cnfrmrId;
	}
	public void setCnfrmrId(String cnfrmrId) {
		this.cnfrmrId = cnfrmrId;
	}
	public String getCnfrmrUserNm() {
		return cnfrmrUserNm;
	}
	public void setCnfrmrUserNm(String cnfrmrUserNm) {
		this.cnfrmrUserNm = cnfrmrUserNm;
	}
	public String getRqester1stEmail() {
		return rqester1stEmail;
	}
	public void setRqester1stEmail(String rqester1stEmail) {
		this.rqester1stEmail = rqester1stEmail;
	}
	public String getSrvcRsponsSj() {
		return srvcRsponsSj;
	}
	public void setSrvcRsponsSj(String srvcRsponsSj) {
		this.srvcRsponsSj = srvcRsponsSj;
	}
	public String getDataUpdtYn() {
		return dataUpdtYn;
	}
	public void setDataUpdtYn(String dataUpdtYn) {
		this.dataUpdtYn = dataUpdtYn;
	}
	public String getProgrmUpdtYn() {
		return progrmUpdtYn;
	}
	public void setProgrmUpdtYn(String progrmUpdtYn) {
		this.progrmUpdtYn = progrmUpdtYn;
	}
	public String getStopInstlYn() {
		return stopInstlYn;
	}
	public void setStopInstlYn(String stopInstlYn) {
		this.stopInstlYn = stopInstlYn;
	}
	public String getNoneStopInstlYn() {
		return noneStopInstlYn;
	}
	public void setNoneStopInstlYn(String noneStopInstlYn) {
		this.noneStopInstlYn = noneStopInstlYn;
	}
	public String getRqesterPsitn() {
		return rqesterPsitn;
	}
	public void setRqesterPsitn(String rqesterPsitn) {
		this.rqesterPsitn = rqesterPsitn;
	}
	public String getInstlYn() {
		return instlYn;
	}
	public void setInstlYn(String instlYn) {
		this.instlYn = instlYn;
	}
	public String getInfraOpertYn() {
		return infraOpertYn;
	}
	public void setInfraOpertYn(String infraOpertYn) {
		this.infraOpertYn = infraOpertYn;
	}
	public String getInfraOpertNo() {
		return infraOpertNo;
	}
	public void setInfraOpertNo(String infraOpertNo) {
		this.infraOpertNo = infraOpertNo;
	}

	public String getVerifyYn() {
		return verifyYn;
	}

	public void setVerifyYn(String verifyYn) {
		this.verifyYn = verifyYn;
	}

	public String getReSrvcRsponsCn() {
		return reSrvcRsponsCn;
	}

	public void setReSrvcRsponsCn(String reSrvcRsponsCn) {
		this.reSrvcRsponsCn = reSrvcRsponsCn;
	}

	public String getReSrvcRsponsSj() {
		return reSrvcRsponsSj;
	}

	public void setReSrvcRsponsSj(String reSrvcRsponsSj) {
		this.reSrvcRsponsSj = reSrvcRsponsSj;
	}



	
}
