package com.meli.be_java_hisp_w26_g09.service.impl;

import com.meli.be_java_hisp_w26_g09.dto.ResponseDTO;
import com.meli.be_java_hisp_w26_g09.dto.UserDTO;
import com.meli.be_java_hisp_w26_g09.entity.Role;
import com.meli.be_java_hisp_w26_g09.entity.User;
import com.meli.be_java_hisp_w26_g09.exception.BadRequestException;
import com.meli.be_java_hisp_w26_g09.exception.NotContentFollowedException;
import com.meli.be_java_hisp_w26_g09.exception.NotFoundException;
import com.meli.be_java_hisp_w26_g09.util.mapper.UserMapper;
import com.meli.be_java_hisp_w26_g09.repository.IUserRepository;
import com.meli.be_java_hisp_w26_g09.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public UserDTO getFollowedCount(Integer id) {
        UserDTO user = getFollowersById(id);
        user.setFollowers_count(user.getFollowers().size());
        user.setFollowers(null);
        return user;
    }
    
    public UserDTO getFollowedByIdOrdered(Integer id, String order) {
        UserDTO userDTO = getFollowedById(id);

        if (!("name_asc".equalsIgnoreCase(order) || "name_desc".equalsIgnoreCase(order)) || order == null) {
            throw new BadRequestException("Invalid order parameter. Valid values are 'name_asc' or 'name_desc'.");
        }


        if ("name_asc".equalsIgnoreCase(order)) {
            userDTO.setFollowed(userDTO.getFollowed()
                    .stream()
                    .sorted(Comparator.comparing(UserDTO::getUser_name))
                    .collect(Collectors.toList()));
        } else if ("name_desc".equalsIgnoreCase(order)) {
            userDTO.setFollowed(userDTO.getFollowed()
                    .stream()
                    .sorted(Comparator.comparing(UserDTO::getUser_name).reversed())
                    .collect(Collectors.toList()));
        }

        return userDTO;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> userMapper.userToUserDTO(user))
                .toList();
    }

    @Override
    public ResponseDTO unfollowUser(int userId, int userIdToUnfollow) {
        Optional<User> userWhoUnfollowOptional = userRepository.findById(userId);

        if(userWhoUnfollowOptional.isEmpty()) throw new NotFoundException("User not found");

        User userWhoUnfollow = userWhoUnfollowOptional.get();
        List<User> followedUsers = userWhoUnfollow.getFollowed();

        Optional<User> userToUnfollowOptional = followedUsers.stream()
                .filter(user -> user.getUserId() == userIdToUnfollow)
                .findFirst();

        if(userToUnfollowOptional.isEmpty()) throw new NotFoundException("User not found in followers list");

        User userToUnfollow = userToUnfollowOptional.get();
        userRepository.unfollowUser(userWhoUnfollow, userToUnfollow);
        return new ResponseDTO("Unfollow successfull");
    }
    
    public ResponseDTO follow(Integer userId, Integer userIdToFollow) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (!userOptional.isPresent())
            throw new NotFoundException("The user with id " + userId + " was not found.");
        
        User customer = userOptional.get();

        userOptional = userRepository.findById(userIdToFollow);

        if (!userOptional.isPresent())
            throw new NotFoundException("The user with id " + userIdToFollow + " was not found.");

        User seller = userOptional.get();
        
        customer.getRole().getIdRole().equals(Role.ID_CUSTOMER);

        if (!customer.getRole().getIdRole().equals(Role.ID_CUSTOMER) || !seller.getRole().getIdRole().equals(Role.ID_SELLER))
            throw new BadRequestException("Some submitted user does not comply with the role restrictions.");
        
        for (User userIterarion : customer.getFollowed()) {
            if (userIterarion.getUserId().equals(userIdToFollow))
                throw new BadRequestException("The user already follow to this customer.");
        }

        userRepository.addFollowed(customer, seller);

        ResponseDTO followDTO = new ResponseDTO("The user with id " + userId + " is follow  to " + userIdToFollow);
        return followDTO;
    }
}
