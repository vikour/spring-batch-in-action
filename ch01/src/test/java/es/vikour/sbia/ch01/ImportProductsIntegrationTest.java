package es.vikour.sbia.ch01;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"/import-products-job-context.xml",
	"/test-context.xml"
})
public class ImportProductsIntegrationTest {
	
	private static final String INPUT_FILE = 
			"classpath:/input/products.zip";
	
	private static final String INPUT_ERROR_FILE = 
			"classpath:/input/productsWithError.zip";
	
	private static final String TARGET_DIRECTORY = 
			"./target/importproductbatch/";
	
	private static final String TARGET_FILE = 
			"products.txt";

	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private Job job;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Before
	public void cleanUp() {
		jdbcTemplate.update("delete from product");
	}
	
	@Test
	public void importProducts() throws Exception {
		
		jobLauncher.run(
			job, new JobParametersBuilder()
			 .addString("inputResource", INPUT_FILE)
			 .addString("targetDirectory", TARGET_DIRECTORY)
			 .addString("targetFile", TARGET_FILE)
			 .toJobParameters());
		
		assertEquals(8,jdbcTemplate.queryForInt("select count(1) from product"));
		assertTargetDirectoryDoesntExists();
	}
	
	@Test
	public void importProductsWithErrors() throws Exception {
		
		jobLauncher.run(
			job, new JobParametersBuilder()
			 .addString("inputResource", INPUT_ERROR_FILE)
			 .addString("targetDirectory", TARGET_DIRECTORY)
			 .addString("targetFile", TARGET_FILE)
			 .toJobParameters());
		
		// Assertions
		assertEquals(7,jdbcTemplate.queryForInt("select count(1) from product"));
		assertTargetDirectoryDoesntExists();
	}
	
	private void assertTargetDirectoryDoesntExists() {
		File targetDirectory = new File(TARGET_DIRECTORY);
		assertFalse(targetDirectory.exists());
	}
	

}
