package by.pochepko.hes.testapp.service;

import by.pochepko.hes.testapp.dto.UserAccountDto;
import by.pochepko.hes.testapp.model.UserAccount;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class CreatedUserAccountDtoValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return UserAccountDto.class.equals(clazz);
    }


    @Override
    public void validate(Object target, Errors errors) {

        UserAccountDto user = (UserAccountDto) target;


        if (user.getId() != 0l) {
            errors.rejectValue("id", "value.notnull");
        }
        if (!user.getUsername().matches("^[a-zA-Z]{3,16}$")) {
            errors.rejectValue("username", "username.validation");
        }

        if (!user.getPassword().matches("^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]{3,16})$")) {
            errors.rejectValue("password", "password.validation", "Should be 3 to 16 characters and contain only letters or numerals");
        }

        if (user.getFirstName().length() < 1 || user.getFirstName().length() > 16 || !user.getFirstName().matches("[a-zA-Z]+\\.?")) {
            errors.rejectValue("firstName", "firstName.validation");
        }

        if (user.getLastName().length() < 1 || user.getFirstName().length() > 16 || !user.getFirstName().matches("[a-zA-Z]+\\.?")) {
            errors.rejectValue("LastName", "lastName.validation");
        }

        if (user.getRole() == null || !Arrays.stream(UserAccount.Role.values()).map(Enum::name).collect(Collectors.toList()).contains(user.getRole())) {
            errors.rejectValue("role", "role.validation");
        }

        if (user.getStatus() == null || !Arrays.stream(UserAccount.Status.values()).map(Enum::name).collect(Collectors.toList()).contains(user.getStatus())) {
            errors.rejectValue("status", "status.validation");
        }

        if (user.getCreatedAt() != null) {
            errors.rejectValue("createdAt", "");
        }


    }

    public void validateUsername(Object target, Errors errors) {
    }
}
