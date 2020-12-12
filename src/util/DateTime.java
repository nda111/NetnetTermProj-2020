package util;

import java.text.SimpleDateFormat;

public class DateTime {

	public static String millisToShortTimeString(long millis) {
		
		return new SimpleDateFormat("a hh:mm").format(millis);
	}
	
	public static String millisToLongDateString(long millis) {
		
		return new SimpleDateFormat("yyyy³â MM¿ù ddÀÏ").format(millis);
	}
}
