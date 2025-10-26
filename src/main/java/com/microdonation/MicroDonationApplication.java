package com.microdonation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MicroDonationApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroDonationApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("✓ College Fund Platform Started Successfully!");
        System.out.println("✓ Access at: http://localhost:8080");
        System.out.println("========================================\n");
    }
}
