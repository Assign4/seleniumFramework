package FunctionLibrary;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.Properties;
import java.util.Set;


import org.apache.http.HeaderElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class ReadTable {
public static WebDriver webdriver;
protected static String propertyFileLocation = "src/main/java/driver/AutomationParameters.properties";
public static Properties prop;

public ReadTable(WebDriver webdriver) {
this.webdriver=webdriver;
}
public void readWebtable()
{
//WebElement mytable=webdriver.findElement(By.xpath("//div[@class='myProfile_info']"));
ArrayList<String> columnNames = new ArrayList<String>();
Hashtable<String, String> t1 = new Hashtable<String, String>();

List<WebElement> rowcount=webdriver.findElements(By.xpath("//div[@class='myProfile_info']/child::div"));


System.out.println("header size is"+rowcount.size());


for (WebElement headerElement: rowcount) {
	
	WebElement level = headerElement.findElement(By.xpath("./child::div[1]"));
	WebElement data = headerElement.findElement(By.xpath("./child::div[2]"));
	t1.put(level.getText(), data.getText());
}

Set<Entry<String, String>> entires = t1.entrySet();
for(Entry<String,String> ent:entires){
System.out.println("key is"+ent.getKey()+"+value is ==> "+ent.getValue());
}
}
public static void main(String args[]) throws IOException
{
FileInputStream fis=new FileInputStream(propertyFileLocation);
prop=new Properties();
prop.load(fis);
//System.setProperty("webdriver.chrome.driver", "C:\\gitLocalRepository\\tmuninst.ini\\chromedriver_win32_2.33\\chromedriver.exe");
System.setProperty("webdriver.chrome.driver", prop.getProperty("ChromeDriverLocation") );
webdriver= new ChromeDriver();
String URL="https://myaccount.velocity-np.ag/#/mygrowerdetails";
webdriver.get(URL);
System.out.println("test");
webdriver.manage().window().maximize();
webdriver.manage().timeouts().implicitlyWait(20,TimeUnit.SECONDS);
ReadTable mygrowerup=new ReadTable(webdriver);
mygrowerup.readWebtable();
}
}