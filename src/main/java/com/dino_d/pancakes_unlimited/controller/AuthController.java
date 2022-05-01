package com.dino_d.pancakes_unlimited.controller;

import com.dino_d.pancakes_unlimited.dto.LoginDto;
import com.dino_d.pancakes_unlimited.dto.SignUpDto;
import com.dino_d.pancakes_unlimited.entity.Role;
import com.dino_d.pancakes_unlimited.entity.User;
import com.dino_d.pancakes_unlimited.repository.RoleRepository;
import com.dino_d.pancakes_unlimited.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.Collections;
import java.util.Optional;

import static com.dino_d.pancakes_unlimited.utils.AppConstants.ROLE_CUSTOMER;

@RestController
@Validated
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@Valid @RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>("User logged in successfully.", HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> authenticateUser(@Valid @RequestBody SignUpDto signUpDto) {
        if (userRepository.existsByUsername(signUpDto.getUsername())) {
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        User newUser = new User();
        newUser.setUsername(signUpDto.getUsername());
        newUser.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        Optional<Role> role = roleRepository.findByName(ROLE_CUSTOMER);
        newUser.setRoles(Collections.singleton(role.get()));

        userRepository.save(newUser);

        return new ResponseEntity<>("User registered successfully.", HttpStatus.CREATED);
    }
}
