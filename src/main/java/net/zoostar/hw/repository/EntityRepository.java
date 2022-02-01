package net.zoostar.hw.repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface EntityRepository<E, T> extends PagingAndSortingRepository<E, T> {
	Optional<E> findBySourceCodeAndId(String sourceCode, String sourceId);
}
