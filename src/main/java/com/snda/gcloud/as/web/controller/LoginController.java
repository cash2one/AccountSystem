package com.snda.gcloud.as.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.snda.gcloud.as.web.model.LoginForm;


@Controller
public class LoginController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
	
	public LoginController() {
		LOGGER.info("LoginController initialized.");
	}

	@RequestMapping(value="login", method=RequestMethod.GET)
	public ModelAndView test(HttpServletRequest request,HttpServletResponse response,LoginForm command) {
		String username = command.getUsername();
		ModelAndView mv = new ModelAndView("/index/index","command","LOGIN SUCCESS, " + username);
		return mv;
	}
	
	@RequestMapping(value="redirect", method=RequestMethod.GET)
	public ModelAndView redirect(HttpServletRequest request,HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("/index/redirect");
		return mv;
	}
	
}
