package com.ssubijana.roleauthorization.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssubijana.roleauthorization.domain.ResponseInfoJwtDTO;
import com.ssubijana.roleauthorization.utils.TokenProvider;
import com.ssubijana.roleauthorization.web.presentation.AuthorizationRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static com.ssubijana.roleauthorization.utils.Constants.HEADER_AUTHORIZATION_KEY;
import static com.ssubijana.roleauthorization.utils.Constants.TOKEN_BEARER_PREFIX;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private TokenProvider tokenProvider;

    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        tokenProvider = new TokenProvider();
        super.setAuthenticationFailureHandler(new JwtAuthenticationFailureHandler());
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            AuthorizationRequest userCredentials = new ObjectMapper()
                    .readValue(request.getInputStream(), AuthorizationRequest.class);

            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    userCredentials.getUserName(), userCredentials.getPassword()));
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        String token = tokenProvider.generateToken(authResult);
        response.setContentType("application/json");

        response.addHeader(HEADER_AUTHORIZATION_KEY, TOKEN_BEARER_PREFIX + " " + token);
        response.getWriter().append(ResponseInfoJwtDTO.Create(response));
    }

    private void jsonFormat(HttpServletResponse response, Map<?, ?> responseMap) throws IOException {
        new ObjectMapper().writeValue(response.getOutputStream(), responseMap);
    }
}
