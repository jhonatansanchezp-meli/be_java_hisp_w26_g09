package com.meli.be_java_hisp_w26_g09.service.impl;

import com.meli.be_java_hisp_w26_g09.dto.PostDTO;
import com.meli.be_java_hisp_w26_g09.dto.ProductDTO;
import com.meli.be_java_hisp_w26_g09.entity.Post;
import com.meli.be_java_hisp_w26_g09.entity.Product;
import com.meli.be_java_hisp_w26_g09.exception.NotFoundException;
import com.meli.be_java_hisp_w26_g09.repository.IRepository;
import com.meli.be_java_hisp_w26_g09.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    IRepository repository;

    @Override
    public ResponseEntity<?> searchAllProducts() {
        List<ProductDTO> result = new ArrayList<>();
        List<Product> products = repository.findAllProducts();
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
        List<Post> posts = repository.findAllPosts();
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
}
