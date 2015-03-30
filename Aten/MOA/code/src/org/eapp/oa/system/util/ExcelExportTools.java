/**
 * 
 */
package org.eapp.oa.system.util;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * @author zsy
 * @version Mar 4, 2009
 */
public class ExcelExportTools {
	
	private HSSFWorkbook wb;
	
	public ExcelExportTools(HSSFWorkbook wb) {
		this.wb = wb;
	}
	
	public HSSFCellStyle getCellStyle(Short bgColor, boolean center, boolean bold) {
		return getCellStyle(bgColor, center, bold, true);
	}
	
	/**
	 * 设置Excel单元格样式
	 * @param wb 工作簿
	 * @param bgColor 背景颜色
	 * @param center 是否居中
	 * @param bold 是否加粗
	 * @param border 是否加边框
	 * @return
	 */
	public  HSSFCellStyle getCellStyle(Short bgColor, boolean center, boolean bold, boolean border) {
		HSSFCellStyle cellStyle = wb.createCellStyle();
		if (border) {
	        cellStyle.setBorderBottom((short)1);
	        cellStyle.setBorderLeft((short)1);   
	        cellStyle.setBorderRight((short)1);   
	        cellStyle.setBorderTop((short)1);
		}
		if (center) {
			cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直   
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平   
		}
		if (bold) {
			HSSFFont font = wb.createFont();   
	        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);   
	        cellStyle.setFont(font);
		}
		if (bgColor != null) {
			cellStyle.setFillForegroundColor(bgColor.shortValue());   
	        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		}
        return cellStyle;
	}
	
	public HSSFCellStyle getPercentCellStyle(Short bgColor) {
		return getPercentCellStyle(bgColor, true);
	}
	
	/**
	 * 取得百分比样式
	 * @param wb
	 * @return
	 */
	public HSSFCellStyle getPercentCellStyle(Short bgColor, boolean border) {
		HSSFCellStyle cellStyle = wb.createCellStyle();
		if (border) {
	        cellStyle.setBorderBottom((short)1);
	        cellStyle.setBorderLeft((short)1);   
	        cellStyle.setBorderRight((short)1);   
	        cellStyle.setBorderTop((short)1);
		}
        if (bgColor != null) {
			cellStyle.setFillForegroundColor(bgColor.shortValue());   
	        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		}
		cellStyle.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
		return cellStyle;
	}

	public HSSFCellStyle getTitleCellStyle(Short bgColor) {
		HSSFCellStyle cellStyle = wb.createCellStyle();
		HSSFFont font = wb.createFont();   
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
        font.setFontHeightInPoints((short)16);
        cellStyle.setFont(font);
        
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直   
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平
        
        if (bgColor != null) {
			cellStyle.setFillForegroundColor(bgColor.shortValue());   
	        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		}
		cellStyle.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
		return cellStyle;
	}
	
	
	/*
	 * 生成相加公式
	 */
	public static String getSumExpress(int startRowNum, int endRowNum, int celNum) {
		String colName = getColName(celNum);
		//组成相加表达式
		String exp = "SUM(" + colName +(startRowNum + 1) + ":" +colName + (endRowNum + 1) +")";
	
		
		return exp;
	}
	
	/*
	 * 生成相除公式
	 */
	public static String getDivExpress(int startColNum, int endColNum, int rowNum) {

		return getColName(startColNum) + (rowNum + 1) + "/" + getColName(endColNum) + (rowNum + 1);
	}
	
	
	public static String getColName(int celNum) {
		/*
		 * 将0，1，2……转为A，B，C……
		 */
		int num = celNum + 1;//celNum是从0算起
		String tem = "";
		while(num > 0) {
			int lo = (num - 1) % 26;//取余，A到Z是26进制，
			tem = (char)(lo + 'A') + tem;
			num = (num - 1) / 26;//取模
		}
		return tem;
	}
}
