package excelHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;

public class ExcelReader {
	private String path;
	protected File excel;
	protected FileInputStream fis;
	private FileOutputStream FileOut;
	private XSSFWorkbook workbook;
	private HSSFWorkbook workbookOLE;
	private XSSFSheet sheet;
	private HSSFSheet sheetOLE;
	private XSSFRow row;
	private HSSFRow rowOLE;
	private XSSFCell cell;
	private HSSFCell cellOLE;

	public ExcelReader(String path) {
		this.path = path;
		try (FileInputStream fis = new FileInputStream(path)) {
			// XSSFworkbook to read the xls and xlsx file format files
			workbook = new XSSFWorkbook(fis);
			// Mention the sheet index id to get the sheet
			sheet = workbook.getSheetAt(0);
			//fis.close();
		} catch (IOException e) {
			System.out.println(e.toString());
			Assert.fail("No such file or directory");
		}
	}

	public void ExcelReaderOLE(String path) {
		this.path = path;
		try (FileInputStream fis = new FileInputStream(path)) {
			// XSSFworkbook to read the xls and xlsx file format files
			workbookOLE = new HSSFWorkbook(new FileInputStream(path));
			// Mention the sheet index id to get the sheet
			sheetOLE = workbookOLE.getSheetAt(0);
			//fis.close();
		} catch (IOException e) {
			System.out.println(e.toString());
			Assert.fail("No such file or directory");
		}
	}

	/**
	 * Getting the index number of the sheet name and pass the index number to get
	 ** the RowCount of the sheet add +1 as the row count starts from Zero
	 **/
	public int getRowCount(String SheetName) {
		int index = workbook.getSheetIndex(SheetName);
		if (index == -1)
			return 0;
		else {
			sheet = workbook.getSheetAt(index);
			int number = sheet.getLastRowNum() + 1;
			return number;
		}
	}

	/**
	 * Getting the cell data when the user have the colName so the method accept
	 * STRING, STRING, INTEGER
	 */
	public String getCellData(String sheetName, String colName, int rowNum) {
		try {
			if (rowNum <= 0)
				return "";

			int index = workbook.getSheetIndex(sheetName);
			int col_Num = -1;
			if (index == -1)
				return "";

			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(0);
			// row = sheet.getRow(1);
			for (int i = 0; i < row.getLastCellNum(); i++) {
				// Looping the columns and getting the colnum for the given colname.
				if (row.getCell(i).getStringCellValue().trim().equals(colName.trim()))
					col_Num = i;
			}
			if (col_Num == -1)
				return ""; // When the colnum does not exist and return null
			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(rowNum - 1);
			if (row == null)
				return ""; // When the Row does not exist and return null
			cell = row.getCell(col_Num);

			if (cell == null)
				return ""; // When the cell does not exist/null return null

			if (cell.getCellType() == CellType.STRING)
				return cell.getStringCellValue(); // Returns the string value
			else if (cell.getCellType() == CellType.NUMERIC || cell.getCellType() == CellType.FORMULA) {
				// Check the celltype by numeric and formula realted
				String cellText = String.valueOf(cell.getNumericCellValue());
				if (DateUtil.isCellDateFormatted(cell)) {
					// format in form of M/D/YY
					double d = cell.getNumericCellValue();

					Calendar cal = Calendar.getInstance();
					cal.setTime(DateUtil.getJavaDate(d));

					cellText = (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
					cellText = cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + 1 + "/" + cellText;
				}
				return cellText; // Format the date in the year month and date
			} else if (cell.getCellType() == CellType.BLANK)
				return ""; // Returns when the data is blank
			else
				return String.valueOf(cell.getBooleanCellValue());
		} catch (Exception e) {
			System.out.println(e.toString());
			// e.printStackTrace();
			return "row" + rowNum + "or column" + colName + "does not exist in xls";
		}
	}

	/**
	 * Getting the cell data when the user have the colName so the method accept
	 * STRING, STRING, INTEGER
	 */
	public String getCellDataOLE(String sheetName, String colName, int rowNum) {
		try {
			if (rowNum <= 0)
				return "";

			int index = workbookOLE.getSheetIndex(sheetName);
			int col_Num = -1;
			if (index == -1)
				return "";

			sheetOLE = workbookOLE.getSheetAt(index);
			rowOLE = sheetOLE.getRow(0);
			// row = sheet.getRow(1);
			for (int i = 0; i < rowOLE.getLastCellNum(); i++) {
				// Looping the columns and getting the colnum for the given colname.
				if (rowOLE.getCell(i).getStringCellValue().trim().equals(colName.trim()))
					col_Num = i;
			}
			if (col_Num == -1)
				return ""; // When the colnum does not exist and return null
			sheetOLE = workbookOLE.getSheetAt(index);
			rowOLE = sheetOLE.getRow(rowNum - 1);
			if (rowOLE == null)
				return ""; // When the Row does not exist and return null
			cellOLE = rowOLE.getCell(col_Num);

			if (cellOLE == null)
				return ""; // When the cell does not exist/null return null

			if (cellOLE.getCellType() == CellType.STRING)
				return cellOLE.getStringCellValue(); // Returns the string value
			else if (cellOLE.getCellType() == CellType.NUMERIC || cellOLE.getCellType() == CellType.FORMULA) {
				// Check the celltype by numeric and formula realted
				String cellText = String.valueOf(cellOLE.getNumericCellValue());
				if (DateUtil.isCellDateFormatted(cellOLE)) {
					// format in form of M/D/YY
					double d = cellOLE.getNumericCellValue();

					Calendar cal = Calendar.getInstance();
					cal.setTime(DateUtil.getJavaDate(d));

					cellText = (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
					cellText = cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + 1 + "/" + cellText;
				}
				return cellText; // Format the date in the year month and date
			} else if (cellOLE.getCellType() == CellType.BLANK)
				return ""; // Returns when the data is blank
			else
				return String.valueOf(cellOLE.getBooleanCellValue());
		} catch (Exception e) {
			System.out.println(e.toString());
			// e.printStackTrace();
			return "row" + rowNum + "or column" + colName + "does not exist in xls";
		}
	}

	/**
	 * Getting the cell data when the user dont have the colName so the method
	 * accept STRING, INTEGER, INTEGER
	 */
	public String getCellData(String SheetName, int colNum, int rowNum) {
		try {
			if (rowNum <= 0)
				return "";
			int index = workbook.getSheetIndex(SheetName);

			if (index == -1)
				return "";// Check the sheet name and return the sheet index

			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(rowNum - 1);
			if (row == null)
				return "";
			cell = row.getCell(colNum);
			if (cell == null)
				return "";

			if (cell.getCellType() == CellType.STRING)
				return cell.getStringCellValue(); // Return the String value
			else if (cell.getCellType() == CellType.NUMERIC || cell.getCellType() == CellType.FORMULA) {
				// String cellText = String.valueOf(cell.getNumericCellValue());
				return String.valueOf(cell.getNumericCellValue());
			} else if (cell.getCellType() == CellType.BLANK)
				return "";
			else
				return String.valueOf(cell.getBooleanCellValue());
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println(e.toString());
			return "row " + rowNum + " or column " + colNum + " does not exist  in xls";
		}
	}

	/**
	 * Setting the cell data for the specified sheet and column & row with the data
	 */
	public boolean setCellData(String sheetName, String colName, int rowNum, String data) {
		try (FileInputStream fis = new FileInputStream(path)) {
			workbook = new XSSFWorkbook(fis);

			if (rowNum <= 0)
				return false;

			int index = workbook.getSheetIndex(sheetName);
			int colNum = -1;
			if (index == -1)
				return false;

			sheet = workbook.getSheetAt(index); // Getting the sheet address

			row = sheet.getRow(0);
			// row = sheet.getRow(rowNum);
			for (int i = 0; i < row.getLastCellNum(); i++) {
				// Looping the columns and getting the colnum for the given colname.
				if (row.getCell(i).getStringCellValue().trim().equals(colName.trim()))
					colNum = i;
			}
			if (colNum == -1)
				return false; // When the colnum does not exist and return null

			sheet.autoSizeColumn(colNum);
			row = sheet.getRow(rowNum - 1);
			if (row == null)
				row = sheet.createRow(rowNum - 1); // Creating a row
			cell = row.getCell(colNum);
			if (cell == null)
				cell = row.createCell(colNum);

			// Cell Style
			CellStyle cs = workbook.createCellStyle();
			cs.setWrapText(true);
			cell.setCellStyle(cs);
			cell.setCellValue(data); // Setting the value of the data in the cell on the specified row and column

			try {
				FileOut = new FileOutputStream(path);
				workbook.write(FileOut);
			} finally {
				FileOut.close();
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
		return true;
	}

	/**
	 * Setting the cell data for the specified sheet and column & row with the data
	 */
	public boolean setCellData(String sheetName, int colName, int rowNum, String data) {
		try (FileInputStream fis = new FileInputStream(path)) {
			workbook = new XSSFWorkbook(fis);

			if (rowNum <= 0)
				return false;

			int index = workbook.getSheetIndex(sheetName);
			@SuppressWarnings("unused")
			int colNum = -1;
			if (index == -1)
				return false;

			sheet = workbook.getSheetAt(index); // Getting the sheet address

			// row = sheet.getRow(0);
			row = sheet.getRow(rowNum);
			colNum = colName;

			if (colName == -1)
				return false; // When the colnum does not exist and return null

			sheet.autoSizeColumn(colName);
			// row = sheet.getRow(rowNum-1);
			row = sheet.getRow(rowNum);
			if (row == null)
				row = sheet.createRow(rowNum);
			// row = sheet.createRow(rowNum-1); //Creating a row
			cell = row.getCell(colName);
			if (cell == null)
				cell = row.createCell(colName);

			// Cell Style
			CellStyle cs = workbook.createCellStyle();
			cs.setWrapText(true);
			cell.setCellStyle(cs);
			cell.setCellValue(data); // Setting the value of the data in the cell on the specified row and column
			try {
				FileOut = new FileOutputStream(path);
				workbook.write(FileOut);
			} finally {
				FileOut.close();
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
		return true;
	}

	/**
	 * Setting the data in the given cell and if the data is a url it will be in
	 * Underline format. ACCEPTS STRING, STRING, INT, STRING , STRING
	 */
	public boolean setCellData(String sheetName, String colName, int rowNum, String data, String url) {
		try (FileInputStream fis = new FileInputStream(path)) {
			workbook = new XSSFWorkbook(fis);

			if (rowNum <= 0)
				return false;

			int index = workbook.getSheetIndex(sheetName);
			int colNum = -1;
			if (index == -1)
				return false;

			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(0);
			for (int i = 0; i < row.getLastCellNum(); i++) {
				if (row.getCell(i).getStringCellValue().trim().equalsIgnoreCase(colName))
					colNum = i;
			}

			if (colNum == -1)
				return false;
			sheet.autoSizeColumn(colNum);
			row = sheet.getRow(rowNum - 1);
			if (row == null)
				row = sheet.createRow(rowNum - 1);

			cell = row.getCell(colNum);
			if (cell == null)
				cell = row.createCell(colNum);

			cell.setCellValue(data);
			CreationHelper createHelper = workbook.getCreationHelper(); // cREATES A HELPER BY WHICH CAN BE EDIT THE
																		// CELL IN THE EXCEL

			// cell style for hyperlinks
			// by default hypelrinks are blue and underlined
			CellStyle hlink_style = workbook.createCellStyle();
			Font hlink_font = workbook.createFont();
			hlink_font.setUnderline(Font.U_SINGLE);
			hlink_font.setColor(IndexedColors.BLUE.getIndex());
			hlink_style.setFont(hlink_font);
			// hlink_style.setWrapText(true);

			Hyperlink link = createHelper.createHyperlink(HyperlinkType.FILE); // USING THE HYPERLINK AND GIVING THE
																				// FORMAT OF THE TYPE
			// url=url.replace("\\", "/");
			link.setAddress(url);
			cell.setHyperlink(link);
			cell.setCellStyle(hlink_style);
			try {
				FileOut = new FileOutputStream(path);
				workbook.write(FileOut);
			} finally {
				FileOut.close();
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
		return true;
	}

	/** Creates a sheet and given by the SheetName */
	public boolean addSheet(String sheetname) {
		try (FileOutputStream fileOut = new FileOutputStream(path)) {
			workbook.createSheet(sheetname);
			workbook.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
		return true;
	}

	/**
	 * returns true if sheet is removed successfully else false if sheet does not
	 * exist
	 */
	public boolean removeSheet(String sheetName) {
		int index = workbook.getSheetIndex(sheetName);
		if (index == -1)
			return false;

		try (FileOutputStream fileOut = new FileOutputStream(path)) {
			workbook.removeSheetAt(index);
			workbook.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
		return true;
	}

	/** Adding the column in the given sheet and column name */
	public boolean addColumn(String sheetName, String colName) {
		try (FileInputStream fis = new FileInputStream(path)) {
			workbook = new XSSFWorkbook(fis);
			int index = workbook.getSheetIndex(sheetName);
			if (index == -1)
				return false;

			XSSFCellStyle style = workbook.createCellStyle();
			style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_40_PERCENT.getIndex());
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			sheet = workbook.getSheetAt(index);

			row = sheet.getRow(0);
			if (row == null)
				row = sheet.createRow(0);

			// cell = row.getCell();
			// if (cell == null)
			// System.out.println(row.getLastCellNum());
			if (row.getLastCellNum() == -1)
				cell = row.createCell(0);
			else
				cell = row.createCell(row.getLastCellNum());

			cell.setCellValue(colName);
			cell.setCellStyle(style);
			try {
				FileOut = new FileOutputStream(path);
				workbook.write(FileOut);
			} finally {
				FileOut.close();
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}

		return true;

	}

	/** Removing the column from the sheet */
	public boolean removeColumn(String sheetName, int colNum) {
		try (FileInputStream fis = new FileInputStream(path)) {
			if (!isSheetExist(sheetName))
				return false;
			workbook = new XSSFWorkbook(fis);
			sheet = workbook.getSheet(sheetName);
			XSSFCellStyle style = workbook.createCellStyle();
			style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_40_PERCENT.getIndex());
			style.setFillPattern(FillPatternType.NO_FILL);
			/* Changed the color of the excel sheet */

			for (int i = 0; i < getRowCount(sheetName); i++) {
				row = sheet.getRow(i);
				if (row != null) {
					cell = row.getCell(colNum);
					if (cell != null) {
						cell.setCellStyle(style);
						row.removeCell(cell);
					}
				}
			}
			try {
				FileOut = new FileOutputStream(path);
				workbook.write(FileOut);
			} finally {
				FileOut.close();
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
		return true;

	}

	/** Verify if the Sheet Exist or not */
	public boolean isSheetExist(String sheetName) {
		int index = workbook.getSheetIndex(sheetName);
		if (index == -1) {
			index = workbook.getSheetIndex(sheetName.toUpperCase());
			if (index == -1)
				return false;
			else
				return true;
		} else
			return true;
	}

	/** Return the Column Count from the Sheet */
	public int getColumnCount(String sheetName) {
		// check if sheet exists
		if (!isSheetExist(sheetName))
			return -1;

		sheet = workbook.getSheet(sheetName);
		row = sheet.getRow(0);

		if (row == null)
			return -1;

		return row.getLastCellNum();
	}

	/** Adding the Hyperlink in the desired cell */
	public boolean addHyperLink(String sheetName, String screenShotColName, String testCaseName, int index, String url,
			String message) {
		url = url.replace('\\', '/');
		if (!isSheetExist(sheetName))
			return false;

		sheet = workbook.getSheet(sheetName);

		for (int i = 2; i <= getRowCount(sheetName); i++) {
			if (getCellData(sheetName, 0, i).equalsIgnoreCase(testCaseName)) {
				// System.out.println("**caught "+(i+index));
				setCellData(sheetName, screenShotColName, i + index, message, url);
				break;
			}
		}
		return true;
	}

	/** Get the Cell Row Number from the sheet */
	public int getCellRowNum(String sheetName, String colName, String cellValue) {
		for (int i = 2; i <= getRowCount(sheetName); i++) {
			if (getCellData(sheetName, colName, i).equalsIgnoreCase(cellValue)) {
				return i;
			}
		}
		return -1;
	}

	public boolean setCellDataResult(String sheetName, int colName, int rowNum, String data) {
		try (FileInputStream fis = new FileInputStream(path)) {
			workbook = new XSSFWorkbook(fis);

			if (rowNum <= 0)
				return false;

			int index = workbook.getSheetIndex(sheetName);
			@SuppressWarnings("unused")
			int colNum = -1;
			if (index == -1)
				return false;

			sheet = workbook.getSheetAt(index); // Getting the sheet address

			// row = sheet.getRow(0);
			row = sheet.getRow(rowNum);
			// for(int i=0; i<row.getLastCellNum(); i++) {
			// Looping the columns and getting the colnum for the given colname.
			// if(row.getCell(i).getStringCellValue().trim().equals(colName.trim()))
			colNum = colName;
			// }
			if (colName == -1)
				return false; // When the colnum does not exist and return null

			sheet.autoSizeColumn(colName);
			// row = sheet.getRow(rowNum-1);
			row = sheet.getRow(rowNum);
			if (row == null)
				row = sheet.createRow(rowNum);
			// row = sheet.createRow(rowNum-1); //Creating a row
			cell = row.getCell(colName);
			if (cell == null)
				cell = row.createCell(colName);

			// Cell Style
			CellStyle cs = workbook.createCellStyle();
			cs.setWrapText(true);
			cell.setCellStyle(cs);
			cell.setCellValue(data); // Setting the value of the data in the cell on the specified row and column

			try {
				FileOut = new FileOutputStream(path);
				workbook.write(FileOut);
			} finally {
				FileOut.close();
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
		return true;
	}

	public String ReadRecentDownloadedFile(String directoryPath) {
		// Get a list of files in the directory
		File[] files = new File(directoryPath).listFiles();
		// Sort the files by timestamp in descending order
		Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
		// Find the most recent Excel file
		for (File file : files) {
			if (file.isFile() && file.getName().endsWith(".xlsx")) {
				System.out.println("Recently Downloaded Excel Filepath : " + file.getPath());
				return file.getPath();
				//break;
			}
		}
		//System.out.println("Recently Downloaded Excel Filepath : " + recentFile.getPath());
		//return recentFile.getPath();
		return null;
	}

	/**
	 * Getting the cell data when the user have the colName so the method accept
	 * STRING, STRING, INTEGER
	 */
	public String getCellDataWithCustomRow(int startRow, String sheetName, String columnName, int targetRow) {
		try {
			// Get the first sheet from the workbook
			sheet = workbook.getSheet(sheetName);
			if (sheet == null) {
				workbook.close();
				return "Sheet '" + sheetName + "' does not exist.";
			} else {
				System.out.println("Sheet Name Exist");
			}

			Row headerRow = sheet.getRow(startRow);
			if (headerRow == null) {
				workbook.close();
				return "Row " + startRow + " does not exist.";
			} else {
				System.out.println("Header row name Exist");
			}

			int columnIndex = -1;
			int lastCellIndex = headerRow.getLastCellNum();
			System.out.println("Last cell Index : " + lastCellIndex);
			System.out.println("Column Name : " + columnName);
			for (int i = 0; i < lastCellIndex; i++) {
				Cell cell = headerRow.getCell(i);
				System.out.println(headerRow.getCell(i));
				if (cell != null && cell.getCellType() == CellType.STRING
						&& cell.getStringCellValue().equalsIgnoreCase(columnName)) {
					columnIndex = i;
					break;
				}
			}

			if (columnIndex == -1) {
				workbook.close();
				return "Column '" + columnName + "' not found in the header row.";
			} else {
				System.out.println("Column '" + columnName + "' found in the header row.");
			}

			Row dataRow = sheet.getRow(targetRow);
			if (dataRow == null) {
				workbook.close();
				return "Row " + targetRow + " does not exist.";
			} else {
				System.out.println("Row " + targetRow + " exist.");
			}

			Cell cell = dataRow.getCell(columnIndex);
			if (cell == null) {
				workbook.close();
				return "Cell at row " + targetRow + " and column " + columnName + " does not exist.";
			} else {
				System.out.println("Cell at row " + targetRow + " and column " + columnName + " exist.");
			}

			String cellValue;
			CellType cellType = cell.getCellType();
			if (cellType == CellType.STRING) {
				cellValue = cell.getStringCellValue();
				System.out.println("value of the cell : " + cellValue);
				return cellValue;
			} else if (cellType == CellType.NUMERIC) {
				double numericValue = cell.getNumericCellValue();
				DecimalFormat decimalFormat = new DecimalFormat("#");
				cellValue = decimalFormat.format(numericValue);
				System.out.println("value of the cell : " + cellValue);
				return cellValue;
			} else if (cellType == CellType.BOOLEAN) {
				cellValue = String.valueOf(cell.getBooleanCellValue());
				System.out.println("value of the cell : " + cellValue);
				return cellValue;
			} else if (cellType == CellType.BLANK) {
				cellValue = "";
				System.out.println("value of the cell : " + cellValue);
				return cellValue;
			} else if (cell.getCellType() == CellType.FORMULA) {
				return cell.getCellFormula();
			} else {
				workbook.close();
				return "Unsupported cell type.";

			}
		} catch (Exception e) {
			System.out.println(e.toString());
			// e.printStackTrace();
		}
		return columnName;

	}

}