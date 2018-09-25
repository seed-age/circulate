package com.sunnsoft.util.pdf;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.itextpdf.awt.geom.Rectangle2D.Float;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

/**
 * pdf渲染监听,当找到渲染的文本时，得到文本的坐标x,y,w,h
 * @user : caoxu-yiyang@qq.com
 * @user : llade 改进
 * @date : 2017年10月9日
 */
public class PositionRenderListener implements RenderListener{
	
	private static class Replacement{
		private String keyString;
		private String newText;
		/**
		 * @return the keyString
		 */
		public String getKeyString() {
			return keyString;
		}
		/**
		 * @param keyString the keyString to set
		 */
		public void setKeyString(String keyString) {
			this.keyString = keyString;
		}
		/**
		 * @return the newText
		 */
		public String getNewText() {
			return newText;
		}
		/**
		 * @param newText the newText to set
		 */
		public void setNewText(String newText) {
			this.newText = newText;
		}
		
	}
	
	private Map<String,String> replaceText = new HashMap<>();	//需要查找的文本
	private Map<String,Integer> replaceFontSize = new HashMap<>();	//需要查找的文本
	private Map<String,BaseColor> replaceFileColor = new HashMap<>();	//需要查找的文本
	private float defaultH;		///出现无法取到值的情况，默认为12
	private float fixHeight;	//可能出现无法完全覆盖的情况，提供修正的参数，默认为2
	
	public PositionRenderListener(Map<String,String> replaceText,Map<String,Integer> replaceFontSize,Map<String,BaseColor> replaceFileColor, float defaultH,float fixHeight) {
		this.replaceText = replaceText;
		this.replaceFontSize = replaceFontSize;
		this.replaceFileColor = replaceFileColor;
		this.defaultH = defaultH;
		this.fixHeight = fixHeight;
	}

	public PositionRenderListener(Map<String,String> replaceText,Map<String,Integer> replaceFontSize,Map<String,BaseColor> replaceFileColor) {
		this(replaceText, replaceFontSize, replaceFileColor, 12, 2);
	}
	
	@Override
	public void beginTextBlock() {
		//暂时未用到
	}

	@Override
	public void endTextBlock() {
		//暂时未用到
	}

	@Override
	public void renderImage(ImageRenderInfo imageInfo) {
		//暂时未用到
	}

	private Map<String, ReplaceRegion> result = new HashMap<>();
	@Override
	public void renderText(TextRenderInfo textInfo) {
		String findText = textInfo.getText();
		Replacement replacement = getReplacement(findText);
		
		if(replacement != null) {
			String keyString = replacement.getKeyString();
			ReplaceRegion replaceRegion = result.get(keyString);
			if(replaceRegion == null) {
				replaceRegion = new ReplaceRegion();
				result.put(keyString, replaceRegion);
			}
			Float bound = textInfo.getBaseline().getBoundingRectange();
			int fontSize = this.replaceFontSize.get(keyString) == null ?0 : this.replaceFontSize.get(keyString);
			replaceRegion.addRegion(bound.x, bound.y-this.fixHeight, bound.width, bound.height == 0 ? defaultH : bound.height,this.replaceFileColor.get(keyString),fontSize,replacement.getNewText());
		}
	}

	
	private Replacement getReplacement(String input) {
		Set<Entry<String, String>> entrys = replaceText.entrySet();
		for (Entry<String, String> entry : entrys) {
			String key = entry.getKey();
			String value = entry.getValue();
			if(input.indexOf(key) != -1) {
				Replacement replacement = new Replacement();
				replacement.setKeyString(key);
				replacement.setNewText(input.replace(key, value));
				return replacement;
			}
		}
		return null;
	}
	
	
	public Collection<ReplaceRegion> getResult() {
		return this.result.values();
	}
}
