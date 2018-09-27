package com.sunnsoft.sloa.util;

import com.sunnsoft.sloa.auth.SystemUser;
import com.sunnsoft.sloa.db.vo.SystemLog;
import com.sunnsoft.sloa.db.vo.User;
import com.sunnsoft.sloa.service.SystemLogService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.Executor;

@Service
public class SysLogger {
	
	@Resource
	private SystemLogService systemLogService;
	
	@Resource
	private Executor threadPool;
	
	public void log(Object... operation){
		final SystemLog log = new SystemLog();
		StringBuilder logString = new StringBuilder();
		for (int i = 0; i < operation.length; i++) {
			logString.append(operation[i]).append(" ");
		}
		log.setAction(logString.toString());
		SecurityContext context = SecurityContextHolder.getContext();
		if(context != null){
			Authentication authentication = context.getAuthentication();
			if(authentication != null){
				Object details = authentication.getDetails();
				if(details instanceof WebAuthenticationDetails){
					WebAuthenticationDetails d = (WebAuthenticationDetails)details;
					log.setIp(d.getRemoteAddress());
					Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					if(o instanceof SystemUser){
						SystemUser user = (SystemUser)o;
						User userVo = user.getUserVo();
						log.setOperator(userVo.getNickName());
						log.setIdentityInfo(userVo.getNickName()+"["+userVo.getAccountName()+"],id:"+userVo.getUserId());
					}
				}
			}
		}
		if(log.getIp() == null){
			log.setIp("本机线程调用");
		}
		log.setLogTime(new Date());
		threadPool.execute(new Runnable(){

			@Override
			public void run() {
				systemLogService.add(log);
				//System.out.println("xxxxxx");
			}
			
		});
	}
	
}
