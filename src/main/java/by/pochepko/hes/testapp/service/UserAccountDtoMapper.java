package by.pochepko.hes.testapp.service;

import by.pochepko.hes.testapp.dto.UserAccountDto;
import by.pochepko.hes.testapp.model.UserAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface UserAccountDtoMapper {
    UserAccountDto modelToDto(UserAccount userAccount);

    UserAccount dtoToModel(UserAccountDto userAccountDto);
}
