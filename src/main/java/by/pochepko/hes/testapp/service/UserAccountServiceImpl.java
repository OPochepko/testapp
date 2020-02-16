package by.pochepko.hes.testapp.service;

import by.pochepko.hes.testapp.dto.UserAccountDto;
import by.pochepko.hes.testapp.model.UserAccount;
import by.pochepko.hes.testapp.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class UserAccountServiceImpl implements UserAccountService {

    private UserAccountDtoMapper userAccountDtoMapper;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserAccountServiceImpl(UserAccountDtoMapper userAccountDtoMapper) {
        this.userAccountDtoMapper = userAccountDtoMapper;
    }

    @Override
    public List<UserAccountDto> getUserAccountList() {
        return StreamSupport.stream(userAccountRepository.findAll().spliterator(), true)
                .map(userAccountDtoMapper::modelToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserAccountDto createUser(UserAccountDto userAccount) {
        String encodedpassword = passwordEncoder.encode(userAccount.getPassword());
        userAccount.setPassword(encodedpassword);
        UserAccount createdUser = userAccountRepository.save(userAccountDtoMapper.dtoToModel(userAccount));
        createdUser.setPassword("");
        return userAccountDtoMapper.modelToDto(createdUser);
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

    @Override
    public List<UserAccountDto> findPaginated(Pageable pageable) {
        return userAccountRepository.findAll(pageable).getContent().stream()
                .map(userAccountDtoMapper::modelToDto)
                .collect(Collectors.toList())
                ;
    }

    public int getTotalPages(Pageable pageable) {
        return userAccountRepository.findAll(pageable).getTotalPages();
    }

}
