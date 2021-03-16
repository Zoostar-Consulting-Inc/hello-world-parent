package net.zoostar.hw.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.zoostar.hw.dao.hsql.ProductRepository;
import net.zoostar.hw.exception.EntityAlreadyExistsException;
import net.zoostar.hw.exception.EntityNotFoundException;
import net.zoostar.hw.model.Product;
import net.zoostar.hw.service.ProductService;

@Slf4j
@Getter
@Setter
@ToString
@NoArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	protected ProductRepository repository;
	
	public ProductServiceImpl(ProductRepository repository) {
		this.repository = repository;
	}

	@Override
	public Product create(Product product) throws EntityAlreadyExistsException {
		log.info("Creating entity: {}...", product);
		Product entity = null;
		
		if(!product.isNew()) {
			throw new EntityAlreadyExistsException(product);
		}

		try {
			entity = retrieve(product.getAssetId(), product.getSku(), product.getSource());
			if(entity != null) {
				throw new EntityAlreadyExistsException(entity);
			}
		} catch (EntityNotFoundException e) {
			entity = getRepository().save(product);
		}

		return entity;
	}

	@Override
	public Product retrieve(String id) throws EntityNotFoundException {
		log.info("Return record by id: {}", id);
		Optional<Product> product = repository.findById(id);
		if(!product.isPresent()) {
			throw new EntityNotFoundException("No Product found for id: " + id);
		}
		return product.get();
	}

	@Override
	public Page<Product> retrieve(int number, int limit) {
		log.info("retrieve(number:{}, limit:{})", number, limit);
		
		if(number < 0)
			throw new IllegalArgumentException("Page number may not be less than 0!");
		
		if(limit <= 0)
			throw new IllegalArgumentException("Page size limit has to be greater than 0!");
		
		return getRepository().findAll(PageRequest.of(number, limit));
	}

	@Override
	public Product update(Product entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Product delete(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Product retrieve(String assetId, String sku, String source) throws EntityNotFoundException {
		log.info("Retrieving Product for assetId {}, sku {} and source {}", assetId, sku, source);
		Product product = getRepository().findByAssetIdAndSkuAndSource(assetId, sku, source);
		if(product == null) {
			throw new EntityNotFoundException(String.format("No Product found for sku %s and source %s", sku, source));
		}
		return product;
	}

}
