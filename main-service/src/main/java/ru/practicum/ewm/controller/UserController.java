package ru.practicum.ewm.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.create.UserCreateDto;
import ru.practicum.ewm.dto.response.UserResponseDto;
import ru.practicum.ewm.entity.User;
import ru.practicum.ewm.mapper.UserMapper;
import ru.practicum.ewm.service.UserService;

import java.util.List;

import static ru.practicum.ewm.controller.URIConstants.ID_PARAM;
import static ru.practicum.ewm.controller.URIConstants.USERS_ADMIN_URI;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService service;
    private final UserMapper mapper;

    @GetMapping(USERS_ADMIN_URI)
    public List<UserResponseDto> getUsersByIds(@RequestParam(required = false) String ids,
                                               @RequestParam(defaultValue = "0") int from,
                                               @RequestParam(defaultValue = "10") int size) {
        return service.getUsersByIds(ids, from, size).stream().map(mapper::toResponse).toList();
    }

    @PostMapping(USERS_ADMIN_URI)
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(@RequestBody @Valid UserCreateDto userDto) {
        User user = mapper.toEntity(userDto);
        return mapper.toResponse(service.addUser(user));
    }

    @DeleteMapping(USERS_ADMIN_URI + ID_PARAM)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable long id) {
        service.deleteUserById(id);
    }
}
