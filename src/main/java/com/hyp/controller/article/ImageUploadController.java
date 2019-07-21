package com.hyp.controller.article;


import com.hyp.service.IImgUploadService;
import com.hyp.utils.FileNameUtils;
import com.hyp.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author heyap
 */
@Controller
public class ImageUploadController {

    private final ResourceLoader resourceLoader;
    @Autowired
    private IImgUploadService iImgUploadService;

    @Autowired
    public ImageUploadController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * @param file 要上传的文件
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
    public Map<String, String> upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {

        // /D:/LifeSpace/IdeaWorkSpace/springstart/target/classes/uploadImg
        URL url = this.getClass().getClassLoader().getResource("uploadImg");
        String urlString = url.toString();
        // D:/LifeSpace/IdeaWorkSpace/springstart/target/classes/uploadImg
        String savePath = urlString.substring(urlString.indexOf("/") + 1);
        // 获得文件类型 image/jpeg
        String fileType = file.getContentType();
        // 获得文件后缀名称 jpeg
        String imageName = fileType.substring(fileType.indexOf("/") + 1);
        // 原名称 test.jpg
        String fileName = file.getOriginalFilename();
        //注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：  c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
        //处理获取到的上传文件的文件名的路径部分，只保留文件名部分 test.jpg
        String filename = fileName.substring(fileName.lastIndexOf("\\") + 1);
        //得到文件保存的名称 f1a315ae4b7a4c81b5cf1cb396fbfdd2_test.jpg
        String saveFilename = new FileNameUtils().makeFileName(filename);
        System.out.println(saveFilename + "saveFilename");
        //得到文件的保存目录  D:/LifeSpace/IdeaWorkSpace/springstart/target/classes/uploadImg/12/1
        //不存储的太深了
        String realSavePath = savePath;
        System.out.println(realSavePath + "realSavePath");
        try {
            FileUtils.upload(file.getBytes(), realSavePath, saveFilename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //获得保存到数据库的信息
        String dataSavePath = realSavePath.substring(realSavePath.indexOf("/uploadImg"));
        // 拼接图片url
        String imgPath = "//" + request.getServerName().replace("/", "") + ":" + request.getServerPort() + dataSavePath + "/" + saveFilename;
        System.out.println(imgPath + "imgPath");
        //iImgUploadService.imgUpload(imgPath);
        Map<String, String> map = new HashMap<>(1);
        map.put("url", imgPath);
        return map;
    }

    /**
     * 跳转到文件上传页面
     *
     * @return
     */
    @RequestMapping("test")
    public String toUpload() {
        return "wangEditor/wangEditor";
    }

}
