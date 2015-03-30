/**
 * 
 */
package org.eapp.oa.system.util;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.UUID;

/**
 * 
 * @author zsy
 * @version Nov 27, 2008
 */
public final class SerialNoCreater {
	private int order = 0;
	
	public SerialNoCreater(String maxId) {
		if (maxId == null || maxId.length() <= 4) {
			order = 0;
		} else {
			try {
				maxId = maxId.substring(4);//去掉前4位年份
				order = Integer.parseInt(maxId);
			} catch (Exception e) {
				e.printStackTrace();
				order = 0;
			}
		}
	}
	
	public SerialNoCreater(int order) {
		this.order = order;
	}

	
	/**
	 * 生成ID序列号，规则为年份+递增数字，如“20090001”
	 * @return
	 */
	public synchronized String createNo() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		
		order++;
		DecimalFormat df = new DecimalFormat("0000");
		return Integer.toString(year) + df.format(order);
	}
	
	public static String createUUID() {
		return UUID.randomUUID().toString();
	}
	
	/**
	 * 判断numStr(流水号)长度是否满足maxMedian(最大流水号位数)，满足直接输出numStr，不满足在numStr之前补0返回
	 * @param numStr 流水号
	 * @param maxMedian 最大流水号位数
	 * @return
	 */
	public static String getIsomuxByNum(String numStr,int maxMedian){
		int numLength = numStr.length();
		numLength = maxMedian-numLength;
		String str="";
		if(numLength > 0){
			for (int i = 0; i < numLength; i++) {
				str += "0";
			}
			return str+numStr;
		}else{
			return numStr;
		}
		
	}
}
