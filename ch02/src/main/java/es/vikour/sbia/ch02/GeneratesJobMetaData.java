package es.vikour.sbia.ch02;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.sql.DataSource;

import org.apache.commons.io.FileUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class GeneratesJobMetaData {
	
	private static final String repoDir = "./repo";
	private static final String targetDir = "./temp";
	
	public static void main(String [] args) throws Exception {
		
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				"/batch-infrastructure.xml",
				"/connect-datasource-context.xml",
				"/import-products-job-context.xml");
		
		truncate(ctx);
		
		JobLauncher jobLauncher = ctx.getBean(JobLauncher.class);
		Job job = ctx.getBean(Job.class);
		
		Calendar calendar = Calendar.getInstance();
		
		for (int i = 0 ; i < 10; i++) {
			
			if (i == 7) {
				corruptedProcessing(jobLauncher, job, calendar);
			}
			
			normalProcessing(jobLauncher, job, calendar);
			calendar.add(Calendar.DATE, 1);
		}
		
		cleanRepoDir();
		cleanTargetDir();
	}

	private static void cleanTargetDir() throws IOException {
		cleanDir(targetDir);
	}

	private static void cleanRepoDir() throws IOException {
		cleanDir(repoDir);
	}

	private static void cleanDir(String dir) throws IOException {
		File dirAsFile = new File(dir);
		
		if (dirAsFile.exists())
			FileUtils.deleteDirectory(dirAsFile);
	}

	private static void initRepoDir() throws IOException {
		initDir(repoDir);
	}

	private static void initTargetDir() throws IOException {
		initDir(targetDir);
	}

	private static void initDir(String dir) throws IOException {
		File dirAsFile = new File(dir);
		
		if (dirAsFile.exists()) 
			FileUtils.deleteDirectory(dirAsFile);
		
		FileUtils.forceMkdir(dirAsFile);
		
	}

	private static void copyInputFileToRepoDir(String file) throws IOException {
		FileUtils.copyFile(new File("./input", file), new File(repoDir, "products.zip"));
	}

	private static void normalProcessing(JobLauncher jobLauncher, Job job, Calendar calendar) throws Exception {
		initTargetDir();
		initRepoDir();
		
		copyInputFileToRepoDir("products.zip");
		
		launchJob(jobLauncher, job, calendar);
		
	}

	private static void corruptedProcessing(JobLauncher jobLauncher, Job job, Calendar calendar) throws Exception {
		initTargetDir();
		initRepoDir();
		
		copyInputFileToRepoDir("productsWithError.zip");
		
		launchJob(jobLauncher, job, calendar);
		launchJob(jobLauncher, job, calendar);
	}
	
	private static void launchJob(JobLauncher jobLauncher, Job job, Calendar calendar) throws Exception {

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		jobLauncher.run(job, new JobParametersBuilder()
				.addString("inputResource", "file:" + repoDir + "/products.zip")
				.addString("targetDirectory", targetDir + "/importproductbatch/")
				.addString("targetFile", "products.txt")
				.addString("date", dateFormat.format(calendar.getTime()))
				.toJobParameters());
		
	}

	private static void truncate(ApplicationContext ctx) {
		SimpleJdbcTemplate tpl = new SimpleJdbcTemplate((DataSource) ctx.getBean(DataSource.class));
		tpl.update("delete from BATCH_STEP_EXECUTION_CONTEXT");
		tpl.update("delete from BATCH_JOB_EXECUTION_CONTEXT");
		tpl.update("delete from BATCH_STEP_EXECUTION");
		tpl.update("delete from BATCH_JOB_EXECUTION");
		tpl.update("delete from BATCH_JOB_PARAMS");
		tpl.update("delete from BATCH_JOB_INSTANCE");	
	}

}
