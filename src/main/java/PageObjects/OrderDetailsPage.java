package PageObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class OrderDetailsPage extends CommonPageMyAccount {

	public OrderDetailsPage(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}
	//X-path of NO DATA AVAILABLE message. This is generic and can be used for all tables by passing the table element
	By txt_NoDataTable = By.xpath("./ancestor::div[@class='mdc-data-table']//span[contains(text(),'No data available')]");
	//X-path to fetch rows of a table
	By row_Table=By.xpath("//tr");
	//X-path to fetch different tabs of a table
	By header_OrderDetailstab=By.xpath("//span[@class='mdc-tab__content']//span");
	//X-path to get tab button of a table
	By tabButton=By.xpath("./ancestor::button");
	//X-path to get back link from a table
	By link_BackArrowButton = By.xpath("//button[contains(text(),'BACK')]");
	//X-path to get Header of any page
	By txt_TableHeader = By.xpath("//div[contains(@class,'title') and contains(@class,'data-table')]");
	//X-Path to get Filter Chips in a table
	By link_FilterChips=By.xpath("//div[contains(@class,'mdc-chip-set mdc-chip-set--filter')][1]//div[contains(@tabindex,'0')]");
	//X-Path to get Active Filter Chips in a table
	By link_ActiveFilterChips = By.xpath("//div[contains(@class,'mdc-chip--selected')]");
	//X-Path to text value of a filter chip
	By txt_FilterChip=By.xpath("./child::div[contains(@class,'mdc-chip__text')]");
	//X-path to get search field from the table
	By searchField=By.xpath("//div[contains(@class,'search')]");
	//X-path to pass search field text in the table
	By txt_Search = By.xpath("//input[@placeholder='Search']");
	//X-path to get total records displayed in footer section of a table
	By txt_TotalRecords=By.xpath("//div[contains(@class,'mdc-pagination-row-per-page')]");
	//X-path to get download link
	By link_Download = By.xpath("//section[contains(@class,'top-bar')]//button");
	//X-Path to get Summary tab in Order Details table
	By tbl_SummaryShpStatTab = By.xpath("//div[contains(text(),'Locations')]/ancestor::header/following-sibling::div//table");
	By tbl_OrderDetailsTab = By.xpath("//div[contains(text(),'Order Details')]/ancestor::header/following-sibling::div//table");
	//X-Path to get Trait tab in Order Details table
	By tbl_TraitShpStatTab = By.xpath("//div[contains(text(),'Trait')]/ancestor::header/following-sibling::div//table");
	//X-Path to get Product tab in Order Details table
	By tbl_ProductShpStatTab = By.xpath("//div[contains(text(),'Product')]/ancestor::header/following-sibling::div//table");
	//X-Path to get Product Description tab in Order Details table
	By tbl_ProdDescShpStatTab = By.xpath("//div[contains(text(),'Product Description')]/ancestor::header/following-sibling::div//table");
	//X-Path to get Farmer tab in Order Details table
	By tbl_FarmerTab = By.xpath("//div[contains(text(),'Farmer')]/ancestor::header/following-sibling::div//table");
	By row_Header = By.xpath("./thead/tr/th");
	By row_Data = By.xpath("./tbody/tr");
	By field_Data = By.xpath("./td");
	By btn_RowsPerPage = By.xpath("//div[@class='mdc-pagination-row-per-page-text-number']//button");
	By list_RowsPerPage = By.xpath("//div[@class='mdc-pagination-row-per-page-text-number']/div/ul/li");
	By tbl_ProductDescription = By.xpath("//div[contains(text(),'Product Description')]/ancestor::header/following-sibling::div//table");
	By btn_NextPage = By.xpath("//i[contains(text(),'right')]");
	By txt_PageNo = By.xpath("//div[@class='mdc-pagination-page-of-container']");
	By hdr_BioAgOrderDetails = By.xpath("//span[contains(text(),'Order Details')]");
	By tbl_BioAgOrdrDtls = By.xpath("//table");
	//X-Path to get table displaying No Data available
	By tbl_NoDataAvlbl = By.xpath("//div[contains(text(),'Farmer')]/ancestor::header/following-sibling::div//table//span[contains(text(),'NO DATA AVAILABLE')]");

	//Method to get NO DATA AVAILABLE message in all widgets
	public WebElement get_NoDataTable(WebElement element) {
		return isNestedElementFound(txt_NoDataTable, "No data in table", 90, element);
	}
	//Method to fetch all rows in a table
	public List<WebElement> get_Table() {
		return areElementsFound(row_Table, "Data row", 90);
	}
	//Method to get tabs displayed in data tables
	public List<WebElement> get_header_OrderDetailstab() {
		return areElementsFound(header_OrderDetailstab, "Header text", 90);
	}
	//Method to get tabs button in data tables
	public WebElement get_TabButton(WebElement element) {
		return isNestedElementFound(tabButton, "Tab button", 90, element);
	}
	//Method to get Back link from data tables
	public WebElement getBackArrowoption(){
		return isElementFound(link_BackArrowButton, "Back Button", 90);
	}
	//Method to get Header of any page
	public List<WebElement> get_TableHeader() {
		return areElementsFound(txt_TableHeader, "Table Header", 90);
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
	//Method to get any Header field
	public List<WebElement> get_SortField(String fieldName) {
		By header_SortField = By.xpath("//th[normalize-space(text())='" + fieldName + "']");
		return areElementsFound(header_SortField, fieldName , 90);
	}
	//Method to get the record count displayed in footer section of a table 
	public List<WebElement> get_Footertext() {
		return areElementsFound(txt_TotalRecords, "Footer text" , 90);
	}
	//Method to get download link
	public WebElement get_lnk_Download() {
		return isElementFound(link_Download, "Download link", 90);
	}
	//Method to get Summary tab in Order Details table (Shipping Status - View Data)
	public WebElement get_SummaryShpStatTable() {
		return isElementFound(tbl_SummaryShpStatTab, "Summary", 90);
	}
	//Method to get Order Details table
	public WebElement get_OrderDetailsTable() {
		return isElementFound(tbl_OrderDetailsTab, "Order Details", 90);
	}
	//Method to get Trait tab in Order Details table (Shipping Status - View Data)
	public WebElement get_TraitShpStatTable() {
		return isElementFound(tbl_TraitShpStatTab, "Trait", 90);
	}
	//Method to get Product tab in Order Details table (Shipping Status - View Data)
	public WebElement get_ProductShpStatTable() {
		return isElementFound(tbl_ProductShpStatTab, "Product", 90);
	}
	//Method to get Product tab in Order Details table (Shipping Status - View Data)
	public WebElement get_FarmerTable() {
		return isElementFound(tbl_ProductShpStatTab, "Product", 90);
	}
	//Method to get Product Description tab in Order Details table (Shipping Status - View Data)
	public WebElement get_ProdDescShpStatTable() {
		return isElementFound(tbl_ProdDescShpStatTab, "Product Description", 90);
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
	public WebElement get_RowsPerPageButton() {
		return isElementFound(btn_RowsPerPage, "Rows Per Page drop-down", 90);
	}

	public List<WebElement> get_RowsPerPageList() {
		return areElementsFound(list_RowsPerPage, "Rows Per Page List" , 90);
	}
	public WebElement get_ProdDescTable() {
		return isElementFound(tbl_ProductDescription, "Product Description table", 90);
	}
	public WebElement get_NextPageButton() {
		return isElementFound(btn_NextPage, "Next Page button", 90);
	}
	public WebElement get_PageNo() {
		return isElementFound(txt_PageNo, "Page No.", 90);
	}
	public List<WebElement> get_header_OrderDetails() {
		return areElementsFound(hdr_BioAgOrderDetails, "Header text", 90);
	}

	public WebElement get_OrdrDtlsTbl() {
		return isElementFound(tbl_BioAgOrdrDtls, "Order Details", 90);
	}

	public WebElement get_SearchText() {
		return driver.findElement(txt_Search);
	}

	public WebElement get_SearchField() {
		return isElementFound(searchField, "Search Text Field", 150);
	}
	
	//Method to get Farmer tab in Order Details table 
		public WebElement get_FarmerShpStatTable() {
			return isElementFound(tbl_FarmerTab, "Farmer tab", 90);
		}
	//Method to get when table displays No Data Available	
		public WebElement get_NoDataAvailableTable() {
			return isElementFound(tbl_NoDataAvlbl, "Table-No Data Available", 90);
		}
}