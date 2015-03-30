/**
 * 
 */
package org.eapp.oa.lucene.builder;

import org.eapp.oa.kb.hbean.Knowledge;

/**
 * @author zsy
 * @version Jun 12, 2009
 */
public class TempKBIndexBuilder extends KBIndexBuilder {
	public static final String DOCUMENTTYPE = "TEMP_KB";
	
	public TempKBIndexBuilder(String indexDir) {
		this.indexDir = indexDir;
		this.documentType = DOCUMENTTYPE;
	}
	
	@Override
	public Integer getKnowledgeStatus() {
		return Knowledge.STATUS_TEMP;
    }
}
