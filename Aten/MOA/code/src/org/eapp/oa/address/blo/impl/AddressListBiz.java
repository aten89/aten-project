package org.eapp.oa.address.blo.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.eapp.client.hessian.UserAccountService;
import org.eapp.exception.RpcAuthorizationException;
import org.eapp.oa.address.blo.IAddressListBiz;
import org.eapp.oa.address.dao.IAddressListDAO;
import org.eapp.oa.address.hbean.AddressList;
import org.eapp.oa.system.exception.OaException;
import org.eapp.rpc.dto.UserAccountInfo;
import org.eapp.util.web.DataFormatUtil;
import org.eapp.util.web.upload.FileDispatcher;
import org.apache.commons.lang3.StringUtils;
/**
 * 地址list 业务层
 * 
 * 
 * <pre>
 * 修改日期      修改人      修改原因
 * 2012-7-24      方文伟      修改注释
 * </pre>
 */
public class AddressListBiz implements IAddressListBiz {

    /**
     * 日志
     */
    private static final Log log = LogFactory.getLog(AddressListBiz.class);

    /**
     * 地址list DAO层
     */
    private IAddressListDAO addressListDAO;
    public void setAddressListDAO(IAddressListDAO addressListDAO) {
        this.addressListDAO = addressListDAO;
    }

    @Override
    public AddressList getById(String id) {
        return addressListDAO.findById(id);
    }

    @Override
    public AddressList getByAccountId(String accountId) {
        AddressList addr = addressListDAO.findByAccountId(accountId);
        return addr;
    }

    @Override
    public AddressList addOrModifyAddressList(String id, String userAccountId, String employeeNumber,
            String seatNumber, Date userEnterCorpDate, String userMobile, String userOfficeTel, String userEmail,
            String userQQ, String userMSN, String userNickName, String userSex, Date userBirthDate,
            String userNativePlace, String userNation, String userCommAddr, String zipCode, String userHomeAddr,
            String userHomeTel, String remark, String userPhoto) {

        AddressList addrList;
        if (id == null) {
            addrList = new AddressList();
        } else {
            addrList = addressListDAO.findById(id);
            if (userPhoto != null) {
                // 删除掉旧的
                if (addrList.getUserPhoto() != null) {
                	// 删除文件
    	        	File f = FileDispatcher.findFile(FileDispatcher.getAbsPath(addrList.getUserPhoto()));
    				if (f != null) {
    					f.delete();
    				}
    				
//                    FileUtil.delFiles(addrList.getUserPhoto());
                }
            }
        }

        addrList.setUserAccountId(userAccountId);
        addrList.setEmployeeNumber(employeeNumber);
        addrList.setSeatNumber(seatNumber);
        addrList.setUserEnterCorpDate(userEnterCorpDate);
        addrList.setUserMobile(userMobile);
        addrList.setUserOfficeTel(userOfficeTel);
        addrList.setUserEmail(userEmail);
        addrList.setUserQQ(userQQ);
        addrList.setUserMSN(userMSN);
        addrList.setUserNickName(userNickName);
        addrList.setUserSex(userSex);
        addrList.setUserBirthDate(userBirthDate);
        addrList.setUserNativePlace(userNativePlace);
        addrList.setUserNation(userNation);
        addrList.setUserCommAddr(userCommAddr);
        addrList.setZipCode(zipCode);
        addrList.setUserHomeAddr(userHomeAddr);
        addrList.setUserHomeTel(userHomeTel);
        addrList.setRemark(remark);
        if (userPhoto != null) {
        	addrList.setUserPhoto(userPhoto);
        }
        addressListDAO.saveOrUpdate(addrList);
        return addrList;
    }

    @Override
    public List<AddressList> getAddressList(List<String> accountIDs) {
        return addressListDAO.queryAddressList(accountIDs);
    }

//    @Override
//    public AddressList getAddressList(String id, String userAccountId) {
//        AddressList addr = null;
//        if (id != null) {
//            addr = addressListDAO.findById(id);
//            if (addr == null) {
//                throw new IllegalArgumentException();
//            }
//        } else {
//            if (userAccountId == null) {
//                throw new IllegalArgumentException();
//            }
//            addr = addressListDAO.findByAccountId(userAccountId);
//            if (addr == null) {
//                addr = new AddressList();
//                addr.setUserAccountId(userAccountId);
//            }
//        }
//        return addr;
//    }

    

    @Override
    public void csEexportAsCSV(List<String> uids, String path) throws OaException, IOException {
    	List<AddressList> addressList = getAddressListByAccountIds(uids, 1, 0);
        if (addressList != null) {
            StringBuffer addrlistContent = new StringBuffer();
            for (AddressList addrList : addressList) {
                // String userAccountId = DataFormatUtil.noNullValue(addrList.getUserAccountId());
                String userName = DataFormatUtil.noNullValue(addrList.getUserName());// 姓名
                // String userMobile = DataFormatUtil.noNullValue(addrList.getUserMobile());// 手机
                // String officeTel = DataFormatUtil.noNullValue(addrList.getUserOfficeTel());// 公司电话
                String email = DataFormatUtil.noNullValue(addrList.getUserEmail());// email
                // String seatNumber = DataFormatUtil.noNullValue(addrList.getSeatNumber());

                // String employeeNumber = DataFormatUtil.noNullValue(addrList.getEmployeeNumber());
                // String userEnterCorpDate = DataFormatUtil.noNullValue(addrList.getUserEnterCorpDate());// 入司时间
                // String userQQ = DataFormatUtil.noNullValue(addrList.getUserQQ());// QQ
                // String userMSN = DataFormatUtil.noNullValue(addrList.getUserMSN());
                String userNickName = DataFormatUtil.noNullValue(addrList.getUserNickName());// 昵称
                // String userSex = DataFormatUtil.noNullValue(addrList.getUserSex());// 性别
                // String userBirthDate = DataFormatUtil.noNullValue(addrList.getUserBirthDate());// 生日
                // String userNativePlace = DataFormatUtil.noNullValue(addrList.getUserNativePlace());
                // String userNation = DataFormatUtil.noNullValue(addrList.getUserNation());
                // String userCommAddr = DataFormatUtil.noNullValue(addrList.getUserCommAddr());
                // String zipCode = DataFormatUtil.noNullValue(addrList.getZipCode());
                // String userHomeAddr = DataFormatUtil.noNullValue(addrList.getUserHomeAddr());
                // String userHomeTel = DataFormatUtil.noNullValue(addrList.getUserHomeTel());// 家庭电话
                // String remark = DataFormatUtil.noNullValue(addrList.getRemark());
                String wordBreak = "\r\n";
                // String desc = remark + wordBreak +
                //
                // ("".equals(seatNumber) ? "" : "座位号:" + seatNumber + wordBreak)
                // + ("".equals(userEnterCorpDate) ? "" : "入司时间:" + userEnterCorpDate + wordBreak)
                // + ("".equals(userMSN) ? "" : "MSN:" + userMSN + wordBreak) +
                //
                // ("".equals(userCommAddr) ? "" : "通讯地址:" + userCommAddr + wordBreak)
                // + ("".equals(zipCode) ? "" : "邮编:" + zipCode + wordBreak)
                // + ("".equals(userNativePlace) ? "" : "籍贯:" + userNativePlace + wordBreak)
                // + ("".equals(userNation) ? "" : "民族:" + userNation);

                addrlistContent.append(changeComma(userName.substring(1)));// 名
                addrlistContent.append(",");
                addrlistContent.append(changeComma(userName.substring(0, 1)));// 姓
                addrlistContent.append(",");
                addrlistContent.append(changeComma(userName));// 姓名
                addrlistContent.append(",");

                addrlistContent.append(changeComma(userNickName));// 昵称
                addrlistContent.append(",");
                addrlistContent.append(changeComma(email));// email
                /*
                 * addrlistContent.append(","); addrlistContent.append(changeComma(userMobile));//手机
                 * addrlistContent.append(","); addrlistContent.append(changeComma(userQQ));//qq
                 * addrlistContent.append(","); if("M".equals(userSex)){ userSex = "男"; } else if("F".equals(userSex)){
                 * userSex = "女"; } addrlistContent.append(changeComma(userSex));//性别 addrlistContent.append(",");
                 * addrlistContent.append(changeComma(userBirthDate));//生日 addrlistContent.append(",");
                 * addrlistContent.append(changeComma(userHomeTel));//家庭电话 addrlistContent.append(",");
                 * addrlistContent.append(changeComma(userHomeAddr));//家庭电话 addrlistContent.append(",");
                 * addrlistContent.append(changeComma(addrList.getUserPostName()));//职位 addrlistContent.append(",");
                 * addrlistContent.append(changeComma(addrList.getUserDeptName()));//部门 addrlistContent.append(",");
                 * addrlistContent.append(changeComma(officeTel));//办公电话 addrlistContent.append(",");
                 * addrlistContent.append("有限公司");//公司 addrlistContent.append(",");
                 * addrlistContent.append("福州市");//公司所在街道 addrlistContent.append(",");
                 * addrlistContent.append("350011");//公司所在地邮政编码 addrlistContent.append(",");
                 * addrlistContent.append("0591-87834090");//公司传真 addrlistContent.append(",");
                 * addrlistContent.append("\"" + changeQuoto(changeComma(desc)) + "\"");//附注
                 */
                addrlistContent.append(wordBreak);

            }
            StringBuffer addrlistHeader = getAddressListHeader();
            String content = addrlistHeader.toString() + addrlistContent.toString();
            BufferedWriter output = null;
            try {
            	File file = new File(path);
            	File parentFile = file.getParentFile();
                if (!parentFile.exists() && !parentFile.mkdirs()) {
                	//创建文件夹
            		throw new IOException("文件夹创建失败");
            	}
                file.createNewFile();
            	output = new BufferedWriter(new FileWriter(file));
            	 output.write(content);
            } finally {
            	if (output != null) {
            		output.close();
            	}
            }
        }
    }

    /**
     * 改变字符串中的逗号的中英文形式
     * @param str 字符串
     * @return 改变后的字符串
     *
     * <pre>
     * 修改日期      修改人      修改原因
     * 2012-7-24      方文伟      修改注释
     * </pre>
     */
    private String changeComma(String str) {
        if (str == null || str.equals("")) {
            return "";
        }

        return str.replaceAll(",", "，");
    }

    // private String changeQuoto(String str) {
    // if (str == null || str.equals("")) {
    // return str;
    // }
    //
    // return str.replaceAll("\"", "“");
    // }

    /**
     * 获取通讯录头
     * 
     * @return 通讯录头的字符串
     * 
     *         <pre>
     * 修改日期      修改人      修改原因
     * 2012-7-24      方文伟      修改注释
     * </pre>
     */
    public StringBuffer getAddressListHeader() {
        String[] fieldArray = { "名", "姓", "姓名", "昵称", "电子邮件地址" };
        StringBuffer sbHeader = new StringBuffer();
        for (int i = 0; i < fieldArray.length; i++) {
            if (!sbHeader.toString().equals("")) {
                sbHeader.append(",");
            }
            sbHeader.append(fieldArray[i]);
        }
        String wordBreak = "\r\n";
        sbHeader.append(wordBreak);
        return sbHeader;
    }

    @Override
    public List<String> getUserAccountIdsByDeptAndAccount(String userDeptName, String userAccountQueryString)
            throws OaException {
        try {
        	UserAccountService uas = new UserAccountService();
        	if (StringUtils.isBlank(userDeptName)) {
        		userDeptName = null;
        	}
        	if (StringUtils.isBlank(userAccountQueryString)) {
        		userAccountQueryString = null;
        	}
        	List<UserAccountInfo> users =  uas.queryUserAccounts(userDeptName, userAccountQueryString);
        	if (users != null && !users.isEmpty()) {
        		List<String> uids = new ArrayList<String>(users.size());
        		for (UserAccountInfo u : users) {
        			//有效
        			if (!u.getIsLock() && (u.getInvalidDate() == null || u.getInvalidDate().getTime() > System.currentTimeMillis())) {
        				uids.add(u.getAccountID());
        			}
        		}
        		return uids;
        	}
        } catch (MalformedURLException e) {
            log.error("getUserAccountByGroup faild", e);
        } catch (RpcAuthorizationException e) {
            log.error("getUserAccountByGroup faild", e);
        }
        return null;
    }

    public List<AddressList> getAddressListByAccountIds(List<String> uids, int pageNo, int pageSize) throws OaException {
        if (uids == null) {
            throw new OaException("无查询数据");
        }
        int totalCount = uids.size();// 总记录数

        int firstResultIndex = (pageNo - 1) * pageSize;

        if (firstResultIndex > totalCount || totalCount == 0) {// 开始记录大于总记录数
            throw new OaException("无查询数据");
        }
        
        // 排序
        if (uids != null) {
            Collections.sort(uids);
        }
        
        // 过滤翻页
        List<String> pageUids = new ArrayList<String>(pageSize);
        for (int i = firstResultIndex; pageSize != 0 && (i < firstResultIndex + pageSize && i < uids.size())
                || pageSize == 0 && i < uids.size(); i++) {
            pageUids.add(uids.get(i));
        }

        List<AddressList> addressList_ = addressListDAO.queryAddressList(pageUids);
        Map<String, AddressList> addressMap_ = new HashMap<String, AddressList>();
        for (AddressList address : addressList_) {
        	addressMap_.put(address.getUserAccountId(), address);
        }
        
        List<AddressList> retList = new ArrayList<AddressList>();
        //按原来顺序添加
        for (String uid : pageUids) {
        	AddressList al = addressMap_.get(uid);
        	if (al == null) {
        		al = new AddressList();
        		al.setUserAccountId(uid);
        	}
            retList.add(al);
        }

        return retList;
    }

    @Override
    public void deleteAddress(String userId) {
        AddressList addr = addressListDAO.findByAccountId(userId);
        if (addr != null) {
            addressListDAO.delete(addr);
        }
    }
}
