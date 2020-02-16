package by.pochepko.hes.testapp.repository;

import by.pochepko.hes.testapp.model.UserAccount;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends PagingAndSortingRepository<UserAccount, Long> {
    Optional<UserAccount> findUserAccountByUsername(String username);

}
