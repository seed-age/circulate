package com.sunnsoft.util;

import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.io.File;
import java.io.IOException;

public class UserImageTempPathDeleteSessionListener implements
		HttpSessionListener {

	public void sessionCreated(HttpSessionEvent event) {

	}

	public void sessionDestroyed(HttpSessionEvent event) {
		FileStore store = (FileStore) WebApplicationContextUtils
				.getRequiredWebApplicationContext(
						event.getSession().getServletContext()).getBean(
						"userTempStore");
		File userDir = new File(store.getRootFile(), event.getSession().getId());
		if (userDir.exists()) {
			try {
				org.apache.commons.io.FileUtils.cleanDirectory(userDir);
				userDir.delete();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
