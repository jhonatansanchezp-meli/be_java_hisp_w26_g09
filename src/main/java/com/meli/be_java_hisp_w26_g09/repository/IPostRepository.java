package com.meli.be_java_hisp_w26_g09.repository;

import com.meli.be_java_hisp_w26_g09.entity.Post;

import java.util.List;

public interface IPostRepository {
    public List<Post> findAll();
}
