package com.meli.be_java_hisp_w26_g09.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meli.be_java_hisp_w26_g09.entity.Post;
import com.meli.be_java_hisp_w26_g09.entity.Product;
import com.meli.be_java_hisp_w26_g09.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RepositoryImpl implements IRepository{
    List<User> listOfUser = new ArrayList<>();
    List<Product> listOfProduct = new ArrayList<>();
    List<Post> listOfPost = new ArrayList<>();

    public RepositoryImpl() throws IOException {
        loadDataBase();
    }

    private void loadDataBase() throws IOException {
        File file;
        ObjectMapper objectMapper = new ObjectMapper();
        List<Product> products ;
        List<User> users ;
        List<Post> posts ;

        file= ResourceUtils.getFile("classpath:products_generated.json");
        products= objectMapper.readValue(file,new TypeReference<List<Product>>(){});
        listOfProduct = products;

        file= ResourceUtils.getFile("classpath:users_generated.json");
        users= objectMapper.readValue(file,new TypeReference<List<User>>(){});
        listOfUser = users;

        file= ResourceUtils.getFile("classpath:posts_generated.json");
        posts= objectMapper.readValue(file,new TypeReference<List<Post>>(){});
        listOfPost = posts;
    }

    @Override
    public List<Post> findAllPosts() {
        return listOfPost;
    }
    @Override
    public List<Product> findAllProducts() {
        return listOfProduct;
    }

    @Override
    public List<User> findAllUsers() {
        return listOfUser;
    }
}
