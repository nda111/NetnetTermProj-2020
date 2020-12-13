package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.time.format.DateTimeFormatter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class WeatherApi {
    public static void main(String[] args) throws IOException, ParseException {
    	
    	String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtNcst"; //apiUrl
    	
        String servicekey = "b3liWapCGfntqc68jXVFcdV8gqJ9p3dgbq6ovnjvSsppK6ZZtNpMQEo96IEZ6GSGy45LREk3NVHVe9VwXjQfDw%3D%3D"; 
        
    	/*동네예보조회 api는 2시,5시, 8시, 11시, 14시, 17시, 20시,23시   (3시간 단위 차이) 로 시간을 받아올 수 있기에 
         * 아래의 기준에 따라 받아온 서버 시간을 고정했습니다.*/
        
        //(i+1)//3*3-1)%24 공식 사용 
        /*2시 이상 5시미만까지:2시로 고정
         5시 이상 8시미만까지 :5시로 고정
         8시 이상 11시미만까지 :8시로 고정
         11시 이상 14시미만까지:11시로 고정
         14시이상 17시 미만까지: 14시로 고정
         17시이상 20시미만까지: 17시로 고정
         20시이상 23시미만까지 : 20시로 고정
         23시이상 00시 미만까지 : 23시로 고정
         00시이상 2시 미만까지 : 23시로 고정
         */
        String page = "1";//페이지번호
        String rows = "10";//한페이지 결과수
        String nx = "62";// 위도
        String ny = "124";// 경도 (위도 경도는 참고로 중원구 복정동 기준)
        String type = "JSON"; //데이터 타입
    	String baseDate = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()); //조회하고싶은 날짜(서버날짜로 받아오기)
    	String baseTime =  new SimpleDateFormat("HHmm").format(Calendar.getInstance().getTime());//조회하고싶은 날짜(서버시간으로 받아오기)
    	int baseTimeNum=Integer.parseInt(baseTime) / 100;// 받아온시간 (시,분 )-> 시 로만 나타내기 
    	//본래 시간이 00시나, 1시일경우 전날 23시로 설정해야 하므로 
    	
    	LocalDate now=LocalDate.now();
    	if((baseTimeNum==0)||(baseTimeNum==1)) {
    		 LocalDate oneDayAgo = now.minusDays(1);
    		 SimpleDateFormat date= new SimpleDateFormat("yyyyMMdd");
    		 baseDate=date.format(oneDayAgo);
    		 
    	}
    		int RevisedTimeNum=((baseTimeNum + 1) / 3 * 3 -1) % 24;
    		String RevisedTime=Integer.toString(RevisedTimeNum)+"00";
    		System.out.println(RevisedTime);

    
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + servicekey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode(page, "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode(rows, "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode(type, "UTF-8")); /*요청자료형식(XML/JSON)Default: XML*/
        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8")); /*15년 12월 1일발표*/
        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(RevisedTime, "UTF-8")); /*05시 발표*/
        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); /*예보지점 X 좌표값*/
        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); /*예보지점의 Y 좌표값*/
        URL url = new URL(urlBuilder.toString());
        System.out.println(url); //url 출력
        HttpURLConnection conn = (HttpURLConnection) url.openConnection(); //HttpUrlConnection 클래스 생성
        conn.setRequestMethod("GET");//요청방식 설정 (GET)
        conn.setRequestProperty("Content-type", "application/json");//헤더의 메소드 정의

        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
      //프로토콜 반환 코드가 200이상 300이하인 경우 스트림으로 반환 결과값 받기
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        }
      //그 이외인 경우 에러 발생
        else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder(); //문자열을 담기 위한 객체
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();//접속해제

        
        String stringJson = sb.toString(); //stringbuilder-> string으로 변환하기
        System.out.println(stringJson);

        // string -> jsonobject 로
        JSONParser jsonParser = new JSONParser();// json 객체 만들기, parser통해 파싱하기
        JSONObject jsonObj = (JSONObject) jsonParser.parse(stringJson);// parser로 문자열 데이터를 json 데이터로 변환


        JSONObject parse_response = (JSONObject) jsonObj.get("response"); // response key값에 맞는 Value인 JSON객체를 가져오기
       
        JSONObject parse_body = (JSONObject) parse_response.get("body"); // response 로 부터 body 찾아오기

        JSONObject parse_items = (JSONObject) parse_body.get("items");  // body 로 부터 items 받아오기
     
        JSONArray parse_itemlist = (JSONArray) parse_items.get("item");  // items로 부터 itemlist 를 받아오기  itemlist : 뒤에 [ 로 시작하므로 jsonarray

        System.out.println(parse_itemlist);
        
        String[]skySunny = {"0","1","2","3","4","5"};
        String[]skyCloudy= {"6","7","8"};
        String[]skyBad= {"9","10"};
       

 
        System.out.println("* 날씨 *");

        for (int i = 0; i < parse_itemlist.size(); i++) {

           // 배열 안에 있는것도 JSON형식 이기 때문에 JSON Object 로 추출
           JSONObject weatherObject = (JSONObject) parse_itemlist.get(i);

           String skyKind  = String.valueOf(weatherObject.get("category"));
           String skyValue = String.valueOf(weatherObject.get("fcstValue"));
          String skyState="";
           //동네예보 항목 값에 'SKY' 가 있을 경우 
           if(skyKind.contains("SKY")) {
        	   if(Arrays.asList(skySunny).contains(skyValue)) {
        		   skyState="맑음";
        	   }
        	   else if(Arrays.asList(skyCloudy).contains(skyValue)) {
        		   skyState="구름많음";
        	   }
        	   else if (Arrays.asList(skyBad).contains(skyValue)) {
        		   skyState="흐림";
        	   }
        		  
          
           // JSON name으로 추출
           System.out.println("성남시 중원구 복정동 기준");
           System.out.println("오늘의 날짜는 " + weatherObject.get("baseDate")+" ,");
           System.out.println("측정 기준 시간은 " + weatherObject.get("baseTime")+"시 이며");
           System.out.println("현재 하늘 상태는 "+ skyState +" 입니다");
//          
           
           }
        }
    }
}

