package com.app.barbershopweb.user.avatar;

import com.app.barbershopweb.user.avatar.validator.AvatarImageValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;

@RestController
@RequestMapping("/users/avatars")
@Validated
public class UserAvatarController {

    private final UserAvatarService userAvatarService;
    private final AvatarImageValidator imageValidator;

    public UserAvatarController(UserAvatarService userAvatarService, AvatarImageValidator imageValidator) {
        this.userAvatarService = userAvatarService;
        this.imageValidator = imageValidator;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable @Min(1) Long userId) {
        return ResponseEntity
                .ok()
                .header("Content-type", "application/octet-stream")
                .body(userAvatarService.downloadProfileAvatar(userId));
    }

    @PostMapping("/{userId}")
    public void uploadAvatar(@PathVariable @Min(1) Long userId,
                             @RequestParam(value = "file") MultipartFile image) {
        imageValidator.isValid(image);
        userAvatarService.uploadProfileAvatar(userId, image);
    }

    @DeleteMapping("/{userId}")
    public void removeAvatar(@PathVariable @Min(1) Long userId) {
        userAvatarService.deleteProfileAvatar(userId);
    }
}
