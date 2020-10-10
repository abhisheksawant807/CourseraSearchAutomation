package Utilities;

import java.util.Hashtable;

public class TestDataProvider {

	/************** To get the Data for TestCase ******************/
	public static Object[][] getTestData(String DataFileName, String SheetName, String TestScenario) {

		ReadExcelDataFile readData = new ReadExcelDataFile(
				System.getProperty("user.dir") + "/test-data/" + DataFileName);
		String sheetName = SheetName;
		String testScenario = TestScenario;

		// Find Start Row of TestScenario
		int startRowNum = 0;
		while (!readData.getCellData(sheetName, 0, startRowNum).equalsIgnoreCase(testScenario)) {
			startRowNum++;
		}

		int startTestColumn = startRowNum + 1;
		int startTestRow = startRowNum + 2;

		// Find Number of Rows or TestCases
		int rows = 0;
		while (!readData.getCellData(sheetName, 0, startTestRow + rows).equals("")) {
			rows++;
		}

		// Find Number of Columns in TestCases
		int colmns = 0;
		while (!readData.getCellData(sheetName, colmns, startTestColumn).equals("")) {
			colmns++;
		}

		// Define 2-D Object Array
		Object[][] dataSet = new Object[rows][1];
		Hashtable<String, String> dataTable = null;
		int dataRowNumber = 0;
		for (int rowNumber = startTestRow; rowNumber <= startTestColumn + rows; rowNumber++) {
			dataTable = new Hashtable<String, String>();
			for (int colNumber = 0; colNumber < colmns; colNumber++) {
				String key = readData.getCellData(sheetName, colNumber, startTestColumn);
				String value = readData.getCellData(sheetName, colNumber, rowNumber);
				dataTable.put(key, value);
			}
			dataSet[dataRowNumber][0] = dataTable;
			dataRowNumber++;
		}
		return dataSet;
	}
}
