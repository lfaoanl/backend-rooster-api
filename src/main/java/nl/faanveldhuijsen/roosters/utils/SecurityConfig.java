package nl.faanveldhuijsen.roosters.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user").password("$2a$12$9gCOn4fl0V4wTDKkUrY6n.klaY9Xnu43NkVxrv1I6QhPZWLY/8.UO").roles("USER")
                .and()
                .withUser("admin").password("$2a$12$9gCOn4fl0V4wTDKkUrY6n.klaY9Xnu43NkVxrv1I6QhPZWLY/8.UO").roles("USER", "ADMIN");

    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/teapot").hasRole("ADMIN")
                .antMatchers("/**").hasAnyRole("ADMIN", "USER");
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
