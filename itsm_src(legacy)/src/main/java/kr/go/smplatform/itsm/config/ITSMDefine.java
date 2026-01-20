package kr.go.smplatform.itsm.config;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

public class ITSMDefine {
	public static final String ROOT_PATH_WINDOWS = "C:/itsm/";
	public static final String ROOT_PATH_UNIX = "/itsm/";
	
	public static final String SERVICE_MODE_RUN = "run";
	public static final String SERVICE_MODE_DEV = "dev";
	
	public static String rootPath = ROOT_PATH_UNIX;

	public static String serviceMode = SERVICE_MODE_DEV;
	/**
	 * SHA256적용시 SALT의 접두어 
	 */
	public static final String PREFIX_SALT = "$5$";
	
	public static final String SAVE_TOKEN = "SAVE_TOKEN";
	
    public static final String PATH_DATE_FORMAT = "yyyy-MM-dd";
	
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";
	public static final String YEAR_MONTH_FORMAT = "yyyy-MM";
	public static final String PROCESS_MT_FORMAT = "yyyyMM";
	
	
	public static final String DATE_AMPM_TIME_FORMAT = "yyyy-MM-dd aa hh:mm";
	
	public static final String DATE_HH_MM_FORMAT = "HH:mm";
	
	public static final String DATE_HH_FORMAT = "HH";
	
	public static final String DATE_MM_FORMAT = "mm";
	
	public static final String ERROR_MESSAGE = "ERROR_MESSAGE";
	public static final String SUCCESS_MESSAGE = "SUCCESS_MESSAGE";
	
	public static final String RETURN_CODE = "RETURN_CODE";
	public static final String RETURN_CODE_SUCCESS = "200";
	public static final String RETURN_CODE_ERROR = "201";
	public static final String RETURN_CODE_NEED_LOGIN = "202";
	
	public static void init() {
		String os = System.getProperty("os.name");

		if (os.toUpperCase().indexOf("WINDOW") > -1) {
			rootPath = ROOT_PATH_WINDOWS;			
		} else {
			rootPath = ROOT_PATH_UNIX;			
		}
	} 
	
	public static synchronized String generateSaveToken(HttpSession session){
		return generateToken(session, SAVE_TOKEN);
	}
	
	
	public static synchronized boolean checkSaveToken(HttpSession session, String saveToken){
	    return checkToken(session, SAVE_TOKEN, saveToken);
	}
	
	public static synchronized String generateToken(HttpSession session, String tokenType){
		String saveToken = UUID.randomUUID().toString();
		
		List<String> tokenList = (List<String>)session.getAttribute(tokenType);
		if(tokenList == null){
			tokenList = new ArrayList<String>();
			session.setAttribute(tokenType, tokenList);
		}
		
		tokenList.add(saveToken);
		
		return saveToken;
	}
	
	public static synchronized boolean checkToken(HttpSession session, String tokenType,String saveToken){
		boolean isResult = false;
		List<String> tokenList = (List<String>)session.getAttribute(tokenType);
		if(tokenList == null){
			tokenList = new ArrayList<String>();
			session.setAttribute(tokenType, tokenList);
		}
		
		if(tokenList.contains(saveToken)){
			isResult =  true;
		}
		
		tokenList.remove(saveToken);
		
		return isResult;
	}
}
