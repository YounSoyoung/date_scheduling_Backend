package com.example.date_scheduling.user.api;


import com.example.date_scheduling.error.ErrorDTO;
import com.example.date_scheduling.security.TokenProvider;
import com.example.date_scheduling.user.dto.UserRequestDTO;
import com.example.date_scheduling.user.dto.UserResponseDTO;
import com.example.date_scheduling.user.entity.UserEntity;
import com.example.date_scheduling.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
@CrossOrigin
public class UserController {

    private final UserService userService;
    private final TokenProvider provider;

    //회원가입하기
    @PostMapping("/join")
    public ResponseEntity<?> register(@RequestBody UserRequestDTO dto){
        try {
            //userRequestDTO를 서비스에 전송하기 위해서 먼저 userEntity로 변환해줘야한다.

            UserEntity userEntity = new UserEntity(dto);
            UserEntity user = userService.createServ(userEntity);
            return ResponseEntity.ok().body(user);
        }catch(RuntimeException e){
            return ResponseEntity.badRequest().body(new ErrorDTO(e.getMessage()));
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

        try {
            UserEntity userEntity = userService.changeServ(entity);
            return ResponseEntity.ok().body(userEntity);
        }catch(RuntimeException e){
            return ResponseEntity.badRequest().body(new ErrorDTO(e.getMessage()));
        }
    }


}

