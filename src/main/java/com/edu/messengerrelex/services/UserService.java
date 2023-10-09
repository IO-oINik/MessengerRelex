package com.edu.messengerrelex.services;

import com.edu.messengerrelex.dto.UserDto;
import com.edu.messengerrelex.models.User;
import com.edu.messengerrelex.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository repository;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.repository = userRepository;
        this.modelMapper = modelMapper;
    }
    public List<User> getAll() {
        return this.repository.findAll();
    }
    public void save(User user) {
        this.repository.save(user);
    }
    public UserDto getByUsername(String username) {
        User user = this.repository.findByUsername(username);
        if(user == null) {
            return null;
        } else {
            return modelMapper.map(user, UserDto.class);
        }
    }

    public String getPasswordByUsername(String username) {
        User user = repository.findByUsername(username);
        if(user == null)
            return null;
        else
            return user.getPassword();
    }

    public void editFirstnameByUsername(String username, String firstname) {
        repository.editFirstnameByUsername(username, firstname);
    }
    public void editLastnameByUsername(String username, String lastname) {
        repository.editLastnameByUsername(username, lastname);
    }

    public void editUsernameByUsername(String username, String editUsername) {
        repository.editUsernameByUsername(username, editUsername);
    }

    public void editEmailByUsername(String username, String email) {
        repository.editEmailByUsername(username, email);
    }

    public void editPasswordByUsername(String username, String password) {
        repository.editPasswordByUsername(username, password);
    }

    public void deleteByUsername(String username) {
        repository.deleteByUsername(username);
    }
}
