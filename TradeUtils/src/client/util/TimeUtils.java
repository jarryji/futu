package client.util;

import java.util.Date;

public class TimeUtils {
	public static long getCurrentTimestamp()
	{
		return new Date().getTime();
	}
}
