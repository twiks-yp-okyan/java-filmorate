package ru.yandex.practicum.filmorate.validator.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.yandex.practicum.filmorate.validator.DateAfterSpecialValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateAfterSpecialValidator.class)
public @interface DateAfterSpecial {
    String minDate();
    String message() default "Release date must be after {minDate}";
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default {};
}
