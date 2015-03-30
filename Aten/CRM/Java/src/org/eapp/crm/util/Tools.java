package org.eapp.crm.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;


/**
 * @author zsy
 * 
 */
public class Tools {
	/**
	 * 生成UUID
	 * 
	 * @return
	 */
	public static String generateUUID() {
		String uuid = UUID.randomUUID().toString();
		return uuid.replaceAll("-", "");
	}

	/**
	 * 格式化分钟，如“2000”转化为“1天9小时20分钟”
	 * 
	 * @param minutes
	 * @return
	 */
	public static String formatMinute(int minutes) {
		if (minutes <= 0) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		int m = minutes % 60;
		if (m != 0) {
			sb.append(m).append("分钟");
		}
		int hours = minutes / 60;
		if (hours == 0) {
			return sb.toString();
		}
		int h = hours % 24;
		if (h != 0) {
			sb.insert(0, h + "小时");
		}
		int days = hours / 24;
		if (days == 0) {
			return sb.toString();
		}
		sb.insert(0, days + "天");
		return sb.toString();
	}

	/**
	 * 格式化分钟，如“2000”转化为“1天9小时20分钟”
	 * 
	 * @param minutes
	 * @param preLable
	 *            如果minutes为负数的前缀
	 * @return
	 */
	public static String formatMinute(int minutes, String preLable) {
		StringBuffer sb = new StringBuffer();
		if (minutes < 0) {
			sb.append(preLable);
			minutes = -minutes;
		}
		if (minutes == 0) {
			return "0分钟";
		}
		int m = minutes % 60;
		if (m != 0) {
			sb.append(m).append("分钟");
		}
		int hours = minutes / 60;
		if (hours == 0) {
			return sb.toString();
		}
		int h = hours % 24;
		if (h != 0) {
			sb.insert(0, h + "小时");
		}
		int days = hours / 24;
		if (days == 0) {
			return sb.toString();
		}
		sb.insert(0, days + "天");
		return sb.toString();
	}

	/**
	 * 判断numStr(流水号)长度是否满足maxMedian(最大流水号位数)，满足直接输出numStr，不满足在numStr之前补0返回
	 * 
	 * @param numStr
	 *            流水号
	 * @param maxMedian
	 *            最大流水号位数
	 * @return
	 */
	public static String getIsomuxByNum(String numStr, int maxMedian) {
		int numLength = numStr.length();
		numLength = maxMedian - numLength;
		String str = "";
		if (numLength > 0) {
			for (int i = 0; i < numLength; i++) {
				str += "0";
			}
			return str + numStr;
		} else {
			return numStr;
		}
	}

	// 根据随机码位数获取随机码
	public static String getBitRan(int num) {
		Random ran = new Random();
		String ranNum = String.valueOf(Math.abs(ran.nextInt()));
		while (ranNum.length() != num)
			if (ranNum.length() > num)
				ranNum = ranNum.substring(0, ranNum.length() - 1);
			else
				ranNum = ranNum + "0";

		return ranNum;
	}

	// 写信息到文件
	public static void WriteToFile(String strFileName, String message,
			String format) {
		String strTime = Tools.getDateTime();
		String myyear = strTime.substring(0, 4);
		String mymonth = strTime.substring(5, 7);
		String myday = strTime.substring(8, 10);

		File aFile = new File(strFileName);
		if (!(aFile.exists()))
			aFile.mkdirs();
		strFileName = strFileName + myyear + "-" + mymonth + "-" + myday + "."
				+ format;
		PrintWriter out = null;
		try {
			out = new PrintWriter(new FileOutputStream(strFileName, true));
			out.println(strTime + ":" + message + "\r\n");
			out.close();
		} catch (Exception e) {
			out.close();
			e.printStackTrace();
		}
	}

	// 写信息到文件
	public static void WriteToFile(String strFileName, String message) {
		PrintWriter out = null;
		try {
			out = new PrintWriter(new FileOutputStream(strFileName, true));
			out.print(message);
			out.close();
		} catch (Exception e) {
			out.close();
			e.printStackTrace();
		}
	}

	// 获取当前系统时间(格式为:yyyy-MM-dd HH:mm:ss)
	public static String getDateTime() {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = f.format(new Date());
		return time;
	}

	// 根据日期格式获取当前系统时间
	public static String getDateTime(String sFormat) {
		SimpleDateFormat f = new SimpleDateFormat(sFormat);
		String time = f.format(new Date());
		return time;
	}

	/**
	 * 将日期格式化并返回
	 * 
	 * @param date
	 *            待格式化的日期
	 * @param format
	 *            格式
	 * @return 格式化后的日期字符串 如果格式化失败，则返回null
	 */
	public static String toDateString(Date date, String formatString) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat(formatString);
		try {
			return format.format(date);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String htmlEncode(String source) {
		String html = "";
		if (source != null && source.length() > 0) {
			html = source/*
						 * .replaceAll("&" , "&amp;") .replaceAll("<" , "&lt;")
						 * .replaceAll(">" , "&gt;") .replaceAll("\"" ,
						 * "&quot;")
						 */
			.replaceAll(" ", "&nbsp;").replaceAll("\\n", "<br/>").replaceAll(
					"\\r", "<br/>");
		}
		return html;
	}

	public static String htmlDecode(String source) {
		String html = "";
		if (source != null && source.length() > 0) {
			html = source.replaceAll("&amp;", "&").replaceAll("&lt;", "<")
					.replaceAll("&gt;", ">").replaceAll("&quot;", "\"")
					.replaceAll("&nbsp;", " ").replaceAll("<br/>", "\n")
					.replaceAll("<br/>", "\r");
		}
		return html;
	}

	public static String htmlEnter(String source) {
	    String html="";
	    if (source != null && source.length()>0) {
	        html = source
	        .replaceAll("&lt;br/&gt;","&#13;")
	        .replaceAll("\\n","&#13;")
	        .replaceAll("&amp;" , "&");
	    }
	    return html;
	}
	
	public static String excelHtmlDecode(String source) {
		String html = "";
		if (source != null && source.length() > 0) {
			html = source.replaceAll("&nbsp;", " ").replaceAll("<br/>", "\\n")
					.replaceAll("<br/>", "\\r");
		}
		return html;
	}

	/**
	 * 字符串形式的日期转Date
	 * 
	 * @param dateString
	 *            待格式化的字符串
	 * @param formatString
	 *            格式化字符串 如yyyy-MM-dd
	 * @return
	 */
	public static Date toDate(String dateString, String formatString) {
		if (StringUtils.isEmpty(dateString)) {
			return null;
		}			
		SimpleDateFormat format = new SimpleDateFormat(formatString);// yyyy-MM-dd
																	// HH:mm:ss.SSS
		try {
			return format.parse(dateString);
		} catch (ParseException e) {
			return null;
		}
	}

	public static String getCurrency(double d) {
		int scale = 10;
		int i = 0;
		String formatString = "";
		while (i < scale) {
			if (i == 0) {
				formatString += ".";
			}
			formatString += "0";
			i++;
		}
		if (!"".equals(formatString)) {
			formatString = "###,##0" + formatString;
		}
		// "###,##0.00000"
		DecimalFormat fmt = new DecimalFormat(formatString);
		String outStr = null;
		try {
			outStr = fmt.format(d);
			outStr = Tools.trimTailZero(outStr);
			return outStr;
		} catch (Exception e) {

		}
		return outStr;
	}

	/**
	 * 去除末尾的0
	 * 
	 * @param str
	 *            数字的字符串形式
	 * @return
	 */
	public static String trimTailZero(String str) {
		try {
			if (str == null || "".equals(str) || str.indexOf(".") == -1) {
				return str;
			}
			StringBuffer sb = new StringBuffer();
			sb.append(str);
			sb.reverse();

			char[] chArray = sb.toString().toCharArray();
			StringBuffer tempSB = null;
			while (chArray.length != 0) {
				tempSB = new StringBuffer();
				tempSB.append(chArray);
				if (!"0".equals(String.valueOf(chArray[0]))) {
					break;
				}
				if (".".equals(String.valueOf(chArray[1]))) {
					tempSB = tempSB.replace(0, 2, "");
					break;
				} else {
					tempSB = tempSB.replace(0, 1, "");
					chArray = tempSB.toString().toCharArray();
				}
			}
			return tempSB == null ? null : tempSB.reverse().toString();
		} catch (Exception e) {
			return str;
		}
	}

	/**
	 * 取得指定月份的第一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getMonthBegin(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
		String tempDate = df.format(date) + "-01";
		tempDate = tempDate + "-01";
		try {
			return df2.parse(tempDate);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 取得指定月份的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getMonthEnd(Date date) {
		Date startDate = getMonthBegin(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		return calendar.getTime();
	}

	/**
	 * 将指定日期转换成指定日期的0点0分0秒
	 * 
	 * @param date
	 * @return
	 */
	public static Date to0Oclock(Date date) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		Calendar cloneCa = (Calendar) ca.clone();
		ca.clear();
		ca.set(Calendar.YEAR, cloneCa.get(Calendar.YEAR));
		ca.set(Calendar.MONTH, cloneCa.get(Calendar.MONTH));
		ca.set(Calendar.DATE, cloneCa.get(Calendar.DATE));
		return ca.getTime();
	}

	/**
	 * 对Double类型进行格式化
	 * 
	 * @param value
	 * @return
	 */
	public static String doubleToString(Double value) {
		if (value != null) {
			boolean flag = false;
			if (value < 0) {
				value = Math.abs(value);
				flag = true;
			}
			NumberFormat nf1 = NumberFormat.getInstance();
			nf1.setMinimumFractionDigits(2);
			if (flag) {
				return "-" + nf1.format(value);
			} else {
				return nf1.format(value);
			}

		} else {
			return "0.00";
		}

	}

	// 存入数据库
	public static String OperationChar(String message) {
		if (message == null) {
			return null;
		}
		char content[] = new char[message.length()];
		message.getChars(0, message.length(), content, 0);
		StringBuffer result = new StringBuffer(content.length + 50);
		for (int i = 0; i < content.length; i++) {
			switch (content[i]) {
			// case '<':
			// result.append("&lt;");
			// break;
			// case '>':
			// result.append("&gt;");
			// break;
			// case '&':
			// result.append("&amp;");
			// break;
			case '"':
				result.append("&quot;");
				break;
			case ' ':
				result.append("&nbsp;");
				break;
			case '\n':
				result.append("<br/>");
				break;
			default:
				result.append(content[i]);
			}
		}
		return result.toString();
	}
	
	public static Date plusDay(Date date, int day) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    calendar.add(Calendar.DAY_OF_MONTH, day);
	    return calendar.getTime();
	}

	// 显示在页面的时候
	public static String DEOperationChar(String message, boolean isBr) {
		StringBuffer sb = new StringBuffer();
		if (message == null) {
			return null;
		}
		if (isBr) {
			sb.append(message.replaceAll("<br/>", "").replaceAll("&lt;", "<")
					.replaceAll("&gt;", ">").replaceAll("&quot;", "\"")
					.replaceAll("&nbsp;", " "));
		} else {
			sb.append(message);
		}
		return sb.toString();
	}
	
	public static Date getLateSecondForDate(Date date) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    calendar.set( calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE), 23, 59, 59);
        return calendar.getTime();
    
	}
	
	public static String screenPhoneNumber(String phone) {
		if(StringUtils.isNotEmpty(phone) && phone.length()==11){
			return phone.substring(0, 3) + "****" + phone.substring(phone.length()-4, phone.length());
		} else {
			return phone;
		}
	}

	public static void main(String[] args) {
		System.out.println("13810011001".substring("13810011001".length()-8, "13810011001".length()-4));
		System.out.println(Tools.screenPhoneNumber("13810011001"));
	}
}
