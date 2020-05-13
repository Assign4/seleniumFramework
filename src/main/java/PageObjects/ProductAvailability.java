package PageObjects;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ProductAvailability extends CommonPageMyAccount {
	public ProductAvailability(WebDriver driver) {
		super(driver);
	}
	public JavascriptExecutor js;
	//X-path for 'Product Availability' locator
	By productAvailability=By.xpath("//span[contains(text(),'Product Availability')]");
	//X-path for 'Product Availability-Corn' locator
	By productAvailabilityCorn=By.xpath("//span[contains(text(),'Corn')]");
	//X-path for 'Product Availability-Soybeans' locator
	By productAvailabilitySoybeans=By.xpath("//span[contains(text(),'Soybeans')]");
	//X-path for 'Product Availability-Cotton' locator
	By productAvailabilityCotton=By.xpath("//span[contains(text(),'Cotton')]");
	//X-path for 'Product Availability-Alfalfa' locator
	By productAvailabilityAlfalfa=By.xpath("//span[contains(text(),'Alfalfa')]");
	//X-path for 'Product Availability-Canola' locator
	By productAvailabilityCanola=By.xpath("//span[contains(text(),'Canola')]");
	//X-path for 'Product Availability-Sorghum' locator
	By productAvailabilitySorghum=By.xpath("//span[contains(text(),'Sorghum')]");
	//X-path of 'View Data' button. This is generic and can be used for all widget by passing the widget element
	By link_ViewMore = By.xpath("./ancestor::div[@class='mdc-card']//button[contains(text(),'View Products')]");
	//X-path to get Header of any page
	By txt_TableHeader = By.xpath("//div[contains(@class,'title') and contains(@class,'data-table')]");
	By row_Header = By.xpath("./thead/tr/th");
	By row_Data = By.xpath("./tbody/tr");
	By field_Data = By.xpath("./td");
	By download_Button = By.xpath("//i[contains(text(),'save')]");
	//X-path to get back link from a table
	By link_BackArrowButton = By.xpath("//button[contains(text(),'BACK')]");
	By rowperpage_dropdown =  By.xpath("//div[contains(text(),'Rows per page')]/following-sibling::div//i");
	By rowperpage_dropdown_list = By.xpath("//div[contains(text(),'Rows per page')]/following-sibling::div//ul//li");
	By page_dropdown = By.xpath("//button[contains(text(),'Page')]");
	By page_dropdown_List = By.xpath("//button[contains(text(),'Page')]//parent::div//div//ul//li");
	
	

	//Method to get 'Product Availability' locator
	public WebElement get_ProductAvailability() {
		return isElementFound(productAvailability, "Product Availability locator", 150);
	}
	//Method to get right arrow locator
	public WebElement get_pageDropDown() {
		return isElementFound(page_dropdown, "Right arrow", 90);
	}
	
	//Method to get rows per page drop down locator
		public WebElement get_rowperpage_dropdown() {
			return isElementFound(rowperpage_dropdown, "rows per page drop down locator", 90);
		}
	//Method to get 'Product Availability' locator
	public WebElement get_filterClose(String fieldName) {
		By filter_close_button = By.xpath("//label[contains(text(),'"+fieldName+"')]//parent::div//input/following-sibling::i[contains(text(),'close')]");
		return isElementFound(filter_close_button, fieldName , 90);
	}
	//Method to get download button locator
	public WebElement get_download_Button() {
		return isElementFound(download_Button, "Download Button locator", 90);
	}
	//Method to get 'Product Availability' locator
	public WebElement get_ProductAvailabilityCorn() {
		return isElementFound(productAvailabilityCorn, "Product Availability - Corn locator", 90);
	}
	//Method to get 'Product Availability' locator
	public WebElement get_ProductAvailabilitySoybeans() {
		return isElementFound(productAvailabilitySoybeans, "Product Availability - Soybeans locator", 90);
	}
	//Method to get 'Product Availability' locator
	public WebElement get_ProductAvailabilityCotton() {
		return isElementFound(productAvailabilityCotton, "Product Availability - Cotton locator", 90);
	}
	//Method to get 'Product Availability' locator
	public WebElement get_ProductAvailabilityAlfalfa() {
		return isElementFound(productAvailabilityAlfalfa, "Product Availability - Alfalfa locator", 90);
	}
	//Method to get 'Product Availability' locator
	public WebElement get_ProductAvailabilityCanola() {
		return isElementFound(productAvailabilityCanola, "Product Availability - Canola locator", 90);
	}
	//Method to get 'Product Availability' locator
	public WebElement get_ProductAvailabilitySorghum() {
		return isElementFound(productAvailabilitySorghum, "Product Availability - Sorghum locator", 90);
	}

	//Method to get element of View More link in all widgets
	public WebElement get_ViewMoreLink(WebElement element) {
		return isNestedElementFound(link_ViewMore, "View More Link", 90, element);
	}
	
	//Method to get Corn availability Table
	public WebElement get_CropTable(String crop) {

		//X-Path to get Corn table view data Product Availability-Corn
		By tbl_CropList = By.xpath("//div[contains(text(),'"+crop+"')]/ancestor::header/following-sibling::div//table");
		return isElementFound(tbl_CropList, "View More table", 90);
		
	}
	//Method to get Header of any page
	public List<WebElement> get_TableHeader() {
		return areElementsFound(txt_TableHeader, "Table Header", 90);
	}
	//Method to get page drop down list
	public List<WebElement> get_page_dropdown_List() {
		return areElementsFound(page_dropdown_List, "page dropdown list", 90);
	}
	


		public List<WebElement> get_RowHeader(WebElement element) {
			return areNestedElementsFound(row_Header, "Header Row" , 90, element);
		}
		public List<WebElement> get_RowData(WebElement element) {
			return areNestedElementsFound(row_Data, "Data Row" , 90, element);
		}

		public By get_fieldData() {
			return field_Data;
		}

		public WebElement get_NoDataTag(String crop) {
			By Nodatatag = By.xpath("//div[contains(text(),'"+crop+"')]/ancestor::header/following-sibling::div//table//span[contains(text(),'NO DATA AVAILABLE')]");
			
			return isElementFound(Nodatatag, "No Data Available", 90);
		}
		//Method to get any Header field
		public List<WebElement> get_SortField(String fieldName) {
			By header_SortField = By.xpath("//th[normalize-space(text())='" + fieldName + "']");
			return areElementsFound(header_SortField, fieldName , 90);
		}
		//Method to get rows per page dropdown list
		public List<WebElement> get_rowsperpage_dropdownList() {
			return areElementsFound(rowperpage_dropdown_list, "Rows per page dropdown list" , 90);
		}
		

		//Method to get any Header field
		public WebElement filter_TextBox(String fieldName) {
			By textbox = By.xpath("//label[contains(text(),'"+fieldName+"')]//parent::div//input");
			return isElementFound(textbox, fieldName , 90);
		}
		//Method to get Back link from data tables
		public WebElement getBackArrowoption(){
			return isElementFound(link_BackArrowButton, "Back Button", 90);
		}
		
		
		//Method to get entire table data
		public List<Hashtable<String, String>> tableData_Allpages(String Crop) {  
			js = ((JavascriptExecutor) driver);
			List<WebElement> header = get_RowHeader(get_CropTable(Crop));
			List<Hashtable<String, String>> data_UITable = new ArrayList<Hashtable<String, String>>();
			js.executeScript("arguments[0].scrollIntoView();", get_rowperpage_dropdown());
			get_rowperpage_dropdown().click();
			if(!get_rowsperpage_dropdownList().isEmpty()) {
				get_rowsperpage_dropdownList().get(get_rowsperpage_dropdownList().size()-1).click();
				if (get_pageDropDown()!=null) {
					get_pageDropDown().click();
					List<WebElement> dropdownList_intial = get_page_dropdown_List();  
					get_pageDropDown().click();
					for(int i =0;i<dropdownList_intial.size();i++) {
						get_pageDropDown().click();
						List<WebElement> dropdownList = get_page_dropdown_List();
						dropdownList.get(i).click();
						data_UITable = tableData_perPage(Crop,header);
					}
				}
				else
					data_UITable = tableData_perPage(Crop,header);
			}
			return data_UITable;
		}
		public List<Hashtable<String, String>> tableData_perPage(String Crop, List<WebElement> header) {
		List<WebElement> row_Data = get_RowData(get_CropTable(Crop));
		List<Hashtable<String, String>> data_UITable = new ArrayList<Hashtable<String, String>>();
		for (WebElement row : row_Data) {
			Hashtable<String, String> data = new Hashtable<String, String>();
			List<WebElement> data_UI = areNestedElementsFound(get_fieldData(), "Data", 90, row);
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
}
