package com.greencross.lims.service;

import com.greencross.lims.dto.Analysis;
import com.greencross.lims.dto.QueryServerside;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class AnalysisController {
	private final Logger Log = LoggerFactory.getLogger(getClass());
	private final AnalysisService svc;
	public AnalysisController(AnalysisService svc) {
		this.svc = svc;
	}

	@PostMapping(value="/analysis", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Page<Analysis> analysis(@RequestBody QueryServerside query) {
		return svc.list(query);
	}

	@PatchMapping(value="/analysis/{sample}/{service}/etc", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Analysis update(@PathVariable Long sample, @PathVariable String service, @RequestBody(required = false) String info) {
		svc.update(sample, service, info);
		return svc.get(sample, service);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String exception(Exception e) {
		Log.error(e.getMessage(), e);
		return e.getMessage();
	}
}
