package com.meli.be_java_hisp_w26_g09.service;

import com.meli.be_java_hisp_w26_g09.dto.UserDTO;

import java.util.List;

public interface IUserService {

    List<UserDTO> getAllUsers();
    String unfollowUser(int userId, int userIdToUnfollow);
}
