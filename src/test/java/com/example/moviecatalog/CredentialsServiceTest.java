package com.example.moviecatalog;

import com.example.moviecatalog.config.jwt.JwtProvider;
import com.example.moviecatalog.converter.CredentialsConverter;
import com.example.moviecatalog.converter.TokenConverter;
import com.example.moviecatalog.dao.CredentialsDao;
import com.example.moviecatalog.dao.UserDao;
import com.example.moviecatalog.dto.CredentialsDto;
import com.example.moviecatalog.dto.TokenDto;
import com.example.moviecatalog.entity.CredentialsEntity;
import com.example.moviecatalog.entity.UserEntity;
import com.example.moviecatalog.exception.CredentialsValidationException;
import com.example.moviecatalog.service.CredentialsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CredentialsServiceTest {

    private final Long EXISTING_USER_ID = 44L;
    private final String EXISTING_CREDENTIALS_LOGIN = "login";
    private final String NOT_EXISTING_CREDENTIALS_LOGIN = "Bla";
    private final String EXISTING_CREDENTIALS_PASSWORD = "password";
    private final String ENCODED_CREDENTIALS_PASSWORD = "encrypted pass";
    private final String TOKEN_STRING = "token string";
    private final String AUTHENTIFICATION_FAIL_MESSAGE = "Authentication failed";
    @Mock
    private CredentialsDao credentialsDao;
    @Mock
    private CredentialsConverter credentialsConverter;
    @Mock
    private UserDao userDao;
    @Mock
    private JwtProvider jwtProvider;
    @Mock
    private TokenConverter tokenConverter;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private CredentialsService credentialsService;
    private UserEntity existingUserEntity;
    private CredentialsEntity exitingCredentialsEntity;
    private CredentialsDto incomingCredentials;


    @BeforeEach
    public void prepareToTest() {
        MockitoAnnotations.openMocks(this);

        existingUserEntity = UserEntity.builder()
                .id(EXISTING_USER_ID)
                .active(true)
                .roles(new HashSet<>())
                .build();
        incomingCredentials = new CredentialsDto(EXISTING_CREDENTIALS_LOGIN, EXISTING_CREDENTIALS_PASSWORD);
        exitingCredentialsEntity = CredentialsEntity.builder()
                .login(EXISTING_CREDENTIALS_LOGIN)
                .password(ENCODED_CREDENTIALS_PASSWORD)
                .build();
        doCallRealMethod().when(credentialsConverter).newCredentials(any());
        doCallRealMethod().when(credentialsConverter).fillFromDto(any(), any());
        doCallRealMethod().when(tokenConverter).convert(any());
        when(passwordEncoder.matches(ENCODED_CREDENTIALS_PASSWORD, ENCODED_CREDENTIALS_PASSWORD)).thenReturn(Boolean.TRUE);
        when(credentialsDao.findByLogin(EXISTING_CREDENTIALS_LOGIN)).thenReturn(exitingCredentialsEntity);
        when(userDao.findByCredentials(exitingCredentialsEntity)).thenReturn(existingUserEntity);
        when(credentialsDao.save(eq(exitingCredentialsEntity))).thenReturn(exitingCredentialsEntity);
        when(passwordEncoder.encode(EXISTING_CREDENTIALS_PASSWORD)).thenReturn(ENCODED_CREDENTIALS_PASSWORD);
        when(jwtProvider.generateToken(EXISTING_CREDENTIALS_LOGIN)).thenReturn(TOKEN_STRING);
    }

    @Test
    public void getCredentials_userCredentialsNull_createdCredentialsEntity() {

        CredentialsEntity resultCredentials = credentialsService.getCredentials(existingUserEntity, incomingCredentials);

        assertNotNull(resultCredentials);
        assertEquals(ENCODED_CREDENTIALS_PASSWORD, resultCredentials.getPassword());
        assertEquals(EXISTING_CREDENTIALS_LOGIN, resultCredentials.getLogin());
        verify(credentialsConverter).newCredentials(incomingCredentials);
        verify(credentialsDao).save(exitingCredentialsEntity);
        verify(passwordEncoder).encode(EXISTING_CREDENTIALS_PASSWORD);
        verify(credentialsConverter, never()).fillFromDto(any(), any());
    }

    @Test
    public void getCredentials_userCredentialsFilled_updatedCredentialsEntity() {
        existingUserEntity.setCredentials(exitingCredentialsEntity);

        CredentialsEntity resultCredentials = credentialsService.getCredentials(existingUserEntity, incomingCredentials);

        assertNotNull(resultCredentials);
        assertEquals(ENCODED_CREDENTIALS_PASSWORD, resultCredentials.getPassword());
        assertEquals(EXISTING_CREDENTIALS_LOGIN, resultCredentials.getLogin());
        verify(credentialsDao).save(exitingCredentialsEntity);
        verify(credentialsConverter).fillFromDto(incomingCredentials, exitingCredentialsEntity);
        verify(passwordEncoder).encode(EXISTING_CREDENTIALS_PASSWORD);
        verify(credentialsConverter, never()).newCredentials(any());
    }

    @Test
    public void getTokenFromCredentials_validCredentials_tokenDto() {
        TokenDto expectedToken = new TokenDto(TOKEN_STRING);

        TokenDto resultToken = credentialsService.getTokenFromCredentials(incomingCredentials);

        assertNotNull(resultToken);
        assertEquals(expectedToken, resultToken);
        verify(jwtProvider).generateToken(EXISTING_CREDENTIALS_LOGIN);
        verify(tokenConverter).convert(eq(TOKEN_STRING));
        verify(credentialsDao).findByLogin(EXISTING_CREDENTIALS_LOGIN);
        verify(userDao).findByCredentials(exitingCredentialsEntity);
        verify(passwordEncoder).matches(ENCODED_CREDENTIALS_PASSWORD, ENCODED_CREDENTIALS_PASSWORD);
    }

    @Test
    public void getTokenFromCredentials_NotExistingLogin_CredentialsValidationException() {
        incomingCredentials.setLogin(NOT_EXISTING_CREDENTIALS_LOGIN);

        CredentialsValidationException exception = assertThrows(CredentialsValidationException.class,
                () -> credentialsService.getTokenFromCredentials(incomingCredentials));

        assertEquals(AUTHENTIFICATION_FAIL_MESSAGE, exception.getMessage());
        verify(credentialsDao).findByLogin(NOT_EXISTING_CREDENTIALS_LOGIN);
        verify(userDao, never()).findByCredentials(any());
        verify(passwordEncoder, never()).matches(any(), any());
        verify(jwtProvider, never()).generateToken(any());
        verify(tokenConverter, never()).convert(any());
    }

    @Test
    public void getTokenFromCredentials_notValidPassword_CredentialsValidationException() {
        when(passwordEncoder.matches(ENCODED_CREDENTIALS_PASSWORD, ENCODED_CREDENTIALS_PASSWORD))
                .thenReturn(Boolean.FALSE);

        CredentialsValidationException exception = assertThrows(CredentialsValidationException.class,
                () -> credentialsService.getTokenFromCredentials(incomingCredentials));

        assertEquals(AUTHENTIFICATION_FAIL_MESSAGE, exception.getMessage());
        verify(credentialsDao).findByLogin(EXISTING_CREDENTIALS_LOGIN);
        verify(userDao).findByCredentials(exitingCredentialsEntity);
        verify(passwordEncoder).matches(ENCODED_CREDENTIALS_PASSWORD, ENCODED_CREDENTIALS_PASSWORD);
        verify(jwtProvider, never()).generateToken(any());
        verify(tokenConverter, never()).convert(any());
    }

    @Test
    public void getTokenFromCredentials_notActiveUser_CredentialsValidationException() {
        existingUserEntity.setActive(Boolean.FALSE);

        CredentialsValidationException exception = assertThrows(CredentialsValidationException.class,
                () -> credentialsService.getTokenFromCredentials(incomingCredentials));

        assertEquals(AUTHENTIFICATION_FAIL_MESSAGE, exception.getMessage());
        verify(credentialsDao).findByLogin(EXISTING_CREDENTIALS_LOGIN);
        verify(userDao).findByCredentials(exitingCredentialsEntity);
        verify(passwordEncoder).matches(ENCODED_CREDENTIALS_PASSWORD, ENCODED_CREDENTIALS_PASSWORD);
        verify(jwtProvider, never()).generateToken(any());
        verify(tokenConverter, never()).convert(any());
    }
}
