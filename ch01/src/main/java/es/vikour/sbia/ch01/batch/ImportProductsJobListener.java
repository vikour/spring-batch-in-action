package es.vikour.sbia.ch01.batch;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ImportProductsJobListener implements JobExecutionListener {

	@Override
	public void beforeJob(JobExecution jobExecution) {
		log.info("Starting job {}", jobExecution.getJobInstance().getJobName());
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		
		if (jobExecution.getStatus() == BatchStatus.COMPLETED)
			log.info("Import products job exited successfully");
		else if (jobExecution.getStatus() == BatchStatus.FAILED)
			log.warn("Import products job exited wrongly");
		else
			log.debug("Import products job exited with status '{}'", jobExecution.getStatus());
		
	}

}
