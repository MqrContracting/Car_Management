package com.carwash.CarWash.controller;

import com.carwash.CarWash.entity.AuthRequest;
import com.carwash.CarWash.entity.UserInfo;
import com.carwash.CarWash.service.JwtService;
import com.carwash.CarWash.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")  // Allow Angular app to communicate with Spring Boot backend
public class UserController {

    @Autowired
    private UserInfoService service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    // Public welcome endpoint
    @GetMapping("/welcome")
    public ResponseEntity<Map<String, String>> welcome() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Welcome! This endpoint is not secure.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Endpoint to register new user
    @PostMapping("/addNewUser")
    public ResponseEntity<Map<String, String>> addNewUser(@RequestBody UserInfo userInfo) {
      // System.out.println(userInfo.getEmail()+" - " + userInfo.getName() + " - " + userInfo.getPassword() + " - " + userInfo.getRoles());
        String result = service.addUser(userInfo);
        Map<String, String> response = new HashMap<>();
        response.put("message", result);
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    // Endpoint for user profile (requires ROLE_USER)
    @GetMapping("/user/userProfile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Map<String, String>> userProfile() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Welcome to User Profile");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Endpoint to get all users (requires ROLE_USER)
    @GetMapping("/user")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<List<UserInfo>> getAllUsers() {
        List<UserInfo> users = this.service.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Endpoint for admin profile (requires ROLE_ADMIN)
    @GetMapping("/admin/adminProfile")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Map<String, String>> adminProfile() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Welcome to Admin Profile");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Public endpoint for authentication and token generation
    @PostMapping("/generateToken")
    public ResponseEntity<Map<String, String>> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Map<String, String> response = new HashMap<>();

        // Authenticate the user with the provided credentials
        try{
            var u = new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate(u);

        // Check if authentication is successful
        if (authentication.isAuthenticated()) {
            // Generate JWT token
            String token = jwtService.generateToken(authRequest.getUsername());
            response.put("token", token); // Provide token in the response
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        }catch(Exception e){
            e.printStackTrace();
        }

        // If authentication fails, return 401 Unauthorized
          response.put("error", "Invalid user credentials");
            return  ResponseEntity.unprocessableEntity().body(response);

    }
}
