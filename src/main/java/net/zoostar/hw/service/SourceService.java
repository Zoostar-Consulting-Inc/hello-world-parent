package net.zoostar.hw.service;

import net.zoostar.hw.entity.EntityMapper;

import org.springframework.data.domain.Persistable;
import org.springframework.http.ResponseEntity;

public interface SourceService<E extends Persistable<T>, T> {

	ResponseEntity<EntityMapper<E>> retrieve(String sourceCode, String sourceId);

	ResponseEntity<E> create(String sourceCode, String sourceId);

	ResponseEntity<E> update(E entity);

}
