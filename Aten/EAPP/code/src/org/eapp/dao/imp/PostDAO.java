package org.eapp.dao.imp;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.dao.IPostDAO;
import org.eapp.dao.param.PostQueryParameters;
import org.eapp.hbean.Post;
import org.eapp.util.hibernate.CommQuery;
import org.eapp.util.hibernate.ListPage;
import org.hibernate.Query;

/**
 * @see org.eapp.hbean.Group
 * @author MyEclipse Persistence Tools
 */

public class PostDAO extends BaseHibernateDAO implements IPostDAO {
	private static final Log log = LogFactory.getLog(PostDAO.class);

	@Override
	public Post findById(String id) {
		log.debug("getting Post instance with id: " + id);
		Post instance = (Post) getSession().get("org.eapp.hbean.Post", id);
		return instance;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Post> findByPostName(String postName) {
		Query queryObject = getSession().createQuery("from Post as post where post.postName= :postName");
		queryObject.setString("postName", postName);
		return queryObject.list();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Post> findByGroupID(String groupID) {
		Query queryObject = getSession().createQuery("from Post as post where post.group.groupID= :groupID");
		queryObject.setString("groupID", groupID);
		return queryObject.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Post> findRootPosts() {
		log.debug("finding root Post instances");
		String queryString = "select p,p.subPosts.size from Post as p where p.parentPost is null";
		queryString += " order by p.displayOrder";
		Query queryObject = getSession().createQuery(queryString);
		List<Object[]> list = queryObject.list();
		List<Post> posts = new ArrayList<Post>(list.size());
		Post p = null;
		Integer count = null;
		for(Object[] o : list) {
			p = (Post)o[0];
			count = (Integer)o[1];
			p.setHasSubPost(count != null && count.longValue() > 0);
			posts.add(p);
		}
		return posts;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Post> findSubPosts(String postID) {
		log.debug("finding sub Post instances");
		String queryString = "select p,p.subPosts.size from Post as p where p.parentPost.postID=:postID";
		queryString += " order by p.displayOrder";
		Query queryObject = getSession().createQuery(queryString).setString("postID", postID);
		List<Object[]> list = queryObject.list();
		List<Post> posts = new ArrayList<Post>(list.size());
		Post p = null;
		Integer count = null;
		for(Object[] o : list) {
			p = (Post)o[0];
			count = (Integer)o[1];
			p.setHasSubPost(count != null && count.longValue() > 0);
			posts.add(p);
		}
		return posts;
	}

	@Override
	public ListPage<Post> queryPost(PostQueryParameters qp) {
		if (qp == null)  {
			throw new IllegalArgumentException();
		}
		try {
			StringBuffer hql = new StringBuffer("select p from Post as p where 1=1");
			
			//拼接查询条件
			if (qp.getPostName() != null) {
				hql.append(" and p.postName like :postName");
				qp.toArountParameter("postName");
			}
			Boolean isBinded = qp.isBinded();
			if (isBinded != null) {
				if (isBinded.booleanValue()) {
					hql.append(" and p.group is not null");
				} else {
					hql.append(" and p.group is null");
				}
				qp.setBinded(null);
			}
				return new CommQuery<Post>().queryListPage(qp, 
						qp.appendOrders(hql, "p" ), getSession());
			} catch (RuntimeException re) {
				log.error("query listPage error",re);
				return new ListPage<Post>();
			}
	}

	@Override
	public int getNextDisplayOrder(String postID) {
		StringBuffer hql = new StringBuffer("select max(p.displayOrder) from Post as p where");
		if (StringUtils.isNotBlank(postID)) {
			hql.append(" p.parentPost.postID=:postID");
		} else {
			hql.append(" p.parentPost is null");
		}
		Query query = getSession().createQuery(hql.toString());
		if (StringUtils.isNotBlank(postID)) {
			query.setString("postID", postID);
		}
		Integer order = (Integer)query.uniqueResult();
		if(order == null) {
			return 1;
		}
		return order.intValue() + 1;
	}

	@Override
	public int getNextTreeLevel(String postID) {
		if (StringUtils.isBlank(postID)) {
			return 1;
		}
		Query query = getSession().createQuery("select p.treeLevel from Post as p " +
				"where p.postID=:postID").setString("postID", postID);
		Integer level = (Integer)query.uniqueResult();
		if (level == null) {
			return 1;
		}
		return level.intValue() + 1;
	}

	@Override
	public boolean existSubPost(Post post) {
		Long count = (Long)getSession().createFilter(post.getSubPosts(), "select count(*)").uniqueResult();
		return count.intValue() > 0;
	}

	@Override
	public void deleteByID(String postID) {
		if (StringUtils.isBlank(postID)) {
			return;
		}
		Post post = this.findById(postID);
		if (post == null) {
			return;
		}
		//更新职位关联的群组
		Query query = getSession().createQuery("update Group as g set g.managerPost = null " +
				"where g.managerPost=:post");
		query.setParameter("post", post);
		query.executeUpdate();
		this.delete(post);
	}

	@Override
	public void saveOrder(String[] postIDs, String parentPostID) {
		if (postIDs == null || postIDs.length < 1) {
			return;
		}

		Query query = null;
		if (StringUtils.isNotBlank(parentPostID)) {
			query = getSession().createQuery("update Post as p set p.displayOrder=:order " +
					"where p.postID=:postID and p.parentPost.postID=:parentPostID");
			query.setString("parentPostID", parentPostID);
		} else {
			query = getSession().createQuery("update Post as p set p.displayOrder=:order " +
					"where p.postID=:postID and p.parentPost is null");
		}
		int order = 1;
		for (int i = 0; i < postIDs.length; i++) {
			if (StringUtils.isBlank(postIDs[i])) {
				continue;
			}
			query.setInteger("order", order)
					.setString("postID", postIDs[i]);
			if (query.executeUpdate() > 0) {
				order++;
			}
		}
	}

	@Override
	public void updateOrder(String parentPostID, Integer theOrder) {
		StringBuffer hql = new StringBuffer("update Post as p set p.displayOrder=p.displayOrder-1 " +
				"where p.displayOrder>:displayOrder");
		if (StringUtils.isNotBlank(parentPostID)) {
			hql.append(" and p.parentPost.postID=:parentPostID");
		} else {
			hql.append(" and p.parentPost is null");
		}
		Query query = getSession().createQuery(hql.toString());
		query.setInteger("displayOrder", theOrder == null ? 0 : theOrder.intValue());
		if (StringUtils.isNotBlank(parentPostID)) {
			query.setString("parentPostID", parentPostID);
		}
		query.executeUpdate();
	}
	
	@Override
	public boolean checkRepetition(String parentPostID, String name) {
		if(StringUtils.isBlank(name)) {
			return true;
		}
		Query query = null;
		if (parentPostID == null) {
			query = getSession().createQuery("select count(*) from Post as p where p.postName=:postName");
		} else {
			query = getSession().createQuery("select count(*) from Post as p where p.postName=:postName " +
					"and p.parentPost.postID=:parentPostID");
			query.setString("parentPostID", parentPostID);
		}
		query.setString("postName", name);
		Long count = (Long)query.uniqueResult();
		if (count != null && count.longValue() > 0) {
			return true;
		}
		return false;
	}
}