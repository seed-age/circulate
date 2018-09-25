package com.sunnsoft.sloa.schedule;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.sunnsoft.sloa.service.UserService;
import com.sunnsoft.util.FileStore;

@Service
public class TestSchedule {

	@Resource
	private UserService userService;
	
	@Value("${schedule.on}")
	private boolean scheduleOn;

	/*@PostConstruct
	private void init() {
		this.doJob();
	}*/

	@Resource
	private FileStore generalFileStore;
	
	/*@Scheduled(cron = "0 45 1 * * ?")*/
	/*@Scheduled(cron = "0 * * * * ?")*/
	public void doJob() {
		if(!scheduleOn){//开关没打开则不跑定时任务。
			return;
		}
	}
}
