package com.app.barbershopweb.user.avatar.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {AvatarImageValidator.class})
public @interface AvatarImage {
    String message() default "Invalid avatar image";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}