package com.sunnsoft.sloa.actions.app.create;

import com.alibaba.fastjson.JSONObject;
import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.AttachmentItem;
import com.sunnsoft.sloa.db.vo.Mail;
import com.sunnsoft.sloa.db.vo.Receive;
import com.sunnsoft.sloa.db.vo.UserMssage;
import com.sunnsoft.sloa.util.HrmMessagePushUtils;
import com.sunnsoft.sloa.util.mail.MessageUtils;
import com.sunnsoft.util.struts2.Results;
import org.apache.commons.lang3.StringEscapeUtils;
import org.gteam.db.dao.TransactionalCallBack;
import org.gteam.service.IService;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 新建传阅(APP端): 添加收件人(多个) , 添加附件(多个) , 传阅主题 , 传阅内容 APP端是没有选项的, 但是 要默认选中 PC端
 * 的五个默认的选项
 *
 * @author chenjian
 *
 */
public class InsertMail extends BaseParameter {

	private static final long serialVersionUID = 1L;

	private boolean transmission; // 如果是true 代表是发送新建传阅 如果是 false 表示保存新建传阅 不是发送

	/** 该参数接收从前端页面传过来的接收人参数 */
	private Integer[] receiveUserId; // 收件人id

	/** 该参数接收从前端页面传过来上传附件的批次ID */
	private String[] bulkId; // 附件上传批次ID

	/** 下列参数是新建传阅用 */
	private long mailId; // 传阅id(主要用于二次以上的保存传阅记录)
	private long userId; // 发件人id
	private String title; // 传阅主题
	private String mailContent; // 邮件内容

	@Override
	public String execute() throws Exception {
		String result = null ;
		try {
			result = (String)Services.getMailService().executeTransactional(new TransactionalCallBack() {

				@Override
				public Object execute(IService arg0) {
					System.out.println("开启事物了....");
					return getInsertMail();
				}
			});
		} catch (Exception e) {
			System.out.println("事物回滚了.....");
			if(receiveUserId == null) {
				msg = "联系人不能为空!";
			}else if(title == null){
				msg = "传阅主题不能为空";
			}else if(mailContent == null){
				msg = "传阅内容不能为空!";
			}else{
				msg = "网络繁忙,请稍后重试!";
			}
			success = false;
			code = "404";
			json = "null";
			return Results.GLOBAL_FORM_JSON;
		}
		return result;
	}

	public String getInsertMail(){

		// 校验参数
		Assert.notNull(transmission, "发送/保存 传阅的开关,不能为空");
		Assert.notNull(userId, "发件人ID不能为空");
		Assert.notNull(mailContent, "邮件内容不能为空");

		// 设置默认的选项参数
		String ruleName = "重要传阅;允许修订Word、Excel附件;允许上传附件;确认时提醒;允许新添加人员";

		// 把接收人拼接成一个字符串
		StringBuilder sb = new StringBuilder();

		// 把接收人的名称拼接成一个字符串
		List<UserMssage> userMssages = null;
		String allName = "";
		if (receiveUserId != null) {
			try {
				// 查询接收人信息
				userMssages = Services.getUserMssageService().createHelper().getUserId().In(receiveUserId).list();

				for (UserMssage userMssage : userMssages) {
					sb.append(userMssage.getLastName()).append(";");
				}
			} catch (Exception e) {
				e.printStackTrace();
				msg = "请添加存在的联系人!";
				success = false;
				code = "205";
				json = "null";
				return Results.GLOBAL_FORM_JSON;
			}
			allName = sb.toString().substring(0, sb.toString().length() - 1);
		}

		// 判断是否有附件
		boolean hasAttachment = false;
		if (bulkId != null) { // 如果数组的长度大于 0 说明有附件, 反之, 则没有附件
			hasAttachment = true;
		} else {
			hasAttachment = false;
		}

		// 转义
		String string = StringEscapeUtils.unescapeHtml4(mailContent);

		// 根据用户ID查询用户信息
		UserMssage mssage = Services.getUserMssageService().createHelper().getUserId().Eq((int) userId).uniqueResult();

		if (mssage == null) {
			msg = "该用户不存在!";
			success = false;
			code = "205";
			json = "null";
			return Results.GLOBAL_FORM_JSON;
		}

		// 如果是true 表示发送传阅
		if (transmission) {

			Assert.notNull(receiveUserId, "接收人ID不能为空!");
			Assert.notNull(title, "传阅主题不能为空");

			try {
				Mail mail = null;
				if (mailId > 0) { // 如果mailId 大于 0 又是发送选择, 那么修改状态
					// 查询该传阅
					mail = Services.getMailService().findById(mailId);
					// 更新数据
					mail.setAllReceiveName(allName); // 接收人名称
					mail.setTitle(title);
					mail.setMailContent(string);
					if (hasAttachment) {
						mail.setHasAttachment(hasAttachment);
					}
					mail.setRuleName(ruleName);
					mail.setStepStatus(1); // 发阅中
					mail.setStatus(0); // 无状态
					mail.setCreateTime(new Date());
					mail.setSendTime(new Date());
					mail.setCompleteTime(new Date());

					// 更新
					Services.getMailService().update(mail);

					// 更新接收人数据
					List<Receive> receives = mail.getReceives();
					//删除所有旧的联系人呢
					Services.getReceiveService().deleteList(receives);
				}

				if (mailId == 0) {
					// 新建传阅
					mail = Services.getMailService().createHelper().bean().create().setUserId(mssage.getUserId())
							.setWorkCode(mssage.getWorkCode()).setLastName(mssage.getLastName())
							.setLoginId(mssage.getLoginId()).setSubcompanyName(mssage.getFullName())
							.setDepartmentName(mssage.getDeptFullname()).setAllReceiveName(allName).setTitle(title)
							.setMailContent(string).setCreateTime(new Date()).setSendTime(new Date())
							.setCompleteTime(new Date()).setStepStatus(1).setIfImportant(true).setIfUpdate(true)
							.setIfUpload(true).setIfRemind(true).setIfAdd(true).setIfRead(false).setIfNotify(false)
							.setIfRemindAll(false).setIfSecrecy(false).setIfSequence(false)
							.setHasAttachment(hasAttachment).setEnabled(false).setAttention(false).setRuleName(ruleName)
							.insertUnique();

				}
				// 让附件和传阅建立关系
				if (hasAttachment) { // 如果hasAttachment为false , 说明用户没有上传附件
					// 遍历
					for (int i = 0; i < bulkId.length; i++) {
						// 通过附件批次ID查询附件
						List<AttachmentItem> itemList = Services.getAttachmentItemService().createHelper().getBulkId()
								.Eq(bulkId[i]).list();
						// 遍历取出附件数据
						for (AttachmentItem attachmentItem : itemList) {
							// 设置传阅ID
							attachmentItem.setMail(mail);
							attachmentItem.setUserId(mail.getUserId());
							// 更新
							Services.getAttachmentItemService().update(attachmentItem);
						}
					}
				}

				// 添加接收人记录
				// 定义标识
				boolean receiveStr = false;

				String LoginIds = "";
				String userIds = "";
				long receiveuserId = 0;
				for (UserMssage userMssage : userMssages) {
					receiveuserId = userMssage.getUserId();
					// 收件人loginId拼接
					LoginIds += userMssage.getLoginId() + ",";
					userIds += userMssage.getUserId() + ",";
					// 新增收件人记录
					Services.getReceiveService().createHelper().bean().create().setMail(mail)
							.setUserId(userMssage.getUserId()).setLastName(userMssage.getLastName())
							.setLoginId(userMssage.getLoginId()).setWorkCode(userMssage.getWorkCode())
							.setSubcompanyName(userMssage.getFullName()).setDepartmentName(userMssage.getDeptFullname())
							.setReceiveTime(mail.getSendTime()).setReceiveStatus(0).setReDifferentiate(userId)
							.setStepStatus(2).setMailState(5).setJoinTime(mail.getCreateTime()).setIfConfirm(false)
							.insertUnique();

					receiveStr = true;


				}

				// 判断
				if (mail != null && receiveStr == true) {

					String loginIds = LoginIds.substring(0, LoginIds.length() - 1);
					String ids = userIds.substring(0, userIds.length() - 1);

					Map<String, Object> map = new HashMap<String, Object>();
					// 添加需要返回的数据
					map.put("userId", mail.getUserId());
					map.put("mailId", mail.getMailId());
					map.put("transmission", true);

					// 推送消息 --> (APP)
					MessageUtils.pushEmobile(loginIds, 1, mail.getMailId(), null);

					// 调用消息推送的方法 --> (web)
					HrmMessagePushUtils.getSendPush(mail.getLastName(), 1, ids, receiveuserId, 1, mail.getMailId());

					msg = "传阅发送成功!";
					success = true;
					code = "200";
					json = JSONObject.toJSONString(map);
					return Results.GLOBAL_FORM_JSON;

				} else {
					msg = "传阅发送失败!";
					success = false;
					code = "404";
					json = "null";
					return Results.GLOBAL_FORM_JSON;
				}
			} catch (Exception e) {
				e.printStackTrace();
				msg = "传阅发送失败!";
				success = false;
				code = "4000";
				json = "null";
				return Results.GLOBAL_FORM_JSON;
			}

		} else { // 如果是false , 表示保存新建传阅

			Mail mail = null;
			if (mailId > 0) { // 如果mailId 大于 0 说明是第二次保存或是 N次
				// 查询该传阅ID的记录
				mail = Services.getMailService().findById(mailId);
				// 更新数据
				mail.setAllReceiveName(allName); // 接收人名称
				mail.setTitle(title);
				mail.setMailContent(string);
				mail.setHasAttachment(hasAttachment);
				mail.setRuleName(ruleName);
				mail.setCreateTime(new Date());
				mail.setSendTime(new Date());
				mail.setCompleteTime(new Date());

				// 更新
				Services.getMailService().update(mail);

			}

			if (mailId == 0) {// 第一次保存
				// 保存新建传阅 , 没有流程状态, 只有待发状态
				mail = Services.getMailService().createHelper().bean().create().setUserId(mssage.getUserId())
						.setWorkCode(mssage.getWorkCode()).setLastName(mssage.getLastName())
						.setLoginId(mssage.getLoginId()).setSubcompanyName(mssage.getFullName())
						.setDepartmentName(mssage.getDeptFullname()).setAllReceiveName(allName).setTitle(title)
						.setMailContent(string).setCreateTime(new Date()).setSendTime(new Date())
						.setCompleteTime(new Date()).setStatus(1).setIfRead(false).setIfNotify(false)
						.setIfRemindAll(false).setIfSecrecy(false).setIfSequence(false).setIfImportant(true)
						.setIfUpdate(true).setIfUpload(true).setIfRemind(true).setIfAdd(true)
						.setHasAttachment(hasAttachment).setEnabled(false).setAttention(false).setRuleName(ruleName)
						.insertUnique();
			}

			// 让附件和传阅建立关系
			if (bulkId != null) { // 如果bulkid的ID长度为0 , 说明用户没有上传附件
				for (int i = 0; i < bulkId.length; i++) {

					List<AttachmentItem> itemList = Services.getAttachmentItemService().createHelper().getBulkId()
							.Eq(bulkId[i]).list();

					for (AttachmentItem attachmentItem : itemList) {
						// 设置
						attachmentItem.setMail(mail);
						attachmentItem.setUserId(mail.getUserId());
						// 更新
						Services.getAttachmentItemService().update(attachmentItem);
					}
				}
			}

			// 添加接收人记录
			// 定义标识
			boolean receiveStr = false;

			if (receiveUserId != null) {

				// 更新接收人数据
				List<Receive> receives = mail.getReceives();
				if(receives.size() > 0 && receives != null) {
					Services.getReceiveService().deleteList(receives);
				}

				for (UserMssage userMssage : userMssages) {
					// 新增收件人记录
					Services.getReceiveService().createHelper().bean().create().setMail(mail)
							.setUserId(userMssage.getUserId()).setLastName(userMssage.getLastName())
							.setLoginId(userMssage.getLoginId()).setWorkCode(userMssage.getWorkCode())
							.setSubcompanyName(userMssage.getFullName()).setDepartmentName(userMssage.getDeptFullname())
							.setReceiveTime(mail.getSendTime()).setReceiveStatus(0).setReDifferentiate(userId)
							.setStepStatus(0).setMailState(4).setJoinTime(mail.getCreateTime()).setIfConfirm(false)
							.insertUnique();

					receiveStr = true;
				}

			}else {
				receiveStr = true;
			}
			// 判断
			if (mail != null && receiveStr == true) {
				Map<String, Object> map = new HashMap<>();
				map.put("mailId", mail.getMailId());
				map.put("userId", mail.getUserId());
				map.put("transmission", false);
				msg = "传阅保存成功!";
				success = true;
				code = "201";
				json = JSONObject.toJSONString(map);
				return Results.GLOBAL_FORM_JSON;

			} else {
				msg = "传阅保存失败!";
				success = false;
				code = "404";
				json = "null";
				return Results.GLOBAL_FORM_JSON;
			}
		}
	}

	public boolean isTransmission() {
		return transmission;
	}

	public void setTransmission(boolean transmission) {
		this.transmission = transmission;
	}

	public Integer[] getReceiveUserId() {
		return receiveUserId;
	}

	public void setReceiveUserId(Integer[] receiveUserId) {
		this.receiveUserId = receiveUserId;
	}

	public long getMailId() {
		return mailId;
	}

	public void setMailId(long mailId) {
		this.mailId = mailId;
	}

	public String[] getBulkId() {
		return bulkId;
	}

	public void setBulkId(String[] bulkId) {
		this.bulkId = bulkId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMailContent() {
		return mailContent;
	}

	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}

}
