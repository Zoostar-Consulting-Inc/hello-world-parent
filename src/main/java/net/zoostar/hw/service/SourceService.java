package net.zoostar.hw.service;

import javax.xml.bind.ValidationException;

import net.zoostar.hw.entity.Source;
import net.zoostar.hw.entity.SourceEntity;
import net.zoostar.hw.entity.SourceEntityMapper;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface SourceService<E extends SourceEntity<T>, T> {
    
    String GZIP = "gzip";
    String CONTENT_ENCODING = "Content-Encoding";
	String ACCEPT_ENCODING = "Accept-Encoding";
	String PROTOCOL = "https://";

	EntityService<E, T> getEntityManager();
	
	Source create(Source source);
	
	Source retrieve(String sourceCode);

	Source update(Source source) throws ValidationException;
	
	SourceEntityMapper<E, T> retrieve(String sourceCode, String sourceId,
			String endPoint, Class<? extends SourceEntityMapper<E, T>> clazz);

	Page<Source> retrieve(int pageNum, int pageSize);

	E create(String sourceCode, String sourceId,
			String endPoint, Class<? extends SourceEntityMapper<E, T>> clazz);

	ResponseEntity<E> update(E entity, String endPoint, Class<? extends SourceEntityMapper<E, T>> clazz);

}
