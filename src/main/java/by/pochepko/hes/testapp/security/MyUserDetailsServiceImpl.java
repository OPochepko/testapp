package by.pochepko.hes.testapp.security;

import by.pochepko.hes.testapp.model.UserAccount;
import by.pochepko.hes.testapp.repository.UserAccountRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class MyUserDetailsServiceImpl implements MyUserDetailsService {

    private UserAccountRepository userAccountRepository;

    public MyUserDetailsServiceImpl(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = userAccountRepository.findUserAccountByUsername(username).get();
        GrantedAuthority authority = new SimpleGrantedAuthority(userAccount.getRole().name());
        UserDetails userDetails = new User(userAccount.getUsername(), userAccount.getPassword(),
                true, true, true,
                userAccount.getStatus() == UserAccount.Status.ACTIVE, Arrays.asList(authority));
        return userDetails;
    }
}
