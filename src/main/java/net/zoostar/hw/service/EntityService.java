package net.zoostar.hw.service;

import net.zoostar.hw.entity.SourceEntity;

public interface EntityService<E extends SourceEntity<T>, T> {
	E create(E entity);
	E retrieve(String sourceCode, String sourceId);
	E update(E entity, E persistable);
	void delete(T id);
}
