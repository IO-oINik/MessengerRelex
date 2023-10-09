package com.edu.messengerrelex.dto;

import com.edu.messengerrelex.models.User;
import lombok.Data;

@Data
public class UserDto {
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    public UserDto(User user) {
        username = user.getUsername();
        firstname = user.getFirstname();
        lastname = user.getLastname();
        email = user.getEmail();
    }
    public UserDto() {}
}
