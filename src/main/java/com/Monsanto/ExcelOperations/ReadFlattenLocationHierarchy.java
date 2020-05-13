package com.Monsanto.ExcelOperations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadFlattenLocationHierarchy {

	private XSSFWorkbook irdWB;
	private HSSFWorkbook xlsread;
	private XSSFSheet irdWS;
	private HSSFSheet xlssheet;
	private static XSSFWorkbook wb;
	public static XSSFSheet ws;
	private HSSFWorkbook wb2;
	public static Properties prop;
	private static String propFilePath = "src/main/java/driver/AutomationParameters.properties";
	public XSSFSheet initializeExcel(String filePath, int index, String sheetName) throws FileNotFoundException, IOException {
		irdWB = new XSSFWorkbook(new FileInputStream(new File(filePath)));
		if (!sheetName.isEmpty())
			irdWS = irdWB.getSheet(sheetName);
		else
			irdWS = irdWB.getSheetAt(index);

		return irdWS;
	}
	public HSSFSheet initializeExcelxls(String filePath, int index, String sheetName) throws FileNotFoundException, IOException {
		xlsread = new HSSFWorkbook(new FileInputStream(new File(filePath)));
		if (!sheetName.isEmpty())
			xlssheet = xlsread.getSheet(sheetName);
		else
			xlssheet = xlsread.getSheetAt(index);

		return xlssheet;
	}
	public List<Hashtable<String, String>> getIRDData(XSSFSheet worksheet) {
		List<Hashtable<String, String>> data_IRDLocationHierarchy = new ArrayList<Hashtable<String, String>>();
		Iterator<Row> rowIterator = worksheet.iterator();

		while(rowIterator.hasNext()) {
			Row row = rowIterator.next();
			Row header = worksheet.getRow(0);

			data_IRDLocationHierarchy.add(storeCellData(row, header));
		}

		return data_IRDLocationHierarchy;
	}
	public List<Hashtable<String, String>> getxlsdata(HSSFSheet worksheet) {
		List<Hashtable<String, String>> data_IRDLocationHierarchy = new ArrayList<Hashtable<String, String>>();
		Iterator<Row> rowIterator = worksheet.iterator();

		while(rowIterator.hasNext()) {
			Row row = rowIterator.next();
			Row header = worksheet.getRow(0);

			data_IRDLocationHierarchy.add(storeCellData(row, header));
		}

		return data_IRDLocationHierarchy;
	}
	//Method to fetch data from excel and store into hash map
	@SuppressWarnings("deprecation")
	public Hashtable<String, String> storeCellData(Row row, Row header){
		Hashtable<String, String> dataHash = new Hashtable<String, String>();
		//Fetching each cell in a List for the data row
		Iterator<Cell> cellIterator = row.cellIterator();

		//Loop until header row has value
		while(cellIterator.hasNext()) {
			//Loop through each cell of the header row
			Cell cell = cellIterator.next();

			//Fetching values from header row and data row and storing them in a hashmap as key-value pair
			switch(cell.getCellType()) {

			case Cell.CELL_TYPE_BLANK:
				dataHash.put(header.getCell(cell.getColumnIndex()).getStringCellValue(), "");
				break;

			case Cell.CELL_TYPE_NUMERIC:
				dataHash.put(header.getCell(cell.getColumnIndex()).getStringCellValue(), Double.toString(cell.getNumericCellValue()));
				break;

			case Cell.CELL_TYPE_FORMULA:
				dataHash.put(header.getCell(cell.getColumnIndex()).getStringCellValue(), cell.getStringCellValue());
				break;

			case Cell.CELL_TYPE_STRING:
				try{
					dataHash.put(header.getCell(cell.getColumnIndex()).getStringCellValue(), cell.getStringCellValue());
					break;
				}catch(IllegalStateException ise) {
					dataHash.put(header.getCell(cell.getColumnIndex()).getStringCellValue(), Double.toString(cell.getNumericCellValue()));
					break;
				}

			}

		}
		return dataHash;
	}

	//Method to fetch All Location Levels
	public List<Hashtable<String, String>> getLocationLevel(List<Hashtable<String, String>> location_Flatten, int level) {
		// TODO Auto-generated method stub
		List<Hashtable<String, String>> dealerLocation_Level1 = new ArrayList<Hashtable<String, String>>();
		for(Hashtable<String, String> data : location_Flatten) {
			if(data.get("Filter").equalsIgnoreCase("Use") && data.get("Final Clean Level 2(SAP ID Only)").equalsIgnoreCase("") && data.get("Final Clean Level 3(SAP ID Only)").equalsIgnoreCase("") && data.get("Final Clean Level 4(SAP ID Only)").equalsIgnoreCase("") && data.get("Final Clean Level 5(SAP ID Only)").equalsIgnoreCase("")) {
				Hashtable<String, String> hierarchy_Updated = new Hashtable<String, String>();
				hierarchy_Updated.put("Level 1 SAP Account ID", data.get("Final Clean Level 1(SAP ID Only)"));
				hierarchy_Updated.put("Level 1 Account Name", data.get("Final Clean Level 1(Name Only)").split(" - ")[0]);
				dealerLocation_Level1.add(hierarchy_Updated);
			}
		}
		return dealerLocation_Level1;
	}
		
	//Method to fetch Location Level 1 list
	public List<Hashtable<String, String>> getLocationLevel1(List<Hashtable<String, String>> location_Flatten) {
		// TODO Auto-generated method stub
		List<Hashtable<String, String>> dealerLocation_Level1 = new ArrayList<Hashtable<String, String>>();
		for(Hashtable<String, String> data : location_Flatten) {
			if(data.get("Filter").equalsIgnoreCase("Use") && data.get("Final Clean Level 2(SAP ID Only)").equalsIgnoreCase("") && data.get("Final Clean Level 3(SAP ID Only)").equalsIgnoreCase("") && data.get("Final Clean Level 4(SAP ID Only)").equalsIgnoreCase("") && data.get("Final Clean Level 5(SAP ID Only)").equalsIgnoreCase("")) {
				Hashtable<String, String> hierarchy_Updated = new Hashtable<String, String>();
				hierarchy_Updated.put("Level 1 SAP Account ID", data.get("Final Clean Level 1(SAP ID Only)"));
				hierarchy_Updated.put("Level 1 Account Name", data.get("Final Clean Level 1(Name Only)").split(" - ")[0]);
				dealerLocation_Level1.add(hierarchy_Updated);
			}
		}
		return dealerLocation_Level1;
	}

	//Method to fetch Location Level 2 list
	public List<Hashtable<String, String>> getLocationLevel2(List<Hashtable<String, String>> location_Flatten) {
		// TODO Auto-generated method stub
		List<Hashtable<String, String>> dealerLocation_Level2 = new ArrayList<Hashtable<String, String>>();
		for(Hashtable<String, String> data : location_Flatten) {
			if(data.get("Filter").equalsIgnoreCase("Use") && !data.get("Final Clean Level 2(SAP ID Only)").isEmpty() && !data.get("Final Clean Level 2(SAP ID Only)").equalsIgnoreCase("") && data.get("Final Clean Level 3(SAP ID Only)").equalsIgnoreCase("") && data.get("Final Clean Level 4(SAP ID Only)").equalsIgnoreCase("") && data.get("Final Clean Level 5(SAP ID Only)").equalsIgnoreCase("")) {
				Hashtable<String, String> hierarchy_Updated = new Hashtable<String, String>();
				hierarchy_Updated.put("Level 1 SAP Account ID", data.get("Final Clean Level 1(SAP ID Only)"));
				hierarchy_Updated.put("Level 1 Account Name", data.get("Final Clean Level 1(Name Only)").split(" - ")[0]);
				hierarchy_Updated.put("Level 2 SAP Account ID", data.get("Final Clean Level 2(SAP ID Only)"));
				hierarchy_Updated.put("Level 2 Account Name", data.get("Final Clean Level 2(Name Only)").split(" - ")[0]);
				dealerLocation_Level2.add(hierarchy_Updated);
			}
		}
		return dealerLocation_Level2;
	}

	//Method to fetch Location Level 3 list
	public List<Hashtable<String, String>> getLocationLevel3(List<Hashtable<String, String>> location_Flatten) {
		// TODO Auto-generated method stub
		List<Hashtable<String, String>> dealerLocation_Level3 = new ArrayList<Hashtable<String, String>>();
		for(Hashtable<String, String> data : location_Flatten) {
			if(data.get("Filter").equalsIgnoreCase("Use") && !data.get("Final Clean Level 3(SAP ID Only)").isEmpty() && !data.get("Final Clean Level 3(SAP ID Only)").equalsIgnoreCase("") && data.get("Final Clean Level 4(SAP ID Only)").equalsIgnoreCase("") && data.get("Final Clean Level 5(SAP ID Only)").equalsIgnoreCase("")) {
				Hashtable<String, String> hierarchy_Updated = new Hashtable<String, String>();
				hierarchy_Updated.put("Level 1 SAP Account ID", data.get("Final Clean Level 1(SAP ID Only)"));
				hierarchy_Updated.put("Level 1 Account Name", data.get("Final Clean Level 1(Name Only)").split(" - ")[0]);
				hierarchy_Updated.put("Level 2 SAP Account ID", data.get("Final Clean Level 2(SAP ID Only)"));
				hierarchy_Updated.put("Level 2 Account Name", data.get("Final Clean Level 2(Name Only)").split(" - ")[0]);
				hierarchy_Updated.put("Level 3 SAP Account ID", data.get("Final Clean Level 3(SAP ID Only)"));
				hierarchy_Updated.put("Level 3 Account Name", data.get("Final Clean Level 3(Name Only)").split(" - ")[0]);
				dealerLocation_Level3.add(hierarchy_Updated);
			}
		}
		return dealerLocation_Level3;
	}

	//Method to fetch Location Level 4 list
	public List<Hashtable<String, String>> getLocationLevel4(List<Hashtable<String, String>> location_Flatten) {
		// TODO Auto-generated method stub
		List<Hashtable<String, String>> dealerLocation_Level4 = new ArrayList<Hashtable<String, String>>();
		for(Hashtable<String, String> data : location_Flatten) {
			if(data.get("Filter").equalsIgnoreCase("Use") && !data.get("Final Clean Level 4(SAP ID Only)").isEmpty() && !data.get("Final Clean Level 4(SAP ID Only)").equalsIgnoreCase("") && data.get("Final Clean Level 5(SAP ID Only)").equalsIgnoreCase("")) {
				Hashtable<String, String> hierarchy_Updated = new Hashtable<String, String>();
				hierarchy_Updated.put("Level 1 SAP Account ID", data.get("Final Clean Level 1(SAP ID Only)"));
				hierarchy_Updated.put("Level 1 Account Name", data.get("Final Clean Level 1(Name Only)").split(" - ")[0]);
				hierarchy_Updated.put("Level 2 SAP Account ID", data.get("Final Clean Level 2(SAP ID Only)"));
				hierarchy_Updated.put("Level 2 Account Name", data.get("Final Clean Level 2(Name Only)").split(" - ")[0]);
				hierarchy_Updated.put("Level 3 SAP Account ID", data.get("Final Clean Level 3(SAP ID Only)"));
				hierarchy_Updated.put("Level 3 Account Name", data.get("Final Clean Level 3(Name Only)").split(" - ")[0]);
				hierarchy_Updated.put("Level 4 SAP Account ID", data.get("Final Clean Level 4(SAP ID Only)"));
				hierarchy_Updated.put("Level 4 Account Name", data.get("Final Clean Level 4(Name Only)").split(" - ")[0]);
				dealerLocation_Level4.add(hierarchy_Updated);
			}
		}
		return dealerLocation_Level4;
	}

	//Method to fetch Location Level 5 list
	public List<Hashtable<String, String>> getLocationLevel5(List<Hashtable<String, String>> location_Flatten) {
		// TODO Auto-generated method stub
		List<Hashtable<String, String>> dealerLocation_Level5 = new ArrayList<Hashtable<String, String>>();
		for(Hashtable<String, String> data : location_Flatten) {
			if(data.get("Filter").equalsIgnoreCase("Use") && !data.get("Final Clean Level 5(SAP ID Only)").isEmpty() && !data.get("Final Clean Level 5(SAP ID Only)").equalsIgnoreCase("")) {
				Hashtable<String, String> hierarchy_Updated = new Hashtable<String, String>();
				hierarchy_Updated.put("Level 1 SAP Account ID", data.get("Final Clean Level 1(SAP ID Only)"));
				hierarchy_Updated.put("Level 1 Account Name", data.get("Final Clean Level 1(Name Only)").split(" - ")[0]);
				hierarchy_Updated.put("Level 2 SAP Account ID", data.get("Final Clean Level 2(SAP ID Only)"));
				hierarchy_Updated.put("Level 2 Account Name", data.get("Final Clean Level 2(Name Only)").split(" - ")[0]);
				hierarchy_Updated.put("Level 3 SAP Account ID", data.get("Final Clean Level 3(SAP ID Only)"));
				hierarchy_Updated.put("Level 3 Account Name", data.get("Final Clean Level 3(Name Only)").split(" - ")[0]);
				hierarchy_Updated.put("Level 4 SAP Account ID", data.get("Final Clean Level 4(SAP ID Only)"));
				hierarchy_Updated.put("Level 4 Account Name", data.get("Final Clean Level 4(Name Only)").split(" - ")[0]);
				hierarchy_Updated.put("Level 5 SAP Account ID", data.get("Final Clean Level 5(SAP ID Only)"));
				hierarchy_Updated.put("Level 5 Account Name", data.get("Final Clean Level 5(Name Only)").split(" - ")[0]);
				dealerLocation_Level5.add(hierarchy_Updated);
			}
		}
		return dealerLocation_Level5;
	}
	public static XSSFSheet selectExcelType(FileInputStream fis, File f1) throws IOException {


		String filename=f1.getAbsolutePath();

		if (FilenameUtils.getExtension(filename).equals("xlsx")) {
			wb = new XSSFWorkbook(fis);
			if(wb.getSheet("Dealer List") != null)
			{	
				ws = wb.getSheet("Dealer List");

			} 

		}

		else
			System.out.println("Please select a valid file.");
		//return ws;
		return ws;	

	}

	@SuppressWarnings("deprecation")
	public List<Hashtable<String, String>> readExcel(String Orderdetailssheeetname, String fileName) throws Exception

	{
		
		FileInputStream fis = new FileInputStream(new File(fileName));
		XSSFSheet ws;				
		wb = new XSSFWorkbook(fis);
		ws = wb.getSheet(Orderdetailssheeetname);
		List<Hashtable<String, String>> dataExcelread = new ArrayList<Hashtable<String, String>>();
		Iterator<Row> rowIterator = ws.rowIterator();


		while(rowIterator.hasNext()) {
			Hashtable<String, String> dataRow = new Hashtable<String, String>();
			Row row = rowIterator.next();
			Row header = ws.getRow(0);

			Iterator<Cell> cellIterator = row.cellIterator();

			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();

				switch(cell.getCellType()) {

				case Cell.CELL_TYPE_BLANK:
					dataRow.put(header.getCell(cell.getColumnIndex()).getStringCellValue(), "");

				case Cell.CELL_TYPE_NUMERIC:
					dataRow.put(header.getCell(cell.getColumnIndex()).getStringCellValue(), Double.toString(cell.getNumericCellValue()));

				case Cell.CELL_TYPE_STRING:
					try{
						dataRow.put(header.getCell(cell.getColumnIndex()).getStringCellValue(), cell.getStringCellValue());
					}catch(IllegalStateException ise) {
						dataRow.put(header.getCell(cell.getColumnIndex()).getStringCellValue(), Double.toString(cell.getNumericCellValue()));
					}

				}

			}
			dataExcelread.add(dataRow);
		}
		return dataExcelread;

	}

	//public List<Hashtable<String, String>> readExcelXLS(HSSFSheet ws) throws IOException
	@SuppressWarnings("deprecation")
	public List<Hashtable<String, String>> readExcelXLS(String Orderdetailssheeetname) throws IOException
	{
	//	String fileName = "C:\\Users\\AAHUJ\\Desktop\\Task\\2018\\08052018\\Inventory (2).xls";
		
		String fileName = "C:\\Users\\AADAS3\\Desktop\\tasks\\2018\\05282018\\Summary Details.xls";
		FileInputStream fis = new FileInputStream(new File(fileName));
		HSSFSheet ws;				
		wb2 = new HSSFWorkbook(fis);
		ws = wb2.getSheet(Orderdetailssheeetname);
		//ws = wb.getSheet("Summary");
		//ws = wb.getSheet("Dealer List");
		List<Hashtable<String, String>> dataExcelread = new ArrayList<Hashtable<String, String>>();
		Iterator<Row> rowIterator = ws.rowIterator();


		while(rowIterator.hasNext()) {
			Hashtable<String, String> dataRow = new Hashtable<String, String>();
			Row row = rowIterator.next();
			Row header = ws.getRow(0);

			Iterator<Cell> cellIterator = row.cellIterator();

			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();

				switch(cell.getCellType()) {

				case Cell.CELL_TYPE_BLANK:
					dataRow.put(header.getCell(cell.getColumnIndex()).getStringCellValue(), "");

				case Cell.CELL_TYPE_NUMERIC:
					dataRow.put(header.getCell(cell.getColumnIndex()).getStringCellValue(), Double.toString(cell.getNumericCellValue()));

				case Cell.CELL_TYPE_STRING:
					try{
						dataRow.put(header.getCell(cell.getColumnIndex()).getStringCellValue(), cell.getStringCellValue());
					}catch(IllegalStateException ise) {
						dataRow.put(header.getCell(cell.getColumnIndex()).getStringCellValue(), Double.toString(cell.getNumericCellValue()));
					}

				}

			}
			dataExcelread.add(dataRow);
		}
		return dataExcelread;

	}

}
