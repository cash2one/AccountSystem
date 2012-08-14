package com.snda.gcloud.as.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.snda.gcloud.as.web.model.LoginForm;


@Controller
public class LoginController {

	@RequestMapping(value="login", method=RequestMethod.GET)
	public ModelAndView test(HttpServletRequest request,HttpServletResponse response,LoginForm command) {
		String username = command.getUsername();
		ModelAndView mv = new ModelAndView("/index/index","command","LOGIN SUCCESS, " + username);
		return mv;
	}
	
}
