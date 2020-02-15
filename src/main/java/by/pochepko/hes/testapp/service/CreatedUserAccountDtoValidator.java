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


        if (!user.getUsername().matches("^[a-zA-Z]{3,16}$")) {
            errors.rejectValue("username", "username.validation");
        }

        if (!user.getPassword().matches("^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]{3,16})$")) {
            errors.rejectValue("password", "password.validation");
        }

        if (user.getFirstName().length() < 1 || user.getFirstName().length() > 16 || !user.getFirstName().matches("^[a-zA-Z]{1,16}$")) {
            errors.rejectValue("firstName", "firstName.validation");
        }

        if (user.getLastName().length() < 1 || user.getFirstName().length() > 16 || !user.getFirstName().matches("^[a-zA-Z]{1,16}$")) {
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

}
