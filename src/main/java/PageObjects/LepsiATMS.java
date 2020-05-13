package PageObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LepsiATMS extends CommonPageMyAccount{

	public LepsiATMS(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}
	//X-Path to get Available Space widget header
	By widget_AvailableSpace = By.xpath("//div[contains(text(),'Available Space')]");

	//X-Path to get Product Summary widget header
	By widget_ProductSummary = By.xpath("//div[contains(text(),'Product Summary')]");

	//X-Path to get Tank Status widget header
	By widget_TankStatus = By.xpath("//div[contains(text(),'Tank Status')]");

	//X-path to get Header of ATMS page
	By txt_PageHeader = By.xpath("//span[contains(text(),'ATMS')]");

	//X-path of List on widget
	By widget_list = By.xpath("./ancestor::div[@class='mdc-card']//ul");

	//X-path of NO DATA AVAILABLE message. This is generic and can be used for all widgets by passing the widget element
	By txt_NoDataWidget = By.xpath("./ancestor::div[@class='mdc-card']//span[contains(text(),'No data available')]");

	//X-path of Try Again. This is generic and can be used for all widget by passing the widget element
	By txt_TryAgain= By.xpath("./ancestor::div[@class='mdc-card']//span[contains(text(),'Service unavailable')]");

	//X-path of 'View Data' button. This is generic and can be used for all widget by passing the widget element
	By link_ViewMore = By.xpath("./ancestor::div[@class='mdc-card']//button[contains(text(),'View Data')]");

	//X-path to get Header of any page
	By txt_TableHeader = By.xpath("//div[contains(@class,'title') and contains(@class,'data-table')]");

	//X-path to fetch Detail tab of ATMS-Details table
	By header_ATMSDetailstab=By.xpath("//span[@class='mdc-tab__content']//span");

	//X-path to fetch rows of a table
	By row_Table=By.xpath("//tr");

	//X-path of NO DATA AVAILABLE message. This is generic and can be used for all tables by passing the table element
	By txt_NoDataTable = By.xpath("./ancestor::div[@class='mdc-data-table']//span[contains(text(),'No data available')]");

	//X-Path to get Summary tab in ATMS Details table
	By tbl_SummaryTab = By.xpath("//div[contains(text(),'Summary')]/ancestor::header/following-sibling::div//table");

	//X-Path to get Details tab in ATMS Details table
	By tbl_DetailTab = By.xpath("//div[contains(text(),'Detail')]/ancestor::header/following-sibling::div//table");

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

	//X-path of Locations drop-down. 
	By dropdown_Location = By.xpath("(//div[contains(@class,'container dropdown')]//button)[1]");

	//X-path of Product drop-down. 
	By dropdown_Product = By.xpath("(//div[contains(@class,'container dropdown')]//button)[2]");

	//X-path of Tank Status drop-down. 
	By dropdown_TankStatus = By.xpath("(//div[contains(@class,'container dropdown')]//button)[3]");

	//X-path of list of Crop filter drop-down. 
	By dropDown_List = By.xpath("./following-sibling::div//ul[@class='mdc-list']/li");

	//X-path to get tab button of a table
	By tabButton=By.xpath("./ancestor::button");

	//X-path to get back link from a table
	By link_BackArrowButton = By.xpath("//button[contains(text(),'BACK')]");

	//X-Path to get Summary tab in ATMS Details table
	By tbl_TankReadings = By.xpath("//div[contains(text(),'Tank Readings')]/ancestor::header/following-sibling::div//table");

	//X-path to get download link
	By link_Download = By.xpath("//section[contains(@class,'top-bar')]//button");


	//Method to get element of Available Space widget
	public WebElement get_AvailableSpace() {
		return isElementFound(widget_AvailableSpace, "Available Space widget", 90);
	}

	//Method to get element of Product Summary widget
	public WebElement get_ProductSummary() {
		return isElementFound(widget_ProductSummary, "Product Summary widget", 90);
	}

	//Method to get element of Tank Status widget
	public WebElement get_TankStatus() {
		return isElementFound(widget_TankStatus, "Tank Status widget", 90);
	}

	//Method to get Header of ATMS page
	public List<WebElement> get_PageHeader() {
		return areElementsFound(txt_PageHeader, "Page Header", 90);
	}

	//Method to get element of Crop drop-down in all widgets
	public WebElement get_widgetList(WebElement element) {
		return isNestedElementFound(widget_list, "Widget List", 90, element);
	}
	//Method to get NO DATA AVAILABLE message in all widgets
	public WebElement get_NoDataWidget(WebElement element) {
		return isNestedElementFound(txt_NoDataWidget, "No data in widget", 90, element);
	}

	//Method to get Try Again message in all widgets
	public WebElement get_TryAgain(WebElement element) {
		return isNestedElementFound(txt_TryAgain, "Try Again error", 90, element);
	}

	//Method to get element of View More link in all widgets
	public WebElement get_ViewMoreLink(WebElement element) {
		return isNestedElementFound(link_ViewMore, "View More Link", 90, element);
	}

	//Method to get Header of any page
	public List<WebElement> get_TableHeader() {
		return areElementsFound(txt_TableHeader, "Table Header", 90);
	}

	//Method to get Detail tab displayed in ATMS-Details tables
	public List<WebElement> get_header_ATMSDetailstab() {
		return areElementsFound(header_ATMSDetailstab, "Header text", 90);
	}


	//Method to fetch all rows in a table
	public List<WebElement> get_Table() {
		return areElementsFound(row_Table, "Data row", 90);
	}

	//Method to get NO DATA AVAILABLE message in all widgets
	public WebElement get_NoDataTable(WebElement element) {
		return isNestedElementFound(txt_NoDataTable, "No data in table", 90, element);
	}

	//Method to get Summary tab in ATMS Details table 
	public WebElement get_SummaryTab() {
		return isElementFound(tbl_SummaryTab, "Summary", 90);
	}

	//Method to get Detail tab in ATMS Details table 
	public WebElement get_DetailTab() {
		return isElementFound(tbl_DetailTab, "Detail", 90);
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

	//Method to get element of Locations drop-down
	public WebElement get_LocationDropdown() {
		return isElementFound(dropdown_Location, "Locations drop-down", 90);
	}

	//Method to get element of Crop drop-down
	public WebElement get_ProductDropdown() {
		return isElementFound(dropdown_Product, "Products drop-down", 90);
	}

	//Method to get element of Crop drop-down
	public WebElement get_TankStatusDropdown() {
		return isElementFound(dropdown_TankStatus, "Tank Status drop-down", 90);
	}

	//Method to select the Crop drop down list
	public List<WebElement> get_DropdownList(WebElement dropdown){
		return areNestedElementsFound(dropDown_List, "Crop Drop-down list", 90, dropdown);
	}

	//Method to get tabs button in data tables
	public WebElement get_TabButton(WebElement element) {
		return isNestedElementFound(tabButton, "Tab button", 90, element);
	}

	//Method to get Back link from data tables
	public WebElement getBackArrowoption(){
		return isElementFound(link_BackArrowButton, "Back Button", 90);
	}

	//Method to get a random dealer location in ATMS-Detail tab
	public WebElement get_lctnlnk_ATMSDetailstab(int i) {
		By link_location = By.xpath("(//td[contains(@class, 'hyperlink')])"+"["+i+"]");
		return isElementFound(link_location, "Location position" , 90);
	}

	//Method to get selected dealer location name in ATMS-Detail tab
	public WebElement get_lctnname_ATMSDetailstab(int i) {
		By location_name = By.xpath("(//td[contains(@class, 'hyperlink')])"+"["+i+"]/a");
		return isElementFound(location_name, "Location name" , 90);
	}

	//Method to get Tank Readings table in ATMS 
	public WebElement get_tankReadingsTbl() {
		return isElementFound(tbl_TankReadings, "Tank Readings", 90);
	}

	//Method to get download link
		public WebElement get_lnk_Download() {
			return isElementFound(link_Download, "Download link", 90);
		}
	}
