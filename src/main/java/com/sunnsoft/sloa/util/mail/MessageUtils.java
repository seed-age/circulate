package com.sunnsoft.sloa.util.mail;

import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageUtils {

	/**
	 * APP端e-mobile推送消息
	 * 
	 * @param LoginIds
	 *            推送对象
	 * @param type
	 *            1：收到传阅提醒	2:确认时提醒	3：开封时提醒
	 */
	public static void pushEmobile(String LoginIds, Integer type, Long mailId) {

		String basePushUrl = "http://oa-test.seedland.cc:89/pushMessage.do";
		String key = "a058f453-82e8-4210-9860-df6d6ae675f5";// emobile后台的推送秘钥
		String messagetypeid = "100"; // 在mobile后台注册的消息类型id
		String reviceIds = LoginIds; // 接收者的loginid，多用户使用英文半角逗号分开
		System.out.println("e-mobile的LoginIds：：：：：：：：：：：：：：：" + LoginIds);
		String badge = "1"; // 消息数量+1
		String message = null; // 推送消息标题
		int status = 0; //状态
		switch (type) {
		case 1:
			message = "收到一封新的传阅，请及时查看。";
			status = 3;
			break;
		case 2:
			message = "一封传阅被确认，请及时查看。";
			status = 1; 
			break;
		case 3:
			message = "一封传阅被开封并确认，请及时查看。";
			status = 1;
			break;
		default:
			System.out.println("default");
			break;
		}
		
		String url = "sloa://router?module=circulate&flag="+status+"&detailId=" + mailId; // 带http或https则为全路径，否则则为相对路径，点击消息会打开此url
		
		try {
			HttpClient httpClient = new HttpClient();
			// 设置访问编码
			httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			// 让服务器知道访问源为浏览器
			httpClient.getParams().setParameter(HttpMethodParams.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; rv:8.0.1) Gecko/20100101 Firefox/8.0.1");
			PostMethod postMethod = new PostMethod(basePushUrl);
			Map<String, String> para = new HashMap<String, String>();
			para.put("messagetypeid", messagetypeid);
			para.put("module", "-2"); // 标示属于自定义消息
			para.put("url", url);
			String paraJson = JSONObject.fromObject(para).toString();
			if (message.length() > 100)
				message = message.substring(0, 100) + "...";
			StringBuffer sendMsg = new StringBuffer();
			sendMsg.append(reviceIds);
			sendMsg.append(message);
			sendMsg.append(badge);
			sendMsg.append(paraJson);
			sendMsg.append(key);
			String hash = DigestUtils.md5Hex(sendMsg.toString().getBytes("UTF-8"));
			List<NameValuePair> datalist = new ArrayList<NameValuePair>();
			datalist.add(new NameValuePair("userid", reviceIds));
			datalist.add(new NameValuePair("msg", message));
			datalist.add(new NameValuePair("badge", badge));
			datalist.add(new NameValuePair("para", paraJson));
			datalist.add(new NameValuePair("hash", hash));
			NameValuePair[] data = datalist.toArray(new NameValuePair[0]);
			postMethod.setRequestBody(data);

			int code = httpClient.executeMethod(postMethod);
			System.out.println("e-mobile推送状态：：：：：：：：：：：：：：：" + code);
			if (code != HttpStatus.SC_OK) {  
			    //响应失败处理  
			}  
			// 读取内容  
			byte[] responseBody = postMethod.getResponseBody();  
			// 处理内容  
			System.out.println("e-mobile处理内容：：：：：：：：：：：：：：：" + new String(responseBody)); 
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
