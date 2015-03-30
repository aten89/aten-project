/**
 * 
 */
package org.eapp.crm.util.script;

import java.util.Map;

/**
 * 报表中心报表式引擎帮助类
 * @author zsy
 *
 */
public final class ReportExpressionHelper {
	/**
	 * 表达式前缀
	 * 以此开头表示后面为表达式
	 */
	public static final String EXPRESSION_PREFIX = "EXP:";
	
	/**
	 * 不需要被实例化
	 */
	private ReportExpressionHelper() {
		
	}
	
	/**
	 * 是否表达式
	 * @param exp 测试字符串
	 * @return true/false
	 */
	public static boolean isExpession(String exp) {
		if (exp == null) {
			return false;
		}
		//前缀匹配判断
		return exp.toUpperCase().startsWith(EXPRESSION_PREFIX);
	}
	
	/**
	 * 测试表达式
	 * @param val 测试字符串
	 * @param vars 表达式执行的上下文变量
	 * @return 执行成功或失败
	 */
	public static boolean testExp(String val, Map<String, Object> vars) {
		if (isExpession(val)) {
			//截取表达式
			String exp = val.substring(EXPRESSION_PREFIX.length());
			//测试执行
			try {
				ExpressionEngine.eval(exp, vars);
				return true;
			} catch(Exception e) {
				return false;
			}
		} else {
			return true;
		}
	}
	
	
	/**
	 * 执行表达式，
	 * 并将结果转为指定类型
	 * @param val 表达式
	 * @param vars 表达式执行的上下文变量
	 * @param returnType 执行结果的目录类型
	 * @param <T> 返回类型
	 * @return 表达式执行结果
	 */
	public static <T> T evalExp(String val, Map<String, Object> vars, Class<T> returnType) {
		if (isExpession(val)) {
			//如果是表达式
			String exp = val.substring(4);
			//返回指定类型的执行结果
			return ExpressionEngine.eval(exp, vars, returnType);
		} 
		return convertVal(val, returnType);
	}
	
	/**
	 * 类型转换
	 * @param val 值
	 * @param returnType 转换类型
	 * @return 转换后类型
	 */
	@SuppressWarnings("unchecked")
	public static <T> T convertVal(String val, Class<T> returnType) {
		//如果不是表达式
		//要进行类型转换
		if (val == null) {
			//空类型不需要转换
			return null;
		} else if (returnType.equals(String.class)) {
			//String类型
			return (T) val;
		} else if (Number.class.isAssignableFrom(returnType)) {
			//Number类型
			//如果结果值是String类型，尝试格式化
			Number n = (Number) Double.parseDouble(val);
			//返回指定的Number子类
			return ExpressionEngine.getNumberValue(n, returnType);

		}
		//否则都认为不兼容的转换关系
		throw new ClassCastException("结果类型转换出错");
	}
	
/*	public static void main(String[] args) {
		Map<String, Object> funcs = new java.util.HashMap<String, Object>();
		funcs.put("$", new SystemFunction());
		ExpressionEngine.setFunctions(funcs);
		
		
		Map<String,Object> vars = new java.util.HashMap<String, Object>();
		vars.put("num", 3);
		vars.put("str", "abc");
		
		//String exp = "EXP:aa();function aa(){var r = num==1 ? 'a':" +
		//		"(num==2 ? 'b':(num==3 ? 'c':(num==4 ? 'd':" +
		//		"(num==5 ? 'e':(num==6 ? 'f':'g')))));return r;}";
		String exp = "EXP:aa();"
				+ "	function aa() {"
				+ "		var r; "
				+ "		var num=params.get('num');"
				+ "		if (num ==1){"
				+ "			r =$.currentDateName();"
				+ "		} else if (num == 2) {"
				+ "			r ='b';"
				+ "		} else {"
				+ "			r = params.get('str');"
				+ "		}"
				+ "		return r;"
				+ "	}";
		Object o = evalExp(exp, vars, Object.class);
		System.out.println(o);
		System.out.println(o.getClass());
		
	}*/
}
