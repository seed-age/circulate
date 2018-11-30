package com.sunnsoft.sloa.actions.web.createmail;

import com.lenovo.css.lenovocloud.sdk.LenovoCloudSDK;
import com.lenovo.css.lenovocloud.sdk.model.FileGetIdModel;
import com.lenovo.css.lenovocloud.sdk.model.PathType;
import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.config.Config;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.AttachmentItem;
import com.sunnsoft.sloa.db.vo.Mail;
import com.sunnsoft.sloa.util.LenovoCloudSDKUtils;
import com.sunnsoft.util.struts2.Results;
import org.gteam.db.dao.TransactionalCallBack;
import org.gteam.service.IService;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

/**
 * 新建传阅--删除附件: 点击删除, 去网盘上查询该附件的信息, 并删除该附件(本地删除, 网盘上的不做删除).
 *
 * @author chenjian
 *
 */
public class DeleteUpload extends BaseParameter {

	private static final long serialVersionUID = 1L;

	private Long userId; // 用户ID
	private Long itemId; // 附件ID
	private Long mailId; // 附件ID

	@Resource
	private Config config;

	@Override
	public String execute() throws Exception {
		String result = null;

		try {
			result = (String)Services.getAttachmentItemService().executeTransactional(new TransactionalCallBack() {

				@Override
				public Object execute(IService arg0) {
					System.out.println("开启事物了....");
					String delUpload = null;
					try {
						delUpload = getDelUpload();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return delUpload;
				}
			});
		} catch (Exception e) {
			System.out.println("事物回滚了.....");
			e.printStackTrace();
			success = false;
			code = "4000";
			msg = "删除附件失败!";
			json = "null";
			return Results.GLOBAL_FORM_JSON;
		}
		return result;
	}

	public String getDelUpload() throws Exception {

		// 校验参数
		Assert.notNull(userId, "用户ID不能为空!");
		Assert.notNull(itemId, "附件ID不能为空!");

		// 判断: 如果mailId为空 表示在新建传阅中进行删除上传的附件
		if (mailId == null) {
			// 直接根据itemId去数据库中查询, 查询到直接删除
			Services.getAttachmentItemService().deleteById(itemId);

			success = true;
			code = "200";
			msg = "删除附件成功!";
			json = "null";
			return Results.GLOBAL_FORM_JSON;
		}

		Assert.notNull(mailId, "传阅ID不能为空!");

		// 根据传阅ID查询传阅数据
		Mail mail = Services.getMailService().findById(mailId);

		// 根据文件ID查询文件信息
		AttachmentItem attachmentItem = Services.getAttachmentItemService().createHelper().getItemId().Eq(itemId)
				.uniqueResult();

		// 进行校验, 判断该用户是否用权限删除这个附件
		// 如果该附件是当前用户上传的, 或是 当前用户是发件人. 就进来
		if (attachmentItem.getUserId().equals(userId) || mail.getUserId() == userId.longValue()) {
			// 执行删除
			Services.getAttachmentItemService().delete(attachmentItem);

			List<AttachmentItem> attachmentItems = mail.getAttachmentItems();
			if (attachmentItems.size() == 1 || attachmentItems.size() == 0) {
				mail.setHasAttachment(false);// 设置
			}
			// 更新
			Services.getMailService().update(mail);
			success = true;
			code = "200";
			msg = "删除附件成功!";
			json = "null";
			return Results.GLOBAL_FORM_JSON;
		} else {

			success = false;
			code = "205";
			msg = "你没有权限删除该附件!";
			json = "null";
			return Results.GLOBAL_FORM_JSON;
		}
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getMailId() {
		return mailId;
	}

	public void setMailId(Long mailId) {
		this.mailId = mailId;
	}

}
