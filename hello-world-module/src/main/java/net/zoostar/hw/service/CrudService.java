package net.zoostar.hw.service;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;

import net.zoostar.hw.exception.EntityAlreadyExistsException;
import net.zoostar.hw.exception.EntityNotFoundException;

public interface CrudService<T, ID> {
	PagingAndSortingRepository<T, ID> getRepository();
	T create(T entity) throws EntityAlreadyExistsException;
	T retrieveById(ID id) throws EntityNotFoundException;
	Page<T> retrieve(int number, int limit);
	T update(T entity);
	T delete(ID id);
}
