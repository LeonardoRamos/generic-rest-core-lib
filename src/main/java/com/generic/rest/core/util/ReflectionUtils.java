package com.generic.rest.core.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.generic.rest.core.BaseConstants;

/**
 * Utility class for reflection based operations.
 * 
 * @author leonardo.ramos
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ReflectionUtils {
	
	private static ObjectMapper mapper = new ObjectMapper();

	/**
	 * Default constructor.
	 */
	private ReflectionUtils() {
		
	}
	
	/**
	 * Retrieve list of field values parsed as a given Class type.
	 * 
	 * @param values
	 * @param clazz
	 * @return list of fields
	 * @throws IOException
	 */
	public static List<Object> getTypifiedValue(List<String> values, Class<?> clazz) throws IOException {
		List<Object> typifiedValues = new ArrayList<>();
		
		for (String value : values) {
			typifiedValues.add(getTypifiedValue(value, clazz));
		}
		
		return typifiedValues;
	}
	
	/**
	 * Parse String value for specific class parsed value.
	 * 
	 * @param value
	 * @param clazz
	 * @return field data value
	 * @throws IOException
	 */
	public static Object getTypifiedValue(String value, Class<?> clazz) throws IOException {
		if (value != null && !"".equals(value)) {
			if (clazz.equals(Long.class)) {
				return Double.valueOf(value).longValue();
			
			} else if (clazz.equals(Double.class)) {
				return Double.valueOf(value);
			
			} else if (clazz.equals(Float.class)) {
				return Float.valueOf(value);
			
			} else if (clazz.equals(Integer.class)) {
				return Double.valueOf(value).intValue();
			
			} else if (clazz.equals(BigDecimal.class)) {
				return new BigDecimal(value.trim());
				
			} else if (clazz.equals(Date.class)) {
				return Double.valueOf(value);
			
			} else if (clazz.equals(Calendar.class)) {
				return getCalendarFieldValue(value);
			
			} else if (clazz.equals(String.class)) {
				return value;
			
			} else if (clazz.isEnum()) {
				return getEnumFieldValue(value, clazz);
				
			} else {
				return mapper.readValue(value, clazz);
			}
		}
		
		return value;
	}
	
	/**
	 * Retrieve {@link Field} with fiven fieldName.
	 * 
	 * @param clazz
	 * @param fieldName
	 * @return field name
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public static Field getEntityFieldByName(Class<?> clazz, String fieldName) throws NoSuchFieldException, SecurityException {
		try {
			return clazz.getDeclaredField(fieldName);
		
		} catch (NoSuchFieldException e) {
			Class<?> superClazz = clazz.getSuperclass();
			
			while (superClazz != null) {
				try {
					return superClazz.getDeclaredField(fieldName);
				
				} catch (NoSuchFieldException ex) {
					superClazz = superClazz.getSuperclass();
				}
			}
			
			throw new NoSuchFieldException(e.getMessage());
		}
	}
	
	/**
	 * Parse value of type {@link Enum}.
	 * 
	 * @param value
	 * @param clazz
	 * @return Field value for {@link Enum} type
	 */
	private static Object getEnumFieldValue(String value, Class<?> clazz) {
		try {
			return Enum.valueOf((Class<? extends Enum>) clazz, value);
		} catch (IllegalArgumentException | NullPointerException e) {
			return value.toUpperCase();
		}
	}

	/**
	 * Parse value of type {@link Calendar}.
	 * 
	 * @param value
	 * @return {@link Calendar} value for data value
	 */
	private static Object getCalendarFieldValue(String value) {
		if (StringParserUtils.isNumeric(value)) {
			return CalendarUtils.createCalendarFromMiliseconds(Double.valueOf(value).longValue());
		}
		return CalendarUtils.createCalendarFromString(value, BaseConstants.DEFAULT_DATE_FORMAT);
	}
	
}