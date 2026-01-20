package kr.go.smplatform.itsm.assets.vo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kr.go.smplatform.itsm.base.vo.BaseVO;
import kr.go.smplatform.itsm.config.ITSMDefine;

public class AssetsVO extends BaseVO {
    
    public static final String ASSETS_SE_HW = "T001"; //HW
    public static final String ASSETS_SE_SW = "T002"; //상용SW
    
    /** 자산 번호*/
    private int assetsSn;
    /** 자산관리번호 */
    private String assetsNo;
    /** 자산구분*/
    private String assetsSe1;
    private String assetsSe1Nm;
    private String assetsSe2;
    private String assetsSe2Nm;
    /** 구매방식*/
    private String purchsMthd;
    /** 제조사*/
    private String maker;
    /** 제품명*/
    private String productNm;
    /** 납품업체*/
    private String dlvgbiz;
    /** 수량*/
    private int assetQy;
    /** 수량 단위*/
    private String qyUnit;
    /** 용도*/
    private String assetPrpos;
    /** 자산 여부*/
    private String assetYn;
    /** 유지보수대상여부*/
    private String mntnceYn;
    /**기술 지원 업체*/
    private String tchnlgySprt;
    /** 설치 위치*/
    private String instlLc;
    private String instlLcNm;
    /** 사용 여부*/
    private String useYn;
    /** 도입 일자*/
    private Date indcDt;
    private String indcDtDisplay;
    /** 도입 단가*/
    private int indcUntpc;
    /** 도입 금액*/
    private int indcAmount;
    /** 유지보수 요율*/
    private String mntnceTariff;
    /** 유지보수 금액*/
    private int mntnceAmount;
    /** 내용 연수*/
    private String cnSdytrn; 
    /** 비고*/
    private String assetsRm;
    /** 담당자 이름*/
    private String chargerNm;
    /** 담당자 연락처*/
    private String chargerCttpc;
    /** 담당자 이메일*/
    private String chargerEmail;
    /** 담당자 직급*/
    private String chargerClsf;
    /** 첨부파일ID*/
    private String licenseAtchmnflId; 
    private String manualAtchmnflId;
    
    public int getAssetsSn() {
        return assetsSn;
    }
    public void setAssetsSn(int assetsSn) {
        this.assetsSn = assetsSn;
    }

    public String getAssetsNo() {
        return assetsNo;
    }

    public void setAssetsNo(String assetsNo) {
        this.assetsNo = assetsNo;
    }

    public String getAssetsSe1() {
        return assetsSe1;
    }
    public void setAssetsSe1(String assetsSe1) {
        this.assetsSe1 = assetsSe1;
    }
    
    public String getAssetsSe1Nm() {
        return assetsSe1Nm;
    }
    public void setAssetsSe1Nm(String assetsSe1Nm) {
        this.assetsSe1Nm = assetsSe1Nm;
    }
    public String getAssetsSe2() {
        return assetsSe2;
    }
    public void setAssetsSe2(String assetsSe2) {
        this.assetsSe2 = assetsSe2;
    }
    public String getAssetsSe2Nm() {
        return assetsSe2Nm;
    }
    public void setAssetsSe2Nm(String assetsSe2Nm) {
        this.assetsSe2Nm = assetsSe2Nm;
    }
    public String getPurchsMthd() {
        return purchsMthd;
    }
    public void setPurchsMthd(String purchsMthd) {
        this.purchsMthd = purchsMthd;
    }
    public String getMaker() {
        return maker;
    }
    public void setMaker(String maker) {
        this.maker = maker;
    }
    public String getProductNm() {
        return productNm;
    }
    public void setProductNm(String productNm) {
        this.productNm = productNm;
    }
    public String getDlvgbiz() {
        return dlvgbiz;
    }
    public void setDlvgbiz(String dlvgbiz) {
        this.dlvgbiz = dlvgbiz;
    }
    public int getAssetQy() {
        return assetQy;
    }
    public void setAssetQy(int assetQy) {
        this.assetQy = assetQy;
    }
    public String getQyUnit() {
        return qyUnit;
    }
    public void setQyUnit(String qyUnit) {
        this.qyUnit = qyUnit;
    }
    public String getAssetPrpos() {
        return assetPrpos;
    }
    public void setAssetPrpos(String assetPrpos) {
        this.assetPrpos = assetPrpos;
    }
    public String getAssetYn() {
        return assetYn;
    }
    public void setAssetYn(String assetYn) {
        this.assetYn = assetYn;
    }
    public String getMntnceYn() {
        return mntnceYn;
    }
    public void setMntnceYn(String mntnceYn) {
        this.mntnceYn = mntnceYn;
    }
    public String getTchnlgySprt() {
        return tchnlgySprt;
    }
    public void setTchnlgySprt(String tchnlgySprt) {
        this.tchnlgySprt = tchnlgySprt;
    }
    public String getInstlLc() {
        return instlLc;
    }
    public void setInstlLc(String instlLc) {
        this.instlLc = instlLc;
    }
    public String getInstlLcNm() {
        return instlLcNm;
    }
    public void setInstlLcNm(String instlLcNm) {
        this.instlLcNm = instlLcNm;
    }
    public String getUseYn() {
        return useYn;
    }
    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }
    public Date getIndcDt() {
        return indcDt;
    }
    public void setIndcDt(Date indcDt) {
        this.indcDt = indcDt;
    }
    public String getIndcDtDisplay() {
        if(indcDt != null){
            indcDtDisplay = new SimpleDateFormat(ITSMDefine.DATE_FORMAT).format(indcDt);
        }
        return indcDtDisplay;
    }
    public void setIndcDtDisplay(String indcDtDisplay) {
        this.indcDtDisplay = indcDtDisplay;
    }     
    public int getIndcUntpc() {
        return indcUntpc;
    }
    public void setIndcUntpc(int indcUntpc) {
        this.indcUntpc = indcUntpc;
    }
    public int getIndcAmount() {
        return indcAmount;
    }
    public void setIndcAmount(int indcAmount) {
        this.indcAmount = indcAmount;
    }
    public String getMntnceTariff() {
        return mntnceTariff;
    }
    public void setMntnceTariff(String mntnceTariff) {
        this.mntnceTariff = mntnceTariff;
    }
    public int getMntnceAmount() {
        return mntnceAmount;
    }
    public void setMntnceAmount(int mntnceAmount) {
        this.mntnceAmount = mntnceAmount;
    }
    public String getCnSdytrn() {
        return cnSdytrn;
    }
    public void setCnSdytrn(String cnSdytrn) {
        this.cnSdytrn = cnSdytrn;
    }
    public String getAssetsRm() {
        return assetsRm;
    }
    public void setAssetsRm(String assetsRm) {
        this.assetsRm = assetsRm;
    }
    public String getChargerNm() {
        return chargerNm;
    }
    public void setChargerNm(String chargerNm) {
        this.chargerNm = chargerNm;
    }
    public String getChargerCttpc() {
        return chargerCttpc;
    }
    public void setChargerCttpc(String chargerCttpc) {
        this.chargerCttpc = chargerCttpc;
    }
    public String getChargerEmail() {
        return chargerEmail;
    }
    public void setChargerEmail(String chargerEmail) {
        this.chargerEmail = chargerEmail;
    }
    public String getChargerClsf() {
        return chargerClsf;
    }
    public void setChargerClsf(String chargerClsf) {
        this.chargerClsf = chargerClsf;
    }
    public String getLicenseAtchmnflId() {
        return licenseAtchmnflId;
    }
    public void setLicenseAtchmnflId(String licenseAtchmnflId) {
        this.licenseAtchmnflId = licenseAtchmnflId;
    }
    public String getManualAtchmnflId() {
        return manualAtchmnflId;
    }
    public void setManualAtchmnflId(String manualAtchmnflId) {
        this.manualAtchmnflId = manualAtchmnflId;
    }
    public void makeIndcDtDisplay(){
        String dateString = this.getIndcDtDisplay();
        
        try {
            this.indcDt = new SimpleDateFormat(ITSMDefine.DATE_FORMAT).parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
