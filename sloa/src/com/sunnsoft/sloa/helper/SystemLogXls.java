package com.sunnsoft.sloa.helper;

import com.sunnsoft.sloa.db.vo.SystemLog;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.gteam.db.helper.hibernate.HelperException;
import org.gteam.db.helper.hibernate.ScrollEach;
import org.gteam.db.helper.xls.Export;
import org.gteam.db.helper.xls.Import;

import java.io.*;

public class SystemLogXls {
	
	private SystemLogHelper helper;
	
	private Import importer ;
	private Export<SystemLog> exporter;
	
	public SystemLogXls(SystemLogHelper helper) {
		this.helper = helper;
	}

	private InputStream getInputStreamFromWorkbook(final Workbook workbook) throws IOException{
		PipedInputStream in = new PipedInputStream(4096);
		final PipedOutputStream out = new PipedOutputStream(in);
		new Thread(new Runnable(){

			@Override
			public void run() {
				try {
					workbook.write(out);
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					try {
						out.flush();
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					if(workbook instanceof SXSSFWorkbook){
						((SXSSFWorkbook) workbook).dispose();
					}
				}
			}
			
		}).start();
		
	
		return in;
	}
	
	private void processWorkbook(Workbook workbook){
		final Sheet sheet = workbook.createSheet();
		this.exporter.metaRow(sheet.createRow(0));
		this.helper.scrollResult(new ScrollEach<SystemLog>(){

			@Override
			public void each(SystemLog bean, long index) {
				Row row = sheet.createRow((int)index+1);
				try{
					exporter.eachDataRow(bean, row);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
	}
	
	private Workbook exportXls2003(){
		if(this.exporter == null){
			throw new HelperException("请调用setExporter方法设置Export处理类");
		}
		Workbook workbook = new HSSFWorkbook();
		processWorkbook(workbook);
		return workbook;
	}
	
	private Workbook exportXls2007(boolean lessMemoryCost){
		if(this.exporter == null){
			throw new HelperException("请调用setExporter方法设置Export处理类");
		}
		Workbook workbook = lessMemoryCost? 
				new SXSSFWorkbook(100)//只保留100行在内存，其余写入临时文件
				: new XSSFWorkbook();
		processWorkbook(workbook);
		return workbook;
	}
	
	/**
	 * 获取excel2007格式的输入流，以便被struts action使用
	 * @param lessMemoryCost 为true时，采用SXSSFWorkbook的方式（只保持最后得100行数据在内存，其他行使用临时文件保存，对于巨量的导出数据可以极大节省内存占用）
	 *						 缺点是生成临时文件，处理时间稍微有点长。
	 * @return 
	 * @throws IOException
	 */
	public InputStream getXls2007ExportStream(boolean lessMemoryCost) throws IOException{
		return getInputStreamFromWorkbook(this.exportXls2007(lessMemoryCost));
	}
	
	/**
	 * 获取excel2003格式的输入流，以便被struts action使用
	 * @return
	 * @throws IOException
	 */
	public InputStream getXls2003ExportStream() throws IOException{
		return getInputStreamFromWorkbook(this.exportXls2003());
	}
	
	/**
	 * 直接写入到2007格式的excel文件中
	 * @param file
	 * @param lessMemoryCost 为true时，采用SXSSFWorkbook的方式（只保持最后得100行数据在内存，其他行使用临时文件保存，对于巨量的导出数据可以极大节省内存占用）
	 *						 缺点是生成临时文件，处理时间稍微有点长。
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void exportXls2007ToFile(File file,boolean lessMemoryCost) throws FileNotFoundException, IOException{
		OutputStream os = new FileOutputStream(file);
		this.exportXls2007ToStream(os, lessMemoryCost);
		os.flush();
		os.close();
	}
	
	/**
	 * 直接写入到excel2007格式的输出流中
	 * @param os
	 * @param lessMemoryCost 为true时，采用SXSSFWorkbook的方式（只保持最后得100行数据在内存，其他行使用临时文件保存，对于巨量的导出数据可以极大节省内存占用）
	 *						 缺点是生成临时文件，处理时间稍微有点长。
	 * @throws IOException
	 */
	public void exportXls2007ToStream(OutputStream os,boolean lessMemoryCost) throws IOException{
		this.exportXls2007(lessMemoryCost).write(os);
	}
	
	/**
	 * 直接写入到2003格式的excel文件中
	 * @param file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void exportXls2003ToFile(File file) throws FileNotFoundException, IOException{
		OutputStream os = new FileOutputStream(file);
		this.exportXls2003ToStream(os);
		os.flush();
		os.close();
	}
	
	/**
	 * 直接写入到excel2003格式的输出流中
	 * @param os
	 * @throws IOException
	 */
	public void exportXls2003ToStream(OutputStream os) throws IOException{
		this.exportXls2003().write(os);
	}
	
	/**
	 * 设置Import处理类
	 * @param importer
	 * @
	 */
	public SystemLogXls setImporter(Import importer){
		this.importer = importer;
		return this;
	}
	
	/**
	 * 设置Export处理类
	 * @param exporter
	 * @return
	 */
	public SystemLogXls setExporter(Export<SystemLog> exporter){
		this.exporter = exporter;
		return this;
	}
	
	/**
	 * 调用Importer来处理导入的Workbook。
	 * @param workbook
	 */
	public void importXls(Workbook workbook){
		if(this.importer == null){
			throw new HelperException("请调用setImporter方法设置Import处理类");
		}
		Sheet sheet = workbook.getSheetAt(this.importer.getUseSheet());
		int sheetRecord = sheet.getLastRowNum();
		Row metaRow = sheet.getRow(this.importer.getMetaRow());
		this.importer.metaRow(metaRow);
		for(int i = this.importer.getStartRow(); i <= sheetRecord; i++){
			Row row = sheet.getRow(i);
			boolean going = this.importer.eachRow(row, i);
			if(!going){
				break;
			}
		}
	}
	
	/**
	 * 调用Importer来处理导入的excel文件。
	 * @param file
	 */
	public void importXlsFile(File file){
		try {
			Workbook workbook = WorkbookFactory.create(new FileInputStream(file));
			this.importXls(workbook);
		} catch (Exception e) {
			throw new HelperException("导入excel异常",e);
		}
	}


}
