package net.zoostar.hw.service;

import javax.xml.bind.ValidationException;

public interface Validator<T> {
	
	default void validate() throws ValidationException{}
	
	default void validate(T object) throws ValidationException{}
}
