package org.eapp.oa.system.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import org.eapp.oa.system.hbean.Attachment;
import org.eapp.util.web.upload.FileDispatcher;
import org.eapp.util.web.upload.FileUtil;

/**
 * 附件操作
 * @author TimLin
 * @version 2009-05-14
 */
public class AttachmentHelper {
	
	/**
	 * Copy Attachment
	 * @author TimLin 
	 * @return Attachment
	 * @param attachment 
	 * @param dir
	 */
	public static Attachment copyAttachment(Attachment attachment,String dir) throws IOException {
		if(attachment != null){
			//获得该正文附件的相对路径
			String path = FileDispatcher.getAbsPath(attachment.getFilePath());
			//获得文件
			File file = FileDispatcher.findFile(path);
        	FileInputStream in = new FileInputStream(file);
        	Attachment attachmentCopy = new Attachment();
        	attachmentCopy.setDisplayName(attachment.getDisplayName());
        	attachmentCopy.setFileExt(attachment.getFileExt());
        	String attachmentPath = dir + SerialNoCreater.createUUID() + attachment.getFileExt();
        	attachmentCopy.setFilePath(attachmentPath);
        	attachmentCopy.setSize(attachment.getSize());
        	attachmentCopy.setUploadDate(new Date());
        	FileUtil.saveFile(FileDispatcher.getSaveDir(FileDispatcher.getAbsPath(attachmentPath)), in);
        	return attachmentCopy;
		}return null;
	}
}
