package es.vikour.sbia.ch01.batch;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import es.vikour.sbia.ch01.model.Product;

public class ProductFieldSetMapper implements FieldSetMapper<Product> {

	public Product mapFieldSet(FieldSet fieldSet) throws BindException {
		return Product.builder()
				.id(fieldSet.readString("PRODUCT_ID"))
				.name(fieldSet.readString("NAME"))
				.description(fieldSet.readString("DESCRIPTION"))
				.price(fieldSet.readBigDecimal("PRICE"))
				.build();
	}
	
}
