package kr.go.smplatform.itsm.base.excel;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

public class ExcelView extends AbstractExcelView {
	public static final String TEMPLATE_SR =  "template_sr.xls";
	public static final String TEMPLATE_WDTB =  "template_wdtb.xls";
	public static final String TEMPLATE_FI =  "template_fi.xlsx";
	public static final String TEMPLATE_ASSET =  "template_asset.xlsx";
	public static final String EXCEL_NAME_SR =  "SR_List";
	public static final String EXCEL_NAME_WDTB =  "WDTB_List";
	public static final String EXCEL_NAME_FI =  "FI_List";
	public static final String EXCEL_NAME_ASSET =  "ASSET_List";
	
	protected void buildExcelDocument(Map<String, Object> modelMap, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		JXLSHelper.setHeader(request, response, modelMap);
		
		if (modelMap.get("templateName") != null) {
			JXLSHelper.generateXLSTansformer(request, response, modelMap);
		} else {
			JXLSHelper.generateWorkBook(response, workbook, modelMap);
		}
	}
}
