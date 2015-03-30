/*
 * @(#) IAddressListBiz.java 1.0 09/11/05
 */

package org.eapp.oa.address.blo;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.eapp.oa.address.hbean.AddressList;
import org.eapp.oa.system.exception.OaException;

/**
 * 通讯录模块中的我的资料子模块的业务逻辑处理接口
 * 
 *
 */
public interface IAddressListBiz{
	/**
	 * 根据用户帐号ID获得与之对应的通讯录信息
	 * 
	 * @param accountId 用户帐号ID
	 * @return 匹配的用户通讯录
	 */
	public AddressList getById(String id);
	
	/**
	 * 根据用户帐号ID获得与之对应的通讯录
	 * 
	 * @param accountId 用户帐号ID
	 * @return 匹配的用户通讯录
	 */
	public AddressList getByAccountId( String accountId );
	/**
	 * 保存或者更新通讯录数据
	 * 
	 * @param id id=null或id=""时,新建通讯录;当id!=null时保存通讯录
	 * @param userAccountId
	 * @param employeeNumber
	 * @param seatNumber
	 * @param userEnterCorpDate
	 * @param userMobile
	 * @param userOfficeTel
	 * @param userEmail
	 * @param userQQ
	 * @param userMSN
	 * @param userNickName
	 * @param userSex
	 * @param userBirthDate
	 * @param userNativePlace
	 * @param userNation
	 * @param userCommAddr
	 * @param zipCode
	 * @param userHomeAddr
	 * @param userHomeTel
	 * @param remark
	 * @param userPhoto
	 */
	public AddressList addOrModifyAddressList(String id, String userAccountId, String employeeNumber,
			String seatNumber, Date userEnterCorpDate, String userMobile,
			String userOfficeTel, String userEmail, String userQQ,
			String userMSN, String userNickName, String userSex,
			Date userBirthDate, String userNativePlace, String userNation,
			String userCommAddr, String zipCode, String userHomeAddr, String userHomeTel,
			String remark, String userPhoto);
	
	
	/**
	 * 获取匹配帐户id的通讯录数据
	 * 
	 * @return 
	 */
	public List<AddressList> getAddressList(List<String> accountIDs);

	/**
	 * 根据机构id获取匹配的用户帐户信息
	 * 
	 * @param userDeptId
	 * @return
	 */
//	public List<UserAccountDTO> getUserAccountByGroup(String userDeptId);
	
	/**
	 * 根据主键id,帐户id返回对应的通讯录详情
	 * 
	 * @param id 主键id
	 * @param userAccountId 帐户id
	 * @see #getById(String)
	 * @return
	 * @throws OaException 
	 */
//	public AddressList getAddressList(String id, String userAccountId);
	
	/**
	 * 读取通讯录Excel并导入数据库
	 * @param fis
	 * @throws Exception 
	 */
//	public void txReadAddrListExcel(FileInputStream fis) throws Exception;
	
	/**
	 * 导出通讯录数据到CSV文件中
	 * 
	 * @param uids 通讯录数据
	 * @param file 文件
	 * @throws OaException
	 * @throws IOException
	 */
	public void csEexportAsCSV(List<String> uids, String file) throws OaException, IOException;
	
	/**
	 * 根据机构id、帐户搜索条件(姓名或者帐户id)搜索出所有符合条件的用户帐户id列表
	 * @param userDeptId
	 * @param userAccountQueryString
	 * @return
	 * @throws OaException
	 */
	public List<String> getUserAccountIdsByDeptAndAccount(String userDeptId, String userAccountQueryString) throws OaException;
	
	/**
	 * 根据帐户id列表匹配对应的通讯录数据
	 * 
	 * @param uids
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws OaException
	 */
	public List<AddressList> getAddressListByAccountIds(List<String> uids, int pageNo, int pageSize) throws OaException;

	public void deleteAddress(String userId);
}