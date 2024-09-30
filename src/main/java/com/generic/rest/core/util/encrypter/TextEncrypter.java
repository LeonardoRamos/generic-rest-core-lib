package com.generic.rest.core.util.encrypter;

/**
 * Interface responsible for data encrypting and validation operations.
 * 
 * @author leonardo.ramos
 *
 */
public interface TextEncrypter {
	
	/**
	 * Encrypt given text.
	 * 
	 * @param textToHash
	 * @return encrypted text
	 */
	public String encryptText(String textToHash);
	
	/**
	 * Performs validation of hashedText with encoded text.
	 * 
	 * @param text
	 * @param hashedText
	 * @return true if hashedText matches encoded text false otherwise.
	 */
	public boolean matchText(String text, String hashedText);
	
}