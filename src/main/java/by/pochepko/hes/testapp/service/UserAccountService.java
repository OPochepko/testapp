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
     * Return created user entity
     *
     * @param userAccount must not be null
     * @return created entity
     */
    UserAccountDto createUser(UserAccountDto userAccount);

    /**
     * Return user entity with given id
     *
     * @param id
     * @return the entity with the given id
     */

    UserAccountDto getUserAccountById(long id);

    /**
     * Update user detail for existing user identified by passed id
     *
     * @param userAccount must not be null
     * @param id          must not be null
     */

    void updateUserAccount(UserAccountDto userAccount, long id);

    /**
     * Return list of user entity on page depending on its size
     *
     * @param page
     * @param size
     * @return List of users
     */


    List<UserAccountDto> getUserAccountsList(int page, int size);

    /**
     * Return total count of entities in repository
     *
     * @return total count
     */


    long getTotalCount();
}
