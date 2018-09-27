package com.sunnsoft.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.codec.binary.Base64;

/**
 * 
 * @author llade Andy
 * 注意，GIF处理只能保留第一帧。
 */

public class ImageUtils {
	
	
	/**
	 * @description:判断文件是否为图片文件,判断依据为:获取文件的宽高属性;
	 * @param file 待判定文件对象
	 * @return true/false
	 * @author:liujun
	 * @date:2017年8月10日下午4:48:56
	 */
	public static boolean isImage(File file)  
    {  
		if(file == null || !file.exists() || !file.isFile())
		{
			return false;
		}
		
		Image img = null;
		try 
		{  
			img = ImageIO.read(file);  
			if(img == null || img.getWidth(null) <= 0 || img.getHeight(null) <= 0) 
			{  
				return false;  
	        }  
	           
			return true;  
		}
		catch(Exception e) 
		{  
			return false;  
	    } 
		finally 
		{  
			img = null;  
	    }  
    }
	
	/**
	 * @author 林宇民(llade)
	 *         把srcfile文件图片按原始比例缩至targetLength（宽度）大小，并写入targetFile目标文件。如果图片目标实际小于targetLength
	 *         ，则保持图像不变。 此方法在WEB应用中，可以方便裁剪提交上来的过大的图像，而不失真。
	 * @param srcfile
	 * @param targetFile
	 * @param targetLength
	 * */

	public static void cutImageKeepScaleByWidth(File srcfile,File targetFile, int targetLength) {
		cutImageKeepScale(srcfile, targetFile,targetLength, true);
	}

	/**
	 * @author 林宇民(llade)
	 *          把srcfile文件图片按原始比例缩至targetLength（高度）大小，并写入targetFile目标文件。如果图片目标实际小于targetLength
	 *         ，则保持图像不变。 此方法在WEB应用中，可以方便裁剪提交上来的过大的图像，而不失真。
	 * @param srcfile
	 * @param targetFile
	 * @param targetLength
	 * */

	public static void cutImageKeepScaleByHeight(File srcfile,File targetFile, int targetLength) {
		cutImageKeepScale(srcfile, targetFile,targetLength, false);
	}

	/**
	 * @author 林宇民(llade)
	 *         把srcfile文件按原始比例缩小图片至targetLength大小，并写入targetFile目标文件。如果图片目标实际小于targetLength
	 *         ，则保持图像不变。 isWidth参数表示targetLength 指的是宽度还是高度，true为宽度。
	 *         此方法在WEB应用中，可以方便裁剪提交上来的过大的图像，而不失真。
	 * @param srcfile
	 * @param targetFile
	 * @param targetLength
	 * @param isWidth
	 */

	public static void cutImageKeepScale(File srcfile,File targetFile, int targetLength,
			boolean isWidth) {
		try {
			if (!srcfile.exists()) {
				return;
			}
			Image src = ImageIO.read(srcfile);

			// 原始图像高和宽
			int width = src.getWidth(null);
			int height = src.getHeight(null);

			int widthdist = 0;
			int heightdist = 0;

			int length = targetLength;
			
			// 确定图像的缩放后的高和宽
			if (isWidth) {
				
				if (targetLength > width) {
					length = width;
				}
				double scale = length * 1.0 / width;
				widthdist = length;
				heightdist = (int) (height * scale);
			} else {
				if (targetLength > height) {
					length = height;
				}
				double scale = length * 1.0 / height;
				widthdist = (int) (width * scale);
				heightdist = length;
			}

			BufferedImage tag = new BufferedImage((int) widthdist,
					(int) heightdist, BufferedImage.TYPE_INT_RGB);

			tag.getGraphics().drawImage(
					src.getScaledInstance(widthdist, heightdist,
							Image.SCALE_SMOOTH), 0, 0, null);
			String formatName = getFormatName(srcfile);
			FileOutputStream out = new FileOutputStream(targetFile);
			ImageIO.write(tag, formatName, out);
			out.flush();
			out.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void cutImageToSize(File srcfile,File targetFile, int targetWidth,int targetHeight){
		try {
			if (!srcfile.exists()) {
				return;
			}
			Image src = ImageIO.read(srcfile);

			// 原始图像高和宽

			BufferedImage tag = new BufferedImage(targetWidth,
					targetHeight, BufferedImage.TYPE_INT_RGB);

			tag.getGraphics().drawImage(
					src.getScaledInstance(targetWidth, targetHeight,
							Image.SCALE_SMOOTH), 0, 0, null);
			String formatName = getFormatName(srcfile);
			FileOutputStream out = new FileOutputStream(targetFile);
			ImageIO.write(tag, formatName, out);
			out.flush();
			out.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 *  是否正方形
	 * @param srcfile
	 */
	public static boolean  isSquare(File srcfile){
		try {
			if (!srcfile.exists()) {
				return false;
			}
			Image src = ImageIO.read(srcfile);

			// 原始图像高和宽
			int width = src.getWidth(null);
			int height = src.getHeight(null);
			return width == height;
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 剪切图片
	 * @param srcfile源图片文件
	 * @param targetFile目标存放文件
	 * @param x 剪切点起始坐标x轴(左上角坐标是0)
	 * @param y 剪切点起始坐标y轴(左上角坐标是0)
	 * @param xoffset 剪切x轴大小偏移量。
	 * @param yoffset 剪切y轴大小偏移量。
	 */
	public static void cutImage(File srcfile , File targetFile , int x , int y , int xoffset ,int yoffset){
		try {
			ImageInputStream iis = ImageIO.createImageInputStream(srcfile);   
			Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
			ImageReader reader = (ImageReader)iter.next(); 
			reader.setInput(iis, true);
			String formatName = reader.getFormatName();
			ImageReadParam param = reader.getDefaultReadParam();   
			Rectangle rect = new Rectangle(x, y, xoffset,yoffset);    
			param.setSourceRegion(rect);   
			BufferedImage bi = reader.read(0,param);     
			ImageIO.write(bi, formatName, targetFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}   
		
	}

	private static String getFormatName(Object o) {
		try {
			// Create an image input stream on the image
			ImageInputStream iis = ImageIO.createImageInputStream(o);

			// Find all image readers that recognize the image format
			Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
			if (!iter.hasNext()) {
				// No readers found
				return null;
			}

			// Use the first reader
			ImageReader reader = iter.next();

			// Close stream
			iis.close();

			// Return the format name
			return reader.getFormatName();
		} catch (IOException e) {
		}
		// The image could not be read
		return null;
	}

	/** 
     * 文字水印 
     * @param pressText 水印文字 
     * @param targetImg 目标图片 
     * @param fontName 字体名称 
     * @param fontStyle 字体样式 
     * @param color 字体颜色 
     * @param fontSize 字体大小 
     * @param x 修正值 
     * @param y 修正值 
     * @param alpha 透明度 
     */  
    public static void pressText(String pressText, File targetImg, String fontName, int fontStyle, Color color, int fontSize, int x, int y, float alpha) {  
        try {  
            File img = targetImg;  
            Image src = ImageIO.read(img);  
            int width = src.getWidth(null);  
            int height = src.getHeight(null);  
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  
            Graphics2D g = image.createGraphics();  
            g.drawImage(src, 0, 0, width, height, null);  
            g.setColor(color);  
            g.setFont(new Font(fontName, fontStyle, fontSize));  
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));  
            g.drawString(pressText, (width - (getLength(pressText) * fontSize)) / 2 + x, (height - fontSize) / 2 + y);  
            g.dispose();  
            ImageIO.write((BufferedImage) image, "jpg", img);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }
    
    /**
	 * 图片水印
	 * @param pressImg 水印文件
	 * @param targetImg 目标文件
	 * @param x 修正值，默认在图片中央
	 * @param y 修正值，默认在图片中央
	 * @param alpha
	 */
    public final static void pressImage(File pressImg, File targetImg, int x, int y, float alpha) {  
        try {  
            Image src = ImageIO.read(targetImg);  
            int wideth = src.getWidth(null);  
            int height = src.getHeight(null);  
            BufferedImage image = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);  
            Graphics2D g = image.createGraphics();  
            g.drawImage(src, 0, 0, wideth, height, null);  
            //水印文件  
            Image src_biao = ImageIO.read(pressImg);  
            int wideth_biao = src_biao.getWidth(null);  
            int height_biao = src_biao.getHeight(null);  
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));  
            g.drawImage(src_biao, (wideth - wideth_biao) / 2 + x, (height - height_biao) / 2 + y, wideth_biao, height_biao, null);  
            //水印文件结束  
            g.dispose();  
            ImageIO.write((BufferedImage) image, "jpg", targetImg);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }

	/**
	 * 图片文件转换成base64格式
	 * @param imageFile
	 * @param isDataUrl 是否生成“data:image/xxx;base64,....”开头的适合浏览器img标签的 data url格式
	 * @return
	 */
	public static String encodeImageToBase64(File imageFile ,boolean isDataUrl){
		try {
			return encodeImageToBase64(new FileInputStream(imageFile),getFormatName(imageFile),isDataUrl);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 图片留转换为base64格式
	 * @param imageFileInputStream
	 * @param formatName 格式名称
	 * @param isDataUrl 是否生成“data:image/xxx;base64,....”开头的适合浏览器img标签的 data url格式
	 * @return
	 */
	public static String encodeImageToBase64(InputStream imageFileInputStream,String formatName ,boolean isDataUrl){
		ByteArrayOutputStream outputStream = null;
		try {
			// Create an image input stream on the image
			
			BufferedImage bufferedImage = ImageIO.read(imageFileInputStream);
	        outputStream = new ByteArrayOutputStream();
	        ImageIO.write(bufferedImage, formatName, outputStream);
	        
			    // 对字节数组Base64编码
			
			return (isDataUrl ? "data:image/"+formatName.toLowerCase()+";base64," : "") + Base64.encodeBase64String(outputStream.toByteArray());
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(outputStream != null ){
					outputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
		
	}
	
	/**
	 * 把base64编码的字符串转换为图片文件输出。
	 * @param base64String 
	 * @param outputFile 输出目标文件
	 */
	public static void decodeBase64toImageFile(String base64String , File outputFile){
		String input = base64String;
		if(base64String.startsWith("data:image/")){
			input = base64String.substring(base64String.indexOf(";base64,") + 8);
		}
	    try {
	      FileOutputStream write = new FileOutputStream(outputFile);
	      byte[] decoderBytes = Base64.decodeBase64(input);;
	      write.write(decoderBytes);
	      write.close();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	}
	
	/**
	 * 压缩PNG图片，减少颜色数
	 * @param srcfile
	 * @param targetFile
	 */
	public static void compressPng(File srcfile,File targetFile) {
		try {
			if (!srcfile.exists()) {
				return;
			}
			Image src = ImageIO.read(srcfile);

			// 原始图像高和�?
			int width = src.getWidth(null);
			int height = src.getHeight(null);


			BufferedImage tag = new BufferedImage((int) width,
					(int) height, BufferedImage.TYPE_USHORT_555_RGB);

			tag.getGraphics().drawImage(
					src.getScaledInstance(width, height,
							Image.SCALE_SMOOTH), 0, 0, null);
			String formatName = getFormatName(srcfile);
			FileOutputStream out = new FileOutputStream(targetFile);
			ImageIO.write(tag, formatName, out);
			out.flush();
			out.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private static int getLength(String text) {  
        int length = 0;  
        for (int i = 0; i < text.length(); i++) {  
            if (new String(text.charAt(i) + "").getBytes().length > 1) {  
                length += 2;  
            } else {  
                length += 1;  
            }  
        }  
        return length / 2;  
    } 

}
