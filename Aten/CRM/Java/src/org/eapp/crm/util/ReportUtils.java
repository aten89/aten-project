/**
 * 
 */
package org.eapp.crm.util;

import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * 对JASPERREPORT报表工具的封装
 * 提供导出HTML、PDF、XLS、打印
 * 等常用操作
 * 
 * @author zsy
 *
 */
public final class ReportUtils {
	/**
	 * 日志
	 */
	private static Logger logger = LoggerFactory.getLogger(ReportUtils.class);
	/**
	 * 报表模板编译缓存大小
	 */
	private static int rptTempCompileCacheSize = 100;
	/**
	 * 报表模板编译缓存时间
	 * 默认10秒
	 */
	private static int rptTempCompileCacheTime = 10000;
	/**
	 * 报表模板编译缓存
	 */
	private static Map<String, RptTempCacheItem> rptTempCompileCache = new HashMap<String, RptTempCacheItem>(rptTempCompileCacheSize);
	
	/**
	 * EXCEL导出器
	 */
	private static Class<?> xlsExporterClass;
	
	/**
     * 生成HTML格式
     * */
    public static final String EXPORT_HTML = "HTML";
    /**
     * 生成PDF格式
     * */
    public static final String EXPORT_PDF = "PDF";
    /**
     * 生成XLS格式
     * */
    public static final String EXPORT_XLS = "XLS";
    
    /**
     * 取得报表导出文件的扩展名
     * @param exportType 导出类型
     * @return 扩展名
     */
    public static String getFileExtName(String exportType) {
    	if (EXPORT_HTML.equals(exportType)) {
			return ".html";
		} else if (EXPORT_PDF.equals(exportType)) {
			return ".pdf";
		} else if (EXPORT_XLS.equals(exportType)) {
			if (isExcelExpToXLSX()) {
				return ".xlsx";
			} else {
				return ".xls";
			}
		} else {
			throw new IllegalArgumentException("不支持导出类型：" + exportType);
		}
    }
    
    /**
     * 是否生成XLSX类型
     * @return TRUE/FALSE
     */
    public static boolean isExcelExpToXLSX() {
    	return xlsExporterClass != null && JRXlsxExporter.class.isAssignableFrom(xlsExporterClass);
    }

	/**
	 * 缓存内容类
	 * @author zsy
	 *
	 */
	private static class RptTempCacheItem {
		/**
		 * 构造
		 * @param jasperReport 模板编译后对象
		 */
		RptTempCacheItem(JasperReport jasperReport) {
			this.jasperReport = jasperReport;
			//当前时间
			cacheTime = System.currentTimeMillis();
		}
		JasperReport jasperReport;
		long cacheTime;
	}
	
	/**
	 * 添加报表模板编译缓存
	 * @param tempFileName 报表模板文件
	 * @param jasperReport 模板编译后对象
	 */
	private static void addRptTempCache(String tempFileName, JasperReport jasperReport) {
		if (rptTempCompileCache.size() > rptTempCompileCacheSize) {
			//缓存超出容量，清空
			rptTempCompileCache.clear();
//			System.out.println("clearCache:...1....");
		}
//		System.out.println("compileReport:...1...." + tempFileName);
		//添加缓存
		rptTempCompileCache.put(tempFileName, new RptTempCacheItem(jasperReport));
	}
	
	/**
	 * 获取报表模板编译缓存
	 * @param tempFileName 报表模板文件
	 * @return 模板编译后对象
	 */
	private static JasperReport getRptTempCache(String tempFileName) {
		
		RptTempCacheItem item = rptTempCompileCache.get(tempFileName);
		if (item != null) {
			//缓存不为空
			if (System.currentTimeMillis() - item.cacheTime < rptTempCompileCacheTime) {
				//缓存有效
				//返回
				return item.jasperReport;
			} else {
				//缓存失效
				//移除缓存
				rptTempCompileCache.remove(tempFileName);
			}
		}
		
		//无缓存或失效
		//直接从模板文件编译
		JasperReport jasperReport = compileReport(tempFileName);
		//编译后放入缓存
		addRptTempCache(tempFileName, jasperReport);
		//返回编译后模板
		return jasperReport;
	}
	
	/**
	 * 工具类，不需被实例化
	 */
	private ReportUtils() {
		
	}
	
	/**
	 * 解析报表模板中的参数
	 * @param tempFileName IREPORT制作的报表模板文件（.JRXML文件）
	 * @return 返回报表模板的参数列表
	 * @throws FileNotFoundException 
	 * @throws DocumentException 
	 */
	public static List<TempParam> readTempParams(String tempFileName) 
			throws FileNotFoundException, DocumentException {
		if (tempFileName == null) {
			return null;
		}
		List<TempParam> params = new ArrayList<TempParam>();
		//解析XML文件（.JRXML）
		//使用dom4j
		FileInputStream fin = new FileInputStream(tempFileName);
		InputSource xmlSource = new InputSource(fin);
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(xmlSource);
		//取得parameter节点
		@SuppressWarnings("unchecked")
		List<Element> parEles = document.getRootElement().elements("parameter");
		for (Element e : parEles) {
			if ("false".equals(e.attributeValue("isForPrompting"))) {
				//忽略不提示参数
				continue;
			}
			//读取name与class属性
			params.add(new TempParam(e.attributeValue("name"), e.attributeValue("class")));
		}
		
		return params;
	}
	
	/**
	 * 获取EXCEL导出器
	 * 如果为空，默认使用JExcelApiExporter
	 * @return EXCEL导出器
	 */
	private static JRXlsAbstractExporter getXlsExporter() {
		if (xlsExporterClass != null) {
			try {
				return (JRXlsAbstractExporter)xlsExporterClass.newInstance();
			} catch (Exception e) {
				logger.error("创建实例失败：" + xlsExporterClass, e);
			}
		}
		
		return new JExcelApiExporter();
	}
	
	/**
	 * 设置EXCEL导出器
	 * @param className 类名
	 */
	public static void setXlsExporterClass(String className) {
		try {
			xlsExporterClass = Class.forName(className);
			if (!JRXlsAbstractExporter.class.isAssignableFrom(xlsExporterClass)) {
				throw new ClassCastException();
			}
		} catch (Exception e) {
			logger.error("加载类失败：" + className, e);
		}
	}
	
	/**
	 * 编译模板
	 * @param tempFileName IREPORT制作的报表模板文件（.JRXML文件）
	 * @return 编译后的JasperReport对象
	 */
	public static JasperReport compileReport(String tempFileName)  {
		if (tempFileName == null) {
			return null;
		}
		JasperReport jasperReport = null;
		try {
			//模板的文件流
			FileInputStream fin = new FileInputStream(tempFileName);
			//转成jasperReport对象
			jasperReport = JasperCompileManager.compileReport(fin);
		} catch (IOException e) {
			logger.error("读取模板文件出错", e);
			throw new IllegalArgumentException(e);
		} catch (JRException e) {
			logger.error("编译模板文件出错", e);
			throw new IllegalArgumentException(e);
		}
		return jasperReport;
	}
	
	/**
	 * 编译模板到文件
	 * @param tempFileName IREPORT制作的报表模板文件（.JRXML文件）
	 * @param complieFileName 编译后的文件
	 */
	public static void compileReport(String tempFileName, String complieFileName)  {
		try {
			//编译成文件
			JasperCompileManager.compileReportToFile(tempFileName, complieFileName);
		} catch (JRException e) {
			logger.error("编译模板文件出错", e);
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * 填充模板JasperPrint对象，并序列化到文件
	 * @param jasperPrintFileName JasperPrint对象序列化文件名
	 * @param tempFileName 报表模板文件
	 * @param noPages 报表无数据时是否生成空报表
	 * @param parameters 报表运行参数
	 * @param conn 数据库连接
	 */
	public static void fillReport(String jasperPrintFileName, String tempFileName, boolean noPages,
			Map<String, Object> parameters, Connection conn) {
		JasperReport jasperReport = getRptTempCache(tempFileName);
		if (noPages) {
			jasperReport.setWhenNoDataType(WhenNoDataTypeEnum.NO_PAGES);
		}
		File destFile = getFile(jasperPrintFileName);
		try {
			//填充报表到文件
			JasperFillManager.fillReportToFile(jasperReport, destFile.getPath(), parameters, conn);
		} catch (JRException e) {
			logger.error("填充报表出错", e);
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * 判断是否为空报表
	 * @param jasperPrintFile JasperPrint对象序列化文件名
	 * @return TRUE/FALS
	 */
	public static boolean isNullReport(File jasperPrintFile) {
		//如果忽略空报表
        JasperPrint jasperPrint;
		try {
			jasperPrint = (JasperPrint)JRLoader.loadObject(jasperPrintFile);
			//当ignoreNullFile为true时任务生成JasperPrint时，设置WhenNoDataTypeEnum.NO_PAGES，
        	//所以无数据时是空白页，则JasperPrint中有页数为0，表示空报表。
			return jasperPrint.getPages().isEmpty();
		} catch (JRException e) {
			logger.error("填充报表出错", e);
		}
        return true;
	}
	
	/**
	 * 导出PDF
	 * @param destFileName 生成PDF文件的全路径名
	 * @param jasperReport 编译后的JasperReport对象
	 * @param conn 数据库连接
	 * @param parameters 报表运行参数
	 */
	public static void exportToPdf(String destFileName, JasperReport jasperReport,
			Connection conn, Map<String, Object> parameters) {
		try {
			File destFile = getFile(destFileName);
			//填充报表
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			JRExporter exporter = new JRPdfExporter();
			//设置导出参数
		    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		    exporter.setParameter(JRExporterParameter.OUTPUT_FILE, destFile);
		    //导出报表
		    exporter.exportReport();
		} catch (JRException e) {
			logger.error("报表运行出错", e);
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * 导出PDF
	 * @param destFileName 生成PDF文件的全路径名
	 * @param tempFileName 报表模板文件
	 * @param conn 数据库连接
	 * @param parameters 报表运行参数
	 */
	public static void exportToPdf(String destFileName, String tempFileName, 
			Connection conn, Map<String, Object> parameters) {
		JasperReport jasperReport = getRptTempCache(tempFileName);
		exportToPdf(destFileName, jasperReport, conn, parameters);
	}
	
	/**
	 * 导出PDF
	 * @param destFileName 生成PDF文件的全路径名
	 * @param jasperPrintFileName JasperPrint对象序列化文件名
	 */
	public static void exportToPdf(String destFileName, String jasperPrintFileName) {
		try {
			File destFile = getFile(destFileName);
			JRExporter exporter = new JRPdfExporter();
			//设置导出参数
		    exporter.setParameter(JRExporterParameter.INPUT_FILE_NAME, jasperPrintFileName);
		    exporter.setParameter(JRExporterParameter.OUTPUT_FILE, destFile);
		    //导出报表
		    exporter.exportReport();
		} catch (JRException e) {
			logger.error("报表运行出错", e);
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * 导出XLS
	 * @param destFileName 生成XLS文件的全路径名
	 * @param jasperReport 编译后的JasperReport对象
	 * @param conn 数据库连接
	 * @param parameters 报表运行参数
	 */
	public static void exportToXls(String destFileName, JasperReport jasperReport, 
			Connection conn, Map<String, Object> parameters) {
		try {
			
			File destFile = getFile(destFileName);
			//填充报表
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			JRExporter exporter = getXlsExporter();
			//设置导出参数
	        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
	        exporter.setParameter(JRExporterParameter.OUTPUT_FILE, destFile);
	       
	        exporter.setParameter(JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
	        exporter.setParameter(JRXlsAbstractExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.TRUE);
	        exporter.setParameter(JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
	        exporter.setParameter(JRXlsAbstractExporterParameter.CREATE_CUSTOM_PALETTE, Boolean.TRUE);
	        //导出报表
	        exporter.exportReport();
		} catch (JRException e) {
			logger.error("报表运行出错", e);
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * 导出XLS
	 * @param destFileName 生成XLS文件的全路径名
	 * @param tempFileName 报表模板文件
	 * @param conn 数据库连接
	 * @param parameters 报表运行参数
	 */
	public static void exportToXls(String destFileName, String tempFileName, 
			Connection conn, Map<String, Object> parameters) {
		JasperReport jasperReport = getRptTempCache(tempFileName);
		exportToXls(destFileName, jasperReport, conn, parameters);
	}
	
	/**
	 * 导出XLS
	 * @param destFileName 生成XLS文件的全路径名
	 * @param jasperPrintFileName JasperPrint对象序列化文件名
	 */
	public static void exportToXls(String destFileName, String jasperPrintFileName) {
		try {
			File destFile = getFile(destFileName);
			JRExporter exporter = getXlsExporter();
			//设置导出参数
	        exporter.setParameter(JRExporterParameter.INPUT_FILE_NAME, jasperPrintFileName);
	        exporter.setParameter(JRExporterParameter.OUTPUT_FILE, destFile);
	       
	        exporter.setParameter(JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
	        exporter.setParameter(JRXlsAbstractExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.TRUE);
	        exporter.setParameter(JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
	        exporter.setParameter(JRXlsAbstractExporterParameter.CREATE_CUSTOM_PALETTE, Boolean.TRUE);
	        //导出报表
	        exporter.exportReport();
		} catch (JRException e) {
			logger.error("报表运行出错", e);
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * 导出HTML
	 * @param destFileName 生成HTML文件的全路径名
	 * @param jasperReport 编译后的JasperReport对象
	 * @param conn 数据库连接
	 * @param parameters 报表运行参数
	 */
	public static void exportToHtml(String destFileName, JasperReport jasperReport, 
			Connection conn, Map<String, Object> parameters) {
		try {
			File destFile = getFile(destFileName);
			//填充报表
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			JRExporter exporter = new JRHtmlExporter();
			//设置导出参数
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE, destFile);
			//解决生成HTML报表缩小问题
			exporter.setParameter(JRHtmlExporterParameter.SIZE_UNIT, "pt");
//			exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, HTML_DEFAULT_ENCODING);
			//导出报表
			exporter.exportReport();
		} catch (JRException e) {
			logger.error("报表运行出错", e);
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * 导出HTML
	 * @param destFileName 生成HTML文件的全路径名
	 * @param tempFileName 报表模板文件
	 * @param conn 数据库连接
	 * @param parameters 报表运行参数
	 */
	public static void exportToHtml(String destFileName, String tempFileName, 
			Connection conn, Map<String, Object> parameters) {
		JasperReport jasperReport = getRptTempCache(tempFileName);
		exportToHtml(destFileName, jasperReport, conn, parameters);
	}
	
	/**
	 * 导出HTML
	 * @param destFileName 生成HTML文件的全路径名
	 * @param jasperPrintFileName JasperPrint对象序列化文件名
	 */
	public static void exportToHtml(String destFileName, String jasperPrintFileName) {
		try {
			File destFile = getFile(destFileName);
			JRExporter exporter = new JRHtmlExporter();
			//设置导出参数
			exporter.setParameter(JRExporterParameter.INPUT_FILE_NAME, jasperPrintFileName);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE, destFile);
			//解决生成HTML报表缩小问题
			exporter.setParameter(JRHtmlExporterParameter.SIZE_UNIT, "pt");
//			exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, HTML_DEFAULT_ENCODING);
			//导出报表
			exporter.exportReport();
		} catch (JRException e) {
			logger.error("报表运行出错", e);
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * 输出到打印机
	 * @param printerName 打印机名称
	 * @param jasperReport 编译后的JasperReport对象
	 * @param conn 数据库连接
	 * @param parameters 报表运行参数
	 */
	public static void toPrint(String printerName, JasperReport jasperReport, 
			Connection conn, Map<String, Object> parameters) {
		try {
			//填充报表
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
			
			JRExporter je = new JRPrintServiceExporter();  
			//设置导出参数
			je.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, getPrintService(printerName));  
			je.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, false);  
			je.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, false);
			je.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			//导出报表
			je.exportReport();
		} catch (JRException e) {
			logger.error("报表运行出错", e);
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * 输出到打印机
	 * @param printerName 打印机名称
	 * @param tempFileName 报表模板文件
	 * @param conn 数据库连接
	 * @param parameters 报表运行参数
	 */
	public static void toPrint(String printerName, String tempFileName, 
			Connection conn, Map<String, Object> parameters) {
		JasperReport jasperReport = getRptTempCache(tempFileName);
		toPrint(printerName, jasperReport, conn, parameters);
	}
	
	/**
	 * 输出到打印机
	 * @param printerName 打印机名称
	 * @param jasperPrintFileName JasperPrint对象序列化文件名
	 */
	public static void toPrint(String printerName, String jasperPrintFileName) {
		try {
			JRExporter je = new JRPrintServiceExporter();  
			//设置导出参数
			je.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, getPrintService(printerName));  
			je.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, false);  
			je.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, false);
			je.setParameter(JRExporterParameter.INPUT_FILE_NAME, jasperPrintFileName);
			//导出报表
			je.exportReport();
		} catch (JRException e) {
			logger.error("报表运行出错", e);
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * 获取服务器打印机对象，打印机名称为空时使用默认打印机
	 * @param printerName 打印机名
	 * @return 打印服务
	 */
	private static PrintService getPrintService(String printerName) {
		PrintService pser = null;
		if (printerName == null) {
			//定位默认的打印服务
			pser = PrintServiceLookup.lookupDefaultPrintService();
		} else {
			//根据名字查找打印机
			PrintService[] pss = PrinterJob.lookupPrintServices();  
			for (int i = 0; i < pss.length; i++) {  
				if (printerName.equalsIgnoreCase(pss[i].getName())) {
					pser = pss[i];
					break;
				}
			} 
		}
		//返回查找的打印服务
		return pser;
	}
	
	/**
	 * 取得文件对象，如果父目录不存在则创建
	 * @param fileName 文件的全路径名
	 * @return File对象
	 */
	private static File getFile(String fileName) {
		File file = new File(fileName);
		//最得父目录
		File pfile = file.getParentFile();
        if (!pfile.exists()) {
        	//创建文件夹
        	pfile.mkdirs();
    	}
        return file;
	}
	
	/**
	 * 报表模板里解析出来的报表参数信息
	 * @author zsy
	 *
	 */
	public static class TempParam {
		/**
		 * 参数名称
		 */
		private String paramName;
		/**
		 * 参数类型
		 */
		private String className;
		
		/**
		 * 构造模板参数
		 * @param paramName 参数名
		 * @param className 类型
		 */
		public TempParam(String paramName, String className) {
			this.paramName = paramName;
			this.className = className;
		}
		public String getParamName() {
			return paramName;
		}
		/**
		 * 设置属性
		 * @param paramName 参数名
		 */
		public void setParamName(String paramName) {
			this.paramName = paramName;
		}
		public String getClassName() {
			return className;
		}
		/**
		 * 设置属性
		 * @param className 类型
		 */
		public void setClassName(String className) {
			this.className = className;
		}
		/**
		 * 自动生成
		 * hashCode
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((className == null) ? 0 : className.hashCode());
			result = prime * result
					+ ((paramName == null) ? 0 : paramName.hashCode());
			return result;
		}
		
		/**
		 * 自动生成
		 * equals
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
			TempParam other = (TempParam) obj;
			if (className == null) {
				if (other.className != null) {
					return false;
				}
			} else if (!className.equals(other.className)) {
				return false;
			}
			if (paramName == null) {
				if (other.paramName != null) {
					return false;
				}
			} else if (!paramName.equals(other.paramName)) {
				return false;
			}
			return true;
		}
		
	}
	
//	public static void main(String[] args) throws Exception {
//		Class.forName("oracle.jdbc.driver.OracleDriver");
//		Connection conn = java.sql.DriverManager.getConnection(
//			"jdbc:oracle:thin:@127.0.0.1:1521:ORCL", "eb_metadb", "eb_metadb");
//		Map<String, Object> parameters = new java.util.HashMap<String, Object>();
////		fillReport("F:/report2.jrxml", "F:/report2.jrprint", parameters, conn);
////		exportToHtml("F:/aa.html", "F:/report2.jrxml",conn, parameters);
////		toPrint(null, "F:/report2.jrxml",conn, parameters);
////		toPrint(null, "F:/report2.jrprint");
//	}
	
}
