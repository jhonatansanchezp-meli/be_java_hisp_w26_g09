package com.meli.be_java_hisp_w26_g09.service.impl;

import com.meli.be_java_hisp_w26_g09.dto.PostDTO;
import com.meli.be_java_hisp_w26_g09.dto.ProductDTO;
import com.meli.be_java_hisp_w26_g09.dto.ResponseDTO;
import com.meli.be_java_hisp_w26_g09.entity.Post;
import com.meli.be_java_hisp_w26_g09.entity.Product;
import com.meli.be_java_hisp_w26_g09.exception.BadRequestException;
import com.meli.be_java_hisp_w26_g09.exception.NotFoundException;
import com.meli.be_java_hisp_w26_g09.repository.IPostRepository;
import com.meli.be_java_hisp_w26_g09.repository.IProductRepository;
import com.meli.be_java_hisp_w26_g09.repository.IUserRepository;
import com.meli.be_java_hisp_w26_g09.service.IProductService;
import com.meli.be_java_hisp_w26_g09.util.mapper.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Stream;

@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    IProductRepository productRepository;
    @Autowired
    IPostRepository postRepository;
    @Autowired
    IUserRepository userRepository;
    @Autowired
    PostMapper postMapper;

    @Override
    public ResponseDTO addPost(PostDTO post) {
        if(post.getPrice()<0){
            throw new BadRequestException("The price cannot be negative");
        }
        //The discount must be between 0 and 1
        if(post.getDiscount()>1 || post.getDiscount()<0){
            throw new BadRequestException("The discount is not valid");
        }
        if(Stream.of(post.getUserId(),
                post.getDate() ,
                post.getProduct(),
                post.getCategory(),
                post.getPrice(),
                post.getHas_promo(),
                post.getDiscount()).anyMatch(Objects::isNull)){
            throw new BadRequestException("No field can be null");
        }
        if(userRepository.findById(post.getUserId()).isEmpty()){
            throw new BadRequestException("The user_id does not exist ");
        }
        Post postEntity = postMapper.postDTOtoPost(post);
        if(!productRepository.isCreated(postEntity.getProduct())){
            productRepository.createProduct(postEntity.getProduct());
        }
        postRepository.createPost(postEntity);
        return new ResponseDTO("Post has been created");
    }
}
