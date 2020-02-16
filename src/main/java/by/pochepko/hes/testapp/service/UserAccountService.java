package by.pochepko.hes.testapp.service;

import by.pochepko.hes.testapp.dto.UserAccountDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Interface provide CRUD methods for UserAccountDto
 */

@Service
public interface UserAccountService {
    /**
     * @return all entities
     */
    List<UserAccountDto> getUserAccountList();

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

    /**
     * @param pageable must not be null
     * @return List of all entities on page depending on given Pageable pageble
     */

    List<UserAccountDto> findPaginated(Pageable pageable);

    /**
     * @param pageable
     * @return number of available pages of entities
     */

    int getTotalPages(Pageable pageable);
}
