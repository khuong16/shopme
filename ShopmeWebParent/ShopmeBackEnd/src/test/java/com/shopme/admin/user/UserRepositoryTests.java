package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	
	@Autowired
	private UserRepository repo;
	
	// Đối tượng TestEntityManager sẽ lấy dữ liệu ở bảng tương ứng để chuyền vào 1 dữ liệu khi cần thiết
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateUser() {
		// sử dụng entityManager để lấy ra dữ liệu tương ứng của bảng "Role" trong db
		Role roleAdmin = entityManager.find(Role.class, 1);
		User userRavi = new User("ravi@gmail.com", "ravi2020", "Ravi", "Kumar");
		userRavi.addRole(roleAdmin);
		
		User savedUser = repo.save(userRavi);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateNewUserWithTwoRoles() {
		User userMK = new User("khuong@gmail.com", "khuong2022", "khuong", "minh");
		
		Role roleEditor = new Role(3);
		Role roleAssitant = new Role(5);
		
		userMK.addRole(roleEditor);
		userMK.addRole(roleAssitant);
		
		User savedUser = repo.save(userMK);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers = repo.findAll();
		listUsers.forEach(user -> System.out.println(user));
	}
	
	@Test
	public void testGetUserBydId() {
		User userRavi = repo.findById(1).get();
		System.out.println(userRavi);
		assertThat(userRavi).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetais() {
		User userRavi = repo.findById(1).get();
		userRavi.setEnabled(true);
		userRavi.setEmail("ravi2021@gmail.com");
		
		repo.save(userRavi);
	}
	
	@Test
	public void testUpdateUserRoles() {
		User userMK = repo.findById(2).get();
		
		Role roleEditor = new Role(3);
		Role roleSalePerson = new Role(2);
		
		userMK.getRoles().remove(roleEditor);
		userMK.addRole(roleSalePerson);
		
		repo.save(userMK);
	}
	
	@Test
	public void testDeleteUser() {
		Integer userId = 2;
		repo.deleteById(userId);
	}
	
	@Test
	public void testGetUserByEmail() {
		String email = "khuong@gmail.com";
		User user = repo.getUserByEmail(email);
		
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testCountById() {
		Integer id = 1;
		Long countById = repo.countById(id);
		
		assertThat(countById).isNotNull().isGreaterThan(0);
	}
	
	@Test
	public void testDisableUser() {
		Integer id = 1;
		repo.updateEnabledStatus(id, false);
	}
	
	@Test
	public void testEnableUser() {
		Integer id = 2;
		repo.updateEnabledStatus(id, true);
	}
	
	@Test
	public void testListFirstPage() {
		int pageNumber = 0;
		int pageSize = 4;
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(pageable);
		
		List<User> listUsers = page.getContent();
		listUsers.forEach(user -> System.out.println(user));
		assertThat(listUsers.size()).isEqualTo(pageSize);
	}
	
	 @Test
	 public void testSearchUsers() {
		 String keyword = "bruce";
		 int pageNumber = 0;
		 int pageSize = 4;
		 
		 Pageable pageable = PageRequest.of(pageNumber, pageSize);
		 Page<User> page = repo.findAll(keyword, pageable);
		 
		 List<User> listUser = page.getContent();
		 listUser.forEach(user -> System.out.println(user));
		 
		 assertThat(listUser.size()).isGreaterThan(0);
		 
	 }
	
}
