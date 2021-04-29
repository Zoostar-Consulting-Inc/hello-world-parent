package net.zoostar.hw.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class UserTest {

	@Test
	void testIsNew() {
		User user = new User();
		assertTrue(user.isNew());
	}
	
	@Test
	void testIsNotNew() {
		User user = new User();
		user.setId(UUID.randomUUID().toString());
		assertFalse(user.isNew());
	}

}
