package org.eapp.workflow.exe;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;


/**
 * PooledActorRoles entity.
 * 
 * @author 林良益
 * @version 1.0
 * 
 */

public class PooledRole implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -1126987075235351163L;
	String id;
	protected TaskInstance taskInstance;
	protected String actorRoleId;

	// Constructors

	/** default constructor */
	public PooledRole() {
	}

	/** minimal constructor */
	public PooledRole(String actorRoleId) {
		this.actorRoleId = actorRoleId;
	}

	/** full constructor */
	public PooledRole(TaskInstance taskInstance, String actorRoleId) {
		this.taskInstance = taskInstance;
		this.actorRoleId = actorRoleId;
	}
	
	/**
	 * 根据ID数组生成PooledRole集合
	 * @param roleIds
	 * @param taskInstance
	 * @return
	 */
	public static Set<PooledRole> createPool(String[] roleIds, TaskInstance taskInstance) {
		Set<PooledRole> pooledRoles = new HashSet<PooledRole>();
		for (int i=0; i< roleIds.length; i++) {
			if (StringUtils.isBlank(roleIds[i])) {
				continue;
			}
			PooledRole pooledRole = new PooledRole(taskInstance , roleIds[i]);
			pooledRoles.add(pooledRole);
		}
		return pooledRoles;
	}
		

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TaskInstance getTaskInstance() {
		return this.taskInstance;
	}

	public void setTaskInstance(TaskInstance taskInstance) {
		this.taskInstance = taskInstance;
	}

	public String getActorRoleId() {
		return this.actorRoleId;
	}

	public void setActorRoleId(String actorRoleId) {
		this.actorRoleId = actorRoleId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof PooledRole))
			return false;
		final PooledRole other = (PooledRole) obj;
		if (id == null || other.getId() == null) {
				return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}

}