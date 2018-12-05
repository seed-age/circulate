package com.sunnsoft.sloa.util;

import com.sunnsoft.ThirdPartyConfiguration;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.Mail;
import com.sunnsoft.sloa.db.vo.Receive;
import com.sunnsoft.sloa.db.vo.UserMssage;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 调用消息推送
 *
 * @author chenjian
 *
 */
public class HrmMessagePushUtils {

	public static final String ADD_URL = ThirdPartyConfiguration.getOaHrmPushUrl();//"http://oa-test.seedland.cc/social/PushRemindWebService.jsp";
	//public static final String ADD_URL = "https://oa.seedland.cc/social/PushRemindWebService.jsp";

	/**
	 * 消息推送! --> 确认时提醒所有人
	 *
	 * @param userName
	 *            消息推送人的名字
	 * @param requesttitle
	 *            标题 --> 1：收到传阅提醒 2:确认时提醒 3：开封时提醒
	 * @param receiverIds
	 *            接收人 ids , 多个用逗号(,)分割
	 * @param userId
	 *            消息推送人的 userID
	 * @param mailStatus
	 *            状态 1：收到传阅提醒 2:确认时提醒 3：开封时提醒 4:确认时提示所有
	 * @param mailId
	 *            传阅ID
	 * @param remindAll
	 *            如果是提醒所有 设置为true
	 */
	public static void getSendPush(String userName, Integer requesttitle, String receiverIds, Long userId,
								   Integer mailStatus, Long mailId, boolean remindAll) {
		HttpServletRequest request = ServletActionContext.getRequest();
		String serverName = request.getServerName();
		System.out.println("域名: " + serverName);
		String message = "一封传阅被确认，请及时查看。提示该传阅的所有人"; // 推送消息标题
		int type = 3; // 标识

		// 如果为true
		if (remindAll) {

			String[] userIds = receiverIds.split(",");
			int leng = userIds.length - 1;
			for (int i = 0; i < userIds.length; i++) {
				System.out.println("确认时提醒所有人: " + userIds[i]);
				if (leng == i) {
					type = 1;
				}
				JSONObject obj = new JSONObject();

				String path = "http://" + serverName + "/web/article.htm?userId=" + userIds[i] + "&mailStatus=" + type
						+ "&article=" + mailId;

				try {
					obj.element("title", java.net.URLEncoder.encode("", "utf-8"));
					obj.element("requesttitle", java.net.URLEncoder.encode(message, "utf-8"));
					obj.element("requestdetails", java.net.URLEncoder.encode("创建人: " + userName, "utf-8"));

					obj.element("requesturl", java.net.URLEncoder.encode(path, "utf-8"));
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				obj.element("receiverIds", userIds[i]);
				obj.element("key", "3d0786ea-13df-44cb-9d23-e0412658ebd5");
				obj.element("method", "pushExternal");
				String url = ADD_URL + "?title=" + obj.getString("title") + "&requesttitle="
						+ obj.getString("requesttitle") + "&method=" + obj.getString("method") + "&requestdetails="
						+ obj.getString("requestdetails") + "&requesturl=" + obj.getString("requesturl")
						+ "&receiverIds=" + obj.getString("receiverIds") + "&key=" + obj.getString("key");
				// 通过？在后面传参
//				System.out.println(url);
//				System.out.println("跳转路径: " + path);
				String result = "";
				try {
					URL httpurl = new URL(url);
					// 创建一个HTTP连接
					HttpURLConnection httpConn = (HttpURLConnection) httpurl.openConnection();
					// 设置访问方式
					httpConn.setRequestMethod("POST");
					// 向连接中写入数据
					httpConn.setDoInput(true);
					// 从连接中读取数据
					httpConn.setDoOutput(true);
					// 自动执行HTTP重定向
					// httpConn.setInstanceFollowRedirects(true);
					// 获取输出流
					PrintWriter out = new PrintWriter(httpConn.getOutputStream());
					out.flush();
					out.close();
					BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
					String line;
					while ((line = in.readLine()) != null) {
						result += line;
					}
					System.out.println(userIds[i] + "推送状态码: =========" + httpConn.getResponseCode());
					in.close();
				} catch (Exception e) {
					System.out.println("没有结果！" + e);
				}
				if (result.equals("false")) {
					System.out.println(userIds[i] + "发送外部推送失败！ " + result);
				} else {
					System.out.println(userIds[i] + "发送外部推送成功! " + result + "==" + obj.toString());
				}
			}

		}

	}

	/**
	 * 消息推送!
	 *
	 * @param userName
	 *            消息推送人的名字
	 * @param requesttitle
	 *            标题 --> 1：收到传阅提醒 2:确认时提醒 3：开封时提醒
	 * @param receiverIds
	 *            接收人 ids , 多个用逗号(,)分割
	 * @param userId
	 *            消息推送人的 userID
	 * @param mailStatus
	 *            状态 1：收到传阅提醒 2:确认时提醒 3：开封时提醒 4:确认时提示所有
	 * @param mailId
	 *            传阅ID
	 */
	public static void getSendPush(String userName, Integer requesttitle, String receiverIds, Long userId,
								   Integer mailStatus, Long mailId) {


		HttpServletRequest request = ServletActionContext.getRequest();
		String serverName = request.getServerName();

		System.out.println("域名: " + serverName);

		Integer send = 1;

		Mail mail = Services.getMailService().findById(mailId);

		if(requesttitle == send) {
//			String message = "收到一封新的传阅，请及时查看。";   张三-传阅标题-2018年11月29日 web
			String message = mail.getLastName() + "-" + mail.getTitle() + "-" + dateToString(mail.getSendTime()) + "";; // 推送消息标题
			int type = 3; // 标识
			String[] ids = receiverIds.split(",");
			for (int i = 0; i < ids.length; i++) {
				JSONObject obj = new JSONObject();
				String path = "http://" + serverName + "/web/article.htm?userId=" + ids[i] + "&mailStatus=" + type + "&article="
						+ mailId;

				try {
					obj.element("title", java.net.URLEncoder.encode("", "utf-8"));
					obj.element("requesttitle", java.net.URLEncoder.encode(message, "utf-8"));
					obj.element("requestdetails", java.net.URLEncoder.encode("创建人: " + userName, "utf-8"));

					obj.element("requesturl", java.net.URLEncoder.encode(path, "utf-8"));
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				obj.element("receiverIds", ids[i]);
				obj.element("key", "3d0786ea-13df-44cb-9d23-e0412658ebd5");
				obj.element("method", "pushExternal");
				String url = ADD_URL + "?title=" + obj.getString("title") + "&requesttitle=" + obj.getString("requesttitle")
						+ "&method=" + obj.getString("method") + "&requestdetails=" + obj.getString("requestdetails")
						+ "&requesturl=" + obj.getString("requesturl") + "&receiverIds=" + obj.getString("receiverIds")
						+ "&key=" + obj.getString("key");
				// 通过？在后面传参
				System.out.println(url);
				System.out.println("跳转路径: " + path);
				String result = "";
				try {
					URL httpurl = new URL(url);
					// 创建一个HTTP连接
					HttpURLConnection httpConn = (HttpURLConnection) httpurl.openConnection();
					// 设置访问方式
					httpConn.setRequestMethod("POST");
					// 向连接中写入数据
					httpConn.setDoInput(true);
					// 从连接中读取数据
					httpConn.setDoOutput(true);
					// 自动执行HTTP重定向
					// httpConn.setInstanceFollowRedirects(true);
					// 获取输出流
					PrintWriter out = new PrintWriter(httpConn.getOutputStream());
					out.flush();
					out.close();
					BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
					String line;
					while ((line = in.readLine()) != null) {
						result += line;
					}
					System.out.println("推送状态码: =========" + httpConn.getResponseCode());
					in.close();
				} catch (Exception e) {
					System.out.println("没有结果！" + e);
				}
				if (result.equals("false")) {
					System.out.println("发送外部推送失败！ " + result);
				} else {
					System.out.println("发送外部推送成功! " + result + "==" + obj.toString());
				}

			}
			return;
		}

		JSONObject obj = new JSONObject();

		String message = null; // 推送消息标题
		int type = 0; // 标识
		switch (requesttitle) {
			case 1:
//			message = "收到一封新的传阅，请及时查看。";
				message = mail.getLastName() + "-" + mail.getTitle() + "-" + dateToString(mail.getSendTime()) + "";
				type = 3;
				break;
			case 2:
//			message = "一封传阅被确认，请及时查看。";
				UserMssage userMssage1 = Services.getUserMssageService().createHelper().getUserId().Eq(userId.intValue()).uniqueResult();
				String confirmDate = getDate(userMssage1.getLoginId(), mail);
				message = mail.getLastName() + "-" + mail.getTitle() + confirmDate + "被" + userMssage1.getLastName() + "确认";
				type = 1;
				break;
			case 3:
//			message = "一封传阅被开封并确认，请及时查看。";
				UserMssage userMssage = Services.getUserMssageService().createHelper().getUserId().Eq(userId.intValue()).uniqueResult();
				String openDate = getDate(userMssage.getLoginId(), mail);
				message = mail.getLastName() + "-" + mail.getTitle() + openDate + "被" + userMssage.getLastName() + "开封";
				type = 1;
				break;
			default:
				System.out.println("default");
				break;
		}

		/**
		 * 外部（自定义）消息推送
		 *
		 * @param title
		 *            窗口标题
		 * @param requesttitle
		 *            标题
		 * @param requestdetails
		 *            详情信息（标签文本）
		 * @param requesturl
		 *            外部链接
		 * @param receiverIds
		 *            接收人id
		 */

		String path = "http://" + serverName + "/web/article.htm?userId=" + userId + "&mailStatus=" + type + "&article="
				+ mailId;

		try {
			obj.element("title", java.net.URLEncoder.encode("", "utf-8"));
			obj.element("requesttitle", java.net.URLEncoder.encode(message, "utf-8"));
			obj.element("requestdetails", java.net.URLEncoder.encode("创建人: " + userName, "utf-8"));

			obj.element("requesturl", java.net.URLEncoder.encode(path, "utf-8"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		obj.element("receiverIds", receiverIds);
		obj.element("key", "3d0786ea-13df-44cb-9d23-e0412658ebd5");
		obj.element("method", "pushExternal");
		String url = ADD_URL + "?title=" + obj.getString("title") + "&requesttitle=" + obj.getString("requesttitle")
				+ "&method=" + obj.getString("method") + "&requestdetails=" + obj.getString("requestdetails")
				+ "&requesturl=" + obj.getString("requesturl") + "&receiverIds=" + obj.getString("receiverIds")
				+ "&key=" + obj.getString("key");
		// 通过？在后面传参
		System.out.println(url);
		System.out.println("跳转路径: " + path);
		String result = "";
		try {
			URL httpurl = new URL(url);
			// 创建一个HTTP连接
			HttpURLConnection httpConn = (HttpURLConnection) httpurl.openConnection();
			// 设置访问方式
			httpConn.setRequestMethod("POST");
			// 向连接中写入数据
			httpConn.setDoInput(true);
			// 从连接中读取数据
			httpConn.setDoOutput(true);
			// 自动执行HTTP重定向
			// httpConn.setInstanceFollowRedirects(true);
			// 获取输出流
			PrintWriter out = new PrintWriter(httpConn.getOutputStream());
			out.flush();
			out.close();
			BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			System.out.println("推送状态码: =========" + httpConn.getResponseCode());
			in.close();
		} catch (Exception e) {
			System.out.println("没有结果！" + e);
		}
		if (result.equals("false")) {
			System.out.println("发送外部推送失败！ " + result);
		} else {
			System.out.println("发送外部推送成功! " + result + "==" + obj.toString());
		}

	}

	private static String getDate(String LoginIds, Mail mail) {
		List<Receive> receives = mail.getReceives();
		for (Receive receive : receives) {
			if (receive.getLoginId().equals(LoginIds)) {
				return dateToString(receive.getOpenTime());
			}
		}
		return dateToString(receives.get(0).getReceiveTime());
	}


	/**
	 * 将java.util.Date 格式转换为字符串格式'yyyy-MM-dd HH:mm:ss'(24小时制) 如Sat May 11 17:24:21
	 * CST 2002 to '2002-05-11 17:24:21'
	 *
	 * @param time
	 *            Date 日期
	 * @return String 字符串
	 */
	public static String dateToString(Date time) {
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("yyyy年MM月dd日");
		String ctime = formatter.format(time);

		return ctime;
	}

}
