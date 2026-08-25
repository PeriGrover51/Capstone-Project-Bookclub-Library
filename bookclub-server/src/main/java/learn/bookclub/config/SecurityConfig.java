package learn.bookclub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration //tell Spring this is a config file, and has to search for configs here
@EnableWebSecurity //tell Spring Security to not use default flow, but instead use the flow here
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.csrf(customizer -> customizer.disable()) //disable csrf - make http stateless instead
                .authorizeHttpRequests(request ->
                    request.anyRequest().authenticated()) //no access to any request unless authenticated
                .httpBasic(Customizer.withDefaults()) //implements login for Postman / rest api access
                .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //makes it stateless, doesn't work w form login - every request is new session / requires login
                .build(); //tells spring sec to use this sec filter chain
    }

}
