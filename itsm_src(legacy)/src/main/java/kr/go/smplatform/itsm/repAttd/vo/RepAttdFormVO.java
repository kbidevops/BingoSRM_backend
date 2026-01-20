package kr.go.smplatform.itsm.repAttd.vo;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kr.go.smplatform.itsm.base.vo.BaseVO;
import kr.go.smplatform.itsm.cmmncode.vo.CmmnCodeVO;
import kr.go.smplatform.itsm.util.DateUtil;

public class RepAttdFormVO extends BaseVO{
	
	private RepAttdVO repAttdVO;
	private RepAttdVO planAttdVO;
	private RepAttdVO execAttdVO;
	private String planAttd = "";
	private String execAttd = "";
	private List<RepAttdVO> repAttdVOList;
	private Date planDate;
	private String userLocat = "";
	
	public RepAttdFormVO() {
		repAttdVO =  new RepAttdVO();
		planAttdVO = new RepAttdVO();
		execAttdVO = new RepAttdVO();
		repAttdVOList = new ArrayList<RepAttdVO>();
		planDate = new Date();
	}
	
	
	public RepAttdVO getRepAttdVO() {
		return repAttdVO;
	}
	public void setRepAttdVO(RepAttdVO repAttdVO) {
		this.repAttdVO = repAttdVO;
	}
	public String getUserLocat() {
		return userLocat;
	}
	public void setUserLocat(String userLocat) {
		this.userLocat = userLocat;
	}
	public Date getPlanDate() {
		return planDate;
	}
	public void setPlanDate(Date execDate, List<CmmnCodeVO> solar, List<CmmnCodeVO> lunar) throws ParseException {
		//다음 날짜 구하기
		Calendar cal = Calendar.getInstance();
		cal.setTime(execDate);
		
		cal.add(Calendar.DATE, +1);
		
		long date = DateUtil.dateToLong(cal.getTime());
		
		while(DateUtil.isHoliday(date,solar,lunar)) {
			cal.add(Calendar.DATE, +1);
			date = DateUtil.dateToLong(cal.getTime());
		}
		
		this.planDate = cal.getTime();
		this.planAttdVO.setAttdDt(planDate);
	}
	public List<RepAttdVO> getRepAttdVOList() {
		return repAttdVOList;
	}
	public void setRepAttdVOList(List<RepAttdVO> repAttdVOList) {
		this.repAttdVOList = repAttdVOList;
	}
	public RepAttdVO getPlanAttdVO() {
		planAttdVO.setAttdCode(planAttd);
		planAttdVO.setUserId(this.getCreatId());
		planAttdVO.setCreatId(this.getCreatId());
		planAttdVO.setUserLocat(userLocat);
		return planAttdVO;
	}
	public void setPlanAttdVO(RepAttdVO planAttdVO) {
		this.planAttdVO = planAttdVO;
	}
	public RepAttdVO getExecAttdVO() {
		execAttdVO.setAttdCode(execAttd);
		execAttdVO.setUserId(this.getCreatId());
		execAttdVO.setCreatId(this.getCreatId());
		execAttdVO.setUserLocat(userLocat);
		return execAttdVO;
	}
	public void setExecAttdVO(RepAttdVO execAttdVO) {
		this.execAttdVO = execAttdVO;
	}
	public String getPlanAttd() {
		return planAttd;
	}
	public void setPlanAttd(String planAttd) {
		this.planAttd = planAttd;
	}
	public String getExecAttd() {
		return execAttd;
	}
	public void setExecAttd(String execAttd) {
		this.execAttd = execAttd;
	}
	
	
	
}
