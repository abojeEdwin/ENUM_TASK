package com.EnumDayTask.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtils {


    public static final String USER_NOT_FOUND = "User not found";
    public static final String EMAIL_ALREADY_EXISTS = "Email already exists";
    public static final String USER_ALREADY_EXISTS = "User already exists";
    public static final String USER_CREATED = "User created successfully";
    public static final String VERIFICATION_RESENT = "Verification email sent successfully";
    public static final String TOKEN_INVALID = "Token invalid";
    public static final String TOKEN_ALREADY_USED = "Token already used";
    public static final String TOKEN_ALREADY_EXPIRED = "Token expired";
    public static final String LOGIN_SUCCESSFUL = "Login successful";
    public static final String EMAIL_IS_NOT_VERIFIED = "Email is not verified";
    public static final String INVALID_CREDENTIALS = "Invalid credentials";
    public static final String RATE_LIMIT_EXCEEDED = "You have exceeded the maximum number of login attempts. Please try again later.";
    public static final int MAX_FAILED_ATTEMPTS = 5;
    public static final int LOCKOUT_DURATION = 15;


    private static final String EMAIL_REGEX = "^(?!.*\\.\\.)[a-zA-Z0-9](?:[a-zA-Z0-9._%+-]{0,63}[a-zA-Z0-9])?@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,253}[a-zA-Z0-9])?\\.[a-zA-Z]{2,63}$\n";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public static String hashPassword(String password){
        return passwordEncoder.encode(password);
    }
    public static boolean verifyPassword(String rawPassword, String encodedPassword) {
        if (rawPassword == null || rawPassword.isEmpty() || encodedPassword == null || encodedPassword.isEmpty()) {return false;}
        try {return passwordEncoder.matches(rawPassword, encodedPassword);} catch (IllegalArgumentException e) {return false;}}

}
