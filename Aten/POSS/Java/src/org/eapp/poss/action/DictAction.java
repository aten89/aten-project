/**
 * 
 */
package org.eapp.poss.action;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eapp.poss.config.SysCodeDictLoader;
import org.eapp.poss.dto.DataDictionarySelect;
import org.eapp.poss.util.HTMLResponse;
import org.eapp.rpc.dto.DataDictInfo;


/**
 * 
 * 
 *
 * <pre>
 * 修改日期		修改人	修改原因
 * 2014-5-26	lhg		新建
 * </pre>
 */
public class DictAction extends BaseAction {

    /**
     * 
     */
    private static final long serialVersionUID = 1979804529492382842L;
    
    private static Log logger = LogFactory.getLog(DictAction.class);
    
    
    public void initProdStatusSel() {
        try {
            Collection<DataDictInfo> list = SysCodeDictLoader.getInstance().getProdStatusMap().values();
            if (list == null) {
                return;
            }
            HTMLResponse.outputHTML(getResponse(), new DataDictionarySelect(list).toString());
        } catch (Exception e) {
            logger.error("initProdStatusSel failed: ", e);
            return;
        }
    }
    
    public void initSellRankSel() {
        try {
            Collection<DataDictInfo> list = SysCodeDictLoader.getInstance().getSellRankMap().values();
            if (list == null) {
                return;
            }
            HTMLResponse.outputHTML(getResponse(), new DataDictionarySelect(list).toString());
        } catch (Exception e) {
            logger.error("initSellRankSel failed: ", e);
            return;
        }
    }

}
