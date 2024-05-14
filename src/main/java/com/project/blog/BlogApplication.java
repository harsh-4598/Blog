package com.project.blog;

import com.project.blog.entities.Role;
import com.project.blog.repositories.RoleRepo;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEncryptableProperties
public class BlogApplication implements CommandLineRunner {
	@Autowired
	private RoleRepo roleRepo;

	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Override
	public void run(String... args) throws Exception {
		Role roleAdmin = roleRepo.findByRole("ROLE_ADMIN");
		if (roleAdmin ==  null) {
			roleAdmin = new Role();
			roleAdmin.setRole("ROLE_ADMIN");
			roleRepo.save(roleAdmin);
		}
		Role roleUser = roleRepo.findByRole("ROLE_USER");
		if (roleUser ==  null) {
			roleUser = new Role();
			roleUser.setRole("ROLE_USER");
			roleRepo.save(roleUser);
		}
	}
}
