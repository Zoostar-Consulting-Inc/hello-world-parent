package net.zoostar.hw.web.api;

import javax.persistence.EntityNotFoundException;

import net.zoostar.hw.service.EntityService;
import net.zoostar.hw.service.SourceService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Persistable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.http.ResponseEntity;

import lombok.Getter;

public abstract class AbstractApi<R extends PagingAndSortingRepository<E, T>, E extends Persistable<T>, T> {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Getter
	@Autowired
	private EntityService<R, E, T> entityManager;

	@Getter
	@Autowired
	private SourceService<E, T> sourceManager;
	
	protected ResponseEntity<E> cudOperation(String sourceCode, String sourceId) {
		log.info("Evaluating type of operation for source code {} and source id {}...", sourceCode, sourceId);
		ResponseEntity<E> response = null;
		try {
			var entity = getEntityManager().retrieve(sourceCode, sourceId);
			log.info("Entity retrieved: {}", entity);
			response = sourceManager.update(entity);
		} catch(EntityNotFoundException e) {
			log.info("{}", e.getMessage());
			response = getSourceManager().create(sourceCode, sourceId);
		}
		log.info("Response: {}", response);
		return response;
	}
	
}
