package com.sunnsoft.sloa.actions.web.received;

import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.*;
import com.sunnsoft.sloa.helper.ReceiveBean;
import com.sunnsoft.sloa.util.ConstantUtils;
import com.sunnsoft.sloa.util.HrmUtils;
import com.sunnsoft.util.struts2.Results;
import org.apache.log4j.Logger;
import org.gteam.db.dao.TransactionalCallBack;
import org.gteam.service.IService;
import org.springframework.util.Assert;

import java.util.*;

/**
 * 已发/已收传阅---查看传阅对象: 点击添加联系人, 添加传阅对象
 *
 * @author chenjian
 *
 */
public class InsertMailObject extends BaseParameter {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(InsertMailObject.class);

	private Long userId; // 用户ID
	private Long mailId; // 传阅ID

	private Integer userTotal = 0; // 0 不是  1 表示添加整个组织架构
	private Integer[] subcompanyIds; // 分部Ids
	private Integer[] departmentIds; // 部门Ids
	private Integer[] receiveUserIds; // 收件人ids

	@Override
	public String execute() throws Exception {
		String result = null ;
		try {
			result = (String)Services.getReceiveService().executeTransactional(new TransactionalCallBack() {

				@Override
				public Object execute(IService arg0) {
					LOGGER.warn("开启事物了....");
					return doInsertMailObject();
				}
			});
		} catch (Exception e) {
			LOGGER.warn("事物回滚了.....");
			msg = this.msg;
			success = false;
			code = "404";
			json = "null";
			return Results.GLOBAL_FORM_JSON;
		}
		return result;

	}

	private String doInsertMailObject() {
		// 校验参数
		Assert.notNull(userId, "用户ID不能为空");
		Assert.notNull(mailId, "传阅ID不能为空");

		try {
			// 根据传阅ID查询数据
			Mail mail = Services.getMailService().findById(mailId);

			if (mail == null) {
				success = false;
				msg = "该传阅记录不存在!";
				code = "405";
				json = "null";
				return Results.GLOBAL_FORM_JSON;
			}

			if(mail.getStatus() == ConstantUtils.MAIL_DELETE_STATUS || mail.getReceives().size() == 0){
				success = false;
				msg = "您打开的传阅已过期～";
				code = "403";
				json = "null";
				return Results.GLOBAL_FORM_JSON;
			}

			// 判断该传阅是否有权限再次添加新的传阅对象
			Boolean ifAdd = mail.getIfAdd();
			if (!ifAdd) {
				success = false;
				msg = "该传阅没有权限再次新增传阅对象";
				code = "206";
				json = "null";
				return Results.GLOBAL_FORM_JSON;
			}

			if(mail.getStepStatus() == ConstantUtils.MAIL_COMPLETE_STATUS){ // 已完成的传阅 不能进行添传阅对象操作
				success = false;
				msg = "已完成的传阅不能新增传阅对象";
				code = "206";
				json = "null";
				return Results.GLOBAL_FORM_JSON;
			}

			// 把接收人的名称拼接成一个字符串
			Set<UserMssage> userMssageSet = new HashSet<>();
			try {

				int id = (int)mail.getUserId();

				if (subcompanyIds != null) { // 不为 null, 表示添加了分部
					// 根据分部ID查询信息
					List<Hrmsubcompany> hrmsubcompanyList = Services.getHrmsubcompanyService().findByIds(subcompanyIds);
					for (Hrmsubcompany hrmsubcompany : hrmsubcompanyList) {
						HrmUtils.getSubcompanyUserMssage(hrmsubcompany.getId(), null, userMssageSet, null, id);
					}
				}else if(departmentIds != null){

					List<Hrmdepartment> hrmdepartmentList = Services.getHrmdepartmentService().findByIds(departmentIds);
					HrmUtils.getSubcompanyUserMssage(null, hrmdepartmentList, userMssageSet, null, id);

				}else if(receiveUserIds != null) {

					// 查询接收人信息
					List<UserMssage> userMssages  = Services.getUserMssageService().createHelper().getUserId().In(receiveUserIds)
							.list();

					for (UserMssage userMssage : userMssages) {
						if (userMssage.getUserId() == id) { // 如果接受人中有发件人, 直接跳过
							continue;
						}
						userMssageSet.add(userMssage);
					}


				}else  if (userTotal == 1) { // 表示整个组织架构
					List<UserMssage> mssageList = Services.getUserMssageService().createHelper().startOr().getStatus().Eq(ConstantUtils.OA_USER_PROBATION_STATUS)
							.getStatus().Eq(ConstantUtils.OA_USER_OFFICIAL_STATUS).getStatus().Eq(ConstantUtils.OA_USER_TEMPORARY_STATUS)
							.getStatus().Eq(ConstantUtils.OA_USER_PROBATION_DELAY_STATUS)
							.stopOr().list();
					for (UserMssage userMssage : mssageList) {
						if (userMssage.getUserId() == id) { // 如果接受人中有发件人, 直接跳过
							continue;
						}
						userMssageSet.add(userMssage);
					}

				}else {
					msg = "请选择联系人!";
					success = false;
					code = "205";
					json = "null";
					return Results.GLOBAL_FORM_JSON;
				}

			} catch (Exception e) {
				e.printStackTrace();
				msg = "请添加存在的联系人!";
				success = false;
				code = "205";
				json = "null";
				return Results.GLOBAL_FORM_JSON;
			}

			if (userMssageSet.size() == 0 && userMssageSet.isEmpty()) {
				msg = "传阅对象不能是发件人";
				success = false;
				code = "205";
				json = "null";
				return Results.GLOBAL_FORM_JSON;
			}


			// 创建list集合, 用于存储user
			List<UserMssage> list = new ArrayList<>(userMssageSet);
			// 获取接收人的数据
			List<Receive> receivesList = mail.getReceives();
			if (userTotal == 0) {

				String receiveLastName = "";
				// 进行迭代
				Iterator<UserMssage> it = list.iterator();
				while (it.hasNext()) {
					UserMssage next = it.next();
					// 遍历接收人集合
					for (Receive receive : receivesList) {
						// 取出接收人userId, 进行判断
						if (next.getUserId() == receive.getUserId()) {
							// 如果相同, 就把这个元素删除
							it.remove();
							receiveLastName += receive.getLastName() + ", ";
						}
					}
				}

				int size = list.size();
				if (size == 0) {
					receiveLastName = receiveLastName.substring(0, receiveLastName.length() - 2);
					success = true;
					msg = "该联系人 " + receiveLastName + " 已经是传阅对象了!";
					code = "404";
					json = "null";
					return Results.GLOBAL_FORM_JSON;
				}
			}else if(userTotal == 1){
				// 如果是添加整个组织架构, 则删除原有的接收人.
				Services.getReceiveService().deleteList(receivesList);
			}


			Assert.notNull(list, "接收人ID不能为空");


			// 拼接字符串
			StringBuilder sb = new StringBuilder();

			// 定义一个标记
			boolean res = false;
			// 添加新的收件人
			int num = 0;
			ReceiveBean bean = Services.getReceiveService().createHelper().bean();
			for (UserMssage userMssage : list) {

				try {
					// 新增收件人记录
					bean.create().setMail(mail).setUserId(userMssage.getUserId())
							.setLastName(userMssage.getLastName()).setLoginId(userMssage.getLoginId())
							.setWorkCode(userMssage.getWorkCode()).setSubcompanyName(userMssage.getFullName())
							.setDepartmentName(userMssage.getDeptFullname()).setJoinTime(new Date()).setReceiveTime(new Date())
							.setReceiveStatus(ConstantUtils.RECEIVE_NOTOPEN_STATUS)
							.setStepStatus(ConstantUtils.RECEIVE_AWAIT_STATUS).setMailState(ConstantUtils.RECEIVE_UNREAD_STATUS)
							.setIfConfirm(false).setReDifferentiate(userId);

					if (num == 100) {
						LOGGER.warn("传阅详情 -- 开始新增接收人数据:::::::::: 一次一百条.");
						bean.insert();
						LOGGER.warn("传阅详情 -- 新增后, 进行睡眠 两秒:::::::::::::::::::");
						Thread.sleep(2000);
						bean = Services.getReceiveService().createHelper().bean();
						num = 0;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				sb.append(";").append(userMssage.getLastName());
				res = true;
			}

			if (num < 100) {
				LOGGER.warn("传阅详情 --  一次性新增 " + num + "条接收人数据!");
				bean.insert();
			}

			if (res) { // true 表示联系人增加成功

//				根据需求: 重新确认只有在添加附件的时候才会有 (2019-1-4 10:55  徐经理)
				// 取出该传阅的联系人
//				List<Receive> receives = mail.getReceives();
//				int count = 0;
				// 遍历
//				for (Receive receive : receives) {
//					// 判断,只要是已经确认该传阅的联系人, 就需要重新确认
//					if (receive.getIfConfirm() == true) {
//						count++;
//						// 设置开启重新确认
//						receive.setAfreshConfim(true); // true 为开启重新确认
//						// 更新
//						Services.getReceiveService().update(receive);
//
//						if (count == 1) {
//							msg = msg + "  已经开启重新确认!";
//						}
//					}
//				}

				String allReceiveName = mail.getAllReceiveName();
				String allName = sb.toString();
				mail.setAllReceiveName(allReceiveName + allName);
				if(userTotal == 1){
					mail.setAllReceiveName(allName);
				}
				Services.getMailService().update(mail);

				code = "200";
				json = "null";
				success = true;
				msg = this.msg != null ? this.msg : "再次添加联系人成功!";
				return Results.GLOBAL_FORM_JSON;
			}
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			msg = "网络繁忙,请稍后再试!";
			code = "4000";
			json = "null";
			return Results.GLOBAL_FORM_JSON;
		}
		success = false;
		msg = "添加联系人失败!";
		code = "404";
		json = "null";
		return Results.GLOBAL_FORM_JSON;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getMailId() {
		return mailId;
	}

	public void setMailId(Long mailId) {
		this.mailId = mailId;
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

	public Integer[] getReceiveUserIds() {
		return receiveUserIds;
	}

	public void setReceiveUserIds(Integer[] receiveUserIds) {
		this.receiveUserIds = receiveUserIds;
	}
}
