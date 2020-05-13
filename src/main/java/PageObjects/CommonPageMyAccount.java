package PageObjects;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.utils.DateUtil;

import FunctionLibrary.ReusableFunctions;

public class CommonPageMyAccount {
	public WebDriver driver;
	ReusableFunctions reusefunc = new ReusableFunctions();
	private static Logger objLgr=LogManager.getLogger(CommonPageMyAccount.class.getName());

	public CommonPageMyAccount(WebDriver driver) {
		this.driver=driver;
	}
	//Change My View Locator 
	By button_ChangeMyView  = By.xpath("//i[contains(text(),'filter_list')]");
	//Apply locator from My View Section
	By button_Apply  = By.xpath("//button[contains(text(),'Apply')]");
	//Line of Business text locator
	By lob_Text = By.xpath("//span[contains(text(),'LINE OF BUSINESS')]//parent::section//label");
	//Aggregate Data text locator
	By aggregateData_Text = By.xpath("//span[contains(text(),'AGGREGATE DATA')]//parent::section//label");
	By lineOfBusiness_UI = By.xpath("//span[contains(text(),'LINE OF BUSINESS')]//parent::section//label");

	//Method to check if any element is present in the page and return the element if true else null
	public WebElement isElementFound (By element_Name, String elementName, int timeoutInSec) {
		WebDriverWait wait = new WebDriverWait(driver, timeoutInSec);
		WebElement webElement = null;
		int attempts = 0;
		try {
			while (attempts <= 2) {
				objLgr.info("(isElementFound)  Attempt " + (attempts + 1) + ": " + elementName + " (" + element_Name);
				try {
					wait.until(ExpectedConditions.visibilityOfElementLocated(element_Name));
					webElement = driver.findElement(element_Name);
					break;
					//			objLgr.info(elementName + " found: " + element_Name );
					//		} catch (TimeoutException exp) {
					//			objLgr.info(elementName + " visibility wait timed out after "+timeoutInSec+" seconds. "+exp);
					//		} catch (NoSuchElementException noexp) {
					//			objLgr.info(elementName + " not found : "+noexp);
				} catch (StaleElementReferenceException stelexp) {
					objLgr.info(elementName + " not attached to page object : " + stelexp);
				}
				attempts++ ;
			}
		} catch (TimeoutException exp) {
			objLgr.info(elementName + "(" + element_Name + ") visibility wait timed out after "+timeoutInSec+" seconds. "+exp);
		} catch (NoSuchElementException noexp) {
			objLgr.info(elementName + " not found : " + noexp);
		} catch (Exception e){
			objLgr.info(elementName + " (" + element_Name + " ) Not Found. " + e);
		}
		return webElement;
	}

	//Method to check if any element is present in any dependent element and return the element if true else null
	public WebElement isNestedElementFound (By element_Name, String elementName, int timeoutInSec, WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, timeoutInSec);
		WebElement webElement = null;
		int attempts = 0;
		try {
			while (attempts < 2) {
				objLgr.info("(isNestedElementFound) Attempt " + (attempts + 1) + ": " + elementName + " (" + element_Name);
				try {
					wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(element, element_Name));
					webElement = element.findElement(element_Name);
					break;
					//			objLgr.info(elementName + " element nested under " + element + " element found.");
					//			} catch (TimeoutException exp) {
					//				objLgr.info(elementName + " visibility wait timed out after " + timeoutInSec + " seconds. " + exp);
					//			} catch (NoSuchElementException noexp) {
					//				objLgr.info(elementName + " not found : " + noexp);
				} catch (StaleElementReferenceException stelexp) {
					objLgr.info(elementName + " not attached to page object : " + stelexp);
				}
				attempts++;
			}
		} catch (TimeoutException exp) {
			objLgr.info(elementName + " visibility wait timed out after " + timeoutInSec + " seconds. " + exp);
		} catch (NoSuchElementException noexp) {
			objLgr.info(elementName + " not found : " + noexp);
		} catch (Exception e){
			objLgr.info(elementName + " (" + element_Name + " ) Not Found. " + e);
		}
		return webElement;
	}

	//Method to check if any element is present in the page and return the element if true else null
	public List<WebElement> areElementsFound (By element_Name, String elementName, int timeoutInSec) {
		WebDriverWait wait = new WebDriverWait(driver, timeoutInSec);
		List<WebElement> webElement = null;
		int attempts = 0;
		try{
			while(attempts < 2){
				objLgr.info("(areElementsFound) Attempt " + (attempts + 1) + ": " + elementName + " (" + element_Name);
				try {
					wait.until(ExpectedConditions.visibilityOfElementLocated(element_Name));
					webElement = driver.findElements(element_Name);
					break;
					//			objLgr.info(webElement.size() + " matching elements found for " + elementName + "(" + element_Name + ")");
					//			objLgr.info(elementName + " found.  (" + element_Name + ")");
				} catch (StaleElementReferenceException stelexp) {
					objLgr.info(elementName + " not attached to page object : "+stelexp);
				}
				attempts++;
			}
		} catch (TimeoutException exp) {
			objLgr.info(elementName + " visibility wait timed out after "+timeoutInSec+" seconds. "+exp);
		} catch (NoSuchElementException noexp) {
			objLgr.info(elementName + " not found : "+noexp);
		} catch (Exception e){
			objLgr.info(elementName + " (" + element_Name + " ) Not Found. " + e);
		}

		return webElement;
	}

	//Method to check if any element is present in the page and return the element if true else null
	public List<WebElement> areNestedElementsFound (By element_Name, String elementName, int timeoutInSec, WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, timeoutInSec);
		List<WebElement> webElement = null;
		int attempts = 0;
		try{
			while(attempts <= 2) {
				objLgr.info("(areNestedElementsFound) Attempt " + (attempts + 1) + ": " + elementName + "(" + element_Name + ") nested under " + element);
				try {
					wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(element, element_Name));
					webElement = element.findElements(element_Name);
					//			objLgr.info(elementName + " located by By.xpath:" + element_Name + " nested under " + element + " element found.");
					break;
				} catch (StaleElementReferenceException stelexp) {
					objLgr.info(elementName + "(" + element_Name + ") not found nested under " + element);
					stelexp.printStackTrace();
				}
				attempts++;
			}
		} catch (TimeoutException exp) {
			objLgr.info(elementName + " visibility wait timed out after "+timeoutInSec+" seconds. "+exp);
		} catch (NoSuchElementException noexp) {
			objLgr.info(elementName + " not found : "+noexp);
		} catch (Exception e){
			objLgr.info(elementName + " (" + element_Name + " ) Not Found. " + e);
		}
		return webElement;
	}

	/*Method to read Data from all the tables
	 Create By - Sanjib
	 Data - 07-FEB-2019*/
	public List<Hashtable<String, String>> read_TableData(List<WebElement> header, List<WebElement> row_Data, By value_Field) {
		List<Hashtable<String, String>> data_UITable = new ArrayList<Hashtable<String, String>>();

		for (WebElement row : row_Data) {
			Hashtable<String, String> data = new Hashtable<String, String>();
			List<WebElement> data_UI = areNestedElementsFound(value_Field, "Data", 90, row);

			if (data_UI.size() == header.size()) {
				for (int j = 0; j < header.size(); j++) {
					if(j==0) {
						if(data_UI.get(j).getText().contains(","))
							data.put(header.get(j).getText(), data_UI.get(j).getText().replace(",", ""));
						else
							data.put(header.get(j).getText(), data_UI.get(j).getText());
					}
					else {
						if(data_UI.get(j).getAttribute("innerHTML").trim().contains(","))
							data.put(header.get(j).getText(), data_UI.get(j).getAttribute("innerHTML").trim().replace(",", ""));
						else
							data.put(header.get(j).getText(), data_UI.get(j).getAttribute("innerHTML").trim());
					}

				}
				data_UITable.add(data);
			}
		}
		return data_UITable;		
	}

	/*Method to read Data for a specific row from all the tables
	 Create By - Sanjib
	 Data - 02-APR-2019*/
	public Hashtable<String, String> read_TableData(List<WebElement> header, List<WebElement> row_Data, By value_Field, int row_Num) {
		WebElement row = row_Data.get(row_Num);
		Hashtable<String, String> data = new Hashtable<String, String>();
		List<WebElement> data_UI = areNestedElementsFound(value_Field, "Data", 90, row);

		if (data_UI.size() == header.size()) {
			for (int j = 0; j < header.size(); j++) {
				if(j==0) {
					if(data_UI.get(j).getText().contains(","))
						data.put(header.get(j).getText(), data_UI.get(j).getText().replace(",", ""));
					else
						data.put(header.get(j).getText(), data_UI.get(j).getText());
				}
				else {
					if(data_UI.get(j).getAttribute("innerHTML").trim().contains(","))
						data.put(header.get(j).getText(), data_UI.get(j).getAttribute("innerHTML").trim().replace(",", ""));
					else
						data.put(header.get(j).getText(), data_UI.get(j).getAttribute("innerHTML").trim());
				}

			}
		}
		return data;		
	}

	/*Method to validate if data sorted correctly
	 Create By - Sanjib
	 Data - 07-FEB-2019*/
	public boolean sort_TableData(List<Hashtable<String, String>> data, String field, String sortOrder) throws ParseException {
		boolean sortFlag = false;
		String dateformat = null;
		Date  date_current = null;
		Date  date_next = null;
		if(data.size()>1) { 
			for (int i = 0; i < data.size() - 1; i++) {
				String data_Current = data.get(i).get(field);
				String data_Next = data.get(i+1).get(field);				
				if (sortOrder.toLowerCase().contains("ascending")) {
					try {
						if(!data_Current.isEmpty()) {
							if(field.contains("Date")) {
								dateformat = ReusableFunctions.determineDateFormat(data_Current);
								SimpleDateFormat sdf =new SimpleDateFormat(dateformat);
								date_current = sdf.parse(data_Current);
								date_next = sdf.parse(data_Next);
								if(date_current.after(date_next)) {
									objLgr.info("Value of field " +field+ " has displayed as " +data_Current+ " before " +data_Next+ " in ascending order.");
									sortFlag = false;
									break;
								}
								else
									sortFlag = true;
							}
							else {
								Double data_CurrentInt = Double.parseDouble(data_Current.replace(",", "").replace(")", "").replace("(", "-").replace(" %", "").replace("$", ""));
								Double data_NextInt = Double.parseDouble(data_Next.replace(",", "").replace(")", "").replace("(", "-").replace(" %", "").replace("$", ""));
								if (data_CurrentInt > data_NextInt) {
									objLgr.info("Value of field " +field+ " has displayed as " +data_Current+ " before " +data_Next+ " in ascending order.");
									sortFlag = false;
									break;
								}
								else
									sortFlag = true;
							}
						}
						else
							sortFlag = true;
					} catch (NumberFormatException numExp) {
						if(!data_Current.isEmpty()) {
							if (data_Current.compareTo(data_Next) > 0) {
								objLgr.info("Value of field " +field+ " has displayed as " +data_Current+ " before " +data_Next+ " in ascending order.");
								sortFlag = false;
								break;
							}
							else
								sortFlag = true;
						}
						else
							sortFlag = true;
					}
				}
				else if (sortOrder.toLowerCase().contains("descending")) {
					try {
						if(!data_Current.isEmpty()) {
							if(field.contains("Date")) {
								dateformat = ReusableFunctions.determineDateFormat(data_Current);
								SimpleDateFormat sdf =new SimpleDateFormat(dateformat);
								date_current = sdf.parse(data_Current);
								date_next = sdf.parse(data_Next);
								if(date_current.before(date_next)) {
									objLgr.info("Value of field " +field+ " has displayed as " +data_Current+ " before " +data_Next+ " in descending order.");
									sortFlag = false;
									break;
								}
								else
									sortFlag = true;
							}
							else {
								Double data_CurrentInt =Double.parseDouble(data_Current.replace(",", "").replace(")", "").replace("(", "-").replace(" %", "").replace("$", ""));
								Double data_NextInt = Double.parseDouble(data_Next.replace(",", "").replace(")", "").replace("(", "-").replace(" %", "").replace("$", ""));
								if (data_CurrentInt < data_NextInt) {
									objLgr.info("Value of field " +field+ " has displayed as " +data_Current+ " before " +data_Next+ " in descending order.");
									sortFlag = false;
									break;
								}
								else
									sortFlag = true;
							}
						}
						else
							sortFlag = true;
					} catch (NumberFormatException numExp) {
						if(!data_Current.isEmpty()) {
							if (data_Current.compareTo(data_Next) < 0) {
								objLgr.info("Value of field " +field+ " has displayed as " +data_Current+ " before " +data_Next+ " in descending order.");
								sortFlag = false;
								break;
							}
							else
								sortFlag = true;

						}
						else
							sortFlag = true;
					}
				}
			}
		}
		else
			sortFlag=true;

		return sortFlag;		
	}

	/*Method to validate if data sorted correctly, if the data is of type String (Left Aligned) or of type Integer (Right Aligned)*/
	
	public boolean sort_TableData(List<Hashtable<String, String>> data, String field, String sortOrder, String alignment) throws ParseException {
		boolean sortFlag = false;
		String dateformat = null;
		Date  date_current = null;
		Date  date_next = null;
		if(data.size()>1) { 
			for (int i = 0; i < data.size() - 1; i++) {
				String data_Current = data.get(i).get(field);
				String data_Next = data.get(i+1).get(field);				
				if (sortOrder.toLowerCase().contains("ascending")) {
					if(!data_Current.isEmpty()) {
						if(field.contains("Date")) {
							dateformat = ReusableFunctions.determineDateFormat(data_Current);
							SimpleDateFormat sdf =new SimpleDateFormat(dateformat);
							date_current = sdf.parse(data_Current);
							date_next = sdf.parse(data_Next);
							if(date_current.after(date_next)) {
								objLgr.info("Value of field " +field+ " has displayed as " +data_Current+ " before " +data_Next+ " in ascending order.");
								sortFlag = false;
								break;
							}
							else
								sortFlag = true;
						}
						else if(alignment.toLowerCase().contains("left"))
						{
							String data_CurrentString = data_Current;
							String data_NextString = data_Next;
							if(data_CurrentString.compareTo(data_NextString) > 0) {
								objLgr.info("Value of field " +field+ " has displayed as " +data_Current+ " before " +data_Next+ " in ascending order.");
								sortFlag = false;
								break;
							}
							else
								sortFlag = true;
						}
						else {
							Double data_CurrentInt = Double.parseDouble(data_Current.replace(",", "").replace(")", "").replace("(", "-").replace(" %", "").replace("$", ""));
							Double data_NextInt = Double.parseDouble(data_Next.replace(",", "").replace(")", "").replace("(", "-").replace(" %", "").replace("$", ""));
							if (data_CurrentInt > data_NextInt) {
								objLgr.info("Value of field " +field+ " has displayed as " +data_Current+ " before " +data_Next+ " in ascending order.");
								sortFlag = false;
								break;
							}
							else
								sortFlag = true;
						}
					}
					else
						sortFlag = true;
				}
				else if (sortOrder.toLowerCase().contains("descending")) {
					if(!data_Current.isEmpty()) {
						if(field.contains("Date")) {
							dateformat = ReusableFunctions.determineDateFormat(data_Current);
							SimpleDateFormat sdf =new SimpleDateFormat(dateformat);
							date_current = sdf.parse(data_Current);
							date_next = sdf.parse(data_Next);
							if(date_current.before(date_next)) {
								objLgr.info("Value of field " +field+ " has displayed as " +data_Current+ " before " +data_Next+ " in descending order.");
								sortFlag = false;
								break;
							}
							else
								sortFlag = true;
						}
						else if(alignment.toLowerCase().contains("left"))
						{
							String data_CurrentString = data_Current;
							String data_NextString = data_Next;
							if(data_CurrentString.compareTo(data_NextString) < 0) {
								objLgr.info("Value of field " +field+ " has displayed as " +data_Current+ " before " +data_Next+ " in ascending order.");
								sortFlag = false;
								break;
							}
							else
								sortFlag = true;
						}
						else {
							Double data_CurrentInt =Double.parseDouble(data_Current.replace(",", "").replace(")", "").replace("(", "-").replace(" %", "").replace("$", ""));
							Double data_NextInt = Double.parseDouble(data_Next.replace(",", "").replace(")", "").replace("(", "-").replace(" %", "").replace("$", ""));
							if (data_CurrentInt < data_NextInt) {
								objLgr.info("Value of field " +field+ " has displayed as " +data_Current+ " before " +data_Next+ " in descending order.");
								sortFlag = false;
								break;
							}
							else
								sortFlag = true;
						}
					}
					else
						sortFlag = true;
				}
			}
		}
		else
			sortFlag=true;

		return sortFlag;		
	}

	// Method to validate the search text from a table
	public boolean isSearchText_Present(List<Hashtable<String, String>> data, String search_text) {
		boolean flag= false;
		int count=0;
		for(Hashtable<String,String>row_data: data ) {
			if(row_data.toString().contains(search_text))
				count++;
			else
				objLgr.info("Search text not present in the record - "+row_data);
		}
		if(data.size()==count)
			flag=true;
		else
			flag =false;
		return flag;
	}
	//Method to get Change My View Button
	public WebElement changeMyViewButton() {
		return isElementFound(button_ChangeMyView, "Change My View Button", 90);
	}

	//Method to get Change My View Button
	public WebElement applyButton() {
		return isElementFound(button_Apply, "Apply Button", 90);
	}

	//Method to get Hierarchy Dropdown Button
	public WebElement hierachyButton(int level) {
		WebElement webElement = null;
		By levelbutton=By.xpath("//div[contains(@class,'level"+ level +"')]/ancestor::div[@class='mdc-menu-anchor']//button");
		WebDriverWait wait = new WebDriverWait(driver, 2);
		try {
			wait.until(ExpectedConditions.elementToBeClickable(levelbutton));
			webElement = driver.findElement(levelbutton);
		}
		catch (TimeoutException exp) {
		} catch (NoSuchElementException noexp) {
		}
		return webElement;
	}

	//Method to get Hierarchy List
	public List<WebElement> hierachyList(int level) {
		By levellist=By.xpath("//div[contains(@class,'level"+level+"')]//ul//li");
		return areElementsFound(levellist, "Hierachy list", 90);
	}

	//Method to get line of business displayed in UI
	public WebElement lineOfBusiness(String lob) {
		By levellist=By.xpath("//span[contains(text(),'"+lob+"')]");
		return isElementFound(levellist, "line of Business text", 90);
	}

	//Method to get Hierarchy List
	public List<WebElement> available_Lob_Options() {
		return areElementsFound(lob_Text, "LOB text list", 90);
	}

	//Method to get Hierarchy List
	public List<WebElement> selected_aggregateData_Text() {
		return areElementsFound(aggregateData_Text, "aggregate data list", 90);
	}
	//Method to get Hierarchy List
	public WebElement radioButton_LOB(String lob) {
		WebElement webElement = null;
		By levellist=By.xpath("//label[contains(text(),'"+lob+"')]//parent::div//input");
		//			WebDriverWait wait = new WebDriverWait(driver, 2);
		//			   try {
		//			   wait.until(ExpectedConditions.elementToBeClickable(levellist));
		webElement = driver.findElement(levellist);
		//			   }
		//			   catch (TimeoutException exp) {
		//				   objLgr.info(levellist + " visibility wait timed out"+exp);
		//				} catch (NoSuchElementException noexp) {
		//				}
		return webElement;
	}
	//Method to get Hierarchy List
	public WebElement dataAggregated_SelectedText(String lob) {
		WebElement webElement = null;
		By levellist=By.xpath("//span[contains(text(),'"+lob+"')]");
		//			WebDriverWait wait = new WebDriverWait(driver, 2);
		//			   try {
		//			   wait.until(ExpectedConditions.elementToBeClickable(levellist));
		webElement = driver.findElement(levellist);
		//			   }
		//			   catch (TimeoutException exp) {
		//				   objLgr.info(levellist + " visibility wait timed out"+exp);
		//				} catch (NoSuchElementException noexp) {
		//				}
		return webElement;
	}
	public List<WebElement> get_Childnode(WebElement parentElement) {
		return parentElement.findElements(By.xpath("./td"));
	}
}
