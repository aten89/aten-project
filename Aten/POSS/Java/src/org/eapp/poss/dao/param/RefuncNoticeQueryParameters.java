/**
 * 
 */
package org.eapp.poss.dao.param;

import java.util.Date;

import org.eapp.util.hibernate.QueryParameters;

/**
 * 
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-21	lhg		新建
 * </pre>
 */
public class RefuncNoticeQueryParameters extends QueryParameters {

    /**
     * 
     */
    private static final long serialVersionUID = -4991227586623723865L;

    public Date getCreateTimeBegin() {
        return (Date) this.getParameter("createTimeBegin");
    }

    public void setCreateTimeBegin(Date createTimeBegin) {
        if (createTimeBegin != null) {
            this.addParameter("createTimeBegin", createTimeBegin);
        }
    }
    
    public Date getCreateTimeEnd() {
        return (Date) this.getParameter("createTimeEnd");
    }

    public void setCreateTimeEnd(Date createTimeEnd) {
        if (createTimeEnd != null) {
            this.addParameter("createTimeEnd", createTimeEnd);
        }
    }
    
    public String getTrustCompany() {
    	return (String) this.getParameter("trustCompany");
    }
    
    public void setTrustCompany(String trustCompany) {
    	if (trustCompany != null) {
            this.addParameter("trustCompany", trustCompany);
        }
    }
}
