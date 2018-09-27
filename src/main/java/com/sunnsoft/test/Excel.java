package com.sunnsoft.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

/**
 * 用于一次性处理excel
 * 
 * @author llade
 * 
 */
public class Excel {

	/**
	 * @param args
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws InvalidFormatException
	 */
	public static void main(String[] args) throws InvalidFormatException,
			FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		String[] strs = new String[] { "酷派5820", "中兴N760", "三星I509",
				"HTC A315C", "HTCS710D", "华为C8650", "华为C8800", "酷派9930",
				"酷派D539", "华为S8600", "酷派E239" };
		Random r = new Random();
		System.out.println(strs.length);
		Workbook wb = WorkbookFactory.create(new FileInputStream(new File(
				"ex.xls")));
		Sheet sh = wb.getSheetAt(0);
		for (Iterator iterator = sh.iterator(); iterator.hasNext();) {
			Row row = (Row) iterator.next();
			Cell cell = row.getCell(1);
			// System.out.println(cell);
			if (cell == null)
				continue;
			String phone = getCellValue(cell);
			if (phone.indexOf("诺基亚") != -1) {
				int index = r.nextInt(strs.length);
				System.out.println("index:" + index);
				String t = strs[index];
				System.out.println("phone:" + t);
				cell.setCellValue(t);
			}
		}
		wb.write(new FileOutputStream(new File("ex2.xls")));
	}

	private static String getCellValue(Cell cell) {
		String obj = null;
		if (cell == null) {
			return obj;
		}
		switch (cell.getCellType()) {
//		case HSSFCell.CELL_TYPE_NUMERIC:
			case NUMERIC:
			obj = (int) cell.getNumericCellValue() + "";
			// System.out.println(obj);
			break;
//		case HSSFCell.CELL_TYPE_STRING:
			case STRING:
			obj = StringUtils.isNotEmpty(cell.getStringCellValue()) == true ? cell
					.getStringCellValue()
					: null;
			// System.out.println(obj);
			break;
		default:
			break;
		}
		return obj;
	}
}
