package com.meli.be_java_hisp_w26_g09.service.impl;

import com.meli.be_java_hisp_w26_g09.dto.PostDTO;
import com.meli.be_java_hisp_w26_g09.dto.ProductDTO;
import com.meli.be_java_hisp_w26_g09.entity.Post;
import com.meli.be_java_hisp_w26_g09.entity.Product;
import com.meli.be_java_hisp_w26_g09.exception.BadRequestException;
import com.meli.be_java_hisp_w26_g09.exception.NotFoundException;

import com.meli.be_java_hisp_w26_g09.repository.IPostRepository;
import com.meli.be_java_hisp_w26_g09.repository.IProductRepository;
import com.meli.be_java_hisp_w26_g09.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    IProductRepository productRepository;
    @Autowired
    IPostRepository postRepository;

    @Override
    public ResponseEntity<?> searchAllProducts() {
        List<ProductDTO> result = new ArrayList<>();
        List<Product> products = productRepository.findAll();
        if(products.size()>0){
            for(Product p: products){
                result.add(new ProductDTO(p.getProductId(),
                        p.getProductName(),
                        p.getType(),
                        p.getBrand(),
                        p.getColor(),
                        p.getNotes()));
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        }else{
            throw new NotFoundException("No existen productos");
        }
    }

    @Override
    public ResponseEntity<?> searchAllPosts() {
        List<PostDTO> result = new ArrayList<>();
        List<Post> posts = postRepository.findAll();
        if(posts.size()>0){
            for(Post p: posts){
                ProductDTO product = new ProductDTO(p.getProduct().getProductId(),
                        p.getProduct().getProductName(),
                        p.getProduct().getType(),
                        p.getProduct().getBrand(),
                        p.getProduct().getColor(),
                        p.getProduct().getNotes());
                result.add(new PostDTO(p.getUserId(),
                        p.getDate(),
                        product,
                        p.getCategory(),
                        p.getPrice(),
                        p.getHas_promo(),
                        p.getDiscount()));
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        }else{
            throw new NotFoundException("No existen posts");
        }
    }

    @Override
    public ResponseEntity<?> addPost(PostDTO post) {
        if(post.getPrice()<0){
            throw new BadRequestException("The price cannot be negative");
        }
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
        Product product = new Product(post.getProduct().getProductId(),
                post.getProduct().getProductName(),
                post.getProduct().getType(),
                post.getProduct().getBrand(),
                post.getProduct().getColor(),
                post.getProduct().getNotes());
        if(!productRepository.isCreated(product)){
            productRepository.createProduct(product);
        }
        Post postEntity = new Post(post.getUserId(),
                post.getDate() ,
                product,
                post.getCategory(),
                post.getPrice(),
                post.getHas_promo(),
                post.getDiscount());
        postRepository.createPost(postEntity);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
