package Utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.apache.poi.xssf.usermodel.*;

public class ExcelUtils {

	public String path;
	public FileInputStream fis = null;
	public FileOutputStream fos = null;
	private XSSFWorkbook workbook = null;
	private XSSFSheet sheet = null;
	private XSSFRow row = null;
	private XSSFCell cell = null;

	/****************** Constructor ***********************/
	public ExcelUtils(String path, String sheetName) {

		this.path = path;
		try {
			fis = new FileInputStream(path); // Accessing the particular Excel file
			workbook = new XSSFWorkbook(fis); // Creating a new workbook object
			if(!isSheetPresent(sheetName)) {
				throw new Exception("The given sheet '"+sheetName
						+"' is NOT present in the mentioned file : "+path);
			}
			sheet = workbook.getSheet(sheetName); // Accessing the particular sheet
			fis.close(); // Closing 'fis' to prevent data leakage
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/********** Returns true if data is set successfully, else false *******************/
	public void setCellData(int colRow, String colName, int rowNum, String data) {
		try {
			// Checking the validity of 'rowNum'
			if (rowNum < 0) {
				throw new Exception("Given row number is invalid!");
			}

			int colNum = -1;
			row = sheet.getRow(colRow);
			for (int i = 0; i < row.getLastCellNum(); i++) {
				if (row.getCell(i).getStringCellValue().trim().equalsIgnoreCase(colName)) {
					// Initialising the 'colNum' value
					colNum = i;
					break;
				}
			}
			if (colNum == -1) {
				throw new Exception("Given column is NOT present in the sheet!");
			}
			
			// Accessing the particular row
			row = sheet.getRow(rowNum);
			if (row == null) {
				row = sheet.createRow(rowNum);
			}

			// Accessing the particular cell
			cell = row.getCell(colNum);
			if (cell == null) {
				cell = row.createCell(colNum);
			}
				
			// Writing the specified value in the specified cell
			cell.setCellValue(data);

			fos = new FileOutputStream(path);
			workbook.write(fos); // Writing data into the excel file using 'fos'
			fos.close(); // Closing 'fos' to prevent data leakage

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/****************** Returns the data from a cell ***********************/
	public String getCellData(String sheetName, int colNum, int rowNum) {
		try {
			if (rowNum < 0)
				return "";

			row = sheet.getRow(rowNum);
			if (row == null)
				return "";
			cell = row.getCell(colNum);
			if (cell == null)
				return "";

			return cell.getStringCellValue();
		} catch (Exception e) {

			e.printStackTrace();
			return "row " + rowNum + " or column " + colNum + " does not exist  in xls";
		}
	}
	
	
	/****************** Find whether sheets exists ***********************/
	public boolean isSheetPresent(String sheetName) {
		int index = workbook.getSheetIndex(sheetName); // Accessing the specified sheet
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
