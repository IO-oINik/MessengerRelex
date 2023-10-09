package com.edu.messengerrelex.repositories;

import com.edu.messengerrelex.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    @Transactional
    void deleteByUsername(String username);
    @Modifying
    @Transactional
    @Query(value = "UPDATE users u set firstname=:firstname where username=:username", nativeQuery = true)
    void editFirstnameByUsername(String username, String firstname);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users u set lastname=:lastname where username=:username", nativeQuery = true)
    void editLastnameByUsername(String username, String lastname);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users u set username=:editUsername where username=:username", nativeQuery = true)
    void editUsernameByUsername(String username, String editUsername);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users u set email=:email where username=:username", nativeQuery = true)
    void editEmailByUsername(String username, String email);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users u set password=:password where username=:username", nativeQuery = true)
    void editPasswordByUsername(String username, String password);

}
