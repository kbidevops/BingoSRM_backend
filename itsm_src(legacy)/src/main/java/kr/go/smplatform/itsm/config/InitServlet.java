package kr.go.smplatform.itsm.config;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InitServlet extends javax.servlet.http.HttpServlet {
	/**
	 * 시스템 로딩시 초기화 대상 정보 추가 요망
	 */
	public void init() throws ServletException {
		super.init();
		ITSMDefine.init();
	}
	
	/**
	 * 시스템 초기화 대상 url로 초기화 하도록 doGet, doPost 추가 정의
	 * 
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    // TODO Auto-generated method stub
		ITSMDefine.init();
    }
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	    // TODO Auto-generated method stub
	    this.doGet(req, resp);
    }
}
