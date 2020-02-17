package by.pochepko.hes.testapp.service;

import by.pochepko.hes.testapp.dto.UserAccountDto;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Interface provide CRUD methods for UserAccountDto
 */

@Service
public interface UserAccountService {

    /**
     * @param userAccount must not be null
     * @return created entity
     */
    UserAccountDto createUser(UserAccountDto userAccount);

    /**
     * @param id must not be null
     * @return the entity with the given id
     */

    UserAccountDto getUserAccountById(long id);

    /**
     * @param userAccount must not be null
     * @param id          must not be null
     */

    void updateUserAccount(UserAccountDto userAccount, long id);


    List<UserAccountDto> getUserAccountsList(int page, int size);


    int getTotalPages(int page, int size);
}
