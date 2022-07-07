package com.example.moviecatalog;

import com.example.moviecatalog.converter.RoleConverter;
import com.example.moviecatalog.converter.UserConverter;
import com.example.moviecatalog.dao.CredentialsDao;
import com.example.moviecatalog.dao.RoleDao;
import com.example.moviecatalog.dao.UserDao;
import com.example.moviecatalog.dto.CredentialsDto;
import com.example.moviecatalog.dto.RoleDto;
import com.example.moviecatalog.dto.UserDto;
import com.example.moviecatalog.entity.CredentialsEntity;
import com.example.moviecatalog.entity.RoleEntity;
import com.example.moviecatalog.entity.UserEntity;
import com.example.moviecatalog.exception.CredentialsConflictException;
import com.example.moviecatalog.exception.NotFoundException;
import com.example.moviecatalog.exception.UserValidationException;
import com.example.moviecatalog.service.CredentialsService;
import com.example.moviecatalog.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class UserServiceTest {

    private final Long DEFAULT_ROLE_ID = 0L;
    private final Long ADMIN_ROLE_ID = 1L;
    private final Long NOT_EXISTED_ROLE_ID = 2L;
    private final Long INCOMING_USER_ID = 55L;
    private final Long NOT_EXIST_USER_ID = 77L;
    private final Long OUTCOMING_USER_ID = 66L;
    private final Long EXISTING_USER_ID = 44L;
    private final RoleEntity DEFAULT_ROLE = RoleEntity.
            builder().
            id(DEFAULT_ROLE_ID).
            name("DEFAULT").
            build();
    private final RoleEntity ADMIN_ROLE = RoleEntity.
            builder().
            id(ADMIN_ROLE_ID).
            name("ADMIN").
            build();
    private final String USER_NOT_FOUND_MESSAGE = "User not found!";
    private final String ROLES_VALIDATIONS_FALSE_MESSAGE = "User must have at list one role!";
    private final String ROLE_NOT_FOUND_MESSAGE = "Role not found!";
    private final String CREDENTIALS_CONFLICT_MESSAGE = "User with this user name is already exist";
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Mock
    private CredentialsDao credentialsDao;
    @Mock
    private UserDao userDao;
    @Mock
    private RoleDao roleDao;
    @Mock
    private CredentialsService credentialsService;
    @Mock
    private UserConverter userConverter;
    @Mock
    private RoleConverter roleConverter;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void prepareToTests() {
        MockitoAnnotations.openMocks(this);
        doCallRealMethod().when(roleConverter).convert(any());
        doCallRealMethod().when(userConverter).convertEntityToDto(any());
        doCallRealMethod().when(userConverter).convertDtoToEntity(any());
        doCallRealMethod().when(userConverter).fillEntityFromDto(any(), any());
        when(userDao.existsById(NOT_EXIST_USER_ID)).thenReturn(Boolean.FALSE);
        when(userDao.existsById(EXISTING_USER_ID)).thenReturn(Boolean.TRUE);
        when(roleDao.getReferenceById(DEFAULT_ROLE_ID)).thenReturn(DEFAULT_ROLE);
        when(roleDao.getReferenceById(ADMIN_ROLE_ID)).thenReturn(ADMIN_ROLE);
        when(roleDao.existsById(DEFAULT_ROLE_ID)).thenReturn(Boolean.TRUE);
        when(roleDao.existsById(ADMIN_ROLE_ID)).thenReturn(Boolean.TRUE);
        when(roleDao.existsById(NOT_EXISTED_ROLE_ID)).thenReturn(Boolean.FALSE);
    }

    @Test
    public void addUser_newUser_createdUserDto() {
        UserDto incomingDto = new UserDto(INCOMING_USER_ID,
                "Test user",
                "Test lastname",
                null,
                Boolean.TRUE);
        UserEntity newUserEntity = UserEntity.builder()
                .Id(null)
                .name(incomingDto.getName())
                .description(incomingDto.getDescription())
                .lastname(incomingDto.getLastname())
                .active(incomingDto.getActive())
                .roles(new HashSet<>())
                .build();
        UserEntity userWithDefaultRole = UserEntity.builder()
                .Id(null)
                .name(incomingDto.getName())
                .description(incomingDto.getDescription())
                .lastname(incomingDto.getLastname())
                .active(incomingDto.getActive())
                .roles(new HashSet<>())
                .build();
        userWithDefaultRole.addRole(DEFAULT_ROLE);
        UserEntity savedNewUserEntity = UserEntity.builder()
                .Id(OUTCOMING_USER_ID)
                .name(incomingDto.getName())
                .description(incomingDto.getDescription())
                .lastname(incomingDto.getLastname())
                .active(incomingDto.getActive())
                .roles(new HashSet<>())
                .build();
        savedNewUserEntity.addRole(DEFAULT_ROLE);
        UserDto outcomingUserDto = new UserDto(OUTCOMING_USER_ID,
                "Test user",
                "Test lastname",
                null,
                Boolean.TRUE);
        when(userConverter.convertDtoToEntity(incomingDto)).thenReturn(newUserEntity);
        when(userDao.save(userWithDefaultRole)).thenReturn(savedNewUserEntity);
        when(userConverter.convertEntityToDto(savedNewUserEntity)).thenReturn(outcomingUserDto);

        UserDto updatedDto = userService.addUser(incomingDto);

        assertNotNull(updatedDto);
        assertNull(incomingDto.getId());
        assertEquals(updatedDto, outcomingUserDto);
        assertFalse(savedNewUserEntity.getRoles().isEmpty());
        verify(userConverter).convertDtoToEntity(incomingDto);
        verify(userConverter).convertEntityToDto(savedNewUserEntity);
        verify(roleDao).getReferenceById(DEFAULT_ROLE_ID);
        verify(userDao).save(userWithDefaultRole);
    }

    @Test
    public void updateUser_existingUser_updatedUserDto() {
        String newName = "new name";
        String existingName = "Test user";
        UserDto incomingDto = UserDto.builder()
                .id(null)
                .name(newName)
                .build();
        UserEntity existingUserEntity = UserEntity.builder()
                .Id(EXISTING_USER_ID)
                .name(existingName)
                .roles(new HashSet<>())
                .build();
        existingUserEntity.addRole(DEFAULT_ROLE);
        UserEntity updatedUserEntity = UserEntity.builder()
                .Id(EXISTING_USER_ID)
                .name(newName)
                .roles(new HashSet<>())
                .build();
        updatedUserEntity.addRole(DEFAULT_ROLE);
        when(userDao.getReferenceById(EXISTING_USER_ID)).thenReturn(existingUserEntity);
        when(userDao.save(updatedUserEntity)).thenReturn(updatedUserEntity);

        UserDto resultDto = userService.updateUser(EXISTING_USER_ID, incomingDto);

        assertNotNull(resultDto);
        assertEquals(resultDto.getId(), EXISTING_USER_ID);
        assertEquals(resultDto.getName(), newName);
        verify(userDao).existsById(EXISTING_USER_ID);
        verify(userDao).getReferenceById(EXISTING_USER_ID);
        verify(userDao).save(updatedUserEntity);
        verify(userConverter).convertEntityToDto(updatedUserEntity);
        verify(userConverter).fillEntityFromDto(existingUserEntity, incomingDto);
    }

    @Test
    public void updateUser_notExistingUser_throwNotFoundException() {
        UserDto incomingDto = UserDto.builder().build();

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.updateUser(NOT_EXIST_USER_ID, incomingDto));

        assertEquals(USER_NOT_FOUND_MESSAGE, exception.getMessage());
        verify(userDao).existsById(NOT_EXIST_USER_ID);
        verify(userDao, never()).getReferenceById(any());
        verify(userDao, never()).save(any());
        verify(userConverter, never()).convertEntityToDto(any());
        verify(userConverter, never()).fillEntityFromDto(any(), any());
    }

    @Test
    public void getAllUsers_getListOfUsersDTOs_setUserDto() {
        UserEntity existingUserEntity1 = UserEntity.builder()
                .Id(11L)
                .roles(new HashSet<>())
                .build();
        existingUserEntity1.addRole(DEFAULT_ROLE);
        UserEntity existingUserEntity2 = UserEntity.builder()
                .Id(12L)
                .roles(new HashSet<>())
                .build();
        existingUserEntity2.addRole(DEFAULT_ROLE);
        List<UserEntity> listOfEntity = new ArrayList<>();
        listOfEntity.add(existingUserEntity1);
        listOfEntity.add(existingUserEntity2);
        List<UserDto> expectedList = listOfEntity.stream()
                .map(userConverter::convertEntityToDto)
                .collect(Collectors.toList());
        when(userDao.findAll()).thenReturn(listOfEntity);

        List<UserDto> resultList = userService.getUsers();

        assertArrayEquals(resultList.toArray(), expectedList.toArray());
        assertEquals(2, resultList.size());
        verify(userDao).findAll();
        verify(userConverter, times(listOfEntity.size() * 2)).convertEntityToDto(any());
    }

    @Test
    public void getUsersRoles_existingUser_setRolesDTOs() {

        UserEntity existingUserEntity = UserEntity.builder()
                .Id(EXISTING_USER_ID)
                .roles(new HashSet<>())
                .build();
        existingUserEntity.addRole(DEFAULT_ROLE);
        when(userDao.getReferenceById(EXISTING_USER_ID)).thenReturn(existingUserEntity);
        Set<RoleDto> expectedSet = new HashSet<>();
        expectedSet.add(roleConverter.convert(DEFAULT_ROLE));

        Set<RoleDto> resultRoles = userService.getUsersRoles(EXISTING_USER_ID);

        assertNotNull(resultRoles);
        assertFalse(resultRoles.isEmpty());
        assertArrayEquals(expectedSet.toArray(), resultRoles.toArray());
        verify(roleConverter, times(2)).convert(DEFAULT_ROLE);
        verify(userDao).existsById(EXISTING_USER_ID);
        verify(userDao).getReferenceById(EXISTING_USER_ID);
    }

    @Test
    public void getUsersRoles_notExistingUser_throwNotFoundException() {

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.getUsersRoles(NOT_EXIST_USER_ID));

        assertEquals(USER_NOT_FOUND_MESSAGE, exception.getMessage());
        verify(userDao).existsById(NOT_EXIST_USER_ID);
        verify(roleConverter, never()).convert(any());
        verify(userDao, never()).getReferenceById(NOT_EXIST_USER_ID);
    }

    @Test
    public void setUsersRoles_existingUserValidRolesSet_setRolesDTOs() {
        UserEntity existingUserEntity = UserEntity.builder()
                .Id(EXISTING_USER_ID)
                .roles(new HashSet<>())
                .build();
        existingUserEntity.addRole(DEFAULT_ROLE);
        Set<RoleEntity> expectedRolesEntitySet = new HashSet<>();
        expectedRolesEntitySet.add(DEFAULT_ROLE);
        expectedRolesEntitySet.add(ADMIN_ROLE);
        Set<RoleDto> convertedRoles = expectedRolesEntitySet
                .stream()
                .map(roleConverter::convert)
                .collect(Collectors.toSet());
        Set<RoleDto> expectedRoles = expectedRolesEntitySet
                .stream()
                .map(roleConverter::convert)
                .collect(Collectors.toSet());
        when(userDao.getReferenceById(EXISTING_USER_ID)).thenReturn(existingUserEntity);
        when(userDao.save(existingUserEntity)).thenReturn(existingUserEntity);

        Set<RoleDto> resultSet = userService.setUsersRoles(EXISTING_USER_ID, convertedRoles);

        assertFalse(resultSet.isEmpty());
        assertArrayEquals(resultSet.toArray(), expectedRoles.toArray());
        assertFalse(existingUserEntity.getRoles().isEmpty());
        assertArrayEquals(existingUserEntity.getRoles().toArray(), expectedRolesEntitySet.toArray());
        verify(userDao).existsById(EXISTING_USER_ID);
        verify(userDao).getReferenceById(EXISTING_USER_ID);
        verify(roleDao).getReferenceById(ADMIN_ROLE_ID);
        verify(roleDao).getReferenceById(DEFAULT_ROLE_ID);
        verify(roleDao).existsById(ADMIN_ROLE_ID);
        verify(roleDao).existsById(DEFAULT_ROLE_ID);
        verify(userDao).save(eq(existingUserEntity));
    }

    @Test
    public void setUsersRoles_notExistingUserValidRolesSet_throwNotFoundException() {
        Set<RoleEntity> expectedRolesEntitySet = new HashSet<>();
        expectedRolesEntitySet.add(DEFAULT_ROLE);
        expectedRolesEntitySet.add(ADMIN_ROLE);
        Set<RoleDto> convertedRoles = expectedRolesEntitySet
                .stream()
                .map(roleConverter::convert)
                .collect(Collectors.toSet());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.setUsersRoles(NOT_EXIST_USER_ID, convertedRoles));

        assertEquals(USER_NOT_FOUND_MESSAGE, exception.getMessage());
        verify(userDao).existsById(NOT_EXIST_USER_ID);
        verify(userDao, never()).getReferenceById(any());
        verify(roleDao, never()).existsById(any());
        verify(roleDao, never()).getReferenceById(any());
        verify(userDao, never()).save(any());
    }

    @Test
    public void setUsersRoles_existingUserEmptyRolesSet_throwUserValidationException() {
        UserEntity existingUserEntity = UserEntity.builder()
                .Id(EXISTING_USER_ID)
                .roles(new HashSet<>())
                .build();
        when(userDao.getReferenceById(EXISTING_USER_ID)).thenReturn(existingUserEntity);
        existingUserEntity.addRole(DEFAULT_ROLE);

        UserValidationException exception = assertThrows(UserValidationException.class, () -> userService.setUsersRoles(EXISTING_USER_ID, new HashSet<>()));

        assertEquals(ROLES_VALIDATIONS_FALSE_MESSAGE, exception.getMessage());
        verify(userDao).existsById(EXISTING_USER_ID);
        verify(userDao).getReferenceById(EXISTING_USER_ID);
        verify(roleDao, never()).existsById(any());
        verify(roleDao, never()).getReferenceById(any());
        verify(userDao, never()).save(any());
    }

    @Test
    public void setUsersRoles_existingUserNotExistingRole_throwNotFoundException() {
        UserEntity existingUserEntity = UserEntity.builder()
                .Id(EXISTING_USER_ID)
                .roles(new HashSet<>())
                .build();
        existingUserEntity.addRole(DEFAULT_ROLE);
        RoleDto notExistedRole = new RoleDto(NOT_EXISTED_ROLE_ID, null);
        Set<RoleDto> troyRoleSet = new HashSet<>();
        troyRoleSet.add(roleConverter.convert(DEFAULT_ROLE));
        troyRoleSet.add(notExistedRole);
        when(userDao.getReferenceById(EXISTING_USER_ID)).thenReturn(existingUserEntity);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.setUsersRoles(EXISTING_USER_ID, troyRoleSet));

        assertEquals(ROLE_NOT_FOUND_MESSAGE, exception.getMessage());
        verify(userDao).existsById(EXISTING_USER_ID);
        verify(roleDao).existsById(NOT_EXISTED_ROLE_ID);
        verify(userDao, never()).getReferenceById(any());
        verify(roleDao, never()).getReferenceById(any());
        verify(userDao, never()).save(any());
    }

    @Test
    public void addRoleToUser_existingUserAndRole_updatedSetRoles() {
        UserEntity existingUserEntity = UserEntity.builder()
                .Id(EXISTING_USER_ID)
                .roles(new HashSet<>())
                .build();
        existingUserEntity.addRole(DEFAULT_ROLE);
        Set<RoleDto> expectedResult = new HashSet<>();
        expectedResult.add(roleConverter.convert(DEFAULT_ROLE));
        expectedResult.add(roleConverter.convert(ADMIN_ROLE));
        when(userDao.getReferenceById(EXISTING_USER_ID)).thenReturn(existingUserEntity);
        when(userDao.save(existingUserEntity)).thenReturn(existingUserEntity);

        Set<RoleDto> resultSetDtos = userService.addRoleToUser(EXISTING_USER_ID, roleConverter.convert(ADMIN_ROLE));

        assertNotNull(resultSetDtos);
        assertArrayEquals(expectedResult.toArray(), resultSetDtos.toArray());
        verify(userDao).getReferenceById(EXISTING_USER_ID);
        verify(userDao).existsById(EXISTING_USER_ID);
        verify(roleDao).existsById(ADMIN_ROLE_ID);
        verify(userDao).save(eq(existingUserEntity));
        verify(roleConverter, times(2)).convert(DEFAULT_ROLE);
        verify(roleConverter, times(3)).convert(ADMIN_ROLE);
        verify(roleDao).getReferenceById(ADMIN_ROLE_ID);
    }

    @Test
    public void addRoleToUser_notExistingUserExistRole_UserNotFoundException() {
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> userService.addRoleToUser(NOT_EXIST_USER_ID, roleConverter.convert(ADMIN_ROLE)));

        assertEquals(USER_NOT_FOUND_MESSAGE, exception.getMessage());
        verify(userDao).existsById(NOT_EXIST_USER_ID);
        verify(roleConverter).convert(ADMIN_ROLE);
        verify(userDao, never()).getReferenceById(any());
        verify(roleDao, never()).getReferenceById(any());
        verify(roleDao, never()).existsById(any());
        verify(userDao, never()).save(any());
    }

    @Test
    public void addRoleToUser_ExistUserNotExistRole_RoleNotFoundException() {
        UserEntity existingUserEntity = UserEntity.builder()
                .Id(EXISTING_USER_ID)
                .roles(new HashSet<>())
                .build();
        existingUserEntity.addRole(DEFAULT_ROLE);
        RoleDto notExistedRole = new RoleDto(NOT_EXISTED_ROLE_ID, null);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService
                .addRoleToUser(EXISTING_USER_ID, notExistedRole));

        assertEquals(ROLE_NOT_FOUND_MESSAGE, exception.getMessage());
        verify(userDao).existsById(EXISTING_USER_ID);
        verify(roleDao).existsById(NOT_EXISTED_ROLE_ID);
        verify(roleConverter, never()).convert(any());
        verify(userDao, never()).getReferenceById(any());
        verify(roleDao, never()).getReferenceById(any());
        verify(userDao, never()).save(any());
    }

    @Test
    public void addRoleToUser_NotExistUserNotExistRole_UserNotFoundException() {
        RoleDto notExistedRole = new RoleDto(NOT_EXISTED_ROLE_ID, null);

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> userService.addRoleToUser(NOT_EXIST_USER_ID, notExistedRole));

        assertEquals(USER_NOT_FOUND_MESSAGE, exception.getMessage());
        verify(userDao).existsById(NOT_EXIST_USER_ID);
        verify(roleConverter, never()).convert(any());
        verify(userDao, never()).getReferenceById(any());
        verify(roleDao, never()).getReferenceById(any());
        verify(roleDao, never()).existsById(any());
        verify(userDao, never()).save(any());
    }

    @Test
    public void setCredentials_existingUserValidCredentials_runWithoutException() {
        String login = "test user";
        String pass = "pass";
        UserEntity existingUserEntity = UserEntity.builder()
                .Id(EXISTING_USER_ID)
                .build();
        CredentialsDto credentialsDto = new CredentialsDto(login, pass);
        CredentialsEntity credentialsEntity = new CredentialsEntity();
        existingUserEntity.setCredentials(credentialsEntity);
        when(userDao.getReferenceById(EXISTING_USER_ID)).thenReturn(existingUserEntity);
        when(userDao.save(existingUserEntity)).thenReturn(existingUserEntity);
        when(credentialsDao.findByLogin(login)).thenReturn(credentialsEntity);
        when(credentialsService.getCredentials(existingUserEntity, credentialsDto)).thenReturn(credentialsEntity);

        userService.setCredentials(EXISTING_USER_ID, credentialsDto);

        assertNotNull(existingUserEntity.getCredentials());
        assertEquals(existingUserEntity.getCredentials(), credentialsEntity);
        verify(userDao).existsById(EXISTING_USER_ID);
        verify(userDao).getReferenceById(EXISTING_USER_ID);
        verify(credentialsDao).findByLogin(login);
        verify(credentialsService).getCredentials(existingUserEntity, credentialsDto);
        verify(userDao).save(eq(existingUserEntity));
    }

    @Test
    public void setCredentials_notExistingUserValidCredentials_UserNotFoundException() {
        String login = "test user";
        String pass = "pass";
        CredentialsDto credentialsDto = new CredentialsDto(login, pass);

        NotFoundException exception = assertThrows(NotFoundException.class
                , () -> userService.setCredentials(NOT_EXIST_USER_ID, credentialsDto));

        assertEquals(USER_NOT_FOUND_MESSAGE, exception.getMessage());
        verify(userDao).existsById(NOT_EXIST_USER_ID);
        verify(userDao, never()).getReferenceById(any());
        verify(credentialsDao, never()).findByLogin(any());
        verify(credentialsService, never()).getCredentials(any(), any());
        verify(userDao, never()).save(any());
    }

    @Test
    public void setCredentials_existingUserNotValidCredentials_CredentialsConflictException() {
        String login = "test user";
        String pass = "pass";
        UserEntity existingUserEntity = UserEntity.builder()
                .Id(EXISTING_USER_ID)
                .build();
        CredentialsDto credentialsDto = new CredentialsDto(login, pass);
        CredentialsEntity credentialsEntity = new CredentialsEntity();
        when(userDao.getReferenceById(EXISTING_USER_ID)).thenReturn(existingUserEntity);
        when(userDao.save(existingUserEntity)).thenReturn(existingUserEntity);
        when(credentialsDao.findByLogin(login)).thenReturn(credentialsEntity);
        when(credentialsService.getCredentials(existingUserEntity, credentialsDto)).thenReturn(credentialsEntity);

        CredentialsConflictException exception = assertThrows(CredentialsConflictException.class
                , () -> userService.setCredentials(EXISTING_USER_ID, credentialsDto));

        assertEquals(CREDENTIALS_CONFLICT_MESSAGE, exception.getMessage());
        assertNull(existingUserEntity.getCredentials());
        verify(userDao).existsById(EXISTING_USER_ID);
        verify(userDao).getReferenceById(EXISTING_USER_ID);
        verify(credentialsDao).findByLogin(login);
        verify(credentialsService, never()).getCredentials(any(), any());
        verify(userDao, never()).save(any());
    }

    @Test
    public void setCredentials_notExistingUserNotValidCredentials_UserNotFoundException() {
        String login = "test user";
        String pass = "pass";
        UserEntity existingUserEntity = UserEntity.builder()
                .Id(EXISTING_USER_ID)
                .build();
        CredentialsDto credentialsDto = new CredentialsDto(login, pass);
        CredentialsEntity credentialsEntity = new CredentialsEntity();
        when(userDao.getReferenceById(EXISTING_USER_ID)).thenReturn(existingUserEntity);
        when(userDao.save(existingUserEntity)).thenReturn(existingUserEntity);
        when(credentialsDao.findByLogin(login)).thenReturn(credentialsEntity);
        when(credentialsService.getCredentials(existingUserEntity, credentialsDto)).thenReturn(credentialsEntity);

        NotFoundException exception = assertThrows(NotFoundException.class
                , () -> userService.setCredentials(NOT_EXIST_USER_ID, credentialsDto));

        assertEquals(USER_NOT_FOUND_MESSAGE, exception.getMessage());
        verify(userDao).existsById(NOT_EXIST_USER_ID);
        verify(userDao, never()).getReferenceById(any());
        verify(credentialsDao, never()).findByLogin(any());
        verify(credentialsService, never()).getCredentials(any(), any());
        verify(userDao, never()).save(any());
    }
}
