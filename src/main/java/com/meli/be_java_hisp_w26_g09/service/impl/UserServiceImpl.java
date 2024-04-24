package com.meli.be_java_hisp_w26_g09.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meli.be_java_hisp_w26_g09.dto.UserDTO;
import com.meli.be_java_hisp_w26_g09.entity.User;
import com.meli.be_java_hisp_w26_g09.exception.NotFoundException;
import com.meli.be_java_hisp_w26_g09.repository.impl.UserRepositoryImpl;
import com.meli.be_java_hisp_w26_g09.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepositoryImpl userRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.getAllUsers().stream().map(this::convertUserToUserDTO).toList();
    }

    @Override
    public String unfollowUser(int userId, int userIdToUnfollow) {
        Optional<User> userToUnfollowOptional = userRepository.getAllUsers().stream()
                .filter(user -> user.getUserId().equals(userIdToUnfollow)).findFirst();

        Optional<User> userWhoUnfollowedOptional = userRepository.getAllUsers().stream()
                .filter(user -> user.getUserId().equals(userId)).findFirst();

        if(userToUnfollowOptional.isPresent()){
            if(userWhoUnfollowedOptional.isPresent()){
                User userWhoUnfollowed = userWhoUnfollowedOptional.get();
                userWhoUnfollowed.getFollowed().remove(userToUnfollowOptional.get());
                return "User " + userWhoUnfollowed.getUserName() + " unfollowed";
            }else{
                throw new NotFoundException("User not found");
            }
        }else{
            throw new NotFoundException("User not found");
        }
    }

    private UserDTO convertUserToUserDTO(User user){
        return objectMapper.convertValue(user, UserDTO.class);
    }

    private User convertUserDTOToUser(UserDTO userDTO){
        return objectMapper.convertValue(userDTO, User.class);
    }
}
