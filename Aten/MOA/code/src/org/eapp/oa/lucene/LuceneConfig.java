/**
 * 
 */
package org.eapp.oa.lucene;

/**
 * @author zsy
 * @version Jun 16, 2009
 */
public class LuceneConfig {
	private String analyzerClassName;//索引分词器
	private String indexRootdir;//索引存放的根目录
	private int period;//索引更新间隔，单位：毫秒
	public String getAnalyzerClassName() {
		return analyzerClassName;
	}
	public void setAnalyzerClassName(String analyzerClassName) {
		this.analyzerClassName = analyzerClassName;
	}
	public String getIndexRootdir() {
		return indexRootdir;
	}
	public void setIndexRootdir(String indexRootdir) {
		this.indexRootdir = indexRootdir;
	}
	public int getPeriod() {
		return period;
	}
	public void setPeriod(int period) {
		this.period = period;
	}
	
}
