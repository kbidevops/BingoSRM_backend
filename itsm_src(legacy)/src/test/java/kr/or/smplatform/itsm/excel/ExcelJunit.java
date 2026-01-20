package kr.or.smplatform.itsm.excel;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import kr.go.smplatform.itsm.srvcrspons.dao.SrvcRsponsMapper;
import kr.go.smplatform.itsm.srvcrspons.vo.SrvcRsponsVO;
import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/egovframework/spring/context-aspect.xml"
		, "/egovframework/spring/context-common.xml"
		, "/egovframework/spring/context-idgen.xml"
		, "/egovframework/spring/context-datasource.xml"
		, "/egovframework/spring/context-properties.xml"
//		, "/egovframework/spring/context-sqlMap.xml"
		, "/egovframework/spring/context-mapper.xml"
		, "/egovframework/spring/context-transaction.xml"
})
public class ExcelJunit {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelJunit.class);
	
	@Resource(name="srvcRsponsMapper")
	private SrvcRsponsMapper srvcRsponsMapper;
	
	@Test
	public void testInit(){
		Assert.assertNotNull(srvcRsponsMapper);
	}
	
	@Test
	public void testRetrievePagingList() throws IOException, ParsePropertyException, InvalidFormatException{
		List<SrvcRsponsVO> list = null;
		try {
			SrvcRsponsVO paramSrvcRsponsVO = new SrvcRsponsVO();
			paramSrvcRsponsVO.setProcessMt("201711");
			list = srvcRsponsMapper.retrieveAllList(paramSrvcRsponsVO);
			for(SrvcRsponsVO srvcRsponsVO:list){
				LOGGER.info(srvcRsponsVO.toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertNotNull(list);
		
		File file = new File("C:/eGovFrameDev-3.6.0-64bit/workspace/itsm/src/main/webapp/WEB-INF/template/excel/template_sr.xls");
//		ZipSecureFile.setMinInflateRatio(-1.0d);
		
		InputStream is = new BufferedInputStream(new FileInputStream("C:/eGovFrameDev-3.6.0-64bit/workspace/itsm/src/main/webapp/WEB-INF/template/excel/template_sr.xls"));
		OutputStream os =  new BufferedOutputStream(new FileOutputStream("D:/temp/sr_20171130.xls"));
		
//		Context context = new Context();
//        context.putVar("srvcRsponsVOS", list);
//        try {
//			JxlsHelper.getInstance().processTemplate(is, os, context);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("srvcRsponsVO", list);
		XLSTransformer transformer = new XLSTransformer();
		Workbook workbook = transformer.transformXLS(is, modelMap);
		workbook.write(os);
        os.flush();
		
		if (os != null) os.close();
		if (is != null) is.close();
		
	}
	
}
