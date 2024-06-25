package io.swagger.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    JwtTokenFilter jwtTokenFilter;


    private static final String [] AUTH_WHITELIST = {
            "/login",
            "h2-console/**/**",
            "/api-docs",
            "webjars/**",
            "/swagger-resources/**/**"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //disable cors
        http.cors().and().csrf().disable();
        http.csrf().disable(); // no CSRF protection needed
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // no sessions needed
        http.authorizeRequests()
                .antMatchers("/swagger-ui/**/**", "/").permitAll()/// allow some URLs for unauthenticated users
                .antMatchers("/login", "/").permitAll()
                .antMatchers("/users", "/").permitAll()
                .antMatchers("/h2-console/**/**", "/").permitAll()
                .anyRequest().authenticated(); // disallow any other URL for unauthenticated users
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class); // add the filter
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/h2-console/**/**");
        web.ignoring().antMatchers(AUTH_WHITELIST);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}