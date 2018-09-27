package com.sunnsoft.util.mail;

import com.sunnsoft.util.MimeTypeDictionary;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author 林宇民 线程不安全，改进中。
 */
public class SimpleMail {

	public static void main(String[] args) throws AddressException, Exception {
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		Properties props = new Properties();
	  final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	 
	  props.put("mail.transport.protocol", "smtp");
	  
	  props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
	  props.put("mail.smtp.socketFactory.fallback", "false");
	  props.put("mail.smtp.port", 465 + "");
	  props.put("mail.smtp.socketFactory.port", 465 + "");
	  
	  props.put("mail.smtp.host", "smtp.exmail.qq.com");
	  props.put("mail.smtp.auth", "true");
		// ExecutorService e = Executors.newFixedThreadPool(10);
		// byte[] codes="<span style=\"color:red\">今晚过来吃饭</span><br/><div
		// style=\"color:blue\">OK?</div>".getBytes("GBK");
		// System.out.println(ArrayUtils.toString(codes));
		for (int i = 0; i < 5; i++) {
			SimpleMail sms = new SimpleMail();

			sms.setSessionDebug(true);

			sms.setSessionProperties(props);

			// sms.setExecutor(e);

			// sms.setSessionJndiName("java:comp/env/mail/session");
			sms.setSmtpUser("username");
			sms.setSmtpPassword("password");
			sms.setToAddressOfString("mmg<xxx@qq.com>");
			sms.addToAddress("mmg@gmail.com");
			sms.setFromAddress("督办邮箱<xxx@xxx.com>");
			// sms.setFromAddress("xxx<xxx@21cn.com>");
			sms.setContentType("text/html");
			StringBuffer sb = new StringBuffer();
			// InputStreamReader f = new InputStreamReader(
			// new FileInputStream(
			// "D:\\workspace_poweroa\\PowerOA3CNCOA\\WebContent\\WEB-INF\\mail-templates\\shenpi.tle"),
			// "GBK");
			// char[] chars = new char[1024];
			// int read = 0;
			// while ((read = f.read(chars)) != -1) {
			// sb.append(chars, 0, read);
			// }
			// f.close();

			sms.setContent(sb.toString());
			System.out.println(sms.content);
			sms.setSessionDebug(true);
			// sms.setCcAddressOfString(",");
			// sms.send();
			sms.send();
			System.out.println(sms.isSuccess());
			System.out.println(sms.isFail());
		}
		// e.shutdown();
	}

	private LinkedHashMap<String, InputStream> attachmentInputStreams = new LinkedHashMap<String, InputStream>();

	private Address[] bccAddress = new Address[] {};

	private Address[] ccAddress = new Address[] {};

	private String charsetEocode = "GBK";

	private String content;

	private String contentType = "text/plain";

	private String encodingType = "B";

	private boolean fail = false;

	private Address fromAddress;

	private Hashtable initContextProperties = new Hashtable();

	Log logger = LogFactory.getLog(SimpleMail.class);

	private Address[] replyToAddress = new Address[] {};

	private Address sender;

	private SendMailCallback sendMailCallback;

	private boolean sessionDebug = false;

	private String sessionJndiName;

	private Properties sessionProperties = new Properties();

	private String smtpPassword;

	private String smtpUser;

	private String subject;

	private boolean success = false;

	private boolean throwRuntimeException = true;

	private Address[] toAddress = new Address[] {};

	private String mailDomain;

	public SimpleMail() {

	}

	public SimpleMail(String fromAddress, String smtpUser, String smtpPassword,
			String[] toAddress, String subject, String content,
			Properties sessionProperties) throws AddressException {
		super();
		this.fromAddress = parrseAddress(fromAddress);
		this.smtpUser = smtpUser;
		this.smtpPassword = smtpPassword;
		this.setToAddress(toAddress);
		this.subject = subject;
		this.content = content;
		this.sessionProperties = sessionProperties;
	}

	public void addAttachment(String fileName, InputStream in) {
		this.attachmentInputStreams.put(fileName, in);
	}

	private void failDone() {
		if (this.sendMailCallback != null)
			this.sendMailCallback.doInFail();
	}

	public String[] getBccAddress() {
		String[] result = new String[bccAddress.length];
		for (int i = 0; i < bccAddress.length; i++) {
			result[i] = bccAddress[i].toString();
		}
		return result;
	}

	public String[] getCcAddress() {
		String[] result = new String[ccAddress.length];
		for (int i = 0; i < ccAddress.length; i++) {
			result[i] = ccAddress[i].toString();
		}
		return result;
	}

	public String getContent() {
		return content;
	}

	public String getFromAddress() {
		return fromAddress == null ? null : fromAddress.toString();
	}

	public String[] getReplyToAddress() {
		String[] result = new String[replyToAddress.length];
		for (int i = 0; i < replyToAddress.length; i++) {
			result[i] = replyToAddress[i].toString();
		}
		return result;
	}

	public String getSender() {
		return sender == null ? null : sender.toString();
	}

	public Properties getSessionProperties() {
		return sessionProperties;
	}

	public String getSmtpPassword() {
		return smtpPassword;
	}

	public String getSmtpUser() {
		return smtpUser;
	}

	public String getSubject() {
		return subject;
	}

	public String[] getToAddress() {
		String[] result = new String[toAddress.length];
		for (int i = 0; i < toAddress.length; i++) {
			result[i] = toAddress[i].toString();
		}
		return result;
	}

	public boolean isFail() {
		return fail;
	}

	public boolean isSessionDebug() {
		return sessionDebug;
	}

	public boolean isSuccess() {
		return success;
	}

	public boolean isThrowRuntimeException() {
		return throwRuntimeException;
	}

	private Address parrseAddress(String address) throws AddressException {
		if (address.indexOf("<") != -1 && address.indexOf(">") != -1) {
			String email = address.substring(address.indexOf("<") + 1,
					address.indexOf(">")).trim();
			String personName = address.substring(0, address.indexOf("<"))
					.trim();
			try {
				return new InternetAddress(email, personName,
						this.charsetEocode);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		} else {
			return new InternetAddress(address);
		}
	}

	private Address[] parseAddresses(String address)
			throws UnsupportedEncodingException, AddressException {
		String[] addrs = address.split(",");
		Set set = new HashSet();
		for (int i = 0; i < addrs.length; i++) {
			if (!addrs[i].trim().equals("")) {
				set.add(addrs[i]);
			}
		}
		Address[] result = new Address[set.size()];
		int index = 0;
		for (Iterator iter = set.iterator(); iter.hasNext();) {
			String addr = (String) iter.next();
			result[index] = this.parrseAddress(addr);
			index++;
		}
		return result;
	}

	public void send() {
		Session mailSession = null;

		if (this.sessionJndiName != null && this.initContextProperties != null) {
			InitialContext initCtx = null;
			try {
				if (this.initContextProperties.size() > 0) {
					initCtx = new InitialContext(this.initContextProperties);
				} else {
					initCtx = new InitialContext();
				}
				mailSession = (Session) initCtx.lookup(this.sessionJndiName);
			} catch (NamingException e) {
				logger.error("can not lookup mail session", e);
			} finally {
				if (initCtx != null) {
					try {
						initCtx.close();
					} catch (NamingException e) {
						logger.error("can not close InitialContext", e);
					}
				}
			}
		} else {
			mailSession = Session.getInstance(sessionProperties,
					new Authenticator() {
						public PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(smtpUser,
									smtpPassword);// smpt user and password
						}
					});
		}
		try {
			mailSession.setDebug(sessionDebug);
			MimeMessage mms = new MimeMessage(mailSession);
			if (this.subject != null)
				mms.setSubject(MimeUtility.encodeText(this.subject,
						this.charsetEocode, this.encodingType));
			if (this.fromAddress != null)
				mms.setFrom(this.fromAddress);

			mms.addRecipients(javax.mail.Message.RecipientType.TO,
					this.toAddress);
			if (this.ccAddress.length > 0) {
				mms.addRecipients(javax.mail.Message.RecipientType.CC,
						ccAddress);
			}
			if (this.bccAddress.length > 0) {
				mms.addRecipients(javax.mail.Message.RecipientType.BCC,
						bccAddress);
			}

			if (this.replyToAddress.length > 0) {
				mms.setReplyTo(replyToAddress);
			}

			Multipart multipart = new MimeMultipart();

			if (this.content != null) {
				MimeBodyPart messageBodyPart = new MimeBodyPart();

				// messageBodyPart.setContent(this.content,"text/html;
				// charset="+File.);
				// messageBodyPart.setContentLanguage(new
				// String[]{this.charsetEocode});
				// System.out.println("测试");
				// System.out.println(System.getProperty("file.encoding"));
				// System.out.println(new String(
				// this.content.getBytes("ISO-8859-1"),"GBK"));
				messageBodyPart.setDataHandler(new DataHandler(
						new ByteArrayDataSource(this.content
								.getBytes(this.charsetEocode), contentType)));
				messageBodyPart.setHeader("Content-Type", this.contentType
						+ "; charset=" + this.charsetEocode);
				multipart.addBodyPart(messageBodyPart);
			}

			for (Iterator<String> iter = this.attachmentInputStreams.keySet()
					.iterator(); iter.hasNext();) {
				String fileName = iter.next();
				MimeBodyPart messageBodyPart = new MimeBodyPart();

				javax.activation.DataSource source = new ByteArrayDataSource(
						this.attachmentInputStreams.get(fileName),
						MimeTypeDictionary.getMimeType(fileName));
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(MimeUtility.encodeText(fileName));
				multipart.addBodyPart(messageBodyPart);

			}
			mms.setContent(multipart);
			if (this.sender != null)
				mms.setSender(this.sender);
			Transport.send(mms);
			this.success = true;
			successDone();
		} catch (AddressException e) {
			this.fail = true;
			failDone();
			if (this.sessionDebug)
				e.printStackTrace();
			if (logger.isWarnEnabled())
				logger.warn(e.getLocalizedMessage(), e);
			if (throwRuntimeException)
				throw new SendMailException(e);
		} catch (MessagingException e) {
			this.fail = true;
			failDone();
			if (this.sessionDebug)
				e.printStackTrace();
			if (logger.isWarnEnabled())
				logger.warn(e.getLocalizedMessage(), e);
			if (throwRuntimeException)
				throw new SendMailException(e);
		} catch (IOException e) {
			this.fail = true;
			failDone();
			if (this.sessionDebug)
				e.printStackTrace();
			if (logger.isWarnEnabled())
				logger.warn(e.getLocalizedMessage(), e);
			if (throwRuntimeException)
				throw new SendMailException(e);
		} catch (RuntimeException e) {
			this.fail = true;
			failDone();
			if (this.sessionDebug)
				e.printStackTrace();
			if (logger.isWarnEnabled())
				logger.warn(e.getLocalizedMessage(), e);
			if (throwRuntimeException)
				throw new SendMailException(e);
		} finally {

		}

	}

	/**
	 * 设置暗送用户
	 * 
	 * @param bccAddress
	 *            符合RFC
	 *            822格式的邮件地址列表，"name<email@abc.org>","email@abc.org"都是合法的邮件地址
	 *            ，前者会使邮件地址具有用户名。
	 * @throws AddressException
	 */
	public void setBccAddress(String[] bccAddress) throws AddressException {
		this.bccAddress = new Address[bccAddress.length];
		for (int i = 0; i < bccAddress.length; i++) {
			this.bccAddress[i] = this.parrseAddress(bccAddress[i]);
		}
	}

	/**
	 * 设置暗送用户
	 * 
	 * @param bccAddress
	 *            符合RFC
	 *            822格式的邮件地址列表，邮件地址之间使用","分隔。"name<email@abc.org>,email@abc.org"
	 *            是合法，会解析为2个地址。前一个地址有用户名
	 * @throws AddressException
	 * @throws UnsupportedEncodingException
	 */
	public void setBccAddressOfString(String bccAddress)
			throws AddressException, UnsupportedEncodingException {
		bccAddress = bccAddress.replace(';', ',');
		this.bccAddress = parseAddresses(bccAddress);
	}

	/**
	 * 设置抄送用户，可以不设置。
	 * 
	 * @param ccAddress
	 *            符合RFC
	 *            822格式的邮件地址列表，"name<email@abc.org>","email@abc.org"都是合法的邮件地址
	 *            ，前者会使邮件地址具有用户名。
	 * @throws AddressException
	 */
	public void setCcAddress(String[] ccAddress) throws AddressException {
		this.ccAddress = new Address[ccAddress.length];
		for (int i = 0; i < ccAddress.length; i++) {
			this.ccAddress[i] = parrseAddress(ccAddress[i]);
		}
	}

	/**
	 * 设置抄送用户，可以不设置。
	 * 
	 * @param ccAddress
	 *            符合RFC
	 *            822格式的邮件地址列表，邮件地址之间使用","分隔。"name<email@abc.org>,email@abc.org"
	 *            是合法，会解析为2个地址。前一个地址有用户名
	 * @throws AddressException
	 * @throws UnsupportedEncodingException
	 */
	public void setCcAddressOfString(String ccAddress) throws AddressException,
			UnsupportedEncodingException {
		ccAddress = ccAddress.replace(';', ',');
		this.ccAddress = parseAddresses(ccAddress);
	}

	public void setCharsetEocode(String charsetEocode) {
		this.charsetEocode = charsetEocode;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setEncodingType(String encodingType) {
		this.encodingType = encodingType;
	}

	/**
	 * 设置发送地址。注意不是smtp服务器验证账号，是邮件头中的发送邮件的地址。
	 * 
	 * @param fromAddress
	 *            符合RFC
	 *            822格式的邮件地址列表，"name<email@abc.org>","email@abc.org"都是合法的邮件地址
	 *            ，前者会使邮件地址具有用户名。
	 * @throws AddressException
	 * @throws UnsupportedEncodingException
	 */
	public void setFromAddress(String fromAddress) throws AddressException,
			UnsupportedEncodingException {
		this.fromAddress = parseAddresses(fromAddress)[0];
	}

	public void setInitContextProperties(Hashtable initContextProperties) {
		this.initContextProperties = initContextProperties;
	}

	/**
	 * 设置答复地址，一般用于回复邮件，邮件头中包含的答复地址，可以不设置。
	 * 
	 * @param replyToAddress
	 *            符合RFC
	 *            822格式的邮件地址列表，"name<email@abc.org>","email@abc.org"都是合法的邮件地址
	 *            ，前者会使邮件地址具有用户名。
	 * @throws AddressException
	 */
	public void setReplyToAddress(String[] replyToAddress)
			throws AddressException {
		this.replyToAddress = new Address[replyToAddress.length];
		for (int i = 0; i < replyToAddress.length; i++) {
			this.replyToAddress[i] = parrseAddress(replyToAddress[i]);
		}
	}

	/**
	 * 设置答复地址，一般用于回复邮件，邮件头中包含的答复地址，可以不设置。
	 * 
	 * @param replyToAddress
	 *            符合RFC
	 *            822格式的邮件地址列表，邮件地址之间使用","分隔。"name<email@abc.org>,email@abc.org"
	 *            是合法，会解析为2个地址。前一个地址有用户名
	 * @throws AddressException
	 * @throws UnsupportedEncodingException
	 */
	public void setReplyToAddressOfString(String replyToAddress)
			throws AddressException, UnsupportedEncodingException {
		replyToAddress = replyToAddress.replace(';', ',');
		this.replyToAddress = parseAddresses(replyToAddress);
	}

	public void setSender(String sender) throws AddressException,
			UnsupportedEncodingException {
		this.sender = parseAddresses(sender)[0];
	}

	public void setSendMailCallback(SendMailCallback sendMailCallback) {
		this.sendMailCallback = sendMailCallback;
	}

	public void setSessionDebug(boolean sessionDebug) {
		this.sessionDebug = sessionDebug;
	}

	public void setSessionJndiName(String sessionJndiName) {
		this.sessionJndiName = sessionJndiName;
	}

	public void setSessionProperties(Properties sessionProperties) {
		this.sessionProperties = sessionProperties;
	}

	/**
	 * smtp密码
	 * 
	 * @param smtpPassword
	 */
	public void setSmtpPassword(String smtpPassword) {
		this.smtpPassword = smtpPassword;
	}

	/**
	 * smtp账号,会自动根据属性mailDomain 以及Session Property来智能推断发送帐户。智能推断需要满足以下条件：<br/>
	 * <li>在mailDomain以及Session Property设置之前。 <li>fromAddress没有被设置。
	 * 
	 * @param smtpUser
	 */
	public void setSmtpUser(String smtpUser) {
		if (this.fromAddress == null) {
			try {
				if (this.mailDomain != null) {
					this.fromAddress = this.parrseAddress(smtpUser + "@"
							+ this.mailDomain);
				} else {
					String smtphost = this.sessionProperties
							.getProperty("mail.smtp.host");
					Pattern p = Pattern.compile("(?<=(?i)smtp\\.)\\S*");
					Matcher m = p.matcher(smtphost);
					while (m.find()) {
						String hostDomain = m.group(0);
						this.fromAddress = this.parrseAddress(smtpUser + "@"
								+ hostDomain);
					}
				}
			} catch (AddressException e) {
				if (logger.isWarnEnabled())
					logger.warn("Default fromAddress is not set");
			}
		}
		this.smtpUser = smtpUser;
	}

	/**
	 * 标题
	 * 
	 * @param subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setThrowRuntimeException(boolean throwRuntimeException) {
		this.throwRuntimeException = throwRuntimeException;
	}

	/**
	 * 设置主送帐户
	 * 
	 * @param toAddress
	 *            符合RFC
	 *            822格式的邮件地址列表，"name<email@abc.org>","email@abc.org"都是合法的邮件地址
	 *            ，前者会使邮件地址具有用户名。
	 * @throws AddressException
	 */
	public void setToAddress(String[] toAddress) throws AddressException {
		this.toAddress = new Address[toAddress.length];
		for (int i = 0; i < toAddress.length; i++) {
			this.toAddress[i] = parrseAddress(toAddress[i]);
		}
	}

	/**
	 * 设置主送帐户
	 * 
	 * @param toAddressString
	 *            符合RFC
	 *            822格式的邮件地址列表，邮件地址之间使用","分隔。"name<email@abc.org>,email@abc.org"
	 *            是合法，会解析为2个地址。前一个地址有用户名
	 * @throws AddressException
	 * @throws UnsupportedEncodingException
	 */
	public void setToAddressOfString(String toAddressString)
			throws AddressException, UnsupportedEncodingException {
		toAddressString = toAddressString.replace(';', ',');
		this.toAddress = parseAddresses(toAddressString);
	}

	private void successDone() {
		if (this.sendMailCallback != null)
			this.sendMailCallback.doInSuccess();
	}

	public void setMailDomain(String mailDomain) {
		this.mailDomain = mailDomain;
	}

	public void addToAddress(String addresses) throws AddressException,
			UnsupportedEncodingException {
		this.toAddress = (Address[]) ArrayUtils.addAll(this.toAddress, this
				.parseAddresses(addresses));
	}

	public void addCcAddress(String addresses) throws AddressException,
			UnsupportedEncodingException {
		this.ccAddress = (Address[]) ArrayUtils.addAll(this.ccAddress, this
				.parseAddresses(addresses));
	}

	public void addBccAddress(String addresses) throws AddressException,
			UnsupportedEncodingException {
		this.bccAddress = (Address[]) ArrayUtils.addAll(this.bccAddress, this
				.parseAddresses(addresses));
	}
}
