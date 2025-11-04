package br.com.paulopinheiro.springmvc.security;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class DefaultAuthenticationProvider implements AuthenticationProvider {
    @Autowired private UserDetailsService userDetailsService;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        System.out.println("===== In Authentication Provider method =====");

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        System.out.println("Authentication object: " + authentication);
        System.out.println("User Name: " + username);
        System.out.println("Password: " + password);
        System.out.println("authentication.getDetails(): " + authentication.getDetails());
        System.out.println("authentication.getPrincipal(): " + authentication.getPrincipal());
        System.out.println("authentication.getAuthorieis(): " + authentication.getAuthorities());

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (Optional.ofNullable(userDetails).isPresent()) {
            if (isPasswordValid(userDetails, password)) {
                return new UsernamePasswordAuthenticationToken(username,password, userDetails.getAuthorities());
            } else {
                throw new BadCredentialsException("Incorrect login/password");
            }
        } else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        System.out.println("===== in supports() method =====");
        System.out.println("Authentication class: " + authentication);
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private boolean isPasswordValid(UserDetails userDetails, String password) {
        return passwordEncoder.matches(password, userDetails.getPassword());
    }
}
