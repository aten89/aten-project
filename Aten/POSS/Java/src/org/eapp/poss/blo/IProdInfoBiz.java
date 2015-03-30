/**
 * 
 */
package org.eapp.poss.blo;

import java.io.File;

import org.eapp.poss.dao.param.ProdInfoQueryParameters;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.Attachment;
import org.eapp.poss.hbean.ProdInfo;
import org.eapp.util.hibernate.ListPage;

/**
 * 
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-21	lhg		新建
 * </pre>
 */
public interface IProdInfoBiz {
	public ProdInfo getProdInfoById(String id) throws PossException;
	
    public ProdInfo queryProdInfoById(String id) throws PossException;
    
    public ProdInfo txAddProdInfo(ProdInfo transProdInfo) throws PossException;
    
    public void txUpdateProdInfo(ProdInfo transProdInfo) throws PossException;
    
    public ProdInfo txDelProdInfo(String id) throws PossException;
    
    public ListPage<ProdInfo> queryProdInfoListPage(ProdInfoQueryParameters qp, boolean initFlag) throws PossException;
    
    public Attachment saveAttachmentToTempDir(File upgradeFile, String extName, String displayName) throws PossException;
    
}
