package neospider.mngr.mvc.configuration;


import lombok.extern.slf4j.Slf4j;
import neospider.mngr.mvc.filter.XSSFilter;
import neospider.mngr.mvc.persistence.entities.UserEntity;
import neospider.mngr.mvc.persistence.repositories.MyBatisUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFilter;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Configuration
@EnableWebSecurity
@Profile("default")
@Slf4j
public class JSessionIdSecurityConfiguration extends WebSecurityConfigurerAdapter {

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
        http
                .authorizeRequests()
                .mvcMatchers("/", "/hello").authenticated()
                .and()
                .formLogin().permitAll()
                .and()
                .logout().permitAll()
                .and()
                .headers()
                .xssProtection().xssProtectionEnabled(true).and().contentSecurityPolicy("script-src 'self'");

    }

    @Bean
    public FilterRegistrationBean xssPreventFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();

        registrationBean.setFilter(new XSSFilter());
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }
}
