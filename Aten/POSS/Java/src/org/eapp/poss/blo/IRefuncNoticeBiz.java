/**
 * 
 */
package org.eapp.poss.blo;

import java.util.Collection;

import org.eapp.poss.dao.param.RefuncNoticeQueryParameters;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.Attachment;
import org.eapp.poss.hbean.RefuncNotice;
import org.eapp.util.hibernate.ListPage;

/**
 * 
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-21	钟华杰		新建
 * </pre>
 */
public interface IRefuncNoticeBiz {
    
    public RefuncNotice queryRefuncNoticeById(String id) throws PossException;
    
    public RefuncNotice txAddRefuncNotice(RefuncNotice transRefuncNotice) throws PossException;
    
    public RefuncNotice txUpdateRefuncNotice(RefuncNotice transRefuncNotice) throws PossException;
    
    public void txDelRefuncNotice(String id) throws PossException;
    
    public ListPage<RefuncNotice> queryRefuncNoticeListPage(RefuncNoticeQueryParameters qp) throws PossException;
    
    /**
     * 上传退款须知附件
     * @param id
     * @param deletedFiles
     * @param refuncNoticeAttachments
     * @throws PossException
     */
    public void txBatchUploadRefuncNoticeAttachment(final String id, String[] deletedFiles, final Collection<Attachment> refuncNoticeAttachments) throws PossException;
    
}