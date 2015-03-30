package org.eapp.util.web.upload;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

/**
 * 文件工具类
 * 对文件的拷贝、删除、移动等
 * @author 卓诗垚
 * @version May 8, 2007
 */
public final class FileUtil {
	
	/**
	 *  不需要被创建实例
	 */
	private FileUtil() {
		
	}
	
	/**
	 * 移动文件
	 * @param targetFile 源文件
	 * @param toFile 目标文件
	 * @return 文件大小
	 * @throws Exception 移动异常
	 */
	public static long moveFile(File targetFile, File toFile) throws Exception {
		return moveFile(targetFile, toFile, 0);
	}
	
	/**
	 * 移动文件
	 * @param targetPath 源文件
	 * @param toPath 目标文件
	 * @return 文件大小
	 * @throws Exception 移动异常
	 */
	public static long moveFile(String targetPath, String toPath)
			throws Exception {
		return moveFile(new File(targetPath), new File(toPath), 0);
	}
	
    /**
     * 移动文件
     * @param targetPath 源文件
     * @param toPath 目标文件
     * @param maxLength 文件限大小限制
     * @return 文件大小
     * @throws Exception 移动异常
     */
	public static long moveFile(String targetPath, String toPath, long maxLength)
			throws Exception {
		return moveFile(new File(targetPath), new File(toPath), maxLength);
	}
	
	/**
	 * 移动文件
	 * @param targetFile 源文件
	 * @param toFile 目标文件
	 * @param maxLength 文件大小限制（0为不限）
	 * @return 文件大小
	 * @throws Exception  移动异常
	 */
	public static long moveFile(File targetFile, File toFile, long maxLength) throws Exception {
		if (targetFile == null || toFile == null) {
			return -1;
		}
		//文件长度
		long flength = targetFile.length();

		if (maxLength > 0 && flength > maxLength) {
			//超出限制抛出异常
			throw new Exception("上载文件大于" + maxLength / 1024 + "K!");
		}
		File parentFile = toFile.getParentFile();
        if (!parentFile.exists() && !parentFile.mkdirs()) {
        	//创建文件夹
    		throw new Exception("文件夹创建失败");
    	}
    	if (!targetFile.renameTo(toFile)) {
    		//移动文件失败抛出异常
    		throw new Exception("移动文件失败");
    	}

		return flength;
	}
	
	/**
	 * 拷贝文件
	 * @param targetFile 源文件
	 * @param toFile 目标文件
	 * @throws Exception  拷贝异常
	 */
	public static void copyFile(File targetFile, File toFile) throws Exception {
		copyFile(targetFile, toFile, 1024);
	}
	
	/**
	 * 拷贝文件
	 * @param targetFile 源文件
	 * @param toFile 目标文件
	 * @param buffer 缓冲大小
	 * @throws Exception 拷贝异常
	 */
	public static void copyFile(File targetFile, File toFile, int buffer) throws Exception {
		if (targetFile == null || toFile == null) {
			throw new IllegalArgumentException("参数不正确");
		}
		if (!targetFile.isFile()) {
			//目录不是文件
			throw new Exception("源文件不存在");
		}
		
		if (toFile.exists()) {
			//存在则删除
			toFile.delete();
		} else {
			File parentFile = toFile.getParentFile();
	        if (!parentFile.exists() && !parentFile.mkdirs()) {
	    		throw new Exception("文件夹创建失败");
	    	}
	        toFile.createNewFile();
		}
		
		FileOutputStream fos = null;
		FileInputStream fis = null;
		try {
			fos = new FileOutputStream(toFile);
			fis = new FileInputStream(targetFile);
			//缓冲办输出字节
			byte[] data = new byte[buffer];
			//逐步输出数据
			int i = 0;
			while ((i = fis.read(data)) > 0) {
				fos.write(data, 0, i);
			}
			fos.flush();
		} finally {
			//关闭流
			fos.close();
			fis.close();
		}
	}
	
	/**
	 * 拷贝文件夹
	 * @param targetDir 源目录
	 * @param toDir 目标目录
	 * @throws Exception  拷贝异常
	 */
	public static void copyDir(File targetDir, File toDir) throws Exception {
		copyDir(targetDir, toDir, 1024);
	}
	
	/**
	 * 拷贝文件夹
	 * @param targetDir 源目录
	 * @param toDir 目标目录
	 * @param buffer 缓冲大小
	 * @throws Exception 拷贝异常
	 */
	public static void copyDir(File targetDir, File toDir, int buffer) throws Exception {
		if (targetDir == null || toDir == null) {
			throw new IllegalArgumentException("参数不正确");
		}
		if (!toDir.exists() && !toDir.mkdirs()) {
			//文件夹创建失败
    		throw new Exception("文件夹创建失败");
    	}
		File[] fs = targetDir.listFiles();
		if (fs == null || fs.length == 0) {
			//目录下无文件
			return;
		}
		for (File f : fs) {
			//逐个拷贝
			copyFile(f, new File(toDir, f.getName()), buffer);
		}
	}
	
	/**
	 * 把文件写入硬盘
	 * @param fileFullName 保存文件路径
	 * @param in 源文件流
	 * @throws IOException 流异常
	 */
	public static void saveFile(String fileFullName, InputStream in) throws IOException {
		saveFile(fileFullName, in, false);
	}
	
	/**
	 * 把文件写入硬盘
	 * @param fileFullName 保存文件路径
	 * @param data 源文件字节数组
	 * @throws IOException 流异常
	 */
	public static void saveFile(String fileFullName, byte[] data) throws IOException {
		saveFile(fileFullName, data, false);
	}
	
	/**
	 * 把文件写入硬盘
	 * @param fileFullName 保存文件路径
	 * @param data 源文件字节数组
	 * @param isWatermark 是否添加水印
	 * @throws IOException 流异常
	 */
	public static void saveFile(String fileFullName, byte[] data, boolean isWatermark) throws IOException {
		BufferedOutputStream out = null;
		try {
			File file = new File(fileFullName);
			//父目录
			File parentFile = file.getParentFile();
            if (!parentFile.exists() && !parentFile.mkdirs()) {
            	//文件夹创建失败
        		throw new IOException("文件夹创建失败");
        	}
            FileOutputStream fout = new FileOutputStream(file);
            
//            if (isWatermark) {
//            	//加水印并保存
//            	Watermark.createRandomMark(data, fout);
//            } else {
            	//直接保存
            	out = new BufferedOutputStream(fout);
            	out.write(data);
            	out.flush();
//            }
		} finally {
			if (out != null) {
        		out.close();
        	}
		}
	}
	
	/**
	 * 把文件写入硬盘
	 * @param fileFullName 保存文件路径
	 * @param in 源文件流
	 * @param isWatermark 是否添加水印
	 * @throws IOException 流异常
	 */
	public static void saveFile(String fileFullName, InputStream in, boolean isWatermark) throws IOException {
		BufferedOutputStream out = null;
		try {
			File file = new File(fileFullName);
			//父目录
			File parentFile = file.getParentFile();
            if (!parentFile.exists() && !parentFile.mkdirs()) {
            	//文件夹创建失败
        		throw new IOException("文件夹创建失败");
        	}
            out = new BufferedOutputStream(
        			new FileOutputStream(file));
//	        if (isWatermark) {
//	        	//加水印并保存
//	          	Watermark.createRandomMark(data, out);
//	        } else {
	        	//直接保存
	        	byte[] b = new byte[1024 * 10];
	        	int length = 0;
		    	while ((length = in.read(b)) > 0) {
		        	out.write(b, 0, length);
		    	}
//	        }

		} finally {
        	if (in != null) {
        		in.close();
        	}
        	if (out != null) {
        		out.close();
        	}
        }
	}
	
	
	/**
	 * 删除给定文件及该文件所相关的附属文件（文件名前缀一样）
	 * @param absPath  upload/news/img/9GZJO8QN1QWX.jpg
	 */
	public static void delSimilarFiles(String absPath) {
		if (absPath == null) {
			return;
		}
		//规范路径分割符
		String path = absPath.replaceAll("\\\\", "/");
		int index = path.lastIndexOf('/');
		if (index < 0) {
			return;
		}
		//截取文件名
		String fileName = path.substring(index + 1);
		File f = FileDispatcher.findFile(path);
		if (f == null) {
			return;
		} else {
			//查找目录文件
			File[] fs = f.getParentFile().listFiles(new PrefixNameFilter(fileName));
			if (fs == null) {
				return;
			}
			for (int i = 0; i < fs.length; i++) {
				//逐个删除
				fs[i].delete();
			}
		}
	}
	
	/**
	 * 删除一个相对目录下的所有文件
	 * @param absDir upload/news/img/
	 */
	public static void delAbsDir(String absDir) {
		//定位到真实路径
		List<File> dirList = FileDispatcher.findDirs(absDir);
		for (File f : dirList) {
			delDir(f);
		}
	}
	
	/**
	 * 删除一个目录下的所有文件
	 * @param dir 目录
	 */
	public static void delFiles(File dir) {
		if (dir == null) {
			throw new IllegalArgumentException("参数不正确");
		}
		File[] fs = dir.listFiles();
		if (fs == null || fs.length == 0) {
			return;
		}
		for (File f : fs) {
			if (f.isDirectory()) {
				//如果是目录
				//递归删除
				delFiles(f);
			}
			//删除文件
			f.delete();
		}
	}
	
	/**
	 * 删除文件夹
	 * @param dir 目录
	 */
	public static void delDir(File dir) {
		//先删除文件
		delFiles(dir);
		//再删除文件夹
		dir.delete();
	}
	
	/**
	 * 清理修改时间在给定小时以前的临时文件
	 * @param hourAgo 小时
	 */
	public static void cleanTempFile(int hourAgo) {
		//当前时间
		Calendar c = Calendar.getInstance();
		//定位到目录时间
		c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY) - hourAgo);
		long t = c.getTimeInMillis();
		File f = new File(FileDispatcher.getTempDir());
		if (!f.exists() || !f.isDirectory()) {
			return;
		}
		File[] fs = f.listFiles();
		for (int i = 0; i < fs.length; i++) {
			//修改时间在两小时前
			if (fs[i].lastModified() < t) {
				if (fs[i].isDirectory()) { 
					//删除文件夹
					delDir(fs[i]);
				} else if (fs[i].isFile()) { 
					//删除文件
					fs[i].delete();
				}
			}
		}
	}
	
	/**
	 * 文件名过滤器，
	 * 根据图片的文件名过滤出前缀匹配的文件 ，
	 * 如：fileName为X7Z47MMN5THL.jpg
	 * 能过滤出：X7Z47MMN5THL_50_50.jpg，X7Z47MMN5THL_68_68.jpg
	 * @author zsy
	 * @version Feb 2, 2009
	 */
	static class PrefixNameFilter implements FilenameFilter {
		/**
		 * 过滤文件名
		 */
		private String fileName;
		/**
		 * 过滤文件名来构造
		 * @param fileName 过滤文件名
		 */
		public PrefixNameFilter(String fileName) {
			this.fileName = fileName;
		}

		public String getFileName() {
			return fileName;
		}


		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		/**
		 * 过滤文件名
		 * @param dir 要过滤的目录
		 * @param name 文件名
		 * @return true/false
		 */
		public boolean accept(File dir, String name) {
			//截取文件名，
			//有扩展名的去除
			int i = fileName.indexOf('.');
			String prefix = null;
			if (i > 0) {
				prefix = fileName.substring(0, i);
			} else {
				prefix = name;
			}
			//前缀一样返回true
			if (name.startsWith(prefix)) {
				return true;
			}
			return false;
		}
	}
}
