/**
 * 
 */
package org.eapp.oa.lucene.builder;

import org.eapp.oa.kb.hbean.Knowledge;

/**
 * @author zsy
 * @version Jun 12, 2009
 */
public class FinalKBIndexBuilder extends KBIndexBuilder {
	public static final String DOCUMENTTYPE = "FINAL_KB";
	
	public FinalKBIndexBuilder(String indexDir) {
		this.indexDir = indexDir;
		this.documentType = DOCUMENTTYPE;
	}
	
	@Override
	public Integer getKnowledgeStatus() {
		return Knowledge.STATUS_FINAL;
    }
}
