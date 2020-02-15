package by.pochepko.hes.testapp.service;

import by.pochepko.hes.testapp.dto.UserAccountDto;
import by.pochepko.hes.testapp.model.UserAccount;
import by.pochepko.hes.testapp.repository.UserAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
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

@ExtendWith(MockitoExtension.class)
class UserAccountServiceImplTest {

    private UserAccountDtoMapper userAccountDtoMapper = Mappers.getMapper(UserAccountDtoMapper.class);

    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserAccountServiceImpl sut = new UserAccountServiceImpl(userAccountDtoMapper);


    @Test
    void getUserAccountList_userAccountMappedFromEntityShouldNotContainPassword() {
        // given
        UserAccount user1 = new UserAccount();
        user1.setPassword("password");
        Iterable<UserAccount> users = new ArrayList<>(Arrays.asList(user1));
        Mockito.when(userAccountRepository.findAll()).thenReturn(users);
        //when
        List<UserAccountDto> userDtos = sut.getUserAccountList();
        //then
        assertThat(userDtos.get(0).getPassword()).isEqualTo("");
    }

    @Test
    void createUser_givenNewUserAccountDto_PasswordShouldBeEncodedBeforeSave() {
        //given
        UserAccountDto newUser = new UserAccountDto();
        newUser.setPassword("password");
        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("encodedPassword");
        Mockito.when(userAccountRepository.save(Mockito.any(UserAccount.class)))
                .thenReturn(userAccountDtoMapper.dtoToModel(newUser));
        ArgumentCaptor<UserAccount> captor = ArgumentCaptor.forClass(UserAccount.class);

        //when
        UserAccountDto savedUser = sut.createUser(newUser);

        //then

        Mockito.verify(userAccountRepository, Mockito.times(1)).save(captor.capture());
        assertThat(captor.getAllValues()).hasSize(1);
        UserAccount capturedArgument = captor.getValue();
        assertThat(capturedArgument.getPassword()).isEqualTo("encodedPassword");

    }

    @Test
    void getUserAccountById_userAccountMappedFromEntityShouldNotContainPassword() {

        // given
        UserAccount user1 = new UserAccount();
        user1.setPassword("password");
        Iterable<UserAccount> users = new ArrayList<>(Arrays.asList(user1));
        Mockito.when(userAccountRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));
        //when
        UserAccountDto userDto = sut.getUserAccountById(1l);
        //then
        assertThat(userDto.getPassword()).isEqualTo("");
    }

    @Test
    void updateUserAccount_shouldUpdateEveryFieldExceptPasswordAndCreatedAtAndUsername() {
        //given
        UserAccountDto oldUser = new UserAccountDto(1l, "username", "password", "firstName", "lastename", "USER", "ACTIVE", "2018-12-30T19:34:50.63");
        UserAccountDto updatedUser = new UserAccountDto(1l, "updatedUsername", "UpdatedPassword", "UpdatedFirstName", "UpdatedLastename", "ADMIN", "INACTIVE", "2019-12-30T19:34:50.63");
        Mockito.when(userAccountRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(userAccountDtoMapper.dtoToModel(oldUser)));
        Mockito.when(userAccountRepository.save(Mockito.any(UserAccount.class)))
                .thenReturn(new UserAccount());
        ArgumentCaptor<UserAccount> captor = ArgumentCaptor.forClass(UserAccount.class);
        //when
        sut.updateUserAccount(updatedUser, 1l);

        //then

        Mockito.verify(userAccountRepository, Mockito.times(1)).save(captor.capture());
        assertThat(captor.getAllValues()).hasSize(1);
        UserAccount capturedArgument = captor.getValue();
        assertThat(capturedArgument.getUsername()).isEqualTo(oldUser.getUsername());
        assertThat(capturedArgument.getPassword()).isEqualTo(oldUser.getPassword());
        assertThat(capturedArgument.getCreatedAt()).isEqualTo(LocalDateTime.parse(oldUser.getCreatedAt()));
        assertThat(capturedArgument.getFirstName()).isEqualTo(updatedUser.getFirstName());
        assertThat(capturedArgument.getLastName()).isEqualTo(updatedUser.getLastName());
        assertThat(capturedArgument.getRole()).isEqualTo(Enum.valueOf(UserAccount.Role.class, updatedUser.getRole()));
        assertThat(capturedArgument.getStatus()).isEqualTo(Enum.valueOf(UserAccount.Status.class, updatedUser.getStatus()));


    }

}