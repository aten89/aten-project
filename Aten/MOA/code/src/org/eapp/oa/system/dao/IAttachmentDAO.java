package org.eapp.oa.system.dao;

import java.util.List;

import org.eapp.oa.system.hbean.Attachment;

public interface IAttachmentDAO extends IBaseHibernateDAO {

	public Attachment findById(String id);

	public List<Attachment> findByFileExt(Object fileExt);

	public List<Attachment> findByDisplayName(Object displayName);
}