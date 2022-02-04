package net.zoostar.hw.entity;

import org.springframework.data.domain.Persistable;

public interface SourceEntity<T> extends Persistable<T> {
	String getSourceCode();
	String getSourceId();
	void setId(T id);
}
