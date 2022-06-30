package kr.co.board.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final LoginSuccessHandler loginSuccessHandler;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().sameOrigin()
                .and()
                    .csrf()
                .and()
                    .authorizeRequests()
                    //.antMatchers("/home").authenticated()
                    .anyRequest()
                    .permitAll()

                .and()
                    .formLogin()
                    .loginPage("/member/login")
                    .permitAll()
                    .successHandler(loginSuccessHandler)
                    .defaultSuccessUrl("/");
//                    .failureUrl("/member/login?error=1");
//                   .loginProcessingUrl("/home");
//                .and()
//                    .logout()
//                    //.logoutUrl("/user/logout")
//                    .permitAll()
//                    .logoutSuccessUrl("/");
    }
}