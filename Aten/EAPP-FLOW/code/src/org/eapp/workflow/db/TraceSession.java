/**
 * 
 */
package org.eapp.workflow.db;

import java.util.List;

import org.eapp.workflow.WfmException;
import org.eapp.workflow.trace.TracePoint;
import org.hibernate.Query;


/**
 * TrackPoint数据库操作对象
 * @author 林良益
 * 2008-11-04
 * @version 2.0
 */
public class TraceSession {

	protected HibernateSessionFactory dbService;
	
	public TraceSession(HibernateSessionFactory dbService){
		this.dbService = dbService;
	}
	
	/**
	 * 标记一个轨迹---保存
	 * @param job
	 */
	public void save(TracePoint tp){
		dbService.getSession(true).saveOrUpdate(tp);
	}
	
	/**
	 * 加载流程轨迹
	 * @param tracePointID
	 * @return
	 */
	public TracePoint getTracePoint(String tracePointID){
		return (TracePoint)dbService.getSession().get(TracePoint.class, tracePointID);
	}
	
	/**
	 * 根据流程实例，获取全流程的轨迹
	 * @param flowInstance
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TracePoint> getTracePoints(String flowInstanceId){
		List<TracePoint> traces = null;
		try{
			Query query = dbService.getSession().getNamedQuery("TraceSession.getTracePoints");
			query.setString("flowInstanceId", flowInstanceId);
			traces = query.list();
		} catch(Exception e){
			e.printStackTrace();
			throw new WfmException("无法取得流程实例关联的轨迹", e);
		}
		return traces;
	}
}
