package net.zoostar.hw.service.impl;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import net.zoostar.hw.entity.SourceEntity;
import net.zoostar.hw.repository.EntityRepository;
import net.zoostar.hw.service.EntityService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class AbstractEntityService<E extends SourceEntity<T>, T>
implements EntityService<E, T> {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	public abstract EntityRepository<E, T> getRepository();
	
	@Override
	public E create(E entity) {
		log.info("Persisting entity: {}...", entity);
		return getRepository().save(entity);
	}

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
	public E update(E entity, E persistable) {
		log.info("Updating {} with ID: {}", persistable, entity.getId());
		persistable.setId(entity.getId());
		return getRepository().save(persistable);
	}

	@Override
	public void delete(T id) {
		log.info("Deleting entity for id: {}...", id);
		getRepository().deleteById(id);
	}

}
