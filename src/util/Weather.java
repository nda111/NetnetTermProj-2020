package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Weather {

	private static final String Servicekey = "b3liWapCGfntqc68jXVFcdV8gqJ9p3dgbq6ovnjvSsppK6ZZtNpMQEo96IEZ6GSGy45LREk3NVHVe9VwXjQfDw%3D%3D";

	private String nx = "62";//latitude
	private String ny = "124";// longtitude (Based on Joongwon gu- Bokjeong dong)
	private String dataType = "JSON"; // data type
	private String baseDate = null;
	private String baseTime = null;

	/*VilageFcstInfoService-getUltraSrtNcst  api can only detect time 2:00,5:00, 8:00, 11:00, 14:00, 17:00, 20:00,23:00  (by 3 hour term) 
     * Therefore, server time is fixed as below.*/
    
    /*2:00 >= and <5:00: fixed into 2:00
     5:00 >= and <8:00: fixed into 5:00
     8:00 >= and <11:00: fixed into 8:00
     11:00 >= and <14:00: fixed into 11:00
     14:00 >= and <17:00: fixed into 14:00
     17:00 >= and <20:00: fixed into 17:00
     20:00 >= and <23:00: fixed into 20:00
     23:00 >= and <2:00: fixed into 23:00
     */

	
	public Weather(int nx, int ny, String dataType, long timeInMillis) {

		this.nx = Integer.toString(nx);
		this.ny = Integer.toString(ny);
		this.dataType = dataType;

        LocalDateTime now = LocalDateTime.now();
        int hour = ((now.getHour() + 1) / 3 * 3 - 1) % 24;
        if (now.getHour() < 2) { //if < 2:00
        	
        	now = now.minusDays(1); // date should be one day before server day because it will be fixed in 23:00
        	hour = 23;
        }
        
        baseDate = String.format("%d%02d%02d", now.getYear(), now.getMonthValue(), now.getDayOfMonth());
        baseTime = String.format("%02d00", hour);
	}

	public JSONArray getData(int page, int rows) {

		StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst"); /* URL */
		try {

			urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + Servicekey); /* Service Key */
			urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(Integer.toString(page), "UTF-8")); /* page number */
			urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(Integer.toString(rows), "UTF-8")); /* result nums from one page */
			urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode(dataType, "UTF-8")); /* Request data format */
			urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8")); /* base date for getting weather information*/
			urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8")); /* basetime for getting weather information*/
			urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); /* X coordinate value of the forecast point */
			urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); /* Y coordinate value of the forecast point */
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();

			return null;
		}

		try {
			
			URL url = new URL(urlBuilder.toString());

			HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // HttpUrlConnection class 
			conn.setRequestMethod("GET"); // request way set(GET)
			conn.setRequestProperty("Content-type", "application/json");// header method 

			BufferedReader rd;
			// if protocol response code is 200>= and <=300 get return result by stream
			if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
				
				rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			}
			// when error occurs
			else {
				
				rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
				
				return null;
			}
			StringBuilder sb = new StringBuilder(); // object for getting string
			String line;
			while ((line = rd.readLine()) != null) {
				
				sb.append(line);
			}
			rd.close();
			conn.disconnect();// disconnection

			String stringJson = sb.toString(); // stringbuilder into  string

			// string -> jsonobject 
			JSONParser jsonParser = new JSONParser();// json object creation, parsing through parser
			JSONObject jsonObj = (JSONObject) jsonParser.parse(stringJson);// Convert string data to json data with parser
			JSONObject parse_response = (JSONObject) jsonObj.get("response"); // Import JSON objects whose value corresponds to the response key value
			JSONObject parse_body = (JSONObject) parse_response.get("body"); // Find body from response
			JSONObject parse_items = (JSONObject) parse_body.get("items"); // Get Items from body
			JSONArray parse_itemlist = (JSONArray) parse_items.get("item"); // get item list from items item list : jsonarray after []
			
			return parse_itemlist;
		} catch (IOException | ParseException e) {

			e.printStackTrace();
			
			return null;
		}
	}
	
	public String getDataAsString(int page, int rows) {

		JSONArray parse_itemlist = this.getData(page, rows);
		if (parse_itemlist == null) {
			
			return null;
		}
		
		for (int i = 0; i < parse_itemlist.size(); i++) {
			JSONObject weatherObject = (JSONObject) parse_itemlist.get(i);

			//Based on Weather Open data Api (동네예보조회 서비스)
			//category 'SKY' state is divided into three parts, sunny, cloudy, and bad
			String[] skySunny = { "0", "1", "2", "3", "4", "5" };
			String[] skyCloudy = { "6", "7", "8" };
			String[] skyBad = { "9", "10" };

			String skyKind = String.valueOf(weatherObject.get("category"));
			String skyValue = String.valueOf(weatherObject.get("fcstValue"));
			String baseDates = String.valueOf(weatherObject.get("baseDate"));
			String baseTimes = String.valueOf(weatherObject.get("baseTime"));

			String skyState = "";
			// If therer is 'SKY' in category
			if (skyKind.contains("SKY")) {
				
				if (Arrays.asList(skySunny).contains(skyValue)) {
					
					skyState = "맑음";
				} else if (Arrays.asList(skyCloudy).contains(skyValue)) {
					
					skyState = "구름많음";
				} else if (Arrays.asList(skyBad).contains(skyValue)) {
					
					skyState = "흐림";
				}

				StringBuilder resultBuilder = new StringBuilder("성남시 중원구 복정동 기준");
				resultBuilder.append("오늘의 날짜는 " + baseDates+",\n");
				resultBuilder.append("측정 기준 시간은 " + baseTimes+"시 이며\n");
				resultBuilder.append("현재 하늘 상태는 "+ skyState +" 입니다.");

				return resultBuilder.toString();
			}
		}
		
		return null;
	}
	
	public String getDataAsHtml(int page, int rows) {
		
		String result = getDataAsString(page, rows);
		
		if (result != null) {
			
			result = String.format("<html>%s</html>", result.replace("\n", "<br/>"));
		}
		
		return result;
	}
}