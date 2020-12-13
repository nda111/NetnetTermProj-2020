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
        
    	/*���׿�����ȸ api�� 2��,5��, 8��, 11��, 14��, 17��, 20��,23��   (3�ð� ���� ����) �� �ð��� �޾ƿ� �� �ֱ⿡ 
         * �Ʒ��� ���ؿ� ���� �޾ƿ� ���� �ð��� �����߽��ϴ�.*/
        
        //(i+1)//3*3-1)%24 ���� ��� 
        /*2�� �̻� 5�ù̸�����:2�÷� ����
         5�� �̻� 8�ù̸����� :5�÷� ����
         8�� �̻� 11�ù̸����� :8�÷� ����
         11�� �̻� 14�ù̸�����:11�÷� ����
         14���̻� 17�� �̸�����: 14�÷� ����
         17���̻� 20�ù̸�����: 17�÷� ����
         20���̻� 23�ù̸����� : 20�÷� ����
         23���̻� 00�� �̸����� : 23�÷� ����
         00���̻� 2�� �̸����� : 23�÷� ����
         */
        String page = "1";//��������ȣ
        String rows = "10";//�������� �����
        String nx = "62";// ����
        String ny = "124";// �浵 (���� �浵�� ����� �߿��� ������ ����)
        String type = "JSON"; //������ Ÿ��
    	String baseDate = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()); //��ȸ�ϰ���� ��¥(������¥�� �޾ƿ���)
    	String baseTime =  new SimpleDateFormat("HHmm").format(Calendar.getInstance().getTime());//��ȸ�ϰ���� ��¥(�����ð����� �޾ƿ���)
    	int baseTimeNum=Integer.parseInt(baseTime) / 100;// �޾ƿ½ð� (��,�� )-> �� �θ� ��Ÿ���� 
    	//���� �ð��� 00�ó�, 1���ϰ�� ���� 23�÷� �����ؾ� �ϹǷ� 
    	
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
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode(page, "UTF-8")); /*��������ȣ*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode(rows, "UTF-8")); /*�� ������ ��� ��*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode(type, "UTF-8")); /*��û�ڷ�����(XML/JSON)Default: XML*/
        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8")); /*15�� 12�� 1�Ϲ�ǥ*/
        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(RevisedTime, "UTF-8")); /*05�� ��ǥ*/
        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); /*�������� X ��ǥ��*/
        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); /*���������� Y ��ǥ��*/
        URL url = new URL(urlBuilder.toString());
        System.out.println(url); //url ���
        HttpURLConnection conn = (HttpURLConnection) url.openConnection(); //HttpUrlConnection Ŭ���� ����
        conn.setRequestMethod("GET");//��û��� ���� (GET)
        conn.setRequestProperty("Content-type", "application/json");//����� �޼ҵ� ����

        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
      //�������� ��ȯ �ڵ尡 200�̻� 300������ ��� ��Ʈ������ ��ȯ ����� �ޱ�
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        }
      //�� �̿��� ��� ���� �߻�
        else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder(); //���ڿ��� ��� ���� ��ü
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();//��������

        
        String stringJson = sb.toString(); //stringbuilder-> string���� ��ȯ�ϱ�
        System.out.println(stringJson);

        // string -> jsonobject ��
        JSONParser jsonParser = new JSONParser();// json ��ü �����, parser���� �Ľ��ϱ�
        JSONObject jsonObj = (JSONObject) jsonParser.parse(stringJson);// parser�� ���ڿ� �����͸� json �����ͷ� ��ȯ


        JSONObject parse_response = (JSONObject) jsonObj.get("response"); // response key���� �´� Value�� JSON��ü�� ��������
       
        JSONObject parse_body = (JSONObject) parse_response.get("body"); // response �� ���� body ã�ƿ���

        JSONObject parse_items = (JSONObject) parse_body.get("items");  // body �� ���� items �޾ƿ���
     
        JSONArray parse_itemlist = (JSONArray) parse_items.get("item");  // items�� ���� itemlist �� �޾ƿ���  itemlist : �ڿ� [ �� �����ϹǷ� jsonarray

        System.out.println(parse_itemlist);
        
        String[]skySunny = {"0","1","2","3","4","5"};
        String[]skyCloudy= {"6","7","8"};
        String[]skyBad= {"9","10"};
       

 
        System.out.println("* ���� *");

        for (int i = 0; i < parse_itemlist.size(); i++) {

           // �迭 �ȿ� �ִ°͵� JSON���� �̱� ������ JSON Object �� ����
           JSONObject weatherObject = (JSONObject) parse_itemlist.get(i);

           String skyKind  = String.valueOf(weatherObject.get("category"));
           String skyValue = String.valueOf(weatherObject.get("fcstValue"));
          String skyState="";
           //���׿��� �׸� ���� 'SKY' �� ���� ��� 
           if(skyKind.contains("SKY")) {
        	   if(Arrays.asList(skySunny).contains(skyValue)) {
        		   skyState="����";
        	   }
        	   else if(Arrays.asList(skyCloudy).contains(skyValue)) {
        		   skyState="��������";
        	   }
        	   else if (Arrays.asList(skyBad).contains(skyValue)) {
        		   skyState="�帲";
        	   }
        		  
          
           // JSON name���� ����
           System.out.println("������ �߿��� ������ ����");
           System.out.println("������ ��¥�� " + weatherObject.get("baseDate")+" ,");
           System.out.println("���� ���� �ð��� " + weatherObject.get("baseTime")+"�� �̸�");
           System.out.println("���� �ϴ� ���´� "+ skyState +" �Դϴ�");
//          
           
           }
        }
    }
}

