package com.webfejl.beadando.service;

import com.webfejl.beadando.exception.UserNotFoundException;
import com.webfejl.beadando.request.LoginRequest;
import com.webfejl.beadando.auth.jwt.JwtManager;
import com.webfejl.beadando.entity.User;
import com.webfejl.beadando.exception.UserCreationException;
import com.webfejl.beadando.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtManager jwtManager;
    private final UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtManager jwtManager,
                       UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtManager = jwtManager;
        this.userRepository = userRepository;
    }

    @Transactional
    public User createUser(User user) throws UserCreationException, DataIntegrityViolationException {
        try {
            validateCreateUser(user);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User saved = userRepository.save(user);

            userRepository.flush();

            return saved;
        } catch (DataIntegrityViolationException e) {
            throw new UserCreationException("Something went wrong!");
        }
    }

    private void validateCreateUser(User user) throws UserCreationException {
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new UserCreationException("Email field is empty or invalid!");
        }
        if (user.getUsername() == null || user.getUsername().isEmpty() || user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new UserCreationException("Username or password field is empty!");
        }
        if (user.getPassword().length() < 10) {
            throw new UserCreationException("Password must be at least 10 characters long!");
        }
        if (!user.getPassword().matches(".*\\d.*")) {
            throw new UserCreationException("Password must contain at least one number!");
        }
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserCreationException("This username already exists!");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserCreationException("This email is already registered!");
        }
    }

    public String login(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(), loginRequest.getPassword()
                    )
            );
            return jwtManager.generateToken(loginRequest.getUsername());

        } catch (AuthenticationException ex) {
            throw new BadCredentialsException("Invalid username or password", ex);
        }
    }

    public User getUserById(String userId) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) throw new UserNotFoundException("This user does not exist!");

        return user.get();
    }

    public User getUserByUsername(String username) throws UserNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isEmpty()) throw new UserNotFoundException("This user does not exist!");

        return user.get();
    }
}
