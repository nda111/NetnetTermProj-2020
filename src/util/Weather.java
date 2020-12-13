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

	private String nx = "62";// ����
	private String ny = "124";// �浵 (���� �浵�� ����� �߿��� ������ ����)
	private String dataType = "JSON"; // ������ Ÿ��
	private String baseDate = null;
	private String baseTime = null;

	public Weather(int nx, int ny, String dataType, long timeInMillis) {

		this.nx = Integer.toString(nx);
		this.ny = Integer.toString(ny);
		this.dataType = dataType;

        LocalDateTime now = LocalDateTime.now();
        int hour = ((now.getHour() + 1) / 3 * 3 - 1) % 24;
        if (now.getHour() < 2) {
        	
        	now = now.minusDays(1);
        	hour = 23;
        }
        
        baseDate = String.format("%d%02d%02d", now.getYear(), now.getMonthValue(), now.getDayOfMonth());
        baseTime = String.format("%02d00", hour);
	}

	public JSONArray getData(int page, int rows) {

		StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst"); /* URL */
		try {

			urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + Servicekey); /* Service Key */
			urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(Integer.toString(page), "UTF-8")); /* ��������ȣ */
			urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(Integer.toString(rows), "UTF-8")); /* �� ������ ��� �� */
			urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode(dataType, "UTF-8")); /* ��û�ڷ����� */
			urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8")); /* 15�� 12�� 1�Ϲ�ǥ */
			urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8")); /* 05�� ��ǥ */
			urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); /* �������� X ��ǥ�� */
			urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); /* ���������� Y ��ǥ�� */
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();

			return null;
		}

		try {
			
			URL url = new URL(urlBuilder.toString());

			HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // HttpUrlConnection Ŭ���� ����
			conn.setRequestMethod("GET"); // ��û��� ���� (GET)
			conn.setRequestProperty("Content-type", "application/json");// ����� �޼ҵ� ����

			System.out.println("Response code: " + conn.getResponseCode());
			BufferedReader rd;
			// �������� ��ȯ �ڵ尡 200�̻� 300������ ��� ��Ʈ������ ��ȯ ����� �ޱ�
			if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
				
				rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			}
			// �� �̿��� ��� ���� �߻�
			else {
				
				rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
				
				return null;
			}
			StringBuilder sb = new StringBuilder(); // ���ڿ��� ��� ���� ��ü
			String line;
			while ((line = rd.readLine()) != null) {
				
				sb.append(line);
			}
			rd.close();
			conn.disconnect();// ��������

			String stringJson = sb.toString(); // stringbuilder-> string���� ��ȯ�ϱ�
			System.out.println(stringJson);

			// string -> jsonobject ��
			JSONParser jsonParser = new JSONParser();// json ��ü �����, parser���� �Ľ��ϱ�
			JSONObject jsonObj = (JSONObject) jsonParser.parse(stringJson);// parser�� ���ڿ� �����͸� json �����ͷ� ��ȯ
			JSONObject parse_response = (JSONObject) jsonObj.get("response"); // response key���� �´� Value�� JSON��ü�� ��������
			JSONObject parse_body = (JSONObject) parse_response.get("body"); // response �� ���� body ã�ƿ���
			JSONObject parse_items = (JSONObject) parse_body.get("items"); // body �� ���� items �޾ƿ���
			JSONArray parse_itemlist = (JSONArray) parse_items.get("item"); // items�� ���� itemlist �� �޾ƿ��� itemlist : �ڿ� [ ��
																			// �����ϹǷ� jsonarray		
			System.out.println(parse_itemlist);
			
			return parse_itemlist;
		} catch (IOException | ParseException e) {

			e.printStackTrace();
			
			return null;
		}
	}
	
	public String getDataAsString(int page, int rows) {

		JSONArray parse_itemlist = this.getData(page, rows);
		for (int i = 0; i < parse_itemlist.size(); i++) {
			JSONObject weatherObject = (JSONObject) parse_itemlist.get(i);

			String[] skySunny = { "0", "1", "2", "3", "4", "5" };
			String[] skyCloudy = { "6", "7", "8" };
			String[] skyBad = { "9", "10" };

			String skyKind = String.valueOf(weatherObject.get("category"));
			String skyValue = String.valueOf(weatherObject.get("fcstValue"));
			String baseDates = String.valueOf(weatherObject.get("baseDate"));
			String baseTimes = String.valueOf(weatherObject.get("baseTime"));

			String skyState = "";
			// ���׿��� �׸� ���� 'SKY' �� ���� ���
			if (skyKind.contains("SKY")) {
				
				if (Arrays.asList(skySunny).contains(skyValue)) {
					
					skyState = "����";
				} else if (Arrays.asList(skyCloudy).contains(skyValue)) {
					
					skyState = "��������";
				} else if (Arrays.asList(skyBad).contains(skyValue)) {
					
					skyState = "�帲";
				}

				StringBuilder resultBuilder = new StringBuilder("������ �߿��� ������ ����");
				resultBuilder.append("������ ��¥�� " + baseDates+" ,\n");
				resultBuilder.append("���� ���� �ð��� " + baseTimes+"�� �̸�\n");
				resultBuilder.append("���� �ϴ� ���´� "+ skyState +" �Դϴ�.");

				return resultBuilder.toString();
			}
		}
		
		return null;
	}
}