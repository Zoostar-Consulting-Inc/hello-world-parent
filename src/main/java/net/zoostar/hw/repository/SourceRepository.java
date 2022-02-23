package net.zoostar.hw.repository;

import java.util.Optional;

import net.zoostar.hw.entity.Source;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface SourceRepository extends PagingAndSortingRepository<Source, String> {
	Optional<Source> findBySourceCode(String sourceCode);
}
