/**
 * 
 */
package org.eapp.poss.action;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.comobj.SessionAccount;
import org.eapp.poss.blo.IProdInfoBiz;
import org.eapp.poss.blo.IProdIssuePlanBiz;
import org.eapp.poss.config.SysConstants;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.ProdInfo;
import org.eapp.poss.hbean.ProdIssuePlan;

import com.alibaba.fastjson.JSON;

/**
 */
public class ProdIssuePlanAction extends BaseAction {

    /**
     * 
     */
    private static final long serialVersionUID = 3969928122388907674L;
    /**
     * logger
     */
    private static Log logger = LogFactory.getLog(ProdIssuePlanAction.class);
    
    //service
    private IProdIssuePlanBiz prodIssuePlanBiz;
    private IProdInfoBiz prodInfoBiz;
    // 输入参数
    private String prodId;
    private String jsonStr;
    
    // 输出参数
    private ProdIssuePlan prodIssuePlan;
    
    
    public ProdIssuePlan getProdIssuePlan() {
		return prodIssuePlan;
	}

	public void setProdIssuePlanBiz(IProdIssuePlanBiz prodIssuePlanBiz) {
		this.prodIssuePlanBiz = prodIssuePlanBiz;
	}

	public void setProdInfoBiz(IProdInfoBiz prodInfoBiz) {
		this.prodInfoBiz = prodInfoBiz;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}


	public String initEditProdPaln() {
    	SessionAccount user = (SessionAccount) getSession().getAttribute(SysConstants.SESSION_USER_KEY);
    	if (user == null) {
    		return error("请先登录");
	  	}
	   	if (StringUtils.isEmpty(prodId)) {
	   		return error("非法参数");
	  	}
        try {
        	prodIssuePlan = prodIssuePlanBiz.getProdIssuePlanByProdId(prodId);
            if (prodIssuePlan == null) {
            	prodIssuePlan = new ProdIssuePlan();
            	ProdInfo prodInfo = prodInfoBiz.getProdInfoById(prodId);
            	if (prodInfo == null) {
            		return error("产品信息不存在");
            	}
            	prodIssuePlan.setProdInfo(prodInfo);
            }
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("queryProdFaqById failed: ", e);
            return error(e.getMessage());
        }
    }
    
    
    public String editProdPlan() {
        // params judge
        if (StringUtils.isEmpty(jsonStr)) {
            return error("非法参数：产品问答对象为空");
        }
        try {
        	ProdIssuePlan transProdFaq  = (ProdIssuePlan)JSON.parseObject(jsonStr, ProdIssuePlan.class);
        	ProdIssuePlan plan = prodIssuePlanBiz.addOrModifyProdIssuePlan(transProdFaq);
        	ActionLogger.log(getRequest(), plan.getId(), plan.toString());
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("updateProdFaq failed: ", e);
            return error();
        }
    }
    
    public String viewProdPlan() {
        if (StringUtils.isEmpty(prodId)) {
            return error("非法参数");
        }
        try {
        	prodIssuePlan = prodIssuePlanBiz.getProdIssuePlanByProdId(prodId);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("queryProdFaqById failed: ", e);
            return error();
        }
    }

}
