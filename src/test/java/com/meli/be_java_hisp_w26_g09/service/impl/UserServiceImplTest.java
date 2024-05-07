package com.meli.be_java_hisp_w26_g09.service.impl;

import com.meli.be_java_hisp_w26_g09.dto.RoleDTO;
import com.meli.be_java_hisp_w26_g09.dto.UserDTO;
import com.meli.be_java_hisp_w26_g09.entity.User;
import com.meli.be_java_hisp_w26_g09.exception.BadRequestException;
import com.meli.be_java_hisp_w26_g09.repository.IUserRepository;
import com.meli.be_java_hisp_w26_g09.service.IPostService;
import com.meli.be_java_hisp_w26_g09.service.IUserService;
import com.meli.be_java_hisp_w26_g09.util.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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
    public void setup()
    {
        userDTO = new UserDTO();
        userDTO.setUserId(2);
        userDTO.setUserName("JohnDoe");

        List<UserDTO> followers = new ArrayList<>();
        UserDTO follower1 = new UserDTO();
        follower1.setUserId(2);
        follower1.setUserName("Bob");
        followers.add(follower1);

        UserDTO follower2 = new UserDTO();
        follower2.setUserId(3);
        follower2.setUserName("Margarita");
        followers.add(follower2);

        userDTO.setFollowers(followers);

    }


    @Test
    @DisplayName("Test to get followers in ascending order from getFollowedByIdOrdered class")
    public void getFollowedByIdOrderedTestAsc() {
        // Arrange
        User user = new User();
        user.setUserId(2);
        when(userRepository.findById(2)).thenReturn(Optional.of(user));
        when(userService.getFollowersById(2)).thenReturn(userDTO);

        // Act
        UserDTO result = userService.getFollowersByIdOrdered(2, "name_asc");

        // Assert
        assertEquals("Bob", result.getFollowers().get(0).getUserName());
    }

    @Test
    @DisplayName("Test to get followers in descending order from getFollowedByIdOrdered class")
    public void getFollowedByIdOrderedTestDesc() {
        // Arrange
        User user = new User();
        user.setUserId(2);
        when(userRepository.findById(2)).thenReturn(Optional.of(user));
        when(userService.getFollowersById(2)).thenReturn(userDTO);

        // Act
        UserDTO result = userService.getFollowersByIdOrdered(2, "name_desc");

        // Assert
        assertEquals("Margarita", result.getFollowers().get(0).getUserName());
    }


    @Test
    @DisplayName("Test to get followers throwing error from getFollowedByIdOrdered class")
    public void getFollowedByIdOrderedTestError() {
        // Arrange
        User user = new User();
        user.setUserId(2);
        when(userRepository.findById(2)).thenReturn(Optional.of(user));
        when(userService.getFollowersById(2)).thenReturn(userDTO);


        // Assert
        assertThrows(BadRequestException.class, () -> userService.getFollowersByIdOrdered(2, "name_ascDesc"));
    }




}