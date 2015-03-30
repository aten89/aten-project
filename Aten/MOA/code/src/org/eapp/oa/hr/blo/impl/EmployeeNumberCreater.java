package org.eapp.oa.hr.blo.impl;

import java.text.DecimalFormat;

import org.eapp.oa.hr.blo.IEmployeeNumberCreater;

public class EmployeeNumberCreater implements IEmployeeNumberCreater {
	private String prefixStr;
	private DecimalFormat numberFormat;

	public void setPrefixStr(String prefixStr) {
		this.prefixStr = prefixStr;
	}


	public void setNumberFormat(String formatStr) {
		this.numberFormat = new DecimalFormat(formatStr);
	}


	@Override
	public String createNextNO(String currentNo) {
		int order = 0;
		if (currentNo == null || currentNo.length() <= prefixStr.length()) {
			order = 0;
		} else {
			try {
				//去掉前缀
				order = Integer.parseInt(currentNo.substring(prefixStr.length()));
			} catch (Exception e) {
				e.printStackTrace();
				order = 0;
			}
		}
		
		
		return prefixStr + numberFormat.format(order + 1);
	}
}
