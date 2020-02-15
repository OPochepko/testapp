package by.pochepko.hes.testapp.service;

import by.pochepko.hes.testapp.dto.UserAccountDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserAccountService {
    List<UserAccountDto> getUserAccountList();

    UserAccountDto createUser(UserAccountDto userAccount);

    UserAccountDto getUserAccountById(long id);

    void updateUserAccount(UserAccountDto userAccount, long id);
}
