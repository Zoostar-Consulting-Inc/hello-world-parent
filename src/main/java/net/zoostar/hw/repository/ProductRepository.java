package net.zoostar.hw.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import net.zoostar.hw.entity.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, String> {

}
