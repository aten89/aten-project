/**
 * 
 */
package org.eapp.workflow.def;

/**
 * 可分派对像接口
 * @author 林良益
 * 2008-08-28
 * @version 1.0
 */
public interface IAssignable {

	  /**
	   * 分派指定对象
	   * @param actorId
	   */	
	  public void setActorId(String actorId);
	  
	  /**
	   * 分派群体对象
	   * @param pooledActors
	   */
	  public void setPooledActors(String[] pooledActors);
	  
	  /**
	   * 分配群体角色
	   * @param pooledRoles
	   */
	  public void setPooledRoles(String[] pooledRoles);
}
