/*
 * 创建日期 2006-1-4
 *
 */
package org.eapp.util.web;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;

/**
 * 
 * 数据格式转换工具类
 * 提供数据非空处理，全半角转换，字符串截断处理等功能
 * @author linliangyi@team of miracle
 * modify by zhuoshiyao 2007.8.22
 * 创建日期 2006-1-4 
 * 页面显示辅助工具类
 */
public class DataFormatUtil {
	/**
	 * 空字符串
	 */
	private static final String NULLSTRING = "";
	/**
	 * 时间格式
	 */
	public static final String STANDARD_DATE_PATTERN = "yyyy-MM-dd";
	/**
	 * 时间格式
	 */
	public static final String SHORT_DATE_PATTERN = "MM-dd";
	/**
	 * 时间格式
	 */
	public static final String STANDARD_TIME_PATTERN = "yyyy-MM-dd HH:mm";
	/**
	 * 时间格式
	 */
	public static final String FULL_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    
    /**
     * 修正WEB页显示中的NULL
     * @param arg 原始参数值
     * @return 修正后的显示值
     */
    public static String noNullValue(String arg) {
        if (arg != null) {
        	return arg;
        } else {
        	return NULLSTRING;
        }
    }

    /**
     * 转化WEB页显示的日期
     * @param arg 原始参数值
     * @param format 修正后的日期格式
     * @return 修正后的结果
     */
    public static String noNullValue(Date arg , String format) {
        if (arg != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(arg);
        } else {
        	return NULLSTRING;
        }
    }
    
    /**
     * 转化WEB页显示的日期
     * @param arg 原始参数值
     * @return 修正后的显示值
     */
    public static String noNullValue(Date arg) {
        return noNullValue(arg , STANDARD_DATE_PATTERN);
    }
    
    /**
     * 修正WEB页显示中的NULL
     * @param arg 原始参数值
     * @return 修正后的显示值
     */
    public static String noNullValue(Object arg) {
    	if (arg != null) {
        	return arg.toString();
        } else {
        	return NULLSTRING;
        }
    }

    /**
     * 字窜全角转半角的函数(DBC case)
     * 全角空格为12288，半角空格为32
     * 其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
     * @param input 全角字符串
     * @return 半角字符串
     */
    public static String toDBC(String input) {
        if (input == null) {
            return NULLSTRING;
        }
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375) {
                c[i] = (char) (c[i] - 65248);
            }
        }
        return new String(c);
    }
	
	/**
	 * 格式化时间字符窜
	 * 如2007-12-1 12:2会被格式化成2007-12-01 12:02:00
	 * 转化后的格式支持Timestamp.valueOf(String value)
	 * @param time 字符窜表示的时间
	 * @return 格式代的结果
	 */
	public static String formatTime(String time) {
		if (time == null || time.trim().equals(NULLSTRING)) {
			return null;
		}
		StringReader sr = new StringReader(time.trim());
		StringBuffer sb = new StringBuffer();
		
		try {
			int b = -1;
			while ((b = sr.read()) != -1) {
				if (sb.length() < 4) {
					//年
					if (b < 48 || b > 57) {
						//'0'为48,'9'为57
						throw new IllegalArgumentException("年份必需为4位数字");
					}
					sb.append((char) b);
				} else if (sb.length() == 4) {
					if (b != 45 && b != 47) {
						//'-'为45,'/'为47
						throw new IllegalArgumentException("日期分割符必需为“－”或“/”");
					}
					sb.append((char) 45);
				} else if (sb.length() == 5) {
					//月
					if (b < 48 || b > 57) {
						throw new IllegalArgumentException("月份必需为2位以内的数字");
					}
					sb.append((char) b);
					sr.mark(0);
					b = sr.read();
					if (b < 48 || b > 57) {
						//不是数字
						//前一位插入一个0
						sb.insert(5, '0');
						//重置
						sr.reset();
					} else {
						sb.append((char) b);
					}
				} else if (sb.length() == 7) {
					if (b != 45 && b != 47) {
						throw new IllegalArgumentException("日期分割符必需为“－”或“/”");
					}
					sb.append((char) 45);
				} else if (sb.length() == 8) {
					//日
					if (b < 48 || b > 57) {
						throw new IllegalArgumentException("日必需为2位以内的数字");
					}
					sb.append((char) b);
					sr.mark(0);
					b = sr.read();
					if (b < 48 || b > 57) {
						sb.insert(8, '0');
						sr.reset();
					} else {
						sb.append((char) b);
					}
				} else if (sb.length() == 10) {
					if (b != 32) {
						//' '为32
						throw new IllegalArgumentException("日期后分割符必需为“ ”");
					}
					sb.append((char) b);
				} else if (sb.length() == 11) {
					//小时
					if (b < 48 || b > 57) {
						throw new IllegalArgumentException("小时必需为2位以内的数字");
					}
					sb.append((char) b);
					sr.mark(0);
					b = sr.read();
					if (b < 48 || b > 57) {
						sb.insert(11, '0');
						sr.reset();
					} else {
						sb.append((char) b);
					}
				} else if (sb.length() == 13) {
					if (b != 58) {
						//':'为58
						throw new IllegalArgumentException("时间分割符必需为“:”");
					}
					sb.append((char) b);
				} else if (sb.length() == 14) {
					//分
					if (b < 48 || b > 57) {
						throw new IllegalArgumentException("分钟必需为2位以内的数字");
					}
					sb.append((char) b);
					sr.mark(0);
					b = sr.read();
					if (b < 48 || b > 57) {
						sb.insert(14, '0');
						sr.reset();
					} else {
						sb.append((char) b);
					}
				} else if (sb.length() == 16) {
					if (b != 58) {
						throw new IllegalArgumentException("时间分割符必需为“:”");
					}
					sb.append((char) b);
				} else if (sb.length() == 17) {
					//秒
					if (b < 48 || b > 57) {
						throw new IllegalArgumentException("秒必需为2位以内的数字");
					}
					sb.append((char) b);
					sr.mark(0);
					b = sr.read();
					if (b < 48 || b > 57) {
						sb.insert(17, '0');
						sr.reset();
					} else {
						sb.append((char) b);
					}
				} else if (sb.length() == 19) {
					if (b != 46) {
						//'.'为46
						throw new IllegalArgumentException("毫秒分割符必需为“.”");
					}
					sb.append((char) b);
				} else if (sb.length() < 29) {
					//毫秒
					if (b < 48 || b > 57) {
						//'0'为48,'9'为57
						throw new IllegalArgumentException("毫秒必需为4位数字");
					}
					sb.append((char) b);
				} else {
					throw new IllegalArgumentException("不是有效的时间表达式");
				}
			}
		} catch (IOException e) {
			throw new IllegalArgumentException("不是有效的时间表达式", e);
		}
		
		if (sb.length() == 10) {
			//补时间
			sb.append(" 00:00:00");
		} else if (sb.length() == 16) {
			//补秒
			sb.append(":00");
		}
		if (sb.length() < 19 || sb.length() == 20) {
			throw new IllegalArgumentException("不是有效的时间表达式");
		}
		return sb.toString();

	}
    
    /**
     * 截取字符窜长度
     * 如:cutHtmlText("标题长度保留四个字符", 4, "....");
     * 结果为："例子：标题长度...."
     * 
     * @param str 原来字符窜
     * @param length 保留长度
     * @param endStr 截掉部门替代字符窜
     * @return 截取字符窜
     */
    public static String cutString(String str, int length, String endStr) {
    	if (length == 0) {
    		return str;
    	}
    	if (str == null) {
    		return NULLSTRING;
    	}
    	if (str.length() > length + 1) {
    		str = str.substring(0 , length) + endStr;
        }
    	return str;
    }
    /**
     * 截取字符窜长度
     * @param str 原来字符窜
     * @param length 保留长度
     * @return 截取字符窜
     */
    public static String cutString(String str, int length) {

    	return cutString(str, length, "...");
    }
    /**
     * 去除HTML元素，并截取给定长度
     * 如:cutHtmlText("<div>例子：<b>截取HTML固定的内容</b></div>", 10, "....");
     * 结果为："例子：截取HTML...."
     * 
     * @param str HTML内容
     * @param length 保留长度
     * @param endStr 截掉部门替代字符窜
     * @return 截取字符窜
     */
    public static String cutHtmlText(String str, int length, String endStr) {
    	if (length == 0) {
    		return str;
    	}
    	if (str == null) {
    		return NULLSTRING;
    	}
    	Pattern pattern = Pattern.compile("\\<[^\\<]+\\>");
    	Matcher matcher = pattern.matcher(str);
    	str = matcher.replaceAll(NULLSTRING);

    	if (str.length() > length + 1) {
    		str = str.substring(0 , length) + endStr;
        }
    	return str;
    }
    
    /**
     * 去除HTML元素，并截取给定长度
     * @param str HTML内容
     * @param length 保留长度
     * @return 截取字符窜
     */
    public static String cutHtmlText(String str, int length) {

    	return cutHtmlText(str, length, "...");
    }
}
