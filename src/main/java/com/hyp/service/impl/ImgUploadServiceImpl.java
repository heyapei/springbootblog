package com.hyp.service.impl;


import com.hyp.mapper.ImageUploadMapper;
import com.hyp.pojo.ImageUpload;
import com.hyp.service.IImgUploadService;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2019/7/21 15:12
 * @Description: TODO
 */
@Service("iImgUploadService")
@MapperScan("com.autumn.mapper")
public class ImgUploadServiceImpl implements IImgUploadService {

    @Autowired
    private ImageUploadMapper imageUploadMapper;

    private static Logger logger = LoggerFactory.getLogger("ImgUploadServiceImpl.class");


    /**
     * 存储上传图片的路径
     * @param
     * @return
     */
    @Override
    public String imgUpload(String imgPath){

        ImageUpload upload = new ImageUpload();
        upload.setImgpath(imgPath);
        // 一定要加非空判断，否则会报空指针异常
        if(upload.getImgpath() == null){
            return "图片地址为空";
        }
        int rowCount = imageUploadMapper.insertImgPath(upload.getImgpath());
        logger.error(rowCount + "");
        if(rowCount > 0){
            return "图片地址存储成功";
        }
        return "图片地址存储失败";
    }
}
