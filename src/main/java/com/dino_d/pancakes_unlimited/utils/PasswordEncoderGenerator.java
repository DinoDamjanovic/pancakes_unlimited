package com.dino_d.pancakes_unlimited.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Scanner;

// Use to encrypt password for manual storing into db
public class PasswordEncoderGenerator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter password to encode: ");
        String password = scanner.nextLine();

        System.out.print("Encoded password: " + new BCryptPasswordEncoder().encode(password));
    }
}
