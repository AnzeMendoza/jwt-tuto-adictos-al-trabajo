package com.ssubijana.roleauthorization.web;

import com.ssubijana.roleauthorization.domain.User;
import com.ssubijana.roleauthorization.mapper.UserMapper;
import com.ssubijana.roleauthorization.service.UserService;
import com.ssubijana.roleauthorization.web.presentation.AuthorizationRequest;
import com.ssubijana.roleauthorization.web.presentation.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    //@Secured("ROLE_ADMIN")
    @PreAuthorize("hasRole('ROLE_USER') OR hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable long id) {
        final User user = userService.getUser(id);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        UserResponse userResponse = userMapper.toResponse(user);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<User> saveUser(@RequestBody AuthorizationRequest userRequest) {
        userRequest.setPassword(bCryptPasswordEncoder.encode(userRequest.getPassword()));
        final User userToSave = userService.save(userMapper.toDomain(userRequest));

        return new ResponseEntity<>(userToSave, HttpStatus.OK);
    }
}
