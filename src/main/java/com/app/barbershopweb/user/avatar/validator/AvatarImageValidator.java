package com.app.barbershopweb.user.avatar.validator;

import com.app.barbershopweb.exception.ValidationException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class AvatarImageValidator {

    public void isValid(MultipartFile multipartFile) {

        var msgValidation = imageValidations(multipartFile);

        if (!msgValidation.isEmpty()) {
            throw new ValidationException(List.of("'image' " + msgValidation));
        }
    }

    private String imageValidations(MultipartFile multipartFile) {
        var contentType = multipartFile.getContentType();
        if (contentType == null)
            return "No image was uploaded";

        if (!isSupportedContentType(contentType)) {
            return "Only JPG and PNG images are allowed.";
        } else if (multipartFile.isEmpty()) {
            return "It must not be an empty image.";
        } else if (multipartFile.getSize() > (1024 * 1024)) {
            return "File size should be at most 1MB.";
        }

        return "";
    }

    private boolean isSupportedContentType(String contentType) {
        var supportedContents = List.of("image/jpg", "image/jpeg", "image/png");
        return supportedContents.contains(contentType);
    }
}
