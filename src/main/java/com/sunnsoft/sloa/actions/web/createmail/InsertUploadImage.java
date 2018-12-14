package com.sunnsoft.sloa.actions.web.createmail;

import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.util.FileStore;
import com.sunnsoft.util.ImageUtils;
import com.sunnsoft.util.struts2.Results;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.gteam.util.FastJSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;

/**
 * 新建传阅--富文本上传图片
 * @author chenjian
 *
 */
public class InsertUploadImage extends BaseParameter {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(InsertUploadSdk.class);

	private File fileData; // 二进制图片对象
	private String fileDataFileName; // 图片名称;

	@Resource
	private FileStore generalFileStore;

	@Override
	@Action(interceptorRefs = {
			@InterceptorRef(value = "fileUpload", params = { "maximumSize", "504800000" }),
			@InterceptorRef("extStack") })
	public String execute() throws Exception {

		// 校验参数
		Assert.notNull(fileData, "上传的附件不能为空!");

		// 参数校验;
		if (StringUtils.isBlank(fileDataFileName) || fileDataFileName.lastIndexOf(".") == -1) {
			msg = "不支持的图片类型或非法的图片后缀!!!";

			return Results.GLOBAL_FAILURE_JSON;
		}

		// 校验上传文件是否为图片;
		if (!ImageUtils.isImage(fileData)) {
			msg = "不支持的图片类型或非法的图片后缀!!!";

			return Results.GLOBAL_FAILURE_JSON;
		}

		// 获取上传图片后缀;
		String suffix = fileDataFileName.substring(fileDataFileName.lastIndexOf("."));

		// 重新生成上传之后的图片名称;
		String newImageName = UUID.randomUUID().toString() + suffix;
		File storeFile = new File(generalFileStore.getRootFile(), newImageName);
		if (storeFile.exists()) {
			msg = "图片名称冲突,请稍后再试!!!";

			return Results.GLOBAL_FAILURE_JSON;
		}
		// 复制文件
		FileUtils.copyFile(fileData, storeFile);

		success = true;
		msg = "图片上传成功";
		json = FastJSONUtils.getJsonHelper().toJSONString("/file/" + storeFile.getName());

		return Results.GLOBAL_FORM_JSON;
	}

	public File getFileData() {
		return fileData;
	}

	public void setFileData(File fileData) {
		this.fileData = fileData;
	}

	public String getFileDataFileName() {
		return fileDataFileName;
	}

	public void setFileDataFileName(String fileDataFileName) {
		this.fileDataFileName = fileDataFileName;
	}
}
