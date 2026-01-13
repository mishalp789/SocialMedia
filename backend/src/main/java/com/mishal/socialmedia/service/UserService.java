package com.mishal.socialmedia.service;

import com.mishal.socialmedia.dto.UserRequest;
import com.mishal.socialmedia.entity.User;
import com.mishal.socialmedia.event.UserCreatedEvent;
import com.mishal.socialmedia.exception.UserNotFoundException;
import com.mishal.socialmedia.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.context.ApplicationEventPublisher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final ApplicationEventPublisher eventPublisher;

    private final String uploadDir = "uploads/profile-pictures/";

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
    }

    public User createUser(UserRequest request) {
        log.info("Creating user with username={}", request.getUsername());
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("ROLE_USER");
        User saved = userRepository.save(user);
        log.info("User created successfully: id={}, username={}", saved.getId(), saved.getUsername());
        log.info("Publishing UserCreatedEvent for {}", saved.getUsername());
        eventPublisher.publishEvent(new UserCreatedEvent(saved.getUsername()));
        return saved;
    }

    @Cacheable(value = "users", key = "#username")
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
    }

    @Transactional
    @CacheEvict(value = "users", key = "#username")
    public void deleteUser(String username) {
        log.warn("Deleting user with username={}", username);
        if (!userRepository.findByUsername(username).isPresent()) {
            throw new UserNotFoundException("User not found: " + username);
        }
        userRepository.deleteByUsername(username);
    }

    // Upload profile picture
    public void uploadProfilePicture(String username, MultipartFile file) throws IOException {
        log.info("Uploading profile picture for user={}", username);
        User user = getUserByUsername(username);

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadDir + filename);

        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());

        user.setProfilePicture(path.toString()); // store path as string
        userRepository.save(user);
        log.info("Profile picture uploaded for user={}, path={}", username, path);
    }


    public byte[] getProfilePicture(String username) throws IOException {
        User user = getUserByUsername(username);

        if (user.getProfilePicture() == null) {
            return null;
        }

        Path path = Paths.get(user.getProfilePicture());
        return Files.readAllBytes(path);
    }


}
