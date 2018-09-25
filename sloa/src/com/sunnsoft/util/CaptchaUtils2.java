package com.sunnsoft.util;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;

import org.patchca.color.RandomColorFactory;
import org.patchca.service.ConfigurableCaptchaService;
import org.patchca.utils.encoder.EncoderHelper;
import org.patchca.word.AdaptiveRandomWordFactory;

/**
 * @author 林宇民 Andy (llade)
 * 验证码生成工具2，识别强度高，较难识别，不容易被破解，需要pcapcha jar
 */
public class CaptchaUtils2 {

	
	private static final String STR_AZ09 = "cdefhjkmnprstpuvwxy23456789";// 去掉a/b/q,同时去除难以辨认的i/0/1/l/o/g等字符
	private static final String STR_09 = "0123456789";

	/**
	 * 生成随机验证码图片到输出流
	 * @param os 输出留
	 * @param length 生成的随机字符串长度
	 * @param format 格式支持png和jpg
	 * @param is09 是否纯数字
	 * @return 随机码字符串
	 * @throws IOException
	 */
	public static String outputImage(OutputStream os,int length, String format ,boolean is09) throws IOException {
		ConfigurableCaptchaService cs = new ConfigurableCaptchaService();
		AdaptiveRandomWordFactory wordFactory = new AdaptiveRandomWordFactory();
		if(is09) {
			wordFactory.setCharacters(STR_09);
		}else {
			wordFactory.setCharacters(STR_AZ09);
		}

		wordFactory.setMaxLength(length);
		wordFactory.setMinLength(length);
		cs.setWordFactory(wordFactory);
		RandomColorFactory colorFactory = new RandomColorFactory();
		colorFactory.setMax(new Color(170, 170, 170));
		colorFactory.setMin(new Color(0, 0, 0));
		cs.setColorFactory(colorFactory);
		cs.setFilterFactory(new MyRippleFilterFactory(colorFactory));
		return EncoderHelper.getChallangeAndWriteImage(cs, format, os);
	}

}
