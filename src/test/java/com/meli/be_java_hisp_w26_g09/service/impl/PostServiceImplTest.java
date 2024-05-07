package com.meli.be_java_hisp_w26_g09.service.impl;

import static org.mockito.Mockito.doReturn;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.meli.be_java_hisp_w26_g09.dto.PostForListDTO;
import com.meli.be_java_hisp_w26_g09.dto.ProductFollowedListDTO;



@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    // SUT
    @Spy
    private PostServiceImpl systemUnderTest;

    @Test
    @DisplayName("Test to getFollowedPostLastTwo order by date_asc with 4 post")
    public void getPostOrderByDateAsc() {

        // Arrange
        int idUser = 1;
        String order = "date_asc";
        ProductFollowedListDTO expected = getExpectedPostOrderAsc();
        
        // Act
        doReturn(getFakePostTwoWeeksAgo()).when(systemUnderTest).findFollowedPostsLastTwoWeeks(idUser);
        ProductFollowedListDTO result = systemUnderTest.findFollowedPostsLastTwoWeeksSorted(idUser, order);

        // Asertions

        Assertions.assertEquals(expected.getPosts().size(), result.getPosts().size());
        
        List<PostForListDTO> postsExpected = expected.getPosts();
        List<PostForListDTO> postsResult   = result.getPosts();

        for (int i = 0; i < postsExpected.size(); i++) {
            PostForListDTO postExpected = postsExpected.get(i);
            PostForListDTO postResult = postsResult.get(i);
            Assertions.assertEquals(postExpected.getDate(), postResult.getDate());
        }

    }

    @Test
    @DisplayName("Test to getFollowedPostLastTwo order by date_desc with 4 post")
    public void getPostOrderByDateDesc() {

        // Arrange
        int idUser = 1;
        String order = "date_desc";
        ProductFollowedListDTO expected = getExpectedPostOrderDesc();
        
        // Act
        doReturn(getFakePostTwoWeeksAgo()).when(systemUnderTest).findFollowedPostsLastTwoWeeks(idUser);
        ProductFollowedListDTO result = systemUnderTest.findFollowedPostsLastTwoWeeksSorted(idUser, order);

        // Asertions

        Assertions.assertEquals(expected.getPosts().size(), result.getPosts().size());
        
        List<PostForListDTO> postsExpected = expected.getPosts();
        List<PostForListDTO> postsResult   = result.getPosts();

        for (int i = 0; i < postsExpected.size(); i++) {
            PostForListDTO postExpected = postsExpected.get(i);
            PostForListDTO postResult = postsResult.get(i);
            Assertions.assertEquals(postExpected.getDate(), postResult.getDate());
        }
    }

    @Test
    @DisplayName("Test to getFollowedPostLastTwoWeeaksOrder when self dependency is Empty (post)")
    public void getPostOrderAnyDependencyEmpty() {
        //Arrange
        String order = "date_asc";
        int idUser = 1;


        //Act
        doReturn(getFakePostTwoWeeksAgoEmpty()).when(systemUnderTest).findFollowedPostsLastTwoWeeks(idUser);
        ProductFollowedListDTO result = systemUnderTest.findFollowedPostsLastTwoWeeksSorted(idUser, order);
        //Assertions
        Assertions.assertTrue(result.getPosts().isEmpty());
    }

    public ProductFollowedListDTO getFakePostTwoWeeksAgoEmpty() {
        ProductFollowedListDTO emptyListPost = new ProductFollowedListDTO();
        emptyListPost.setPosts(new ArrayList<>());
        return emptyListPost;
    }
    
    /**
     * Method util
     * @return Return a ProductFollowedListDTO that contain 4 LocalDates unorder
     */
    private ProductFollowedListDTO getFakePostTwoWeeksAgo() {
        ProductFollowedListDTO list = new ProductFollowedListDTO();

        PostForListDTO post1 = new PostForListDTO(null, null, LocalDate.of(2024, 12, 1), null, null, null);
        PostForListDTO post2 = new PostForListDTO(null, null, LocalDate.of(2024, 10, 3), null, null, null);
        PostForListDTO post3 = new PostForListDTO(null, null, LocalDate.of(2024, 11, 2), null, null, null);
        PostForListDTO post4 = new PostForListDTO(null, null, LocalDate.of(2024, 9, 4), null, null, null);

        list.setPosts(List.of(post1, post2, post3, post4));

        return list;
    }

    private ProductFollowedListDTO getExpectedPostOrderAsc() {
        ProductFollowedListDTO list = new ProductFollowedListDTO();

        PostForListDTO post1 = new PostForListDTO(null, null, LocalDate.of(2024, 9, 4), null, null, null);
        PostForListDTO post2 = new PostForListDTO(null, null, LocalDate.of(2024, 10, 3), null, null, null);
        PostForListDTO post3 = new PostForListDTO(null, null, LocalDate.of(2024, 11, 2), null, null, null);
        PostForListDTO post4 = new PostForListDTO(null, null, LocalDate.of(2024, 12, 1), null, null, null);
        list.setPosts(List.of(post1, post2, post3, post4));

        return list;
    }

    private ProductFollowedListDTO getExpectedPostOrderDesc() {
        ProductFollowedListDTO list = new ProductFollowedListDTO();

        PostForListDTO post1 = new PostForListDTO(null, null, LocalDate.of(2024, 12, 1), null, null, null);
        PostForListDTO post2 = new PostForListDTO(null, null, LocalDate.of(2024, 11, 2), null, null, null);
        PostForListDTO post3 = new PostForListDTO(null, null, LocalDate.of(2024, 10, 3), null, null, null);
        PostForListDTO post4 = new PostForListDTO(null, null, LocalDate.of(2024, 9, 4), null, null, null);

        list.setPosts(List.of(post1, post2, post3, post4));

        return list;
    }
}