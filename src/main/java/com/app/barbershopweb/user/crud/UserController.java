package com.app.barbershopweb.user.crud;

import com.app.barbershopweb.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    private final UserService userService;
    private final UserConverter userConverter;

    public UserController(UserService userService, UserConverter userConverter) {
        this.userService = userService;
        this.userConverter = userConverter;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        return new ResponseEntity<>(
                userConverter.userEntityListToDtoList(userService.getUsers()), HttpStatus.OK
        );
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> findUserById(@PathVariable @Min(1) Long userId) {
        User user = userService.findUserById(userId)
                .orElseThrow(() ->
                        new NotFoundException(
                                List.of("User with id '" + userId + "' not found.")
                        )
                );
        return new ResponseEntity<>(userConverter.mapToDto(user), HttpStatus.OK);
    }

    @PostMapping
    //id is obligation due to @Valid
    public ResponseEntity<Long> addUser(@RequestBody @Valid UserDto userDto) {
        User entity = userConverter.mapToEntity(userDto);
        return new ResponseEntity<>(userService.addUser(entity), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<UserDto> updateUser(@RequestBody @Valid UserDto userDto) {
        User entity = userService.updateUser(userConverter.mapToEntity(userDto))
                .orElseThrow(() ->
                        new NotFoundException(
                                List.of("User with id '" + userDto.id() + "' not found.")
                        )
                );
        return new ResponseEntity<>(userConverter.mapToDto(entity), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUserById(@PathVariable @Min(1) Long userId) {
        userService.deleteUserById(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
