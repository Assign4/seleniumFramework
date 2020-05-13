package driver;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;
 
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
 
public class ExtentReporterNG implements IReporter {
    private ExtentReports extent;
    private static Logger objLgr = LogManager.getLogger(ExtentReporterNG.class.getName());
 
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
    	objLgr.info("Generating Status Report at " + outputDirectory + File.separator + "ExtentReportsTestNG.html");
        extent = new ExtentReports(outputDirectory + File.separator + "ExtentReportsTestNG.html", true);
 
        for (ISuite suite : suites) {
            Map<String, ISuiteResult> result = suite.getResults();
 
            for (ISuiteResult r : result.values()) {
                ITestContext context = r.getTestContext();

                buildTestNodes(context.getPassedTests(), LogStatus.PASS);
                buildTestNodes(context.getFailedTests(), LogStatus.FAIL);
                buildTestNodes(context.getSkippedTests(), LogStatus.SKIP);
            }
        }
 
        extent.flush();
        extent.close();
        objLgr.info("------------------------------------------------------------------------");
        objLgr.info("Testing Report generation complete " + outputDirectory + File.separator + "ExtentReportsTestNG.html");
        objLgr.info("------------------------------------------------------------------------");
    }

    private void buildTestNodes(IResultMap tests, LogStatus status) {
        ExtentTest test;
        String testDesc = null;
        if (tests.size() > 0) {
            for (ITestResult result : tests.getAllResults()) {
                test = extent.startTest(result.getMethod().getMethodName());


                if(result.getMethod().getDescription() == null) {
                    testDesc = "Test description unavailable.";
                } else {
                    testDesc = result.getMethod().getDescription();
                }
                test.setDescription(testDesc);
 
                /*test.getTest(). = getTime(result.getStartMillis());
                test.getTest().endedTime = getTime(result.getEndMillis());*/
 
                for (String group : result.getMethod().getGroups())
                    test.assignCategory(group);

                String message = "Test " + status.toString().toLowerCase() + "ed: " + testDesc;
 
                if (result.getThrowable() != null)
                    message = result.getThrowable().getMessage();
 
                test.log(status, message);
 
                extent.endTest(test);
            }
        }
    }
 
    private Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();        
    }

    private String getTestInputArguments( ITestResult result ) {

        StringBuilder inputArguments = new StringBuilder();

        Object[] inputArgs = result.getParameters();
        inputArguments.append("( ");
        if (inputArgs != null && inputArgs.length > 0) {
            for (Object inputArg : inputArgs) {
                if (inputArg == null) {
                    inputArguments.append("null");
                } else {
                    inputArguments.append(inputArg.toString());
                }
                inputArguments.append(", ");
            }
            inputArguments.delete(inputArguments.length() - 2, inputArguments.length() - 1); //removing the last comma
        }
        inputArguments.append(")");

        return inputArguments.toString();
    }
}