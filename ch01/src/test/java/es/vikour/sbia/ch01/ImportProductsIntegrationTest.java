package es.vikour.sbia.ch01;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.List;

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

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"/import-products-job-context.xml",
	"/test-context.xml"
})
@Log4j2
public class ImportProductsIntegrationTest {
	
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
			 .addString("inputResource", "classpath:/input/products.zip")
			 .addString("targetDirectory", "./target/importproductbatch/")
			 .addString("targetFile", "products.txt")
			 .toJobParameters());
		
		assertEquals(8,jdbcTemplate.queryForInt("select count(1) from product"));
		
		jdbcTemplate.query("select * from product", 
				(rs, rownum) -> 	
				    Product.builder()
				    .id(rs.getString("id"))
				    .name(rs.getString("name"))
				    .description(rs.getString("description"))
				    .price(BigDecimal.valueOf(rs.getDouble("price")))
				    .build()
				 ).stream().forEach((p) -> log.info(p.toString()));
				    
	}
	

}
