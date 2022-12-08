package com.van.mngr.bt.services;

import com.van.mngr.bt.persistence.repository.user.MyBatisUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class UserService {

    @Autowired
    private MyBatisUserRepository userRepository;

    public Map getUserByUserName(String username) {
        return userRepository.findByUsername(username);
    }
}
