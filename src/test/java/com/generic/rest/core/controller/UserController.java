package com.generic.rest.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generic.rest.core.ApiConstants.CONTROLLER;
import com.generic.rest.core.domain.User;
import com.generic.rest.core.service.UserService;

@RestController
@RequestMapping(CONTROLLER.USER.PATH)
public class UserController extends BaseApiRestController<User, UserService>{
	
	@Autowired
	private UserService userService;

	@Override
	public UserService getService() {
		return userService;
	}
    
}