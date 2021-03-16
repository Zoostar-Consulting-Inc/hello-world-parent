package net.zoostar.hw.exception;

import lombok.Getter;

@Getter
public class EntityAlreadyExistsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Object entity;
	
	public EntityAlreadyExistsException(Object entity) {
		this.entity = entity;
	}
}
