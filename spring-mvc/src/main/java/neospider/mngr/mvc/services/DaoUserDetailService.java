package neospider.mngr.mvc.services;

import neospider.mngr.mvc.persistence.entities.UserEntity;
import neospider.mngr.mvc.persistence.repositories.MyBatisUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class DaoUserDetailService implements UserDetailsService {

    private MyBatisUserRepository userRepository;

    @Autowired
    public DaoUserDetailService(MyBatisUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(userEntity.getUsername(), userEntity.getPassword(),
                Arrays.asList(new SimpleGrantedAuthority(String.format("ROLE_%s", userEntity.getRole().toUpperCase()))));
    }
}
