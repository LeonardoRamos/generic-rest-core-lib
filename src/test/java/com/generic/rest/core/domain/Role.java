package com.generic.rest.core.domain;

import java.util.Arrays;

public enum Role {
	
	ADMIN("Admin"), 
	USER("User");
	
	private final String roleName;

	Role(String roleName) {
		this.roleName = roleName;
	}
	
	public String getRoleName() {
		return roleName;
	}
	
	public static Role getRoleFromString(String userRole) {
		return Arrays.stream(values())
				.filter(ro -> ro.name().equals(userRole) || ro.getRoleName().equals(userRole))
				.findFirst()
				.orElse(null);
	}
	
}