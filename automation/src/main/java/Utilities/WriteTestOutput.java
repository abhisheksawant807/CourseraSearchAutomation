package Utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.apache.poi.xssf.usermodel.*;

public class WriteTestOutput {

	public String path;
	public FileInputStream fis = null;
	public FileOutputStream fos = null;
	private XSSFWorkbook workbook = null;
	private XSSFSheet sheet = null;
	private XSSFRow row = null;
	private XSSFCell cell = null;

	/****************** Constructor ***********************/
	public WriteTestOutput(String path, String sheetName) {

		this.path = path;
		try {
			fis = new FileInputStream(path);
			workbook = new XSSFWorkbook(fis);
			if(!isSheetPresent(sheetName)) {
				throw new Exception("The given sheet '"+sheetName
						+"' is NOT present in the mentioned file : "+path);
			}
			sheet = workbook.getSheet(sheetName);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/********** Returns true if data is set successfully, else false *******************/
	public void setCellData(int colRow, String colName, int rowNum, String data) {
		try {
			if (rowNum <= 0) {
				throw new Exception("Given row number is invalid!");
			}

			int colNum = -1;
			row = sheet.getRow(colRow);
			for (int i = 0; i < row.getLastCellNum(); i++) {
				if (row.getCell(i).getStringCellValue().trim().equalsIgnoreCase(colName)) {
					colNum = i;
				}
			}
			if (colNum == -1) {
				throw new Exception("Given column is NOT present in the sheet!");
			}
			
			row = sheet.getRow(rowNum);
			if (row == null) {
				row = sheet.createRow(rowNum);
			}

			cell = row.getCell(colNum);
			if (cell == null) {
				cell = row.createCell(colNum);
			}
				
			cell.setCellValue(data);

			fos = new FileOutputStream(path);
			workbook.write(fos);
			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/****************** Find whether sheets exists ***********************/
	public boolean isSheetPresent(String sheetName) {
		int index = workbook.getSheetIndex(sheetName);
		if (index == -1) {
			index = workbook.getSheetIndex(sheetName.toUpperCase());
			if (index == -1) {
				index = workbook.getSheetIndex(sheetName.toLowerCase());
				if(index == -1)
					return false;
				else
					return true;
			} else {
				return true;
			}
		} 
		return true;
	}

}
