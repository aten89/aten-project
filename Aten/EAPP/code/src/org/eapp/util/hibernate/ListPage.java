/*
 * 创建日期 2005-12-29
 * 林良益 @caripower
 */
package org.eapp.util.hibernate;

import java.io.Serializable;
import java.util.List;
/**
 * 
 * @param <T> 分页对象类型
 * 
 * @author linliangyi@team of miracle
 *
 * 创建日期 2005-12-29
 * 
 * 基于List的数据分页对象
 */

public class ListPage<T> implements Serializable {

    /**
	 * 序列
	 */
	private static final long serialVersionUID = 1960189378734246173L;
	/**
	 * 空页
	 */
	public static final ListPage<Object> EMPTY_PAGE = new ListPage<Object>();

    /**
     * 页面记录数，等于0时显示所有
     */
    private int currentPageSize;
    /**
     * 总记录数
     */
    private long totalCount;
    /**
     * 当前页页码
     */
    private int currentPageNo;
    /**
     * 当前页记录列表
     */
    private List<T> dataList;
    /**
     * 构造
     */
    public ListPage() {
    }    
    /**
     * 获取当前页码
     * @return 返回 currentPageno。
     */
    public int getCurrentPageNo() {
        return currentPageNo;
    }
    /**
     * 设置当前页码
     * @param currentPageNo 要设置的 currentPageNo。
     */
    public void setCurrentPageNo(int currentPageNo) {
        this.currentPageNo = currentPageNo;
    }
    /**
     * 获取当前页面记录数
     * @return 返回 currentPageSize。
     */
    public int getCurrentPageSize() {
        return currentPageSize;
    }
    /**
     * 设置当前页面记录数
     * @param currentPageSize 要设置的 currentPageSize。
     */
    public void setCurrentPageSize(int currentPageSize) {
        this.currentPageSize = currentPageSize;
    }
    /**
     * 获取当前页对象列表
     * @return 返回 dataList。
     */
    public List<T> getDataList() {
        return dataList;
    }
    /**
     * 设置当前页对象列表
     * @param dataList 要设置的 dataList。
     */
    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
    /**
     * 获取总记录数
     * @return 返回 totalSize。
     */
    public long getTotalCount() {
        return totalCount;
    }
    /**
     * 设置总记录数
     * @param totalCount 要设置的 totalCount。
     */
    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
    
    /**
     * 获取总页数
     * @return 总页数
     */
    public long getTotalPageCount() {
    	if (currentPageSize == 0) {
    		return 1;
    	}
     	return (totalCount - 1) / currentPageSize + 1;
    }
    
    /**
     * 是否有下一页
     * @return 是否有下一页
     */
    public boolean hasNextPage() {
      return (currentPageNo < this.getTotalPageCount());
    }

    /**
     * 是否有上一页
     * @return  是否有上一页
     */
    public boolean hasPreviousPage() {
      return (currentPageNo > 1);
    }
    
    /**
     * 判断是否为空页
     * @return 是否为空页
     */
    public boolean isEmpty() {
        return totalCount == 0;
    }
    
}
