package by.pochepko.hes.testapp.repository;

import by.pochepko.hes.testapp.TestappApplication;
import by.pochepko.hes.testapp.model.UserAccount;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = TestappApplication.class)
class UserAccountRepositoryTest {
    @Autowired
    UserAccountRepository sut;
    @Autowired
    Flyway flyway;

    @BeforeEach
    public void init() {
        flyway.clean();
        flyway.migrate();
    }
//    @AfterEach
//    public void finish(){
//        flyway.clean();
//        flyway.migrate();
//    }


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

    @Test
    void userAccountRepositoryUsernameShouldBeUniq() {
        //given
        UserAccount firstUserAccount = new UserAccount();
        firstUserAccount.setUsername("TestUser");
        firstUserAccount.setPassword("TestPassword");
        firstUserAccount.setFirstName("Test");
        firstUserAccount.setLastName("Test");
        firstUserAccount.setRole(UserAccount.Role.USER);
        firstUserAccount.setStatus(UserAccount.Status.INACTIVE);

        UserAccount secondUserAccount = new UserAccount();
        secondUserAccount.setUsername("TestUser");
        secondUserAccount.setPassword("TestPassword");
        secondUserAccount.setFirstName("Testt");
        secondUserAccount.setLastName("Testt");
        secondUserAccount.setRole(UserAccount.Role.USER);
        secondUserAccount.setStatus(UserAccount.Status.INACTIVE);

//   when
        sut.save(firstUserAccount);
////   then
        assertThatThrownBy(() -> sut.save(secondUserAccount))
                .isInstanceOf(Exception.class);

    }


}