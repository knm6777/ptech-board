package kr.co.board.config;

import kr.co.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    private final MemberService memberService;
    private final DataSource dataSource;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().sameOrigin()
                .and()
                    .csrf()
                .and()
                    .authorizeRequests()
                    .antMatchers("/posts/**", "/comments/**", "/member/check/**").authenticated()
                    .anyRequest()
                    .permitAll()
                .and()
                    .formLogin()
                    .loginPage("/member/login")
                    .loginProcessingUrl("/member/login")
                    .permitAll()
                    .defaultSuccessUrl("/")
                    .failureUrl("/member/login?error=1")
                .and()
                    .logout()
                    .logoutUrl("/member/logout")
                    .permitAll()
                    .logoutSuccessUrl("/")
                .and()
                    .rememberMe()
                    .rememberMeParameter("remember-me")
                    .userDetailsService(memberService)
                    .tokenRepository(tokenRepository());
    }
    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}