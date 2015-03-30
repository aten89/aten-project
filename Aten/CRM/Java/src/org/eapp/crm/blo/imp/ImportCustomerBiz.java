/**
 * 
 */
package org.eapp.crm.blo.imp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.eapp.client.util.UsernameCache;
import org.eapp.crm.blo.IImportCustomerBiz;
import org.eapp.crm.dao.IImportCustomerDAO;
import org.eapp.crm.dao.param.ImportCustomerQueryParameters;
import org.eapp.crm.excelimp.ExcelCellToValue;
import org.eapp.crm.exception.CrmException;
import org.eapp.crm.hbean.CustomerInfo;
import org.eapp.crm.hbean.ImportCustomer;
import org.eapp.crm.hbean.UserAccountExt;
import org.eapp.util.hibernate.ListPage;
import org.springframework.util.CollectionUtils;

/**
 * 导入客户业务逻辑层实现
 * 
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-4-26	lhg		新建
 * </pre>
 */
public class ImportCustomerBiz implements IImportCustomerBiz {

    /**
     * 导入客户DAO接口
     */
    private IImportCustomerDAO importCustomerDAO;

    /*
     * (non-Javadoc)
     * 
     * @see org.eapp.crm.blo.IImportCustomerBiz#txManualAllotImportCustomer(java.lang.String, java.util.List)
     */
    @Override
    public void txManualAllotImportCustomer(String userAccountId, List<String> importCustIds) throws CrmException {
        // 参数判断
        if (StringUtils.isEmpty(userAccountId)) {
            throw new IllegalArgumentException("非法参数:销售人员账号为空");
        }
        if (importCustIds == null) {
            throw new IllegalArgumentException("非法参数:分配的客户不能为空");
        }
        // 根据ID查找为分配的导入客户
        List<ImportCustomer> importCustomers = importCustomerDAO.findByIds(importCustIds, false);
        if (importCustomers != null) {
            for (ImportCustomer importCustomer : importCustomers) {
                // 拷贝导入客户到客户信息表
                CustomerInfo customerInfo = txCopyImportCustToCustInfo(userAccountId, importCustomer);
                // 设置关联客户ID
                importCustomer.setRelateCustid(customerInfo.getId());
                // 设置分配对象
                importCustomer.setAllocateTo(userAccountId);
                // 设置为已推送
                importCustomer.setAllocateFlag(true);
                // 设置分配时间
                importCustomer.setAllocateTime(new Date());
                importCustomerDAO.saveOrUpdate(importCustomer);
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eapp.crm.blo.IImportCustomerBiz#txAutoAllotImportCustomer(java.util.List)
     */
    @Override
    public void txAutoAllotImportCustomer(List<UserAccountExt> userAccountExts) throws CrmException {
        // 参数判断
        if (userAccountExts == null) {
            throw new CrmException();
        }
        for (UserAccountExt userAccountExt : userAccountExts) {
            // 销售人员账号
            String userAccountId = userAccountExt.getAccountId();
            // 分配导入客户数量
            int allotCustomerNum = userAccountExt.getAllotCustomerNum();
            // 找出所有的导入客户
            List<ImportCustomer> importCustomers = importCustomerDAO.findByIds(null, false);
            // 判断分配的数量是否大于所有未分配的数据
            if (importCustomers == null) {
                throw new CrmException("没有未分配的客户了");
            }
            if (allotCustomerNum > importCustomers.size()) {
                throw new CrmException("分配的数量大于存在的未分配的数量");
            }
            // 随机从数据库里查找 allotCustomerNum 数量的导入客户分配给该销售人员
            for (int i = 0; i < importCustomers.size(); i++) {
                if (i >= allotCustomerNum) {
                    break;
                }
                ImportCustomer importCustomer = importCustomers.get(i);
                // 拷贝导入客户到客户信息表
                CustomerInfo customerInfo = txCopyImportCustToCustInfo(userAccountId, importCustomer);
                // 设置关联客户ID
                importCustomer.setRelateCustid(customerInfo.getId());
                // 设置分配对象
                importCustomer.setAllocateTo(userAccountId);
                // 设置为已推送
                importCustomer.setAllocateFlag(true);
                // 设置分配时间
                importCustomer.setAllocateTime(new Date());
                importCustomerDAO.saveOrUpdate(importCustomer);
            }
        }

    }

    /**
     * 拷贝导入客户到客户信息
     * 
     * @param userAccountId 销售人员
     * @param importCustomer 导入客户
     * @return 客户信息
     * @throws CrmException 异常
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-4-27	lhg		新建
     * </pre>
     */
    private CustomerInfo txCopyImportCustToCustInfo(String userAccountId, ImportCustomer importCustomer)
            throws CrmException {
        // 参数判断
        if (importCustomer == null) {
            throw new CrmException("非法参数:导入客户对象为空");
        }
        CustomerInfo customerInfo = new CustomerInfo();
        // 拷贝客户名称
        if (StringUtils.isNotEmpty(importCustomer.getCustName())) {
            customerInfo.setCustName(importCustomer.getCustName());
        }
        // 拷贝客户性别
        if (importCustomer.getSex() != null && !"".equals(importCustomer.getSex())) {
            customerInfo.setSex(importCustomer.getSex());
        }
        // 拷贝客户电话
        if (StringUtils.isNotEmpty(importCustomer.getTel())) {
            customerInfo.setTel(importCustomer.getTel());
        }
        // 拷贝客户邮箱
        if (StringUtils.isNotEmpty(importCustomer.getEmail())) {
            customerInfo.setEmail(importCustomer.getEmail());
        }
        // 设置销售人员
        customerInfo.setSaleMan(userAccountId);
        // 数据来源
        customerInfo.setDataSource(CustomerInfo.DATA_SOURCE_COMPANY_ALLOT);
        // 设置为未提交
        customerInfo.setStatus(CustomerInfo.UNCOMMIT_STATUS);
        // 创建时间
        customerInfo.setCreateTime(new Date());
        importCustomerDAO.save(customerInfo);
        return customerInfo;
    }

    @Override
    public String txImportCustomer(File excel, String fileName, File dir, String importUser) throws IOException, CrmException {
        FileInputStream in = new FileInputStream(excel);
        HSSFWorkbook wb = null;
        try {
            wb = new HSSFWorkbook(in);
        } catch (Exception e) {
            throw new IOException("系统目前只支持导入Excel2003文件", e.getCause());
        }
        
        //取当前时间作为一个批次号
        Date now = new Date(); 
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        String batchNumber = df.format(now);

        HSSFSheet childSheet = wb.getSheetAt(0);
        if (childSheet != null) {
            int rowNum = childSheet.getLastRowNum();
            ImportCustomer customer = null;
            for (int i = 1; i <= rowNum; i++) {

                HSSFRow row = childSheet.getRow(i);
                if (row == null) {
                    continue;
                }
                String name = ExcelCellToValue.cellValueToString(row.getCell(0));
                String sex = ExcelCellToValue.cellValueToString(row.getCell(1));
                String tel = ExcelCellToValue.cellValueToString(row.getCell(2));
                String email = ExcelCellToValue.cellValueToString(row.getCell(3));

                customer = new ImportCustomer();
                customer.setCustName(name);
                if(sex != null && sex != ""){
                   if(sex.equals("男") || sex.equals("M")){
                       customer.setSex('M');
                   } else if (sex.equals("女") || sex.equals("F")){
                       customer.setSex('F');
                   } 
                }
                customer.setTel(tel);
                customer.setEmail(email);
                customer.setBatchNumber(batchNumber);
                customer.setAllocateFlag(false);
                //导入人
                customer.setImportUser(importUser);
                //导入时间
                customer.setImportTime(now);
                
                importCustomerDAO.saveOrUpdate(customer);
            }
        }
        return batchNumber;
    }

    @Override
    public ListPage<ImportCustomer> queryCustomerList(ImportCustomerQueryParameters qp) throws CrmException {
    	ListPage<ImportCustomer> result = importCustomerDAO.queryImportCustomers(qp);
    	if(!CollectionUtils.isEmpty(result.getDataList())) {
    		//设置日期格式
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		for(ImportCustomer importCustomer : result.getDataList()) {
    			if(importCustomer.getAllocateTime() != null){
    				importCustomer.setAllocateTimeStr(df.format(importCustomer.getAllocateTime()));
                }
    			
    			//翻译分配用户
    			if(importCustomer.getAllocateTo() != null) {
    				importCustomer.setAllocateToName(UsernameCache.getDisplayName(importCustomer.getAllocateTo()));
    			}
    		}
    	}
        return result;
    }

    @Override
    public void txDeleteImportCustomer(String id) throws CrmException {
        if (StringUtils.isEmpty(id)) {
            throw new IllegalArgumentException("非法参数：导入客户ID为空");
        }
        ImportCustomer importCustomer = getSimpleImportCustomerById(id);       
        importCustomerDAO.delete(importCustomer);
    }
    
    @Override
    public ImportCustomer getSimpleImportCustomerById(String id) throws CrmException {
        if (StringUtils.isEmpty(id)) {
            throw new IllegalArgumentException("id 不能为空");
        }
        ImportCustomer importCustomer = importCustomerDAO.findById(id);
        if (importCustomer == null) {
            throw new CrmException("导入客户不存在！");
        }
        return importCustomer;
    }
    
    /**
     * set the importCustomerDAO to set
     * 
     * @param importCustomerDAO the importCustomerDAO to set
     */
    public void setImportCustomerDAO(IImportCustomerDAO importCustomerDAO) {
        this.importCustomerDAO = importCustomerDAO;
    }

}
