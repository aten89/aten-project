package org.eapp.oa.hr.blo;

public interface IEmployeeNumberCreater {
	/**
	 * 生成下一个工号
	 * @param currentNo
	 * @return
	 */
	String createNextNO(String currentNo);
}
