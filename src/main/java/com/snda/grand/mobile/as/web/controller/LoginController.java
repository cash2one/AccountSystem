package com.snda.grand.mobile.as.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class LoginController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
	
	public LoginController() {
		LOGGER.info("LoginController initialized.");
	}

	@RequestMapping(value="redirect", method=RequestMethod.GET)
	public ModelAndView redirect(HttpServletRequest request,HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("/index/redirect");
		return mv;
	}
	
	@RequestMapping(value="authorize", method=RequestMethod.GET)
	public ModelAndView authorize(HttpServletRequest request,HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("/index/authorize");
		return mv;
	}
	
	@RequestMapping(value="redirect_successful", method=RequestMethod.GET)
	public ModelAndView redirectToDefault(HttpServletRequest request,HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("/index/redirect_successful");
		return mv;
	}
	
}
