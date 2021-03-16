package net.zoostar.hw.dao.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import net.zoostar.hw.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {
	Product findBySkuAndSource(String sku, String source);
}
