package es.vikour.sbia.ch01.model;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Product {
	
	private String id;
	private String name;
	private String description;
	private BigDecimal price;

}
