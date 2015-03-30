/**
 * 
 */
package org.eapp.oa.lucene;

import org.apache.lucene.analysis.Analyzer;



/**
 * 中文分词器实例的工厂
 * @author zsy
 * @version Jun 12, 2009
 */
public class ConfigFactory {
	private static LuceneConfig luceneConfig;
	
	private static Analyzer analyzer;
	
	public static void init(LuceneConfig luceneConfig) {
		ConfigFactory.luceneConfig = luceneConfig;
		try {
			Class<?> analyzerClass = Class.forName(luceneConfig.getAnalyzerClassName());
			analyzer = (Analyzer)analyzerClass.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	

	public static LuceneConfig getLuceneConfig() {
		return luceneConfig;
	}

	public static void setLuceneConfig(LuceneConfig luceneConfig) {
		ConfigFactory.luceneConfig = luceneConfig;
	}

	/**
	 * 返回一个系统配置的分词器实例
	 * @return Analyzer
	 */
	public static Analyzer getAnalyzer() {
		return analyzer;
	}
	
	/**
	 * 取得索引绝对路径
	 * @param absDir
	 * @return
	 */
	public static String getIndexDir(String absDir) {
		return luceneConfig.getIndexRootdir() + absDir;
	}
}
