package com.edu.messengerrelex.controllers;

import com.edu.messengerrelex.dto.UserDto;
import com.edu.messengerrelex.models.User;
import com.edu.messengerrelex.services.JwtUserDetailsService;
import com.edu.messengerrelex.services.TokenBlackListService;
import com.edu.messengerrelex.services.UserService;
import com.edu.messengerrelex.utils.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    final private UserService userService;
    final private AuthenticationManager authenticationManager;
    final private JwtUserDetailsService userDetailsService;
    final private JwtTokenUtil jwtTokenUtil;
    final private TokenBlackListService tokenBlackListService;
    SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    public AuthenticationController(UserService userService, AuthenticationManager authenticationManager,
                                    JwtUserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil, TokenBlackListService tokenBlackListService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.tokenBlackListService = tokenBlackListService;
    }

    @GetMapping("/logout") // Выход из системы
    public ResponseEntity<?> logoutUser(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        String requestTokenHeader = request.getHeader("Authorization");
        String token = requestTokenHeader.substring(7);
        tokenBlackListService.add(token); // Добавляем токен в черный список
        this.logoutHandler.logout(request, response, authentication);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("error", false);
        responseMap.put("message", "Logged Out");
        return ResponseEntity.ok(responseMap);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestParam("user_name") String username,
                                       @RequestParam("password") String password) {
        Map<String, Object> responseMap = new HashMap<>();
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            if (auth.isAuthenticated()) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                String token = jwtTokenUtil.generateToken(userDetails);
                responseMap.put("error", false);
                responseMap.put("message", "Logged In");
                responseMap.put("token", token);
                return ResponseEntity.ok(responseMap);
            } else {
                responseMap.put("error", true);
                responseMap.put("message", "Invalid Credentials");
                return ResponseEntity.status(401).body(responseMap);
            }
        } catch (DisabledException e) {
            e.printStackTrace();
            responseMap.put("error", true);
            responseMap.put("message", "User is disabled");
            return ResponseEntity.status(500).body(responseMap);
        } catch (BadCredentialsException e) {
            responseMap.put("error", true);
            responseMap.put("message", "Invalid Credentials");
            return ResponseEntity.status(401).body(responseMap);
        } catch (Exception e) {
            e.printStackTrace();
            responseMap.put("error", true);
            responseMap.put("message", "Something went wrong");
            return ResponseEntity.status(500).body(responseMap);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestParam("first_name") String firstName,
                                      @RequestParam("last_name") String lastName,
                                      @RequestParam("username") String username,
                                      @RequestParam("password") String password,
                                      @RequestParam("email")  String email) {
        Map<String, Object> responseMap = new HashMap<>();
        User user = new User();
        user.setFirstname(firstName);
        user.setLastname(lastName);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setRole("ROLE_USER");
        user.setEmail(email);
        user.setUsername(username);
        try {
            userService.save(user);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            String token = jwtTokenUtil.generateToken(userDetails);
            UserDto userDto = new UserDto(user);
            responseMap.put("error", false);
            responseMap.put("message", "Account created successfully");
            responseMap.put("token", token);
            responseMap.put("user", userDto);
            return ResponseEntity.ok(responseMap);
        } catch (DataIntegrityViolationException e) {
            responseMap.put("error", true);
            responseMap.put("message", "The fields are missing or incorrectly specified");
            return ResponseEntity.status(422).body(responseMap);
        }

    }
}
