package net.zoostar.hw.service;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;

import net.zoostar.hw.exception.EntityAlreadyExistsException;
import net.zoostar.hw.exception.EntityNotFoundException;
import net.zoostar.hw.exception.MissingRequiredFieldException;

public interface CrudService<T, E> {
	PagingAndSortingRepository<T, E> getRepository();
	T create(T entity) throws EntityAlreadyExistsException, MissingRequiredFieldException;
	T retrieveById(E id) throws EntityNotFoundException;
	Page<T> retrieve(int number, int limit);
	T update(T entity);
	T delete(E id);
}
