package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.validator.annotation.DateAfterSpecial;

import java.time.LocalDate;

public class DateAfterSpecialValidator implements ConstraintValidator<DateAfterSpecial, LocalDate> {
    private LocalDate minDate;

    @Override
    public void initialize(DateAfterSpecial constraintAnnotation) {
        this.minDate = LocalDate.parse(constraintAnnotation.minDate());
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        if (date == null) {
            return true;
        }
        return !date.isBefore(minDate);
    }
}
