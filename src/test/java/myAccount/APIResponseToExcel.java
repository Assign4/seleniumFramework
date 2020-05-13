package myAccount;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import FunctionLibrary.JSONFunctions;
import FunctionLibrary.ReusableFunctions;
import driver.Driver;

public class APIResponseToExcel extends Driver {
	private static Logger objLgr=LogManager.getLogger(APIResponseToExcel.class.getName());
	String parentSAPAccountID, sapAccountID;
	ReusableFunctions reusefunc = new ReusableFunctions();
	JSONFunctions jsonFunc = new JSONFunctions();
	JSONObject akanaAccessToken = null;
	
	@BeforeClass
	public void initialize() throws JSONException, Exception {
		getAutomationProperties();
	}

	public APIResponseToExcel(Hashtable<String, String> input) {
		this.parentSAPAccountID = input.get("ParentSAPID");
		this.sapAccountID = input.get("SAPAccountID");
	}

	@BeforeClass
	public void getAkanaAccessToken() throws Exception {             
		akanaAccessToken = jsonFunc.getToken_v2("POST","https://test.amp.monsanto.com/as/token.oauth2","PD-ACS2-MYACCOUNT-SVC","StOZ51iex3b8HpGNYX9CcJ3RwygIrRdkF9RokJkvofJz1SMOh2","client_credentials");
	}

	@Test (enabled=false)
	public void get_Top10FarmOppNB() throws JSONException, Exception {
		List<Hashtable<String, String>> data_HybrisResponse = new ArrayList<Hashtable<String, String>>();
		String[] fieldNames = {"Farmer Name", "Crop", "Gap"};
		JSONObject message = null;
		JSONArray crop_Record = null, cropDetails_Record = null;
		JSONObject farmerDetails_Record = null;
		//Store the API Response in a JSON Object
		objLgr.info("Hitting Shipping Status API for - "+sapAccountID);
		message = jsonFunc.callAPI_JSONObject("GET","https://api01-np.agro.services/hybris-q-api/dealer/" + sapAccountID + "/growerOppGapList",akanaAccessToken.getString("token_type") + " " + akanaAccessToken.getString("access_token"));
		reusefunc.writeToTextFile(prop.getProperty("DownloadPath")+sapAccountID+"-Top 10 Farmer Opp API Response.txt", message.toString());
		try {
			crop_Record = message.getJSONObject("aggregated").getJSONArray("details");
			for (int j = 0; j < crop_Record.length(); j++) {
				cropDetails_Record = crop_Record.getJSONObject(j).getJSONArray("cropDetails");
				for (int k = 0; k < cropDetails_Record.length(); k++) {
					Hashtable<String, String> data_Crop = new Hashtable<String, String>();
					farmerDetails_Record = cropDetails_Record.getJSONObject(k);
					data_Crop.put("Farmer Name", farmerDetails_Record.getString("growerName"));
					data_Crop.put("Crop", crop_Record.getJSONObject(j).getString("cropName"));
					data_Crop.put("Gap", String.valueOf(farmerDetails_Record.getLong("gap")));
					data_HybrisResponse.add(data_Crop);
				}
			}
			if (data_HybrisResponse.size() > 0) {
				reusefunc.writeLMVResponseToExcel(prop.getProperty("DownloadPath"), sapAccountID+"-Top Ten Farmer Opportunity.xlsx", "Farmer Opp", data_HybrisResponse, fieldNames);
				objLgr.info("Top Ten Farmer Opportunity API hit Successfully for SAP ID - "+sapAccountID);
			}
			else
				objLgr.info("Top Ten Farmer Opportunity data not present for SAP ID - "+sapAccountID);
		} catch (NullPointerException expnp) {
			objLgr.info("Top Ten Farmer Opportunity data not present for SAP ID - "+sapAccountID+". Error - "+expnp);
		}
	}

	@Test (enabled=false)
	public void get_DealerShippingStatus() throws JSONException, Exception {
		List<Hashtable<String, String>> data_HybrisResponse = new ArrayList<Hashtable<String, String>>();
		String[] fieldNames = {"Crop", "Dealer Order", "Shipped", "Scheduled", "To Ship", "Shipped %", "Scheduled %", "To Ship %"};
		JSONObject message = null;
		JSONArray unit_Record = null, percent_Record = null;
		//Store the API Response in a JSON Object
		objLgr.info("Hitting Shipping Status API for - "+sapAccountID);
		message = jsonFunc.callAPI_JSONObject("GET","https://api01-np.agro.services/hybris-q-api/dealer/" + sapAccountID + "/dealerShippingStatusData",akanaAccessToken.getString("token_type") + " " + akanaAccessToken.getString("access_token"));
		reusefunc.writeToTextFile(prop.getProperty("DownloadPath")+sapAccountID+"-Shipping Status API Response.txt", message.toString());
		try {
			unit_Record = message.getJSONObject("aggregated").getJSONArray("unit");
			percent_Record = message.getJSONObject("aggregated").getJSONArray("percentage");
			for (int j = 0; j < unit_Record.length(); j++) {
				Hashtable<String, String> data_Crop = new Hashtable<String, String>();
				data_Crop.put("Crop", unit_Record.getJSONObject(j).getString("key").replace("ALLCROPS", "Grand Total"));
				JSONObject data_Unit = unit_Record.getJSONObject(j).getJSONObject("value");
				JSONObject data_Percent = percent_Record.getJSONObject(j).getJSONObject("value");
				data_Crop.put("Dealer Order", String.valueOf(data_Unit.getLong("demand")));
				data_Crop.put("Shipped", String.valueOf(data_Unit.getLong("shipped")));
				data_Crop.put("Scheduled", String.valueOf(data_Unit.getLong("scheduled")));
				data_Crop.put("To Ship", String.valueOf(data_Unit.getLong("leftToShip")));
				data_Crop.put("Shipped %", String.valueOf(data_Percent.getLong("shipped"))+"%");
				data_Crop.put("Scheduled %", String.valueOf(data_Percent.getLong("scheduled"))+"%");
				data_Crop.put("To Ship %", String.valueOf(data_Percent.getLong("leftToShip"))+"%");
				data_HybrisResponse.add(data_Crop);
			}
			if (data_HybrisResponse.size() > 0) {
				reusefunc.writeLMVResponseToExcel(prop.getProperty("DownloadPath"), sapAccountID+"-Shipping Status.xlsx", "Shipping Status", data_HybrisResponse, fieldNames);
				objLgr.info("Dealer Shipping Status API hit Successfully for SAP ID - "+sapAccountID);
			}
			else
				objLgr.info("Shipping data not present for SAP ID - "+sapAccountID);
		} catch (NullPointerException expnp) {
			objLgr.info("Shipping Status data not present for SAP ID - "+sapAccountID+". Error - "+expnp);
		}
	}

	@Test (enabled=false)
	public void get_ProgToBizPlan() throws JSONException, Exception {
		List<Hashtable<String, String>> data_HybrisResponse = new ArrayList<Hashtable<String, String>>();
		String[] fieldNames = {"Crop", "Dealer Order", "Gap To Goal", "Dealer Order %", "Gap To Goal %"};
		JSONObject message = null;
		JSONArray unit_Record = null, percent_Record = null;
		//Store the API Response in a JSON Object
		objLgr.info("Hitting Progress To Business Plan API for - "+sapAccountID);
		message = jsonFunc.callAPI_JSONObject("GET","https://api01-np.agro.services/hybris-q-api/dealer/" + sapAccountID + "/progressToBusinessPlan",akanaAccessToken.getString("token_type") + " " + akanaAccessToken.getString("access_token"));
		reusefunc.writeToTextFile(prop.getProperty("DownloadPath")+sapAccountID+"-Progress To Business Plan API Response.txt", message.toString());
		try {
			unit_Record = message.getJSONObject("aggregated").getJSONArray("unit");
			percent_Record = message.getJSONObject("aggregated").getJSONArray("percentage");
			for (int j = 0; j < unit_Record.length(); j++) {
				Hashtable<String, String> data_Crop = new Hashtable<String, String>();
				data_Crop.put("Crop", unit_Record.getJSONObject(j).getString("key").replace("ALLCROPS", "Grand Total"));
				JSONObject data_Unit = unit_Record.getJSONObject(j).getJSONObject("value");
				JSONObject data_Percent = percent_Record.getJSONObject(j).getJSONObject("value");
				data_Crop.put("Dealer Order", String.valueOf(data_Unit.getDouble("ordersToTarget")));
				data_Crop.put("Gap To Goal", String.valueOf(data_Unit.getDouble("gapToTarget")));
				data_Crop.put("Dealer Order %", String.valueOf(data_Percent.getDouble("ordersToTarget"))+"%");
				data_Crop.put("Gap To Goal %", String.valueOf(data_Percent.getDouble("gapToTarget"))+"%");
				data_HybrisResponse.add(data_Crop);
			}
			if (data_HybrisResponse.size() > 0) {
				reusefunc.writeLMVResponseToExcel(prop.getProperty("DownloadPath"), sapAccountID+"-Progress To Business Plan.xlsx", "Progress To Business Plan", data_HybrisResponse, fieldNames);
				objLgr.info("Progress To Business Plan API hit Successfully for SAP ID - "+sapAccountID);
			}
			else
				objLgr.info("Goal data not present for SAP ID - "+sapAccountID);
		} catch (NullPointerException expnp) {
			objLgr.info("Goal data not present for SAP ID - "+sapAccountID+". Error - "+expnp);
		}
	}

	@Test (enabled=false)
	public void get_BulkSoybeanShipped() throws JSONException, Exception {
		List<Hashtable<String, String>> data_HybrisResponse = new ArrayList<Hashtable<String, String>>();
		String[] fieldNames = {"Dealer Order", "Shipped", "Scheduled", "To Ship", "Shipped %", "Scheduled %", "To Ship %"};
		JSONObject message = null, unit_Record = null, percent_Record = null;
		//Store the API Response in a JSON Object
		objLgr.info("Hitting Bulk Soybean Shipped API for - "+sapAccountID);
		message = jsonFunc.callAPI_JSONObject("GET","https://api01-np.agro.services/hybris-q-api/dealer/" + sapAccountID + "/bulkSoyBeanData",akanaAccessToken.getString("token_type") + " " + akanaAccessToken.getString("access_token"));
		reusefunc.writeToTextFile(prop.getProperty("DownloadPath")+sapAccountID+"-Bulk Soybean Shipped API Response.txt", message.toString());
		Hashtable<String, String> data_Crop = new Hashtable<String, String>();
		try {
			unit_Record = message.getJSONObject("aggregated").getJSONObject("byUnit");
			percent_Record = message.getJSONObject("aggregated").getJSONObject("byPercentage");
			data_Crop.put("Dealer Order", String.valueOf(unit_Record.getLong("demand")));
			data_Crop.put("Shipped", String.valueOf(unit_Record.getLong("shipped")));
			data_Crop.put("Scheduled", String.valueOf(unit_Record.getLong("scheduled")));
			data_Crop.put("To Ship", String.valueOf(unit_Record.getLong("leftToShip")));
			data_Crop.put("Shipped %", String.valueOf(percent_Record.getLong("shipped"))+"%");
			data_Crop.put("Scheduled %", String.valueOf(percent_Record.getLong("scheduled"))+"%");
			data_Crop.put("To Ship %", String.valueOf(percent_Record.getLong("leftToShip"))+"%");
			data_HybrisResponse.add(data_Crop);
			if (data_HybrisResponse.size() > 0) {
				reusefunc.writeLMVResponseToExcel(prop.getProperty("DownloadPath"), sapAccountID+"-Bulk Soybean Shipped.xlsx", "Bulk Soybean Shipped", data_HybrisResponse, fieldNames);
				objLgr.info("Bulk Soybean Shipped API hit Successfully for SAP ID - "+sapAccountID);
			}
			else
				objLgr.info("Bulk Soybean Shipped data not present for SAP ID - "+sapAccountID);
		} catch (NullPointerException expnp) {
			objLgr.info("Bulk Soybean Shipped data not present for SAP ID - "+sapAccountID+". Error - "+expnp);
		}
	}

	@Test (enabled=false)
	public void get_DealerLongShort() throws JSONException, Exception {
		List<Hashtable<String, String>> data_HybrisResponse = new ArrayList<Hashtable<String, String>>();
		String[] fieldNames = {"Crop", "Total", "Even", "Long", "Short"};
		JSONObject message = null;
		JSONArray unit_Record = null;
		//Store the API Response in a JSON Object
		objLgr.info("Hitting Long Short API for - "+sapAccountID);
		message = jsonFunc.callAPI_JSONObject("GET","https://api01-np.agro.services/hybris-q-api/order/dealer/" + sapAccountID + "/longandshort",akanaAccessToken.getString("token_type") + " " + akanaAccessToken.getString("access_token"));
		reusefunc.writeToTextFile(prop.getProperty("DownloadPath")+sapAccountID+"-Dealer Order Long Short API Response.txt", message.toString());
		try {
			unit_Record = message.getJSONObject("orderLongAndShortSummary").getJSONArray("aggregated");
			for (int j = 0; j < unit_Record.length(); j++) {
				Hashtable<String, String> data_Crop = new Hashtable<String, String>();
				data_Crop.put("Crop", unit_Record.getJSONObject(j).getString("key").replace("ALLCROPS", "Grand Total"));
				JSONObject data_Unit = unit_Record.getJSONObject(j).getJSONObject("value");
				data_Crop.put("Total", String.valueOf(data_Unit.getLong("orderEven")+data_Unit.getLong("orderLong")+data_Unit.getLong("orderShort")));
				data_Crop.put("Even", String.valueOf(data_Unit.getLong("orderEven")));
				data_Crop.put("Long", String.valueOf(data_Unit.getLong("orderLong")));
				data_Crop.put("Short", String.valueOf(data_Unit.getLong("orderShort")));
				data_HybrisResponse.add(data_Crop);
			}
			if (data_HybrisResponse.size() > 0) {
				reusefunc.writeLMVResponseToExcel(prop.getProperty("DownloadPath"), sapAccountID+"-Dealer Order Long Short.xlsx", "DealerOrderLongShort", data_HybrisResponse, fieldNames);
				objLgr.info("Dealer Order Long Short API hit Successfully for SAP ID - "+sapAccountID);
			}
			else
				objLgr.info("Dealer Long Short data not present for SAP ID - "+sapAccountID);
		} catch (NullPointerException expnp) {
			objLgr.info("Long Short data not present for SAP ID - "+sapAccountID+". Error - "+expnp);
		}
	}

	@Test (enabled=false)
	public void get_ScheduledToShip() throws JSONException, Exception {
		List<Hashtable<String, String>> data_HybrisResponse = new ArrayList<Hashtable<String, String>>();
		String[] fieldNames = {"Crop", "Hybrid", "Scheduled"};
		JSONObject message = null;
		JSONArray unit_Record = null;
		//Store the API Response in a JSON Object
		objLgr.info("Hitting Scheduled To Ship API for - "+sapAccountID);
		message = jsonFunc.callAPI_JSONObject("GET","https://api01-np.agro.services/hybris-q-api/product/inventory/widget/" + sapAccountID + "/scheduledToBeShipped",akanaAccessToken.getString("token_type") + " " + akanaAccessToken.getString("access_token"));
		reusefunc.writeToTextFile(prop.getProperty("DownloadPath")+sapAccountID+"-Scheduled API Response.txt", message.toString());
		try {
			unit_Record = message.getJSONObject("scheduledToBeShippedProducts").getJSONArray("aggregated");
			for (int j = 0; j < unit_Record.length(); j++) {
				Hashtable<String, String> data_Crop = new Hashtable<String, String>();
				data_Crop.put("Crop", unit_Record.getJSONObject(j).getString("key").replace("ALLCROPS", "Grand Total"));
				JSONObject data_Unit = unit_Record.getJSONObject(j).getJSONObject("value");
				data_Crop.put("Hybrid", String.valueOf(data_Unit.getLong("units")));
				data_Crop.put("Scheduled", String.valueOf(data_Unit.getLong("totalUnits")));
				data_HybrisResponse.add(data_Crop);
			}
			if (data_HybrisResponse.size() > 0) {
				reusefunc.writeLMVResponseToExcel(prop.getProperty("DownloadPath"), sapAccountID+"-Scheduled To Ship.xlsx", "Scheduled", data_HybrisResponse, fieldNames);
				objLgr.info("Inventory Scheduled To Ship API hit Successfully for SAP ID - "+sapAccountID);
			}
			else
				objLgr.info("Inventory Scheduled To Ship data not present for SAP ID - "+sapAccountID);
		} catch (NullPointerException expnp) {
			objLgr.info("Inventory Scheduled To Ship data not present for SAP ID - "+sapAccountID+". Error - "+expnp);
		}
	}

	@Test (enabled=false)
	public void get_RemainingToShip() throws JSONException, Exception {
		List<Hashtable<String, String>> data_HybrisResponse = new ArrayList<Hashtable<String, String>>();
		String[] fieldNames = {"Crop", "Hybrid", "To Ship"};
		JSONObject message = null;
		JSONArray unit_Record = null;
		//Store the API Response in a JSON Object
		objLgr.info("Hitting Remaining To Ship API for - "+sapAccountID);
		message = jsonFunc.callAPI_JSONObject("GET","https://api01-np.agro.services/hybris-q-api/product/inventory/widget/" + sapAccountID + "/remainingToBeShipped",akanaAccessToken.getString("token_type") + " " + akanaAccessToken.getString("access_token"));
		reusefunc.writeToTextFile(prop.getProperty("DownloadPath")+sapAccountID+"-Remaining API Response.txt", message.toString());
		try {
			unit_Record = message.getJSONObject("remainingToBeShippedProducts").getJSONArray("aggregated");
			for (int j = 0; j < unit_Record.length(); j++) {
				Hashtable<String, String> data_Crop = new Hashtable<String, String>();
				data_Crop.put("Crop", unit_Record.getJSONObject(j).getString("key").replace("ALLCROPS", "Grand Total"));
				JSONObject data_Unit = unit_Record.getJSONObject(j).getJSONObject("value");
				data_Crop.put("Hybrid", String.valueOf(data_Unit.getLong("units")));
				data_Crop.put("To Ship", String.valueOf(data_Unit.getLong("totalUnits")));
				data_HybrisResponse.add(data_Crop);
			}
			if (data_HybrisResponse.size() > 0) {
				reusefunc.writeLMVResponseToExcel(prop.getProperty("DownloadPath"), sapAccountID+"-Remaining To Ship.xlsx", "To Ship", data_HybrisResponse, fieldNames);
				objLgr.info("Inventory Remaining To Ship API hit Successfully for SAP ID - "+sapAccountID);
			}
			else
				objLgr.info("Inventory Remaining To Ship data not present for SAP ID - "+sapAccountID);
		} catch (NullPointerException expnp) {
			objLgr.info("Inventory Remaining To Ship data not present for SAP ID - "+sapAccountID+". Error - "+expnp);
		}
	}

	@Test (enabled=false)
	public void get_InventoryLongShort() throws JSONException, Exception {
		List<Hashtable<String, String>> data_HybrisResponse = new ArrayList<Hashtable<String, String>>();
		String[] fieldNames = {"Crop", "Total", "Even", "Long", "Short"};
		JSONObject message = null;
		JSONArray unit_Record = null;
		//Store the API Response in a JSON Object
		objLgr.info("Hitting Inventory Long Short API for - "+sapAccountID);
		message = jsonFunc.callAPI_JSONObject("GET","https://api01-np.agro.services/myaccount-service/inventory?sapAccountId=" + sapAccountID + "&startDate=2018-09-01&endDate=2019-08-31&fiscalYear=2019",akanaAccessToken.getString("token_type") + " " + akanaAccessToken.getString("access_token"));
		reusefunc.writeToTextFile(prop.getProperty("DownloadPath")+sapAccountID+"-Inventory Long Short API Response.txt", message.toString());
		try {
			unit_Record = message.getJSONArray("aggregated");
			for (int j = 0; j < unit_Record.length(); j++) {
				Hashtable<String, String> data_Crop = new Hashtable<String, String>();
				JSONObject data_Unit = unit_Record.getJSONObject(j);
				data_Crop.put("Crop", data_Unit.getString("cropName").replace("ALLCROPS", "Grand Total"));
				data_Crop.put("Total", String.valueOf(data_Unit.getLong("even")+data_Unit.getLong("long")+data_Unit.getLong("short")));
				data_Crop.put("Even", String.valueOf(data_Unit.getLong("even")));
				data_Crop.put("Long", String.valueOf(data_Unit.getLong("long")));
				data_Crop.put("Short", String.valueOf(data_Unit.getLong("short")));
				data_HybrisResponse.add(data_Crop);
			}
			if (data_HybrisResponse.size() > 0) {
				reusefunc.writeLMVResponseToExcel(prop.getProperty("DownloadPath"), sapAccountID+"-Inventory Long Short.xlsx", "InventoryLongShort", data_HybrisResponse, fieldNames);
				objLgr.info("Inventory Long Short API hit Successfully for SAP ID - "+sapAccountID);
			}
			else
				objLgr.info("Inventory Long Short data not present for SAP ID - "+sapAccountID);
		} catch (NullPointerException expnp) {
			objLgr.info("Inventory Long Short data not present for SAP ID - "+sapAccountID+". Error - "+expnp);
		}
	}

	@Test (enabled=false)
	public void get_SellableInventoryChannel() throws JSONException, Exception {
		List<Hashtable<String, String>> data_HybrisResponse = new ArrayList<Hashtable<String, String>>();
		String[] fieldNames = {"Hybrid", "Product ID", "Product Description", "Crop", "Confirmed Stock Order", "Long", "Plant", "Seed Size", "Lot Number", "Storage Location", "On Hand Deliverable"};
		JSONArray message = null;
		//Store the API Response in a JSON Object
		objLgr.info("Hitting Sellable Inventory API for Channel Seedsman - "+sapAccountID);
		message = jsonFunc.callAPI_JSONArray("GET","https://api01-np.agro.services:443/datainsights/v1/channel/inventory?fiscalYear=2019&priorYear=0&seedsmanId=" + sapAccountID,akanaAccessToken.getString("token_type") + " " + akanaAccessToken.getString("access_token"));
		reusefunc.writeToTextFile(prop.getProperty("DownloadPath")+sapAccountID+"-Sellable Inventory API Response.txt", message.toString());
		try {
			for (int j = 0; j < message.length(); j++) {
				JSONObject data_Unit = message.getJSONObject(j);
				try {
					JSONArray arr_Details = data_Unit.getJSONArray("details");
					for (int k = 0; k < arr_Details.length(); k++) {
						Hashtable<String, String> data_Product = new Hashtable<String, String>();
						JSONObject data_Details = arr_Details.getJSONObject(k);
						data_Product.put("Hybrid", data_Unit.getString("acronym"));
						data_Product.put("Product ID", data_Unit.getString("materialNumber"));
						data_Product.put("Product Description", data_Unit.getString("productDescription"));
						data_Product.put("Crop", data_Unit.getString("crop").replace("ALLCROPS", "Grand Total"));
						data_Product.put("Confirmed Stock Order", String.valueOf(data_Unit.getLong("confirmedStockOrderQty")));
						try {
							data_Product.put("Long", String.valueOf(data_Unit.getLong("longQuantity")));
						} catch (JSONException expJSON){
							data_Product.put("Long", "null");
						}
						data_Product.put("Plant", data_Details.getString("plantNumber"));
						try {
							data_Product.put("Seed Size", data_Details.getString("seedSizeCode"));
						} catch (JSONException expJSON){
							data_Product.put("Seed Size", "null");
						}
						data_Product.put("Lot Number", data_Details.getString("lotNumber"));
						data_Product.put("Storage Location", data_Details.getString("storageLocation"));
						try {
							data_Product.put("On Hand Deliverable", String.valueOf(data_Details.getLong("onHandDeliverableQty")));
						} catch (JSONException expJSON){
							data_Product.put("Seed Size", "null");
						}
						data_HybrisResponse.add(data_Product);
					}
				} catch (JSONException expJSONData) {
					Hashtable<String, String> data_Product = new Hashtable<String, String>();
					data_Product.put("Hybrid", data_Unit.getString("acronym"));
					data_Product.put("Product ID", data_Unit.getString("materialNumber"));
					data_Product.put("Product Description", data_Unit.getString("productDescription"));
					data_Product.put("Crop", data_Unit.getString("crop").replace("ALLCROPS", "Grand Total"));
					try {
						data_Product.put("Confirmed Stock Order", String.valueOf(data_Unit.getLong("confirmedStockOrderQty")));
					} catch (JSONException expJSON){
						data_Product.put("Long", "null");
					}
					try {
						data_Product.put("Long", String.valueOf(data_Unit.getLong("longQuantity")));
					} catch (JSONException expJSON){
						data_Product.put("Long", "null");
					}
					data_HybrisResponse.add(data_Product);
				}
			}
			if (data_HybrisResponse.size() > 0) {
				reusefunc.writeLMVResponseToExcel(prop.getProperty("DownloadPath"), sapAccountID+"-Sellable Inventory.xlsx", "Sellable", data_HybrisResponse, fieldNames);
				objLgr.info("Sellable Inventory API hit Successfully for SAP ID - "+sapAccountID);
			}
			else
				objLgr.info("Inventory Long Short data not present for SAP ID - "+sapAccountID);
		} catch (NullPointerException expnp) {
			objLgr.info("Sellable Inventory data not present for SAP ID - "+sapAccountID+". Error - "+expnp);
		}
	}

	@Test (enabled=false)
	public void get_BioAgAPIResponse() throws JSONException, Exception {
		List<Hashtable<String, String>> data_HybrisResponse = new ArrayList<Hashtable<String, String>>();
		String[] fieldNames = {"LOB", "SoldToSAPID","AccountType", "SoldToName", "ShipToSAPID", "ShipToName", "ShipToCity", "BillToSAPID", "BillToName", "PONumber", "PODate", "SONumber", "MaterialNumber", "ProdDesc", "OrderedQty", "BaseUOM", "SalesUOM", "TreatmentType", "Crop", "UnitsTreated", "ShipStatus"};
		JSONObject message = null;
		JSONArray unit_Record = null;
		//Store the API Response in a JSON Object
		objLgr.info("Hitting BioAg API for User - "+sapAccountID);
		String APIbody = "{\n"+"\"accountId\": "+"\""+sapAccountID+"\",\r\n" +
				"\"accountType\": null,\r\n" +
				"\"myLocation\": \"1\",\r\n" +
				"\"cache\": false,\r\n" +
				"\"cacheKey\": null,\r\n" +
				"\"debug\": false,\r\n" +
				"\"lineOfBusiness\": \"BIOAG\",\r\n" +
				"\"limit\": \"100000\",\r\n" +
				"\"offset\": \"0\",\r\n" +
				"\"purchaseOrderNumber\": null,\r\n" +
				"\"materialNumber\": null"+ "\n}"; 
		message = jsonFunc.callAPI_JSONObject("POST","https://api01-np.agro.services:443/datainsights/v1/sas-insights-api/sasOrderDetail",akanaAccessToken.getString("token_type") + " " + akanaAccessToken.getString("access_token"),APIbody);
		reusefunc.writeToTextFile(prop.getProperty("DownloadPath")+sapAccountID+"-BioAg API Response.txt", message.toString());
		try {
			unit_Record = message.getJSONArray("sasorderDetailResponseEntityList");
			for (int j = 0; j < unit_Record.length(); j++) {
				JSONObject data_Unit = unit_Record.getJSONObject(j);
				JSONArray arr_Details = data_Unit.getJSONArray("sasOrderDetailResponseLineItemList");
				//int array_length=arr_Details.length();
				//System.out.println(array_length);
				for (int k = 0; k < arr_Details.length(); k++) {
					Hashtable<String, String> data_Product = new Hashtable<String, String>();
					JSONObject data_Details = arr_Details.getJSONObject(k);
					data_Product.put("LOB", data_Unit.getString("lineOfBusiness"));
					data_Product.put("SoldToSAPID", data_Unit.getString("soldToSAPID"));
					data_Product.put("AccountType", data_Unit.getString("soldToAccountType"));
					data_Product.put("SoldToName", data_Unit.getString("soldToName"));
					data_Product.put("ShipToSAPID", data_Unit.getString("shipToSAPID"));
					data_Product.put("ShipToName", data_Unit.getString("shipToName"));
					data_Product.put("ShipToCity", data_Unit.getString("shipToCity"));
					data_Product.put("BillToSAPID", data_Unit.getString("billToSAPID"));
					data_Product.put("BillToName", data_Unit.getString("billToName"));
					try{
						data_Product.put("PONumber", data_Unit.getString("purchaseOrderNumber"));
					} catch (JSONException expJSON){
						data_Product.put("PONumber", "null");
					}

					try{
						data_Product.put("PODate", data_Unit.getString("purchaseOrderDate"));
					} catch (JSONException expJSON){
						data_Product.put("PODate", "null");
					}
					data_Product.put("SONumber", data_Unit.getString("salesOrderNumber"));
					data_Product.put("MaterialNumber", data_Details.getString("materialNumber"));
					data_Product.put("ProdDesc", data_Details.getString("productDescription"));

					try{
						data_Product.put("OrderedQty", String.valueOf(data_Details.getDouble("orderedQuantity")));
					} catch (JSONException expJSON){
						data_Product.put("OrderedQty", "null");
					}

					data_Product.put("BaseUOM", data_Details.getString("baseUnitOfMeasure"));
					data_Product.put("SalesUOM", data_Details.getString("salesUnitOfMeasure"));
					data_Product.put("TreatmentType", data_Details.getString("treatmentType"));
					data_Product.put("Crop", data_Details.getString("crop"));
					data_Product.put("UnitsTreated", data_Details.getString("unitsTreated"));
					data_Product.put("ShipStatus", data_Details.getString("shipStatus"));
					data_HybrisResponse.add(data_Product);
				}
				//data_HybrisResponse.add(data_Product);
			} 

			if (data_HybrisResponse.size() > 0) {
				reusefunc.writeLMVResponseToExcel(prop.getProperty("DownloadPath"), sapAccountID+"-BioAg response.xlsx", "SAS", data_HybrisResponse, fieldNames);
				objLgr.info("BioAg API hit Successfully for SAP ID - "+sapAccountID);
			}
			else
				objLgr.info("BioAg Data not present for the User - "+sapAccountID);
		} catch (NullPointerException expnp) {
			objLgr.info("BioAg Response data not available for SAP ID - "+sapAccountID+". Error - "+expnp);
		}
	}

	@Test (enabled=false)
	public void get_AcceleronAPIResponse() throws JSONException, Exception {
		List<Hashtable<String, String>> data_HybrisResponse = new ArrayList<Hashtable<String, String>>();
		String[] fieldNames = {"LOB", "SoldToSAPID","AccountType", "SoldToName", "ShipToSAPID", "ShipToName", "ShipToCity", "BillToSAPID", "BillToName", "PONumber", "PODate", "SONumber", "MaterialNumber", "ProdDesc", "OrderedQty", "BaseUOM", "SalesUOM", "TreatmentType", "Crop", "UnitsTreated", "ShipStatus"};
		JSONObject message = null;
		JSONArray unit_Record = null;
		//Store the API Response in a JSON Object
		objLgr.info("Hitting Acceleron API for User - "+sapAccountID);
		String APIbody = "{\n"+"\"accountId\": "+"\""+sapAccountID+"\",\r\n" +
				"\"accountType\": null,\r\n" +
				"\"myLocation\": \"1\",\r\n" +
				"\"cache\": false,\r\n" +
				"\"cacheKey\": null,\r\n" +
				"\"debug\": false,\r\n" +
				"\"lineOfBusiness\": \"ACCELERON\",\r\n" +
				"\"limit\": \"100000\",\r\n" +
				"\"offset\": \"0\",\r\n" +
				"\"purchaseOrderNumber\": null,\r\n" +
				"\"materialNumber\": null"+ "\n}"; 
		message = jsonFunc.callAPI_JSONObject("POST","https://api01-np.agro.services:443/datainsights/v1/sas-insights-api/sasOrderDetail",akanaAccessToken.getString("token_type") + " " + akanaAccessToken.getString("access_token"),APIbody);
		reusefunc.writeToTextFile(prop.getProperty("DownloadPath")+sapAccountID+"-Acceleron API Response.txt", message.toString());
		try {
			unit_Record = message.getJSONArray("sasorderDetailResponseEntityList");
			for (int j = 0; j < unit_Record.length(); j++) {
				JSONObject data_Unit = unit_Record.getJSONObject(j);

				JSONArray arr_Details = data_Unit.getJSONArray("sasOrderDetailResponseLineItemList");
				for (int k = 0; k < arr_Details.length(); k++) {
					Hashtable<String, String> data_Product = new Hashtable<String, String>();
					JSONObject data_Details = arr_Details.getJSONObject(k);
					data_Product.put("LOB", data_Unit.getString("lineOfBusiness"));
					data_Product.put("SoldToSAPID", data_Unit.getString("soldToSAPID"));
					data_Product.put("AccountType", data_Unit.getString("soldToAccountType"));
					data_Product.put("SoldToName", data_Unit.getString("soldToName"));
					data_Product.put("ShipToSAPID", data_Unit.getString("shipToSAPID"));
					data_Product.put("ShipToName", data_Unit.getString("shipToName"));
					data_Product.put("ShipToCity", data_Unit.getString("shipToCity"));
					data_Product.put("BillToSAPID", data_Unit.getString("billToSAPID"));
					data_Product.put("BillToName", data_Unit.getString("billToName"));
					try{
						data_Product.put("PONumber", data_Unit.getString("purchaseOrderNumber"));
					} catch (JSONException expJSON){
						data_Product.put("PONumber", "null");
					}
					try{
						data_Product.put("PODate", data_Unit.getString("purchaseOrderDate"));
					} catch (JSONException expJSON){
						data_Product.put("PODate", "null");
					}
					data_Product.put("SONumber", data_Unit.getString("salesOrderNumber"));
					data_Product.put("MaterialNumber", data_Details.getString("materialNumber"));
					data_Product.put("ProdDesc", data_Details.getString("productDescription"));

					try{
						data_Product.put("OrderedQty", String.valueOf(data_Details.getDouble("orderedQuantity")));
					} catch (JSONException expJSON){
						data_Product.put("OrderedQty", "null");
					}

					data_Product.put("BaseUOM", data_Details.getString("baseUnitOfMeasure"));
					data_Product.put("SalesUOM", data_Details.getString("salesUnitOfMeasure"));
					data_Product.put("TreatmentType", data_Details.getString("treatmentType"));
					data_Product.put("Crop", data_Details.getString("crop"));
					data_Product.put("UnitsTreated", data_Details.getString("unitsTreated"));
					data_Product.put("ShipStatus", data_Details.getString("shipStatus"));
					data_HybrisResponse.add(data_Product);
				}
			}
			if (data_HybrisResponse.size() > 0) {
				reusefunc.writeLMVResponseToExcel(prop.getProperty("DownloadPath"), sapAccountID+"-Acceleron response.xlsx", "SAS", data_HybrisResponse, fieldNames);
				objLgr.info("Acceleron API hit Successfully for SAP ID - "+sapAccountID);
			}
			else
				objLgr.info("Acceleron Data not present for the User - "+sapAccountID);
		} catch (NullPointerException expnp) {
			objLgr.info("Acceleron Response data not available for SAP ID - "+sapAccountID+". Error - "+expnp);
		}
	}
	
	@Test (enabled=false)
	public void get_LMVAPIResponse() throws JSONException, Exception {
		List<Hashtable<String, String>> api_LMVResponse = new ArrayList<Hashtable<String, String>>();
		String[] fieldNames = {"soldToAccountNumber", "shipToCustomerNumber", "deliveryNumber", "shipmentNumber", "plannedShipmentStartTimestamp", "plannedDeliveryTimestamp", "carrierId", "materialNumber", "deliveryStatusText"};
		JSONObject message = null, deliveries = null, deliveryMaterials = null;
		//Store the API Response in a JSON Object
		message = jsonFunc.callAPI_JSONObject("GET","https://api01-np.agro.services:443/commercial/v1/sales-order-delivery-tracking?idNumber=" + sapAccountID + "&idType=shipToOrSoldTo&limit=10000",akanaAccessToken.getString("token_type") + " " + akanaAccessToken.getString("access_token"));
		//message = jsonFunc.callAPI_JSONObject("GET","https://api01-np.agro.services:443/commercial/v1/sales-order-delivery-tracking?idNumber=" + sapAccountID + "&idType=deliveryNumber&limit=10000",akanaAccessToken.getString("token_type") + " " + akanaAccessToken.getString("access_token"));
		reusefunc.writeToTextFile(prop.getProperty("DownloadPath")+sapAccountID+"-LMV API Response.txt", message.toString());
		//Loop through each 'Deliveries' JSON Array
		for (int i = 0; i < message.getJSONArray("deliveries").length(); i++) {
			deliveries = message.getJSONArray("deliveries").getJSONObject(i);
			//Loop through each 'DeliveryMaterials' JSON Array
			for (int j = 0; j < deliveries.getJSONArray("deliveryMaterials").length(); j++) {
				Hashtable<String, String> deliveryDetails = new Hashtable<String, String>();
				deliveryMaterials = deliveries.getJSONArray("deliveryMaterials").getJSONObject(j);
				//Store the Required fields in a HashMap
				for (int k = 0; k < fieldNames.length; k++) {
					String value = null;
					try {
						if (fieldNames[k].equals("materialNumber"))
							value = deliveryMaterials.getString(fieldNames[k]);
						else
							value = deliveries.getString(fieldNames[k]);
					} catch (JSONException jsonEXP) {
						value = "null";
					}
					
					deliveryDetails.put(fieldNames[k], value);
				}
				//Store the HasMap in a ArrayList
				api_LMVResponse.add(deliveryDetails);
			}
		}
		if (api_LMVResponse.size() > 0) {
			reusefunc.writeLMVResponseToExcel(prop.getProperty("DownloadPath"), parentSAPAccountID+"-LMV Report.xlsx", "LMV Report", api_LMVResponse, fieldNames);
			objLgr.info("API Hit Successfully for SAP ID - "+sapAccountID);
		}
		else
			objLgr.info("Data not present for SAP ID - "+sapAccountID);
	}
	
	@Test (enabled=false)
	public void get_ListofShipmentsChannelAPIResponse() throws JSONException, Exception {
		List<Hashtable<String, String>> api_LMVResponse = new ArrayList<Hashtable<String, String>>();
		String[] fieldNames = {"shipmentType", "growerName", "transferFromName", "sfdcDeliveryNumber", "deliveryNumber", "deliveryCreatedOnDate", "plannedDepartureDate", "plannedArrivalDate", "carrierName", "warehouseStorageLocation", "sfdcDeliveryStatus"};
		JSONObject message = null, deliveries = null;
		//Store the API Response in a JSON Object
		message = jsonFunc.callAPI_JSONObject("GET","https://api01-np.agro.services:443/shipment/v1/cb-list-of-shipments-by-delivery?fiscalYear=2019&seedsmanId=" + sapAccountID, akanaAccessToken.getString("token_type") + " " + akanaAccessToken.getString("access_token"));
		reusefunc.writeToTextFile(prop.getProperty("DownloadPath")+sapAccountID+"-LOS API Response.txt", message.toString());
		//Loop through each 'Deliveries' JSON Array
		for (int i = 0; i < message.getJSONArray("deliveryListOfShipmentsResponse").length(); i++) {
			deliveries = message.getJSONArray("deliveryListOfShipmentsResponse").getJSONObject(i);
			Hashtable<String, String> deliveryDetails = new Hashtable<String, String>();
			//Store the Required fields in a HashMap
			for (int k = 0; k < fieldNames.length; k++) {
				String value = null;
				try {
					value = deliveries.getString(fieldNames[k]);
				} catch (JSONException jsonEXP) {
					value = "null";
				}
				
				deliveryDetails.put(fieldNames[k], value);
			}
			//Store the HasMap in a ArrayList
			api_LMVResponse.add(deliveryDetails);
		}
		if (api_LMVResponse.size() > 0) {
			reusefunc.writeLMVResponseToExcel(prop.getProperty("DownloadPath"), parentSAPAccountID+"-LOS Report.xlsx", "LOS Report", api_LMVResponse, fieldNames);
			objLgr.info("API Hit Successfully for SAP ID - "+sapAccountID);
		}
		else
			objLgr.info("Data not present for SAP ID - "+sapAccountID);
	}
	
	@Test (enabled=false)
	public void get_ListofShipmentsNBAPIResponse() throws JSONException, Exception {
		List<Hashtable<String, String>> api_LMVResponse = new ArrayList<Hashtable<String, String>>();
		String[] fieldNames = {"deliveryLocationType", "shipToAccountName", "materialNumber", "materialSalesText", "deliveredSSUQuantity", "cropName", "shipmentNumber", "deliveryNumber", "deliveryCreatedOnDate", "plannedDepartureDate", "plannedArrivalDate", "carrierName", "lmvShipmentStatus"};
		JSONObject message = null, deliveries = null, deliveryMaterials = null;
		//Store the API Response in a JSON Object
		message = jsonFunc.callAPI_JSONObject("GET","https://api01-np.agro.services:443/shipment/v1/list-of-shipments?cropYear=2019&salesAreaDescription=DAD&limit=10000&accountIdList=" + sapAccountID, akanaAccessToken.getString("token_type") + " " + akanaAccessToken.getString("access_token"));
		reusefunc.writeToTextFile(prop.getProperty("DownloadPath")+sapAccountID+"-LOS NB API Response.txt", message.toString());
		//Loop through each 'Deliveries' JSON Array
		for (int i = 0; i < message.getJSONArray("shipmentHeaders").length(); i++) {
			deliveries = message.getJSONArray("shipmentHeaders").getJSONObject(i);
			//Loop through each 'DeliveryMaterials' JSON Array
			for (int j = 0; j < deliveries.getJSONArray("shipmentDetails").length(); j++) {
				Hashtable<String, String> deliveryDetails = new Hashtable<String, String>();
				deliveryMaterials = deliveries.getJSONArray("shipmentDetails").getJSONObject(j);
				//Store the Required fields in a HashMap
				for (int k = 0; k < fieldNames.length; k++) {
					String value = null;
					try {
						if (fieldNames[k].equals("cropName") || fieldNames[k].equals("materialNumber") || fieldNames[k].equals("materialSalesText"))
							value = deliveryMaterials.getString(fieldNames[k]);
						else if (fieldNames[k].equals("deliveredSSUQuantity"))
							value = String.valueOf(deliveryMaterials.getDouble(fieldNames[k]));
						else
							value = deliveries.getString(fieldNames[k]);
					} catch (JSONException jsonEXP) {
						value = "null";
					}
					
					deliveryDetails.put(fieldNames[k], value);
				}
				//Store the HasMap in a ArrayList
				api_LMVResponse.add(deliveryDetails);
			}
		}
		if (api_LMVResponse.size() > 0) {
			reusefunc.writeLMVResponseToExcel(prop.getProperty("DownloadPath"), parentSAPAccountID+"-LOS NB Report.xlsx", "LOS Report", api_LMVResponse, fieldNames);
			objLgr.info("API Hit Successfully for SAP ID - "+sapAccountID);
		}
		else
			objLgr.info("Data not present for SAP ID - "+sapAccountID);
	}
	
	@Test (enabled=false)
	public void get_OrderGallonsCPHybirsAPIResponse() throws JSONException, Exception {
		List<Hashtable<String, String>> api_HybrisResponse = new ArrayList<Hashtable<String, String>>();
		String[] fieldNames = {"Tier", "FIL", "FL1", "FL2"};
		JSONObject message = null, orders = null, orderDetails = null;
		//Store the Hybris API Response in a JSON Object
		message = jsonFunc.callAPI_JSONObject("GET","https://api01-np.agro.services/hybris-q-api/order/CP/orderInsights/gallonOrders?sapAccountId=" + sapAccountID + "&location=ALL", akanaAccessToken.getString("token_type") + " " + akanaAccessToken.getString("access_token"));
		reusefunc.writeToTextFile(prop.getProperty("DownloadPath")+sapAccountID+"-CP Orders (Gallons).txt", message.toString());
		
		//Loop through each 'Tier' in JSON Array
		for (int i = 0; i < message.getJSONArray("gallonorders").length(); i++) {
			Hashtable<String, String> orderGallons = new Hashtable<String, String>();
			orders = message.getJSONArray("gallonorders").getJSONObject(i);
			orderGallons.put("Tier", orders.getString("productTier"));
			for (int j = 0; j < orders.getJSONArray("orders").length(); j++) {
				orderDetails = orders.getJSONArray("orders").getJSONObject(j);
				orderGallons.put(orderDetails.getString("fillperiod"), String.valueOf(orderDetails.getDouble("orderval")));
			}
			api_HybrisResponse.add(orderGallons);
		}
		if (api_HybrisResponse.size() > 0) {
			reusefunc.writeLMVResponseToExcel(prop.getProperty("DownloadPath"), sapAccountID+"-Orders (Gallons).xlsx", "OrdersGallons", api_HybrisResponse, fieldNames);
			objLgr.info("API Hit Successfully for SAP ID - "+sapAccountID);
		}
		else
			objLgr.info("Data not present for SAP ID - "+sapAccountID);
	}
	
	@Test (enabled=false)
	public void get_FillPeriodStatusCPHybirsAPIResponse() throws JSONException, Exception {
		List<Hashtable<String, String>> api_HybrisResponse = new ArrayList<Hashtable<String, String>>();
		String[] fieldNames = {"Fill Period", "Tier", "Delivered", "In Transit", "Planned", "Open", "% Delivered", "% In Transit", "% Planned", "% Open"};
		JSONObject message = null, orders = null, fillorders = null, fillOrderPercentage = null, fillOrderUnit = null;
		//Store the Hybris API Response in a JSON Object
		message = jsonFunc.callAPI_JSONObject("GET","https://api01-np.agro.services/hybris-q-api/order/CP/orderInsights/fillPeriodStatus?sapAccountId=" + sapAccountID + "&location=ALL", akanaAccessToken.getString("token_type") + " " + akanaAccessToken.getString("access_token"));
		reusefunc.writeToTextFile(prop.getProperty("DownloadPath")+sapAccountID+"-Fill Period Status.txt", message.toString());
		
		//Loop through each 'Tier' in JSON Array
		for (int i = 0; i < message.getJSONArray("fpSummary").length(); i++) {
			orders = message.getJSONArray("fpSummary").getJSONObject(i);
			for (int j = 0; j < orders.getJSONArray("fillPeriods").length(); j++) {
				Hashtable<String, String> fillPeriodOrder = new Hashtable<String, String>();
				fillorders = orders.getJSONArray("fillPeriods").getJSONObject(j);
				fillPeriodOrder.put("Tier", orders.getString("productTier"));
				fillPeriodOrder.put("Fill Period", fillorders.getString("fpName"));
				fillOrderPercentage = fillorders.getJSONObject("status").getJSONObject("percentage");
				fillOrderUnit = fillorders.getJSONObject("status").getJSONObject("unit");
				
				fillPeriodOrder.put("Delivered", String.valueOf(fillOrderUnit.getDouble("delivered")));
				fillPeriodOrder.put("In Transit", String.valueOf(fillOrderUnit.getDouble("inTransit")));
				fillPeriodOrder.put("Planned", String.valueOf(fillOrderUnit.getDouble("planned")));
				fillPeriodOrder.put("Open", String.valueOf(fillOrderUnit.getDouble("open")));
				
				fillPeriodOrder.put("% Delivered", String.valueOf(fillOrderPercentage.getDouble("delivered")));
				fillPeriodOrder.put("% In Transit", String.valueOf(fillOrderPercentage.getDouble("inTransit")));
				fillPeriodOrder.put("% Planned", String.valueOf(fillOrderPercentage.getDouble("planned")));
				fillPeriodOrder.put("% Open", String.valueOf(fillOrderPercentage.getDouble("open")));
				api_HybrisResponse.add(fillPeriodOrder);
			}
		}
		if (api_HybrisResponse.size() > 0) {
			reusefunc.writeLMVResponseToExcel(prop.getProperty("DownloadPath"), sapAccountID+"-Fill Period Status.xlsx", "FPStatus", api_HybrisResponse, fieldNames);
			objLgr.info("API Hit Successfully for SAP ID - "+sapAccountID);
		}
		else
			objLgr.info("Data not present for SAP ID - "+sapAccountID);
	}
	
	@Test (enabled=false)
	public void get_ShippingStatusWholeSaleRetailCPHybirsAPIResponse() throws JSONException, Exception {
		List<Hashtable<String, String>> api_HybrisResponse = new ArrayList<Hashtable<String, String>>();
		String[] fieldNames = {"Tier", "Account Type", "Delivered", "In Transit", "Planned", "Open"};
		JSONObject message = null, tier = null, accountDetails = null;
		//Store the Hybris API Response in a JSON Object
		message = jsonFunc.callAPI_JSONObject("GET","https://api01-np.agro.services/hybris-q-api/order/CP/orderInsights/shippingStatus?sapAccountId=" + sapAccountID + "&location=ALL", akanaAccessToken.getString("token_type") + " " + akanaAccessToken.getString("access_token"));
		reusefunc.writeToTextFile(prop.getProperty("DownloadPath")+sapAccountID+"-Shipping Status (Wholesale-Retail).txt", message.toString());
		
		//Loop through each 'Tier' in JSON Array
		for (int i = 0; i < message.getJSONArray("productTiers").length(); i++) {
			tier = message.getJSONArray("productTiers").getJSONObject(i);
			for (int j = 0; j < tier.getJSONArray("accountDetails").length(); j++) {
				Hashtable<String, String> shippingStatus = new Hashtable<String, String>();
				accountDetails = tier.getJSONArray("accountDetails").getJSONObject(j);
				shippingStatus.put("Tier", tier.getString("productTier"));
				shippingStatus.put("Account Type", accountDetails.getString("accountType"));
				shippingStatus.put("Delivered", String.valueOf(accountDetails.getJSONObject("productTierDetails").getDouble("delivered")));
				shippingStatus.put("In Transit", String.valueOf(accountDetails.getJSONObject("productTierDetails").getDouble("inTransit")));
				shippingStatus.put("Planned", String.valueOf(accountDetails.getJSONObject("productTierDetails").getDouble("planned")));
				shippingStatus.put("Open", String.valueOf(accountDetails.getJSONObject("productTierDetails").getDouble("open")));
				api_HybrisResponse.add(shippingStatus);
			}
		}
		if (api_HybrisResponse.size() > 0) {
			reusefunc.writeLMVResponseToExcel(prop.getProperty("DownloadPath"), sapAccountID+"-Shipping Status Accounts.xlsx", "Wholesale-Retail", api_HybrisResponse, fieldNames);
			objLgr.info("API Hit Successfully for SAP ID - "+sapAccountID);
		}
		else
			objLgr.info("Data not present for SAP ID - "+sapAccountID);
	}
	
	@Test (enabled=true)
	public void get_ShippingStatusPackageTypeCPHybirsAPIResponse() throws JSONException, Exception {
		List<Hashtable<String, String>> api_HybrisResponse = new ArrayList<Hashtable<String, String>>();
		String[] fieldNames = {"Tier", "Package Type", "Delivered", "In Transit", "Planned", "Open"};
		JSONObject message = null, tier = null, accountDetails = null;
		//Store the Hybris API Response in a JSON Object
		message = jsonFunc.callAPI_JSONObject("GET","https://api01-np.agro.services/hybris-q-api/order/CP/orderInsights/packageTypeShippingStatus?sapAccountId=" + sapAccountID + "&location=ALL", akanaAccessToken.getString("token_type") + " " + akanaAccessToken.getString("access_token"));
		reusefunc.writeToTextFile(prop.getProperty("DownloadPath")+sapAccountID+"-Shipping Status Package Type.txt", message.toString());
		
		//Loop through each 'Tier' in JSON Array
		for (int i = 0; i < message.getJSONArray("productTiers").length(); i++) {
			tier = message.getJSONArray("productTiers").getJSONObject(i);
			for (int j = 0; j < tier.getJSONArray("accountDetails").length(); j++) {
				Hashtable<String, String> shippingStatus = new Hashtable<String, String>();
				accountDetails = tier.getJSONArray("accountDetails").getJSONObject(j);
				shippingStatus.put("Tier", tier.getString("productTier"));
				shippingStatus.put("Package Type", accountDetails.getString("packageType"));
				shippingStatus.put("Delivered", String.valueOf(accountDetails.getJSONObject("productTierDetails").getDouble("delivered")));
				shippingStatus.put("In Transit", String.valueOf(accountDetails.getJSONObject("productTierDetails").getDouble("inTransit")));
				shippingStatus.put("Planned", String.valueOf(accountDetails.getJSONObject("productTierDetails").getDouble("planned")));
				shippingStatus.put("Open", String.valueOf(accountDetails.getJSONObject("productTierDetails").getDouble("open")));
				api_HybrisResponse.add(shippingStatus);
			}
		}
		if (api_HybrisResponse.size() > 0) {
			reusefunc.writeLMVResponseToExcel(prop.getProperty("DownloadPath"), sapAccountID+"-Shipping Status Package.xlsx", "Package-Bulk", api_HybrisResponse, fieldNames);
			objLgr.info("API Hit Successfully for SAP ID - "+sapAccountID);
		}
		else
			objLgr.info("Data not present for SAP ID - "+sapAccountID);
	}
	
	@Test (enabled=true)
	public void get_InSeasonOrdersCPHybirsAPIResponse() throws JSONException, Exception {
		List<Hashtable<String, String>> api_HybrisResponse = new ArrayList<Hashtable<String, String>>();
		String[] fieldNames = {"Tier", "Delivered", "In Transit", "Planned", "Open"};
		JSONObject message = null, tier = null;
		//Store the Hybris API Response in a JSON Object
		message = jsonFunc.callAPI_JSONObject("GET","https://api01-np.agro.services/hybris-q-api/dealerorders/" + sapAccountID + "/cp/openorders?location=ALL", akanaAccessToken.getString("token_type") + " " + akanaAccessToken.getString("access_token"));
		reusefunc.writeToTextFile(prop.getProperty("DownloadPath")+sapAccountID+"-In Season Order.txt", message.toString());
		
		//Loop through each 'Tier' in JSON Array
		for (int i = 0; i < message.getJSONArray("productTier").length(); i++) {
			Hashtable<String, String> inSeasonOrders = new Hashtable<String, String>();
			tier = message.getJSONArray("productTier").getJSONObject(i);
			inSeasonOrders.put("Tier", tier.getString("productTier"));
			inSeasonOrders.put("Delivered", String.valueOf(tier.getJSONObject("productTierDetails").getDouble("delivered")));
			inSeasonOrders.put("In Transit", String.valueOf(tier.getJSONObject("productTierDetails").getDouble("inTransit")));
			inSeasonOrders.put("Planned", String.valueOf(tier.getJSONObject("productTierDetails").getDouble("planned")));
			inSeasonOrders.put("Open", String.valueOf(tier.getJSONObject("productTierDetails").getDouble("open")));
			api_HybrisResponse.add(inSeasonOrders);
		}
		if (api_HybrisResponse.size() > 0) {
			reusefunc.writeLMVResponseToExcel(prop.getProperty("DownloadPath"), sapAccountID+"-In Season Orders.xlsx", "In Season Orders", api_HybrisResponse, fieldNames);
			objLgr.info("API Hit Successfully for SAP ID - "+sapAccountID);
		}
		else
			objLgr.info("Data not present for SAP ID - "+sapAccountID);
	}
}