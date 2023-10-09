package com.edu.messengerrelex.repositories;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Component
public class TokenBlackListRepository {
    final private Set<String> tokenBlackList = new HashSet<>();

    public void add(String token) {
        tokenBlackList.add(token);
    }

    public void delete(String token) {
        tokenBlackList.remove(token);
    }

    public boolean contains(String token) {
        return tokenBlackList.contains(token);
    }

    public Iterator<String> iterator() {
        return tokenBlackList.iterator();
    }

}
