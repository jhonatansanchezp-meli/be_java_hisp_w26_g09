package com.meli.be_java_hisp_w26_g09.service;

import com.meli.be_java_hisp_w26_g09.dto.UserDTO;

public interface IUserService {

    UserDTO getFollowedById(Integer id);
    UserDTO getFollowersById(Integer id);

}
