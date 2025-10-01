package com.EnumDayTask.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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


    private static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public static String hashPassword(String password){
        return passwordEncoder.encode(password);
    }
    public static boolean verifyPassword(String hashedPassword, String inputPassword) {
        if (hashedPassword == null || hashedPassword.isEmpty() || inputPassword == null || inputPassword.isEmpty()) {return false;}
        try {return passwordEncoder.matches(inputPassword, hashedPassword);} catch (IllegalArgumentException e) {return false;}}

}
