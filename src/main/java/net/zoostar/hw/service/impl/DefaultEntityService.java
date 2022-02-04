package net.zoostar.hw.service.impl;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import net.zoostar.hw.entity.SourceEntity;
import net.zoostar.hw.repository.EntityRepository;
import net.zoostar.hw.service.EntityService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Getter;

@Service
@Transactional
public class DefaultEntityService<R extends EntityRepository<E, T>, E extends SourceEntity<T>, T>
implements EntityService<R, E, T> {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Getter
	@Autowired
	protected R repository;

	@Override
	@Transactional(readOnly = true)
	public E retrieve(String sourceCode, String sourceId) {
		log.info("Find Entity by Source Code: {} and Source ID: {}", sourceCode, sourceId);
		Optional<E> optional = getRepository().findBySourceCodeAndId(sourceCode, sourceId);
		if(optional.isEmpty()) {
			throw new EntityNotFoundException(String.format("No Entity found for sourceCode: %s and sourceId: %s", sourceCode, sourceId));
		}
		return optional.get();
	}

	@Override
	public E create(E entity) {
		log.info("Persisting entity: {}...", entity);
		return repository.save(entity);
	}

	@Override
	public boolean exists(String sourceCode, String sourceId) {
		return repository.existsBySourceCodeAndId(sourceCode, sourceId);
	}

	@Override
	public E update(E persistable) {
		var entity = retrieve(persistable.getSourceCode(), persistable.getSourceId());
		persistable.setId(entity.getId());
		return repository.save(persistable);
	}

}
