package com.tuanviet.gaminggear.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneNumber {

    String message() default "Số điện thoại phải có dạng 0xxxxxxxxx hoặc +84xxxxxxxxx";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}