/**
 * 
 */
package org.eapp.crm.action;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.comobj.SessionAccount;
import org.eapp.crm.blo.IImportCustomerBiz;
import org.eapp.crm.config.SysConstants;
import org.eapp.crm.dao.param.ImportCustomerQueryParameters;
import org.eapp.crm.dto.MultipleUserAccountExtDTO;
import org.eapp.crm.exception.CrmException;
import org.eapp.crm.hbean.ImportCustomer;
import org.eapp.crm.util.Tools;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.web.upload.FileDispatcher;
import org.eapp.util.web.upload.FileUtil;

import com.alibaba.fastjson.JSON;

/**
 * 导入客户Action
 * 
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-4-27	lhg		新建
 * </pre>
 */
public class ImportCustomerAction extends BaseAction {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1484507535505406726L;

    /**
     * 日志
     */
    private static Log logger = LogFactory.getLog(ImportCustomerAction.class);

    /**
     * 导入客户业务逻辑层接口
     */
    private IImportCustomerBiz importCustomerBiz;

    // 输入参数
    /**
     * id
     */
    private String id;
    /**
     * 销售人员账号
     */
    private String serviceAccountId;
    /**
     * 导入客户IDSJSOn
     */
    private String importCustIdsJson;
    /**
     * 随机分配的销售人员和数量JSON
     */
    private String multipleUserAccountExtJson;
    /**
     * 批次号
     */
    private String batchNumber;
    /**
     * 客户名称
     */
    private String customerName;
    /**
     * 电话
     */
    private String tel;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 文件路径
     */
	private String fileUrl;
    /**
     * 最大上传数
     */
    private long maxUploadSize;
    /**
     * 导入文件
     */
    private File importFile;
    /**
     * 分配开始时间
     */
    private Date allocateTimeBegin;
    /**
     * 分配结束时间
     */
    private Date allocateTimeEnd;
    /**
     * 分配（推送）标识
     */
    private Boolean allocateFlag;
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
    
    // 输出参数
    /**
     * 客户经理
     */
    private String customerManage;
    /**
     * 查询客户列表结果返回值
     */
    private ListPage<ImportCustomer> listPage;

    /**
     * 查询导入客户列表
     * @return 操作结果
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2014-4-28	黄云耿	新建
     * </pre>
     */
    public String queryCustomerList() {
      try {
    	  ImportCustomerQueryParameters qp = new ImportCustomerQueryParameters();
    	  if(StringUtils.isNotEmpty(batchNumber)){
    	      qp.addParameter("batchNumber", batchNumber);
    	  }
    	  if(StringUtils.isNotEmpty(customerName)){
              qp.addParameter("customerName", customerName);
          }
    	  if(StringUtils.isNotEmpty(tel)){
              qp.addParameter("tel", tel);
          }
          if(StringUtils.isNotEmpty(email)){
              qp.addParameter("email", email);
          }
          if(null != allocateTimeBegin) {
        	  qp.setAllocateTimeBegin(allocateTimeBegin);
          }
          if(null != allocateTimeEnd) {
        	  qp.setAllocateTimeEnd(allocateTimeEnd);
          }
          if(null != allocateFlag) {
        	  qp.setAllocateFlag(allocateFlag);
          }
      	  qp.setPageNo(pageNo);
          qp.setPageSize(pageSize);
          // 增加对排序列的处理
          if (StringUtils.isNotEmpty(sortCol)) {
              qp.addOrder(sortCol, ascend);
          }
          listPage = importCustomerBiz.queryCustomerList(qp);
          return success();
      } catch (CrmException e) {
          return error(e.getMessage());
      } catch (Exception e) {
    	  logger.error("queryCustomerList failed: ", e);
          return error("系统错误");
      }
  }

	/**
	 * 通过excel导入客户资料
	 * @return 操作结果
	 *
	 * <pre>
	 * 修改日期		修改人	修改原因
	 * 2014-4-28	黄云耿	新建
	 * </pre>
	 */
	public String importCustomer() {
	    // 获取当前登录用户
        SessionAccount user = (SessionAccount) getSession().getAttribute(SysConstants.SESSION_USER_KEY);
        if (user == null) {
          return error("请先登录");
        }
	    
	    String fileName = Tools.generateUUID() + ".xls";
	      try {
	          batchNumber = importCustomerBiz.txImportCustomer(new File((String) getSession().getAttribute("filePath")),
	                  fileName, new File(FileDispatcher.getTempDir()), user.getAccountID());
	          return success();
	      } catch (CrmException e) {
	          return error(e.getMessage());
	      } catch (IOException e) {
	          return error(e.getMessage());
	      } catch (Exception e) {
	          logger.error("importCustomer error", e);
	          return error();
	      }
	}
      
	/**
	 * 根据id 删除客户
	 * @return 操作结果
	 *
	 * <pre>
	 * 修改日期		修改人	修改原因
	 * 2014-4-29	黄云耿	新建
	 * </pre>
	 */
	public String deleteImportCustomer() {
        try {
            if (StringUtils.isEmpty(id)) {
                throw new CrmException("参数异常：ID不能为空");
            }
            importCustomerBiz.txDeleteImportCustomer(id);
            return success();
        } catch (CrmException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("deleteImportCustomer failed: ", e);
            return error("系统错误");
        }
    }
	
  /**
   * 将Excel文件拷贝到服务器 保存文件绝对路径到session中
   * 
   * @return 结果
   */
  public String uploadCustomer() {
      // 获取当前登录用户
      SessionAccount user = (SessionAccount) getSession().getAttribute(SysConstants.SESSION_USER_KEY);
      if (user == null) {
        return error("请先登录");
      }
      if (maxUploadSize > 0 && maxUploadSize < importFile.length()) {
          return error("导入文件不能大于" + maxUploadSize / 1024 + "KB");
      }
      try {
          String fileDir = FileDispatcher.getTempDir() + Tools.generateUUID() + ".xls";
          FileUtil.moveFile(importFile, new File(fileDir));
          getSession().setAttribute("filePath", fileDir);
          if (allocateFlag) {
              customerManage = user.getAccountID();
          }
          return success("确认要导入客户资料么？");
      } catch (Exception e) {
    	  logger.error("uploadWorkorder error", e);
          return error("客户资料导入失败");
      }
  }
  
  //临时上传
  public String uploadCustomerTemp() {
      try {
          String fileDir = "D:/客户上传文件/" + Tools.generateUUID() + ".xls";
          FileUtil.moveFile(importFile, new File(fileDir));
          getSession().setAttribute("filePath", fileDir);
          return success();
      } catch (Exception e) {
    	  logger.error("uploadWorkorder error", e);
          return error();
      }
  }
  
  
  public String initManualAllotPage() {
      return success();
  }
     

    /**
     * 手动分配的导入客户
     * 
     * @return 操作结果
     * 
     *         <pre>
     * 修改日期		修改人	修改原因
     * 2014-4-27	lhg		新建
     * </pre>
     */
    public String manualAllotImportCustomer() {
        // 参数判断
        if (StringUtils.isEmpty(serviceAccountId)) {
            return error("非法参数：销售人员账号为空");
        }
        if (StringUtils.isEmpty(importCustIdsJson)) {
            return error("非法参数：要分配的客户为空");
        }
        try {
            List<String> importCustIds = Arrays.asList(importCustIdsJson.split(","));
            importCustomerBiz.txManualAllotImportCustomer(serviceAccountId, importCustIds);
            return success();
        } catch (CrmException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("manualAllotImportCustomer failed: ", e);
            return error();
        }
    }

    /**
     * 自动分配导入的客户
     * 
     * @return 操作结果
     * 
     *         <pre>
     * 修改日期		修改人	修改原因
     * 2014-4-27	lhg		新建
     * </pre>
     */
    public String autoAllotImportCustomer() {
        // 参数判断
        if (StringUtils.isEmpty(multipleUserAccountExtJson)) {
            return error("非法参数：随机分配的JSON为空");
        }
        try {
            MultipleUserAccountExtDTO multipleUserAccountExtDTO = JSON.parseObject(multipleUserAccountExtJson,
                    MultipleUserAccountExtDTO.class);
            importCustomerBiz.txAutoAllotImportCustomer(multipleUserAccountExtDTO.getUserAccountExts());
            return success();
        } catch (CrmException e) {
            return error(e.getMessage());
        } catch (Exception e) {
            logger.error("autoAllotImportCustomer failed: ", e);
            return error();
        }
    }
    
    
    /**
     * set the id to set
     * 
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * set the importCustomerBiz to set
     * 
     * @param importCustomerBiz the importCustomerBiz to set
     */
    public void setImportCustomerBiz(IImportCustomerBiz importCustomerBiz) {
        this.importCustomerBiz = importCustomerBiz;
    }

    /**
     * set the serviceAccountId to set
     * 
     * @param serviceAccountId the serviceAccountId to set
     */
    public void setServiceAccountId(String serviceAccountId) {
        this.serviceAccountId = serviceAccountId;
    }

    /**
     * set the importCustIdsJson to set
     * 
     * @param importCustIdsJson the importCustIdsJson to set
     */
    public void setImportCustIdsJson(String importCustIdsJson) {
        this.importCustIdsJson = importCustIdsJson;
    }

    /**
     * set the multipleUserAccountExtJson to set
     * 
     * @param multipleUserAccountExtJson the multipleUserAccountExtJson to set
     */
    public void setMultipleUserAccountExtJson(String multipleUserAccountExtJson) {
        this.multipleUserAccountExtJson = multipleUserAccountExtJson;
    }
    
	public String getBatchNumber() {
        return batchNumber;
    }
	
    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public void setMaxUploadSize(long maxUploadSize) {
		this.maxUploadSize = maxUploadSize;
	}

	public void setImportFile(File importFile) {
		this.importFile = importFile;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setSortCol(String sortCol) {
		this.sortCol = sortCol;
	}

	public void setAscend(boolean ascend) {
		this.ascend = ascend;
	}

	public ListPage<ImportCustomer> getListPage() {
		return listPage;
	}

	public Date getAllocateTimeBegin() {
		return allocateTimeBegin;
	}

	public void setAllocateTimeBegin(Date allocateTimeBegin) {
		this.allocateTimeBegin = allocateTimeBegin;
	}

	public Date getAllocateTimeEnd() {
		return allocateTimeEnd;
	}

	public void setAllocateTimeEnd(Date allocateTimeEnd) {
		this.allocateTimeEnd = allocateTimeEnd;
	}

	/**
	 * @return the allocateFlag
	 */
	public Boolean getAllocateFlag() {
		return allocateFlag;
	}

	/**
	 * @param allocateFlag the allocateFlag to set
	 */
	public void setAllocateFlag(Boolean allocateFlag) {
		this.allocateFlag = allocateFlag;
	}

    /**
     * get the customerManage
     * @return the customerManage
     */
    public String getCustomerManage() {
        return customerManage;
    }

}
