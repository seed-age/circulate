package com.sunnsoft.sloa.actions.web.createmail;

import cc.seedland.sdk.exceptions.ClientException;
import cc.seedland.sdk.urm.UrmClient;
import cc.seedland.sdk.urm.model.SendSmsResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.config.Config;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.*;
import com.sunnsoft.sloa.helper.ReceiveBean;
import com.sunnsoft.sloa.util.ConstantUtils;
import com.sunnsoft.sloa.util.HrmMessagePushUtils;
import com.sunnsoft.sloa.util.HrmUtils;
import com.sunnsoft.sloa.util.UrmClientUtils;
import com.sunnsoft.sloa.util.mail.MessageUtils;
import com.sunnsoft.util.DateUtils;
import com.sunnsoft.util.struts2.Results;
import org.apache.commons.lang3.StringEscapeUtils;
import org.gteam.db.dao.TransactionalCallBack;
import org.gteam.service.IService;
import org.springframework.util.Assert;
import org.apache.log4j.Logger;

import javax.annotation.Resource;
import java.util.*;

/**
 * 新建传阅 --点击发送/保存: 接收人名称, 附件 , 传阅主题, 传阅内容. (删除附件没有实现)
 *
 * @author chenjian
 *
 */
public class InsertMail extends BaseParameter {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(InsertMail.class);

	private boolean transmission; // 如果是true 代表是发送新建传阅 如果是 false 表示保存新建传阅 不是发送

	private Integer userTotal = 0; // 0 不是  1 表示添加整个组织架构
	private Integer[] subcompanyIds; // 分部Ids
	private Integer[] departmentIds; // 部门Ids
	private Integer[] receiveUserId; // 收件人id

	private String[] bulkId; // 附件上传批次ID

	private long mailId; // 传阅id(主要用于二次以上的保存传阅记录)
	private long userId; // 发件人id
	private String title; // 传阅主题
	private String mailContent; // 邮件内容
	private Boolean ifImportant = false; // 重要传阅

	private Boolean ifUpdate = false; // 允许修订附件
	private Boolean ifUpload = false; // 允许上传附件
	private Boolean ifRead = false; // 开封已阅确认
	private Boolean ifNotify = false; // 短信提醒
	private Boolean ifRemind = false; // 确认时提醒
	private Boolean ifRemindAll = false; // 确认时提醒所有传阅对象
	private Boolean ifSecrecy = false; // 传阅密送
	private Boolean ifAdd = false; // 允许新添加人员
	private Boolean ifSequence = false; // 有序确认

	@Resource
	private Config config;

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
				msg = "接收人不能为空!";
			}else {
				msg = "系统繁忙!";
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
		Assert.notNull(ifImportant, "重要传阅的值不能为空");
		Assert.notNull(ifUpdate, "允许修订附件的值不能为空");
		Assert.notNull(ifUpload, "允许上传附件不能的值为空");
		Assert.notNull(ifRemind, "确认是提醒的值不能为空");
		Assert.notNull(ifAdd, "允许新添加人员的值不能为空");

		if(title == null || title.equals("")) {
			title = "传阅主题";
		}

		String ruleName = null;

		// 把接收人的名称拼接成一个字符串
		Set<UserMssage> userMssageSet = new HashSet<>();
		StringBuilder sb = new StringBuilder();
		String allName = "";

		try {

			int id = (int)userId;

			if (subcompanyIds != null) { // 不为 null, 表示添加了分部
				// 根据分部ID查询信息
				List<Hrmsubcompany> hrmsubcompanyList = Services.getHrmsubcompanyService().findByIds(subcompanyIds);
				for (Hrmsubcompany hrmsubcompany : hrmsubcompanyList) {
					HrmUtils.getSubcompanyUserMssage(hrmsubcompany.getId(), null, userMssageSet, sb, id);
				}
			}

			if(departmentIds != null){

				List<Hrmdepartment> hrmdepartmentList = Services.getHrmdepartmentService().findByIds(departmentIds);
				HrmUtils.getSubcompanyUserMssage(null, hrmdepartmentList, userMssageSet, sb, id);

			}

			if(receiveUserId != null) {

				// 查询接收人信息
				List<UserMssage> userMssages  = Services.getUserMssageService().createHelper().getUserId().In(receiveUserId)
						.list();

				for (UserMssage userMssage : userMssages) {

					if (userMssage.getUserId() == id) { // 如果接受人中有发件人, 直接跳过
						continue;
					}

					userMssageSet.add(userMssage);
					sb.append(userMssage.getLastName()).append(";");
				}

				System.out.println(receiveUserId.length);
				if (receiveUserId.length == 1 && userMssageSet.size() == 0) {
					msg = "不可以自己对自己发送传阅";
					success = false;
					code = "205";
					json = "null";
					return Results.GLOBAL_FORM_JSON;
				}

			}

			if (userTotal == 1) { // 表示整个组织架构
				List<UserMssage> mssageList = Services.getUserMssageService().createHelper().startOr().getStatus().Eq(ConstantUtils.OA_USER_PROBATION_STATUS)
						.getStatus().Eq(ConstantUtils.OA_USER_OFFICIAL_STATUS).getStatus().Eq(ConstantUtils.OA_USER_TEMPORARY_STATUS)
						.getStatus().Eq(ConstantUtils.OA_USER_PROBATION_DELAY_STATUS)
						.stopOr().list();

				for (UserMssage userMssage : mssageList) {

					if (userMssage.getUserId() == id) { // 如果接受人中有发件人, 直接跳过
						continue;
					}
					userMssageSet.add(userMssage);
					sb.append(userMssage.getLastName()).append(";");
				}


			}


		} catch (Exception e) {
			e.printStackTrace();
			msg = "请添加存在的联系人!";
			success = false;
			code = "205";
			json = "null";
			return Results.GLOBAL_FORM_JSON;
		}


		boolean hasAttachment = false;

		// 判断是否有附件
		if (bulkId != null) { // 如果数组不为null 说明有附件, 反之, 则没有附件
			hasAttachment = true;
		} else {
			hasAttachment = false;
		}

		// 转义
		String string = StringEscapeUtils.unescapeHtml4(mailContent);
		title = StringEscapeUtils.unescapeHtml4(title);

		// 根据当前用户ID 查询该用户的信息
		UserMssage mssage = Services.getUserMssageService().createHelper().getUserId().Eq((int) userId).uniqueResult();

		if (mssage == null) {
			msg = "该用户不存在!";
			success = false;
			code = "205";
			json = "null";
			return Results.GLOBAL_FORM_JSON;
		}

		/**
		 * 如果是true 代表是发送传阅 false 表示保存新建传阅 不是发送
		 */
		// 如果是true 表示发送传阅
		if (transmission) {

			if(userMssageSet.size() == 0){
				msg = "请填写收件人";
				success = false;
				code = "205";
				json = "null";
				return Results.GLOBAL_FORM_JSON;
			}

			allName = sb.toString().substring(0, sb.toString().length() - 1);

			Assert.notNull(userMssageSet, "接收人ID不能为空");
			Assert.notNull(title, "传阅主题不能为空");

			Mail mail = null;
			if (mailId > 0) { // 如果mailId 大于 0 又是发送选择, 那么修改状态
				// 查询该传阅
				mail = Services.getMailService().findById(mailId);
				// 更新数据
				mail.setAllReceiveName(allName); // 接收人名称
				mail.setTitle(title);
				mail.setMailContent(string);
				mail.setIfImportant(ifImportant);
				mail.setIfUpdate(ifUpdate);
				mail.setIfUpload(ifUpload);
				mail.setIfRead(ifRead);
				mail.setIfNotify(ifNotify);
				mail.setIfRemind(ifRemind);
				mail.setIfRemindAll(ifRemindAll);
				mail.setIfSecrecy(ifSecrecy);
				mail.setIfAdd(ifAdd);
				mail.setIfSequence(ifSequence);
				if (hasAttachment) {
					mail.setHasAttachment(hasAttachment);
				}
				mail.setRuleName(ruleName);
				mail.setStepStatus(ConstantUtils.MAIL_HALFWAY_STATUS); // 发阅中
				mail.setStatus(ConstantUtils.MAIL_STATUS); // 无状态
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
						.setCompleteTime(new Date()).setStepStatus(ConstantUtils.MAIL_HALFWAY_STATUS)
						.setStatus(ConstantUtils.MAIL_STATUS)
						.setIfImportant(ifImportant).setIfUpdate(ifUpdate)
						.setIfUpload(ifUpload).setIfRead(ifRead).setIfNotify(ifNotify).setIfRemind(ifRemind)
						.setIfRemindAll(ifRemindAll).setIfSecrecy(ifSecrecy).setIfAdd(ifAdd).setIfSequence(ifSequence)
						.setHasAttachment(hasAttachment).setEnabled(false).setAttention(false).setRuleName(ruleName)
						.insertUnique();

			}

			// 让附件和传阅建立关系
			if (bulkId != null) { // 如果bulkid为长度为 0 , 说明用户没有上传附件
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

			// 拼接收件人的loginId
			String receiverIds = "";
			// 短信接收人的手机号码(手机号码以英文逗号分隔，不超过50个号码)
			List<String> phoneList = new ArrayList<>();

			int num = 0;
			ReceiveBean bean = Services.getReceiveService().createHelper().bean();
			for (UserMssage userMssage : userMssageSet) {

				num++;
				// 拼接
				receiverIds += userMssage.getUserId()+ ",";

				if (userMssage.getMobile() != null && !userMssage.getMobile().equals("")) {
					phoneList.add(userMssage.getMobile());
				}


				try {
					// 新增收件人记录
					bean.create()
							.setMail(mail).setUserId(userMssage.getUserId()).setLastName(userMssage.getLastName()).setLoginId(userMssage.getLoginId())
							.setReceiveStatus(ConstantUtils.RECEIVE_NOTOPEN_STATUS).setWorkCode(userMssage.getWorkCode()).setReceiveTime(mail.getSendTime())
							.setSubcompanyName(userMssage.getFullName()).setDepartmentName(userMssage.getDeptFullname())
							.setStepStatus(ConstantUtils.RECEIVE_AWAIT_STATUS).setMailState(ConstantUtils.RECEIVE_UNREAD_STATUS)
							.setJoinTime(mail.getCreateTime()).setIfConfirm(false)
							.setReDifferentiate(userId);

					if (num == 100) {
						LOGGER.warn("开始新增接收人数据:::::::::: 一次一百条.");
						bean.insert();
						LOGGER.warn("新增后, 进行睡眠 两秒:::::::::::::::::::");
						Thread.sleep(2000);
						bean = Services.getReceiveService().createHelper().bean();
						num = 0;
					}
				} catch (Exception e) {
					e.printStackTrace();

				}
				receiveStr = true;
			}

			if (num < 100) {
				LOGGER.warn("一次性新增 " + num + "条接收人数据!");
				bean.insert();
			}

			// 截取掉最后的 , 号
			String ids = receiverIds.substring(0, receiverIds.toString().length() - 1);

			// 判断
			if (mail != null && receiveStr == true) {

				Map<String, Object> map = new HashMap<>();
				map.put("mailId", mail.getMailId());
				map.put("userId", mail.getUserId());

				// 调用消息推送的方法 --> (web)
				HrmMessagePushUtils.getSendPush(mail.getLastName(), 1, ids, mail.getUserId(),1 , mail.getMailId());

				// 推送消息 --> (APP)
				MessageUtils.pushEmobile(ids, 1, mail.getMailId(), null);

				if (mail.getIfNotify()) {
					// 发送短信
					LOGGER.warn("========================开始进行发送短信========================================");
					// 1. 初始化
					UrmClient urmClient = UrmClientUtils.getUrmClient(config);
					// 2. 发送
					Map<String, String> smsResult = new HashMap<>();
					smsResult.put("title", mail.getTitle());
					smsResult.put("time", DateUtils.dateToString(mail.getSendTime(), null));
					smsResult.put("name", mail.getLastName());
					UrmClientUtils.getSendSms(urmClient, phoneList, config, JSON.toJSONString(smsResult));
				}

				msg = "发送新建传阅成功!";
				success = true;
				code = "200";
				json = JSONObject.toJSONString(map);
				return Results.GLOBAL_FORM_JSON;

			} else {
				msg = "发送新建传阅失败!";
				success = false;
				code = "404";
				json = "null";
				return Results.GLOBAL_FORM_JSON;
			}

			// 如果是false , 表示保存新建传阅
		} else {

			if (sb.toString() != null && !sb.toString().equals("")) {

				allName = sb.toString().substring(0, sb.toString().length() - 1);
			}

			Mail mail = null;
			if (mailId > 0) { // 如果mailId 大于 0 说明是第二次保存或是 N次
				// 查询该传阅ID的记录
				mail = Services.getMailService().findById(mailId);
				// 更新数据
				mail.setAllReceiveName(allName); // 接收人名称
				mail.setTitle(title);
				mail.setMailContent(string);
				mail.setIfImportant(ifImportant);
				mail.setIfUpdate(ifUpdate);
				mail.setIfUpload(ifUpload);
				mail.setIfRead(ifRead);
				mail.setIfNotify(ifNotify);
				mail.setIfRemind(ifRemind);
				mail.setIfRemindAll(ifRemindAll);
				mail.setIfSecrecy(ifSecrecy);
				mail.setIfAdd(ifAdd);
				mail.setIfSequence(ifSequence);
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
						.setCompleteTime(new Date()).setStatus(ConstantUtils.MAIL_HALFWAY_STATUS)
						.setIfImportant(ifImportant).setIfUpdate(ifUpdate)
						.setIfUpload(ifUpload).setIfRead(ifRead).setIfNotify(ifNotify).setIfRemind(ifRemind)
						.setIfRemindAll(ifRemindAll).setIfSecrecy(ifSecrecy).setIfAdd(ifAdd).setIfSequence(ifSequence)
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

			if (userMssageSet != null && userMssageSet.size() > 0) {

				// 更新接收人数据
				List<Receive> receives = mail.getReceives();
				if(receives.size() > 0 && receives != null) {
					Services.getReceiveService().deleteList(receives);
				}

				int num = 0;
				ReceiveBean bean = Services.getReceiveService().createHelper().bean();

				for (UserMssage userMssage : userMssageSet) {

					num++;

					try {
						// 新增收件人记录
						bean.create()
								.setMail(mail).setUserId(userMssage.getUserId()).setLastName(userMssage.getLastName()).setLoginId(userMssage.getLoginId())
								.setReceiveStatus(ConstantUtils.RECEIVE_NOTOPEN_STATUS).setWorkCode(userMssage.getWorkCode()).setReceiveTime(mail.getSendTime())
								.setSubcompanyName(userMssage.getFullName()).setDepartmentName(userMssage.getDeptFullname())
								.setStepStatus(ConstantUtils.RECEIVE_NOTOPEN_STATUS).setMailState(ConstantUtils.RECEIVE_WAIT_STATUS)
								.setJoinTime(mail.getCreateTime()).setIfConfirm(false)
								.setReDifferentiate(userId);

						if (num == 100) {
							LOGGER.warn("开始新增接收人数据:::::::::: 一次一百条.");
							bean.insert();
							LOGGER.warn("新增后, 进行睡眠 两秒:::::::::::::::::::");
							Thread.sleep(2000);
							bean = Services.getReceiveService().createHelper().bean();
							num = 0;
						}
					} catch (Exception e) {
						e.printStackTrace();

					}

					receiveStr = true;
				}

				if (num < 100) {
					LOGGER.warn("一次性新增 " + num + "条接收人数据!");
					bean.insert();
				}


			}else {
				receiveStr = true;
				// 更新接收人数据
				List<Receive> receives = mail.getReceives();
				if(receives.size() > 0 && receives != null) {
					Services.getReceiveService().deleteList(receives);
				}
			}

			// 判断
			if (mail != null && receiveStr == true) {
				Map<String, Object> map = new HashMap<>();
				map.put("mailId", mail.getMailId());
				map.put("userId", mail.getUserId());
				msg = "保存新建传阅成功..";
				success = true;
				code = "201";
				json = JSONObject.toJSONString(map);
				return Results.GLOBAL_FORM_JSON;

			} else {
				msg = "保存新建传阅失败..";
				success = false;
				code = "404";
				json = "null";
				return Results.GLOBAL_FORM_JSON;
			}
		}
	}

	// 拼接规则名称, 已分号分隔
	public String ruleNameMethod(List<Mail> list) {
		StringBuilder sb = new StringBuilder();

		for (Mail mail : list) {

			if (mail.getIfImportant() == true) {
				String if_important = "重要传阅";
				sb.append(if_important).append(";");
			}

			if (mail.getIfUpdate() == true) {
				String if_update = "允许修订Word、Excel附件";
				sb.append(if_update).append(";");

			}

			if (mail.getIfUpload() == true) {
				String if_up = "允许上传附件";
				sb.append(if_up).append(";");
			}

			if (mail.getIfRead().equals(true)) {
				String if_read = "开封已阅确认";
				sb.append(if_read).append(";");
			}

			if (mail.getIfNotify() == true) {
				String if_note = "短信提醒";
				sb.append(if_note).append(";");

			}

			if (mail.getIfRemind() == true) {
				String if_remind = "确认时提醒";
				sb.append(if_remind).append(";");

			}

			if (mail.getIfRemindAll() == true) {
				String if_remind_all = "确认时提醒所有传阅对象";
				sb.append(if_remind_all).append(";");

			}

			if (mail.getIfSecrecy() == true) {
				String if_secrecy = "传阅密送";
				sb.append(if_secrecy).append(";");

			}

			if (mail.getIfAdd() == true) {
				String if_add = "允许新添加人员";
				sb.append(if_add).append(";");

			}

			if (mail.getIfSequence() == true) {
				String if_sequence = "有序确认";
				sb.append(if_sequence).append(";");

			}
		}

		return sb.toString();
	}

	public boolean isTransmission() {
		return transmission;
	}

	public void setTransmission(boolean transmission) {
		this.transmission = transmission;
	}

	public Integer getUserTotal() {
		return userTotal;
	}

	public void setUserTotal(Integer userTotal) {
		this.userTotal = userTotal;
	}

	public Integer[] getSubcompanyIds() {
		return subcompanyIds;
	}

	public void setSubcompanyIds(Integer[] subcompanyIds) {
		this.subcompanyIds = subcompanyIds;
	}

	public Integer[] getDepartmentIds() {
		return departmentIds;
	}

	public void setDepartmentIds(Integer[] departmentIds) {
		this.departmentIds = departmentIds;
	}

	public Integer[] getReceiveUserId() {
		return receiveUserId;
	}

	public void setReceiveUserId(Integer[] receiveUserId) {
		this.receiveUserId = receiveUserId;
	}

	public String[] getBulkId() {
		return bulkId;
	}

	public void setBulkId(String[] bulkId) {
		this.bulkId = bulkId;
	}

	public long getMailId() {
		return mailId;
	}

	public void setMailId(long mailId) {
		this.mailId = mailId;
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

	public Boolean getIfImportant() {
		return ifImportant;
	}

	public void setIfImportant(Boolean ifImportant) {
		this.ifImportant = ifImportant;
	}

	public Boolean getIfUpdate() {
		return ifUpdate;
	}

	public void setIfUpdate(Boolean ifUpdate) {
		this.ifUpdate = ifUpdate;
	}

	public Boolean getIfUpload() {
		return ifUpload;
	}

	public void setIfUpload(Boolean ifUpload) {
		this.ifUpload = ifUpload;
	}

	public Boolean getIfRead() {
		return ifRead;
	}

	public void setIfRead(Boolean ifRead) {
		this.ifRead = ifRead;
	}

	public Boolean getIfNotify() {
		return ifNotify;
	}

	public void setIfNotify(Boolean ifNotify) {
		this.ifNotify = ifNotify;
	}

	public Boolean getIfRemind() {
		return ifRemind;
	}

	public void setIfRemind(Boolean ifRemind) {
		this.ifRemind = ifRemind;
	}

	public Boolean getIfRemindAll() {
		return ifRemindAll;
	}

	public void setIfRemindAll(Boolean ifRemindAll) {
		this.ifRemindAll = ifRemindAll;
	}

	public Boolean getIfSecrecy() {
		return ifSecrecy;
	}

	public void setIfSecrecy(Boolean ifSecrecy) {
		this.ifSecrecy = ifSecrecy;
	}

	public Boolean getIfAdd() {
		return ifAdd;
	}

	public void setIfAdd(Boolean ifAdd) {
		this.ifAdd = ifAdd;
	}

	public Boolean getIfSequence() {
		return ifSequence;
	}

	public void setIfSequence(Boolean ifSequence) {
		this.ifSequence = ifSequence;
	}
}