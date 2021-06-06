package neospider.mngr.bt.configuration.security;

import lombok.extern.slf4j.Slf4j;
import neospider.mngr.bt.persistence.entities.UserEntity;
import neospider.mngr.bt.persistence.repositories.MyBatisUserRepository;
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
        log.info("Inserting -> {}", userRepository.insert(
                UserEntity.builder().id(UUID.randomUUID().toString()).username("admin").password(passwordEncoder().encode("password")).role("admin").build()));
        log.info("Inserting -> {}", userRepository.insert(
                UserEntity.builder().id(UUID.randomUUID().toString()).username("user").password(passwordEncoder().encode("password")).role("user").build()));

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
