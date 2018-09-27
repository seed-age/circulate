package com.sunnsoft.sloa.schedule;

import com.sunnsoft.sloa.db.handler.Services;
import com.sunnsoft.sloa.db.vo.AttachmentItem;
import com.sunnsoft.sloa.service.AttachmentItemService;
import com.sunnsoft.util.FileStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileFilter;
import java.util.List;

/**
 * 1. 如果有用户在新建传阅的时候上传了附件, 但是又因为网络原因或是人为原因, 直接终止了浏览器(PC端)或是APP端. 但是上传的附件是会保存到数据库中的.
 * 2. 如果是这样, 就定义一个定时任务, 每天凌晨 1点30分 定时删除这些没有指定是那个传阅的附件记录.
 * 
 * @author chenjian
 *
 */
@Component
public class DeleteFileSchedule {

	@Resource
	private AttachmentItemService attachmentItemService;

	@Value("${schedule.on}")
	private boolean scheduleOn;

	@Resource
	private FileStore generalFileStore;
	
	@Scheduled(cron = "0 30 1 * * ?")
	public void doJob() {
		if (!scheduleOn) {// 开关没打开则不跑定时任务。
			return;
		}
//		System.out.println("定时任务被调用了!! --->附件");
		List<AttachmentItem> list = attachmentItemService.createHelper().getMail().Eq(null).list();
		
		int count = 0;
		boolean item = false;
		
		if(list.size() > 0) {
			for (AttachmentItem attachmentItem : list) {
				
				//再次判断是否关联了传阅表
				if(attachmentItem.getMail() == null) {
					
					//删除
					Services.getAttachmentItemService().delete(attachmentItem);
					count++;
					item = true;
				}
				
			}
		}
		
		if(count == list.size() && item) {
			System.out.println("定时删除没有关联传阅表的附件记录成功!");
		}else {
			System.out.println("目前暂时没有查询到没有关联传阅表的附件记录!");
		}
		
//		System.out.println("定时清除没用的附件信息完成!");
		
		
//		System.out.println("定时清除APP端下载的文件, 超过七天时间就会被删除!!");
		
		generalFileStore.getRootFile().listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File file) {
//				System.out.println("当前时间戳： " + System.currentTimeMillis());
//				System.out.println("最后修改文件的时间戳： " + file.lastModified());
//				System.out.println("结果： " + (System.currentTimeMillis() - file.lastModified()));
				
				if(System.currentTimeMillis() - file.lastModified() > 7*3600*2400*1000) {//最后修改时间大于1周的。
					System.out.println(file.getName() + "删除了。。。。。。。。。。。。。。。。。。");
					file.delete();
				}else {
					System.out.println(file.getName() + "没有合适的文件可以删除。。。。。。。。。。。。。。。。。。");
				}
				return true;
			}
		});
		
//		System.out.println("定时清除 APP端下载的文件 完成!!!!!");
	}
}
