package com.example.date_scheduling.post.service;

import com.example.date_scheduling.post.dto.CategoryDto;
import com.example.date_scheduling.post.entity.Category;
import com.example.date_scheduling.post.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.xml.catalog.CatalogException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    //지역 목록 조회 중간처리
    public CategoryDto findAreaServ(){
        return new CategoryDto(categoryRepository.findAreaList());
    }


    //지역에 해당하는 주소 목록 조회 중간처리
    public CategoryDto findAddressServ(String area){
        return new CategoryDto(categoryRepository.findAddressList(area));
    }

    //선택한 주소 보여주기 -> cID까지 들어있는 Category 타입을 받는다. 프론트에서는 json.address
    //입력받은 지역과 주소를 바탕으로 완전한 카테고리 객체를 불러온다(findOne과 유사)
    public Category findCategoryServ(String address){
        Category category = categoryRepository.findCategory(address);
        return category;
    }

    public Category findCategoryByCIDServ(String cID){
        Category category = categoryRepository.findCategoryByCID(cID);
        return category;
    }

}

