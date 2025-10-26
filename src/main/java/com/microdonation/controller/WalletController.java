package com.microdonation.controller;

import com.microdonation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class WalletController {

    private final UserService userService;

    /**
     * Get wallet balance for a user
     * GET /api/wallet/balance/{userId}
     */
    @GetMapping("/balance/{userId}")
    public ResponseEntity<Map<String, Object>> getWalletBalance(@PathVariable Long userId) {
        try {
            BigDecimal balance = userService.getWalletBalance(userId);
            Map<String, Object> response = new HashMap<>();
            response.put("userId", userId);
            response.put("balance", balance);
            response.put("message", "Wallet balance retrieved successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Add money to wallet
     * POST /api/wallet/add
     * Body: { "userId": 1, "amount": 1000.00 }
     */
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addMoneyToWallet(
            @RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            BigDecimal amount = new BigDecimal(request.get("amount").toString());

            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Amount must be greater than zero");
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }

            BigDecimal newBalance = userService.addMoneyToWallet(userId, amount);

            Map<String, Object> response = new HashMap<>();
            response.put("userId", userId);
            response.put("amountAdded", amount);
            response.put("newBalance", newBalance);
            response.put("message", "Money added to wallet successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Check if user has sufficient balance for donation
     * GET /api/wallet/check-balance/{userId}/{amount}
     */
    @GetMapping("/check-balance/{userId}/{amount}")
    public ResponseEntity<Map<String, Object>> checkSufficientBalance(
            @PathVariable Long userId,
            @PathVariable BigDecimal amount) {
        try {
            boolean hasSufficientBalance = userService.hasSufficientBalance(userId, amount);
            BigDecimal currentBalance = userService.getWalletBalance(userId);

            Map<String, Object> response = new HashMap<>();
            response.put("userId", userId);
            response.put("requestedAmount", amount);
            response.put("currentBalance", currentBalance);
            response.put("hasSufficientBalance", hasSufficientBalance);

            if (!hasSufficientBalance) {
                response.put("shortfall", amount.subtract(currentBalance));
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }
}
