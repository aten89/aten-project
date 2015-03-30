package org.eapp.oa.info.blo;

import java.util.List;

import org.eapp.oa.info.hbean.InfoLayout;
import org.eapp.oa.system.exception.OaException;

/**
 * 信息参数业务逻辑层接口
 * 
 * 
 * <pre>
 * 修改日期		修改人	修改原因
 * 2013-3-25	李海根	注释修改
 * </pre>
 */
public interface IInfoLayoutBiz {

    /**
     * 新增信息参数
     * 
     * @param infoName 分类名称
     * @param flowClass 流程类别
     * @param desc 说明
     * @return InfoLayout
     * @throws OaException 异常
     * 
     */
    public InfoLayout addInfoLayout(String infoName, String flowClass, String desc)
            throws OaException;

    /**
     * 删除信息参数
     * 
     * @param id 主键ID
     * @return 信息参数配置
     * @throws OaException 异常
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2013-3-4	           李海根	注释修改
     * </pre>
     */
    public InfoLayout deleteInfoLayout(String id) throws OaException;

    /**
     * 修改信息参数
     * 
     * @param id 主键
     * @param infoName 分类名称
     * @param flowClass 流程类别
     * @param desc 说明
     * @return InfoLayout
     * @throws OaException 异常
     */
    public InfoLayout modifyInfoLayour(String id, String infoName, String flowClass, String desc)
            throws OaException;

    /**
     * 取得用户有权限的信息版块
     * 
     * @param userAccountId 用户账号
     * @param groupNames 部门
     * @param postNames 职位
     * @param flag 类别
     * @return 信息版块配置
     * 
     *         <pre>
     * 修改日期		修改人	修改原因
     * 2013-3-4	            李海根	注释修改
     * </pre>
     */
    public List<InfoLayout> getAssignLayout(String userAccountId, List<String> groupNames, List<String> postNames,
            int flag);

    /**
     * 通过名称查找有权限的信息版块
     * 
     * @param userAccountId 用户账号
     * @param groupNames 部门
     * @param postNames 职位
     * @param name 分类名称
     * @param flag 类别
     * @return 信息版块配置
     * 
     *         <pre>
     * 修改日期		修改人	修改原因
     * 2013-3-4	        李海根	注释修改
     * </pre>
     */
    public InfoLayout getAssignLayoutByName(String userAccountId, List<String> groupNames, List<String> postNames,
            String name, int flag);

    /**
     * 获得所有的信息配置
     * 
     * @return 信息配置
     * 
     *         <pre>
     * 修改日期		修改人	修改原因
     * 2013-3-4	            李海根	注释修改
     * </pre>
     */
    public List<InfoLayout> getAllInfoLayout();

    /**
     * 保存排序
     * 
     * @param infoLayoutIds 信息流程配置IDS
     * 
     *            <pre>
     * 修改日期		修改人	修改原因
     * 2013-3-4	            李海根	注释修改
     * </pre>
     */
    public void txSaveOrder(String[] infoLayoutIds);

    /**
     * 通过名称查找
     * 
     * @param name 分类名称
     * @return 信息流程配置
     * 
     *         <pre>
     * 修改日期		修改人	修改原因
     * 2013-3-4	        李海根	注释修改
     * </pre>
     */
    public InfoLayout getLayoutByName(String name);

//    /**
//     * 通过ID查找收件人账号
//     * 
//     * @param id 主键
//     * @return 收件人账号
//     * @throws OaException 异常
//     * 
//     *             <pre>
//     * 修改日期		修改人	修改原因
//     * 2013-3-4	            李海根	新建
//     * </pre>
//     */
//    public List<String> getLayoutEmailUsers(String id) throws OaException;
//
//    /**
//     * 保存绑定的收件人
//     * 
//     * @param id 主键
//     * @param emailUsers 收件人账号 格式：lihaigen,super@admins,
//     * @throws OaException 异常
//     * 
//     *             <pre>
//     * 修改日期		修改人	修改原因
//     * 2013-3-4	          李海根	新建
//     * </pre>
//     */
//    public void txSaveBindEmailUser(String id, String emailUsers) throws OaException;

}
