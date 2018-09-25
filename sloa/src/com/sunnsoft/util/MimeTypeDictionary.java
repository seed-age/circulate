package com.sunnsoft.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author llade
 * 
 */
public class MimeTypeDictionary {

	public static final String DEFAULT_MIME_TYPE = "application/octet-stream";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MimeTypeDictionary m = new MimeTypeDictionary();
		System.out.println(m.getDictionary());
		System.out.println(getMimeType("XXXXXX"));
		System.out.println(getMimeType("XXXXXX.sql"));
		System.out.println(getMimeType("XXXXXX.SQL"));
		System.out.println(getMimeType("XXXXXX.XXXXXX"));
	}

	private Map<String, String> dictionary = new HashMap<String, String>();

	private static MimeTypeDictionary dic;

	public Map<String, String> getDictionary() {
		return dictionary;
	}

	public static String getMimeType(String fileName) {
		if (dic == null) {
			dic = new MimeTypeDictionary();
		}

		String mimetype = null;// DEFAULT

		if (fileName.indexOf('.') != -1
				&& fileName.indexOf('.') != fileName.length() - 1) {
			String suffix = fileName.substring(fileName.lastIndexOf('.') + 1)
					.toLowerCase();
			mimetype = dic.getDictionary().get(suffix);
		}
		if (mimetype == null)
			return DEFAULT_MIME_TYPE;
		return mimetype;
	}

	public MimeTypeDictionary() {
		super();
		String className = this.getClass().getName();
		String path = className.replace('.', '/');
		// System.out.println(path);
		path = path.substring(0, path.lastIndexOf('/')) + "/mime-type.ini";
		// System.out.println("path:"+path);
		Pattern p = Pattern.compile("\\s*(\\S+)\\s*[,=]*(\\S+)\\s*");
//		Map map = new HashMap(0);
		try {
			// 读取数据字典的内容
			LineNumberReader ln = new LineNumberReader(new InputStreamReader(
					this.getClass().getClassLoader().getResourceAsStream(path)));
			String read = null;
			while ((read = ln.readLine()) != null) {
				Matcher m = p.matcher(read);
				while (m.find()) {
					String key = m.group(1);
					String value = m.group(2);
					this.dictionary.put(key, value);
				}
			}
			ln.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
