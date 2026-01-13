package com.mishal.socialmedia.controller;

import com.mishal.socialmedia.dto.UserRequest;
import com.mishal.socialmedia.entity.User;
import com.mishal.socialmedia.service.UserService;
import jakarta.validation.Valid;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final String defaultProfilePicture = "uploads/profile-pictures/default-profile.png";

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody UserRequest request) {
        return userService.createUser(request);
    }

    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
    }

    // GET profile picture – public
    @PostMapping("/{username}/profile-picture")
    public ResponseEntity<Void> uploadProfilePicture(
            @PathVariable String username,
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        String loggedInUser =
                SecurityContextHolder.getContext().getAuthentication().getName();

        if (!loggedInUser.equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        userService.uploadProfilePicture(username, file);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}/profile-picture")
    public ResponseEntity<byte[]> getProfilePicture(
            @PathVariable String username
    ) throws IOException {

        byte[] image = userService.getProfilePicture(username);

        if (image == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(image);
    }


    @GetMapping("/uploads/profile-pictures/{filename:.+}")
    public ResponseEntity<ByteArrayResource> serveProfilePicture(@PathVariable String filename) throws IOException {
        File file = new File("uploads/profile-pictures/" + filename);
        if (!file.exists()) return ResponseEntity.notFound().build();

        byte[] bytes = Files.readAllBytes(file.toPath());
        MediaType mediaType = Files.probeContentType(file.toPath()) != null ?
                MediaType.parseMediaType(Files.probeContentType(file.toPath())) : MediaType.IMAGE_JPEG;

        return ResponseEntity.ok().contentType(mediaType).body(new ByteArrayResource(bytes));
    }

}
