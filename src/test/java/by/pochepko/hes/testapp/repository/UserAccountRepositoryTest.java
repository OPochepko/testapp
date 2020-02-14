package by.pochepko.hes.testapp.repository;

import by.pochepko.hes.testapp.TestappApplication;
import by.pochepko.hes.testapp.model.UserAccount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TestappApplication.class)
class UserAccountRepositoryTest {
    @Autowired
    UserAccountRepository sut;

    @Test
    void userAccountRepositoryShouldSaveNewEntity() {
        //given
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername("TestUser");
        userAccount.setPassword("TestPassword");
        userAccount.setFirstName("Test");
        userAccount.setLastName("Test");
        userAccount.setRole(UserAccount.Role.USER);
        userAccount.setStatus(UserAccount.Status.INACTIVE);
//   whenthen
        assertThat(sut.save(userAccount)).isEqualTo(userAccount);

    }

}