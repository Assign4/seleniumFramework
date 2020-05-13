package Factory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
//	import org.apache.logging.log4j.LogManager;
//	import org.apache.logging.log4j.Logger;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import FunctionLibrary.ReusableFunctions;
import myAccount.APIResponseToExcel;

public class APIResponseToExcel_Factory {
	//private static Logger objLgr = LogManager.getLogger(MergeLMVResponse_Factory.class.getName());

	@Factory(dataProvider="MyAccountTestData")
	public Object[] HybrisResponseToExcel_Factory(final Hashtable<String,String> data) {	
		return new Object[] {new APIResponseToExcel(data)};
	}

	@SuppressWarnings("unchecked")
	@DataProvider(name="MyAccountTestData")
	public static Object[][] myAccountUserList() throws IOException {
		ArrayList<Hashtable<String, String>> refinedDataset = new ArrayList<Hashtable<String, String>>();
		ReusableFunctions rf = new ReusableFunctions();

		@SuppressWarnings("static-access")
		Object[][] rawdata = rf.universalDataProvider_v2("Data/APIResponseDataSheet.xlsx","Datasheet");
		for (int i=0;i<rawdata.length;i++) {
			Hashtable<String,String> datainstance = (Hashtable<String, String>) rawdata[i][0];
			if (datainstance.get("Execute(Y/N)").equalsIgnoreCase("Yes")) {
				refinedDataset.add((Hashtable<String, String>) rawdata[i][0]);
			}
		}
		Object[][] finalDataSet=new Object[refinedDataset.size()][1];
		for (int i=0;i<refinedDataset.size();i++) {
			Hashtable<String,String> datainstance = refinedDataset.get(i);
			finalDataSet[i][0] = datainstance;
		}
		return finalDataSet;
	}  
}

