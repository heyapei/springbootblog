package com.hyp.pojo.shoes.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2019/12/30 12:50
 * @Description: TODO cookie的对象
 */
@Data
public class ShoesCookieDTO {
    private Integer userId;
    /**
     * 时间戳
     */
    private String timeStamp;
}
