package net.zoostar.hw.repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface EntityRepository<T, ID> extends PagingAndSortingRepository<T, ID> {
	Optional<T> findBySourceCodeAndId(String sourceCode, String sourceId);
}
