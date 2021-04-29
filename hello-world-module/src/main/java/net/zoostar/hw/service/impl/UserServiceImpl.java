package net.zoostar.hw.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.zoostar.hw.dao.hsql.UserRepository;
import net.zoostar.hw.exception.EntityAlreadyExistsException;
import net.zoostar.hw.exception.EntityNotFoundException;
import net.zoostar.hw.model.User;
import net.zoostar.hw.service.UserService;
import net.zoostar.hw.validate.MissingRequiredFieldException;
import net.zoostar.hw.validate.Validator;
import net.zoostar.hw.validate.ValidatorException;

@Slf4j
@Getter
@Setter
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository repository;
	
	@Override
	@Transactional
	public User create(User user) throws EntityAlreadyExistsException, ValidatorException {
		log.info("Creating entity: {}...", user);
		
		Validator<User> validator = new Validator<>() {
			@Override
			public void validate(User user) throws ValidatorException {
				log.info("{}...", "Validating user for required field: email");
				log.info("... {} ...", "validating");
				if(!StringUtils.hasText(user.getEmail())) {
					throw new MissingRequiredFieldException("Missing Required Field: email");
				}
				log.info("Validation passed. Email: {}", user.getEmail());
			}
		};
		validator.validate(user);
		
		try {
			user = retrieveByEmail(user.getEmail());
			throw new EntityAlreadyExistsException(user);
		} catch (EntityNotFoundException e) {
			user = getRepository().save(user);
		}

		return user;
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
	public Page<User> retrieve(int number, int limit) {
		log.info("retrieve(number:{}, limit:{})", number, limit);
		
		if(number < 0)
			throw new IllegalArgumentException("Page number may not be less than 0!");
		
		if(limit <= 0)
			throw new IllegalArgumentException("Page size limit has to be greater than 0!");
		
		return getRepository().findAll(PageRequest.of(number, limit));
	}

}
