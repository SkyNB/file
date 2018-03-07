package com.tasfe.sis.order.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	public static String getToDay() {
		return format.format(new Date());
	}

}
