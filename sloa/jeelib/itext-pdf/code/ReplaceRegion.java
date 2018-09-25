package com.sunnsoft.util.pdf;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.BaseColor;

/**
 * 需要替换的区域
 * @user : caoxu-yiyang@qq.com
 * @user : llade 改进
 * @date : 2016年11月9日
 */
public class ReplaceRegion {
	
	public static class Region{
		private Float x;
		private Float y;
		private Float w;
		private Float h;
		private BaseColor fillColor;
		private int fontSize;
		private String newText;
		/**
		 * @return the x
		 */
		public Float getX() {
			return x;
		}
		/**
		 * @param x the x to set
		 */
		public void setX(Float x) {
			this.x = x;
		}
		/**
		 * @return the y
		 */
		public Float getY() {
			return y;
		}
		/**
		 * @param y the y to set
		 */
		public void setY(Float y) {
			this.y = y;
		}
		/**
		 * @return the w
		 */
		public Float getW() {
			return w;
		}
		/**
		 * @param w the w to set
		 */
		public void setW(Float w) {
			this.w = w;
		}
		/**
		 * @return the h
		 */
		public Float getH() {
			return h;
		}
		/**
		 * @param h the h to set
		 */
		public void setH(Float h) {
			this.h = h;
		}
		/**
		 * @return the fillColor
		 */
		public BaseColor getFillColor() {
			return fillColor;
		}
		/**
		 * @param fillColor the fillColor to set
		 */
		public void setFillColor(BaseColor fillColor) {
			this.fillColor = fillColor;
		}
		/**
		 * @return the fontSize
		 */
		public int getFontSize() {
			return fontSize;
		}
		/**
		 * @param fontSize the fontSize to set
		 */
		public void setFontSize(int fontSize) {
			this.fontSize = fontSize;
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

	private List<Region> regions = new ArrayList<>();
	
	public void addRegion(Float x, Float y, Float w ,Float h,BaseColor fillColor,int fontSize,String newText) {
		Region r = new Region();
		r.setX(x);
		r.setY(y);
		r.setW(w);
		r.setH(h);
		r.setFillColor(fillColor);
		r.setFontSize(fontSize);
		r.setNewText(newText);
		regions.add(r);
	}
	
	public void addRegion(Float x, Float y, Float w ,Float h,String newText) {
		this.addRegion(x, y, w, h, null,0,newText);
	}

	/**
	 * @return the regions
	 */
	public List<Region> getRegions() {
		return regions;
	}

	
	
}
