package com.meli.be_java_hisp_w26_g09.repository;

import com.meli.be_java_hisp_w26_g09.entity.User;

import java.util.List;

public interface IUserRepository {
    List<User> getAllUsers();
}
