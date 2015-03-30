/**
 * 
 */
package org.eapp.poss.blo.imp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.poss.blo.IProdInfoBiz;
import org.eapp.poss.config.SysConstants;
import org.eapp.poss.dao.IBlankContractDAO;
import org.eapp.poss.dao.IConfirmExtendDAO;
import org.eapp.poss.dao.IContractHandDAO;
import org.eapp.poss.dao.IContractRequestDAO;
import org.eapp.poss.dao.IProdInfoDAO;
import org.eapp.poss.dao.IProdIssuePlanDAO;
import org.eapp.poss.dao.IProdTypeDAO;
import org.eapp.poss.dao.param.ProdInfoQueryParameters;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.Attachment;
import org.eapp.poss.hbean.BlankContract;
import org.eapp.poss.hbean.CustPayment;
import org.eapp.poss.hbean.CustRefund;
import org.eapp.poss.hbean.ExpectYearYield;
import org.eapp.poss.hbean.Message;
import org.eapp.poss.hbean.ProdInfo;
import org.eapp.poss.hbean.ProdPayDate;
import org.eapp.poss.hbean.ProdType;
import org.eapp.poss.util.SerialNoCreater;
import org.eapp.poss.util.Tools;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.upload.FileDispatcher;
import org.eapp.util.web.upload.FileUtil;
import org.hibernate.Hibernate;
import org.springframework.beans.BeanUtils;

/**
 * 
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-21	lhg		新建
 * </pre>
 */
public class ProdInfoBiz implements IProdInfoBiz {
    
    private static Log logger = LogFactory.getLog(ProdInfoBiz.class);
    
    // dao
    private IProdInfoDAO prodInfoDAO;
    private IProdTypeDAO prodTypeDAO;
    private IProdIssuePlanDAO prodIssuePlanDAO;
    
    private IBlankContractDAO blankContractDAO;
    private IContractRequestDAO contractRequestDAO;
	private IContractHandDAO contractHandDAO;
	private IConfirmExtendDAO confirmExtendDAO;
    
    public void setBlankContractDAO(IBlankContractDAO blankContractDAO) {
		this.blankContractDAO = blankContractDAO;
	}
    
    public void setContractRequestDAO(IContractRequestDAO contractRequestDAO) {
		this.contractRequestDAO = contractRequestDAO;
	}

	public void setContractHandDAO(IContractHandDAO contractHandDAO) {
		this.contractHandDAO = contractHandDAO;
	}
	
	public void setConfirmExtendDAO(IConfirmExtendDAO confirmExtendDAO) {
		this.confirmExtendDAO = confirmExtendDAO;
	}
	
    @Override
    public ProdInfo getProdInfoById(String id) throws PossException {
        // params judge
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        return prodInfoDAO.findById(id);
    }

    /* (non-Javadoc)
     * @see org.eapp.poss.blo.IProdInfoBiz#queryProdInfoById(java.lang.String)
     */
    @Override
    public ProdInfo queryProdInfoById(String id) throws PossException {
        // params judge
        if (StringUtils.isEmpty(id)) {
            throw new IllegalArgumentException("非法参数：产品ID为空");
        }
        ProdInfo prodInfo = prodInfoDAO.findById(id);
        if (prodInfo == null) {
            throw new PossException("产品不存在");
        }
        initProdInfo(prodInfo);
        return prodInfo;
    }

    /* (non-Javadoc)
     * @see org.eapp.poss.blo.IProdInfoBiz#txAddProdInfo(org.eapp.poss.hbean.ProdInfo)
     */
    @Override
    public ProdInfo txAddProdInfo(ProdInfo transProdInfo) throws PossException {
        if (transProdInfo == null) {
            throw new IllegalArgumentException("非法参数：产品信息对象为空");
        }
        checkProdInfo(transProdInfo);
        transProdInfo.setId(null);
        if (transProdInfo.getExpectYearYields() != null) {
            for (ExpectYearYield expectYearYield : transProdInfo.getExpectYearYields()) {
                expectYearYield.setId(null);
                expectYearYield.setProdInfo(transProdInfo);
            }
        }
        if (transProdInfo.getProdPayDates() != null) {
            for (ProdPayDate prodPayDate : transProdInfo.getProdPayDates()) {
                prodPayDate.setId(null);
                prodPayDate.setProdInfo(transProdInfo);
            }
        }
        if (transProdInfo.getJaneEditionAttch() != null) {
            saveAttachment(transProdInfo, transProdInfo.getJaneEditionAttch(), ProdInfo.ATTCH_TYPE_JANE_EDITION);
        }
        if (transProdInfo.getOtherEditionAttch() != null) {
            saveAttachment(transProdInfo, transProdInfo.getOtherEditionAttch(), ProdInfo.ATTCH_TYPE_OTHER_EDITION);
        }
        prodInfoDAO.save(transProdInfo);
        return transProdInfo;
    }
    
    /* (non-Javadoc)
     * @see org.eapp.poss.blo.IProdInfoBiz#txUpdateProdInfo(org.eapp.poss.hbean.ProdInfo)
     */
    @Override
    public void txUpdateProdInfo(ProdInfo transProdInfo) throws PossException {
        if (transProdInfo == null) {
            throw new IllegalArgumentException("非法参数：产品信息对象为空");
        }
        if (StringUtils.isEmpty(transProdInfo.getId())) {
            throw new IllegalArgumentException("非法参数：产品ID为空");
        }
        ProdInfo perisProdInfo = prodInfoDAO.findById(transProdInfo.getId());
        if (perisProdInfo == null) {
            throw new PossException("产品不存在");
        }
        checkProdInfo(transProdInfo);
        BeanUtils.copyProperties(transProdInfo, perisProdInfo, new String[] { "otherEditionAttch", "janeEditionAttch",
                "expectYearYields", "prodPayDates", "prodFaqs", "messages", "custRefunds", "custPayments" });
        perisProdInfo.getExpectYearYields().clear();
        perisProdInfo.getProdPayDates().clear();
        if (transProdInfo.getExpectYearYields() != null) {
            for (ExpectYearYield expectYearYield : transProdInfo.getExpectYearYields()) {
                expectYearYield.setId(null);
                expectYearYield.setProdInfo(perisProdInfo);
            }
            perisProdInfo.getExpectYearYields().addAll(transProdInfo.getExpectYearYields());
        }
        if (transProdInfo.getProdPayDates() != null) {
            for (ProdPayDate prodPayDate : transProdInfo.getProdPayDates()) {
                prodPayDate.setId(null);
                prodPayDate.setProdInfo(perisProdInfo);
            }
            perisProdInfo.getProdPayDates().addAll(transProdInfo.getProdPayDates());
        }
        saveAttachment(perisProdInfo, transProdInfo.getJaneEditionAttch(), ProdInfo.ATTCH_TYPE_JANE_EDITION);
        saveAttachment(perisProdInfo, transProdInfo.getOtherEditionAttch(), ProdInfo.ATTCH_TYPE_OTHER_EDITION);
        prodInfoDAO.saveOrUpdate(perisProdInfo);

    }
    
    private void saveAttachment(ProdInfo prodInfo, Attachment attachment, String attchType) throws PossException {
        if (attachment != null) {
            if (StringUtils.isEmpty(attachment.getFilePath())) {
                return;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM/");
            String dir = SysConstants.PROD_INFO_ATTACH_DIR + sdf.format(new Date());
            File file = new File(attachment.getFilePath());
            Attachment am = new Attachment(attachment.getFilePath(), file.length());
            String newPath = dir + Tools.generateUUID() + am.getFileExt();
            // 保存附件
            try {
                FileUtil.saveFile(FileDispatcher.getSaveDir(FileDispatcher.getAbsPath(newPath)), new FileInputStream(
                        file));
            } catch (IOException e) {
                throw new PossException("文件拷贝出错!!!");
            }
            am.setFilePath(newPath);
            am.setDisplayName(attachment.getDisplayName().replace("\\", ""));
            am.setUploadDate(new Date());
            prodInfoDAO.saveOrUpdate(am);
            
            
            // 判断是新增还是修改
            if (StringUtils.isNotEmpty(prodInfo.getId())) {
                // 修改
                Attachment oldAttch = null;
                if (ProdInfo.ATTCH_TYPE_JANE_EDITION.equals(attchType)) {
                    oldAttch = prodInfo.getJaneEditionAttch();
                } else if (ProdInfo.ATTCH_TYPE_OTHER_EDITION.equals(attchType)) {
                    oldAttch = prodInfo.getOtherEditionAttch();
                }
                if (oldAttch != null) {
                    File oldAttchFile = new File(oldAttch.getFullPath());
                    prodInfoDAO.delete(oldAttch);
                    oldAttchFile.delete();
                }
            }
            if (ProdInfo.ATTCH_TYPE_JANE_EDITION.equals(attchType)) {
                prodInfo.setJaneEditionAttch(am);
            } else if (ProdInfo.ATTCH_TYPE_OTHER_EDITION.equals(attchType)) {
                prodInfo.setOtherEditionAttch(am);
            }
            prodInfoDAO.saveOrUpdate(prodInfo);
        } else {
            // 判断是新增还是修改
            if (StringUtils.isNotEmpty(prodInfo.getId())) {
                // 修改
                Attachment oldAttch = null;
                if (ProdInfo.ATTCH_TYPE_JANE_EDITION.equals(attchType)) {
                    oldAttch = prodInfo.getJaneEditionAttch();
                } else if (ProdInfo.ATTCH_TYPE_OTHER_EDITION.equals(attchType)) {
                    oldAttch = prodInfo.getOtherEditionAttch();
                }
                if (oldAttch != null) {
                    File oldAttchFile = new File(oldAttch.getFullPath());
                    prodInfoDAO.delete(oldAttch);
                    oldAttchFile.delete();
                    if (ProdInfo.ATTCH_TYPE_JANE_EDITION.equals(attchType)) {
                        prodInfo.setJaneEditionAttch(null);
                    } else if (ProdInfo.ATTCH_TYPE_OTHER_EDITION.equals(attchType)) {
                        prodInfo.setOtherEditionAttch(null);
                    }
                    prodInfoDAO.saveOrUpdate(prodInfo);
                }
            }
        }
    }
    
    private void checkProdInfo(ProdInfo prodInfo) throws PossException {
        // 判断产品代码和产品名称不能重复
        if (prodInfo == null) {
            throw new IllegalArgumentException("非法参数：产品对象为空");
        }
        if (StringUtils.isEmpty(prodInfo.getProdCode())) {
            throw new PossException("产品代码不能为空");
        }
        if (StringUtils.isEmpty(prodInfo.getProdName())) {
            throw new PossException("产品名称不能为空");
        }
//        if (StringUtils.isEmpty(prodInfo.getProdType())) {
//            throw new PossException("产品类型不能为空");
//        }
//        if (StringUtils.isEmpty(prodInfo.getProdSecondaryClassify())) {
//            throw new PossException("产品二级分类不能为空");
//        }
        if (prodInfo.getPjtTotalAmount() == null) {
            throw new PossException("项目总额度不能为空");
        }
        if (prodInfo.getSellAmount() == null) {
            throw new PossException("发行额度不能为空");
        }
        ProdInfoQueryParameters qp = new ProdInfoQueryParameters();
        qp.setPageNo(1);
        qp.setPageSize(0);
        qp.setProdCode(prodInfo.getProdCode());
        if (StringUtils.isNotEmpty(prodInfo.getId())) {
            qp.setExcludeProdId(prodInfo.getId());
        }
        ListPage<ProdInfo> prodInfoListPage = prodInfoDAO.findProdInfoListPage(qp);
        if (prodInfoListPage != null && prodInfoListPage.getDataList() != null && !prodInfoListPage.getDataList().isEmpty()) {
            throw new PossException("已存在相同产品代码的产品");
        }
        qp.removeParameter("prodCode");
        qp.setProdName(prodInfo.getProdName());
        prodInfoListPage = prodInfoDAO.findProdInfoListPage(qp);
        if (prodInfoListPage != null && prodInfoListPage.getDataList() != null && !prodInfoListPage.getDataList().isEmpty()) {
            throw new PossException("已存在相同产品名称的产品");
        }
        
    }
    
    /* (non-Javadoc)
     * @see org.eapp.poss.blo.IProdInfoBiz#txDelProdInfo(java.lang.String)
     */
    @Override
    public ProdInfo txDelProdInfo(String id) throws PossException {
        // params judge
        if (StringUtils.isEmpty(id)) {
            throw new IllegalArgumentException("非法参数：产品ID为空");
        }
        ProdInfo prodInfo = prodInfoDAO.findById(id);
        if (prodInfo == null) {
            throw new PossException("产品不存在");
        }
        // check can del
        if (prodInfo.getCustPayments() != null && !prodInfo.getCustPayments().isEmpty()) {
            for (CustPayment custPayment : prodInfo.getCustPayments()) {
                custPayment.setProdId(null);
            }
        }
        if (prodInfo.getMessages() != null && !prodInfo.getMessages().isEmpty()) {
            for (Message message : prodInfo.getMessages()) {
                message.setProdInfo(null);
            }
        }
        if (prodInfo.getCustRefunds() != null && !prodInfo.getCustRefunds().isEmpty()) {
            for (CustRefund custRefund : prodInfo.getCustRefunds()) {
                custRefund.setProdInfo(null);
            }
        }
        //删除发行方案
        prodIssuePlanDAO.deleteByProdId(prodInfo.getId());
        //删除空白合同
        BlankContract bcon = blankContractDAO.findByProdId(prodInfo.getId());
        if (bcon != null) {
        	blankContractDAO.delete(bcon);
        }
        //删除合同需求
        contractRequestDAO.deleteAll(prodInfo.getId(), null);
        //删除合同上交
        contractHandDAO.deleteAll(prodInfo.getId(), null);
        //删除合同发放
        confirmExtendDAO.deleteAll(prodInfo.getId(), null);
        
        //附件
        Attachment otherEditionAttch = prodInfo.getOtherEditionAttch();
        Attachment janeEditionAttch = prodInfo.getJaneEditionAttch();
        
        prodInfoDAO.delete(prodInfo);
        //删除附件
        if (otherEditionAttch != null) {
        	prodInfoDAO.delete(otherEditionAttch);
        	// 删除文件
			File f = FileDispatcher.findFile(FileDispatcher.getAbsPath(otherEditionAttch.getFilePath()));
			if (f != null) {
				f.delete();
			}
        }
        if (janeEditionAttch != null) {
        	prodInfoDAO.delete(janeEditionAttch);
        	// 删除文件
			File f = FileDispatcher.findFile(FileDispatcher.getAbsPath(janeEditionAttch.getFilePath()));
			if (f != null) {
				f.delete();
			}
        }
        
        return prodInfo;
    }

    /* (non-Javadoc)
     * @see org.eapp.poss.blo.IProdInfoBiz#queryProdInfoListPage(org.eapp.poss.dao.param.ProdInfoQueryParameters)
     */
    @Override
    public ListPage<ProdInfo> queryProdInfoListPage(ProdInfoQueryParameters qp, boolean initFlag) throws PossException {
        // params judeg
        if (qp == null) {
            throw new IllegalArgumentException("非法参数：查询条件为空");
        }
        ListPage<ProdInfo> prodInfoListPage = prodInfoDAO.findProdInfoListPage(qp);
        if (initFlag) {
	        if (prodInfoListPage != null && prodInfoListPage.getDataList() != null) {
	            for (ProdInfo prodInfo : prodInfoListPage.getDataList()) {
	                // init obj
	                initProdInfo(prodInfo);
	            }
	        }
        }
        return prodInfoListPage;
    }
    
    private void initProdInfo(ProdInfo prodInfo) throws PossException {
        // param judge
        if (prodInfo == null) {
            throw new IllegalArgumentException("非法参数：产品对象为空");
        }
        Hibernate.initialize(prodInfo.getJaneEditionAttch());
        Hibernate.initialize(prodInfo.getOtherEditionAttch());
        Hibernate.initialize(prodInfo.getSupplier());
        Hibernate.initialize(prodInfo.getProdFaqs());
        Hibernate.initialize(prodInfo.getExpectYearYields());
        Hibernate.initialize(prodInfo.getProdPayDates());
        Hibernate.initialize(prodInfo.getMessages());
        Hibernate.initialize(prodInfo.getCustRefunds());
        Hibernate.initialize(prodInfo.getCustPayments());
        
        if (StringUtils.isNotEmpty(prodInfo.getProdType())) {
            ProdType prodType = prodTypeDAO.findByID(prodInfo.getProdType());
            if (prodType != null) {
                prodInfo.setProdTypeName(prodType.getProdType());
            }
        }
        if (StringUtils.isNotEmpty(prodInfo.getProdSecondaryClassify())) {
            ProdType secondaryClassify = prodTypeDAO.findByID(prodInfo.getProdSecondaryClassify());
            if (secondaryClassify != null) {
                prodInfo.setSecondClassifyName(secondaryClassify.getProdType());
            }
        }
    }
    
    @Override
    public Attachment saveAttachmentToTempDir(File upgradeFile, String extName, String displayName)
            throws PossException {
        if (upgradeFile.length() <= 0) {
            throw new PossException("不能上传0kb的文件!!!");
        }
        String fileName = SerialNoCreater.createUUID() + extName;
        // 这个是临时目录
        String fileFullName = FileDispatcher.getTempDir() + fileName;
        try {
            FileUtil.saveFile(fileFullName, new FileInputStream(upgradeFile));
        } catch (IOException e) {
            logger.error("saveFile failed: ", e);
            throw new PossException("文件拷贝出错!!!");
        }
        Attachment am = null;
        if (StringUtils.isNotEmpty(fileFullName)) {
            File file = new File(fileFullName);
            am = new Attachment(fileFullName, file.length());
            am.setDisplayName(displayName.replace("\\", ""));
            am.setUploadDate(new Date());
            am.setFilePath(fileFullName);
        }
        return am;
    }

    /**
     * set the prodInfoDAO to set
     * @param prodInfoDAO the prodInfoDAO to set
     */
    public void setProdInfoDAO(IProdInfoDAO prodInfoDAO) {
        this.prodInfoDAO = prodInfoDAO;
    }

    /**
     * set the prodTypeDAO to set
     * @param prodTypeDAO the prodTypeDAO to set
     */
    public void setProdTypeDAO(IProdTypeDAO prodTypeDAO) {
        this.prodTypeDAO = prodTypeDAO;
    }

	public void setProdIssuePlanDAO(IProdIssuePlanDAO prodIssuePlanDAO) {
		this.prodIssuePlanDAO = prodIssuePlanDAO;
	}

}
