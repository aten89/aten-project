package org.eapp.crm.excelimp;

import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.hssf.usermodel.HSSFCell;

public class ExcelCellToValue {
	public static String cellValueToString(HSSFCell cell){
		if (cell == null) {
			return "";
		}
		if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {// 字符串
			return cell.getRichStringCellValue().toString().trim();
		} else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {// 是否是数字
			String str = NumberFormat.getNumberInstance().format(
					cell.getNumericCellValue());
			if (str.indexOf(",") > -1) {
				str = str.substring(0, str.indexOf(","))
						+ str.substring(str.indexOf(",") + 1);
				str = str.replaceAll(",", "");
			}
			return str;
		} else if (cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {// Boolean类型
			return Boolean.toString(cell.getBooleanCellValue());
		} else if (cell.getCellType() == HSSFCell.CELL_TYPE_ERROR) {// 错误类型
			return Byte.toString(cell.getErrorCellValue());
		}
		return "";
		
	}

	public static int cellValueToInt(HSSFCell cell) {
		if (cell == null) {
			return 0;
		}
		if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			if (cell.getStringCellValue().trim().length()==0) {
				return 0;
			}else{
				return Integer.valueOf(cell.getStringCellValue().trim());
			}
		} else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			return new Integer((int) (cell.getNumericCellValue()));
		}
		return 0;
	}

	public static Date cellValueToDate(HSSFCell cell)  {
		
		if (cell == null) {
			return null;
		}
		if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			return cell.getDateCellValue();
		} else {
			return null;
		}
	}

	public static boolean validEnAndNum(HSSFCell cell, long num){
		String valid = "^\\w+$";
		if (cell == null) {
			return false;
		}else 
			if(cell.toString().trim().length()==0){
				return false;
			}
			if (cell.toString().trim().length() >= num) {
				return true;
			} else {
				if (cell.toString().matches(valid)) {
					return false;
				} else {
					return true;
				}
			
		}
	}

	/**
	 * 验证券商代码 券商代码只能是大写字母
	 * 
	 * @param cell
	 * @param num
	 * @return
	 */
	public static boolean validCustCode(HSSFCell cell, long num) {
		String valid = "^[A-Z]+$";
		if (cell == null) {
			return true;
		}else {
			if (cell.toString().trim().length() >= num) {
				return true;
			} else {
				if (cell.toString().trim().matches(valid)) {
					return false;
				} else {
					return true;
				}
			}
		}

	}

	/**
	 * 字符串A是否包含字符串B
	 * 
	 * @param strA
	 * @param strB
	 * @return
	 */
	public static boolean strAContainStrB(String strA, String strB) {
		if (strA == null || "".equals(strA)) {
			return false;
		} else {
			if (strA.indexOf(strB) != -1) {
				return true;
			} else {
				return false;
			}
		}

	}

	/**
	 * 验证Excel单元格不能为空且不能超过字符长度
	 * 
	 * @param cell
	 * @param num
	 * @return
	 */
	public static boolean validString(HSSFCell cell, long num) {
		
		if (cell == null) {
			return true;
		} else {
			if (ExcelCellToValue.cellValueToString(cell).length() > 0) {
				String val="";
				try {
					val = new String(ExcelCellToValue.cellValueToString(cell).getBytes("GBK"), "ISO8859_1");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				if(val.trim().length()>num){
					return true;
				}else{
					String valid = "<.*?>|'";
					Pattern   p   =   Pattern.compile(valid,   Pattern.CASE_INSENSITIVE); 
					Matcher   m   =   p.matcher(cell.toString().trim()); 
					return   m.find(); 
				}
			} else {
				return true;
			} 
		}

	}

	/**
	 * 验证Excel单元格允许为空且不能超过字符长度
	 * 
	 * @param cell
	 * @param num
	 * @return
	 */
	public static boolean validNuLLString(HSSFCell cell, long num)   {
		if (cell == null) {
			return false;
		}
		if(cell.toString().trim().length()==0){
			return false;
		}
		if (cell.toString().trim().length() >0) {
			String val="";
			try {
				val = new String(cell.toString().trim().getBytes("GBK"), "ISO8859_1");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if(val.trim().length()>num){
				return true;
			}else{
				String valid = "<.*?>|'";
				Pattern   p   =   Pattern.compile(valid,   Pattern.CASE_INSENSITIVE); 
				Matcher   m   =   p.matcher(cell.toString().trim()); 
				return   m.find();
			}
		}
		return false; 

	}

	/**
	 * 验证电话号码
	 * 
	 * @param cell
	 * @param num
	 * @return
	 * @throws CRMException 
	 */
	public static boolean validTelphone(HSSFCell cell,boolean flag){
		// String valid="\\d{3,4}-\\d{7,8}";
		String valid= "^\\d{3,4}-\\d{7,9}(-\\d{1,4})?$";
		Pattern ptt = Pattern.compile(valid);
		if (cell == null) {
			if(flag){
				return true;
			}else{
				return false;
			}
		}
		if (cell.toString().trim().length() == 0) {
			if(flag){
				return true;
			}else{
				return false;
			}
		} else {
			Matcher mch = ptt.matcher(ExcelCellToValue.cellValueToString(cell));
			if (mch.matches()) {
				return false;
			} else {
				return true;
			}

		}
		
	}
	/**
	 * 验证传真
	 * 
	 * @param cell
	 * @param num
	 * @return
	 * @throws CRMException 
	 */
	public static boolean validFax(HSSFCell cell){
		// String valid="\\d{3,4}-\\d{7,8}";
		String valid= "^\\d{3,4}-\\d{7,9}(-\\d{1,4})?$";
		Pattern ptt = Pattern.compile(valid);
		if (cell == null) {
			return false;
		}
		if (cell.toString().trim().length() == 0) {
			return false;
		} else {
			Matcher mch = ptt.matcher(ExcelCellToValue.cellValueToString(cell));
			if (mch.matches()) {
				return false;
			} else {
				return true;
			}

		}
		
	}
	/**
	 * 验证数字
	 * 
	 * @param cell
	 * @param num
	 * @return
	 * @throws CRMException 
	 */
	public static boolean validNum(HSSFCell cell, long num){
		String valid = "^[0-9]*$";
		if (cell == null) {
			return false;
		}
		if (cell.toString().trim().length() == 0) {
			return false;
		} else if (ExcelCellToValue.cellValueToString(cell).length() > num) {
			return true;
		} else {
			if (ExcelCellToValue.cellValueToString(cell).matches(valid)) {
				return false;
			} else {
				return true;
			}

		}
	}

	/**
	 * 验证Email是否合法
	 * 
	 * @param cell
	 * @param num
	 * @return
	 */
	public static boolean validEmail(HSSFCell cell, long num) {
		String valid = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
		if (cell == null) {
			return false;
		}
		if (cell.toString().trim().length() == 0) {
			return false;
		} else if (cell.toString().trim().length() >= num) {
			return true;
		} else {
			if (cell.toString().trim().matches(valid)) {
				return false;
			} else {
				return true;
			}

		}
	}

	/**
	 * 验证邮政编码 邮政编码由6位数字组成
	 * 
	 * @param cell
	 * @return
	 * @throws CRMException 
	 */
	public static boolean validCode(HSSFCell cell) {
		String zipCodePattern = "\\d{6}";
		if (cell == null) {
			return false;
		}
		if (cell.toString().trim().length() == 0) {
			return false;
		} else {
			if (ExcelCellToValue.cellValueToString(cell)
					.matches(zipCodePattern)) {
				return false;
			} else {
				return true;
			}

		}
	}

	/**
	 * 验证URL
	 * 
	 * @param cell
	 * @param num
	 * @return
	 */
	public static boolean validWebsite(HSSFCell cell, long num)   {
		if (cell == null) {
			return false;
		}
		if (cell.toString().trim().length() == 0) {
			return false;
		} else if (cell.toString().trim().length() >= num) {
			return true;
		} else {
				return false;

		}
	}

	/**
	 * 验证移动电话
	 * 
	 * @param cell
	 * @return
	 * @throws CRMException 
	 */
	public static boolean validMobile(HSSFCell cell) {
		String valid = "(^((\\(\\d{3}\\))|(\\d{3}\\-))?\\d{11}$)|(^\\d{3,4}-\\d{7,9}$)";
		if (cell == null) {
			return false;
		}
		if (cell.toString().trim().length() == 0) {
			return false;
		} else {
			if (ExcelCellToValue.cellValueToString(cell).matches(valid)) {
				return false;
			} else {
				return true;
			}

		}
	}
	/**
	 * 生日
	 * 
	 * @param cell
	 * @return
	 * @throws CRMException 
	 */
	public static boolean validBirthday(HSSFCell cell) {
		String valid = "^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|" +
				"(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|" +
				"1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|" +
				"1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|" +
				"((16|[2468][048]|[3579][26])00))-0?2-29-))$";
		if (cell == null) {
			return false;
		}
		if (cell.toString().trim().length() == 0) {
			return false;
		} else {
			if (ExcelCellToValue.cellValueToString(cell).matches(valid)) {
				return false;
			} else {
				return true;
			}

		}
	}
	public static boolean validNull(HSSFCell cell) {
		if (cell == null) {
			return false;
		}else{
			if(cell.getStringCellValue().trim().length()>0){
				return true;
			}else{
				return false;	
			}
		}
	}
}
