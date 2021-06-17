package neospider.mngr.bt.configuration.security;

import lombok.extern.slf4j.Slf4j;
import neospider.mngr.bt.persistence.repository.user.MyBatisUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private MyBatisUserRepository userRepository;

    @PostConstruct
    public void initData() {
        Map admin = new HashMap<String, String>();
        admin.put("id", UUID.randomUUID().toString());
        admin.put("username", "admin");
        admin.put("password", passwordEncoder().encode("password"));
        admin.put("role", "admin");

        Map user = new HashMap<String, String>();
        user.put("id", UUID.randomUUID().toString());
        user.put("username", "user");
        user.put("password", passwordEncoder().encode("password"));
        user.put("role", "user");

        userRepository.deleteAll(); // TODO refresh data
        log.info("Inserting -> {}", userRepository.insert(admin));
        log.info("Inserting -> {}", userRepository.insert(user));

    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf().disable()
                .authorizeRequests().mvcMatchers("/**").permitAll()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);

    }
}
