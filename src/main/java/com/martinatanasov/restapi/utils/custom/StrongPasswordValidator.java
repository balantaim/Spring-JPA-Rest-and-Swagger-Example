package com.martinatanasov.restapi.utils.custom;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {

    /**
     * Password Rules:
     *     Minimum 8 characters, maximum 50
     *     At least 1 uppercase
     *     At least 1 lowercase
     *     At least 1 special character (non-alphanumeric)
     *     No spaces
     */
    private static final Pattern PATTERN = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*[^a-zA-Z0-9])[^\\s]{8,50}$");

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        return password != null && PATTERN.matcher(password).matches();
    }
}
