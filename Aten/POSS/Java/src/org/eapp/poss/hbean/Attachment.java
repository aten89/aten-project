package org.eapp.poss.hbean;

import java.util.Date;

import org.eapp.util.web.upload.FileDispatcher;

/**
 * Attachment entity. @author MyEclipse Persistence Tools
 */

public class Attachment implements java.io.Serializable {

	// Fields

	/**
     * 
     */
    private static final long serialVersionUID = -6451139822313303570L;
    private String id;
	private String fileExt;
	private String filePath;
	private String displayName;
	private Long size;
	private Date uploadDate;

	// Constructors

	/** default constructor */
	public Attachment() {
	}

	public Attachment(String fileName, Long size) {
		this.size = size;
		this.uploadDate = new Date();
		
		if (fileName == null) {
			return;
		}
		int i = fileName.lastIndexOf(".");
		if (i < 0) {
			this.displayName = fileName;
		} else {
			this.displayName = fileName.substring(0, i);
			this.fileExt = fileName.substring(i);
		}
	}
	
	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFileExt() {
		return this.fileExt;
	}

	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Long getSize() {
		return this.size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public Date getUploadDate() {
		return this.uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}
	
	public String getFullPath() {
        return FileDispatcher.getConfig().getSavePath() + FileDispatcher.getAbsPath(filePath);
    }
	
	public String getAbsFilePath() {
        return FileDispatcher.getAbsPath(filePath);
    }

}