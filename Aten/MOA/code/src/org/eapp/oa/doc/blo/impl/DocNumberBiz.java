package org.eapp.oa.doc.blo.impl;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import org.eapp.oa.doc.blo.IDocNumberBiz;
import org.eapp.oa.doc.dao.IDocNumberDAO;
import org.eapp.oa.doc.hbean.DocNumber;
import org.eapp.oa.system.exception.OaException;
import org.eapp.oa.system.hbean.Attachment;
import org.eapp.util.web.upload.FileDispatcher;

/**
 * 
 * @author sds
 * @version May 8, 2009
 */
public class DocNumberBiz implements IDocNumberBiz {

	private IDocNumberDAO docNumberDAO;

	@Override
	public List<DocNumber> getAllDocNumber() {
		return docNumberDAO.findAll();
	}

	@SuppressWarnings("deprecation")
	@Override
	public DocNumber addDocNumber(String docWord, String yearPrefix,
			String yearPostfix, String orderPrefix, String orderPostfix)
			throws OaException {
		isDocWordRepeat(docWord);
		Calendar cal = Calendar.getInstance();  
		int currentYear = cal.get(Calendar.YEAR); 
		int orderNumber = 1;
		DocNumber docNumber = new DocNumber();
		docNumber.setDocWord(docWord);
		docNumber.setYearPrefix(yearPrefix);
		docNumber.setCurrentYear(Integer.valueOf(currentYear));
		docNumber.setYearPostfix(yearPostfix);
		docNumber.setOrderPrefix(orderPrefix);
		docNumber.setOrderNumber(Integer.valueOf(orderNumber));
		docNumber.setOrderPostfix(orderPostfix);
		docNumber.setDescription(docNumber.toString());
		docNumberDAO.save(docNumber);

		return docNumber;
	}
	
	/**
	 * 验证公文类别名称是否重复
	 * 
	 * @param name
	 * @throws OaException
	 */
	private void isDocWordRepeat(String docword) throws OaException {
		List<DocNumber> docNumbers = docNumberDAO.findByDocWord(docword);
		if (docNumbers != null && docNumbers.size() > 0) {
			throw new OaException("文件字不能重复!");
		}

	}

	@Override
	public DocNumber deleteDocNumber(String id) throws OaException {
		DocNumber docNumber = docNumberDAO.findById(id);
		if (docNumber == null) {
			throw new OaException("编号不存在");
		}
		
		Attachment headTemplate = docNumber.getHeadTemplate();
		if (headTemplate != null) {
			// 删除文件
			File f = FileDispatcher.findFile(FileDispatcher.getAbsPath(headTemplate.getFilePath()));
			if (f != null) {
				f.delete();
			}
		}
		docNumberDAO.delete(docNumber);

		return docNumber;
	}

	@Override
	public DocNumber modifyDocNumber(String id, String docWord,
			String yearPrefix, String yearPostfix, String orderPrefix,
			String orderPostfix) throws OaException {

		if (id == null) {
			throw new IllegalArgumentException("非法参数:ID不能为空！");
		}
		DocNumber docNumber = docNumberDAO.findById(id);
		if (docNumber == null) {
			throw new IllegalArgumentException("非法参数:ID不能为空！");
		}
		
		// 判断名称是否重复
		if (!docNumber.getDocWord().equals(docWord)) {
			isDocWordRepeat(docWord);
		}
		
		docNumber.setDocWord(docWord);
		docNumber.setYearPrefix(yearPrefix);
		docNumber.setYearPostfix(yearPostfix);
		docNumber.setOrderPrefix(orderPrefix);
		docNumber.setOrderPostfix(orderPostfix);
		docNumber.setDescription(docNumber.toString());
		docNumberDAO.saveOrUpdate(docNumber);

		return docNumber;
	}
	
	@Override
	public DocNumber txUpdateOrder(String id, int order, int year) {

		if (id == null) {
			throw new IllegalArgumentException("非法参数:ID不能为空！");
		}
		DocNumber docNumber = docNumberDAO.findById(id);
		if (docNumber == null) {
			throw new IllegalArgumentException("非法参数:ID不能为空！");
		}
		
		docNumber.setCurrentYear(year);
		docNumber.setOrderNumber(order);
		docNumber.setDescription(docNumber.toString());
		docNumberDAO.saveOrUpdate(docNumber);

		return docNumber;
	}

	@Override
	public DocNumber getDocNumber(String id) {
		return docNumberDAO.findById(id);
	}

	@Override
	public void addHeadTemplate(String id, Attachment am) {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		DocNumber docNumber = docNumberDAO.findById(id);
		if (docNumber == null) {
			throw new IllegalArgumentException();
		}

		Attachment headTemplate = docNumber.getHeadTemplate();

		if (headTemplate != null) {
			// 删除文件
			File f = FileDispatcher.findFile(FileDispatcher.getAbsPath(headTemplate.getFilePath()));
			if (f != null) {
				f.delete();
			}
			docNumberDAO.delete(headTemplate);
		}
		docNumber.setHeadTemplate(am);
		docNumberDAO.saveOrUpdate(docNumber);

	}

	public void setDocNumberDAO(IDocNumberDAO docNumberDAO) {
		this.docNumberDAO = docNumberDAO;
	}
}
