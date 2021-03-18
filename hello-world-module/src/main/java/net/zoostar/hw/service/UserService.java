package net.zoostar.hw.service;

import net.zoostar.hw.dao.hsql.UserRepository;
import net.zoostar.hw.exception.EntityNotFoundException;
import net.zoostar.hw.model.User;

public interface UserService extends CrudService<User, String> {
	void setRepository(UserRepository repository);
	UserRepository getRepository();
	User retrieveByEmail(String email) throws EntityNotFoundException;
}
