package com.itacademy.jd2.lg.ms.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class HomeController {
	@RequestMapping(value = "/home")
	public String index() {
		return "index"; // tiles definition name.
						// webapp/WEB-INF/tiles-definitions/views.xml
	}
}
