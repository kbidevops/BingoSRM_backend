package kr.go.smplatform.itsm.atchmnfl.web;

import kr.go.smplatform.itsm.atchmnfl.service.AtchmnflService;
import kr.go.smplatform.itsm.atchmnfl.vo.AtchmnflVO;
import kr.go.smplatform.itsm.base.file.DownLoadHelper;
import kr.go.smplatform.itsm.base.web.BaseController;
import kr.go.smplatform.itsm.config.ITSMDefine;
import kr.go.smplatform.itsm.srvcrspons.web.SrvcRsponsMngrController;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.validator.GenericValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
public class AtchmnflController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SrvcRsponsMngrController.class);
	
	@Resource(name = "atchmnflService")
	private AtchmnflService atchmnflService;
	
	@RequestMapping(value="/atchmnfl/site/create.do")
	public String create(AtchmnflVO atchmnflVO, ModelMap model) throws Exception {
		final MultipartFile file = atchmnflVO.getMultipartFile();
		final String uploadPath = "file/";
		final String path = ITSMDefine.rootPath+uploadPath+new SimpleDateFormat("yyyy").format(new Date())+"/"+new SimpleDateFormat("MM").format(new Date())+"/";
		
		// 첨부파일이 없을 경우
		if (GenericValidator.isBlankOrNull(atchmnflVO.getAtchmnflId())) {
			atchmnflVO.setAtchmnflId(UUID.randomUUID().toString());
		}

		if (file != null) {
			final String fileSaveName = UUID.randomUUID().toString();
			final String fileSrcName = file.getOriginalFilename();

			LOGGER.debug("fileSaveName: " + fileSaveName);
			LOGGER.debug("fileSrcName: " + fileSrcName);

			final File filePath = new File(path);

			if (!filePath.exists()) {
				filePath.mkdirs();
			}
			final String streAllCours = path + FilenameUtils.getName(fileSaveName);
			LOGGER.debug("streAllCours: " + streAllCours);

			final File tempFile = new File(path + FilenameUtils.getName(streAllCours));
			final FileOutputStream fos = new FileOutputStream(tempFile);
			IOUtils.write(file.getBytes(), fos);
			fos.close();

			atchmnflVO.setStreAllCours(streAllCours);
			atchmnflVO.setOrginlFileNm(fileSrcName);
			atchmnflVO.setFileSize(file.getSize());

			atchmnflService.create(atchmnflVO);
		}

		model.clear();

		if (!GenericValidator.isBlankOrNull(atchmnflVO.getAtchmnflId())) {
			List<AtchmnflVO> resultList = atchmnflService.retrieveList(atchmnflVO);
			model.addAttribute("returnMessage", "success");
	        model.addAttribute("resultList", resultList);
		}
		else {
			model.addAttribute("returnMessage", "fail");
		}

		return "jsonView";
	}

	/**
	 * 첨부파일로 등록된 파일에 대하여 다운로드를 제공한다.
	 * @param atchmnflVO
	 * @param request
	 * @param response
	 * @throws Exception
	 */
    @RequestMapping(value = "/atchmnfl/site/retrieve.do")    
    public void retrieve(AtchmnflVO atchmnflVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	final AtchmnflVO exsitAtchmnflVO = atchmnflService.retrieve(atchmnflVO);
    	
    	// 자료실은 특별히 다운로드 제약이 없으므로 파일에 대한 접근 권한은 체크 하지 않는다.
    	if (exsitAtchmnflVO != null) {
    		final File sourceFile = new File(exsitAtchmnflVO.getStreAllCours());
    		final int sourceFileSize = (int) sourceFile.length();
    		LOGGER.debug("sourceFileSize: " + sourceFileSize);

    		try {
    			response.setHeader("Access-Control-Allow-Origin", "*");
    			downloadFile(request, response, sourceFile, exsitAtchmnflVO.getOrginlFileNm());
    		}
			catch (Exception e) {
    			LOGGER.error(e.getMessage());
    			throw e;
    		}
		}
	}
    
    @RequestMapping(value = "/atchmnfl/site/delete.do")   
    public String delete(AtchmnflVO atchmnflVO, ModelMap model) throws Exception {
    	try {
    		final AtchmnflVO exsitAtchmnflVO = atchmnflService.retrieve(atchmnflVO);
        	
        	// 자료실은 특별히 다운로드 제약이 없으므로 파일에 대한 접근 권한은 체크 하지 않는다.
        	if (exsitAtchmnflVO != null) {
        		final File sourceFile = new File(exsitAtchmnflVO.getStreAllCours());
        		final int sourceFileSize = (int)sourceFile.length();

        		LOGGER.debug("sourceFileSize: "+sourceFileSize);

        		try {
        			FileUtils.forceDelete(sourceFile);
        		}
				catch (Exception e) {
        			LOGGER.error(e.getMessage());
        		}
    		}
        	
        	atchmnflService.delete(atchmnflVO);
        	model.addAttribute("returnMessage", "success");
    	}
		catch (Exception e) {
    		model.addAttribute("returnMessage", "fail");
    	}
    	
    	return "jsonView";
	}

	/**
	 * 파일 다운로드
	 * @param request
	 * @param response
	 * @param downFile
	 * @param sourceFileName
	 * @throws Exception
	 * @throws IOException
	 */
    protected void downloadFile(HttpServletRequest request, HttpServletResponse response, File downFile, String sourceFileName) throws Exception {
    	final int downFileSize = (int) downFile.length();

		if (downFileSize > 0) {
			final String mimetype = "application/x-msdownload";
			
			// response.setBufferSize(fSize);	// OutOfMemeory 발생
			response.setContentType(mimetype);
			// response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fvo.getOrignlFileNm(), "utf-8") + "\"");
			DownLoadHelper.setDisposition(sourceFileName, request, response);
			response.setContentLength(downFileSize);

			/*
			 * FileCopyUtils.copy(in, response.getOutputStream());
			 * in.close(); 
			 * response.getOutputStream().flush();
			 * response.getOutputStream().close();
			 */
			try (final BufferedInputStream in = new BufferedInputStream(new FileInputStream(downFile));
				 final BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
				 ) {

				FileCopyUtils.copy(in, out);
				out.flush();
			}
			catch (Exception ex) {
			    // ex.printStackTrace();
			    // 다음 Exception 무시 처리
			    // Connection reset by peer: socket write error
			    LOGGER.debug("IGNORED: " + ex.getMessage());
			}
		}
		else {
			response.setContentType("text/html; charset=utf-8");

			final PrintWriter printwriter = response.getWriter();
			printwriter.println("<script>alert('" + sourceFileName + "을 다운로드 할 수 없습니다. 관리자에게 문의하세요.');history.back();</script>");
			printwriter.flush();
			printwriter.close();
		}
	}
}
