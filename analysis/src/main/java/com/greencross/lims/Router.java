package com.greencross.lims;

import com.greencross.lims.dto.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Router {
	@RequestMapping(value="/services", method= RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public Page[] services() {
		return new Page[] {
			new Page().icon("fa-diagnoses").title("Analysis").uri("/avoid-service/analysis.html").order("7")
		};
	}
}
