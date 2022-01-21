package net.zoostar.hw.service;

import org.springframework.data.domain.Persistable;

public interface EntityService<E extends Persistable<T>, T> {
	E create(E entity);
}
