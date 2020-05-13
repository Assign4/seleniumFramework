package PageObjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LeftNavigation_Accountdropdown extends CommonPageMyAccount {

	public LeftNavigation_Accountdropdown(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}
	By lnk_DashBoard = By.xpath("//div[contains(text(),'Dashboard') and not(contains(text(),'Climate'))]");
	By lnk_Farmers = By.xpath("//div[contains(text(),'Farmers')]");
	By lnk_BusinessPlan = By.xpath("//div[contains(text(),'Business Plan') and not(contains(text(),'Progress')) and not(contains(text(),'My'))]");
	By lnk_FieldCheckupSeries = By.xpath("//div[contains(text(),'Field Check Up Series')]");
	By lnk_Orders = By.xpath("//div[contains(text(),'Orders')]");
	By lnk_OrderDetails=By.xpath("//div[contains(text(),'Order Details')]");
	By lnk_Opportunities=By.xpath("//div[contains(text(),'Opportunities') and not(contains(text(),'Farmer'))]");
	By lnk_Inventory=By.xpath("//div[contains(text(),'Inventory') and not(contains(text(),'Details'))and not(contains(text(),'Warehouse'))and not(contains(text(),'Certification'))]");
	By lnk_InventoryDetails=By.xpath("//div[contains(text(),'Inventory Details')]");//div[contains(text(),'Reporting')]
	By lnk_Reporting_dropdown=By.xpath("//div[contains(text(),'Reporting')]/parent::span/following-sibling::i");
	By lnk_GPOS=By.xpath("//div[contains(text(),'GPOS')]");
	By lnk_LogisticDetails=By.xpath("//div[contains(text(),'Logistic Details')]");
	By lnk_MyCompany=By.xpath("//div[contains(text(),'My Company')]");
	//X-path of Log Out drop-down.
	By dropDown_LogOut= By.xpath("//div[contains(@class,'mdc-drawer__header-content')]//i");
	//X-path to get Log Out link
	By link_LogOut = By.xpath("//button[contains(text(),'Logout')]");
	//X-path for ham burger menu
	By ham_icon = By.xpath("//i[contains(text(),'menu')]");
	By lnk_logisticsDetails=By.xpath("//div[contains(text(),'Logistic Details')]");
	//X-path for ATMS Link
	By lnk_ATMS = By.xpath("//div[contains(text(),'ATMS') and not(contains(text(),'Details'))]");
	//Method to get ham icon link
	public WebElement get_HamBurgerlink() {
		return isElementFound(ham_icon, "Ham burger icon", 150);
	}
	//Method to get Drop-down link to get Log out button 
	public WebElement get_DropdownLogOut() {
		return isElementFound(dropDown_LogOut, "Log Out Drop-down", 90);
	}
	//Method to get Log out button
	public List<WebElement> get_LogOut() {
		return areElementsFound(link_LogOut, "Log Out Link", 90);
	}
	//Method to get 'Dashboard' link from left navigation panel
	public WebElement get_linkDashBoard() {
		return isElementFound(lnk_DashBoard, "Dashboard Link", 150);
	}
	//Method to get 'Farmers' link from left navigation panel
	public WebElement get_linkFarmers() {
		return isElementFound(lnk_Farmers, "Farmers Link", 150);
	}
	//Method to get 'Business Plan' link from left navigation panel
	public WebElement get_linkBusinessPlan() {
		return isElementFound(lnk_BusinessPlan, "Business Plan", 150);
	}
	//Method to get 'Field Checkup Series' link from left navigation panel
	public WebElement get_linkFieldCheckupSeries() {
		return isElementFound(lnk_FieldCheckupSeries, "Field Checkup Series", 150);
	}
	//Method to get 'Orders' link from left navigation panel
	public WebElement get_linkOrders() {
		return isElementFound(lnk_Orders, "Orders", 150);
	}
	//Method to get 'OrderDetails' link from left navigation panel
	public WebElement get_linkOrderDetails() {
		return isElementFound(lnk_OrderDetails, "OrderDetails", 150);
	}
	//Method to get 'Opportunities' link from left navigation panel
	public WebElement get_linkOpportunities() {
		return isElementFound(lnk_Opportunities, "Opportunities", 150);
	}
	//Method to get 'Inventory' link from left navigation panel
	public WebElement get_linkInventory() {
		return isElementFound(lnk_Inventory, "Inventory", 150);
	}
	//Method to get 'Inventory Details' link from left navigation panel
	public WebElement get_linkInventoryDetails() {
		return isElementFound(lnk_InventoryDetails, "Inventory Details", 150);
	}
	//Method to get 'Reporting' dropdown link from left navigation panel
	public WebElement get_Reporting_dropdown() {
		return isElementFound(lnk_Reporting_dropdown, "Reporting Drop-down button", 150);
	}
	//Method to get 'GPOS' link from left navigation panel
	public WebElement get_GPOS() {
		return isElementFound(lnk_GPOS, "GPOS button", 150);
	}
	//Method to get 'Inventory Details' link from left navigation panel
	public WebElement get_linkLogisticDetails() {
		return isElementFound(lnk_LogisticDetails, "Inventory Details", 150);
	}
	//Method to get 'My Company' link from left navigation panel
	public WebElement get_linkMyCompany() {
		return isElementFound(lnk_MyCompany, "My Company", 150);
	}
	//Method to get drop-down link for details link in left navigation panel
	public WebElement get_OrdersDropDown(String link) {
		By lnk_Dropdown = By.xpath("//div[contains(text(),'" + link + "')]//ancestor::li//i");
		return isElementFound(lnk_Dropdown, "Orders sub-menu drop-down link", 150);
	}
	//Method to get 'logisticDetails' link from left navigation panel
	public WebElement get_linklogisticDetails() {
		return isElementFound(lnk_logisticsDetails, "logisticDetails", 150);
	}
	//Method to get 'ATMS' link from left navigation panel
		public WebElement get_linkATMS() {
			return isElementFound(lnk_ATMS, "ATMS Link", 150);
		}
	//Method to get 'ATMS' link from left navigation panel
		public WebElement get_linkProductCodeLookup() {
			return isElementFound(lnk_ATMS, "ATMS Link", 150);
		}
}


