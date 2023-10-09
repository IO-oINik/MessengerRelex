package com.edu.messengerrelex.payload.requests;

import lombok.Data;

@Data
public class EditUserRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String username;
    public boolean isEmpty() {
        return (firstname == null || firstname.isEmpty())
                && (lastname == null || lastname.isEmpty())
                && (email == null || email.isEmpty())
                && (username == null || username.isEmpty());
    }
}
