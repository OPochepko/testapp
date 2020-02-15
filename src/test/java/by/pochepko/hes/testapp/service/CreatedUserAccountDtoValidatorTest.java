package by.pochepko.hes.testapp.service;

import by.pochepko.hes.testapp.TestappApplication;
import by.pochepko.hes.testapp.dto.UserAccountDto;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = TestappApplication.class)
class CreatedUserAccountDtoValidatorTest {
    CreatedUserAccountDtoValidator sut = new CreatedUserAccountDtoValidator();

    @Test
    void validate_givenUserWithInvalidTwoCharactersUsername_ShouldReturnErrorOfValidation() {
        //given
        UserAccountDto invalidUsernameUser = new UserAccountDto(0l, "us", "password1"
                , "firstName", "lastName", "USER", "ACTIVE", null);
        //when
        Errors errors = new BeanPropertyBindingResult(invalidUsernameUser, "invalidUsernameUser");
        sut.validate(invalidUsernameUser, errors);
        //then
        assertThat(errors.getFieldErrors("username").get(0).getCode()).isEqualTo("username.validation");
        assertThat(errors.getAllErrors().size()).isEqualTo(1);
    }

    @Test
    void validate_givenUserWithUsernameIncludeNumerals_ShouldReturnErrorOfValidation() {
        //given
        UserAccountDto invalidUsernameUser = new UserAccountDto(0l, "username12", "password1"
                , "firstName", "lastName", "USER", "ACTIVE", null);
        //when
        Errors errors = new BeanPropertyBindingResult(invalidUsernameUser, "invalidUsernameUser");
        sut.validate(invalidUsernameUser, errors);
        //then
        assertThat(errors.getFieldErrors("username").get(0).getCode()).isEqualTo("username.validation");
        assertThat(errors.getAllErrors().size()).isEqualTo(1);

    }

    @Test
    void validate_givenUserWithSeventeenCharactersInvalidUsername_shouldReturnErrorOfValidation() {
        //given
        UserAccountDto invalidUsernameUser = new UserAccountDto(0l, "usernameUsernameU", "password1"
                , "firstName", "lastName", "USER", "ACTIVE", null);
        //when
        Errors errors = new BeanPropertyBindingResult(invalidUsernameUser, "invalidUsernameUser");
        sut.validate(invalidUsernameUser, errors);
        //then
        assertThat(errors.getFieldErrors("username").get(0).getCode()).isEqualTo("username.validation");
        assertThat(errors.getAllErrors().size()).isEqualTo(1);


    }

    @Test
    void validate_givenUserWithProperUsername_ShouldReturnNoErrors() {
        //given
        UserAccountDto invalidUsernameUser = new UserAccountDto(0l, "username", "password1"
                , "firstName", "lastName", "USER", "ACTIVE", null);
        //when
        Errors errors = new BeanPropertyBindingResult(invalidUsernameUser, "invalidUsernameUser");
        sut.validate(invalidUsernameUser, errors);
        //then
        assertThat(errors.getAllErrors().size()).isEqualTo(0);

    }
}