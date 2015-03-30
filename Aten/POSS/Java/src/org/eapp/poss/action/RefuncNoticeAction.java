/**
 * 
 */
package org.eapp.poss.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.poss.blo.IRefuncNoticeBiz;
import org.eapp.poss.config.SysConstants;
import org.eapp.poss.dao.param.RefuncNoticeQueryParameters;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.Attachment;
import org.eapp.poss.hbean.RefuncNotice;
import org.eapp.poss.util.Tools;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.upload.FileDispatcher;
import org.eapp.util.web.upload.FileUtil;

import com.alibaba.fastjson.JSON;

/**
 * 
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-21	lhg		新建
 * </pre>
 */
public class RefuncNoticeAction extends BaseAction {

    /**
     * 
     */
    private static final long serialVersionUID = -1844550277277380145L;
    
    // log
    private static Log logger = LogFactory.getLog(RefuncNoticeAction.class);
    
    // service
    private IRefuncNoticeBiz refuncNoticeBiz;

    
    // 输入参数
    private Date createTimeBegin;
    private Date createTimeEnd;
    private String trustCompany;
    private String refuncNoticeJson;
    private String referid;
    private String id;							//ID
    private List<File> uploadFiles;				//上传文件
    private List<String> uploadFilesFileName;	// 上传文件的文件名
    private String[] delFilenames;				// 要删除附件的名称
    // 输出参数
    private ListPage<RefuncNotice> listPage;
    
    private RefuncNotice refuncNotice;
    
    private Set<Attachment> refunNoticeAttachments;
    
    // 方法
    
    /**
     * 根据参数获取退款须知列表
     * @return
     */
    public String queryRefuncNoticeListPage() {
    	RefuncNoticeQueryParameters qp = createRefuncNoticeQP();
        try {
        	listPage = refuncNoticeBiz.queryRefuncNoticeListPage(qp);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("queryProdInfoListPage failed: ", e);
            return error();
        }
    }
    
    /**
     * 初始化修改页面
     * @return
     */
    public String initEditRefuncNotice() {
    	try {
    		refuncNotice = refuncNoticeBiz.queryRefuncNoticeById(id);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("initEditRefuncNotice failed: ", e);
            return error();
        }
    }
    
    /**
     * 修改退款须知
     * @return
     */
    public String modifyRefuncNotice() {
    	if (StringUtils.isEmpty(refuncNoticeJson)) {
            return error("非法参数：退款须知为空");
        }
        try {
        	RefuncNotice refuncNoticeTmp  = (RefuncNotice)JSON.parseObject(refuncNoticeJson, RefuncNotice.class);
        	refuncNotice = refuncNoticeBiz.txUpdateRefuncNotice(refuncNoticeTmp);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("txAddProdInfo failed: ", e);
            return error();
        }
    }
    
    /**
     * 添加退款须知
     * @return
     */
    public String addRefuncNotice() {
    	if (StringUtils.isEmpty(refuncNoticeJson)) {
            return error("非法参数：退款须知为空");
        }
        try {
        	RefuncNotice refuncNoticeTmp  = (RefuncNotice)JSON.parseObject(refuncNoticeJson, RefuncNotice.class);
        	refuncNotice = refuncNoticeBiz.txAddRefuncNotice(refuncNoticeTmp);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("txAddProdInfo failed: ", e);
            return error();
        }
    }
    
    /**
     * 删除退款须知
     * @return
     */
    public String delRefuncNotice() {
        try {
        	refuncNoticeBiz.txDelRefuncNotice(id);
            return success();
        } catch (PossException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("delRefuncNotice failed: ", e);
            return error();
        }
    }
    
    /**
     * 组装查询参数
     * @return
     */
    private RefuncNoticeQueryParameters createRefuncNoticeQP() {
    	RefuncNoticeQueryParameters qp = new RefuncNoticeQueryParameters();
    	if(null != createTimeBegin) {
    		qp.setCreateTimeBegin(createTimeBegin);
    	}
    	
    	if(null != createTimeEnd) {
    		qp.setCreateTimeEnd(createTimeEnd);
    	}
    	
    	if(!StringUtils.isEmpty(trustCompany)) {
    		qp.setTrustCompany(trustCompany);
    	}
    	return qp;
    }
    
    /************附件上传相关*****************/
    /**
     * 根据退款须知ID加载对应的附件列表
     * @return
     */
    public String loadRefuncNoticeAttachments() {
        try {
        	refunNoticeAttachments = refuncNoticeBiz.queryRefuncNoticeById(referid).getReFuncNoticAttachments();
            return success();
        } catch (Exception e) {
        	logger.error("loadAttachment failed:", e);
            return error("系统错误");
        }
    }
    
    /**
     * 上传退款须知附件（带业务）
     * @throws IOException
     */
    public String uploadRefuncNoticeFile() throws IOException {
        try {
        	//先上传文件
            List<Attachment> files = uploadFiles();
            //对业务进行操作
            refuncNoticeBiz.txBatchUploadRefuncNoticeAttachment(referid, delFilenames, files);
            // HtmlResponse MessageDTO 不存在
//            planUpgradeCustomerService.txUploadlicenceAttachment(referid, delFilenames, files);
//            HTMLResponse.outputHTML(getResponse(), new MessageDTO("1", "保存成功").toString());
        } catch (PossException e) {
//            HTMLResponse.outputHTML(getResponse(), new MessageDTO("0", e.getMessage()).toString());
        	e.printStackTrace();
        } catch (Exception e) {
            logger.error("上传许可证失败", e);
            e.printStackTrace();
//            HTMLResponse.outputHTML(getResponse(), new MessageDTO("0", "上载文件失败").toString());
        }
        return success("1");
    }
    
    /**
     * 上传文件，并且转化为Attachment
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    private List<Attachment> uploadFiles() throws PossException, FileNotFoundException, IOException {
        List<Attachment> files = new ArrayList<Attachment>();
        if (uploadFiles != null && uploadFiles.size() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM/");
            String dir = SysConstants.REFUNC_NOTICE_DIR + sdf.format(new Date());
            for (int i = 0; i < uploadFiles.size(); i++) {
                String fileName = uploadFilesFileName.get(i);
                if (fileName == null || fileName.trim().equals("")) {
                    continue;
                }
                File file = uploadFiles.get(i);
                if (file.length() == 0) {
                    throw new PossException("文件“" + fileName + "”大小不能为空！");
                }
//                if (maxUploadSize > 0 && maxUploadSize < file.length()) {
//                    throw new PossException("文件“" + fileName + "”不能大于" + maxUploadSize / 1024 + "KB");
//                }
                Attachment am = new Attachment(fileName, file.length());
                String path = dir + Tools.generateUUID() + am.getFileExt();
                am.setFilePath(path);
                // 保存附件
                FileUtil.saveFile(FileDispatcher.getSaveDir(FileDispatcher.getAbsPath(path)), new FileInputStream(file));
                files.add(am);
            }
        }
        return files;
    }
    
    /************get and set*****************/
    public void setRefuncNoticeBiz(IRefuncNoticeBiz refuncNoticeBiz) {
    	this.refuncNoticeBiz = refuncNoticeBiz;
    }

	public ListPage<RefuncNotice> getListPage() {
		return listPage;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Set<Attachment> getRefunNoticeAttachments() {
		return refunNoticeAttachments;
	}

	public void setUploadFiles(List<File> uploadFiles) {
		this.uploadFiles = uploadFiles;
	}

	public void setUploadFilesFileName(List<String> uploadFilesFileName) {
		this.uploadFilesFileName = uploadFilesFileName;
	}

	public void setDelFilenames(String[] delFilenames) {
		this.delFilenames = delFilenames;
	}

	public RefuncNotice getRefuncNotice() {
		return refuncNotice;
	}

	public void setRefuncNotice(RefuncNotice refuncNotice) {
		this.refuncNotice = refuncNotice;
	}

	public void setRefuncNoticeJson(String refuncNoticeJson) {
		this.refuncNoticeJson = refuncNoticeJson;
	}

	public void setReferid(String referid) {
		this.referid = referid;
	}

	public void setCreateTimeBegin(Date createTimeBegin) {
		this.createTimeBegin = createTimeBegin;
	}

	public void setCreateTimeEnd(Date createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	public void setTrustCompany(String trustCompany) {
		this.trustCompany = trustCompany;
	}
	
}
