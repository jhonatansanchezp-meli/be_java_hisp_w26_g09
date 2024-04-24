package com.meli.be_java_hisp_w26_g09.util.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meli.be_java_hisp_w26_g09.dto.User2DTO;
import com.meli.be_java_hisp_w26_g09.dto.UserDTO;
import com.meli.be_java_hisp_w26_g09.dto.UserResponseDTO;
import com.meli.be_java_hisp_w26_g09.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {

    public UserDTO userToUserDTO(User user) {

        if (user == null || user.getUserId() == null)
            return new UserDTO();

        UserDTO userDTO = new UserDTO();
        userDTO.setUser_id(user.getUserId());
        userDTO.setUser_name(user.getUserName());
        userDTO.setFollowed(
                (user.getFollowed() != null
                        && user.getFollowed().isEmpty()) ? null :
                        this.userListToUserDTOList(user.getFollowed()));
        return userDTO;
    }


    public User2DTO userToUserDTO2(User user) {

        if (user == null || user.getUserId() == null)
            return new User2DTO();

        User2DTO userDTO = new User2DTO();
        userDTO.setUser_id(user.getUserId());
        userDTO.setUser_name(user.getUserName());
        userDTO.setFollowers(
                (user.getFollowed() != null
                        && user.getFollowed().isEmpty()) ? null :
                        user.getFollowed().stream().map(users -> new UserResponseDTO(users.getUserId(), userDTO.getUser_name())).toList());
        return userDTO;
    }


    public User userDTOToUser(UserDTO userDTO) {

        if (userDTO == null || userDTO.getUser_id() == null)
            return new User();

        User user = new User();
        user.setUserId(userDTO.getUser_id());
        user.setUserName(userDTO.getUser_name());
        user.setFollowed(user.getFollowed() != null
                && (userDTO.getFollowed().isEmpty()) ? null :
                        userDTOListToUserList(userDTO.getFollowed()));

        return user;
    }

    public List<User> userDTOListToUserList(List<UserDTO> userDTOList) {
        if (userDTOList == null || userDTOList.isEmpty())
            return new ArrayList<>();

        return userDTOList.stream()
                .map(this::userDTOToUser)
                .toList();
    }

    public List<UserDTO> userListToUserDTOList(List<User> userList) {
        if (userList == null || userList.isEmpty())
            return new ArrayList<>();

        return userList.stream()
                .map(this::userToUserDTO)
                .toList();
    }

    public UserDTO userFollowedToUserDTO(User user) {
        if (user == null || user.getUserId() == null)
            return new UserDTO();

        UserDTO userDTO = userToUserDTO(user);
        if (userDTO.getFollowed() != null)
            userDTO.getFollowed().forEach(userDTO1 -> userDTO1.setRole(null));

        return userDTO;
    }

    public User2DTO userFollowersToUserDTO2(User user) {
        if (user == null || user.getUserId() == null)
            return new User2DTO();

        return userToUserDTO2(user);
    }



}
