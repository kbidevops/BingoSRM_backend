package kr.go.smplatform.itsm.cmmncode.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import kr.go.smplatform.itsm.base.web.BaseController;
import kr.go.smplatform.itsm.cmmncode.service.CmmnCodeTyService;
import kr.go.smplatform.itsm.cmmncode.vo.CmmnCodeTyVO;
import kr.go.smplatform.itsm.cmmncode.vo.CmmnCodeVO;
import kr.go.smplatform.itsm.report.web.RepDetailController;
import kr.go.smplatform.itsm.user.vo.UserVO;

@Controller
public class CmmnCodeConroller extends BaseController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RepDetailController.class);
	
	@Resource(name = "cmmnCodeTyService")
	CmmnCodeTyService cmmnCodeTyService;
	
	/**
	 * 조회
	 * @param cmmnCodeVO
	 * @param session
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/cmmncode/mngr/retrieveList.do")
	public String retrieveList(
			CmmnCodeVO cmmnCodeVO,
			HttpSession session,
			Model model) 
	throws Exception {
		cmmnCodeVO.setDeleteYn(null);
		model.addAttribute("tyList", cmmnCodeTyService.retrieveAllList());
		if(cmmnCodeVO.getCmmnCodeTy() != null) {
			cmmnCodeVO.setDeleteYn("");
			model.addAttribute("resultList", cmmnCodeService.retrieveList(cmmnCodeVO));
		}
		
		return "/itsm/cmmn/mngr/list";
	}
	
	/**
	 * 코드 삭제 여부 변경
	 * @param deleteYns
	 * @param cmmnCodeVO
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/cmmncode/mngr/delete.do")
	public String delete(
			@RequestParam(value = "deleteYns", required = false) List<String> deleteYns,
			CmmnCodeVO cmmnCodeVO)
	throws Exception {
		
		cmmnCodeVO.setDeleteYn("Y");
		List<CmmnCodeVO> deleteList = cmmnCodeService.retrieveList(cmmnCodeVO);
		
		for(CmmnCodeVO deleteVO : deleteList) {
			cmmnCodeService.restore(deleteVO);
		}
		
		if(deleteYns.size() > 0) {
			for(String deleteCode : deleteYns) {
				CmmnCodeVO deleteVO = new CmmnCodeVO();
				deleteVO.setCmmnCode(deleteCode);
				cmmnCodeService.delete(deleteVO);
			}
		}
		
		return "redirect:/cmmncode/mngr/retrieveList.do";
	}
	
	/**
	 * 코드 수정
	 * @param cmmnCodes
	 * @param cmmnCodeNms
	 * @param sortNos
	 * @param cmmnCodeDcs
	 * @param cmmnCodeTy
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/cmmncode/mngr/update.do")
	public String save(
			@RequestParam(value = "deleteYnTys", required = false) List<String> deleteYnTys,
			@RequestParam(value = "cmmnCodeTys", required = false) List<String> cmmnCodeTys,
			@RequestParam(value = "cmmnCodeTyNms", required = false) List<String> cmmnCodeTyNms,
			@RequestParam(value = "cmmnCodeTyDcs", required = false) List<String> cmmnCodeTyDcs,
			@RequestParam(value = "deleteYns", required = false) List<String> deleteYns,
			@RequestParam(value = "cmmnCodes", required = false) List<String> cmmnCodes,
			@RequestParam(value = "cmmnCodeNms", required = false) List<String> cmmnCodeNms,
			@RequestParam(value = "cmmnCodeSubNm1s", required = false) List<String> cmmnCodeSubNm1s,
			@RequestParam(value = "sortNos", required = false) List<String> sortNos,
			@RequestParam(value = "cmmnCodeDcs", required = false) List<String> cmmnCodeDcs,
			String cmmnCodeTy,
			HttpSession session) 
	throws Exception {
		UserVO loginUserVO = (UserVO) session.getAttribute(UserVO.LOGIN_USER_VO);
		
		if(cmmnCodeTys != null) {
			CmmnCodeTyVO cmmnCodeTyVO = new CmmnCodeTyVO();
			for(int i=0; i<cmmnCodeTys.size(); i++) {
				cmmnCodeTyVO.setCmmnCodeTy(cmmnCodeTys.get(i));
				cmmnCodeTyVO.setCmmnCodeTyNm(cmmnCodeTyNms.get(i));
				cmmnCodeTyVO.setCmmnCodeTyDc(cmmnCodeTyDcs.get(i));
				cmmnCodeTyVO.setDeleteYn(null);
				
				if(cmmnCodeTyService.retrieveList(cmmnCodeTyVO).size() == 0) {
					cmmnCodeTyVO.setCreatId(loginUserVO.getUserId());
					cmmnCodeTyService.create(cmmnCodeTyVO);
				}else{
					
					if(deleteYnTys != null) {
						for(String deleteYnCode : deleteYnTys) {
							if(deleteYnCode.equals(cmmnCodeTys.get(i))) {
								cmmnCodeTyVO.setDeleteYn("Y");
								break;
							}else {
								cmmnCodeTyVO.setDeleteYn("N");
							}
						}
					}
					
					cmmnCodeTyVO.setUpdtId(loginUserVO.getUserId());
					cmmnCodeTyService.update(cmmnCodeTyVO); 
				}
			}
			
		}
		if(cmmnCodes != null) {
			CmmnCodeVO cmmnCodeVO = new CmmnCodeVO();
			for(int i=0; i<cmmnCodes.size(); i++) {
				cmmnCodeVO.setCmmnCodeTy(cmmnCodeTy);
				cmmnCodeVO.setCmmnCode(cmmnCodes.get(i));
				cmmnCodeVO.setCmmnCodeNm(cmmnCodeNms.get(i));
				cmmnCodeVO.setSortNo(sortNos.get(i));
				cmmnCodeVO.setCmmnCodeDc(cmmnCodeDcs.get(i));
				cmmnCodeVO.setCmmnCodeSubNm1(cmmnCodeSubNm1s.get(i));
				
				if(cmmnCodeService.retrieve(cmmnCodeVO) == null) {
					cmmnCodeVO.setCreatId(loginUserVO.getUserId());
					cmmnCodeService.create(cmmnCodeVO);
				}else{
					cmmnCodeService.restore(cmmnCodeVO);
					if(deleteYns != null) {
						for(int j=0; j<deleteYns.size(); j++) {
							if(cmmnCodes.get(i).equals(deleteYns.get(j))) {
								cmmnCodeService.delete(cmmnCodeVO);
							}
						}
					}
					cmmnCodeVO.setUpdtId(loginUserVO.getUserId());
					cmmnCodeService.update(cmmnCodeVO);
				}
			}
		}
		return "redirect:/cmmncode/mngr/retrieveList.do";
	}
	
	/**
	 * cmmnCode 목록 가져오기
	 * @param cmmnCodes
	 * @param cmmnCodeTy
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/cmmncode/mngr/cmmnCodeValidationAjax.do")
	public String cmmnCodeValidationAjax(
			@RequestParam(value = "deleteYnTys", required = false) List<String> deleteYnTys,
			@RequestParam(value = "cmmnCodes", required = false) List<String> cmmnCodes,
			@RequestParam(value = "cmmnCodeTys", required = false) List<String> cmmnCodeTys,
			String cmmnCodeTy,
			Model model) 
	throws Exception {
		CmmnCodeVO vo = new CmmnCodeVO();
		
		//코드 중복 검사
		if(cmmnCodeTys!= null) {
			for(int i=0; i<cmmnCodeTys.size(); i++) {
				int cnt = 0;
				for(int j=0; j<cmmnCodeTys.size(); j++) {
					if (cmmnCodeTys.get(i).equals(cmmnCodeTys.get(j))) {
						cnt++;
					}
					if(cnt == 2) {
						model.addAttribute("returnMessage", cmmnCodeTys.get(i));
						return "jsonView";
					}
				}
			}
		}
		if(cmmnCodes != null) {
			for(int i=0; i<cmmnCodes.size(); i++) {
				int cnt = 0;
				for(int j=0; j<cmmnCodes.size(); j++) {
					if (cmmnCodes.get(i).equals(cmmnCodes.get(j))) {
						cnt++;
					}
					if(cnt == 2) {
						model.addAttribute("returnMessage", cmmnCodes.get(i));
						return "jsonView";
					}
				}
			}
		}
		
		model.addAttribute("returnMessage", "");
		
		
		return "jsonView";
	}
	
	/**
	 * 하위 코드 조회
	 * @param cmmnCodeTy
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/cmmncode/mngr/retrieveCmmnCodeAjax.do")
	public String retrieveCmmnCodeAjax(
			String cmmnCodeTy,
			Model model) 
	throws Exception {
		
		CmmnCodeVO vo = new CmmnCodeVO();
		vo.setCmmnCodeTy(cmmnCodeTy);
		vo.setDeleteYn(null);
		model.addAttribute("resultList", cmmnCodeService.retrieveList(vo));
		
		return "jsonView";
	}
	
	
}
