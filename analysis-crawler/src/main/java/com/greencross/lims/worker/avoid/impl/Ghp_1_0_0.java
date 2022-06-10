package com.greencross.lims.worker.avoid.impl;

import com.greencross.lims.dao.RequestDAO;
import com.greencross.lims.entity.Analysis;
import com.greencross.lims.entity.AnalysisFile;
import com.greencross.lims.entity.readonly.Request;
import com.greencross.lims.test.avoid.TestInfo;
import com.greencross.lims.worker.Worker;
import com.sun.istack.NotNull;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service("AVOID ver.1.0.0")
public class Ghp_1_0_0 implements Worker {
	private static final Pattern FILE_NAME_PATTERN = Pattern.compile("^((\\d{2}GHP\\d{1,4})[-_](\\d{1,4})[-_](\\d{8}-\\d{3}-\\d{4})).AVOID.v1.0.0.txt$");
	private final List<String> services = new LinkedList<>();
	private final RequestDAO requestDAO;
	public Ghp_1_0_0(RequestDAO requestDAO) {
		this.requestDAO = requestDAO;
		Arrays.stream(TestInfo.TESTS).map(TestInfo::code).forEach(services::add);
	}
	@Override
	public String name() {
		return "AVOID ver.1.0.0";
	}

	@Override
	public boolean chk(File file) {
		if(!"v1.0.0".equalsIgnoreCase(file.getParentFile().getName())) return false;
		if(!"GH".equalsIgnoreCase(file.getParentFile().getParentFile().getName())) return false;
		String fileName = file.getName();
		Matcher m = FILE_NAME_PATTERN.matcher(fileName);
		return m.find();
	}

	private Long sample(String fileName) {
		Matcher m = FILE_NAME_PATTERN.matcher(fileName);
		if(!m.find()) return null;
		if(m.group(4) == null || m.group(4).isBlank()) return null;
		return Long.parseLong(m.group(4).replace("-", ""));
	}
	private String[] services() {
		return services.toArray(String[]::new);
	}
	private static final String FIND_REQUEST_BY_SAMPLE = "SELECT * FROM request WHERE sample IN (SELECT id FROM sample where patient=(SELECT patient FROM sample WHERE id=:sample))";
	private List<Analysis> findEntity(String fileName) {
		var sample = sample(fileName);
		if(sample == null) return null;
		var targets = Arrays.stream(services()).collect(Collectors.toSet());
		List<Request> req = requestDAO.em().createNativeQuery(FIND_REQUEST_BY_SAMPLE, Request.class).setParameter("sample", sample).getResultList();
		return req.stream()
				  .filter(r->targets.contains(r.pk().service()))
				  .map(r->map(fileName, r))
				  .map(a->{
					  Analysis prev = requestDAO.em().find(Analysis.class, a.pk());
					  if(prev!=null) {
						  Matcher m = FILE_NAME_PATTERN.matcher(fileName);
						  if(m.find()) {
							  String serial = m.group(1);
							  String batch = m.group(2);
							  int row = Integer.parseInt(m.group(3));
							  prev.serial(serial).batch(batch).row(row);
						  }
					  	return prev;
					  } else return a;
				  }).collect(Collectors.toList());
	}

	private Analysis map(String fileName, @NotNull Request request) {
		Matcher m = FILE_NAME_PATTERN.matcher(fileName);
		if(!m.find()) return null;
		String serial = m.group(1);
		String batch = m.group(2);
		int row = Integer.parseInt(m.group(3));
		return new Analysis(Analysis.AnalysisPK.builder().sample(request.pk().sample()).service(request.pk().service()).build())
								  .serial(serial)
								  .batch(batch)
								  .row(row);
	}
	@Override
	public void process(AnalysisFile entity, File file) throws Exception {
		List<Analysis> targets = findEntity(entity.name());
		for(Analysis target: targets) requestDAO.em().merge(target.value(fileToMap(file)).file(file.getName()));
	}
	private static Map<String, Object> fileToMap(File file) throws IOException {
		List<String> lines = Files.readAllLines(file.toPath());
		String[] header = lines.get(0).split("\t", -1);
		int disease = IntStream.range(0, header.length-1).filter(i->"Disease".equalsIgnoreCase(header[i])).findFirst().getAsInt();
		int rsid = IntStream.range(0, header.length-1).filter(i->"rsID".equalsIgnoreCase(header[i])).findFirst().getAsInt();
		Map<String, Object> values = new HashMap<>();
		for(int i = 1; i < lines.size(); ++i) {
			String[] row = lines.get(i).split("\t", -1);
			String key = row[disease].toLowerCase() + ":" + row[rsid];
			values.put(key, rowToMap(header, row));
		}
		return values;
	}
	private static Map<String, String> rowToMap(String[] header, String[] row) {
		Map<String, String> values = new HashMap<>();
		for(int i = 0; i < header.length; ++i) {
			String key = header[i];
			if(key.startsWith("VAF_")) key = "vaf";
			else if(key.startsWith("Depth_")) key = "depth";
			values.put(key.toLowerCase(), row[i]);
		}
		return values;
	}
}
