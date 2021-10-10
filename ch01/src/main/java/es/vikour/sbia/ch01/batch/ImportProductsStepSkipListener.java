package es.vikour.sbia.ch01.batch;

import org.springframework.batch.core.SkipListener;

import es.vikour.sbia.ch01.model.Product;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ImportProductsStepSkipListener implements SkipListener<Product, Product> {

	@Override
	public void onSkipInRead(Throwable t) {
		log.info("READ SKIPPED: {}", t.getMessage());
	}

	@Override
	public void onSkipInWrite(Product item, Throwable t) {
		// Do nothing
	}

	@Override
	public void onSkipInProcess(Product item, Throwable t) {
		// Do nothing
	}

}
