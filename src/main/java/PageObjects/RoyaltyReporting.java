package PageObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class RoyaltyReporting extends CommonPageMyAccount{

	public RoyaltyReporting(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}
	//X-path of Traits Royalty widget header
	By traitsRoyalty_Header = By.xpath("//span[contains(text(),'Traits Royalty')]");
	//X-path of Genetic Royalty widget header
	By geneticRoyalty_Header = By.xpath("//span[contains(text(),'Genetics Royalty')]");
	//X-path of Traits Royalty widget header
	By cottonRoyalty_Header = By.xpath("//span[contains(text(),'Cotton Royalty')]");
	//X-path of Traits Royalty widget body 
	By traitsRoyalty_body = By.xpath("(//div[@class='mdc-card'])[1]//div[contains(@class,'royalty-reporting-card-body')]");
	//X-path of Genetic Royalty widget body 
	By geneticRoyalty_body = By.xpath("(//div[@class='mdc-card'])[2]//div[contains(@class,'royalty-reporting-card-body')]");
	//X-path of Cotton Royalty widget body 
	By cottonRoyalty_body = By.xpath("(//div[@class='mdc-card'])[3]//div[contains(@class,'royalty-reporting-card-body')]");
	//X-path of 'View Data' button. This is generic and can be used for all widget by passing the widget element
	By link_ViewMore = By.xpath("./ancestor::div[@class='mdc-card']//button[contains(text(),'View Data')]");
	//X-path to fetch rows of a table
	By row_Table=By.xpath("//tr");
	//X-path of NO DATA AVAILABLE message. This is generic and can be used for all tables by passing the table element
	By txt_NoDataTable = By.xpath("./ancestor::div[@class='mdc-data-table']//span[contains(text(),'No data available')]");
	//X-path to get Corn Trait table
	By tbl_CornTrait = By.xpath("(//div[contains(text(),'Corn Traits')]/ancestor::header/following-sibling::div//table)[1]");
	//X-path to get Corn Trait By Zone table
	By tbl_CornTrait_ByZone = By.xpath("(//div[contains(text(),'Corn Traits')]/ancestor::header/following-sibling::div//table)[2]");
	By row_Header = By.xpath("./thead/tr/th");
	By row_Data = By.xpath("./tbody/tr");
	By field_Data = By.xpath("./td");
	By dropdown_trait = By.xpath("//div[contains(@class,'container dropdown')]//button");
	By dropDown_List = By.xpath("./following-sibling::div//ul[@class='mdc-list']/li");
	//X-path to fetch different tabs of a table
	By tab_header=By.xpath("//span[@class='mdc-tab__content']//span");
	//X-path to get tab button of a table
	By tabButton=By.xpath("./ancestor::button");
	//X-path to get Header of any page
	By txt_TableHeader = By.xpath("//div[contains(@class,'title') and contains(@class,'data-table')]");
	//X-Path to get Soybean Trait tab
	By tbl_SoybeanTraitTab = By.xpath("//div[contains(text(),'Soybean Traits')]/ancestor::header/following-sibling::div//table");
	//X-Path to get Canola Trait tab
	By tbl_CanolaTraitTab = By.xpath("//div[contains(text(),'Canola Traits')]/ancestor::header/following-sibling::div//table");
	//X-Path to get Corn Treatment Trait tab
	By tbl_CornTreatmentTraitTab = By.xpath("//div[contains(text(),'Corn Treatment')]/ancestor::header/following-sibling::div//table");
	//X-path to get search field from the table
	By searchField=By.xpath("//div[contains(@class,'search')]");
	//X-path to pass search field text in the table
	By txt_Search = By.xpath("//input[@placeholder='Search']");
	//X-path to get back link from a table
	By link_BackArrowButton = By.xpath("//button[contains(text(),'BACK')]");
	//X-Path to get Corn Genetic tab
	By tbl_CornGeneticsTab = By.xpath("//div[contains(text(),'Corn Genetics')]/ancestor::header/following-sibling::div//table");
	//X-Path to get Soybean Genetic tab
	By tbl_SoybeansGeneticsTab = By.xpath("//div[contains(text(),'Soybeans Genetics')]/ancestor::header/following-sibling::div//table");
	//X-Path to get Canola Genetic tab
	By tbl_CanolaGeneticsTab = By.xpath("//div[contains(text(),'Canola Genetics')]/ancestor::header/following-sibling::div//table");
	//X-Path to get Cotton Royalty tab
	By tbl_CottonTab = By.xpath("//div[contains(text(),'Cotton Royalty')]/ancestor::header/following-sibling::div//table");

	//Method to get Traits Royalty widget header
	public WebElement get_traitsRoyalty_Header() {
		return isElementFound(traitsRoyalty_Header, "Trait royalty Header", 150);
	} 

	//Method to get Genetic Royalty widget header
	public WebElement get_geneticRoyalty_Header() {
		return isElementFound(geneticRoyalty_Header, "Genetic royalty Header", 90);
	} 

	//Method to get Cotton Royalty widget header
	public WebElement get_cottonRoyalty_Header() {
		return isElementFound(cottonRoyalty_Header, "Cotton royalty Header", 90);
	} 
	//Method to get Traits Royalty widget body
	public WebElement get_traitsRoyalty_body() {
		return isElementFound(traitsRoyalty_body, "Traits Royalty widget body", 90);
	} 
	//Method to get Genetic Royalty widget body
	public WebElement get_geneticRoyalty_body() {
		return isElementFound(traitsRoyalty_body, "Genetic Royalty widget body", 90);
	} 
	//Method to get Cotton Royalty widget body
	public WebElement get_cottonRoyalty_body() {
		return isElementFound(traitsRoyalty_body, "Cotton Royalty widget body", 90);
	}

	//Method to get element of View More link in all widgets
	public WebElement get_ViewMoreLink(WebElement element) {
		return isNestedElementFound(link_ViewMore, "View More Link", 90, element);
	}
	//Method to fetch all rows in a table
	public List<WebElement> get_Table() {
		return areElementsFound(row_Table, "Data row", 90);
	}
	//Method to get NO DATA AVAILABLE message in all tables
	public WebElement get_NoDataTable(WebElement element) {
		return isNestedElementFound(txt_NoDataTable, "No data in table", 90, element);
	}
	//Method to get Corn Trait table (Trait Royalty - View Data)
	public WebElement get_CornTraitTable() {
		return isElementFound(tbl_CornTrait, "Corn Trait table", 90);
	}
	//Method to get Corn Trait By Zone table (Trait Royalty - View Data)
	public WebElement get_CornTrait_ByZone_table() {
		return isElementFound(tbl_CornTrait_ByZone, "Corn Trait table", 90);
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
	//Method to get any Header field
	public List<WebElement> get_SortField(String fieldName) {
		By header_SortField = By.xpath("//th[normalize-space(text())='" + fieldName + "']");
		return areElementsFound(header_SortField, fieldName , 90);
	}
	//Method to get element of Crop drop-down
	public WebElement get_TraitDropdown() {
		return isElementFound(dropdown_trait, "Trait drop-down", 90);
	}
	//Method to select the drop down list
	public List<WebElement> get_DropdownList(WebElement dropdown){
		return areNestedElementsFound(dropDown_List, "Crop Drop-down list", 90, dropdown);
	}
	//Method to get tabs displayed in data tables
	public List<WebElement> get_tab_Header() {
		return areElementsFound(tab_header, "Header text", 90);
	}
	//Method to get tabs button in data tables
	public WebElement get_TabButton(WebElement element) {
		return isNestedElementFound(tabButton, "Tab button", 90, element);
	}
	//Method to get Header of any page
	public WebElement get_TableHeader() {
		return isElementFound(txt_TableHeader, "Table Header", 90);
	}
	//Method to get Soybean Trait tab
	public WebElement get_SoybeanTraittable() {
		return isElementFound(tbl_SoybeanTraitTab, "Soybean Trait", 90);
	}
	//Method to get Corn Genetics tab
	public WebElement get_CornGeneticstable() {
		return isElementFound(tbl_CornGeneticsTab, "Corn Genetics", 90);
	}
	//Method to get Soybeans Genetics tab
	public WebElement get_SoybeansGeneticstable() {
		return isElementFound(tbl_SoybeansGeneticsTab, "Soybeans Genetics", 90);
	}
	//Method to get Canola Genetics tab
	public WebElement get_CanolaGeneticstable() {
		return isElementFound(tbl_CanolaGeneticsTab, "Canola Genetics", 90);
	}
	//Method to get Canola Trait tab
	public WebElement get_CanolaTraitTable() {
		return isElementFound(tbl_CanolaTraitTab, "Canola Trait", 90);
	}
	//Method to get Cotton Royalty tab
	public WebElement get_CottonRoyaltyTable() {
		return isElementFound(tbl_CottonTab, "Cotton Royalty", 90);
	}
	//Method to get Corn Treatment Trait tab
	public WebElement get_CornTreatmentTraitTable() {
		return isElementFound(tbl_CornTreatmentTraitTab, "Corn Treatment Trait", 90);
	}
	//Method to get search field from the table
	public WebElement get_SearchField() {
		return isElementFound(searchField, "Search Text Field", 150);
	}
	//Method to pass search field text in the table
	public WebElement get_SearchText() {
		return driver.findElement(txt_Search);
	}
	//Method to get Back link from data tables
	public WebElement getBackArrowoption(){
		return isElementFound(link_BackArrowButton, "Back Button", 90);
	}
}
