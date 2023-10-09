package com.edu.messengerrelex.services;

import com.edu.messengerrelex.repositories.TokenBlackListRepository;
import com.edu.messengerrelex.utils.JwtTokenUtil;
import org.springframework.stereotype.Service;

import java.util.Iterator;

@Service
public class TokenBlackListService {
    final private TokenBlackListRepository tokenBlackListRepository;
    final private JwtTokenUtil jwtTokenUtil;
    private int countAdds;
    final int countAddsForClear = 10;

    public TokenBlackListService(TokenBlackListRepository tokenBlackListRepository, JwtTokenUtil jwtTokenUtil) {
        this.tokenBlackListRepository = tokenBlackListRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.countAdds = 0;
    }

    public void add(String token) {
        tokenBlackListRepository.add(token);
        countAdds += 1;
        if(countAdds > countAddsForClear) {
            clearExpiredToken();
            countAdds = 0;
        }
    }

    public boolean contains(String token) {
        return tokenBlackListRepository.contains(token);
    }

    private void clearExpiredToken() {
        Iterator<String> tokens = tokenBlackListRepository.iterator();
        while(tokens.hasNext()) {
            String token = tokens.next();
            if(jwtTokenUtil.isTokenExpired(token)) {
                tokenBlackListRepository.delete(token);
            }
        }
    }
}
