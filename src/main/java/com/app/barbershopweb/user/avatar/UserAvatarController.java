package com.app.barbershopweb.user.avatar;

import com.app.barbershopweb.user.avatar.validator.AvatarImage;
import org.springframework.core.io.ByteArrayResource;
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

    public UserAvatarController(UserAvatarService userAvatarService) {
        this.userAvatarService = userAvatarService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ByteArrayResource> downloadAvatar(@PathVariable @Min(1) Long userId) {
        return ResponseEntity
                .ok()
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "inline")
                .body(userAvatarService.downloadProfileAvatar(userId));

    }

    @PostMapping("/{userId}")
    public void uploadAvatar(@PathVariable @Min(1) Long userId,
                             @RequestParam(value = "file") @AvatarImage MultipartFile image) {
        userAvatarService.uploadProfileAvatar(userId, image);
    }

    @DeleteMapping("/{userId}")
    public void removeAvatar(@PathVariable @Min(1) Long userId) {
        userAvatarService.deleteProfileAvatar(userId);
    }

}
