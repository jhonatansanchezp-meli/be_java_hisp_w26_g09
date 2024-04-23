package com.meli.be_java_hisp_w26_g09.service;

import com.meli.be_java_hisp_w26_g09.dto.PostDTO;
import com.meli.be_java_hisp_w26_g09.dto.ProductDTO;
import com.meli.be_java_hisp_w26_g09.entity.Post;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IProductService {
    public ResponseEntity<?> searchAllProducts();
    public ResponseEntity<?> searchAllPosts();
    public ResponseEntity<?> addPost(PostDTO post);
}
