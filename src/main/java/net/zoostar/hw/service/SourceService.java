package net.zoostar.hw.service;

import net.zoostar.hw.entity.EntityMapper;
import net.zoostar.hw.entity.Source;
import net.zoostar.hw.entity.SourceEntity;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.http.ResponseEntity;

public interface SourceService<R extends PagingAndSortingRepository<E, T>, E extends SourceEntity<T>, T> {

	Source retrieve(String sourceCode);

	ResponseEntity<? extends EntityMapper<E, T>> retrieve(String sourceCode, String sourceId, Class<? extends EntityMapper<E, T>> clazz);

	E create(String sourceCode, String sourceId, Class<? extends EntityMapper<E, T>> clazz);

	E update(String sourceCode, String sourceId, Class<? extends EntityMapper<E, T>> clazz);

}
