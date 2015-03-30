package org.eapp.oa.system.hbean;

import java.util.Date;

import org.eapp.util.web.upload.FileDispatcher;


/**
 * 附件实体
 */
public class Attachment implements java.io.Serializable {

    // Fields

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 5969745445187949970L;
    /**
     * ID_,VARCHAR2(36),不能为空 --主键ID
     */
    private String id;
    /**
     * FileExt_,VARCHAR2(36) --文件类型
     */
    private String fileExt;
    /**
     * FilePath_,VARCHAR2(512) --文件路径
     */
    private String filePath;
    /**
     * DisplayName_,VARCHAR2(128) --显示名称
     */
    private String displayName;
    /**
     * Size_,INTEGER --文件大小
     */
    private Long size;
    /**
     * UploadDate_,TIMESTAMP --上传时间
     */
    private Date uploadDate;

    // Constructors

    /** default constructor */
    public Attachment() {
    }

    /**
     * 构造方法
     * 
     * @param fileName 文件名
     * @param sizes 大小
     */
    public Attachment(String fileName, Long sizes) {
        this.size = sizes;
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

    /**
     * 构造方法
     * 
     * @param ext 文件后缀
     * @param path 文件路径
     * @param displayNameShow 显示名称
     * @param sizes 文件大小
     * @param uploadTime 上传时间
     */
    public Attachment(String ext, String path, String displayNameShow, Long sizes, Date uploadTime) {
        this.fileExt = ext;
        this.filePath = path;
        this.displayName = displayNameShow;
        this.size = sizes;
        this.uploadDate = uploadTime;
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

    public String getFullPath() {
        return FileDispatcher.getConfig().getSavePath() + FileDispatcher.getAbsPath(filePath);
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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        int num = 0;
        if (id == null) {
            num = 0;
        } else {
            num = id.hashCode();
        }
        result = prime * result + num;
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Attachment other = (Attachment) obj;
        if (id == null) {
            // if (other.id != null)
            return false;
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

}