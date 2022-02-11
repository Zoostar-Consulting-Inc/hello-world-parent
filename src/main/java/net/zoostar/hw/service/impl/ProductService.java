package net.zoostar.hw.service.impl;

import net.zoostar.hw.entity.Product;
import net.zoostar.hw.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Getter;

@Service
public class ProductService extends AbstractEntityService<Product, String> {

	@Getter
	@Autowired
	protected ProductRepository repository;

}
