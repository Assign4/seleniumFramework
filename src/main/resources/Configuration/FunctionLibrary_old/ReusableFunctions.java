package FunctionLibrary;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.specification.RequestSpecification;

public class ReusableFunctions_old{
	private static Logger objLgr = LogManager.getLogger(ReusableFunctions.class.getName());
	
	public Properties readConfig(String propertyFileLocation) throws IOException {
		objLgr.info("Test Execution begins");
		Properties configFileProp =new Properties();
		FileInputStream configFile = new FileInputStream(propertyFileLocation);
		if (configFile == null) {
			objLgr.error("Failed to read Property file" + propertyFileLocation);
		} else {
			objLgr.info("Property file" + propertyFileLocation + " read");
		}
		
		configFileProp.load(configFile);	
		return configFileProp;
	}
	
	
	public WebDriver initialize(Properties localProp) throws IOException {
		WebDriver webdriver;
		if(localProp.getProperty("browser").contains("chrome")) {
			System.setProperty("webdriver.chrome.driver", localProp.getProperty("chromeDriverLocation") );
			
		}
		if(localProp.getProperty("browser").contains("Internet Explorer")) {
			//Create Internet Explorer webdriver object
		}
		if(localProp.getProperty("browser").contains("Safari")) {
			//Create Safari webdriver object
		}
		webdriver = new ChromeDriver();
	    
	    if(webdriver == null) {
	    	objLgr.error("Webdriver object createion for " + localProp.getProperty("browser") + " failed.");
	    } else {
	    	objLgr.info("Webdriver object created for " + localProp.getProperty("browser") + " browser.");
	    }
		return webdriver;		
	}	
	
	
	
//	public WebDriver localWebDriver;
	public void closeApp(WebDriver localWebDriver){
//		localWebDriver.close();
		localWebDriver.quit();
	}
	
	
	public JSONObject callAPI(String requestMethod, String apiURL) throws Exception {
		String USER_AGENT = "Mozilla/5.0";
		URL obj = new URL(apiURL);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		
		if(requestMethod == "POST"){
			con.setRequestMethod("POST");
		}
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		int responseCode = con.getResponseCode();
		objLgr.info("Response Code : " + responseCode);		
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		objLgr.info(response.toString());
		JSONObject jsonObject = new JSONObject(response.toString());
		return jsonObject;		
	}
	
	
	
	
//	================================================================================================================
	public JSONObject noPayload_getAPIcall (WebDriver localWebDriver, String APIURL) throws Throwable{
		localWebDriver.get(APIURL);
		WebElement element = localWebDriver.findElement(By.xpath("//pre"));
		JSONObject jsonObject = new JSONObject(element.getText());
		return jsonObject;
	}
	
	public void noPayload_postAPIcall(WebDriver localWebDriver, String APIURL) throws JSONException,InterruptedException {
		
		//Initializing Rest API's URL
//		String APIUrl = "http://{API URL}";
			
		//Initializing payload or API body
		String APIBody = ""; //e.g.- "{\"key1\":\"value1\",\"key2\":\"value2\"}"
					
		// Building request using requestSpecBuilder
		RequestSpecBuilder builder = new RequestSpecBuilder();
			
		//Setting API's body
		builder.setBody(APIBody);
			
		//Setting content type as application/json or application/xml
		builder.setContentType("application/json; charset=UTF-8");
			
		RequestSpecification requestSpec = builder.build();

		//Making post request with authentication, leave blank in case there are no credentials- basic("","")
		//Response response = given().authentication().preemptive().basic("","").spec(requestSpec).when().post(APIURL);

		}	

}
