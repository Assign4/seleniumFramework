package PageObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class GposNB extends CommonPageMyAccount{

	public GposNB(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}
	By page_header = By.xpath("//span[contains(text(),'Reconciliation Report')]");
	By product_summary_table = By.xpath("//div[contains(text(),'Summary')]/ancestor::header/following-sibling::div//table");
	By locations_table = By.xpath("//div[contains(text(),'Locations')]/ancestor::header/following-sibling::div//table");
	By farmers_table = By.xpath("//div[contains(text(),'Farmers')]/ancestor::header/following-sibling::div//table");
	By product_details_table = By.xpath("//div[contains(text(),'Details')]/ancestor::header/following-sibling::div//table");
    By table_header = By.xpath("//thead");
	//X-path of NO DATA AVAILABLE message. This is generic and can be used for all tables by passing the table element
	By txt_NoDataTable = By.xpath("./span[contains(text(),'NO DATA AVAILABLE')]");
	//X-path of report Selection locator
	By dropDown_reportSelection = By.xpath("//div[contains(@role,'button') and contains(text(),'Organizational Structure')]");
    //X-Path to get drop-down list
	By dropDownList_reportSelection = By.xpath("//li[contains(@value,'Organizational Structure')]/parent::ul//li");//X-path of report Selection locator
	By dropDown_fiscalYear = By.xpath("//div[contains(@role,'button') and contains(text(),'2019')]");
    //X-Path to get drop-down list
	By dropDownList_fiscalYear = By.xpath("//li[contains(@value,'2019')]/parent::ul//li");
	//X-Path to get header row
	By row_Header = By.xpath("./thead/tr/th");

	//X-Path to get  row data
	By row_Data = By.xpath("./tbody/tr");

	//X-path of each field data 
	By field_Data = By.xpath("./td");
	//X-Path to get Filter Chips in a table
	By link_FilterChips=By.xpath("//div[contains(@class,'mdc-chip-set mdc-chip-set--filter')][1]//div[contains(@tabindex,'0')]");
	//X-Path to get Active Filter Chips in a table
	By link_ActiveFilterChips = By.xpath("//div[contains(@class,'mdc-chip--selected')]");
	//X-Path to text value of a filter chip
	By txt_FilterChip=By.xpath("./child::div[contains(@class,'mdc-chip__text')]");
	//X-path to fetch different tabs of a table
	By header_GPOStab=By.xpath("//span[@class='mdc-tab__content']//span");
	//X-path to get tab button of a table
	By tabButton=By.xpath("./ancestor::button");
	
    
    //Method to get Drop-down link to get Log out button 
	public WebElement get_page_header() {
		return isElementFound(page_header, "Page Header locator", 90);
	}
	//Method to get summary table of Product tab 
	public WebElement get_product_summary_table() {
		return isElementFound(product_summary_table, "Product tab Summary table", 90);
	}
	//Method to get Details table of Product tab 
	public WebElement get_product_details_table() {
		return isElementFound(product_details_table, "Product tab Details table", 90);
	}
	//Method to get Locations tab 
	public WebElement get_Locations_table() {
		return isElementFound(locations_table, "Locations table", 90);
	}
	//Method to get Farmers tab 
	public WebElement get_Farmers_table() {
		return isElementFound(farmers_table, "Locations table", 90);
	}
	//Method to get headers of any table on GPOS page
	public WebElement get_headerTable(WebElement element) {
		return isNestedElementFound(table_header, "Headers", 90, element);
	}
	//Method to get report Selection locator
	public WebElement get_dropDown_reportSelection() {
		return isElementFound(dropDown_reportSelection, "report selection dropdown button", 90);
	}
	//Method to get report Selection list locator
	public List<WebElement> get_dropDownlist_reportSelection() {
		return areElementsFound(dropDownList_reportSelection, "report selection dropdown list", 90);
	}
	//Method to get Fiscal Year locator
	public WebElement get_dropDown_fiscalYear() {
		return isElementFound(dropDown_fiscalYear, "Fiscal Year dropdown button", 90);
	}
	//Method to get report Selection list locator
	public List<WebElement> get_dropDownList_fiscalYear() {
		return areElementsFound(dropDownList_fiscalYear, "Fiscal Year dropdown list", 90);
	}

	//Method to get header row
	public List<WebElement> get_RowHeader(WebElement element) {
		return areNestedElementsFound(row_Header, "Header Row" , 90, element);
	}

	//Method to get any Header field
	public List<WebElement> get_SortField(String fieldName) {
		By header_SortField = By.xpath("//th[normalize-space(text())='" + fieldName + "']");
		return areElementsFound(header_SortField, fieldName , 90);
	}

	//Method to get row data
	public List<WebElement> get_RowData(WebElement element) {
		return areNestedElementsFound(row_Data, "Data Row" , 90, element);
	}

	//Method to return field data 
	public By get_fieldData() {
		return field_Data;
	}
	//Method to get NO DATA AVAILABLE message in all tables
	public WebElement get_NoDataTable(WebElement element) {
		return isNestedElementFound(txt_NoDataTable, "No data in table", 90, element);
	}
	//Method to fetch all active chips in a table
	public List<WebElement> get_ActivateFilterChips() {
		return areElementsFound(link_ActiveFilterChips, "Active Filter Chips", 90);
	}
	//Method to fetch all filter chips in a table
	public List<WebElement> get_FilterChips() {
		return areElementsFound(link_FilterChips, "Filter Chips", 90);
	}
	//Method to select/de-select filter chips
	public void update_FilterChips(List<WebElement> filterChips) {
		for(WebElement filterChip : filterChips)			 
			filterChip.click();
	}
	//Method to fetch text of a Filter Chip
	public By get_Chiptext() {
		return txt_FilterChip;
	}
	//Method to get tabs displayed in data tables
	public List<WebElement> get_header_GPOStab() {
		return areElementsFound(header_GPOStab, "Header text", 90);
	}
	//Method to get tabs button in data tables
	public WebElement get_TabButton(WebElement element) {
		return isNestedElementFound(tabButton, "Tab button", 90, element);
	}
	
	
	
	
}
