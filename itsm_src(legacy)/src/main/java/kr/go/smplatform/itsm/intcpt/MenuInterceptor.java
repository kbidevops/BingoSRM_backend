package kr.go.smplatform.itsm.intcpt;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import kr.go.smplatform.itsm.config.ITSMDefine;
import kr.go.smplatform.itsm.hist.use.service.HistUseService;
import kr.go.smplatform.itsm.hist.use.vo.HistUseVO;
import kr.go.smplatform.itsm.progrm.vo.ProgrmVO;
import kr.go.smplatform.itsm.progrmaccesauthor.service.ProgrmAccesAuthorService;
import kr.go.smplatform.itsm.progrmaccesauthor.vo.ProgrmAccesAuthorVO;
import kr.go.smplatform.itsm.user.vo.UserVO;

public class MenuInterceptor extends HandlerInterceptorAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(MenuInterceptor.class);
	
	@Resource(name = "progrmAccesAuthorService")
	private ProgrmAccesAuthorService progrmAccesAuthorService;
	
	@Resource(name = "histUseService")
	private HistUseService histUseService;
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		//로그인 정보 및 접근 제한 체크
		HttpSession session = request.getSession();
		UserVO loginUserVO = (UserVO)session.getAttribute(UserVO.LOGIN_USER_VO);
		
		//application 영역에 권한별 메뉴 목록이 들어가 있다.
		
		ServletContext servletContext = request.getServletContext();
		List<ProgrmAccesAuthorVO> progrmAccesAuthorVOList =  (List<ProgrmAccesAuthorVO>)servletContext.getAttribute(loginUserVO.getUserTyCode());
		if(progrmAccesAuthorVOList == null){
			//없으면 조회해서 넣을것
			ProgrmAccesAuthorVO paramProgrmAccesAuthorVO = new ProgrmAccesAuthorVO();
			paramProgrmAccesAuthorVO.setProgrmAccesAuthorCode(loginUserVO.getUserTyCode());
			
			progrmAccesAuthorVOList = progrmAccesAuthorService.retrieveAssignList(paramProgrmAccesAuthorVO);
			
			servletContext.removeAttribute(loginUserVO.getUserTyCode());
			servletContext.setAttribute(loginUserVO.getUserTyCode(), progrmAccesAuthorVOList);
		}
		
		boolean isAccessPermit = false;
		boolean isExist = false;
		String uri = request.getRequestURI();
		String programUri = uri.substring(servletContext.getContextPath().length());
		//선택된 LEFT MENU
		ProgrmAccesAuthorVO selectedProgrmAccesAuthorVO = null;		
		//TOP MENU 목록
		List<ProgrmAccesAuthorVO> topMenuList = new ArrayList<ProgrmAccesAuthorVO>();
		//선택된 TOP MENU
		ProgrmAccesAuthorVO topProgrmAccesAuthorVO = null;
		//LEFT MENU 목록
		List<ProgrmAccesAuthorVO> leftMenuList = new ArrayList<ProgrmAccesAuthorVO>();
				
		for(ProgrmAccesAuthorVO progrmAccesAuthorVO:progrmAccesAuthorVOList){			
			//선택된 LEFT MENU
			if(programUri != null
					&& progrmAccesAuthorVO.getProgrmUri() != null
					&& programUri.startsWith(progrmAccesAuthorVO.getProgrmUri())
					&& !ProgrmVO.ROOT_PROGRAM_SN.equals(progrmAccesAuthorVO.getProgrmSn())
					&& !ProgrmVO.ROOT_PROGRAM_SN.equals(progrmAccesAuthorVO.getUpperProgrmSn())){				
				selectedProgrmAccesAuthorVO = progrmAccesAuthorVO;
				
				session.removeAttribute(ProgrmAccesAuthorVO.SELECT_LEFT_MENU);
				session.setAttribute(ProgrmAccesAuthorVO.SELECT_LEFT_MENU, progrmAccesAuthorVO);
			}
			
			if(ProgrmVO.ROOT_PROGRAM_SN.equals(progrmAccesAuthorVO.getUpperProgrmSn())
					&& "N".equals(progrmAccesAuthorVO.getDeleteYn())
					&& "Y".equals(progrmAccesAuthorVO.getMenuIndictYn())
					){
				topMenuList.add(progrmAccesAuthorVO);
			}
			
			if(programUri != null
					&& progrmAccesAuthorVO.getProgrmUri() != null
					&& progrmAccesAuthorVO.getProgrmUri().startsWith(programUri.substring(0, programUri.lastIndexOf("/")+1)) 
					&& !ProgrmVO.ROOT_PROGRAM_SN.equals(progrmAccesAuthorVO.getProgrmSn())
					&& !ProgrmVO.ROOT_PROGRAM_SN.equals(progrmAccesAuthorVO.getUpperProgrmSn())){
				isAccessPermit = true;
			}
		}
		
		
		
		if(selectedProgrmAccesAuthorVO == null){
			//유사한것이 있을 경우 같은
			ProgrmAccesAuthorVO sessionProgrmAccesAuthorVO = (ProgrmAccesAuthorVO)session.getAttribute(ProgrmAccesAuthorVO.SELECT_LEFT_MENU);
			
			if(sessionProgrmAccesAuthorVO != null
					&& sessionProgrmAccesAuthorVO.getProgrmUri().startsWith(programUri.substring(0, programUri.lastIndexOf("/")+1))){
				selectedProgrmAccesAuthorVO = sessionProgrmAccesAuthorVO;
			}
		}
		
		
		if(selectedProgrmAccesAuthorVO != null){
			//선택된 TOP MENU
			for(ProgrmAccesAuthorVO progrmAccesAuthorVO:progrmAccesAuthorVOList){
				if(ProgrmVO.ROOT_PROGRAM_SN.equals(progrmAccesAuthorVO.getUpperProgrmSn())
						&& selectedProgrmAccesAuthorVO.getUpperProgrmSn().equals(progrmAccesAuthorVO.getProgrmSn())){
					topProgrmAccesAuthorVO = progrmAccesAuthorVO;	
				}
				
				for(int i=0; i<topMenuList.size(); i++) {
					if(progrmAccesAuthorVO.getUpperProgrmSn().equals(topMenuList.get(i).getProgrmSn())
							&& !ProgrmVO.ROOT_PROGRAM_SN.equals(progrmAccesAuthorVO.getUpperProgrmSn())
							&& "N".equals(progrmAccesAuthorVO.getDeleteYn())
							&& "Y".equals(progrmAccesAuthorVO.getMenuIndictYn())
							){
						isExist = false;
						for(ProgrmAccesAuthorVO vo : topMenuList.get(i).getProgrmList()) {
							if(vo.getProgrmSn().equals(progrmAccesAuthorVO.getProgrmSn())) {
								isExist = true;
								break;
							}
						}
						
						if(!isExist) {
							topMenuList.get(i).getProgrmList().add(progrmAccesAuthorVO);
						}
					}
				}
				
			}
		}
		
		request.setAttribute(ProgrmAccesAuthorVO.TOP_MENU_PROGRMACCESAUTHORVO, topProgrmAccesAuthorVO);
		request.setAttribute(ProgrmAccesAuthorVO.LEFT_MENU_PROGRMACCESAUTHORVO, selectedProgrmAccesAuthorVO);
		request.setAttribute(ProgrmAccesAuthorVO.TOP_MENU_LIST, topMenuList);
		request.setAttribute(ProgrmAccesAuthorVO.LEFT_MENU_LIST, leftMenuList);
		
		if(!isAccessPermit){
			//접근 불가 url 호출 인식
			//session.setAttribute(ITSMDefine.ERROR_MESSAGE, "접근권한이 불충분 합니다.");			
			//response.sendRedirect(request.getContextPath()+ "/itsm/main/index.do");
		}
		
		try{
			//사용이력 추가
			HistUseVO histUseVO = new HistUseVO();
			histUseVO.setSessionId(session.getId());
			histUseVO.setUserId(loginUserVO.getUserId());
			histUseVO.setUserTyCode(loginUserVO.getUserTyCode());
			histUseVO.setRequestUri(uri);
			histUseVO.setRequestMethod(request.getMethod());
			
			if(selectedProgrmAccesAuthorVO != null){
				histUseVO.setTitle(topProgrmAccesAuthorVO.getProgrmNm());
				histUseVO.setSubMenuTitle(selectedProgrmAccesAuthorVO.getProgrmNm());
				if(topProgrmAccesAuthorVO != null){
					histUseVO.setMenuTitle(topProgrmAccesAuthorVO.getProgrmNm()+">"+selectedProgrmAccesAuthorVO.getProgrmNm());
				}
			}
			histUseService.create(histUseVO);
		}catch(Exception e){
			LOGGER.error("사용이력 저장중 오류발생: "+e.getMessage());
		}
		
		return super.preHandle(request, response, handler);
	}
	
	
	public static void main(String[] args) {
		String uri = "/dkskd/dksidl/kdieldks.do";
		
		System.out.println(uri.substring(0, uri.lastIndexOf("/")+1));
	}
}
