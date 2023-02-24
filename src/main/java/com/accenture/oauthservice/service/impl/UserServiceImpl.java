package com.accenture.oauthservice.service.impl;

import com.accenture.oauthservice.exception.OAuthServiceException;
import com.accenture.oauthservice.model.ErrorResponse;
import com.accenture.oauthservice.model.dto.UserDTO;
import com.accenture.oauthservice.service.UserService;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Mapper mapper;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private static String DOMAIN = "http://user-service";

    private static String GET_USER_BY_NAME = "/user/getUserByName/";

    @Override
    public UserDTO getUserByName(String name) throws OAuthServiceException {
        try{
            String url = DOMAIN + GET_USER_BY_NAME + name;
            restTemplate.setMessageConverters(getJsonMessageConverters());
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
            ResponseEntity<ErrorResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, ErrorResponse.class);
            return mapper.map(getResponse(responseEntity), UserDTO.class);
        } catch (Throwable t) {
            logger.error("[Error " + t.getClass() + "] " + t.getMessage());
            throw t;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            UserDTO user = this.getUserByName(username);
            if(user == null) {
                logger.error("No existe el usuario " + username);
                throw new UsernameNotFoundException("No existe el usuario");
            }
            List<GrantedAuthority> authorities = user.getRoles()
                    .stream()
                    .map(rol -> new SimpleGrantedAuthority(rol.getName()))
                    .collect(Collectors.toList());
            return new User(user.getName(), user.getPassword(), user.getEnabled(), Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, authorities);
        } catch (OAuthServiceException e) {
            throw new RuntimeException(e);
        } catch (Throwable t) {
            logger.error("[Error " + t.getClass() + "] " + t.getMessage());
            throw t;
        }
    }

    private Object getResponse(ResponseEntity<ErrorResponse> responseEntity) throws OAuthServiceException {
        ErrorResponse errorResponse = responseEntity.getBody();
        if(responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            if(errorResponse.getCode() == 0) {
                return errorResponse.getData();
            } else {
                throw new OAuthServiceException(errorResponse.getDesc(), errorResponse.getCode());
            }
        } else {
            throw new OAuthServiceException(errorResponse.getDesc(), errorResponse.getCode());
        }
    }

    private List<HttpMessageConverter<?>> getJsonMessageConverters() {
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new MappingJackson2HttpMessageConverter());
        return converters;
    }

}
