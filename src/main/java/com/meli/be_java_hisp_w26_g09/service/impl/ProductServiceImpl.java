package com.meli.be_java_hisp_w26_g09.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meli.be_java_hisp_w26_g09.dto.*;
import com.meli.be_java_hisp_w26_g09.entity.Post;
import com.meli.be_java_hisp_w26_g09.entity.User;
import com.meli.be_java_hisp_w26_g09.exception.BadRequestException;
import com.meli.be_java_hisp_w26_g09.exception.NotFoundException;
import com.meli.be_java_hisp_w26_g09.repository.IPostRepository;
import com.meli.be_java_hisp_w26_g09.repository.IProductRepository;
import com.meli.be_java_hisp_w26_g09.repository.IUserRepository;
import com.meli.be_java_hisp_w26_g09.service.IProductService;
import com.meli.be_java_hisp_w26_g09.util.mapper.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
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
        if (post.getPrice() < 0)
            throw new BadRequestException("The price cannot be negative");

        post.setHasPromo(false);
        if (post.getDiscount() != null && post.getDiscount() != 0.0)
            throw new BadRequestException("Cannot add a promo post on this end point");

        post.setDiscount(0.0);
        if (Stream.of(post.getUserId(),
                post.getDate(),
                post.getProduct(),
                post.getCategory(),
                post.getPrice()).anyMatch(Objects::isNull)) {
            throw new BadRequestException("No field can be null");
        }
        if (userRepository.findById(post.getUserId()).isEmpty())
            throw new BadRequestException("The user_id does not exist ");

        Post postEntity = postMapper.postDTOtoPost(post);
        if (!productRepository.isCreated(postEntity.getProduct()))
            productRepository.createProduct(postEntity.getProduct());

        postRepository.createPost(postEntity);
        return new ResponseDTO("Post has been created");
    }

    @Override
    public ProductFollowedListDTO findFollowedPostsLastTwoWeeks(int userID) {
        ObjectMapper mapper = new ObjectMapper();
        Optional<User> user = userRepository.findById(userID);
        if (user.isEmpty()) throw new NotFoundException("The user was not found");
        List<User> followed = user.get().getFollowed();
        if (followed == null) throw new NotFoundException("The user don't follow no seller");


        Calendar twoWeeksAgo = Calendar.getInstance();
        twoWeeksAgo.add(Calendar.WEEK_OF_YEAR, -2);


        List<Post> posts = postRepository.findAll();
        List<Post> followedPosts = new ArrayList<>();
        followed.forEach(seller -> followedPosts.addAll(posts.stream()
                .filter(post -> post.getUserId().equals(seller.getUserId()))
                .toList()));

        List<Post> followedPostsLastTwoWeeks = followedPosts.stream()
                .filter(post -> post.getDate().after(twoWeeksAgo.getTime())).toList()
                .stream().sorted(Comparator.comparing(Post::getDate).reversed()).toList();


        List<PostForListDTO> postForListDTOS = new ArrayList<>();
        followedPostsLastTwoWeeks.forEach(post -> postForListDTOS.add(new PostForListDTO(post.getUserId(),
                post.getId(),
                post.getDate(),
                mapper.convertValue(post.getProduct(), ProductDTO.class), post.getCategory(), post.getPrice())));
        ProductFollowedListDTO productFollowedListDTO = new ProductFollowedListDTO();

        productFollowedListDTO.setUserId(user.get().getUserId());
        productFollowedListDTO.setPosts(postForListDTOS);


        return productFollowedListDTO;
    }
    @Override
    public ProductFollowedListDTO findFollowedPostsLastTwoWeeksSorted(int userID, String order){
        ProductFollowedListDTO productFollowedListDTOSorted = findFollowedPostsLastTwoWeeks(userID);

        if (!("date_asc".equalsIgnoreCase(order) || "date_desc".equalsIgnoreCase(order)))
            throw new BadRequestException("Invalid order parameter. Valid values are 'date_asc' or 'date_desc'.");

        if ("date_asc".equalsIgnoreCase(order)) {
             productFollowedListDTOSorted.setPosts(productFollowedListDTOSorted.getPosts()
                    .stream()
                    .sorted(Comparator.comparing(PostForListDTO::getDate))
                    .collect(Collectors.toList()));
        }
        return productFollowedListDTOSorted;
    }
}
