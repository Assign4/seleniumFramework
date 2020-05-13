package driver;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.mashape.unirest.http.exceptions.UnirestException;

import driver.Driver;

public class listeners implements ITestListener,IInvokedMethodListener  {
	Driver b=new Driver();
	private static Logger objLgr = LogManager.getLogger(listeners.class.getName());
	
	public void onFinish(IInvokedMethod result) {
		// TODO Auto-generated method stub

	}

	public void onStart(ITestContext result) {
		// TODO Auto-generated method stub
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onTestFailure(ITestResult result) {
		// TODO Auto-generated method stub
		objLgr.info("onTestFailure screenshot for Test Case: " + result.getName());
		try {			
			b.getScreenshot(result.getName());			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(b == null){
				objLgr.info("b is null");
			} else {
				objLgr.info("b is NOT null");
			}
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			objLgr.info("Screenshoting failed " +e);
		}
	}

	public void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub
		objLgr.info(result.getName() + " skipped");
		objLgr.info("onTestSkipped screenshot for Test Case: " + result.getName());
		try {
			b.getScreenshot(result.getName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(b == null){
				objLgr.info("b is null");
			} else {
				objLgr.info("b is NOT null");
			}
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			objLgr.info("Screenshoting failed " +e);
		}

	}

	public void onTestStart(ITestResult result) {
		// TODO Auto-generated method stub
		objLgr.info("OnTestStart screenshot for Test Case: " + result.getName());
		try {
			b.getScreenshot(result.getName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(b == null){
				objLgr.info("b is null");
			} else {
				objLgr.info("b is NOT null");
			}
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			objLgr.info("Screenshoting failed " +e);
		}
	}

	public void onTestSuccess(ITestResult result) {
		// TODO Auto-generated method stub
		objLgr.info("onTestSuccess screenshot for Test Case: " + result.getName());
		try {
			b.getScreenshot(result.getName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(b == null){
				objLgr.info("b is null");
			} else {
				objLgr.info("b is NOT null");
			}
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			objLgr.info("Screenshoting failed " +e);
		}
	}

	public void onFinish(ITestContext arg0) {
		// TODO Auto-generated method stub
		
	}

	public void afterInvocation(IInvokedMethod arg0, ITestResult arg1) {
		// TODO Auto-generated method stub
		//objLgr.info("Finishing executing method : " + arg0.getTestMethod());
		
	}

	public void beforeInvocation(IInvokedMethod arg0, ITestResult arg1) {
		// TODO Auto-generated method stub
		//objLgr.info("Beginning executing  method : " + arg0.getTestMethod());
		
	}

}
