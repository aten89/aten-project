/**
 * 
 */
package org.eapp.poss.blo.imp;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.client.hessian.UserAccountService;
import org.eapp.exception.RpcAuthorizationException;
import org.eapp.poss.blo.IProdFaqBiz;
import org.eapp.poss.dao.IProdFaqDAO;
import org.eapp.poss.dao.param.ProdFaqQueryParameters;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.ProdFaq;
import org.eapp.rpc.dto.UserAccountInfo;
import org.eapp.util.hibernate.ListPage;
import org.hibernate.Hibernate;

/**
 * 
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-21	lhg		新建
 * </pre>
 */
public class ProdFaqBiz implements IProdFaqBiz {
    
    private static Log logger = LogFactory.getLog(ProdFaqBiz.class);
    
    //DAO
    private IProdFaqDAO prodFaqDAO;
    
    private UserAccountService uas = new UserAccountService();

    /* (non-Javadoc)
     * @see org.eapp.poss.blo.IProdFaqBiz#queryProdFaqById(java.lang.String)
     */
    @Override
    public ProdFaq queryProdFaqById(String id) throws PossException {
        // params judge
        if (StringUtils.isEmpty(id)) {
            throw new IllegalArgumentException("非法参数：产品问答ID为空");
        }
        ProdFaq prodFaq = prodFaqDAO.findById(id);
        if (prodFaq == null) {
            throw new PossException("产品问答不存在");
        }
        initProdFaq(prodFaq);
        return prodFaq;
    }

    /* (non-Javadoc)
     * @see org.eapp.poss.blo.IProdFaqBiz#txAddProdFaq(org.eapp.poss.hbean.ProdFaq)
     */
    @Override
    public void txAddProdFaq(ProdFaq transProdFaq) throws PossException {
        // params judge
        if (transProdFaq == null) {
            throw new IllegalArgumentException("非法参数：产品问答对象为空");
        }
        transProdFaq.setCreateTime(new Date());
//        if (transProdFaq.getParentProdFaq() != null) {
//            
//        }
        prodFaqDAO.save(transProdFaq);
    }

    /* (non-Javadoc)
     * @see org.eapp.poss.blo.IProdFaqBiz#txUpdateProdFaq(org.eapp.poss.hbean.ProdFaq)
     */
    @Override
    public void txUpdateProdFaq(ProdFaq transProdFaq) throws PossException {
        // params judge
        if (transProdFaq == null) {
            throw new IllegalArgumentException("非法参数：产品问答对象为空");
        }
        if (StringUtils.isEmpty(transProdFaq.getId())) {
            throw new IllegalArgumentException("非法参数：产品问答ID为空");
        }
        ProdFaq persiProdFaq = prodFaqDAO.findById(transProdFaq.getId());
        if (persiProdFaq == null) {
            throw new PossException("产品问答不存在");
        }
        persiProdFaq.setTitle(transProdFaq.getTitle());
        persiProdFaq.setContent(transProdFaq.getContent());
        prodFaqDAO.saveOrUpdate(persiProdFaq);

    }
    
    /* (non-Javadoc)
     * @see org.eapp.poss.blo.IProdFaqBiz#txDelProdFaq(java.lang.String)
     */
    @Override
    public void txDelProdFaq(String id) throws PossException {
        // params judge
        if (StringUtils.isEmpty(id)) {
            throw new IllegalArgumentException("非法参数：产品问答ID为空");
        }
        ProdFaq prodFaq = prodFaqDAO.findById(id);
        if (prodFaq == null) {
            throw new PossException("产品问答不存在");
        }
        // TODO check can del
        prodFaqDAO.delete(prodFaq);
    }

    /* (non-Javadoc)
     * @see org.eapp.poss.blo.IProdFaqBiz#queryProdFaqListPage(org.eapp.poss.dao.param.ProdFaqQueryParameters)
     */
    @Override
    public ListPage<ProdFaq> queryProdFaqListPage(ProdFaqQueryParameters qp) throws PossException {
        ListPage<ProdFaq> listPage = prodFaqDAO.findProdFaqListPage(qp);
        if (listPage != null && listPage.getDataList() != null) {
            for (ProdFaq prodFaq : listPage.getDataList()) {
                initProdFaq(prodFaq);
            }
        }
        return listPage;
    }
    
    private void initProdFaq(ProdFaq prodFaq) throws PossException {
        if (prodFaq == null) {
            throw new IllegalArgumentException("非法参数：产品问答对象为空");
        }
        Hibernate.initialize(prodFaq.getProdInfo());
        Hibernate.initialize(prodFaq.getParentProdFaq());
        Hibernate.initialize(prodFaq.getChildProdFaqs());
        if (prodFaq.getProdInfo() != null) {
            prodFaq.setProdInfoName(prodFaq.getProdInfo().getProdName());
        }
        if (StringUtils.isNotEmpty(prodFaq.getCreator())) {
            try {
                UserAccountInfo userAccountInfo = uas.getUserAccount(prodFaq.getCreator());
                if (userAccountInfo != null) {
                    prodFaq.setCreatorName(userAccountInfo.getDisplayName());
                }
            } catch (RpcAuthorizationException e) {
                logger.error("getUserAccount failed: ", e);
                throw new PossException(e.getMessage());
            } catch (MalformedURLException e) {
                logger.error("getUserAccount failed: ", e);
                throw new PossException(e.getMessage());
            }
        }
        if (prodFaq.getChildProdFaqs() != null && !prodFaq.getChildProdFaqs().isEmpty()) {
            for (ProdFaq childProdFaq : prodFaq.getChildProdFaqs()) {
                initProdFaq(childProdFaq);
            }
            prodFaq.setHasAnswer(true);
        } else {
            prodFaq.setHasAnswer(false);
        }
    }
    
    @Override
    public List<UserAccountInfo> queryCreatorList() throws PossException {
        Set<UserAccountInfo> userAccountInfos = new HashSet<UserAccountInfo>(0);
        List<String> creatorList = prodFaqDAO.findProdFaqCreator();
        if (creatorList != null) {
            for (String creator : creatorList) {
                if (StringUtils.isNotEmpty(creator)) {
                    try {
                        UserAccountInfo userAccountInfo = uas.getUserAccount(creator);
                        if (userAccountInfo != null) {
                            userAccountInfos.add(userAccountInfo);
                        }
                    } catch (RpcAuthorizationException e) {
                        logger.error("getUserAccount failed: ", e);
                        throw new PossException(e.getMessage());
                    } catch (MalformedURLException e) {
                        logger.error("getUserAccount failed: ", e);
                        throw new PossException(e.getMessage());
                    }
                }
            }
        }
        return new ArrayList<UserAccountInfo>(userAccountInfos);
    }

    /**
     * set the prodFaqDAO to set
     * @param prodFaqDAO the prodFaqDAO to set
     */
    public void setProdFaqDAO(IProdFaqDAO prodFaqDAO) {
        this.prodFaqDAO = prodFaqDAO;
    }

}
