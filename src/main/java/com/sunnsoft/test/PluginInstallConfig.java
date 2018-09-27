package com.sunnsoft.test;

import java.io.File;

public class PluginInstallConfig {
	/**
	 * MyEclipse 7.x & 8.x + 插件安装代码生成器
	 * 
	 * @author FL-soft
	 * @version 2.0
	 *          改变了算法，解决了汉化包中jar文件名带_cn时，报java.lang.ArrayIndexOutOfBoundsException的异常
	 */
	private String path;

	public PluginInstallConfig(String path) {
		this.path = path;
	}

	public void print() {
		try {
			File f = new File(path);
			if (f.isDirectory()) {
				File[] files = f.listFiles();
				StringBuffer sb = new StringBuffer(0);
				for (File file : files) {
					String[] splitStrs = file.getName().split("_");
					String version = (splitStrs[splitStrs.length - 1]
							.split(".jar"))[0];
					String pluginName = (file.getName().split("_"
							+ splitStrs[splitStrs.length - 1]))[0];
					String filepath = "file:/"
							+ file.getAbsolutePath().replace('\\', '/');
					sb.append(pluginName + "," + version + "," + filepath
							+ ",4,false\r\n");
				}
				System.out.println(sb);
				return;
			}
			System.out.println("插件目录错误！");
		} catch (Exception e) {
			System.out.println("插件目录错误！");
		}
	}

	public static void main(String[] args) {
		// 替换插件所在的路径
		String path = "D:\\Program Files\\Genuitec\\plugins\\spket\\plugins";
		new PluginInstallConfig(path).print();
	}
}