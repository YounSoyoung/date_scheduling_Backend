package com.example.date_scheduling.post.repository;


import com.example.date_scheduling.post.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface CategoryRepository {

    //지역들을 보여주는 기능
    List<Category> findAreaList();

    //지역을 선택했을 때 해당하는 구를 보여주는 기능
    List<Category> findAddressList(String area);

    //카테고리 개별 조회 기능 - 리뷰 저장할 때 사용
    Category findCategory(String address);

    //cID로 카테고리 조회 - Post 상세 페이지에서 사용
    Category findCategoryByCID(String cID);
}
