package com.meli.be_java_hisp_w26_g09.repository.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meli.be_java_hisp_w26_g09.entity.Post;
import com.meli.be_java_hisp_w26_g09.repository.IPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PostRepositoryImpl implements IPostRepository {
    List<Post> listOfPost = new ArrayList<>();

    public PostRepositoryImpl() throws IOException {
        loadDataBase();
    }

    private void loadDataBase() throws IOException {
        File file;
        ObjectMapper objectMapper = new ObjectMapper();
        List<Post> posts ;

        file= ResourceUtils.getFile("classpath:posts_generated.json");
        posts= objectMapper.readValue(file,new TypeReference<List<Post>>(){});
        listOfPost = posts;
    }

    @Override
    public List<Post> findAll() {
        return listOfPost;
    }

    @Override
    public void createPost(Post post) {
        listOfPost.add(post);
    }
}
