package net.zoostar.hw.service.impl;

import javax.persistence.EntityNotFoundException;

import net.zoostar.hw.entity.EntityMapper;
import net.zoostar.hw.entity.Source;
import net.zoostar.hw.entity.SourceEntity;
import net.zoostar.hw.repository.SourceRepository;
import net.zoostar.hw.service.EntityService;
import net.zoostar.hw.service.SourceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class SourceServiceImpl<R extends PagingAndSortingRepository<E, T>, E extends SourceEntity<T>, T>
implements SourceService<R, E, T> {

	@Autowired
	protected SourceRepository repository;
	
	@Getter
	@Autowired
	protected EntityService<R, E, T> entityManager;
	
	@Autowired
	protected RestTemplate api;

	@Override
	@Transactional(readOnly = true)
	public Source retrieve(String sourceCode) {
		log.info("Find Entity by Source Code: {}...", sourceCode);
		var entity = repository.findBySourceCode(sourceCode);
		if(entity.isEmpty()) {
			throw new EntityNotFoundException(String.format("No entity found for source code %s", sourceCode));
		}
		return entity.get();
	}
	
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<? extends EntityMapper<E, T>> retrieve(String sourceCode, String sourceId, Class<? extends EntityMapper<E, T>> clazz) {
		Source source = retrieve(sourceCode);
		log.info("Retrieved Entity from Repository: {}", source);
		String url = new StringBuilder(source.getBaseUrl()).append(source.getEndPoint()).
				append("?id=").append(sourceId).toString();
		log.info("Fetching entity from source: {}...", url);
		var response = api.getForEntity(url, clazz);
		if(response.getStatusCode() != HttpStatus.OK && response.getBody() == null) {
			throw new EntityNotFoundException(String.format("No entity found for source code %s and source id %s", sourceCode, sourceId));
		}
		return response;
	}

	@Override
	public E create(String sourceCode, String sourceId, Class<? extends EntityMapper<E, T>> clazz) {
		var response = retrieve(sourceCode, sourceId, clazz);
		log.info("Retrieved response from Source: {}", response);
		var persistable = response.getBody();
		log.info("Retrieved Entity from Source: {}", persistable);
		return getEntityManager().create(persistable.toEntity());
	}

	@Override
	public E update(String sourceCode, String sourceId, Class<? extends EntityMapper<E, T>> clazz) {
		var response = retrieve(sourceCode, sourceId, clazz);
		log.info("Retrieved response from Source: {}", response);
		var persistable = response.getBody();
		log.info("Retrieved Entity from Source: {}", persistable);
		return getEntityManager().update(persistable.toEntity());
	}

}
