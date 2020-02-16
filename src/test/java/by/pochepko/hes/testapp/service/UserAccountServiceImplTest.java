package by.pochepko.hes.testapp.service;

import by.pochepko.hes.testapp.dto.UserAccountDto;
import by.pochepko.hes.testapp.mapper.UserAccountDtoMapper;
import by.pochepko.hes.testapp.model.UserAccount;
import by.pochepko.hes.testapp.repository.UserAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAccountServiceImplTest {

    @Mock
    private UserAccountDtoMapper userAccountDtoMapper;

    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserAccountServiceImpl sut = new UserAccountServiceImpl(userAccountDtoMapper);


    @Test
    void getUserAccountList_userAccountMappedFromEntityShouldBeEqualToUserAccountDto() {
        // given
        UserAccount user = new UserAccount();
        UserAccountDto userAccountDto = new UserAccountDto();

        Iterable<UserAccount> users = new ArrayList<>(Arrays.asList(user));
        when(userAccountRepository.findAll()).thenReturn(users);
        when(userAccountDtoMapper.modelToDto(any(UserAccount.class))).thenReturn(userAccountDto);

        //when
        List<UserAccountDto> userDtos = sut.getUserAccountList();
        //then
        assertThat(userDtos.size()).isEqualTo(1);
        assertThat(userDtos.get(0)).isEqualTo(userAccountDto);

    }

    @Test
    void createUser_givenNewUserAccountDto_PasswordShouldBeEncodedBeforeSave() {
        //given
        UserAccountDto newUser = new UserAccountDto();
        newUser.setPassword("password");
        UserAccount userToSave = new UserAccount();
        userToSave.setPassword("encodedPassword");
        when(passwordEncoder.encode(Mockito.anyString())).thenReturn("encodedPassword");
        when(userAccountRepository.save(any(UserAccount.class)))
                .thenReturn(userToSave);
        when(userAccountDtoMapper.modelToDto(any(UserAccount.class))).thenReturn(newUser);
        when(userAccountDtoMapper.dtoToModel(any(UserAccountDto.class))).thenReturn(userToSave);

        ArgumentCaptor<UserAccountDto> captor = ArgumentCaptor.forClass(UserAccountDto.class);

        //when
        UserAccountDto savedUser = sut.createUser(newUser);

        //then

        Mockito.verify(userAccountDtoMapper, Mockito.times(1)).dtoToModel(captor.capture());
        assertThat(captor.getAllValues()).hasSize(1);
        UserAccountDto capturedArgument = captor.getValue();
        assertThat(capturedArgument.getPassword()).isEqualTo("encodedPassword");

    }

    @Test
    void getUserAccountById_userAccountMappedFromEntityShouldReturnUserAccountDto() {

        // given
        UserAccount user = new UserAccount();
        UserAccountDto userAccountDto = new UserAccountDto();
        Iterable<UserAccount> users = new ArrayList<>(Arrays.asList(user));
        when(userAccountRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(userAccountDtoMapper.modelToDto(any(UserAccount.class))).thenReturn(userAccountDto);
        //when
        UserAccountDto userDto = sut.getUserAccountById(1l);
        //then
        assertThat(userDto).isEqualTo(userAccountDto);
    }

    @Test
    void updateUserAccount_shouldUpdateEveryFieldExceptPasswordAndCreatedAt() {
        //given
        UserAccount oldUser = new UserAccount(1l, "username", "password", "firstName", "lastename", UserAccount.Role.USER, UserAccount.Status.ACTIVE, LocalDateTime.parse("2018-12-30T19:34:50.63"));
        UserAccount updatedUser = new UserAccount(1l, "updatedUsername", "password", "UpdatedFirstName", "UpdatedLastename", UserAccount.Role.ADMIN, UserAccount.Status.INACTIVE, LocalDateTime.parse("2018-12-30T19:34:50.63"));
        UserAccountDto userAccountDto = new UserAccountDto(1l, "updatedUsername", "UpdatedPassword", "UpdatedFirstName", "UpdatedLastename", "ADMIN", "INACTIVE", "2019-12-30T19:34:50.63");
        when(userAccountRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(oldUser));
        when(userAccountRepository.save(any(UserAccount.class)))
                .thenReturn(updatedUser);
        ArgumentCaptor<UserAccount> captor = ArgumentCaptor.forClass(UserAccount.class);
        //when
        sut.updateUserAccount(userAccountDto, 1l);

        //then

        Mockito.verify(userAccountRepository, Mockito.times(1)).save(captor.capture());
        assertThat(captor.getAllValues()).hasSize(1);
        UserAccount capturedArgument = captor.getValue();
        assertThat(capturedArgument).isEqualTo(updatedUser);


    }

}