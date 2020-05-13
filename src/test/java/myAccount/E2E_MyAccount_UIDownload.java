package myAccount;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import com.applitools.eyes.BatchInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.*;
import com.Monsanto.ExcelOperations.ReadFlattenLocationHierarchy;

import FunctionLibrary.ReusableFunctions;
import PageObjects.CommonPageMyAccount;
import PageObjects.LeftNavigation_Accountdropdown;
import PageObjects.OrderDetailsPage;
import PageObjects.GigyaLoginPage;
import driver.Driver;

public class E2E_MyAccount_UIDownload extends Driver{
	private static Logger objLgr=LogManager.getLogger(E2E_MyAccount_UIDownload.class.getName());
	public GigyaLoginPage gigyaLoginPage;
	public CommonPageMyAccount commonPageMyAccount;
	public LeftNavigation_Accountdropdown lnk;
	String userName, userSAPAccountID, userType, userPassword, siteURL, platform_CBT, lob_selection;
	ReusableFunctions reusefunc = new ReusableFunctions();
	public boolean isTestCasePassed;
	public ReadFlattenLocationHierarchy readFlattenLocationHierarchy = new ReadFlattenLocationHierarchy();
	public List<Hashtable<String, String>> location_Flatten = new ArrayList<Hashtable<String, String>>();
	@SuppressWarnings("unchecked")
	public List<Hashtable<String, String>>[] location_Levels= new List[6];
	public JavascriptExecutor js;
	//public Eyes eyes;
	public static WebElement top10FarmOppWidgetHeader = null, progToZizPlnWidgetHeader = null, ordLngShrtWidgetHeader = null, shippingStatusWidgetHeader = null;
	public static WebElement ordConfWidgetHeader = null, ofrMgmtWidgetHeader = null, invCertWidgetHeader = null;
	public static WebElement lcnsStatWidgetHeader = null;
	public static WebElement fcusCCRWidgetHeader = null, cstmCropRptWidgetHeader = null, fcusActWidgetHeader = null;

	public E2E_MyAccount_UIDownload(Hashtable<String, String> input) {
		this.userName = input.get("UserName");
		this.userSAPAccountID = input.get("SAPAccountID");
		this.userType = input.get("UserType");
		this.userPassword = input.get("Password");
		this.siteURL = input.get("ApplicationURL");
		this.platform_CBT = input.get("CBT Platform");
		this.lob_selection = input.get("LOB");
	}

	@BeforeSuite
	public void beforeSuite() {
	}

	@AfterSuite
	public void afterSuite(){
	}

	@BeforeMethod
	public void nameBefore(Method method) {
		objLgr.info("Test name: " + method.getName() + " started.");
	}

	@AfterMethod
	public void nameAfter(Method method) {
		objLgr.info("Test name: " + method.getName() + " ended.");
	}

	@BeforeClass
	public void launchMyAccountHome() throws JSONException, Exception {
		Hashtable<String, String> platform_CBTHash = reusefunc.toHashtable(platform_CBT, ",", "=");
		webdriver=initialize(platform_CBTHash); //Fetching webdriver of the browser
		objLgr.info("Web driver initializing complete");
        js = ((JavascriptExecutor) webdriver);
		objLgr.info("webdriver/remoteWebDriver Initialized");
		webdriver.manage().deleteAllCookies();
		String env = null;
		String testName;
		if(applitoolsTurnedOn) {
			if(siteURL.contains("myaccount-q.agro.services")){
				env = "QA Env";
			} else if (siteURL.contains("my-agriportal-np.agro.services")){
				env = "Stg Env";
			} else {
				env = "Other Env";
			}
			BatchInfo bInfo = new BatchInfo("(" + env + ")" + "MyAccount E2E Testing for " + userName + "(" + userType + ")");
			testName = "ACS2 MyAccount (" + userType + "-" + env + ")";
//			bInfo.setId(userName);
			eyes = initializeApplitools(webdriver, bInfo, appName, testName);
		}


		objLgr.info("Launching application " + siteURL);
		if(webdriver == null){
			objLgr.info("webdriver is null");
		}
		webdriver.get(siteURL); //Opening the Application
		objLgr.info("Running test on " + siteURL);
		actn=new Actions(webdriver);
		lnk = new LeftNavigation_Accountdropdown(webdriver);
		commonPageMyAccount = new CommonPageMyAccount(webdriver);
		gigyaLoginPage=new GigyaLoginPage(webdriver);
		isTestCasePassed=true;
		try {
			objLgr.info("Attempting login as " +userName+" ("+userType+")");
			//Login to My Account Page
			gigyaLoginPage.get_UserName().sendKeys(userName);
			gigyaLoginPage.get_Password().sendKeys(userPassword);
			gigyaLoginPage.get_LoginButton().click();
			objLgr.info("Submit button on login page clicked");
		}
		catch(Exception e) {
			objLgr.info("Exception on login page " + e);
		}

		if(userType.equalsIgnoreCase("NB Dealer")) {
			location_Flatten = readFlattenLocationHierarchy.getIRDData(readFlattenLocationHierarchy.initializeExcel(prop.getProperty("FlattenedIRDLocHierarchy")+userSAPAccountID+"-Flatten.xlsx", 0, "")); //Getting location hierarchy for the logged in User
			location_Levels[1] = readFlattenLocationHierarchy.getLocationLevel1(location_Flatten); //Storing data for Location Level 1
			location_Levels[2] = readFlattenLocationHierarchy.getLocationLevel2(location_Flatten); //Storing data for Location Level 2
			location_Levels[3] = readFlattenLocationHierarchy.getLocationLevel3(location_Flatten); //Storing data for Location Level 3
			location_Levels[4] = readFlattenLocationHierarchy.getLocationLevel4(location_Flatten); //Storing data for Location Level 4
			location_Levels[5] = readFlattenLocationHierarchy.getLocationLevel5(location_Flatten); //Storing data for Location Level 5
		}

	}

	//Validating if the dashboard left navigation link is displayed
	@Test (enabled=false, description = "Validating whether 'Orders' link is available in left navigation.", priority=1000)
	public void linkVisibility_Order() throws Exception {
		// Start the test by setting AUT's name, window or the page name that's being tested, viewport width and height
		isTestCasePassed=true;
		if(platform_CBT.contains("Mobile")) {
			lnk.get_HamBurgerlink().click();
			objLgr.info("Hamburg Menu in left navigation clicked for Mobile");
		}
		if(lnk.get_OrdersDropDown("Orders") != null)
			objLgr.info("Orders link is displayed in Left Navigation panel");
		else
			isTestCasePassed=false;
		if (!isTestCasePassed)
			Assert.fail("Orders not displayed in left navigation panel");
	}
	
	//Validating MyView button and setting up the specified location
	@Test (enabled=false, description = "Validating LOB button and setting up the specified LOB.", dependsOnMethods= {"leftNavigation_OrdersDetails"},priority=1002)
	public void lob_selection() throws Exception {
	 isTestCasePassed=true;
	 Boolean flag=false;
	 if(userType.equalsIgnoreCase("NB Dealer")) {
	  WebElement changeMyViewButton = commonPageMyAccount.changeMyViewButton();
	  if(changeMyViewButton!=null) {
		 changeMyViewButton.click();
		 objLgr.info("Successfully clicked on MyView Button");
		 List<WebElement> list_LOBs = commonPageMyAccount.available_Lob_Options();
		 if(!list_LOBs.isEmpty())
		 for(WebElement lob :list_LOBs)
		 {
		  if(lob.getText().equals(lob_selection))
			 {
			  flag=true;
			  objLgr.info("Successfully located LOB in MyView section");
			    if(list_LOBs.size()>1) {
			       commonPageMyAccount.radioButton_LOB(lob_selection).click();
				   objLgr.info("Successfully clicked on "+lob_selection+" button");
				}
				 else 
					 objLgr.info("Only "+lob_selection+" LOB is available for the logged in Dealer");
					 break;
		  }
		}
		 if(flag==false) {
			 objLgr.error("Failed to locate " +lob_selection+" in LOB section of MyView");
			 isTestCasePassed=false;
		 }
		 if(commonPageMyAccount.applyButton()!=null) {
			commonPageMyAccount.applyButton().click();
		 }
		 else {
			isTestCasePassed=false;
			objLgr.error("Failed to locate apply button in  MyView");
		 }
	 }
	  else {
		   isTestCasePassed=false;
		   objLgr.error("Failed to set up specified LOB in MyView");
	  }
   }
	else {
	  objLgr.info("MyView TestCases will not apply to Channel Seedsman; skipping the test case");
	  throw new SkipException("MyView TestCases will not apply to Channel Seedsman");
	}
	if (!isTestCasePassed)
	  Assert.fail("Validating LOB button and setting up the specified LOB failed");
    }
	
	@Test(enabled=false, description = "Downloading Order Details page", dependsOnMethods= {"leftNavigation_OrdersDetails"}, priority=1003)
	public void OrderDetails_Download() throws Throwable {
		isTestCasePassed=true;
		commonPageMyAccount = new CommonPageMyAccount(webdriver);
		DateFormat dateFormat = new SimpleDateFormat("MMddYYYY");
		Date currentDate = new Date();
		String currentDateString = dateFormat.format(currentDate);
		//home.get_TabButton(home.get_header_OrderDetailstab().get(3)).click();
    	WebElement changeMyViewButton = commonPageMyAccount.changeMyViewButton();
    	if(changeMyViewButton!=null) {
    	   objLgr.info("Change My View Button is visible");
    	   changeMyViewButton.click();
    	}
    	reusefunc.changeDealerSRP(location_Levels,1,"OrderDetails_"+currentDateString+".csv","Order Details.csv");
    	actn.moveToElement(commonPageMyAccount.applyButton()).click().build().perform();
    	objLgr.info("Successfully downloaded Order Details UI extract");
	}
//	
//	@Test(enabled=false, description = "Downloading Trait tab of Order Details page", dependsOnMethods= {"leftNavigation_OrdersDetails"}, priority=1004)
//	public void TraitTab_OrderDetails_Download() throws Throwable {
//		isTestCasePassed=true;
//		commonPageMyAccount = new CommonPageMyAccount(webdriver);
//		DateFormat dateFormat = new SimpleDateFormat("MMddYYYY");
//		Date currentDate = new Date();
//		String currentDateString = dateFormat.format(currentDate);
//		home.get_TabButton(home.get_header_OrderDetailstab().get(1)).click();
//    	WebElement changeMyViewButton = commonPageMyAccount.changeMyViewButton();
//    	if(changeMyViewButton!=null) {
//    	   objLgr.info("Change My View Button is visible");
//    	   changeMyViewButton.click();
//    	}
//    	reusefunc.changeDealerSRP(location_Levels,1,"Trait_"+currentDateString+".csv","Trait-Order Details.csv");
//    	actn.moveToElement(commonPageMyAccount.applyButton()).click().build().perform();
//		
//	}
//	
//	@Test(enabled=false, description = "Downloading Product tab of Order Details page", dependsOnMethods= {"leftNavigation_OrdersDetails"}, priority=1005)
//	public void ProductTab_OrderDetails_Download() throws Throwable {
//		isTestCasePassed=true;
//		commonPageMyAccount = new CommonPageMyAccount(webdriver);
//		DateFormat dateFormat = new SimpleDateFormat("MMddYYYY");
//		Date currentDate = new Date();
//		String currentDateString = dateFormat.format(currentDate);
//		home.get_TabButton(home.get_header_OrderDetailstab().get(2)).click();
//    	WebElement changeMyViewButton = commonPageMyAccount.changeMyViewButton();
//    	if(changeMyViewButton!=null) {
//    	   objLgr.info("Change My View Button is visible");
//    	   changeMyViewButton.click();
//    	}
//    	reusefunc.changeDealerSRP(location_Levels,1,"Product_"+currentDateString+".csv","Hybrid-Order Details.csv");
//    	actn.moveToElement(commonPageMyAccount.applyButton()).click().build().perform();
//	}
//	
//	@Test(enabled=false, description = "Downloading Product Description tab of Order Details page", dependsOnMethods= {"leftNavigation_OrdersDetails"}, priority=1006)
//	public void ProdDescTab_OrderDetails_Download() throws Throwable {
//		isTestCasePassed=true;
//		commonPageMyAccount = new CommonPageMyAccount(webdriver);
//		DateFormat dateFormat = new SimpleDateFormat("MMddYYYY");
//		Date currentDate = new Date();
//		String currentDateString = dateFormat.format(currentDate);
//		home.get_TabButton(home.get_header_OrderDetailstab().get(3)).click();
//    	WebElement changeMyViewButton = commonPageMyAccount.changeMyViewButton();
//    	if(changeMyViewButton!=null) {
//    	   objLgr.info("Change My View Button is visible");
//    	   changeMyViewButton.click();
//    	}
//    	reusefunc.changeDealerSRP(location_Levels,1,"ProductDescription_"+currentDateString+".csv","ProdDesc-Order Details.csv");
//    	actn.moveToElement(commonPageMyAccount.applyButton()).click().build().perform();
//	}
//	
//	//Validating if the dashboard left navigation link is displayed
//		@Test (enabled=false, description = "Validating whether 'Inventory' link is available in left navigation.", dependsOnMethods= {"ProdDescTab_OrderDetails_Download"},priority=1007)
//		public void linkVisibility_Inventory() throws Exception {
//			// Start the test by setting AUT's name, window or the page name that's being tested, viewport width and height
//			isTestCasePassed=true;
//			validate_LogOut();
//			launchMyAccountHome();
//			if(platform_CBT.contains("Mobile")) {
//				lnk.get_HamBurgerlink().click();
//				objLgr.info("Hamburg Menu in left navigation clicked for Mobile");
//			}
//			if(lnk.get_OrdersDropDown("Inventory") != null)
//				objLgr.info("Inventory link is displayed in Left Navigation panel");
//			else
//				isTestCasePassed=false;
//			if (!isTestCasePassed)
//				Assert.fail("Inventory not displayed in left navigation panel");
//		}
//
//		@Test(enabled=false, description = "Validating that Inventory page is loaded when Inventory in left navigation is clicked.", dependsOnMethods= {"linkVisibility_Inventory"}, priority=1008)
//		public void leftNavigation_InventoryDetails() throws Exception {
//			isTestCasePassed=true;
//			lnk.get_OrdersDropDown("Inventory").click();
//			lnk.get_linkInventoryDetails().click();
//			if(ordDtls.get_TableHeader() != null)
//				objLgr.info("Inventory Details page loaded successfully");
//			else
//				isTestCasePassed=false;
//			if (!isTestCasePassed)
//				Assert.fail("Inventory Details page not loaded succesfully");
//			verifyPageWithApplitools(eyes);
//		}
//		
//		@Test(enabled=false, description = "Downloading Summary tab of Inventory Details page", dependsOnMethods= {"leftNavigation_InventoryDetails"}, priority=1009)
//		public void summaryTab_InventoryDetails_Download() throws Throwable {
//			isTestCasePassed=true;
//			commonPageMyAccount = new CommonPageMyAccount(webdriver);
//			DateFormat dateFormat = new SimpleDateFormat("MMddYYYY");
//			Date currentDate = new Date();
//			String currentDateString = dateFormat.format(currentDate);
//			//home.get_TabButton(home.get_header_OrderDetailstab().get(3)).click();
//	    	WebElement changeMyViewButton = commonPageMyAccount.changeMyViewButton();
//	    	if(changeMyViewButton!=null) {
//	    	   objLgr.info("Change My View Button is visible");
//	    	   changeMyViewButton.click();
//	    	}
//	    	reusefunc.changeDealerSRP(location_Levels,1,"Summary_"+currentDateString+".csv","Summary-Inventory Details.csv");
//	    	actn.moveToElement(commonPageMyAccount.applyButton()).click().build().perform();
//			
//		}
//		
//		@Test(enabled=false, description = "Downloading Trait tab of Inventory Details page", dependsOnMethods= {"leftNavigation_InventoryDetails"}, priority=1010)
//		public void TraitTab_InventoryDetails_Download() throws Throwable {
//			isTestCasePassed=true;
//			commonPageMyAccount = new CommonPageMyAccount(webdriver);
//			DateFormat dateFormat = new SimpleDateFormat("MMddYYYY");
//			Date currentDate = new Date();
//			String currentDateString = dateFormat.format(currentDate);
//			home.get_TabButton(home.get_header_OrderDetailstab().get(2)).click();
//	    	WebElement changeMyViewButton = commonPageMyAccount.changeMyViewButton();
//	    	if(changeMyViewButton!=null) {
//	    	   objLgr.info("Change My View Button is visible");
//	    	   changeMyViewButton.click();
//	    	}
//	    	reusefunc.changeDealerSRP(location_Levels,1,"Trait_"+currentDateString+".csv","Trait-Inventory Details.csv");
//	    	actn.moveToElement(commonPageMyAccount.applyButton()).click().build().perform();
//		}
//		
//		@Test(enabled=false, description = "Downloading Product tab of Inventory Details page", dependsOnMethods= {"leftNavigation_InventoryDetails"}, priority=1011)
//		public void ProductTab_InventoryDetails_Download() throws Throwable {
//			isTestCasePassed=true;
//			commonPageMyAccount = new CommonPageMyAccount(webdriver);
//			DateFormat dateFormat = new SimpleDateFormat("MMddYYYY");
//			Date currentDate = new Date();
//			String currentDateString = dateFormat.format(currentDate);
//			home.get_TabButton(home.get_header_OrderDetailstab().get(3)).click();
//	    	WebElement changeMyViewButton = commonPageMyAccount.changeMyViewButton();
//	    	if(changeMyViewButton!=null) {
//	    	   objLgr.info("Change My View Button is visible");
//	    	   changeMyViewButton.click();
//	    	}
//	    	reusefunc.changeDealerSRP(location_Levels,1,"Product_"+currentDateString+".csv","Hybrid-Inventory Details.csv");
//	    	actn.moveToElement(commonPageMyAccount.applyButton()).click().build().perform();
//		}
//		
//		@Test(enabled=false, description = "Downloading Product Description tab of Inventory Details page", dependsOnMethods= {"leftNavigation_InventoryDetails"}, priority=1012)
//		public void ProdDescTab_InventoryDetails_Download() throws Throwable {
//			isTestCasePassed=true;
//			commonPageMyAccount = new CommonPageMyAccount(webdriver);
//			DateFormat dateFormat = new SimpleDateFormat("MMddYYYY");
//			Date currentDate = new Date();
//			String currentDateString = dateFormat.format(currentDate);
//			home.get_TabButton(home.get_header_OrderDetailstab().get(1)).click();
//	    	WebElement changeMyViewButton = commonPageMyAccount.changeMyViewButton();
//	    	if(changeMyViewButton!=null) {
//	    	   objLgr.info("Change My View Button is visible");
//	    	   changeMyViewButton.click();
//	    	}
//	    	reusefunc.changeDealerSRP(location_Levels,1,"ProductDescription_"+currentDateString+".csv","ProdDesc-Inventory Details.csv");
//	    	actn.moveToElement(commonPageMyAccount.applyButton()).click().build().perform();
//			 
//		}
//	
//	@Test(enabled=true, description = "Check visiblility of VIEW DATA link of Top Ten Farmer Opportunity widget in Dashboard", priority=1013)
//	public void visibilityViewData_TopTenFarmOppDashboard() throws Exception {
//		isTestCasePassed=true;
//		String message = null;
//		top10FarmOppWidgetHeader = home.get_TopTenFarmerOpportunities();
//		if (home.get_ViewMoreLink(top10FarmOppWidgetHeader) != null)
//			message = "View More link has displayed in Top Ten Farmer Opportunities widget";
//		else if (home.get_NoDataWidget(top10FarmOppWidgetHeader) != null)
//			message = "Top Ten Farmer Opportunities widget displays \"No Data Available\".";
//		else {
//			isTestCasePassed = false;
//			message = "View More link is not available in Top Ten Farmer Opportunities widget";
//		}
//		objLgr.info(message);
//		if (!isTestCasePassed)
//			Assert.fail("visibilityViewData_topTenFarmOppDashboard failed - " + message);
//	}
//	
//	@Test(enabled=true, description = "Validate VIEW DATA table of Top Ten Farmer Opportunity widget loaded from Dashboard", dependsOnMethods= {"visibilityViewData_TopTenFarmOppDashboard"}, priority=1014)
//	public void download_viewData_LocationList_TopTenFarmOppDashboard() throws Throwable {
//		isTestCasePassed=true;
//		String message = null;
//		WebElement lnk_ViewData = home.get_ViewMoreLink(top10FarmOppWidgetHeader);
//		if (lnk_ViewData != null) {
//			lnk_ViewData.click();
//			home.update_FilterChips(home.get_ActivateFilterChips());
//			List<WebElement> data_Table = home.get_Table();
//			if (data_Table.size() > 1) {
//				message = "Location list table is loaded successfully";
//				commonPageMyAccount = new CommonPageMyAccount(webdriver);
//				DateFormat dateFormat = new SimpleDateFormat("MMddYYYY");
//				Date currentDate = new Date();
//				String currentDateString = dateFormat.format(currentDate);
//				WebElement changeMyViewButton = commonPageMyAccount.changeMyViewButton();
//		    	if(changeMyViewButton!=null) {
//		    	   objLgr.info("Change My View Button is visible");
//		    	   changeMyViewButton.click();
//		    	}
//		    	reusefunc.changeDealerSRP(location_Levels,1,"DealerOrderLongShort_"+currentDateString+".csv","Dealer Order Long Short View More.csv");
//				
//			}
//			else if (data_Table.size() == 1) {
//				isTestCasePassed = false;
//				message = "Only header has displayed in Location List table";
//			}
//			else if (home.get_NoDataTable(home.get_LocationListTable()) != null) {
//				isTestCasePassed = false;
//				message = "Top Ten Farmer Opportunities widget displays \"No Data Available\".";
//			}
//			else {
//				isTestCasePassed = false;
//				message = "Location list has not loaded successfully";
//			}
//		}
//		else if (home.get_NoDataWidget(home.get_TopTenFarmerOpportunities()) != null)
//			message = "Top Ten Farmer Opportunities widget displays \"No Data Available\".";
//		objLgr.info(message);
//		if (!isTestCasePassed)
//			Assert.fail("viewData_TopTenFarmOppDashboard failed - " + message);
//	}
	
	@Test(enabled=false, description = "Log out from My Account", dependsOnMethods= {"download_viewData_LocationList_TopTenFarmOppDashboard"}, priority=10001, alwaysRun=true)
	public void validate_LogOut() throws Exception {
		isTestCasePassed=true;
		if (siteURL.contains("-q.agro"))
			objLgr.info("Log out functionality not available in QA environment.");
		else {
			String message = "";
			lnk.get_DropdownLogOut().click();
			lnk.get_LogOut().get(0).click();
			if (gigyaLoginPage.get_UserName() != null)
				message = "Logged out successful.";
			else {
				isTestCasePassed=false;
				message = "Username field not displayed after logout action";
			}
			objLgr.info(message);
			if (!isTestCasePassed)
				Assert.fail("Logout failed. " + message);
		}
	}

	//------------------------------End of Testcases included on 21st-Feb-2019------------------------------------------//

	@AfterClass
	public void captureBrowserConsoleLogs() {
		try {
			if(applitoolsTurnedOn) {
				eyes.close(false);
				objLgr.info("Applitools Eyes.close() successful");
			}
		} catch (Exception e){
			objLgr.info("Applitools Eyes.close() failed." + e);
		}
		if (browser.equals("Chrome")) {
			analyzeLog();
		}
		objLgr.info("Closing browser.");
		webdriver.close();
		if(webdriver == null){
			objLgr.info("Webdriver is null");
		}
		objLgr.info("Disposing webdriver/removeWebdriver.");
		webdriver.quit();
	}
}

