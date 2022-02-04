package net.zoostar.hw.service.impl;

import net.zoostar.hw.entity.EntityMapper;
import net.zoostar.hw.entity.Source;
import net.zoostar.hw.entity.SourceEntity;
import net.zoostar.hw.repository.SourceRepository;
import net.zoostar.hw.service.EntityService;
import net.zoostar.hw.service.SourceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class SourceServiceImpl<R extends PagingAndSortingRepository<E, T>, E extends SourceEntity<T>, T> implements SourceService<R, E, T> {

	@Autowired
	protected SourceRepository repository;
	
	@Autowired
	protected EntityService<R, E, T> entityManager;
	
	@Autowired
	protected RestTemplate api;

	@Override
	@Transactional(readOnly = true)
	public Source retrieve(String sourceCode) {
		log.info("Find Entity by Source Code: {}...", sourceCode);
		var exists = repository.findBySourceCode(sourceCode);
		return exists.get();
	}
	
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<? extends EntityMapper<E, T>> retrieve(String sourceCode, String sourceId, Class<? extends EntityMapper<E, T>> clazz) {
		Source source = retrieve(sourceCode);
		log.info("Retrieved Entity from Repository: {}", source);
		String url = new StringBuilder(source.getBaseUrl()).append(source.getEndPoint()).
				append("?id=").append(sourceId).toString();
		log.info("Fetching entity from source: {}...", url);
		return api.getForEntity(url, clazz);
	}

	@Override
	public E create(String sourceCode, String sourceId, Class<? extends EntityMapper<E, T>> clazz) {
		var response = retrieve(sourceCode, sourceId, clazz);
		log.info("Retrieved response from Source: {}", response);
		var mapper = response.getBody();
		log.info("Retrieved Entity from Source: {}", mapper);
		return entityManager.create(mapper.toEntity());
	}

	@Override
	public E update(String sourceCode, String sourceId, Class<? extends EntityMapper<E, T>> clazz) {
		var response = retrieve(sourceCode, sourceId, clazz);
		log.info("Retrieved response from Source: {}", response);
		var persistable = response.getBody().toEntity();
		log.info("Retrieved Entity from Source: {}", persistable);
		return entityManager.update(persistable);
	}

}
