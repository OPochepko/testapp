package by.pochepko.hes.testapp.service;

import by.pochepko.hes.testapp.dto.UserAccountDto;
import by.pochepko.hes.testapp.model.UserAccount;
import by.pochepko.hes.testapp.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Transactional
public class UserAccountServiceImpl implements UserAccountService {
    @Autowired
    private UserAccountDtoMapper userAccountDtoMapper;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<UserAccountDto> getUserAccountList() {
        return StreamSupport.stream(userAccountRepository.findAll().spliterator(), true)
                .peek(r -> r.setPassword(""))
                .map(userAccountDtoMapper::modelToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserAccountDto createUser(UserAccountDto userAccount) {
        String encodedpassword = passwordEncoder.encode(userAccount.getPassword());
        userAccount.setPassword(encodedpassword);
        return userAccountDtoMapper.modelToDto(userAccountRepository
                .save(userAccountDtoMapper.dtoToModel(userAccount)));
    }

    @Override
    public UserAccountDto getUserAccountById(long id) {
        UserAccount userAccount = userAccountRepository.findById(id).get();
        userAccount.setPassword("");

        return userAccountDtoMapper.modelToDto(userAccount);
    }

    @Override
    public void updateUserAccount(UserAccountDto userAccountDto, long id) {
        UserAccount userAccount = userAccountRepository.findById(id).get();
        userAccount.setFirstName(userAccountDto.getFirstName());
        userAccount.setLastName(userAccountDto.getLastName());
        userAccount.setRole(UserAccount.Role.valueOf(userAccountDto.getRole()));
        userAccount.setStatus(UserAccount.Status.valueOf(userAccountDto.getStatus()));
        userAccountRepository.save(userAccount);
    }

}
