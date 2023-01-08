package com.example.date_scheduling.user.entity;

import com.example.date_scheduling.user.dto.UserRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Setter @Getter @ToString
@AllArgsConstructor
public class UserEntity {
    private String uId; //회원 아이디 식별하는 아이디 컴퓨터가 알아서 정해주는 무작위 String
    private String username; //회원의 닉네임 , post 테이블의 userId

    private String loginId; //회원가입시 입력하는 id
    private String email;
    private String password;

    private String profileImg;
    public UserEntity(){
        this.uId = UUID.randomUUID().toString();
    }

    public UserEntity(UserRequestDTO dto){
        this();
        this.username = dto.getUsername();
        this.loginId = dto.getLoginId();
        this.email = dto.getEmail();
        this.password = dto.getPassword();
    }
}
