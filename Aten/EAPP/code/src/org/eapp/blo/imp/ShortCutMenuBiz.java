/**
 * 
 */
package org.eapp.blo.imp;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eapp.blo.IShortCutMenuBiz;
import org.eapp.dao.IShortCutMenuDAO;
import org.eapp.dao.IUserAccountDAO;
import org.eapp.exception.EappException;
import org.eapp.hbean.ShortCutMenu;
import org.eapp.hbean.UserAccount;


/**
 * @version  1.0
 */
public class ShortCutMenuBiz implements IShortCutMenuBiz {

	private IShortCutMenuDAO shortCutMenuDAO = null;
	private IUserAccountDAO userAccountDAO = null;
	
	public void setUserAccountDAO(IUserAccountDAO userAccountDAO) {
		this.userAccountDAO = userAccountDAO;
	}

	/**
	 * @param shortCutMenuDAO the shortCutMenuDAO to set
	 */
	public void setShortCutMenuDAO(IShortCutMenuDAO shortCutMenuDAO) {
		this.shortCutMenuDAO = shortCutMenuDAO;
	}

	@Override
	public List<ShortCutMenu> getAllShortCutMenusByUser(String userid) {
		return shortCutMenuDAO.findByUserID(userid);
	}

	@Override
	public Set<ShortCutMenu> getEnableShortCutMenu(String userid) {
		UserAccount userAccount = userAccountDAO.findById(userid);
		if (userAccount == null) {
			return null;
		}
		userAccount.getEnableShortCutMenus().size();//加载延迟内容
		return userAccount.getEnableShortCutMenus();
	}
	
	@Override
	public Set<ShortCutMenu> getFavoriteShortCutMenu(String userid) {
		UserAccount userAccount = userAccountDAO.findById(userid);
		if (userAccount == null) {
			return null;
		}
		userAccount.getFavoriteShortCutMenus().size();//加载延迟内容
		return userAccount.getFavoriteShortCutMenus();
	}

	@Override
	public Set<ShortCutMenu> getDisableShortCutMenu(String userid) {
		UserAccount userAccount = userAccountDAO.findById(userid);
		if (userAccount == null) {
			return null;
		}
		userAccount.getDisableShortCutMenus().size();//加载延迟内容
		return userAccount.getDisableShortCutMenus();
	}

	@Override
	public ShortCutMenu getShortCutMenu(String id) {
		if(StringUtils.isBlank(id)){
			throw new IllegalArgumentException("快捷方式ID不能为空");
		}
		return shortCutMenuDAO.findById(id);
	}

	@Override
	public void txDisableShortCutMenu(String shortCutMenuId) throws EappException {
		if(StringUtils.isBlank(shortCutMenuId)){
			throw new IllegalArgumentException("快捷方式ID不能为空");
		}
		ShortCutMenu shortCut = shortCutMenuDAO.findById(shortCutMenuId);
		if(shortCut == null){
			throw new IllegalArgumentException("参数异常:数据库中不存在该快捷方式ID"+shortCutMenuId);
		}
		if(!shortCut.getIsValid()){
			throw new EappException("快捷方式已撤销,不能重复撤销");
		}
		shortCut.setIsValid(Boolean.FALSE);
		shortCutMenuDAO.saveOrUpdate(shortCut);
	}

	@Override
	public ShortCutMenu addShortCutMenu(String userid, String menuTitle, String moduleTitle, String menuLink,
			String windowTarget, String menuIcon, boolean isValid, String type) throws EappException {
		if(StringUtils.isBlank(menuTitle)){
			throw new IllegalArgumentException("快捷方式标题不能为空");
		}
		if(StringUtils.isBlank(menuLink)){
			throw new IllegalArgumentException("快捷方式链接不能为空");
		}
		if(StringUtils.isBlank(windowTarget)){
			throw new IllegalArgumentException("快捷方式打开方式不能为空");
		}
		if(StringUtils.isBlank(type)){
			throw new IllegalArgumentException("快捷方式的类型不能为空");
		}
		if(shortCutMenuDAO.isShortCutMenuRepeat(userid, type, menuTitle)){
			throw new EappException("不能重复添加");
		}
		int maxOrder = shortCutMenuDAO.getMaxDisplayOrderByUserID(userid);
		ShortCutMenu shortCutMenu = new ShortCutMenu();
		UserAccount userAccount = userAccountDAO.findById(userid);
		shortCutMenu.setUserAccount(userAccount);
		shortCutMenu.setMenuTitle(menuTitle);
		shortCutMenu.setModuleTitle(moduleTitle);
		shortCutMenu.setUrl(menuLink);
		shortCutMenu.setWindowTarget(windowTarget);
		shortCutMenu.setLogoURL(menuIcon);
		shortCutMenu.setIsValid(isValid);
		shortCutMenu.setDisplayOrder(maxOrder+1);
		shortCutMenu.setType(type);
		shortCutMenuDAO.save(shortCutMenu);
		return shortCutMenu;
	}

	@Override
	public void deleteShortCutMenu(String id, boolean custom) {
		ShortCutMenu sc = shortCutMenuDAO.findById(id);
		if (sc == null) {
			return;
		}
		if (custom && ShortCutMenu.SYSTEM_TYPE.equals(sc.getType())) {
			throw new IllegalArgumentException("不能删除系统快捷菜单");
		}
		shortCutMenuDAO.delete(sc);
	}

	@Override
	public void txEnableShortCutMenu(String id) throws EappException {
		if(StringUtils.isBlank(id)){
			throw new IllegalArgumentException("快捷方式ID不能为空");
		}
		ShortCutMenu shortCut = shortCutMenuDAO.findById(id);
		if(shortCut == null){
			throw new IllegalArgumentException("参数异常:数据库中不存在该快捷方式ID"+id);
		}
		if(shortCut.getIsValid()){
			throw new EappException("快捷方式已发布,不能重复发布");
		}
		shortCut.setIsValid(Boolean.TRUE);
		shortCutMenuDAO.saveOrUpdate(shortCut);

	}

	@Override
	public void modifyShortCutMenu(String shortCutID, String menuTitle, String menuLink, String windowTarget, String menuIcon, boolean isValid, String type) throws EappException {
		if(StringUtils.isBlank(shortCutID)){
			throw new IllegalArgumentException("快捷方式ID不能为空");
		}
		if(StringUtils.isBlank(menuTitle)){
			throw new IllegalArgumentException("快捷方式标题不能为空");
		}
		if(StringUtils.isBlank(menuLink)){
			throw new IllegalArgumentException("快捷方式链接不能为空");
		}
		if(StringUtils.isBlank(windowTarget)){
			throw new IllegalArgumentException("快捷方式打开方式不能为空");
		}
		if(StringUtils.isBlank(type)){
			throw new IllegalArgumentException("快捷方式的类型不能为空");
		}
		ShortCutMenu srcShortCutMenu = shortCutMenuDAO.findById(shortCutID);
		if( srcShortCutMenu == null ){
			throw new IllegalArgumentException("参数异常:数据库中不存在该快捷方式ID:"+shortCutID);
		}
		if(!srcShortCutMenu.getMenuTitle().equals(menuTitle)&&shortCutMenuDAO.isShortCutMenuRepeat(srcShortCutMenu.getUserAccount().getUserID(), type, menuTitle)){
			throw new EappException("快捷方式的标题不能重复");
		}
		srcShortCutMenu.setMenuTitle(menuTitle);
		srcShortCutMenu.setUrl(menuLink);
		srcShortCutMenu.setLogoURL(menuIcon);
		srcShortCutMenu.setWindowTarget(windowTarget);
		srcShortCutMenu.setIsValid(isValid);
		srcShortCutMenu.setType(type);
		shortCutMenuDAO.merge(srcShortCutMenu);
	}

	@Override
	public void modifyShortCutMenuSort(Map<String, Integer> idsort) {
		if(idsort == null){
			throw new IllegalArgumentException();
		}
		shortCutMenuDAO.sortShortCutMenus(idsort);
	}

	@Override
	public void txManageShortCutMenus(Map<String, Integer> idsortPublish, Map<String, Integer> idsortCancel) {
		shortCutMenuDAO.sortShortCutMenus(idsortPublish, Boolean.TRUE);
		shortCutMenuDAO.sortShortCutMenus(idsortCancel, Boolean.FALSE);
	}

	@Override
	public List<ShortCutMenu> getSystemShortCutMenus() {
		return shortCutMenuDAO.findByType(ShortCutMenu.SYSTEM_TYPE);
	}
	
//	@Override
//	public ListPage<ShortCutMenu> queryShortCutMenus(QueryParameters qp) {
//		return shortCutMenuDAO.queryShortCutMenus(qp);
//	}

	@Override
	public List<ShortCutMenu> getEnableSystemShortCutMenus() {
		return shortCutMenuDAO.findEnableByType(ShortCutMenu.SYSTEM_TYPE);
	}
	
}
