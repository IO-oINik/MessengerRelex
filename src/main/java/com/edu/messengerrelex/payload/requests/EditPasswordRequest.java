package com.edu.messengerrelex.payload.requests;

import lombok.Data;

@Data
public class EditPasswordRequest {
    private String oldPassword;
    private String newPassword;
    public boolean isFull() {
        return oldPassword != null && !oldPassword.isEmpty() && newPassword != null && !newPassword.isEmpty();
    }
}
