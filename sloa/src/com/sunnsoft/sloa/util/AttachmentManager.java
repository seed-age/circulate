package com.sunnsoft.sloa.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.sunnsoft.sloa.auth.SystemUser;
import com.sunnsoft.sloa.db.vo.AttachmentItem;
import com.sunnsoft.sloa.helper.AttachmentItemHelper;
import com.sunnsoft.sloa.service.AttachmentItemService;
import com.sunnsoft.util.FileStore;

import org.gteam.db.helper.hibernate.Each;

@Service
@SuppressWarnings("unchecked")
public class AttachmentManager {
	
	@Resource
	private AttachmentItemService attachmentItemService;
	@Resource
	private FileStore generalFileStore;
	@Resource
	private FileStore generalFileTempStore;	
	
	@Resource
	private FileStore userTempStore;
	
	
	
	/**
	 * 把临时附件集合和Bean绑定，使之成为该Bean的固定集合，假如该Bean已经有固定的附件集合，则合并到Bean固定附件集合里。
	 * 和Bean绑定以后，BulkId会被清空，只能使用Bean class和ID组合访问Bean的固定附件集合
	 * 临时附件集合里的每个Item(文件Bean)所属的分类category，绑定到Bean的是时候，保持不变。
	 * @param beanClass
	 * @param beanId
	 * @param bulkId 
	 */
	/*public void saveAttachmentToBean(Class beanClass,Serializable beanId,String bulkId){
		saveAttachmentToBean(beanClass,beanId,bulkId,null);
	}*/
	
	/**
	 * 把临时附件集合和Bean绑定，使之成为该Bean的固定集合，假如该Bean已经有固定的附件集合，则合并到Bean固定附件集合里。
	 * 和Bean绑定以后，BulkId会被清空，只能使用Bean class和ID组合访问Bean的固定附件集合
	 * 临时附件集合里的每个Item(文件Bean)所属的分类category，绑定到Bean的是时候，将会设置为参数指定的category。
	 * @param beanClass
	 * @param beanId
	 * @param bulkId
	 * @param category 分类，长度不能超过25个字符。
	 */
	/*public void saveAttachmentToBean(Class beanClass,Serializable beanId,String bulkId,String category){
		final String categoryStr = category;
		final String beanClassStr = beanClass.toString();
		final String beanIdStr = beanId.toString();
		
		new AttachmentItemHelper(this.attachmentItemService).getBulkId().Eq(bulkId).each(new Each<AttachmentItem>(){
			@Override
			public void each(AttachmentItem arg0, List<AttachmentItem> arg1) {							
				try {//文件转移	
					FileUtils.moveFile(generalFileTempStore.getFile(arg0.getSaveDir()==null?"":arg0.getSaveDir()+arg0.getSaveName()), generalFileStore.getFile(arg0.getSaveDir()==null?"":arg0.getSaveDir()+arg0.getSaveName()));
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}				
				if(StringUtils.isNotBlank(categoryStr)){//设置为参数指定的category
					arg0.setFileCategory(categoryStr);
				}
				arg0.setBulkId(null);//清空bulkid
				arg0.setEntityId(beanClassStr+":"+beanIdStr);//设置实体id
				arg0.setAttached(true);
				attachmentItemService.update(arg0);
			}			
		});
	}*/
	
	/*public File getFile(AttachmentItem item){
		if(StringUtils.isBlank(item.getBulkId())){
			return generalFileTempStore.getFile(item.getSaveDir()==null?"":item.getSaveDir()+item.getSaveName());
		}else{
			return generalFileStore.getFile(item.getSaveDir()==null?"":item.getSaveDir()+item.getSaveName());
		}		
	}*/
	
	/*public List<AttachmentItem> getAttachments(Class beanClass,Serializable beanId){
		return getCategoryAttachments(beanClass,beanId,null);
	}*/
	
	/*public List<AttachmentItem> getCategoryAttachments(Class beanClass,Serializable beanId,String category){
		AttachmentItemHelper helper = new AttachmentItemHelper(this.attachmentItemService);
		helper.getEntityId().Eq(beanClass.toString()+":"+beanId.toString());
		if(StringUtils.isNotBlank(category)){
			helper.getFileCategory().Eq(category);
		}		
		return helper.list();
	}*/
	
	public List<AttachmentItem> getAttachmentBulk(String bulkId){
		return getCategoryAttachmentBulk(bulkId,null);
	}
	
	public List<AttachmentItem> getCategoryAttachmentBulk(String bulkId,String category){
		AttachmentItemHelper helper = new AttachmentItemHelper(this.attachmentItemService);
		helper.getBulkId().Eq(bulkId);
		if(StringUtils.isNotBlank(category)){
			helper.getFileCategory().Eq(category);
		}
		return helper.list();
	}
	
	/*public void removeAttachmentItem(Serializable attachmentItemId){
		removeAttachment(AttachmentItem.class,attachmentItemId);
	}
	
	public boolean removeAttachment(Class beanClass,Serializable beanId){
		String str = beanClass.toString()+":"+beanId.toString();
		try{
			AttachmentItem vo = new AttachmentItemHelper(this.attachmentItemService).getEntityId().Eq(str).uniqueResult();
			FileUtils.deleteQuietly(generalFileStore.getFile(vo.getSaveDir()==null?"":vo.getSaveDir()+vo.getSaveName()));
			this.attachmentItemService.delete(vo);
			return true;
		}catch (Exception e) {
			System.out.println("removeAttachment(Class beanClass,Serializable beanId) error");
			return false;
		}		
	}*/
	
	/*public boolean removeAttachment(String bulkId){
		try{
			new AttachmentItemHelper(this.attachmentItemService).getBulkId().Eq(bulkId).each(new Each<AttachmentItem>() {
				@Override
				public void each(AttachmentItem arg0, List<AttachmentItem> arg1) {
					FileUtils.deleteQuietly(generalFileStore.getFile(arg0.getSaveDir()==null?"":arg0.getSaveDir()+arg0.getSaveName()));
					attachmentItemService.delete(arg0);
				}				
			});
			return true;
		}catch (Exception e) {
			System.out.println("removeAttachment(String bulkId) error");
			return false;
		}
	}
	*/
	/*public boolean removeAttachment(Class beanClass,Serializable beanId,String category){
		try{
			AttachmentItem vo = new AttachmentItemHelper(this.attachmentItemService).getEntityId().Eq(beanClass.toString()+":"+beanId.toString()).getFileCategory().Eq(category).uniqueResult();
			FileUtils.deleteQuietly(generalFileStore.getFile(vo.getSaveDir()==null?"":vo.getSaveDir()+vo.getSaveName()));
			this.attachmentItemService.delete(vo);
			return true;
		}catch (Exception e) {
			System.out.println("removeAttachment(Class beanClass,Serializable beanId,String category) error");
			return false;
		}
	}
	*/
	/*public boolean removeAttachment(String bulkId,String category){
		try{
			new AttachmentItemHelper(this.attachmentItemService).getBulkId().Eq(bulkId).getFileCategory().Eq(category).each(new Each<AttachmentItem>() {				
				@Override
				public void each(AttachmentItem arg0, List<AttachmentItem> arg1) {
					FileUtils.deleteQuietly(generalFileStore.getFile(arg0.getSaveDir()==null?"":arg0.getSaveDir()+arg0.getSaveName()));
					attachmentItemService.delete(arg0);
				}
			});			
			return true;
		}catch (Exception e) {
			System.out.println("removeAttachment(String bulkId,String category) error");
			return false;
		}
	}*/
	/**
	 * 创建唯一字符串bulkId标识，上传和添加附件文件需要用此标识进行操作
	 * @return
	 */
	public String generateBulkId(){
		String bulkId = UUID.randomUUID().toString();
		return bulkId.substring(0,8)+bulkId.substring(9,13)+bulkId.substring(14,18)+bulkId.substring(19,23)+bulkId.substring(24); 
	}
	
	/**
	 * 使用bulkId创建临时附件空间，以便操作。
	 * 
	 * @return 。
	 */
	public void createTempAttachment(String bulkId){
//		File tempDir = this.generalFileTempStore.getFile(bulkId);
//		tempDir.mkdir();
//		if(this.attachmentService.createHelper().getBulkId().Eq(bulkId).rowCount() == 0){
//			throw new RuntimeException("重复的bulkId");
//		}
//		this.attachmentService.createHelper().bean().create()
//		.setBulkId(bulkId).setCreateTime(new Date())
//		.insert()
//		;
	}
	
	/*public void addAttachmentToBulk(String bulkId,File file,String fileName){
//		Attachment a = this.attachmentService.createHelper().getBulkId().Eq(bulkId).uniqueResult();
//		if()
		addCategoryAttachmentToBulk(bulkId,null,file,fileName);
		
	}
	*/
	/*public void addCategoryAttachmentToBulk(String bulkId,String category,File file,String fileName){
		addCategoryAttachmentToBulkWithFilePath(bulkId,category,file,fileName,null);
	}

	public void addAttachmentToBulkWithFilePath(String bulkId,File file,String fileName,String filePath){
		addCategoryAttachmentToBulkWithFilePath(bulkId,null,file,fileName,filePath);
	}*/
	
	/*public void addCategoryAttachmentToBulkWithFilePath(String bulkId,String category,File file,String fileName,String filePath){
		AttachmentItem vo = new AttachmentItem();
		vo.setBulkId(bulkId);
		vo.setCreateTime(new Date());
		try{
			vo.setCreator(((SystemUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
		}catch (Exception e) {
			System.out.println("非登录用户，上传附件");
		}
		vo.setFileName(fileName);
		vo.setSaveName(file.getName());
		if(StringUtils.isNotBlank(category)){
			vo.setFileCategory(category);
		}
		if(StringUtils.isNotBlank(filePath)){
			vo.setUrlPath(filePath+file.getName());
			vo.setSaveDir(filePath);
		}else{
			//获取savedir（相对路径）
			String tmpPath = file.getAbsolutePath();
			String rootPath = userTempStore.getRootFile().getAbsolutePath();
			tmpPath = tmpPath.replaceAll(rootPath, "");
			vo.setUrlPath(tmpPath);
			tmpPath = tmpPath.replaceAll(file.getName(), "");			
			vo.setSaveDir(tmpPath);
		}
		
		vo.setAttached(false);
		this.attachmentItemService.add(vo);
	}*/
	
	/**
	 * 定时任务调用的方法，定时清除超时1天的临时文件。释放空间。
	 */
	
	public void tempFileRemove(){
		File tempRootDir = this.generalFileTempStore.getRootFile();
		File[] oldFiles =  tempRootDir.listFiles(new FileFilter(){

			@Override
			public boolean accept(File pathname) {
				long lastModified= pathname.lastModified();
				if(System.currentTimeMillis() - lastModified > 86400){
					return true;
				}
				return false;
			}
			
		});
		for (File file : oldFiles) {
			FileUtils.deleteQuietly(file);
		}
	}
	
}
