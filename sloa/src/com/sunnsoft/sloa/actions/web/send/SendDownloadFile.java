package com.sunnsoft.sloa.actions.web.send;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.gteam.util.FastJSONUtils;
import org.springframework.util.Assert;

import com.lenovo.css.lenovocloud.sdk.LenovoCloudSDK;
import com.lenovo.css.lenovocloud.sdk.model.PathType;
import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.config.Config;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.AttachmentItem;
import com.sunnsoft.sloa.db.vo.Mail;
import com.sunnsoft.sloa.util.LenovoCloudSDKUtils;
import com.sunnsoft.util.FileStore;
import com.sunnsoft.util.struts2.Results;

/**
 * 已发传阅--传阅详情页面--附件列表---点击下载附件--从网盘上下载附件. 批量下载,  已压缩包的形式
 * 
 * @author chenjian
 *
 */
public class SendDownloadFile extends BaseParameter {

	private static final long serialVersionUID = 1L;

	private Long[] itemId; // 附件ID
	
	@Resource
	private Config config;
	
	@Resource
	private FileStore generalFileStore;

	@Action(interceptorRefs = { @InterceptorRef(value = "fileUpload"), @InterceptorRef("extStack") })
	@Override
	public String execute() throws Exception {

		// 校验参数
		Assert.notNull(itemId, "附件ID不能为空!");


		// 联系网盘(正式环境地址)
		LenovoCloudSDK sdk = LenovoCloudSDKUtils.getLenovoCloudSDK(config);
		
		//使用网盘统一账号 system_OA 进行登录(只限于web端)
		String session = LenovoCloudSDKUtils.getLenovoCloudSDKSession(sdk, config);

		StringBuilder sb = new StringBuilder();
		Integer differentiate = null;
		String fileName = "";
		for (Long l : itemId) {

			AttachmentItem attachmentItem = Services.getAttachmentItemService().createHelper().getItemId().Eq(l)
					.uniqueResult();
			//用于重新命名压缩包
			Mail mail = attachmentItem.getMail();
			fileName = mail.getTitle();
			
			// 获取附件上传的URL
			String urlPath = attachmentItem.getUrlPath();

			// 拼接
			sb.append(urlPath).append(",");

			differentiate = attachmentItem.getItemDifferentiate();
		}

		String path = sb.toString().substring(0, sb.toString().length() - 1);

		InputStream fileArchivesDownload = null;
		
		// 判断是   个人空间  还是  企业空间
		if (differentiate == 0) {
			// 返回一个流
			fileArchivesDownload = sdk.fileArchivesDownload(path, session, null, "", PathType.SELF);
		}
		if (differentiate == 1) {

			fileArchivesDownload = sdk.fileArchivesDownload(path, session, null, "", PathType.ENT);
		}
		// 生成UUID
		String newImageName = UUID.randomUUID().toString() + "." + "zip";
		// 构建路径
		File storeFile = new File(generalFileStore.getRootFile(), newImageName);
		// 下载文件
		inputstreamtofile(fileArchivesDownload, storeFile);

		//进行设置:  download ---> 设置为 true 表示 强制浏览器下载附件并且不打开
		//		   rename   ---> 表示对下载的附件进行重命名, 并且进行编码
		//String encode = URLEncoder.encode(fileName, "UTF-8");
		//进行编码
		HttpServletRequest request = ServletActionContext.getRequest();
		if(request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
			fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1"); // firefox浏览器 
			fileName = URLEncoder.encode(fileName, "UTF-8");
			//System.out.println("火狐浏览器");
		}else {
			// IE浏览器, 需要进行二次编码
			fileName = URLEncoder.encode(fileName, "UTF-8");
			fileName = URLEncoder.encode(fileName, "UTF-8");
			//System.out.println("其他浏览器");
		}
		
		json = FastJSONUtils.getJsonHelper().toJSONString("/file/" + storeFile.getName() + "?download=true&rename=" + fileName + "." + "zip");
		
		// 判断
		if (json != null) {
			success = true;
			code = "200";
			msg = "下载附件成功!";
			return Results.GLOBAL_FORM_JSON;
		}
		success = true;
		code = "404";
		msg = "下载附件失败!";
		json = "null";
		return Results.GLOBAL_FORM_JSON;
	}

	/**
	 *
	 * @param ins
	 *            输入文件流
	 * @param file
	 *            要保存的文件路径
	 */
	public static boolean inputstreamtofile(InputStream ins, File file) {
		boolean status = false;
		try {
			OutputStream os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
				if (bytesRead > -1) {
					status = true;
				}
			}
			os.close();
			ins.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	public Long[] getItemId() {
		return itemId;
	}

	public void setItemId(Long[] itemId) {
		this.itemId = itemId;
	}
}
