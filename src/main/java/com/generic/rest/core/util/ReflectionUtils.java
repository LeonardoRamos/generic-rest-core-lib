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

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ReflectionUtils {
	
	private static ObjectMapper mapper = new ObjectMapper();

	private ReflectionUtils() {
		
	}
	
	public static List<Object> getFieldList(String value, Class<?> clazz) throws IOException {
		List<Object> values = new ArrayList<>();
		StringBuilder word = new StringBuilder();
		
		for (int i = 0; i < value.length(); i++) {
			if (value.charAt(i) == ',') {
				values.add(getEntityValueParsed(word.toString().trim(), clazz));
				word = new StringBuilder();
			} else {
				word.append(value.charAt(i));
			}
		}
		
		values.add(getEntityValueParsed(word.toString().trim(), clazz));
		
		return values;
	}
	
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
	
	public static Object getEntityValueParsed(String value, Class<?> clazz) throws IOException {
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

	private static Object getEnumFieldValue(String value, Class<?> clazz) {
		try {
			return Enum.valueOf((Class<? extends Enum>) clazz, value);
		} catch (IllegalArgumentException | NullPointerException e) {
			return value.toUpperCase();
		}
	}

	private static Object getCalendarFieldValue(String value) {
		if (Boolean.TRUE.equals(StringParserUtils.isNumeric(value))) {
			return CalendarUtils.createCalendarFromMiliseconds(Double.valueOf(value).longValue());
		}
		return CalendarUtils.createCalendarFromString(value, BaseConstants.DEFAULT_DATE_FORMAT);
	}
	
}