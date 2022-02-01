package net.zoostar.hw.service.impl;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import net.zoostar.hw.repository.EntityRepository;
import net.zoostar.hw.service.EntityService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Persistable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Getter;

@Service
@Transactional
public class DefaultEntityService<R extends EntityRepository<E, T>, E extends Persistable<T>, T>
implements EntityService<R, E, T> {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Getter
	@Autowired
	protected R repository;

	@Override
	@Transactional(readOnly = true)
	public E retrieve(String sourceCode, String sourceId) {
		logger.info("Find Entity by Source Code: {} and Source ID: {}", sourceCode, sourceId);
		Optional<E> optional = getRepository().findBySourceCodeAndId(sourceCode, sourceId);
		if(optional.isEmpty()) {
			throw new EntityNotFoundException(String.format("No Entity found for sourceCode: %s and sourceId: %s", sourceCode, sourceId));
		}
		return optional.get();
	}

}
