package com.sunnsoft.util.pdf;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;

/**
 * 解析PDF中文本的x,y位置
 * @user : caoxu-yiyang@qq.com
 * @user : llade 改进
 * @date : 2017年10月9日
 */
public class PdfPositionParse {

	private PdfReader reader;
	private Map<String,String> replaceText = new HashMap<>();	
	private Map<String,Integer> replaceFontSize = new HashMap<>();	
	private Map<String,BaseColor> replaceFileColor = new HashMap<>();
	private PdfReaderContentParser parser;
	private Map<Integer,Collection<ReplaceRegion>> pageRegions = new HashMap<>();

	public PdfPositionParse(String fileName) throws IOException{
		FileInputStream in = null;
		try{
			in =new FileInputStream(fileName);
			byte[] bytes = new byte[in.available()];
			in.read(bytes);
			init(bytes);
		}finally{
			in.close();
		}
	}
	
	public PdfPositionParse(byte[] bytes) throws IOException{
		init(bytes);
	}
	
	public void addReplaceText(String find,String replace,int fontSize,BaseColor fillColor){
		this.replaceText.put(find,replace);
		this.replaceFontSize.put(find, fontSize);
		this.replaceFileColor.put(find, fillColor);
	}
	
	private void init(byte[] bytes) throws IOException {
		reader = new PdfReader(bytes);
		parser = new PdfReaderContentParser(reader);
	}
	
	/**
	 * 解析文本
	 * @throws IOException
	 */
	public void parse() throws IOException{
		try{
			if(this.replaceText.isEmpty()){
				throw new NullPointerException("没有找到要查找的文本");
			}
			int total = reader.getNumberOfPages();
			for(int i = 0 ; i < total ; i++) {
				PositionRenderListener listener = new PositionRenderListener(replaceText,replaceFontSize,replaceFileColor);
				int page = i + 1;
				parser.processContent(page, listener);
				this.pageRegions.put(page, listener.getResult());
			}
			
		}finally{
			if(reader != null){
				reader.close();
			}
		}
	}

	/**
	 * @return the pageRegions
	 */
	public Map<Integer,Collection<ReplaceRegion>> getPageRegions() {
		return pageRegions;
	}
	
	
	
}
