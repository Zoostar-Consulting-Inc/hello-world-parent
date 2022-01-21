package net.zoostar.hw.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Getter;
import net.zoostar.hw.entity.Product;
import net.zoostar.hw.repository.ProductRepository;
import net.zoostar.hw.service.ProductService;

@Getter
@Service
public class ProductServiceImpl extends AbstractEntityService<Product, String>
implements ProductService {

	@Autowired
	protected ProductRepository repository;
	
	@Override
	public Product create(Product entity) {
		return super.create(entity);
	}

}
