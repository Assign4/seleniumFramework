package FunctionLibrary;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class JSONFunctions {

	private static Logger objLgr = LogManager.getLogger(JSONFunctions.class.getName());
	public static final String PATTERN1 = "#,##0;(#,##0)"; 
	public List<Hashtable<String, String>> list_companyDataAPI = new ArrayList<Hashtable<String, String>>();
	public List<Hashtable<String, String>> list_myGrowerAPI = new ArrayList<Hashtable<String, String>>();


	public JSONObject getToken_v2(String requestMethod, String apiURL, String client_id, String client_secret, String grant_type) throws Exception {
		//objLgr.info("getToken invokved with requestMethod " + requestMethod + " URL " + apiURL + " clientID " + client_id + "client secret " + client_secret + " and grant type " + grant_type);
		String USER_AGENT = "Mozilla/5.0";
		URL obj = new URL(apiURL);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		List<NameValuePair> form = new ArrayList<>();
		form.add(new BasicNameValuePair("client_id", client_id));
		form.add(new BasicNameValuePair("client_secret", client_secret));
		form.add(new BasicNameValuePair("grant_type", grant_type));        

		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form, Consts.UTF_8);

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty("Content-type","application/x-www-form-urlencoded;charset=utf-8");
		con.setDoOutput(true);
		con.connect();
		OutputStream os = con.getOutputStream();
		entity.writeTo(os);
		os.flush();

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		//int responseCode = con.getResponseCode();
		//objLgr.info("Response Code : " + responseCode);		
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		//objLgr.info(response.toString());
		return new JSONObject(response.toString());
		//		return jsonObject.get("access_token").toString();
	}

	public JSONObject callAPI_JSONObject(String requestMethod, String apiURL, String authorization_header) throws Exception {
		//objLgr.info("Method callAPI invoked with requestMethod " + requestMethod + " URL " + apiURL);
		String USER_AGENT = "Mozilla/5.0";
		URL obj = null;
		JSONObject jsonObject = null;
		BufferedReader in = null;
		//		int responseCode;


		try{
			obj = new URL(apiURL);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

			//add request header
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setRequestProperty("Authorization",authorization_header);

			if(requestMethod == "POST"){
				con.setRequestMethod("POST");
			}
			long startTime = System.currentTimeMillis();
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			long endTime = System.currentTimeMillis();
			objLgr.info("Response time :" + (endTime-startTime));
			//			responseCode = con.getResponseCode();
			//objLgr.info("Response Code : " + responseCode);

			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			//print result
			//objLgr.info(response.toString());
			jsonObject = new JSONObject(response.toString());
		} catch(Exception e){
			objLgr.error("Exception Caught :" + e);
			for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
				System.out.println(ste);
			}
		}
		return jsonObject;		
	}

	public JSONArray callAPI_JSONArray(String requestMethod, String apiURL, String authorization_header) throws Exception {
		//objLgr.info("Method callAPI invoked with requestMethod " + requestMethod + " URL " + apiURL);
		String USER_AGENT = "Mozilla/5.0";
		URL obj = null;
		JSONArray jsonArray = null;
		BufferedReader in = null;
		//		int responseCode;


		try{
			obj = new URL(apiURL);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

			//add request header
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setRequestProperty("Authorization",authorization_header);

			if(requestMethod == "POST"){
				con.setRequestMethod("POST");
			}
			long startTime = System.currentTimeMillis();
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			long endTime = System.currentTimeMillis();
			objLgr.info("Response time :" + (endTime-startTime));
			//			responseCode = con.getResponseCode();
			//objLgr.info("Response Code : " + responseCode);

			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			//print result
			//objLgr.info(response.toString());
			jsonArray = new JSONArray(response.toString());
		} catch(Exception e){
			objLgr.error("Exception Caught :" + e);
			for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
				System.out.println(ste);
			}
		}
		return jsonArray;		
	}

	//Method to get Response Code and Response Time of each API called
	public Hashtable<String, String> getAPI_ResponseTime(String scenarioName, String requestMethod, String apiURL, String authorization_header) throws Exception {
		Hashtable<String, String> details_API = new Hashtable<String, String>();
		String USER_AGENT = "Mozilla/5.0";
		URL obj = null;
		BufferedReader in = null;

		try {
			obj = new URL(apiURL);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

			//add request header
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setRequestProperty("Authorization",authorization_header);

			if(requestMethod == "POST"){
				con.setRequestMethod("POST");
			}

			long startTime = System.currentTimeMillis();
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			long endTime = System.currentTimeMillis();

			//Storing the scenarion name to HashMap
			details_API.put("Scenario",scenarioName);
			//Storing the response code to HashMap
			details_API.put("ResponseCode", Integer.toString(con.getResponseCode()));

			//Storing the response time to HashMap
			details_API.put("ResponseTime", Long.toString(endTime-startTime));

			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		} catch(Exception e){
			objLgr.error("Exception Caught :" + e);
			for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
				System.out.println(ste);
			}
		}
		return details_API;		
	}

	public JSONObject callAPI_JSONObject(String requestMethod, String apiURL, String authorization_header, String APIbody) throws Exception {
		//objLgr.info("Method callAPI invoked with requestMethod " + requestMethod + " URL " + apiURL);
				String USER_AGENT = "Mozilla/5.0";
				URL obj = null;
				JSONObject jsonObject = null;
				BufferedReader in = null;
				//		int responseCode;


				try{
					obj = new URL(apiURL);
					HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

					//add request header
					con.setRequestProperty("User-Agent", USER_AGENT);
					con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
					con.setRequestProperty("Authorization",authorization_header);

					if(requestMethod == "POST"){
						con.setRequestMethod("POST");
						con.setRequestProperty("Content-Type", "application/json");
						con.setDoOutput(true);
					    OutputStream os = con.getOutputStream();
					    String POST_PARAMS = APIbody;
					    os.write(POST_PARAMS.getBytes());
					    os.flush();
					    os.close();
					}
					long startTime = System.currentTimeMillis();
					in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					long endTime = System.currentTimeMillis();
					objLgr.info("Response time :" + (endTime-startTime));
					//			responseCode = con.getResponseCode();
					//objLgr.info("Response Code : " + responseCode);

					String inputLine;
					StringBuffer response = new StringBuffer();

					while ((inputLine = in.readLine()) != null) {
						response.append(inputLine);
					}
					in.close();

					//print result
					//objLgr.info(response.toString());
					jsonObject = new JSONObject(response.toString());
				} catch(Exception e){
					objLgr.error("Exception Caught :" + e);
					for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
						System.out.println(ste);
					}
				}
				return jsonObject;	
		// TODO Auto-generated method stub
	}
}