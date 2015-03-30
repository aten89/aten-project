/**
 * 
 */
package org.eapp.action;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.blo.IFlowDataBiz;
import org.eapp.dto.FlowTaskBean;
import org.eapp.util.web.DataFormatUtil;

/**
 * @version
 */
public class FlowDataAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7294486562729366421L;
	private static final Log log = LogFactory.getLog(FlowDataAction.class);
	
	private IFlowDataBiz flowDataBiz;

	//IN PARAMS
	private String taskName;	//任务名称
    private String startCreateTime;	//开始任务创建时间
    private String endCreateTime;	//结束任务创建时间
    private String clearDate;	//清除日期
	
	//OUT PARAMS
	private List<FlowTaskBean> flowTaskBeans;

	public void setFlowDataBiz(IFlowDataBiz flowDataBiz) {
		this.flowDataBiz = flowDataBiz;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public void setStartCreateTime(String startCreateTime) {
		this.startCreateTime = startCreateTime;
	}

	public void setEndCreateTime(String endCreateTime) {
		this.endCreateTime = endCreateTime;
	}

	public void setClearDate(String clearDate) {
		this.clearDate = clearDate;
	}

	public List<FlowTaskBean> getFlowTaskBeans() {
		return flowTaskBeans;
	}


	/**
	 * 页面初始化
	 */
	public String initQueryTaskPage() {
		return success();
	}
	
	//查询任务
	public String queryFlowTask() {
		if (StringUtils.isBlank(startCreateTime) || StringUtils.isBlank(endCreateTime)) {
			return error("查询开始与结束时间不能为空");
		}
		
		try {
			Timestamp _beginTime = Timestamp.valueOf(DataFormatUtil.formatTime(startCreateTime));
			Timestamp _endTime = Timestamp.valueOf(DataFormatUtil.formatTime(endCreateTime));
			//加一天
			Calendar c = Calendar.getInstance();
			c.setTime(_endTime);
			c.set(Calendar.HOUR_OF_DAY, 24);
			_endTime = new Timestamp(c.getTimeInMillis());
			flowTaskBeans = flowDataBiz.queryOpeningTasks(_beginTime, _endTime, taskName);
			return success();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error();
		}
	}
	
	
	/**
	 * 页面初始化
	 */
	public String initDelDataPage() {
		return success();
	}
	
	//删除历史数据
	public String deleteFlowData() {
		if (StringUtils.isBlank(clearDate)) {
			return error("清除时间不能为空");
		}
		try {
			Timestamp _clearDate = Timestamp.valueOf(DataFormatUtil.formatTime(clearDate));
			flowDataBiz.clearFlowInstanceData(_clearDate);
			ActionLogger.log(getRequest(), null, "清除日期：" + clearDate);
			return success();
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			return error(e.getMessage());
		}
	}

}
