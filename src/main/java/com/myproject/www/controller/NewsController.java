package com.myproject.www.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.myproject.www.api.NaverNewsAPIHandler;

@Controller
public class NewsController {
	// 초기화
	@Autowired
	HttpSession ses;
	 //
	private NaverNewsAPIHandler nh;
}
