package com.sunnsoft.servlet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 生成随机验证码
 * 
 * @author bitiliu,andylin
 * 
 */
public class ValidateCodeServlet extends HttpServlet {

	public static final String VALIDATE_CODE_SESSION_NAME = "_ValidateCode";

	private static Log logger = LogFactory.getLog(ValidateCodeServlet.class);

	private static final long serialVersionUID = 1L;

	// 验证码图片的宽度。
	private int width = 60;
	// 验证码图片的高度。
	private int height = 20;
	// 验证码字符个数
	private int codeCount = 4;

	// 第一个字符前的空白
	private int leftMargin = 6;
	// 最大随机偏移（使得字符出现重叠，不容易识别，不宜设置过高）
	private int maxRandomPositionX = 6;

	// 出现干扰横线数量（不容易识别，不宜设置过高）
	private int randomLineCount = 4;

	private int x = 0;
	// 字体高度
	private int fontHeight;
	private int codeY;

	// char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
	// 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
	// 'X', 'Y', 'Z'};

	char[] codeSequence = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	/**
	 * 初始化验证图片属性
	 */
	public void init() throws ServletException {
		// 从web.xml中获取初始信息
		// 宽度
		String strWidth = this.getInitParameter("width");
		// 高度
		String strHeight = this.getInitParameter("height");
		// 字符个数
		String strCodeCount = this.getInitParameter("codeCount");

		String strLeftMargin = this.getInitParameter("leftMargin");

		String strMaxRandomPositionX = this
				.getInitParameter("maxRandomPositionX");

		String strRandomLineCount = this.getInitParameter("randomLineCount");

		// 将配置的信息转换成数值
		try {
			if (strWidth != null && strWidth.length() != 0) {
				width = Integer.parseInt(strWidth);
			}
			if (strHeight != null && strHeight.length() != 0) {
				height = Integer.parseInt(strHeight);
			}
			if (strCodeCount != null && strCodeCount.length() != 0) {
				codeCount = Integer.parseInt(strCodeCount);
			}
			if (strLeftMargin != null && strLeftMargin.length() != 0) {
				leftMargin = Integer.parseInt(strLeftMargin);
			}
			if (strMaxRandomPositionX != null
					&& strMaxRandomPositionX.length() != 0) {
				maxRandomPositionX = Integer.parseInt(strMaxRandomPositionX);
			}
			if (strRandomLineCount != null && strRandomLineCount.length() != 0) {
				randomLineCount = Integer.parseInt(strRandomLineCount);
			}
		} catch (NumberFormatException e) {
		}

		x = width / (codeCount + 1);
		fontHeight = height - 2;
		codeY = height - 4;
		logger.debug("ValidateCodeServlet started");
	}

	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, java.io.IOException {

		// 定义图像buffer
		BufferedImage buffImg = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = buffImg.createGraphics();

		// 创建一个随机数生成器类
		Random random = new Random();

		// 将图像填充为白色
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);

		// 创建字体，字体的大小应该根据图片的高度来定。
		Font font = new Font("Fixedsys", Font.PLAIN, fontHeight);
		// 设置字体。
		g.setFont(font);

		// 画边框。
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, width - 1, height - 1);

		// randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
		StringBuffer randomCode = new StringBuffer();
		int red = 0, green = 0, blue = 0;

//		Color lineColor = Color.BLACK;
		java.util.List<Color> colorList = new ArrayList<Color>(codeCount);
		// 随机产生codeCount数字的验证码。
		for (int i = 0; i < codeCount; i++) {
			// 得到随机产生的验证码数字。
			String strRand = String.valueOf(codeSequence[random
					.nextInt(this.codeSequence.length)]);
			// 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
			red = random.nextInt(100);
			green = random.nextInt(100);
			blue = random.nextInt(100);

			// 用随机产生的颜色将验证码绘制到图像中。
			Color color = new Color(red, green, blue);
			colorList.add(color);
			g.setColor(color);
			g.drawString(strRand, leftMargin + (i * x)
					+ (int) ((Math.random() - 0.5) * this.maxRandomPositionX),
					codeY);

			// 将产生的四个随机数组合在一起。
			randomCode.append(strRand);
		}

		// 随机产生160条干扰线，使图象中的认证码不易被其它程序探测到。
		// g.setColor(colorList.get(random.nextInt(codeCount)));
		// for (int i = 0; i < 30; i++) {
		// int x = random.nextInt(width);
		// int y = random.nextInt(height);
		// int xl = random.nextInt(12);
		// int yl = random.nextInt(12);
		// g.drawLine(x, y, x + xl, y + yl);
		// }
		// 随机产生3条干扰横线，使图象中的认证码不易被其它程序探测到。

		int lineHeightScale = fontHeight / randomLineCount;
		for (int i = 0; i < randomLineCount; i++) {
			int x = this.leftMargin + 5 - random.nextInt(10);
			int baseHeight = lineHeightScale * i;
			int y = random.nextInt(lineHeightScale) + baseHeight;
			g.setColor(colorList.get(random.nextInt(codeCount)));
			g.drawLine(x, y, x + width + 5 - random.nextInt(10), y);
		}
		// 将四位数字的验证码保存到Session中。
		HttpSession session = req.getSession();
		session.setAttribute(VALIDATE_CODE_SESSION_NAME, randomCode.toString());

		// 禁止图像缓存。
		resp.setHeader("Pragma", "no-cache");
		resp.setHeader("Cache-Control", "no-cache");
		resp.setDateHeader("Expires", 0);

		resp.setContentType("image/jpeg");

		// 将图像输出到Servlet输出流中。
		ServletOutputStream sos = resp.getOutputStream();
		ImageIO.write(buffImg, "jpeg", sos);
		sos.close();
	}

}
