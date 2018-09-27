package com.sunnsoft.util;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

/**
 * 
 * @author llade
 * @deprecated
 * 侦听hibernate删除操作，删除含图片文件的实体的时候，删除源图片文件。
 * 
 * 由于应用范围过窄，同时需要设定较为复杂,已经弃用。
 */
@SuppressWarnings("unchecked")
public class ImageDeleteInterceptor extends EmptyInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private FileStore fileStore;

	private List imageProperties = new ArrayList();
	private List imageClassNames = new ArrayList();

	public List getImageClassNames() {
		return imageClassNames;
	}

	public void setImageClassNames(List imageClassNames) {
		this.imageClassNames = imageClassNames;
	}

	public List getImageProperties() {
		return imageProperties;
	}

	public void setImageProperties(List imageProperties) {
		this.imageProperties = imageProperties;
	}

	public FileStore getFileStore() {
		return fileStore;
	}

	public void setFileStore(FileStore fileStore) {
		this.fileStore = fileStore;
	}

	@Override
	public void onDelete(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) {
		// super.onDelete(entity, id, state, propertyNames, types);
		if (entity != null
				&& imageClassNames.contains(entity.getClass().getName())) {
			File parentPath = fileStore.getRootFile();
			for (int i = 0; i < propertyNames.length; i++) {
				if (imageProperties.contains(propertyNames[i])) {
					if (state[i] != null
							&& StringUtils.isNotBlank(state[i].toString())) {
						// System.out.println("delete img:"+parentPath+"/"+state[i].toString());
						new File(parentPath, state[i].toString()).delete();
					}
				}
			}
		}
	}

}
