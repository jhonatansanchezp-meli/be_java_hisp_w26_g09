package com.meli.be_java_hisp_w26_g09.service.impl;

import com.meli.be_java_hisp_w26_g09.dto.UserDTO;
import com.meli.be_java_hisp_w26_g09.entity.Role;
import com.meli.be_java_hisp_w26_g09.entity.User;
import com.meli.be_java_hisp_w26_g09.exception.NotContentFollowedException;
import com.meli.be_java_hisp_w26_g09.exception.NotFoundException;
import com.meli.be_java_hisp_w26_g09.util.mapper.UserMapper;
import com.meli.be_java_hisp_w26_g09.repository.IUserRepository;
import com.meli.be_java_hisp_w26_g09.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDTO getFollowedById(Integer id) {

        Optional<User> userFollowed = userRepository.findById(id);

        if (userFollowed.isEmpty())
            throw new NotFoundException("No information was found about those followed.");

        if(userFollowed.get().getRole() != null
                && userFollowed.get().getRole().getIdRole().equals(Role.ID_SELLER))
            throw new NotContentFollowedException("The seller does not have the option to follow");

        return userMapper.userFollowedToUserDTO(userFollowed.get());
    }

    @Override
    public UserDTO getFollowersById(Integer id) {
        Optional<User> userFollowers = userRepository.findById(id);
        if (userFollowers.isEmpty())
            throw new NotFoundException("No information was found about those followed.");

        if(userFollowers.get().getRole() != null && userFollowers.get().getRole().getIdRole().equals(Role.ID_CUSTOMER))
            throw new NotContentFollowedException("The customers don't have an option for followers");

        List<User> users = userRepository.findAll();

        List<User> followers = users.stream()
                .filter(user -> user.getFollowed() != null && user.getFollowed().stream().map(User::getUserId).
                        anyMatch(userId -> userId.equals(userFollowers.get().getUserId()))).toList();

        userFollowers.get().setFollowed(followers);

        return userMapper.userFollowersToUserDTO(userFollowers.get());
    }

}
