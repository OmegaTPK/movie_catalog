package com.example.moviecatalog;

import com.example.moviecatalog.config.CustomUserDetails;
import com.example.moviecatalog.converter.CustomUserDetailsConverter;
import com.example.moviecatalog.dao.CredentialsDao;
import com.example.moviecatalog.dao.UserDao;
import com.example.moviecatalog.entity.CredentialsEntity;
import com.example.moviecatalog.entity.RoleEntity;
import com.example.moviecatalog.entity.UserEntity;
import com.example.moviecatalog.exception.CredentialsValidationException;
import com.example.moviecatalog.service.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CustomUserDetailsServiceTest {

    private final Long EXISTING_USER_ID = 44L;
    private final String EXISTING_CREDENTIALS_LOGIN = "login";
    private final String NOT_EXISTING_CREDENTIALS_LOGIN = "Bla";
    private final String ENCODED_CREDENTIALS_PASSWORD = "encrypted pass";
    private final String AUTHENTIFICATION_FAIL_MESSAGE = "Authentication failed";
    @Mock
    private UserDao userDao;
    @Mock
    private CredentialsDao credentialsDao;
    @Mock
    private CustomUserDetailsConverter customUserDetailsConverter;
    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;
    private UserEntity existingUserEntity;
    private CredentialsEntity exitingCredentialsEntity;
    private RoleEntity defaultRole;
    private CustomUserDetails customUserDetails;

    @BeforeEach
    public void prepareToTest() {
        MockitoAnnotations.openMocks(this);
        exitingCredentialsEntity = CredentialsEntity.builder()
                .login(EXISTING_CREDENTIALS_LOGIN)
                .password(ENCODED_CREDENTIALS_PASSWORD)
                .build();
        defaultRole = RoleEntity.builder().id(0L).name("DEFAULT").build();
        existingUserEntity = UserEntity.builder()
                .id(EXISTING_USER_ID)
                .active(true)
                .roles(new HashSet<>(Collections.singleton(defaultRole)))
                .credentials(exitingCredentialsEntity)
                .build();
        doCallRealMethod().when(customUserDetailsConverter).fromUserEntityToCustomUserDetails(existingUserEntity);
        when(credentialsDao.findByLogin(EXISTING_CREDENTIALS_LOGIN)).thenReturn(exitingCredentialsEntity);
        when(userDao.findByCredentials(exitingCredentialsEntity)).thenReturn(existingUserEntity);
        customUserDetails = CustomUserDetails.builder()
                .login(exitingCredentialsEntity.getLogin())
                .password(exitingCredentialsEntity.getPassword())
                .grantedAuthorities(existingUserEntity
                        .getRoles()
                        .stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toSet()))
                .build();
    }

    @Test
    public void loadUserByUsername_validUsernameValidCredentialsInUser_customUserDetail() {
        CustomUserDetails result = customUserDetailsService.loadUserByUsername(EXISTING_CREDENTIALS_LOGIN);

        assertEquals(customUserDetails, result);
        verify(credentialsDao).findByLogin(EXISTING_CREDENTIALS_LOGIN);
        verify(userDao).findByCredentials(exitingCredentialsEntity);
        verify(customUserDetailsConverter).fromUserEntityToCustomUserDetails(existingUserEntity);
    }

    @Test
    public void loadUserByUsername_notValidUsername_authentificationFaildException() {

        CredentialsValidationException exception = assertThrows(CredentialsValidationException.class,
                () -> customUserDetailsService.loadUserByUsername(NOT_EXISTING_CREDENTIALS_LOGIN));

        assertEquals(AUTHENTIFICATION_FAIL_MESSAGE, exception.getMessage());
        verify(credentialsDao).findByLogin(NOT_EXISTING_CREDENTIALS_LOGIN);
        verify(userDao, never()).findByCredentials(any());
        verify(customUserDetailsConverter, never()).fromUserEntityToCustomUserDetails(any());
    }

    @Test
    public void loadUserByUsername_validLoginNotFoundUser_authentificationFaildException() {
        when(userDao.findByCredentials(exitingCredentialsEntity)).thenReturn(null);

        CredentialsValidationException exception = assertThrows(CredentialsValidationException.class,
                () -> customUserDetailsService.loadUserByUsername(EXISTING_CREDENTIALS_LOGIN));

        assertEquals(AUTHENTIFICATION_FAIL_MESSAGE, exception.getMessage());
        verify(credentialsDao).findByLogin(EXISTING_CREDENTIALS_LOGIN);
        verify(userDao).findByCredentials(exitingCredentialsEntity);
        verify(customUserDetailsConverter, never()).fromUserEntityToCustomUserDetails(any());
    }

    @Test
    public void loadUserByUsername_notActiveUser_authentificationFaildException() {
        existingUserEntity.setActive(Boolean.FALSE);

        CredentialsValidationException exception = assertThrows(CredentialsValidationException.class,
                () -> customUserDetailsService.loadUserByUsername(EXISTING_CREDENTIALS_LOGIN));

        assertEquals(AUTHENTIFICATION_FAIL_MESSAGE, exception.getMessage());
        verify(credentialsDao).findByLogin(EXISTING_CREDENTIALS_LOGIN);
        verify(userDao).findByCredentials(exitingCredentialsEntity);
        verify(customUserDetailsConverter, never()).fromUserEntityToCustomUserDetails(any());
    }
}
