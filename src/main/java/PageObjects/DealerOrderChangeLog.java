package PageObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class DealerOrderChangeLog extends CommonPageMyAccount {
	public DealerOrderChangeLog(WebDriver driver) {
		super(driver);
	}
	
	//X-path to fetch rows of a table
	By row_Table=By.xpath("//tr");
	//X-Path to get Product table
	By tbl_ChangeLog = By.xpath("//div[contains(text(),'Change Log')]/ancestor::header/following-sibling::div//table");
	By row_Header = By.xpath("./thead/tr/th");
	By row_Data = By.xpath("./tbody/tr");
	By field_Data = By.xpath("./td");
	//X-path to get search field from the table
	By searchField=By.xpath("//div[contains(@class,'search')]");
	//X-path to pass search field text in the table
	By txt_Search = By.xpath("//input[@placeholder='search']");
	
	//Method to fetch all rows in a table
			public List<WebElement> get_Table() {
				return areElementsFound(row_Table, "Data row", 150);
			}
			//Method to get Product Table
			public WebElement get_ChangeLogTable() {
				return isElementFound(tbl_ChangeLog, "Change Log table", 90);
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
			//Method to get search field from the table
			public WebElement get_SearchField() {
				return isElementFound(searchField, "Search Text Field", 150);
			}
			//Method to pass search field text in the table
			public WebElement get_SearchText() {
				return driver.findElement(txt_Search);
			}
}
