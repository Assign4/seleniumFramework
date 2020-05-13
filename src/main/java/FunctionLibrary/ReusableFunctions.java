package FunctionLibrary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.DataProvider;

import PageObjects.CommonPageMyAccount;
import PageObjects.OrderDetailsPage;
import driver.Driver;

public class ReusableFunctions extends Driver {
	private static Logger objLgr=LogManager.getLogger(ReusableFunctions.class.getName());
	private static FileInputStream fis;
	private static Properties prop;
	private static String propFilePath = "src/main/java/driver/AutomationParameters.properties";
	public CommonPageMyAccount commonPageMyAccount;
	public OrderDetailsPage ordDtls;
	
	//Method to read data from excel and work as a data provider
	@DataProvider(name="dataset")
	public static Object[][] universalDataProvider_v2(String FilePath,String SheetName) throws IOException {
		//Initializing object to read data from properties file
		prop = new Properties();
		try{
			fis = new FileInputStream(propFilePath);
			//Loading properties file
			prop.load(fis);
		}catch(FileNotFoundException fe) {
			objLgr.error("Failed to read Property file");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			objLgr.error("Error loading properties file.");
		}

		String[][] tabArray = null;
		Object[][] data = null;
		DataFormatter objDataFormatter = new DataFormatter();

		try {
			FileInputStream fis = new FileInputStream(FilePath);
			XSSFWorkbook wb = new XSSFWorkbook(fis);
			FormulaEvaluator objFormulaEvaluator = new XSSFFormulaEvaluator(wb);
			XSSFSheet ws = wb.getSheet(SheetName);
			XSSFRow rw = ws.getRow(1);		
			XSSFCell cell = null;
			XSSFCell cellhrd = null;
			XSSFCell cellval = null;
			int ci,cj,totalRows,totalCols;
			int firstRow = 1;
			int firstCol = 0;			
			totalRows = ws.getLastRowNum() + 1;
			totalCols = rw.getLastCellNum();
			data=new Object[totalRows-1][1];
			Hashtable<String,String> table=null;
			//objLgr.info("Reading " + FilePath + " sheet " + SheetName + " # of rows: " + totalRows + " ; # of columns: " + totalCols); 
			tabArray=new String[totalRows][totalCols];

			ci=0;
			for (int i=firstRow;i<totalRows;i++, ci++) {
				//objLgr.info("debug 1");
				table = new Hashtable<String,String>();
				cj=0;
				//objLgr.info("debug 2");
				for (int j=firstCol;j<totalCols;j++, cj++){
					//objLgr.info("debug 3");
					cell = wb.getSheet(SheetName).getRow(i).getCell(j);
					objFormulaEvaluator.evaluate(cell);
					tabArray[ci][cj]= objDataFormatter.formatCellValue(cell, objFormulaEvaluator);
					cellhrd = wb.getSheet(SheetName).getRow(0).getCell(j);
					objFormulaEvaluator.evaluate(cellhrd);
					cellval = wb.getSheet(SheetName).getRow(i).getCell(j);
					objFormulaEvaluator.evaluate(cellval);					
					table.put(objDataFormatter.formatCellValue(cellhrd, objFormulaEvaluator), objDataFormatter.formatCellValue(cellval, objFormulaEvaluator));
					//					objLgr.info("row=" + i + " column=" + j + " ci=" + ci + " cj=" + cj + " " + " totalCols=" + totalCols + " " + tabArray[ci][cj] + "\n");
				}
				data[i-1][0] = table;
			}
		}

		catch (FileNotFoundException e){
			objLgr.error("Could not read the Excel sheet" + FilePath + ": " + SheetName);
			e.printStackTrace();
		}
		catch (IOException e){
			objLgr.error("Could not read the Excel sheet" + FilePath + ": " + SheetName);
			e.printStackTrace();
		}
		return data;		
	}
	
	//Method to wait until download is completed
	public void waitForDownloadToComplete(String fullFileName) throws InterruptedException {
		Thread.sleep(5000);
		objLgr.info("Download file path - "+fullFileName);
		File file;
		do {
			file = new File(fullFileName);
		} while (!file.exists());
		objLgr.info("Download successful");
	}

	//Method to move file from downloaded folder to mentioned folder location
	public File renameDownloadedFile(String sourceFileName, String targetFileName, String targetFolder) throws InterruptedException {
		String exportedFile = System.getProperty("user.home")+"\\Downloads\\"+sourceFileName;
		File file = new File(exportedFile);
		File destFile = new File(targetFolder+targetFileName);
		file.renameTo(destFile);
		return destFile;
	}
	
	public File renameDownloadedFile_v3(String fullSourceFileName, String fullTargetFileName) throws InterruptedException {
//		String exportedFile = System.getProperty("user.home")+"\\Downloads\\"+sourceFileName;
		objLgr.info("file being moved from " + fullSourceFileName + " to " + fullTargetFileName);
		File file = new File(fullSourceFileName);
		File destFile = new File(fullTargetFileName);
		if(file.renameTo(destFile)){
			objLgr.info("File move successful!");
		} else {
			objLgr.info("File move failed!");
		};
		return destFile;
	}
	
	//Method to get a random number within a max number range
	public int get_RandomInteger(int min, int max, String RandomLength) {
		int randomnumber = 0;
		int range = (max - min) + 1;  
		if(max < Integer.parseInt(RandomLength)) {
			randomnumber = 1;
		}
		else {
			randomnumber = (int)(Math.random() * range);
		}
		if(randomnumber<0)
			randomnumber=randomnumber*-1;
		
		return randomnumber;
    }
	
    public String getSubString(String str,int length) {
        Random random = new Random();
        String randomString= null;

        int index = random.nextInt(str.length()-length);
        if((int)str.charAt(index)==0)
            randomString = str.substring(index+1, (index+length+1));
        else
            randomString = str.substring(index, (index+length));

        return randomString;
    }
    
    public String get_RandomString(List<Hashtable<String, String>> tableData) {
        String random_String=null;
        Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~//-]");
        List<Hashtable<String, String>> table_Data = tableData;
        do {
        	int iterator=0;
        	int random_number=get_RandomInteger(1, tableData.size(), "1");
            Hashtable<String, String> random_Row=tableData.get(random_number);
            int random_key=get_RandomInteger(1, random_Row.keySet().size(), "1");
        	//objLgr.info(">>>>> Inside Do Loop <<<<<");
        	for(String key:random_Row.keySet()) {
        		//objLgr.info("+++++ Inside For Loop +++++" + iterator);
        		if(random_key==iterator) {
                	//objLgr.info(">>>>> Inside If <<<<<");
        			if(random_Row.get(key).toString().length()!=0) {
                	    int startIndex = get_RandomInteger(1, random_Row.get(key).toString().length(), "3");
	                	if (startIndex == 0)
	                		random_String=random_Row.get(key).toString().substring(startIndex + 1);
	                	else if (startIndex > 3)
	                		random_String=random_Row.get(key).toString().substring(startIndex - 3, startIndex);
	                	else {
	                		if(startIndex<=random_Row.get(key).toString().length()-1)
	                		 random_String=random_Row.get(key).toString().substring(startIndex);
	                		else {
	                			startIndex=startIndex-random_Row.get(key).toString().length();
	                		    random_String=random_Row.get(key).toString().substring(startIndex);
	                		}
	                	}
	                	objLgr.info("Random String generated as - "+random_String);
                	}
                	else
                		random_String="0";
    	            break;
                }
                else
                	iterator++;
            }
        } while (random_String.length() < 2);
        
        Matcher hasSpecial = special.matcher(random_String);
        boolean b = hasSpecial.matches();
        
        if (b == true)
        	random_String = get_RandomString(table_Data);
        
        return random_String;
    }
    
    public Boolean nextPageButtonApplies(String totRecText){
		Boolean nextPageButtonApplies = false;
		int currLastRecord = 0;
		int maxRecords = 0;
		int breakPoint1 = totRecText.indexOf("Page") + 5;
		int breakPoint2 = totRecText.indexOf("\narrow");
		int breakPoint3 = totRecText.indexOf("of") + 3;
		currLastRecord =Integer.parseInt(totRecText.substring(breakPoint1,breakPoint2));
		maxRecords = Integer.parseInt(totRecText.substring(breakPoint3));
		objLgr.info("Current last record number is " + totRecText.substring(breakPoint1,breakPoint2));
		objLgr.info("Max record number is " + totRecText.substring(breakPoint3));
		if(currLastRecord < maxRecords)
			nextPageButtonApplies = true;
		return nextPageButtonApplies;
	}
    
    public Hashtable<String, String> toHashtable(String input, String nodeSeparator, String keyValueSeparator){
    	Hashtable<String, String> converted_Data = new Hashtable<String, String>();
    	String[] nodes = input.split(nodeSeparator);
    	for (int i = 0; i < nodes.length; i++) {
    		converted_Data.put(nodes[i].split(keyValueSeparator)[0].trim(), nodes[i].split(keyValueSeparator)[1].trim());
    	}
    	return converted_Data;
    }
    
  //Method to write LMV Response in an Excel
  	public void writeLMVResponseToExcel(String fileLocation, String fileName, String sheetName,List<Hashtable<String, String>> data, String[] fields) throws IOException {
  		XSSFWorkbook wb;
  		XSSFSheet workSheet;
  		int i = 0;
  		File file = new File(fileLocation + fileName);
  		//Check if file exists, if not then create the same
  		if (file.exists()) {
  			wb = new XSSFWorkbook(new FileInputStream(file));
  			workSheet = wb.getSheet(sheetName);
  			i = workSheet.getLastRowNum() + 1;
  		}
  		else {
  			wb = new XSSFWorkbook();
  			workSheet = wb.createSheet(sheetName);
  			i = 1;
  			Row header = workSheet.createRow(0);
  			for (int field = 0; field < fields.length; field++) {
  				header.createCell(field).setCellValue(fields[field]);
  			}
  		}		

  		for (Hashtable<String, String> input : data) {
  			Row row = workSheet.createRow(i);
  			for (int field = 0; field < fields.length; field++) {
  				row.createCell(field).setCellValue(input.get(fields[field]));
  			}
  			i++;
  		}

  		//Write to output File
  		FileOutputStream fileOut = new FileOutputStream(file);
  		fileOut.flush();
  		wb.write(fileOut);
  		fileOut.close();

  		//Closing the workbook
  		wb.close();
  	}
    
    public void writeToTextFile(String fileName, String message) {
		// TODO Auto-generated method stub
		try {
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(message);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
      
    public boolean select_Loction (Hashtable<String, String> location_data, String aggregate_Data,String lob_Data) throws InterruptedException {
    	commonPageMyAccount = new CommonPageMyAccount(webdriver);
    	actn = new Actions(webdriver);
    	String location_Name = null;
    	boolean flag = false;
    	JavascriptExecutor js = (JavascriptExecutor)webdriver;
    	WebElement changeMyViewButton = commonPageMyAccount.changeMyViewButton();
		if(changeMyViewButton!=null) {
			 js.executeScript("arguments[0].scrollIntoView();",changeMyViewButton);
			 changeMyViewButton.click();
			 //objLgr.info("Successfully clicked on MyView Button");
			 for(int i=0; i < location_data.size()/2-1; i++) {
			    WebElement hierarchyButtonInitial = commonPageMyAccount.hierachyButton(i+1);
				if(hierarchyButtonInitial!=null) {
					objLgr.info("Level " +(i+2)+ " Hierarchy drop down button is visible");
					hierarchyButtonInitial.click();
					objLgr.info("Succesfully clicked on level " +(i+2)+ " Hierarchy drop down button");
					List<WebElement> hierarchyListInitial = commonPageMyAccount.hierachyList(i+1);
					location_Name = location_data.get("Level " +(i+2)+ " Account Name");
					for(WebElement location : hierarchyListInitial) {
						if(location.getText().equals(location_Name)) {
							js.executeScript("arguments[0].scrollIntoView();",location);
							location.click();
							objLgr.info(location_Name+" is selected from level "+(i+2)+" location dropdown list");
							flag=true;
							break;
						}
					}
					if(!flag) 
						objLgr.info(location_Name+" is not available in the selected dropdownlist");
				}
				else {
					objLgr.info("No Sub-locations available for the selected Dealer");
					flag=true;
				}
				Thread.sleep(1000);
	         }
			 if(lob_Data.equalsIgnoreCase("Seed")) {
				 if(aggregate_Data.isEmpty())
					 aggregate_Data = "All Locations";
				 List<WebElement> list_aggregateData = commonPageMyAccount.selected_aggregateData_Text();
				 if(!list_aggregateData.isEmpty()) {
					 if(aggregate_Data.equalsIgnoreCase("My Location"))
					 {
						 objLgr.info("Successfully located Aggregate Data option '"+aggregate_Data+"' in MyView section");
						 for(int k =0; k<list_aggregateData.size(); k++)
						 {
							 if(list_aggregateData.get(k).getText().equalsIgnoreCase(aggregate_Data))
							 {
								 commonPageMyAccount.radioButton_LOB(aggregate_Data).click();
								 break;
							 }
						 }
					 }
				 }
				 else
					 objLgr.error("Aggregated Data options locator list is empty");
				 if(commonPageMyAccount.applyButton()!=null) {
					 commonPageMyAccount.applyButton().click();
					 objLgr.info("Succesfully clicked on Apply button");
					 flag=true;
				 }
				 else{
					 objLgr.info("Unable to locate Apply button in MyView section");
					 flag=false;
				 }
			 }
			 else {
				 objLgr.info("Aggregated Data does not apply to :"+lob_Data);
				 flag=true;
			 }
		}
		 else {
			 objLgr.info("My View button is not visible");
			 flag=false;
		 }
		if(flag==true)
			objLgr.info("Setting up of specified locations is succesfull");
		else
			objLgr.info("Failed to set up specified locations in MyView");
		
		return flag;
    }
    
    //Method to compare location in data sheet with IRD
    public Hashtable<String, String> get_LocationData(String sapID_Level1, String sapID_Level2, String sapID_Level3, String sapID_Level4, String sapID_Level5, List<Hashtable<String, String>>[] location_LevelsIRD) {
    	Hashtable<String, String> location_ListNode = new Hashtable<String, String>();
    	if (!sapID_Level5.isEmpty())
    		location_ListNode = find_Node(sapID_Level5, location_LevelsIRD[5], 5);
    	else if (!sapID_Level4.isEmpty())
    		location_ListNode = find_Node(sapID_Level4, location_LevelsIRD[4], 4);
    	else if (!sapID_Level3.isEmpty())
    		location_ListNode = find_Node(sapID_Level3, location_LevelsIRD[3], 3);
    	else if (!sapID_Level2.isEmpty())
    		location_ListNode = find_Node(sapID_Level2, location_LevelsIRD[2], 2);
    	else if (!sapID_Level1.isEmpty())
    		location_ListNode = find_Node(sapID_Level1, location_LevelsIRD[1], 1);
    	return location_ListNode;
    }

    //Method to get node from List of Hashtables
	public Hashtable<String, String> find_Node(String sapAccountID, List<Hashtable<String, String>> location_LevelsIRD, int level) {
		Hashtable<String, String> location_ListFinal = new Hashtable<String, String>();
    	for (Hashtable<String, String> location_List : location_LevelsIRD) {
    		if (location_List.get("Level " +level+ " SAP Account ID").equals(sapAccountID)) {
    			location_ListFinal = location_List;
    			break;
    		}
    	}
		return location_ListFinal;
    }
	
	//Method to compare location SAP ID between IRD export and Data sheet
	public boolean compare_LocationSAP(String location_DataSheet, String location_IRD) {
		boolean isLocationDataMatching = false;
		if (location_IRD != null && location_IRD.equals(location_DataSheet))
			isLocationDataMatching = true;
		else if (location_IRD == null && location_DataSheet.isEmpty())
			isLocationDataMatching = true;
		else if (location_IRD != null && !location_IRD.equals(location_DataSheet))
			isLocationDataMatching = false;
		return isLocationDataMatching;
	}
	
	public void changeDealerSRP(List<Hashtable<String, String>>[] flattenHierarchy, int i, String Download, String targetfileName) throws Throwable {
		commonPageMyAccount = new CommonPageMyAccount(webdriver);
		ordDtls = new OrderDetailsPage(webdriver);
		WebDriverWait wait = new WebDriverWait(webdriver, 2);
		actn=new Actions(webdriver);
		String location_Name=null;
		String start_Filename=null;
		String finalSheetName=null;
		WebElement changeMyViewButton = null;
		WebElement next_Hierarchybutton = null; 
		WebElement hierarchyOptionList = null;
		WebElement hierarchyButtonInitial = commonPageMyAccount.hierachyButton(i);
		if(hierarchyButtonInitial!=null) {
			objLgr.info("Level "+(i+1)+" Hierarchy drop down button is visible");
			actn.moveToElement(hierarchyButtonInitial).click().build().perform();
			objLgr.info("Succesfully clicked on level "+(i+1)+" Hierarchy drop down button");
			List<WebElement> hierarchyListInitial = commonPageMyAccount.hierachyList(i);
			for(int j = 1; j<hierarchyListInitial.size(); j++) {
				hierarchyOptionList = hierarchyListInitial.get(j);
				if(hierarchyOptionList!=null) {
				start_Filename=null;
				JavascriptExecutor js= (JavascriptExecutor)webdriver;
				js.executeScript("arguments[0].scrollIntoView();",hierarchyOptionList);
				location_Name = hierarchyOptionList.getText().toString();
				objLgr.info(location_Name+" option from level "+(i+1)+" is visible");
				actn.moveToElement(hierarchyOptionList).click().build().perform();
				}
				else
					continue;
				if(!location_Name.equals("ALL LOCATIONS")) {
					if(validate_location(location_Name,flattenHierarchy,i)) {         // Condition to check selected location with IRD data
						objLgr.info(location_Name+" is matching with IRD data");
						objLgr.info(location_Name+" option from level "+(i+1)+" is selected");
						start_Filename=getFileName(location_Name,flattenHierarchy,i);  //Fetching the SAP hierachy for selected location in a string
					}
					else {
						objLgr.error(location_Name+" is not matching with IRD data");
						start_Filename=location_Name;
					}
				}
				else {
					if(i==1) {
						start_Filename=getFileName(location_Name,flattenHierarchy,1);
					}
					else
						continue;
				}
				finalSheetName=start_Filename+"-"+targetfileName;
				if(commonPageMyAccount.applyButton()!=null) {
					actn.moveToElement(commonPageMyAccount.applyButton()).click().build().perform();
				    objLgr.info("Succesfully clicked on Apply button");
				}
				else
					objLgr.info("Failed to click on Apply button : Apply button WebElement is null");
				try {
					if(ordDtls.get_Table() != null) {
								if(ordDtls.get_lnk_Download()!=null) {
									ordDtls.get_lnk_Download().click();
									renameDownloadedFile(Download,finalSheetName);
								}
								else
									objLgr.info("Clicking on Download button failed");
							}
							else
								objLgr.info("No data to download for the location "+location_Name);
				}
				catch(Exception e){
							objLgr.info("No data to download for the location "+location_Name);
				}
				changeMyViewButton = commonPageMyAccount.changeMyViewButton();	
				if(changeMyViewButton!=null) {
					actn.moveToElement(changeMyViewButton).click().build().perform();
					objLgr.info("Succesfully clicked on My View button");
					Thread.sleep(1000);
					if(i!=3) {
						next_Hierarchybutton =commonPageMyAccount.hierachyButton(i+1);
						if(next_Hierarchybutton!=null) {
						  objLgr.info("Sub-Location available for "+location_Name);
						  changeDealerSRP(flattenHierarchy,i+1,Download,targetfileName);
						  objLgr.info("Succesfully validated sub-locations of "+location_Name);
							if(j!=hierarchyListInitial.size()) {
								if(wait.until(ExpectedConditions.elementToBeClickable(hierarchyButtonInitial))!=null) {
								   actn.moveToElement(hierarchyButtonInitial).click().build().perform();
								   objLgr.info("Level "+(i+1)+" dropdown is selected");
							}
							 else
								objLgr.error("Unable to click on level "+(i+1)+ " dropdown button");
							}
						}
						 else {
							objLgr.info("No sub-location available for " +location_Name);
							if(wait.until(ExpectedConditions.elementToBeClickable(hierarchyButtonInitial))!=null) {
							   actn.moveToElement(hierarchyButtonInitial).click().build().perform();
							   objLgr.info("Level "+(i+1)+" dropdown is selected");
							}
							 else
								 objLgr.error("Unable to click on level "+(i+1)+ " dropdown button");
							 }
				   }
					else {
						 if(wait.until(ExpectedConditions.elementToBeClickable(hierarchyButtonInitial))!=null) {
						 actn.moveToElement(hierarchyButtonInitial).click().build().perform();
						 objLgr.info("Level "+(i+1)+" dropdown is selected");
						 }
						  else
							 objLgr.error("Unable to click on level "+(i+1)+ " dropdown button");
					}
				}
				 else
					 objLgr.error("Unable to click on MyView button");
			}
			if(i==1) {
				objLgr.info("Succesfully validated all sub-locations");
				actn.moveToElement(hierarchyButtonInitial).click().build().perform();
				actn.moveToElement(commonPageMyAccount.applyButton()).click().build().perform();
			}	
		}
	}
		
		public boolean validate_location(String location_name, List<Hashtable<String, String>>[] flattenHierarchy,int level) {   
	    	boolean flag=false;
	    	for(Hashtable<String, String> data_AccountHierarchyLevel : flattenHierarchy[level+1]) {
	    	    if (location_name.contains(data_AccountHierarchyLevel.get("Level "+(level+1)+" Account Name"))) {
	    	    	flag = true;
	    	    	objLgr.info(location_name+" matching with IRD data");
	    	    	break;
	    	    }
	    	    else
	    	    	continue;
	    	}
	    	return flag;    
		}
		
		public String getFileName(String location_name,List<Hashtable<String, String>>[] flattenHierarchy,int level) {
	    	String start_Filename=null;
	    	for(Hashtable<String, String> data_AccountHierarchyLevel : flattenHierarchy[level+1]) {
	    		    if(location_name!="All Locations") {
		    		if(location_name.contains(data_AccountHierarchyLevel.get("Level "+(level+1)+" Account Name"))) {
				    	 start_Filename = data_AccountHierarchyLevel.get("Level " + (level+1) + " SAP Account ID");
				    	 break;
				    }
	    		    }
	    		    else {
	    		    	start_Filename = data_AccountHierarchyLevel.get("Level 1" + " SAP Account ID");
	    		    	break;
	    		    }
	    		    	
	    	}
	    	return start_Filename;
		}
		
		//Method to move file from downloaded folder to mentioned folder location
		public void renameDownloadedFile(String sourceFileName, String targetFileName) throws InterruptedException {
			String exportedFile = System.getProperty("user.home")+"\\Downloads\\"+sourceFileName;
			objLgr.info("Download file path - "+sourceFileName);
			File file;
			do {
				file = new File(exportedFile);
			} while (!file.exists());
			objLgr.info("Download successful");
			String targetFolder = prop.getProperty("DownloadPath");
			File file1 = new File(exportedFile);
			File destFile = new File(targetFolder+targetFileName);
			file1.renameTo(destFile);
			objLgr.info("Downloaded file moved to the target folder");
			
		}
		private static final Map<String, String> DATE_FORMAT_REGEXPS = new HashMap<String, String>() {{
	        put("^\\d{8}$", "yyyyMMdd");
	        put("^\\d{1,2}-\\d{1,2}-\\d{4}$", "dd-MM-yyyy");
	        put("^\\d{4}-\\d{1,2}-\\d{1,2}$", "yyyy-MM-dd");
	        put("^\\d{1,2}/\\d{1,2}/\\d{4}$", "MM/dd/yyyy");
	        put("^\\d{4}/\\d{1,2}/\\d{1,2}$", "yyyy/MM/dd");
	        put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}$", "dd MMM yyyy");
	        put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}$", "dd MMMM yyyy");
	        put("^\\d{12}$", "yyyyMMddHHmm");
	        put("^\\d{8}\\s\\d{4}$", "yyyyMMdd HHmm");
	        put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}$", "dd-MM-yyyy HH:mm");
	        put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy-MM-dd HH:mm");
	        put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}$", "MM/dd/yyyy HH:mm");
	        put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy/MM/dd HH:mm");
	        put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMM yyyy HH:mm");
	        put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMMM yyyy HH:mm");
	        put("^\\d{14}$", "yyyyMMddHHmmss");
	        put("^\\d{8}\\s\\d{6}$", "yyyyMMdd HHmmss");
	        put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd-MM-yyyy HH:mm:ss");
	        put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy-MM-dd HH:mm:ss");
	        put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "MM/dd/yyyy HH:mm:ss");
	        put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy/MM/dd HH:mm:ss");
	        put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMM yyyy HH:mm:ss");
	        put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMMM yyyy HH:mm:ss");
	    }};
	    
	    public static String determineDateFormat(String dateString) {
	        for (String regexp : DATE_FORMAT_REGEXPS.keySet()) {
	            if (dateString.toLowerCase().matches(regexp)) {
	                return DATE_FORMAT_REGEXPS.get(regexp);
	            }
	        }
	        return null; // Unknown format.
	    }
}