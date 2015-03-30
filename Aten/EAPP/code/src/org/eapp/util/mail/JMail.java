/**
 * 
 */
package org.eapp.util.mail;

import java.util.Date;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Java 发送邮件
 * @author 卓诗垚
 * @version Aug 5, 2007
 * 
 * modify at Dec 30, 2008
 */
public class JMail {
	/**
	 * 邮件配置信息
	 */
	private MailConfig mailConfig;
    
	/**
	 * 默认构造
	 */
    public JMail() {
    	
    }
    /**
     * 通过邮件配置构造
     * @param mailConfig 邮件配置
     */
	public JMail(MailConfig mailConfig) {
		this.mailConfig = mailConfig;
	}
	
	/**
	 * 获取属性
	 * @return 邮件配置信息
	 */
	public MailConfig getMailConfig() {
		return mailConfig;
	}
	
	/**
	 * 设置属性
	 * @param mailConfig 邮件配置信息
	 */
	public void setMailConfig(MailConfig mailConfig) {
		this.mailConfig = mailConfig;
	}
	
	/**
	 * 直接发送邮件
	 * @param mailMsg 邮件信息
	 * @throws MessagingException 消息异常
	 */
	public void send(MailMessage mailMsg) throws MessagingException {
		if (mailMsg == null) {
			//无邮件消息
			return;
		}
		if (mailConfig == null) {
			//未配置邮件
			throw new IllegalArgumentException("未配置邮件");
		}
		//取得邮件发送Session
		Session mailSession = Session.getInstance(
				mailConfig.getProps(), new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailConfig.getUsername(), mailConfig.getPassword());
            }
		});
		//初始化邮件发送的消息体
        Message msg = new MimeMessage(mailSession);
        try {
        	//设置发件人
            msg.setFrom(new InternetAddress(mailConfig.getSendAddress()));

            if (mailMsg.getToAddress() != null) {
            	//设置收件人
	            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(
	            		mailMsg.getToAddress()));
            }
            if (mailMsg.getCopyToAddress() != null) {
            	//设置抄送人
	            msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(
	            		mailMsg.getCopyToAddress()));
            }
            //设置邮件标题
            msg.setSubject(mailMsg.getSubject());
            //设置发送时间
            msg.setSentDate(new Date());
            //设置邮件内容
            msg.setContent(mailMsg.getMultiparts());
            
            //发送
            Transport.send(msg);
        } catch (AddressException e) {
        	e.printStackTrace();
            throw e;
        } catch (MessagingException e) {
            e.printStackTrace();
            throw e;
        }
	}
}

