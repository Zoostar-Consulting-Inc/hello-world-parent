package net.zoostar.hw.service.impl;

import net.zoostar.hw.entity.EntityMapper;
import net.zoostar.hw.entity.Source;
import net.zoostar.hw.repository.SourceRepository;
import net.zoostar.hw.service.EntityService;
import net.zoostar.hw.service.SourceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Persistable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
public class SourceServiceImpl<R extends PagingAndSortingRepository<E, T>, E extends Persistable<T>, T> implements SourceService<R, E, T> {

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
	public ResponseEntity<? extends EntityMapper<E>> retrieve(String sourceCode, String sourceId, Class<? extends EntityMapper<E>> clazz) {
		Source source = retrieve(sourceCode);
		log.info("Retrieve Entity from Source: {}...", sourceCode, sourceId);
		String url = new StringBuilder(source.getBaseUrl()).append(source.getEndPoint()).
				append("?id=").append(sourceId).toString();
		return api.getForEntity(url, clazz);
	}

	@Override
	public E create(String sourceCode, String sourceId, Class<? extends EntityMapper<E>> clazz) {
		var response = retrieve(sourceCode, sourceId, clazz);
		var mapper = response.getBody();
		return entityManager.create(mapper.toEntity());
	}

	@Override
	public ResponseEntity<E> update(E entity) {
		// TODO Auto-generated method stub
		return null;
	}

}
