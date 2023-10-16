package excelHelper;

import java.util.HashMap;

import utility.Constants;

public class DataUtils {

	public Object[][] getData(ExcelReader xls, String testName, String sheetName) {
		int testStartRowNum = 1;
		while (!xls.getCellData(sheetName, 0, testStartRowNum).equals(testName)) {
			testStartRowNum++;
		}
		int colStartRowNum = testStartRowNum + 1;
		int dataStartRowNum = testStartRowNum + 2;

		int rows = 0;
		while (!xls.getCellData(sheetName, 0, dataStartRowNum + rows).equals("")) {
			rows++;
		}

		int cols = 0;
		while (!xls.getCellData(sheetName, cols, colStartRowNum).equals("")) {
			cols++;
		}

		Object[][] data = new Object[rows][1];
		int dataRow = 0;
		HashMap<String, String> table = null;

		for (int rNum = dataStartRowNum; rNum < dataStartRowNum + rows; rNum++) {
			table = new HashMap<String, String>();

			for (int cNum = 0; cNum < cols; cNum++) {
				String key = xls.getCellData(sheetName, cNum, colStartRowNum);
				String value = xls.getCellData(sheetName, cNum, rNum);
				table.put(key, value);
			}
			
			if(!testName.contains("Login")) {
				table.putAll(getLoginData(xls));
			}

			data[dataRow][0] = table;
			dataRow++;
		}
		return data;
	}
	
	public HashMap<String, String> getLoginData(ExcelReader xls) {
		String sheetName = "LoginDetails", testName = "LoginDetails";
		int testStartRowNum = 1;
		while (!xls.getCellData(sheetName, 0, testStartRowNum).equals(testName)) {
			testStartRowNum++;
		}
		
		int colStartRowNum = testStartRowNum + 1;
		int dataStartRowNum = testStartRowNum + 2;

		int rows = 0;
		while (!xls.getCellData(sheetName, 0, dataStartRowNum + rows).equals("")) {
			rows++;
		}

		int cols = 0;
		while (!xls.getCellData(sheetName, cols, colStartRowNum).equals("")) {
			cols++;
		}

		HashMap<String, String> table = null;

		for (int rNum = dataStartRowNum; rNum < dataStartRowNum + rows; rNum++) {
			table = new HashMap<String, String>();
			for (int cNum = 0; cNum < cols; cNum++) {
				String key = xls.getCellData(sheetName, cNum, colStartRowNum);
				String value = xls.getCellData(sheetName, cNum, rNum);
				table.put(key, value);
			}
		}
		return table;
	}

	public boolean isSkip(ExcelReader xls, String testName) {
		int rows = xls.getRowCount(Constants.TESTCASES_SHEET);

		for (int rNum = 2; rNum <= rows; rNum++) {
			String tcid = xls.getCellData(Constants.TESTCASES_SHEET, Constants.TCID_COL, rNum);
			if (tcid.equals(testName)) {
				String runmode = xls.getCellData(Constants.TESTCASES_SHEET, Constants.RUNMODE_COL, rNum);
				return !runmode.equals("Y");
			}
		}
		return true;
	}

	public String testDescription(ExcelReader xls, String testName) {
		int rows = xls.getRowCount(Constants.TESTCASES_SHEET);

		for (int rNum = 2; rNum <= rows; rNum++) {
			String tcid = xls.getCellData(Constants.TESTCASES_SHEET, Constants.TCID_COL, rNum);

			if (tcid.equals(testName)) {
				String runmode = xls.getCellData(Constants.TESTCASES_SHEET, Constants.RUNMODE_COL, rNum);

				if (runmode.equals("Y")) {
					return xls.getCellData(Constants.TESTCASES_SHEET, Constants.DESCRIPTION_COL, rNum);
				}
			}
		}
		return null;
	}

	private int getRnum(ExcelReader xls, String testName, String sheetName) {
		int testStartRowNum = 1;

		while (!xls.getCellData(sheetName, 0, testStartRowNum).equals(testName))
			testStartRowNum++;

		return testStartRowNum + 1;
	}

	private boolean isColNamePresent(ExcelReader xls, String testName, String columnName, String sheetName) {
		int testStartRowNum = 1;
		while (!xls.getCellData(sheetName, 0, testStartRowNum).equals(testName)) {
			testStartRowNum++;
		}
		int colStartRowNum = testStartRowNum + 1;
		int dataStartRowNum = testStartRowNum + 2;

		int rows = 0;
		while (!xls.getCellData(sheetName, 0, dataStartRowNum + rows).equals("")) {
			rows++;
		}
		int cols = 0;
		while (!xls.getCellData(sheetName, cols, colStartRowNum).equals("")) {
			cols++;
		}

		int col = 0;
		for (int rNum = dataStartRowNum; rNum < dataStartRowNum + rows;) {
			for (int cNum = 0; cNum < cols; cNum++) {
				String key = xls.getCellData(sheetName, cNum, colStartRowNum);
				if (key.equals(columnName)) {
					col = cNum;
					break;
				}
			}
			break;
		}
		if (col == 0)
			return true;
		return false;
	}

	private int findRowCol(ExcelReader xls, String testName, String columnName, String sheetName, Boolean isNewData) {
		int testStartRowNum = 1;
		while (!xls.getCellData(sheetName, 0, testStartRowNum).equals(testName)) {
			testStartRowNum++;
		}
		int colStartRowNum = testStartRowNum + 1;
		int dataStartRowNum = testStartRowNum + 2;

		int rows = 0;
		while (!xls.getCellData(sheetName, 0, dataStartRowNum + rows).equals("")) {
			rows++;
		}
		int cols = 0;
		while (!xls.getCellData(sheetName, cols, colStartRowNum).equals("")) {
			cols++;
		}

		if (!isNewData) {
			return cols++;
		} else {
			int col = 0;
			for (int rNum = dataStartRowNum; rNum < dataStartRowNum + rows;) {
				for (int cNum = 0; cNum < cols; cNum++) {
					String key = xls.getCellData(sheetName, cNum, colStartRowNum);
					if (key.equals(columnName)) {
						col = cNum;
						break;
					}
				}
				break;
			}
			return col;
		}
	}

	public void addDataInExcel(ExcelReader xls, String sheetName, String testName, int dataCount, String columnName,
			String data, Boolean isNewData) {
		int col = findRowCol(xls, testName, columnName, sheetName, isNewData);
		if (!isNewData && isColNamePresent(xls, testName, columnName, sheetName))
			xls.setCellData(sheetName, col, (getRnum(xls, testName, sheetName) - 1), columnName); // Updating the header
		// of the column
		xls.setCellData(sheetName, col, (getRnum(xls, testName, sheetName)) + dataCount, data); // Test Data
	}
}