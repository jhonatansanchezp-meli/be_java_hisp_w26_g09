package com.meli.be_java_hisp_w26_g09.service.impl;

import com.meli.be_java_hisp_w26_g09.dto.PostForListDTO;
import com.meli.be_java_hisp_w26_g09.dto.ProductFollowedListDTO;
import com.meli.be_java_hisp_w26_g09.exception.BadRequestException;
import com.meli.be_java_hisp_w26_g09.util.JsonUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Spy
    private PostServiceImpl postService;


    @Test
    @DisplayName("Test findFollowedPostsLastTwoWeeksSorted with valid order asc")
    void findFollowedPostsLastTwoWeeksSortedWithvalidOrderAsc() throws IOException {
        // Arrange
        int userID = 1;
        String order = "date_asc";
        ProductFollowedListDTO expected = new ProductFollowedListDTO();
        expected.setUserId(userID);
        expected.setPosts(JsonUtil.readJsonFromFileToList("postsordered/allposts.json", PostForListDTO.class));

        // Stub the postService.findFollowedPostsLastTwoWeeks(userID) method call
        doReturn(expected).when(postService).findFollowedPostsLastTwoWeeks(userID);


        // Act
        ProductFollowedListDTO result = postService.findFollowedPostsLastTwoWeeksSorted(userID, order);


        // Assert
        assertEquals(expected.getUserId(), userID);
        assertEquals(expected.getPosts().get(0).getDate(), LocalDate.of(2024, 04, 29));

    }

    @Test
    @DisplayName("Test findFollowedPostsLastTwoWeeksSorted with valid order asc")
    void findFollowedPostsLastTwoWeeksSortedWithvalidOrderDesc() throws IOException {
        // Arrange
        int userID = 1;
        String order = "date_desc";
        ProductFollowedListDTO expected = new ProductFollowedListDTO();
        expected.setUserId(userID);
        expected.setPosts(JsonUtil.readJsonFromFileToList("postsordered/allposts.json", PostForListDTO.class));

        // Stub the postService.findFollowedPostsLastTwoWeeks(userID) method call
        doReturn(expected).when(postService).findFollowedPostsLastTwoWeeks(userID);


        // Act
        ProductFollowedListDTO result = postService.findFollowedPostsLastTwoWeeksSorted(userID, order);


        // Assert
        assertEquals(expected.getUserId(), userID);
        assertEquals(expected.getPosts().get(0).getDate(), LocalDate.of(2024, 04, 30));

    }

    @Test
    @DisplayName("Test findFollowedPostsLastTwoWeeksSorted with invalid order")
    void findFollowedPostsLastTwoWeeksSortedWithInvalidOrder() throws IOException {
        // Arrange
        int userID = 1;
        String order = "invalid_order";
        ProductFollowedListDTO expected = new ProductFollowedListDTO();
        expected.setUserId(userID);
        expected.setPosts(JsonUtil.readJsonFromFileToList("postsordered/allposts.json", PostForListDTO.class));

        // Stub the postService.findFollowedPostsLastTwoWeeks(userID) method call
        doReturn(expected).when(postService).findFollowedPostsLastTwoWeeks(userID);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> postService.findFollowedPostsLastTwoWeeksSorted(userID, order));
    }
}
