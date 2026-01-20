package kr.go.smplatform.itsm.progrmaccesauthor.vo;

import java.util.ArrayList;
import java.util.List;

public class ProgrmAccesAuthorFormVO {
	private String progrmAccesAuthorCode;		/* 프로그램_접근_권한_코드 */
	private List<ProgrmAccesAuthorVO> progrmAccesAuthorVOList;
	
	public ProgrmAccesAuthorFormVO(){
		progrmAccesAuthorVOList = new ArrayList<ProgrmAccesAuthorVO>();
	}
	public String getProgrmAccesAuthorCode() {
		return progrmAccesAuthorCode;
	}
	public void setProgrmAccesAuthorCode(String progrmAccesAuthorCode) {
		this.progrmAccesAuthorCode = progrmAccesAuthorCode;
	}
	public List<ProgrmAccesAuthorVO> getProgrmAccesAuthorVOList() {
		return progrmAccesAuthorVOList;
	}
	public void setProgrmAccesAuthorVOList(List<ProgrmAccesAuthorVO> progrmAccesAuthorVOList) {
		this.progrmAccesAuthorVOList = progrmAccesAuthorVOList;
	}
	
	
}
