package kr.go.smplatform.itsm.base.excel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import kr.go.smplatform.itsm.base.file.DownLoadHelper;
import net.sf.jxls.transformer.XLSTransformer;

public class JXLSHelper {
	public static void setHeader(HttpServletRequest request, HttpServletResponse response, Map<String, Object> modelMap) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String dateStr = sdf.format(new Date());
		
		String excelName = modelMap.get("excelName").toString() + "_" + dateStr + ".xls";

		//response.setContentType("Application/Msexcel");
//		response.setHeader("Content-Disposition", DownLoadHelper.setDisposition(excelName, request, response));
		String mimetype = "application/x-msdownload";
		response.setContentType(mimetype);
		DownLoadHelper.setDisposition(excelName, request, response);
		
	}
	
	public static void generateXLSTansformer(HttpServletRequest request, HttpServletResponse response, Map<String, Object> modelMap) throws Exception {
		modelMap.put("templateFilePath", getTmeplateFilePath(request, modelMap));
		
		File file = new File(String.valueOf(modelMap.get("templateFilePath")));
		
		if (!file.isFile()) {
			throw new Exception("템플릿 양식 파일이 존재하지 않습니다.");
		}
		
		InputStream is = new BufferedInputStream(new FileInputStream(file));
		
		
		OutputStream os = response.getOutputStream();
		
		XLSTransformer transformer = new XLSTransformer();
		Workbook workbook = transformer.transformXLS(is, modelMap);
		
		workbook.write(os);
		
		
		os.flush();
		
		if (os != null) os.close();
		if (is != null) is.close();
	}
	
	/***
	 * 실제 경로 조회
	 */
	public static String getRealPath(HttpServletRequest request){
		return request.getSession().getServletContext().getRealPath("/");
	}
	
	public static String getTmeplateFilePath(HttpServletRequest request, Map<String, Object> map) {
		String realPath = getRealPath(request);		

		return realPath + "WEB-INF/template/excel" + "/" + String.valueOf(map.get("templateName"));
	}
	
	public static void generateWorkBook(HttpServletResponse response, Map<String, Object> modelMap) throws IOException {
		generateWorkBook(response, null, modelMap);
	}
	
	public static void generateWorkBook(HttpServletResponse response, HSSFWorkbook workbook, Map<String, Object> modelMap) throws IOException {
		
		boolean isWorkbookExists = true;
		
		if (workbook == null) {
			isWorkbookExists = false;
			workbook = new HSSFWorkbook();
		}

		@SuppressWarnings("unchecked")
		List<LinkedHashMap<String, Object>> list = (List<LinkedHashMap<String, Object>>) modelMap.get("excelList");
		
		workbook.createSheet();
		
		if (list.isEmpty()) {
			generateNoData(workbook);
		} else {
			generateData(workbook, list);
		}

		if (!isWorkbookExists) {
			OutputStream os = response.getOutputStream();
			
			workbook.write(os);
			os.flush();
			
			if (os != null) os.close();
		}
	}

	private static void generateNoData(Workbook workbook){
		Sheet sheet = workbook.getSheetAt(0);
		
		Row headerRow = sheet.createRow(0);
		
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
		
		Font font = workbook.createFont();
		font.setColor(Font.COLOR_RED);
		
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFont(font);
		
		Cell cell = headerRow.createCell(0);
		cell.setCellStyle(cellStyle);
		cell.setCellValue("데이터가 존재하지 않습니다.");
	}
	
	private static void generateData(Workbook workbook, List<LinkedHashMap<String, Object>> list){
		Sheet sheet = workbook.getSheetAt(0);
		
		Row headerRow = sheet.createRow(0);
		
		LinkedHashMap<String, Object> map = list.get(0);
		
		Iterator<String> it = map.keySet().iterator();

		List<String> keyList = new ArrayList<String>();
		while (it.hasNext()) {
			keyList.add(it.next());
		}
		
		for (int i = 0; i < keyList.size(); i++) {
			Cell headerCell = headerRow.createCell(i);
			headerCell.setCellStyle(getHeaderCellStyle(workbook));
			headerCell.setCellValue(keyList.get(i));
		}
		
		for (int rownum = 0; rownum < list.size(); rownum++) {
			Row row = sheet.createRow(rownum + 1);
			
			for (int celnum = 0; celnum < keyList.size();  celnum++){
				Cell cell = row.createCell(celnum);
				cell.setCellStyle(getBodyCellStyle(workbook));
				cell.setCellValue(String.valueOf(list.get(rownum).get(keyList.get(celnum))));
				sheet.autoSizeColumn(celnum);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private static CellStyle getHeaderCellStyle(Workbook workbook) {
		CellStyle cellStyle = workbook.createCellStyle();
//		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		return cellStyle;
	}
	
	@SuppressWarnings("deprecation")
	private static CellStyle getBodyCellStyle(Workbook workbook) {
		CellStyle cellStyle = workbook.createCellStyle();
//		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyle.setBorderRight(CellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
		
		return cellStyle;
	}
}
