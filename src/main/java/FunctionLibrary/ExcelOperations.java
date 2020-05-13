package FunctionLibrary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import driver.Driver;

public class ExcelOperations {
	
	XSSFWorkbook wb;
	XSSFSheet ws;
	HashMap<String, String> dataHash = new HashMap<String, String>();
	FileInputStream fis = null;
	private static Logger objLgr = LogManager.getLogger(Driver.class.getName());

	//constructor to initialize the data file
	public ExcelOperations(String sheetName, String filepath) {
		try {
			//Initializing file path
			fis = new FileInputStream(new File(filepath));
			//Initializing workbook
			wb = new XSSFWorkbook(fis);
			//Initializing worksheet
			ws = wb.getSheet(sheetName);
			objLgr.info("Excel file has initiated successfully from "+filepath+" Pass");
		}
		catch(FileNotFoundException fe) {
			fe.printStackTrace();
		} catch (IOException ioe) {
			// TODO Auto-generated catch block
			ioe.printStackTrace();
		}
	}
	
	//Method to fetch each row present in an excel and store them in an Iterator List
	public Iterator<Row> getRows() {
		Iterator<Row> rowIterator = ws.iterator();
		objLgr.info("Rows are fetched from datasheet Pass");
		return rowIterator;
	}
	
	//Method to identify the header row
	public Row getHeader() {
		Row header = ws.getRow(0);
		objLgr.info("Header rows is identified from datasheet Pass");
		return header;
	}
	
	//Method to fetch data from excel and store into hash map
	public HashMap<String, String> storeCellData(Row row, Row header){
		//Fetching each cell in a List for the data row
		Iterator<Cell> cellIterator = row.cellIterator();
		
		//Fetching each cell in a List for the header row
		Iterator<Cell> headercellIterator = header.cellIterator();
		
		//Loop until header row has value
		while(headercellIterator.hasNext()) {
			//Loop through each cell of the data row
			Cell headerCell = headercellIterator.next();
			//Loop through each cell of the header row
			Cell cell = cellIterator.next();
			
			//Fetching values from header row and data row and storing them in a hashmap as key-value pair
			dataHash.put(headerCell.getStringCellValue(), cell.getStringCellValue());		
		}
		objLgr.info("Values are fetched from data sheet and stored in hash map Pass");
		return dataHash;
	}
}
