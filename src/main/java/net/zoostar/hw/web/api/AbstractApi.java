package net.zoostar.hw.web.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Persistable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import net.zoostar.hw.entity.EntityMapper;
import net.zoostar.hw.service.EntityService;

public abstract class AbstractApi<E extends Persistable<T>, T> {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	protected abstract EntityService<E, T> getEntityManager();

	protected ResponseEntity<E> create(EntityMapper<E> request) {
		log.info("Creating entity from request: {}", request);
		return new ResponseEntity<>(getEntityManager().
				create(request.toEntity()), HttpStatus.CREATED);
	}
}
