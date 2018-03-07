package com.tasfe.sis.base.utils.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
	public static Date getDateBefore(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		return now.getTime();
	}
	
	public static Date getDateAfter(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
		return now.getTime();
	}
	
	public static String getToDay() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String time = format.format(Calendar.getInstance().getTime());
		return time;
	}
	
	public static Date getYesterDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar now = Calendar.getInstance();
		now.set(Calendar.DATE, now.get(Calendar.DATE) - 1);
		String time = format.format(now.getTime());
		try {
			return format.parse(time);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static Date getMonthDate(int month) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar now = Calendar.getInstance();
		now.set(Calendar.DATE, now.get(Calendar.DATE));
		now.set(Calendar.MONTH, now.get(Calendar.MONTH)-month);
		String time = format.format(now.getTime());
		try {
			return format.parse(time);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static String getYesterDay() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar now = Calendar.getInstance();
		now.set(Calendar.DATE, now.get(Calendar.DATE) - 1);
		String time = format.format(now.getTime());
		return time;
	}
	
	public static Date getStrtoDate(String strDate) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return format.parse(strDate);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static String getTimestamptoStr(Date date2) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(date2.getTime());
		try {
			return format.format(date);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static java.sql.Date getDatetoSDate(Date time) {
		
		try {
			return new java.sql.Date(time.getTime());
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String getStrByDate(Date timeDate) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return format.format(timeDate);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String getStrtoDateStr(String strDate) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return  format.format(format.parse(strDate));
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static java.sql.Date getDatetoSqlDate(Date udate) {
		try {
			return new java.sql.Date(udate.getTime());
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Long getStrtoDateLong(String strDate) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return format.parse(strDate).getTime();
		} catch (ParseException e) {
			return 0L;
		}
	}
	
	public static void main(String[] args) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date startsDate =DateUtil.getStrtoDate("2017-12-24");
		Date date = new Date();
		Date date1 =getDateAfter(startsDate,1);
		java.sql.Date date2 = new java.sql.Date(date1.getTime());
		System.out.println(format.format(date1));
		System.out.println(format.format(date2));
	}
}
