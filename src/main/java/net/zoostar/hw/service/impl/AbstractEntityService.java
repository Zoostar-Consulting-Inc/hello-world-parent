package net.zoostar.hw.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Persistable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import net.zoostar.hw.service.EntityService;

@Transactional
public abstract class AbstractEntityService<E extends Persistable<T>, T>
implements EntityService<E, T> {
	
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	protected abstract PagingAndSortingRepository<E, T> getRepository();

	public E create(E entity) {
		log.info("Creating new entity: {}...", entity);
		return getRepository().save(entity);
	}

}
