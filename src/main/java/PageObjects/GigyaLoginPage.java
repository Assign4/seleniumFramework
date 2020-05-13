package PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class GigyaLoginPage extends CommonPageMyAccount {

	public GigyaLoginPage(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	//X-Path for User ID
	By txt_username = By.xpath("//input[@name='username' and @placeholder]");
	//X-Path for Password
	By txt_password = By.xpath("//input[@name='password' and @placeholder]");
	//X-Path for Submit button
	By btn_loginButton=By.xpath("//input[@value='Submit']");

	//Method to get user name field in Login page
	public WebElement get_UserName() {
		return isElementFound(txt_username, "User Name field", 150);
	}
	//Method to get password field in Login page
	public WebElement get_Password() {
		return isElementFound(txt_password, "Password field", 90);
	}
	//Method to get Submit button in Login page
	public WebElement get_LoginButton() {
		return isElementFound(btn_loginButton, "Submit button", 90);
	}
}
