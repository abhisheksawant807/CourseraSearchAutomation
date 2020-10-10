package Utilities;

import java.util.Date;

public class DateUtil {

	// Returns a unique timestamp for naming unique files
	public static String getTimeStamp() {
		Date date = new Date();
		return date.toString().replaceAll(":", "_").replaceAll(" ", "_");
	}
}
