package kr.go.smplatform.itsm.funcimprvm.vo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kr.go.smplatform.itsm.config.ITSMDefine;
import kr.go.smplatform.itsm.srvcrspons.vo.SrvcRsponsVO;

public class FuncImprvmVO extends SrvcRsponsVO {
    private String conectSysYn;
    private String conectSys;
    private String conectSysNm;
    private String fiCl;
    private String fiClNm;
    private Date applyPlanDt;
    private String applyPlanDtDisplay;
    private Date applyRDt;
    private String applyRDtDisplay;
    private String cnfrmYn;
    private String noCnfrmResn;

    private String fiRunSvrYn = "N";
    private String fiDevSvrYn = "N";
    private String fiDbYn = "N";
    private String fiVmYn = "N";
    private String fiEtcYn = "N";

    private String fiPlan;

    private String rquireDfnYn = "N";
    private String rquireSpcYn = "N";
    private String rquireTrcYn = "N";
    private String pckgProgrmListYn = "N";
    private String uiDsignYn = "N";
    private String progrmDsignYn = "N";
    private String tableDsignYn = "N";
    private String progrmListYn = "N";
    private String userMnualYn = "N";
    private String admnMnualYn = "N";
    private String unitTestYn = "N";
    private String unionTestYn = "N";

    private String chngePlan;
    private String backupPlan;
    private String rstorePlan;
    private String constrnt;
    private String consder;
    private String navigation;
    private String fiRquire;
    private String fiCn;

    private String fiAtchmnflId;
    private String asisAtchmnflId;
    private String tobeAtchmnflId;

    private String confirmUsr;

    public String getFiClNm() {
        return fiClNm;
    }

    public void setFiClNm(String fiClNm) {
        this.fiClNm = fiClNm;
    }

    public String getConectSysNm() {
        return conectSysNm;
    }

    public void setConectSysNm(String conectSysNm) {
        this.conectSysNm = conectSysNm;
    }

    public String getConectSysYn() {
        return conectSysYn;
    }

    public void setConectSysYn(String conectSysYn) {
        this.conectSysYn = conectSysYn;
    }

    public String getConectSys() {
        return conectSys;
    }

    public void setConectSys(String conectSys) {
        this.conectSys = conectSys;
    }

    public String getFiCl() {
        return fiCl;
    }

    public void setFiCl(String fiCl) {
        this.fiCl = fiCl;
    }

    public void makeApplyPlanDt() {
        String dateString = this.getApplyPlanDtDisplay();
        if (dateString == null || dateString.isEmpty()) {
            return;
        }
        try {
            this.applyPlanDt = new SimpleDateFormat(ITSMDefine.DATE_FORMAT).parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void makeApplyRDt() {
        String dateString = this.getApplyRDtDisplay();
        if (dateString == null || dateString.isEmpty()) {
            return;
        }
        try {
            this.applyRDt = new SimpleDateFormat(ITSMDefine.DATE_FORMAT).parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Date getApplyPlanDt() {
        return applyPlanDt;
    }

    public void setApplyPlanDt(Date applyPlanDt) {
        this.applyPlanDt = applyPlanDt;
    }

    public Date getApplyRDt() {
        return applyRDt;
    }

    public void setApplyRDt(Date applyRDt) {
        this.applyRDt = applyRDt;
    }

    public String getApplyPlanDtDisplay() {
        if (applyPlanDt != null) {
            applyPlanDtDisplay = new SimpleDateFormat(ITSMDefine.DATE_FORMAT).format(applyPlanDt);
        }
        return applyPlanDtDisplay;
    }

    public void setApplyPlanDtDisplay(String applyPlanDtDisplay) {
        this.applyPlanDtDisplay = applyPlanDtDisplay;
    }

    public String getApplyRDtDisplay() {
        if (applyRDt != null) {
            applyRDtDisplay = new SimpleDateFormat(ITSMDefine.DATE_FORMAT).format(applyRDt);
        }
        return applyRDtDisplay;
    }

    public void setApplyRDtDisplay(String applyRDtDisplay) {
        this.applyRDtDisplay = applyRDtDisplay;
    }

    public String getCnfrmYn() {
        return cnfrmYn;
    }

    public void setCnfrmYn(String cnfrmYn) {
        this.cnfrmYn = cnfrmYn;
    }

    public String getNoCnfrmResn() {
        return noCnfrmResn;
    }

    public void setNoCnfrmResn(String noCnfrmResn) {
        this.noCnfrmResn = noCnfrmResn;
    }

    public String getFiRunSvrYn() {
        return fiRunSvrYn;
    }

    public void setFiRunSvrYn(String fiRunSvrYn) {
        this.fiRunSvrYn = fiRunSvrYn;
    }

    public String getFiDevSvrYn() {
        return fiDevSvrYn;
    }

    public void setFiDevSvrYn(String fiDevSvrYn) {
        this.fiDevSvrYn = fiDevSvrYn;
    }

    public String getFiDbYn() {
        return fiDbYn;
    }

    public void setFiDbYn(String fiDbYn) {
        this.fiDbYn = fiDbYn;
    }

    public String getFiVmYn() {
        return fiVmYn;
    }

    public void setFiVmYn(String fiVmYn) {
        this.fiVmYn = fiVmYn;
    }

    public String getFiEtcYn() {
        return fiEtcYn;
    }

    public void setFiEtcYn(String fiEtcYn) {
        this.fiEtcYn = fiEtcYn;
    }

    public String getFiPlan() {
        return fiPlan;
    }

    public void setFiPlan(String fiPlan) {
        this.fiPlan = fiPlan;
    }

    public String getRquireDfnYn() {
        return rquireDfnYn;
    }

    public void setRquireDfnYn(String rquireDfnYn) {
        this.rquireDfnYn = rquireDfnYn;
    }

    public String getRquireSpcYn() {
        return rquireSpcYn;
    }

    public void setRquireSpcYn(String rquireSpcYn) {
        this.rquireSpcYn = rquireSpcYn;
    }

    public String getRquireTrcYn() {
        return rquireTrcYn;
    }

    public void setRquireTrcYn(String rquireTrcYn) {
        this.rquireTrcYn = rquireTrcYn;
    }

    public String getPckgProgrmListYn() {
        return pckgProgrmListYn;
    }

    public void setPckgProgrmListYn(String pckgProgrmListYn) {
        this.pckgProgrmListYn = pckgProgrmListYn;
    }

    public String getUiDsignYn() {
        return uiDsignYn;
    }

    public void setUiDsignYn(String uiDsignYn) {
        this.uiDsignYn = uiDsignYn;
    }

    public String getProgrmDsignYn() {
        return progrmDsignYn;
    }

    public void setProgrmDsignYn(String progrmDsignYn) {
        this.progrmDsignYn = progrmDsignYn;
    }

    public String getTableDsignYn() {
        return tableDsignYn;
    }

    public void setTableDsignYn(String tableDsignYn) {
        this.tableDsignYn = tableDsignYn;
    }

    public String getProgrmListYn() {
        return progrmListYn;
    }

    public void setProgrmListYn(String progrmListYn) {
        this.progrmListYn = progrmListYn;
    }

    public String getUserMnualYn() {
        return userMnualYn;
    }

    public void setUserMnualYn(String userMnualYn) {
        this.userMnualYn = userMnualYn;
    }

    public String getAdmnMnualYn() {
        return admnMnualYn;
    }

    public void setAdmnMnualYn(String admnMnualYn) {
        this.admnMnualYn = admnMnualYn;
    }

    public String getUnitTestYn() {
        return unitTestYn;
    }

    public void setUnitTestYn(String unitTestYn) {
        this.unitTestYn = unitTestYn;
    }

    public String getUnionTestYn() {
        return unionTestYn;
    }

    public void setUnionTestYn(String unionTestYn) {
        this.unionTestYn = unionTestYn;
    }

    public String getChngePlan() {
        return chngePlan;
    }

    public void setChngePlan(String chngePlan) {
        this.chngePlan = chngePlan;
    }

    public String getBackupPlan() {
        return backupPlan;
    }

    public void setBackupPlan(String backupPlan) {
        this.backupPlan = backupPlan;
    }

    public String getRstorePlan() {
        return rstorePlan;
    }

    public void setRstorePlan(String rstorePlan) {
        this.rstorePlan = rstorePlan;
    }

    public String getConstrnt() {
        return constrnt;
    }

    public void setConstrnt(String constrnt) {
        this.constrnt = constrnt;
    }

    public String getConsder() {
        return consder;
    }

    public void setConsder(String consder) {
        this.consder = consder;
    }

    public String getFiAtchmnflId() {
        return fiAtchmnflId;
    }

    public void setFiAtchmnflId(String fiAtchmnflId) {
        this.fiAtchmnflId = fiAtchmnflId;
    }

    public String getNavigation() {
        return navigation;
    }

    public void setNavigation(String navigation) {
        this.navigation = navigation;
    }

    public String getFiRquire() {
        return fiRquire;
    }

    public void setFiRquire(String fiRquire) {
        this.fiRquire = fiRquire;
    }

    public String getFiCn() {
        return fiCn;
    }

    public void setFiCn(String fiCn) {
        this.fiCn = fiCn;
    }

    public String getAsisAtchmnflId() {
        return asisAtchmnflId;
    }

    public void setAsisAtchmnflId(String asisAtchmnflId) {
        this.asisAtchmnflId = asisAtchmnflId;
    }

    public String getTobeAtchmnflId() {
        return tobeAtchmnflId;
    }

    public void setTobeAtchmnflId(String tobeAtchmnflId) {
        this.tobeAtchmnflId = tobeAtchmnflId;
    }

    public String getConfirmUsr() {
        return confirmUsr;
    }

    public void setConfirmUsr(String confirmUsr) {
        this.confirmUsr = confirmUsr;
    }
}
