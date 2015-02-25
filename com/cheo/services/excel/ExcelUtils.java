package com.cheo.services.excel;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class ExcelUtils {

	private ExcelUtils(){}

	public static final String NON_ASCII = "[^\\p{ASCII}]";

	public final static String BASE = "/Users/hamidpoursepanj/Desktop/Masters-semesters/Project-Data/annotated-new";

	//Save to these file names from database
	public final static String FILENAME_DIFF = "/Users/hamidpoursepanj/Desktop/Masters-semesters/Project-Data/comments_different.xls";

	public final static String FILENAME_SAME = "/Users/hamidpoursepanj/Desktop/Masters-semesters/Project-Data/comments_same.xls";

	public static String readCell(Sheet sheet, int columnNum, int rowNum) throws Exception{
		Cell cell = sheet.getRow(rowNum).getCell(columnNum, Row.RETURN_BLANK_AS_NULL);
		if(cell == null) return null;
		String value = null;
		try{
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				value = cell.getRichStringCellValue().getString();
				value = StringUtils.chomp(value);
				value = StringUtils.trim(value);
				break;
			case Cell.CELL_TYPE_NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					value = cell.getDateCellValue().toString();
				} else {
					value = Double.toString(cell.getNumericCellValue());
				}
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				value = Boolean.toString(cell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA:
				value = cell.getCellFormula();
				break;
			default:
				throw new Exception("Cell format not supported");
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}

		if(!StringUtils.isBlank(value)){
			value = value.replaceAll(ExcelUtils.NON_ASCII, "").trim();
		}
		return value;
	}
	
	

}
