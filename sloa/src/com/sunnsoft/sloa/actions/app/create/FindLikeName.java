package com.sunnsoft.sloa.actions.app.create;

import com.sunnsoft.sloa.actions.common.BaseParameter;
import com.sunnsoft.sloa.config.Config;
import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.UserMssage;
import com.sunnsoft.sloa.util.LenovoCloudSDKUtils;
import com.sunnsoft.util.struts2.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 根据文件名字进行模糊搜索.
 * 
 * @author chenjian
 *
 */
public class FindLikeName extends BaseParameter {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(FindLikeName.class);
	
	private String likeName; // 搜索条件
	private String searchPath; // 搜索路径(即当前文件夹目录)
	private String pathType; // self 表示 上传到个人空间 ent 上传到企业空间  share_in: 收到的分享   share_out : 我的分享
	private Integer userId; //当前用户ID
	private Integer page; // 当前页
	private Integer pageRows; // 当前页显示记录数

	@Resource
	private Config config;
	
	@Override
	public String execute() throws Exception {

		// 校验参数
		Assert.notNull(likeName, "查询条件不能为null!");
		Assert.notNull(searchPath, "搜索路径(即当前文件夹目录)不能为null!");

		// 设置默认参数
		if(page <= 0) {
			page = 0; // 1
		}
		
		pageRows = 10; // 10

		UserMssage userMssage = Services.getUserMssageService().createHelper().getUserId().Eq(userId).uniqueResult();
		
		//根据SSO登录获取当前用户的session
		String session = null;
		String s = null;
		try {
			Map<String, Object> result = LenovoCloudSDKUtils.getSessionAndS(userMssage.getLoginId());
			session = (String) result.get("session");
			s = (String) result.get("s");
			String code = (String) result.get("code");
			if(code.equals("401")) {
				success = false;
				code = "4000";
				msg = "请开通网盘账号";
				json = "null";
				return Results.GLOBAL_FORM_JSON;
			}
		} catch (Exception e1) {
			LOGGER.warn("根据LoginId获取session失败: :::::::::" + session);
			e1.printStackTrace();
		}
				
		try {
			
			if(searchPath != null && !searchPath.equals("")) {
				// 截取
				searchPath = searchPath.substring(1, searchPath.length());
			}
			// 进行编码
			searchPath = URLEncoder.encode(searchPath, "UTF-8");
			likeName = URLEncoder.encode(likeName, "UTF-8");
			
			if(page > 0) {
				page = page * pageRows;
			}
			
			String appSearch = LenovoCloudSDKUtils.getAPPSearch(session, s, searchPath, pathType, pageRows, page, "desc", likeName);
			success = true;
			code = "200";
			msg = "查询文件列表成功!";
			json = appSearch;
			return Results.GLOBAL_FORM_JSON;

		} catch (Exception e) {
			e.printStackTrace();
			success = false;
			code = "4000";
			msg = "网络繁忙!";
			json = "null";
			return Results.GLOBAL_FORM_JSON;
		}
//		success = false;
//		code = "404";
//		msg = "内容为空或查询失败!";
//		json = "null";
//		return Results.GLOBAL_FORM_JSON;
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
	 
	public static <F> List<F> getpageList(List<F> f, int pageNo, int dataSize) {
		/*
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
		/*
		 * 进行分页处理,采用for循环的方式来进行处理
		 * 
		 * 首先for循环中，i应该从哪里开始:i应该从 (当前是第几页 -1 乘以 条数) 开始
		 * 
		 * 然后for循环应该到哪里结束，也就是i应该小于:判断(开始的索引+显示条数)是不是大于总条数，如果大于就是总条数，如果小于就是(开始的索引+显示条数)
		 * 
		 * 然后让i++
		

		//int i = (pageNo - 1) * dataSize; 
		
		/*int s = i < (((pageNo - 1) * dataSize) + dataSize > totalitems ? totalitems
				: ((pageNo - 1) * dataSize) + dataSize); i++
				
		for (int i = (pageNo - 1) * dataSize; i < (((pageNo - 1) * dataSize) + dataSize > totalitems ? totalitems
				: ((pageNo - 1) * dataSize) + dataSize); i++) {
			// 然后将数据存入afterList中

			afterList.add(f.get(i));
		}

		// 然后将处理后的数据集合进行返回
		return afterList;
	}*/

	public String getLikeName() {
		return likeName;
	}

	public void setLikeName(String likeName) {
		this.likeName = likeName;
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

	public String getSearchPath() {
		return searchPath;
	}

	public void setSearchPath(String searchPath) {
		this.searchPath = searchPath;
	}

	public String getPathType() {
		return pathType;
	}

	public void setPathType(String pathType) {
		this.pathType = pathType;
	}
	
}
