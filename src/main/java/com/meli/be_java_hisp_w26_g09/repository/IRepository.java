package com.meli.be_java_hisp_w26_g09.repository;

import com.meli.be_java_hisp_w26_g09.entity.Post;
import com.meli.be_java_hisp_w26_g09.entity.Product;
import com.meli.be_java_hisp_w26_g09.entity.User;

import java.util.List;

public interface IRepository {
    public List<Post> findAllPosts();
    public List<Product> findAllProducts();
    public List<User> findAllUsers();
}
