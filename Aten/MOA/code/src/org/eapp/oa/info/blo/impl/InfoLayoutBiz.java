package org.eapp.oa.info.blo.impl;

import java.util.List;

import org.eapp.oa.info.blo.IInfoLayoutBiz;
import org.eapp.oa.info.dao.IInfoLayoutDAO;
import org.eapp.oa.info.hbean.InfoLayout;
import org.eapp.oa.system.exception.OaException;

/**
 * 信息流程配置业务逻辑访问实现类
 * 
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2013-3-4	            李海根	注释修改
 * </pre>
 */
public class InfoLayoutBiz implements IInfoLayoutBiz {
    
    /**
     * 信息流程配置数据访问层接口
     */
    private IInfoLayoutDAO infoLayoutDAO;

    /**
     * 获取infoLayoutDAO
     * 
     * @return the infoLayoutDAO
     */
    public IInfoLayoutDAO getInfoLayoutDAO() {
        return infoLayoutDAO;
    }

    /**
     * 设置infoLayoutDAO为infoLayoutDAO
     * 
     * @param infoLayoutDAO the infoLayoutDAO to set
     */
    public void setInfoLayoutDAO(IInfoLayoutDAO infoLayoutDAO) {
        this.infoLayoutDAO = infoLayoutDAO;
    }

    @Override
    public InfoLayout addInfoLayout(String infoName, String flowClass, String desc)
            throws OaException {
        if (infoName == null) {
            throw new IllegalArgumentException("非法参数:分类名称不能为空！");
        }
        // 判断分类名称是否重复
        isInfoNameRepeat(infoName);

        InfoLayout infoLayout = new InfoLayout();
        // 设置名称
        infoLayout.setName(infoName);
        // 设置公告类别
        infoLayout.setFlowClass(flowClass);
//        // 设置是否发邮件
//        infoLayout.setIsEmail(isEmail);
        // 设置描述
        infoLayout.setDescription(desc);
        // 取得最新的排序号
        int displayOrder = infoLayoutDAO.getDisplayOrder() + 1;
        // 设置排序号
        infoLayout.setDisplayOrder(displayOrder);
        infoLayoutDAO.save(infoLayout);
        return infoLayout;
    }

    /*
     * (non-Javadoc)
     */
    @Override
    public InfoLayout deleteInfoLayout(String id) throws OaException {
        InfoLayout il = infoLayoutDAO.findById(id);
        if (il != null) {
            //删除
            infoLayoutDAO.delete(il);
        }
        return il;
    }

    /*
     * (non-Javadoc)
     */
    @Override
    public InfoLayout modifyInfoLayour(String id, String infoName, String flowClass, String desc)
            throws OaException {
        if (id == null) {
            throw new IllegalArgumentException("非法参数:ID不能为空！");
        }
        if (infoName == null) {
            throw new IllegalArgumentException("非法参数:infoName不能为空！");
        }
        //根据ID查找信息参数设置
        InfoLayout il = infoLayoutDAO.findById(id);
        if (il == null) {
            throw new OaException("信息配置不存在");
        }
        // 判断分类名称是否重复
        if (!il.getName().equals(infoName)) {
            isInfoNameRepeat(infoName);
        }
        // 设置名称
        il.setName(infoName);
        // 设置流程类别
        il.setFlowClass(flowClass);
//        // 设置是否发邮件
//        il.setIsEmail(isEmail);
        // 设置描述
        il.setDescription(desc);
        infoLayoutDAO.saveOrUpdate(il);
        return il;
    }

    /**
     *  验证分类名称是否重复
     * @param infoName 分类名称
     * @throws OaException 异常
     *
     * <pre>
     * 修改日期		修改人	修改原因
     * 2013-3-4	            李海根	注释修改
     * </pre>
     */
    private void isInfoNameRepeat(String infoName) throws OaException {
        List<InfoLayout> infoLayout = infoLayoutDAO.findByName(infoName);
        // 如果为空说明不存在
        if (infoLayout != null && !infoLayout.isEmpty()) {
            throw new OaException("分类名称不能重复!");
        }
    }

    @Override
    public List<InfoLayout> getAssignLayout(String userAccountId, List<String> groupNames, List<String> postNames,
            int flag) {
        return infoLayoutDAO.findAssignLayout(userAccountId, groupNames, postNames, flag, null);
    }

    @Override
    public InfoLayout getAssignLayoutByName(String userAccountId, List<String> groupNames, List<String> postNames,
            String name, int flag) {
        List<InfoLayout> ils = infoLayoutDAO.findAssignLayout(userAccountId, groupNames, postNames, flag, name);
        if (ils != null && !ils.isEmpty()) {
            return ils.get(0);
        }
        return null;
    }

    @Override
    public List<InfoLayout> getAllInfoLayout() {
        return infoLayoutDAO.findByDisplayOrder();
    }

    @Override
    public void txSaveOrder(String[] infoLayoutIds) {
        int order = 1;
        for (int i = 0; i < infoLayoutIds.length; i++) {
            if (infoLayoutIds[i] == null || infoLayoutIds[i].trim().equals("")) {
                continue;
            }
            InfoLayout infoLayout = infoLayoutDAO.findById(infoLayoutIds[i]);
            if (infoLayout == null) {
                continue;
            }
            // 设置排序号
            infoLayout.setDisplayOrder(order);
            //保存
            infoLayoutDAO.saveOrUpdate(infoLayout);
            order++;
        }
    }

    @Override
    public InfoLayout getLayoutByName(String name) {
        if (name == null) {
            return null;
        }
        // 根据名称取得信息公告参数设置
        List<InfoLayout> list = infoLayoutDAO.findByName(name);
        // 如果为空，则直接返回
        if (list == null || list.isEmpty()) {
            return null;
        }
        // 如果不为空，则取第一个
        return list.get(0);
    }

//    @SuppressWarnings("unchecked")
//    @Override
//    public List<String> getLayoutEmailUsers(String id) throws OaException {
//        if (id == null) {
//            throw new OaException("参数异常：ID为空");
//        }
//        List<String> emailUsers = new ArrayList<String>(0);
//        InfoLayout infoLayout = infoLayoutDAO.findById(id);
//        // 如果该信息公告参数不存在，则直接退出
//        if (infoLayout == null) {
//            return emailUsers;
//        }
//        //取得该信息配置的收件人账号
//        String emailAddr = infoLayout.getEmailAddr();
//        if (emailAddr != null) {
//            //分隔
//            emailUsers = Arrays.asList(emailAddr.split(","));
//        }
//        return emailUsers;
//    }
//
//    @Override
//    public void txSaveBindEmailUser(String id, String emailUsers) throws OaException {
//        if (id == null) {
//            throw new OaException("参数异常：ID为空");
//        }
//        //根据ID查找信息参数设置
//        InfoLayout infoLayout = infoLayoutDAO.findById(id);
//        if (infoLayout != null) {
//            // 设置收件人账号
//            infoLayout.setEmailAddr(emailUsers);
//        }
//        //保存
//        infoLayoutDAO.saveOrUpdate(infoLayout);
//    }
}
