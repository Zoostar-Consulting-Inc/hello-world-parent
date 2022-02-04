package net.zoostar.hw.service;

import net.zoostar.hw.entity.EntityMapper;
import net.zoostar.hw.entity.Source;
import net.zoostar.hw.entity.SourceEntity;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface SourceService<R extends PagingAndSortingRepository<E, T>, E extends SourceEntity<T>, T> {

	EntityService<R, E, T> getEntityManager();
	
	Source retrieve(String sourceCode);

	EntityMapper<E, T> retrieve(String sourceCode, String sourceId, Class<? extends EntityMapper<E, T>> clazz);

	E create(String sourceCode, String sourceId, Class<? extends EntityMapper<E, T>> clazz);

	E update(String sourceCode, String sourceId, Class<? extends EntityMapper<E, T>> clazz);

}
