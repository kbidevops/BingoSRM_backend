package kr.go.smplatform.itsm.repAttd.web;

import kr.go.smplatform.itsm.repAttd.service.RepAttdService;
import kr.go.smplatform.itsm.repAttd.vo.RepAttdVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
public class RepAttdController {
	
	@Resource(name = "repAttdService")
	private RepAttdService repAttdService;

	@RequestMapping(value = "/rep/master/mngr/addAttdCodeAjax.do")
	public String addAttdCodeAjax(RepAttdVO repAttdVO) throws Exception {
		if (repAttdService.retrieve(repAttdVO) != null) {
			repAttdService.update(repAttdVO);
		}
		else {
			repAttdService.createOne(repAttdVO);
		}
		
		return "jsonView";
	}
	
	@RequestMapping(value = "/rep/master/mngr/deleteAttdCodeAjax.do")
	public String deleteAttdCodeAjax(RepAttdVO repAttdVO) throws Exception {
		repAttdService.deleteOne(repAttdVO);
		
		return "jsonView";
	}
}
