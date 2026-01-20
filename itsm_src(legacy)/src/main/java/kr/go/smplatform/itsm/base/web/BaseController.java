package kr.go.smplatform.itsm.base.web;

import egovframework.rte.fdl.property.EgovPropertyService;
import kr.go.smplatform.itsm.cmmncode.service.CmmnCodeService;
import org.springmodules.validation.commons.DefaultBeanValidator;

import javax.annotation.Resource;

public class BaseController {

	/** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;

    /** Validator */
    @Resource(name = "beanValidator")
	protected DefaultBeanValidator beanValidator;
    
    /** CmmnCodeService */
    @Resource(name = "cmmnCodeService")
	protected CmmnCodeService cmmnCodeService;
    
//    @Resource(name = "ARIACryptoService")
//	protected EgovCryptoService cryptoService;
}
