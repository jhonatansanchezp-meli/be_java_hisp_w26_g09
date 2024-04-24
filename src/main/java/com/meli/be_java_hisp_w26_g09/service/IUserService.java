package com.meli.be_java_hisp_w26_g09.service;

import com.meli.be_java_hisp_w26_g09.dto.UserDTO;

import java.util.List;

public interface IUserService {

    UserDTO getFollowedById(Integer id);
    UserDTO getFollowersById(Integer id);
    public UserDTO getFollowedByIdOrdered(Integer id, String order);

    List<UserDTO> getAllUsers();

    String unfollowUser(int userId, int userToUnfollow);

}
