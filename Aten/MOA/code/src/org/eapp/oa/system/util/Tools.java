package org.eapp.oa.system.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

/**
 * 工具类
 * 
 * <pre>
 * 修改日期	              修改人		修改原因
 * 2012-8-2		卢凯宁		修改注释
 * </pre>
 */
public class Tools {

    /**
     * 生成UUID
     * 
     * @return UUID
     * 
     *         <pre>
     * 修改日期	              修改人		修改原因
     * 2012-8-2		卢凯宁		修改注释
     * </pre>
     */
    public static String generateUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }

    /**
     * 格式化分钟，如“2000”转化为“1天9小时20分钟”
     * 
     * @param minutes 分钟
     * @return 指定字符串
     * 
     *         <pre>
     * 修改日期	              修改人		修改原因
     * 2012-8-2		卢凯宁		修改注释
     * </pre>
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
     * 判断numStr(流水号)长度是否满足maxMedian(最大流水号位数)，满足直接输出numStr，不满足在numStr之前补0返回
     * 
     * @param numStr 流水号
     * @param maxMedian 最大流水号位数
     * @return 指定字符串
     */
    public static String getIsomuxByNum(String numStr, int maxMedian) {
        int numLength = numStr.length();
        numLength = maxMedian - numLength;
        StringBuffer str = new StringBuffer();
        if (numLength > 0) {
            for (int i = 0; i < numLength; i++) {
                str.append("0");
            }
            return str.append(numLength).toString();
        } else {
            return numStr;
        }
    }

    /**
     * 判断compareDate是否在startDate和endDate之间
     * 
     * @param compareDate null时取当前系统时间
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return true|false
     */
    public static boolean betweenDate(Date compareDate, Date startDate, Date endDate) {
        if (compareDate == null) {
            compareDate = new Date();
        }
        if (startDate == null && endDate != null) {
            return compareDate.compareTo(endDate) <= 0;
        }
        if (startDate != null && endDate == null) {
            return compareDate.compareTo(startDate) >= 0;
        }
        if (startDate != null && endDate != null) {
            return compareDate.compareTo(startDate) >= 0 && compareDate.compareTo(endDate) <= 0;
        }
        return false;
    }

    /**
     * 对Double类型进行格式化
     * 
     * @param value
     * @return 字符串
     */
    public static String doubleToString(Double value) {
        if (value == null) {
            return "0.00";
        } else {
            NumberFormat nf1 = NumberFormat.getInstance();
            nf1.setMinimumFractionDigits(2);
            return nf1.format(value);
        }

    }

    /**
     * 字符串形式的日期转Date
     * 
     * @param dateString 待格式化的字符串
     * @param formatString 格式化字符串 如yyyy-MM-dd
     * @return 指定字符串
     */
    public static Date toDate(String dateString, String formatString) {
        SimpleDateFormat format = new SimpleDateFormat(formatString);// yyyy-MM-dd HH:mm:ss.SSS
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 提供精确的加法运算。
     * 
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 根据随机码位数获取随机码
     * 
     * @param num 位数
     * @return 随机码
     * 
     *         <pre>
     * 修改日期	              修改人		修改原因
     * 2012-8-2		卢凯宁		修改注释
     * </pre>
     */
    public static String getBitRan(int num) {
        Random ran = new Random();
        String ranNum = String.valueOf(Math.abs(ran.nextInt()));
        while (ranNum.length() != num) {
            if (ranNum.length() > num) {
                ranNum = ranNum.substring(0, ranNum.length() - 1);
            } else {
                ranNum = ranNum + "0";
            }
        }
        return ranNum;
    }

    /**
     * 写信息到文件
     * 
     * @param strFileName 文件名
     * @param message 信息
     * @param format 格式
     * 
     *            <pre>
     * 修改日期	              修改人		修改原因
     * 2012-8-2		卢凯宁		修改注释
     * </pre>
     */
    public static void writeToFile(String strFileName, String message, String format) {
        String strTime = Tools.getDateTime();
        String myyear = strTime.substring(0, 4);
        String mymonth = strTime.substring(5, 7);
        String myday = strTime.substring(8, 10);

        File aFile = new File(strFileName);
        if (!(aFile.exists()))
            aFile.mkdirs();
        StringBuffer fileName = new StringBuffer(strFileName);
        fileName.append(myyear).append("-").append(mymonth).append("-").append(myday).append(".").append(format);
        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileOutputStream(fileName.toString(), true));
            out.println(strTime + ":" + message + "\r\n");
            out.close();
        } catch (Exception e) {
            out.close();
            e.printStackTrace();
        }
    }

    /**
     * 写信息到文件
     * 
     * @param strFileName 文件名
     * @param message 信息
     * 
     *            <pre>
     * 修改日期	              修改人		修改原因
     * 2012-8-2		卢凯宁		修改注释
     * </pre>
     */
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

    /**
     * 获取当前系统时间(格式为:yyyy-MM-dd HH:mm:ss)
     * 
     * @return 指定时间格式
     * 
     *         <pre>
     * 修改日期	              修改人		修改原因
     * 2012-8-2		卢凯宁		修改注释
     * </pre>
     */
    public static String getDateTime() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return f.format(new Date());
    }

    /**
     * 根据日期格式获取当前系统时间
     * 
     * @param sFormat 指定格式
     * @return 系统时间字符串
     * 
     *         <pre>
     * 修改日期	              修改人		修改原因
     * 2012-8-2		卢凯宁		修改注释
     * </pre>
     */
    public static String getDateTime(String sFormat) {
        SimpleDateFormat f = new SimpleDateFormat(sFormat);
        return f.format(new Date());
    }

    /**
     * 将日期格式化并返回
     * 
     * @param date 待格式化的日期
     * @param format 格式
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

    /**
     * 转换 html 标记
     * 
     * @param source html字符串
     * @return 转码后的html字符串
     * 
     *         <pre>
     * 修改日期	              修改人		修改原因
     * 2012-8-2		卢凯宁		修改注释
     * </pre>
     */
    public static String htmlEncode(String source) {
        String html = "";
        if (source != null && source.length() > 0) {
            html = source/*
                          * .replaceAll("&" , "&amp;") .replaceAll("<" , "&lt;") .replaceAll(">" , "&gt;")
                          * .replaceAll("\"" , "&quot;")
                          */
            .replaceAll(" ", "&nbsp;").replaceAll("\\n", "<br/>").replaceAll("\\r", "<br/>");
        }
        return html;
    }

    /**
     * 解码数据库html字符串转回页面显示格式
     * 
     * @param source 字符串
     * @return 解码后的字符串
     * 
     *         <pre>
     * 修改日期	              修改人		修改原因
     * 2012-8-2		卢凯宁		修改注释
     * </pre>
     */
    public static String htmlDecode(String source) {
        String html = "";
        if (source != null && source.length() > 0) {
            html = source.replaceAll("&nbsp;", " ").replaceAll("<br/>", "\\n").replaceAll("<br/>", "\\r");
        }
        return html;
    }

    /**
     * 把xml字符串转换成xml对象
     * 
     * @param xml
     * @return <pre>
     * 修改日期		修改人	修改原因
     * 2012-7-11	卢凯宁		新建
     * </pre>
     */
    public static Document xmlString2XmlObject(String xmlStr) {
        Document document = null;
        try {
            document = DocumentHelper.parseText(xmlStr);
            document.setXMLEncoding("UTF-8");
            return document;
        } catch (DocumentException e) {
            e.printStackTrace();
            return document;
        }
    }

    /**
     * 取得当前月份的季度的开始时间和结束时间
     * 
     * @param month 月份
     * @param year 年份
     * @return 日期数组 第一个：开始时间 ，第二个：结束时间
     */
    public static Date[] getThisSeasonTime(int month, int year) {
        if (month > 12 || month < 1) {
            throw new IllegalArgumentException("非法的月份");
        }
        int array[][] = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 }, { 10, 11, 12 } };
        int season = getThisSeason(month);
        int start_month = array[season - 1][0];
        int end_month = array[season - 1][2];

        int start_days = 1;
        int end_days = getLastDayOfMonth(year, end_month);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, start_month - 1);
        calendar.set(Calendar.DATE, start_days);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        

        Date startDate = calendar.getTime();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, end_month - 1);
        calendar.set(Calendar.DATE, end_days);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date endDate = calendar.getTime();
        Date[] dateArray = new Date[] { startDate, endDate };
        return dateArray;

    }
    
    public static Date getYeayStartDate(Date dateTime) {
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        // 年份
        int year = calendar.get(Calendar.YEAR);
        
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startDate = calendar.getTime();
        return startDate;
    }

    /**
     * 取得当前月份的季度的开始时间和结束时间
     * 
     * @param dateTime 时间
     * @return 日期数组 第一个：开始时间 ，第二个：结束时间
     */
    public static Date[] getThisSeasonRangeTime(Date dateTime) {
        if (dateTime == null) {
            throw new IllegalArgumentException("参数不能为空！");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        // 月份
        int month = calendar.get(Calendar.MONTH) + 1;
        // 年份
        int year = calendar.get(Calendar.YEAR);

        return getThisSeasonTime(month, year);
    }

    /**
     * 取得月份所在的季度
     * 
     * @param month 月份
     * @return 季度
     * 
     */
    public static int getThisSeason(int month) {
        if (month > 12 || month < 1) {
            throw new IllegalArgumentException("非法的月份");
        }
        int season = 1;
        if (month >= 1 && month <= 3) {
            season = 1;
        }
        if (month >= 4 && month <= 6) {
            season = 2;
        }
        if (month >= 7 && month <= 9) {
            season = 3;
        }
        if (month >= 10 && month <= 12) {
            season = 4;
        }
        return season;
    }

    /**
     * 取得当前时间月份的所在的季度
     * 
     * @param dateTime 时间
     * @return 季度
     * 
     */
    public static int getThisSeason(Date dateTime) {
        if (dateTime == null) {
            throw new IllegalArgumentException("参数不能为空！");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        // 月份
        int month = calendar.get(Calendar.MONTH) + 1;
        return getThisSeason(month);
    }

    /**
     * 获取某年某月的最后一天
     * 
     * @param year 年
     * @param month 月
     * @return 最后一天
     */
    public static int getLastDayOfMonth(int year, int month) {
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            return 31;
        }
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        }
        if (month == 2) {
            if (isLeapYear(year)) {
                return 29;
            } else {
                return 28;
            }
        }
        return 0;
    }

    /**
     * 是否闰年
     * 
     * @param year 年
     * @return boolean
     */
    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
    
//    public static void main(String[] args) {
//       Date [] dd =  getThisSeasonRangeTime(new Date());
//       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//       System.out.println(sdf.format(dd[0]));
//       System.out.println(sdf.format(dd[1]));
//       System.out.println(sdf.format(getYeayStartDate(new Date())));
//       
//    }

}
