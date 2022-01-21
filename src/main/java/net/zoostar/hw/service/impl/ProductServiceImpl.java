package net.zoostar.hw.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import net.zoostar.hw.entity.Product;
import net.zoostar.hw.repository.ProductRepository;
import net.zoostar.hw.service.ProductService;

@Slf4j
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	protected ProductRepository productRepository;
	
	@Override
	public Product create(Product entity) {
		log.info("Creating new entity: {}...", entity);
		return productRepository.save(entity);
	}

}
