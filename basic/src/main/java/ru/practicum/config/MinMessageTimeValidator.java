package ru.practicum.config;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class MinMessageTimeValidator implements ConstraintValidator<MinMessageTime, LocalDateTime> {

    private LocalDateTime minMessageTime;

    @Override
    public void initialize(MinMessageTime constraintAnnotation) {
        minMessageTime = LocalDateTime.parse(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        return value == null || value.isAfter(minMessageTime);
    }
}
