package net.zoostar.hw.dao.hsql;

import org.springframework.data.repository.PagingAndSortingRepository;

import net.zoostar.hw.model.User;

public interface UserRepository extends PagingAndSortingRepository<User, String> {
	User findByEmail(String email);
}
