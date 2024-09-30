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

import io.micrometer.common.util.StringUtils;

/**
 * Utilitary class for {@link Calendar} operations.
 * 
 * @author leonardo.ramos
 *
 */
public class CalendarUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CalendarUtils.class);

	/**
	 * Default constructor
	 */
	private CalendarUtils() {
		
	}
	
	/**
	 * Create instance of {@link Calendar} with given format from parameter dateText.
	 * 
	 * @param dateText
	 * @param dateFormat
	 * @return {@link Calendar}
	 */
	public static Calendar createCalendarFromString(String dateText, String dateFormat) {
		if (StringUtils.isBlank(dateText)) {
			return null;
		}
		
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(BaseConstants.DATE_TIMEZONE));
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, 
				Locale.of(BaseConstants.LOCALE_PT, BaseConstants.LOCALE_BR));

		try {
			calendar.setTime(simpleDateFormat.parse(dateText));
		} 
		catch (ParseException e) {
			LOGGER.error(MSGERROR.ERROR_PARSE_DATE, dateText, e);
			return null;
		}
		
		return calendar;
	}
	
	/**
	 * Create instance of {@link Calendar} from miliseconds.
	 * 
	 * @param miliseconds
	 * @return {@link Calendar}
	 */
	public static Calendar createCalendarFromMiliseconds(Long miliseconds) {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(BaseConstants.DATE_TIMEZONE), 
				Locale.of(BaseConstants.LOCALE_PT, BaseConstants.LOCALE_BR));
		calendar.setTimeInMillis(miliseconds);
		return calendar;
	}
	
}