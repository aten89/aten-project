/**
 * 
 */
package org.eapp.poss.action;

import java.io.File;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.comobj.SessionAccount;
import org.eapp.poss.blo.IProdInfoBiz;
import org.eapp.poss.config.SysConstants;
import org.eapp.poss.dao.param.ProdInfoQueryParameters;
import org.eapp.poss.dto.ProdInfoSelect;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.Attachment;
import org.eapp.poss.hbean.ProdInfo;
import org.eapp.poss.util.HTMLResponse;
import org.eapp.poss.util.XMLResponse;
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
public class ProdInfoAction extends BaseAction {

    /**
     * 
     */
    private static final long serialVersionUID = -5844550277277380145L;
    
    /**
     * logger
     */
    private static Log logger = LogFactory.getLog(ProdInfoAction.class);
    
    // service
    private IProdInfoBiz prodInfoBiz;
    
    private String jsoncallback;
    private String htmlValue;
    
    
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
    /**
     * 发行日期开始
     */
    private Date sellDateBegin;
    
    private Date sellDateEnd;
    
    private String supplierId;
    
    private String prodType;
    
    private String prodSecondaryClassify;
    
    private String prodStatus;
    private String excProdStatus;//排除状态
    
    private String prodInfoJSON;
    
    private File attachment;
    
    private String extName;// 升级包说明文件扩展名
    
    private String displayName;// 升级包说明文件名
    
    // 输出参数
    private ListPage<ProdInfo> prodInfoListPage;
    
    private ProdInfo prodInfo;
    
    // method
    public String queryProdInfoListPage() {
        ProdInfoQueryParameters qp = createProdInfoQP();
        try {
            prodInfoListPage = prodInfoBiz.queryProdInfoListPage(qp, true);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("queryProdInfoListPage failed: ", e);
            return error();
        }
    }
    
    public String initAddProdInfo() {
        SessionAccount user = (SessionAccount) getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
            return error("请先登录");
        }
        return success();
    }
    
    public void saveAttachmentToTempDir() {
        try {
            if (attachment == null) {
                XMLResponse.outputStandardResponse(getResponse(), 0, "请上传附件！");
            } else if (attachment.length() <= 0) {
                XMLResponse.outputStandardResponse(getResponse(), 0, "不能上传0kb文件！");
            } else {
                Attachment attach = prodInfoBiz.saveAttachmentToTempDir(attachment, extName, displayName);
                // 把附件对象放入session中
                // String path = attachment.getAbsFilePath();
                String path = attach.getFilePath();
                XMLResponse.outputStandardResponse(getResponse(), 1, path);
            }

        } catch (Exception e) {
            logger.error("saveAttachmentToTempDir failed:", e);
        }
    }
    
    public String addProdInfo() {
        // params judge
        if (StringUtils.isEmpty(prodInfoJSON)) {
            return error("非法参数：产品信息为空");
        }
        try {
            ProdInfo transProdInfo  = (ProdInfo)JSON.parseObject(prodInfoJSON, ProdInfo.class);
            ProdInfo prod = prodInfoBiz.txAddProdInfo(transProdInfo);
            ActionLogger.log(getRequest(), prod.getId(), prod.toString());
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("txAddProdInfo failed: ", e);
            return error();
        }
    }
    
    public String updateProdInfo() {
        // params judge
        if (StringUtils.isEmpty(prodInfoJSON)) {
            return error("非法参数：产品信息为空");
        }
        try {
            ProdInfo transProdInfo  = (ProdInfo)JSON.parseObject(prodInfoJSON, ProdInfo.class);
            prodInfoBiz.txUpdateProdInfo(transProdInfo);
            ActionLogger.log(getRequest(), transProdInfo.getId(), transProdInfo.toString());
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("updateProdInfo failed: ", e);
            return error();
        }
    }
    
    public String viewProdInfo() {
        // params judge
        if (StringUtils.isEmpty(id)) {
            return error("非法参数：产品ID为空");
        }
        try {
            prodInfo = prodInfoBiz.queryProdInfoById(id);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("queryProdInfoById failed: ", e);
            return error();
        }
    }
    
    public String delProdInfo() {
        // params judge
        if (StringUtils.isEmpty(id)) {
            return error("非法参数：产品ID为空");
        }
        try {
        	ProdInfo prodInfo = prodInfoBiz.txDelProdInfo(id);
            ActionLogger.log(getRequest(), prodInfo.getId(), prodInfo.toString());
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("txDelProdInfo failed: ", e);
            return error();
        }
    }
    
    public String initProdInfoFrame() {
        return success();
    }
    
    
    private ProdInfoQueryParameters createProdInfoQP() {
        ProdInfoQueryParameters qp = new ProdInfoQueryParameters();
        qp.setPageNo(pageNo);
        qp.setPageSize(pageSize);
        // 增加对排序列的处理
        if (StringUtils.isNotEmpty(sortCol)) {
            qp.addOrder(sortCol, ascend);
        } else {
//            qp.addOrder("prodCode", false);
//            qp.addOrder("prodName", false);
        	qp.addOrder("sellAmount", true);
        }
        if (sellDateBegin != null) {
            qp.setSellDateBegin(sellDateBegin);
        }
        if (sellDateEnd != null) {
            qp.setSellDateEnd(sellDateEnd);
        }
        if (StringUtils.isNotEmpty(supplierId)) {
            qp.setSupplierId(supplierId);
        }
        if (StringUtils.isNotEmpty(prodType)) {
            qp.setProdType(prodType);
        }
        if (StringUtils.isNotEmpty(prodSecondaryClassify)) {
            qp.setProdSecondaryClassify(prodSecondaryClassify);
        }
        if (StringUtils.isNotEmpty(prodStatus)) {
            qp.setProdStatus(prodStatus);
        }
        if (StringUtils.isNotEmpty(excProdStatus)) {
            qp.setExcProdStatus(excProdStatus);
        }
        return qp;
    }
    
    /**
     * 初始化产品下拉框
     * 
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-25	黄云耿	新建
     * </pre>
     */
    public String initProdInfoSel() {
    	try {
    		ProdInfoQueryParameters qp = createProdInfoQP();
    		prodInfoListPage = prodInfoBiz.queryProdInfoListPage(qp, false);
    		if (prodInfoListPage == null || prodInfoListPage.getDataList() == null) {
    			return error();
    		}
            String sHtml = "";
            
            if (prodInfoListPage != null && prodInfoListPage.getDataList() != null) {
                sHtml = new ProdInfoSelect(prodInfoListPage.getDataList()).toString();
            }
            
            if(jsoncallback != null){
                htmlValue = sHtml;
                return success();
            } else {
                HTMLResponse.outputHTML(getResponse(), sHtml);
                return null;
            }
        } catch (Exception e) {
            logger.error("initProdInfoSel failed: ", e);
            return error();
        }
    	
    	
//        try {
//            ProdInfoQueryParameters qp = createProdInfoQP();
//            prodInfoListPage = prodInfoBiz.queryProdInfoListPage(qp);
//     
//            if (prodInfoListPage == null || prodInfoListPage.getDataList() == null) {
//                return;
//            }
//            
//            HTMLResponse.outputHTML(getResponse(), new ProdInfoSelect(prodInfoListPage.getDataList()).toString());
//        } catch (Exception e) {
//            logger.error("initCustomerInfoSel failed: ", e);
//            return;
//        }
    }
    
    /**
     * 通过ID获取产品
     * @return 操作结果
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-5-25	黄云耿	新建
     * </pre>
     */
    public String getProdInfoById() {
        
        if (StringUtils.isEmpty(id)) {
            return success();
        }
        try {
            prodInfo = prodInfoBiz.queryProdInfoById(id);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("getProdInfoById failed: ", e);
            return error();
        }
    }
    
    
    
    /************get and set*****************/

    /**
     * set the prodInfoBiz to set
     * @param prodInfoBiz the prodInfoBiz to set
     */
    public void setProdInfoBiz(IProdInfoBiz prodInfoBiz) {
        this.prodInfoBiz = prodInfoBiz;
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
     * set the sellDateBegin to set
     * @param sellDateBegin the sellDateBegin to set
     */
    public void setSellDateBegin(Date sellDateBegin) {
        this.sellDateBegin = sellDateBegin;
    }

    /**
     * set the sellDateEnd to set
     * @param sellDateEnd the sellDateEnd to set
     */
    public void setSellDateEnd(Date sellDateEnd) {
        this.sellDateEnd = sellDateEnd;
    }

    /**
     * set the supplierId to set
     * @param supplierId the supplierId to set
     */
    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * set the prodType to set
     * @param prodType the prodType to set
     */
    public void setProdType(String prodType) {
        this.prodType = prodType;
    }

    /**
     * set the prodSecondaryClassify to set
     * @param prodSecondaryClassify the prodSecondaryClassify to set
     */
    public void setProdSecondaryClassify(String prodSecondaryClassify) {
        this.prodSecondaryClassify = prodSecondaryClassify;
    }

    /**
     * set the prodStatus to set
     * @param prodStatus the prodStatus to set
     */
    public void setProdStatus(String prodStatus) {
        this.prodStatus = prodStatus;
    }
    
    public void setExcProdStatus(String excProdStatus) {
        this.excProdStatus = excProdStatus;
    }

    /**
     * set the prodInfoJSON to set
     * @param prodInfoJSON the prodInfoJSON to set
     */
    public void setProdInfoJSON(String prodInfoJSON) {
        this.prodInfoJSON = prodInfoJSON;
    }

    /**
     * set the attachment to set
     * @param attachment the attachment to set
     */
    public void setAttachment(File attachment) {
        this.attachment = attachment;
    }

    /**
     * set the extName to set
     * @param extName the extName to set
     */
    public void setExtName(String extName) {
        this.extName = extName;
    }

    /**
     * set the displayName to set
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * get the prodInfoListPage
     * @return the prodInfoListPage
     */
    public ListPage<ProdInfo> getProdInfoListPage() {
        return prodInfoListPage;
    }

    /**
     * get the prodInfo
     * @return the prodInfo
     */
    public ProdInfo getProdInfo() {
        return prodInfo;
    }

	public String getJsoncallback() {
		return jsoncallback;
	}

	public void setJsoncallback(String jsoncallback) {
		this.jsoncallback = jsoncallback;
	}

	public String getHtmlValue() {
		return htmlValue;
	}

	public void setHtmlValue(String htmlValue) {
		this.htmlValue = htmlValue;
	}
}
