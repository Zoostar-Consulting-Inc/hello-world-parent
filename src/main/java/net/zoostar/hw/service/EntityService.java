package net.zoostar.hw.service;

import net.zoostar.hw.entity.SourceEntity;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface EntityService<R extends PagingAndSortingRepository<E, T>, E extends SourceEntity<T>, T> {
	R getRepository();
	boolean exists(String sourceCode, String sourceId);
	E retrieve(String sourceCode, String sourceId);
	E create(E entity);
	E update(E persistable);
}
