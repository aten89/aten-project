/**
 * 
 */
package org.eapp.util.mail;

import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.ConcurrentLinkedQueue;

import javax.mail.MessagingException;

/**
 * Java 邮件发送代理
 * 支持后台线程发送
 * 可配置多个备用发送器，自动尝试可用的邮件服务器
 * 
 * @author 卓诗垚
 * @version Aug 5, 2007
 * 
 * modify at Dec 30, 2008
 */
public final class JMailProxy implements Runnable {
	/**
	 * 单例类
	 */
	private static JMailProxy mailPxy;
	/**
	 * 邮件服务列表
	 */
	private  List<JMail> jmails;
	/**
	 * 待发送邮件对列
	 */
	private ConcurrentLinkedQueue<MailMessage> messages = new ConcurrentLinkedQueue<MailMessage>();
	/**
	 * 后台线程是否运行
	 */
	private boolean isRunning = false;
	
	/**
	 * 单例私有构造
	 */
	private JMailProxy() {
		
	}
	/**
	 * 获取单例
	 * @return 单例对象
	 */
	public static JMailProxy getInstance() {
		if (mailPxy == null) {
			mailPxy = new JMailProxy();
		}
		return mailPxy;
	}
	
	/**
	 * 配置多个邮件（JMail对像）
	 * @param emailConfigs 邮件配置列表
	 */
	public static void setMailConfigs(List<MailConfig> emailConfigs) {
		JMailProxy mp = getInstance();
		//初始化邮件服务列表
		mp.jmails = new ArrayList<JMail>();
		for (MailConfig mailConfig : emailConfigs) {
			//添加邮件服务
			mp.jmails.add(new JMail(mailConfig));
		}
	}
	
	/**
	 * 添加后台发送的邮件
	 * @param msg 邮件消息
	 */
	public void addMessage(MailMessage msg) {
		messages.offer(msg);
	}
	
	/**
	 *  发送邮件
	 * @param textMsg 邮件消息
	 * @throws MessagingException 消息异常
	 */
	public static void sendMail(MailMessage textMsg
			) throws MessagingException {
		JMailProxy mp = getInstance();
		if (mp.jmails == null) {
			//无发送邮件服务
			throw new MessagingException("email配置出错,不能发送邮件");
		}
		
		MessagingException ex = null;
		for (JMail sender : mp.jmails) {
			try {
				//发送
				sender.send(textMsg);
				//有一个发送成功直接返回
				return;
			} catch (MessagingException e) {
				ex = e;
				//出错使用备用邮件服务器发送
				continue;
			}
		}
		//若都没一个发送成功，抛出捕获的异常
		if (ex != null) {
			throw ex;
		}
	}
	
	/**
	 * 后台发送
	 * @param textMsg 邮件消息
	 */
	public static void daemonSend(MailMessage textMsg) {
		JMailProxy mp = getInstance();
		mp.addMessage(textMsg);
		
		//当前不处于运行中，启动
		if (!mp.isRunning) {
			//启动新线程发送
			new Thread(mp).start();
		}
	}
	/**
	 * 线程运行实现
	 */
	public void run() {
		//标记为运行中
		isRunning = true;
		while (!messages.isEmpty()) {
			//消息队列不为空，循环运行
			//取得第一个消息
			MailMessage textMsg = messages.poll();
			try {
				//发送
				sendMail(textMsg);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
		//标记为运行结束
		isRunning = false;
	}
	
	/**
	 * 调试
	 * @param args 参数
	 */
//	public static void main(String[] args)  {
//		MailConfig c = new MailConfig();
//		c.addProperty("mail.smtp.host", "smtp.163.com");
//		c.addProperty("mail.smtp.auth", "true");
//		c.addProperty("mail.smtp.port", "25");
//		c.setPassword("xy123456");
//		c.setUsername("test");
//		c.setSendAddress("test@163.com");
//		
//		List<MailConfig> mcs = new ArrayList<MailConfig>();
//		mcs.add(c);
//		JMailProxy.setMailConfigs(mcs);
//		/*	如果是web应用，以上部分可以在应用启动时，从配置文件初始化*/
//		
//		System.out.println("start.....");
//		try {
//		//	JMailProxy.sendMail(new MailMessage("zhuoshiyao@163.com","邮件标题","<b>邮件内容</b>"));
//			
//			
//			MailMessage m = new MailMessage("zhuoshiyao@163.com", "邮件标题");
//			
//			//添加内容
//			m.addContent("<b>邮件内容</b>", "text/html;charset=gbk");
//		//	m.addText("邮件内容");
//			
//			//添加附件1
////			m.addAttachment(new java.io.File("D:/测试.txt"), new String("测试.txt".getBytes(),"ISO-8859-1"));
////			
////			//添加附件2
////			m.addAttachment(new java.net.URL("http://www.baidu.com/img/baidu_logo.gif"), "baidu.gif");
////			
////			//添加附件3
////			java.io.FileInputStream in = new java.io.FileInputStream(new java.io.File("D:/OA.txt"));
////			byte[] b = new byte[in.available()];
////			in.read(b);
////			m.addAttachment(b, "oa.txt");
//			
//			//发送
//			JMailProxy.sendMail(m);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		System.out.println("end....");
//	}
}
