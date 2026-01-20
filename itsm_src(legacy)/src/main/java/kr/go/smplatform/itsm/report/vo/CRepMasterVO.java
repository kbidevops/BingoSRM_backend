package kr.go.smplatform.itsm.report.vo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 보고서 개편 하면서 만들어진 VO
 * insert용 master vo
 */
public class CRepMasterVO {
    private final String repTyCode;
    private final Date reportDt;
    private final String userId;

    private CRepMasterVO(String repTyCode, Date reportDt, String userId) {
        this.repTyCode = repTyCode;
        this.reportDt = reportDt;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "CRepMasterVO{" +
                "repTyCode='" + repTyCode + '\'' +
                ", reportName='" + getReportName() + '\'' +
                ", reportDt=" + reportDt +
                ", userId='" + userId + '\'' +
                '}';
    }

    public String getReportName() {
        switch (repTyCode) {
            case "B001":
                return new SimpleDateFormat("yyyy-MM-dd").format(reportDt) + " 업무보고서";

            case "B002": {
                final Calendar cal = Calendar.getInstance();
                cal.setTime(reportDt);
                final int weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);

                return "주간업무보고(" + weekOfYear + "주차)";
            }

            case "B003": {
                final Calendar cal = Calendar.getInstance();
                cal.setTime(reportDt);

                return (cal.get(Calendar.MONTH) + 1) + "월 월간업무보고";
            }

            default:
                return "";
        }
    }

    public String getRepTyCode() {
        return repTyCode;
    }

    public Date getReportDt() {
        return reportDt;
    }

    public String getUserId() {
        return userId;
    }

    public static final class Builder {
        private String repTyCode;
        private Date reportDt;
        private String userId;

        private Builder() {
        }

        public static Builder aCRepMasterVO() {
            return new Builder();
        }

        public Builder repTyCode(String repTyCode) {
            this.repTyCode = repTyCode;
            return this;
        }

        public Builder reportDt(Date reportDt) {
            this.reportDt = reportDt;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public CRepMasterVO build() {
            return new CRepMasterVO(repTyCode, reportDt, userId);
        }
    }
}
