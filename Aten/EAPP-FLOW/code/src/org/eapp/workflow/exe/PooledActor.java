package org.eapp.workflow.exe;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;


/**
 * PooledActors entity.
 * 
 * @author 卓诗垚
 * @version 1.0
 */

public class PooledActor implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -3092471054813280993L;
	String id;
	protected TaskInstance taskInstance;
	protected String actorId;

	// Constructors

	/** default constructor */
	public PooledActor() {
	}

	/** minimal constructor */
	public PooledActor(String actorId) {
		this.actorId = actorId;
	}

	/** full constructor */
	public PooledActor(TaskInstance taskInstance, String actorId) {
		this.taskInstance = taskInstance;
		this.actorId = actorId;
	}
	
	/**
	 * 根据ID数组生成PooledActor集合
	 * @param actorIds
	 * @param taskInstance
	 * @return
	 */
	public static Set<PooledActor> createPool(String[] actorIds, TaskInstance taskInstance) {
		Set<PooledActor> pooledActors = new HashSet<PooledActor>();
		for (int i=0; i<actorIds.length; i++) {
			if (StringUtils.isBlank(actorIds[i])) {
				continue;
			}
			PooledActor pooledActor = new PooledActor(taskInstance , actorIds[i]);
			pooledActors.add(pooledActor);
		}
		return pooledActors;
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

	public String getActorId() {
		return this.actorId;
	}

	public void setActorId(String actorId) {
		this.actorId = actorId;
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
		if (!(obj instanceof PooledActor))
			return false;
		final PooledActor other = (PooledActor) obj;
		if (id == null || other.getId() == null) {
			return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}

}