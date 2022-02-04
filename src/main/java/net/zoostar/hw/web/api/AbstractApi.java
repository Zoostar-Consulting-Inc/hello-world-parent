package net.zoostar.hw.web.api;

import net.zoostar.hw.entity.EntityMapper;
import net.zoostar.hw.entity.SourceEntity;
import net.zoostar.hw.service.EntityService;
import net.zoostar.hw.service.SourceService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Getter;

public abstract class AbstractApi<R extends PagingAndSortingRepository<E, T>, E extends SourceEntity<T>, T> {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Getter
	@Autowired
	private EntityService<R, E, T> entityManager;

	@Getter
	@Autowired
	private SourceService<R, E, T> sourceManager;
	
	protected abstract Class<? extends EntityMapper<E, T>> getEntityMapperClazz();
	
	protected ResponseEntity<E> cudOperation(String sourceCode, String sourceId) {
		sanitizeLogging(sourceCode, sourceId);
		ResponseEntity<E> response = null;
		if(getEntityManager().exists(sourceCode, sourceId)) {
			log.info("Entity exists for source code: {} and source id: {}", sourceCode, sourceId);
			response = new ResponseEntity<>(
					sourceManager.update(sourceCode, sourceId, getEntityMapperClazz()),
					HttpStatus.OK);
		} else {
			response = new ResponseEntity<>(
					getSourceManager().create(sourceCode, sourceId, getEntityMapperClazz()),
					HttpStatus.CREATED);
		}
		log.info("Response: {}", response);
		return response;
	}

	private void sanitizeLogging(String sourceCode, String sourceId) {
		var param1 = sourceCode.replaceAll("[\n\r\t]", "_");
		var param2 = sourceId.replaceAll("[\n\r\t]", "_");
		log.info("Evaluating type of operation for source code {} and source id {}...", param1, param2);
	}
	
}
