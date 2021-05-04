package net.zoostar.hw.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Getter;
import net.zoostar.hw.dao.hsql.ProductRepository;
import net.zoostar.hw.model.Product;
import net.zoostar.hw.service.ProductService;

@Getter
@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	protected ProductRepository repository;

	@Override
	public Product create(Product entity) {
		return repository.save(entity);
	}

}
