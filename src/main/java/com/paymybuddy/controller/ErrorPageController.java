package com.paymybuddy.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorPageController implements ErrorController{

	@RequestMapping("/error")
	public String errorPage() {
		return "error";
	}
}
