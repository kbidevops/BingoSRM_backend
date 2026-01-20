package kr.go.smplatform.itsm.config.vo;

public class ResponseRequiredVO {
    private String phone;
    private String userId;

    @Override
    public String toString() {
        return "ResponseRequiredVO{" +
                "phone='" + phone + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
