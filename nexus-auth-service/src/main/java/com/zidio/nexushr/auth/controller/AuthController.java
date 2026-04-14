package com.zidio.nexushr.auth.controller;

import com.zidio.nexushr.auth.repository.UserRepository;
import com.zidio.nexushr.auth.util.JwtUtils;
import com.zidio.nexushr.common.model.Employee;
import com.zidio.nexushr.common.model.EmployeeStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody Employee signUpRequest) {
        logger.info("📝 Signup request received for email: {}", signUpRequest.getEmail());
        
        // Validation checks
        if (signUpRequest.getEmail() == null || signUpRequest.getEmail().isEmpty()) {
            return errorResponse("Email is required!");
        }
        
        if (signUpRequest.getPassword() == null || signUpRequest.getPassword().isEmpty()) {
            return errorResponse("Password is required!");
        }
        
        if (signUpRequest.getFirstName() == null || signUpRequest.getFirstName().isEmpty()) {
            return errorResponse("First name is required!");
        }
        
        if (signUpRequest.getLastName() == null || signUpRequest.getLastName().isEmpty()) {
            return errorResponse("Last name is required!");
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            logger.warn("⚠️ Email already exists: {}", signUpRequest.getEmail());
            return errorResponse("Email is already in use!");
        }

        try {
            Employee user = new Employee();
            user.setFirstName(signUpRequest.getFirstName());
            user.setLastName(signUpRequest.getLastName());
            user.setEmail(signUpRequest.getEmail());
            user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
//            user.setDepartment(signUpRequest.getDepartment());
//            user.setDesignation(signUpRequest.getDesignation());
            user.setStatus(EmployeeStatus.ACTIVE);

            // Set default role if not provided
            String role = signUpRequest.getRole();
            if (role != null && !role.isEmpty()) {
                user.setRole(role);
            } else {
                user.setRole("EMPLOYEE");
            }

            userRepository.save(user);
            logger.info("✅ User registered successfully: {}", user.getEmail());
            
            return successResponse("User registered successfully!");
            
        } catch (Exception e) {
            logger.error("❌ Error during signup: {}", e.getMessage());
            return errorResponse("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        logger.info("🔐 Login request received for email: {}", loginRequest.getEmail());
        
        // Validation
        if (loginRequest.getEmail() == null || loginRequest.getEmail().isEmpty()) {
            return errorObjectResponse("Email is required!");
        }
        
        if (loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
            return errorObjectResponse("Password is required!");
        }
        
        Optional<Employee> userOptional = userRepository.findByEmail(loginRequest.getEmail());

        if (userOptional.isEmpty()) {
            logger.warn("⚠️ User not found: {}", loginRequest.getEmail());
            return errorObjectResponse("User not found!");
        }

        Employee user = userOptional.get();
        
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            logger.warn("⚠️ Invalid password for user: {}", loginRequest.getEmail());
            return errorObjectResponse("Invalid password!");
        }

        try {
            String token = jwtUtils.generateTokenFromUsername(user.getEmail());
            logger.info("✅ User logged in successfully: {}", user.getEmail());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("token", token);
            response.put("type", "Bearer");
            response.put("email", user.getEmail());
            response.put("role", user.getRole());
            response.put("firstName", user.getFirstName());
            response.put("lastName", user.getLastName());

            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("❌ Error during login: {}", e.getMessage());
            return errorObjectResponse("Login failed: " + e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        logger.info("👤 Get current user request received");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return errorObjectResponse("Authorization header missing or invalid!");
        }
        
        String token = authHeader.substring(7);
        
        if (!jwtUtils.validateJwtToken(token)) {
            logger.warn("⚠️ Invalid token received");
            return errorObjectResponse("Invalid or expired token!");
        }
        
        String email = jwtUtils.getUserNameFromJwtToken(token);
        Optional<Employee> userOptional = userRepository.findByEmail(email);
        
        if (userOptional.isEmpty()) {
            return errorObjectResponse("User not found!");
        }
        
        Employee user = userOptional.get();
        user.setPassword(null); // Remove password before sending
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("user", user);
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, Object>> refreshToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        logger.info("🔄 Refresh token request received");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return errorObjectResponse("Authorization header missing or invalid!");
        }
        
        String token = authHeader.substring(7);
        
        if (!jwtUtils.validateJwtToken(token)) {
            return errorObjectResponse("Invalid or expired token!");
        }
        
        String email = jwtUtils.getUserNameFromJwtToken(token);
        String newToken = jwtUtils.generateTokenFromUsername(email);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("token", newToken);
        response.put("type", "Bearer");
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        logger.info("🚪 Logout request received");
        return successResponse("Logout successful. Please delete your token on client side.");
    }

    // Helper methods for consistent responses
    private ResponseEntity<Map<String, String>> successResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("success", "true");
        response.put("message", message);
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<Map<String, String>> errorResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("success", "false");
        response.put("error", message);
        return ResponseEntity.badRequest().body(response);
    }

    private ResponseEntity<Map<String, Object>> errorObjectResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("error", message);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    // Inner class for login request
    public static class LoginRequest {
        private String email;
        private String password;
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}