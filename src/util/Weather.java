package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Weather {

	private static final String Servicekey = "b3liWapCGfntqc68jXVFcdV8gqJ9p3dgbq6ovnjvSsppK6ZZtNpMQEo96IEZ6GSGy45LREk3NVHVe9VwXjQfDw%3D%3D";

	private String nx = "62";// 위도
	private String ny = "124";// 경도 (위도 경도는 참고로 중원구 복정동 기준)
	private String dataType = "JSON"; // 데이터 타입
	private String baseTime = null;
	private String baseDate = null;
	private String revisedTime = null;

	public Weather(int nx, int ny, String dataType, long timeInMillis) {

		this.nx = Integer.toString(nx);
		this.ny = Integer.toString(ny);
		this.dataType = dataType;

		baseDate = new SimpleDateFormat("yyyyMMdd").format(timeInMillis); // 조회하고싶은 날짜(서버날짜로 받아오기)
		baseTime = new SimpleDateFormat("HHmm").format(timeInMillis);// 조회하고싶은 날짜(서버시간으로 받아오기)
		int baseTimeNum = Integer.parseInt(baseTime) / 100;// 받아온시간 (시,분 )-> 시 로만 나타내기

		LocalDate now = LocalDate.now();
		if ((baseTimeNum == 0) || (baseTimeNum == 1)) {

			LocalDate oneDayAgo = now.minusDays(1);
			SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
			baseDate = date.format(oneDayAgo);
		}

		int revisedTimeNum = ((baseTimeNum + 1) / 3 * 3 - 1) % 24;
		this.revisedTime = Integer.toString(revisedTimeNum) + "00";
		if (this.revisedTime.length() == 3) {

			this.revisedTime = '0' + this.revisedTime;
		}
	}

	public JSONArray getData(int page, int rows) {

		StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst"); /* URL */
		try {

			urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + Servicekey); /* Service Key */
			urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(Integer.toString(page), "UTF-8")); /* 페이지번호 */
			urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(Integer.toString(rows), "UTF-8")); /* 한 페이지 결과 수 */
			urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode(dataType, "UTF-8")); /* 요청자료형식 */
			urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8")); /* 15년 12월 1일발표 */
			urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(revisedTime, "UTF-8")); /* 05시 발표 */
			urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); /* 예보지점 X 좌표값 */
			urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); /* 예보지점의 Y 좌표값 */
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();

			return null;
		}

		try {
			
			URL url = new URL(urlBuilder.toString());

			HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // HttpUrlConnection 클래스 생성
			conn.setRequestMethod("GET"); // 요청방식 설정 (GET)
			conn.setRequestProperty("Content-type", "application/json");// 헤더의 메소드 정의

			System.out.println("Response code: " + conn.getResponseCode());
			BufferedReader rd;
			// 프로토콜 반환 코드가 200이상 300이하인 경우 스트림으로 반환 결과값 받기
			if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
				
				rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			}
			// 그 이외인 경우 에러 발생
			else {
				
				rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
				
				return null;
			}
			StringBuilder sb = new StringBuilder(); // 문자열을 담기 위한 객체
			String line;
			while ((line = rd.readLine()) != null) {
				
				sb.append(line);
			}
			rd.close();
			conn.disconnect();// 접속해제

			String stringJson = sb.toString(); // stringbuilder-> string으로 변환하기
			System.out.println(stringJson);

			// string -> jsonobject 로
			JSONParser jsonParser = new JSONParser();// json 객체 만들기, parser통해 파싱하기
			JSONObject jsonObj = (JSONObject) jsonParser.parse(stringJson);// parser로 문자열 데이터를 json 데이터로 변환
			JSONObject parse_response = (JSONObject) jsonObj.get("response"); // response key값에 맞는 Value인 JSON객체를 가져오기
			JSONObject parse_body = (JSONObject) parse_response.get("body"); // response 로 부터 body 찾아오기
			JSONObject parse_items = (JSONObject) parse_body.get("items"); // body 로 부터 items 받아오기
			JSONArray parse_itemlist = (JSONArray) parse_items.get("item"); // items로 부터 itemlist 를 받아오기 itemlist : 뒤에 [ 로
																			// 시작하므로 jsonarray		
			System.out.println(parse_itemlist);
			
			return parse_itemlist;
		} catch (IOException | ParseException e) {

			e.printStackTrace();
			
			return null;
		}
	}
	
	public String getDataAsString(int page, int rows) {
		
		JSONArray parse_itemlist = this.getData(page, rows);
		
		String[] skySunny = { "0", "1", "2", "3", "4", "5" };
		String[] skyCloudy = { "6", "7", "8" };
		String[] skyBad = { "9", "10" };

		// Build and return a string to display on the window instead of null.
		
		return null;
	}
}
