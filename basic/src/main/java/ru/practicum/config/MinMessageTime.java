package ru.practicum.config;

import javax.validation.Constraint;
import javax.validation.constraints.Past;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MinMessageTimeValidator.class)
@Past
public @interface MinMessageTime {

    String message() default "Date must not be before {value}";

    String value() default "1970-01-01T00:00:01";
}
