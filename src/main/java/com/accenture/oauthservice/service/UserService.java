package com.accenture.oauthservice.service;

import com.accenture.oauthservice.exception.OAuthServiceException;
import com.accenture.oauthservice.model.dto.UserDTO;

public interface UserService {

    UserDTO getUserByName(String name) throws OAuthServiceException;

}
