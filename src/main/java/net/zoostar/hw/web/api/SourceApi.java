package net.zoostar.hw.web.api;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.xml.bind.ValidationException;

import net.zoostar.hw.entity.Source;
import net.zoostar.hw.entity.SourceEntity;
import net.zoostar.hw.service.SourceService;
import net.zoostar.hw.web.request.SourceRequest;
import net.zoostar.hw.web.response.PageResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/source")
public class SourceApi<E extends SourceEntity<T>, T> {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	protected SourceService<E, T> sourceManager;
	
	@PostMapping(path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Source> create(@RequestBody SourceRequest request) {
		log.info("Request received to create new entity: {}", request);
		ResponseEntity<Source> response = null;
		try {
			response = new ResponseEntity<>(sourceManager.create(request.toEntity()), HttpStatus.CREATED);
		} catch(EntityExistsException e) {
			log.warn(e.getMessage());
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return response;
	}
	
	@GetMapping(path = "/retrieve/{pageNum}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PageResponse<Source>> retrieve(@PathVariable int pageNum, @RequestParam int size) {
		return new ResponseEntity<>(new PageResponse<>(sourceManager.retrieve(pageNum, size)), HttpStatus.OK);
	}

	@PostMapping(path="/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Source> update(@RequestBody SourceRequest request) {
		log.info("Request received to update existing entity: {}...", request);
		ResponseEntity<Source> response = null;
		try {
			response = new ResponseEntity<>(sourceManager.update(request.toEntity()), HttpStatus.OK);
		} catch(EntityNotFoundException | ValidationException e) {
			log.warn(e.getMessage());
			response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return response;
	}
}
