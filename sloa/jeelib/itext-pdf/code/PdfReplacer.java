package com.sunnsoft.util.pdf;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.sunnsoft.util.pdf.ReplaceRegion.Region;


/**
 * 替换PDF文件某个区域内的文本
 * @user : caoxu-yiyang@qq.com
 * @user : llade 改进
 * @date : 2017年10月9日
 */
public class PdfReplacer {
	private static final Logger logger = LoggerFactory.getLogger(PdfReplacer.class);
	
	private int fontSize;
	private Map<Integer,Collection<ReplaceRegion>> pageRegions;
	private PdfPositionParse parse;
	private ByteArrayOutputStream output;
	private PdfReader reader;
	private PdfStamper stamper;
	private List<PdfContentByte> canvases = new ArrayList<>();
	private Font font;
	private byte[] pdfBytes;
	
	public PdfReplacer(byte[] pdfBytes) throws DocumentException, IOException{
		this.pdfBytes = pdfBytes;
		init();
	}
	
	public PdfReplacer(InputStream in)  throws DocumentException, IOException{
		pdfBytes = new byte[in.available()];
		in.read(pdfBytes);
		init();
	}
	
	public PdfReplacer(String fileName) throws IOException, DocumentException{
		FileInputStream in = null;
		try{
			in =new FileInputStream(fileName);
			pdfBytes = new byte[in.available()];
			in.read(pdfBytes);
			init();
		}finally{
			in.close();
		}
	}
	
	private void init() throws DocumentException, IOException{
		logger.info("初始化开始");
		reader = new PdfReader(pdfBytes);
		output = new ByteArrayOutputStream();
		stamper = new PdfStamper(reader, output);
		int total = reader.getNumberOfPages();
		for(int i = 0 ; i < total ; i++) {
			canvases.add(stamper.getOverContent(i+1));
		}
		parse = new PdfPositionParse(this.pdfBytes);
	    setFont(10);
	    logger.info("初始化完成");
	}
	
	private void close() throws DocumentException, IOException{
		if(reader != null){
			reader.close();
		}
		if(output != null){
			output.close();
		}
		
		output=null;
	}
	
	public void replaceText(String find, String replace){
		parse.addReplaceText(find, replace, 0 ,null);
	}
	
	public void replaceText(String find, String replace,int fontSize){
		parse.addReplaceText(find, replace, fontSize ,null);
	}
	
	public void replaceText(String find, String replace, int fontSize, BaseColor fillColor){
		parse.addReplaceText(find, replace,fontSize,fillColor);
	}
	
	public void replaceText(String find, String replace, BaseColor fillColor){
		parse.addReplaceText(find, replace,0,fillColor);
	}
	/**
	 * 替换文本
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	private void process() throws DocumentException, IOException{
		try{
			parseReplaceText();
			int page = 1;
			for (PdfContentByte canvas : canvases) {
				Collection<ReplaceRegion> replaceRegions= this.pageRegions.get(page);
				if(!replaceRegions.isEmpty()) {
					canvas.saveState();
			        for (ReplaceRegion replaceRegion : replaceRegions) {
			        	List<Region> regoins = replaceRegion.getRegions();
			        	for (Region region : regoins) {
			        		if(region.getFillColor() != null) {
			        			canvas.setColorFill(region.getFillColor());
			        		}else {
			        			canvas.setColorFill(BaseColor.WHITE);
			        		}
				            canvas.rectangle(region.getX(),region.getY(),region.getW(),region.getH());
						}
			        	
					}
			        canvas.fill();
			        canvas.restoreState();
			        canvas.beginText(); 
			        for (ReplaceRegion replaceRegion : replaceRegions) {
			        	List<Region> regoins = replaceRegion.getRegions();
			        	for (Region region : regoins) {
				        	//设置字体
			        		int fontSize = getFontSize();
			        		if(region.getFontSize() != 0) {
			        			fontSize = region.getFontSize();
			        		}
				        	canvas.setFontAndSize(font.getBaseFont(), fontSize);  
				            canvas.setTextMatrix(region.getX(),region.getY()+2/*修正背景与文本的相对位置*/);  
				            canvas.showText(region.getNewText());   
			        	}
			        	
					}
			        canvas.endText();
				}
		        page ++;
			}
			
		}finally{
			if(stamper != null){
				stamper.close();
			}
		}
	}
	
	/**
	 * 未指定具体的替换位置时，系统自动查找位置
	 */
	private void parseReplaceText() {
		try {
			parse.parse();
			pageRegions = parse.getPageRegions();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 生成新的PDF文件
	 * @param fileName
	 * @throws DocumentException
	 * @throws IOException
	 */
	public void toPdf(String fileName) throws DocumentException, IOException{
		FileOutputStream fileOutputStream = null;
		try{
			process();
			fileOutputStream = new FileOutputStream(fileName);
			fileOutputStream.write(output.toByteArray());
			fileOutputStream.flush();
		}catch(IOException e){
			logger.error(e.getMessage(), e);
			throw e;
		}finally{
			if(fileOutputStream != null){
				fileOutputStream.close();
			}
			close();
		}
		logger.info("文件生成成功");
	}
	
	public void toPdf(OutputStream out) throws DocumentException, IOException{
		try{
			process();
			out.write(output.toByteArray());
			out.flush();
		}catch(IOException e){
			logger.error(e.getMessage(), e);
			throw e;
		}finally{
			close();
		}
		logger.info("文件输出成功");
	}
	
	
	/**
	 * 将生成的PDF文件转换成二进制数组
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 */
	public byte[] toBytes() throws DocumentException, IOException{
		try{
			process();
			logger.info("二进制数据生成成�?");
			return output.toByteArray();
		}finally{
			close();
		}
	}
	
	public int getFontSize() {
		return fontSize;
	}

	/**
	 * 设置字体大小
	 * @param fontSize
	 * @throws DocumentException
	 * @throws IOException
	 */
	public void setFont(int fontSize) throws DocumentException, IOException{
		if(fontSize != this.fontSize){
			this.fontSize = fontSize;
			BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
		    font = new Font(bf,this.fontSize,Font.BOLD);
		}
	}
	
	public void setFont(Font font){
		if(font == null){
			throw new NullPointerException("font is null");
		}
		this.font = font;
	}
	
}
