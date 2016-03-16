package org.gaaroth.devlib.utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormatUtils {
	
	public static final String SQL_DATE_FORMAT = "yyyy-MM-dd";
	public static final String SQL_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public static final String DATE_FORMAT = "dd/MM/yyyy";
	public static final String DATETIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
	
	public static final String TIME_FORMAT = "HH:mm";
	
	public static String componeStringNullCheck(String... strings) {
		StringBuffer sb = new StringBuffer();
		for (String string : strings) {
			if (string != null && !string.isEmpty() && !"null".equals(string))
				sb.append(string);
		}
		return sb.toString();
	}
	
	//TIME
	public static SimpleDateFormat getTimeFormatter() {
		return getDateFormatter(TIME_FORMAT);
	}
	
	public static SimpleDateFormat getTimeFormatter(String pattern) {
		SimpleDateFormat df = new SimpleDateFormat();
		df.applyPattern(pattern);
		return df;
	}
	
	//DATE
	public static SimpleDateFormat getDateFormatter() {
		return getDateFormatter(DATE_FORMAT);
	}
	
	public static SimpleDateFormat getDateFormatter(String pattern) {
		SimpleDateFormat df = new SimpleDateFormat();
		df.applyPattern(pattern);
		return df;
	}
	
	//DATETIME
	public static SimpleDateFormat getDateTimeFormatter() {
		return getDateFormatter(DATETIME_FORMAT);
	}
	
	/**
	 * "dd/MM/yyyy hh:mm" (aa)
	 */
	public static SimpleDateFormat getDateTimeFormatter(String pattern) {
		SimpleDateFormat df = new SimpleDateFormat();
		df.applyPattern(pattern);
		return df;
	}
	
	/**
	 * Formato dd/MM/yyyy
	 */
	public static String dateToString(Date date) {
		if (date == null) return "";
		return getDateFormatter().format(date);
	}
	
	/**
	 * Formato dd/MM/yyyy hh:mm
	 */
	public static String dateTimeToString(Date date) {
		if (date == null) return "";
		return getDateTimeFormatter().format(date);
	}
	
	public static Integer stringToInt(String bigDecimalString) {
		try {
			return new Integer(bigDecimalString);
		} catch (Exception e) {
			return new Integer(0);
		}
	}
	
	public static BigDecimal stringToBigDecimal(String bigDecimalString) {
		try {
			return new BigDecimal(bigDecimalString.replace(',', '.'));
		} catch (Exception e) {
			return new BigDecimal(0);
		}
	}
	
	public static Date stringToDate(String dateString) {
		try {
			String year, separator;
			if (dateString.length()==8) {
				year = "yy";
			} else {
				year = "yyyy";
			}
			if (dateString.contains("/")) {
				separator = "/";
			} else {
				separator = "-";
			}
			return getDateFormatter("dd"+separator+"MM"+separator+year).parse(dateString);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * TRUE se e solo se il valore � "true"
	 */
	public static Boolean stringToBoolean(String stringToBoolean) {
		return Boolean.valueOf(stringToBoolean);
	}
	
	/**
	 * TRUE se e solo se il valore � 1
	 */
	public static Boolean integerToBoolean(Integer integerBoolean) {
		if (integerBoolean == null || integerBoolean == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * 1 se e solo se il valore � TRUE
	 */
	public static Integer booleanToInteger(Boolean booleanInteger) {
		if (booleanInteger == null || !booleanInteger) {
			return 0;
		} else {
			return 1;
		}
	}
	
	/**
	 * Umanizza la stringa splittando sull'underscore o maiuscole e mettendo la prima lettera maiuscola
	 */
	public static String humanizeString(String stringToHumanize) {
		String humanizedString = "", splitSequence;

		if (stringToHumanize != null && stringToHumanize.contains("_")) {
			splitSequence = "_";
		} else {
			splitSequence = "(?=[A-Z])";
		}
		
		boolean first = true;
		for(String part : stringToHumanize.split(splitSequence) ) {
			if(first) {
				part = part.substring(0,1).toUpperCase() + part.substring(1).toLowerCase();
				first = false;
			} else {
				part = " " + part.toLowerCase();
			}
			humanizedString = humanizedString + part;
		}		
		
		return humanizedString;
	}
	
	/**
	 * Converte una stringa in mappa.La stringa deve essere nel formato k1=v1;k2=v2 etc.
	 */
	public static Map<String, String> stringToMap(String stringToMap) {
		return stringToMap(stringToMap, ";", "=");
	}
	
	/**
	 * Converte una stringa in mappa.La stringa deve essere nel formato k1[valueSplit]v1[itemSplit]k2[valueSplit]v2 etc.
	 */
	public static Map<String, String> stringToMap(String stringToMap, String itemSplit, String valueSplit) {
		Map<String, String> mappa = new HashMap<String, String>();
		String[] items = stringToMap.split(itemSplit);
		String[] values;
		for (String value : items) {
			values = value.split(valueSplit);
			if (values.length == 2) {
				mappa.put(values[0], values[1]);
			}
		}
		return mappa;
	}
	
	/**
	 * Converte una stringa in mappa.La stringa deve essere nel formato k1,k2,k3 etc.
	 */
	public static List<String> stringToList(String stringToList) {
		return stringToList(stringToList, ",");
	}
	
	/**
	 * Converte una stringa in mappa.La stringa deve essere nel formato k1[itemSplit]k2[itemSplit]k3 etc.
	 */
	public static List<String> stringToList(String stringToList, String itemSplit) {
		List<String> lista = new ArrayList<String>();
		String[] items = stringToList.split(itemSplit);
		for (String value : items) {
			lista.add(value);
		}
		return lista;
	}

}
