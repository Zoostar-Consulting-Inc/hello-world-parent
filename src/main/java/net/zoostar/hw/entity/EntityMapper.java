package net.zoostar.hw.entity;

public interface EntityMapper<E extends SourceEntity<T>, T> {
	E toEntity();
}
