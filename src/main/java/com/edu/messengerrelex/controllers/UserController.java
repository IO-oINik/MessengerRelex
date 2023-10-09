package com.edu.messengerrelex.controllers;

import com.edu.messengerrelex.dto.UserDto;
import com.edu.messengerrelex.models.User;
import com.edu.messengerrelex.payload.requests.DeleteUserRequest;
import com.edu.messengerrelex.payload.requests.EditPasswordRequest;
import com.edu.messengerrelex.payload.requests.EditUserRequest;
import com.edu.messengerrelex.services.UserService;
import com.edu.messengerrelex.utils.JwtTokenUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService service;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;

    public UserController(UserService Userservice, JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService) {
        this.service = Userservice;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("")
    public List<User> all() {
        return service.getAll();
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> info(@PathVariable String username) {
        UserDto user = service.getByUsername(username);
        if(user == null) {
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("error", "true");
            responseMap.put("message", "Not found");
            return ResponseEntity.status(404).body(responseMap);
        } else {
            return ResponseEntity.ok(user);
        }
    }

    @PatchMapping("/edit")
    public ResponseEntity<?> edit(Authentication authentication, @RequestBody EditUserRequest editUserRequest) {
        String username = authentication.getName();
        Map<String, Object> responseMap = new HashMap<>();
        if(editUserRequest.isEmpty()) {
            responseMap.put("error", "true");
            responseMap.put("message", "Invalid request body");
            return ResponseEntity.status(400).body(responseMap);
        }
        if(editUserRequest.getFirstname() != null && !editUserRequest.getFirstname().isEmpty()) {
            service.editFirstnameByUsername(username, editUserRequest.getFirstname());
        }
        if(editUserRequest.getLastname() != null && !editUserRequest.getLastname().isEmpty()) {
            service.editLastnameByUsername(username, editUserRequest.getLastname());
        }
        if(editUserRequest.getEmail() != null && editUserRequest.getEmail().isEmpty()) {
            service.editEmailByUsername(username, editUserRequest.getEmail());
        }
        if(editUserRequest.getUsername() != null && !editUserRequest.getUsername().isEmpty()) {
            service.editUsernameByUsername(username, editUserRequest.getUsername());
            UserDetails userDetails = userDetailsService.loadUserByUsername(editUserRequest.getUsername());
            String token = jwtTokenUtil.generateToken(userDetails);
            responseMap.put("token", token);
            username = editUserRequest.getUsername();
        }
        UserDto user = service.getByUsername(username);
        responseMap.put("user", user);
        return ResponseEntity.ok(responseMap);
    }

    @PostMapping("/edit/password")
    public ResponseEntity<?> editPassword(@RequestBody EditPasswordRequest editPasswordRequest, Authentication authentication) {
        Map<String, Object> responseMap = new HashMap<>();
        if(!editPasswordRequest.isFull()) {
            responseMap.put("error", true);
            responseMap.put("message", "Invalid request body");
            return ResponseEntity.status(400).body(responseMap);
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String oldPassword = service.getPasswordByUsername(authentication.getName());
        if(!bCryptPasswordEncoder.matches(editPasswordRequest.getOldPassword(), oldPassword)) {
            responseMap.put("error", true);
            responseMap.put("message", "Invalid old password");
            return ResponseEntity.status(400).body(responseMap);
        }
        String newPassword = bCryptPasswordEncoder.encode(editPasswordRequest.getNewPassword());
        service.editPasswordByUsername(authentication.getName(), newPassword);
        responseMap.put("error", false);
        responseMap.put("message", "Successful password change");
        return ResponseEntity.ok(responseMap);
    }

    @PostMapping("edit/delete")
    public ResponseEntity<?> deleteUser(@RequestBody DeleteUserRequest deleteUserRequest, Authentication authentication) {
        Map<String, Object> responseMap = new HashMap<>();
        if(deleteUserRequest.getPassword() == null || deleteUserRequest.getPassword().isEmpty()) {
            responseMap.put("error", true);
            responseMap.put("message", "Invalid request body");
            return ResponseEntity.status(400).body(responseMap);
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String oldPassword = service.getPasswordByUsername(authentication.getName());
        if(!bCryptPasswordEncoder.matches(deleteUserRequest.getPassword(), oldPassword)) {
            responseMap.put("error", true);
            responseMap.put("message", "Invalid password");
            return ResponseEntity.status(400).body(responseMap);
        }
        service.deleteByUsername(authentication.getName());
        responseMap.put("error", false);
        responseMap.put("message", "Successful deletion");
        return ResponseEntity.ok().body(responseMap);
    }
}
