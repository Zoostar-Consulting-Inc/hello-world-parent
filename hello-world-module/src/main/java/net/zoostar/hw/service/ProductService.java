package net.zoostar.hw.service;

import net.zoostar.hw.dao.hsql.ProductRepository;
import net.zoostar.hw.exception.EntityNotFoundException;
import net.zoostar.hw.model.Product;

public interface ProductService extends CrudService<Product, String> {
	void setRepository(ProductRepository repository);
	ProductRepository getRepository();
	Product retrieve(String assetId, String sku, String source) throws EntityNotFoundException;
}
