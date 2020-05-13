package driver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.Date;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.selenium.Eyes;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import java.util.logging.Level;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;

import io.appium.java_client.remote.MobileCapabilityType;

import org.apache.logging.log4j.*;

public class Driver extends ExtentReporterNG{
	private static Logger objLgr = LogManager.getLogger(Driver.class.getName());
	protected String propertyFileLocation = "src/main/java/driver/AutomationParameters.properties";
	public static WebDriver webdriver;
	public static RemoteWebDriver remWebDriver;
	public static Actions actn;
	public static Properties prop;
	public static FileInputStream fisPropfile;
	protected String demoSleepTime;
	protected Boolean safariBrowserUsed = false;
	public static SessionId sessionID;
	public static String PingId;
	public static String pwd;
	public static String platform;
	public static String browser;
	public static Eyes eyes;
	public static String applitoolsServerURI, applitoolsAPIKey;
	public int viewportWidth, viewportHeight;
	public boolean applitoolsTurnedOn;
	public String env, appName;
	public ChromeOptions chromeOptions;

	public void getAutomationProperties() throws IOException {

		prop=new Properties();
		fisPropfile = new FileInputStream(propertyFileLocation);
		if (fisPropfile == null)
			objLgr.error("Test Execution begins");
		else
			objLgr.info("Property file " + propertyFileLocation + " read");
		prop.load(fisPropfile);
		platform = prop.getProperty("Platform");
		browser = prop.getProperty("Browser");
//		applitoolsServerURI = prop.getProperty("ApplitoolsServerURL");
//		applitoolsAPIKey=prop.getProperty("ApplitoolsAPIKey");
//		viewportWidth=Integer.parseInt(prop.getProperty("ViewportWidthSize"));
//		viewportHeight=Integer.parseInt(prop.getProperty("ViewportHeightSize"));
//		applitoolsTurnedOn=Boolean.parseBoolean(prop.getProperty("ApplitoolsTurnedOn"));
//		appName = prop.getProperty("ApplitoolsAppName");
	}


	public WebDriver initialize(Hashtable<String, String> remote_platform) throws IOException {
		objLgr.info("Initializing webdriver/remoteWebDriver.");
		/*prop=new Properties();
		fisPropfile = new FileInputStream(propertyFileLocation);
		if (fisPropfile == null)
			objLgr.error("Test Execution begins");
		else
			objLgr.info("Property file " + propertyFileLocation + " read");
		prop.load(fisPropfile);
		browser = prop.getProperty("Browser");
		platform = prop.getProperty("Platform");
		applitoolsServerURI = prop.getProperty("ApplitoolsServerURL");
		applitoolsAPIKey=prop.getProperty("ApplitoolsAPIKey");*/
		getAutomationProperties();
		chromeOptions = new ChromeOptions();
		if (platform.equalsIgnoreCase("Perfecto"))
			browser = remote_platform.get("browserName");
		objLgr.info("Test running on " + platform + " with " + browser + " browser.");
		if(platform.equalsIgnoreCase("Perfecto")) {
			try {
				perfecto(remote_platform);
			} catch(Exception e) {
				objLgr.error("Failed to launch Remote WebDriver " + e);
			}
		}

		//Load driver for Cross Browser
		else if(platform.equalsIgnoreCase("crossbrowsertesting.com")) {
			try {
				crossBrowser(remote_platform);
			} catch(Exception e) {
				objLgr.error("Failed to launch Remote WebDriver " + e);
			}
		}

		else if(prop.getProperty("Browser").equalsIgnoreCase("Chrome")) {	
			try {
				chromedriver();		
			} catch(Exception e) {
				objLgr.error("Failed to launch " + prop.getProperty("Browser") + " browser.");
			}
		}

		//Load driver for Internet Explorer
		else if(prop.getProperty("Browser").equalsIgnoreCase("Internet Explorer")) {
			//Create Internet Explorer Webdriver object
			IEdriver();
		}

		//Load driver for Safari Browser
		else if(prop.getProperty("Browser").equalsIgnoreCase("Safari")) {			
			//Create Safari webdriver object
			SafariDriver();			
		}

		//Load driver for Mobile Emulator Browser
		else if(prop.getProperty("Browser").equalsIgnoreCase("MobileChrome")) {			
			//Create Mobile Emulator webdriver object
			mobilechrome();
		}
		

		else {
			objLgr.error("Browser is not configured."+ " failed.");
		}


		if(webdriver == null) {
			objLgr.error("Webdriver/RemoteWebDriver object creation Failed.");
		}

		//Create action object
		actn = new Actions(webdriver);
		if(prop.getProperty("RunType").equalsIgnoreCase("Demo")) {
			webdriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
		demoSleepTime = prop.getProperty("DemoSleepTime");
		//Maximize browser for Windows and Mac
//		if (platform.equals("MAC") || (platform.equals("crossbrowsertesting.com") && platform_CBT.toString().contains("Desktop") && platform_CBT.toString().contains("Mac")))
//			webdriver.manage().window().fullscreen();
//		else if (platform.equals("Windows") || (platform.equals("crossbrowsertesting.com") && platform_CBT.toString().contains("Desktop") && platform_CBT.toString().contains("Windows")))
//			webdriver.manage().window().maximize();
		return webdriver;		
	}
		
	
	
	//Method to initialize Chrome Driver
	public void chromedriver(){
		if (platform.equals("Windows"))
			chromeOptions.setExperimentalOption("useAutomationExtension", false);
		chromeOptions.addArguments("disable-infobars");
		if (platform.equals("MAC"))
			System.setProperty("webdriver.chrome.driver", prop.getProperty("MACChromeDriverLocation"));
		else
			System.setProperty("webdriver.chrome.driver", prop.getProperty("ChromeDriverLocation"));
		DesiredCapabilities caps = DesiredCapabilities.chrome();
		LoggingPreferences logPrefs = new LoggingPreferences();
		logPrefs.enable(LogType.BROWSER, Level.ALL);
		caps.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
		caps.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
//		driver = new ChromeDriver(caps);
//		webdriver= new ChromeDriver(chromeOptions);
		webdriver= new ChromeDriver(caps);
	}
/**/
	//Method to initialize Internet Explorer Driver
	public void IEdriver() {
		System.setProperty("webdriver.ie.driver", prop.getProperty("IEDriverLocation") );
		webdriver= new InternetExplorerDriver();
	}

	//Method to initialize Safari Driver
	public void SafariDriver() {
		webdriver = new SafariDriver();
		safariBrowserUsed = true;
	}

	//Method to initialize mobile Chrome Driver
	public void mobilechrome() {
		String deviceName = prop.getProperty("Device");
		objLgr.info("deviceName=" + deviceName);
		Map<String, String> mobileEmulation = new HashMap<String, String>();      
		mobileEmulation.put("deviceName", deviceName);
		System.setProperty("webdriver.chrome.driver", prop.getProperty("ChromeDriverLocation") );
		Map<String, Object> chromeOptions = new HashMap<String, Object>();
		chromeOptions.put("mobileEmulation", mobileEmulation);
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
		webdriver = new ChromeDriver(capabilities);
	}
	
	//Method to initialize RemoteWebDriver for Perfecto
    public WebDriver perfecto(Hashtable<String, String> platform_Perfecto) throws IOException {
        objLgr.info("Connecting to Perfecto.com");
        try {
        	    DesiredCapabilities caps = new DesiredCapabilities();
        	    caps.setCapability("securityToken", prop.getProperty("PerfectoAuthKey"));
				String url = "https://bayer.perfectomobile.com/nexperience/perfectomobile/wd/hub/fast";
				if(platform_Perfecto.get("Device").equalsIgnoreCase("Desktop")) {
        	    caps.setCapability("platformName",platform_Perfecto.get("platformName"));
        	    caps.setCapability("platformVersion", platform_Perfecto.get("platformVersion"));
        	    caps.setCapability("browserName", platform_Perfecto.get("browserName"));
        	    caps.setCapability("browserVersion", platform_Perfecto.get("browserVersion"));
        	    caps.setCapability("resolution", "1366x768");
				}
				else if(platform_Perfecto.get("Device").equalsIgnoreCase("ipad")) {
					caps.setCapability("platformName",platform_Perfecto.get("platformName"));
	        	    caps.setCapability("platformVersion", platform_Perfecto.get("platformVersion"));
	        	    caps.setCapability("browserName", platform_Perfecto.get("browserName"));
	        	    caps.setCapability("browserVersion", platform_Perfecto.get("browserVersion"));
	        	    caps.setCapability("manufacturer", platform_Perfecto.get("manufacturer"));
	        	    caps.setCapability("model", platform_Perfecto.get("model"));
				}
        	    webdriver = new RemoteWebDriver(new URL(url), caps);
				objLgr.info("capabilities : " + caps.asMap().toString());
				sessionID = ((RemoteWebDriver) webdriver).getSessionId();
				objLgr.info("Current session ID is " + ((RemoteWebDriver) webdriver).getSessionId().toString());
        } catch (Exception e){
               objLgr.info("Perfecto connection failed. " + e);
        }
        return webdriver;
 }

	//Method to initialize mobile Chrome Driver
	public WebDriver crossBrowser(Hashtable<String, String> platform_CBT) throws IOException {
		objLgr.info("Connecting to crossbrowsertesting.com");
		try {
			String username = prop.getProperty("CrossBrowserUserName");
			objLgr.info("Crossbrowser user identified as - " +username);
			String authkey = prop.getProperty("CrossBrowserAuthKey");
			DesiredCapabilities caps = new DesiredCapabilities();
			String device_CBT = platform_CBT.get("Device");
			if (device_CBT.equalsIgnoreCase("Desktop")) {
				objLgr.info("Setting capabilities for crossbrowsertesting.com in "+device_CBT+" with platform "+platform_CBT.get("Platform"));
				chromeOptions.addArguments("disable-infobars");
				caps.setCapability("name", "CBT Validation in "+device_CBT+" with platform "+platform_CBT.get("Platform"));
				caps.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
				caps.setCapability("browserName", platform_CBT.get("Browser"));
				caps.setCapability("version", platform_CBT.get("BrowserVersion"));
				caps.setCapability("platform", platform_CBT.get("Platform"));	
				caps.setCapability("screenResolution", "1366x768");
				caps.setCapability("record_video", "true");
				caps.setCapability("record_network", "false");
				caps.setCapability("max_duration", 12200);

			} else if (device_CBT.equalsIgnoreCase("Mobile")) {
				objLgr.info("Setting capabilities for crossbrowsertesting.com in "+device_CBT+" with platform "+platform_CBT.get("Platform")+" and device - "+platform_CBT.get("TestDevice"));
				caps.setCapability("name", "CBT Validation in "+device_CBT+" with platform "+platform_CBT.get("Platform")+" and device - "+platform_CBT.get("TestDevice"));
				caps.setCapability("build", "1.0");
				caps.setCapability("browserName", platform_CBT.get("Browser"));
				caps.setCapability("deviceName", platform_CBT.get("TestDevice"));
				caps.setCapability("platformVersion", platform_CBT.get("PlatformVersion"));
				caps.setCapability("platformName", platform_CBT.get("Platform"));
				caps.setCapability("deviceOrientation", platform_CBT.get("DeviceOrientation"));
				caps.setCapability("record_video", "true");
				caps.setCapability("record_network", "false");
				caps.setCapability("max_duration", 12200);
			}
			webdriver = new RemoteWebDriver(new URL("http://" + username + ":" + authkey + "@hub.crossbrowsertesting.com:80/wd/hub"), caps);
			objLgr.info("Running test on crossbrowsertesting.com as " + username);
			objLgr.info("capabilities : " + caps.asMap().toString());
			sessionID = ((RemoteWebDriver) webdriver).getSessionId();
			objLgr.info("Current session ID is " + ((RemoteWebDriver) webdriver).getSessionId().toString());
		} catch (Exception e){
			objLgr.info("crossbrowsertesting.com connection failed. " + e);
		}
		return webdriver;
	}

	//Method to initialize Android Driver
	public WebDriver initAndroidMobWebDriver() throws IOException {
		DesiredCapabilities objDCap = new DesiredCapabilities(); 
		objDCap.setCapability(MobileCapabilityType.DEVICE_NAME, "Nexus_5X_API_25");
		objDCap.setCapability("platformName", "Android");
		objDCap.setCapability("browserName", "chrome");
		webdriver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), objDCap);
		return webdriver;		
	}

	//Method to initialize OS Mobile Driver
	public WebDriver initiOSMobWebDriver() throws MalformedURLException, InterruptedException {
		/*This method uses the xCode simulator to run tests on.  However xCode simulator does not allow to install chrome on it
		 So testing with xCode simulator using Chrome will not be possible
		 Alternative to this is to use iPhone emulator provided by Chrome.  Function mobileEmulation has been defined for the same*/

		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("deviceName", "iPhone 6");
		//capabilities.setCapability("udid", "");
		capabilities.setCapability("platformName", "iOS");
		capabilities.setCapability("platformVersion", "11.0");
		//capabilities.setCapability("automationName", "XCUITest");
		capabilities.setCapability("browserName", "Safari");
		capabilities.setCapability("newCommandTimeout",180);
		capabilities.setCapability("webkitResponseTimeout", 70000);
		capabilities.setCapability("networkConnectionEnabled",true);
		capabilities.setCapability("--session-override",true);

		webdriver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
		webdriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		Thread.sleep(1000);
		return webdriver;		
	}	

	//Method to initialize Mobile Emulation
	public WebDriver mobileEmulation() {
		String deviceName = "iPhone 6";
		objLgr.info("deviceName=" + deviceName);
		Map<String, String> mobileEmulation = new HashMap<String, String>();      
		mobileEmulation.put("deviceName", deviceName);
		System.setProperty("webdriver.chrome.driver", prop.getProperty("ChromeDriverLocation") );
		Map<String, Object> chromeOptions = new HashMap<String, Object>();
		chromeOptions.put("mobileEmulation", mobileEmulation);
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
		webdriver = new ChromeDriver(capabilities);
		return webdriver;
	} 	

	//Method to get Screenshot
	public void getScreenshot(String result) throws IOException, UnirestException {
		if(prop.getProperty("Platform").equalsIgnoreCase("crossbrowsertesting.com")) {
			String snapshotHash = takeSnapshot(sessionID.toString());
            setDescription(sessionID.toString(), snapshotHash, result);
			objLgr.info("Screenshot taken on CBT: sessionID=" + sessionID.toString() + "; snapshotHash=" + snapshotHash + "; result=" + result);
		}
		else {
			objLgr.info("Screenshot location " + prop.getProperty("ScreenshotLocation")+result+prop.getProperty("Screenshot")+"screenshot.png");
			if(webdriver == null) {
				objLgr.info("Webdriver is null.  Cannot take screenshot");
			}
			else {
				File src=((TakesScreenshot)webdriver).getScreenshotAs(OutputType.FILE);
				try{
					FileUtils.copyFile(src, new File(prop.getProperty("ScreenshotLocation")+result+prop.getProperty("Screenshot")+"screenshot.png"));
				} catch (Exception e){
					objLgr.info("Screenshoting failed!!! " + e);
				}

			}
		}
	}
	
	public String takeSnapshot(String seleniumTestId) throws UnirestException {
        /*
         * Takes a snapshot of the screen for the specified test.
         * The output of this function can be used as a parameter for setDescription()
         */
        HttpResponse<JsonNode> response = Unirest.post("http://crossbrowsertesting.com/api/v3/selenium/{seleniumTestId}/snapshots")
                .basicAuth(prop.getProperty("CrossBrowserUserName"), prop.getProperty("CrossBrowserAuthKey"))
                .routeParam("seleniumTestId", seleniumTestId)
                .asJson();
		objLgr.info("Screenshot link: " + response.getBody().getObject().get("image"));
        // grab out the snapshot "hash" from the response
        String snapshotHash = (String) response.getBody().getObject().get("hash");
        
        return snapshotHash;
    }
	
	public JsonNode setDescription(String seleniumTestId, String snapshotHash, String description) throws UnirestException{
        /* 
         * sets the description for the given seleniemTestId and snapshotHash
         */
        HttpResponse<JsonNode> response = Unirest.put("http://crossbrowsertesting.com/api/v3/selenium/{seleniumTestId}/snapshots/{snapshotHash}")
                .basicAuth(prop.getProperty("CrossBrowserUserName"), prop.getProperty("CrossBrowserAuthKey"))
                .routeParam("seleniumTestId", seleniumTestId)
                .routeParam("snapshotHash", snapshotHash)
                .field("description", description)
                .asJson();
        return response.getBody();
    }

	public static String getScreenshotNew(String result) throws IOException {
		objLgr.info("Screenshot location " + prop.getProperty("ScreenshotLocation")+result+prop.getProperty("Screenshot")+"screenshot.png");
		String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		if(webdriver == null) {
			objLgr.info("Webdriver is null.  Cannot take screenshot");
		}
		else {
			File src=((TakesScreenshot)webdriver).getScreenshotAs(OutputType.FILE);
			try{
				FileUtils.copyFile(src, new File(prop.getProperty("ScreenshotLocation")+result+prop.getProperty("Screenshot")+dateName+".png"));
			} catch (Exception e){
				objLgr.info("Screenshoting failed!!! " + e);
			}

		}
		return prop.getProperty("ScreenshotLocation")+result+prop.getProperty("Screenshot")+dateName+".png";
	}

	//Method to wait for any element to load completely
	public void waitForElementLoad(WebElement webElement, String elementName) {
		WebDriverWait wait = new WebDriverWait(webdriver,4000);
		try {
			wait.until(ExpectedConditions.elementToBeClickable(webElement));
		}catch(NoSuchElementException exp) {
			objLgr.error("Failed to load element "+elementName+" Failed");
		}catch(ElementNotFoundException exp) {
			objLgr.error("Failed to load element "+elementName+" Failed");
		}catch(WebDriverException exp) {
			objLgr.error("Failed to load element "+elementName+" Failed");
		}		
	}

	//Method to get PingID
	public String pingid() throws IOException {
		objLgr.info("Test Execution begins");
		prop=new Properties();
		fisPropfile = new FileInputStream(propertyFileLocation);
		if (fisPropfile == null) {
			objLgr.error("Test Execution begins");
		} else {
			objLgr.info("Property file " + propertyFileLocation + " read");
		}

		prop.load(fisPropfile);
		PingId = prop.getProperty("PingID");
		objLgr.info("Testing with " + prop.getProperty("PingID") + " PingID");
		return PingId;
	}

	//Method to get Ping password
	public String pingpwd() throws IOException {
		objLgr.info("Test Execution begins");
		prop=new Properties();
		fisPropfile = new FileInputStream(propertyFileLocation);
		if (fisPropfile == null) {
			objLgr.error("Test Execution begins");
		}
		else {
			objLgr.info("Property file " + propertyFileLocation + " read");
		}

		prop.load(fisPropfile);
		pwd = prop.getProperty("PingPassword");
		objLgr.info("Testing with " + prop.getProperty("PingID") + " PingID");
		return pwd;
	}

	public void analyzeLog() {
		objLgr.info("------------------------------------------------------------------------");
		objLgr.info("Browser Console Log - Start");
		objLgr.info("------------------------------------------------------------------------");
		LogEntries logEntries = webdriver.manage().logs().get(LogType.BROWSER);
		for (LogEntry entry : logEntries) {
			objLgr.info(entry.getLevel() + " " + entry.getMessage());
			//do something useful with the data
		}
		objLgr.info("------------------------------------------------------------------------");
		objLgr.info("Browser Console Log - End");
		objLgr.info("------------------------------------------------------------------------");
	}

	public Eyes initializeApplitools(WebDriver webdriver, BatchInfo bi, String appName, String testName){
		try {
			eyes = new Eyes();
			eyes.setServerUrl(applitoolsServerURI);
			objLgr.info("Eyes object created and connected to Bayer Eyes Server");
//			eyes.setApiKey("bZFQWQOsEcwvKUur2z3PbaDYx0hcDazmXqsKhlHWMV0110");
			eyes.setApiKey(applitoolsAPIKey);
			objLgr.info("Applitools Eyes API Key set");
			eyes.setForceFullPageScreenshot(true);
			eyes.setSaveNewTests(false);
			objLgr.info("Eyes full page screenshot set to true");
		}catch (Exception e){
			objLgr.info("Failed to connect to Bayer Eyes Server " + eyes);
		}
		try {
			eyes.setBatch(bi);
			eyes.open(webdriver, appName, testName, new RectangleSize(viewportWidth, viewportHeight));
			objLgr.info("Applitools Eyes.open() successful.");
		} catch (Exception e){
			objLgr.info("Applitools Eyes.open() failed. " + e);
		}
		return eyes;
	}
	public void verifyPageWithApplitools(Eyes eyes){
		if(applitoolsTurnedOn) {
			try {
				objLgr.info("Verifying page using Applitools eyes");
				eyes.checkWindow();
			} catch (Exception e) {
				objLgr.info("Exception at Applitools eyes check window " + e);
			}
		}
	}
}
