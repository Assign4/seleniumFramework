package myAccount;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.Monsanto.ExcelOperations.ReadFlattenLocationHierarchy;
import com.applitools.eyes.BatchInfo;
import com.mashape.unirest.http.exceptions.UnirestException;

import FunctionLibrary.ReusableFunctions;
import PageObjects.CommonPageMyAccount;
import PageObjects.GigyaLoginPage;
import PageObjects.LeftNavigation_Accountdropdown;
import PageObjects.RoyaltyReporting;
import driver.Driver;

public class E2E_Lepsi_RoyaltyReporting extends Driver{
	private static Logger objLgr=LogManager.getLogger(E2E_Lepsi.class.getName());
	public boolean isTestCasePassed,isLoginSuccessfull;
	public GigyaLoginPage gigyaLoginPage;
	public RoyaltyReporting rr;
	public ReadFlattenLocationHierarchy readFlattenLocationHierarchy = new ReadFlattenLocationHierarchy();
	public List<Hashtable<String, String>> location_Flatten = new ArrayList<Hashtable<String, String>>();
	@SuppressWarnings("unchecked")
	public List<Hashtable<String, String>>[] location_LevelsIRD= new List[6];
	Hashtable<String, String> locationNode_IRD = new Hashtable<String, String>();
	public JavascriptExecutor js;
	public CommonPageMyAccount commonPageMyAccount;
	public LeftNavigation_Accountdropdown lnk;
	String program,crop,userName, userSAPAccountID, userType, userPassword, siteURL, platform_CBT, lob_selection,aggregate_Data,widgetText;
	String location_level1SAPID, location_level2SAPID, location_level3SAPID, location_level4SAPID, location_level5SAPID;
	public int sessionCount=0;
	public static WebElement traitsRoyaltyWidgetHeader = null,geneticRoyaltyWidgetHeader = null,cottonRoyaltyWidgetHeader = null;
	ReusableFunctions reusefunc = new ReusableFunctions();
	public E2E_Lepsi_RoyaltyReporting(Hashtable<String, String> input) {
		this.userName = input.get("UserName");
		this.userSAPAccountID = input.get("SAPAccountID");
		this.userType = input.get("UserType");
		this.userPassword = input.get("Password");
		this.siteURL = input.get("ApplicationURL");
		this.platform_CBT = input.get("CBT Platform");
		this.lob_selection = input.get("LOB");
		this.aggregate_Data = input.get("Aggregate Data Selection");
		this.location_level1SAPID = input.get("Level 1 SAP ID");
		this.location_level2SAPID = input.get("Level 2 SAP ID");
		this.location_level3SAPID = input.get("Level 3 SAP ID");
		this.location_level4SAPID = input.get("Level 4 SAP ID");
		this.location_level5SAPID = input.get("Level 5 SAP ID");
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
			testName="ACS2MyAccount("+userType+"-"+env+")Session:"+sessionCount;
			eyes = initializeApplitools(webdriver, bInfo, appName, testName);
			//Maximize browser for Windows and Mac
			if (platform.equals("MAC") || (platform.equals("crossbrowsertesting.com") && platform_CBT.toString().contains("Desktop") && platform_CBT.toString().contains("Mac")))
				webdriver.manage().window().fullscreen();
			else if (platform.equals("Windows") || (platform.equals("crossbrowsertesting.com") && platform_CBT.toString().contains("Desktop") && platform_CBT.toString().contains("Windows")))
				webdriver.manage().window().maximize();
		}
		objLgr.info("Launching application " + siteURL);
		if(webdriver == null){
			objLgr.info("webdriver is null");
		}
		webdriver.get(siteURL); //Opening the Application
		objLgr.info("Running test on " + siteURL);
		lnk = new LeftNavigation_Accountdropdown(webdriver);
		commonPageMyAccount = new CommonPageMyAccount(webdriver);
		gigyaLoginPage=new GigyaLoginPage(webdriver);
		rr = new RoyaltyReporting(webdriver);
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
		if (!isTestCasePassed)
			Assert.fail("Dashboard not displayed in left navigation panel");
		verifyPageWithApplitools(eyes);
	}


	//Validating if the Rpyalty Reporting page is loaded
	@Test (enabled=false, description = "Validating whether Royalty Reporting page loaded", priority=2000)
	public void pagecheck_royaltyReporting() throws Exception {
		// Start the test by setting AUT's name, window or the page name that's being tested, viewport width and height
		isTestCasePassed=true;
		webdriver.get("https://dev-mycrop.bayer.com/licensee/royalty-reporting/");
		
		if(rr.get_traitsRoyalty_Header() != null) {
			objLgr.info("Royalty Reporting page loaded successfuly");
			isLoginSuccessfull = true;
		}
		else
			isTestCasePassed=false;
		if (!isTestCasePassed)
			Assert.fail("Dashboard not displayed in left navigation panel");
	}


	//Validating if trait royalty widget is displayed
	@Test (enabled=false, description = "Validating whether Royalty Reporting page loaded", dependsOnMethods= {"pagecheck_royaltyReporting"},priority=2001)
	public void traitRoyalty_RoyaltyReporting() throws Exception {
		// Start the test by setting AUT's name, window or the page name that's being tested, viewport width and height
		isTestCasePassed=true;
		traitsRoyaltyWidgetHeader = rr.get_traitsRoyalty_Header();
		if(rr.get_traitsRoyalty_body() != null) {
			objLgr.info("Trait Royalty widget in Royalty Reporting page loaded successfuly");
			isLoginSuccessfull = true;
		}
		else
			isTestCasePassed=false;
		if (!isTestCasePassed)
			Assert.fail("Trait Royalty widget in Royalty Reporting page not loaded successfuly");
	}

	@Test(enabled=false, description = "Check visiblility of VIEW DATA link of Trait Royalty widget in Royalty Reporting page", dependsOnMethods= {"traitRoyalty_RoyaltyReporting"}, priority=2002)
	public void visibilityViewData_traitRoyalty_RoyaltyReporting() throws Exception {
		isTestCasePassed=true;
		String message = null;
		if (rr.get_ViewMoreLink(traitsRoyaltyWidgetHeader) != null) 
				message = "View More link is displayed in Trait Royalty widget";
			else {
				isTestCasePassed = false;
				message = "View More link is not available in Trait Royalty widget";
			}
			objLgr.info(message);
		
		if (!isTestCasePassed)
			Assert.fail("visibilityViewData_traitRoyalty_RoyaltyReporting failed "+ message);
	}

	@Test(enabled=false, description = "Validate VIEW DATA table of Trait Royalty widget in Royalty Reporting page", dependsOnMethods= {"visibilityViewData_traitRoyalty_RoyaltyReporting"}, priority=2003)
	public void viewData_traitRoyalty_RoyaltyReporting() throws Exception {
		isTestCasePassed=true;
		String message = null;
		WebElement lnk_ViewData = rr.get_ViewMoreLink(traitsRoyaltyWidgetHeader);
		if (lnk_ViewData != null) {
			lnk_ViewData.click();
			WebElement tbl_CornTrait = rr.get_CornTraitTable();
			if (tbl_CornTrait!=null)
				message = "Trait table is loaded successfully";
			else if (rr.get_NoDataTable(traitsRoyaltyWidgetHeader) != null) {
				isTestCasePassed = false;
				message = "Trait Royalty widget displays \"No Data Available\".";
			}
			else {
				isTestCasePassed = false;
				message = "Trait table has not loaded successfully";
			}
			objLgr.info(message);
		}
		if (!isTestCasePassed)
			Assert.fail("viewData_traitRoyalty_RoyaltyReporting failed - " + message);
		verifyPageWithApplitools(eyes);
	}

	@Test(enabled=false, description = "Sorting functionality validation of Corn Trait tab of Trait Royalty view more table", dependsOnMethods= {"viewData_traitRoyalty_RoyaltyReporting"}, priority=2004)
	public void sort_CornTrait_traitRoyalty() throws Exception {
		isTestCasePassed=true;
		String message = null;
		WebElement tbl_CornTrait = rr.get_CornTraitTable();
		List<WebElement> row_HeaderList = rr.get_RowHeader(tbl_CornTrait);
		for (WebElement header_Field : row_HeaderList) {
			String txt_Field = header_Field.getText();
			objLgr.info("Sorting has been started for field - "+txt_Field);
			for(int i=0;i<=1;i++) {
				List<WebElement> fields_Sort = rr.get_SortField(txt_Field);
				WebElement field_Sort = fields_Sort.get(0);
				field_Sort.click();
				List<WebElement> row_Header = rr.get_RowHeader(tbl_CornTrait);
				List<WebElement> row_Data = rr.get_RowData(tbl_CornTrait);
				List<Hashtable<String, String>> SelectedCropData = commonPageMyAccount.read_TableData(row_Header, row_Data, rr.get_fieldData());
				String sort_Order = field_Sort.getAttribute("aria-sort");
				if (commonPageMyAccount.sort_TableData(SelectedCropData, txt_Field, sort_Order))
					objLgr.info("Sorting has been validated successfully in Corn Trait table for field " +txt_Field+ " in " + sort_Order + "order.");
				else {
					isTestCasePassed=false;
					message = "Sorting has been failed in Corn Trait table for field " +txt_Field+ " in " + sort_Order + "order.";
				}
				objLgr.info(message);
			}
		}
		if (!isTestCasePassed)
			Assert.fail("sort_CornTrait_traitRoyalty has failed - "+message);
		else
			objLgr.info("Sorting has been validated successfully in Corn Trait table for all fields");
	}

	@Test(enabled=false, description = "Validate trait drop-downs in Corn Trait By Zone table", dependsOnMethods= {"viewData_traitRoyalty_RoyaltyReporting"}, priority=2005)
	public void traitdropDown_CornTrait_traitRoyalty() throws Exception {
		isTestCasePassed=true;
		String trait = null;
		WebElement tbl_CornTrait_ByZone = null;
		List<WebElement> list_TraitInitial = null;
		WebElement traitDropdown = rr.get_TraitDropdown();
		if (traitDropdown != null) {
			js.executeScript("arguments[0].scrollIntoView();",rr.get_CornTrait_ByZone_table());
			tbl_CornTrait_ByZone = rr.get_CornTrait_ByZone_table();
			traitDropdown.click();
			list_TraitInitial = rr.get_DropdownList(traitDropdown);
			traitDropdown.click();
			for (int i = 0; i < list_TraitInitial.size(); i++) {
				traitDropdown.click();
				List<WebElement> list_Traits = rr.get_DropdownList(traitDropdown);
				trait = list_Traits.get(i).getText();
				list_Traits.get(i).click();
				if(!trait.equalsIgnoreCase("All Traits")) {
					objLgr.info(trait + " is selected in Trait's drop-down of Corn Trait By Zone Table");
					tbl_CornTrait_ByZone = rr.get_CornTrait_ByZone_table();
					if(tbl_CornTrait_ByZone!=null) {
						objLgr.info("Corn Trait By Zone table is loaded successfully for selected crop : "+trait);
						for(int j=0; j<=1; j++) {
							List<WebElement> fields_Sort = rr.get_SortField("Trait Description");
							WebElement field_Sort = fields_Sort.get(1);
							field_Sort.click();
							objLgr.info("Sorting action is performed on the Trait Description column in " +field_Sort.getAttribute("aria-sort") + " order.");
							List<WebElement> row_Header = rr.get_RowHeader(tbl_CornTrait_ByZone);
							List<WebElement> row_Data = rr.get_RowData(tbl_CornTrait_ByZone);
							Hashtable<String, String> firstRow_CropData = commonPageMyAccount.read_TableData(row_Header, row_Data, rr.get_fieldData(), 0);
							if(firstRow_CropData.get("Trait Description").toString().equalsIgnoreCase(trait))
								objLgr.info("Data is displayed for the selected crop: "+trait);
							else {
								isTestCasePassed=false;
								objLgr.error("Data is not displayed for the selected crop: "+trait);
							}
						}
					}
					else if (rr.get_NoDataTable(tbl_CornTrait_ByZone) != null) 
						objLgr.info("Corn Trait By Zone table displays \"No Data Available\". for trait - " + trait);
					else {
						isTestCasePassed = false;
						objLgr.error("Corn Trait By Zone table displays Try Again. for crop - " + trait);
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
			objLgr.info("Successfully validated Crop drop down in product look up table");
			traitDropdown.click();
			list_TraitInitial= rr.get_DropdownList(traitDropdown);
			for(WebElement option : list_TraitInitial) {
				if(option.getText().equalsIgnoreCase("All Traits"))
					option.click();
				break; 
			}
		}
	}

	@Test(enabled=false, description = "Sorting functionality validation of Corn Trait By Zone table of Trait Royalty view more page", dependsOnMethods= {"viewData_traitRoyalty_RoyaltyReporting"}, priority=2006)
	public void sort_CornTraitByZone_traitRoyalty() throws Exception {
		isTestCasePassed=true;
		String message = null;
		WebElement tbl_CornTrait_ByZone = rr.get_CornTrait_ByZone_table();
		List<WebElement> row_HeaderList = rr.get_RowHeader(tbl_CornTrait_ByZone);
		for (WebElement header_Field : row_HeaderList) {
			String txt_Field = header_Field.getText();
			objLgr.info("Sorting has been started for field - "+txt_Field);
			for(int i=0;i<=1;i++) {
				List<WebElement> fields_Sort = rr.get_SortField(txt_Field);
				WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
				field_Sort.click();
				List<WebElement> row_Header = rr.get_RowHeader(tbl_CornTrait_ByZone);
				List<WebElement> row_Data = rr.get_RowData(tbl_CornTrait_ByZone);
				List<Hashtable<String, String>> SelectedCropData = commonPageMyAccount.read_TableData(row_Header, row_Data, rr.get_fieldData());
				String sort_Order = field_Sort.getAttribute("aria-sort");
				if (commonPageMyAccount.sort_TableData(SelectedCropData, txt_Field, sort_Order))
					message = "Sorting has been validated successfully in Corn Trait By Zone table for field " +txt_Field+ " in " + sort_Order + "order.";
				else {
					isTestCasePassed=false;
					message = "Sorting has been failed in Corn Trait By Zone table for field " +txt_Field+ " in " + sort_Order + "order.";
				}
				objLgr.info(message);
			}
		}
		if (!isTestCasePassed)
			Assert.fail("sort_CornTrait_traitRoyalty has failed - "+message);
		else
			objLgr.info("Sorting has been validated successfully in Corn Trait By Zone table for all fields");
	}

	@Test(enabled=false, description = "Validate search functionality in View More table of Corn Trait By Zone table of Trait Royalty view more page",dependsOnMethods= {"viewData_traitRoyalty_RoyaltyReporting"}, priority=2007)
	public void search_CornTraitByZone_traitRoyalty() throws Exception {
		isTestCasePassed=true;
		String message = null;
		WebElement tbl_CornTrait_ByZone = rr.get_CornTrait_ByZone_table();
		List<WebElement> row_Header = rr.get_RowHeader(tbl_CornTrait_ByZone);
		List<WebElement> row_Data = rr.get_RowData(tbl_CornTrait_ByZone);
		List<Hashtable<String, String>> tableData = commonPageMyAccount.read_TableData(row_Header, row_Data, rr.get_fieldData());
		if (rr.get_SearchField()!= null) {
			String search_Txt=reusefunc.get_RandomString(tableData);
			objLgr.info("Search text is identified as - "+search_Txt);
			rr.get_SearchText().sendKeys(search_Txt);
			rr.get_SearchText().sendKeys(Keys.ENTER);
			List<WebElement> row_SearchData = rr.get_RowData(tbl_CornTrait_ByZone);
			List<Hashtable<String, String>> tableData_Search = commonPageMyAccount.read_TableData(row_Header, row_SearchData, rr.get_fieldData());
			objLgr.info("Total records found in Corn Trait By Zone table after searching with text " + search_Txt + " is - "+tableData_Search.size());
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
			Assert.fail("search_CornTraitByZone_traitRoyalty failed - " + message);
	}

	@Test(enabled=false, description = "Validate Soybeans tab loaded in Traits Royalty View More page", dependsOnMethods= {"viewData_traitRoyalty_RoyaltyReporting"}, priority=2008)
	public void soybeantblLoad_traitRoyalty() throws Exception {
			isTestCasePassed=true;
			String message = null;
			rr.get_TabButton(rr.get_tab_Header().get(1)).click();
			String tableName = null;
			if (rr.get_Table() != null) {
				tableName = rr.get_TableHeader().getText();
				message = tableName + " table is loaded successfully";
			} else if (rr.get_NoDataTable(rr.get_SoybeanTraittable()) != null) {
				isTestCasePassed = false;
				message = tableName + " table displays \"No Data Available\".";
			} else {
				isTestCasePassed = false;
				message = tableName + " table has not loaded successfully";
			}
			objLgr.info(message);
			if (!isTestCasePassed)
				Assert.fail("SoybeantblLoad_traitRoyalty failed." + message);
		
	}

	@Test(enabled=false, description = "Sorting functionality validation of Soybean Trait table of Trait Royalty view more page", dependsOnMethods= {"viewData_traitRoyalty_RoyaltyReporting"}, priority=2009)
	public void sort_SoybeanTrait_traitRoyalty() throws Exception {
		isTestCasePassed=true;
		String message = null;
		WebElement tbl_SoybeanTrait = rr.get_SoybeanTraittable();
		List<WebElement> row_HeaderList = rr.get_RowHeader(tbl_SoybeanTrait);
		for (WebElement header_Field : row_HeaderList) {
			String txt_Field = header_Field.getText();
			objLgr.info("Sorting has been started for field - "+txt_Field);
			for(int i=0;i<=1;i++) {
				List<WebElement> fields_Sort = rr.get_SortField(txt_Field);
				WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
				field_Sort.click();
				List<WebElement> row_Header = rr.get_RowHeader(tbl_SoybeanTrait);
				List<WebElement> row_Data = rr.get_RowData(tbl_SoybeanTrait);
				List<Hashtable<String, String>> SelectedCropData = commonPageMyAccount.read_TableData(row_Header, row_Data, rr.get_fieldData());
				String sort_Order = field_Sort.getAttribute("aria-sort");
				if (commonPageMyAccount.sort_TableData(SelectedCropData, txt_Field, sort_Order))
					message = "Sorting has been validated successfully in Soybean Trait table for field " +txt_Field+ " in " + sort_Order + "order.";
				else {
					isTestCasePassed=false;
					message = "Sorting has been failed in Soybean Trait table for field " +txt_Field+ " in " + sort_Order + "order.";
				}
				objLgr.info(message);
			}
		}
		if (!isTestCasePassed)
			Assert.fail("sort_SoybeanTrait_traitRoyalty has failed - "+message);
		else
			objLgr.info("Sorting has been validated successfully in Soybean Trait table for all fields");
	}

	@Test(enabled=false, description = "Validate search functionality in View More table of Soybean Trait table of Trait Royalty view more page",dependsOnMethods= {"viewData_traitRoyalty_RoyaltyReporting"}, priority=2010)
	public void search_SoybeanTrait_traitRoyalty() throws Exception {
		isTestCasePassed=true;
		String message = null;
		WebElement tbl_SoybeanTrait = rr.get_SoybeanTraittable();
		List<WebElement> row_Header = rr.get_RowHeader(tbl_SoybeanTrait);
		List<WebElement> row_Data = rr.get_RowData(tbl_SoybeanTrait);
		List<Hashtable<String, String>> tableData = commonPageMyAccount.read_TableData(row_Header, row_Data, rr.get_fieldData());
		if (rr.get_SearchField()!= null) {
			String search_Txt=reusefunc.get_RandomString(tableData);
			objLgr.info("Search text is identified as - "+search_Txt);
			rr.get_SearchText().sendKeys(search_Txt);
			rr.get_SearchText().sendKeys(Keys.ENTER);
			List<WebElement> row_SearchData = rr.get_RowData(tbl_SoybeanTrait);
			List<Hashtable<String, String>> tableData_Search = commonPageMyAccount.read_TableData(row_Header, row_SearchData, rr.get_fieldData());
			objLgr.info("Total records found in Soybean Trait table after searching with text " + search_Txt + " is - "+tableData_Search.size());
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
			Assert.fail("search_SoybeanTrait_traitRoyalty failed - " + message);
	}

	@Test(enabled=false, description = "Validate Canola tab loaded in Traits Royalty View More page", dependsOnMethods= {"viewData_traitRoyalty_RoyaltyReporting"}, priority=2011)
	public void canolatblLoad_traitRoyalty() throws Exception {
			isTestCasePassed=true;
			String message = null;
			rr.get_TabButton(rr.get_tab_Header().get(2)).click();
			String tableName = null;
			if (rr.get_Table() != null) {
				tableName = rr.get_TableHeader().getText();
				message = tableName + " table is loaded successfully";
			} else if (rr.get_NoDataTable(rr.get_CanolaTraitTable()) != null) {
				isTestCasePassed = false;
				message = tableName + " table displays \"No Data Available\".";
			} else {
				isTestCasePassed = false;
				message = tableName + " table has not loaded successfully";
			}
			objLgr.info(message);
			if (!isTestCasePassed)
				Assert.fail("CanolatblLoad_traitRoyalty failed." + message);
			verifyPageWithApplitools(eyes);
		
	}

	@Test(enabled=false, description = "Sorting functionality validation of Canola Trait table of Trait Royalty view more page", dependsOnMethods= {"viewData_traitRoyalty_RoyaltyReporting"}, priority=2012)
	public void sort_CanolaTrait_traitRoyalty() throws Exception {
		isTestCasePassed=true;
		String message = null;
		WebElement tbl_CanolaTrait = rr.get_CanolaTraitTable();
		List<WebElement> row_HeaderList = rr.get_RowHeader(tbl_CanolaTrait);
		for (WebElement header_Field : row_HeaderList) {
			String txt_Field = header_Field.getText();
			objLgr.info("Sorting has been started for field - "+txt_Field);
			for(int i=0;i<=1;i++) {
				List<WebElement> fields_Sort = rr.get_SortField(txt_Field);
				WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
				field_Sort.click();
				List<WebElement> row_Header = rr.get_RowHeader(tbl_CanolaTrait);
				List<WebElement> row_Data = rr.get_RowData(tbl_CanolaTrait);
				List<Hashtable<String, String>> SelectedCropData = commonPageMyAccount.read_TableData(row_Header, row_Data, rr.get_fieldData());
				String sort_Order = field_Sort.getAttribute("aria-sort");
				if (commonPageMyAccount.sort_TableData(SelectedCropData, txt_Field, sort_Order))
					message = "Sorting has been validated successfully in Canola Trait table for field " +txt_Field+ " in " + sort_Order + "order.";
				else {
					isTestCasePassed=false;
					message = "Sorting has been failed in Canola Trait table for field " +txt_Field+ " in " + sort_Order + "order.";
				}
				objLgr.info(message);
			}
		}
		if (!isTestCasePassed)
			Assert.fail("sort_CanolaTrait_traitRoyalty has failed - "+message);
		else
			objLgr.info("Sorting has been validated successfully in Canola Trait table for all fields");
	}

	@Test(enabled=false, description = "Validate search functionality in View More table of Canola Trait table of Trait Royalty view more page",dependsOnMethods= {"viewData_traitRoyalty_RoyaltyReporting"}, priority=2013)
	public void search_CanolaTrait_traitRoyalty() throws Exception {
		isTestCasePassed=true;
		String message = null;
		WebElement tbl_CanolaTrait = rr.get_CanolaTraitTable();
		List<WebElement> row_Header = rr.get_RowHeader(tbl_CanolaTrait);
		List<WebElement> row_Data = rr.get_RowData(tbl_CanolaTrait);
		List<Hashtable<String, String>> tableData = commonPageMyAccount.read_TableData(row_Header, row_Data, rr.get_fieldData());
		if (rr.get_SearchField()!= null) {
			String search_Txt=reusefunc.get_RandomString(tableData);
			objLgr.info("Search text is identified as - "+search_Txt);
			rr.get_SearchText().sendKeys(search_Txt);
			rr.get_SearchText().sendKeys(Keys.ENTER);
			List<WebElement> row_SearchData = rr.get_RowData(tbl_CanolaTrait);
			List<Hashtable<String, String>> tableData_Search = commonPageMyAccount.read_TableData(row_Header, row_SearchData, rr.get_fieldData());
			objLgr.info("Total records found in Canola Trait table after searching with text " + search_Txt + " is - "+tableData_Search.size());
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
			Assert.fail("search_CanolaTrait_traitRoyalty failed - " + message);
	}

	@Test(enabled=false, description = "Validate Corn Treatment tab loaded in Traits Royalty View More page", dependsOnMethods= {"viewData_traitRoyalty_RoyaltyReporting"}, priority=2014)
	public void cornTreatmenttblLoad_traitRoyalty() throws Exception {
			isTestCasePassed=true;
			String message = null;
			rr.get_TabButton(rr.get_tab_Header().get(3)).click();
			String tableName = null;
			if (rr.get_Table() != null) {
				tableName = rr.get_TableHeader().getText();
				message = tableName + " table is loaded successfully";
			} else if (rr.get_NoDataTable(rr.get_CanolaTraitTable()) != null) {
				isTestCasePassed = false;
				message = tableName + " table displays \"No Data Available\".";
			} else {
				isTestCasePassed = false;
				message = tableName + " table has not loaded successfully";
			}
			objLgr.info(message);
			if (!isTestCasePassed)
				Assert.fail("CanolatblLoad_traitRoyalty failed." + message);
			verifyPageWithApplitools(eyes);
	}

	@Test(enabled=false, description = "Sorting functionality validation of Corn Treatment Trait table of Trait Royalty view more page", dependsOnMethods= {"viewData_traitRoyalty_RoyaltyReporting"}, priority=2014)
	public void sort_CornTreatmentTrait_traitRoyalty() throws Exception {
		isTestCasePassed=true;
		String message = null;
		WebElement tbl_CornTreatmentTrait = rr.get_CornTreatmentTraitTable();
		List<WebElement> row_HeaderList = rr.get_RowHeader(tbl_CornTreatmentTrait);
		for (WebElement header_Field : row_HeaderList) {
			String txt_Field = header_Field.getText();
			objLgr.info("Sorting has been started for field - "+txt_Field);
			for(int i=0;i<=1;i++) {
				List<WebElement> fields_Sort = rr.get_SortField(txt_Field);
				WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
				field_Sort.click();
				List<WebElement> row_Header = rr.get_RowHeader(tbl_CornTreatmentTrait);
				List<WebElement> row_Data = rr.get_RowData(tbl_CornTreatmentTrait);
				List<Hashtable<String, String>> SelectedCropData = commonPageMyAccount.read_TableData(row_Header, row_Data, rr.get_fieldData());
				String sort_Order = field_Sort.getAttribute("aria-sort");
				if (commonPageMyAccount.sort_TableData(SelectedCropData, txt_Field, sort_Order))
					message = "Sorting has been validated successfully in Corn Treatment Trait table for field " +txt_Field+ " in " + sort_Order + "order.";
				else {
					isTestCasePassed=false;
					message = "Sorting has been failed in Corn Treatment Trait table for field " +txt_Field+ " in " + sort_Order + "order.";
				}
				objLgr.info(message);
			}
		}
		if (!isTestCasePassed)
			Assert.fail("sort_CornTreatmentTrait_traitRoyalty has failed - "+message);
		else
			objLgr.info("Sorting has been validated successfully in Corn Treatment Trait table for all fields");
	}

	@Test(enabled=false, description = "Validate search functionality in View More table of Corn Treatment Trait table of Trait Royalty view more page",dependsOnMethods= {"viewData_traitRoyalty_RoyaltyReporting"}, priority=2015)
	public void search_CornTreatmentTrait_traitRoyalty() throws Exception {
		isTestCasePassed=true;
		String message = null;
		WebElement tbl_CornTreatmentTrait = rr.get_CornTreatmentTraitTable();
		List<WebElement> row_Header = rr.get_RowHeader(tbl_CornTreatmentTrait);
		List<WebElement> row_Data = rr.get_RowData(tbl_CornTreatmentTrait);
		List<Hashtable<String, String>> tableData = commonPageMyAccount.read_TableData(row_Header, row_Data, rr.get_fieldData());
		if (rr.get_SearchField()!= null) {
			String search_Txt=reusefunc.get_RandomString(tableData);
			objLgr.info("Search text is identified as - "+search_Txt);
			rr.get_SearchText().sendKeys(search_Txt);
			rr.get_SearchText().sendKeys(Keys.ENTER);
			List<WebElement> row_SearchData = rr.get_RowData(tbl_CornTreatmentTrait);
			List<Hashtable<String, String>> tableData_Search = commonPageMyAccount.read_TableData(row_Header, row_SearchData, rr.get_fieldData());
			objLgr.info("Total records found in Corn Treatment Trait table after searching with text " + search_Txt + " is - "+tableData_Search.size());
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
			Assert.fail("search_CornTreatmentTrait_traitRoyalty failed - " + message);
	}

	@Test(enabled=false, description = "Validate that back button works from Trait Royalty-View Data tab", dependsOnMethods= {"viewData_traitRoyalty_RoyaltyReporting"}, priority=2016)
	public void back_traitRoyalty() throws Exception {
		isTestCasePassed=true;
		String message = null;
		rr.getBackArrowoption().click();
		if (traitsRoyaltyWidgetHeader != null)
			message = "Trait Royalty widget loaded after clicking on back button from View More page";
		else {
			isTestCasePassed = false;
			message = "Trait Royalty failed to load after clicking on back button from View More page";
		}
		objLgr.info(message);
		if (!isTestCasePassed)
			Assert.fail("back_traitRoyalty failed - " + message);
	}


	//Validating if Genetic royalty widget is displayed
	@Test (enabled=false, description = "Validating whether Royalty Reporting page loaded",dependsOnMethods= {"back_traitRoyalty"}, priority=3001)
	public void geneticRoyalty_RoyaltyReporting() throws Exception {
		// Start the test by setting AUT's name, window or the page name that's being tested, viewport width and height
		isTestCasePassed=true;
		validate_LogOut();
		captureBrowserConsoleLogs();
		launchMyAccountHome();
		linkVisibility_Dashboard();
		pagecheck_royaltyReporting();
		geneticRoyaltyWidgetHeader = rr.get_geneticRoyalty_Header();
		if(rr.get_geneticRoyalty_body() != null) {
			objLgr.info("Genetic Royalty widget in Royalty Reporting page loaded successfuly");
			isLoginSuccessfull = true;
		}
		else
			isTestCasePassed=false;
		if (!isTestCasePassed)
			Assert.fail("Genetic Royalty widget in Royalty Reporting page not loaded successfuly");
		verifyPageWithApplitools(eyes);
	}

	@Test(enabled=false, description = "Check visiblility of VIEW DATA link of Genetic Royalty widget in Royalty Reporting page", dependsOnMethods= {"geneticRoyalty_RoyaltyReporting"}, priority=3002)
	public void visibilityViewData_geneticRoyalty_RoyaltyReporting() throws Exception {
		isTestCasePassed=true;
		String message = null;
		if (rr.get_ViewMoreLink(geneticRoyaltyWidgetHeader) != null) 
				message = "View More link is displayed in Genetic Royalty widget";
			else {
				isTestCasePassed = false;
				message = "View More link is not available in Genetic Royalty widget";
			}
			objLgr.info(message);
		
		if (!isTestCasePassed)
			Assert.fail("visibilityViewData_geneticRoyalty_RoyaltyReporting failed" );
	}

	@Test(enabled=false, description = "Validate VIEW DATA table of Genetic Royalty widget in Royalty Reporting page", dependsOnMethods= {"visibilityViewData_geneticRoyalty_RoyaltyReporting"}, priority=3003)
	public void viewData_geneticRoyalty_RoyaltyReporting() throws Exception {
		isTestCasePassed=true;
		String message = null;
		WebElement lnk_ViewData = rr.get_ViewMoreLink(geneticRoyaltyWidgetHeader);
		if (lnk_ViewData != null) {
			lnk_ViewData.click();
			WebElement tbl_CornGenetic = rr.get_CornGeneticstable();
			if (tbl_CornGenetic!=null)
				message = "Genetic table is loaded successfully";
			else if (rr.get_NoDataTable(geneticRoyaltyWidgetHeader) != null) {
				isTestCasePassed = false;
				message = "Genetic Royalty widget displays \"No Data Available\".";
			}
			else {
				isTestCasePassed = false;
				message = "Genetic table has not loaded successfully";
			}
			objLgr.info(message);
		}
		if (!isTestCasePassed)
			Assert.fail("viewData_geneticRoyalty_RoyaltyReporting failed - " + message);
		verifyPageWithApplitools(eyes);
	}

	@Test(enabled=false, description = "Sorting functionality validation of Corn Genetic tab of Genetic Royalty view more table", dependsOnMethods= {"viewData_geneticRoyalty_RoyaltyReporting"}, priority=3004)
	public void sort_CornGenetics_geneticRoyalty() throws Exception {
		isTestCasePassed=true;
		String message = null;
		WebElement tbl_CornGenetic = rr.get_CornGeneticstable();
		List<WebElement> row_HeaderList = rr.get_RowHeader(tbl_CornGenetic);
		for (WebElement header_Field : row_HeaderList) {
			String txt_Field = header_Field.getText();
			objLgr.info("Sorting has been started for field - "+txt_Field);
			for(int i=0;i<=1;i++) {
				List<WebElement> fields_Sort = rr.get_SortField(txt_Field);
				WebElement field_Sort = fields_Sort.get(0);
				field_Sort.click();
				List<WebElement> row_Header = rr.get_RowHeader(tbl_CornGenetic);
				List<WebElement> row_Data = rr.get_RowData(tbl_CornGenetic);
				List<Hashtable<String, String>> SelectedCropData = commonPageMyAccount.read_TableData(row_Header, row_Data, rr.get_fieldData());
				String sort_Order = field_Sort.getAttribute("aria-sort");
				if (commonPageMyAccount.sort_TableData(SelectedCropData, txt_Field, sort_Order))
					message = "Sorting has been validated successfully in Corn Genetics table for field " +txt_Field+ " in " + sort_Order + "order.";
				else {
					isTestCasePassed=false;
					message = "Sorting has been failed in Corn Genetics table for field " +txt_Field+ " in " + sort_Order + "order.";
				}
				objLgr.info(message);
			}
		}
		if (!isTestCasePassed)
			Assert.fail("sort_CornTrait_geneticRoyalty has failed - "+message);
		else
			objLgr.info("Sorting has been validated successfully in Corn Genetics table for all fields");
	}

	@Test(enabled=false, description = "Validate search functionality in View More table of Corn Genetics table of Genetics Royalty view more page",dependsOnMethods= {"viewData_geneticRoyalty_RoyaltyReporting"}, priority=3005)
	public void search_CornGenetics_geneticRoyalty() throws Exception {
		isTestCasePassed=true;
		String message = null;
		WebElement tbl_CornGenetic = rr.get_CornGeneticstable();
		List<WebElement> row_Header = rr.get_RowHeader(tbl_CornGenetic);
		List<WebElement> row_Data = rr.get_RowData(tbl_CornGenetic);
		List<Hashtable<String, String>> tableData = commonPageMyAccount.read_TableData(row_Header, row_Data, rr.get_fieldData());
		if (rr.get_SearchField()!= null) {
			String search_Txt=reusefunc.get_RandomString(tableData);
			objLgr.info("Search text is identified as - "+search_Txt);
			rr.get_SearchText().sendKeys(search_Txt);
			rr.get_SearchText().sendKeys(Keys.ENTER);
			List<WebElement> row_SearchData = rr.get_RowData(tbl_CornGenetic);
			List<Hashtable<String, String>> tableData_Search = commonPageMyAccount.read_TableData(row_Header, row_SearchData, rr.get_fieldData());
			objLgr.info("Total records found in Corn Genetics table after searching with text " + search_Txt + " is - "+tableData_Search.size());
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
			Assert.fail("search_CornGenetics_geneticRoyalty failed - " + message);
	}

	@Test(enabled=false, description = "Validate Soybeans tab loaded in Genetics Royalty View More page", dependsOnMethods= {"viewData_geneticRoyalty_RoyaltyReporting"}, priority=3006)
	public void soybeantblLoad_geneticRoyalty() throws Exception {
			isTestCasePassed=true;
			String message = null;
			rr.get_TabButton(rr.get_tab_Header().get(1)).click();
			String tableName = null;
			if (rr.get_Table() != null) {
				tableName = rr.get_TableHeader().getText();
				message = tableName + " table is loaded successfully";
			} else if (rr.get_NoDataTable(rr.get_SoybeanTraittable()) != null) {
				isTestCasePassed = false;
				message = tableName + " table displays \"No Data Available\".";
			} else {
				isTestCasePassed = false;
				message = tableName + " table has not loaded successfully";
			}
			objLgr.info(message);
			if (!isTestCasePassed)
				Assert.fail("soybeantblLoad_geneticRoyalty failed." + message);
			verifyPageWithApplitools(eyes);
		
	}

	@Test(enabled=false, description = "Sorting functionality validation of Soybean Genetics table of Genetics Royalty view more page", dependsOnMethods= {"viewData_geneticRoyalty_RoyaltyReporting"}, priority=3007)
	public void sort_SoybeanGenetics_geneticRoyalty() throws Exception {
		isTestCasePassed=true;
		String message = null;
		WebElement tbl_SoybeanGenetics = rr.get_SoybeansGeneticstable();
		List<WebElement> row_HeaderList = rr.get_RowHeader(tbl_SoybeanGenetics);
		for (WebElement header_Field : row_HeaderList) {
			String txt_Field = header_Field.getText();
			objLgr.info("Sorting has been started for field - "+txt_Field);
			for(int i=0;i<=1;i++) {
				List<WebElement> fields_Sort = rr.get_SortField(txt_Field);
				WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
				field_Sort.click();
				List<WebElement> row_Header = rr.get_RowHeader(tbl_SoybeanGenetics);
				List<WebElement> row_Data = rr.get_RowData(tbl_SoybeanGenetics);
				List<Hashtable<String, String>> SelectedCropData = commonPageMyAccount.read_TableData(row_Header, row_Data, rr.get_fieldData());
				String sort_Order = field_Sort.getAttribute("aria-sort");
				if (commonPageMyAccount.sort_TableData(SelectedCropData, txt_Field, sort_Order))
					message = "Sorting has been validated successfully in Soybean Genetics table for field " +txt_Field+ " in " + sort_Order + "order.";
				else {
					isTestCasePassed=false;
					message = "Sorting has been failed in Soybean Genetics table for field " +txt_Field+ " in " + sort_Order + "order.";
				}
				objLgr.info(message);
			}
		}
		if (!isTestCasePassed)
			Assert.fail("sort_SoybeanGenetics_geneticRoyalty has failed - "+message);
		else
			objLgr.info("Sorting has been validated successfully in Soybean Genetics table for all fields");
	}

	@Test(enabled=false, description = "Validate search functionality in View More table of Soybean Genetics table of Genetics Royalty view more page",dependsOnMethods= {"viewData_geneticRoyalty_RoyaltyReporting"}, priority=3008)
	public void search_SoybeanGenetics_geneticRoyalty() throws Exception {
		isTestCasePassed=true;
		String message = null;
		WebElement tbl_SoybeanGenetics = rr.get_SoybeansGeneticstable();
		List<WebElement> row_Header = rr.get_RowHeader(tbl_SoybeanGenetics);
		List<WebElement> row_Data = rr.get_RowData(tbl_SoybeanGenetics);
		List<Hashtable<String, String>> tableData = commonPageMyAccount.read_TableData(row_Header, row_Data, rr.get_fieldData());
		if (rr.get_SearchField()!= null) {
			String search_Txt=reusefunc.get_RandomString(tableData);
			objLgr.info("Search text is identified as - "+search_Txt);
			rr.get_SearchText().sendKeys(search_Txt);
			rr.get_SearchText().sendKeys(Keys.ENTER);
			List<WebElement> row_SearchData = rr.get_RowData(tbl_SoybeanGenetics);
			List<Hashtable<String, String>> tableData_Search = commonPageMyAccount.read_TableData(row_Header, row_SearchData, rr.get_fieldData());
			objLgr.info("Total records found in Soybean Genetics table after searching with text " + search_Txt + " is - "+tableData_Search.size());
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
			Assert.fail("search_SoybeanGenetics_geneticRoyalty failed - " + message);
	}

	@Test(enabled=false, description = "Validate Canola tab loaded in Genetics Royalty View More page", dependsOnMethods= {"viewData_geneticRoyalty_RoyaltyReporting"}, priority=3009)
	public void canolatblLoad_geneticRoyalty() throws Exception {
			isTestCasePassed=true;
			String message = null;
			rr.get_TabButton(rr.get_tab_Header().get(2)).click();
			String tableName = null;
			if (rr.get_Table() != null) {
				tableName = rr.get_TableHeader().getText();
				message = tableName + " table is loaded successfully";
			} else if (rr.get_NoDataTable(rr.get_CanolaTraitTable()) != null) {
				isTestCasePassed = false;
				message = tableName + " table displays \"No Data Available\".";
			} else {
				isTestCasePassed = false;
				message = tableName + " table has not loaded successfully";
			}
			objLgr.info(message);
			if (!isTestCasePassed)
				Assert.fail("canolatblLoad_geneticRoyalty failed." + message);
			verifyPageWithApplitools(eyes);
		
	}

	@Test(enabled=false, description = "Sorting functionality validation of Canola Genetics table of Genetics Royalty view more page", dependsOnMethods= {"viewData_geneticRoyalty_RoyaltyReporting"}, priority=3010)
	public void sort_CanolaGenetics_geneticRoyalty() throws Exception {
		isTestCasePassed=true;
		String message = null;
		WebElement tbl_CanolaGenetics = rr.get_CanolaGeneticstable();
		List<WebElement> row_HeaderList = rr.get_RowHeader(tbl_CanolaGenetics);
		for (WebElement header_Field : row_HeaderList) {
			String txt_Field = header_Field.getText();
			objLgr.info("Sorting has been started for field - "+txt_Field);
			for(int i=0;i<=1;i++) {
				List<WebElement> fields_Sort = rr.get_SortField(txt_Field);
				WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
				field_Sort.click();
				List<WebElement> row_Header = rr.get_RowHeader(tbl_CanolaGenetics);
				List<WebElement> row_Data = rr.get_RowData(tbl_CanolaGenetics);
				List<Hashtable<String, String>> SelectedCropData = commonPageMyAccount.read_TableData(row_Header, row_Data, rr.get_fieldData());
				String sort_Order = field_Sort.getAttribute("aria-sort");
				if (commonPageMyAccount.sort_TableData(SelectedCropData, txt_Field, sort_Order))
					message = "Sorting has been validated successfully in Canola Genetics table for field " +txt_Field+ " in " + sort_Order + "order.";
				else {
					isTestCasePassed=false;
					message = "Sorting has been failed in Canola Genetics table for field " +txt_Field+ " in " + sort_Order + "order.";
				}
				objLgr.info(message);
			}
		}
		if (!isTestCasePassed)
			Assert.fail("sort_CanolaGenetics_geneticRoyalty has failed - "+message);
		else
			objLgr.info("Sorting has been validated successfully in Canola Genetics table for all fields");
	}

	@Test(enabled=false, description = "Validate search functionality in View More table of Canola Genetics table of Genetics Royalty view more page",dependsOnMethods= {"viewData_geneticRoyalty_RoyaltyReporting"}, priority=3011)
	public void search_CanolaGenetics_geneticRoyalty() throws Exception {
		isTestCasePassed=true;
		String message = null;
		WebElement tbl_CanolaGenetics = rr.get_CanolaGeneticstable();
		List<WebElement> row_Header = rr.get_RowHeader(tbl_CanolaGenetics);
		List<WebElement> row_Data = rr.get_RowData(tbl_CanolaGenetics);
		List<Hashtable<String, String>> tableData = commonPageMyAccount.read_TableData(row_Header, row_Data, rr.get_fieldData());
		if (rr.get_SearchField()!= null) {
			String search_Txt=reusefunc.get_RandomString(tableData);
			objLgr.info("Search text is identified as - "+search_Txt);
			rr.get_SearchText().sendKeys(search_Txt);
			rr.get_SearchText().sendKeys(Keys.ENTER);
			List<WebElement> row_SearchData = rr.get_RowData(tbl_CanolaGenetics);
			List<Hashtable<String, String>> tableData_Search = commonPageMyAccount.read_TableData(row_Header, row_SearchData, rr.get_fieldData());
			objLgr.info("Total records found in Canola Genetics table after searching with text " + search_Txt + " is - "+tableData_Search.size());
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
			Assert.fail("search_CanolaTrait_traitRoyalty failed - " + message);
	}

	@Test(enabled=false, description = "Validate that back button works from Genetic Royalty-View Data tab", dependsOnMethods= {"viewData_geneticRoyalty_RoyaltyReporting"}, priority=3012)
	public void back_GeneticRoyalty() throws Exception {
		isTestCasePassed=true;
		String message = null;
		rr.getBackArrowoption().click();
		if (geneticRoyaltyWidgetHeader != null)
			message = "Genetics Royalty widget loaded after clicking on back button from View More page";
		else {
			isTestCasePassed = false;
			message = "Genetics Royalty failed to load after clicking on back button from View More page";
		}
		objLgr.info(message);
		if (!isTestCasePassed)
			Assert.fail("back_GeneticRoyalty failed - " + message);
	}


	//Validating if Cotton royalty widget is displayed
	@Test (enabled=false, description = "Validating whether Royalty Reporting page loaded",dependsOnMethods= {"back_GeneticRoyalty"}, priority=4001)
	public void cottonRoyalty_RoyaltyReporting() throws Exception {
		// Start the test by setting AUT's name, window or the page name that's being tested, viewport width and height
		isTestCasePassed=true;
		validate_LogOut();
		captureBrowserConsoleLogs();
		launchMyAccountHome();
		linkVisibility_Dashboard();
		pagecheck_royaltyReporting();
		cottonRoyaltyWidgetHeader = rr.get_cottonRoyalty_Header();
		if(rr.get_cottonRoyalty_body()!= null) {
			objLgr.info("Cotton Royalty widget in Royalty Reporting page loaded successfuly");
			isLoginSuccessfull = true;
		}
		else
			isTestCasePassed=false;
		if (!isTestCasePassed)
			Assert.fail("Cotton Royalty widget in Royalty Reporting page not loaded successfuly");
		verifyPageWithApplitools(eyes);
	}

	@Test(enabled=false, description = "Check visiblility of VIEW DATA link of Cotton Royalty widget in Royalty Reporting page", dependsOnMethods= {"cottonRoyalty_RoyaltyReporting"}, priority=4002)
	public void visibilityViewData_cottonRoyalty_RoyaltyReporting() throws Exception {
		isTestCasePassed=true;
		String message = null;
		if (rr.get_ViewMoreLink(cottonRoyaltyWidgetHeader) != null) 
				message = "View More link is displayed in Cotton Royalty widget";
			else {
				isTestCasePassed = false;
				message = "View More link is not available in Cotton Royalty widget";
			}
			objLgr.info(message);
		
		if (!isTestCasePassed)
			Assert.fail("visibilityViewData_cottonRoyalty_RoyaltyReporting failed" );
	}

	@Test(enabled=false, description = "Validate VIEW DATA table of Genetic Royalty widget in Royalty Reporting page", dependsOnMethods= {"visibilityViewData_cottonRoyalty_RoyaltyReporting"}, priority=4003)
	public void viewData_cottonRoyalty_RoyaltyReporting() throws Exception {
		isTestCasePassed=true;
		String message = null;
		WebElement lnk_ViewData = rr.get_ViewMoreLink(cottonRoyaltyWidgetHeader);
		if (lnk_ViewData != null) {
			lnk_ViewData.click();
			WebElement tbl_CottonRoyalty = rr.get_CottonRoyaltyTable();
			if (tbl_CottonRoyalty!=null)
				message = "Cotton Royalty table is loaded successfully";
			else if (rr.get_NoDataTable(cottonRoyaltyWidgetHeader) != null) {
				isTestCasePassed = false;
				message = "Cotton Royalty widget displays \"No Data Available\".";
			}
			else {
				isTestCasePassed = false;
				message = "Cotton table has not loaded successfully";
			}
			objLgr.info(message);
		}
		if (!isTestCasePassed)
			Assert.fail("viewData_cottonRoyalty_RoyaltyReporting failed - " + message);
		verifyPageWithApplitools(eyes);
	}

	@Test(enabled=false, description = "Sorting functionality validation of Zone tab of Cotton Royalty view more table", dependsOnMethods= {"viewData_cottonRoyalty_RoyaltyReporting"}, priority=4004)
	public void sort_Zone_cottonRoyalty() throws Exception {
		isTestCasePassed=true;
		String message = null;
		WebElement tbl_CottonRoyalty = rr.get_CottonRoyaltyTable();
		List<WebElement> row_HeaderList = rr.get_RowHeader(tbl_CottonRoyalty);
		for (WebElement header_Field : row_HeaderList) {
			String txt_Field = header_Field.getText();
			objLgr.info("Sorting has been started for field - "+txt_Field);
			for(int i=0;i<=1;i++) {
				List<WebElement> fields_Sort = rr.get_SortField(txt_Field);
				WebElement field_Sort = fields_Sort.get(0);
				field_Sort.click();
				List<WebElement> row_Header = rr.get_RowHeader(tbl_CottonRoyalty);
				List<WebElement> row_Data = rr.get_RowData(tbl_CottonRoyalty);
				List<Hashtable<String, String>> SelectedCropData = commonPageMyAccount.read_TableData(row_Header, row_Data, rr.get_fieldData());
				String sort_Order = field_Sort.getAttribute("aria-sort");
				if (commonPageMyAccount.sort_TableData(SelectedCropData, txt_Field, sort_Order))
					message = "Sorting has been validated successfully in Zone table for field " +txt_Field+ " in " + sort_Order + "order.";
				else {
					isTestCasePassed=false;
					message = "Sorting has been failed in Zone table for field " +txt_Field+ " in " + sort_Order + "order.";
				}
				objLgr.info(message);
			}
		}
		if (!isTestCasePassed)
			Assert.fail("sort_Zone_cottonRoyalty has failed - "+message);
		else
			objLgr.info("Sorting has been validated successfully in Zone table for all fields");
	}

	@Test(enabled=false, description = "Validate search functionality in View More table of Corn Genetics table of Genetics Royalty view more page",dependsOnMethods= {"viewData_cottonRoyalty_RoyaltyReporting"}, priority=4005)
	public void search_Zone_cottonRoyalty() throws Exception {
		isTestCasePassed=true;
		String message = null;
		WebElement tbl_CottonRoyalty = rr.get_CottonRoyaltyTable();
		List<WebElement> row_Header = rr.get_RowHeader(tbl_CottonRoyalty);
		List<WebElement> row_Data = rr.get_RowData(tbl_CottonRoyalty);
		List<Hashtable<String, String>> tableData = commonPageMyAccount.read_TableData(row_Header, row_Data, rr.get_fieldData());
		if (rr.get_SearchField()!= null) {
			String search_Txt=reusefunc.get_RandomString(tableData);
			objLgr.info("Search text is identified as - "+search_Txt);
			rr.get_SearchText().sendKeys(search_Txt);
			rr.get_SearchText().sendKeys(Keys.ENTER);
			List<WebElement> row_SearchData = rr.get_RowData(tbl_CottonRoyalty);
			List<Hashtable<String, String>> tableData_Search = commonPageMyAccount.read_TableData(row_Header, row_SearchData, rr.get_fieldData());
			objLgr.info("Total records found in Zone table after searching with text " + search_Txt + " is - "+tableData_Search.size());
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
			Assert.fail("search_Zone_cottonRoyalty failed - " + message);
	}

	@Test(enabled=false, description = "Validate Product Name tab loaded in Cotton Royalty View More page", dependsOnMethods= {"viewData_cottonRoyalty_RoyaltyReporting"}, priority=4006)
	public void productNametblLoad_cottonRoyalty() throws Exception {
			isTestCasePassed=true;
			String message = null;
			rr.get_TabButton(rr.get_tab_Header().get(1)).click();
			String tableName = null;
			if (rr.get_Table() != null) {
				tableName = rr.get_TableHeader().getText();
				message = tableName + " table is loaded successfully";
			} else if (rr.get_NoDataTable(rr.get_SoybeanTraittable()) != null) {
				isTestCasePassed = false;
				message = tableName + " table displays \"No Data Available\".";
			} else {
				isTestCasePassed = false;
				message = tableName + " table has not loaded successfully";
			}
			objLgr.info(message);
			if (!isTestCasePassed)
				Assert.fail("soybeantblLoad_geneticRoyalty failed." + message);
			verifyPageWithApplitools(eyes);
		
	}

	@Test(enabled=false, description = "Sorting functionality validation of Product Name table of Cotton Royalty view more page", dependsOnMethods= {"viewData_cottonRoyalty_RoyaltyReporting"}, priority=4007)
	public void sort_productName_cottonRoyalty() throws Exception {
		isTestCasePassed=true;
		String message = null;
		WebElement tbl_CottonRoyalty = rr.get_CottonRoyaltyTable();
		List<WebElement> row_HeaderList = rr.get_RowHeader(tbl_CottonRoyalty);
		for (WebElement header_Field : row_HeaderList) {
			String txt_Field = header_Field.getText();
			objLgr.info("Sorting has been started for field - "+txt_Field);
			for(int i=0;i<=1;i++) {
				List<WebElement> fields_Sort = rr.get_SortField(txt_Field);
				WebElement field_Sort = fields_Sort.get(fields_Sort.size()-1);
				field_Sort.click();
				List<WebElement> row_Header = rr.get_RowHeader(tbl_CottonRoyalty);
				List<WebElement> row_Data = rr.get_RowData(tbl_CottonRoyalty);
				List<Hashtable<String, String>> SelectedCropData = commonPageMyAccount.read_TableData(row_Header, row_Data, rr.get_fieldData());
				String sort_Order = field_Sort.getAttribute("aria-sort");
				if (commonPageMyAccount.sort_TableData(SelectedCropData, txt_Field, sort_Order))
					message = "Sorting has been validated successfully in Product Name table for field " +txt_Field+ " in " + sort_Order + "order.";
				else {
					isTestCasePassed=false;
					message = "Sorting has been failed in  Product Name table for field " +txt_Field+ " in " + sort_Order + "order.";
				}
				objLgr.info(message);
			}
		}
		if (!isTestCasePassed)
			Assert.fail("sort_productName_cottonRoyalty has failed - "+message);
		else
			objLgr.info("Sorting has been validated successfully in Product Name table for all fields");
	}

	@Test(enabled=false, description = "Validate search functionality in View More table of Product Name table of Cotton Royalty view more page",dependsOnMethods= {"viewData_cottonRoyalty_RoyaltyReporting"}, priority=4008)
	public void search_productName_cottonRoyalty() throws Exception {
		isTestCasePassed=true;
		String message = null;
		WebElement tbl_CottonRoyalty = rr.get_CottonRoyaltyTable();
		List<WebElement> row_Header = rr.get_RowHeader(tbl_CottonRoyalty);
		List<WebElement> row_Data = rr.get_RowData(tbl_CottonRoyalty);
		List<Hashtable<String, String>> tableData = commonPageMyAccount.read_TableData(row_Header, row_Data, rr.get_fieldData());
		if (rr.get_SearchField()!= null) {
			String search_Txt=reusefunc.get_RandomString(tableData);
			objLgr.info("Search text is identified as - "+search_Txt);
			rr.get_SearchText().sendKeys(search_Txt);
			rr.get_SearchText().sendKeys(Keys.ENTER);
			List<WebElement> row_SearchData = rr.get_RowData(tbl_CottonRoyalty);
			List<Hashtable<String, String>> tableData_Search = commonPageMyAccount.read_TableData(row_Header, row_SearchData, rr.get_fieldData());
			objLgr.info("Total records found in Product Name table after searching with text " + search_Txt + " is - "+tableData_Search.size());
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
			Assert.fail("search_productName_cottonRoyalty failed - " + message);
	}

	@Test(enabled=false, description = "Validate that back button works from Cotton Royalty-View Data tab", dependsOnMethods= {"viewData_cottonRoyalty_RoyaltyReporting"}, priority=4009)
	public void back_CottonRoyalty() throws Exception {
		isTestCasePassed=true;
		String message = null;
		rr.getBackArrowoption().click();
		if (cottonRoyaltyWidgetHeader != null)
			message = "Cotton Royalty widget loaded after clicking on back button from View More page";
		else {
			isTestCasePassed = false;
			message = "Cotton Royalty failed to load after clicking on back button from View More page";
		}
		objLgr.info(message);
		if (!isTestCasePassed)
			Assert.fail("back_CottonRoyalty failed - " + message);
	}

	//-------------------------------LogOut Test case------------------------------//

	@Test(enabled=false, description = "Log out from My Account", dependsOnMethods= {"back_CottonRoyalty"}, priority=10001, alwaysRun=true)
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
