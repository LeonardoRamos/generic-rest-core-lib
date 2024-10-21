package com.generic.rest.core.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for custom String operations without the use of any time consuming REGEXP.
 * 
 * @author leonardo.ramos
 *
 */
public class StringParserUtils {
	
	/**
	 * Default constructor.
	 */
	private StringParserUtils() {
		
	}
	
	/**
	 * Split string with given character delimiter.
	 * 
	 * @param value
	 * @param delimiter
	 * @return splitted string
	 */
	public static List<String> splitStringList(String value, char delimiter) {
		List<String> values = new ArrayList<>();
		
		if (value == null ) {
			return values;
		}

		StringBuilder word = new StringBuilder();
		
		for (int i = 0; i < value.length(); i++) {
			if (value.charAt(i) == delimiter) {
				values.add(word.toString().trim());
				word = new StringBuilder();
			} else {
				word.append(value.charAt(i));
			}
		}
		values.add(word.toString().trim());
		
		return values;
	}
	
	/**
	 * Replace a fragment of a string with a given new fragment.
	 * 
	 * @param source
	 * @param toBeReplaced
	 * @param newToReplace
	 * @return string with replaced value
	 */
	public static String replace(String source, String toBeReplaced, String newToReplace) {
	    if (source == null) {
	        return null;
	    }
	    
	    int i = 0;
	    if ((i = source.indexOf(toBeReplaced, i)) >= 0) {
	        char[] sourceArray = source.toCharArray();
	        char[] nsArray = newToReplace.toCharArray();
	        int oLength = toBeReplaced.length();

	        StringBuilder buf = new StringBuilder(sourceArray.length);
	        buf.append(sourceArray, 0, i).append(nsArray);
	        i += oLength;
	        int j = i;
	        
	        while ((i = source.indexOf(toBeReplaced, i)) > 0) {
	            buf.append(sourceArray, j, i - j).append(nsArray);
	            i += oLength;
	            j = i;
	        }
	    
	        buf.append(sourceArray, j, sourceArray.length - j);
	        source = buf.toString();
	        buf.setLength (0);
	    }
	    
	    return source;
	}

	/**
	 * Replace fragments of a string with a given new fragment.
	 * 
	 * @param source
	 * @param originalString
	 * @param newString
	 * @return string with replaced values
	 */
	public static String replace(String source, String[] originalStrings, String newString) {
	    if (source == null) {
	        return null;
	    }
	    
	    for (int k = 0; k < originalStrings.length; k++) {
	    	source = replace(source, originalStrings[k], newString); 
	    }
	    
	    return source;
	}

	
	/**
	 * Verify if a given String is numeric.
	 * 
	 * @param value
	 * @return true if string is numeric, false otherwise.
	 */
	public static boolean isNumeric(String value) {
		for (char ch : value.toCharArray()) {
			if (!Character.isDigit(ch)) {
				return false;
			}
		}
			
		return true;
	}
	
}