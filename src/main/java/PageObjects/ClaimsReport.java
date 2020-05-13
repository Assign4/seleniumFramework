package PageObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ClaimsReport extends CommonPageMyAccount {
	public ClaimsReport(WebDriver driver) {
		super(driver);
	}
	// TODO Auto-generated constructor stub

	//X-path of Claims Report Title
	By claimsReport_Title = By.xpath("//span[contains(text(),'Claims Report')]");

	//X-path to fetch Claims table header
	By table_header=By.xpath("//div[contains(@class,'title') and contains(@class,'data-table')]");

	//X-path to fetch rows of a table
	By row_Table=By.xpath("//tr");

	//X-path of NO DATA AVAILABLE message. This is generic and can be used for all tables by passing the table element
	By txt_NoDataTable = By.xpath("./ancestor::div[@class='mdc-data-table']//span[contains(text(),'No data available')]");

	//X-Path to get Claims table 
	By tbl_Claims = By.xpath("//div[contains(text(),'Claims')]/ancestor::header/following-sibling::div//table");

	//X-path of Year drop-down. 
	By dropdown_year = By.xpath("(//div[contains(@class,'container dropdown')]//button)[1]");

	//X-path of list of Trait filter drop-down. 
	By dropDown_List = By.xpath("./following-sibling::div//ul[@class='mdc-list']/li");

	//X-Path to get header row
	By row_Header = By.xpath("./thead/tr/th");

	//X-Path to get  row data
	By row_Data = By.xpath("./tbody/tr");

	//X-path of each field data 
	By field_Data = By.xpath("./td");

	//X-path to get search field from the table
	By searchField=By.xpath("//div[contains(@class,'search')]");

	//X-path to pass search field text in the table
	By txt_Search = By.xpath("//input[@placeholder='Search']");

	//Method to get Claims Report title
	public List<WebElement> get_claimsReportTitle() {
		return areElementsFound(claimsReport_Title, "Claims Report Title", 90);
	}

	//Method to get Claims table header 
	public List<WebElement> get_table_Header() {
		return areElementsFound(table_header, "Header text", 90);
	}		

	//Method to fetch all rows in a table
	public List<WebElement> get_Table() {
		return areElementsFound(row_Table, "Data row", 90);
	}

	//Method to get NO DATA AVAILABLE message in all widgets
	public WebElement get_NoDataTable(WebElement element) {
		return isNestedElementFound(txt_NoDataTable, "No data in table", 90, element);
	}

	//Method to get Claims table 
	public WebElement get_claimsTable() {
		return isElementFound(tbl_Claims, "Claims table", 90);
	}
	//Method to get element of Year drop-down
	public WebElement get_yearDropdown() {
		return isElementFound(dropdown_year, "Year drop-down", 90);
	}

	//Method to select the Year drop down list
	public List<WebElement> get_DropdownList(WebElement dropdown){
		return areNestedElementsFound(dropDown_List, "Year Drop-down list", 90, dropdown);
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

	//Method to get Search field
	public WebElement get_SearchField() {
		return isElementFound(searchField, "Search Text Field", 150);
	}

	//Method to pass search field text in the table
	public WebElement get_SearchText() {
		return driver.findElement(txt_Search);
	}
}