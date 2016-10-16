package org.durcframework.core.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DateUtil {
	// yyyy-MM-dd HH:mm:ss
	public static DateFormat YMDHMS_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static String ymdhmsFormat(Date date) {
		return YMDHMS_FORMAT.format(date);
	}

	// 年月
	private static final DateFormat ymFormat = new SimpleDateFormat("yyyy-MM");
	// 年
	private static final DateFormat yFormat = new SimpleDateFormat("yyyy");
	// 月
	private static final DateFormat mFormat = new SimpleDateFormat("MM");

	public static final DateFormat DEFAULT_DATEFORMAT = new SimpleDateFormat(
			"yyyy-MM-dd");

	private static final DateFormat TIME__DATEFORMAT = new SimpleDateFormat(
			"HH:mm:ss");

	private static final DateFormat DATETIME_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private static final DateFormat WEEK_FORMAT = new SimpleDateFormat("E");

	private static final Map<String, DateFormat> DATE_FORMAT_MAP = new HashMap<String, DateFormat>();

	static {
		DATE_FORMAT_MAP.put("yyyy-MM-dd", DEFAULT_DATEFORMAT);
		DATE_FORMAT_MAP.put("HH:mm:ss", TIME__DATEFORMAT);
		DATE_FORMAT_MAP.put("yyyy-MM-dd HH:mm:ss", DATETIME_FORMAT);
		DATE_FORMAT_MAP.put("E", WEEK_FORMAT);
	}

	public static final int BEFORE = -1;

	public static final int AFTER = 1;

	public static final int EQUAL = 0;

	/**
	 * @author wxj
	 * @disc 得到当前时间
	 * @return date
	 */
	public static Date getCurrentDate() {
		Date date = new Date();
		return zerolizedTime(date);
	}

	/**
	 * 把日期后的时间归0 变成(yyyy-MM-dd 00:00:00:000)
	 * 
	 * @param date
	 * @return Date
	 */
	public static final Date zerolizedTime(Date fullDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(fullDate);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * 得到两个时间的间隔
	 * 
	 * @param bDate
	 * @param eDate
	 * @return
	 */
	static public long dateDiffByDay(Date bDate, Date eDate) {
		if (bDate == null || eDate == null)
			return 0L;
		return (bDate.getTime() - eDate.getTime()) / (1000 * 3600 * 24);
	}

	/**
	 * @author wxj disc 根据当前日期,得到当前年月
	 * @param date
	 * @return str
	 */
	public static final String ymFormat(Date date) {
		if (date == null)
			return "";
		return ymFormat.format(date);
	}

	/**
	 * @author wxj disc 根据当前日期,得到当前年份
	 * @param date
	 * @return str
	 */
	public static final String yFormat(Date date) {
		if (date == null)
			return "";
		return yFormat.format(date);
	}

	/**
	 * @author wxj disc 根据当前日期,得到当前月份
	 * @param date
	 * @return str
	 */
	public static final String mFormat(Date date) {
		if (date == null)
			return "";
		return mFormat.format(date);
	}

	/**
	 * @param ymdStringDate
	 * @return date
	 * @throws ParseException
	 */
	public static final Date ymdString2Date(String ymdStringDate) {
		if (ymdStringDate == null)
			return null;
		Date ret = new Date();
		try {
			ret = DEFAULT_DATEFORMAT.parse(ymdStringDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * 把日期格式转换成格式(yyyy-MM-dd)
	 * 
	 * @param Date
	 * @return string
	 */
	public static final String getDate(Date aDate) {
		if (aDate == null)
			return null;
		return DEFAULT_DATEFORMAT.format(aDate);
	}

	/**
	 * 把日期格式转换成格式aMask
	 * 
	 * @param Date
	 * @param aMask
	 *            包含日期格式的字符串
	 * @return string
	 */
	public static final String getDate(Date aDate, String aMask) {
		SimpleDateFormat df = null;
		String returnValue = "";

		if (aDate != null) {
			df = new SimpleDateFormat(aMask);
			returnValue = df.format(aDate);
		}

		return returnValue;
	}

	/**
	 * 返回当前的时间，格式为H:mm:ss
	 * 
	 * @return 时间字符串
	 */
	public static final String getTimeNow() {
		return TIME__DATEFORMAT.format(new Date());
	}

	/**
	 * 取得当前的时间,格式化: yyyy-MM-dd
	 * 
	 * @return 返回格式为yyyy-MM-dd的字符串
	 * @throws ParseException
	 */
	public static String getToday() throws ParseException {
		return convertDateToString(new Date());
	}

	/**
	 * 取得当前的时间,格式化为指定格式
	 * 
	 * @return 返回格式为指定格式的字符串
	 * @throws ParseException
	 */
	public static final String getToday(String aMask) throws ParseException {
		return convertDateToString(new Date(), aMask);
	}

	/**
	 * 取得特定时间对应的字符串,格式化为指定格式
	 * 
	 * @param aMask
	 *            包含日期格式的字符串
	 * @param Date
	 * @return 返回格式为指定格式的字符串
	 * @see java.text.SimpleDateFormat
	 */
	public static final String getDateTime(Date aDate, String aMask) {
		if (aDate == null)
			return null;

		DateFormat format = DATE_FORMAT_MAP.get(aMask);
		if (format == null) {
			format = new SimpleDateFormat(aMask);
			DATE_FORMAT_MAP.put(aMask, format);
		}

		return format.format(aDate);
	}

	/**
	 * 取得特定时间对应的字符串,格式化为yyyy-MM-dd HH:mm:ss
	 * 
	 * @param aMask
	 *            包含日期格式的字符串
	 * @param Date
	 * @return 返回格式为指定格式的字符串
	 * @see java.text.SimpleDateFormat
	 */
	public static final String getDateTime(Date aDate) {
		if (aDate == null)
			return null;
		return DATETIME_FORMAT.format(aDate);
	}

	/**
	 * 把日期形式转换成字符串形式，格式为yyyy-MM-dd
	 * 
	 * @param aDate
	 * @return 返回格式为指定格式的字符串
	 */
	public static final String convertDateToString(Date aDate) {
		if (aDate == null)
			return null;
		return DEFAULT_DATEFORMAT.format(aDate);
	}

	/**
	 * 把日期形式转换成字符串形式，格式为指定格式
	 * 
	 * @param aDate
	 * @return 返回格式为指定格式的字符串
	 */
	public static final String convertDateToString(Date aDate, String aMask) {
		if (aDate == null)
			return null;
		if (aMask == null || aMask.equals(""))
			return null;
		return getDateTime(aDate, aMask);
	}

	/**
	 * 把字符串形式转换成日期形式，字符串的格式必须为yyyy-MM-dd
	 * 
	 * @param aMask
	 *            包含日期格式的字符串
	 * @param Date
	 * @return a formatted string representation of the date
	 * @throws ParseException
	 * @see java.text.SimpleDateFormat
	 */
	public static Date convertStringToDate(String strDate) {
		if (strDate == null)
			return null;
		if (strDate.equals(""))
			return null;
		Date tmp = null;
		try {
			tmp = DEFAULT_DATEFORMAT.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return tmp;
	}

	/**
	 * 把一个表示日期的字符串转化成一个指定格式的日期类型
	 * 
	 * @param aMask
	 *            包含日期格式的字符串
	 * @param strDate
	 *            表示一个日期的字符串
	 * @return 已经转换成具体格式的Date类
	 * @throws ParseException
	 */
	public static final Date convertStringToDate(String strDate, String aMask)
			throws ParseException {
		if (strDate == null)
			return null;
		if (aMask == null)
			return null;

		DateFormat format = DATE_FORMAT_MAP.get(aMask);
		if (format == null) {
			format = new SimpleDateFormat(aMask);
			DATE_FORMAT_MAP.put(aMask, format);
		}

		return format.parse(strDate);
	}

	/**
	 * 取得当天的星期数
	 * 
	 * @return String
	 */
	public static final String getDay() {
		return getWeek(new Date());
	}

	/**
	 * 取得指定日期的星期数
	 * 
	 * @return String
	 */
	public static final String getWeek(Date date) {
		if (date == null)
			return null;
		return WEEK_FORMAT.format(date);
	}

	/**
	 * 判断两个日期字符串是否相等,格式必需为yyyy-MM-dd
	 * 
	 * @param one
	 *            第一个日期字符串
	 * @param two
	 *            第二个日期字符串
	 * @return Boolean
	 * @throws ParseException
	 */
	public static final Boolean isEqual(String one, String two)
			throws ParseException {
		int temp = DEFAULT_DATEFORMAT.parse(one).compareTo(
				DEFAULT_DATEFORMAT.parse(two));
		return temp == 0;
	}

	/**
	 * 判断两个日期字符串是否相等
	 * 
	 * @param one
	 *            第一个日期字符串
	 * @param two
	 *            第二个日期字符串
	 * @param aMask
	 *            包含日期格式的字符串
	 * @return Boolean
	 * @throws ParseException
	 */
	public static final Boolean isEqual(String one, String two, String aMask)
			throws ParseException {

		return isEqual(one, two, aMask, aMask);
	}

	/**
	 * 判断两个日期字符串是否相等
	 * 
	 * @param one
	 *            第一个日期字符串
	 * @param two
	 *            第二个日期字符串
	 * @param aMaskOne
	 *            对应第一个日期字符串的包含日期格式的字符串
	 * @param aMaskTwo
	 *            对应第二个日期字符串的包含日期格式的字符串
	 * @return Boolean
	 * @throws ParseException
	 */
	public static final Boolean isEqual(String one, String two,
			String aMaskOne, String aMaskTwo) throws ParseException {

		return ((convertStringToDate(one, aMaskOne)
				.compareTo(convertStringToDate(two, aMaskTwo))) == 0);
	}

	/**
	 * 判断两个日期字符串前后,格式必需为yyyy-MM-dd 如果第一个日期字符串在第二个字符串前，返回-1
	 * 如果第一个日期字符串在第二个字符串后，返回1 如果第一个日期字符串等于第二个字符串，返回0
	 * 
	 * @param one
	 *            第一个日期字符串
	 * @param two
	 *            第二个日期字符串
	 * @return Boolean
	 * @throws ParseException
	 */
	public static final int compare(String one, String two)
			throws ParseException {
		return DEFAULT_DATEFORMAT.parse(one).compareTo(
				DEFAULT_DATEFORMAT.parse(two));
	}

	/**
	 * 判断两个日期字符串前后,格式必需为yyyy-MM-dd 如果第一个日期字符串在第二个字符串前，返回-1
	 * 如果第一个日期字符串在第二个字符串后，返回1 如果第一个日期字符串等于第二个字符串，返回0
	 * 
	 * @param one
	 *            第一个日期字符串
	 * @param two
	 *            第二个日期字符串
	 * @param aMask
	 *            包含日期格式的字符串
	 * @return Boolean
	 * @throws ParseException
	 */
	public static final int compare(String one, String two, String aMask)
			throws ParseException {

		return compare(one, two, aMask, aMask);
	}

	/**
	 * 判断两个日期字符串前后,格式必需为yyyy-MM-dd 如果第一个日期字符串在第二个字符串前，返回-1
	 * 如果第一个日期字符串在第二个字符串后，返回1 如果第一个日期字符串等于第二个字符串，返回0
	 * 
	 * @param one
	 *            第一个日期字符串
	 * @param two
	 *            第二个日期字符串
	 * @param aMaskOne
	 *            对应第一个日期字符串的包含日期格式的字符串
	 * @param aMaskTwo
	 *            对应第二个日期字符串的包含日期格式的字符串
	 * @return Boolean
	 * @throws ParseException
	 */
	public static final int compare(String one, String two, String aMaskOne,
			String aMaskTwo) throws ParseException {

		return (convertStringToDate(one, aMaskOne)
				.compareTo(convertStringToDate(two, aMaskTwo)));
	}

	/**
	 * 返回两时间的时间间隔（以分计算）
	 * 
	 * @param time1
	 * @param time2
	 * @return
	 */
	static public long spaceMinute(Date date1, Date date2) {
		Long num1 = date1.getTime();
		Long num2 = date2.getTime();
		Long space = (num2 - num1) / (1000 * 60);
		return space;
	}

	/**
	 * 返回两时间的时间间隔（以天计算）
	 * 
	 * @param time1
	 * @param time2
	 * @return
	 */
	static public Long spaceDay(Date date1, Date date2) {
		Long num1 = date1.getTime();
		Long num2 = date2.getTime();
		Long space = (num2 - num1) / (1000 * 3600 * 24);
		return space;
	}

	static public List<String> getdateList(Date beginDate, Date endDate) {
		if (endDate == null)
			endDate = new Date();
		List<String> list = new ArrayList<String>();
		while(beginDate.before(endDate)) {
			list.add(getDate(beginDate));
			beginDate = getDateAfterDay(beginDate, 1);
		}
		list.add(getDate(endDate));
		return list;
	}

	static public Date getCurrentTime() {
		return new Date();
	}

	static public Date currentDate() {
		String ret = "";
		Date date = new Date();
		ret = getDate(date);
		return ymdString2Date(ret);
	}

	static public Date getDateAfterDay(Date somedate, int day) {
		if (somedate == null)
			return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(somedate);
		cal.add(Calendar.DAY_OF_MONTH, day);
		return new Date(cal.getTime().getTime());
	}

	static public Timestamp getDateAfterDay(int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_MONTH, day);
		return new Timestamp(cal.getTime().getTime());
	}

	static public Timestamp getTSAfterDay(Date somedate, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(somedate);
		cal.add(Calendar.DAY_OF_MONTH, day);
		return new Timestamp(cal.getTime().getTime());
	}

	// 取得本月第一天时间
	static public Date getFirstDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}

}
