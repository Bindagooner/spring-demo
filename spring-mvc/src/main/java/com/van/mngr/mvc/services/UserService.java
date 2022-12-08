package com.van.mngr.mvc.services;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import com.van.mngr.mvc.clients.BusinessTierClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sun.net.www.protocol.https.DefaultHostnameVerifier;

import java.util.Map;

@Service
@Slf4j
public class UserService {

    @Autowired
    private BusinessTierClient client;

    public Map<String, String> getUserByUsername(String username) {
        try {
            return  (Map<String, String>) client.getUserByUsername(username).get("data");
        } catch (FeignException ex) {
            if (ex.status() == 404) {
                throw new UsernameNotFoundException(username);
            }
        }
        return null;
    }
}
