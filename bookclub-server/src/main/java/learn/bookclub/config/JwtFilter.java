package learn.bookclub.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import learn.bookclub.services.JWTService;

import learn.bookclub.services.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter { //onceperrequestfilter activates once in filter chain

    @Autowired
    private JWTService jwtService;

    @Autowired
    ApplicationContext context;

    //abstract method we need to implement
    //we only want to work with request - get jwt token from request
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //client-side: Authorization: Bearer {token}
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtService.extractUsername(token); //decodes token and gets username
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) { //check it hasn't already been authenticated
            //validate token
            //if valid, create authentication object

            UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(username); //gets user from db

            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken); //adding the token in the chain
            }
        }
        filterChain.doFilter(request, response); //continue the filter chain
    }
}
