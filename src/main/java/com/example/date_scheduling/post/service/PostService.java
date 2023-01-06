package com.example.date_scheduling.post.service;

import com.example.date_scheduling.post.dto.FindAllPostDto;
import com.example.date_scheduling.post.dto.PostDto;
import com.example.date_scheduling.post.dto.RequestPostDto;
import com.example.date_scheduling.post.entity.Category;
import com.example.date_scheduling.post.entity.Post;
import com.example.date_scheduling.post.repository.MyLikeRepository;
import com.example.date_scheduling.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// 역할 : 컨트롤러와 저장소(repository) 사이의 잡일 처리 역할
@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {
    private final PostRepository repository;
    private final MyLikeRepository myLikeRepository;

    private final CategoryService categoryService;
    private final MyLikeService myLikeService;

    //    리뷰 목록 조회 중간처리
    public FindAllPostDto findAllServ() {
        return new FindAllPostDto(repository.findAll());
    }

    // 마이페이지에서 리뷰 목록 조회 중간처리
    public FindAllPostDto findAllMyReviewsServ(String userId){
        return new FindAllPostDto(repository.findAllMyReviews(userId));
    }
    public FindAllPostDto createServ(final Post newPost, String address) { //createServ(final Review newReview, Category category)
        Category category = categoryService.findCategoryServ(address);
        String categoryId = category.getCID();
        newPost.setCID(categoryId);

        if(newPost == null){
            log.warn("newReview cannot be null");
            throw new RuntimeException("newReview cannot be null!");
        }

        boolean flag = repository.save(newPost); //repository.save(newReview, c)

        if(flag) log.info("새로운 할일 [Id: {}]이 저장되었습니다.", newPost.getPostId());

        return flag ? findAllServ() : null; //새로운 리뷰를 삽입한 후 전체 리뷰 목록을 반환
    }

    //////////////카테고리에 해당하는 리뷰 리스트 반환
    public FindAllPostDto searchReviewsServ(String address){
        Category category = categoryService.findCategoryServ(address);
        String categoryId = category.getCID();

        return new FindAllPostDto(repository.findReviews(categoryId));
    }


    ///// 같은 카테고리를 가지고 있는 다른 리뷰들 보여주기
    public FindAllPostDto findOtherReviewsServ(String cID, String postID){
        return new FindAllPostDto(repository.findOtherReviews(cID, postID));
    }

    public FindAllPostDto searchOtherReviewsServ(String address, String postId){
        Category category = categoryService.findCategoryServ(address);
        String categoryId = category.getCID();

        return findOtherReviewsServ(categoryId, postId);
    }


    public RequestPostDto findOneServ(String postId) {
        Post post = repository.findOne(postId);
        log.info("findOneServ return data - {}", post);

        String cID = post.getCID();
        Category category = categoryService.findCategoryByCIDServ(cID);

        RequestPostDto dto = new RequestPostDto();
        dto.setPost(post);
        dto.setCategory(category);

        return dto != null ? dto : null;
    }

    public RequestPostDto findOneMyPostServ(String postId, String username) {
        Post post = repository.findOneMyPost(postId, username);
        log.info("findOneServ return data - {}", post);

        String cID = post.getCID();
        Category category = categoryService.findCategoryByCIDServ(cID);

        RequestPostDto dto = new RequestPostDto();
        dto.setPost(post);
        dto.setCategory(category);

        return dto != null ? dto : null;
    }



    public FindAllPostDto deleteServ(String postId, String userId) {
        boolean flag = repository.remove(postId);

        if (!flag){
            log.warn("delete fail! not found id [{}]", postId);
            throw new RuntimeException("delete fail!");
        }
        return findAllMyReviewsServ(userId);
    }

    public FindAllPostDto update(Post modifyPost, String modifyAddress) {
        Category category = categoryService.findCategoryServ(modifyAddress);
        String modifyCID = category.getCID();

        modifyPost.setCID(modifyCID);

        boolean flag = repository.modify(modifyPost);
        return flag ? findAllMyReviewsServ(modifyPost.getUserId()) : new FindAllPostDto();
    }


    ///////////////////////////////////////////

    // 마이라이크 페이지에서 좋아요한 리뷰 목록 조회 중간처리
    public FindAllPostDto findAllMyLikesServ(String username){
        List<String> postIds = myLikeService.findAllPostIdServ(username);
        List<Post> mylikePosts = new ArrayList<>();

        for(String postId : postIds){
            Post post = repository.findOne(postId);
            mylikePosts.add(post);
        }
        return new FindAllPostDto(mylikePosts);
    }

    //좋아요한 게시글 삭제 처리
    public FindAllPostDto deleteMyLikeServ(String username, String postId){
        boolean flag = myLikeRepository.removeMyLike(username, postId);

        if (!flag){
            log.warn("delete fail! not found id [{}]", postId);
            throw new RuntimeException("delete fail!");
        }
        return findAllMyLikesServ(username);
    }

}
