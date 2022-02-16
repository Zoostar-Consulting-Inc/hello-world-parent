package net.zoostar.hw.entity;

import org.springframework.data.domain.Persistable;

public interface PersistableEntityMapper<E extends Persistable<T>, T> {
	E toEntity();
}
