package com.generic.rest.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.generic.rest.core.BaseConstants;
import com.generic.rest.core.BaseConstants.MSGERROR;

public class CalendarUtils {
	
	private CalendarUtils() {
		
	}
	
	private static final Logger log = LoggerFactory.getLogger(CalendarUtils.class);
	
	public static Calendar createCalendarFromString(String dateText, String dateFormat) {
		if (dateText == null || "".equals(dateText)) {
			return null;
		}
		
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(BaseConstants.DATE_TIMEZONE));
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, 
				new Locale(BaseConstants.LOCALE_PT, BaseConstants.LOCALE_BR));

		try {
			calendar.setTime(simpleDateFormat.parse(dateText));
		} 
		catch (ParseException e) {
			log.error(MSGERROR.ERROR_PARSE_DATE, dateText, e);
			return null;
		}
		
		return calendar;
	}
	
	public static Calendar createCalendarFromMiliseconds(Long miliseconds) {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(BaseConstants.DATE_TIMEZONE), 
				new Locale(BaseConstants.LOCALE_PT, BaseConstants.LOCALE_BR));
		calendar.setTimeInMillis(miliseconds);
		return calendar;
	}
	
}