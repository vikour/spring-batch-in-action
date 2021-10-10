package es.vikour.sbia.ch01.batch;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;

import es.vikour.sbia.ch01.model.Product;

public class ProductJdbcItemWriter implements ItemWriter<Product> {
	
	private static final String INSERT_PRODUCT = "insert into product " +
			"(id,name,description,price) values (?,?,?,?)";
	
	private static final String UPDATE_PRODUCT = "update product set " +
			" name = ?, description = ?, price = ? where id = ? ";
	
	private JdbcTemplate jdbcTemplate;
	
	
	public ProductJdbcItemWriter(DataSource ds) {
		this.jdbcTemplate = new JdbcTemplate(ds);
	}
	

	public void write(List<? extends Product> items) throws Exception {
		
		for (Product product : items) {
			
			int updated = jdbcTemplate.update(
					UPDATE_PRODUCT,
					product.getName(),
					product.getDescription(),
					product.getPrice(),
					product.getId());
			
			if (updated == 0) 
				jdbcTemplate.update(
					INSERT_PRODUCT,
					product.getId(),
					product.getName(),
					product.getDescription(),
					product.getPrice());
			
		}
		
	}

}
