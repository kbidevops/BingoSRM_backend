package kr.go.smplatform.itsm.intcpt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import kr.go.smplatform.itsm.user.vo.UserVO;

public class LoginInterceptor extends HandlerInterceptorAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		//로그인 정보 및 접근 제한 체크
		HttpSession session = request.getSession();
		UserVO loginUserVO = (UserVO)session.getAttribute(UserVO.LOGIN_USER_VO);
				
		if(loginUserVO == null){
			//임시 개발을 위해 추가
				loginUserVO = new UserVO();
//				loginUserVO.setUserId("itsmadmin");
//				loginUserVO.setUserNm("관리자");
//				loginUserVO.setUserTyCode(UserVO.USER_TY_CODE_CHARGER);
				
				loginUserVO.setUserId(UserVO.TEMP_USER);
				loginUserVO.setUserNm("가입전사용자");
				loginUserVO.setUserTyCode(UserVO.USER_TY_CODE_TEMP);
				
				session.setAttribute(UserVO.LOGIN_USER_VO, loginUserVO);
		}
		
		return super.preHandle(request, response, handler);
	}
}
