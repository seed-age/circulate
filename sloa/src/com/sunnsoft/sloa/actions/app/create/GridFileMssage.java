package com.sunnsoft.sloa.actions.app.create;

import com.alibaba.fastjson.JSONObject;
import com.lenovo.css.lenovocloud.sdk.LenovoCloudSDK;
import com.lenovo.css.lenovocloud.sdk.model.FileListModel;
import com.lenovo.css.lenovocloud.sdk.model.FileModel;
import com.lenovo.css.lenovocloud.sdk.model.PathType;
import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.config.Config;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.UserMssage;
import com.sunnsoft.sloa.util.LenovoCloudSDKUtils;
import com.sunnsoft.util.struts2.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 新建传阅--去网盘上查询附件信息: 获取个人空间文件列表, 数据以分页形式返回.
 * 
 * @author chenjian
 *
 */
public class GridFileMssage extends BaseParameter {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(GridFileMssage.class);

	private Integer userId; // 用户ID
	private String path; // 表示路径
	private String pathType; // self 表示 上传到个人空间 ent 上传到企业空间
	private Integer page; // 当前页
	private Integer pageRows; // 当前页显示记录数

	@Resource
	private Config config;

	@Override
	public String execute() throws Exception {

		// 设置默认参数
		if (page == null) {
			page = 0; // 0 : 网盘的第一页是 从 0 开始
		}
		pageRows = 10; // 10

		// 设置url(测试地址)
		LenovoCloudSDK sdk = LenovoCloudSDKUtils.getLenovoCloudSDK(config);
		
		UserMssage userMssage = Services.getUserMssageService().createHelper().getUserId().Eq(userId).uniqueResult();
		// 根据SSO登录获取当前用户的session
		String session = null;
		try {
			session = LenovoCloudSDKUtils.getSession(userMssage);
			if(session.equals("401")) {
				success = false;
				code = "500";
				msg = "请开通网盘账号";
				json = "null";
				return Results.GLOBAL_FORM_JSON;
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			LOGGER.warn("根据LoginId获取session失败: :::::::::" + session);
			/*// 设置正式账号和密码
			String userSlug = config.getUserSlug();
			String password = config.getPassword();
			// 用户登录(测试用户)
			// 调用登录方法, 设置账户和密码
			UserLoginModel login = sdk.login(userSlug, password);
			session = login.getXLENOVOSESSID();*/
		}

		//PageBean pb;
		try {
			// 调用网盘接口, 查询文件信息
			FileListModel fileListModel = null;
			String encode = URLEncoder.encode(path, "UTF-8");
			if (pathType.equals("self")) {
				fileListModel = sdk.getFileList(encode, page, pageRows, session, PathType.SELF);
			}
			if (pathType.equals("ent")) {

				fileListModel = sdk.getFileList(encode, page, pageRows, session, PathType.ENT);
			}
			List<FileModel> content = fileListModel.getContent();
			// 获取这次查询的文件的总数
			long totalSize = fileListModel.getTotalSize();
			LOGGER.warn("总数: " + totalSize);
			// 计算出总页数
			int totalPage = ((int) totalSize + pageRows - 1) / pageRows;
			if (page > totalPage || content == null) {
				success = true;
				code = "200";
				msg = "已经是最后一页了!";
				json = "null";
				return Results.GLOBAL_FORM_JSON;
			}
			if (totalSize > 0) {
				// 当前集合总数
				//int size = content.size();
				// 创建存储容器
				Map<String, Object> map = new HashMap<>();
				map.put("pageNum", page);// 添加当前页
				map.put("pageSize", pageRows); // 添加每页显示数
				map.put("totalPage", totalPage); // 添加总页数
				map.put("totalSize", totalSize); // 总记录数
				//List<FileModel> pageList = getpageList(content, page, pageRows);
				map.put("list", content);
				map.put("listSize", content.size());

				success = true;
				code = "200";
				msg = "查询文件列表成功!";
				json = JSONObject.toJSONString(map);
				return Results.GLOBAL_FORM_JSON;
			}
		} catch (Exception e) {
			success = false;
			code = "500";
			msg = "网络繁忙,请稍后重试!";
			json = "null";
			e.printStackTrace();
			return Results.GLOBAL_FORM_JSON;
		}

		success = false;
		code = "404";
		msg = "已经是最后一页了";
		json = "null";
		return Results.GLOBAL_FORM_JSON;
	}

	/**
	 * 数据集合的分页方法，根据传入总共的数据跟页码，返回页码所需要显示多少条的数据 <BR/>
	 * 采用泛型的方法，即为，list中为什么类型的数据就返回什么类型的数据
	 * 
	 * @param f
	 *            带有需要进行分页的数据集合
	 * @param pageNo
	 *            第几页
	 * @param dataSize
	 *            显示多少条数据
	 * @return 进过分页之后返回的数据
	 *//*
	public static <F> List<F> getpageList(List<F> f, int pageNo, int dataSize) {
		
		 * 经过测试发现当pageNo为0或者小于时，也就是第0页时，程序会报错，所以需要处理一下pageNo的值
		 * 
		 * 先进行空值的判断，避免程序出现null异常
		 * 
		 * 当pageNo的值小于等于0时，我们让它的值为1
		 
		// 参数的校验
		if (f == null) {// 当传入过来的list集合为null时，先进行实例化
			f = new ArrayList<F>();
		}
		if ((Object) pageNo == null) {// 当传入过来的pageNo为null时，先进行赋值操作
			pageNo = 1;
		}
		if ((Object) dataSize == null) {// 当传入过来的dataSize为null时，先进行赋值操作
			dataSize = 1;
		}
		if (pageNo <= 0) {
			pageNo = 1;
		}

		// 记录一下数据一共有多少条
		int totalitems = f.size();
		// 实例化一个接受分页处理之后的数据
		List<F> afterList = new ArrayList<F>();
		
		 * 进行分页处理,采用for循环的方式来进行处理
		 * 
		 * 首先for循环中，i应该从哪里开始:i应该从 (当前是第几页 -1 乘以 条数) 开始
		 * 
		 * 然后for循环应该到哪里结束，也就是i应该小于:判断(开始的索引+显示条数)是不是大于总条数，如果大于就是总条数，如果小于就是(开始的索引+显示条数)
		 * 
		 * 然后让i++
		 

		for (int i = (pageNo - 1) * dataSize; i < (((pageNo - 1) * dataSize) + dataSize > totalitems ? totalitems
				: ((pageNo - 1) * dataSize) + dataSize); i++) {
			// 然后将数据存入afterList中

			afterList.add(f.get(i));
		}

		// 然后将处理后的数据集合进行返回
		return afterList;
	}
*/
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPathType() {
		return pathType;
	}

	public void setPathType(String pathType) {
		this.pathType = pathType;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageRows() {
		return pageRows;
	}

	public void setPageRows(Integer pageRows) {
		this.pageRows = pageRows;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}
