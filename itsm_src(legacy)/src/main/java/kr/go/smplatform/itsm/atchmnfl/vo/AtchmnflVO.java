package kr.go.smplatform.itsm.atchmnfl.vo;

import java.io.Serializable;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

public class AtchmnflVO implements Serializable{
	private String atchmnflId;
	private String atchmnflSn;
	private String orginlFileNm;
	private String streAllCours;
	private long fileSize;
	
	private MultipartFile[] files;
	
	private MultipartFile multipartFile;
	
	

	public MultipartFile getMultipartFile() {
		return multipartFile;
	}

	public void setMultipartFile(MultipartFile multipartFile) {
		this.multipartFile = multipartFile;
	}

	public String getAtchmnflId() {
		return atchmnflId;
	}

	public void setAtchmnflId(String atchmnflId) {
		this.atchmnflId = atchmnflId;
	}

	public String getAtchmnflSn() {
		return atchmnflSn;
	}

	public void setAtchmnflSn(String atchmnflSn) {
		this.atchmnflSn = atchmnflSn;
	}

	public String getOrginlFileNm() {
		return orginlFileNm;
	}

	public void setOrginlFileNm(String orginlFileNm) {
		this.orginlFileNm = orginlFileNm;
	}

	public String getStreAllCours() {
		return streAllCours;
	}

	public void setStreAllCours(String streAllCours) {
		this.streAllCours = streAllCours;
	}

	public MultipartFile[] getFiles() {
		return files;
	}

	public void setFiles(MultipartFile[] files) {
		this.files = files;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	
	public String getFileSizeDisplay() {
		return FileUtils.byteCountToDisplaySize(fileSize);
	}
	
	
}
