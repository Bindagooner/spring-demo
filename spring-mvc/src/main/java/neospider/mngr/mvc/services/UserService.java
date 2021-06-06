package neospider.mngr.mvc.services;

import feign.Client;
import feign.Feign;
import feign.FeignException;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import lombok.extern.slf4j.Slf4j;
import neospider.mngr.mvc.clients.BusinessTierClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sun.net.www.protocol.https.DefaultHostnameVerifier;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
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
