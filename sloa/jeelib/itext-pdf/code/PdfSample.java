package com.sunnsoft.util.pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author llade 改进，引用自网络公开程序代码
 *
 */
public class PdfSample {

	/**
	 * 演示pdf 水印和签章的代码
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		File file = new File("d:/tmp/test-create.pdf");
		FileOutputStream output1 = new FileOutputStream(file);
		createPDF(output1);
		output1.close();
		

		//文本替换
		PdfReplacer textReplacer = new PdfReplacer("d:/tmp/test-create.pdf");
//		textReplacer.replaceText("（企业版）", "（企鹅版）");
//		textReplacer.replaceText("（企业版）", "（企鹅版）", 25);
//		textReplacer.replaceText("（企业版）", "（企鹅版）", BaseColor.RED);
//		textReplacer.replaceText("（企业版）", "（企鹅版）", 10,BaseColor.RED);
		textReplacer.replaceText("${userName}", "张三", 12 , BaseColor.WHITE);
		textReplacer.toPdf("d:/tmp/test-replace.pdf");
		

		//文字斜面水印
		FileInputStream input1 = new FileInputStream("d:/tmp/test-replace.pdf");
		FileOutputStream output2 = new FileOutputStream("d:/tmp/test-water-string.pdf");
		PDFUtil.stringWaterMark(input1, output2, "测试水印", 4, 5, 0.2f,45, 20,BaseColor.BLACK);//文字水印
		
		output2.close();
		input1.close();
		
		//图片水印
		FileInputStream input2 = new FileInputStream("d:/tmp/test-water-string.pdf");

		FileOutputStream output3 = new FileOutputStream("d:/tmp/test-water-image.pdf");
		PDFUtil.imageWaterMark(input2, output3, PdfSign.class.getResource("water-mark.jpg").getFile(),0.2f);//图片背景或水印
		
		output3.close();
		input2.close();
		
		//电子签章（放在最后，因为盖章后就不能更改内容了）
		PdfSign pdfSign = new PdfSign();
		//测试证书生成命令： .\keytool -genkey -alias yourkey -keypass yourpass -keyalg RSA -keysize 1024 -validity 365 -storetype PKCS12 -keystore D:/tmp/sample.p12 -storepass yourpass -dname "CN=www.yourcompany.cn,OU=你们部门或组织名,O=你们公司名,L=广州,ST=广东,C=中国"
		//其中 PKCS12的-keypass 和 -storepass 密码必须一致
		//可用如下命令导出csr文件给 CA机构认证。再导入证书
		//.\keytool -certreq -alias yourkey  -storetype PKCS12 -keystore D:/tmp/sample.p12 -storepass yourpass -file D:/tmp/sample.csr
		URL url = PdfSign.class.getResource("sample.p12");//电子签章的证书文件，这里由于测试方便放在PdfSign在同一个包里。基于安全原则，实际生产环境下请勿放于包目录下。
		pdfSign.setPkcs12FilePath(url.getFile());
		pdfSign.setKeyAlias("yourkey");
		pdfSign.setKeyAliasPass("yourpass");
		pdfSign.setKeyStorePass("yourpass");
		pdfSign.setStamperFilePath(PdfSign.class.getResource("stamper.png").getFile());//文件放在PdfSign在同一个包里
		pdfSign.init();
		pdfSign.sign(new FileInputStream("d:/tmp/test-water-image.pdf"), new FileOutputStream("d:/tmp/test-stamp.pdf"), "电子签章例子（仅演示，无法律效率）", "XXX公司", true, 300, 40, 0.5f);
	}

	/**
	 * 创建PDF文档
	 * 
	 * @return
	 * @throws Exception
	 * @throws docException
	 */
	public static void createPDF(OutputStream output) throws Exception {

		// 设置纸张
		Rectangle rect = new Rectangle(PageSize.A4);

		// 创建文档实例
		Document doc = new Document(rect);

		// 添加中文字体
		BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);

		// 设置字体样式
		Font textFont = new Font(bfChinese, 11, Font.NORMAL); // 正常
		Font redTextFont = new Font(bfChinese, 11, Font.NORMAL, BaseColor.RED); // 正常,红色
		Font boldFont = new Font(bfChinese, 11, Font.BOLD); // 加粗
		Font redBoldFont = new Font(bfChinese, 11, Font.BOLD, BaseColor.RED); // 加粗,红色
		Font firsetTitleFont = new Font(bfChinese, 22, Font.BOLD); // 一级标题
		Font secondTitleFont = new Font(bfChinese, 15, Font.BOLD); // 二级标题
		Font underlineFont = new Font(bfChinese, 11, Font.UNDERLINE); // 下划线斜体

		// 创建输出流
		PdfWriter.getInstance(doc, output);

		doc.open();
		doc.newPage();

		// 段落
		Paragraph p1 = new Paragraph();
		// 短语
		Phrase ph1 = new Phrase();
		// 块
		Chunk c1 = new Chunk("*********", boldFont);
		Chunk c11 = new Chunk("（信用报告提供机构l ogo）", textFont);
		// 将块添加到短语
		ph1.add(c1);
		ph1.add(c11);
		// 将短语添加到段落
		p1.add(ph1);
		// 将段落添加到短语
		doc.add(p1);

		p1 = new Paragraph();
		ph1 = new Phrase();
		Chunk c2 = new Chunk("报告编号：", boldFont);
		Chunk c22 = new Chunk("SN-201604010001", textFont);
		ph1.add(c2);
		ph1.add(c22);
		p1.add(ph1);
		doc.add(p1);

		p1 = new Paragraph("企业信用报告", firsetTitleFont);
		p1.setLeading(50);
		p1.setAlignment(Element.ALIGN_CENTER);
		doc.add(p1);

		p1 = new Paragraph("（企业版）", textFont);
		p1.setLeading(20);
		p1.setAlignment(Element.ALIGN_CENTER);
		doc.add(p1);

		p1 = new Paragraph();
		p1.setLeading(20);
		p1.setAlignment(Element.ALIGN_CENTER);
		ph1 = new Phrase();
		Chunk c3 = new Chunk("查询时间：", boldFont);
		Chunk c33 = new Chunk("2016-04-01 00:00:00", textFont);
		Chunk c4 = new Chunk(leftPad("查询人：", 10), boldFont);
		Chunk c44 = new Chunk("${userName}", boldFont);
//		Chunk c44 = new Chunk("admin（用户登录名）", textFont);
		ph1.add(c3);
		ph1.add(c33);
		ph1.add(c4);
		ph1.add(c44);
		p1.add(ph1);
		doc.add(p1);

		p1 = new Paragraph("报告说明", secondTitleFont);
		p1.setLeading(50);
		p1.setAlignment(Element.ALIGN_CENTER);
		doc.add(p1);

		p1 = new Paragraph(" ");
		p1.setLeading(30);
		doc.add(p1);

		p1 = new Paragraph();
		ph1 = new Phrase();
		Chunk c5 = new Chunk("1.本报告由", textFont);
		Chunk c6 = new Chunk("****信用信息服务中心${userName}****", underlineFont);
		c6.setSkew(0, 30);
		Chunk c7 = new Chunk(" 出具，依据截止报告时间小微企业信用信息数据库记录的信息生成。除异议标注和查询记录外，报告中的信息均由相关报数机构和信息主体提供，", textFont);
		Chunk c8 = new Chunk("不保证其真实性和准确性，但承诺在信息整合、汇总、展示的全过程中保持客观、中立的地位。", textFont);
		ph1.add(c5);
		ph1.add(c6);
		ph1.add(c7);
		ph1.add(c6);
		ph1.add(c8);
		p1.add(ph1);
		doc.add(p1);

		p1 = new Paragraph();
		ph1 = new Phrase();
		Chunk c9 = new Chunk("2.异议标注是", textFont);
		Chunk c10 = new Chunk(" 对报告中的信息记录或对信息主体所作的说明。", textFont);
		ph1.add(c9);
		ph1.add(c6);
		ph1.add(c10);
		p1.add(ph1);
		doc.add(p1);

		p1 = new Paragraph("3.信息主体说明是信息主体对报数机构提供的信息记录所作的简要说明。", textFont);
		doc.add(p1);

		p1 = new Paragraph();
		ph1 = new Phrase();
		Chunk c12 = new Chunk("4.信息主体有权对本报告中的内容提出异议。如有异议，可联系报数机构，也可到", textFont);
		Chunk c13 = new Chunk(" 提出异议申请。", textFont);
		ph1.add(c12);
		ph1.add(c6);
		ph1.add(c13);
		p1.add(ph1);
		doc.add(p1);

		p1 = new Paragraph("5.更多咨询，请致电客户服务热线********。", textFont);
		doc.add(p1);

		p1 = new Paragraph("信息概况", secondTitleFont);
		p1.setSpacingBefore(30);
		p1.setSpacingAfter(30);
		p1.setAlignment(Element.ALIGN_CENTER);
		doc.add(p1);

		p1 = new Paragraph("信息主体实缴资本**万元，****年**月**日股东****公司向***公司转让**万元股权。", textFont);
		p1.setFirstLineIndent(23);
		p1.setSpacingAfter(15);
		doc.add(p1);

		p1 = new Paragraph(
				"信息主体于20**年首次与金融机构发生信贷关系，报告期内，共在**家金融机构办理过信贷业务，目前**家金融机构对信息主体授信共**万元，**家金融机构**万元贷款未结清。提供对外担保****万元。",
				textFont);
		p1.setFirstLineIndent(23);
		p1.setSpacingAfter(15);
		doc.add(p1);

		p1 = new Paragraph(
				"报告期内，信息主体共有**条人民银行表彰记录、**条外汇管理表彰记录、**条环保部门表彰记录、**条质监部门表彰记录、**条税务部门表彰记录、**条财政部门表彰记录、**条工商部门表彰记录、**条卫生部门表彰记录、**条海关部门表彰记录、**条其他部门表彰记录。",
				textFont);
		p1.setFirstLineIndent(23);
		p1.setSpacingAfter(15);
		doc.add(p1);

		p1 = new Paragraph(
				"报告期内，信息主体共有**条人民银行处罚记录、**条外汇管理处罚记录、**条环保部门处罚记录、${userName}**条质监部门处罚记录、**条税务部门处罚记录、**条财政部门处罚记录、**条工商部门处罚记录、**条卫生部门处罚记录、**条海关部门处罚记录、**条其他部门处罚记录。共有**条欠税记录、**条欠水/电/汽记录、**条法院判决执行记录。",
				textFont);
		p1.setFirstLineIndent(23);
		doc.add(p1);

		p1 = new Paragraph("信息主体最近一次缴纳社会保险为****年**月**日，最近一次缴纳公积金为****年**月**日。", textFont);
		p1.setFirstLineIndent(23);
		doc.add(p1);

		p1 = new Paragraph("目前，报告中共有*条报数机构说明、*条信息主体说明、*条服务中心说明。", textFont);
		p1.setFirstLineIndent(23);
		p1.setSpacingAfter(15);
		doc.add(p1);

		p1 = new Paragraph();
		p1.setSpacingAfter(10);
		ph1 = new Phrase();
		Chunk c15 = new Chunk(leftPad("身份信息", 7), boldFont);
		ph1.add(c15);
		p1.add(ph1);
		doc.add(p1);

		// 创建一个有4列的表格
		PdfPTable table = new PdfPTable(4);
		table.setTotalWidth(new float[] { 105, 170, 105, 170 }); // 设置列宽
		table.setLockedWidth(true); // 锁定列宽

		PdfPCell cell;
		cell = new PdfPCell(new Phrase("企业名称", boldFont));
		cell.setBorderWidthLeft(3);
		cell.setBorderWidthTop(3);
		cell.setMinimumHeight(30); // 设置单元格高度
		cell.setUseAscender(true); // 设置可以居中
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); // 设置水平居中
		cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE); // 设置垂直居中
		table.addCell(cell);
		cell = new PdfPCell();
		cell.setBorderWidthRight(3);
		cell.setBorderWidthTop(3);
		cell.setUseAscender(true); // 设置可以居中
		cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE); // 设置垂直居中
		cell.setColspan(3);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("统一社会信用代码（组织机构代码）", boldFont));
		cell.setBorderWidthLeft(3);
		cell.setMinimumHeight(40); // 设置单元格高度
		cell.setUseAscender(true); // 设置可以居中
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); // 设置水平居中
		cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE); // 设置垂直居中
		table.addCell(cell);
		cell = new PdfPCell(new Phrase(" ", textFont));
		cell.setUseAscender(true); // 设置可以居中
		cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE); // 设置垂直居中
		table.addCell(cell);
		cell = new PdfPCell(new Phrase("注册地址", boldFont));
		cell.setUseAscender(true); // 设置可以居中
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); // 设置水平居中
		cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE); // 设置垂直居中
		table.addCell(cell);
		cell = new PdfPCell(new Phrase(" ", textFont));
		cell.setBorderWidthRight(3);
		cell.setUseAscender(true); // 设置可以居中
		cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE); // 设置垂直居中
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("行业分类", boldFont));
		cell.setBorderWidthLeft(3);
		cell.setMinimumHeight(30); // 设置单元格高度
		cell.setUseAscender(true); // 设置可以居中
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); // 设置水平居中
		cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE); // 设置垂直居中
		table.addCell(cell);
		cell = new PdfPCell(new Phrase(" ", textFont));
		cell.setUseAscender(true); // 设置可以居中
		cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE); // 设置垂直居中
		table.addCell(cell);
		cell = new PdfPCell(new Phrase("实缴资本", boldFont));
		cell.setUseAscender(true); // 设置可以居中
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); // 设置水平居中
		cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE); // 设置垂直居中
		table.addCell(cell);
		cell = new PdfPCell(new Phrase(" ", textFont));
		cell.setBorderWidthRight(3);
		cell.setUseAscender(true); // 设置可以居中
		cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE); // 设置垂直居中
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("数据来源", boldFont));
		cell.setBorderWidthLeft(3);
		cell.setBorderWidthBottom(3);
		cell.setMinimumHeight(30); // 设置单元格高度
		cell.setUseAscender(true); // 设置可以居中
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); // 设置水平居中
		cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE); // 设置垂直居中
		table.addCell(cell);
		cell = new PdfPCell(new Phrase(" ", textFont));
		cell.setBorderWidthBottom(3);
		cell.setUseAscender(true); // 设置可以居中
		cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE); // 设置垂直居中
		table.addCell(cell);
		cell = new PdfPCell(new Phrase("采集时间", boldFont));
		cell.setBorderWidthBottom(3);
		cell.setUseAscender(true); // 设置可以居中
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); // 设置水平居中
		cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE); // 设置垂直居中
		table.addCell(cell);
		cell = new PdfPCell(new Phrase(" ", textFont));
		cell.setBorderWidthRight(3);
		cell.setBorderWidthBottom(3);
		cell.setUseAscender(true); // 设置可以居中
		cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE); // 设置垂直居中
		table.addCell(cell);
		doc.add(table);

		p1 = new Paragraph();
		p1.setSpacingBefore(20);
		p1.setSpacingAfter(10);
		ph1 = new Phrase();
		Chunk c18 = new Chunk(leftPad("出资情况", 7), boldFont);
		ph1.add(c18);
		p1.add(ph1);
		doc.add(p1);

		table = new PdfPTable(9);
		table.setTotalWidth(new float[] { 80, 60, 60, 50, 60, 60, 60, 60, 60 }); // 设置列宽
		table.setLockedWidth(true); // 锁定列宽

		table = createTable(table,
				new String[] { "出资方名称", "证件类型", "证件号码", "币种", "出资金额", "出资方式", "出资占比", "数据来源", "采集时间" }, 2, 9);
		doc.add(table);

		p1 = new Paragraph();
		p1.setSpacingBefore(20);
		p1.setSpacingAfter(10);
		ph1 = new Phrase();
		Chunk c63 = new Chunk(leftPad("行政处罚信息", 9), boldFont);
		ph1.add(c63);
		p1.add(ph1);
		doc.add(p1);

		table = new PdfPTable(10);
		table.setTotalWidth(new float[] { 55, 55, 55, 55, 55, 55, 55, 55, 55, 55 }); // 设置列宽
		table.setLockedWidth(true); // 锁定列宽

		table = createTable(table,
				new String[] { "处罚文书号", "裁定处罚部门", "处罚时间", "违法或违规行为描述", "涉及金额", "处罚金额", "处罚决定", "整改情况", "数据来源", "采集时间" },
				2, 10);
		doc.add(table);

		p1 = new Paragraph();
		p1.setSpacingBefore(20);
		p1.setSpacingAfter(10);
		ph1 = new Phrase();
		Chunk c66 = new Chunk(leftPad("司法信息", 7), boldFont);
		ph1.add(c66);
		p1.add(ph1);
		doc.add(p1);

		table = new PdfPTable(7);
		table.setTotalWidth(new float[] { 70, 80, 80, 80, 80, 80, 80 }); // 设置列宽
		table.setLockedWidth(true); // 锁定列宽

		table = createTable(table, new String[] { "案号", "立案法院", "立案日期", "案由", "执行标的", "执行标的金额", "已执行标的" }, 2, 7);
		table = createTable(table, new String[] { "已执行标的金额", "执行标的日期", "案件状态", "结案日期", "执行结案方式", "数据来源", "采集时间" }, 2, 7);
		doc.add(table);

		p1 = new Paragraph();
		p1.setSpacingBefore(20);
		p1.setSpacingAfter(10);
		ph1 = new Phrase();
		Chunk c69 = new Chunk(leftPad("银行授信", 7), boldFont);
		Chunk c70 = new Chunk("${userName}", boldFont);
		ph1.add(c69);
		ph1.add(c70);
		p1.add(ph1);
		doc.add(p1);

		table = new PdfPTable(8);
		table.setTotalWidth(new float[] { 40, 100, 75, 75, 75, 60, 60, 60 }); // 设置列宽
		table.setLockedWidth(true); // 锁定列宽

		table = createTable(table, new String[] { "编号", "授信金融机构", "授信额度", "起始日期", "终止日期", "银行内部评级结果", "数据来源", "采集时间" },
				2, 8);
		doc.add(table);

		p1 = new Paragraph();
		p1.setSpacingBefore(20);
		p1.setSpacingAfter(10);
		ph1 = new Phrase();
		Chunk c72 = new Chunk(leftPad("银行贷款", 7), boldFont);
		ph1.add(c72);
		p1.add(ph1);
		doc.add(p1);

		table = new PdfPTable(10);
		table.setTotalWidth(new float[] { 30, 55, 75, 55, 55, 55, 55, 55, 55, 55 }); // 设置列宽
		table.setLockedWidth(true); // 锁定列宽

		table = createTable(table,
				new String[] { " ", "编号", "贷款金融机构", "贷款余额", "贷款日期", "到期日", "五级分类", "欠息金额", "数据来源", "采集时间" }, 1, 10);
		table = createTable(table, null, 3, 10);
		table = createTable(table, null, 3, 10);
		doc.add(table);

		p1 = new Paragraph();
		p1.setSpacingBefore(20);
		p1.setSpacingAfter(10);
		ph1 = new Phrase();
		Chunk c75 = new Chunk(leftPad("贴现", 5), boldFont);
		Chunk c76 = new Chunk("${userName}", boldFont);
		ph1.add(c75);
		ph1.add(c76);
		p1.add(ph1);
		doc.add(p1);

		table = new PdfPTable(8);
		table.setTotalWidth(new float[] { 40, 155, 60, 60, 60, 60, 60, 60 }); // 设置列宽
		table.setLockedWidth(true); // 锁定列宽

		table = createTable(table, new String[] { "编号", "贴现金融机构", "贴现金额", "贴现日", "贴现率", "五级分类", "数据来源", "采集时间" }, 2, 8);
		doc.add(table);

		p1 = new Paragraph();
		p1.setSpacingBefore(20);
		p1.setSpacingAfter(10);
		ph1 = new Phrase();
		Chunk c86 = new Chunk(leftPad("信息主体说明", 9), boldFont);
		ph1.add(c86);
		p1.add(ph1);
		doc.add(p1);

		table = new PdfPTable(1);
		table.setTotalWidth(new float[] { 500 }); // 设置列宽
		table.setLockedWidth(true); // 锁定列宽

		table = createTable(table, null, 1, 1);
		doc.add(table);

		p1 = new Paragraph();
		p1.setSpacingBefore(20);
		p1.setSpacingAfter(10);
		ph1 = new Phrase();
		Chunk c88 = new Chunk(leftPad("查询记录", 7), boldFont);
		ph1.add(c88);
		p1.add(ph1);
		doc.add(p1);

		table = new PdfPTable(4);
		table.setTotalWidth(new float[] { 220, 100, 100, 100 }); // 设置列宽
		table.setLockedWidth(true); // 锁定列宽

		table = createTable(table, new String[] { "序号", "查询日期", "查询用户", "查询原因" }, 3, 4);
		table.addCell("1");
		table.addCell("2017-10-09");
		table.addCell("${userName}");
		table.addCell("房贷查询");
		table.completeRow();
		doc.add(table);

		p1 = new Paragraph();
		p1.setSpacingBefore(20);
		p1.setSpacingAfter(10);
		ph1 = new Phrase();
		Chunk c89 = new Chunk("异议标注（用于记录信息主体对某类信息提出异议处理情况，置于每类信息项下方）：", redBoldFont);
		Chunk c90 = new Chunk(
				"信息主体于2016年**月**日提出异议：我公司从未发生过***；业务发生机构于2016年**月**日提交说明：该笔信息确实存在；信息主体于2016年**月**日提出声明：该笔信息为我公司***所致。（在每一板块下详细记录信息主体提出异议处理过程和结果。）",
				redTextFont);
		ph1.add(c89);
		ph1.add(c90);
		p1.add(ph1);
		doc.add(p1);

		doc.close();
	}

	/**
	 * 创建单元格
	 * 
	 * @param table
	 * @param row
	 * @param cols
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	private static PdfPTable createTable(PdfPTable table, String[] title, int row, int cols)
			throws DocumentException, IOException {
		// 添加中文字体
		BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		Font font = new Font(bfChinese, 11, Font.BOLD);

		for (int i = 0; i < row; i++) {

			for (int j = 0; j < cols; j++) {

				PdfPCell cell = new PdfPCell();

				if (i == 0 && title != null) {// 设置表头
					cell = new PdfPCell(new Phrase(title[j], font)); // 这样表头才能居中
					
					if (table.getRows().isEmpty()) {
						cell.setBorderWidthTop(1);
					}
				}

				if (row == 1 && cols == 1) { // 只有一行一列
					cell.setBorderWidthTop(1);
				}

				if (j == 0) {// 设置左边的边框宽度
					cell.setBorderWidthLeft(1);
				}

				if (j == (cols - 1)) {// 设置右边的边框宽度
					cell.setBorderWidthRight(1);
				}

				if (i == (row - 1)) {// 设置底部的边框宽度
					cell.setBorderWidthBottom(1);
				}

				cell.setMinimumHeight(40); // 设置单元格高度
				cell.setUseAscender(true); // 设置可以居中
				cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); // 设置水平居中
				cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE); // 设置垂直居中

				table.addCell(cell);
			}
		}

		return table;
	}
	
	/**
	 * 设置左边距
	 * 
	 * @param str
	 * @param i
	 * @return
	 */
	private static String leftPad(String str, int i) {
		int addSpaceNo = i - str.length();
		String space = "";
		for (int k = 0; k < addSpaceNo; k++) {
			space = " " + space;
		}
		;
		String result = space + str;
		return result;
	}

}
