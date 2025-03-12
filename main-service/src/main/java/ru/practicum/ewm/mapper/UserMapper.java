package ru.practicum.ewm.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ewm.dto.create.UserCreateDto;
import ru.practicum.ewm.dto.response.UserResponseDto;
import ru.practicum.ewm.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserCreateDto userDto);

    UserResponseDto toResponse(User user);
}
