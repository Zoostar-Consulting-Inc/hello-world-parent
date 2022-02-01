package net.zoostar.hw.service;

import net.zoostar.hw.entity.EntityMapper;
import net.zoostar.hw.entity.Source;

import org.springframework.data.domain.Persistable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.http.ResponseEntity;

public interface SourceService<R extends PagingAndSortingRepository<E, T>, E extends Persistable<T>, T> {

	Source retrieve(String sourceCode);

	ResponseEntity<? extends EntityMapper<E>> retrieve(String sourceCode, String sourceId, Class<? extends EntityMapper<E>> clazz);

	E create(String sourceCode, String sourceId, Class<? extends EntityMapper<E>> clazz);

	ResponseEntity<E> update(E entity);

}
