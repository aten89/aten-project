package org.eapp.poss.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eapp.comobj.SessionAccount;
import org.eapp.comobj.SessionAccount.Name;
import org.eapp.poss.blo.ITaskBiz;
import org.eapp.poss.exception.PossException;
import org.eapp.poss.hbean.Task;
import org.eapp.util.hibernate.ListPage;
import org.eapp.util.hibernate.QueryParameters;

/**
 * 流程任务Action
 * <pre>
 * 修改日期		修改人    修改原因
 * 2012-11-1	hql	          添加注释
 * </pre>
 */
public class TaskAction extends BaseAction{
	/**
     * 
     */
    private static final long serialVersionUID = 6462742925906293161L;

	private ITaskBiz taskBiz;
	
    //IN PARAMS
    private int pageNo;
	private int pageSize;
	private String taskid;
	private String tiid;
	
	
	//OUT PARAMS
	private ListPage<Task> taskPage;
	private String viewUrl;
    
	public void setTaskBiz(ITaskBiz taskBiz) {
        this.taskBiz = taskBiz;
    }

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public void setTiid(String tiid) {
		this.tiid = tiid;
	}

	public ListPage<Task> getTaskPage() {
		return taskPage;
	}

	public String getViewUrl() {
		return viewUrl;
	}

	/**
	 * 所有待办任务门户
	 * @return
	 */
	public String loadDealingTasks() {
		SessionAccount user = (SessionAccount)getSession().getAttribute("sessionUser");
		if (user == null) {
			return error("请先登录");
		}
		QueryParameters qp = new QueryParameters();
        qp.setPageNo(pageNo);
        qp.setPageSize(pageSize);
		taskPage = taskBiz.queryDealingTasks(qp, user.getAccountID());
		return success();
	}
	
	/**
	 * 初始化流程处理
	 */
	public String initDispose() {
		SessionAccount user = (SessionAccount)getSession().getAttribute("sessionUser");
		if (user == null) {
			return error("请先登录");
		}
		if (StringUtils.isEmpty(taskid)) {
        	return error("任务不存在");
        }
		try {
			
			List<String> userRoles = new ArrayList<String>();
     		List<Name> roles = user.getRoles();
     		if (roles != null && !roles.isEmpty()) {
     			for (Name role : roles) {
     				userRoles.add(role.getName());
     			}
     		}
     		
			viewUrl = taskBiz.txStartTask(taskid, user.getAccountID(), userRoles);
			viewUrl += "&tiid=" + tiid;
			
			return success();
		} catch (PossException e) {
			return error(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return error("系统错误");
		}
	}

}