package net.zoostar.hw.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.zoostar.hw.dao.hsql.UserRepository;
import net.zoostar.hw.exception.EntityAlreadyExistsException;
import net.zoostar.hw.exception.EntityNotFoundException;
import net.zoostar.hw.exception.MissingRequiredFieldException;
import net.zoostar.hw.model.User;
import net.zoostar.hw.service.UserService;

@Slf4j
@Getter
@Setter
@ToString
@NoArgsConstructor
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository repository;
	
	public UserServiceImpl(UserRepository repository) {
		this.repository = repository;
	}

	@Override
	@Transactional
	public User create(User user) throws EntityAlreadyExistsException, MissingRequiredFieldException {
		log.info("Creating entity: {}...", user);
		User entity = null;
		
		if(!user.isNew()) {
			throw new EntityAlreadyExistsException(user);
		}

		if(!StringUtils.hasText(user.getEmail())) {
			throw new MissingRequiredFieldException("Missing Required Field: email");
		}
		
		try {
			entity = retrieveByEmail(user.getEmail());
			if(entity != null) {
				throw new EntityAlreadyExistsException(entity);
			}
		} catch (EntityNotFoundException e) {
			entity = getRepository().save(user);
		}

		return entity;
	}

	@Override
	@Transactional(readOnly = true)
	public User retrieveByEmail(String email) throws EntityNotFoundException {
		if(!StringUtils.hasText(email)) {
			throw new EntityNotFoundException(email);
		}
		
		log.info("Retrieve by email: {}...", email);
		User entity = getRepository().findByEmail(email);
		if(entity == null) {
			throw new EntityNotFoundException(email);
		}
		
		return entity;
	}

	@Override
	@Transactional(readOnly = true)
	public User retrieveById(String id) throws EntityNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<User> retrieve(int number, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public User update(User entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public User delete(String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
