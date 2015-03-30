/**
 * 
 */
package org.eapp.poss.action;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.comobj.SessionAccount;
import org.eapp.poss.blo.IProdFaqBiz;
import org.eapp.poss.config.SysConstants;
import org.eapp.poss.dao.param.ProdFaqQueryParameters;
import org.eapp.poss.dto.UserAccountInfoSelect;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.ProdFaq;
import org.eapp.poss.hbean.ProdInfo;
import org.eapp.poss.util.HTMLResponse;
import org.eapp.rpc.dto.UserAccountInfo;
import org.eapp.util.hibernate.ListPage;

import com.alibaba.fastjson.JSON;

/**
 * 
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-21	lhg		新建
 * </pre>
 */
public class ProdFaqAction extends BaseAction {

    /**
     * 
     */
    private static final long serialVersionUID = 3969928122388907674L;
    /**
     * logger
     */
    private static Log logger = LogFactory.getLog(ProdFaqAction.class);
    
    //service
    private IProdFaqBiz prodFaqBiz;
    
    // 输入参数
    /**
     * 页码
     */
    private int pageNo;
    /**
     * 每页数
     */
    private int pageSize;
    /**
     * 当前排序列
     */
    private String sortCol;
    /**
     * true:升序;false:降序
     */
    private boolean ascend;
    
    private String id;
    
    private Date createTimeBegin;
    
    private Date createTimeEnd;
    
    private String prodInfoId;
    
    private String creator;
    /**
     * 是否有答案
     */
    private String hasAnswer;
    
    private String prodFaqJSON;
    
    // 输出参数
    private ListPage<ProdFaq> prodFaqListPage;
    
    private ProdFaq prodFaq;
    
    
    //method
    public String queryProdFaqListPage() {
        ProdFaqQueryParameters qp = createProdFaqQP();
        try {
            prodFaqListPage = prodFaqBiz.queryProdFaqListPage(qp);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("queryProdFaqListPage failed: ", e);
            return error();
        }
    }
    
    public String initEditProdFaqPage() {
      SessionAccount user = (SessionAccount) getSession().getAttribute(SysConstants.SESSION_USER_KEY);
      if (user == null) {
          return error("请先登录");
      }
        try {
            if (StringUtils.isNotEmpty(id)) {
                prodFaq = prodFaqBiz.queryProdFaqById(id);
            } else {
                prodFaq = new ProdFaq();
                prodFaq.setCreator(user.getAccountID());
                prodFaq.setCreatorName(user.getDisplayName());
                if (StringUtils.isNotEmpty(prodInfoId)) {
                    ProdInfo prodInfo = new ProdInfo();
                    prodInfo.setId(prodInfoId);
                    prodFaq.setProdInfo(prodInfo);
                }
            }
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("queryProdFaqById failed: ", e);
            return error(e.getMessage());
        }
    }
    
    public String addProdFaq() {
        // params judge
        SessionAccount user = (SessionAccount) getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            return error("请先登录");
        }
        if (StringUtils.isEmpty(prodFaqJSON)) {
            return error("非法参数：产品问答对象为空");
        }
        try {
            ProdFaq transProdFaq  = (ProdFaq)JSON.parseObject(prodFaqJSON, ProdFaq.class);
            transProdFaq.setCreator(user.getAccountID());
            prodFaqBiz.txAddProdFaq(transProdFaq);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("addProdFaq failed: ", e);
            return error();
        }
    }
    
    public String updateProdFaq() {
        // params judge
        if (StringUtils.isEmpty(prodFaqJSON)) {
            return error("非法参数：产品问答对象为空");
        }
        try {
            ProdFaq transProdFaq  = (ProdFaq)JSON.parseObject(prodFaqJSON, ProdFaq.class);
            prodFaqBiz.txUpdateProdFaq(transProdFaq);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("updateProdFaq failed: ", e);
            return error();
        }
    }
    
    public String viewProdFaq() {
        // params judge
        if (StringUtils.isEmpty(id)) {
            return error("非法参数：产品问答ID为空");
        }
        try {
            prodFaq = prodFaqBiz.queryProdFaqById(id);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("queryProdFaqById failed: ", e);
            return error();
        }
    }
    
    public String delProdFaq() {
        // params judge
        if (StringUtils.isEmpty(id)) {
            return error("非法参数：产品问答ID为空");
        }
        try {
            prodFaqBiz.txDelProdFaq(id);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("txDelProdFaq failed: ", e);
            return error();
        }
    }
    
    public void initCreatorSel() {
        try {
            List<UserAccountInfo> userAccountInfos = prodFaqBiz.queryCreatorList();
            if (userAccountInfos == null) {
                return;
            }
            HTMLResponse.outputHTML(getResponse(), new UserAccountInfoSelect(userAccountInfos).toString());
        } catch (Exception e) {
            logger.error("queryCreatorList failed: ", e);
        }
    }
    
    private ProdFaqQueryParameters createProdFaqQP() {
        ProdFaqQueryParameters qp = new ProdFaqQueryParameters();
        qp.setPageNo(pageNo);
        qp.setPageSize(pageSize);
        // 增加对排序列的处理
        if (StringUtils.isNotEmpty(sortCol)) {
            qp.addOrder(sortCol, ascend);
        }
        if (createTimeBegin != null) {
            qp.setCreateTimeBegin(createTimeBegin);
        }
        if (createTimeEnd != null) {
            qp.setCreateTimeEnd(createTimeEnd);
        }
        if (StringUtils.isNotEmpty(prodInfoId)) {
            qp.setProdInfoId(prodInfoId);
        }
        if (StringUtils.isNotEmpty(creator)) {
            qp.setCreator(creator);
        }
        if (StringUtils.isNotEmpty(hasAnswer)) {
            qp.setHasAnswer(Boolean.valueOf(hasAnswer));
        }
        return qp;
    }
    
    
    /*************get and set **************/

    /**
     * set the prodFaqBiz to set
     * @param prodFaqBiz the prodFaqBiz to set
     */
    public void setProdFaqBiz(IProdFaqBiz prodFaqBiz) {
        this.prodFaqBiz = prodFaqBiz;
    }

    /**
     * set the pageNo to set
     * @param pageNo the pageNo to set
     */
    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    /**
     * set the pageSize to set
     * @param pageSize the pageSize to set
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * set the sortCol to set
     * @param sortCol the sortCol to set
     */
    public void setSortCol(String sortCol) {
        this.sortCol = sortCol;
    }

    /**
     * set the ascend to set
     * @param ascend the ascend to set
     */
    public void setAscend(boolean ascend) {
        this.ascend = ascend;
    }

    /**
     * set the id to set
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * set the createTimeBegin to set
     * @param createTimeBegin the createTimeBegin to set
     */
    public void setCreateTimeBegin(Date createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    /**
     * set the createTimeEnd to set
     * @param createTimeEnd the createTimeEnd to set
     */
    public void setCreateTimeEnd(Date createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    /**
     * set the prodInfoId to set
     * @param prodInfoId the prodInfoId to set
     */
    public void setProdInfoId(String prodInfoId) {
        this.prodInfoId = prodInfoId;
    }

    /**
     * set the creator to set
     * @param creator the creator to set
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * set the hasAnswer to set
     * @param hasAnswer the hasAnswer to set
     */
    public void setHasAnswer(String hasAnswer) {
        this.hasAnswer = hasAnswer;
    }

    /**
     * set the prodFaqJSON to set
     * @param prodFaqJSON the prodFaqJSON to set
     */
    public void setProdFaqJSON(String prodFaqJSON) {
        this.prodFaqJSON = prodFaqJSON;
    }

    /**
     * get the prodFaqListPage
     * @return the prodFaqListPage
     */
    public ListPage<ProdFaq> getProdFaqListPage() {
        return prodFaqListPage;
    }

    /**
     * get the prodFaq
     * @return the prodFaq
     */
    public ProdFaq getProdFaq() {
        return prodFaq;
    }

}
