package net.zoostar.hw.service;

import net.zoostar.hw.dao.hsql.ProductRepository;
import net.zoostar.hw.model.Product;

public interface ProductService {

	ProductRepository getRepository();
	
	Product create(Product entity);

}
