package com.meli.be_java_hisp_w26_g09.service;

import com.meli.be_java_hisp_w26_g09.dto.PostDTO;
import com.meli.be_java_hisp_w26_g09.dto.ResponseDTO;
import com.meli.be_java_hisp_w26_g09.dto.ProductFollowedListDTO;

public interface IProductService {
    public ResponseDTO addPost(PostDTO post);
    public ProductFollowedListDTO findFollowedPostsLastTwoWeeks(int userID);
}
