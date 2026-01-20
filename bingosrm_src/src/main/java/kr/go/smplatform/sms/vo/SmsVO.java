package kr.go.smplatform.sms.vo;

public class SmsVO {
	
    /** 발송타입 (1:SMS 2:LMS/MMS)*/
    private String sendType;	    
    /** 순번*/
    private String memberSeq;
    /**MMS제목*/
    private String subject;	      
    /**보낼 메세지*/
    private String msg;
    /**수신자번호*/
    private String destel;      
    /**발신자번호*/
    private String srctel;

	@Override
	public String toString() {
		return "SmsVO{" +
				"sendType='" + sendType + '\'' +
				", memberSeq='" + memberSeq + '\'' +
				", subject='" + subject + '\'' +
				", msg='" + msg + '\'' +
				", destel='" + destel + '\'' +
				", srctel='" + srctel + '\'' +
				'}';
	}

	public String getSendType() {
		return sendType;
	}
	public void setSendType(String sendType) {
		this.sendType = sendType;
	}
	public String getMemberSeq() {
		return memberSeq;
	}
	public void setMemberSeq(String memberSeq) {
		this.memberSeq = memberSeq;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getDestel() {
		return destel;
	}
	public void setDestel(String destel) {
		this.destel = destel;
	}
	public String getSrctel() {
		return srctel;
	}
	public void setSrctel(String srctel) {
		this.srctel = srctel;
	}
    
    
	
}
