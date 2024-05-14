package com.project.blog.repositories;

import com.project.blog.entities.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class UserRepoTest {
    @Autowired
    private UserRepo userRepo;

    @Test
    void findUserByEmailTest() {
        User user = this.userRepo.findByEmail("spidy@gmail.com");
        assertThat(user.getName()).isEqualTo("Harsh Panchal");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "abc@gmail.com",
            "@xyz@gmail.com"
    })
    void findByEmailNotFoundTest(String email) {
        User user = this.userRepo.findByEmail(email);
        assertThat(user == null).isTrue();
        //assertThat(user).isNull();
    }
}