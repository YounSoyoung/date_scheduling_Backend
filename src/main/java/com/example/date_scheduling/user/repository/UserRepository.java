package com.example.date_scheduling.user.repository;

import com.example.date_scheduling.user.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRepository {

    //이메일 주소를 통해 회원정보 조회하기
    UserEntity findUserByEmail(String email);

    //loginId를 통해 회원정보 조회하기
    UserEntity findUserByLoginId(String loginId);

    //loginId가 중복인지 조회하기
    boolean existByLoginId(String loginId);

    //이메일 주소가 중복인지 조회하기
    boolean existByEmail(String email);

    //닉네임이 중복인지 조회하기
    boolean existByUsername(String username);

    //회원 가입하기
    boolean register(UserEntity entity);

    boolean change(UserEntity entity);
}

