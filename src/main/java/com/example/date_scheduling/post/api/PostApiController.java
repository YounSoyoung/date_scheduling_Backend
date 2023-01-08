package com.example.date_scheduling.post.api;

import com.example.date_scheduling.error.ErrorDTO;
import com.example.date_scheduling.post.dto.CategoryDto;
import com.example.date_scheduling.post.dto.FindAllPostDto;
import com.example.date_scheduling.post.dto.PostDto;
import com.example.date_scheduling.post.dto.RequestPostDto;
import com.example.date_scheduling.post.entity.Category;
import com.example.date_scheduling.post.entity.Post;
import com.example.date_scheduling.post.service.CategoryService;
import com.example.date_scheduling.post.service.MyLikeService;
import com.example.date_scheduling.post.service.PostService;
import com.example.date_scheduling.util.FileUploadUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j  // 로깅을 위해
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@CrossOrigin    // 다른 서버의 요청 허용
public class PostApiController {

    private final PostService service;
    private final CategoryService categoryService;
    private final MyLikeService myLikeService;

    @Value("${upload.path}")
    private String uploadRootPath;

    //메인페이지에서 보여줄 리뷰 목록과 마이페이지에서 보여줄 리뷰 목록 나누기
    // 게시물 목록 전체 조회 요청
    @GetMapping
    public FindAllPostDto posts(){
        log.info("/api/posts/ GET request!");

        return service.findAllServ();
    }

    /////////////////////////////////////////////////////////////////////////
    // <<리뷰 작성 페이지>>
    //리뷰 작성 페이지를 들어가면 먼저 지역 목록들을 보여준다
    @GetMapping("/new")
    public CategoryDto areas(){
        log.info("/api/posts/new GET category request");

        return categoryService.findAreaServ();
    }

    //지역 하나를 선택하면 해당하는 주소(구)들을 보여준다.
    @GetMapping(value = "/new/{area}", produces = "application/json; charset=UTF-8")
    public CategoryDto addresses(@PathVariable String area){
        log.info("/api/posts/new/{} GET request", area);

        return categoryService.findAddressServ(area);
    }

    //주소를 선택하면 주소와 categoryID값을 가진 Category가 반환된다.
    @GetMapping(value = "/new/{area}/{address}", produces = "application/json; charset=UTF-8")
    public ResponseEntity<?> category(@PathVariable String area, @PathVariable String address){
        log.info("/api/posts/new/{}/{} POST request", area, address);
        if(area == null) return ResponseEntity.badRequest().build();

        Category fullCategory = categoryService.findCategoryServ(address);
        if(fullCategory == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(fullCategory);
    }


    //리뷰 등록 요청
    @PostMapping(value = "/new")
    public ResponseEntity<?> create(@AuthenticationPrincipal String username,
                                    @RequestPart("postInfo") RequestPostDto requestPostDto,
                                    @RequestPart(value = "postImg", required = false) MultipartFile postImg) throws IOException {



        Post newPost = requestPostDto.getPost();
        Category category = requestPostDto.getCategory();

        newPost.setUserId(username);
        log.info("/api/reviews POST request! - {}", newPost);

        try{
            if(postImg!=null){
                log.info("postImg: {}", postImg.getOriginalFilename());

                //1. 서버에 이미지를 저장 - 이미지를 서버에 업로드

                //1-a. 파일 저장 위치를 지정하여 파일 객체에 포장
                String originalFilename = postImg.getOriginalFilename();

                //1-a-1. 파일명이 중복되지 않도록 변경
                String uploadFileName = UUID.randomUUID() + "_" + originalFilename;

                //1-a-2. 업로드 폴더를 날짜별로 생성
                String newUploadPath = FileUploadUtil.makeUploadDirectory(uploadRootPath);

                File uploadFile = new File(newUploadPath + "/" + uploadFileName);

                //1-b. 파일을 해당 경로에 업로드
                postImg.transferTo(uploadFile);


                //2. 데이터베이스에 이미지 정보를 저장 - 누가 어떤 사진을 올렸는가

                //2-a. newUploadPath에서 rootPath를 제거
                //ex) new: D:/upload/2023/01/07
                // root: D:/upload
                // new - root == /2023/01/07
                String savePath = newUploadPath.substring(uploadRootPath.length());

                newPost.setImage(savePath + File.separator + uploadFileName);
            }

            FindAllPostDto dto = service.createServ(newPost, category.getAddress());

            if(dto == null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok().body(dto);
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body((e.getMessage()));
        }

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    // <<메인 페이지에서 카테고리를 통해 리뷰들 검색>>
    //지역 버튼을 클릭하면 지역 리스트를 불러온다(서울, 부산)
    @GetMapping("/search")
    public CategoryDto areaSearch(){
        log.info("/api/posts/search GET category request");

        return categoryService.findAreaServ();
    }

    //지역 하나를 선택하면 해당하는 주소(구)들을 보여준다.
    @GetMapping(value = "/search/{area}", produces = "application/json; charset=UTF-8")
    public CategoryDto addresseSearch(@PathVariable String area){
        log.info("/api/posts/search/{} GET request", area);

        return categoryService.findAddressServ(area);
    }

    //지역이랑 주소를 모두 선택한 다음 검색 버튼을 누르면 입력받은 카테고리에 해당하는 리뷰들을 불러온다
    @PostMapping("/search")
    public FindAllPostDto searchReviews(@RequestBody Category category){
        log.info("/api/posts/search/{} GET request", category);

        if(category.getArea() == null || category.getAddress() == null){
            log.warn("{area} or {address} cannot be null");
            throw new RuntimeException("{area} or {address} cannot be null!");
        }

        return service.searchReviewsServ(category.getAddress());

    }

    @PostMapping("/search/{postId}")
    public FindAllPostDto searchOtherReviews(@RequestBody Category category, @PathVariable String postId){
        log.info("/api/posts/search/{} GET request", category);

        if(category.getArea() == null || category.getAddress() == null){
            log.warn("{area} or {address} cannot be null");
            throw new RuntimeException("{area} or {address} cannot be null!");
        }

        return service.searchOtherReviewsServ(category.getAddress(), postId);

    }
    /////////////////////////////////////////////////////////////////////////////////////////////


    // 게시물 개별 조회 요청
    @GetMapping("/{postId}")
    public ResponseEntity<?> findOne(@PathVariable String postId) {
        log.info("/api/posts/{} GET request!", postId);

        if (postId == null) return ResponseEntity.badRequest().build();

        RequestPostDto postWithCategory = service.findOneServ(postId);

        if (postWithCategory == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(postWithCategory);
    }

    // 마이페이지 > 내가 작성한 리뷰(findAllMyReview(userId))
    // 작성한 리뷰 목록 전체조회
    @GetMapping("/mypost")
    public FindAllPostDto myReviews(@AuthenticationPrincipal String username){
        log.info("/api/posts/mypost GET request!");

        return service.findAllMyReviewsServ(username);
    }

    // 내가 작성한 게시물 개별 조회 요청
    @GetMapping("/mypost/{postId}")
    public ResponseEntity<?> findOneMyPost(@PathVariable String postId, @AuthenticationPrincipal String username) {
        log.info("/api/posts/mypost/{} GET request!", postId);

        if (postId == null) return ResponseEntity.badRequest().build();

        RequestPostDto postWithCategory = service.findOneMyPostServ(postId, username);

        if (postWithCategory == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(postWithCategory);
    }


    // 게시물 삭제 요청
    @DeleteMapping("/mypost/{postId}")
    public ResponseEntity<?> delete (@PathVariable String postId, @AuthenticationPrincipal String username) {
        String userId = service.findOneServ(postId).getPost().getUserId();
        if(!username.equals(userId)) return ResponseEntity.badRequest().body(new ErrorDTO("접근 권한이 없습니다 - username이 일치X"));
        log. info("/api/posts/mypost/{} DELETE request!", postId);

        try {
            FindAllPostDto dtos = service.deleteServ(postId, username);
            return ResponseEntity.ok().body(dtos);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }



    // 게시물 수정 요청
    @PutMapping("/mypost/{postId}")
    public ResponseEntity<?> update(@RequestBody RequestPostDto requestPostDto, @PathVariable String postId, @AuthenticationPrincipal String username) {
//        post.setUserId(username);

        String nickByPostId = service.findOneMyPostServ(postId, username).getPost().getUserId();
        if(!nickByPostId.equalsIgnoreCase(requestPostDto.getPost().getUserId())) return ResponseEntity.badRequest().body(new ErrorDTO("접근권한이 없습니다-username 불일치"));

        Post modifyPost = requestPostDto.getPost();
        Category modifyCategory = requestPostDto.getCategory();

        log.info("/api/posts PUT request! Post-{} / Category-{}", modifyPost, modifyCategory);

        try {

            FindAllPostDto dtos = service.update(modifyPost, modifyCategory.getAddress());
            return ResponseEntity.ok().body(dtos);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /////////////////////////////////////////////////
    // 게시글 좋아요 기능(추가)
    @PostMapping("/mylike/{postId}")
    public ResponseEntity<?> addLike(@PathVariable String postId, @AuthenticationPrincipal String username){

        boolean flag = myLikeService.addLikeServ(postId, username);
        log.info("{} 좋아요 추가 - {}", postId, username);
        return ResponseEntity.ok().body(flag);
    }

    //좋아요한 게시글들 조회
    @GetMapping("/mylike")
    public FindAllPostDto myLikes(@AuthenticationPrincipal String username){
        log.info("/api/posts/mylike GET request!");

        return service.findAllMyLikesServ(username);
    }

    //좋아요 여부 조회
    @GetMapping("/mylike/{postId}")
    public ResponseEntity<?> checkLike(@PathVariable String postId, @AuthenticationPrincipal String username){
        log.info("/api/posts/mylike/{} GET request to check Like!", postId);

        boolean flag = myLikeService.checkLikeServ(postId, username);
        return ResponseEntity.ok().body(flag);

    }

    //좋아요한 게시글 삭제
    @DeleteMapping("/mylike/{postId}")
    public ResponseEntity<?> deleteMyLike(@AuthenticationPrincipal String username, @PathVariable String postId){
        log.info("/api/posts/mylike/{} DELETE request", postId);

        try {
            FindAllPostDto dtos = service.deleteMyLikeServ(username, postId);
            return ResponseEntity.ok().body(dtos);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }



    //클라이언트가 사진을 요청할 시 게시글 사진을 전달해주는 요청처리
    @GetMapping("/load-postimg/{postId}")
    public ResponseEntity<?> loadPostImg(@PathVariable String postId) throws IOException{
        log.info("/api/posts/load-postimg/{} GET", postId);

        //해당 게시글의 아이디를 통해 사진의 경로를 DB에서 조회
        //ex) /2023/01/07/skjeijefjie_파일명.확장자
        String postImgPath = service.getPostImgPath(postId);

        //ex) E:/upload/2023/01/07/~~~
        String fullPath = uploadRootPath + File.separator + postImgPath;

        //해당 경로를 파일 객체로 포장
        File targetFile = new File(fullPath);

        //혹시 해당 파일이 존재하지 않으면 예외가 발생 (FileNotFoundException)
        if(!targetFile.exists()) return ResponseEntity.notFound().build();

        // 파일 데이터를 바이트배열로 포장(blob 데이터)
        byte[] rawImageData = FileCopyUtils.copyToByteArray(targetFile); //예외를 메서드 시그니처에 추가

        //응답 헤더 정보 추가
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(FileUploadUtil.getMediaType(postImgPath));

        //클라이언트에 순수 이미지파일 데이터 리턴
        return ResponseEntity.ok().headers(headers).body(rawImageData);

    }
}
