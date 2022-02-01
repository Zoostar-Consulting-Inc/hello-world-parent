package net.zoostar.hw.service;

import org.springframework.data.domain.Persistable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EntityService<R extends PagingAndSortingRepository<E, T>, E extends Persistable<T>, T> {
	R getRepository();
	E retrieve(String sourceCode, String sourceId);
}
