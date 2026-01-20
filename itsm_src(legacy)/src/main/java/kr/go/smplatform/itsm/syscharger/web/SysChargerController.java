package kr.go.smplatform.itsm.syscharger.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.GenericValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.go.smplatform.itsm.base.web.BaseController;
import kr.go.smplatform.itsm.cmmncode.vo.CmmnCodeVO;
import kr.go.smplatform.itsm.progrmaccesauthor.vo.ProgrmAccesAuthorVO;
import kr.go.smplatform.itsm.progrmaccesauthor.web.ProgrmAccesAuthorController;
import kr.go.smplatform.itsm.syscharger.service.SysChargerService;
import kr.go.smplatform.itsm.syscharger.vo.SysChargerVO;
import kr.go.smplatform.itsm.user.service.UserService;
import kr.go.smplatform.itsm.user.vo.UserVO;

@Controller
public class SysChargerController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProgrmAccesAuthorController.class);
	
	@Resource(name = "sysChargerService")
	private SysChargerService sysChargerService;
	
	@Resource(name = "userService")
	private UserService userService;

	/**
	 * 시스템 담당자 목록을 조회한다.
	 * @param sysChargerVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/syscharger/mngr/retrieveList.do")
    public String retrievePagingList(SysChargerVO sysChargerVO, ModelMap model) throws Exception {
		sysChargerVO.setUserTyCode(UserVO.USER_TY_CODE_CHARGER); // 시스템 담당자만 불러오기 위해
		sysChargerVO.setUserSttusCode(UserVO.USER_STTUS_CODE_ALLOW);
		
		final List<UserVO> list = userService.retrieveList(sysChargerVO);
		final List<SysChargerVO> sysChargerVOList = sysChargerService.retrieveAssignList(sysChargerVO);
		
		for (UserVO userVO : list) {
			for (SysChargerVO inSysChargerVO : sysChargerVOList) {
				if (userVO.getUserId().equals(inSysChargerVO.getUserId())) {
					userVO.getSysChargerVOList().add(inSysChargerVO);
				}
			}
		}
		
        model.addAttribute("resultList", list);
        
        return "/itsm/syscharger/mngr/list";
    }

	/**
	 * 특정 시스템 담당자의 시스템 목록을 조회한다.
	 * @param progrmAccesAuthorFormVO.getSearchProgrmVO() - 조회할 정보가 담긴 ProgrmVO
	 * @param model
	 * @return "/user/mngr/egovSampleList"
	 * @exception Exception
	 */
    @RequestMapping(value="/syscharger/mngr/retrieveSysChargerListAjax.do")
    public String retrieveSysChargerListAjax(SysChargerVO sysChargerVO, ModelMap model) {
    	if (!GenericValidator.isBlankOrNull(sysChargerVO.getUserId())) {
	    	LOGGER.debug("sysChargerVO.getUserId(): " + sysChargerVO.getUserId());

	    	try {
		        final List<SysChargerVO> resultList = sysChargerService.retrieveList(sysChargerVO);

		        model.addAttribute("returnMessage", "success");
		        model.addAttribute("resultList", resultList);
	    	}
			catch (Exception e) {
	    		model.addAttribute("returnMessage", "fail");
	    	}
    	} else {
    		model.addAttribute("returnMessage", "fail");
    	}

        return "jsonView";
    }

	/**
	* 특정 시스템 담당자의 시스템 목록을  저장한다.
	* @param sysChargerVO
	* @param model
	* @return
	* @throws Exception
	*/
	@RequestMapping(value="/syscharger/mngr/createListAjax.do")
	public String createListAjax( SysChargerVO sysChargerVO, ModelMap model) {
		try {
       		sysChargerService.createList(sysChargerVO);
   	        model.addAttribute("returnMessage", "success");
       	}
		catch (Exception e) {
       		model.addAttribute("returnMessage", "fail");
       	}

		return "jsonView";
	}
}
