package net.zoostar.hw.service;

import net.zoostar.hw.entity.EntityMapper;
import net.zoostar.hw.entity.Source;
import net.zoostar.hw.entity.SourceEntity;

import org.springframework.http.ResponseEntity;

public interface SourceService<E extends SourceEntity<T>, T> {
    
    String GZIP = "gzip";
    String CONTENT_ENCODING = "Content-Encoding";
	String ACCEPT_ENCODING = "Accept-Encoding";

	EntityService<E, T> getEntityManager();
	
	Source retrieve(String sourceCode);

	EntityMapper<E, T> retrieve(String sourceCode, String sourceId, Class<? extends EntityMapper<E, T>> clazz);

	E create(String sourceCode, String sourceId, Class<? extends EntityMapper<E, T>> clazz);

	ResponseEntity<E> update(E entity, Class<? extends EntityMapper<E, T>> clazz);

}
