package neospider.mngr.bt.services;

import lombok.extern.slf4j.Slf4j;
import neospider.mngr.bt.persistence.entities.UserEntity;
import neospider.mngr.bt.persistence.repositories.MyBatisUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    @Autowired
    private MyBatisUserRepository userRepository;

    public UserEntity getUserByUserName(String username) {
        return userRepository.findByUsername(username);
    }
}
