package com.example.date_scheduling.user.api;


import com.example.date_scheduling.error.ErrorDTO;
import com.example.date_scheduling.security.TokenProvider;
import com.example.date_scheduling.user.dto.UserRequestDTO;
import com.example.date_scheduling.user.dto.UserResponseDTO;
import com.example.date_scheduling.user.entity.UserEntity;
import com.example.date_scheduling.user.service.UserService;
import com.example.date_scheduling.util.FileUploadUtil;
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
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
@CrossOrigin
public class UserController {

    private final UserService userService;
    private final TokenProvider provider;

    @Value("${profile.path}")
    private String uploadRootPath;

    //회원가입하기
    @PostMapping("/join")
    public ResponseEntity<?> register(@RequestPart("userInfo") UserRequestDTO dto,
                                      @RequestPart(value = "profileImg", required = false)MultipartFile profileImg){
        try {
            //userRequestDTO를 서비스에 전송하기 위해서 먼저 userEntity로 변환해줘야한다.

            UserEntity userEntity = new UserEntity(dto);

            if(profileImg != null){
                log.info("profileImg : {}",profileImg.getOriginalFilename());

                //1.서버에 이미지파일을 저장, 이미지를 서버에 업로드
                //1-a.파일 저장 위치를 지정하여 파일 객체에 포장
                String originalFilename = profileImg.getOriginalFilename();
                //1-a-1.파일명이 중복되지 않도록 변경
                String uploadFileName = UUID.randomUUID() + "_" + originalFilename;
                //1-a-2.압럳, 폴더를 날짜별로 생성
                String newUploadPath = FileUploadUtil.makeUploadDirectory(uploadRootPath);
                File uploadFile = new File(newUploadPath + File.separator + uploadFileName);
                //1-b. 파일을 해당 경로에 업로드
                profileImg.transferTo(uploadFile);
                // 2. 데이터베이스에 이미지 정보를 저장 - 누가 어떤사진을 올렸는가

                // 2-a. newUploadPath에서 rootPath를 제거
                //  ex) new: E:/profile_upload/2023/01/07
                //      root: E:/profile_upload
                //      new - root == /2023/01/07

                // str: hello java
                // str.substring(6) => 6번부터 끝까지 추출 == java
                String savePath
                        = newUploadPath.substring(uploadRootPath.length());

                userEntity.setProfileImg(savePath + File.separator + uploadFileName);

            }



            UserEntity user = userService.createServ(userEntity);
            return ResponseEntity.ok().body(user);
        }catch(RuntimeException e){
            return ResponseEntity.badRequest().body(new ErrorDTO(e.getMessage()));
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //로그인하기
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequestDTO dto){
        log.info("/auth/login POST - login info : {}",dto);
        try{
            UserEntity user = userService.validateLogin(dto.getLoginId(), dto.getPassword());
            //토큰 발행하기
            final String token = provider.create(user);
            UserResponseDTO userResponseDTO = new UserResponseDTO(user);
            userResponseDTO.setToken(token);

            return ResponseEntity.ok().body(userResponseDTO);
        }catch(RuntimeException e){
            return ResponseEntity.badRequest().body(new ErrorDTO(e.getMessage()));
        }
    }

    //이메일 중복처리
    @GetMapping("/checkemail")
    public ResponseEntity<?> checkEmail(String email){
        boolean flag1 = userService.emailDuplicate(email);

        log.info("{} 중복여부 - {}",email, flag1);
        return ResponseEntity.ok().body(flag1);
    }

    //아이디 중복처리
    @GetMapping("/checkid") //  /checkid?loginId=aaa
    public ResponseEntity<?> checkUserid(String loginId){
        boolean flag2 = userService.loginIdDuplicate(loginId);
        log.info("{} 중복여부 - {}",loginId, flag2);
        return ResponseEntity.ok().body(flag2);
    }

    //닉네임 중복처리
    @GetMapping("/checkusername")
    public ResponseEntity<?> checkUsername(String username){
        boolean flag3 = userService.usernameDuplicate(username);
        log.info("{} 중복여부 - {}", username, flag3);
        return ResponseEntity.ok().body(flag3);
    }


    @PutMapping("/put")
    public ResponseEntity<?> change(@RequestBody UserEntity entity, @AuthenticationPrincipal String username){
        log.info("유저 정보 수정 요청! - {}", entity);
        try {
            UserEntity userEntity = userService.changeServ(entity);
            return ResponseEntity.ok().body(userEntity);
        }catch(RuntimeException e){
            return ResponseEntity.badRequest().body(new ErrorDTO(e.getMessage()));
        }
    }

    //클라이언트가 프로필사진을 요청할 시 프로필사진을 전달해주는 요청처리
    @GetMapping("/load-profile")
    public ResponseEntity<?> loadProfile(@AuthenticationPrincipal String username) throws IOException {
        log.info("/auth/load-profile GET - username",username);

        ///해당 유저의 닉네임을 통해서 프로필 사진의 경로를 DB에서 조회
        //ex) /2023/01/07/ㄺㅎㄹ.파일명.확장자
        String profilePath = userService.getProfilePath(username);

        //ex) C:/profile_upload/2023/...
        String fullPath = uploadRootPath + File.separator + profilePath;

        //해당 경로를 파일 객체로 포장
        File targetFile = new File(fullPath);

        //혹시 해당 파일이 존재하지 않으면 예외가 발생(FileNotFoundException)
        if(!targetFile.exists()) return ResponseEntity.notFound().build();

        //파일 데이터를 바이트배열로 포장 (blob 데이터)
        byte[] rawImageData = FileCopyUtils.copyToByteArray(targetFile);

        //응답 헤더 정보 추가
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(FileUploadUtil.getMediaType(profilePath));

        return ResponseEntity.ok().headers(headers).body(rawImageData);
    }

    //클라이언트가 프로필사진을 요청할 시 프로필사진을 전달해주는 요청처리
    @GetMapping(value = "/load-profile/{userid}")
    public ResponseEntity<?> loadCommentProfile(@PathVariable String userid) throws IOException {
        log.info("/auth/load-profile/{} GET - 댓글 이미지",userid);

        ///해당 유저의 닉네임을 통해서 프로필 사진의 경로를 DB에서 조회
        //ex) /2023/01/07/ㄺㅎㄹ.파일명.확장자
        String profilePath = userService.getCommentProfilePath(userid);


        //ex) C:/profile_upload/2023/...
        String fullPath = uploadRootPath + File.separator + profilePath;

        //해당 경로를 파일 객체로 포장
        File targetFile = new File(fullPath);

        //혹시 해당 파일이 존재하지 않으면 예외가 발생(FileNotFoundException)
        if(!targetFile.exists()) return ResponseEntity.notFound().build();

        //파일 데이터를 바이트배열로 포장 (blob 데이터)
        byte[] rawImageData = FileCopyUtils.copyToByteArray(targetFile);

        //응답 헤더 정보 추가
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(FileUploadUtil.getMediaType(profilePath));

        return ResponseEntity.ok().headers(headers).body(rawImageData);
    }

}
