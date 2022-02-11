package net.zoostar.hw.entity;

import org.springframework.data.domain.Persistable;

public interface SourceEntity<T> extends Persistable<T> {
	void setId(T id);
	String getSourceCode();
	String getSourceId();
}
