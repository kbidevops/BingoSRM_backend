package kr.go.smplatform.itsm.report.vo;

import java.util.Date;
import java.util.List;

/**
 * 보고서 개편 하면서 만들어진 VO
 */
public class RepFormVO2 {
    private String repSn;
    private Date date;
    private String type;
    private String currentAttitude;
    private String planAttitude;
    private List<RepDescVO> descriptions;

    @Override
    public String toString() {
        return "RepFormVO2{" +
                "repSn='" + repSn + '\'' +
                ", date=" + date +
                ", type='" + type + '\'' +
                ", currentAttitude='" + currentAttitude + '\'' +
                ", planAttitude='" + planAttitude + '\'' +
                ", descriptions=" + descriptions +
                '}';
    }

    public String getRepSn() {
        return repSn;
    }

    public void setRepSn(String repSn) {
        this.repSn = repSn;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCurrentAttitude() {
        return currentAttitude;
    }

    public void setCurrentAttitude(String currentAttitude) {
        this.currentAttitude = currentAttitude;
    }

    public String getPlanAttitude() {
        return planAttitude;
    }

    public void setPlanAttitude(String planAttitude) {
        this.planAttitude = planAttitude;
    }

    public List<RepDescVO> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<RepDescVO> descriptions) {
        this.descriptions = descriptions;
    }
}
