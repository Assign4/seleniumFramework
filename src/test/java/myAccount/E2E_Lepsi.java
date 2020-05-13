package myAccount;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import com.applitools.eyes.BatchInfo;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.*;
import com.Monsanto.ExcelOperations.ReadFlattenLocationHierarchy;

import FunctionLibrary.ReusableFunctions;
import PageObjects.CommonPageMyAccount;
import PageObjects.DealerOrderChangeLog;
import PageObjects.LeftNavigation_Accountdropdown;
import PageObjects.LepsiATMS;
import PageObjects.OrderDetailsPage;
import PageObjects.ProductAvailability;
import PageObjects.ProductCodeLookUp;
import PageObjects.ClaimsReport;
import PageObjects.PaymentsFromBayer;
import PageObjects.GigyaLoginPage;
import PageObjects.GposNB;
import driver.Driver;

public class E2E_Lepsi extends Driver{
	private static Logger objLgr=LogManager.getLogger(E2E_Lepsi.class.getName());
	public GigyaLoginPage gigyaLoginPage;
	public CommonPageMyAccount commonPageMyAccount;
	public OrderDetailsPage ordDtls;
	public LepsiATMS atms;
	public int random_row;
	public ProductCodeLookUp pclu;
	public DealerOrderChangeLog docl;
	public GposNB gpos;
	public ProductAvailability productAvail;
	public LeftNavigation_Accountdropdown lnk;
	public ClaimsReport cr;
	public PaymentsFromBayer pfb;
	String program,crop,userName, userSAPAccountID, userType, userPassword, siteURL,platform_perfecto, platform_CBT, lob_selection,aggregate_Data,widgetText,tableText;
	String location_level1SAPID, location_level2SAPID, location_level3SAPID, location_level4SAPID, location_level5SAPID;
	ReusableFunctions reusefunc = new ReusableFunctions();
	public boolean isTestCasePassed,isLoginSuccessfull,nodataflag;
	public ReadFlattenLocationHierarchy readFlattenLocationHierarchy = new ReadFlattenLocationHierarchy();
	public List<Hashtable<String, String>> location_Flatten = new ArrayList<Hashtable<String, String>>();
	@SuppressWarnings("unchecked")
	public List<Hashtable<String, String>>[] location_LevelsIRD= new List[6];
	Hashtable<String, String> locationNode_IRD = new Hashtable<String, String>();
	public JavascriptExecutor js;
	public int sessionCount=0;
	public WebElement widget_Corn_ProdAvail,widget_Soybeans_ProdAvail,widget_Cotton_ProdAvail,widget_Alfalfa_ProdAvail,widget_Sorghum_ProdAvail,widget_Canola_ProdAvail;
	//public Eyes eyes
	public static WebElement availableSpaceWidgetHeader = null, productSummaryWidgetHeader=null, tankStatusWidgetHeader=null;

	public E2E_Lepsi(Hashtable<String, String> input) {
		this.userName = input.get("UserName");
		this.userSAPAccountID = input.get("SAPAccountID");
		this.userType = input.get("UserType");
		this.userPassword = input.get("Password");
		this.siteURL = input.get("ApplicationURL");
		this.platform_perfecto = input.get("Perfecto Platform");
		this.platform_CBT = input.get("CBT Platform");
		this.lob_selection = input.get("LOB");
		this.aggregate_Data = input.get("Aggregate Data Selection");
		this.location_level1SAPID = input.get("Level 1 SAP ID");
		this.location_level2SAPID = input.get("Level 2 SAP ID");
		this.location_level3SAPID = input.get("Level 3 SAP ID");
		this.location_level4SAPID = input.get("Level 4 SAP ID");
		this.location_level5SAPID = input.get("Level 5 SAP ID");
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
		objLgr.info("Start of " + method.getName() + " test case screenshot taken");
		try {
			getScreenshot(method.getName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			//			e.printStackTrace();
			objLgr.info("Screenshoting failed " +e);
		}
	}

	@AfterMethod
	public void nameAfter(Method method) {
		objLgr.info("Test name: " + method.getName() + " ended.");
		objLgr.info("End of " + method.getName() + " test case screenshot taken");
		try {
			getScreenshot(method.getName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			//			e.printStackTrace();
			objLgr.info("Screenshoting failed " +e);
		}
	}

	@BeforeClass
	public void launchMyAccountHome() throws JSONException, Exception {
		Hashtable<String, String> platform_Hash = reusefunc.toHashtable(platform_perfecto, ",", "=");
		webdriver=initialize(platform_Hash); //Fetching webdriver of the browser
		objLgr.info("Web driver initializing complete");
		js = ((JavascriptExecutor) webdriver);
		objLgr.info("webdriver/remoteWebDriver Initialized");
		webdriver.manage().deleteAllCookies();
		String env = null;
		String testName;
//				if(applitoolsTurnedOn) {
//					if(siteURL.contains("myaccount-q.agro.services")){
//						env = "QA Env";
//					} else if (siteURL.contains("my-agriportal-np.agro.services")){
//						env = "Stg Env";
//					} else {
//						env = "Other Env";
//					}
//					BatchInfo bInfo = new BatchInfo("(" + env + ")" + "MyAccount E2E Testing for " + userName + "(" + userType + ")");
//					testName="ACS2MyAccount("+userType+"-"+env+")Session:"+sessionCount;
//					eyes = initializeApplitools(webdriver, bInfo, appName, testName);
		//Maximize browser for Windows and Mac
		if (platform.equals("MAC") || (platform.equals("crossbrowsertesting.com")  || platform.equals("Perfecto")  && platform_CBT.toString().contains("Desktop") && platform_CBT.toString().contains("Mac")))
			webdriver.manage().window().fullscreen();
		else if (platform.equals("Windows") || (platform.equals("crossbrowsertesting.com") || platform.equals("Perfecto")  && platform_CBT.toString().contains("Desktop") && platform_CBT.toString().contains("Windows")))
			webdriver.manage().window().maximize();
//				}


		objLgr.info("Launching application " + siteURL);
		if(webdriver == null){
			objLgr.info("webdriver is null");
		}
		webdriver.get(siteURL); //Opening the Application
		objLgr.info("Running test on " + siteURL);
		lnk = new LeftNavigation_Accountdropdown(webdriver);
		commonPageMyAccount = new CommonPageMyAccount(webdriver);
		actn=new Actions(webdriver);
		gigyaLoginPage=new GigyaLoginPage(webdriver);
		atms = new LepsiATMS(webdriver);
		pclu = new ProductCodeLookUp(webdriver);
		docl = new DealerOrderChangeLog(webdriver);
		ordDtls = new OrderDetailsPage(webdriver);
		productAvail = new ProductAvailability(webdriver);
		cr = new ClaimsReport(webdriver);
		pfb = new PaymentsFromBayer(webdriver);
		gpos = new GposNB(webdriver);

		isTestCasePassed=true;
		if(aggregate_Data.isEmpty())
			aggregate_Data="All Locations";
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

		if(userType.equalsIgnoreCase("NB Dealer") && lob_selection.equalsIgnoreCase("Seed")) {
			location_Flatten = readFlattenLocationHierarchy.getIRDData(readFlattenLocationHierarchy.initializeExcel(prop.getProperty("FlattenedIRDLocHierarchy")+userSAPAccountID+"-Flatten.xlsx", 0, "")); //Getting location hierarchy for the logged in User
			location_LevelsIRD[1] = readFlattenLocationHierarchy.getLocationLevel1(location_Flatten); //Storing data for Location Level 1
			location_LevelsIRD[2] = readFlattenLocationHierarchy.getLocationLevel2(location_Flatten); //Storing data for Location Level 2
			location_LevelsIRD[3] = readFlattenLocationHierarchy.getLocationLevel3(location_Flatten); //Storing data for Location Level 3
			location_LevelsIRD[4] = readFlattenLocationHierarchy.getLocationLevel4(location_Flatten); //Storing data for Location Level 4
			location_LevelsIRD[5] = readFlattenLocationHierarchy.getLocationLevel5(location_Flatten); //Storing data for Location Level 5
		}

	}


	//Validating if the dashboard left navigation link is displayed
	@Test (enabled=true, description = "Validating whether 'Dasbhoard' link is available in left navigation.", priority=1000)
	public void linkVisibility_Dashboard() throws Exception {
		// Start the test by setting AUT's name, window or the page name that's being tested, viewport width and height
		isTestCasePassed=true;
		isLoginSuccessfull = false;
		
		if(platform_CBT.contains("Mobile")) {
			lnk.get_HamBurgerlink().click();
			objLgr.info("Hamburg Menu in left navigation clicked for Mobile");
		}
		if(lnk.get_linkDashBoard() != null) {
			objLgr.info("Dashboard link is displayed in Left Navigation panel");
			isLoginSuccessfull = true;
		}
		else
			isTestCasePassed=false;
		verifyPageWithApplitools(eyes);
		if (!isTestCasePassed)
			Assert.fail("Dashboard not displayed in left navigation panel");
	}


	//Validating MyView button and setting up the specified location
	@Test (enabled=false, description = "Validating LOB button and setting up the specified LOB.", dependsOnMethods= {"linkVisibility_Dashboard"},priority=501)
	public void select_LineOfBusiness() throws Exception {
		isTestCasePassed=true;
		actn=new Actions(webdriver);
		if(userType.equalsIgnoreCase("NB Dealer")) {
			if(lob_selection.equalsIgnoreCase("Seed-Product"))
				lob_selection = "Seed";

			WebElement changeMyViewButton = commonPageMyAccount.changeMyViewButton();
			if(changeMyViewButton!=null) {
				changeMyViewButton.click();
				objLgr.info("Successfully clicked on MyView Button");
				List<WebElement> list_LOBs = commonPageMyAccount.available_Lob_Options();
				if(!list_LOBs.isEmpty()) {
					for(WebElement lob :list_LOBs)
					{
						if(lob.getText().equals(lob_selection))
						{
							objLgr.info("Successfully located LOB in MyView section");
							if(!lob_selection.equalsIgnoreCase("Seed")) {
								commonPageMyAccount.radioButton_LOB(lob_selection).click();	
								objLgr.info("Successfully clicked on "+lob_selection+" button");
							}
							else 
								objLgr.info(lob_selection+" LOB is selected by default for the logged in Dealer");
							break;
						}
					}
				}
				else {
					objLgr.error("LOB options weblocator list is empty");
					isTestCasePassed=false;
				}
				if(commonPageMyAccount.applyButton()!=null) {
					commonPageMyAccount.applyButton().click();
				}
				else {
					isTestCasePassed=false;
					objLgr.error("Failed to locate apply button in  MyView");
				}
				if(commonPageMyAccount.dataAggregated_SelectedText(lob_selection).isDisplayed()) {
					objLgr.info("LOB is validated and specified option : '"+lob_selection+"' is selected");
					isTestCasePassed=true;
				}
				else
				{
					isTestCasePassed=false;
					objLgr.error(lob_selection+" is not displayed in the UI");
				}

			}
			else {
				isTestCasePassed=false;
				objLgr.error("Failed to MyView button");
			}
		}
		else if(userType.equalsIgnoreCase("Channel Seedsman")) {
			objLgr.info("MyView TestCases will not apply to Channel Seedsman; skipping the test case");
			throw new SkipException("MyView TestCases will not apply to Channel Seedsman");
		}
		if (!isTestCasePassed)
			Assert.fail("Validating LOB button and setting up the specified LOB failed");
	}

	//Validating MyView button and setting up the specified location
	@Test (enabled=false, description = "Validating MyView button and setting up the specified location.", dependsOnMethods= {"linkVisibility_Dashboard"},priority=502)
	public void MyView() throws Exception {
		isTestCasePassed=true;
		if(userType.equalsIgnoreCase("NB Dealer") && lob_selection.equalsIgnoreCase("Seed")){
			if (location_level1SAPID == null)
				location_level1SAPID = userSAPAccountID;
			locationNode_IRD = reusefunc.get_LocationData(location_level1SAPID, location_level2SAPID, location_level3SAPID, location_level4SAPID, location_level5SAPID, location_LevelsIRD);
			if (reusefunc.compare_LocationSAP(location_level1SAPID, locationNode_IRD.get("Level 1 SAP Account ID")))
				objLgr.info("Location Level 1 in IRD is matching with datasheet.");
			else {
				isTestCasePassed=false;
				objLgr.error("Location Level 1 in IRD is not matching with datasheet.");
			}
			if (reusefunc.compare_LocationSAP(location_level2SAPID, locationNode_IRD.get("Level 2 SAP Account ID")))
				objLgr.info("Location Level 2 in IRD is matching with datasheet.");
			else {
				isTestCasePassed=false;
				objLgr.error("Location Level 2 in IRD is not matching with datasheet.");
			}
			if (reusefunc.compare_LocationSAP(location_level3SAPID, locationNode_IRD.get("Level 3 SAP Account ID")))
				objLgr.info("Location Level 3 in IRD is matching with datasheet.");
			else {
				isTestCasePassed=false;
				objLgr.error("Location Level 3 in IRD is not matching with datasheet.");
			}
			if (reusefunc.compare_LocationSAP(location_level4SAPID, locationNode_IRD.get("Level 4 SAP Account ID")))
				objLgr.info("Location Level 4 in IRD is matching with datasheet.");
			else {
				isTestCasePassed=false;
				objLgr.error("Location Level 4 in IRD is not matching with datasheet.");
			}
			if (reusefunc.compare_LocationSAP(location_level5SAPID, locationNode_IRD.get("Level 5 SAP Account ID")))
				objLgr.info("Location Level 5 in IRD is matching with datasheet.");
			else {
				isTestCasePassed=false;
				objLgr.error("Location Level 5 in IRD is not matching with datasheet.");
			}
			if (reusefunc.select_Loction(locationNode_IRD,aggregate_Data,lob_selection)==true) {
				objLgr.info("MyView button is validated and specified location is setup in MyView section");
				if(commonPageMyAccount.dataAggregated_SelectedText(aggregate_Data).isDisplayed()) {
					objLgr.info("AllLocations/MyLocation is validated and specified option : "+aggregate_Data+" is selected");
					isTestCasePassed=true;
				}
				else
					isTestCasePassed=false;
			}
			else {
				isTestCasePassed=false;
				objLgr.error("Failed to set up specified locations in MyView");
			}
		}
		else {
			objLgr.info("MyView TestCases will not apply to Channel Seedsman; skipping the test case");
			throw new SkipException("MyView TestCases will not apply to Channel Seedsman");
		}
		if (!isTestCasePassed)
			Assert.fail("Validating MyView button and setting up the specified location failed");
	}


	//--------------------------------------ATMS Test Cases---------------------------------------//

	@Test(enabled=false, description = "To check visibility of ATMS link in left navigation panel for Crop Protection", dependsOnMethods= {"linkVisibility_Dashboard"},alwaysRun=true, priority=1001)
	public void linkVisibility_ATMS() throws Exception {
		isTestCasePassed=true;
		if(lob_selection.equalsIgnoreCase("Crop Protection"))
		{
			validate_LogOut();
			captureBrowserConsoleLogs();
			launchMyAccountHome();
			linkVisibility_Dashboard();
			select_LineOfBusiness();
			Thread.sleep(1000);
			if(platform_CBT.contains("Mobile")) {
				lnk.get_HamBurgerlink().click();
				objLgr.info("Hamburg Menu in left navigation clicked for Mobile");
			}
			if(lnk.get_linkATMS() != null)
				objLgr.info("ATMS link is displayed in Left Navigation panel");
			else
				//isTestCasePassed=false;
				//if (!isTestCasePassed)
				//Assert.fail("ATMS link is not displayed in left navigation panel");
				objLgr.info("ATMS link is not displayed in Left Navigation panel");
		}
		else
		{
			objLgr.info("ATMS does not apply to"+userType+"/"+lob_selection);
			throw new SkipException("ATMS does not apply to"+userType+"/"+lob_selection);
		}
	}

	@Test(enabled=false, description = "Validating that ATMS page is loaded successfully when ATMS Link is clicked in left navigation.", dependsOnMethods= {"linkVisibility_ATMS"}, priority=1002)
	public void leftNavigation_ATMS() throws Exception {
		isTestCasePassed=true;
		//lnk.get_linkATMS().click();

		webdriver.get("https://lepsi-d.agro.services/myaccount/atms");
		Thread.sleep(1000);
		select_LineOfBusiness();
		Thread.sleep(1000);

		if(atms.get_PageHeader() != null)
			objLgr.info("Automated Tank Monitoring System (ATMS) page loaded successfully");
		else
			isTestCasePassed=false;
		if (!isTestCasePassed)
			Assert.fail("Automated Tank Monitoring System (ATMS) page loaded successfully");
		//		verifyPageWithApplitools(eyes);
	}

	@Test(enabled=false,description = "Validating if Available Space widget is loaded successfully in ATMS page", dependsOnMethods= {"leftNavigation_ATMS"}, priority=1003)
	public void availableSpaceWidgetLoad_ATMS() throws Exception {
		isTestCasePassed=true;
		String message = null;
		widgetText = null;
		availableSpaceWidgetHeader = atms.get_AvailableSpace();
		if (availableSpaceWidgetHeader != null) {
			js.executeScript("arguments[0].scrollIntoView();",availableSpaceWidgetHeader);
			if (atms.get_widgetList(availableSpaceWidgetHeader) != null)
				message = "Available Space widget has been loaded succesfully";
			else if (atms.get_NoDataWidget(availableSpaceWidgetHeader) != null) {
				message = "Available Space widget displays \"No Data Available\".";
				widgetText = "No Data available";
			}
			else if (atms.get_TryAgain(availableSpaceWidgetHeader) != null) {
				isTestCasePassed = false;
				message = "Available Space widget displays Try Again.";
				widgetText = "Try Again";
			}
			objLgr.info(message);
		}	
		else
			isTestCasePassed = false;
		if (!isTestCasePassed)
			Assert.fail("availableSpaceWidgetLoad_ATMS failed. " + message);

	}

	@Test(enabled=false, description = "Check visiblility of VIEW DATA link of Available Space widget in ATMS page", dependsOnMethods= {"availableSpaceWidgetLoad_ATMS"}, priority=1004)
	public void visibilityViewData_availableSpaceATMS() throws Exception {
		isTestCasePassed=true;
		String message = null;
		if(widgetText == null) {
			availableSpaceWidgetHeader = atms.get_AvailableSpace();
			if (atms.get_ViewMoreLink(availableSpaceWidgetHeader) != null)
				message = "View More link is displayed in Available Space widget";
			else {
				isTestCasePassed = false;
				message = "View More link is not available in Available Space widget";
			}
			objLgr.info(message);
		}
		else if(widgetText.equalsIgnoreCase("No Data Available")) {
			objLgr.info("Available Space widget displays \"No Data Available\".");
			throw new SkipException("Available Space widget has no data to display for the SAPId :"+userSAPAccountID);
		}
		if (!isTestCasePassed)
			Assert.fail("visibilityViewData_availableSpaceATMS failed - " + message);
	}

	@Test(enabled=false, description = "Validate ATMS - View Data table loaded successfully on clicking View Data link on Available Space widget", dependsOnMethods= {"visibilityViewData_availableSpaceATMS"}, priority=1005)
	public void viewData_ATMSAvlblSpc() throws Exception {
		isTestCasePassed=true;
		atms.get_ViewMoreLink(availableSpaceWidgetHeader).click();
		if (atms.get_TableHeader() != null) {
			objLgr.info("ATMS Details page loaded successfully");
			//reusefunc.select_Loction(locationNode_IRD,aggregate_Data);
		}
		else
			isTestCasePassed=false;
		if (!isTestCasePassed)
			Assert.fail("ATMS Details page not loaded succesfully");
		//		verifyPageWithApplitools(eyes);
	}

	@Test(enabled=false, description = "Validate Detail tab loaded in ATMS Details page", dependsOnMethods= {"viewData_ATMSAvlblSpc"}, priority=1006)
	public void detailTabLoad_ATMS() throws Exception {
		List<WebElement> header = null;
		header = atms.get_header_ATMSDetailstab();
		isTestCasePassed=true;
		String message = null;		
		if (header!= null) {
			String tableName = null;
			if (atms.get_Table() != null) {
				tableName = atms.get_TableHeader().get(atms.get_TableHeader().size()-1).getText();
				message = tableName + " table is loaded successfully";
			}
			else if (atms.get_NoDataTable(atms.get_DetailTab()) != null) {
				message = tableName + " table displays \"No Data Available\".";
			}
			else {
				isTestCasePassed = false;
				message = tableName + " table has not loaded successfully";
			}
		}
		objLgr.info(message);
		if (!isTestCasePassed)
			Assert.fail("detailTabLoad_ATMS failed - " + message);
	}

	@Test(enabled=false, description = "Validate download link is displayed in ATMS-Detail tab", dependsOnMethods= {"detailTabLoad_ATMS"}, priority=1007)
	public void DwnldLnkVisibility_ATMSdetailTab() throws Exception {
		isTestCasePassed=true;
		if (atms.get_DetailTab() != null) {
			String tableName = atms.get_TableHeader().get(atms.get_TableHeader().size()-1).getText();		
			if(atms.get_lnk_Download()!=null)
				objLgr.info("Download link is displayed in "+tableName+" tab");
			else
				objLgr.info("Download link is not displayed in "+tableName+" tab");
		}
		else
		{
			objLgr.info("ATMS-Details tab has not loaded");
		}
	}

	@Test(enabled=false, description = "Validate Sorting functionality of ATMS-Detail tab", dependsOnMethods= {"detailTabLoad_ATMS"}, priority=1008)
	public void detailTabSort_ATMS() throws Exception {
		isTestCasePassed=true;
		String message = "";
		String tableName = atms.get_TableHeader().get(atms.get_TableHeader().size()-1).getText();
		WebElement detailTab = null;
		detailTab = atms.get_DetailTab();
		List<WebElement> row_HeaderList = atms.get_RowHeader(detailTab);
		for (WebElement header_Field : row_HeaderList) {
			String txt_Field = header_Field.getText();
			objLgr.info("Sorting has been started for field - "+txt_Field);
			for(int i=0;i<=1;i++) {
				List<WebElement> fields_Sort = atms.get_SortField(txt_Field);
				WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
				field_Sort.click();
				List<WebElement> row_Header = atms.get_RowHeader(detailTab);
				List<WebElement> row_Data = atms.get_RowData(detailTab);
				List<Hashtable<String, String>> SelectedData = commonPageMyAccount.read_TableData(row_Header, row_Data, atms.get_fieldData());
				String sort_Order = field_Sort.getAttribute("aria-sort");
				String value_Alignment = fields_Sort.get(0).getCssValue("text-align").toString();
				if (commonPageMyAccount.sort_Table	Data(SelectedData, txt_Field, sort_Order, value_Alignment))
					message = "Sorting has been validated successfully in "+tableName+" tab for field " +txt_Field+ " in " + sort_Order + "order.";
				else {
					isTestCasePassed=false;
					message = "Sorting has been failed in "+ tableName +" tab for field " +txt_Field+ " in " + sort_Order + "order.";
				}
				objLgr.info(message);
			}
		}
		if (!isTestCasePassed)
			Assert.fail("detailTabSort_ATMS failed -" +message);
	}



	@Test(enabled=false, description = "Validate Locations drop-down in ATMS-Detail Table", dependsOnMethods= {"detailTabLoad_ATMS"}, priority=1009)
	public void locationsdropDown_ATMSDetailTab() throws Exception {
		isTestCasePassed=true;
		String location = null;
		WebElement tbl_ATMSDetail = null;
		List<WebElement> list_LocationsInitial = null;
		WebElement locationsDropdown_ATMSDetail = atms.get_LocationDropdown();
		if (locationsDropdown_ATMSDetail != null) {
			locationsDropdown_ATMSDetail.click();
			tbl_ATMSDetail = atms.get_DetailTab();
			list_LocationsInitial = atms.get_DropdownList(locationsDropdown_ATMSDetail);
			locationsDropdown_ATMSDetail.click();
			for (int i = 0; i < list_LocationsInitial.size(); i++) {
				locationsDropdown_ATMSDetail.click();
				List<WebElement> list_Locations = atms.get_DropdownList(locationsDropdown_ATMSDetail);
				location = list_Locations.get(i).getText();
				js.executeScript("arguments[0].scrollIntoView();",list_Locations.get(i));
				list_Locations.get(i).click();
				if(!location.equalsIgnoreCase("All Locations")) {
					objLgr.info(location + " is selected in Locations drop-down of ATMS-Detail Tab");
					List<WebElement> data_Table = atms.get_Table();
					if(data_Table.size() > 1) {
						objLgr.info("ATMS-Detail tab is loaded successfully for selected location : "+location);
						for(int j=0; j<=1; j++) {
							List<WebElement> fields_Sort = atms.get_SortField("Location");
							WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
							field_Sort.click();
							objLgr.info("Sorting action is performed on the location column in " +field_Sort.getAttribute("aria-sort") + " order.");
							List<WebElement> row_Header = atms.get_RowHeader(tbl_ATMSDetail);
							List<WebElement> row_Data = atms.get_RowData(tbl_ATMSDetail);
							Hashtable<String, String> firstRow_LocationData = commonPageMyAccount.read_TableData(row_Header, row_Data, atms.get_fieldData(), 0);
							if(firstRow_LocationData.get("Location").toString().equalsIgnoreCase(location))
								objLgr.info("Data is displayed for the selected location: "+location);
							else {
								isTestCasePassed=false;
								objLgr.error("Data is not displayed for the selected location: "+location);
							}
						}
					}

					else if (atms.get_NoDataTable(tbl_ATMSDetail) != null) 
						objLgr.info("ATMS-Details tab displays \"No Data Available\". for location - " + location);
					else {
						isTestCasePassed = false;
						objLgr.error("ATMS_Detail tab displays Try Again. for location - " + location);
					}
				}
			}
		}
		else {
			isTestCasePassed = false;
			objLgr.info("location drop down button could not be located");
		}
		if (!isTestCasePassed)
			Assert.fail("locations drop-down list validation failed");
		else {
			objLgr.info("Successfully validated location drop down in ATMS-Detail tab");
			locationsDropdown_ATMSDetail.click();
			list_LocationsInitial= atms.get_DropdownList(locationsDropdown_ATMSDetail);
			for(WebElement option : list_LocationsInitial) {
				if(option.getText().equalsIgnoreCase("All Locations"))
					option.click();
				break; 
			}
		}
	}


	@Test(enabled=false, description = "Validate Products drop-down in ATMS-Detail Table", dependsOnMethods= {"detailTabLoad_ATMS"}, priority=1010)
	public void productsdropDown_ATMSDetailTab() throws Exception {
		isTestCasePassed=true;
		String product = null;
		WebElement tbl_ATMSDetail = null;
		List<WebElement> list_ProductsInitial = null;
		WebElement productsDropdown_ATMSDetail = atms.get_ProductDropdown();
		if (productsDropdown_ATMSDetail != null) {
			productsDropdown_ATMSDetail.click();
			tbl_ATMSDetail = atms.get_DetailTab();
			list_ProductsInitial = atms.get_DropdownList(productsDropdown_ATMSDetail);
			productsDropdown_ATMSDetail.click();
			for (int i = 0; i < list_ProductsInitial.size(); i++) {
				productsDropdown_ATMSDetail.click();
				List<WebElement> list_Products = atms.get_DropdownList(productsDropdown_ATMSDetail);
				product = list_Products.get(i).getText();
				js.executeScript("arguments[0].scrollIntoView();",list_Products.get(i));
				list_Products.get(i).click();
				if(!product.equalsIgnoreCase("All Products")) {
					objLgr.info(product + " is selected in products drop-down of ATMS-Detail Tab");
					List<WebElement> data_Table = atms.get_Table();
					if(data_Table.size() > 1) {
						objLgr.info("ATMS-Detail tab is loaded successfully for selected product : "+product);
						for(int j=0; j<=1; j++) {
							List<WebElement> fields_Sort = atms.get_SortField("Product Name");
							WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
							field_Sort.click();
							objLgr.info("Sorting action is performed on the product column in " +field_Sort.getAttribute("aria-sort") + " order.");
							List<WebElement> row_Header = atms.get_RowHeader(tbl_ATMSDetail);
							List<WebElement> row_Data = atms.get_RowData(tbl_ATMSDetail);
							Hashtable<String, String> firstRow_LocationData = commonPageMyAccount.read_TableData(row_Header, row_Data, atms.get_fieldData(), 0);
							if(firstRow_LocationData.get("Product Name").toString().equalsIgnoreCase(product))
								objLgr.info("Data is displayed for the selected product: "+product);
							else {
								isTestCasePassed=false;
								objLgr.error("Data is not displayed for the selected product: "+product);
							}
						}
					}

					else if (atms.get_NoDataTable(tbl_ATMSDetail) != null) 
						objLgr.info("ATMS-Details tab displays \"No Data Available\". for product - " + product);
					else {
						isTestCasePassed = false;
						objLgr.error("ATMS_Detail tab displays Try Again. for product - " + product);
					}
				}
			}
		}
		else {
			isTestCasePassed = false;
			objLgr.info("Products drop down button could not be located");
		}
		if (!isTestCasePassed)
			Assert.fail("Products drop-down list validation failed");
		else {
			objLgr.info("Successfully validated products drop down in ATMS-Detail tab");
			productsDropdown_ATMSDetail.click();
			list_ProductsInitial= atms.get_DropdownList(productsDropdown_ATMSDetail);
			for(WebElement option : list_ProductsInitial) {
				if(option.getText().equalsIgnoreCase("All Products"))
					option.click();
				break; 
			}
		}
	}

	@Test(enabled=false, description = "Validate Tank Status drop-down in ATMS-Detail Table", dependsOnMethods= {"detailTabLoad_ATMS"}, priority=1011)
	public void tankStatusDropDown_ATMSDetailTab() throws Exception {
		isTestCasePassed=true;
		String tankstatus = null;
		WebElement tbl_ATMSDetail = null;
		List<WebElement> list_TankStatusInitial = null;
		WebElement tankStatusDropdown_ATMSDetail = atms.get_TankStatusDropdown();
		if (tankStatusDropdown_ATMSDetail != null) {
			tankStatusDropdown_ATMSDetail.click();
			tbl_ATMSDetail = atms.get_DetailTab();
			list_TankStatusInitial = atms.get_DropdownList(tankStatusDropdown_ATMSDetail);
			tankStatusDropdown_ATMSDetail.click();
			for (int i = 0; i < list_TankStatusInitial.size(); i++) {
				tankStatusDropdown_ATMSDetail.click();
				List<WebElement> list_TankStatus = atms.get_DropdownList(tankStatusDropdown_ATMSDetail);
				tankstatus = list_TankStatus.get(i).getText();
				js.executeScript("arguments[0].scrollIntoView();",list_TankStatus.get(i));
				list_TankStatus.get(i).click();
				if(!tankstatus.equalsIgnoreCase("All Tank Status")) {
					objLgr.info(tankstatus + " is selected in tank status drop-down of ATMS-Detail Tab");
					List<WebElement> data_Table = atms.get_Table();
					if(data_Table.size() > 1) {
						objLgr.info("ATMS-Detail tab is loaded successfully for selected tank status : "+tankstatus);
						for(int j=0; j<=1; j++) {
							List<WebElement> fields_Sort = atms.get_SortField("Tank Status");
							WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
							field_Sort.click();
							objLgr.info("Sorting action is performed on the tank status column in " +field_Sort.getAttribute("aria-sort") + " order.");
							//js.executeScript("arguments[0].scrollIntoView();",atms.get_RowData(tbl_ATMSDetail).get(0).findElement(atms.get_fieldData()));
							List<WebElement> row_Header = atms.get_RowHeader(tbl_ATMSDetail);
							List<WebElement> row_Data = atms.get_RowData(tbl_ATMSDetail);
							Hashtable<String, String> firstRow_LocationData = commonPageMyAccount.read_TableData(row_Header, row_Data, atms.get_fieldData(), 0);
							if(firstRow_LocationData.get("Tank Status").toString().equalsIgnoreCase(tankstatus))
								objLgr.info("Data is displayed for the selected tank status: "+tankstatus);
							else {
								isTestCasePassed=false;
								objLgr.error("Data is not displayed for the selected tank status: "+tankstatus);
							}
						}
					}

					else if (atms.get_NoDataTable(tbl_ATMSDetail) != null) 
						objLgr.info("ATMS-Details tab displays \"No Data Available\". for tank status - " + tankstatus);
					else {
						isTestCasePassed = false;
						objLgr.error("ATMS_Detail tab displays Try Again. for tank status - " + tankstatus);
					}
				}
			}
		}
		else {
			isTestCasePassed = false;
			objLgr.info("Tank status drop down button could not be located");
		}
		if (!isTestCasePassed)
			Assert.fail("Tank status drop-down list validation failed");
		else {
			objLgr.info("Successfully validated tank status drop down in ATMS-Detail tab");
			tankStatusDropdown_ATMSDetail.click();
			list_TankStatusInitial= atms.get_DropdownList(tankStatusDropdown_ATMSDetail);
			for(WebElement option : list_TankStatusInitial) {
				if(option.getText().equalsIgnoreCase("All Tank Status"))
					option.click();
				break; 
			}
		}
	}

	@Test(enabled=false, description = "Validate search functionality of ATMS-Detail tab",dependsOnMethods= {"detailTabLoad_ATMS"}, priority=1012)
	public void detailTabSearch_ATMS() throws Exception {
		isTestCasePassed=true;
		String message = null;
		WebElement detailTab = null;
		detailTab = atms.get_DetailTab();
		List<WebElement> row_Header = atms.get_RowHeader(detailTab);
		List<WebElement> row_Data = atms.get_RowData(detailTab);
		List<Hashtable<String, String>> tableData = commonPageMyAccount.read_TableData(row_Header, row_Data, atms.get_fieldData());
		if (atms.get_SearchField()!= null) {
			String search_Txt=reusefunc.get_RandomString(tableData);
			objLgr.info("Search text is identified as - "+search_Txt);
			atms.get_SearchText().sendKeys(search_Txt);
			atms.get_SearchText().sendKeys(Keys.ENTER);
			List<WebElement> row_SearchData = atms.get_RowData(detailTab);
			List<Hashtable<String, String>> tableData_Search = commonPageMyAccount.read_TableData(row_Header, row_SearchData, atms.get_fieldData());
			objLgr.info("Total records found in ATMS Details tab after searching with text " + search_Txt + " is - "+tableData_Search.size());
			if(commonPageMyAccount.isSearchText_Present(tableData_Search, search_Txt))
				message = "All Records displayed in the table contains searched text";
			else {
				isTestCasePassed = false;
				message = "All Records displayed in the table do not contain the searched text";
			}
		}
		else {
			isTestCasePassed = false;
			message = "Search text field is not loaded succesfully";
		}
		objLgr.info(message);
		if (!isTestCasePassed)
			Assert.fail("detailTabSearch_ATMS failed - " + message);
	}


	@Test(enabled=false, description = "Validate Summary tab loaded in ATMS Details page", dependsOnMethods= {"detailTabLoad_ATMS"}, priority=1013)
	public void summaryTabLoad_ATMS() throws Exception {
		isTestCasePassed=true;
		String message = null;	
		atms.get_TabButton(atms.get_header_ATMSDetailstab().get(0)).click();
		if (atms.get_header_ATMSDetailstab() != null) {

			String tableName = null;
			if (atms.get_Table() != null) {
				tableName = atms.get_TableHeader().get(atms.get_TableHeader().size()-1).getText();
				message = tableName + " table is loaded successfully";
			}
			else if (atms.get_NoDataTable(atms.get_SummaryTab()) != null) {
				message = tableName + " table displays \"No Data Available\".";
			}
			else {
				isTestCasePassed = false;
				message = tableName + " table has not loaded successfully";
			}
		}
		objLgr.info(message);
		if (!isTestCasePassed)
			Assert.fail("summaryTabLoad_ATMS failed - " + message);
	}	

	@Test(enabled=false, description = "Validate download link is displayed in ATMS-Summary tab", dependsOnMethods= {"summaryTabLoad_ATMS"}, priority=1014)
	public void DwnldLnkVisibility_ATMSsummaryTab() throws Exception {
		isTestCasePassed=true;
		if (atms.get_SummaryTab() != null) {
			String tableName = atms.get_TableHeader().get(atms.get_TableHeader().size()-1).getText();		
			if(atms.get_lnk_Download()!=null)
				objLgr.info("Download link is displayed in "+tableName+" tab");
			else
				objLgr.info("Download link is not displayed in "+tableName+" tab");
		}
		else
		{
			objLgr.info("ATMS-Summary tab has not loaded");
		}
	}

	@Test(enabled=false, description = "Validate Sorting functionality of ATMS-Summary tab", dependsOnMethods= {"summaryTabLoad_ATMS"}, priority=1015)
	public void summaryTabSort_ATMS() throws Exception {
		isTestCasePassed=true;
		String message = "";
		String tableName = atms.get_TableHeader().get(atms.get_TableHeader().size()-1).getText();
		WebElement summaryTab = null;
		summaryTab = atms.get_SummaryTab();
		List<WebElement> row_HeaderList = atms.get_RowHeader(summaryTab);
		for (WebElement header_Field : row_HeaderList) {
			String txt_Field = header_Field.getText();
			objLgr.info("Sorting has been started for field - "+txt_Field);
			for(int i=0;i<=1;i++) {
				List<WebElement> fields_Sort = atms.get_SortField(txt_Field);
				WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
				field_Sort.click();
				List<WebElement> row_Header = atms.get_RowHeader(summaryTab);
				List<WebElement> row_Data = atms.get_RowData(summaryTab);
				List<Hashtable<String, String>> SelectedData = commonPageMyAccount.read_TableData(row_Header, row_Data, atms.get_fieldData());
				String sort_Order = field_Sort.getAttribute("aria-sort");
				if (commonPageMyAccount.sort_TableData(SelectedData, txt_Field, sort_Order))
					message = "Sorting has been validated successfully in "+tableName+" tab for field " +txt_Field+ " in " + sort_Order + "order.";
				else {
					isTestCasePassed=false;
					message = "Sorting has been failed in "+ tableName +" tab for field " +txt_Field+ " in " + sort_Order + "order.";
				}
				objLgr.info(message);
			}
		}
		if (!isTestCasePassed)
			Assert.fail("summaryTabSort_ATMS failed -" +message);
	}

	@Test(enabled=false, description = "Validate Locations drop-down in ATMS-Summary Table", dependsOnMethods= {"summaryTabLoad_ATMS"}, priority=1016)
	public void locationsdropDown_ATMSSummaryTab() throws Exception {
		isTestCasePassed=true;
		String location = null;
		WebElement tbl_ATMSSummary = null;
		List<WebElement> list_LocationsInitial = null;
		WebElement locationsDropdown_ATMSSummary = atms.get_LocationDropdown();
		if (locationsDropdown_ATMSSummary != null) {
			locationsDropdown_ATMSSummary.click();
			tbl_ATMSSummary = atms.get_SummaryTab();
			list_LocationsInitial = atms.get_DropdownList(locationsDropdown_ATMSSummary);
			locationsDropdown_ATMSSummary.click();
			for (int i = 0; i < list_LocationsInitial.size(); i++) {
				locationsDropdown_ATMSSummary.click();
				List<WebElement> list_Locations = atms.get_DropdownList(locationsDropdown_ATMSSummary);
				location = list_Locations.get(i).getText();
				js.executeScript("arguments[0].scrollIntoView();",list_Locations.get(i));
				list_Locations.get(i).click();
				if(!location.equalsIgnoreCase("All Locations")) {
					objLgr.info(location + " is selected in Locations drop-down of ATMS-Summary Tab");
					List<WebElement> data_Table = atms.get_Table();
					if(data_Table.size() > 1) {
						objLgr.info("ATMS-Summary tab is loaded successfully for selected location : "+location);
						for(int j=0; j<=1; j++) {
							List<WebElement> fields_Sort = atms.get_SortField("Location");
							WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
							field_Sort.click();
							objLgr.info("Sorting action is performed on the location column in " +field_Sort.getAttribute("aria-sort") + " order.");
							List<WebElement> row_Header = atms.get_RowHeader(tbl_ATMSSummary);
							List<WebElement> row_Data = atms.get_RowData(tbl_ATMSSummary);
							Hashtable<String, String> firstRow_LocationData = commonPageMyAccount.read_TableData(row_Header, row_Data, atms.get_fieldData(), 0);
							if(firstRow_LocationData.get("Location").toString().equalsIgnoreCase(location))
								objLgr.info("Data is displayed for the selected location: "+location);
							else {
								isTestCasePassed=false;
								objLgr.error("Data is not displayed for the selected location: "+location);
							}
						}
					}

					else if (atms.get_NoDataTable(tbl_ATMSSummary) != null) 
						objLgr.info("ATMS-Sumary tab displays \"No Data Available\". for location - " + location);
					else {
						isTestCasePassed = false;
						objLgr.error("ATMS-Summary tab displays Try Again. for location - " + location);
					}
				}
			}
		}
		else {
			isTestCasePassed = false;
			objLgr.info("locations drop down button could not be located");
		}
		if (!isTestCasePassed)
			Assert.fail("locations drop-down list validation failed");
		else {
			objLgr.info("Successfully validated location drop down in ATMS-Summary tab");
			locationsDropdown_ATMSSummary.click();
			list_LocationsInitial= atms.get_DropdownList(locationsDropdown_ATMSSummary);
			for(WebElement option : list_LocationsInitial) {
				if(option.getText().equalsIgnoreCase("All Locations"))
					option.click();
				break; 
			}
		}
	}

	@Test(enabled=false, description = "Validate Products drop-down in ATMS-Summary Table", dependsOnMethods= {"summaryTabLoad_ATMS"}, priority=1017)
	public void productsdropDown_ATMSSummaryTab() throws Exception {
		isTestCasePassed=true;
		String product = null;
		WebElement tbl_ATMSSummary = null;
		List<WebElement> list_ProductsInitial = null;
		WebElement productsDropdown_ATMSSummary = atms.get_ProductDropdown();
		if (productsDropdown_ATMSSummary != null) {
			productsDropdown_ATMSSummary.click();
			tbl_ATMSSummary = atms.get_SummaryTab();
			list_ProductsInitial = atms.get_DropdownList(productsDropdown_ATMSSummary);
			productsDropdown_ATMSSummary.click();
			for (int i = 0; i < list_ProductsInitial.size(); i++) {
				productsDropdown_ATMSSummary.click();
				List<WebElement> list_Products = atms.get_DropdownList(productsDropdown_ATMSSummary);
				product = list_Products.get(i).getText();
				js.executeScript("arguments[0].scrollIntoView();",list_Products.get(i));
				list_Products.get(i).click();
				if(!product.equalsIgnoreCase("All Products")) {
					objLgr.info(product + " is selected in products drop-down of ATMS-Summary Tab");
					List<WebElement> data_Table = atms.get_Table();
					if(data_Table.size() > 1) {
						objLgr.info("ATMS-Summary tab is loaded successfully for selected product : "+product);
						for(int j=0; j<=1; j++) {
							List<WebElement> fields_Sort = atms.get_SortField("Product Description");
							WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
							field_Sort.click();
							objLgr.info("Sorting action is performed on the product description column in " +field_Sort.getAttribute("aria-sort") + " order.");
							List<WebElement> row_Header = atms.get_RowHeader(tbl_ATMSSummary);
							List<WebElement> row_Data = atms.get_RowData(tbl_ATMSSummary);
							Hashtable<String, String> firstRow_LocationData = commonPageMyAccount.read_TableData(row_Header, row_Data, atms.get_fieldData(), 0);
							if(firstRow_LocationData.get("Product Description").toString().equalsIgnoreCase(product))
								objLgr.info("Data is displayed for the selected product: "+product);
							else {
								isTestCasePassed=false;
								objLgr.error("Data is not displayed for the selected product: "+product);
							}
						}
					}

					else if (atms.get_NoDataTable(tbl_ATMSSummary) != null) 
						objLgr.info("ATMS-Summary tab displays \"No Data Available\". for product - " + product);
					else {
						isTestCasePassed = false;
						objLgr.error("ATMS-Summary tab displays Try Again. for product - " + product);
					}
				}
			}
		}
		else {
			isTestCasePassed = false;
			objLgr.info("Products drop down button could not be located");
		}
		if (!isTestCasePassed)
			Assert.fail("Products drop-down list validation failed");
		else {
			objLgr.info("Successfully validated products drop down in ATMS-Summary tab");
			productsDropdown_ATMSSummary.click();
			list_ProductsInitial= atms.get_DropdownList(productsDropdown_ATMSSummary);
			for(WebElement option : list_ProductsInitial) {
				if(option.getText().equalsIgnoreCase("All Products"))
					option.click();
				break; 
			}
		}
	}


	@Test(enabled=false, description = "Validate search functionality of ATMS-Summary tab",dependsOnMethods= {"summaryTabLoad_ATMS"}, priority=1018)
	public void summaryTabSearch_ATMS() throws Exception {
		isTestCasePassed=true;
		String message = null;
		WebElement summaryTab = null;
		summaryTab = atms.get_SummaryTab();
		List<WebElement> row_Header = atms.get_RowHeader(summaryTab);
		List<WebElement> row_Data = atms.get_RowData(summaryTab);
		List<Hashtable<String, String>> tableData = commonPageMyAccount.read_TableData(row_Header, row_Data, atms.get_fieldData());
		if (atms.get_SearchField()!= null) {
			String search_Txt=reusefunc.get_RandomString(tableData);
			objLgr.info("Search text is identified as - "+search_Txt);
			atms.get_SearchText().sendKeys(search_Txt);
			atms.get_SearchText().sendKeys(Keys.ENTER);
			List<WebElement> row_SearchData = atms.get_RowData(summaryTab);
			List<Hashtable<String, String>> tableData_Search = commonPageMyAccount.read_TableData(row_Header, row_SearchData, atms.get_fieldData());
			objLgr.info("Total records found in ATMS Summary tab after searching with text " + search_Txt + " is - "+tableData_Search.size());
			if(commonPageMyAccount.isSearchText_Present(tableData_Search, search_Txt))
				message = "All Records displayed in the table contains searched text";
			else {
				isTestCasePassed = false;
				message = "All Records displayed in the table do not contain the searched text";
			}
		}
		else {
			isTestCasePassed = false;
			message = "Search text field is not loaded succesfully";
		}
		objLgr.info(message);
		if (!isTestCasePassed)
			Assert.fail("summaryTabSearch_ATMS failed - " + message);
	}

	@Test(enabled=false,description = "Validate that back button works from ATMS Details page", dependsOnMethods= {"summaryTabSearch_ATMS"}, priority=1019)
	public void back_ATMSdtls() throws Exception {
		isTestCasePassed=true;
		String message = null;
		atms.getBackArrowoption().click();
		if (atms.get_AvailableSpace() != null)
			message = "ATMS page loaded after clicking on back button from ATMS Details page";
		else {
			isTestCasePassed = false;
			message = "ATMS page failed to load after clicking on back button from ATMS Details page";
		}
		objLgr.info(message);
		if (!isTestCasePassed)
			Assert.fail("back_ATMSdtls failed." + message);
	}

	@Test(enabled=false,description = "Validating if Product Summary widget is loaded successfully in ATMS page", dependsOnMethods= {"back_ATMSdtls"}, priority=1020)
	public void productSummaryWidgetLoad_ATMS() throws Exception {
		if(lob_selection.equalsIgnoreCase("Crop Protection"))
		{
			validate_LogOut();
			captureBrowserConsoleLogs();
			launchMyAccountHome();
			linkVisibility_Dashboard();
			select_LineOfBusiness();
			Thread.sleep(1000);
			leftNavigation_ATMS();
			isTestCasePassed=true;
			String message = null;
			widgetText = null;
			productSummaryWidgetHeader = atms.get_ProductSummary();
			if (productSummaryWidgetHeader != null) {
				js.executeScript("arguments[0].scrollIntoView();",productSummaryWidgetHeader);
				if (atms.get_widgetList(productSummaryWidgetHeader) != null)
					message = "Product Summary widget has been loaded succesfully";
				else if (atms.get_NoDataWidget(productSummaryWidgetHeader) != null) {
					message = "Product Summary widget displays \"No Data Available\".";
					widgetText = "No Data available";
				}
				else if (atms.get_TryAgain(productSummaryWidgetHeader) != null) {
					isTestCasePassed = false;
					message = "Product Summary widget displays Try Again.";
					widgetText = "Try Again";
				}
				objLgr.info(message);
			}	
			else
				isTestCasePassed = false;
			if (!isTestCasePassed)
				Assert.fail("productSummaryWidgetLoad_ATMS failed. " + message);
		}
		else
		{
			objLgr.info("ATMS does not apply to"+userType+"/"+lob_selection);
			throw new SkipException("ATMS does not apply to"+userType+"/"+lob_selection);
		}
	}

	@Test(enabled=false, description = "Check visiblility of VIEW DATA link of Product Summary widget in ATMS page", dependsOnMethods= {"productSummaryWidgetLoad_ATMS"}, priority=1021)
	public void visibilityViewData_ProductSummary() throws Exception {
		isTestCasePassed=true;
		String message = null;
		if(widgetText == null) {
			productSummaryWidgetHeader = atms.get_ProductSummary();
			if (atms.get_ViewMoreLink(productSummaryWidgetHeader) != null)
				message = "View More link is displayed in Product Summary widget";
			else {
				isTestCasePassed = false;
				message = "View More link is not available in Product Summary widget";
			}
			objLgr.info(message);
		}
		else if(widgetText.equalsIgnoreCase("No Data Available")) {
			objLgr.info("Product Summary widget displays \"No Data Available\".");
			throw new SkipException("Product Summary widget has no data to display for the SAPId :"+userSAPAccountID);
		}
		if (!isTestCasePassed)
			Assert.fail("visibilityViewData_ProductSummary failed - " + message);
	}

	@Test(enabled=false, description = "Validate ATMS - View Data table loaded successfully on clicking View Data link on Product Summary widget", dependsOnMethods= {"visibilityViewData_ProductSummary"}, priority=1022)
	public void viewData_ATMSPrdctSmry() throws Exception {
		isTestCasePassed=true;
		atms.get_ViewMoreLink(productSummaryWidgetHeader).click();
		if (atms.get_TableHeader() != null) {
			objLgr.info("ATMS Details page loaded successfully");
			//reusefunc.select_Loction(locationNode_IRD,aggregate_Data);
		}
		else
			isTestCasePassed=false;
		if (!isTestCasePassed)
			Assert.fail("ATMS Details page not loaded succesfully");
		//		verifyPageWithApplitools(eyes);
	}

	@Test(enabled=false,description = "Validating if Tank Status widget is loaded successfully in ATMS page", dependsOnMethods= {"viewData_ATMSPrdctSmry"}, priority=1023)
	public void tankStatusWidgetLoad_ATMS() throws Exception {
		if(lob_selection.equalsIgnoreCase("Crop Protection"))
		{
			validate_LogOut();
			captureBrowserConsoleLogs();
			launchMyAccountHome();
			linkVisibility_Dashboard();
			select_LineOfBusiness();
			Thread.sleep(1000);
			leftNavigation_ATMS();
			isTestCasePassed=true;
			String message = null;
			widgetText = null;
			tankStatusWidgetHeader = atms.get_TankStatus();
			if (tankStatusWidgetHeader != null) {
				js.executeScript("arguments[0].scrollIntoView();",tankStatusWidgetHeader);
				if (atms.get_widgetList(tankStatusWidgetHeader) != null)
					message = "Tank Status widget has been loaded succesfully";
				else if (atms.get_NoDataWidget(tankStatusWidgetHeader) != null) {
					message = "Tank Status widget displays \"No Data Available\".";
					widgetText = "No Data available";
				}
				else if (atms.get_TryAgain(tankStatusWidgetHeader) != null) {
					isTestCasePassed = false;
					message = "Tank Status widget displays Try Again.";
					widgetText = "Try Again";
				}
				objLgr.info(message);
			}	
			else
				isTestCasePassed = false;
			if (!isTestCasePassed)
				Assert.fail("tankStatusWidgetLoad_ATMS failed. " + message);
		}
		else
		{
			objLgr.info("ATMS does not apply to"+userType+"/"+lob_selection);
			throw new SkipException("ATMS does not apply to"+userType+"/"+lob_selection);
		}
	}

	@Test(enabled=false, description = "Check visiblility of VIEW DATA link of Tank Status widget in ATMS page", dependsOnMethods= {"tankStatusWidgetLoad_ATMS"}, priority=1024)
	public void visibilityViewData_TankStatus() throws Exception {
		isTestCasePassed=true;
		String message = null;
		if(widgetText == null) {
			tankStatusWidgetHeader = atms.get_TankStatus();
			if (atms.get_ViewMoreLink(tankStatusWidgetHeader) != null)
				message = "View More link is displayed in Tank Status widget";
			else {
				isTestCasePassed = false;
				message = "View More link is not available in Tank Status widget";
			}
			objLgr.info(message);
		}
		else if(widgetText.equalsIgnoreCase("No Data Available")) {
			objLgr.info("Tank Status widget displays \"No Data Available\".");
			throw new SkipException("Tank Status widget has no data to display for the SAPId :"+userSAPAccountID);
		}
		if (!isTestCasePassed)
			Assert.fail("visibilityViewData_TankStatus failed - " + message);
	}

	@Test(enabled=false, description = "Validate ATMS - View Data table loaded successfully on clicking View Data link on Tank Status widget", dependsOnMethods= {"visibilityViewData_TankStatus"}, priority=1025)
	public void viewData_ATMSTnkSts() throws Exception {
		isTestCasePassed=true;
		atms.get_ViewMoreLink(tankStatusWidgetHeader).click();
		if (atms.get_TableHeader() != null) {
			objLgr.info("ATMS Details page loaded successfully");
			//reusefunc.select_Loction(locationNode_IRD,aggregate_Data);
		}
		else
			isTestCasePassed=false;
		if (!isTestCasePassed)
			Assert.fail("ATMS Details page not loaded succesfully");
		//		verifyPageWithApplitools(eyes);
	}


	@Test(enabled=false, description = "Validate ATMS-Tank Readings table loaded on clicking a random dealer location from ATMS-Detail tab", dependsOnMethods= {"viewData_ATMSTnkSts"}, priority=1026)
	public void viewData_ATMSTnkRdngs() throws Exception {
		if(lob_selection.equalsIgnoreCase("Crop Protection"))
		{
			validate_LogOut();
			captureBrowserConsoleLogs();
			launchMyAccountHome();
			linkVisibility_Dashboard();
			select_LineOfBusiness();
			Thread.sleep(1000);
			leftNavigation_ATMS();
			visibilityViewData_availableSpaceATMS();
			viewData_ATMSAvlblSpc();
			isTestCasePassed=true;
			String selectedlocation=null;
			int i = reusefunc.get_RandomInteger(1, 10, "1");
			if(i==0)
			{
				i = reusefunc.get_RandomInteger(1, 10, "1");
			}
			objLgr.info("Random number generated is:"+i);
			objLgr.info("Clicking on the dealer location hyperlink located at " +i+"th row");
			selectedlocation = atms.get_lctnname_ATMSDetailstab(i).getText();
			objLgr.info("The selected Dealer location is: "+selectedlocation);
			atms.get_lctnlnk_ATMSDetailstab(i).click();
			Thread.sleep(500);
			if (atms.get_TableHeader() != null) {
				objLgr.info("ATMS-Tank Readings table is loaded successfully");
			}
			else
				isTestCasePassed=false;
			if (!isTestCasePassed)
				Assert.fail("ATMS-Tank Readings table is not loaded successfully");
			//			verifyPageWithApplitools(eyes);
		}

		else
		{
			objLgr.info("ATMS does not apply to"+userType+"/"+lob_selection);
			throw new SkipException("ATMS does not apply to"+userType+"/"+lob_selection);
		}

	}


	@Test(enabled=false, description = "Validate Sorting functionality of ATMS-Tank Readings table", dependsOnMethods= {"viewData_ATMSTnkRdngs"}, priority=1027)
	public void TankReadingsSort_ATMS() throws Exception {
		isTestCasePassed=true;
		String message = "";
		String tableName = atms.get_TableHeader().get(atms.get_TableHeader().size()-1).getText();
		WebElement tnkRdngsTbl = null;
		tnkRdngsTbl = atms.get_tankReadingsTbl();
		List<WebElement> row_HeaderList = atms.get_RowHeader(tnkRdngsTbl);
		for (WebElement header_Field : row_HeaderList) {
			String txt_Field = header_Field.getText();
			objLgr.info("Sorting has been started for field - "+txt_Field);
			for(int i=0;i<=1;i++) {
				List<WebElement> fields_Sort = atms.get_SortField(txt_Field);
				WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
				field_Sort.click();
				List<WebElement> row_Header = atms.get_RowHeader(tnkRdngsTbl);
				List<WebElement> row_Data = atms.get_RowData(tnkRdngsTbl);
				List<Hashtable<String, String>> SelectedData = commonPageMyAccount.read_TableData(row_Header, row_Data, atms.get_fieldData());
				String sort_Order = field_Sort.getAttribute("aria-sort");
				if (commonPageMyAccount.sort_TableData(SelectedData, txt_Field, sort_Order))
					message = "Sorting has been validated successfully in "+tableName+" tab for field " +txt_Field+ " in " + sort_Order + "order.";
				else {
					isTestCasePassed=false;
					message = "Sorting has been failed in "+ tableName +" tab for field " +txt_Field+ " in " + sort_Order + "order.";
				}
				objLgr.info(message);
			}
		}
		if (!isTestCasePassed)
			Assert.fail("TankReadingsSort_ATMS failed -" +message);
	}

	@Test(enabled=false, description = "Validate download link is displayed in ATMS-Tank Readings table", dependsOnMethods= {"viewData_ATMSTnkRdngs"}, priority=1028)
	public void DwnldLnkVisibility_ATMSTankReadings() throws Exception {
		isTestCasePassed=true;
		if (atms.get_tankReadingsTbl() != null) {
			String tableName = atms.get_TableHeader().get(atms.get_TableHeader().size()-1).getText();		
			if(atms.get_lnk_Download()!=null)
				objLgr.info("Download link is displayed in "+tableName+" table");
			else
				objLgr.info("Download link is not displayed in "+tableName+" table");
		}
		else
		{
			objLgr.info("Tank Readings table has not loaded");
		}
	}

	@Test(enabled=false,description = "Validate that back button works from ATMS-Tank Reading page", dependsOnMethods= {"viewData_ATMSTnkRdngs"}, priority=1029)
	public void back_ATMSTnkRdngs() throws Exception {
		isTestCasePassed=true;
		String message = null;
		atms.getBackArrowoption().click();
		if (atms.get_DetailTab() != null)
			message = "ATMS-Detail tab is loaded after clicking on back button from ATMS Tank Readings page";
		else {
			isTestCasePassed = false;
			message = "ATMS-Detail tab is not loaded after clicking on back button from ATMS Tank Readings page";
		}
		objLgr.info(message);
		if (!isTestCasePassed)
			Assert.fail("back_ATMSTnkRdngs failed." + message);
	}

	////-----------------------------------------------------ORDER DETAILS TEST CASES-----------------------------------------------------////

	@Test(enabled=false, description = "To check visibility of Order Details link in left navigation panel", dependsOnMethods= {"back_ATMSTnkRdngs"},priority=2000, alwaysRun=true) 
	public void linkVisibility_OrdDtls() throws Exception {
		isTestCasePassed=true;
		validate_LogOut();
		captureBrowserConsoleLogs();
		launchMyAccountHome();
		linkVisibility_Dashboard();
		if(userType.equalsIgnoreCase("NB Dealer"))
		{
			//			select_LineOfBusiness();
			Thread.sleep(1000);
			lnk.get_OrdersDropDown("Orders").click();
			objLgr.info("Drop-down button in Orders link is clicked ");
			if(lnk.get_linkOrderDetails() != null)
				objLgr.info("Order Details link is displayed in Left Navigation panel");
			else
				isTestCasePassed=false;
			if (!isTestCasePassed)
				Assert.fail("Order Details link not displayed in left navigation panel");
		}
		else 
		{
			objLgr.info("Order Details validation is not applicable to Channel Seedsman; skipping the test case");
			throw new SkipException("Order Details validation is not applicable to Channel Seedsman");
		}
	}

	@Test(enabled=false, description = "Validate Order Details page loaded successfully", dependsOnMethods= {"linkVisibility_OrdDtls"}, priority=2001)
	public void leftNavigation_OrdDtls() throws Exception {
		isTestCasePassed=true;
		lnk.get_linkOrderDetails().click();
		if (ordDtls.get_TableHeader() != null) {
			objLgr.info("Orders page loaded successfully");
		}
		else
			isTestCasePassed=false;
		if (!isTestCasePassed)
			Assert.fail("Orders page not loaded succesfully");
		verifyPageWithApplitools(eyes);
	}

	@Test(enabled=false, description = "Validate Summary table/Locations table visibility in Order Details page", dependsOnMethods= {"leftNavigation_OrdDtls"}, priority=2002)
	public void summarytblLoad_OrdDtls() throws Exception {
		List<WebElement> header = null;
		if(lob_selection.equalsIgnoreCase("Seed")) {
			header = ordDtls.get_header_OrderDetailstab();
			isTestCasePassed=true;
			String message = null;		
			if (header!= null) {
				String tableName = null;
				if (ordDtls.get_Table() != null) {
					tableName = ordDtls.get_TableHeader().get(ordDtls.get_TableHeader().size()-1).getText();
					message = tableName + " table is loaded successfully";
				}
				else if (ordDtls.get_NoDataTable(ordDtls.get_SummaryShpStatTable()) != null) {
					message = tableName + " table displays \"No Data Available\".";
				}
				else {
					isTestCasePassed = false;
					message = tableName + " table has not loaded successfully";
				}
			}
			objLgr.info(message);
			if (!isTestCasePassed)
				Assert.fail("summary_OrdDtls failed - " + message);
		}
		else {
			objLgr.info("Order Details Summary validation is not applicable to selected LOB :"+lob_selection+"; skipping the test case");
			throw new SkipException("Order Details Summary validation is not applicable to is not applicable to selected LOB"+lob_selection);
		}
	}

	@Test(enabled=false, description = "Validate Filter functionality validation of Order Details Summary page", dependsOnMethods= {"summarytblLoad_OrdDtls"}, priority=2003)
	public void summaryFilter_OrdDtls() throws Exception {
		isTestCasePassed=true;
		String message = null;
		List<WebElement> fields_Sort=null;
		WebElement ordrsTbl = null;
		for (WebElement filterChip:ordDtls.get_FilterChips()) {
			filterChip.click();
			WebElement activeFilterChip = ordDtls.get_ActivateFilterChips().get(0);
			String chipText=activeFilterChip.findElement(ordDtls.get_Chiptext()).getText().toString();
			objLgr.info("FilterChip :"+ chipText+" is selected");
			for(int i=0;i<=1;i++) {
				ordrsTbl = ordDtls.get_SummaryShpStatTable();
				List<WebElement> row_Header = ordDtls.get_RowHeader(ordrsTbl);
				List<WebElement> row_Data = ordDtls.get_RowData(ordrsTbl);
				fields_Sort = ordDtls.get_SortField("Crop");
				WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
				field_Sort.click();
				objLgr.info("Sorting action is performed on the Crop column in " +field_Sort.getAttribute("aria-sort") + " order.");
				Hashtable<String, String> firstRow_CropData = commonPageMyAccount.read_TableData(row_Header, row_Data, ordDtls.get_fieldData(), 0);
				if(firstRow_CropData.get("Crop").toString().equalsIgnoreCase(chipText))
					message = "Data is displayed for the selected crop: "+chipText;
				else {
					isTestCasePassed=false;
					message = "Data is not displayed for the selected crop: "+chipText;
				}
				objLgr.info(message);
			}
			ordDtls.get_ActivateFilterChips().get(0).click();
		}
		objLgr.info("Filter functionality is successfully tested for Summary tab Order Details table");
		if (!isTestCasePassed) {
			Assert.fail("filter_OrdDtls failed - "+message);
		}
	}

	@Test(enabled=false, description = "Validate Sorting functionality validation of Order Details Summary page", dependsOnMethods= {"summarytblLoad_OrdDtls"}, priority=2004)
	public void summarySort_OrdDtls() throws Exception {
		isTestCasePassed=true;
		String message = "";
		String tableName = ordDtls.get_TableHeader().get(ordDtls.get_TableHeader().size()-1).getText();
		WebElement ordrsTbl = null;
		ordrsTbl = ordDtls.get_SummaryShpStatTable();
		List<WebElement> row_HeaderList = ordDtls.get_RowHeader(ordrsTbl);
		for (WebElement header_Field : row_HeaderList) {
			String txt_Field = header_Field.getText();
			objLgr.info("Sorting has been started for field - "+txt_Field);
			for(int i=0;i<=1;i++) {
				List<WebElement> fields_Sort = ordDtls.get_SortField(txt_Field);
				WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
				field_Sort.click();
				List<WebElement> row_Header = ordDtls.get_RowHeader(ordrsTbl);
				List<WebElement> row_Data = ordDtls.get_RowData(ordrsTbl);
				List<Hashtable<String, String>> SelectedCropData = commonPageMyAccount.read_TableData(row_Header, row_Data, ordDtls.get_fieldData());
				String sort_Order = field_Sort.getAttribute("aria-sort");
				if (commonPageMyAccount.sort_TableData(SelectedCropData, txt_Field, sort_Order))
					message = "Sorting has been validated successfully in "+tableName+" tab for field " +txt_Field+ " in " + sort_Order + "order.";
				else {
					isTestCasePassed=false;
					message = "Sorting has been failed in "+ tableName +" tab for field " +txt_Field+ " in " + sort_Order + "order.";
				}
				objLgr.info(message);
			}
		}
		if (!isTestCasePassed)
			Assert.fail("sort_OrdDtls failed -" +message);
	}

	@Test(enabled=false, description = "Validate search functionality of Order Details Summary page",dependsOnMethods= {"summarytblLoad_OrdDtls"}, priority=2005)
	public void summarySearch_OrdDtls() throws Exception {
		isTestCasePassed=true;
		String message = null;
		WebElement ordrsTbl = null;
		ordrsTbl = ordDtls.get_SummaryShpStatTable();
		List<WebElement> row_Header = ordDtls.get_RowHeader(ordrsTbl);
		List<WebElement> row_Data = ordDtls.get_RowData(ordrsTbl);
		List<Hashtable<String, String>> tableData = commonPageMyAccount.read_TableData(row_Header, row_Data, ordDtls.get_fieldData());
		if (ordDtls.get_SearchField()!= null) {
			String search_Txt=reusefunc.get_RandomString(tableData);
			objLgr.info("Search text is identified as - "+search_Txt);
			ordDtls.get_SearchText().sendKeys(search_Txt);
			ordDtls.get_SearchText().sendKeys(Keys.ENTER);
			List<WebElement> row_SearchData = ordDtls.get_RowData(ordrsTbl);
			List<Hashtable<String, String>> tableData_Search = commonPageMyAccount.read_TableData(row_Header, row_SearchData, ordDtls.get_fieldData());
			objLgr.info("Total records found in Order Details table after searching with text " + search_Txt + " is - "+tableData_Search.size());
			if(commonPageMyAccount.isSearchText_Present(tableData_Search, search_Txt))
				message = "All Records displayed in the table contains searched text";
			else {
				isTestCasePassed = false;
				message = "All Records displayed in the table do not contain the searched text";
			}
		}
		else {
			isTestCasePassed = false;
			message = "Search text field is not loaded succesfully";
		}
		objLgr.info(message);
		if (!isTestCasePassed)
			Assert.fail("search_OrdDtls failed - " + message);
	}

	@Test(enabled=false, description = "Validate Trait tab loaded in Order Details page", dependsOnMethods= {"summarytblLoad_OrdDtls"}, priority=2006)
	public void traittblLoad_OrdDtls() throws Exception {
		isTestCasePassed=true;
		String message = null;
		ordDtls.get_TabButton(ordDtls.get_header_OrderDetailstab().get(1)).click();
		String tableName = null;
		if (ordDtls.get_Table() != null) {
			tableName = ordDtls.get_TableHeader().get(ordDtls.get_TableHeader().size()-1).getText();
			message = tableName + " table is loaded successfully";
		} else if (ordDtls.get_NoDataTable(ordDtls.get_TraitShpStatTable()) != null) {
			isTestCasePassed = false;
			message = tableName + " table displays \"No Data Available\".";
		} else {
			isTestCasePassed = false;
			message = tableName + " table has not loaded successfully";
		}
		objLgr.info(message);
		if (!isTestCasePassed)
			Assert.fail("traittblLoad_OrdDtls failed." + message);
	}

	@Test(enabled=false,description = "Validate Filter functionality validation of Order Details Trait tab", dependsOnMethods= {"traittblLoad_OrdDtls"}, priority=2007)
	public void traitFilter_OrdDtls() throws Exception {
		isTestCasePassed=true;
		for (WebElement filterChip:ordDtls.get_FilterChips()) {
			filterChip.click();
			WebElement activeFilterChip = ordDtls.get_ActivateFilterChips().get(0);
			String chipText=activeFilterChip.findElement(ordDtls.get_Chiptext()).getText().toString();
			objLgr.info("FilterChip :"+ chipText+" is selected");
			for(int i=0;i<=1;i++) {
				List<WebElement> fields_Sort = ordDtls.get_SortField("Crop");
				WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
				field_Sort.click();
				objLgr.info("Sorting action is performed on the Crop column in " +field_Sort.getAttribute("aria-sort") + " order.");
				WebElement tbl_Trait = ordDtls.get_TraitShpStatTable();
				List<WebElement> row_Header = ordDtls.get_RowHeader(tbl_Trait);
				List<WebElement> row_Data = ordDtls.get_RowData(tbl_Trait);
				Hashtable<String, String> firstRow_CropData = commonPageMyAccount.read_TableData(row_Header, row_Data, ordDtls.get_fieldData(), 0);
				if(firstRow_CropData.get("Crop").toString().equalsIgnoreCase(chipText))
					objLgr.info("Data is displayed for the selected crop: "+chipText);
				else {
					isTestCasePassed=false;
					objLgr.info("Data is not displayed for the selected crop: "+chipText);
				}
			}
			ordDtls.get_ActivateFilterChips().get(0).click();
		}
		if (!isTestCasePassed)
			Assert.fail("traitFilter_OrdDtls has not been executed successfully");
	}

	@Test(enabled=false, description = "Validate Sorting functionality validation of Order Details Trait tab", dependsOnMethods= {"traittblLoad_OrdDtls"}, priority=2008)
	public void traitSort_OrdDtls() throws Exception {
		isTestCasePassed=true;
		String message = "";
		String tableName = ordDtls.get_TableHeader().get(ordDtls.get_TableHeader().size()-1).getText();
		WebElement tbl_Trait = ordDtls.get_TraitShpStatTable();
		List<WebElement> row_HeaderList = ordDtls.get_RowHeader(tbl_Trait);
		for (WebElement header_Field : row_HeaderList) {
			String txt_Field = header_Field.getText();
			objLgr.info("Sorting has been started for field - "+txt_Field);
			for(int i=0;i<=1;i++) {
				List<WebElement> fields_Sort = ordDtls.get_SortField(txt_Field);
				WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
				field_Sort.click();
				List<WebElement> row_Header = ordDtls.get_RowHeader(tbl_Trait);
				List<WebElement> row_Data = ordDtls.get_RowData(tbl_Trait);
				List<Hashtable<String, String>> SelectedCropData = commonPageMyAccount.read_TableData(row_Header, row_Data, ordDtls.get_fieldData());
				String sort_Order = field_Sort.getAttribute("aria-sort");
				if (commonPageMyAccount.sort_TableData(SelectedCropData, txt_Field, sort_Order))
					message = "Sorting has been validated successfully in "+tableName+" tab for field " +txt_Field+ " in " + sort_Order + "order.";
				else {
					isTestCasePassed=false;
					message = "Sorting has been failed in "+ tableName +" tab for field " +txt_Field+ " in " + sort_Order + "order.";
				}
				objLgr.info(message);
			}
		}
		if (!isTestCasePassed)
			Assert.fail("traitSort_OrdDtls has failed -" +message);
	}

	@Test(enabled=false, description = "Validate search functionality of Order Details Trait tab", dependsOnMethods= {"traittblLoad_OrdDtls"}, priority=2009)
	public void traitSearch_OrdDtls() throws Exception {
		isTestCasePassed=true;
		String message = null;
		WebElement tbl_Trait = ordDtls.get_TraitShpStatTable();
		List<WebElement> row_Header = ordDtls.get_RowHeader(tbl_Trait);
		List<WebElement> row_Data = ordDtls.get_RowData(tbl_Trait);
		List<Hashtable<String, String>> tableData = commonPageMyAccount.read_TableData(row_Header, row_Data, ordDtls.get_fieldData());
		if (ordDtls.get_SearchField()!= null) {
			String search_Txt=reusefunc.get_RandomString(tableData);
			objLgr.info("Search text is identified as - "+search_Txt);
			ordDtls.get_SearchText().sendKeys(search_Txt);
			ordDtls.get_SearchText().sendKeys(Keys.ENTER);
			List<WebElement> row_SearchData = ordDtls.get_RowData(tbl_Trait);
			List<Hashtable<String, String>> tableData_Search = commonPageMyAccount.read_TableData(row_Header, row_SearchData, ordDtls.get_fieldData());
			objLgr.info("Total records found in Trait table after searching with text " + search_Txt + " is - "+tableData_Search.size());
			if(commonPageMyAccount.isSearchText_Present(tableData_Search, search_Txt))
				message = "All Records displayed in the table contains searched text";
			else {
				isTestCasePassed = false;
				message = "All Records displayed in the table does not contain searched text";
			}
		}
		else {
			isTestCasePassed = false;
			message = "Search text field is not loaded succesfully";
		}
		objLgr.info(message);
		if (!isTestCasePassed)
			Assert.fail("traitSearch_OrdDtls failed - " + message);
	}

	@Test(enabled=false, description = "Validate product tab loaded in Order Details page", dependsOnMethods= {"traittblLoad_OrdDtls"}, priority=2010)
	public void producttblLoad_OrdDtls() throws Exception {
		isTestCasePassed=true;
		String message = null;
		ordDtls.get_TabButton(ordDtls.get_header_OrderDetailstab().get(2)).click();
		String tableName = null;
		if (ordDtls.get_Table() != null) {
			tableName = ordDtls.get_TableHeader().get(ordDtls.get_TableHeader().size()-1).getText();
			message = tableName + " table is loaded successfully";
		} else if (ordDtls.get_NoDataTable(ordDtls.get_ProductShpStatTable()) != null) {
			isTestCasePassed = false;
			message = tableName + " table displays \"No Data Available\".";
		} else {
			isTestCasePassed = false;
			message = tableName + " table has not loaded successfully";
		}
		objLgr.info(message);
		if (!isTestCasePassed)
			Assert.fail("producttblLoad_OrdDtls failed." + message);
	}

	@Test(enabled=false,description = "Validate Filter functionality validation of Order Details product tab", dependsOnMethods= {"producttblLoad_OrdDtls"}, priority=2011)
	public void productFilter_OrdDtls() throws Exception {
		isTestCasePassed=true;
		for (WebElement filterChip:ordDtls.get_FilterChips()) {
			filterChip.click();
			WebElement activeFilterChip = ordDtls.get_ActivateFilterChips().get(0);
			String chipText=activeFilterChip.findElement(ordDtls.get_Chiptext()).getText().toString();
			objLgr.info("FilterChip :"+ chipText+" is selected");
			for(int i=0;i<=1;i++) {
				List<WebElement> fields_Sort = ordDtls.get_SortField("Crop");
				WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
				field_Sort.click();
				objLgr.info("Sorting action is performed on the Crop column in " +field_Sort.getAttribute("aria-sort") + " order.");
				WebElement tbl_Product = ordDtls.get_ProductShpStatTable();
				List<WebElement> row_Header = ordDtls.get_RowHeader(tbl_Product);
				List<WebElement> row_Data = ordDtls.get_RowData(tbl_Product);
				Hashtable<String, String> firstRow_CropData = commonPageMyAccount.read_TableData(row_Header, row_Data, ordDtls.get_fieldData(), 0);
				if(firstRow_CropData.get("Crop").toString().equalsIgnoreCase(chipText))
					objLgr.info("Data is displayed for the selected crop: "+chipText);
				else {
					isTestCasePassed=false;
					objLgr.info("Data is not displayed for the selected crop: "+chipText);
				}
			}
			ordDtls.get_ActivateFilterChips().get(0).click();
		}
		if (!isTestCasePassed) {
			Assert.fail("productFilter_OrdDtls has not been executed successfully");
		}
	}

	@Test(enabled=false, description = "Validate Sorting functionality validation of Order Details product tab", dependsOnMethods= {"producttblLoad_OrdDtls"}, priority=2012)
	public void productSort_OrdDtls() throws Exception {
		isTestCasePassed=true;
		String message = "";
		String tableName = ordDtls.get_TableHeader().get(ordDtls.get_TableHeader().size()-1).getText();
		WebElement tbl_Product = ordDtls.get_ProductShpStatTable();
		List<WebElement> row_HeaderList = ordDtls.get_RowHeader(tbl_Product);
		for (WebElement header_Field : row_HeaderList) {
			String txt_Field = header_Field.getText();
			objLgr.info("Sorting has been started for field - "+txt_Field);
			for(int i=0;i<=1;i++) {
				List<WebElement> fields_Sort = ordDtls.get_SortField(txt_Field);
				WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
				field_Sort.click();
				List<WebElement> row_Header = ordDtls.get_RowHeader(tbl_Product);
				List<WebElement> row_Data = ordDtls.get_RowData(tbl_Product);
				List<Hashtable<String, String>> SelectedCropData = commonPageMyAccount.read_TableData(row_Header, row_Data, ordDtls.get_fieldData());
				String sort_Order = field_Sort.getAttribute("aria-sort");
				if (commonPageMyAccount.sort_TableData(SelectedCropData, txt_Field, sort_Order))
					message = "Sorting has been validated successfully in "+tableName+" tab for field " +txt_Field+ " in " + sort_Order + "order.";
				else {
					isTestCasePassed=false;
					message = "Sorting has been failed in "+ tableName +" tab for field " +txt_Field+ " in " + sort_Order + "order.";
				}
				objLgr.info(message);
			}
		}
		if (!isTestCasePassed)
			Assert.fail("productSort_OrdDtls has failed -" +message);
	}

	@Test(enabled=false, description = "Validate search functionality of Order Details product tab", dependsOnMethods= {"producttblLoad_OrdDtls"}, priority=2013)
	public void productSearch_OrdDtls() throws Exception {
		isTestCasePassed=true;
		String message = null;
		WebElement tbl_Product = ordDtls.get_ProductShpStatTable();
		List<WebElement> row_Header = ordDtls.get_RowHeader(tbl_Product);
		List<WebElement> row_Data = ordDtls.get_RowData(tbl_Product);
		List<Hashtable<String, String>> tableData = commonPageMyAccount.read_TableData(row_Header, row_Data, ordDtls.get_fieldData());
		if (ordDtls.get_SearchField()!= null) {
			String search_Txt=reusefunc.get_RandomString(tableData);
			objLgr.info("Search text is identified as - "+search_Txt);
			ordDtls.get_SearchText().sendKeys(search_Txt);
			ordDtls.get_SearchText().sendKeys(Keys.ENTER);
			List<WebElement> row_SearchData = ordDtls.get_RowData(tbl_Product);
			List<Hashtable<String, String>> tableData_Search = commonPageMyAccount.read_TableData(row_Header, row_SearchData, ordDtls.get_fieldData());
			objLgr.info("Total records found in Product table after searching with text " + search_Txt + " is - "+tableData_Search.size());
			if(commonPageMyAccount.isSearchText_Present(tableData_Search, search_Txt))
				message = "All Records displayed in the table contains searched text";
			else {
				isTestCasePassed = false;
				message = "All Records displayed in the table does not contain searched text";
			}
		}
		else {
			isTestCasePassed = false;
			message = "Search text field is not loaded succesfully";
		}
		objLgr.info(message);
		if (!isTestCasePassed)
			Assert.fail("productSearch_OrdDtls failed - " + message);
	}

	@Test(enabled=false, description = "Validate productDesc tab loaded in Order Details page", dependsOnMethods= {"producttblLoad_OrdDtls"}, priority=2014)
	public void productDesctblLoad_OrdDtls() throws Exception {
		isTestCasePassed=true;
		String message = null;
		ordDtls.get_TabButton(ordDtls.get_header_OrderDetailstab().get(3)).click();
		String tableName = null;
		if (ordDtls.get_Table() != null) {
			tableName = ordDtls.get_TableHeader().get(ordDtls.get_TableHeader().size()-1).getText();
			message = tableName + " table is loaded successfully";
		} else if (ordDtls.get_NoDataTable(ordDtls.get_ProductShpStatTable()) != null) {
			isTestCasePassed = false;
			message = tableName + " table displays \"No Data Available\".";
		} else {
			isTestCasePassed = false;
			message = tableName + " table has not loaded successfully";
		}
		objLgr.info(message);
		if (!isTestCasePassed)
			Assert.fail("productDesctblLoad_OrdDtls failed." + message);
	}

	@Test(enabled=false,description = "Validate Filter functionality validation of Order Details productDesc tab", dependsOnMethods= {"productDesctblLoad_OrdDtls"}, priority=2015)
	public void productDescFilter_OrdDtls() throws Exception {
		isTestCasePassed=true;
		for (WebElement filterChip:ordDtls.get_FilterChips()) {
			filterChip.click();
			WebElement activeFilterChip = ordDtls.get_ActivateFilterChips().get(0);
			String chipText=activeFilterChip.findElement(ordDtls.get_Chiptext()).getText().toString();
			objLgr.info("FilterChip :"+ chipText+" is selected");
			for(int i=0;i<=1;i++) {
				List<WebElement> fields_Sort = ordDtls.get_SortField("Crop");
				WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
				field_Sort.click();
				objLgr.info("Sorting action is performed on the Crop column in " +field_Sort.getAttribute("aria-sort") + " order.");
				WebElement tbl_ProductDesc = ordDtls.get_ProdDescShpStatTable();
				List<WebElement> row_Header = ordDtls.get_RowHeader(tbl_ProductDesc);
				List<WebElement> row_Data = ordDtls.get_RowData(tbl_ProductDesc);
				Hashtable<String, String> firstRow_CropData = commonPageMyAccount.read_TableData(row_Header, row_Data, ordDtls.get_fieldData(), 0);
				if(firstRow_CropData.get("Crop").toString().equalsIgnoreCase(chipText))
					objLgr.info("Data is displayed for the selected crop: "+chipText);
				else {
					isTestCasePassed=false;
					objLgr.info("Data is not displayed for the selected crop: "+chipText);
				}
			}
			ordDtls.get_ActivateFilterChips().get(0).click();
		}
		if (!isTestCasePassed) {
			Assert.fail("productDescFilter_OrdDtls has not been executed successfully");
		}
	}

	@Test(enabled=false, description = "Validate Sorting functionality validation of Order Details productDesc tab", dependsOnMethods= {"productDesctblLoad_OrdDtls"}, priority=2016)
	public void productDescSort_OrdDtls() throws Exception {
		isTestCasePassed=true;
		String message = "";
		String tableName = ordDtls.get_TableHeader().get(ordDtls.get_TableHeader().size()-1).getText();
		WebElement tbl_ProductDesc = ordDtls.get_ProdDescShpStatTable();
		List<WebElement> row_HeaderList = ordDtls.get_RowHeader(tbl_ProductDesc);
		for (WebElement header_Field : row_HeaderList) {
			String txt_Field = header_Field.getText();
			objLgr.info("Sorting has been started for field - "+txt_Field);
			for(int i=0;i<=1;i++) {
				List<WebElement> fields_Sort = ordDtls.get_SortField(txt_Field);
				WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
				field_Sort.click();
				List<WebElement> row_Header = ordDtls.get_RowHeader(tbl_ProductDesc);
				List<WebElement> row_Data = ordDtls.get_RowData(tbl_ProductDesc);
				List<Hashtable<String, String>> SelectedCropData = commonPageMyAccount.read_TableData(row_Header, row_Data, ordDtls.get_fieldData());
				String sort_Order = field_Sort.getAttribute("aria-sort");
				if (commonPageMyAccount.sort_TableData(SelectedCropData, txt_Field, sort_Order))
					message = "Sorting has been validated successfully in "+tableName+" tab for field " +txt_Field+ " in " + sort_Order + "order.";
				else {
					isTestCasePassed=false;
					message = "Sorting has been failed in "+ tableName +" tab for field " +txt_Field+ " in " + sort_Order + "order.";
				}
				objLgr.info(message);
			}
		}
		if (!isTestCasePassed)
			Assert.fail("productDescSort_OrdDtls has failed -" +message);
	}

	@Test(enabled=false, description = "Validate search functionality of Order Details productDesc tab", dependsOnMethods= {"productDesctblLoad_OrdDtls"}, priority=2017)
	public void productDescSearch_OrdDtls() throws Exception {
		isTestCasePassed=true;
		String message = null;
		WebElement tbl_ProductDesc = ordDtls.get_ProdDescShpStatTable();
		List<WebElement> row_Header = ordDtls.get_RowHeader(tbl_ProductDesc);
		List<WebElement> row_Data = ordDtls.get_RowData(tbl_ProductDesc);
		List<Hashtable<String, String>> tableData = commonPageMyAccount.read_TableData(row_Header, row_Data, ordDtls.get_fieldData());
		if (ordDtls.get_SearchField()!= null) {
			String search_Txt=reusefunc.get_RandomString(tableData);
			objLgr.info("Search text is identified as - "+search_Txt);
			ordDtls.get_SearchText().sendKeys(search_Txt);
			ordDtls.get_SearchText().sendKeys(Keys.ENTER);
			List<WebElement> row_SearchData = ordDtls.get_RowData(tbl_ProductDesc);
			List<Hashtable<String, String>> tableData_Search = commonPageMyAccount.read_TableData(row_Header, row_SearchData, ordDtls.get_fieldData());
			objLgr.info("Total records found in Product Description table after searching with text " + search_Txt + " is - "+tableData_Search.size());
			if(commonPageMyAccount.isSearchText_Present(tableData_Search, search_Txt))
				message = "All Records displayed in the table contains searched text";
			else {
				isTestCasePassed = false;
				message = "All Records displayed in the table does not contain searched text";
			}
		}
		else {
			isTestCasePassed = false;
			message = "Search text field is not loaded succesfully";
		}
		objLgr.info(message);
		if (!isTestCasePassed)
			Assert.fail("productDescSearch_OrdDtls failed - " + message);
	}

	@Test(enabled=false, description = "Validate Farmer tab loaded in Order Details page", dependsOnMethods= {"productDescSearch_OrdDtls"}, priority=2018)
	public void famertblLoad_OrdDtls() throws Exception {
		isTestCasePassed=true;
		String message = null;
		ordDtls.get_TabButton(ordDtls.get_header_OrderDetailstab().get(4)).click();
		String tableName = null;
		if (ordDtls.get_Table() != null) {
			tableName = ordDtls.get_TableHeader().get(ordDtls.get_TableHeader().size()-1).getText();
			message = tableName + " table is loaded successfully";
		} 
		else if (ordDtls.get_NoDataTable(ordDtls.get_FarmerShpStatTable()) != null) {
			isTestCasePassed = false;
			message = tableName + " table displays \"No Data Available\".";
		} 
		else {
			isTestCasePassed = false;
			message = tableName + " table has not loaded successfully";
		}
		objLgr.info(message);
		if (!isTestCasePassed)
			Assert.fail("famertblLoad_OrdDtls failed." + message);
		verifyPageWithApplitools(eyes);

	}

	@Test(enabled=false,description = "Validate Filter functionality validation of Farmer tab in Order Details", dependsOnMethods= {"famertblLoad_OrdDtls"}, priority=2019)
	public void farmerFilter_OrdDtls() throws Exception {
		if(userType.equalsIgnoreCase("NB Dealer")&&lob_selection.equalsIgnoreCase("Seed"))
		{
			isTestCasePassed=true;
			for (WebElement filterChip:ordDtls.get_FilterChips()) {
				filterChip.click();
				WebElement activeFilterChip = ordDtls.get_ActivateFilterChips().get(0);
				String chipText=activeFilterChip.findElement(ordDtls.get_Chiptext()).getText().toString();
				objLgr.info("FilterChip :"+ chipText+" is selected");
				if((ordDtls.get_NoDataAvailableTable()) != null)
				{
					objLgr.info("For selected filter chip:"+chipText+" table displays \"No Data Available\".");
				}
				else
					for(int i=0;i<=1;i++) {
						List<WebElement> fields_Sort = ordDtls.get_SortField("Crop");
						WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
						field_Sort.click();
						objLgr.info("Sorting action is performed on the Crop column in " +field_Sort.getAttribute("aria-sort") + " order.");
						WebElement tbl_Farmer = ordDtls.get_FarmerShpStatTable();
						List<WebElement> row_Header = ordDtls.get_RowHeader(tbl_Farmer);
						List<WebElement> row_Data = ordDtls.get_RowData(tbl_Farmer);
						Hashtable<String, String> firstRow_CropData = commonPageMyAccount.read_TableData(row_Header, row_Data, ordDtls.get_fieldData(), 0);
						if(firstRow_CropData.get("Crop").toString().equalsIgnoreCase(chipText))
							objLgr.info("Data is displayed for the selected crop: "+chipText);
						else {
							isTestCasePassed=false;
							objLgr.info("Data is not displayed for the selected crop: "+chipText);
						}
					}


				ordDtls.get_ActivateFilterChips().get(0).click();
			}
			if (!isTestCasePassed) {
				Assert.fail("farmerFilter_OrdDtls has not been executed successfully");
			}
		}
		objLgr.info("Farmer tab does not apply to"+lob_selection+"/"+userType+"; skipping this test case");
	}

	@Test(enabled=false, description = "Validate Sorting functionality validation of Farmer tab in Order Details", dependsOnMethods= {"famertblLoad_OrdDtls"}, priority=2020)
	public void farmerSort_OrdDtls() throws Exception {
		if(userType.equalsIgnoreCase("NB Dealer")&&lob_selection.equalsIgnoreCase("Seed"))
		{
			isTestCasePassed=true;
			String message = "";
			String tableName = ordDtls.get_TableHeader().get(ordDtls.get_TableHeader().size()-1).getText();
			WebElement tbl_Farmer = ordDtls.get_FarmerShpStatTable();
			List<WebElement> row_HeaderList = ordDtls.get_RowHeader(tbl_Farmer);
			for (WebElement header_Field : row_HeaderList) {
				String txt_Field = header_Field.getText();
				objLgr.info("Sorting has been started for field - "+txt_Field);
				for(int i=0;i<=1;i++) {
					List<WebElement> fields_Sort = ordDtls.get_SortField(txt_Field);
					WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
					field_Sort.click();
					List<WebElement> row_Header = ordDtls.get_RowHeader(tbl_Farmer);
					List<WebElement> row_Data = ordDtls.get_RowData(tbl_Farmer);
					List<Hashtable<String, String>> SelectedCropData = commonPageMyAccount.read_TableData(row_Header, row_Data, ordDtls.get_fieldData());
					String sort_Order = field_Sort.getAttribute("aria-sort");
					if (commonPageMyAccount.sort_TableData(SelectedCropData, txt_Field, sort_Order))
						message = "Sorting has been validated successfully in "+tableName+" tab for field " +txt_Field+ " in " + sort_Order + "order.";
					else {
						isTestCasePassed=false;
						message = "Sorting has been failed in "+ tableName +" tab for field " +txt_Field+ " in " + sort_Order + "order.";
					}
					objLgr.info(message);
				}
			}
			if (!isTestCasePassed)
				Assert.fail("farmerSort_OrdDtls has failed -" +message);
		}
		else
		{
			objLgr.info("Farmer tab does not apply to"+lob_selection+"/"+userType+"; skipping this test case");
		}
	}

	@Test(enabled=false, description = "Validate search functionality of Farmer tab in Order Details", dependsOnMethods= {"famertblLoad_OrdDtls"}, priority=2021)
	public void FarmerSearch_OrdDtls() throws Exception {

		isTestCasePassed=true;
		String message = null;
		WebElement tbl_Farmer = ordDtls.get_FarmerShpStatTable();
		List<WebElement> row_Header = ordDtls.get_RowHeader(tbl_Farmer);
		List<WebElement> row_Data = ordDtls.get_RowData(tbl_Farmer);
		List<Hashtable<String, String>> tableData = commonPageMyAccount.read_TableData(row_Header, row_Data, ordDtls.get_fieldData());
		if (ordDtls.get_SearchField()!= null) {
			String search_Txt=reusefunc.get_RandomString(tableData);
			objLgr.info("Search text is identified as - "+search_Txt);
			ordDtls.get_SearchText().sendKeys(search_Txt);
			ordDtls.get_SearchText().sendKeys(Keys.ENTER);
			List<WebElement> row_SearchData = ordDtls.get_RowData(tbl_Farmer);
			List<Hashtable<String, String>> tableData_Search = commonPageMyAccount.read_TableData(row_Header, row_SearchData, ordDtls.get_fieldData());
			objLgr.info("Total records found in Farmer tab after searching with text " + search_Txt + " is - "+tableData_Search.size());
			if(commonPageMyAccount.isSearchText_Present(tableData_Search, search_Txt))
				message = "All Records displayed in the table contains searched text";
			else {
				isTestCasePassed = false;
				message = "All Records displayed in the table does not contain searched text";
			}
		}
		else {
			isTestCasePassed = false;
			message = "Search text field is not loaded succesfully";
		}
		objLgr.info(message);
		if (!isTestCasePassed)
		{
			Assert.fail("FarmerSearch_OrdDtls failed - " + message);
			captureBrowserConsoleLogs();
		}
	}

	@Test(enabled=false,description = "Validate that back button works from Order Details", dependsOnMethods= {"FarmerSearch_OrdDtls"}, priority=2022)
	public void back_OrderDetails() throws Exception {
		isTestCasePassed=true;
		String message = null;
		ordDtls.getBackArrowoption().click();
		if (availableSpaceWidgetHeader != null)
			message = "";
		else {
			isTestCasePassed = false;
			message = "";
		}
		objLgr.info(message);
		if (!isTestCasePassed)
			Assert.fail("back_OrderDetails failed." + message);
	}



	//--------------------------------------Product Code LookUp Test Cases---------------------------------------//

	@Test(enabled=false, description = "Validating that Product Code LookUp page is loaded successfully when Product Code LookUp Link is clicked in left navigation.", dependsOnMethods= {"back_OrderDetails"}, priority=3000)
	public void leftNavigation_ProductCodeLookUp() throws Exception {
		isTestCasePassed=true;
		lnk.get_linkDashBoard().click();
		webdriver.get("https://lepsi-d.agro.services/myaccount/product-code-look-up");
		if(pclu.get_Table() != null)
			objLgr.info("Product Code lookup page loaded successfully");
		else
			isTestCasePassed=false;
		if (!isTestCasePassed)
			Assert.fail("Product Code lookup page loading unsuccessful");
		verifyPageWithApplitools(eyes);
	}

	@Test(enabled=false, description = "To check the visibility of Product Code Lookup Table ", dependsOnMethods= {"FarmerSearch_OrdDtls"},alwaysRun = true, priority=3001)
	public void visibility_ProductCodeLookUp() throws Exception {
		isTestCasePassed=true;
		if(userType.equalsIgnoreCase("NB Dealer")&&lob_selection.equalsIgnoreCase("Seed")) 
		{
			validate_LogOut();
			captureBrowserConsoleLogs();
			launchMyAccountHome();
			linkVisibility_Dashboard();
			webdriver.get("https://lepsi-d.agro.services/myaccount/product-code-look-up");
			if(pclu.get_Table() != null)
				objLgr.info("Product Code LookUp table is loaded successfully");
			else
				isTestCasePassed=false;
			if (!isTestCasePassed)
				Assert.fail("Product Code LookUp table is not loaded successfully");
		}
		else  {
			objLgr.info("Product code lookup testcases will not be applicable for selected lob"+lob_selection+"; skipping the test case");
			throw new SkipException("Product code lookup TestCases will not apply to selected line of business");
		}
	}

	@Test(enabled=false, description = "Validate crop drop-downs in Product Code Lookup Table", dependsOnMethods= {"visibility_ProductCodeLookUp"}, priority=3002)
	public void cropdropDown_ProductCodeLookUp() throws Exception {
		isTestCasePassed=true;
		String crop = null;
		WebElement tbl_Product = null;
		List<WebElement> list_CropsInitial = null;
		WebElement cropDropdown_ProductCodeLookUp = pclu.get_CropDropdown();
		if (cropDropdown_ProductCodeLookUp != null) {
			cropDropdown_ProductCodeLookUp.click();
			tbl_Product = pclu.get_ProductTable();
			list_CropsInitial = pclu.get_DropdownList(cropDropdown_ProductCodeLookUp);
			cropDropdown_ProductCodeLookUp.click();
			for (int i = 0; i < list_CropsInitial.size(); i++) {
				cropDropdown_ProductCodeLookUp.click();
				List<WebElement> list_Crops = pclu.get_DropdownList(cropDropdown_ProductCodeLookUp);
				crop = list_Crops.get(i).getText();
				js.executeScript("arguments[0].scrollIntoView();",list_Crops.get(i));
				list_Crops.get(i).click();
				if(!crop.equalsIgnoreCase("All Crops")) {
					objLgr.info(crop + " is selected in Crop's drop-down of Product Code Lookup Table");
					List<WebElement> data_Table = pclu.get_Table();
					if(data_Table.size() > 1) {
						objLgr.info("Product Code LookUp table is loaded successfully for selected crop : "+crop);
						for(int j=0; j<=1; j++) {
							List<WebElement> fields_Sort = pclu.get_SortField("Crop");
							WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
							field_Sort.click();
							objLgr.info("Sorting action is performed on the Crop column in " +field_Sort.getAttribute("aria-sort") + " order.");
							List<WebElement> row_Header = pclu.get_RowHeader(tbl_Product);
							List<WebElement> row_Data = pclu.get_RowData(tbl_Product);
							Hashtable<String, String> firstRow_CropData = commonPageMyAccount.read_TableData(row_Header, row_Data, pclu.get_fieldData(), 0);
							if(firstRow_CropData.get("Crop").toString().equalsIgnoreCase(crop))
								objLgr.info("Data is displayed for the selected crop: "+crop);
							else {
								isTestCasePassed=false;
								objLgr.error("Data is not displayed for the selected crop: "+crop);
							}
						}
					}

					else if (pclu.get_NoDataTable(tbl_Product) != null) 
						objLgr.info("Product Code LookUp table displays \"No Data Available\". for crop - " + crop);
					else {
						isTestCasePassed = false;
						objLgr.error("Product Code LookUp table displays Try Again. for crop - " + crop);
					}
				}
			}
		}
		else {
			isTestCasePassed = false;
			objLgr.info("Crop drop down button could not be located");
		}
		if (!isTestCasePassed)
			Assert.fail("Crop's drop-down list validation failed");
		else {
			objLgr.info("Successfully validated Crop drop down in product look up table");
			cropDropdown_ProductCodeLookUp.click();
			list_CropsInitial= pclu.get_DropdownList(cropDropdown_ProductCodeLookUp);
			for(WebElement option : list_CropsInitial) {
				if(option.getText().equalsIgnoreCase("All Crops"))
					option.click();
				break; 
			}
		}
	}

	@Test(enabled=false, description = "Validate product drop-downs in Product Code Lookup Table", dependsOnMethods= {"visibility_ProductCodeLookUp"}, priority=3003)
	public void traitdropDowns_ProductCodeLookUp() throws Exception {
		isTestCasePassed=true;
		String trait = null;
		WebElement tbl_Product = null;
		List<WebElement> list_TraitssInitial = null;
		WebElement traitDropdown_ProductCodeLookUp = pclu.get_TraitDropdown();
		if (traitDropdown_ProductCodeLookUp != null) {
			traitDropdown_ProductCodeLookUp.click();
			tbl_Product = pclu.get_ProductTable();
			list_TraitssInitial = pclu.get_DropdownList(traitDropdown_ProductCodeLookUp);
			traitDropdown_ProductCodeLookUp.click();
			for (int i = 0; i < list_TraitssInitial.size(); i++) {
				traitDropdown_ProductCodeLookUp.click();
				List<WebElement> list_Trait = pclu.get_DropdownList(traitDropdown_ProductCodeLookUp);
				trait = list_Trait.get(i).getText();
				js.executeScript("arguments[0].scrollIntoView();",list_Trait.get(i));
				list_Trait.get(i).click();
				if(!trait.equalsIgnoreCase("All Traits")) {
					objLgr.info(trait + " is selected in Trait's drop-down of Product Code Lookup Table");
					List<WebElement> data_Table = pclu.get_Table();
					if(data_Table.size() > 1) {
						objLgr.info("Product Code LookUp table is loaded successfully for selected Trait : "+trait);
						for(int j=0; j<=1; j++) {
							List<WebElement> fields_Sort = pclu.get_SortField("Trait");
							WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
							field_Sort.click();
							objLgr.info("Sorting action is performed on the Trait column in " +field_Sort.getAttribute("aria-sort") + " order.");
							List<WebElement> row_Header = pclu.get_RowHeader(tbl_Product);
							List<WebElement> row_Data = pclu.get_RowData(tbl_Product);
							Hashtable<String, String> firstRow_CropData = commonPageMyAccount.read_TableData(row_Header, row_Data, pclu.get_fieldData(), 0);
							if(firstRow_CropData.get("Trait").toString().equalsIgnoreCase(trait))
								objLgr.info("Data is displayed for the selected crop: "+trait);
							else {
								isTestCasePassed=false;
								objLgr.error("Data is not displayed for the selected crop: "+trait);
							}
						}
					}
					else if (pclu.get_NoDataTable(tbl_Product) != null) 
						objLgr.info("Product Code LookUp table displays \"No Data Available\". for Trait - " + trait);
					else {
						isTestCasePassed = false;
						objLgr.info("Product Code LookUp table displays Try Again. for trait - " + trait);
					}
				}
			}
		}
		else {
			isTestCasePassed = false;
			objLgr.info("Trait drop down button could not be located");
		}
		if (!isTestCasePassed)
			Assert.fail("Trait's drop-down list validation failed");
		else {
			objLgr.info("Successfully validated trait drop down in product look up table");
			traitDropdown_ProductCodeLookUp.click();
			list_TraitssInitial = pclu.get_DropdownList(traitDropdown_ProductCodeLookUp);
			for(WebElement option : list_TraitssInitial) {
				if(option.getText().equalsIgnoreCase("All Traits"))
					option.click();
				break;
			}
		}
	}

	@Test(enabled=false, description = "Validate Year drop-down in Product Code Lookup Table", dependsOnMethods= {"visibility_ProductCodeLookUp"}, priority=3004)
	public void yeardropDown_ProductCodeLookUp() throws Exception {
		isTestCasePassed=true;
		String year = null;
		WebElement tbl_Product = null;
		List<WebElement> list_YearsInitial = null;
		WebElement yearDropdown_ProductCodeLookUp = pclu.get_YearDropdown();
		if (yearDropdown_ProductCodeLookUp != null) {
			yearDropdown_ProductCodeLookUp.click();
			tbl_Product = pclu.get_ProductTable();
			list_YearsInitial = pclu.get_DropdownList(yearDropdown_ProductCodeLookUp);
			yearDropdown_ProductCodeLookUp.click();
			for (int i = 0; i < list_YearsInitial.size(); i++) {
				yearDropdown_ProductCodeLookUp.click();
				List<WebElement> list_Year = pclu.get_DropdownList(yearDropdown_ProductCodeLookUp);
				year = list_Year.get(i).getText();
				list_Year.get(i).click();
				objLgr.info(year + " is selected in Year's drop-down of Product Code Lookup Table");
				List<WebElement> data_Table = pclu.get_Table();
				if(data_Table.size() > 1) 
					objLgr.info("Product Code LookUp table is loaded successfully for selected year : "+year);
				else if (pclu.get_NoDataTable(tbl_Product) != null) 
					objLgr.info("Product Code LookUp table displays \"No Data Available\". for year - " + year);
				else {
					isTestCasePassed = false;
					objLgr.info("Product Code LookUp table displays Try Again. for trait - " + year);
				}
			}
		}
		else {
			isTestCasePassed = false;
			objLgr.error("Year drop down button could not be located");
		}
		if (!isTestCasePassed)
			Assert.fail("Year's drop-down list validation failed");
		else {
			objLgr.info("Successfully validated Year drop down in product look up table");
			yearDropdown_ProductCodeLookUp.click();
			list_YearsInitial = pclu.get_DropdownList(yearDropdown_ProductCodeLookUp);
			for(WebElement option : list_YearsInitial) {
				if(option.getText().equalsIgnoreCase("All Market Year"))
					option.click();
				break;
			}
		}
	}

	@Test(enabled=false, description = "Sorting functionality validation of Product Code LookUp table", dependsOnMethods= {"visibility_ProductCodeLookUp"}, priority=3005)
	public void sort_ProductCodeLookUp() throws Exception {
		isTestCasePassed=true;
		String message = null;
		WebElement tbl_ProductCodeLookUp = pclu.get_ProductTable();
		List<WebElement> row_HeaderList = pclu.get_RowHeader(tbl_ProductCodeLookUp);
		for (WebElement header_Field : row_HeaderList) {
			String txt_Field = header_Field.getText();
			objLgr.info("Sorting has been started for field - "+txt_Field);
			for(int i=0;i<=1;i++) {
				List<WebElement> fields_Sort = pclu.get_SortField(txt_Field);
				WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
				field_Sort.click();
				List<WebElement> row_Header = pclu.get_RowHeader(tbl_ProductCodeLookUp);
				List<WebElement> row_Data = pclu.get_RowData(tbl_ProductCodeLookUp);
				List<Hashtable<String, String>> SelectedfieldData = commonPageMyAccount.read_TableData(row_Header, row_Data, pclu.get_fieldData());
				String sort_Order = field_Sort.getAttribute("aria-sort");
				if (commonPageMyAccount.sort_TableData(SelectedfieldData, txt_Field, sort_Order))
					message = "Sorting has been validated successfully in Product Code LookUp table for field " +txt_Field+ " in " + sort_Order + "order.";
				else {
					isTestCasePassed=false;
					message = "Sorting has been failed in Product Code LookUp table for field " +txt_Field+ " in " + sort_Order + "order.";
				}
				objLgr.info(message);
			}
		}
		if (!isTestCasePassed)
			Assert.fail("sort_ProductCodeLookUp failed - "+message);
		else
			objLgr.info("Sorting functionality has been validated sucessfully in Product Code LookUp table");
	}



	//--------------------------------------Dealer Order Change Log Test Cases---------------------------------------//

	@Test(enabled=false, description = "Validating that Dealer Order Change Log page is loaded successfully when Product Code LookUp Link is clicked in left navigation.", dependsOnMethods= {"sort_ProductCodeLookUp"}, priority=4000)
	public void leftNavigation_DealerOrderChangeLog() throws Exception {
		isTestCasePassed=true;
		lnk.get_linkDashBoard().click();
		webdriver.get("https://lepsi-d.agro.services/myaccount/product-code-look-up");
		if(docl.get_Table() != null)
			objLgr.info("Dealer Order Change Log page loaded successfully");
		else
			isTestCasePassed=false;
		if (!isTestCasePassed)
			Assert.fail("Dealer Order Change Log page loading unsuccessful");
		verifyPageWithApplitools(eyes);
	}

	@Test(enabled=false, description = "To check the visibility of Dealer Order Change Log Table ", dependsOnMethods= {"sort_ProductCodeLookUp"},alwaysRun = true, priority=4001)
	public void visibility_table_DealerOrderChangeLog() throws Exception {
		isTestCasePassed=true;
		if(userType.equalsIgnoreCase("NB Dealer")&&lob_selection.equalsIgnoreCase("Seed")) 
		{
			validate_LogOut();
			captureBrowserConsoleLogs();
			launchMyAccountHome();
			linkVisibility_Dashboard();
			webdriver.get("https://lepsi-d.agro.services/myaccount/change-log");
			if(docl.get_Table() != null)
				objLgr.info("Dealer Order Change Log table is loaded successfully");
			else
				isTestCasePassed=false;
			if (!isTestCasePassed)
				Assert.fail("Dealer Order Change Log table is not loaded successfully");
		}
		else  {
			objLgr.info("Dealer Order Change Log testcases will not be applicable for selected lob"+lob_selection+"; skipping the test case");
			throw new SkipException("Dealer Order Change Log TestCases will not apply to selected line of business");
		}
	}

	@Test(enabled=false, description = "Sorting functionality validation of Dealer Order Change Log table", dependsOnMethods= {"visibility_table_DealerOrderChangeLog"}, priority=4002)
	public void sort_DealerOrderChangeLog() throws Exception {
		isTestCasePassed=true;
		String message = null;
		WebElement tbl_DealerOrderChangeLog = docl.get_ChangeLogTable();
		List<WebElement> row_HeaderList = docl.get_RowHeader(tbl_DealerOrderChangeLog);
		for (WebElement header_Field : row_HeaderList) {
			String txt_Field = header_Field.getText();
			objLgr.info("Sorting has been started for field - "+txt_Field);
			for(int i=0;i<=1;i++) {
				List<WebElement> fields_Sort = docl.get_SortField(txt_Field);
				WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
				field_Sort.click();
				List<WebElement> row_Header = docl.get_RowHeader(tbl_DealerOrderChangeLog);
				List<WebElement> row_Data = docl.get_RowData(tbl_DealerOrderChangeLog);
				List<Hashtable<String, String>> SelectedfieldData = commonPageMyAccount.read_TableData(row_Header, row_Data, docl.get_fieldData());
				String sort_Order = field_Sort.getAttribute("aria-sort");
				if (commonPageMyAccount.sort_TableData(SelectedfieldData, txt_Field, sort_Order))
					message = "Sorting has been validated successfully in Dealer Order Change Log table for field " +txt_Field+ " in " + sort_Order + "order.";
				else {
					isTestCasePassed=false;
					message = "Sorting has been failed in Dealer Order Change Log table for field " +txt_Field+ " in " + sort_Order + "order.";
				}
				objLgr.info(message);
			}
		}
		if (!isTestCasePassed)
			Assert.fail("sort_DealerOrderChangeLog failed - "+message);
		else
			objLgr.info("Sorting functionality has been validated sucessfully in Dealer Order Change Log table");
	}

	@Test(enabled=false, description = "Validate search functionality in Dealer Order Change Log table",dependsOnMethods= {"visibility_table_DealerOrderChangeLog"}, priority=4003)
	public void search_DealerOrderChangeLog() throws Exception {
		isTestCasePassed=true;
		String message = null;
		WebElement tbl_DealerOrderChangeLog = docl.get_ChangeLogTable();
		List<WebElement> row_HeaderList = docl.get_RowHeader(tbl_DealerOrderChangeLog);
		List<WebElement> row_Data = docl.get_RowData(tbl_DealerOrderChangeLog);
		List<Hashtable<String, String>> tableData = commonPageMyAccount.read_TableData(row_HeaderList, row_Data, docl.get_fieldData());
		if (docl.get_SearchField()!= null) {
			String search_Txt=reusefunc.get_RandomString(tableData);
			objLgr.info("Search text is identified as - "+search_Txt);
			docl.get_SearchText().sendKeys(search_Txt);
			docl.get_SearchText().sendKeys(Keys.ENTER);
			List<WebElement> row_SearchData = docl.get_RowData(tbl_DealerOrderChangeLog);
			List<Hashtable<String, String>> tableData_Search = commonPageMyAccount.read_TableData(row_HeaderList, row_SearchData, docl.get_fieldData());
			objLgr.info("Total records found in Dealer Order Change Log table after searching with text " + search_Txt + " is - "+tableData_Search.size());
			if(commonPageMyAccount.isSearchText_Present(tableData_Search, search_Txt))
				message = "All Records displayed in the table contains searched text";
			else {
				isTestCasePassed = false;
				message = "All Records displayed in the table does not contain searched text";
			}
		}
		else {
			isTestCasePassed = false;
			message = "Search text field is not loaded succesfully";
		}
		objLgr.info(message);
		if (!isTestCasePassed)
			Assert.fail("search_DealerOrderChangeLog failed - " + message);
	}


	//--------------------------------------Product Availability Test Cases---------------------------------------//

	@Test(enabled=false, description = "Validating that Product Availability page is loaded successfully when Product Availability Link is clicked in left navigation.", dependsOnMethods= {"linkVisibility_Dashboard"}, alwaysRun = true, priority=5000)
	public void leftNavigation_ProductAvailability() throws Exception {
		isTestCasePassed=true;
		validate_LogOut();
		captureBrowserConsoleLogs();
		launchMyAccountHome();
		linkVisibility_Dashboard();
		if(lob_selection.equalsIgnoreCase("Seed")) {
			webdriver.get("https://lepsi-d.agro.services/myaccount/Product-Availability");
			widget_Corn_ProdAvail = productAvail.get_ProductAvailabilityCorn();
			if(widget_Corn_ProdAvail!= null)
				objLgr.info("Product Availability page loaded successfully");
			else
				isTestCasePassed=false;
			if (!isTestCasePassed)
				Assert.fail("Product Availability page loading unsuccessful");
			verifyPageWithApplitools(eyes);
		}
		else {
			objLgr.info("Product Availability does not apply to "+userType+"/"+lob_selection);
			throw new SkipException("ATMS does not apply to "+userType+"/"+lob_selection);
		}

	}

	@Test(enabled=false, description = "To check the visibility of Product Availability- Corn", dependsOnMethods= {"leftNavigation_ProductAvailability"}, priority=5001)
	public void visibility_ViewData_ProductAvailabilityCorn() throws Exception {
		isTestCasePassed=true;
		if(productAvail.get_ViewMoreLink(widget_Corn_ProdAvail)!= null)
			objLgr.info("Product Availability-Corn widget's view product button is visible");
		else {
			isTestCasePassed=false;
			objLgr.error("Product Availability-Corn widget's view product button is not visible");
		}
		if (!isTestCasePassed)
			Assert.fail("Product Availability-Corn widget's view product button visibility failed");
	}

	@Test(enabled=false, description = "To check the visibility of Product Availability List - Corn(View Product-Product Availability- Corn)", dependsOnMethods= {"visibility_ViewData_ProductAvailabilityCorn"}, priority=5002)
	public void visibility_Table_ProductAvailabilityList_ProductAvailabilityCorn() throws Exception {
		isTestCasePassed=true;
		tableText = null;
		productAvail.get_ViewMoreLink(widget_Corn_ProdAvail).click();
		WebElement tbl_Corn = productAvail.get_CropTable("Corn");
		if(productAvail.get_RowHeader(tbl_Corn)!= null)
			objLgr.info("Product List 'Corn' availability table loaded successfully");
		else {
			WebElement noDataAvailable = productAvail.get_NoDataTag("Corn");
			if(noDataAvailable!=null) {
				objLgr.error("No Data Available message is dispalyed in Product Availability- Corn table");
				tableText = "No Data Available";
			}
			else {
				isTestCasePassed=false;
				objLgr.error("Try Again message is dispalayed  in Product Availabilityc- Corn table");
				tableText = "Try Again";
			}
		}
		if (!isTestCasePassed)
			Assert.fail("Product List 'Corn' availability table visibility failed");

	}

	@Test(enabled=false, description = "Validate Sorting functionality validation in Product Availability List - Corn table", dependsOnMethods= {"visibility_Table_ProductAvailabilityList_ProductAvailabilityCorn"}, priority=5003)
	public void sort_ProductAvailabilityList_ProductAvailabilityCorn() throws Exception {
		isTestCasePassed=true;
		String message = "";
		if(tableText == null) {
			String tableName = productAvail.get_TableHeader().get(productAvail.get_TableHeader().size()-1).getText();
			WebElement tbl_Corn = productAvail.get_CropTable("Corn");
			List<WebElement> row_HeaderList = productAvail.get_RowHeader(tbl_Corn);
			for (WebElement header_Field : row_HeaderList) {
				String txt_Field = header_Field.getText();
				objLgr.info("Sorting has been started for field - "+txt_Field);
				for(int i=0;i<=1;i++) {
					List<WebElement> fields_Sort = productAvail.get_SortField(txt_Field);
					WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
					field_Sort.click();
					List<WebElement> row_Header = productAvail.get_RowHeader(tbl_Corn);
					List<WebElement> row_Data = productAvail.get_RowData(tbl_Corn);
					List<Hashtable<String, String>> SelectedCropData = commonPageMyAccount.read_TableData(row_Header, row_Data, productAvail.get_fieldData());
					String sort_Order = field_Sort.getAttribute("aria-sort");
					if (commonPageMyAccount.sort_TableData(SelectedCropData, txt_Field, sort_Order))
						message = "Sorting has been validated successfully in "+tableName+" tab for field " +txt_Field+ " in " + sort_Order + "order.";
					else {
						isTestCasePassed=false;
						message = "Sorting has been failed in "+ tableName +" tab for field " +txt_Field+ " in " + sort_Order + "order.";
					}
					objLgr.info(message);
				}
			}
		}
		else if(tableText.equalsIgnoreCase("No Data Available")) {
			objLgr.error("No Data Available message is dispalyed in Product Availability- Corn table");
			throw new SkipException("Skipping Sorting TC on Product Availability - Corn");
		}
		else {
			objLgr.error("Try Again error message is dispalyed in Product Availability- Corn table");
			isTestCasePassed=false;
		}

		if (!isTestCasePassed)
			Assert.fail("sort_ProductAvailabilityList_ProductAvailabilityCorn has failed -" +message);
	}

	@Test(enabled=false, description = "Validate download functionality visibility in Product Availability List - Corn table", dependsOnMethods= {"visibility_Table_ProductAvailabilityList_ProductAvailabilityCorn"}, priority=5004)
	public void download_Visibility_ProductAvailabilityList_ProductAvailabilityCorn() throws Exception {
		isTestCasePassed=true;
		if(tableText == null) {
			if(productAvail.get_download_Button().isEnabled())
				objLgr.info("Download button in Corn Availability list table is enabled");
			else {
				isTestCasePassed=false;
				objLgr.error("Download button in Corn Availability list table is disabled");
			}
		}
		else if(tableText.equalsIgnoreCase("No Data Available")) {
			objLgr.error("No Data Available message is dispalyed in Product Availability- Corn table");
			throw new SkipException("Skipping Sorting TC on Product Availability - Corn");
		}
		else {
			objLgr.error("Try Again error message is dispalyed in Product Availability- Corn table");
			isTestCasePassed=false;
		}
		if (!isTestCasePassed)
			Assert.fail("Download button in Corn availability table visibility failed");

	}

	@Test(enabled=false, description = "Validate Trait Drop-down functionality visibility in Product Availability List - Corn table", dependsOnMethods= {"visibility_Table_ProductAvailabilityList_ProductAvailabilityCorn"}, priority=5005)
	public void traitDropDown_Visibility_ProductAvailabilityList_ProductAvailabilityCorn() throws Exception {
		isTestCasePassed=true;
		String dropdown_Text = "Trait";
		if(tableText == null) {
			random_row = reusefunc.get_RandomInteger(0, 9, "1");
			objLgr.info("Random row no: "+random_row+" data will be fetched");
			WebElement tbl_Corn = productAvail.get_CropTable("Corn");
			List<WebElement> header = productAvail.get_RowHeader(tbl_Corn);
			List<WebElement> data = productAvail.get_RowData(tbl_Corn);
			Hashtable<String, String> random_Data = commonPageMyAccount.read_TableData(header, data, productAvail.get_fieldData(),random_row);
			String value = random_Data.get(dropdown_Text).toString();
			if(!value.isEmpty()) {
				productAvail.filter_TextBox(dropdown_Text).sendKeys(value);
				productAvail.filter_TextBox(dropdown_Text).sendKeys(Keys.ENTER);
				if(productAvail.get_RowHeader(tbl_Corn)!=null) {
					for(int i =0;i<2;i++) {
						List<WebElement> fields_Sort = productAvail.get_SortField(dropdown_Text);
						WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
						field_Sort.click();
						objLgr.info("Sorting action has been performed on "+field_Sort.getText());
						List<WebElement> header_data = productAvail.get_RowHeader(tbl_Corn);
						List<WebElement> row_data = productAvail.get_RowData(tbl_Corn);
						Hashtable<String, String> selectedData = commonPageMyAccount.read_TableData(header_data, row_data, productAvail.get_fieldData(),0);
						if(selectedData.get(dropdown_Text).equalsIgnoreCase(value)) 
							objLgr.info("First value from "+dropdown_Text+" matches with the drop down option selected");
						else {
							isTestCasePassed = false;
							objLgr.error("****Mismatch found****\nExpected Value :"+value+"\nActual value :"+selectedData.get(dropdown_Text).toString());
						}
					}
				}
				else {
					isTestCasePassed = false;
					WebElement noDataAvailable = productAvail.get_NoDataTag("Corn");
					if(noDataAvailable!=null) 
						objLgr.error("No Data Available message is dispalyed for the Trait value :"+value);
					else
						objLgr.error("Try Again message is dispalayed");
				}
				productAvail.get_filterClose(dropdown_Text).click();
			}
			else {
				isTestCasePassed = false;
				objLgr.error("Trait value fetched from random row is blank");
			}
		}
		else if(tableText.equalsIgnoreCase("No Data Available")) {
			objLgr.error("No Data Available message is dispalyed in Product Availability- Corn table");
			throw new SkipException("Skipping Sorting TC on Product Availability - Corn");
		}
		else {
			objLgr.error("Try Again error message is dispalyed in Product Availability- Corn table");
			isTestCasePassed=false;
		}
		if (!isTestCasePassed)
			Assert.fail("Trait drop down button in Corn availability table validation failed");
	}

	@Test(enabled=false, description = "Validate Product Drop-down functionality visibility in Product Availability List - Corn table", dependsOnMethods= {"visibility_Table_ProductAvailabilityList_ProductAvailabilityCorn"}, priority=5006)
	public void productDropDown_Visibility_ProductAvailabilityList_ProductAvailabilityCorn() throws Exception {
		isTestCasePassed=true;
		String dropdown_Text = "Product";
		if(tableText == null) {
			random_row = reusefunc.get_RandomInteger(0, 9, "1");
			objLgr.info("Random row no: "+random_row+" data will be fetched");
			WebElement tbl_Corn = productAvail.get_CropTable("Corn");
			List<WebElement> header = productAvail.get_RowHeader(tbl_Corn);
			List<WebElement> data = productAvail.get_RowData(tbl_Corn);
			Hashtable<String, String> random_Data = commonPageMyAccount.read_TableData(header, data, productAvail.get_fieldData(),random_row);
			String value = random_Data.get(dropdown_Text).toString();
			if(!value.isEmpty()) {
				productAvail.filter_TextBox(dropdown_Text).sendKeys(value);
				productAvail.filter_TextBox(dropdown_Text).sendKeys(Keys.ENTER);
				if(productAvail.get_RowHeader(tbl_Corn)!=null) {
					for(int i =0;i<2;i++) {
						List<WebElement> fields_Sort = productAvail.get_SortField(dropdown_Text);
						WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
						field_Sort.click();
						objLgr.info("Sorting action has been performed on "+field_Sort.getText());
						List<WebElement> header_data = productAvail.get_RowHeader(tbl_Corn);
						List<WebElement> row_data = productAvail.get_RowData(tbl_Corn);
						Hashtable<String, String> selectedData = commonPageMyAccount.read_TableData(header_data, row_data, productAvail.get_fieldData(),0);
						if(selectedData.get(dropdown_Text).equalsIgnoreCase(value)) 
							objLgr.info("First value from "+dropdown_Text+" matches with the drop down option selected");
						else {
							isTestCasePassed = false;
							objLgr.error("****Mismatch found****\nExpected Value :"+value+"\nActual value :"+selectedData.get(dropdown_Text).toString());
						}
					}
				}
				else {
					isTestCasePassed = false;
					WebElement noDataAvailable = productAvail.get_NoDataTag("Corn");
					if(noDataAvailable!=null) 
						objLgr.error("No Data Available message is dispalyed for the Product value :"+value);
					else
						objLgr.error("Try Again message is dispalayed");
				}
				productAvail.get_filterClose(dropdown_Text).click();
			}
			else {
				isTestCasePassed = false;
				objLgr.error(dropdown_Text+" value fetched from random row is blank");
			}
		}
		else if(tableText.equalsIgnoreCase("No Data Available")) {
			objLgr.error("No Data Available message is dispalyed in Product Availability- Corn table");
			throw new SkipException("Skipping Sorting TC on Product Availability - Corn");
		}
		else {
			objLgr.error("Try Again error message is dispalyed in Product Availability- Corn table");
			isTestCasePassed=false;
		}
		if (!isTestCasePassed)
			Assert.fail(dropdown_Text+" drop down button in Corn availability table validation failed");
	}

	@Test(enabled=false, description = "Validate Seed Size Drop-down functionality visibility in Product Availability List - Corn table", dependsOnMethods= {"visibility_Table_ProductAvailabilityList_ProductAvailabilityCorn"}, priority=5007)
	public void seedSizeDropDown_Visibility_ProductAvailabilityList_ProductAvailabilityCorn() throws Exception {
		isTestCasePassed=true;
		String dropdown_Text = "Seed Size";
		if(tableText == null) {
			random_row = reusefunc.get_RandomInteger(0, 9, "1");
			objLgr.info("Random row no: "+random_row+" data will be fetched");
			WebElement tbl_Corn = productAvail.get_CropTable("Corn");
			List<WebElement> header = productAvail.get_RowHeader(tbl_Corn);
			List<WebElement> data = productAvail.get_RowData(tbl_Corn);
			Hashtable<String, String> random_Data = commonPageMyAccount.read_TableData(header, data, productAvail.get_fieldData(),random_row);
			String value = random_Data.get(dropdown_Text).toString();
			if(!value.isEmpty()) {
				productAvail.filter_TextBox(dropdown_Text).sendKeys(value);
				productAvail.filter_TextBox(dropdown_Text).sendKeys(Keys.ENTER);
				if(productAvail.get_RowHeader(tbl_Corn)!=null) {
					for(int i =0;i<2;i++) {
						List<WebElement> fields_Sort = productAvail.get_SortField(dropdown_Text);
						WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
						field_Sort.click();
						objLgr.info("Sorting action has been performed on "+field_Sort.getText());
						List<WebElement> header_data = productAvail.get_RowHeader(tbl_Corn);
						List<WebElement> row_data = productAvail.get_RowData(tbl_Corn);
						Hashtable<String, String> selectedData = commonPageMyAccount.read_TableData(header_data, row_data, productAvail.get_fieldData(),0);
						if(selectedData.get(dropdown_Text).equalsIgnoreCase(value)) 
							objLgr.info("First value from "+dropdown_Text+" matches with the drop down option selected");
						else {
							isTestCasePassed = false;
							objLgr.error("****Mismatch found****\nExpected Value :"+value+"\nActual value :"+selectedData.get(dropdown_Text).toString());
						}
					}
				}
				else {
					isTestCasePassed = false;
					WebElement noDataAvailable = productAvail.get_NoDataTag("Corn");
					if(noDataAvailable!=null) 
						objLgr.error("No Data Available message is dispalyed for the "+dropdown_Text+" value :"+value);
					else
						objLgr.error("Try Again message is dispalayed");
				}
				productAvail.get_filterClose(dropdown_Text).click();
			}
			else {
				isTestCasePassed = false;
				objLgr.error(dropdown_Text+" value fetched from random row is blank");
			}
		}
		else if(tableText.equalsIgnoreCase("No Data Available")) {
			objLgr.error("No Data Available message is dispalyed in Product Availability- Corn table");
			throw new SkipException("Skipping Sorting TC on Product Availability - Corn");
		}
		else {
			objLgr.error("Try Again error message is dispalyed in Product Availability- Corn table");
			isTestCasePassed=false;
		}
		if (!isTestCasePassed)
			Assert.fail(dropdown_Text+" drop down button in Corn availability table validation failed");
	}

	@Test(enabled=false,description = "Validate that back button works from ProductAvailability-Corn", dependsOnMethods= {"seedSizeDropDown_Visibility_ProductAvailabilityList_ProductAvailabilityCorn"}, priority=5008)
	public void back_ProductAvailabilityCorn() throws Exception {
		isTestCasePassed=true;
		productAvail.getBackArrowoption().click();
		if(productAvail.get_ViewMoreLink(productAvail.get_ProductAvailabilityCorn())!= null)
			objLgr.info("Product Availability-Corn widget's view product button is visible");
		else {
			isTestCasePassed=false;
			objLgr.error("Product Availability-Corn widget's view product button is not visible");
		}
		if (!isTestCasePassed)
			Assert.fail("back_ProductAvailabilityCorn failed.");
	}

	@Test(enabled=false, description = "To check the visibility of Product Availability- Soybeans", dependsOnMethods= {"back_ProductAvailabilityCorn"}, alwaysRun = true, priority=5009)
	public void visibility_ViewData_ProductAvailabilitySoybeans() throws Exception {
		isTestCasePassed=true;
		leftNavigation_ProductAvailability();
		widget_Soybeans_ProdAvail = productAvail.get_ProductAvailabilitySoybeans();
		if(productAvail.get_ViewMoreLink(widget_Soybeans_ProdAvail)!= null)
			objLgr.info("Product Availability-Soybeans widget's view product button is visible");
		else {
			isTestCasePassed=false;
			objLgr.error("Product Availability-Soybeans widget's view product button is not visible");
		}
		if (!isTestCasePassed)
			Assert.fail("Product Availability-Soybeans widget's view product button visibility failed");
	}

	@Test(enabled=false, description = "To check the visibility of Product Availability List - Soybeans(View Product-Product Availability- Soybeans)", dependsOnMethods= {"visibility_ViewData_ProductAvailabilitySoybeans"}, priority=5010)
	public void visibility_Table_ProductAvailabilityList_ProductAvailabilitySoybeans() throws Exception {
		isTestCasePassed=true;
		tableText = null;
		productAvail.get_ViewMoreLink(widget_Soybeans_ProdAvail).click();
		WebElement tbl_Soybean = productAvail.get_CropTable("Soybeans");
		if(productAvail.get_RowHeader(tbl_Soybean)!= null)
			objLgr.info("Product List 'Soybeans' availability table loaded successfully");
		else {
			WebElement noDataAvailable = productAvail.get_NoDataTag("Soybeans");
			if(noDataAvailable!=null) {
				objLgr.error("No Data Available message is dispalyed in Product Availability- Soybeans table");
				tableText = "No Data Available";
			}
			else {
				isTestCasePassed=false;
				objLgr.error("Try Again message is dispalayed  in Product Availabilityc- Soybeans table");
				tableText ="Try Again";
			}
		}
		if (!isTestCasePassed)
			Assert.fail("Product List 'Soybeans' availability table visibility failed");

	}

	@Test(enabled=false, description = "Validate Sorting functionality validation in Product Availability List - Soybeans table", dependsOnMethods= {"visibility_Table_ProductAvailabilityList_ProductAvailabilitySoybeans"}, priority=5011)
	public void sort_ProductAvailabilityList_ProductAvailabilitySoybeans() throws Exception {
		isTestCasePassed=true;
		String message = "";
		if(tableText == null) {
			String tableName = productAvail.get_TableHeader().get(productAvail.get_TableHeader().size()-1).getText();
			WebElement tbl_Soybeans = productAvail.get_CropTable("Soybeans");
			List<WebElement> row_HeaderList = productAvail.get_RowHeader(tbl_Soybeans);
			for (WebElement header_Field : row_HeaderList) {
				String txt_Field = header_Field.getText();
				objLgr.info("Sorting has been started for field - "+txt_Field);
				for(int i=0;i<=1;i++) {
					List<WebElement> fields_Sort = productAvail.get_SortField(txt_Field);
					WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
					field_Sort.click();
					List<WebElement> row_Header = productAvail.get_RowHeader(tbl_Soybeans);
					List<WebElement> row_Data = productAvail.get_RowData(tbl_Soybeans);
					List<Hashtable<String, String>> SelectedCropData = commonPageMyAccount.read_TableData(row_Header, row_Data, productAvail.get_fieldData());
					String sort_Order = field_Sort.getAttribute("aria-sort");
					if (commonPageMyAccount.sort_TableData(SelectedCropData, txt_Field, sort_Order))
						message = "Sorting has been validated successfully in "+tableName+" tab for field " +txt_Field+ " in " + sort_Order + "order.";
					else {
						isTestCasePassed=false;
						message = "Sorting has been failed in "+ tableName +" tab for field " +txt_Field+ " in " + sort_Order + "order.";
					}
					objLgr.info(message);
				}
			}
		}
		else if(tableText.equalsIgnoreCase("No Data Available")) {
			objLgr.error("No Data Available message is dispalyed in Product Availability- Soybean table");
			throw new SkipException("Skipping Sorting TC on Product Availability - Soybean");
		}
		else {
			objLgr.error("Try Again error message is dispalyed in Product Availability- Soybean table");
			isTestCasePassed=false;
		}
		if (!isTestCasePassed)
			Assert.fail("sort_ProductAvailabilityList_ProductAvailabilitySoybeans has failed -" +message);
	}

	@Test(enabled=false, description = "Validate download functionality visibility in Product Availability List - Soybeans table", dependsOnMethods= {"visibility_Table_ProductAvailabilityList_ProductAvailabilitySoybeans"}, priority=5012)
	public void download_Visibility_ProductAvailabilityList_ProductAvailabilitySoybeans() throws Exception {
		isTestCasePassed=true;
		if(tableText == null) {
			if(productAvail.get_download_Button().isEnabled())
				objLgr.info("Download button in Soybeans Availability list table is enabled");
			else {
				isTestCasePassed=false;
				objLgr.error("Download button in Soybeans Availability list table is disabled");
			}
		}
		else if(tableText.equalsIgnoreCase("No Data Available")) {
			objLgr.error("No Data Available message is dispalyed in Product Availability- Soybean table");
			throw new SkipException("Skipping Sorting TC on Product Availability - Soybean");
		}
		else {
			objLgr.error("Try Again error message is dispalyed in Product Availability- Soybean table");
			isTestCasePassed=false;
		}
		if (!isTestCasePassed)
			Assert.fail("Download button in Soybeans availability table visibility failed");

	}

	@Test(enabled=false, description = "Validate Trait Drop-down functionality visibility in Product Availability List - Soybeans table", dependsOnMethods= {"visibility_Table_ProductAvailabilityList_ProductAvailabilitySoybeans"}, priority=5013)
	public void traitDropDown_Visibility_ProductAvailabilityList_ProductAvailabilitySoybeans() throws Exception {
		isTestCasePassed=true;
		String dropdown_Text = "Trait";
		if(tableText == null) {
			random_row = reusefunc.get_RandomInteger(0, 9, "1");
			objLgr.info("Random row no: "+random_row+" data will be fetched");
			WebElement tbl_crop = productAvail.get_CropTable("Soybean");
			List<WebElement> header = productAvail.get_RowHeader(tbl_crop);
			List<WebElement> data = productAvail.get_RowData(tbl_crop);
			Hashtable<String, String> random_Data = commonPageMyAccount.read_TableData(header, data, productAvail.get_fieldData(),random_row);
			String value = random_Data.get(dropdown_Text).toString();
			if(!value.isEmpty()) {
				productAvail.filter_TextBox(dropdown_Text).sendKeys(value);
				productAvail.filter_TextBox(dropdown_Text).sendKeys(Keys.ENTER);
				if(productAvail.get_RowHeader(tbl_crop)!=null) {
					for(int i =0;i<2;i++) {
						List<WebElement> fields_Sort = productAvail.get_SortField(dropdown_Text);
						WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
						field_Sort.click();
						objLgr.info("Sorting action has been performed on "+field_Sort.getText());
						List<WebElement> header_data = productAvail.get_RowHeader(tbl_crop);
						List<WebElement> row_data = productAvail.get_RowData(tbl_crop);
						Hashtable<String, String> selectedData = commonPageMyAccount.read_TableData(header_data, row_data, productAvail.get_fieldData(),0);
						if(selectedData.get(dropdown_Text).equalsIgnoreCase(value)) 
							objLgr.info("First value from "+dropdown_Text+" matches with the drop down option selected");
						else {
							isTestCasePassed = false;
							objLgr.error("****Mismatch found****\nExpected Value :"+value+"\nActual value :"+selectedData.get(dropdown_Text).toString());
						}
					}
				}
				else {
					isTestCasePassed = false;
					WebElement noDataAvailable = productAvail.get_NoDataTag("Soybeans");
					if(noDataAvailable!=null) 
						objLgr.error("No Data Available message is dispalyed for thethe "+dropdown_Text+" value :"+value);
					else
						objLgr.error("Try Again message is dispalayed");
				}
				productAvail.get_filterClose(dropdown_Text).click();
			}
			else {
				isTestCasePassed = false;
				objLgr.error(dropdown_Text+" value fetched from random row is blank");
			}
		}
		else if(tableText.equalsIgnoreCase("No Data Available")) {
			objLgr.error("No Data Available message is dispalyed in Product Availability- Soybean table");
			throw new SkipException("Skipping Sorting TC on Product Availability - Soybean");
		}
		else {
			objLgr.error("Try Again error message is dispalyed in Product Availability- Soybean table");
			isTestCasePassed=false;
		}
		if (!isTestCasePassed)
			Assert.fail(dropdown_Text+" drop down button in Soybean availability table validation failed");
	}

	@Test(enabled=false, description = "Validate Product Drop-down functionality visibility in Product Availability List - Soybeans table", dependsOnMethods= {"visibility_Table_ProductAvailabilityList_ProductAvailabilitySoybeans"}, priority=5014)
	public void productDropDown_Visibility_ProductAvailabilityList_ProductAvailabilitySoybeans() throws Exception {
		isTestCasePassed=true;
		String dropdown_Text = "Product";
		if(tableText == null) {
			random_row = reusefunc.get_RandomInteger(0, 9, "1");
			objLgr.info("Random row no: "+random_row+" data will be fetched");
			WebElement tbl_crop = productAvail.get_CropTable("Soybean");
			List<WebElement> header = productAvail.get_RowHeader(tbl_crop);
			List<WebElement> data = productAvail.get_RowData(tbl_crop);
			Hashtable<String, String> random_Data = commonPageMyAccount.read_TableData(header, data, productAvail.get_fieldData(),random_row);
			String value = random_Data.get(dropdown_Text).toString();
			if(!value.isEmpty()) {
				productAvail.filter_TextBox(dropdown_Text).sendKeys(value);
				productAvail.filter_TextBox(dropdown_Text).sendKeys(Keys.ENTER);
				if(productAvail.get_RowHeader(tbl_crop)!=null) {
					for(int i =0;i<2;i++) {
						List<WebElement> fields_Sort = productAvail.get_SortField(dropdown_Text);
						WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
						field_Sort.click();
						objLgr.info("Sorting action has been performed on "+field_Sort.getText());
						List<WebElement> header_data = productAvail.get_RowHeader(tbl_crop);
						List<WebElement> row_data = productAvail.get_RowData(tbl_crop);
						Hashtable<String, String> selectedData = commonPageMyAccount.read_TableData(header_data, row_data, productAvail.get_fieldData(),0);
						if(selectedData.get(dropdown_Text).equalsIgnoreCase(value)) 
							objLgr.info("First value from "+dropdown_Text+" matches with the drop down option selected");
						else {
							isTestCasePassed = false;
							objLgr.error("****Mismatch found****\nExpected Value :"+value+"\nActual value :"+selectedData.get(dropdown_Text).toString());
						}
					}
				}
				else {
					isTestCasePassed = false;
					WebElement noDataAvailable = productAvail.get_NoDataTag("Soybeans");
					if(noDataAvailable!=null) 
						objLgr.error("No Data Available message is dispalyed for the Product value :"+value);
					else
						objLgr.error("Try Again message is dispalayed");
				}
				productAvail.get_filterClose(dropdown_Text).click();
			}
			else {
				isTestCasePassed = false;
				objLgr.error(dropdown_Text+" value fetched from random row is blank");
			}
		}
		else if(tableText.equalsIgnoreCase("No Data Available")) {
			objLgr.error("No Data Available message is dispalyed in Product Availability- Soybean table");
			throw new SkipException("Skipping Sorting TC on Product Availability - Soybean");
		}
		else {
			objLgr.error("Try Again error message is dispalyed in Product Availability- Soybean table");
			isTestCasePassed=false;
		}
		if (!isTestCasePassed)
			Assert.fail(dropdown_Text+" drop down button in Soybean availability table validation failed");
	}

	@Test(enabled=false,description = "Validate that back button works from ProductAvailability-Soybeans", dependsOnMethods= {"productDropDown_Visibility_ProductAvailabilityList_ProductAvailabilitySoybeans"}, priority=5015)
	public void back_ProductAvailabilitySoybeans() throws Exception {
		isTestCasePassed=true;
		productAvail.getBackArrowoption().click();
		if(productAvail.get_ViewMoreLink(productAvail.get_ProductAvailabilitySoybeans())!= null)
			objLgr.info("Product Availability-Soybeans widget's view product button is visible");
		else {
			isTestCasePassed=false;
			objLgr.error("Product Availability-Soybeans widget's view product button is not visible");
		}
		if (!isTestCasePassed)
			Assert.fail("back_ProductAvailabilitySoybeans failed.");
	}

	@Test(enabled=false, description = "To check the visibility of Product Availability- Cotton", dependsOnMethods= {"back_ProductAvailabilitySoybeans"}, alwaysRun= true, priority=5016)
	public void visibility_ViewData_ProductAvailabilityCotton() throws Exception {
		isTestCasePassed=true;
		leftNavigation_ProductAvailability();
		widget_Cotton_ProdAvail = productAvail.get_ProductAvailabilityCotton();
		if(productAvail.get_ViewMoreLink(widget_Cotton_ProdAvail)!= null)
			objLgr.info("Product Availability-Cotton widget's view product button is visible");
		else {
			isTestCasePassed=false;
			objLgr.error("Product Availability-Cotton widget's view product button is not visible");
		}
		if (!isTestCasePassed)
			Assert.fail("Product Availability-Cotton widget's view product button visibility failed");
	}

	@Test(enabled=false, description = "To check the visibility of Product Availability List - Cotton(View Product-Product Availability- Cotton)", dependsOnMethods= {"visibility_ViewData_ProductAvailabilityCotton"}, priority=5017)
	public void visibility_Table_ProductAvailabilityList_ProductAvailabilityCotton() throws Exception {
		isTestCasePassed=true;
		tableText = null;
		productAvail.get_ViewMoreLink(widget_Cotton_ProdAvail).click();
		WebElement tbl_Cotton = productAvail.get_CropTable("Cotton");
		if(productAvail.get_RowHeader(tbl_Cotton)!= null)
			objLgr.info("Product List 'Cotton' availability table loaded successfully");
		else {
			WebElement noDataAvailable = productAvail.get_NoDataTag("Cotton");
			if(noDataAvailable!=null) {
				objLgr.error("No Data Available message is dispalyed in Product Availability- Cotton table");
				tableText = "No Data Available";
			}
			else {
				isTestCasePassed=false;
				objLgr.error("Try Again message is dispalayed  in Product Availabilityc- Cotton table");
				tableText = "Try Again";
			}
		}
		if (!isTestCasePassed)
			Assert.fail("Product List 'Cotton' availability table visibility failed");

	}

	@Test(enabled=false, description = "Validate Sorting functionality validation in Product Availability List - Cotton table", dependsOnMethods= {"visibility_Table_ProductAvailabilityList_ProductAvailabilityCotton"}, priority=5018)
	public void sort_ProductAvailabilityList_ProductAvailabilityCotton() throws Exception {
		isTestCasePassed=true;
		String message = "";
		if(tableText == null) {
			String tableName = productAvail.get_TableHeader().get(productAvail.get_TableHeader().size()-1).getText();
			WebElement tbl_Soybeans = productAvail.get_CropTable("Cotton");
			List<WebElement> row_HeaderList = productAvail.get_RowHeader(tbl_Soybeans);
			for (WebElement header_Field : row_HeaderList) {
				String txt_Field = header_Field.getText();
				objLgr.info("Sorting has been started for field - "+txt_Field);
				for(int i=0;i<=1;i++) {
					List<WebElement> fields_Sort = productAvail.get_SortField(txt_Field);
					WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
					field_Sort.click();
					List<WebElement> row_Header = productAvail.get_RowHeader(tbl_Soybeans);
					List<WebElement> row_Data = productAvail.get_RowData(tbl_Soybeans);
					List<Hashtable<String, String>> SelectedCropData = commonPageMyAccount.read_TableData(row_Header, row_Data, productAvail.get_fieldData());
					String sort_Order = field_Sort.getAttribute("aria-sort");
					if (commonPageMyAccount.sort_TableData(SelectedCropData, txt_Field, sort_Order))
						message = "Sorting has been validated successfully in "+tableName+" tab for field " +txt_Field+ " in " + sort_Order + "order.";
					else {
						isTestCasePassed=false;
						message = "Sorting has been failed in "+ tableName +" tab for field " +txt_Field+ " in " + sort_Order + "order.";
					}
					objLgr.info(message);
				}
			}
		}
		else if(tableText.equalsIgnoreCase("No Data Available")) {
			objLgr.error("No Data Available message is dispalyed in Product Availability- Cotton table");
			throw new SkipException("Skipping Sorting TC on Product Availability - Cotton");
		}
		else {
			objLgr.error("Try Again error message is dispalyed in Product Availability- Cotton table");
			isTestCasePassed=false;
		}
		if (!isTestCasePassed)
			Assert.fail("sort_ProductAvailabilityList_ProductAvailabilityCotton has failed -" +message);
	}

	@Test(enabled=false, description = "Validate download functionality visibility in Product Availability List - Cotton table", dependsOnMethods= {"visibility_Table_ProductAvailabilityList_ProductAvailabilityCotton"}, priority=5019)
	public void download_Visibility_ProductAvailabilityList_ProductAvailabilityCotton() throws Exception {
		isTestCasePassed=true;
		if(tableText == null) {
			if(productAvail.get_download_Button().isEnabled())
				objLgr.info("Download button in Cotton Availability list table is enabled");
			else {
				isTestCasePassed=false;
				objLgr.error("Download button in Cotton Availability list table is disabled");
			}
		}
		else if(tableText.equalsIgnoreCase("No Data Available")) {
			objLgr.error("No Data Available message is dispalyed in Product Availability- Cotton table");
			throw new SkipException("Skipping Sorting TC on Product Availability - Cotton");
		}
		else {
			objLgr.error("Try Again error message is dispalyed in Product Availability- Cotton table");
			isTestCasePassed=false;
		}
		if (!isTestCasePassed)
			Assert.fail("Download button in Cotton availability table visibility failed");

	}

	@Test(enabled=false, description = "Validate Trait Drop-down functionality visibility in Product Availability List - Cotton table", dependsOnMethods= {"visibility_Table_ProductAvailabilityList_ProductAvailabilityCotton"}, priority=5020)
	public void traitDropDown_Visibility_ProductAvailabilityList_ProductAvailabilityCotton() throws Exception {
		isTestCasePassed=true;
		String dropdown_Text = "Trait";
		if(tableText == null) {
			random_row = reusefunc.get_RandomInteger(0, 9, "1");
			objLgr.info("Random row no: "+random_row+" data will be fetched");
			WebElement tbl_crop = productAvail.get_CropTable("Cotton");
			List<WebElement> header = productAvail.get_RowHeader(tbl_crop);
			List<WebElement> data = productAvail.get_RowData(tbl_crop);
			Hashtable<String, String> random_Data = commonPageMyAccount.read_TableData(header, data, productAvail.get_fieldData(),random_row);
			String value = random_Data.get(dropdown_Text).toString();
			if(!value.isEmpty()) {
				productAvail.filter_TextBox(dropdown_Text).sendKeys(value);
				productAvail.filter_TextBox(dropdown_Text).sendKeys(Keys.ENTER);
				if(productAvail.get_RowHeader(tbl_crop)!=null) {
					for(int i =0;i<2;i++) {
						List<WebElement> fields_Sort = productAvail.get_SortField(dropdown_Text);
						WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
						field_Sort.click();
						objLgr.info("Sorting action has been performed on "+field_Sort.getText());
						List<WebElement> header_data = productAvail.get_RowHeader(tbl_crop);
						List<WebElement> row_data = productAvail.get_RowData(tbl_crop);
						Hashtable<String, String> selectedData = commonPageMyAccount.read_TableData(header_data, row_data, productAvail.get_fieldData(),0);
						if(selectedData.get(dropdown_Text).equalsIgnoreCase(value)) 
							objLgr.info("First value from "+dropdown_Text+" matches with the drop down option selected");
						else {
							isTestCasePassed = false;
							objLgr.error("****Mismatch found****\nExpected Value :"+value+"\nActual value :"+selectedData.get(dropdown_Text).toString());
						}
					}
				}
				else {
					isTestCasePassed = false;
					WebElement noDataAvailable = productAvail.get_NoDataTag("Cotton");
					if(noDataAvailable!=null) 
						objLgr.error("No Data Available message is dispalyed for the "+dropdown_Text+" value :"+value);
					else
						objLgr.error("Try Again message is dispalayed");
				}
				productAvail.get_filterClose(dropdown_Text).click();
			}
			else {
				isTestCasePassed = false;
				objLgr.error(dropdown_Text+" value fetched from random row is blank");
			}
		}
		else if(tableText.equalsIgnoreCase("No Data Available")) {
			objLgr.error("No Data Available message is dispalyed in Product Availability- Cotton table");
			throw new SkipException("Skipping Sorting TC on Product Availability - Cotton");
		}
		else {
			objLgr.error("Try Again error message is dispalyed in Product Availability- Cotton table");
			isTestCasePassed=false;
		}
		if (!isTestCasePassed)
			Assert.fail(dropdown_Text+" drop down button in Cotton availability table validation failed");
	}

	@Test(enabled=false, description = "Validate Product Drop-down functionality visibility in Product Availability List - Cotton table", dependsOnMethods= {"visibility_Table_ProductAvailabilityList_ProductAvailabilityCotton"}, priority=5021)
	public void productDropDown_Visibility_ProductAvailabilityList_ProductAvailabilityCotton() throws Exception {
		isTestCasePassed=true;
		String dropdown_Text = "Product";
		if(tableText == null) {
			random_row = reusefunc.get_RandomInteger(0, 9, "1");
			objLgr.info("Random row no: "+random_row+" data will be fetched");
			WebElement tbl_crop = productAvail.get_CropTable("Cotton");
			List<WebElement> header = productAvail.get_RowHeader(tbl_crop);
			List<WebElement> data = productAvail.get_RowData(tbl_crop);
			Hashtable<String, String> random_Data = commonPageMyAccount.read_TableData(header, data, productAvail.get_fieldData(),random_row);
			String value = random_Data.get(dropdown_Text).toString();
			if(!value.isEmpty()) {
				productAvail.filter_TextBox(dropdown_Text).sendKeys(value);
				productAvail.filter_TextBox(dropdown_Text).sendKeys(Keys.ENTER);
				if(productAvail.get_RowHeader(tbl_crop)!=null) {
					for(int i =0;i<2;i++) {
						List<WebElement> fields_Sort = productAvail.get_SortField(dropdown_Text);
						WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
						field_Sort.click();
						objLgr.info("Sorting action has been performed on "+field_Sort.getText());
						List<WebElement> header_data = productAvail.get_RowHeader(tbl_crop);
						List<WebElement> row_data = productAvail.get_RowData(tbl_crop);
						Hashtable<String, String> selectedData = commonPageMyAccount.read_TableData(header_data, row_data, productAvail.get_fieldData(),0);
						if(selectedData.get(dropdown_Text).equalsIgnoreCase(value)) 
							objLgr.info("First value from "+dropdown_Text+" matches with the drop down option selected");
						else {
							isTestCasePassed = false;
							objLgr.error("****Mismatch found****\nExpected Value :"+value+"\nActual value :"+selectedData.get(dropdown_Text).toString());
						}
					}
				}
				else {
					isTestCasePassed = false;
					WebElement noDataAvailable = productAvail.get_NoDataTag("Cotton");
					if(noDataAvailable!=null) 
						objLgr.error("No Data Available message is dispalyed for the" +dropdown_Text+" value :"+value);
					else
						objLgr.error("Try Again message is dispalayed");
				}
				productAvail.get_filterClose(dropdown_Text).click();
			}
			else {
				isTestCasePassed = false;
				objLgr.error(dropdown_Text+" value fetched from random row is blank");
			}
		}
		else if(tableText.equalsIgnoreCase("No Data Available")) {
			objLgr.error("No Data Available message is dispalyed in Product Availability- Cotton table");
			throw new SkipException("Skipping Sorting TC on Product Availability - Cotton");
		}
		else {
			objLgr.error("Try Again error message is dispalyed in Product Availability- Cotton table");
			isTestCasePassed=false;
		}
		if (!isTestCasePassed)
			Assert.fail(dropdown_Text+" drop down button in Cotton availability table validation failed");
	}

	@Test(enabled=false,description = "Validate that back button works from ProductAvailability-Cotton", dependsOnMethods= {"productDropDown_Visibility_ProductAvailabilityList_ProductAvailabilityCotton"}, priority=5022)
	public void back_ProductAvailabilityCotton() throws Exception {
		isTestCasePassed=true;
		productAvail.getBackArrowoption().click();
		if(productAvail.get_ViewMoreLink(productAvail.get_ProductAvailabilityCotton())!= null)
			objLgr.info("Product Availability-Cotton widget's view product button is visible");
		else {
			isTestCasePassed=false;
			objLgr.error("Product Availability-Cotton widget's view product button is not visible");
		}
		if (!isTestCasePassed)
			Assert.fail("back_ProductAvailabilityCotton failed.");
	}

	@Test(enabled=false, description = "To check the visibility of Product Availability- Alfalfa", dependsOnMethods= {"back_ProductAvailabilityCotton"}, alwaysRun = true, priority=5023)
	public void visibility_ViewData_ProductAvailabilityAlfalfa() throws Exception {
		isTestCasePassed=true;
		leftNavigation_ProductAvailability();
		widget_Alfalfa_ProdAvail = productAvail.get_ProductAvailabilityAlfalfa();
		if(productAvail.get_ViewMoreLink(widget_Alfalfa_ProdAvail)!= null)
			objLgr.info("Product Availability-Alfalfa widget's view product button is visible");
		else {
			isTestCasePassed=false;
			objLgr.error("Product Availability-Alfalfa widget's view product button is not visible");
		}
		if (!isTestCasePassed)
			Assert.fail("Product Availability-Alfalfa widget's view product button visibility failed");
	}

	@Test(enabled=false, description = "To check the visibility of Product Availability List - Alfalfa(View Product-Product Availability- Alfalfa)", dependsOnMethods= {"visibility_ViewData_ProductAvailabilityAlfalfa"}, priority=5024)
	public void visibility_Table_ProductAvailabilityList_ProductAvailabilityAlfalfa() throws Exception {
		isTestCasePassed=true;
		tableText =  null;
		productAvail.get_ViewMoreLink(widget_Alfalfa_ProdAvail).click();
		WebElement tbl_Alfalfa = productAvail.get_CropTable("Alfalfa");
		if(productAvail.get_RowHeader(tbl_Alfalfa)!= null)
			objLgr.info("Product List 'Alfalfa' availability table loaded successfully");
		else {
			WebElement noDataAvailable = productAvail.get_NoDataTag("Alfalfa");
			if(noDataAvailable!=null) {
				objLgr.error("No Data Available message is dispalyed in Product Availability- Alfalfa table");
				tableText = "No Data Available";
			}
			else {
				isTestCasePassed=false;
				objLgr.error("Try Again message is dispalayed  in Product Availabilityc- Alfalfa table");
				tableText = "Try Again";
			}
		}
		if (!isTestCasePassed)
			Assert.fail("Product List 'Alfalfa' availability table visibility failed");

	}

	@Test(enabled=false, description = "Validate Sorting functionality validation in Product Availability List - Alfalfa table", dependsOnMethods= {"visibility_Table_ProductAvailabilityList_ProductAvailabilityAlfalfa"}, priority=5025)
	public void sort_ProductAvailabilityList_ProductAvailabilityAlfalfa() throws Exception {
		isTestCasePassed=true;
		String message = "";
		if(tableText == null) {
			String tableName = productAvail.get_TableHeader().get(productAvail.get_TableHeader().size()-1).getText();
			WebElement tbl_Alfalfa = productAvail.get_CropTable("Alfalfa");
			List<WebElement> row_HeaderList = productAvail.get_RowHeader(tbl_Alfalfa);
			for (WebElement header_Field : row_HeaderList) {
				String txt_Field = header_Field.getText();
				objLgr.info("Sorting has been started for field - "+txt_Field);
				for(int i=0;i<=1;i++) {
					List<WebElement> fields_Sort = productAvail.get_SortField(txt_Field);
					WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
					field_Sort.click();
					List<WebElement> row_Header = productAvail.get_RowHeader(tbl_Alfalfa);
					List<WebElement> row_Data = productAvail.get_RowData(tbl_Alfalfa);
					List<Hashtable<String, String>> SelectedCropData = commonPageMyAccount.read_TableData(row_Header, row_Data, productAvail.get_fieldData());
					String sort_Order = field_Sort.getAttribute("aria-sort");
					if (commonPageMyAccount.sort_TableData(SelectedCropData, txt_Field, sort_Order))
						message = "Sorting has been validated successfully in "+tableName+" tab for field " +txt_Field+ " in " + sort_Order + "order.";
					else {
						isTestCasePassed=false;
						message = "Sorting has been failed in "+ tableName +" tab for field " +txt_Field+ " in " + sort_Order + "order.";
					}
					objLgr.info(message);
				}
			}
		}
		else if(tableText.equalsIgnoreCase("No Data Available")) {
			objLgr.error("No Data Available message is dispalyed in Product Availability- Alfalfa table");
			throw new SkipException("Skipping Sorting TC on Product Availability - Alfalfa");
		}
		else {
			objLgr.error("Try Again error message is dispalyed in Product Availability- Alfalfa table");
			isTestCasePassed=false;
		}
		if (!isTestCasePassed)
			Assert.fail("sort_ProductAvailabilityList_ProductAvailabilityAlfalfa has failed -" +message);
	}

	@Test(enabled=false, description = "Validate download functionality visibility in Product Availability List - Alfalfa table", dependsOnMethods= {"visibility_Table_ProductAvailabilityList_ProductAvailabilityAlfalfa"}, priority=5026)
	public void download_Visibility_ProductAvailabilityList_ProductAvailabilityAlfalfa() throws Exception {
		isTestCasePassed=true;
		if(tableText == null) {
			if(productAvail.get_download_Button().isEnabled())
				objLgr.info("Download button in Alfalfa Availability list table is enabled");
			else {
				isTestCasePassed=false;
				objLgr.error("Download button in Alfalfa Availability list table is disabled");
			}
		}
		else if(tableText.equalsIgnoreCase("No Data Available")) {
			objLgr.error("No Data Available message is dispalyed in Product Availability- Alfalfa table");
			throw new SkipException("Skipping Sorting TC on Product Availability - Alfalfa");
		}
		else {
			objLgr.error("Try Again error message is dispalyed in Product Availability- Alfalfa table");
			isTestCasePassed=false;
		}
		if (!isTestCasePassed)
			Assert.fail("Download button in Alfalfa availability table visibility failed");

	}

	@Test(enabled=false, description = "Validate Trait Drop-down functionality visibility in Product Availability List - Alfalfa table", dependsOnMethods= {"visibility_Table_ProductAvailabilityList_ProductAvailabilityAlfalfa"}, priority=5027)
	public void traitDropDown_Visibility_ProductAvailabilityList_ProductAvailabilityAlfalfa() throws Exception {
		isTestCasePassed=true;
		String dropdown_Text = "Trait";
		if(tableText == null) {
			random_row = reusefunc.get_RandomInteger(0, 9, "1");
			objLgr.info("Random row no: "+random_row+" data will be fetched");
			WebElement tbl_crop = productAvail.get_CropTable("Alfalfa");
			List<WebElement> header = productAvail.get_RowHeader(tbl_crop);
			List<WebElement> data = productAvail.get_RowData(tbl_crop);
			Hashtable<String, String> random_Data = commonPageMyAccount.read_TableData(header, data, productAvail.get_fieldData(),random_row);
			String value = random_Data.get(dropdown_Text).toString();
			if(!value.isEmpty()) {
				productAvail.filter_TextBox(dropdown_Text).sendKeys(value);
				productAvail.filter_TextBox(dropdown_Text).sendKeys(Keys.ENTER);
				if(productAvail.get_RowHeader(tbl_crop)!=null) {
					for(int i =0;i<2;i++) {
						List<WebElement> fields_Sort = productAvail.get_SortField(dropdown_Text);
						WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
						field_Sort.click();
						objLgr.info("Sorting action has been performed on "+field_Sort.getText());
						List<WebElement> header_data = productAvail.get_RowHeader(tbl_crop);
						List<WebElement> row_data = productAvail.get_RowData(tbl_crop);
						Hashtable<String, String> selectedData = commonPageMyAccount.read_TableData(header_data, row_data, productAvail.get_fieldData(),0);
						if(selectedData.get(dropdown_Text).equalsIgnoreCase(value)) 
							objLgr.info("First value from "+dropdown_Text+" matches with the drop down option selected");
						else {
							isTestCasePassed = false;
							objLgr.error("****Mismatch found****\nExpected Value :"+value+"\nActual value :"+selectedData.get(dropdown_Text).toString());
						}
					}
				}
				else {
					isTestCasePassed = false;
					WebElement noDataAvailable = productAvail.get_NoDataTag("Alfalfa");
					if(noDataAvailable!=null) 
						objLgr.error("No Data Available message is dispalyed for the" +dropdown_Text+" value :"+value);
					else
						objLgr.error("Try Again message is dispalayed");
				}
				productAvail.get_filterClose(dropdown_Text).click();
			}
			else {
				isTestCasePassed = false;
				objLgr.error(dropdown_Text+" value fetched from random row is blank");
			}
		}
		else if(tableText.equalsIgnoreCase("No Data Available")) {
			objLgr.error("No Data Available message is dispalyed in Product Availability- Alfalfa table");
			throw new SkipException("Skipping Sorting TC on Product Availability - Alfalfa");
		}
		else {
			objLgr.error("Try Again error message is dispalyed in Product Availability- Alfalfa table");
			isTestCasePassed=false;
		}
		if (!isTestCasePassed)
			Assert.fail(dropdown_Text+" drop down button in Alfalfa availability table validation failed");
	}

	@Test(enabled=false, description = "Validate Product Drop-down functionality visibility in Product Availability List - Alfalfa table", dependsOnMethods= {"visibility_Table_ProductAvailabilityList_ProductAvailabilityAlfalfa"}, priority=5028)
	public void productDropDown_Visibility_ProductAvailabilityList_ProductAvailabilityAlfalfa() throws Exception {
		isTestCasePassed=true;
		String dropdown_Text = "Product";
		if(tableText == null) {
			random_row = 0;
			random_row = reusefunc.get_RandomInteger(0, 9, "1");
			objLgr.info("Random row no: "+random_row+" data will be fetched");
			WebElement tbl_crop = productAvail.get_CropTable("Alfalfa");
			List<WebElement> header = productAvail.get_RowHeader(tbl_crop);
			List<WebElement> data = productAvail.get_RowData(tbl_crop);
			Hashtable<String, String> random_Data = commonPageMyAccount.read_TableData(header, data, productAvail.get_fieldData(),random_row);
			String value = random_Data.get(dropdown_Text).toString();
			if(!value.isEmpty()) {
				productAvail.filter_TextBox(dropdown_Text).sendKeys(value);
				productAvail.filter_TextBox(dropdown_Text).sendKeys(Keys.ENTER);
				if(productAvail.get_RowHeader(tbl_crop)!=null) {
					for(int i =0;i<2;i++) {
						List<WebElement> fields_Sort = productAvail.get_SortField(dropdown_Text);
						WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
						field_Sort.click();
						objLgr.info("Sorting action has been performed on "+field_Sort.getText());
						List<WebElement> header_data = productAvail.get_RowHeader(tbl_crop);
						List<WebElement> row_data = productAvail.get_RowData(tbl_crop);
						Hashtable<String, String> selectedData = commonPageMyAccount.read_TableData(header_data, row_data, productAvail.get_fieldData(),0);
						if(selectedData.get(dropdown_Text).equalsIgnoreCase(value)) 
							objLgr.info("First value from "+dropdown_Text+" matches with the drop down option selected");
						else {
							isTestCasePassed = false;
							objLgr.error("****Mismatch found****\nExpected Value :"+value+"\nActual value :"+selectedData.get(dropdown_Text).toString());
						}
					}
				}
				else {
					isTestCasePassed = false;
					WebElement noDataAvailable = productAvail.get_NoDataTag("Alfalfa");
					if(noDataAvailable!=null) 
						objLgr.error("No Data Available message is dispalyed for the" +dropdown_Text+" value :"+value);
					else
						objLgr.error("Try Again message is dispalayed");
				}
				productAvail.get_filterClose(dropdown_Text).click();
			}
			else {
				isTestCasePassed = false;
				objLgr.error(dropdown_Text+" value fetched from random row is blank");
			}
		}
		else if(tableText.equalsIgnoreCase("No Data Available")) {
			objLgr.error("No Data Available message is dispalyed in Product Availability- Alfalfa table");
			throw new SkipException("Skipping Sorting TC on Product Availability - Alfalfa");
		}
		else {
			objLgr.error("Try Again error message is dispalyed in Product Availability- Alfalfa table");
			isTestCasePassed=false;
		}
		if (!isTestCasePassed)
			Assert.fail(dropdown_Text+" drop down button in Alfalfa availability table validation failed");
	}

	@Test(enabled=false,description = "Validate that back button works from ProductAvailability-Alfalfa", dependsOnMethods= {"productDropDown_Visibility_ProductAvailabilityList_ProductAvailabilityAlfalfa"}, priority=5029)
	public void back_ProductAvailabilityAlfalfa() throws Exception {
		isTestCasePassed=true;
		productAvail.getBackArrowoption().click();
		if(productAvail.get_ViewMoreLink(productAvail.get_ProductAvailabilityAlfalfa())!= null)
			objLgr.info("Product Availability-Alfalfa widget's view product button is visible");
		else {
			isTestCasePassed=false;
			objLgr.error("Product Availability-Alfalfa widget's view product button is not visible");
		}
		if (!isTestCasePassed)
			Assert.fail("back_ProductAvailabilityAlfalfa failed.");
	}

	@Test(enabled=false, description = "To check the visibility of Product Availability- Canola", dependsOnMethods= {"back_ProductAvailabilityAlfalfa"}, alwaysRun=true, priority=5030)
	public void visibility_ViewData_ProductAvailabilityCanola() throws Exception {
		isTestCasePassed=true;
		leftNavigation_ProductAvailability();
		widget_Canola_ProdAvail = productAvail.get_ProductAvailabilityCanola();
		if(productAvail.get_ViewMoreLink(widget_Canola_ProdAvail)!= null)
			objLgr.info("Product Availability-Canola widget's view product button is visible");
		else {
			isTestCasePassed=false;
			objLgr.error("Product Availability-Canola widget's view product button is not visible");
		}
		if (!isTestCasePassed)
			Assert.fail("Product Availability-Canola widget's view product button visibility failed");
	}

	@Test(enabled=false, description = "To check the visibility of Product Availability List - Canola(View Product-Product Availability- Canola)", dependsOnMethods= {"visibility_ViewData_ProductAvailabilityCanola"}, priority=5031)
	public void visibility_Table_ProductAvailabilityList_ProductAvailabilityCanola() throws Exception {
		isTestCasePassed=true;
		tableText = null;
		productAvail.get_ViewMoreLink(widget_Canola_ProdAvail).click();
		WebElement tbl_Canola = productAvail.get_CropTable("Canola");
		if(productAvail.get_RowHeader(tbl_Canola)!= null)
			objLgr.info("Product List 'Canola' availability table loaded successfully");
		else {
			WebElement noDataAvailable = productAvail.get_NoDataTag("Canola");
			if(noDataAvailable!=null) {
				objLgr.error("No Data Available message is dispalyed in Product Availability- Canola table");
				tableText = "No Data Available";
			}
			else {
				isTestCasePassed=false;
				objLgr.error("Try Again message is dispalayed  in Product Availabilityc- Canola table");
				tableText = "Try Again";				
			}
		}
		if (!isTestCasePassed)
			Assert.fail("Product List 'Canola' availability table visibility failed");

	}

	@Test(enabled=false, description = "Validate Sorting functionality validation in Product Availability List - Canola table", dependsOnMethods= {"visibility_Table_ProductAvailabilityList_ProductAvailabilityCanola"}, priority=5032)
	public void sort_ProductAvailabilityList_ProductAvailabilityCanola() throws Exception {
		isTestCasePassed=true;
		String message = "";
		if(tableText == null) {
			String tableName = productAvail.get_TableHeader().get(productAvail.get_TableHeader().size()-1).getText();
			WebElement tbl_Canola = productAvail.get_CropTable("Canola");
			List<WebElement> row_HeaderList = productAvail.get_RowHeader(tbl_Canola);
			for (WebElement header_Field : row_HeaderList) {
				String txt_Field = header_Field.getText();
				objLgr.info("Sorting has been started for field - "+txt_Field);
				for(int i=0;i<=1;i++) {
					List<WebElement> fields_Sort = productAvail.get_SortField(txt_Field);
					WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
					field_Sort.click();
					List<WebElement> row_Header = productAvail.get_RowHeader(tbl_Canola);
					List<WebElement> row_Data = productAvail.get_RowData(tbl_Canola);
					List<Hashtable<String, String>> SelectedCropData = commonPageMyAccount.read_TableData(row_Header, row_Data, productAvail.get_fieldData());
					String sort_Order = field_Sort.getAttribute("aria-sort");
					if (commonPageMyAccount.sort_TableData(SelectedCropData, txt_Field, sort_Order))
						message = "Sorting has been validated successfully in "+tableName+" tab for field " +txt_Field+ " in " + sort_Order + "order.";
					else {
						isTestCasePassed=false;
						message = "Sorting has been failed in "+ tableName +" tab for field " +txt_Field+ " in " + sort_Order + "order.";
					}
					objLgr.info(message);
				}
			}
		}
		else if(tableText.equalsIgnoreCase("No Data Available")) {
			objLgr.error("No Data Available message is dispalyed in Product Availability- Canola table");
			throw new SkipException("Skipping Sorting TC on Product Availability - Canola");
		}
		else {
			objLgr.error("Try Again error message is dispalyed in Product Availability- Canola table");
			isTestCasePassed=false;
		}
		if (!isTestCasePassed)
			Assert.fail("sort_ProductAvailabilityList_ProductAvailabilityCanola has failed -" +message);
	}

	@Test(enabled=false, description = "Validate download functionality visibility in Product Availability List - Canola table", dependsOnMethods= {"visibility_Table_ProductAvailabilityList_ProductAvailabilityCanola"}, priority=5033)
	public void download_Visibility_ProductAvailabilityList_ProductAvailabilityCanola() throws Exception {
		isTestCasePassed=true;
		if(tableText == null) {
			if(productAvail.get_download_Button().isEnabled())
				objLgr.info("Download button in Canola Availability list table is enabled");
			else {
				isTestCasePassed=false;
				objLgr.error("Download button in Canola Availability list table is disabled");
			}
		}
		else if(tableText.equalsIgnoreCase("No Data Available")) {
			objLgr.error("No Data Available message is dispalyed in Product Availability- Canola table");
			throw new SkipException("Skipping Sorting TC on Product Availability - Canola");
		}
		else {
			objLgr.error("Try Again error message is dispalyed in Product Availability- Canola table");
			isTestCasePassed=false;
		}
		if (!isTestCasePassed)
			Assert.fail("Download button in Canola availability table visibility failed");

	}

	@Test(enabled=false, description = "Validate Trait Drop-down functionality visibility in Product Availability List - Canola table", dependsOnMethods= {"visibility_Table_ProductAvailabilityList_ProductAvailabilityCanola"}, priority=5034)
	public void traitDropDown_Visibility_ProductAvailabilityList_ProductAvailabilityCanola() throws Exception {
		isTestCasePassed=true;
		String dropdown_Text = "Trait";
		if(tableText == null) {
			random_row = reusefunc.get_RandomInteger(0, 9, "1");
			objLgr.info("Random row no: "+random_row+" data will be fetched");
			WebElement tbl_crop = productAvail.get_CropTable("Canola");
			List<WebElement> header = productAvail.get_RowHeader(tbl_crop);
			List<WebElement> data = productAvail.get_RowData(tbl_crop);
			Hashtable<String, String> random_Data = commonPageMyAccount.read_TableData(header, data, productAvail.get_fieldData(),random_row);
			String value = random_Data.get(dropdown_Text).toString();
			if(!value.isEmpty()) {
				productAvail.filter_TextBox(dropdown_Text).sendKeys(value);
				productAvail.filter_TextBox(dropdown_Text).sendKeys(Keys.ENTER);
				if(productAvail.get_RowHeader(tbl_crop)!=null) {
					for(int i =0;i<2;i++) {
						List<WebElement> fields_Sort = productAvail.get_SortField(dropdown_Text);
						WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
						field_Sort.click();
						objLgr.info("Sorting action has been performed on "+field_Sort.getText());
						List<WebElement> header_data = productAvail.get_RowHeader(tbl_crop);
						List<WebElement> row_data = productAvail.get_RowData(tbl_crop);
						Hashtable<String, String> selectedData = commonPageMyAccount.read_TableData(header_data, row_data, productAvail.get_fieldData(),0);
						if(selectedData.get(dropdown_Text).equalsIgnoreCase(value)) 
							objLgr.info("First value from "+dropdown_Text+" matches with the drop down option selected");
						else {
							isTestCasePassed = false;
							objLgr.error("****Mismatch found****\nExpected Value :"+value+"\nActual value :"+selectedData.get(dropdown_Text).toString());
						}
					}
				}
				else {
					isTestCasePassed = false;
					WebElement noDataAvailable = productAvail.get_NoDataTag("Canola");
					if(noDataAvailable!=null) 
						objLgr.error("No Data Available message is dispalyed for the" +dropdown_Text+" value :"+value);
					else
						objLgr.error("Try Again message is dispalayed");
				}
				productAvail.get_filterClose(dropdown_Text).click();
			}
			else {
				isTestCasePassed = false;
				objLgr.error(dropdown_Text+" value fetched from random row is blank");
			}
		}
		else if(tableText.equalsIgnoreCase("No Data Available")) {
			objLgr.error("No Data Available message is dispalyed in Product Availability- Canola table");
			throw new SkipException("Skipping Sorting TC on Product Availability - Canola");
		}
		else {
			objLgr.error("Try Again error message is dispalyed in Product Availability- Canola table");
			isTestCasePassed=false;
		}
		if (!isTestCasePassed)
			Assert.fail(dropdown_Text+" drop down button in Canola availability table validation failed");
	}

	@Test(enabled=false, description = "Validate Product Drop-down functionality visibility in Product Availability List - Canola table", dependsOnMethods= {"visibility_Table_ProductAvailabilityList_ProductAvailabilityCanola"}, priority=5035)
	public void productDropDown_Visibility_ProductAvailabilityList_ProductAvailabilityCanola() throws Exception {
		isTestCasePassed=true;
		String dropdown_Text = "Product";
		if(tableText == null) {
			random_row = 0;
			random_row = reusefunc.get_RandomInteger(0, 9, "1");
			objLgr.info("Random row no: "+random_row+" data will be fetched");
			WebElement tbl_crop = productAvail.get_CropTable("Canola");
			List<WebElement> header = productAvail.get_RowHeader(tbl_crop);
			List<WebElement> data = productAvail.get_RowData(tbl_crop);
			Hashtable<String, String> random_Data = commonPageMyAccount.read_TableData(header, data, productAvail.get_fieldData(),random_row);
			String value = random_Data.get(dropdown_Text).toString();
			if(!value.isEmpty()) {
				productAvail.filter_TextBox(dropdown_Text).sendKeys(value);
				productAvail.filter_TextBox(dropdown_Text).sendKeys(Keys.ENTER);
				if(productAvail.get_RowHeader(tbl_crop)!=null) {
					for(int i =0;i<2;i++) {
						List<WebElement> fields_Sort = productAvail.get_SortField(dropdown_Text);
						WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
						field_Sort.click();
						objLgr.info("Sorting action has been performed on "+field_Sort.getText());
						List<WebElement> header_data = productAvail.get_RowHeader(tbl_crop);
						List<WebElement> row_data = productAvail.get_RowData(tbl_crop);
						Hashtable<String, String> selectedData = commonPageMyAccount.read_TableData(header_data, row_data, productAvail.get_fieldData(),0);
						if(selectedData.get(dropdown_Text).equalsIgnoreCase(value)) 
							objLgr.info("First value from "+dropdown_Text+" matches with the drop down option selected");
						else {
							isTestCasePassed = false;
							objLgr.error("****Mismatch found****\nExpected Value :"+value+"\nActual value :"+selectedData.get(dropdown_Text).toString());
						}
					}
				}
				else {
					isTestCasePassed = false;
					WebElement noDataAvailable = productAvail.get_NoDataTag("Canola");
					if(noDataAvailable!=null) 
						objLgr.error("No Data Available message is dispalyed for the" +dropdown_Text+" value :"+value);
					else
						objLgr.error("Try Again message is dispalayed");
				}
				productAvail.get_filterClose(dropdown_Text).click();
			}
			else {
				isTestCasePassed = false;
				objLgr.error(dropdown_Text+" value fetched from random row is blank");
			}
		}
		else if(tableText.equalsIgnoreCase("No Data Available")) {
			objLgr.error("No Data Available message is dispalyed in Product Availability- Canola table");
			throw new SkipException("Skipping Sorting TC on Product Availability - Canola");
		}
		else {
			objLgr.error("Try Again error message is dispalyed in Product Availability- Canola table");
			isTestCasePassed=false;
		}
		if (!isTestCasePassed)
			Assert.fail(dropdown_Text+" drop down button in Canola availability table validation failed");
	}

	@Test(enabled=false,description = "Validate that back button works from ProductAvailability-Canola", dependsOnMethods= {"productDropDown_Visibility_ProductAvailabilityList_ProductAvailabilityCanola"}, priority=5036)
	public void back_ProductAvailabilityCanola() throws Exception {
		isTestCasePassed=true;
		productAvail.getBackArrowoption().click();
		if(productAvail.get_ViewMoreLink(productAvail.get_ProductAvailabilityCanola())!= null)
			objLgr.info("Product Availability-Canola widget's view product button is visible");
		else {
			isTestCasePassed=false;
			objLgr.error("Product Availability-Canola widget's view product button is not visible");
		}
		if (!isTestCasePassed)
			Assert.fail("back_ProductAvailabilityCanola failed.");
	}

	@Test(enabled=false, description = "To check the visibility of Product Availability- Canola", dependsOnMethods= {"back_ProductAvailabilityCanola"}, alwaysRun=true, priority=5037)
	public void visibility_ViewData_ProductAvailabilitySorghum() throws Exception {
		isTestCasePassed=true;
		leftNavigation_ProductAvailability();
		widget_Sorghum_ProdAvail = productAvail.get_ProductAvailabilitySorghum();
		if(productAvail.get_ViewMoreLink(widget_Sorghum_ProdAvail)!= null)
			objLgr.info("Product Availability-Sorghum widget's view product button is visible");
		else {
			isTestCasePassed=false;
			objLgr.error("Product Availability-Sorghum widget's view product button is not visible");
		}
		if (!isTestCasePassed)
			Assert.fail("Product Availability-Sorghum widget's view product button visibility failed");
	}

	@Test(enabled=false, description = "To check the visibility of Product Availability List - Sorghum(View Product-Product Availability- Sorghum)", dependsOnMethods= {"visibility_ViewData_ProductAvailabilitySorghum"}, priority=5038)
	public void visibility_Table_ProductAvailabilityList_ProductAvailabilitySorghum() throws Exception {
		isTestCasePassed=true;
		tableText = null;
		productAvail.get_ViewMoreLink(widget_Sorghum_ProdAvail).click();
		WebElement tbl_Sorghum = productAvail.get_CropTable("Sorghum");
		if(productAvail.get_RowHeader(tbl_Sorghum)!= null)
			objLgr.info("Product List 'Sorghum' availability table loaded successfully");
		else {
			WebElement noDataAvailable = productAvail.get_NoDataTag("Sorghum");
			if(noDataAvailable!=null) {
				objLgr.error("No Data Available message is dispalyed in Product Availability- Sorghum table");
				tableText = "No Data Available";
			}
			else {
				isTestCasePassed=false;
				objLgr.error("Try Again message is dispalayed  in Product Availabilityc- Sorghum table");
				tableText = "Try Again";
			}
		}
		if (!isTestCasePassed)
			Assert.fail("Product List 'Sorghum' availability table visibility failed");

	}

	@Test(enabled=false, description = "Validate Sorting functionality validation in Product Availability List - Canola table", dependsOnMethods= {"visibility_Table_ProductAvailabilityList_ProductAvailabilitySorghum"}, priority=5039)
	public void sort_ProductAvailabilityList_ProductAvailabilitySorghum() throws Exception {
		isTestCasePassed=true;
		String message = "";
		if(tableText == null) {
			String tableName = productAvail.get_TableHeader().get(productAvail.get_TableHeader().size()-1).getText();
			WebElement tbl_Sorghum = productAvail.get_CropTable("Sorghum");
			List<WebElement> row_HeaderList = productAvail.get_RowHeader(tbl_Sorghum);
			for (WebElement header_Field : row_HeaderList) {
				String txt_Field = header_Field.getText();
				objLgr.info("Sorting has been started for field - "+txt_Field);
				for(int i=0;i<=1;i++) {
					List<WebElement> fields_Sort = productAvail.get_SortField(txt_Field);
					WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
					field_Sort.click();
					List<WebElement> row_Header = productAvail.get_RowHeader(tbl_Sorghum);
					List<WebElement> row_Data = productAvail.get_RowData(tbl_Sorghum);
					List<Hashtable<String, String>> SelectedCropData = commonPageMyAccount.read_TableData(row_Header, row_Data, productAvail.get_fieldData());
					String sort_Order = field_Sort.getAttribute("aria-sort");
					if (commonPageMyAccount.sort_TableData(SelectedCropData, txt_Field, sort_Order))
						message = "Sorting has been validated successfully in "+tableName+" tab for field " +txt_Field+ " in " + sort_Order + "order.";
					else {
						isTestCasePassed=false;
						message = "Sorting has been failed in "+ tableName +" tab for field " +txt_Field+ " in " + sort_Order + "order.";
					}
					objLgr.info(message);
				}
			}
		}
		else if(tableText.equalsIgnoreCase("No Data Available")) {
			objLgr.error("No Data Available message is dispalyed in Product Availability- Sorghum table");
			throw new SkipException("Skipping Sorting TC on Product Availability - Sorghum");
		}
		else {
			objLgr.error("Try Again error message is dispalyed in Product Availability- Sorghum table");
			isTestCasePassed=false;
		}
		if (!isTestCasePassed)
			Assert.fail("sort_ProductAvailabilityList_ProductAvailabilityCanola has failed -" +message);
	}

	@Test(enabled=false, description = "Validate download functionality visibility in Product Availability List - Sorghum table", dependsOnMethods= {"visibility_Table_ProductAvailabilityList_ProductAvailabilitySorghum"}, priority=5024)
	public void download_Visibility_ProductAvailabilityList_ProductAvailabilitySorghum() throws Exception {
		isTestCasePassed=true;
		if(tableText == null) {
			if(productAvail.get_download_Button().isEnabled())
				objLgr.info("Download button in Sorghum Availability list table is enabled");
			else {
				isTestCasePassed=false;
				objLgr.error("Download button in Sorghum Availability list table is disabled");
			}
		}
		else if(tableText.equalsIgnoreCase("No Data Available")) {
			objLgr.error("No Data Available message is dispalyed in Product Availability- Sorghum table");
			throw new SkipException("Skipping Sorting TC on Product Availability - Sorghum");
		}
		else {
			objLgr.error("Try Again error message is dispalyed in Product Availability- Sorghum table");
			isTestCasePassed=false;
		}
		if (!isTestCasePassed)
			Assert.fail("Download button in Sorghum availability table visibility failed");

	}

	@Test(enabled=false, description = "Validate Trait Drop-down functionality visibility in Product Availability List - Sorghum table", dependsOnMethods= {"visibility_Table_ProductAvailabilityList_ProductAvailabilitySorghum"}, priority=5040)
	public void traitDropDown_Visibility_ProductAvailabilityList_ProductAvailabilitySorghum() throws Exception {
		isTestCasePassed=true;
		String dropdown_Text = "Trait";
		if(tableText == null) {
			random_row = reusefunc.get_RandomInteger(0, 9, "1");
			objLgr.info("Random row no: "+random_row+" data will be fetched");
			WebElement tbl_crop = productAvail.get_CropTable("Sorghum");
			List<WebElement> header = productAvail.get_RowHeader(tbl_crop);
			List<WebElement> data = productAvail.get_RowData(tbl_crop);
			Hashtable<String, String> random_Data = commonPageMyAccount.read_TableData(header, data, productAvail.get_fieldData(),random_row);
			String value = random_Data.get(dropdown_Text).toString();
			if(!value.isEmpty()) {
				productAvail.filter_TextBox(dropdown_Text).sendKeys(value);
				productAvail.filter_TextBox(dropdown_Text).sendKeys(Keys.ENTER);
				if(productAvail.get_RowHeader(tbl_crop)!=null) {
					for(int i =0;i<2;i++) {
						List<WebElement> fields_Sort = productAvail.get_SortField(dropdown_Text);
						WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
						field_Sort.click();
						objLgr.info("Sorting action has been performed on "+field_Sort.getText());
						List<WebElement> header_data = productAvail.get_RowHeader(tbl_crop);
						List<WebElement> row_data = productAvail.get_RowData(tbl_crop);
						Hashtable<String, String> selectedData = commonPageMyAccount.read_TableData(header_data, row_data, productAvail.get_fieldData(),0);
						if(selectedData.get(dropdown_Text).equalsIgnoreCase(value)) 
							objLgr.info("First value from "+dropdown_Text+" matches with the drop down option selected");
						else {
							isTestCasePassed = false;
							objLgr.error("****Mismatch found****\nExpected Value :"+value+"\nActual value :"+selectedData.get(dropdown_Text).toString());
						}
					}
				}
				else {
					isTestCasePassed = false;
					WebElement noDataAvailable = productAvail.get_NoDataTag("Sorghum");
					if(noDataAvailable!=null) 
						objLgr.error("No Data Available message is dispalyed for the" +dropdown_Text+" value :"+value);
					else
						objLgr.error("Try Again message is dispalayed");
				}
				productAvail.get_filterClose(dropdown_Text).click();
			}
			else {
				isTestCasePassed = false;
				objLgr.error(dropdown_Text+" value fetched from random row is blank");
			}
		}
		else if(tableText.equalsIgnoreCase("No Data Available")) {
			objLgr.error("No Data Available message is dispalyed in Product Availability- Sorghum table");
			throw new SkipException("Skipping Sorting TC on Product Availability - Sorghum");
		}
		else {
			objLgr.error("Try Again error message is dispalyed in Product Availability- Sorghum table");
			isTestCasePassed=false;
		}
		if (!isTestCasePassed)
			Assert.fail(dropdown_Text+" drop down button in Sorghum availability table validation failed");
	}

	@Test(enabled=false, description = "Validate Product Drop-down functionality visibility in Product Availability List - Sorghum table", dependsOnMethods= {"visibility_Table_ProductAvailabilityList_ProductAvailabilitySorghum"}, priority=5041)
	public void productDropDown_Visibility_ProductAvailabilityList_ProductAvailabilitySorghum() throws Exception {
		isTestCasePassed=true;
		String dropdown_Text = "Product";
		if(tableText == null) {
			random_row = reusefunc.get_RandomInteger(0, 9, "1");
			objLgr.info("Random row no: "+random_row+" data will be fetched");
			WebElement tbl_crop = productAvail.get_CropTable("Sorghum");
			List<WebElement> header = productAvail.get_RowHeader(tbl_crop);
			List<WebElement> data = productAvail.get_RowData(tbl_crop);
			Hashtable<String, String> random_Data = commonPageMyAccount.read_TableData(header, data, productAvail.get_fieldData(),random_row);
			String value = random_Data.get(dropdown_Text).toString();
			if(!value.isEmpty()) {
				productAvail.filter_TextBox(dropdown_Text).sendKeys(value);
				productAvail.filter_TextBox(dropdown_Text).sendKeys(Keys.ENTER);
				if(productAvail.get_RowHeader(tbl_crop)!=null) {
					for(int i =0;i<2;i++) {
						List<WebElement> fields_Sort = productAvail.get_SortField(dropdown_Text);
						WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
						field_Sort.click();
						objLgr.info("Sorting action has been performed on "+field_Sort.getText());
						List<WebElement> header_data = productAvail.get_RowHeader(tbl_crop);
						List<WebElement> row_data = productAvail.get_RowData(tbl_crop);
						Hashtable<String, String> selectedData = commonPageMyAccount.read_TableData(header_data, row_data, productAvail.get_fieldData(),0);
						if(selectedData.get(dropdown_Text).equalsIgnoreCase(value)) 
							objLgr.info("First value from "+dropdown_Text+" matches with the drop down option selected");
						else {
							isTestCasePassed = false;
							objLgr.error("****Mismatch found****\nExpected Value :"+value+"\nActual value :"+selectedData.get(dropdown_Text).toString());
						}
					}
				}
				else {
					isTestCasePassed = false;
					WebElement noDataAvailable = productAvail.get_NoDataTag("Sorghum");
					if(noDataAvailable!=null) 
						objLgr.error("No Data Available message is dispalyed for the" +dropdown_Text+" value :"+value);
					else
						objLgr.error("Try Again message is dispalayed");
				}
				productAvail.get_filterClose(dropdown_Text).click();
			}
			else {
				isTestCasePassed = false;
				objLgr.error(dropdown_Text+" value fetched from random row is blank");
			}
		}
		else if(tableText.equalsIgnoreCase("No Data Available")) {
			objLgr.error("No Data Available message is dispalyed in Product Availability- Sorghum table");
			throw new SkipException("Skipping Sorting TC on Product Availability - Sorghum");
		}
		else {
			objLgr.error("Try Again error message is dispalyed in Product Availability- Sorghum table");
			isTestCasePassed=false;
		}
		if (!isTestCasePassed)
			Assert.fail(dropdown_Text+" drop down button in Sorghum availability table validation failed");
	}

	@Test(enabled=false,description = "Validate that back button works from ProductAvailability-Sorghum", dependsOnMethods= {"productDropDown_Visibility_ProductAvailabilityList_ProductAvailabilitySorghum"}, priority=5042)
	public void back_ProductAvailabilitySorghum() throws Exception {
		isTestCasePassed=true;
		productAvail.getBackArrowoption().click();
		if(productAvail.get_ViewMoreLink(productAvail.get_ProductAvailabilitySorghum())!= null)
			objLgr.info("Product Availability-Sorghum widget's view product button is visible");
		else {
			isTestCasePassed=false;
			objLgr.error("Product Availability-Sorghum widget's view product button is not visible");
		}
		if (!isTestCasePassed)
			Assert.fail("back_ProductAvailabilitySorghum failed.");
	}
	
	
	//---------------------------------------------------------------Claims Report Test Cases-----------------------------------------------------//

	//Validating if Claims page is launched successfully
	@Test(enabled=false, description = "Validating that Claims Report page is loaded successfully when Claims Report URL is launched", dependsOnMethods= {"back_ProductAvailabilitySorghum"},priority=6001, alwaysRun = true)
	public void leftNavigation_ClaimsReport() throws Exception {
		isTestCasePassed=true;
		validate_LogOut();
		captureBrowserConsoleLogs();
		launchMyAccountHome();
		Thread.sleep(500);
		linkVisibility_Dashboard();
		webdriver.get("https://dev-mycrop.bayer.com/market-funding/claims");	
			if(cr.get_claimsReportTitle()!= null) {
				objLgr.info("Claims Report page loaded successfully");
				isLoginSuccessfull = true;
			}
			else
				isTestCasePassed=false;
			if (!isTestCasePassed)
				Assert.fail("Claims Report page loading unsuccessful");
		}
	
	//Validating if Claims table has loaded successfully in Claims Report page
	@Test(enabled=false, description = "Validate if Claims table loaded in Claims Report page", dependsOnMethods= {"leftNavigation_ClaimsReport"}, priority=6002)
	public void claimsTableLoad_ClaimsReport() throws Exception {
		List<WebElement> header = null;
		header = cr.get_table_Header();
		isTestCasePassed=true;
		String message = null;		
		if (header!= null) {
			String tableName = null;
			if (cr.get_Table() != null) {
				tableName = cr.get_table_Header().get(cr.get_table_Header().size()-1).getText();
				message = tableName + " table is loaded successfully";
			}
			else if (cr.get_NoDataTable(cr.get_claimsTable()) != null) {
				message = tableName + " table displays \"No Data Available\".";
			}
			else {
				isTestCasePassed = false;
				message = tableName + " table has not loaded successfully";
			}
		}
		objLgr.info(message);
		if (!isTestCasePassed)
			Assert.fail(" - " + message);
	}
	
//Validating if CY, CY-1 and CY-2 drop down options are working fine in Claims Report page
	@Test(enabled=false, description = "Validate Year drop-down in Claims Report", dependsOnMethods= {"claimsTableLoad_ClaimsReport"}, priority=6003)
	public void yearDropDown_ClaimsReport() throws Exception {
		isTestCasePassed=true;
		String year = null;
		WebElement tbl_ClaimsReport = null;
		List<WebElement> list_yearInitial = null;
		WebElement yearDropdown_Claims = cr.get_yearDropdown();
		if (yearDropdown_Claims != null) {
			yearDropdown_Claims.click();
			tbl_ClaimsReport = cr.get_claimsTable();
			list_yearInitial = cr.get_DropdownList(yearDropdown_Claims);
			yearDropdown_Claims.click();
			for (int i = 0; i < list_yearInitial.size(); i++) {
				yearDropdown_Claims.click();
				List<WebElement> list_Year = cr.get_DropdownList(yearDropdown_Claims);
				year = list_Year.get(i).getText();
				js.executeScript("arguments[0].scrollIntoView();",list_Year.get(i));
				list_Year.get(i).click();

					objLgr.info(year + " is selected in Year drop-down of Claims report table");
					List<WebElement> data_Table = cr.get_Table();
					if(data_Table.size() > 1) {
						objLgr.info("Claims Report table is loaded successfully for selected year : "+year);
					}

					else if (cr.get_NoDataTable(tbl_ClaimsReport) != null) 
						objLgr.info("Claims Report table displays \"No Data Available\". for year - " + year);
					else {
						isTestCasePassed = false;
						objLgr.error("Claims Report table is not loaded for year - " + year);
					}				
			}		
		}
		else {
			isTestCasePassed = false;
			objLgr.info("Year drop down button could not be located");
		}
		if (!isTestCasePassed)
			Assert.fail("Year drop-down list validation failed");
		else {
			objLgr.info("Successfully validated year drop down in Claims report table");
			
		}
	}

	//Validating if sort functionality works fine in Claims Report page	
	@Test(enabled=false, description = "Sorting functionality validation of Dealer Order Change Log table", dependsOnMethods= {"claimsTableLoad_ClaimsReport"}, priority=6004)
	public void sort_ClaimsReport() throws Exception {
		isTestCasePassed=true;
		String message = null;
		WebElement tbl_ClaimsReport = cr.get_claimsTable();
		List<WebElement> row_HeaderList = cr.get_RowHeader(tbl_ClaimsReport);
		for (WebElement header_Field : row_HeaderList) {
			String txt_Field = header_Field.getText();
			objLgr.info("Sorting has been started for field - "+txt_Field);
			for(int i=0;i<=1;i++) {
				List<WebElement> fields_Sort = cr.get_SortField(txt_Field);
				WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
				field_Sort.click();
				List<WebElement> row_Header = cr.get_RowHeader(tbl_ClaimsReport);
				List<WebElement> row_Data = cr.get_RowData(tbl_ClaimsReport);
				List<Hashtable<String, String>> SelectedfieldData = commonPageMyAccount.read_TableData(row_Header, row_Data, cr.get_fieldData());
				String sort_Order = field_Sort.getAttribute("aria-sort");
				if (commonPageMyAccount.sort_TableData(SelectedfieldData, txt_Field, sort_Order))
					message = "Sorting has been validated successfully in Claims Report table for field " +txt_Field+ " in " + sort_Order + "order.";
				else {
					isTestCasePassed=false;
					message = "Sorting has been failed in Claims Report table for field " +txt_Field+ " in " + sort_Order + "order.";
				}
				objLgr.info(message);
			}
		}
		if (!isTestCasePassed)
			Assert.fail("sort_ClaimsReport failed - "+message);
		else
			objLgr.info("Sorting functionality has been validated sucessfully in Claims Report table");
	}

	//Validating if search functionality works fine in Claims Report page
	@Test(enabled=false, description = "Validate search functionality in Claims Report table",dependsOnMethods= {"claimsTableLoad_ClaimsReport"}, priority=6005)
	public void search_ClaimsReport() throws Exception {
		isTestCasePassed=true;
		String message = null;
		WebElement tbl_ClaimsReport = cr.get_claimsTable();
		List<WebElement> row_HeaderList = cr.get_RowHeader(tbl_ClaimsReport);
		List<WebElement> row_Data = cr.get_RowData(tbl_ClaimsReport);
		List<Hashtable<String, String>> tableData = commonPageMyAccount.read_TableData(row_HeaderList, row_Data, cr.get_fieldData());
		if (cr.get_SearchField()!= null) {
			String search_Txt=reusefunc.get_RandomString(tableData);
			objLgr.info("Search text is identified as - "+search_Txt);
			cr.get_SearchText().sendKeys(search_Txt);
			cr.get_SearchText().sendKeys(Keys.ENTER);
			List<WebElement> row_SearchData = cr.get_RowData(tbl_ClaimsReport);
			List<Hashtable<String, String>> tableData_Search = commonPageMyAccount.read_TableData(row_HeaderList, row_SearchData, cr.get_fieldData());
			objLgr.info("Total records found in Claims Report table after searching with text " + search_Txt + " is - "+tableData_Search.size());
			if(commonPageMyAccount.isSearchText_Present(tableData_Search, search_Txt))
				message = "All Records displayed in the table contains searched text";
			else {
				isTestCasePassed = false;
				message = "All Records displayed in the table do not contain searched text";
			}
		}
		else {
			isTestCasePassed = false;
			message = "Search text field is not loaded succesfully";
		}
		objLgr.info(message);
		if (!isTestCasePassed)
			Assert.fail("search_ClaimsReport failed - " + message);
	}
	
//----------------------------------Payments from Bayer Test Cases----------------------------//
	
	//Validating if Payments from Bayer page is launched successfully
		@Test(enabled=false, description = "Validating that Payments from Bayer page is loaded successfully when Payments from Bayer URL is launched", dependsOnMethods= {"search_ClaimsReport"},priority=7001, alwaysRun = true)
		public void leftNavigation_Payments() throws Exception {
			isTestCasePassed=true;
			validate_LogOut();
			captureBrowserConsoleLogs();
			launchMyAccountHome();
			Thread.sleep(500);
			linkVisibility_Dashboard();
			webdriver.get("https://dev-mycrop.bayer.com/market-funding/payments");	
				if(pfb.get_PaymentsTitle()!= null) {
					objLgr.info("Payments from Bayer page loaded successfully");
					isLoginSuccessfull = true;
				}
				else
					isTestCasePassed=false;
				if (!isTestCasePassed)
					Assert.fail("Payments from Bayer page loading unsuccessful");
			}
	
		//Validating if Payments table has loaded successfully in Payments from Bayer page
		@Test(enabled=false, description = "Validate if Payments table loaded in Payments from Report page", dependsOnMethods= {"leftNavigation_Payments"}, priority=7002)
		public void PaymentsTableLoad_Payments() throws Exception {
			List<WebElement> header = null;
			header = pfb.get_table_Header();
			isTestCasePassed=true;
			String message = null;		
			if (header!= null) {
				String tableName = null;
				if (pfb.get_Table() != null) {
					tableName = pfb.get_table_Header().get(pfb.get_table_Header().size()-1).getText();
					message = tableName + " table is loaded successfully";
				}
				else if (pfb.get_NoDataTable(pfb.get_paymentsTable()) != null) {
					message = tableName + " table displays \"No Data Available\".";
				}
				else {
					isTestCasePassed = false;
					message = tableName + " table has not loaded successfully";
				}
			}
			objLgr.info(message);
			if (!isTestCasePassed)
				Assert.fail(" - " + message);
		}		

		//Validating if CY, CY-1 and CY-2 drop down options are working fine in Payments from Bayer page
		@Test(enabled=false, description = "Validate Year drop-down in Payments from Bayer page", dependsOnMethods= {"PaymentsTableLoad_Payments"}, priority=7003)
		public void yearDropDown_Payments() throws Exception {
			isTestCasePassed=true;
			String year = null;
			WebElement tbl_Payments = null;
			List<WebElement> list_yearInitial = null;
			WebElement yearDropdown_Payments = pfb.get_yearDropdown();
			if (yearDropdown_Payments != null) {
				yearDropdown_Payments.click();
				tbl_Payments = pfb.get_paymentsTable();
				list_yearInitial = pfb.get_DropdownList(yearDropdown_Payments);
				yearDropdown_Payments.click();
				for (int i = 0; i < list_yearInitial.size(); i++) {
					yearDropdown_Payments.click();
					List<WebElement> list_Year = pfb.get_DropdownList(yearDropdown_Payments);
					year = list_Year.get(i).getText();
					js.executeScript("arguments[0].scrollIntoView();",list_Year.get(i));
					list_Year.get(i).click();
						objLgr.info(year + " is selected in Year drop-down of Payments table");
						List<WebElement> data_Table = pfb.get_Table();
						if(data_Table.size() > 1) {
							objLgr.info("Payments table is loaded successfully for selected year : "+year);
						}

						else if (pfb.get_NoDataTable(tbl_Payments) != null) 
							objLgr.info("Payments table displays \"No Data Available\". for year - " + year);
						else {
							isTestCasePassed = false;
							objLgr.error("Payments table is not loaded for year - " + year);
						}				
				}		
			}
			else {
				isTestCasePassed = false;
				objLgr.info("Year drop down button could not be located");
			}
			if (!isTestCasePassed)
				Assert.fail("Year drop-down list validation failed");
			else {
				objLgr.info("Successfully validated year drop down in Payments table");
				
			}
		}
		
		//Validating if sort functionality works fine in Payments from Bayer page	
		@Test(enabled=false, description = "Sorting functionality validation of Payments table", dependsOnMethods= {"PaymentsTableLoad_Payments"}, priority=7004)
		public void sort_Payments() throws Exception {
			isTestCasePassed=true;
			String message = null;
			WebElement tbl_Payments = pfb.get_paymentsTable();
			List<WebElement> row_HeaderList = pfb.get_RowHeader(tbl_Payments);
			for (WebElement header_Field : row_HeaderList) {
				String txt_Field = header_Field.getText();
				objLgr.info("Sorting has been started for field - "+txt_Field);
				for(int i=0;i<=1;i++) {
					List<WebElement> fields_Sort = pfb.get_SortField(txt_Field);
					WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
					field_Sort.click();
					List<WebElement> row_Header = pfb.get_RowHeader(tbl_Payments);
					List<WebElement> row_Data = pfb.get_RowData(tbl_Payments);
					List<Hashtable<String, String>> SelectedfieldData = commonPageMyAccount.read_TableData(row_Header, row_Data, pfb.get_fieldData());
					String sort_Order = field_Sort.getAttribute("aria-sort");
					if (commonPageMyAccount.sort_TableData(SelectedfieldData, txt_Field, sort_Order))
						message = "Sorting has been validated successfully in Payments table for field " +txt_Field+ " in " + sort_Order + "order.";
					else {
						isTestCasePassed=false;
						message = "Sorting has been failed in Payments table for field " +txt_Field+ " in " + sort_Order + "order.";
					}
					objLgr.info(message);
				}
			}
			if (!isTestCasePassed)
				Assert.fail("sort_Payments failed - "+message);
			else
				objLgr.info("Sorting functionality has been validated sucessfully in Payments table");
		}
		
		//Validating if search functionality works fine in Payments from Bayer page
		@Test(enabled=false, description = "Validate search functionality in payments table",dependsOnMethods= {"PaymentsTableLoad_Payments"}, priority=7005)
		public void search_Payments() throws Exception {
			isTestCasePassed=true;
			String message = null;
			WebElement tbl_Payments = pfb.get_paymentsTable();
			List<WebElement> row_HeaderList = pfb.get_RowHeader(tbl_Payments);
			List<WebElement> row_Data = pfb.get_RowData(tbl_Payments);
			List<Hashtable<String, String>> tableData = commonPageMyAccount.read_TableData(row_HeaderList, row_Data, pfb.get_fieldData());
			if (pfb.get_SearchField()!= null) {
				String search_Txt=reusefunc.get_RandomString(tableData);
				objLgr.info("Search text is identified as - "+search_Txt);
				pfb.get_SearchText().sendKeys(search_Txt);
				pfb.get_SearchText().sendKeys(Keys.ENTER);
				List<WebElement> row_SearchData = pfb.get_RowData(tbl_Payments);
				List<Hashtable<String, String>> tableData_Search = commonPageMyAccount.read_TableData(row_HeaderList, row_SearchData, pfb.get_fieldData());
				objLgr.info("Total records found in Payments table after searching with text " + search_Txt + " is - "+tableData_Search.size());
				if(commonPageMyAccount.isSearchText_Present(tableData_Search, search_Txt))
					message = "All Records displayed in the table contains searched text";
				else {
					isTestCasePassed = false;
					message = "All Records displayed in the table do not contain searched text";
				}
			}
			else {
				isTestCasePassed = false;
				message = "Search text field is not loaded succesfully";
			}
			objLgr.info(message);
			if (!isTestCasePassed)
				Assert.fail("search_Payments failed - " + message);
		}
		
	//-------------------------------NB GPOS Test case------------------------------//
		@Test(enabled=true, description = "Validating that NB GPOS page is loaded successfully when NB GPOS Link is clicked in left navigation.", dependsOnMethods= {"linkVisibility_Dashboard"}, alwaysRun = true, priority=7006)
		public void leftNavigation_NBGPOS() throws Exception {
			isTestCasePassed=true;
//			validate_LogOut();
//			captureBrowserConsoleLogs();
//			launchMyAccountHome();
//			Thread.sleep(500);
//			linkVisibility_Dashboard();
			js.executeScript("arguments[0].scrollIntoView();", lnk.get_Reporting_dropdown());
			lnk.get_Reporting_dropdown().click();
			lnk.get_GPOS().click();
			if(gpos.get_page_header() != null)
				objLgr.info("GPOS page loaded successfully");
			else
				isTestCasePassed=false;
			if (!isTestCasePassed)
				Assert.fail("GPOS page loading unsuccessful");
			verifyPageWithApplitools(eyes);
		}

		@Test(enabled=true, description = "To check the visibility of Summary Table of GPOS page ", dependsOnMethods= {"leftNavigation_NBGPOS"}, priority=7007)
		public void visibility_SummaryTable_ProductTab() throws Exception {
			isTestCasePassed=true;
			nodataflag = false;
			if(userType.equalsIgnoreCase("NB Dealer")&&lob_selection.equalsIgnoreCase("Seed")) {
				if(gpos.get_headerTable(gpos.get_product_summary_table())!= null)
					objLgr.info("Summary table is loaded successfully");
				else {
					if(gpos.get_NoDataTable(gpos.get_product_summary_table())!=null) {
						objLgr.info("Summary page is displaying 'No Data Available' message");
						nodataflag = true;
					}
					else 
						isTestCasePassed=false;
					if (!isTestCasePassed)
						Assert.fail("Summary table is not loaded successfully");
				}
			}
			else  {
				objLgr.info("GPOS testcases will not be applicable for selected lob"+lob_selection+"; skipping the test case");
				throw new SkipException("GPOS TestCases will not apply to selected line of business");
			}
		}

		//Validating if sort functionality works fine in GPOS page	
		@Test(enabled=false, description = "Sorting functionality validation of Summary table", dependsOnMethods= {"visibility_SummaryTable_ProductTab"}, priority=7008)
		public void sort_SummaryTable_GPOS() throws Exception {
			isTestCasePassed=true;
			String message = null;
			WebElement tbl_summary = gpos.get_product_summary_table();
			List<WebElement> row_HeaderList = cr.get_RowHeader(tbl_summary);
			for (WebElement header_Field : row_HeaderList) {
				String txt_Field = header_Field.getText();
				objLgr.info("Sorting has been started for field - "+txt_Field);
				for(int i=0;i<=1;i++) {
					List<WebElement> fields_Sort = gpos.get_SortField(txt_Field);
					WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
					field_Sort.click();
					List<WebElement> row_Header = gpos.get_RowHeader(tbl_summary);
					List<WebElement> row_Data = gpos.get_RowData(tbl_summary);
					List<Hashtable<String, String>> SelectedfieldData = commonPageMyAccount.read_TableData(row_Header, row_Data, gpos.get_fieldData());
					String sort_Order = field_Sort.getAttribute("aria-sort");
					if (commonPageMyAccount.sort_TableData(SelectedfieldData, txt_Field, sort_Order))
						message = "Sorting has been validated successfully in Summary table for field " +txt_Field+ " in " + sort_Order + "order.";
					else {
						isTestCasePassed=false;
						message = "Sorting has been failed in Summary table for field " +txt_Field+ " in " + sort_Order + "order.";
					}
					objLgr.info(message);
				}
			}
			if (!isTestCasePassed)
				Assert.fail("sort_SummaryTable_GPOS failed - "+message);
			else
				objLgr.info("Sorting functionality has been validated sucessfully in Summary table");
			}
		

		@Test(enabled=true,description = "Validate drop-down options in Summary table of product tab", dependsOnMethods= {"visibility_SummaryTable_ProductTab"}, priority=7009)
		public void dropdownOptions_SummaryTable_Product() throws Exception {
			isTestCasePassed=true;
			WebElement Dropdown_reportselection = gpos.get_dropDown_reportSelection();
			if (Dropdown_reportselection != null) {
				Dropdown_reportselection.click();
				List<WebElement> list_reportSelectionInitial = gpos.get_dropDownlist_reportSelection();
				Dropdown_reportselection.click();
				for (int i = 0; i < list_reportSelectionInitial.size(); i++) {
					Dropdown_reportselection.click();
					List<WebElement> list_ReportSelection = gpos.get_dropDownlist_reportSelection();
					String reportSelection_option = list_ReportSelection.get(i).getText();
					list_ReportSelection.get(i).click();
					objLgr.info(reportSelection_option + " is selected in GPOS page");
					WebElement dropDwon_fiscalYear = gpos.get_dropDown_fiscalYear();
					dropDwon_fiscalYear.click();
					List<WebElement> list_FiscalYearInitial = gpos.get_dropDownList_fiscalYear();
					dropDwon_fiscalYear.click();
					for (int j = 0; j < list_FiscalYearInitial.size(); j++) {
						dropDwon_fiscalYear.click();
						List<WebElement> list_FiscalYear = gpos.get_dropDownList_fiscalYear();
						String fiscalYear = list_FiscalYear.get(j).getText();
						list_FiscalYear.get(j).click();
						objLgr.info(fiscalYear + " is selected in GPOS page");
						visibility_SummaryTable_ProductTab();
					}
				}
			}
			else {
				isTestCasePassed=false;
				objLgr.error("Summary table not loaded successfully");
			}
			if (!isTestCasePassed)
				Assert.fail("dropdownOptions_SummaryTable_Product failed");
			}
		

		@Test(enabled=true, description = "To check the visibility of Details Table of GPOS page ", dependsOnMethods= {"visibility_SummaryTable_ProductTab"}, priority=7010)
		public void visibility_DetailsTable_ProductTab() throws Exception {
			isTestCasePassed=true;
			nodataflag = false;
			if(gpos.get_headerTable(gpos.get_product_details_table())!= null)
					objLgr.info("Details table is loaded successfully");
				else {
					if(gpos.get_NoDataTable(gpos.get_product_details_table())!=null) {
						objLgr.info("Details page is displaying 'No Data Available' message");
						nodataflag = true;
					}
					else 
						isTestCasePassed=false;
					if (!isTestCasePassed)
						Assert.fail("Details table is not loaded successfully");
				}
		}

		//Validating if sort functionality works fine in GPOS page	
		@Test(enabled=false, description = "Sorting functionality validation of Details table", dependsOnMethods= {"visibility_DetailsTable_ProductTab"}, priority=7011)
		public void sort_DetailsTable_GPOS() throws Exception {
			isTestCasePassed=true;
			String message = null;
			WebElement tbl_details = gpos.get_product_details_table();
			List<WebElement> row_HeaderList = gpos.get_RowHeader(tbl_details);
			for (WebElement header_Field : row_HeaderList) {
				String txt_Field = header_Field.getText();
				objLgr.info("Sorting has been started for field - "+txt_Field);
				for(int i=0;i<=1;i++) {
					List<WebElement> fields_Sort = gpos.get_SortField(txt_Field);
					WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
					field_Sort.click();
					List<WebElement> row_Header = gpos.get_RowHeader(tbl_details);
					List<WebElement> row_Data = gpos.get_RowData(tbl_details);
					List<Hashtable<String, String>> SelectedfieldData = commonPageMyAccount.read_TableData(row_Header, row_Data, gpos.get_fieldData());
					String sort_Order = field_Sort.getAttribute("aria-sort");
					if (commonPageMyAccount.sort_TableData(SelectedfieldData, txt_Field, sort_Order))
						message = "Sorting has been validated successfully in Details table for field " +txt_Field+ " in " + sort_Order + "order.";
					else {
						isTestCasePassed=false;
						message = "Sorting has been failed in Details table for field " +txt_Field+ " in " + sort_Order + "order.";
					}
					objLgr.info(message);
				}
			}
			if (!isTestCasePassed)
				Assert.fail("sort_DetailsTable_GPOS failed - "+message);
			else
				objLgr.info("Sorting functionality has been validated sucessfully in Details table");
			}
		

		@Test(enabled=true,description = "Validate drop-down options in Details table of product tab", dependsOnMethods= {"visibility_DetailsTable_ProductTab"}, priority=7012)
		public void dropdownOptions_DetailsTable_Product() throws Exception {
			isTestCasePassed=true;
			WebElement Dropdown_reportselection = gpos.get_dropDown_reportSelection();
			if (Dropdown_reportselection != null) {
				Dropdown_reportselection.click();
				List<WebElement> list_reportSelectionInitial = gpos.get_dropDownlist_reportSelection();
				Dropdown_reportselection.click();
				for (int i = 0; i < list_reportSelectionInitial.size(); i++) {
					Dropdown_reportselection.click();
					List<WebElement> list_ReportSelection = gpos.get_dropDownlist_reportSelection();
					String reportSelection_option = list_ReportSelection.get(i).getText();
					list_ReportSelection.get(i).click();
					objLgr.info(reportSelection_option + " is selected in GPOS page");
					WebElement dropDwon_fiscalYear = gpos.get_dropDown_fiscalYear();
					dropDwon_fiscalYear.click();
					List<WebElement> list_FiscalYearInitial = gpos.get_dropDownList_fiscalYear();
					dropDwon_fiscalYear.click();
					for (int j = 0; j < list_FiscalYearInitial.size(); j++) {
						dropDwon_fiscalYear.click();
						List<WebElement> list_FiscalYear = gpos.get_dropDownList_fiscalYear();
						String fiscalYear = list_FiscalYear.get(j).getText();
						list_FiscalYear.get(j).click();
						objLgr.info(fiscalYear + " is selected in GPOS page");
						visibility_DetailsTable_ProductTab();
					}
				}
			}
			else {
				isTestCasePassed=false;
				objLgr.error("Details table not loaded successfully");
			}
			if (!isTestCasePassed)
				Assert.fail("dropdownOptions_DetailsTable_Product failed");
			}
		

		@Test(enabled=true,description = "Validate Filter functionality validation of GPOS product tab", dependsOnMethods= {"visibility_DetailsTable_ProductTab"}, priority=7013)
		public void filter_Details_Product_GPOS() throws Exception {
			isTestCasePassed=true;
			for (WebElement filterChip:gpos.get_FilterChips()) {
				filterChip.click();
				WebElement activeFilterChip = gpos.get_ActivateFilterChips().get(0);
				String chipText=activeFilterChip.findElement(gpos.get_Chiptext()).getText().toString();
				objLgr.info("FilterChip :"+ chipText+" is selected");
				for(int i=0;i<=1;i++) {
					List<WebElement> fields_Sort = gpos.get_SortField("Crop");
					WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
					field_Sort.click();
					objLgr.info("Sorting action is performed on the Crop column in " +field_Sort.getAttribute("aria-sort") + " order.");
					WebElement tbl_Details = gpos.get_product_details_table();
					List<WebElement> row_Header = gpos.get_RowHeader(tbl_Details);
					List<WebElement> row_Data = gpos.get_RowData(tbl_Details);
					Hashtable<String, String> firstRow_CropData = commonPageMyAccount.read_TableData(row_Header, row_Data, gpos.get_fieldData(), 0);
					if(firstRow_CropData.get("Crop").toString().equalsIgnoreCase(chipText))
						objLgr.info("Data is displayed for the selected crop: "+chipText);
					else {
						isTestCasePassed=false;
						objLgr.info("Data is not displayed for the selected crop: "+chipText);
					}
				}
				gpos.get_ActivateFilterChips().get(0).click();
			}
			if (!isTestCasePassed)
				Assert.fail("filter_Details_Product_GPOS has not been executed successfully");
		}

		@Test(enabled=true, description = "Validate locations tab loaded in GPOS page", dependsOnMethods= {"visibility_SummaryTable_ProductTab"}, priority=7014)
		public void locationsTab_Load_GPOS() throws Exception {
			isTestCasePassed=true;
			nodataflag= false;
			gpos.get_TabButton(gpos.get_header_GPOStab().get(1)).click();
			if(gpos.get_headerTable(gpos.get_Locations_table())!= null)
				objLgr.info("Locations table is loaded successfully");
			else {
				if(gpos.get_NoDataTable(gpos.get_Locations_table())!=null) {
					objLgr.info("Locations page is displaying 'No Data Available' message");
					nodataflag = true;
				}
				else 
					isTestCasePassed=false;
				if (!isTestCasePassed)
					Assert.fail("Locations table is not loaded successfully");
			}
		}

		//Validating if sort functionality works fine in GPOS page	
		@Test(enabled=false, description = "Sorting functionality validation of Locations table", dependsOnMethods= {"locationsTab_Load_GPOS"}, priority=7015)
		public void sort_LocationsTable_GPOS() throws Exception {
			isTestCasePassed=true;
			String message = null;
			WebElement tbl_details = gpos.get_Locations_table();
			List<WebElement> row_HeaderList = gpos.get_RowHeader(tbl_details);
			for (WebElement header_Field : row_HeaderList) {
				String txt_Field = header_Field.getText();
				objLgr.info("Sorting has been started for field - "+txt_Field);
				for(int i=0;i<=1;i++) {
					List<WebElement> fields_Sort = gpos.get_SortField(txt_Field);
					WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
					field_Sort.click();
					List<WebElement> row_Header = gpos.get_RowHeader(tbl_details);
					List<WebElement> row_Data = gpos.get_RowData(tbl_details);
					List<Hashtable<String, String>> SelectedfieldData = commonPageMyAccount.read_TableData(row_Header, row_Data, gpos.get_fieldData());
					String sort_Order = field_Sort.getAttribute("aria-sort");
					if (commonPageMyAccount.sort_TableData(SelectedfieldData, txt_Field, sort_Order))
						message = "Sorting has been validated successfully in Locations table for field " +txt_Field+ " in " + sort_Order + "order.";
					else {
						isTestCasePassed=false;
						message = "Sorting has been failed in Locations table for field " +txt_Field+ " in " + sort_Order + "order.";
					}
					objLgr.info(message);
				}
			}
			if (!isTestCasePassed)
				Assert.fail("sort_LocationsTable_GPOS failed - "+message);
			else
				objLgr.info("Sorting functionality has been validated sucessfully in Locations table");
			}
		

		@Test(enabled=true,description = "Validate drop-down options in Locations table of product tab", dependsOnMethods= {"locationsTab_Load_GPOS"}, priority=7016)
		public void dropdownOptions_LocationsTable_Product() throws Exception {
			isTestCasePassed=true;
			WebElement Dropdown_reportselection = gpos.get_dropDown_reportSelection();
			if (Dropdown_reportselection != null) {
				Dropdown_reportselection.click();
				List<WebElement> list_reportSelectionInitial = gpos.get_dropDownlist_reportSelection();
				Dropdown_reportselection.click();
				for (int i = 0; i < list_reportSelectionInitial.size(); i++) {
					Dropdown_reportselection.click();
					List<WebElement> list_ReportSelection = gpos.get_dropDownlist_reportSelection();
					String reportSelection_option = list_ReportSelection.get(i).getText();
					list_ReportSelection.get(i).click();
					objLgr.info(reportSelection_option + " is selected in GPOS page");
					WebElement dropDwon_fiscalYear = gpos.get_dropDown_fiscalYear();
					dropDwon_fiscalYear.click();
					List<WebElement> list_FiscalYearInitial = gpos.get_dropDownList_fiscalYear();
					dropDwon_fiscalYear.click();
					for (int j = 0; j < list_FiscalYearInitial.size(); j++) {
						dropDwon_fiscalYear.click();
						List<WebElement> list_FiscalYear = gpos.get_dropDownList_fiscalYear();
						String fiscalYear = list_FiscalYear.get(j).getText();
						list_FiscalYear.get(j).click();
						objLgr.info(fiscalYear + " is selected in GPOS page");
						if(gpos.get_headerTable(gpos.get_Locations_table())!= null)
							objLgr.info("Locations table is loaded successfully");
						else {
							if(gpos.get_NoDataTable(gpos.get_Locations_table())!=null) {
								objLgr.info("Locations page is displaying 'No Data Available' message");
								nodataflag = true;
							}
							else 
								isTestCasePassed=false;
						}
					}
				}
			}
			else {
				isTestCasePassed=false;
				objLgr.error("Locations table not loaded successfully");
			}
			if (!isTestCasePassed)
				Assert.fail("dropdownOptions_LocationsTable_Product failed");
			}
		

		@Test(enabled=true,description = "Validate Filter functionality validation of GPOS Locations tab", dependsOnMethods= {"locationsTab_Load_GPOS"}, priority=7017)
		public void filter_Locations_GPOS() throws Exception {
			isTestCasePassed=true;
			for (WebElement filterChip:gpos.get_FilterChips()) {
				filterChip.click();
				WebElement activeFilterChip = gpos.get_ActivateFilterChips().get(0);
				String chipText=activeFilterChip.findElement(gpos.get_Chiptext()).getText().toString();
				objLgr.info("FilterChip :"+ chipText+" is selected");
				for(int i=0;i<=1;i++) {
					List<WebElement> fields_Sort = gpos.get_SortField("Crop");
					WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
					field_Sort.click();
					objLgr.info("Sorting action is performed on the Crop column in " +field_Sort.getAttribute("aria-sort") + " order.");
					WebElement tbl_Details = gpos.get_product_details_table();
					List<WebElement> row_Header = gpos.get_RowHeader(tbl_Details);
					List<WebElement> row_Data = gpos.get_RowData(tbl_Details);
					Hashtable<String, String> firstRow_CropData = commonPageMyAccount.read_TableData(row_Header, row_Data, gpos.get_fieldData(), 0);
					if(firstRow_CropData.get("Crop").toString().equalsIgnoreCase(chipText))
						objLgr.info("Data is displayed for the selected crop: "+chipText);
					else {
						isTestCasePassed=false;
						objLgr.info("Data is not displayed for the selected crop: "+chipText);
					}
				}
				gpos.get_ActivateFilterChips().get(0).click();
			}
			if (!isTestCasePassed)
				Assert.fail("filter_Locations_GPOS has not been executed successfully");
		}

		@Test(enabled=true, description = "Validate Farmers tab loaded in GPOS page", dependsOnMethods= {"locationsTab_Load_GPOS"}, priority=7018)
		public void FarmersTab_Load_GPOS() throws Exception {
			isTestCasePassed=true;
			nodataflag= false;
			gpos.get_TabButton(gpos.get_header_GPOStab().get(2)).click();
			if(gpos.get_headerTable(gpos.get_Farmers_table())!= null)
				objLgr.info("Farmers table is loaded successfully");
			else {
				if(gpos.get_NoDataTable(gpos.get_Farmers_table())!=null) {
					objLgr.info("Farmers page is displaying 'No Data Available' message");
					nodataflag = true;
				}
				else 
					isTestCasePassed=false;
				if (!isTestCasePassed)
					Assert.fail("Farmers table is not loaded successfully");
			}
		}

		//Validating if sort functionality works fine in GPOS page	
		@Test(enabled=false, description = "Sorting functionality validation of Farmers table", dependsOnMethods= {"locationsTab_Load_GPOS"}, priority=7019)
		public void sort_FarmersTable_GPOS() throws Exception {
			isTestCasePassed=true;
			String message = null;
			WebElement tbl_details = gpos.get_Farmers_table();
			List<WebElement> row_HeaderList = gpos.get_RowHeader(tbl_details);
			for (WebElement header_Field : row_HeaderList) {
				String txt_Field = header_Field.getText();
				objLgr.info("Sorting has been started for field - "+txt_Field);
				for(int i=0;i<=1;i++) {
					List<WebElement> fields_Sort = gpos.get_SortField(txt_Field);
					WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
					field_Sort.click();
					List<WebElement> row_Header = gpos.get_RowHeader(tbl_details);
					List<WebElement> row_Data = gpos.get_RowData(tbl_details);
					List<Hashtable<String, String>> SelectedfieldData = commonPageMyAccount.read_TableData(row_Header, row_Data, gpos.get_fieldData());
					String sort_Order = field_Sort.getAttribute("aria-sort");
					if (commonPageMyAccount.sort_TableData(SelectedfieldData, txt_Field, sort_Order))
						message = "Sorting has been validated successfully in Farmers table for field " +txt_Field+ " in " + sort_Order + "order.";
					else {
						isTestCasePassed=false;
						message = "Sorting has been failed in Farmers table for field " +txt_Field+ " in " + sort_Order + "order.";
					}
					objLgr.info(message);
				}
			}
			if (!isTestCasePassed)
				Assert.fail("sort_FarmersTable_GPOS failed - "+message);
			else
				objLgr.info("Sorting functionality has been validated sucessfully in Farmers table");
			}
		

		@Test(enabled=true,description = "Validate drop-down options in Farmers table of product tab", dependsOnMethods= {"locationsTab_Load_GPOS"}, priority=7020)
		public void dropdownOptions_FarmersTable_Product() throws Exception {
			isTestCasePassed=true;
			WebElement Dropdown_reportselection = gpos.get_dropDown_reportSelection();
			if (Dropdown_reportselection != null) {
				Dropdown_reportselection.click();
				List<WebElement> list_reportSelectionInitial = gpos.get_dropDownlist_reportSelection();
				Dropdown_reportselection.click();
				for (int i = 0; i < list_reportSelectionInitial.size(); i++) {
					Dropdown_reportselection.click();
					List<WebElement> list_ReportSelection = gpos.get_dropDownlist_reportSelection();
					String reportSelection_option = list_ReportSelection.get(i).getText();
					list_ReportSelection.get(i).click();
					objLgr.info(reportSelection_option + " is selected in GPOS page");
					WebElement dropDwon_fiscalYear = gpos.get_dropDown_fiscalYear();
					dropDwon_fiscalYear.click();
					List<WebElement> list_FiscalYearInitial = gpos.get_dropDownList_fiscalYear();
					dropDwon_fiscalYear.click();
					for (int j = 0; j < list_FiscalYearInitial.size(); j++) {
						dropDwon_fiscalYear.click();
						List<WebElement> list_FiscalYear = gpos.get_dropDownList_fiscalYear();
						String fiscalYear = list_FiscalYear.get(j).getText();
						list_FiscalYear.get(j).click();
						objLgr.info(fiscalYear + " is selected in GPOS page");
						if(gpos.get_headerTable(gpos.get_Farmers_table())!= null)
							objLgr.info("Farmers table is loaded successfully");
						else {
							if(gpos.get_NoDataTable(gpos.get_Farmers_table())!=null) {
								objLgr.info("Farmers page is displaying 'No Data Available' message");
								nodataflag = true;
							}
							else 
								isTestCasePassed=false;
						}
					}
				}
			}
			else {
				isTestCasePassed=false;
				objLgr.error("Farmers table not loaded successfully");
			}
			if (!isTestCasePassed)
				Assert.fail("dropdownOptions_FarmersTable_Product failed");
		}

		@Test(enabled=true,description = "Validate Filter functionality validation of GPOS Farmers tab", dependsOnMethods= {"locationsTab_Load_GPOS"}, priority=7021)
		public void filter_Farmers_GPOS() throws Exception {
			isTestCasePassed=true;
			for (WebElement filterChip:gpos.get_FilterChips()) {
				filterChip.click();
				WebElement activeFilterChip = gpos.get_ActivateFilterChips().get(0);
				String chipText=activeFilterChip.findElement(gpos.get_Chiptext()).getText().toString();
				objLgr.info("FilterChip :"+ chipText+" is selected");
				for(int i=0;i<=1;i++) {
					List<WebElement> fields_Sort = gpos.get_SortField("Crop");
					WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
					field_Sort.click();
					objLgr.info("Sorting action is performed on the Crop column in " +field_Sort.getAttribute("aria-sort") + " order.");
					WebElement tbl_Details = gpos.get_product_details_table();
					List<WebElement> row_Header = gpos.get_RowHeader(tbl_Details);
					List<WebElement> row_Data = gpos.get_RowData(tbl_Details);
					Hashtable<String, String> firstRow_CropData = commonPageMyAccount.read_TableData(row_Header, row_Data, gpos.get_fieldData(), 0);
					if(firstRow_CropData.get("Crop").toString().equalsIgnoreCase(chipText))
						objLgr.info("Data is displayed for the selected crop: "+chipText);
					else {
						isTestCasePassed=false;
						objLgr.info("Data is not displayed for the selected crop: "+chipText);
					}
				}
				gpos.get_ActivateFilterChips().get(0).click();
			}
			if (!isTestCasePassed)
				Assert.fail("filter_Farmers_GPOS has not been executed successfully");
		}
		
		
		
	//-------------------------------LogOut Test case------------------------------//

	@Test(enabled=false, description = "Log out from My Account", dependsOnMethods= {"filter_Farmers_GPOS"}, priority=10001, alwaysRun=true)
	public void validate_LogOut() throws Exception {
		isTestCasePassed=true;
		if (siteURL.contains("-q.agro")||siteURL.contains("-d.agro"))
			objLgr.info("Log out functionality not available in QA or Dev environment.");
		else if(isLoginSuccessfull){
			String message = "";
			if(lnk.get_DropdownLogOut()!= null) {
				js.executeScript("arguments[0].scrollIntoView();", lnk.get_DropdownLogOut());
				lnk.get_DropdownLogOut().click();
				js.executeScript("arguments[0].scrollIntoView();", lnk.get_LogOut().get(0));
				lnk.get_LogOut().get(0).click();
				if (gigyaLoginPage.get_UserName() != null)
					message = "Logged out successful.";
				else {
					isTestCasePassed=false;
					message = "Username field not displayed after logout action";
				}
			}
			else {
				isTestCasePassed=false;
				message = "Unable to locate logout drop down";
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

