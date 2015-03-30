/**
 * 
 */
package org.eapp.crm.action;

import java.util.Collection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.crm.config.SysCodeDictLoader;
import org.eapp.crm.dto.DataDictionarySelect;
import org.eapp.crm.system.util.web.HTMLResponse;
import org.eapp.rpc.dto.DataDictInfo;

/**
 * 数据字典Action
 * 
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-6	钟华杰	新建
 * </pre>
 */
public class DictAction extends BaseAction {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -3904449546822356285L;
    /**
     * 日志
     */
    private static Log log = LogFactory.getLog(DictAction.class);
    /**
     * htmlValue
     */
    private String htmlValue;

    /**
     * 初始化产品下拉框
     * 
     * 
     * <pre>
     * 修改日期		修改人	修改原因
     * 2013-7-18	李海根	新建
     * </pre>
     */
    public void initProductSel() {
        try {
            Collection<DataDictInfo> list = SysCodeDictLoader.getInstance().getProductMap().values();
            if (list == null) {
                return;
            }
            HTMLResponse.outputHTML(getResponse(), new DataDictionarySelect(list).toString());
        } catch (Exception e) {
            log.error("initProductSel failed: ", e);
            return;
        }
    }
    
    /**
     * 初始化提醒类型下拉框
     */
    public void initAppointmentTypeSel() {
    	try {
    		Collection<DataDictInfo> list = SysCodeDictLoader.getInstance().getAppointmentTypeMap().values();
    		if (list == null) {
    			return;
    		}
    		HTMLResponse.outputHTML(getResponse(), new DataDictionarySelect(list).toString());
    	} catch (Exception e) {
    		log.error("initAppointmentTypeMapSel failed: ", e);
    		return;
    	}
    }
    
    /**
     * 加载性别字典
     */
    public void initSexSel() {
        try {
            Collection<DataDictInfo> list = SysCodeDictLoader.getInstance().getSexMap().values();
            if (list == null) {
                return;
            }
            HTMLResponse.outputHTML(getResponse(), new DataDictionarySelect(list).toString());
        } catch (Exception e) {
            log.error("initSexSel failed: ", e);
            return;
        }
    }
    
    /**
     * 加载客户性质字典
     */
    public void initCustomerNatureSel() {
        try {
            Collection<DataDictInfo> list = SysCodeDictLoader.getInstance().getCustomerNatureMap().values();
            if (list == null) {
                return;
            }
            HTMLResponse.outputHTML(getResponse(), new DataDictionarySelect(list).toString());
        } catch (Exception e) {
            log.error("initCustomerNatureSel failed: ", e);
            return;
        }
    }
    
    /**
     * 加载沟通类型字典
     */
    public void initCommTypeSel() {
        try {
            Collection<DataDictInfo> list = SysCodeDictLoader.getInstance().getCommTypeMap().values();
            if (list == null) {
                return;
            }
            HTMLResponse.outputHTML(getResponse(), new DataDictionarySelect(list).toString());
        } catch (Exception e) {
            log.error("initCommTypeSel failed: ", e);
            return;
        }
    }
    
    /**
     * 返回 htmlValue
     * @return the htmlValue
     */
    public String getHtmlValue() {
        return htmlValue;
    }
}
