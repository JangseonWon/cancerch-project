package com.greencross.lims.analysis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greencross.lims.dao.AnalysisDAO;
import com.greencross.lims.dao.AnalysisFileDAO;
import com.greencross.lims.entity.AnalysisFile;
import com.greencross.lims.worker.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Configuration
public class AnalysisCrawler {
	private final Logger Log = LoggerFactory.getLogger(getClass());
	private final AnalysisDAO dao;
	private final AnalysisFileDAO dao3;
	private final PlatformTransactionManager tx;
	private final ObjectMapper om;
	@Value("${gcgenome.tmp-dir}")
	private String tmp;
	@Value("${gcgenome.processed-dir}")
	private String processed;
	@Value("${gcgenome.undefined-dir}")
	private String error;
	private final List<Worker> workers;
	public AnalysisCrawler(AnalysisDAO dao, AnalysisFileDAO dao3, PlatformTransactionManager txManager, ObjectMapper om, List<Worker> workers) {
		this.dao = dao;
		this.dao3 = dao3;
		this.tx = txManager;
		this.om = om;
		this.workers = workers;
	}

	// 1시간에 1번 크롤링
	@Scheduled(fixedDelay=1000*60*60)
	public void updateStatus() {
		File dir = new File(tmp);
		File err = new File(error);
		File std = new File(processed);
		if(!dir.exists()) dir.mkdirs();
		if(!err.exists()) err.mkdirs();
		if(!std.exists()) std.mkdirs();
		if(dir.listFiles()==null) return;
		Log.info("Crawling..");
		for(File panel: dir.listFiles()) {
			Log.info("\t" + panel);
			if(panel!=null) for(File version: panel.listFiles()) {
				Log.info("\t\t" + version);
				if(version!=null) for(File child: version.listFiles()) {
					UUID id = UUID.randomUUID();
					String fileName = child.getName();
					TransactionStatus t = tx.getTransaction(TransactionDefinition.withDefaults());
					long size = child.length();
					AnalysisFile entity = new AnalysisFile().id(id).name(fileName).size(size).path(child.getAbsolutePath());
					dao3.merge(entity);

					try {
						boolean processed = false;
						for(Worker w: workers) if(w.chk(child)) {
							Log.info("Processing file:" + panel.getName() + "/" + version.getName() + "/" + child.getName());
							w.process(entity, child);
							processed = true;
						}
						tx.commit(t);
						if(processed) try {
							Files.move(child.toPath(), std.toPath().resolve(child.getName()), StandardCopyOption.REPLACE_EXISTING);
						} catch (IOException ioException) {
							ioException.printStackTrace();
						}
					} catch(Exception e) {
						e.printStackTrace();
						tx.rollback(t);
						try {
							Files.move(child.toPath(), err.toPath().resolve(child.getName()), StandardCopyOption.REPLACE_EXISTING);
						} catch (IOException ioException) {
							ioException.printStackTrace();
						}
					}
				}
			}
		}
		Log.info("Complete");
	}


}
