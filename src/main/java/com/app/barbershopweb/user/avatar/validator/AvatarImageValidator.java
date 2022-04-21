package com.app.barbershopweb.user.avatar.validator;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class AvatarImageValidator implements ConstraintValidator<AvatarImage, MultipartFile> {
    @Override
    public void initialize(AvatarImage constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {


        var msgValidation = imageValidations(multipartFile);

        if (!msgValidation.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(msgValidation).addConstraintViolation();
            return false;
        }

        return true;
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
