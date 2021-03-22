package net.zoostar.hw.validate;

public interface Validator<T> {
	void validate(T object) throws ValidatorException;
}
