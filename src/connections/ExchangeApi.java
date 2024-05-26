package connections;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ExchangeApi {
	
	public static JSONObject exchangeData(String currencyOne) {
		String url1 = "https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies/" + 
		currencyOne + ".json";
		
		try {
			
			HttpURLConnection conn = fetchApiResponse(url1);
			
			if(conn.getResponseCode() != 200) {
				System.out.println("Error: could not connect to api");
				return null;
			}else {
				StringBuilder jsonBuilder = new StringBuilder();
				Scanner scanner = new Scanner(conn.getInputStream());
				
				while(scanner.hasNext()) {
					jsonBuilder.append(scanner.nextLine());
				}
				
				scanner.close();
				
				conn.disconnect();
				
				JSONParser jsonParser = new JSONParser();
				JSONObject result = (JSONObject) jsonParser.parse(String.valueOf(jsonBuilder));
				JSONObject coins = (JSONObject) result.get(currencyOne);
				return coins;
			}
	
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static HttpURLConnection fetchApiResponse(String urlString) {
		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			//set request method to get
			conn.setRequestMethod("GET");
			
			//connect to our API
			conn.connect();
			return conn;
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		//could not make connection
		return null;
	}

}
