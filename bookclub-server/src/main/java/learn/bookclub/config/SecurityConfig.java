package learn.bookclub.config;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration //tell Spring this is a config file, and has to search for configs here
@EnableWebSecurity //tell Spring Security to not use default flow, but instead use the flow here
public class SecurityConfig {

    private final int HASH_STRENGTH = 12;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.csrf(customizer -> customizer.disable()) //disable csrf - make http stateless instead
                .authorizeHttpRequests(request -> request
                        // public: login + signup, books (read only), meetings (read only)
                        .requestMatchers("/api/user/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/books/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/meetings/**").permitAll()

                        //writes to books + meetings: members only
                        .requestMatchers(HttpMethod.POST, "/api/books/**", "/api/meetings/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/books/**", "/api/meetings/**").authenticated()

                        //members only for all access - nominations, votes
                        .requestMatchers("api/nominations/**").authenticated()


                        //no access to any request unless authenticated
                        .anyRequest().authenticated())
                    //TODO: add more requestMatchers (public reads, members-only for all reqs, members-only for writes)
                .httpBasic(Customizer.withDefaults()) //implements login for Postman / rest api access
                .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //makes it stateless, doesn't work w form login - every request is new session / requires login
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) //add JWT filter before usernamepw auth filter
                .build(); //tells spring sec to use this sec filter chain
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(); //for connecting with a database
        provider.setPasswordEncoder(new BCryptPasswordEncoder(HASH_STRENGTH)); //bcrypt encoder to check pw
        provider.setUserDetailsService(userDetailsService); //uses our custom userdetailsservice (MyUserDetailsService)
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception { //get a hold on auth manager, will talk to auth provider
        return config.getAuthenticationManager();
    }

    //TODO: add cors config to allow React client to make requests?


}
