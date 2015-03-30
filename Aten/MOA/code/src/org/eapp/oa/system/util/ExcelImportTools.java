/**
 * 
 */
package org.eapp.oa.system.util;

import java.text.NumberFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.eapp.oa.system.exception.OaException;

/**
 * @author Administrator
 *
 */
public class ExcelImportTools {
	public static String cellValueToString(Cell cell) throws OaException {
		if (cell == null) {
			return null;
		}
		try {
			if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
				return null;
			} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {// 是数字
				return NumberFormat.getNumberInstance().format(cell.getNumericCellValue());
			} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {// 是公式
				try {
					return NumberFormat.getNumberInstance().format(cell.getNumericCellValue());
				} catch (IllegalStateException e) {
					return cell.getRichStringCellValue().toString().trim();
				}
			} else {// 字符串
				return cell.getRichStringCellValue().toString().trim();
			}
		} catch(Exception e) {
			e.printStackTrace();
			throw new OaException("读取单元格出错（文本类型）：" + int2ColumnName(cell.getColumnIndex()) + (cell.getRowIndex() + 1));
		}
	}
	
	public static Float cellValueToFloat(Cell cell) throws OaException {
		if (cell == null) {
			return null;
		}
		try {
			if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
				return null;
			} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				return new Double(cell.getNumericCellValue()).floatValue();
			} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {// 是公式
				return new Double(cell.getNumericCellValue()).floatValue();
			} else {
				return Float.parseFloat(cell.getRichStringCellValue().toString());
			}
		} catch(Exception e) {
			e.printStackTrace();
			throw new OaException("读取单元格出错（浮点类型）：" + int2ColumnName(cell.getColumnIndex()) + (cell.getRowIndex() + 1));
		}
	}
	
	public static Integer cellValueToInt(Cell cell) throws OaException {
		if (cell == null) {
			return null;
		}
		try {
			if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
				return null;
			} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				return new Integer((int)(cell.getNumericCellValue()));
			} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {// 是公式
				return new Integer((int)(cell.getNumericCellValue()));
			} else {
				return Integer.parseInt(cell.getRichStringCellValue().toString());
			}
		} catch(Exception e) {
			e.printStackTrace();
			throw new OaException("读取单元格出错（整数类型）：" + int2ColumnName(cell.getColumnIndex()) + (cell.getRowIndex() + 1));
		}
	}

	public static Date cellValueToDate(Cell cell) throws OaException {
		if (cell == null) {
			return null;
		}
		try {
			if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				return cell.getDateCellValue();
			} else {
				return null;
			}
		} catch(Exception e) {
			e.printStackTrace();
			throw new OaException("读取单元格出错（日期类型）：" + int2ColumnName(cell.getColumnIndex()) + (cell.getRowIndex() + 1));
		}
	}
	
	/**
	 * EXCEL列号转换
	 * @param column
	 * @return
	 */
	public static String int2ColumnName(int column) {  
		int col = column + 1;  
		int system = 26;  
		StringBuffer bf = new StringBuffer();  
		while (col > 0) {  
	         int mod = col % system;  
	         if (mod == 0) {
	        	 mod = system;  
	         }
	         bf.append(dig2Char(mod));  
	         col = (col - 1) / system;  
		}  
	    return bf.reverse().toString();       
	}  
	
	private static char dig2Char(final int dig){  
		int acs = dig - 1 + 'A';  
		return (char)acs;       
	}
}
