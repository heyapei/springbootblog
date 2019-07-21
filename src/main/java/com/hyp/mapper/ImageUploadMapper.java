package com.hyp.mapper;


import com.hyp.pojo.ImageUpload;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageUploadMapper {
    int insert(ImageUpload record);

    int insertSelective(ImageUpload record);

    int insertImgPath(String imgPath);
}