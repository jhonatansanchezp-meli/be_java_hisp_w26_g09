package com.meli.be_java_hisp_w26_g09.service.impl;

import com.meli.be_java_hisp_w26_g09.dto.UserDTO;
import com.meli.be_java_hisp_w26_g09.entity.Role;
import com.meli.be_java_hisp_w26_g09.entity.User;
import com.meli.be_java_hisp_w26_g09.exception.NotContentFollowedException;
import com.meli.be_java_hisp_w26_g09.exception.NotFoundException;
import com.meli.be_java_hisp_w26_g09.mapper.UserMapper;
import com.meli.be_java_hisp_w26_g09.repository.IUserRepository;
import com.meli.be_java_hisp_w26_g09.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
