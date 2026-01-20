package kr.go.smplatform.itsm.user.web;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.go.smplatform.itsm.atchmnfl.service.AtchmnflService;
import kr.go.smplatform.itsm.atchmnfl.vo.AtchmnflVO;
import kr.go.smplatform.itsm.cmmncode.service.CmmnCodeService;
import kr.go.smplatform.itsm.repcharger.dao.RepChargerMapper;
import kr.go.smplatform.itsm.repcharger.service.RepChargerService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.validator.GenericValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import kr.go.smplatform.itsm.base.web.BaseController;
import kr.go.smplatform.itsm.cmmncode.vo.CmmnCodeVO;
import kr.go.smplatform.itsm.config.ITSMDefine;
import kr.go.smplatform.itsm.user.service.UserService;
import kr.go.smplatform.itsm.user.vo.UserFormVO;
import kr.go.smplatform.itsm.user.vo.UserVO;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UserMngrController extends BaseController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserMngrController.class);
	
	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "repChargerService")
	private RepChargerService repChargerService;

	@Resource(name = "cmmnCodeService")
	private CmmnCodeService cmmnCodeService;

	@Resource(name = "atchmnflService")
	private AtchmnflService atchmnflService;
	
	/**
	 * 사용자 목록을 조회한다. (pageing)
	 * @param userFormVO.getSearchUserVO() - 조회할 정보가 담긴 UserVO
	 * @param model
	 * @return "/user/mngr/egovSampleList"
	 * @exception Exception
	 */
    @RequestMapping(value="/user/mngr/retrievePagingList.do")
    public String retrievePagingList(UserFormVO userFormVO, ModelMap model) throws Exception {
    	/** EgovPropertyService.sample */
    	userFormVO.getSearchUserVO().setPageUnit(propertiesService.getInt("pageUnit"));
    	userFormVO.getSearchUserVO().setPageSize(propertiesService.getInt("pageSize"));
    	
    	/** pageing setting */
    	final PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(userFormVO.getSearchUserVO().getPageIndex());
		paginationInfo.setRecordCountPerPage(userFormVO.getSearchUserVO().getPageUnit());
		paginationInfo.setPageSize(userFormVO.getSearchUserVO().getPageSize());
		
		userFormVO.getSearchUserVO().setFirstIndex(paginationInfo.getFirstRecordIndex());
		userFormVO.getSearchUserVO().setLastIndex(paginationInfo.getLastRecordIndex());
		userFormVO.getSearchUserVO().setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
//		userFormVO.getSearchUserVO().setDeleteYn("N");
		
		final List<UserVO> list = userService.retrievePagingList(userFormVO.getSearchUserVO());
        model.addAttribute("resultList", list);
        
        retrieveCmmnCode(model);
        
        final int totalCount = userService.retrievePagingListCnt(userFormVO.getSearchUserVO());
		paginationInfo.setTotalRecordCount(totalCount);
        model.addAttribute("paginationInfo", paginationInfo);
        
        return "/itsm/user/mngr/list";
    }

	private void retrieveCmmnCode(ModelMap model) throws Exception {
        model.addAttribute("userTyCodeVOList", cmmnCodeService.retrieveList(
				new CmmnCodeVO(CmmnCodeVO.USER_TY_CODE)
		));

        model.addAttribute("userSttusCodeVOList", cmmnCodeService.retrieveList(
				new CmmnCodeVO(CmmnCodeVO.USER_STTUS_CODE)
		));
	} 
 
    /**
	 * 글 등록 화면을 조회한다.
	 * @param userFormVO.getSearchUserVO() - 목록 조회조건 정보가 담긴 VO
	 * @param model
	 * @return "/user/mngr/egovSampleRegister"
	 * @exception Exception
	 */
    @RequestMapping("/user/mngr/createView.do")
    public String createView(UserFormVO userFormVO, HttpSession session, ModelMap model) throws Exception {
    	userFormVO.setUserVO(new UserVO());
    	retrieveCmmnCode(model);

		final CmmnCodeVO cmmnCodeVO = new CmmnCodeVO();
		cmmnCodeVO.setDeleteYn("N");
		cmmnCodeVO.setCmmnCodeTy("L0");

		final List<CmmnCodeVO> locationCodes = cmmnCodeService.retrieveList(cmmnCodeVO);
		model.addAttribute("locationCodes", locationCodes);
    	
//    	userFormVO.getUserVO().setUserTyCode(UserVO.USER_TY_CODE_NORMAL);    	
    	
    	// 중복등록 방지 처리
    	final String saveToken = ITSMDefine.generateSaveToken(session);
    	userFormVO.getUserVO().setSaveToken(saveToken);

        return "/itsm/user/mngr/edit";
    }
    
    /**
	 * 글을 등록한다.
	 * @param userVO - 등록할 정보가 담긴 VO
	 * @param userFormVO.getSearchUserVO() - 목록 조회조건 정보가 담긴 VO
	 * @param status
	 * @return "forward:/user/mngr/retrievePagingList.do"
	 * @exception Exception
	 */
    @RequestMapping("/user/mngr/create.do")
    public String create(HttpSession session, UserFormVO userFormVO, BindingResult bindingResult) throws Exception {
    	// Server-Side Validation
//    	beanValidator.validate(userFormVO, bindingResult);
//    	
//    	if (bindingResult.hasErrors()) {
////    		model.addAttribute("userVO", userFormVO.getUserVO());
//			return "/itsm/user/mngr/edit";
//    	}
    	
    	// 중복등록 방지 처리
    	if (!ITSMDefine.checkSaveToken(session, userFormVO.getUserVO().getSaveToken())) {
    		return "forward:/user/mngr/retrievePagingList.do";
    	}
    	
    	// session에서 로그인 정보를 가져온다.
    	final UserVO loginUserVO =  (UserVO)session.getAttribute(UserVO.LOGIN_USER_VO);
    	
    	userFormVO.getUserVO().setCreatId(loginUserVO.getUserId());
    	
    	if (!userService.create(userFormVO.getUserVO())) {
    		session.setAttribute(ITSMDefine.ERROR_MESSAGE, "이미 등록된 아이디 입니다.");
    	}
    	
        return "forward:/user/mngr/retrievePagingList.do";
    }
    
    /**
	 * 글 수정화면을 조회한다.
	 * @param id - 수정할 글 id
	 * @param userFormVO.getSearchUserVO() - 목록 조회조건 정보가 담긴 VO
	 * @param model
	 * @return "/user/mngr/egovSampleRegister"
	 * @exception Exception
	 */
    @RequestMapping("/user/mngr/updateView.do")
    public String updateView(UserFormVO userFormVO, ModelMap model) throws Exception {
    	userFormVO.setUserVO(userService.retrieve(userFormVO.getUserVO()));
        retrieveCmmnCode(model);

		final CmmnCodeVO cmmnCodeVO = new CmmnCodeVO();
		cmmnCodeVO.setDeleteYn("N");
		cmmnCodeVO.setCmmnCodeTy("L0");

		final List<CmmnCodeVO> locationCodes = cmmnCodeService.retrieveList(cmmnCodeVO);
		model.addAttribute("locationCodes", locationCodes);

        return "/itsm/user/mngr/edit";
    }

    /**
	 * 글을 수정한다.
	 * @param userVO - 수정할 정보가 담긴 VO
	 * @param userFormVO.getSearchUserVO() - 목록 조회조건 정보가 담긴 VO
	 * @param status
	 * @return "forward:/user/mngr/retrievePagingList.do"
	 * @exception Exception
	 */
    @RequestMapping("/user/mngr/update.do")
    public String update(HttpSession session, UserFormVO userFormVO, MultipartFile signature, BindingResult bindingResult, ModelMap model) throws Exception {
    	LOGGER.debug("userVO: " + userFormVO.getUserVO());
    	
    	// validator 통과하기 위해 어쩔 수 없이 추가, 전자정부프레임워크에서 필수 옵션 제외 처리가 안되어 있어서 불가피함.
    	if (GenericValidator.isBlankOrNull(userFormVO.getUserVO().getUserPassword())) {
    		userFormVO.getUserVO().setUserPassword(UserVO.DEFAULT_USER_PASSWORD);
    	}
    	
//    	beanValidator.validate(userFormVO, bindingResult);
    	
//    	if (bindingResult.hasErrors()) {
//    		retrieveCmmnCode(model);
//
//			return "/itsm/user/mngr/edit";
//    	}
    	
    	// validator 통과하기 위해 어쩔 수 없이 추가, 전자정부프레임워크에서 필수 옵션 제외 처리가 안되어 있어서 불가피함.
    	if (UserVO.DEFAULT_USER_PASSWORD.equals(userFormVO.getUserVO().getUserPassword())) {
    		userFormVO.getUserVO().setUserPassword(null);
    	}
    	
    	final UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
    	
    	userFormVO.getUserVO().setUpdtId(loginUserVO.getUserId());

		if ("DELETE".equals(userFormVO.getUserVO().getDeleteSignature())) {
			userService.deleteSignature(userFormVO.getUserVO().getUserId());
		}
/*
		if (!signature.isEmpty()) {
			final Date now = new Date();
			final Path path = Paths
					.get(ITSMDefine.rootPath)
					.resolve("file")
					.resolve(
							new SimpleDateFormat("yyyy")
									.format(now)
					)
					.resolve(
							new SimpleDateFormat("MM")
									.format(now)
					);
			final String fileSaveName = UUID.randomUUID().toString();
			final Path streAllCours = path.resolve(fileSaveName);

			path.toFile().mkdirs();
			signature.transferTo(streAllCours.toFile());

			final AtchmnflVO atchmnflVO = new AtchmnflVO();
			final String fileId = UUID.randomUUID().toString();
			atchmnflVO.setAtchmnflId(fileId);
			atchmnflVO.setStreAllCours(streAllCours.toAbsolutePath().toString());
			atchmnflVO.setOrginlFileNm(signature.getOriginalFilename());
			atchmnflVO.setFileSize(signature.getSize());

			atchmnflService.create(atchmnflVO);

			userFormVO.getUserVO().setUserSignature(fileId);
		}
*/
		userService.update(userFormVO.getUserVO());
		repChargerService.updateUserLocat(userFormVO.getUserVO());

        return "forward:/user/mngr/retrievePagingList.do";
    }
    
    /**
	 * 글을 삭제한다.
	 * @param userVO - 삭제할 정보가 담긴 VO
	 * @param userFormVO.getSearchUserVO() - 목록 조회조건 정보가 담긴 VO
	 * @param status
	 * @return "forward:/user/mngr/retrievePagingList.do"
	 * @exception Exception
	 */
    @RequestMapping("/user/mngr/delete.do")
    public String delete(UserFormVO userFormVO, HttpSession session) throws Exception {
    	final UserVO loginUserVO =  (UserVO)session.getAttribute(UserVO.LOGIN_USER_VO);
    	
    	userFormVO.getUserVO().setUpdtId(loginUserVO.getUserId());
    	
    	LOGGER.info("userVO: " + userFormVO.getUserVO());
        userService.delete(userFormVO.getUserVO());
//        status.setComplete();
        return "forward:/user/mngr/retrievePagingList.do";
    }

	/**
	 * AJAX를 통해 사용중인 ID인지 검사 한다.
	 * @param userVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value = "/user/mngr/retrieveAjax.do")    
    public String retrieveAjax(UserVO userVO, ModelMap model) throws Exception {
    	final UserVO existUserVO = userService.retrieve(userVO);
    	
    	// 자료실은 특별히 다운로드 제약이 없으므로 파일에 대한 접근 권한은 체크 하지 않는다.
    	if (existUserVO == null) {
	    	model.addAttribute("returnMessage", "success");
		}
		else {
			model.addAttribute("returnMessage", "fail");
		}
    	
    	return "jsonView";
	}
}
