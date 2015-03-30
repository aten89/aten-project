/**
 * 
 */
package org.eapp.util.mail;

import java.io.File;
import java.net.URL;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.activation.URLDataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

/**
 * 邮件的发送信息
 * @author 卓诗垚
 * @version Dec 30, 2008
 */
public class MailMessage {
	/**
	 * 邮件标题
	 */
	private String subject;
	/**
	 * 接收者,
	 * 多个用“,”分开
	 */
	private String toAddress;
	/**
	 * 抄送者
	 */
	private String copyToAddress;
	/**
	 * 邮件内容
	 */
	private Multipart multiparts;
	
	/**
	 * 构造邮件信息
	 */
	public MailMessage() {
		multiparts = new MimeMultipart();
	}
	
	/**
	 * 构造邮件信息
	 * @param toAddress 收件人，
	 * 		多个用“,”分开
	 * @param subject 邮件标题
	 */
	public MailMessage(String toAddress, String subject) {
		this();
		this.subject = subject;
		this.toAddress = toAddress;
	}
	
	/**
	 * 构造邮件信息
	 * @param toAddress 收件人，多个用“,”分开
	 * @param subject 邮件标题
	 * @param content 邮件内容
	 */
	public MailMessage(String toAddress, String subject, String content) {
		//默认指定mimeType
		this(toAddress, subject, content, "text/html;charset=gbk");
	}
	
	/**
	 * 构造邮件信息
	 * @param toAddress 收件人，多个用“,”分开
	 * @param subject 邮件标题
	 * @param content 邮件内容
	 * @param mimeType 邮件类型
	 */
	public MailMessage(String toAddress, String subject, String content, String mimeType) {
		this(toAddress, subject);
		//添加邮件内容
		MimeBodyPart mbp = new MimeBodyPart();
		try {
			mbp.setContent(content, mimeType);
			multiparts.addBodyPart(mbp);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 添加内容
	 * @param content 邮件内容
	 * @param mimeType 邮件类型
	 */
	public void addContent(String content, String mimeType) {
		//添加邮件内容
		MimeBodyPart mbp = new MimeBodyPart();
		try {
			mbp.setContent(content, mimeType);
			multiparts.addBodyPart(mbp);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 添加文本内容
	 * @param text 邮件内容
	 */
	public void addText(String text) {
		//添加文本内容
		MimeBodyPart mbp = new MimeBodyPart();
		try {
			mbp.setText(text);
			multiparts.addBodyPart(mbp);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 添加二进制附件
	 * @param date 二进制附件
	 * @param attachmentName 附件名
	 */
	public void addAttachment(byte[] date, String attachmentName) {
		if (date == null) {
			return;
		}
		//添加二进制附件
		MimeBodyPart mbp = new MimeBodyPart();
		try {
			ByteArrayDataSource fileds = new ByteArrayDataSource(date, "application/*");
			mbp.setDataHandler(new DataHandler(fileds));
			//附件名
			mbp.setFileName(attachmentName);
			multiparts.addBodyPart(mbp);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 添加文件附件
	 * @param file 附件文件
	 * @param attachmentName 附件名称
	 */
	public void addAttachment(File file, String attachmentName) {
		if (file == null) {
			return;
		}
		//添加文件附件
		MimeBodyPart mbp = new MimeBodyPart();
		try {
			FileDataSource files = new FileDataSource(file);
			mbp.setDataHandler(new DataHandler(files));
			//附件名
	        mbp.setFileName(attachmentName);
	        multiparts.addBodyPart(mbp);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 从URL添加附件
	 * @param url URL附件
	 * @param attachmentName 附件名称
	 */
	public void addAttachment(URL url, String attachmentName) {
		if (url == null) {
			return;
		}
		//从URL添加附件
		MimeBodyPart mbp = new MimeBodyPart();
		try {
			URLDataSource urls = new URLDataSource(url);
			mbp.setDataHandler(new DataHandler(urls));
			//附件名
	        mbp.setFileName(attachmentName);
	        multiparts.addBodyPart(mbp);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取属性
	 * @return 邮件标题
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * 设置属性
	 * @param subject 邮件标题
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * 获取属性
	 * @return 接收者,多个用“,”分开
	 */
	public String getToAddress() {
		return toAddress;
	}

	/**
	 * 设置属性
	 * @param toAddress 接收者,多个用“,”分开
	 */
	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	/**
	 * 获取属性
	 * @return 抄送者
	 */
	public String getCopyToAddress() {
		return copyToAddress;
	}

	/**
	 * 设置属性
	 * @param copyToAddress 抄送者
	 */
	public void setCopyToAddress(String copyToAddress) {
		this.copyToAddress = copyToAddress;
	}

	/**
	 * 获取属性
	 * @return 邮件内容
	 */
	public Multipart getMultiparts() {
		return multiparts;
	}

	/**
	 * 设置属性
	 * @param multiparts 邮件内容
	 */
	public void setMultiparts(Multipart multiparts) {
		this.multiparts = multiparts;
	}
}
