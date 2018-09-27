package com.sunnsoft.util.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.pdf.*;

import java.io.InputStream;
import java.io.OutputStream;
/**
 * 
 * @author llade 改进
 *
 */
public class PDFUtil {
	
	private static Logger logger = LoggerFactory.getLogger(PDFUtil.class);

	/**
	 * 斜角排列、全屏多个重复的花式文字水印
	 * 
	 * @param input
	 *			需要加水印的PDF读取输入流
	 * @param output
	 *			输出生成PDF的输出流
	 * @param waterMarkString
	 *			水印字符
	 * @param xAmout
	 *			x轴重复数量
	 * @param yAmout
	 *			y轴重复数量
	 * @param opacity
	 *			水印透明度
	 * @param rotation
	 *			水印文字旋转角度，一般为45度角
	 * @param waterMarkFontSize
	 *			水印字体大小
	 * @param color
	 *			水印字体颜色
	 */
	public static void stringWaterMark(InputStream input, OutputStream output, String waterMarkString, int xAmout,
			int yAmout, float opacity, float rotation ,int waterMarkFontSize, BaseColor color) {
		try {

			PdfReader reader = new PdfReader(input);
			PdfStamper stamper = new PdfStamper(reader, output);

			// 添加中文字体
			BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			
			int total = reader.getNumberOfPages() + 1;

			PdfContentByte over;
			// 给每一页加水印
			for (int i = 1; i < total; i++) {
				Rectangle pageRect = stamper.getReader().getPageSizeWithRotation(i);
				// 计算水印每个单位步长X,Y
				float x = pageRect.getWidth() / xAmout;
				float y = pageRect.getHeight() / yAmout;

				over = stamper.getOverContent(i);
				PdfGState gs = new PdfGState();
				// 设置透明度为
				gs.setFillOpacity(opacity);
	
				over.setGState(gs);
				over.saveState();

				over.beginText();
				over.setColorFill(color);
				over.setFontAndSize(baseFont, waterMarkFontSize);

				for (int n = 0; n < xAmout + 1; n++) {
					for (int m = 0; m < yAmout + 1; m++) {
						over.showTextAligned(Element.ALIGN_CENTER, waterMarkString, x * n, y * m, rotation);
					}
				}

				over.endText();
			}
			stamper.close();
		} catch (Exception e) {
			logger.error("水印制作错误", e);
		}
	}

	/**
	 * 图片水印，整张页面平铺
	 * 
	 * @param input
	 *            需要加水印的PDF读取输入流
	 * @param output
	 *            输出生成PDF的输出流
	 * @param imageFile
	 *            水印图片路径
	 */
	public static void imageWaterMark(InputStream input, OutputStream output, String imageFile, float opacity) {
		try {

			PdfReader reader = new PdfReader(input);
			PdfStamper stamper = new PdfStamper(reader, output);
			Rectangle pageRect = stamper.getReader().getPageSize(1);
			float w = pageRect.getWidth() ;
			float h = pageRect.getHeight() ;

			int total = reader.getNumberOfPages() + 1;

			Image image = Image.getInstance(imageFile);
			image.setAbsolutePosition(0, 0);// 坐标
			image.scaleAbsolute(w, h);

			PdfGState gs = new PdfGState();
			gs.setFillOpacity(opacity);// 设置透明度

			PdfContentByte over;
			// 给每一页加水印
			for (int i = 1; i < total; i++) {
				over = stamper.getOverContent(i);
				over.setGState(gs);
				over.saveState();//没这个的话，图片透明度不起作用,必须在beginText之前，否则透明度不起作用，会被图片覆盖了内容而看不到文字了。
				over.beginText();
				// 添加水印图片
				over.addImage(image);
			}
			stamper.close();
		} catch (Exception e) {
			logger.error("水印制作错误", e);
		}
	}


}