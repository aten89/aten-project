package org.eapp.oa.doc.dto;

import java.util.List;

import org.eapp.oa.doc.hbean.DocClass;
import org.eapp.oa.system.dto.HTMLOptionsDTO;

/**
 * 
 * @author sds
 * @version May 8, 2009
 */
public class DocClassSelect  extends HTMLOptionsDTO {
	public List<DocClass> page;
	
	public List<DocClass> getPage() {
		return page;
	}

	public void setPage(List<DocClass> page) {
		this.page = page;
	}
	
	public DocClassSelect(List<DocClass> page) {
		super();
		this.page = page;
	}

	public String toString() {
		
		StringBuffer out = new StringBuffer();
		if (page == null || page.size()<0) {
				return out.toString();
		}
		for (DocClass d : page) {
				out.append(createOption(d.getId(),d.getName()));
		}
		return out.toString();
	}
}

