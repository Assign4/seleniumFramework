package PageObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ProductCodeLookUp extends CommonPageMyAccount {
	public ProductCodeLookUp(WebDriver driver) {
		super(driver);
	}
		// TODO Auto-generated constructor stub
		//X-path to fetch rows of a table
		By row_Table=By.xpath("//tr");
		//X-path of Crop drop-down. 
		By dropdown_Crop = By.xpath("(//div[contains(@class,'container dropdown')]//button)[1]");
		//X-path of Crop drop-down. 
		By dropdown_Trait = By.xpath("(//div[contains(@class,'container dropdown')]//button)[2]");
		//X-path of Crop drop-down. 
		By dropdown_Year = By.xpath("(//div[contains(@class,'container dropdown')]//button)[3]");
		//X-path of list of Crop filter drop-down. 
		By dropDown_List = By.xpath("./following-sibling::div//ul[@class='mdc-list']/li");
		//X-Path to get Product table
		By tbl_ProductList = By.xpath("//div[contains(text(),'Product')]/ancestor::header/following-sibling::div//table");
		By row_Header = By.xpath("./thead/tr/th");
		By row_Data = By.xpath("./tbody/tr");
		By field_Data = By.xpath("./td");
		//X-path of NO DATA AVAILABLE message. 
		By txt_NoDataTable = By.xpath("./ancestor::div[contains(@class,'mdc-data-table')]//span[contains(text(),'NO DATA AVAILABLE')]");
		
		
		
		
		

		//Method to fetch all rows in a table
		public List<WebElement> get_Table() {
			return areElementsFound(row_Table, "Data row", 150);
		}
		//Method to get element of Crop drop-down
		public WebElement get_CropDropdown() {
			return isElementFound(dropdown_Crop, "Crop drop-down", 90);
		}
		//Method to get element of Crop drop-down
		public WebElement get_TraitDropdown() {
			return isElementFound(dropdown_Trait, "Trait drop-down", 90);
		}
		//Method to get element of Crop drop-down
		public WebElement get_YearDropdown() {
			return isElementFound(dropdown_Year, "Year drop-down", 90);
		}
		//Method to select the Crop drop down list
		public List<WebElement> get_DropdownList(WebElement dropdown){
			return areNestedElementsFound(dropDown_List, "Crop Drop-down list", 90, dropdown);
		}
		//Method to get Product Table
		public WebElement get_ProductTable() {
			return isElementFound(tbl_ProductList, "Product table", 90);
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
		//Method to get NO DATA AVAILABLE message in all tables
		public WebElement get_NoDataTable(WebElement element) {
			return isNestedElementFound(txt_NoDataTable, "No data in table", 90, element);
		}
		//Method to get any Header field
		public List<WebElement> get_SortField(String fieldName) {
			By header_SortField = By.xpath("//th[normalize-space(text())='" + fieldName + "']");
			return areElementsFound(header_SortField, fieldName , 90);
		}
	
}