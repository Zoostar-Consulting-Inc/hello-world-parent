package net.zoostar.hw.dao.hsql;

import org.springframework.data.repository.PagingAndSortingRepository;

import net.zoostar.hw.model.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, String> {
	Product findBySkuAndSource(String sku, String source);
}
