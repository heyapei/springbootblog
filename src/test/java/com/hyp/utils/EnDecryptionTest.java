package com.hyp.utils;

import com.hyp.pojo.shoes.dto.ShoesCookieDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2019/12/30 12:55
 * @Description: TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class EnDecryptionTest {


    @Test
    public void showOneArticle() {
        EnDecryption enDecryption = new EnDecryption();
        ShoesCookieDTO shoesCookieDTO = new ShoesCookieDTO();
        shoesCookieDTO.setUserId(123);

        String s = enDecryption.enCryptBase64(shoesCookieDTO.toString());
        System.out.println("加密后" + s);

        byte[] bytes = enDecryption.deCryptBase64(s);
        try {
            System.out.println("解密后" + new String(bytes, "UTF-8"));
        } catch (UnsupportedEncodingException e) {

        }

    }

}