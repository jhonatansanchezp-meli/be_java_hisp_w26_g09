package com.meli.be_java_hisp_w26_g09.service.impl;

import com.meli.be_java_hisp_w26_g09.dto.UserDTO;
import com.meli.be_java_hisp_w26_g09.entity.User;
import com.meli.be_java_hisp_w26_g09.exception.BadRequestException;
import com.meli.be_java_hisp_w26_g09.repository.IUserRepository;
import com.meli.be_java_hisp_w26_g09.util.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import com.meli.be_java_hisp_w26_g09.dto.ResponseDTO;
import com.meli.be_java_hisp_w26_g09.entity.Role;
import com.meli.be_java_hisp_w26_g09.exception.NotFoundException;
import com.meli.be_java_hisp_w26_g09.exception.NotContentFollowedException;
import com.meli.be_java_hisp_w26_g09.util.JsonUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.io.IOException;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private IUserRepository userRepository;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    UserServiceImpl userService;

    User user = new User();
    UserDTO userDTO = new UserDTO();

    @BeforeEach
    void setup() throws IOException {
        userDTO = JsonUtil.readJsonFromFile("core/dto/userDTO.json", UserDTO.class);
        user = JsonUtil.readJsonFromFile("core/entity/user.json", User.class);
    }

    @Test
    @DisplayName("Test to follow an user with valid IDs and Roles")
    void testFollowUser_ValidUserIdsAndRoles_Success() {
        Integer userId = 1;
        Integer userIdToFollow = 2;

        User customer = new User(userId, "JohnDoe", new Role(Role.ID_CUSTOMER, "Customer"), new ArrayList<>());
        User seller = new User(userIdToFollow, "JaneSmith", new Role(Role.ID_SELLER, "Seller"), new ArrayList<>());

        when(userRepository.findById(userId)).thenReturn(Optional.of(customer));
        when(userRepository.findById(userIdToFollow)).thenReturn(Optional.of(seller));

        ResponseDTO response = userService.follow(userId, userIdToFollow);

        assertNotNull(response);
        assertEquals("The user with id " + userId + " is follow to " + userIdToFollow, response.getMessage());

        verify(userRepository, atLeastOnce()).addFollowed(customer, seller);
    }

    @Test
    @DisplayName("Test to follow an user with non existent user to follow")
    void testFollowUser_NonexistentUserToFollow_ExceptionThrown() {
        Integer userId = 1;
        Integer userIdToFollow = 2;

        User customer = new User(userId, "JohnDoe", new Role(Role.ID_CUSTOMER, "Customer"), new ArrayList<>());
        when(userRepository.findById(1)).thenReturn(Optional.of(customer));
        when(userRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.follow(userId, userIdToFollow));
    }

    @Test
    @DisplayName("Test to follow an user with the same roles")
    void testFollowUser_SameRoleUsers_ExceptionThrown() {
        Integer userId = 1;
        Integer userIdToFollow = 2;

        User customer = new User(userId, "JohnDoe", new Role(Role.ID_SELLER, "Customer"), new ArrayList<>());
        User seller = new User(userIdToFollow, "JaneSmith", new Role(Role.ID_SELLER, "Customer"), new ArrayList<>());

        when(userRepository.findById(userId)).thenReturn(Optional.of(customer));
        when(userRepository.findById(userIdToFollow)).thenReturn(Optional.of(seller));

        assertThrows(BadRequestException.class, () -> userService.follow(userId, userIdToFollow));
    }

    @Test
    @DisplayName("Test to follow an user who is already in followed list")
    void testFollowUser_AlreadyInFollowedList_ExceptionThrown() {
        Integer userId = 1;
        Integer userIdToFollow = 2;

        User seller = new User(userIdToFollow, "JaneSmith", new Role(Role.ID_SELLER, "Seller"), new ArrayList<>());
        User customer = new User(userId, "JohnDoe", new Role(Role.ID_CUSTOMER, "Customer"), List.of(seller));

        when(userRepository.findById(userId)).thenReturn(Optional.of(customer));
        when(userRepository.findById(userIdToFollow)).thenReturn(Optional.of(seller));

        assertThrows(BadRequestException.class, () -> userService.follow(userId, userIdToFollow));
    }

    @Test
    @DisplayName("get followeds by id customer sorted in ascending order")
    void testGetFollowedsByIdAscendingSuccessful() throws IOException {
        // arrange
        userDTO = JsonUtil.readJsonFromFile("followedorder/ascendingorder.json", UserDTO.class);
        String order = "name_asc";

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(userService.getFollowedById(user.getUserId())).thenReturn(userDTO);

        // act
        UserDTO userDTOResponse = userService.getFollowedByIdOrdered(user.getUserId(), order);

        // assert
        verify(userRepository, atLeastOnce()).findById(user.getUserId());
        assertIterableEquals(userDTO.getFollowed(), userDTOResponse.getFollowed());
    }

    @Test
    @DisplayName("get followeds by id sorted in descending order")
    void testGetFollowedsByIdDescendingSuccessful() throws IOException {
        // arrange
        userDTO = JsonUtil.readJsonFromFile("followedorder/descendingorder.json", UserDTO.class);
        String order = "name_desc";

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(userService.getFollowedById(user.getUserId())).thenReturn(userDTO);

        // act
        UserDTO userDTOResponse = userService.getFollowedByIdOrdered(user.getUserId(), order);

        // assert
        verify(userRepository, atLeastOnce()).findById(user.getUserId());
        assertIterableEquals(userDTO.getFollowed(), userDTOResponse.getFollowed());
    }

    @Test
    @DisplayName("Get Followed by id, when user by id not found")
    void testGetFollowedsById_UserNotFoundException() {
        // arrange
        String order = "name_asc";
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.empty());

        // act and assert
        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> userService.getFollowedByIdOrdered(user.getUserId(), order));

        assertEquals("No information was found about those followed.", notFoundException.getMessage());
    }

    @Test
    @DisplayName("Get Followed by id, when user not is customer")
    void testGetFollowedById_NotContentFollowedException() {
        // arrange
        String order = "name_asc";
        Role role = user.getRole();
        role.setIdRole(Role.ID_SELLER);
        user.setRole(role);
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

        // act and assert
        NotContentFollowedException notContentFollowedException = assertThrows(NotContentFollowedException.class,
                () -> userService.getFollowedByIdOrdered(user.getUserId(), order));

        assertEquals("The seller does not have the option to follow", notContentFollowedException.getMessage());
    }

    @Test
    @DisplayName("Get followeds by id, when is bad request")
    void testGetFollowedById_BadRequestException() {
        // arrange
        String order = "name_ascending";
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(userService.getFollowedById(user.getUserId())).thenReturn(userDTO);

        // act and assert
        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> userService.getFollowedByIdOrdered(user.getUserId(), order));

        assertEquals("Invalid order parameter. Valid values are 'name_asc' or 'name_desc'.",
                badRequestException.getMessage());
    }

    @Test
    @DisplayName("get followers by id customer sorted in ascending order")
    void testGetFollowersByIdAscendingSuccessful() throws IOException {
        // arrange
        user.getRole().setIdRole(Role.ID_SELLER);
        userDTO = JsonUtil.readJsonFromFile("followersorder/ascendingorder.json", UserDTO.class);
        List<User> users = JsonUtil.readJsonFromFileToList("core/entity/usersAll.json", User.class);
        String order = "name_asc";

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(userRepository.findAll()).thenReturn(users);
        when(userService.getFollowersById(user.getUserId())).thenReturn(userDTO);

        // act
        UserDTO userDTOResponse = userService.getFollowersByIdOrdered(user.getUserId(), order);

        // assert
        List<UserDTO> followersUserDTO = userDTO.getFollowers();
        List<UserDTO> followersUserDTOResponse = userDTOResponse.getFollowers();
        assertEquals(followersUserDTO.get(0).getUserId(), followersUserDTOResponse.get(0).getUserId());
        assertEquals(followersUserDTO.get(1).getUserName(), followersUserDTOResponse.get(1).getUserName());
        assertEquals(followersUserDTO.get(2).getUserId(), followersUserDTOResponse.get(2).getUserId());
        assertIterableEquals(followersUserDTO, followersUserDTOResponse);
    }

    @Test
    @DisplayName("get followers by id sorted in descending order")
    void testGetFollowersByIdDescendingSuccessful() throws IOException {
        // arrange
        user.getRole().setIdRole(Role.ID_SELLER);
        userDTO = JsonUtil.readJsonFromFile("followersorder/descendingorder.json", UserDTO.class);
        List<User> users = JsonUtil.readJsonFromFileToList("core/entity/usersAll.json", User.class);
        String order = "name_desc";

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(userRepository.findAll()).thenReturn(users);
        when(userService.getFollowersById(user.getUserId())).thenReturn(userDTO);

        // act
        UserDTO userDTOResponse = userService.getFollowersByIdOrdered(user.getUserId(), order);

        // assert
        List<UserDTO> followersUserDTO = userDTO.getFollowers();
        List<UserDTO> followersUserDTOResponse = userDTOResponse.getFollowers();
        assertEquals(followersUserDTO.get(0).getUserId(), followersUserDTOResponse.get(0).getUserId());
        assertEquals(followersUserDTO.get(1).getUserName(), followersUserDTOResponse.get(1).getUserName());
        assertEquals(followersUserDTO.get(2).getUserId(), followersUserDTOResponse.get(2).getUserId());
        assertIterableEquals(followersUserDTO, followersUserDTOResponse);
    }

    @Test
    @DisplayName("Get Followers by id, when user by id not found")
    void testGetFollowersById_UserNotFoundException() {
        // arrange
        String order = "name_asc";
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.empty());

        // act and assert
        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> userService.getFollowersByIdOrdered(user.getUserId(), order));

        assertEquals("No information was found about those followed.", notFoundException.getMessage());
    }

    @Test
    @DisplayName("Get Followers by id, when user not is seller")
    void testGetFollowersById_NotContentFollowedException() {
        // arrange
        String order = "name_asc";
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

        // act and assert
        NotContentFollowedException notContentFollowedException = assertThrows(NotContentFollowedException.class,
                () -> userService.getFollowersByIdOrdered(user.getUserId(), order));

        assertEquals("The customers don't have an option for followers", notContentFollowedException.getMessage());
    }

    @Test
    @DisplayName("Get followers by id, when is bad request")
    void testGetFollowersById_BadRequestException() {
        // arrange
        String order = "name_ascending";
        user.getRole().setIdRole(Role.ID_SELLER);
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(userService.getFollowersById(user.getUserId())).thenReturn(userDTO);

        // act and assert
        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> userService.getFollowersByIdOrdered(user.getUserId(), order));

        assertEquals("Invalid order parameter. Valid values are 'name_asc' or 'name_desc'.",
                badRequestException.getMessage());
    }

    @Test
    @DisplayName("Test to follow a customer by a seller")
    void testFollowUser_SellerFollowCustomer_ExceptionThrown(){
        Integer userId = 1;
        Integer userIdToFollow = 2;

        User seller = new User(userId, "JaneSmith", new Role(Role.ID_SELLER, "Seller"), new ArrayList<>());
        User customer = new User(userIdToFollow, "JohnDoe", new Role(Role.ID_CUSTOMER, "Customer"), new ArrayList<>());

        when(userRepository.findById(userId)).thenReturn(Optional.of(seller));
        when(userRepository.findById(userIdToFollow)).thenReturn(Optional.of(customer));

        assertThrows(BadRequestException.class, () -> userService.follow(userId, userIdToFollow));
    }

}