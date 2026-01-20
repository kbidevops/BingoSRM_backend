package kr.go.smplatform.itsm.report.vo;

import java.util.Date;

/**
 * 보고서 개편 하면서 만들어진 VO
 */
public class CRepAttitudeVO {
    private final String userId;
    private final String attitudeCode;
    private final Date attitudeDt;

    @Override
    public String toString() {
        return "CRepAttitudeVO{" +
                "userId='" + userId + '\'' +
                ", attitudeCode='" + attitudeCode + '\'' +
                ", attitudeDt='" + attitudeDt + '\'' +
                '}';
    }

    private CRepAttitudeVO(String userId, String attitudeCode, Date attitudeDt) {
        this.userId = userId;
        this.attitudeCode = attitudeCode;
        this.attitudeDt = attitudeDt;
    }

    public String getUserId() {
        return userId;
    }

    public String getAttitudeCode() {
        return attitudeCode;
    }

    public Date getAttitudeDt() {
        return attitudeDt;
    }

    public static final class BUilder {
        private String userId;
        private String attitudeCode;
        private Date attitudeDt;

        private BUilder() {
        }

        public static BUilder aCRepAttitudeVO() {
            return new BUilder();
        }

        public BUilder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public BUilder attitudeCode(String attitudeCode) {
            this.attitudeCode = attitudeCode;
            return this;
        }

        public BUilder attitudeDt(Date attitudeDt) {
            this.attitudeDt = attitudeDt;
            return this;
        }

        public CRepAttitudeVO build() {
            return new CRepAttitudeVO(userId, attitudeCode, attitudeDt);
        }
    }
}
