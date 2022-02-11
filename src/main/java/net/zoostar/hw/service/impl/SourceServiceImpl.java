package net.zoostar.hw.service.impl;

import javax.persistence.EntityNotFoundException;

import net.zoostar.hw.entity.EntityMapper;
import net.zoostar.hw.entity.Source;
import net.zoostar.hw.entity.SourceEntity;
import net.zoostar.hw.repository.SourceRepository;
import net.zoostar.hw.service.EntityService;
import net.zoostar.hw.service.SourceService;

import org.springframework.beans.factory.annotation.Autowired;
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
public class SourceServiceImpl<E extends SourceEntity<T>, T>
implements SourceService<E, T> {

	@Autowired
	protected SourceRepository repository;
	
	@Getter
	@Autowired
	protected EntityService<E, T> entityManager;
	
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
	public EntityMapper<E, T> retrieve(String sourceCode, String sourceId, Class<? extends EntityMapper<E, T>> clazz) {
		Source source = retrieve(sourceCode);
		log.info("Retrieved Entity from Repository: {}", source);
		String url = new StringBuilder(source.getBaseUrl()).append(source.getEndPoint()).
				append("?id=").append(sourceId).toString();
		log.info("Fetching entity from source: {}...", url);
		var response = api.getForEntity(url, clazz);
		if(response.getStatusCode() != HttpStatus.OK && response.getBody() == null) {
			throw new EntityNotFoundException(String.format("No entity found for source code %s and source id %s", sourceCode, sourceId));
		}
		log.info("Retrieved response from Source: {}", response);
		return response.getBody();
	}

	@Override
	public E create(String sourceCode, String sourceId, Class<? extends EntityMapper<E, T>> clazz) {
		var persistable = retrieve(sourceCode, sourceId, clazz);
		log.info("Retrieved Entity from Source: {}", persistable);
		return getEntityManager().create(persistable.toEntity());
	}

	@Override
	public ResponseEntity<E> update(E entity, Class<? extends EntityMapper<E, T>> clazz) {
		ResponseEntity<E> response = null;
		try {
			var persistable = retrieve(entity.getSourceCode(), entity.getSourceId(), clazz);
			log.info("Retrieved Entity from Source: {}", persistable);
			response = new ResponseEntity<>(getEntityManager().update(entity, persistable.toEntity()), HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			log.info("{}", "Deleting Entity not found in source...");
			getEntityManager().delete(entity.getId());
			response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return response;
	}

}
